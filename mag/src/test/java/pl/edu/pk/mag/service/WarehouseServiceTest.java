package pl.edu.pk.mag.service;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.edu.pk.mag.exceptions.AppException;
import pl.edu.pk.mag.exceptions.ApplicationException;
import pl.edu.pk.mag.repository.OrderRepository;
import pl.edu.pk.mag.repository.ProductRepository;
import pl.edu.pk.mag.repository.UserRepository;
import pl.edu.pk.mag.repository.WarehouseRepository;
import pl.edu.pk.mag.repository.entity.*;
import pl.edu.pk.mag.repository.entity.enums.OrderStatus;
import pl.edu.pk.mag.requests.UserAddressReg;
import pl.edu.pk.mag.requests.UserRegistration;
import pl.edu.pk.mag.requests.warehouse.AddUserToWarehouse;
import pl.edu.pk.mag.requests.warehouse.CreateWarehouse;
import pl.edu.pk.mag.requests.warehouse.WarehouseAddress;
import pl.edu.pk.mag.responses.AddressResponse;
import pl.edu.pk.mag.responses.ProductResponse;
import pl.edu.pk.mag.responses.StorageLocationResponse;
import pl.edu.pk.mag.responses.WarehouseListResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
class WarehouseServiceTest {

    @Autowired
    WarehouseService warehouseService;

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @MockBean
    WarehouseRepository warehouseRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    ProductRepository productRepository;

    @MockBean
    OrderRepository orderRepository;

    @Test
    void errorNotUniqueWarehouseCode() {
        CreateWarehouse createWarehouse = new CreateWarehouse();
        createWarehouse.setCode("aaa");
        createWarehouse.setDescription("aaa");
        createWarehouse.setAddress(new WarehouseAddress());
        Mockito.when(warehouseRepository.existsWarehouseByCode("aaa")).thenReturn(true);
        Exception exception = Assert.assertThrows(ApplicationException.class, () -> {
            warehouseService.createNewWarehouse(createWarehouse);
        });
        Assertions.assertEquals("Nie unikatowa nazwa magazynu.", exception.getMessage());
    }

    @Test
    void errorUserNotFound() {
        AddUserToWarehouse addUserToWarehouse = new AddUserToWarehouse();
        addUserToWarehouse.setUsername("abc");
        Mockito.when(userRepository.getUserByUsername("abc")).thenReturn(Optional.empty());
        Exception exception = Assert.assertThrows(ApplicationException.class, () ->
                warehouseService.addUserToWarehouse(addUserToWarehouse, "123")
        );
        Assertions.assertEquals(exception.getMessage(), AppException.NOT_FOUND_USER.getError().getMessage());
    }

    @Test
    void getWarehouseList() {
        Warehouse warehouse = new Warehouse();
        warehouse.setId(1L);
        warehouse.setDescription("test");
        warehouse.setCode("asd");
        warehouse.setAddress(new Address());
        WarehouseListResponse warehouseListResponse = new WarehouseListResponse(warehouse.getCode(), warehouse.getDescription(), new AddressResponse(warehouse.getAddress().getEmail(), warehouse.getAddress().getMobile(), warehouse.getAddress().getCity(), warehouse.getAddress().getStreet()));
        Mockito.when(warehouseRepository.findAll()).thenReturn(List.of(warehouse));
        Assert.assertEquals(warehouseService.getWarehouseList(), List.of(warehouseListResponse));
    }

    @Test
    void errorNotFoundWarehouse() {
        String whCode = "aaa";
        Mockito.when(warehouseRepository.getWarehouseByCode(whCode)).thenReturn(Optional.empty());
        Exception exception = Assert.assertThrows(ApplicationException.class, () ->
                warehouseService.getWarehouseMembers(whCode)
        );

        Assertions.assertEquals(exception.getMessage(), AppException.NOT_FOUND_WAREHOUSE.getError().getMessage());
    }


    @Test
    void toStorageLocationResponse() {
        Product product = new Product();
        product.setCode("123");
        product.setDescription("234");
        ProductResponse productResponse = new ProductResponse(product);
        Mockito.when(productRepository.getById(10L)).thenReturn(product);
        StorageLocation storageLocation = new StorageLocation();
        storageLocation.setWarehouseId(10L);
        storageLocation.setProductId(10L);
        storageLocation.setCode("Asd");
        storageLocation.setQuantity(new BigDecimal("1.0"));
        StorageLocationResponse storageLocationResponse = new StorageLocationResponse();
        storageLocationResponse.setProduct(productResponse);
        storageLocationResponse.setCode("Asd");
        storageLocationResponse.setQuantity(storageLocation.getQuantity());
        Assert.assertEquals(warehouseService.toStorageLocationResponse(storageLocation), storageLocationResponse);
    }

    @Test
    void addNewShelfErrorShelfCodeIsEmpty() {
        Exception exception = Assert.assertThrows(ApplicationException.class, () ->
                warehouseService.addNewShelf("asd", ""));
        Assert.assertEquals(exception.getMessage(), AppException.SHELF_CODE_ID_EMPTY_OR_NULL.getError().getMessage());
    }

    @Test
    void convertToUser() {
        UserRegistration userRegistration = new UserRegistration();
        userRegistration.setUsername("123");
        userRegistration.setPassword("234");
        UserAddressReg userAddressReg = new UserAddressReg();
        userAddressReg.setEmail("sadaf@asd.pl");
        userAddressReg.setMobile("123654234");
        userRegistration.setAddress(userAddressReg);
        User user = userService.convertToUser(userRegistration);
        Assert.assertEquals(user.getUsername(), userRegistration.getUsername());
        Assert.assertTrue(passwordEncoder.matches(userRegistration.getPassword(), user.getPassword()));
    }

    @Test
    void wrongNumberFormat() {
        String test = "1.23";

        Assert.assertThrows(ApplicationException.class, () ->
                orderService.changeOrderStatus(test, "cide", OrderStatus.APPROVED));
    }

    @Test
    void changeOrderStatusNotFountOrder() {
        String test = "2";
        String whCode = "cide";
        Warehouse warehouse = new Warehouse();
        warehouse.setId(2L);
        UserOrder userOrder = new UserOrder();
        userOrder.setOrderStatus(OrderStatus.PENDING);
        Mockito.when(warehouseRepository.getWarehouseByCode(whCode)).thenReturn(Optional.of(warehouse));
        Mockito.when(orderRepository.getOrderByWarehouseIdAndId(warehouse.getId(), Long.valueOf(test))).thenReturn(Optional.of(userOrder));
        orderService.changeOrderStatus(test, whCode, OrderStatus.APPROVED);

    }

    @Test
    void errorInvalidOrderStatus() {
        String test = "2";
        String whCode = "cide";
        Warehouse warehouse = new Warehouse();
        warehouse.setId(2L);
        UserOrder userOrder = new UserOrder();
        userOrder.setOrderStatus(OrderStatus.APPROVED);
        Mockito.when(warehouseRepository.getWarehouseByCode(whCode)).thenReturn(Optional.of(warehouse));
        Mockito.when(orderRepository.getOrderByWarehouseIdAndId(warehouse.getId(), Long.valueOf(test))).thenReturn(Optional.of(userOrder));
        Assert.assertThrows(ApplicationException.class, () ->
                orderService.changeOrderStatus(test, whCode, OrderStatus.APPROVED));


    }


}