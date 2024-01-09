package hexlet.code.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParametrizedTaskDTO {
    private String titleCont;
    private long assigneeId;
    private String status;
    private long labelId;
}
