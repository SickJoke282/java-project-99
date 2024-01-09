package hexlet.code.service;

import hexlet.code.dto.ParametrizedTaskDTO;
import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.repository.TaskRepository;
import hexlet.code.specification.TaskSpecification;
import hexlet.code.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    TaskMapper taskMapper;
    @Autowired
    UserUtils userUtils;
    @Autowired
    TaskSpecification taskSpecification;
    public List<TaskDTO> getAll() {
        var tasks = taskRepository.findAll();
        return tasks.stream()
                .map(taskMapper::map)
                .toList();
    }
    public List<TaskDTO> getParameterizedAll(ParametrizedTaskDTO dto) {
        var tasks = taskRepository.findAll(taskSpecification.build(dto));
        return taskMapper.map(tasks);
    }
    public TaskDTO create(TaskCreateDTO dto) {
        var task = taskMapper.map(dto);
        System.out.println(userUtils.getCurrentUser());
        task.setAssignee(userUtils.getCurrentUser());
        System.out.println(task.toString());
        taskRepository.save(task);
        return taskMapper.map(task);
    }
    public TaskDTO findById(Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        return taskMapper.map(task);
    }
    public TaskDTO update(TaskUpdateDTO dto, Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        taskMapper.update(dto, task);
        taskRepository.save(task);
        return taskMapper.map(task);
    }
    public void delete(Long id) {
        System.out.println(taskRepository.findAll().stream().map(Object::toString).toList());
        taskRepository.deleteById(id);
    }
}
