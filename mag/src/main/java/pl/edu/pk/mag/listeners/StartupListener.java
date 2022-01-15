package pl.edu.pk.mag.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.edu.pk.mag.repository.*;
import pl.edu.pk.mag.repository.entity.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class StartupListener implements ApplicationListener<ContextStartedEvent> {

    @Autowired
    WarehouseGroupRepository warehouseGroupRepository;
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
    @Autowired
    private StorageLocationRepository storageLocationRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {

        Permission warehouseCreateNew = Permission.builder().code("WAREHOUSE.CREATE.NEW").build();
        Permission warehouseAddMember = Permission.builder().code("WAREHOUSE.ADD.USER").build();
        Permission warehouseGetMember = Permission.builder().code("WAREHOUSE.GET.MEMBER").build();
        Permission warehouseGetStorage = Permission.builder().code("WAREHOUSE.GET.STORAGE.LOCATION").build();

        permissionRepository.saveAndFlush(warehouseCreateNew);
        permissionRepository.saveAndFlush(warehouseAddMember);
        permissionRepository.saveAndFlush(warehouseGetMember);
        permissionRepository.saveAndFlush(warehouseGetStorage);
        Role role = new Role("admin", Set.of(warehouseCreateNew, warehouseAddMember, warehouseGetMember, warehouseGetStorage));

        WPermission addMember = WPermission.builder().code("ADD.MEMBER").build();
        WPermission removeMember = WPermission.builder().code("REMOVE.MEMBER").build();
        WPermission modifyShelves = WPermission.builder().code("MODIFY_SHELFS").build();
        WPermission modifyWarehouse = WPermission.builder().code("MODIFY_WAREHOUSE").build();

        roleRepository.saveAndFlush(role);

        User user = new User();
        user.setEnabled(true);
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("admin"));
        user.setAddress(new Address());
        user.getAddress().setEmail("test@test.pl");
        user.setRoles(Set.of(role));
        user = userRepository.saveAndFlush(user);

        Warehouse warehouse = new Warehouse();
        warehouse.setCode("testWh");
        warehouse.setDescription("testowy magazyn");
        Address address = new Address();
        address.setEmail("test2@test2.pl");
        address.setMobile("985234231");
        address.setCity("Kraków");
        address.setStreet("asd");

        Product product1 = productRepository.saveAndFlush(new Product("Mąka", "no mąka"));
        Product product2 = productRepository.saveAndFlush(new Product("Klej", "klej"));
        Product product3 = productRepository.saveAndFlush(new Product("Cement", "do budowy"));
        Product product4 = productRepository.saveAndFlush(new Product("Monitor", "hd"));

        warehouse.setAddress(address);
        warehouse.setWarehouseGroup(new HashSet<>());
        warehouse = warehouseRepository.saveAndFlush(warehouse);
        StorageLocation storageLocation = new StorageLocation("01A", warehouse.getId(), product1.getId(), new BigDecimal("1.00"));
        StorageLocation storageLocation1 = new StorageLocation("02A", warehouse.getId(), product1.getId(), new BigDecimal("2.00"));
        StorageLocation storageLocation2 = new StorageLocation("01B", warehouse.getId(), product2.getId(), new BigDecimal("3.50"));
        StorageLocation storageLocation3 = new StorageLocation("02B", warehouse.getId(), product3.getId(), new BigDecimal("3.44"));
        StorageLocation storageLocation4 = new StorageLocation("01C", warehouse.getId(), product4.getId(), new BigDecimal("4.00"));
        StorageLocation storageLocation5 = new StorageLocation("02C", warehouse.getId(), product2.getId(), new BigDecimal("3.44"));
        storageLocationRepository.saveAll(List.of(storageLocation, storageLocation1, storageLocation2, storageLocation3, storageLocation4, storageLocation5));

        warehouseGroupRepository.saveAndFlush(new WarehouseGroup(warehouse.getId(), user.getId(), List.of(addMember, removeMember, modifyShelves, modifyWarehouse)));
    }
}
