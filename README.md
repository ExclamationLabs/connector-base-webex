NOTE:

Webex connector OAuth2 limited to web re-auth once every few weeks.  It would take some additional
back-end batch programming to possibly try to simulate a user invoking authorization code URL
in browser - if even possible - and store the updated value in configuration
in order to keep this Connector's auth perpetually authenticated.


from https://developer.webex.com/blog/real-world-walkthrough-of-building-an-oauth-webex-integration

There are four grant types in OAuth 2.0:

Authorization Code
Implicit
Resource Owner Password Credentials
Client Credentials
Webex Teams currently only supports Authorization Code, which is a grant flow used mostly with web applications to get access to an API.

