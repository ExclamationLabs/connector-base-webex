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

package com.exclamationlabs.connid.base.webex;

import com.exclamationlabs.connid.base.connector.BaseConnector;
import com.exclamationlabs.connid.base.connector.attribute.ConnectorAttributeMapBuilder;
import com.exclamationlabs.connid.base.connector.authenticator.OAuth2TokenRefreshTokenAuthenticator;
import com.exclamationlabs.connid.base.webex.adapter.WebexGroupsAdapter;
import com.exclamationlabs.connid.base.webex.adapter.WebexUsersAdapter;
import com.exclamationlabs.connid.base.webex.attribute.WebexGroupAttribute;
import com.exclamationlabs.connid.base.webex.attribute.WebexUserAttribute;
import com.exclamationlabs.connid.base.webex.configuration.WebexConfiguration;
import com.exclamationlabs.connid.base.webex.driver.rest.WebexDriver;
import com.exclamationlabs.connid.base.webex.model.WebexGroup;
import com.exclamationlabs.connid.base.webex.model.WebexUser;
import org.identityconnectors.framework.spi.ConnectorClass;

import static com.exclamationlabs.connid.base.connector.attribute.ConnectorAttributeDataType.*;

import static com.exclamationlabs.connid.base.webex.attribute.WebexUserAttribute.*;
import static com.exclamationlabs.connid.base.webex.attribute.WebexGroupAttribute.*;
import static org.identityconnectors.framework.common.objects.AttributeInfo.Flags.*;

@ConnectorClass(displayNameKey = "webex.connector.display", configurationClass = WebexConfiguration.class)
public class WebexConnector extends BaseConnector<WebexUser, WebexGroup> {

    public WebexConnector() {

        setAuthenticator(new OAuth2TokenRefreshTokenAuthenticator());
        setDriver(new WebexDriver());
        setUsersAdapter(new WebexUsersAdapter());
        setGroupsAdapter(new WebexGroupsAdapter());
        setUserAttributes( new ConnectorAttributeMapBuilder<>(WebexUserAttribute.class)
                .add(USER_ID, STRING, NOT_UPDATEABLE)
                .add(FIRST_NAME, STRING)
                .add(LAST_NAME, STRING)
                .add(DISPLAY_NAME, STRING)
                .add(EMAILS, STRING, MULTIVALUED)
                .add(ORG_ID, STRING)
                .add(AVATAR, STRING)
                .add(CREATED, STRING, NOT_CREATABLE, NOT_UPDATEABLE)
                .add(LAST_MODIFIED, STRING, NOT_CREATABLE, NOT_UPDATEABLE)
                .add(LAST_ACTIVITY, STRING, NOT_CREATABLE, NOT_UPDATEABLE)
                .add(TIMEZONE, STRING, NOT_CREATABLE, NOT_UPDATEABLE)
                .add(NICKNAME, STRING, NOT_CREATABLE, NOT_UPDATEABLE)
                .add(STATUS, STRING, NOT_CREATABLE, NOT_UPDATEABLE)
                .add(TYPE, STRING, NOT_CREATABLE, NOT_UPDATEABLE)
                .add(ROLES, STRING, MULTIVALUED)
                .add(LICENSES, STRING, MULTIVALUED)
                .add(PHONE_NUMBERS, STRING, NOT_CREATABLE, NOT_UPDATEABLE, MULTIVALUED)
                .add(INVITE_PENDING, BOOLEAN, NOT_CREATABLE, NOT_UPDATEABLE)
                .add(LOGIN_ENABLED, BOOLEAN, NOT_CREATABLE, NOT_UPDATEABLE)
                .build());
        setGroupAttributes(new ConnectorAttributeMapBuilder<>(WebexGroupAttribute.class)
                .add(GROUP_ID, STRING, NOT_UPDATEABLE)
                .add(GROUP_NAME, STRING, REQUIRED)
                .build());
    }
}
