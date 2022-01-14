package pl.edu.pk.mag.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.edu.pk.mag.repository.*;
import pl.edu.pk.mag.repository.entity.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class StartupListener implements ApplicationListener<ContextStartedEvent> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private WarehouseGroupPermissionRepository warehouseGroupPermissionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {

        Permission warehouseCreateNew = Permission.builder().code("WAREHOUSE.CREATE.NEW").build();
        Permission warehouseAddMember = Permission.builder().code("WAREHOUSE.ADD.USER").build();
        permissionRepository.saveAndFlush(warehouseCreateNew);
        permissionRepository.saveAndFlush(warehouseAddMember);

        WPermission addMember = WPermission.builder().code("ADD.MEMBER").build();
        WPermission removeMember = WPermission.builder().code("REMOVE.MEMBER").build();
        warehouseGroupPermissionRepository.saveAll(List.of(addMember, removeMember));
        warehouseGroupPermissionRepository.flush();

        Role role = new Role("admin", Set.of(warehouseCreateNew, warehouseAddMember));
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
