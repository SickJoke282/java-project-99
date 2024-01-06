package hexlet.code.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCreateDTO {
    @NotBlank
    private String title;
    private Integer index;
    private String content;
    private String status;
    @JsonProperty("assignee_id")
    private Long assigneeId;
}
