package com.ppg.ems_server_side_v0.model.api.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public record AuthenticationDTO(

        String email,

        String password

) {}
