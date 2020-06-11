package com.exclamationlabs.connid.base.webex;

import com.exclamationlabs.connid.base.connector.BaseConnector;
import com.exclamationlabs.connid.base.connector.attribute.ConnectorAttributeMapBuilder;
import com.exclamationlabs.connid.base.webex.adapter.WebexGroupsAdapter;
import com.exclamationlabs.connid.base.webex.adapter.WebexUsersAdapter;
import com.exclamationlabs.connid.base.webex.attribute.WebexGroupAttribute;
import com.exclamationlabs.connid.base.webex.attribute.WebexUserAttribute;
import com.exclamationlabs.connid.base.webex.configuration.WebexConfiguration;
import com.exclamationlabs.connid.base.webex.driver.WebexDriver;
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

        //setAuthenticator(new JWTAuthenticator());
        setDriver(new WebexDriver());
        setUsersAdapter(new WebexUsersAdapter());
        setGroupsAdapter(new WebexGroupsAdapter());
        setUserAttributes( new ConnectorAttributeMapBuilder<>(WebexUserAttribute.class)
                .add(USER_ID, STRING, NOT_UPDATEABLE)
                .add(FIRST_NAME, STRING)
                .add(LAST_NAME, STRING)
                .add(DISPLAY_NAME, STRING)
                .add(EMAILS, STRING, MULTIVALUED)
                .build());
        setGroupAttributes(new ConnectorAttributeMapBuilder<>(WebexGroupAttribute.class)
                .add(GROUP_ID, STRING, NOT_UPDATEABLE)
                .add(GROUP_NAME, STRING, REQUIRED)
                .add(CREATED_DATE, STRING, NOT_UPDATEABLE)
                .build());
    }
}
