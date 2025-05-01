package com.ppg.ems_server_side_v0.service.core;

import com.ppg.ems_server_side_v0.model.api.request.AddressDTO;
import com.ppg.ems_server_side_v0.model.api.response.AddressResponse;

import java.util.List;

public interface AddressService {

    AddressResponse addAddress(AddressDTO addressDTO);

    AddressResponse updateAddress(AddressDTO addressDTO, String id);

    void deleteAddressById(String id);

    AddressResponse findAddressById(String id);

    List<AddressResponse> findAllAddresses();


}
