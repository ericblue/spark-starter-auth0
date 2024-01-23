## About This Project

Spark-Starter-Auth0 - A basic starter project for Spark Java (v2.9.4) integrating with Auth0

This starter web application based on Spark-Starter-Basic, written in Java using the Spark (https://sparkjava.com/) framework provides a simple project template to get started quickly with a new Spark project.

This project illustrates the following:

- Authentication with Auth0 (https://auth0.com/)

- Basic Domain objects 

- Sample service and controller classes

- Dynamic error and exception handling for web views and JSON responses

- Log4J logging configuration

- Maven configuration to support Snyk (https://snyk.io/) for security vulnerability scanning

- Simple support for Swagger API documentations using annotations and JAX RS
  - Avoids more complex 3rd party libraries and projects
  - Builds on examples from 2016, but still leverage lambda expressions for routes


### Configuration - Environment Variables

This app supports loading a single environment variable 'environment' (values = 'development', 'test', 'production')

If no environment variable is set, the default is 'production'

### Configuration - Auth 0

#### Auth0 Dashboard

1. On the Auth0 Dashboard create a new Application of type Regular Web Application.
2. On the Settings tab of your application, add the URL http://localhost:3000/callback to the Allowed Callback URLs field.
3. On the Settings tab of your application, add the URL http://localhost:3000/login to the Allowed Logout URLs field.
4. Save the changes to your application settings. Don't close this page; you'll need some of the settings when configuring the application below.

Note: Recent changes to Auth0 may require login and callback URLs to be https instead of http.  If you are running locally, you can use a tool like ngrok (https://ngrok.com/) to create a secure tunnel to your local server.

#### Auth0 Application configuration

Set the Auth0 Application values from above in the src/main/resources/application.{environment}.properties file.

```
# Auth0 Client ID
auth0-client-id={YOUR-CLIENT-ID}
# Auth0 Client Secret
auth0-client-secret={YOUR-CLIENT-SECRET}
# Auth0 Domain
auth0-issuer-uri=https://{YOUR-DOMAIN}/
# Callback URL - Defined in Auth0 dashboard, e.g http://localhost:3000/callback
# Note: now requires https, an external endpoints is needed (e.g. ngrok)
auth0-callback-url=https://localhost:3000/callback
```

#### Auth0 Role support

This app demonstrates retrieving role information from Auth0 via the Management API v2.  To enable this feature, you will need to create or edit hte default Auth0 API and setup as follows:


Note: Support for getting back role information from the JWT claims is not yet implemented.

- To enable Roles, go to User Management -> Roles and create a new role
- Assign the role to a user by going to Users -> <user> -> Roles and click Assign Roles
- Go to Applications -> APIs -> Auth0 Management API -> Machine to Machine Applications -> <your app> -> Toggle 'Authorized'
- Expand the app and go under Permissions -> Enable the 'read:users', 'read:roles', 'read:role_members' and 'read:organization_member_roles' permissions

#### Auth0 Libraries

Note: the current Java examples for Servlets seem to be slightly dated using https://github.com/auth0/auth0-java-mvc-common
and 1.x versions of the Auth0 libraries.

Additionally, since this project needed full control over enabling Auth0 with Spark controllers, the latest 2.x versions
of the Auth0 libraries are used - https://github.com/auth0/auth0-java


## Development Environment


### Running the app

When running from the command line and maven, ```mvn clean install``` will create a jar file in the target directory.

To start the app, run:

```
export environment=development
java -jar target/spark-starter-auth0-1.0.0.jar
```


## Swagger

Swagger endpoints are available locally at http://localhost:3000/swagger-ui/


## Author

This app was created by Eric Blue - [https://eric-blue.com](https://eric-blue.com/)
