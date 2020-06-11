package com.exclamationlabs.connid.base.webex.configuration;

import com.exclamationlabs.connid.base.connector.authenticator.JWTAuthenticator;
import com.exclamationlabs.connid.base.connector.configuration.BaseConnectorConfiguration;
import org.identityconnectors.framework.spi.ConfigurationProperty;

public class WebexConfiguration extends BaseConnectorConfiguration {

    public WebexConfiguration() {
        super();
    }

    @Override
    @ConfigurationProperty(
            displayMessageKey = "Webex Configuration File Path",
            helpMessageKey = "File path for the Webex Configuration File",
            required = true)
    public String getConfigurationFilePath() {
        return getMidPointConfigurationFilePath();
    }
}
