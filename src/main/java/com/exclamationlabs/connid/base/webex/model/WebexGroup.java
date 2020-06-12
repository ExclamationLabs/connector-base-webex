package com.exclamationlabs.connid.base.webex.model;

import com.exclamationlabs.connid.base.connector.model.GroupIdentityModel;

public class WebexGroup implements GroupIdentityModel {

    private String id;
    private String name;

    @Override
    public String getIdentityIdValue() {
        return getId();
    }

    @Override
    public String getIdentityNameValue() {
        return getName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
