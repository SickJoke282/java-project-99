package hexlet.code.app.controller;

import hexlet.code.app.dto.ParametrizedTaskDTO;
import hexlet.code.app.dto.TaskDTO;
import hexlet.code.app.mapper.TaskMapper;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.specification.TaskSpecification;
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
