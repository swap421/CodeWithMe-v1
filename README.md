1. Video Explanation for app flow
https://github.com/swap421/CodeWithMe-v1/assets/87779175/b2907d96-48a5-43d0-a868-93967b4e9741

2. OAuth 2.0 Implementation in CodeWithMe
        Overview:
        OAuth 2.0 is a widely-used authorization framework that allows third-party applications to obtain limited access to an HTTP service, such as Google APIs,
        on behalf of a user without exposing their credentials.
        
        Implementation Steps:
        
        App Registration:
        
        Register CodeWithMe with the OAuth 2.0 provider (e.g., Google Cloud Console).
        Obtain a client ID and client secret, which are used to authenticate your app with the provider.
        Authorization Request:
        
        When a user wishes to access CodeWithMe, then CodeWithMe redirects them to the provider's authorization server (Google’s OAuth endpoint).
        User Consent:
        
        The user is prompted to grant permissions that CodeWithMe requests.
        Authorization Grant:
        
        Upon user consent, the provider issues an authorization code to CodeWithMe's redirect URI.
        Token Exchange:
        
        CodeWithMe exchanges the authorization code for an access token and optionally a refresh token by making a POST request to the provider’s token endpoint.
        Accessing Protected Resources:
        
        CodeWithMe includes the access token in API requests to the provider’s services.
        The provider verifies the token and grants access to the requested resources if valid.

3. WebSocket Implementation With Stomp.js in CodeWithMe
        Overview: WebSocket is a protocol that enables full-duplex communication between client and server. In this application, STOMP (Simple Text Oriented Messaging Protocol)
        is used over WebSocket to handle messaging.

        Setup Spring Boot WebSocket Configuration:

        Enable WebSocket message broker.
        Configure message broker with application prefixes and endpoints.
        Define a Message Handling Controller:
        
        Create a controller to handle messages from the client.
        Define methods to process incoming messages and broadcast them to subscribers.
   
        Front-End Integration with STOMP.js:
        
        Use STOMP.js to connect to the WebSocket endpoint.
        Subscribe to specific topics and handle incoming messages.
        Send messages to the server using defined endpoints.

