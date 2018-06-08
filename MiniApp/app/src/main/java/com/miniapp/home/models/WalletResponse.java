package com.miniapp.home.models;

import java.util.List;

/**
 * Created by mac on 07/04/18.
 */

public class WalletResponse {
    int status;
    String message;
    String balance;
    List<WalletHistory> historyList;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
