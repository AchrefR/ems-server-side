package com.ppg.ems_server_side_v0.mapper;

import com.ppg.ems_server_side_v0.domain.Address;
import com.ppg.ems_server_side_v0.model.api.response.AddressResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AddressMapper {

    public AddressResponse toAddress(Address address) {
        return new AddressResponse(
                address.getId(),
                address.getStreetName(),
                address.getZipCode(),
                address.getState(),
                address.getTown()
        );
    }

    public List<AddressResponse> toAddressesResponseList(List<Address> addresses) {
        List<AddressResponse> addressesResponseList = new ArrayList<>();
        addresses.forEach(address -> {
            addressesResponseList.add(toAddress(address));
        });
        return addressesResponseList;
    }

}
