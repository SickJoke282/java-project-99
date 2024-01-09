package hexlet.code.app.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserDTO {
    private long id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    @Email
    @NotBlank
    private String email;
    @Size(min = 3)
    private String password;
    private LocalDate createdAt;
}
