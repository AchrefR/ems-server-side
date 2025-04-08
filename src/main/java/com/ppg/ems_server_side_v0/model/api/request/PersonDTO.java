package com.ppg.ems_server_side_v0.model.api.request;

public record PersonDTO(

        String firstName,

        String lastName,

        String birthDate,

        String phoneNumber,

        String personType,

        AddressDTO addressDTO
) {}
