package com.ppg.ems_server_side_v0.model.api.response;

public record AddressResponse(

        String addressId,

        String streetName,

        String zipCode,

        String state,

        String town
) {}
