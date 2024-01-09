package hexlet.code.controller;

import hexlet.code.dto.UserCreateDTO;
import hexlet.code.dto.UserDTO;
import hexlet.code.dto.UserUpdateDTO;
import hexlet.code.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    @Autowired
    UserService userService;
    @GetMapping(path = "")
    public ResponseEntity<List<UserDTO>>  index() {
        return userService.getAll();
    }
    @GetMapping(path = "/{id}")
    public UserDTO show(@PathVariable long id) {
        return userService.findById(id);
    }
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@Valid @RequestBody UserCreateDTO dto) {
        return userService.create(dto);
    }
    @PutMapping(path = "/{id}")
    @PreAuthorize("@userUtils.isCurrentUser(#id)")
    public UserDTO update(@Valid @RequestBody UserUpdateDTO dto, @PathVariable long id) {
        return userService.update(dto, id);
    }
    @DeleteMapping(path = "/{id}")
    @PreAuthorize("@userUtils.isCurrentUser(#id)")
    public void delete(@PathVariable long id) {
        userService.delete(id);
    }
}
