package com.exclamationlabs.connid.base.webex.driver;

import com.ciscospark.*;
import com.exclamationlabs.connid.base.connector.authenticator.Authenticator;
import com.exclamationlabs.connid.base.connector.configuration.BaseConnectorConfiguration;
import com.exclamationlabs.connid.base.connector.configuration.ConnectorProperty;
import com.exclamationlabs.connid.base.connector.driver.Driver;
import com.exclamationlabs.connid.base.webex.model.WebexGroup;
import com.exclamationlabs.connid.base.webex.model.WebexUser;
import org.apache.commons.lang3.StringUtils;
import org.identityconnectors.framework.common.exceptions.ConnectorException;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WebexDriver implements Driver<WebexUser, WebexGroup> {

    private BaseConnectorConfiguration configuration;

    @Override
    public Set<ConnectorProperty> getRequiredPropertyNames() {
        return null;
    }

    @Override
    public void initialize(BaseConnectorConfiguration baseConnectorConfiguration, Authenticator authenticator) throws ConnectorException {

    }

    @Override
    public void test() throws ConnectorException {

    }

    @Override
    public void close() {

    }

    @Override
    public String createUser(WebexUser webexUser) throws ConnectorException {
        Person newUser = null;
        try {
            newUser = setupSpark()
                    .people()
                    .post(webexUser.getPerson());
        } catch (SparkException sparkE) {
            handleSparkException(sparkE);
        }
        return newUser != null ? newUser.getId() : null;
    }

    @Override
    public String createGroup(WebexGroup webexGroup) throws ConnectorException {
        Team newTeam = null;
        try {
            newTeam = setupSpark()
                    .teams()
                    .post(webexGroup.getTeam());
        } catch (SparkException sparkE) {
            handleSparkException(sparkE);
        }
        return newTeam != null ? newTeam.getId() : null;
    }

    @Override
    public void updateUser(String userId, WebexUser webexUser) throws ConnectorException {
        webexUser.getPerson().setId(userId);
        try {
            setupSpark()
                .people()
                .post(webexUser.getPerson());
        } catch (SparkException sparkE) {
            handleSparkException(sparkE);
        }
    }

    @Override
    public void updateGroup(String groupId, WebexGroup webexGroup) throws ConnectorException {
        webexGroup.getTeam().setId(groupId);
        try {
            setupSpark()
                    .teams()
                    .post(webexGroup.getTeam());
        } catch (SparkException sparkE) {
            handleSparkException(sparkE);
        }
    }

    @Override
    public void deleteUser(String userId) throws ConnectorException {
        try {
            setupSpark()
                    .people()
                    .path("/" + userId)
                    .delete();
        } catch (SparkException sparkE) {
            handleSparkException(sparkE);
        }
    }

    @Override
    public void deleteGroup(String groupId) throws ConnectorException {
        try {
            setupSpark()
                    .teams()
                    .path("/" + groupId)
                    .delete();
        } catch (SparkException sparkE) {
            handleSparkException(sparkE);
        }
    }

    @Override
    public List<WebexUser> getUsers() throws ConnectorException {
        List<WebexUser> users = new ArrayList<>();
        try {
            setupSpark()
                    .people()
                    .iterate()
                    .forEachRemaining(item -> users.add(new WebexUser(item)));
        } catch (SparkException sparkE) {
            handleSparkException(sparkE);
        }
        return users;
    }

    @Override
    public List<WebexGroup> getGroups() throws ConnectorException {
        List<WebexGroup> groups = new ArrayList<>();
        try {
            setupSpark()
                    .teams()
                    .iterate()
                    .forEachRemaining(team -> groups.add(new WebexGroup(team)));
        } catch (SparkException sparkE) {
            handleSparkException(sparkE);
        }
        return groups;
    }

    @Override
    public WebexUser getUser(String userId) throws ConnectorException {
        WebexUser user = null;
        try {
            Person person = setupSpark()
                    .people()
                    .path("/" + userId)
                    .get();
            user = new WebexUser(person);
        } catch (SparkException sparkE) {
            handleSparkException(sparkE);
        }
        return user;
    }

    @Override
    public WebexGroup getGroup(String groupId) throws ConnectorException {
        WebexGroup group = null;
        try {
            Team team = setupSpark()
                    .teams()
                    .path("/" + groupId)
                    .get();
            group = new WebexGroup(team);
        } catch (SparkException sparkE) {
            handleSparkException(sparkE);
        }
        return group;
    }

    @Override
    public void addGroupToUser(String s, String s1) throws ConnectorException {

    }

    @Override
    public void removeGroupFromUser(String s, String s1) throws ConnectorException {

    }

    private Spark setupSpark() {
        return Spark.builder()
                .baseUrl(URI.create("https://webexapis.com/v1"))
                //.accessToken(configuration.getCredentialAccessToken())
                .accessToken(
                        "MjllOTllZjMtODdkMy00NWNhLWI5YTUtOWY0YjM5OWJiMGUzNGY0ZWY1YTItZjYy_P0A1_176a65f5-8855-4a07-a005-1ab2959c9962")
                .build();
    }

    private static void handleSparkException(SparkException sparkException) {
        if (sparkException instanceof NotAuthenticatedException) {
            // TODO: handle not Auth exception
        }

        if (StringUtils.containsIgnoreCase(sparkException.getMessage(), "404 Not Found")) {
            // TODO: not found
        }

        // 403 Forbidden

        throw sparkException;
    }

}
