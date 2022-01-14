package pl.edu.pk.mag.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pk.mag.exceptions.AppException;
import pl.edu.pk.mag.repository.UserRepository;
import pl.edu.pk.mag.repository.WarehouseRepository;
import pl.edu.pk.mag.repository.entity.Address;
import pl.edu.pk.mag.repository.entity.User;
import pl.edu.pk.mag.repository.entity.Warehouse;
import pl.edu.pk.mag.requests.warehouse.AddUserToWarehouse;
import pl.edu.pk.mag.requests.warehouse.CreateWarehouse;
import pl.edu.pk.mag.responses.AddressResponse;
import pl.edu.pk.mag.responses.WarehouseListResponse;

import java.util.ArrayList;
import java.util.List;

@Service
public class WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private UserRepository userRepository;

    public void createNewWarehouse(CreateWarehouse createWarehouse) {
        if (warehouseRepository.existsWarehouseByCode(createWarehouse.getCode()))
            throw AppException.WAREHOUSE_CODE_NOT_UNIQUE.getError();
        warehouseRepository.saveAndFlush(createWarehouse(createWarehouse));
    }

    public List<WarehouseListResponse> getWarehouseList() {
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        return toWarehouseList(warehouseList);
    }

    private Warehouse createWarehouse(CreateWarehouse createWarehouse) {
        Warehouse warehouse = new Warehouse();
        warehouse.setCode(createWarehouse.getCode());
        warehouse.setDescription(createWarehouse.getDescription());
        Address address = new Address(
                createWarehouse.getAddress().getEmail(),
                createWarehouse.getAddress().getMobile(),
                createWarehouse.getAddress().getCity(),
                createWarehouse.getAddress().getStreet()
        );
        warehouse.setAddress(address);
        return warehouse;
    }

    private List<WarehouseListResponse> toWarehouseList(List<Warehouse> warehouseList) {
        List<WarehouseListResponse> warehouseListResponses = new ArrayList<>();
        for (Warehouse w : warehouseList
        ) {
            warehouseListResponses.add(
                    new WarehouseListResponse(w.getCode(), w.getDescription(), toAddressResponse(w.getAddress()))
            );

        }
        return warehouseListResponses;
    }

    private AddressResponse toAddressResponse(Address address) {
        return new AddressResponse(address.getEmail(), address.getMobile(), address.getCity(), address.getStreet());
    }


    public void addUserToWarehouse(AddUserToWarehouse addUserToWarehouse, String whCode) {
        User user = userRepository.getUserByUsername(addUserToWarehouse.getUsername()).orElseThrow(AppException.NOT_FOUND_USER::getError);
        Warehouse warehouse = warehouseRepository.getWarehouseByCode(whCode).orElseThrow(AppException.NOT_FOUND_WAREHOUSE::getError);
        if (user.getWarehouseGroups().stream().anyMatch(a -> a.getWarehouse().getCode().equals(whCode))) {
            putWarehousePermissions(addUserToWarehouse, warehouse, user);
        } else {
            createNewWarehouseMember(addUserToWarehouse, warehouse, user);
        }
    }

    private void putWarehousePermissions(AddUserToWarehouse addUserToWarehouse, Warehouse warehouse, User user) {

    }

    private void createNewWarehouseMember(AddUserToWarehouse addUserToWarehouse, Warehouse warehouse, User user) {
        
    }
}
