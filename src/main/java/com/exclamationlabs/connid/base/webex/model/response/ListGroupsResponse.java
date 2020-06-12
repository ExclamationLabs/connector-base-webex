package com.exclamationlabs.connid.base.webex.model.response;

import com.exclamationlabs.connid.base.webex.model.WebexGroup;

import java.util.List;

public class ListGroupsResponse {

    private List<WebexGroup> items;

    public List<WebexGroup> getItems() {
        return items;
    }

    public void setItems(List<WebexGroup> items) {
        this.items = items;
    }
}
