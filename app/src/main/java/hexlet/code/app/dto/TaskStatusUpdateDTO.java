package hexlet.code.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TaskStatusUpdateDTO {
    @NotBlank
    @Size(min = 1)
    private String name;
}
