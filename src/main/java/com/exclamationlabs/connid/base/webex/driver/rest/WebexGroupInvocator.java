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
import com.exclamationlabs.connid.base.connector.results.ResultsFilter;
import com.exclamationlabs.connid.base.connector.results.ResultsPaginator;
import com.exclamationlabs.connid.base.webex.model.WebexGroup;
import com.exclamationlabs.connid.base.webex.model.response.ListGroupsResponse;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.exceptions.PermissionDeniedException;

import java.util.Map;
import java.util.Set;

public class WebexGroupInvocator implements DriverInvocator<WebexDriver, WebexGroup> {

    @Override
    public String create(WebexDriver webexDriver, WebexGroup webexGroup) throws ConnectorException {
        throw new PermissionDeniedException("Webex does not allow creation of roles.");
    }

    @Override
    public void update(WebexDriver webexDriver, String s, WebexGroup webexGroup) throws ConnectorException {
        throw new PermissionDeniedException("Webex does not allow modification of roles.");
    }

    @Override
    public void delete(WebexDriver webexDriver, String s) throws ConnectorException {
        throw new PermissionDeniedException("Webex does not allow deletion of roles.");
    }

    @Override
    public Set<WebexGroup> getAll(WebexDriver driver, ResultsFilter filter,
                                  ResultsPaginator paginator, Integer max) throws ConnectorException {
        ListGroupsResponse response = driver.executeGetRequest("/roles",
                ListGroupsResponse.class).getResponseObject();
        return response.getItems();
    }

    @Override
    public WebexGroup getOne(WebexDriver driver, String groupId, Map<String, Object> map) throws ConnectorException {
        return driver.executeGetRequest("/roles/" + groupId,
                WebexGroup.class).getResponseObject();
    }
}
