package hexlet.code.app.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDTO {
    @Column(unique = true)
    @Email
    @NotBlank
    private String email;
    @Size(min = 3)
    private String passwordDigest;
}
