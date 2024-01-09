package hexlet.code.mapper;

import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        uses = { JsonNullableMapper.class, ReferenceMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {
    @Autowired
    TaskStatusRepository taskStatusRepository;
    @Autowired
    LabelRepository labelRepository;
    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "taskStatus", source = "status", qualifiedByName = "slugToTaskStatus")
    @Mapping(target = "assignee", source = "assigneeId")
    @Mapping(target = "labels", source = "taskLabelIds", qualifiedByName = "labelIdsToLabels")
    public abstract Task map(TaskCreateDTO dto);

    @Mapping(source = "name", target = "title")
    @Mapping(source = "description", target = "content")
    @Mapping(source = "assignee.id", target = "assigneeId")
    @Mapping(source = "taskStatus.slug", target = "status")
    @Mapping(source = "labels", target = "taskLabelIds", qualifiedByName = "labelsToLabelIds")
    public abstract TaskDTO map(Task model);

    public abstract List<TaskDTO> map(List<Task> models);

    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "taskStatus", source = "status", qualifiedByName = "slugToTaskStatus")
    @Mapping(target = "assignee", source = "assigneeId")
    @Mapping(target = "labels", source = "taskLabelIds", qualifiedByName = "labelIdsToLabels")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task model);
    @Named("slugToTaskStatus")
    public TaskStatus slugToTaskStatus(String slug) {
        return taskStatusRepository.findBySlug(slug).orElseThrow(
                () -> new IllegalArgumentException("Invalid TaskStatus slug: " + slug)
        );
    }
    @Named("labelsToLabelIds")
    public Set<Long> labelsToLabelIds(Set<Label> labels) {
        return labels.isEmpty() ? new HashSet<>() : labels.stream()
                .map(Label::getId)
                .collect(Collectors.toSet());
    }
    @Named("labelIdsToLabels")
    public Set<Label> labelIdsToLabels(Set<Long> labelIds) {
        if (labelIds == null) {
            return new HashSet<>();
        }
        return labelIds.isEmpty() ? new HashSet<>() : labelIds.stream()
                .map(id -> labelRepository.findById(id).orElseThrow())
                .collect(Collectors.toSet());
    }
}
