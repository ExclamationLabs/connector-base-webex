/*
    Copyright 2020 Exclamation Labs
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
        http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.exclamationlabs.connid.base.webex.driver.rest;

import com.exclamationlabs.connid.base.connector.driver.DriverInvocator;
import com.exclamationlabs.connid.base.webex.model.WebexUser;
import com.exclamationlabs.connid.base.webex.model.response.ListUsersResponse;
import org.identityconnectors.framework.common.exceptions.ConnectorException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class WebexUserInvocator implements DriverInvocator<WebexDriver, WebexUser> {

    @Override
    public String create(WebexDriver driver, WebexUser webexUser) throws ConnectorException {
        WebexUser newUser = driver.executePostRequest("/people",
                WebexUser.class, webexUser).getResponseObject();
        if (newUser == null || newUser.getId() == null) {
            throw new ConnectorException("Response from user creation was invalid");
        }

        return newUser.getId();
    }

    @Override
    public void update(WebexDriver driver, String userId, WebexUser modifiedUser) throws ConnectorException {
        // Webex requires all fields be present in update, whether they were altered or not
        WebexUser userToUpdate = getOne(driver, userId, Collections.emptyMap());
        userToUpdate.prepareForUpdate(modifiedUser);
        driver.executePutRequest("/people/" + userId, null, userToUpdate);
    }

    @Override
    public void delete(WebexDriver driver, String userId) throws ConnectorException {
        driver.executeDeleteRequest("/people/" + userId, null);
    }

    @Override
    public List<WebexUser> getAll(WebexDriver driver, Map<String, Object> map) throws ConnectorException {
        ListUsersResponse response = driver.executeGetRequest("/people",
                ListUsersResponse.class).getResponseObject();
        return response.getItems();
    }

    @Override
    public WebexUser getOne(WebexDriver driver, String userId, Map<String, Object> map) throws ConnectorException {
        return driver.executeGetRequest("/people/" + userId,
                WebexUser.class).getResponseObject();
    }
}
