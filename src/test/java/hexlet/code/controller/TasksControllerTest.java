package hexlet.code.controller;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.UserContextService;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import hexlet.code.util.ModelGenerator;

import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
public class TasksControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private UserContextService userContextService;

    private JwtRequestPostProcessor token;

    private Task testTask;
    private TaskStatus testTaskStatus;
    private Label testLabel;

    @BeforeEach
    public void setUp() {
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
        testTask = Instancio.of(modelGenerator.getTaskModel())
                .create();
        testTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel())
                        .create();
        testLabel = Instancio.of(modelGenerator.getLabelModel())
                        .create();
        taskStatusRepository.save(testTaskStatus);
        labelRepository.save(testLabel);
        testTask.setAssignee(userContextService.getTestUser());
        testTask.setTaskStatus(testTaskStatus);
        testTask.setLabels(Set.of(testLabel));
    }

    @Test
    public void testTaskIndex() throws Exception {
        taskRepository.save(testTask);
        var result = mockMvc.perform(get("/api/tasks").with(token))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }
    @Test
    public void testParametrizedTaskIndex() throws Exception {
        taskRepository.save(testTask);
        var request = get("/api/tasks?"
                + "titleCont=" + "test"
                + "&assigneeId=" + 2
                + "&status=" + "test"
                + "&labelId=" + 4)
                .with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray().hasSize(0);
    }

    @Test
    public void testTaskCreate() throws Exception {
        var dto = taskMapper.map(testTask);
        var request = post("/api/tasks")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));
        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var task = taskRepository.findByName(testTask.getName()).get();
        assertNotNull(task);
        assertThat(task.getName()).isEqualTo(testTask.getName());
    }

    @Test
    public void testTaskUpdate() throws Exception {
        taskRepository.save(testTask);

        var data = new TaskUpdateDTO();
        data.setTitle(JsonNullable.of("new name"));

        var request = put("/api/tasks/" + testTask.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        testTask = taskRepository.findById(testTask.getId()).get();
        assertThat(testTask.getName()).isEqualTo(data.getTitle().get());
    }
    @Test
    public void testTaskShow() throws Exception {
        taskRepository.save(testTask);

        var request = get("/api/tasks/" + testTask.getId()).with(jwt());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("title").isEqualTo(testTask.getName()),
                v -> v.node("content").isEqualTo(testTask.getDescription()));
    }

    @Test
    public void testTaskDestroy() throws Exception {
        taskRepository.save(testTask);
        var request = delete("/api/tasks/" + testTask.getId()).with(token);
        mockMvc.perform(request)
                .andExpect(status().isNoContent());
        assertThat(taskRepository.existsById(testTask.getId())).isEqualTo(false);
    }
}
