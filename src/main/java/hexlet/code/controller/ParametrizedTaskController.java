package hexlet.code.controller;

import hexlet.code.dto.ParametrizedTaskDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.repository.TaskRepository;
import hexlet.code.specification.TaskSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class ParametrizedTaskController {
    @Autowired
    TaskSpecification taskSpecification;
    @Autowired
    TaskMapper taskMapper;
    @Autowired
    TaskRepository taskRepository;
    @GetMapping("")
    public ResponseEntity<List<TaskDTO>> getAll(ParametrizedTaskDTO dto) {
        var result = taskSpecification.build(dto);
        var tasks = taskMapper.map(taskRepository.findAll(result));
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(tasks.size()))
                .body(tasks);
    }
}
