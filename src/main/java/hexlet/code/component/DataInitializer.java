package hexlet.code.component;

import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.CustomUserDetailsService;
import hexlet.code.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private CustomUserDetailsService userService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);
        if (userRepository.findAll().isEmpty()) {
            var email = "hexlet@example.com";
            var userData = new User();
            userData.setEmail(email);
            userData.setPasswordDigest("qwerty");
            userService.createUser(userData);
            var user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                log.info("User has been found");
            } else {
                log.info("User has not been found");
            }
        }
        if (taskStatusRepository.findAll().isEmpty()) {
            for (int i = 0; i < 5; i++) {
                var taskStatus = new TaskStatus();
                switch (i) {
                    case 0:
                        taskStatus.setName("Draft");
                        taskStatus.setSlug("draft");
                        break;
                    case 1:
                        taskStatus.setName("ToReview");
                        taskStatus.setSlug("to_review");
                        break;
                    case 2:
                        taskStatus.setName("ToBeFixed");
                        taskStatus.setSlug("to_be_fixed");
                        break;
                    case 3:
                        taskStatus.setName("ToPublish");
                        taskStatus.setSlug("to_publish");
                        break;
                    case 4:
                        taskStatus.setName("Published");
                        taskStatus.setSlug("published");
                        break;
                    default:
                        break;
                }
                taskStatusRepository.save(taskStatus);
            }
        }
        if (labelRepository.findAll().isEmpty()) {
            for (int i = 0; i < 2; i++) {
                var label = new Label();
                switch (i) {
                    case 0:
                        label.setName("feature");
                        break;
                    case 1:
                        label.setName("bug");
                        break;
                    default:
                        break;
                }
                labelRepository.save(label);
            }
        }
    }
}
