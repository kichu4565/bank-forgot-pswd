package com.bms.service;

import com.bms.dto.FDCreationRequest;
import com.bms.dto.FDCreationResponse;
import com.bms.dto.FDDetailsResponse;
import java.util.List;

public interface FixedDepositService {
    FDCreationResponse createFD(FDCreationRequest request);
    List<FDDetailsResponse> getFDsByAccount(String accountNumber);
    FDDetailsResponse getFDDetails(String fdAccountNumber);
    void processMaturedFDs();
} 