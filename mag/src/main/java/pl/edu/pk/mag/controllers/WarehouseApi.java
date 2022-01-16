package pl.edu.pk.mag.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.edu.pk.mag.repository.entity.enums.OrderStatus;
import pl.edu.pk.mag.requests.OrderRequest;
import pl.edu.pk.mag.requests.warehouse.AddUserToWarehouse;
import pl.edu.pk.mag.requests.warehouse.CreateWarehouse;
import pl.edu.pk.mag.requests.warehouse.PatchWarehouse;
import pl.edu.pk.mag.responses.WarehouseListResponse;
import pl.edu.pk.mag.service.OrderService;
import pl.edu.pk.mag.service.WarehouseService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/mag/warehouse")
public class WarehouseApi {

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private OrderService orderService;

    @PostMapping
    @PreAuthorize(value = "hasAuthority('WAREHOUSE.CREATE.NEW')")
    public ResponseEntity<?> createNewWarehouse(Principal principal, @RequestBody @Valid CreateWarehouse createWarehouse) {
        warehouseService.createNewWarehouse(createWarehouse);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/code/{whCode}/addUser")
    @PreAuthorize(value = "hasAuthority('WAREHOUSE.ADD.USER')||@warehouseService.isMemberAndHavePermission(#principal.getName(),#whCode,'ADD.MEMBER')")
    public ResponseEntity<?> addUserToWarehouse(
            Principal principal,
            @RequestBody @Valid AddUserToWarehouse addUserToWarehouse,
            @PathVariable(name = "whCode") String whCode) {
        warehouseService.addUserToWarehouse(addUserToWarehouse, whCode);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/code/{whCode}/getMembers")
    @PreAuthorize(value = "@warehouseService.isMemberOfWh(#principal.getName(),#whCode)||hasAuthority('WAREHOUSE.GET.MEMBER')")
    public ResponseEntity<?> getWarehouseMembers(Principal principal, @PathVariable String whCode) {
        return ResponseEntity.ok(warehouseService.getWarehouseMembers(whCode));
    }

    @GetMapping(path = "/code/{whCode}")
    @PreAuthorize(value = "@warehouseService.isMemberOfWh(#principal.getName(),#whCode)||hasAuthority('WAREHOUSE.GET.STORAGE.LOCATION')")
    public ResponseEntity<?> getWarehouseStorageLocation(Principal principal, @PathVariable String whCode) {
        return ResponseEntity.ok(warehouseService.getWarehouseStorageLocation(whCode));
    }

    @PatchMapping(path = "/code/{whCode}")
    @Transactional
    @PreAuthorize(value = "@warehouseService.isMemberAndHavePermission(#principal.getName(),#whCode,'MODIFY_WAREHOUSE')||hasAuthority('WAREHOUSE.CREATE.NEW')")
    public ResponseEntity<?> modifyWarehouse(Principal principal, @PathVariable String whCode, @RequestBody @Valid PatchWarehouse patchWarehouse) {
        warehouseService.patchWarehouse(patchWarehouse, whCode);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/code/{whCode}/modifyShelf")
    @PreAuthorize(value = "@warehouseService.isMemberAndHavePermission(#principal.getName(),#whCode,'MODIFY_SHELVES')||hasAuthority('WAREHOUSE.GET.STORAGE.LOCATION')")
    @Transactional
    public ResponseEntity<?> modifyWarehouseStorageLocation(Principal principal, @RequestBody @Valid ModifyStorageLocation modifyStorageLocation, @PathVariable String whCode) {
        warehouseService.modifyStorageLocation(modifyStorageLocation, whCode);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/code/{whCode}/addShelf")
    @PreAuthorize(value = "@warehouseService.isMemberAndHavePermission(#principal.getName(),#whCode,'MODIFY_SHELVES')||hasAuthority('WAREHOUSE.GET.STORAGE.LOCATION')")
    public ResponseEntity<?> addShelf(Principal principal, @PathVariable(name = "whCode") String whCode, @RequestParam(name = "shelfCode") String shelfCode) {
        warehouseService.addNewShelf(whCode, shelfCode);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @DeleteMapping(path = "/code/{whCode}/removeShelf")
    @PreAuthorize(value = "@warehouseService.isMemberAndHavePermission(#principal.getName(),#whCode,'MODIFY_SHELVES')||hasAuthority('WAREHOUSE.GET.STORAGE.LOCATION')")
    public ResponseEntity<?> removeShelf(Principal principal, @PathVariable(name = "whCode") String whCode, @RequestParam(name = "shelfCode") String shelfCode) {
        warehouseService.removeShelf(whCode, shelfCode);
        return ResponseEntity.noContent().build();
    }


    @GetMapping(path = "/code/{whCode}/SearchByProduct")
    @PreAuthorize(value = "@warehouseService.isMemberOfWh(#principal.getName(),#whCode)||hasAuthority('WAREHOUSE.GET.MEMBER')")
    public ResponseEntity<?> getShelvesByProduct(@PathVariable(name = "whCode") String whCode, @RequestParam(name = "name") String productCode) {
        return ResponseEntity.ok(warehouseService.getWarehouseStorageLocationByProduct(whCode, productCode));
    }

    @PostMapping(path = "/createOrder")
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderRequest orderRequest, Principal principal) {
        orderService.createOrder(orderRequest, principal.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/myOrder")
    public ResponseEntity<?> myOrder(Principal principal) {
        return ResponseEntity.ok(orderService.getOrderByUsername(principal.getName()));
    }

    @GetMapping(path = "/code/{whCode}/orders")
    @PreAuthorize(value = "@warehouseService.isMemberOfWh(#principal.getName(),#whCode)||hasAuthority('WAREHOUSE.GET.MEMBER')")
    public ResponseEntity<?> getWarehouseOrder(@PathVariable(name = "whCode") String whCode, Principal principal) {
        return ResponseEntity.ok(orderService.getOrderByWarehouse(whCode));
    }

    @GetMapping(path = "/code/{whCode}/orders/accept")
    @PreAuthorize(value = "@warehouseService.isMemberOfWh(#principal.getName(),#whCode)||hasAuthority('WAREHOUSE.GET.MEMBER')")
    public ResponseEntity<?> acceptOrder(@PathVariable(name = "whCode") String whCode, Principal principal, @RequestParam(name = "orderId") String orderId) {
        orderService.changeOrderStatus(orderId, whCode, OrderStatus.APPROVED);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/code/{whCode}/orders/reject")
    @PreAuthorize(value = "@warehouseService.isMemberOfWh(#principal.getName(),#whCode)||hasAuthority('WAREHOUSE.GET.MEMBER')")
    public ResponseEntity<?> rejectOrder(@PathVariable(name = "whCode") String whCode, Principal principal, @RequestParam(name = "orderId") String orderId) {
        orderService.changeOrderStatus(orderId, whCode, OrderStatus.CANCELED);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/myWh")
    public ResponseEntity<?> getMyWarehouseList(Principal principal) {
        List<WarehouseListResponse> list = new ArrayList<>();
        if (principal.getName() != null)
            list = warehouseService.getUserWarehouseList(principal.getName());
        return ResponseEntity.ok(list);
    }


    @GetMapping
    public ResponseEntity<?> getWarehouseList() {
        return ResponseEntity.ok(warehouseService.getWarehouseList());
    }


}
