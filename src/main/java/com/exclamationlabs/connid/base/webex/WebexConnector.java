package com.exclamationlabs.connid.base.webex;

import com.exclamationlabs.connid.base.connector.BaseConnector;
import com.exclamationlabs.connid.base.connector.attribute.ConnectorAttributeMapBuilder;
import com.exclamationlabs.connid.base.connector.authenticator.Authenticator;
import com.exclamationlabs.connid.base.connector.configuration.BaseConnectorConfiguration;
import com.exclamationlabs.connid.base.connector.configuration.ConnectorProperty;
import com.exclamationlabs.connid.base.webex.adapter.WebexGroupsAdapter;
import com.exclamationlabs.connid.base.webex.adapter.WebexUsersAdapter;
import com.exclamationlabs.connid.base.webex.attribute.WebexGroupAttribute;
import com.exclamationlabs.connid.base.webex.attribute.WebexUserAttribute;
import com.exclamationlabs.connid.base.webex.configuration.WebexConfiguration;
import com.exclamationlabs.connid.base.webex.driver.rest.WebexDriver;
import com.exclamationlabs.connid.base.webex.model.WebexGroup;
import com.exclamationlabs.connid.base.webex.model.WebexUser;
import org.identityconnectors.framework.common.exceptions.ConnectorSecurityException;
import org.identityconnectors.framework.spi.ConnectorClass;

import java.util.Set;

import static com.exclamationlabs.connid.base.connector.attribute.ConnectorAttributeDataType.*;

import static com.exclamationlabs.connid.base.webex.attribute.WebexUserAttribute.*;
import static com.exclamationlabs.connid.base.webex.attribute.WebexGroupAttribute.*;
import static org.identityconnectors.framework.common.objects.AttributeInfo.Flags.*;

@ConnectorClass(displayNameKey = "webex.connector.display", configurationClass = WebexConfiguration.class)
public class WebexConnector extends BaseConnector<WebexUser, WebexGroup> {

    public WebexConnector() {

        setAuthenticator(new Authenticator() {
            @Override
            public Set<ConnectorProperty> getRequiredPropertyNames() {
                return null;
            }

            @Override
            public String authenticate(BaseConnectorConfiguration baseConnectorConfiguration) throws ConnectorSecurityException {
                return "MjllOTllZjMtODdkMy00NWNhLWI5YTUtOWY0YjM5OWJiMGUzNGY0ZWY1YTItZjYy_P0A1_176a65f5-8855-4a07-a005-1ab2959c9962";
            }
        });
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
