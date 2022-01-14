package pl.edu.pk.mag.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.pk.mag.requests.warehouse.AddUserToWarehouse;
import pl.edu.pk.mag.requests.warehouse.CreateWarehouse;
import pl.edu.pk.mag.service.WarehouseService;

import javax.validation.Valid;
import java.security.Principal;

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

    @PostMapping(path = "/addUser/{whCode}")
    @PreAuthorize(value = "hasAuthority('WAREHOUSE.ADD.USER')")
    public ResponseEntity<?> addUserToWarehouse(@RequestBody @Valid AddUserToWarehouse addUserToWarehouse, @PathVariable(name = "whCode", required = true) String whCode) {
        warehouseService.addUserToWarehouse(addUserToWarehouse, whCode);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> getWarehouseList() {
        return ResponseEntity.ok(warehouseService.getWarehouseList());
    }
}
