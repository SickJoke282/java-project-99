package hexlet.code.app.util;

import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import lombok.Getter;
import net.datafaker.Faker;
import org.instancio.Model;
import org.instancio.Instancio;
import org.instancio.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import hexlet.code.app.model.User;
import jakarta.annotation.PostConstruct;

import java.util.Arrays;
import java.util.List;

@Getter
@Component
public class ModelGenerator {
    private Model<User> userModel;
    private Model<Task> taskModel;
    private Model<TaskStatus> taskStatusModel;
    @Autowired
    private Faker faker;
    @PostConstruct
    private void init() {
        taskModel = Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .supply(Select.field(Task::getName), () -> faker.gameOfThrones().house())
                .supply(Select.field(Task::getDescription), () -> faker.gameOfThrones().quote())
                .ignore(Select.field(Task::getTaskStatus))
                .ignore(Select.field(Task::getAssignee))
                .ignore(Select.field(Task::getCreatedAt))
                .toModel();
        userModel = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .ignore(Select.field(User::getCreatedAt))
                .ignore(Select.field(User::getUpdatedAt))
                .supply(Select.field(User::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(User::getLastName), () -> faker.name().lastName())
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .supply(Select.field(User::getPasswordDigest), () -> faker.internet().password())
                .toModel();
        taskStatusModel = Instancio.of(TaskStatus.class)
                .ignore(Select.field(TaskStatus::getId))
                .supply(Select.field(TaskStatus::getName), () -> faker.gameOfThrones().house())
                .supply(Select.field(TaskStatus::getSlug), () -> faker.gameOfThrones().city().toLowerCase())
                .ignore(Select.field(TaskStatus::getCreatedAt))
                .toModel();
    }
}
