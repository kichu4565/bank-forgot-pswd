// package com.bms.dto;

// import lombok.Getter;
// import lombok.Setter;

// @Getter
// @Setter
// public class ResetPasswordRequest {
//     private String accountNumber;
//     private String newPassword;
// }
package com.bms.dto;

public class ResetPasswordRequest {
    private String accountNumber;
    private String newPassword;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

