package pl.edu.pk.mag.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.edu.pk.mag.repository.UserRepository;
import pl.edu.pk.mag.repository.entity.Address;
import pl.edu.pk.mag.repository.entity.User;

@Component
public class StartupListener implements ApplicationListener<ContextStartedEvent> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        User user = new User();
        user.setEnabled(true);
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("admin"));
        user.setAddress(new Address());
        user.getAddress().setEmail("test@test.pl");
        userRepository.saveAndFlush(user);
    }
}
