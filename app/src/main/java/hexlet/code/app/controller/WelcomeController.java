package hexlet.code.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/welcome")
public class WelcomeController {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    String welcome() {
        return "Welcome to Spring!";
    }
}
