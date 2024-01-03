package hexlet.code.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskStatusDTO {
    private long id;
    private long authorId;
    private String name;
    private String slug;
    private LocalDate createdAt;
}
