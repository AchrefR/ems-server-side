package com.ppg.ems_server_side_v0.model.api.response;


import com.ppg.ems_server_side_v0.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String token ;

    private User user;
}
