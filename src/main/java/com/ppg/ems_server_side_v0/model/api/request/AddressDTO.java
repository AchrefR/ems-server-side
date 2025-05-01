package com.ppg.ems_server_side_v0.model.api.request;

public record AddressDTO(

        String streetName,

        String zipCode,

        String state,

        String town
) {}
