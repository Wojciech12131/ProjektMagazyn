package pl.edu.pk.mag.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.edu.pk.mag.repository.PermissionRepository;
import pl.edu.pk.mag.repository.RoleRepository;
import pl.edu.pk.mag.repository.UserRepository;
import pl.edu.pk.mag.repository.WarehouseRepository;
import pl.edu.pk.mag.repository.entity.*;

import java.util.HashSet;
import java.util.Set;

@Component
public class StartupListener implements ApplicationListener<ContextStartedEvent> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {

        Permission warehouseCreateNew = Permission.builder().code("WAREHOUSE.CREATE.NEW").build();
        permissionRepository.saveAndFlush(warehouseCreateNew);

        Role role = new Role("admin", Set.of(warehouseCreateNew));
        roleRepository.saveAndFlush(role);

        User user = new User();
        user.setEnabled(true);
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("admin"));
        user.setAddress(new Address());
        user.getAddress().setEmail("test@test.pl");
        user.setRoles(Set.of(role));
        userRepository.saveAndFlush(user);

        Warehouse warehouse = new Warehouse();
        warehouse.setCode("testWh");
        warehouse.setDescription("testowy magazyn");
        Address address = new Address();
        address.setEmail("test2@test2.pl");
        address.setMobile("985234231");
        address.setCity("Kraków");
        address.setStreet("asd");
        warehouse.setAddress(address);
        warehouse.setWarehouseGroup(new HashSet<>());
        warehouseRepository.saveAndFlush(warehouse);
    }
}
