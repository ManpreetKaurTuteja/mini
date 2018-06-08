package com.miniapp.home.models;

import com.miniapp.models.UserDetails;

import java.util.List;

/**
 * Created by mac on 30/04/18.
 */

public class AllUsersProposals {
    int status;
    int total_page;
    String message;
    List<ProposalDetails> proposals_list;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotal_page() {
        return total_page;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ProposalDetails> getProposals_list() {
        return proposals_list;
    }

    public void setProposals_list(List<ProposalDetails> proposals_list) {
        this.proposals_list = proposals_list;
    }
}
