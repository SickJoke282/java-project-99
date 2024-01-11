package hexlet.code.service;

import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserContextService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
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
    public boolean isAssignee(long id) {
        var testTaskAssigneeEmail = taskRepository.findById(id).get().getAssignee().getEmail();
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return testTaskAssigneeEmail.equals(authentication.getName());
    }
    public boolean isCurrentUser(long id) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = getCurrentUser();
        return currentUser != null && currentUser.getEmail().equals(userRepository.findById(id).get().getUsername());
    }
    public boolean isAuthenticated() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
}
