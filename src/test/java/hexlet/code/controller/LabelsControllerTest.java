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

import hexlet.code.dto.LabelUpdateDTO;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
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

@SpringBootTest
@AutoConfigureMockMvc
public class LabelsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LabelMapper labelMapper;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private UserContextService userContextService;

    private JwtRequestPostProcessor token;

    private Label testLabel;

    @BeforeEach
    public void setUp() {
        labelRepository.deleteAll();
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
        testLabel = Instancio.of(modelGenerator.getLabelModel())
                .create();
    }

    @Test
    public void testLabelIndex() throws Exception {
        labelRepository.save(testLabel);
        var result = mockMvc.perform(get("/api/labels").with(token))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testLabelCreate() throws Exception {
        var dto = labelMapper.map(testLabel);
        var request = post("/api/labels")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));
        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var label = labelRepository.findByName(testLabel.getName()).get();
        assertNotNull(label);
        assertThat(label.getName()).isEqualTo(testLabel.getName());
    }

    @Test
    public void testLabelUpdate() throws Exception {
        labelRepository.save(testLabel);

        var data = new LabelUpdateDTO();
        data.setName(JsonNullable.of("new_name"));

        var request = put("/api/labels/" + testLabel.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        testLabel = labelRepository.findById(testLabel.getId()).get();
        assertThat(testLabel.getName()).isEqualTo(data.getName().get());
    }

    @Test
    public void testLabelShow() throws Exception {
        labelRepository.save(testLabel);

        var request = get("/api/labels/" + testLabel.getId()).with(jwt());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(testLabel.getName())
        );
    }

    @Test
    public void testLabelDestroy() throws Exception {
        labelRepository.save(testLabel);
        var request = delete("/api/labels/" + testLabel.getId()).with(token);
        mockMvc.perform(request)
                .andExpect(status().isNoContent());
        assertThat(labelRepository.existsById(testLabel.getId())).isEqualTo(false);
    }
}
