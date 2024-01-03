package hexlet.code.app.utils;

import hexlet.code.app.model.User;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        var email = authentication.getName();
        return userRepository.findByEmail(email).get();
    }
    public User getTestUser() {
        return userRepository.findByEmail("hexlet@example.com")
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    public boolean isAuthor(long id) {
        var testStatusAuthorEmail = taskStatusRepository.findById(id).get().getAuthor().getEmail();
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return testStatusAuthorEmail.equals(authentication.getName());
    }
    public boolean isCurrentUser(long id) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = getCurrentUser();
        return currentUser != null && currentUser.getEmail().equals(userRepository.findById(id).get().getUsername());
    }
}
