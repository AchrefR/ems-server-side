package com.ppg.ems_server_side_v0.controller;

import com.ppg.ems_server_side_v0.model.api.request.AddressDTO;
import com.ppg.ems_server_side_v0.model.api.response.AddressResponse;
import com.ppg.ems_server_side_v0.service.core.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressResponse> createAddress(@RequestBody AddressDTO addressDTO) {
        return new ResponseEntity<>(addressService.addAddress(addressDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressResponse> updateAddress(@RequestBody AddressDTO addressDTO, 
                                                       @PathVariable String id) {
        return ResponseEntity.ok(addressService.updateAddress(addressDTO, id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressResponse> getAddressById(@PathVariable String id) {
        return ResponseEntity.ok(addressService.findAddressById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable String id) {
        addressService.deleteAddressById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<AddressResponse>> getAllAddresses() {
        return ResponseEntity.ok(addressService.findAllAddresses());
    }
}