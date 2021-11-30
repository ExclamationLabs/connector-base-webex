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

import com.exclamationlabs.connid.base.connector.driver.rest.BaseRestDriver;
import com.exclamationlabs.connid.base.connector.driver.rest.RestFaultProcessor;
import com.exclamationlabs.connid.base.webex.configuration.WebExConfiguration;
import com.exclamationlabs.connid.base.webex.model.WebexGroup;
import com.exclamationlabs.connid.base.webex.model.WebexUser;
import org.identityconnectors.framework.common.exceptions.ConnectorException;

public class WebexDriver extends BaseRestDriver<WebExConfiguration> {

    public WebexDriver() {
        super();
        addInvocator(WebexUser.class, new WebexUserInvocator());
        addInvocator(WebexGroup.class, new WebexGroupInvocator());
    }

    @Override
    protected RestFaultProcessor getFaultProcessor() {
        return WebexFaultProcessor.getInstance();
    }

    @Override
    protected String getBaseServiceUrl() {
        return getConfiguration().getServiceUrl();
    }

    @Override
    protected boolean usesBearerAuthorization() {
        return true;
    }

    @Override
    public void test() throws ConnectorException {
        try {
            executeGetRequest("/organizations", null);
        } catch (Exception e) {
            throw new ConnectorException("Test listing of organizations for Webex connection failed.", e);
        }

    }

    @Override
    public void close() {
        configuration = null;
        authenticator = null;
    }

}
