package com.ppg.ems_server_side_v0.model.api.response;

public record PersonResponse(

        String personId,

        String firstName,

        String lastName,

        String birthDate,

        String phoneNumber,

        String personType,

        String streetName,

        String zipCode,

        String state,

        String town



) {
}
