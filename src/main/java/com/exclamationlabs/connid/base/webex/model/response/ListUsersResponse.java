package com.exclamationlabs.connid.base.webex.model.response;

import com.exclamationlabs.connid.base.webex.model.WebexUser;

import java.util.List;

public class ListUsersResponse {

    private List<WebexUser> items;

    public List<WebexUser> getItems() {
        return items;
    }

    public void setItems(List<WebexUser> items) {
        this.items = items;
    }
}
