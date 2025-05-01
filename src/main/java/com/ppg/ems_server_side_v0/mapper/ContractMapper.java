package com.ppg.ems_server_side_v0.mapper;

import com.ppg.ems_server_side_v0.domain.Contract;
import com.ppg.ems_server_side_v0.model.api.response.ContractResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ContractMapper {

    public ContractResponse toContractResponse(Contract contract) {
        return new ContractResponse(
                contract.getId(),
                contract.getContactNumber()
        );
    }

    public List<Contract> toContactList(List<Contract> contractList) {
        List<ContractResponse> contractListResponse = new ArrayList<>();
        contractList.forEach(contract -> {
            contractListResponse.add(toContractResponse(contract));
        });
        return contractList;
    }
}
