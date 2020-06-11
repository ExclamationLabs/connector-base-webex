package com.exclamationlabs.connid.base.webex.model;

import com.ciscospark.Person;
import com.exclamationlabs.connid.base.connector.model.UserIdentityModel;

import java.util.List;

public class WebexUser implements UserIdentityModel {

    private Person person;

    public WebexUser() {}

    public WebexUser(Person in) {
        person = in;
    }

    @Override
    public String getAssignedGroupsAttributeName() {
        return null;
    }

    @Override
    public List<String> getAssignedGroupIds() {
        return null;
    }

    @Override
    public String getIdentityIdValue() {
        return person.getId();
    }

    @Override
    public String getIdentityNameValue() {
        return person.getDisplayName();
    }

    public Person getPerson() {
        return person;
    }
}
