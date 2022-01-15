package pl.edu.pk.mag.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.pk.mag.requests.warehouse.AddUserToWarehouse;
import pl.edu.pk.mag.requests.warehouse.CreateWarehouse;
import pl.edu.pk.mag.responses.WarehouseListResponse;
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

    @PostMapping
    @PreAuthorize(value = "hasAuthority('WAREHOUSE.CREATE.NEW')")
    public ResponseEntity<?> createNewWarehouse(Principal principal, @RequestBody @Valid CreateWarehouse createWarehouse) {
        warehouseService.createNewWarehouse(createWarehouse);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/code/{whCode}/addUser")
    @PreAuthorize(value = "hasAuthority('WAREHOUSE.ADD.USER')||@warehouseService.isMemberAndHavePermission(#principal.getName(),#whCode,'ADD.MEMBER')")
    public ResponseEntity<?> addUserToWarehouse(Principal principal, @RequestBody @Valid AddUserToWarehouse addUserToWarehouse, @PathVariable(name = "whCode", required = true) String whCode) {
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

    @GetMapping(path = "/code/myWh")
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
