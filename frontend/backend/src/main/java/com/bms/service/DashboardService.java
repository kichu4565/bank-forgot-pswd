package com.bms.service;

import com.bms.dto.DashboardResponse;
import com.bms.exception.AccountNotFoundException;

public interface DashboardService {
    /**
     * Get the account dashboard for a given account number
     * @param accountNumber The account number
     * @return DashboardResponse containing account details, summary, and recent transactions
     * @throws AccountNotFoundException if the account is not found
     */
    DashboardResponse getAccountDashboard(String accountNumber) throws AccountNotFoundException;
} 