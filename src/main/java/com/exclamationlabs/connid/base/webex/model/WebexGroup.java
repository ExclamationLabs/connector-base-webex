package com.exclamationlabs.connid.base.webex.model;

import com.ciscospark.Team;
import com.exclamationlabs.connid.base.connector.model.GroupIdentityModel;

public class WebexGroup implements GroupIdentityModel {

    private Team team;

    public WebexGroup() {}

    public WebexGroup(Team teamIn) {
        team = teamIn;
    }

    @Override
    public String getIdentityIdValue() {
        return team.getId();
    }

    @Override
    public String getIdentityNameValue() {
        return team.getName();
    }

    public Team getTeam() {
        return team;
    }

}
