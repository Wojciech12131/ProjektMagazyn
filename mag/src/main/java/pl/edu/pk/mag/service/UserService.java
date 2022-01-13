package pl.edu.pk.mag.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.pk.mag.exceptions.AppException;
import pl.edu.pk.mag.repository.UserRepository;
import pl.edu.pk.mag.repository.entity.Address;
import pl.edu.pk.mag.repository.entity.User;
import pl.edu.pk.mag.requests.UserRegistration;
import pl.edu.pk.mag.responses.UserToLogin;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserToLogin getUserByUsername(String username) {
        Optional<User> user = userRepository.getUserByUsername(username);
        return UserToLogin.getInstance(user.orElseThrow(AppException.NOT_FOUND_USER::getError));
    }

    public void createNewUser(UserRegistration userRegistration) {
        if (isUserIsUnique(userRegistration.getUsername()))
            throw AppException.USERNAME_NOT_UNIQUE.getError();
        userRepository.saveAndFlush(convertToUser(userRegistration));
    }

    private boolean isUserIsUnique(String username) {
        return userRepository.existsUserByUsername(username);
    }

    public User convertToUser(UserRegistration userRegistration) {
        User user = new User();
        user.setUsername(userRegistration.getUsername());
        user.setPassword(passwordEncoder.encode(userRegistration.getPassword()));
        user.setEnabled(true);
        Address address = new Address(userRegistration.getAddress().getEmail(), userRegistration.getAddress().getMobile());
        user.setAddress(address);
        return user;
    }
}
