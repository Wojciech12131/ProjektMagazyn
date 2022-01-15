package pl.edu.pk.mag.controllers;


import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.edu.pk.mag.exceptions.ApplicationException;
import pl.edu.pk.mag.requests.UserRegistration;
import pl.edu.pk.mag.responses.UserToLogin;
import pl.edu.pk.mag.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/mag/user")
public class UserApi {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/byName/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable(name = "username") String username) {
        if (username == null || StringUtils.isBlank(username))
            throw new ApplicationException("MISSING_PARAM", 404, "Nie podano obowiązkowego parametru: username");
        UserToLogin userToLogin = userService.getUserByUsername(username);
        return new ResponseEntity<>(userToLogin, HttpStatus.OK);
    }

    @GetMapping(path = "/myPerm")
    public ResponseEntity<?> getMyPermissions() {
        List<String> permissions = new ArrayList<>();
        for (GrantedAuthority g : SecurityContextHolder.getContext().getAuthentication().getAuthorities()
        ) {
            permissions.add(g.getAuthority());
        }
        SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        return ResponseEntity.ok(permissions);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Object> createNewUser(@Valid @RequestBody UserRegistration userRegistration) {
        userService.createNewUser(userRegistration);
        return ResponseEntity.noContent().build();
    }

}
