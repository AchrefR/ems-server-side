package com.ppg.ems_server_side_v0.controller;

import com.ppg.ems_server_side_v0.model.api.request.AddressDTO;
import com.ppg.ems_server_side_v0.model.api.response.AddressResponse;
import com.ppg.ems_server_side_v0.service.core.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
@Slf4j
public class AddressController {
    
    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressResponse> addAddress(@RequestBody AddressDTO addressDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(addressService.addAddress(addressDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressResponse> findAddressById(@PathVariable String id) {
        return ResponseEntity.ok(addressService.findAddressById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressResponse> updateAddress(@PathVariable String id, @RequestBody AddressDTO addressDTO) {
        return ResponseEntity.ok(addressService.updateAddress(addressDTO, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable String id) {
        addressService.deleteAddressById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<AddressResponse>> findAllAddresses() {
        return ResponseEntity.ok(addressService.findAllAddresses());
    }
}