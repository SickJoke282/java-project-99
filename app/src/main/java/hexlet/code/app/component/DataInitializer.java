package hexlet.code.app.component;

import hexlet.code.app.service.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomUserDetailsService userService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);
        userRepository.deleteAll();
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
}
