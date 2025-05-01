package com.ppg.ems_server_side_v0.service.core.implementation;

import com.ppg.ems_server_side_v0.mapper.AddressMapper;
import com.ppg.ems_server_side_v0.domain.Address;
import com.ppg.ems_server_side_v0.mapper.InterviewMapper;
import com.ppg.ems_server_side_v0.model.api.request.AddressDTO;
import com.ppg.ems_server_side_v0.model.api.response.AddressResponse;
import com.ppg.ems_server_side_v0.repository.AddressRepository;
import com.ppg.ems_server_side_v0.service.core.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    private final AddressMapper addressMapper;

    @Override
    public AddressResponse addAddress(AddressDTO addressDTO) {
        Address address = this.addressRepository.save(Address.builder().
                streetName(addressDTO.streetName()).
                zipCode(addressDTO.zipCode()).
                town(addressDTO.town()).
                build());
        return this.addressMapper.toAddress(address);
    }

    @Override
    public AddressResponse updateAddress(AddressDTO addressDTO, String id) {

        Address address = this.addressRepository.findById(id).orElse(null);
        address.setStreetName(addressDTO.streetName());
        address.setZipCode(addressDTO.zipCode());
        address.setState(addressDTO.state());
        address.setTown(addressDTO.town());
        return this.addressMapper.toAddress(this.addressRepository.save(address));
    }

    @Override
    public AddressResponse findAddressById(String id) {
        Address address = this.addressRepository.findById(id).orElseThrow(() -> new RuntimeException("address not found"));
        return this.addressMapper.toAddress(address);
    }

    @Override
    public void deleteAddressById(String id) {
        this.addressRepository.deleteById(id);
    }

    @Override
    public List<AddressResponse> findAllAddresses() {
        List<Address> addressesList = this.addressRepository.findAll();
        return this.addressMapper.toAddressesResponseList(addressesList);
    }
}
