package hexlet.code.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Table(name = "tasks")
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    @Size(min = 1)
    private String name;
    @Digits(integer = 5, fraction = 0)
    private int index;
    private String description;
    @ManyToOne(fetch = FetchType.EAGER)
    private TaskStatus taskStatus;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @NotNull
    private User assignee;
    @CreatedDate
    private LocalDate createdAt;
}
