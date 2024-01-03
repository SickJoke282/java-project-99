package hexlet.code.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskStatusCreateDTO {
    @NotNull
    private long authorId;
    @NotBlank
    @Size(min = 1)
    private String name;
    @NotBlank
    @Size(min = 1)
    private String slug;
}
