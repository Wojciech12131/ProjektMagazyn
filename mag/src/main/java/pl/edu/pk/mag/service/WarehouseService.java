package pl.edu.pk.mag.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pk.mag.controllers.ModifyStorageLocation;
import pl.edu.pk.mag.exceptions.AppException;
import pl.edu.pk.mag.exceptions.ApplicationException;
import pl.edu.pk.mag.repository.*;
import pl.edu.pk.mag.repository.entity.*;
import pl.edu.pk.mag.requests.warehouse.AddUserToWarehouse;
import pl.edu.pk.mag.requests.warehouse.CreateWarehouse;
import pl.edu.pk.mag.responses.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private WarehouseGroupRepository warehouseGroupRepository;

    @Autowired
    private WarehouseGroupPermissionRepository warehouseGroupPermissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StorageLocationRepository storageLocationRepository;

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

    private List<WarehouseListResponse> toWarehouseList(Collection<Warehouse> warehouseList) {
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
        if (user.getWarehouseGroups().stream().anyMatch(a -> a.getWarehouseId().equals(warehouse.getId()))) {
            putWarehousePermissions(addUserToWarehouse, warehouse, user);
        } else {
            createNewWarehouseMember(addUserToWarehouse, warehouse, user);
        }
    }

    private void putWarehousePermissions(AddUserToWarehouse addUserToWarehouse, Warehouse warehouse, User user) {
        WarehouseGroup warehouseGroup = user.getWarehouseGroups()
                .stream().filter(a -> a.getWarehouseId().equals(warehouse.getId())).findFirst().orElseThrow();
        warehouseGroup.setWPermissions(getGroupPermission(addUserToWarehouse));
        warehouseGroupRepository.saveAndFlush(warehouseGroup);
    }

    private void createNewWarehouseMember(AddUserToWarehouse addUserToWarehouse, Warehouse warehouse, User user) {
        if (warehouse.getWarehouseGroup() == null)
            warehouse.setWarehouseGroup(new HashSet<>());
        List<WPermission> wPermissions = getGroupPermission(addUserToWarehouse);
        WarehouseGroup warehouseGroup = new WarehouseGroup(warehouse.getId(), user.getId(), wPermissions);
        warehouseGroupRepository.saveAndFlush(warehouseGroup);
    }

    private List<WPermission> getGroupPermission(AddUserToWarehouse addUserToWarehouse) {
        List<WPermission> wPermissions = new ArrayList<>();
        for (String permission : addUserToWarehouse.getWarehousePermissions()
        ) {
            WPermission wPermission = warehouseGroupPermissionRepository.getWPermissionByCode(permission).orElseThrow(AppException.NOT_FOUND_PERMISSION::getError);
            wPermissions.add(wPermission);
        }
        return wPermissions;
    }

    public List<WarehouseMembers> getWarehouseMembers(String whCode) {
        Warehouse warehouse = warehouseRepository.getWarehouseByCode(whCode).orElseThrow(AppException.NOT_FOUND_WAREHOUSE::getError);
        Set<WarehouseGroup> warehouseGroups = warehouse.getWarehouseGroup();
        List<WarehouseMembers> warehouseMembers = new ArrayList<>();
        warehouseGroups.stream().forEach(
                warehouseGroup ->
                {
                    WarehouseMembers warehouseMember = new WarehouseMembers(
                            userRepository.getUserById(warehouseGroup.getUserId()).orElseThrow(AppException.NOT_FOUND_USER::getError).getUsername(), new ArrayList<>());
                    for (WPermission perm : warehouseGroup.getWPermissions()
                    ) {
                        warehouseMember.getWarehousePermissions().add(perm.getCode());
                    }
                    warehouseMembers.add(warehouseMember);
                }
        );

        return warehouseMembers;
    }

    public List<WarehouseListResponse> getUserWarehouseList(String name) {
        Set<WarehouseGroup> warehouseGroups = userRepository.getUserByUsername(name).orElseThrow(AppException.NOT_FOUND_USER::getError).getWarehouseGroups();
        Set<Warehouse> warehouses = new HashSet<>();
        for (WarehouseGroup group : warehouseGroups
        ) {
            warehouses.add(warehouseRepository.getWarehouseById(group.getWarehouseId()));
        }
        return toWarehouseList(warehouses);
    }

    public boolean isMemberOfWh(String username, String whCode) {
        List<WarehouseListResponse> warehouseListResponses = getUserWarehouseList(username);
        return warehouseListResponses.stream().anyMatch(a -> a.getCode().equals(whCode));
    }

    public boolean isMemberAndHavePermission(String username, String whCode, String permission) {
        try {
            List<WarehouseMembers> warehouseMembers = getWarehouseMembers(whCode);
            Optional<WarehouseMembers> member = warehouseMembers.stream().filter(user -> user.getUsername().equals(username)).findFirst();
            return member.map(members -> members.getWarehousePermissions().stream().anyMatch(per -> per.equals(permission))).orElse(false);
        } catch (ApplicationException applicationException) {
            return false;
        }
    }

    public List<StorageLocationResponse> getWarehouseStorageLocation(String whCode) {
        Warehouse warehouse = warehouseRepository.getWarehouseByCode(whCode).orElseThrow(AppException.NOT_FOUND_WAREHOUSE::getError);
        return warehouse.getStorageLocations().stream().map(this::toStorageLocationResponse).collect(Collectors.toList());
    }

    public StorageLocationResponse toStorageLocationResponse(StorageLocation storageLocation) {
        StorageLocationResponse storageLocationResponse = new StorageLocationResponse();
        storageLocationResponse.setCode(storageLocation.getCode());
        storageLocationResponse.setQuantity(storageLocation.getQuantity());
        if (storageLocation.getProductId() != null)
            storageLocationResponse.setProduct(new ProductResponse(productRepository.getById(storageLocation.getProductId())));
        return storageLocationResponse;
    }

    public void modifyStorageLocation(ModifyStorageLocation modifyStorageLocation, String whCode) {
        validateModifyBody(modifyStorageLocation);
        Warehouse warehouse = warehouseRepository.getWarehouseByCode(whCode).orElseThrow(AppException.NOT_FOUND_WAREHOUSE::getError);
        StorageLocation storageLocation = storageLocationRepository
                .getStorageLocationByCodeAndWarehouseId(modifyStorageLocation.getCode(), warehouse.getId()).orElseThrow(AppException.NOT_FOUND_SHELF::getError);
        if (modifyStorageLocation.isRemoveProduct())
            removeProductFromStorage(storageLocation);
        if (modifyStorageLocation.getMoveProduct() != null)
            moveProductToAnotherLocation(modifyStorageLocation, storageLocation, warehouse);
        if (modifyStorageLocation.getAddProduct() != null)
            addProductToStorageLocation(modifyStorageLocation, storageLocation);
        storageLocationRepository.save(storageLocation);
    }

    private void removeProductFromStorage(StorageLocation storageLocation) {
        storageLocation.setProductId(null);
        storageLocation.setQuantity(new BigDecimal("0.000"));
    }

    private void validateModifyBody(ModifyStorageLocation modifyStorageLocation) {
        if (modifyStorageLocation.isRemoveProduct() &&
                modifyStorageLocation.getMoveProduct() != null && modifyStorageLocation.getMoveProduct().getDestinationShelfCode() != null)
            throw AppException.REMOVE_AND_MOVE_NOT_POSSIBLE.getError();
    }

    private void moveProductToAnotherLocation(ModifyStorageLocation modifyStorageLocation, StorageLocation storageLocation, Warehouse warehouse) {
        StorageLocation targetStorageLocation = storageLocationRepository
                .getStorageLocationByCodeAndWarehouseId(modifyStorageLocation.getMoveProduct().getDestinationShelfCode(), warehouse.getId()).orElseThrow(AppException.NOT_FOUND_DESTINATION_SHELF::getError);
        if (targetStorageLocation.getProductId() != null)
            throw AppException.DESTINATION_SHELF_IS_NOT_EMPTY.getError();
        targetStorageLocation.setProductId(storageLocation.getProductId());
        targetStorageLocation.setQuantity(storageLocation.getQuantity());
        storageLocation.setProductId(null);
        storageLocation.setQuantity(new BigDecimal("0.000"));
        storageLocationRepository.save(targetStorageLocation);
    }

    private void addProductToStorageLocation(ModifyStorageLocation modifyStorageLocation, StorageLocation storageLocation) {
        Product product = productRepository.getProductByCode(modifyStorageLocation.getAddProduct().getCode()).orElse(
                productRepository.save(new Product(modifyStorageLocation.getAddProduct().getCode(), modifyStorageLocation.getAddProduct().getDesc())));
        if (0 == 0)
            throw AppException.NOT_FOUND_BOOK.getError();
        storageLocation.setProductId(product.getId());
        storageLocation.setQuantity(modifyStorageLocation.getAddProduct().getQuantity());
    }

}
