#connector-h2-webex
### Webex Connector based on Connector Base framework

This software is Copyright 2020 Exclamation Labs.  Licensed under the Apache License, Version 2.0.

### Developer Account Setup

Go to webex.com and create a free Webex account
        
Now you need to have a second account (using different email, probably
your personal email) in order to have true User Admin privileges.  In order to do so, you will need
to email devsupport@webex.com (from your main email account) requesting sandbox access
for developer integration.  In this email, you will need to give them the secondary(personal) email
address you wish to use to administer users. 

Using this second account, after walking through a few screens, including creating a subdomain, 
you can now get to the Admin Control Hub at https://admin.webex.com/overview.

Now you can setup an integration, using your secondary email address.

- Create a new Integration - https://developer.webex.com/my-apps/new/integration
- Fill in an Integration name, email, choose an icon and select an icon
- Redirect URIs - http://localhost
- Scopes - check the following
    - spark:all
    - spark-admin:organizations_read
    - spark-admin:people_read
    - spark-admin:people_write
    - spark-admin:resource_group_memberships_read
    - spark-admin:resource_group_memberships_write
    - spark-admin:resource_groups_read
    - spark-admin:roles_read
    - spark-compliance:memberships_read
    - spark-compliance:memberships_write
    - spark-compliance:team_memberships_read
    - spark-compliance:team_memberships_write
    - spark-compliance:teams_read
    - identity:placeonetimepassword_create
    - audit:events_read
  
- on next page, store OAuth ClientId, ClientSecret, OAuth AuthorizationURL,
IntegrationID in a safe place.  These will be needed as described in 
Configuration Properties below.
- Request permission - this is a manual browser step, invoke manually from browser
    - Use the OAuth Auth URL from your Integration, but change the state to a unique string of
    your choosing
    - Store the code value from the http://localhost response URL returned.  This is the
    Authorization Code.

### Configuration properties

- CONNECTOR_BASE_CONFIGURATION_ACTIVE - set to Y to use this configuration
- CONNECTOR_BASE_AUTH_OAUTH2_TOKEN_URL - likely use "https://webexapis.com/v1/access_token"
- CONNECTOR_BASE_AUTH_OAUTH2_AUTHORIZATION_CODE - use the Authorization Code obtained above.
- CONNECTOR_BASE_AUTH_OAUTH2_CLIENT_ID - use the ClientId from instructions above.
- CONNECTOR_BASE_AUTH_OAUTH2_CLIENT_SECRET - use the ClientSecret from instructions above.
- CONNECTOR_BASE_AUTH_OAUTH2_REDIRECT_URI - you can use "http://localhost" for this value
- CONNECTOR_BASE_AUTH_OAUTH2_REFRESH_TOKEN - see Authentication section below
for info on how to obtain RefreshToken from your Authorization Code

### Development testing/usage links:
- [Create and manage developer integrations](https://developer.webex.com)
- [Webex Admin portal - Control Hub](https://admin.webex.com/overview)
- [Admin API Documentation](https://developer.webex.com/docs/api/guides/admin-api)

### Authentication

OAuth2 process is described at https://developer.webex.com/docs/integrations

NOTE: Webex connector OAuth2 only supports OAuth2 Authorization Code and Refresh Token. 
An Authorization Code for Webex must be obtained manually, by copying an initial
OAuth URL manually into your browser, logging in using your development
account and then saving the Authorization Code in the response URL.

Once OAuth2 Authorization Code has been obtained, Connector Base
OAuth2TokenAuthorizationCodeAuthenticator should be executed in
order to obtain a RefreshToken, which is needed for Webex authentication
in this project (CONNECTOR_BASE_AUTH_OAUTH2_REFRESH_TOKEN).

from https://developer.webex.com/blog/real-world-walkthrough-of-building-an-oauth-webex-integration

There are four grant types in OAuth 2.0:

Authorization Code
Implicit
Resource Owner Password Credentials
Client Credentials
Webex Teams currently only supports Authorization Code, which is a grant flow used mostly with web applications to get access to an API.

