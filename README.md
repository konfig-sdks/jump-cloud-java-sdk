<div align="left">

[![Visit Jumpcloud](./header.png)](https://jumpcloud.com)

# [Jumpcloud](https://jumpcloud.com)

# Overview

JumpCloud's V2 API. This set of endpoints allows JumpCloud customers to manage objects, groupings and mappings and interact with the JumpCloud Graph.

## API Best Practices

Read the linked Help Article below for guidance on retrying failed requests to JumpCloud's REST API, as well as best practices for structuring subsequent retry requests. Customizing retry mechanisms based on these recommendations will increase the reliability and dependability of your API calls.

Covered topics include:
1. Important Considerations
2. Supported HTTP Request Methods
3. Response codes
4. API Key rotation
5. Paginating
6. Error handling
7. Retry rates

[JumpCloud Help Center - API Best Practices](https://support.jumpcloud.com/support/s/article/JumpCloud-API-Best-Practices)

# Directory Objects

This API offers the ability to interact with some of our core features; otherwise known as Directory Objects. The Directory Objects are:

* Commands
* Policies
* Policy Groups
* Applications
* Systems
* Users
* User Groups
* System Groups
* Radius Servers
* Directories: Office 365, LDAP,G-Suite, Active Directory
* Duo accounts and applications.

The Directory Object is an important concept to understand in order to successfully use JumpCloud API.

## JumpCloud Graph

We've also introduced the concept of the JumpCloud Graph along with  Directory Objects. The Graph is a powerful aspect of our platform which will enable you to associate objects with each other, or establish membership for certain objects to become members of other objects.

Specific `GET` endpoints will allow you to traverse the JumpCloud Graph to return all indirect and directly bound objects in your organization.

| ![alt text](https://s3.amazonaws.com/jumpcloud-kb/Knowledge+Base+Photos/API+Docs/jumpcloud_graph.png "JumpCloud Graph Model Example") |
|:--:|
| **This diagram highlights our association and membership model as it relates to Directory Objects.** |

# API Key

## Access Your API Key

To locate your API Key:

1. Log into the [JumpCloud Admin Console](https://console.jumpcloud.com/).
2. Go to the username drop down located in the top-right of the Console.
3. Retrieve your API key from API Settings.

## API Key Considerations

This API key is associated to the currently logged in administrator. Other admins will have different API keys.

**WARNING** Please keep this API key secret, as it grants full access to any data accessible via your JumpCloud console account.

You can also reset your API key in the same location in the JumpCloud Admin Console.

## Recycling or Resetting Your API Key

In order to revoke access with the current API key, simply reset your API key. This will render all calls using the previous API key inaccessible.

Your API key will be passed in as a header with the header name "x-api-key".

```bash
curl -H "x-api-key: [YOUR_API_KEY_HERE]" "https://console.jumpcloud.com/api/v2/systemgroups"
```

# System Context

* [Introduction](https://docs.jumpcloud.com)
* [Supported endpoints](https://docs.jumpcloud.com)
* [Response codes](https://docs.jumpcloud.com)
* [Authentication](https://docs.jumpcloud.com)
* [Additional examples](https://docs.jumpcloud.com)
* [Third party](https://docs.jumpcloud.com)

## Introduction

JumpCloud System Context Authorization is an alternative way to authenticate with a subset of JumpCloud's REST APIs. Using this method, a system can manage its information and resource associations, allowing modern auto provisioning environments to scale as needed.

**Notes:**

 * The following documentation applies to Linux Operating Systems only.
 * Systems that have been automatically enrolled using Apple's Device Enrollment Program (DEP) or systems enrolled using the User Portal install are not eligible to use the System Context API to prevent unauthorized access to system groups and resources. If a script that utilizes the System Context API is invoked on a system enrolled in this way, it will display an error.

## Supported Endpoints

JumpCloud System Context Authorization can be used in conjunction with Systems endpoints found in the V1 API and certain System Group endpoints found in the v2 API.

* A system may fetch, alter, and delete metadata about itself, including manipulating a system's Group and Systemuser associations,
  * `/api/systems/{system_id}` | [`GET`](https://docs.jumpcloud.com/api/1.0/index.html#operation/systems_get) [`PUT`](https://docs.jumpcloud.com/api/1.0/index.html#operation/systems_put)
* A system may delete itself from your JumpCloud organization
  * `/api/systems/{system_id}` | [`DELETE`](https://docs.jumpcloud.com/api/1.0/index.html#operation/systems_delete)
* A system may fetch its direct resource associations under v2 (Groups)
  * `/api/v2/systems/{system_id}/memberof` | [`GET`](https://docs.jumpcloud.com/api/2.0/index.html#operation/graph_systemGroupMembership)
  * `/api/v2/systems/{system_id}/associations` | [`GET`](https://docs.jumpcloud.com/api/2.0/index.html#operation/graph_systemAssociationsList)
  * `/api/v2/systems/{system_id}/users` | [`GET`](https://docs.jumpcloud.com/api/2.0/index.html#operation/graph_systemTraverseUser)
* A system may alter its direct resource associations under v2 (Groups)
  * `/api/v2/systems/{system_id}/associations` | [`POST`](https://docs.jumpcloud.com/api/2.0/index.html#operation/graph_systemAssociationsPost)
* A system may alter its System Group associations
  * `/api/v2/systemgroups/{group_id}/members` | [`POST`](https://docs.jumpcloud.com/api/2.0/index.html#operation/graph_systemGroupMembersPost)
    * _NOTE_ If a system attempts to alter the system group membership of a different system the request will be rejected

## Response Codes

If endpoints other than those described above are called using the System Context API, the server will return a `401` response.

## Authentication

To allow for secure access to our APIs, you must authenticate each API request.
JumpCloud System Context Authorization uses [HTTP Signatures](https://tools.ietf.org/html/draft-cavage-http-signatures-00) to authenticate API requests.
The HTTP Signatures sent with each request are similar to the signatures used by the Amazon Web Services REST API.
To help with the request-signing process, we have provided an [example bash script](https://github.com/TheJumpCloud/SystemContextAPI/blob/master/examples/shell/SigningExample.sh). This example API request simply requests the entire system record. You must be root, or have permissions to access the contents of the `/opt/jc` directory to generate a signature.

Here is a breakdown of the example script with explanations.

First, the script extracts the systemKey from the JSON formatted `/opt/jc/jcagent.conf` file.

```bash
#!/bin/bash
conf="`cat /opt/jc/jcagent.conf`"
regex="systemKey\":\"(\w+)\""

if [[ $conf =~ $regex ]] ; then
  systemKey="${BASH_REMATCH[1]}"
fi
```

Then, the script retrieves the current date in the correct format.

```bash
now=`date -u "+%a, %d %h %Y %H:%M:%S GMT"`;
```

Next, we build a signing string to demonstrate the expected signature format. The signed string must consist of the [request-line](https://tools.ietf.org/html/rfc2616#page-35) and the date header, separated by a newline character.

```bash
signstr="GET /api/systems/${systemKey} HTTP/1.1\ndate: ${now}"
```

The next step is to calculate and apply the signature. This is a two-step process:

1. Create a signature from the signing string using the JumpCloud Agent private key: ``printf "$signstr" | openssl dgst -sha256 -sign /opt/jc/client.key``
2. Then Base64-encode the signature string and trim off the newline characters: ``| openssl enc -e -a | tr -d '\n'``

The combined steps above result in:

```bash
signature=`printf "$signstr" | openssl dgst -sha256 -sign /opt/jc/client.key | openssl enc -e -a | tr -d '\n'` ;
```

Finally, we make sure the API call sending the signature has the same Authorization and Date header values, HTTP method, and URL that were used in the signing string.

```bash
curl -iq \
  -H "Accept: application/json" \
  -H "Content-Type: application/json" \
  -H "Date: ${now}" \
  -H "Authorization: Signature keyId=\"system/${systemKey}\",headers=\"request-line date\",algorithm=\"rsa-sha256\",signature=\"${signature}\"" \
  --url https://console.jumpcloud.com/api/systems/${systemKey}
```

### Input Data

All PUT and POST methods should use the HTTP Content-Type header with a value of 'application/json'. PUT methods are used for updating a record. POST methods are used to create a record.

The following example demonstrates how to update the `displayName` of the system.

```bash
signstr="PUT /api/systems/${systemKey} HTTP/1.1\ndate: ${now}"
signature=`printf "$signstr" | openssl dgst -sha256 -sign /opt/jc/client.key | openssl enc -e -a | tr -d '\n'` ;

curl -iq \
  -d "{\"displayName\" : \"updated-system-name-1\"}" \
  -X "PUT" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -H "Date: ${now}" \
  -H "Authorization: Signature keyId=\"system/${systemKey}\",headers=\"request-line date\",algorithm=\"rsa-sha256\",signature=\"${signature}\"" \
  --url https://console.jumpcloud.com/api/systems/${systemKey}
```

### Output Data

All results will be formatted as JSON.

Here is an abbreviated example of response output:

```json
{
  "_id": "625ee96f52e144993e000015",
  "agentServer": "lappy386",
  "agentVersion": "0.9.42",
  "arch": "x86_64",
  "connectionKey": "127.0.0.1_51812",
  "displayName": "ubuntu-1204",
  "firstContact": "2013-10-16T19:30:55.611Z",
  "hostname": "ubuntu-1204"
  ...
```

## Additional Examples

### Signing Authentication Example

This example demonstrates how to make an authenticated request to fetch the JumpCloud record for this system.

[SigningExample.sh](https://github.com/TheJumpCloud/SystemContextAPI/blob/master/examples/shell/SigningExample.sh)

### Shutdown Hook

This example demonstrates how to make an authenticated request on system shutdown.
Using an init.d script registered at run level 0, you can call the System Context API as the system is shutting down.

[Instance-shutdown-initd](https://github.com/TheJumpCloud/SystemContextAPI/blob/master/examples/instance-shutdown-initd) is an example of an init.d script that only runs at system shutdown.

After customizing the [instance-shutdown-initd](https://github.com/TheJumpCloud/SystemContextAPI/blob/master/examples/instance-shutdown-initd) script, you should install it on the system(s) running the JumpCloud agent.

1. Copy the modified [instance-shutdown-initd](https://github.com/TheJumpCloud/SystemContextAPI/blob/master/examples/instance-shutdown-initd) to `/etc/init.d/instance-shutdown`.
2. On Ubuntu systems, run `update-rc.d instance-shutdown defaults`. On RedHat/CentOS systems, run `chkconfig --add instance-shutdown`.

## Third Party

### Chef Cookbooks

[https://github.com/nshenry03/jumpcloud](https://github.com/nshenry03/jumpcloud)

[https://github.com/cjs226/jumpcloud](https://github.com/cjs226/jumpcloud)

# Multi-Tenant Portal Headers

Multi-Tenant Organization API Headers are available for JumpCloud Admins to use when making API requests from Organizations that have multiple managed organizations.

The `x-org-id` is a required header for all multi-tenant admins when making API requests to JumpCloud. This header will define to which organization you would like to make the request.

**NOTE** Single Tenant Admins do not need to provide this header when making an API request.

## Header Value

`x-org-id`

## API Response Codes

* `400` Malformed ID.
* `400` x-org-id and Organization path ID do not match.
* `401` ID not included for multi-tenant admin
* `403` ID included on unsupported route.
* `404` Organization ID Not Found.

```bash
curl -X GET https://console.jumpcloud.com/api/v2/directories \
  -H 'accept: application/json' \
  -H 'content-type: application/json' \
  -H 'x-api-key: {API_KEY}' \
  -H 'x-org-id: {ORG_ID}'

```

## To Obtain an Individual Organization ID via the UI

As a prerequisite, your Primary Organization will need to be setup for Multi-Tenancy. This provides access to the Multi-Tenant Organization Admin Portal.

1. Log into JumpCloud [Admin Console](https://console.jumpcloud.com). If you are a multi-tenant Admin, you will automatically be routed to the Multi-Tenant Admin Portal.
2. From the Multi-Tenant Portal's primary navigation bar, select the Organization you'd like to access.
3. You will automatically be routed to that Organization's Admin Console.
4. Go to Settings in the sub-tenant's primary navigation.
5. You can obtain your Organization ID below your Organization's Contact Information on the Settings page.

## To Obtain All Organization IDs via the API

* You can make an API request to this endpoint using the API key of your Primary Organization.  `https://console.jumpcloud.com/api/organizations/` This will return all your managed organizations.

```bash
curl -X GET \
  https://console.jumpcloud.com/api/organizations/ \
  -H 'Accept: application/json' \
  -H 'Content-Type: application/json' \
  -H 'x-api-key: {API_KEY}'
```

# SDKs

You can find language specific SDKs that can help you kickstart your Integration with JumpCloud in the following GitHub repositories:

* [Python](https://github.com/TheJumpCloud/jcapi-python)
* [Go](https://github.com/TheJumpCloud/jcapi-go)
* [Ruby](https://github.com/TheJumpCloud/jcapi-ruby)
* [Java](https://github.com/TheJumpCloud/jcapi-java)


</div>

## Requirements

Building the API client library requires:

1. Java 1.8+
2. Maven (3.8.3+)/Gradle (7.2+)

If you are adding this library to an Android Application or Library:

3. Android 8.0+ (API Level 26+)

## Installation<a id="installation"></a>
<div align="center">
  <a href="https://konfigthis.com/sdk-sign-up?company=JumpCloud&language=Java">
    <img src="https://raw.githubusercontent.com/konfig-dev/brand-assets/HEAD/cta-images/java-cta.png" width="70%">
  </a>
</div>

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
  <groupId>com.konfigthis</groupId>
  <artifactId>jump-cloud-java-sdk</artifactId>
  <version>2.0</version>
  <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your `build.gradle`:

```groovy
// build.gradle
repositories {
  mavenCentral()
}

dependencies {
   implementation "com.konfigthis:jump-cloud-java-sdk:2.0"
}
```

### Android users

Make sure your `build.gradle` file as a `minSdk` version of at least 26:
```groovy
// build.gradle
android {
    defaultConfig {
        minSdk 26
    }
}
```

Also make sure your library or application has internet permissions in your `AndroidManifest.xml`:

```xml
<!--AndroidManifest.xml-->
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">
    <uses-permission android:name="android.permission.INTERNET"/>
</manifest>
```

### Others

At first generate the JAR by executing:

```shell
mvn clean package
```

Then manually install the following JARs:

* `target/jump-cloud-java-sdk-2.0.jar`
* `target/lib/*.jar`

## Getting Started

Please follow the [installation](#installation) instruction and execute the following Java code:

```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ActiveDirectoryApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String activedirectoryId = "activedirectoryId_example";
    List<String> targets = Arrays.asList(); // Targets which a \"active_directory\" can be associated to.
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    Integer skip = 0; // The offset into the records to return.
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      List<GraphConnection> result = client
              .activeDirectory
              .activeDirectoryAssociationsList(activedirectoryId, targets)
              .limit(limit)
              .skip(skip)
              .xOrgId(xOrgId)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ActiveDirectoryApi#activeDirectoryAssociationsList");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<GraphConnection>> response = client
              .activeDirectory
              .activeDirectoryAssociationsList(activedirectoryId, targets)
              .limit(limit)
              .skip(skip)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ActiveDirectoryApi#activeDirectoryAssociationsList");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

## Documentation for API Endpoints

All URIs are relative to *https://console.jumpcloud.com/api/v2*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*ActiveDirectoryApi* | [**activeDirectoryAssociationsList**](docs/ActiveDirectoryApi.md#activeDirectoryAssociationsList) | **GET** /activedirectories/{activedirectory_id}/associations | List the associations of an Active Directory instance
*ActiveDirectoryApi* | [**activeDirectoryAssociationsPost**](docs/ActiveDirectoryApi.md#activeDirectoryAssociationsPost) | **POST** /activedirectories/{activedirectory_id}/associations | Manage the associations of an Active Directory instance
*ActiveDirectoryApi* | [**activeDirectoryTraverseUser**](docs/ActiveDirectoryApi.md#activeDirectoryTraverseUser) | **GET** /activedirectories/{activedirectory_id}/users | List the Users bound to an Active Directory instance
*ActiveDirectoryApi* | [**activeDirectoryTraverseUserGroup**](docs/ActiveDirectoryApi.md#activeDirectoryTraverseUserGroup) | **GET** /activedirectories/{activedirectory_id}/usergroups | List the User Groups bound to an Active Directory instance
*ActiveDirectoryApi* | [**agentsDelete**](docs/ActiveDirectoryApi.md#agentsDelete) | **DELETE** /activedirectories/{activedirectory_id}/agents/{agent_id} | Delete Active Directory Agent
*ActiveDirectoryApi* | [**agentsGet**](docs/ActiveDirectoryApi.md#agentsGet) | **GET** /activedirectories/{activedirectory_id}/agents/{agent_id} | Get Active Directory Agent
*ActiveDirectoryApi* | [**agentsList**](docs/ActiveDirectoryApi.md#agentsList) | **GET** /activedirectories/{activedirectory_id}/agents | List Active Directory Agents
*ActiveDirectoryApi* | [**agentsPost**](docs/ActiveDirectoryApi.md#agentsPost) | **POST** /activedirectories/{activedirectory_id}/agents | Create a new Active Directory Agent
*ActiveDirectoryApi* | [**delete**](docs/ActiveDirectoryApi.md#delete) | **DELETE** /activedirectories/{id} | Delete an Active Directory
*ActiveDirectoryApi* | [**get**](docs/ActiveDirectoryApi.md#get) | **GET** /activedirectories/{id} | Get an Active Directory
*ActiveDirectoryApi* | [**list**](docs/ActiveDirectoryApi.md#list) | **GET** /activedirectories | List Active Directories
*ActiveDirectoryApi* | [**patch**](docs/ActiveDirectoryApi.md#patch) | **PATCH** /activedirectories/{id} | Update Active Directory
*ActiveDirectoryApi* | [**post**](docs/ActiveDirectoryApi.md#post) | **POST** /activedirectories | Create a new Active Directory
*AdministratorsApi* | [**createByAdministrator**](docs/AdministratorsApi.md#createByAdministrator) | **POST** /administrators/{id}/organizationlinks | Allow Adminstrator access to an Organization.
*AdministratorsApi* | [**listByAdministrator**](docs/AdministratorsApi.md#listByAdministrator) | **GET** /administrators/{id}/organizationlinks | List the association links between an Administrator and Organizations.
*AdministratorsApi* | [**listByOrganization**](docs/AdministratorsApi.md#listByOrganization) | **GET** /organizations/{id}/administratorlinks | List the association links between an Organization and Administrators.
*AdministratorsApi* | [**removeByAdministrator**](docs/AdministratorsApi.md#removeByAdministrator) | **DELETE** /administrators/{administrator_id}/organizationlinks/{id} | Remove association between an Administrator and an Organization.
*AggregatedPolicyStatsApi* | [**get**](docs/AggregatedPolicyStatsApi.md#get) | **GET** /systems/{systemObjectId}/aggregated-policy-stats | Get the Aggregated Policy Stats for a System
*AppleMdmApi* | [**csrget**](docs/AppleMdmApi.md#csrget) | **GET** /applemdms/{apple_mdm_id}/csr | Get Apple MDM CSR Plist
*AppleMdmApi* | [**delete**](docs/AppleMdmApi.md#delete) | **DELETE** /applemdms/{id} | Delete an Apple MDM
*AppleMdmApi* | [**deletedevice**](docs/AppleMdmApi.md#deletedevice) | **DELETE** /applemdms/{apple_mdm_id}/devices/{device_id} | Remove an Apple MDM Device&#39;s Enrollment
*AppleMdmApi* | [**depkeyget**](docs/AppleMdmApi.md#depkeyget) | **GET** /applemdms/{apple_mdm_id}/depkey | Get Apple MDM DEP Public Key
*AppleMdmApi* | [**devicesClearActivationLock**](docs/AppleMdmApi.md#devicesClearActivationLock) | **POST** /applemdms/{apple_mdm_id}/devices/{device_id}/clearActivationLock | Clears the Activation Lock for a Device
*AppleMdmApi* | [**devicesOSUpdateStatus**](docs/AppleMdmApi.md#devicesOSUpdateStatus) | **POST** /applemdms/{apple_mdm_id}/devices/{device_id}/osUpdateStatus | Request the status of an OS update for a device
*AppleMdmApi* | [**devicesRefreshActivationLockInformation**](docs/AppleMdmApi.md#devicesRefreshActivationLockInformation) | **POST** /applemdms/{apple_mdm_id}/devices/{device_id}/refreshActivationLockInformation | Refresh activation lock information for a device
*AppleMdmApi* | [**devicesScheduleOSUpdate**](docs/AppleMdmApi.md#devicesScheduleOSUpdate) | **POST** /applemdms/{apple_mdm_id}/devices/{device_id}/scheduleOSUpdate | Schedule an OS update for a device
*AppleMdmApi* | [**deviceserase**](docs/AppleMdmApi.md#deviceserase) | **POST** /applemdms/{apple_mdm_id}/devices/{device_id}/erase | Erase Device
*AppleMdmApi* | [**deviceslist**](docs/AppleMdmApi.md#deviceslist) | **GET** /applemdms/{apple_mdm_id}/devices | List AppleMDM Devices
*AppleMdmApi* | [**deviceslock**](docs/AppleMdmApi.md#deviceslock) | **POST** /applemdms/{apple_mdm_id}/devices/{device_id}/lock | Lock Device
*AppleMdmApi* | [**devicesrestart**](docs/AppleMdmApi.md#devicesrestart) | **POST** /applemdms/{apple_mdm_id}/devices/{device_id}/restart | Restart Device
*AppleMdmApi* | [**devicesshutdown**](docs/AppleMdmApi.md#devicesshutdown) | **POST** /applemdms/{apple_mdm_id}/devices/{device_id}/shutdown | Shut Down Device
*AppleMdmApi* | [**enrollmentprofilesget**](docs/AppleMdmApi.md#enrollmentprofilesget) | **GET** /applemdms/{apple_mdm_id}/enrollmentprofiles/{id} | Get an Apple MDM Enrollment Profile
*AppleMdmApi* | [**enrollmentprofileslist**](docs/AppleMdmApi.md#enrollmentprofileslist) | **GET** /applemdms/{apple_mdm_id}/enrollmentprofiles | List Apple MDM Enrollment Profiles
*AppleMdmApi* | [**getdevice**](docs/AppleMdmApi.md#getdevice) | **GET** /applemdms/{apple_mdm_id}/devices/{device_id} | Details of an AppleMDM Device
*AppleMdmApi* | [**list**](docs/AppleMdmApi.md#list) | **GET** /applemdms | List Apple MDMs
*AppleMdmApi* | [**put**](docs/AppleMdmApi.md#put) | **PUT** /applemdms/{id} | Update an Apple MDM
*AppleMdmApi* | [**refreshdepdevices**](docs/AppleMdmApi.md#refreshdepdevices) | **POST** /applemdms/{apple_mdm_id}/refreshdepdevices | Refresh DEP Devices
*ApplicationsApi* | [**applicationAssociationsList**](docs/ApplicationsApi.md#applicationAssociationsList) | **GET** /applications/{application_id}/associations | List the associations of an Application
*ApplicationsApi* | [**applicationAssociationsPost**](docs/ApplicationsApi.md#applicationAssociationsPost) | **POST** /applications/{application_id}/associations | Manage the associations of an Application
*ApplicationsApi* | [**applicationTraverseUser**](docs/ApplicationsApi.md#applicationTraverseUser) | **GET** /applications/{application_id}/users | List the Users bound to an Application
*ApplicationsApi* | [**applicationTraverseUserGroup**](docs/ApplicationsApi.md#applicationTraverseUserGroup) | **GET** /applications/{application_id}/usergroups | List the User Groups bound to an Application
*ApplicationsApi* | [**create**](docs/ApplicationsApi.md#create) | **POST** /applications/{application_id}/import/jobs | Create an import job
*ApplicationsApi* | [**deleteLogo**](docs/ApplicationsApi.md#deleteLogo) | **DELETE** /applications/{application_id}/logo | Delete application image
*ApplicationsApi* | [**get**](docs/ApplicationsApi.md#get) | **GET** /applications/{application_id} | Get an Application
*ApplicationsApi* | [**postLogo**](docs/ApplicationsApi.md#postLogo) | **POST** /applications/{application_id}/logo | Save application logo
*ApplicationsApi* | [**users**](docs/ApplicationsApi.md#users) | **GET** /applications/{application_id}/import/users | Get a list of users to import from an Application IdM service provider
*AuthenticationPoliciesApi* | [**delete**](docs/AuthenticationPoliciesApi.md#delete) | **DELETE** /authn/policies/{id} | Delete Authentication Policy
*AuthenticationPoliciesApi* | [**get**](docs/AuthenticationPoliciesApi.md#get) | **GET** /authn/policies/{id} | Get an authentication policy
*AuthenticationPoliciesApi* | [**list**](docs/AuthenticationPoliciesApi.md#list) | **GET** /authn/policies | List Authentication Policies
*AuthenticationPoliciesApi* | [**patch**](docs/AuthenticationPoliciesApi.md#patch) | **PATCH** /authn/policies/{id} | Patch Authentication Policy
*AuthenticationPoliciesApi* | [**post**](docs/AuthenticationPoliciesApi.md#post) | **POST** /authn/policies | Create an Authentication Policy
*BulkJobRequestsApi* | [**userExpires**](docs/BulkJobRequestsApi.md#userExpires) | **POST** /bulk/user/expires | Bulk Expire Users
*BulkJobRequestsApi* | [**userStatesCreate**](docs/BulkJobRequestsApi.md#userStatesCreate) | **POST** /bulk/userstates | Create Scheduled Userstate Job
*BulkJobRequestsApi* | [**userStatesDelete**](docs/BulkJobRequestsApi.md#userStatesDelete) | **DELETE** /bulk/userstates/{id} | Delete Scheduled Userstate Job
*BulkJobRequestsApi* | [**userStatesGetNextScheduled**](docs/BulkJobRequestsApi.md#userStatesGetNextScheduled) | **GET** /bulk/userstates/eventlist/next | Get the next scheduled state change for a list of users
*BulkJobRequestsApi* | [**userStatesList**](docs/BulkJobRequestsApi.md#userStatesList) | **GET** /bulk/userstates | List Scheduled Userstate Change Jobs
*BulkJobRequestsApi* | [**userUnlocks**](docs/BulkJobRequestsApi.md#userUnlocks) | **POST** /bulk/user/unlocks | Bulk Unlock Users
*BulkJobRequestsApi* | [**usersCreate**](docs/BulkJobRequestsApi.md#usersCreate) | **POST** /bulk/users | Bulk Users Create
*BulkJobRequestsApi* | [**usersCreateResults**](docs/BulkJobRequestsApi.md#usersCreateResults) | **GET** /bulk/users/{job_id}/results | List Bulk Users Results
*BulkJobRequestsApi* | [**usersUpdate**](docs/BulkJobRequestsApi.md#usersUpdate) | **PATCH** /bulk/users | Bulk Users Update
*CommandsApi* | [**cancelQueuedCommandsByWorkflowInstanceId**](docs/CommandsApi.md#cancelQueuedCommandsByWorkflowInstanceId) | **DELETE** /commandqueue/{workflow_instance_id} | Cancel all queued commands for an organization by workflow instance Id
*CommandsApi* | [**commandAssociationsList**](docs/CommandsApi.md#commandAssociationsList) | **GET** /commands/{command_id}/associations | List the associations of a Command
*CommandsApi* | [**commandAssociationsPost**](docs/CommandsApi.md#commandAssociationsPost) | **POST** /commands/{command_id}/associations | Manage the associations of a Command
*CommandsApi* | [**commandTraverseSystem**](docs/CommandsApi.md#commandTraverseSystem) | **GET** /commands/{command_id}/systems | List the Systems bound to a Command
*CommandsApi* | [**commandTraverseSystemGroup**](docs/CommandsApi.md#commandTraverseSystemGroup) | **GET** /commands/{command_id}/systemgroups | List the System Groups bound to a Command
*CommandsApi* | [**getQueuedCommandsByWorkflow**](docs/CommandsApi.md#getQueuedCommandsByWorkflow) | **GET** /queuedcommand/workflows | Fetch the queued Commands for an Organization
*CustomEmailsApi* | [**create**](docs/CustomEmailsApi.md#create) | **POST** /customemails | Create custom email configuration
*CustomEmailsApi* | [**destroy**](docs/CustomEmailsApi.md#destroy) | **DELETE** /customemails/{custom_email_type} | Delete custom email configuration
*CustomEmailsApi* | [**getTemplates**](docs/CustomEmailsApi.md#getTemplates) | **GET** /customemail/templates | List custom email templates
*CustomEmailsApi* | [**read**](docs/CustomEmailsApi.md#read) | **GET** /customemails/{custom_email_type} | Get custom email configuration
*CustomEmailsApi* | [**update**](docs/CustomEmailsApi.md#update) | **PUT** /customemails/{custom_email_type} | Update custom email configuration
*DirectoriesApi* | [**list**](docs/DirectoriesApi.md#list) | **GET** /directories | List All Directories
*DuoApi* | [**accountDelete**](docs/DuoApi.md#accountDelete) | **DELETE** /duo/accounts/{id} | Delete a Duo Account
*DuoApi* | [**accountGet**](docs/DuoApi.md#accountGet) | **GET** /duo/accounts/{id} | Get a Duo Acount
*DuoApi* | [**accountList**](docs/DuoApi.md#accountList) | **GET** /duo/accounts | List Duo Accounts
*DuoApi* | [**accountPost**](docs/DuoApi.md#accountPost) | **POST** /duo/accounts | Create Duo Account
*DuoApi* | [**applicationDelete**](docs/DuoApi.md#applicationDelete) | **DELETE** /duo/accounts/{account_id}/applications/{application_id} | Delete a Duo Application
*DuoApi* | [**applicationGet**](docs/DuoApi.md#applicationGet) | **GET** /duo/accounts/{account_id}/applications/{application_id} | Get a Duo application
*DuoApi* | [**applicationList**](docs/DuoApi.md#applicationList) | **GET** /duo/accounts/{account_id}/applications | List Duo Applications
*DuoApi* | [**applicationPost**](docs/DuoApi.md#applicationPost) | **POST** /duo/accounts/{account_id}/applications | Create Duo Application
*DuoApi* | [**applicationUpdate**](docs/DuoApi.md#applicationUpdate) | **PUT** /duo/accounts/{account_id}/applications/{application_id} | Update Duo Application
*FeatureTrialsApi* | [**getFeatureTrials**](docs/FeatureTrialsApi.md#getFeatureTrials) | **GET** /featureTrials/{feature_code} | Check current feature trial usage for a specific feature
*GSuiteApi* | [**addDomain**](docs/GSuiteApi.md#addDomain) | **POST** /gsuites/{gsuite_id}/domains | Add a domain to a Google Workspace integration instance
*GSuiteApi* | [**configuredDomainsList**](docs/GSuiteApi.md#configuredDomainsList) | **GET** /gsuites/{gsuite_id}/domains | List all domains configured for the Google Workspace integration instance
*GSuiteApi* | [**deleteDomain**](docs/GSuiteApi.md#deleteDomain) | **DELETE** /gsuites/{gsuite_id}/domains/{domainId} | Delete a domain from a Google Workspace integration instance
*GSuiteApi* | [**gSuiteAssociationsList**](docs/GSuiteApi.md#gSuiteAssociationsList) | **GET** /gsuites/{gsuite_id}/associations | List the associations of a G Suite instance
*GSuiteApi* | [**gSuiteAssociationsPost**](docs/GSuiteApi.md#gSuiteAssociationsPost) | **POST** /gsuites/{gsuite_id}/associations | Manage the associations of a G Suite instance
*GSuiteApi* | [**gSuiteDelete**](docs/GSuiteApi.md#gSuiteDelete) | **DELETE** /gsuites/{gsuite_id}/translationrules/{id} | Deletes a G Suite translation rule
*GSuiteApi* | [**gSuiteGet**](docs/GSuiteApi.md#gSuiteGet) | **GET** /gsuites/{gsuite_id}/translationrules/{id} | Gets a specific G Suite translation rule
*GSuiteApi* | [**gSuiteList**](docs/GSuiteApi.md#gSuiteList) | **GET** /gsuites/{gsuite_id}/translationrules | List all the G Suite Translation Rules
*GSuiteApi* | [**gSuitePost**](docs/GSuiteApi.md#gSuitePost) | **POST** /gsuites/{gsuite_id}/translationrules | Create a new G Suite Translation Rule
*GSuiteApi* | [**gSuiteTraverseUser**](docs/GSuiteApi.md#gSuiteTraverseUser) | **GET** /gsuites/{gsuite_id}/users | List the Users bound to a G Suite instance
*GSuiteApi* | [**gSuiteTraverseUserGroup**](docs/GSuiteApi.md#gSuiteTraverseUserGroup) | **GET** /gsuites/{gsuite_id}/usergroups | List the User Groups bound to a G Suite instance
*GSuiteApi* | [**get**](docs/GSuiteApi.md#get) | **GET** /gsuites/{id} | Get G Suite
*GSuiteApi* | [**listImportJumpcloudUsers**](docs/GSuiteApi.md#listImportJumpcloudUsers) | **GET** /gsuites/{gsuite_id}/import/jumpcloudusers | Get a list of users in Jumpcloud format to import from a Google Workspace account.
*GSuiteApi* | [**listImportUsers**](docs/GSuiteApi.md#listImportUsers) | **GET** /gsuites/{gsuite_id}/import/users | Get a list of users to import from a G Suite instance
*GSuiteApi* | [**patch**](docs/GSuiteApi.md#patch) | **PATCH** /gsuites/{id} | Update existing G Suite
*GSuiteImportApi* | [**listImportJumpcloudUsers**](docs/GSuiteImportApi.md#listImportJumpcloudUsers) | **GET** /gsuites/{gsuite_id}/import/jumpcloudusers | Get a list of users in Jumpcloud format to import from a Google Workspace account.
*GSuiteImportApi* | [**listImportUsers**](docs/GSuiteImportApi.md#listImportUsers) | **GET** /gsuites/{gsuite_id}/import/users | Get a list of users to import from a G Suite instance
*GoogleEmmApi* | [**create**](docs/GoogleEmmApi.md#create) | **POST** /google-emm/signup-urls | Get a Signup URL to enroll Google enterprise
*GoogleEmmApi* | [**createEnrollmentToken**](docs/GoogleEmmApi.md#createEnrollmentToken) | **POST** /google-emm/enrollment-tokens | Create an enrollment token
*GoogleEmmApi* | [**createEnterprise**](docs/GoogleEmmApi.md#createEnterprise) | **POST** /google-emm/enterprises | Create a Google Enterprise
*GoogleEmmApi* | [**createEnterprisesEnrollmentToken**](docs/GoogleEmmApi.md#createEnterprisesEnrollmentToken) | **POST** /google-emm/enterprises/{enterpriseObjectId}/enrollment-tokens | Create an enrollment token for a given enterprise
*GoogleEmmApi* | [**createWebToken**](docs/GoogleEmmApi.md#createWebToken) | **POST** /google-emm/web-tokens | Get a web token to render Google Play iFrame
*GoogleEmmApi* | [**deleteEnrollmentToken**](docs/GoogleEmmApi.md#deleteEnrollmentToken) | **DELETE** /google-emm/enterprises/{enterpriseId}/enrollment-tokens/{tokenId} | Deletes an enrollment token for a given enterprise and token id
*GoogleEmmApi* | [**deleteEnterprise**](docs/GoogleEmmApi.md#deleteEnterprise) | **DELETE** /google-emm/enterprises/{enterpriseId} | Delete a Google Enterprise
*GoogleEmmApi* | [**eraseDevice**](docs/GoogleEmmApi.md#eraseDevice) | **POST** /google-emm/devices/{deviceId}/erase-device | Erase the Android Device
*GoogleEmmApi* | [**getConnectionStatus**](docs/GoogleEmmApi.md#getConnectionStatus) | **GET** /google-emm/enterprises/{enterpriseId}/connection-status | Test connection with Google
*GoogleEmmApi* | [**getDevice**](docs/GoogleEmmApi.md#getDevice) | **GET** /google-emm/devices/{deviceId} | Get device
*GoogleEmmApi* | [**getDeviceAndroidPolicy**](docs/GoogleEmmApi.md#getDeviceAndroidPolicy) | **GET** /google-emm/devices/{deviceId}/policy_results | Get the policy JSON of a device
*GoogleEmmApi* | [**listDevices**](docs/GoogleEmmApi.md#listDevices) | **GET** /google-emm/enterprises/{enterpriseObjectId}/devices | List devices
*GoogleEmmApi* | [**listEnrollmentTokens**](docs/GoogleEmmApi.md#listEnrollmentTokens) | **GET** /google-emm/enterprises/{enterpriseObjectId}/enrollment-tokens | List enrollment tokens
*GoogleEmmApi* | [**listEnterprises**](docs/GoogleEmmApi.md#listEnterprises) | **GET** /google-emm/enterprises | List Google Enterprises
*GoogleEmmApi* | [**lockDevice**](docs/GoogleEmmApi.md#lockDevice) | **POST** /google-emm/devices/{deviceId}/lock | Lock device
*GoogleEmmApi* | [**patchEnterprise**](docs/GoogleEmmApi.md#patchEnterprise) | **PATCH** /google-emm/enterprises/{enterpriseId} | Update a Google Enterprise
*GoogleEmmApi* | [**rebootDevice**](docs/GoogleEmmApi.md#rebootDevice) | **POST** /google-emm/devices/{deviceId}/reboot | Reboot device
*GoogleEmmApi* | [**resetPassword**](docs/GoogleEmmApi.md#resetPassword) | **POST** /google-emm/devices/{deviceId}/resetpassword | Reset Password of a device
*GraphApi* | [**activeDirectoryAssociationsList**](docs/GraphApi.md#activeDirectoryAssociationsList) | **GET** /activedirectories/{activedirectory_id}/associations | List the associations of an Active Directory instance
*GraphApi* | [**activeDirectoryAssociationsPost**](docs/GraphApi.md#activeDirectoryAssociationsPost) | **POST** /activedirectories/{activedirectory_id}/associations | Manage the associations of an Active Directory instance
*GraphApi* | [**activeDirectoryTraverseUser**](docs/GraphApi.md#activeDirectoryTraverseUser) | **GET** /activedirectories/{activedirectory_id}/users | List the Users bound to an Active Directory instance
*GraphApi* | [**activeDirectoryTraverseUserGroup**](docs/GraphApi.md#activeDirectoryTraverseUserGroup) | **GET** /activedirectories/{activedirectory_id}/usergroups | List the User Groups bound to an Active Directory instance
*GraphApi* | [**applicationAssociationsList**](docs/GraphApi.md#applicationAssociationsList) | **GET** /applications/{application_id}/associations | List the associations of an Application
*GraphApi* | [**applicationAssociationsPost**](docs/GraphApi.md#applicationAssociationsPost) | **POST** /applications/{application_id}/associations | Manage the associations of an Application
*GraphApi* | [**applicationTraverseUser**](docs/GraphApi.md#applicationTraverseUser) | **GET** /applications/{application_id}/users | List the Users bound to an Application
*GraphApi* | [**applicationTraverseUserGroup**](docs/GraphApi.md#applicationTraverseUserGroup) | **GET** /applications/{application_id}/usergroups | List the User Groups bound to an Application
*GraphApi* | [**commandAssociationsList**](docs/GraphApi.md#commandAssociationsList) | **GET** /commands/{command_id}/associations | List the associations of a Command
*GraphApi* | [**commandAssociationsPost**](docs/GraphApi.md#commandAssociationsPost) | **POST** /commands/{command_id}/associations | Manage the associations of a Command
*GraphApi* | [**commandTraverseSystem**](docs/GraphApi.md#commandTraverseSystem) | **GET** /commands/{command_id}/systems | List the Systems bound to a Command
*GraphApi* | [**commandTraverseSystemGroup**](docs/GraphApi.md#commandTraverseSystemGroup) | **GET** /commands/{command_id}/systemgroups | List the System Groups bound to a Command
*GraphApi* | [**gSuiteAssociationsList**](docs/GraphApi.md#gSuiteAssociationsList) | **GET** /gsuites/{gsuite_id}/associations | List the associations of a G Suite instance
*GraphApi* | [**gSuiteAssociationsPost**](docs/GraphApi.md#gSuiteAssociationsPost) | **POST** /gsuites/{gsuite_id}/associations | Manage the associations of a G Suite instance
*GraphApi* | [**gSuiteTraverseUser**](docs/GraphApi.md#gSuiteTraverseUser) | **GET** /gsuites/{gsuite_id}/users | List the Users bound to a G Suite instance
*GraphApi* | [**gSuiteTraverseUserGroup**](docs/GraphApi.md#gSuiteTraverseUserGroup) | **GET** /gsuites/{gsuite_id}/usergroups | List the User Groups bound to a G Suite instance
*GraphApi* | [**idpRoutingPolicyAssociationsList**](docs/GraphApi.md#idpRoutingPolicyAssociationsList) | **GET** /identity-provider/policies/{idp_routing_policy_id}/associations | List the associations of a Routing Policy
*GraphApi* | [**idpRoutingPolicyAssociationsPost**](docs/GraphApi.md#idpRoutingPolicyAssociationsPost) | **POST** /identity-provider/policies/{idp_routing_policy_id}/associations | Manage the associations of a routing policy
*GraphApi* | [**idpRoutingPolicyTraverseUser**](docs/GraphApi.md#idpRoutingPolicyTraverseUser) | **GET** /identity-provider/policies/{idp_routing_policy_id}/associations/users | List the Users bound to a Routing Policy
*GraphApi* | [**idpRoutingPolicyTraverseUserGroup**](docs/GraphApi.md#idpRoutingPolicyTraverseUserGroup) | **GET** /identity-provider/policies/{idp_routing_policy_id}/associations/usergroups | List the User Groups bound to a Routing Policy
*GraphApi* | [**ldapServerAssociationsList**](docs/GraphApi.md#ldapServerAssociationsList) | **GET** /ldapservers/{ldapserver_id}/associations | List the associations of a LDAP Server
*GraphApi* | [**ldapServerAssociationsPost**](docs/GraphApi.md#ldapServerAssociationsPost) | **POST** /ldapservers/{ldapserver_id}/associations | Manage the associations of a LDAP Server
*GraphApi* | [**ldapServerTraverseUser**](docs/GraphApi.md#ldapServerTraverseUser) | **GET** /ldapservers/{ldapserver_id}/users | List the Users bound to a LDAP Server
*GraphApi* | [**ldapServerTraverseUserGroup**](docs/GraphApi.md#ldapServerTraverseUserGroup) | **GET** /ldapservers/{ldapserver_id}/usergroups | List the User Groups bound to a LDAP Server
*GraphApi* | [**office365AssociationsList**](docs/GraphApi.md#office365AssociationsList) | **GET** /office365s/{office365_id}/associations | List the associations of an Office 365 instance
*GraphApi* | [**office365AssociationsPost**](docs/GraphApi.md#office365AssociationsPost) | **POST** /office365s/{office365_id}/associations | Manage the associations of an Office 365 instance
*GraphApi* | [**office365TraverseUser**](docs/GraphApi.md#office365TraverseUser) | **GET** /office365s/{office365_id}/users | List the Users bound to an Office 365 instance
*GraphApi* | [**office365TraverseUserGroup**](docs/GraphApi.md#office365TraverseUserGroup) | **GET** /office365s/{office365_id}/usergroups | List the User Groups bound to an Office 365 instance
*GraphApi* | [**policyAssociationsList**](docs/GraphApi.md#policyAssociationsList) | **GET** /policies/{policy_id}/associations | List the associations of a Policy
*GraphApi* | [**policyAssociationsPost**](docs/GraphApi.md#policyAssociationsPost) | **POST** /policies/{policy_id}/associations | Manage the associations of a Policy
*GraphApi* | [**policyGroupAssociationsList**](docs/GraphApi.md#policyGroupAssociationsList) | **GET** /policygroups/{group_id}/associations | List the associations of a Policy Group.
*GraphApi* | [**policyGroupAssociationsPost**](docs/GraphApi.md#policyGroupAssociationsPost) | **POST** /policygroups/{group_id}/associations | Manage the associations of a Policy Group
*GraphApi* | [**policyGroupMembersList**](docs/GraphApi.md#policyGroupMembersList) | **GET** /policygroups/{group_id}/members | List the members of a Policy Group
*GraphApi* | [**policyGroupMembersPost**](docs/GraphApi.md#policyGroupMembersPost) | **POST** /policygroups/{group_id}/members | Manage the members of a Policy Group
*GraphApi* | [**policyGroupMembership**](docs/GraphApi.md#policyGroupMembership) | **GET** /policygroups/{group_id}/membership | List the Policy Group&#39;s membership
*GraphApi* | [**policyGroupTraverseSystem**](docs/GraphApi.md#policyGroupTraverseSystem) | **GET** /policygroups/{group_id}/systems | List the Systems bound to a Policy Group
*GraphApi* | [**policyGroupTraverseSystemGroup**](docs/GraphApi.md#policyGroupTraverseSystemGroup) | **GET** /policygroups/{group_id}/systemgroups | List the System Groups bound to Policy Groups
*GraphApi* | [**policyMemberOf**](docs/GraphApi.md#policyMemberOf) | **GET** /policies/{policy_id}/memberof | List the parent Groups of a Policy
*GraphApi* | [**policyTraverseSystem**](docs/GraphApi.md#policyTraverseSystem) | **GET** /policies/{policy_id}/systems | List the Systems bound to a Policy
*GraphApi* | [**policyTraverseSystemGroup**](docs/GraphApi.md#policyTraverseSystemGroup) | **GET** /policies/{policy_id}/systemgroups | List the System Groups bound to a Policy
*GraphApi* | [**radiusServerAssociationsList**](docs/GraphApi.md#radiusServerAssociationsList) | **GET** /radiusservers/{radiusserver_id}/associations | List the associations of a RADIUS  Server
*GraphApi* | [**radiusServerAssociationsPost**](docs/GraphApi.md#radiusServerAssociationsPost) | **POST** /radiusservers/{radiusserver_id}/associations | Manage the associations of a RADIUS Server
*GraphApi* | [**radiusServerTraverseUser**](docs/GraphApi.md#radiusServerTraverseUser) | **GET** /radiusservers/{radiusserver_id}/users | List the Users bound to a RADIUS  Server
*GraphApi* | [**radiusServerTraverseUserGroup**](docs/GraphApi.md#radiusServerTraverseUserGroup) | **GET** /radiusservers/{radiusserver_id}/usergroups | List the User Groups bound to a RADIUS  Server
*GraphApi* | [**softwareappsAssociationsList**](docs/GraphApi.md#softwareappsAssociationsList) | **GET** /softwareapps/{software_app_id}/associations | List the associations of a Software Application
*GraphApi* | [**softwareappsAssociationsPost**](docs/GraphApi.md#softwareappsAssociationsPost) | **POST** /softwareapps/{software_app_id}/associations | Manage the associations of a software application.
*GraphApi* | [**softwareappsTraverseSystem**](docs/GraphApi.md#softwareappsTraverseSystem) | **GET** /softwareapps/{software_app_id}/systems | List the Systems bound to a Software App.
*GraphApi* | [**softwareappsTraverseSystemGroup**](docs/GraphApi.md#softwareappsTraverseSystemGroup) | **GET** /softwareapps/{software_app_id}/systemgroups | List the System Groups bound to a Software App.
*GraphApi* | [**systemAssociationsList**](docs/GraphApi.md#systemAssociationsList) | **GET** /systems/{system_id}/associations | List the associations of a System
*GraphApi* | [**systemAssociationsPost**](docs/GraphApi.md#systemAssociationsPost) | **POST** /systems/{system_id}/associations | Manage associations of a System
*GraphApi* | [**systemGroupAssociationsList**](docs/GraphApi.md#systemGroupAssociationsList) | **GET** /systemgroups/{group_id}/associations | List the associations of a System Group
*GraphApi* | [**systemGroupAssociationsPost**](docs/GraphApi.md#systemGroupAssociationsPost) | **POST** /systemgroups/{group_id}/associations | Manage the associations of a System Group
*GraphApi* | [**systemGroupMembersList**](docs/GraphApi.md#systemGroupMembersList) | **GET** /systemgroups/{group_id}/members | List the members of a System Group
*GraphApi* | [**systemGroupMembersPost**](docs/GraphApi.md#systemGroupMembersPost) | **POST** /systemgroups/{group_id}/members | Manage the members of a System Group
*GraphApi* | [**systemGroupMembership**](docs/GraphApi.md#systemGroupMembership) | **GET** /systemgroups/{group_id}/membership | List the System Group&#39;s membership
*GraphApi* | [**systemGroupTraverseCommand**](docs/GraphApi.md#systemGroupTraverseCommand) | **GET** /systemgroups/{group_id}/commands | List the Commands bound to a System Group
*GraphApi* | [**systemGroupTraversePolicy**](docs/GraphApi.md#systemGroupTraversePolicy) | **GET** /systemgroups/{group_id}/policies | List the Policies bound to a System Group
*GraphApi* | [**systemGroupTraversePolicyGroup**](docs/GraphApi.md#systemGroupTraversePolicyGroup) | **GET** /systemgroups/{group_id}/policygroups | List the Policy Groups bound to a System Group
*GraphApi* | [**systemGroupTraverseUser**](docs/GraphApi.md#systemGroupTraverseUser) | **GET** /systemgroups/{group_id}/users | List the Users bound to a System Group
*GraphApi* | [**systemGroupTraverseUserGroup**](docs/GraphApi.md#systemGroupTraverseUserGroup) | **GET** /systemgroups/{group_id}/usergroups | List the User Groups bound to a System Group
*GraphApi* | [**systemMemberOf**](docs/GraphApi.md#systemMemberOf) | **GET** /systems/{system_id}/memberof | List the parent Groups of a System
*GraphApi* | [**systemTraverseCommand**](docs/GraphApi.md#systemTraverseCommand) | **GET** /systems/{system_id}/commands | List the Commands bound to a System
*GraphApi* | [**systemTraversePolicy**](docs/GraphApi.md#systemTraversePolicy) | **GET** /systems/{system_id}/policies | List the Policies bound to a System
*GraphApi* | [**systemTraversePolicyGroup**](docs/GraphApi.md#systemTraversePolicyGroup) | **GET** /systems/{system_id}/policygroups | List the Policy Groups bound to a System
*GraphApi* | [**systemTraverseUser**](docs/GraphApi.md#systemTraverseUser) | **GET** /systems/{system_id}/users | List the Users bound to a System
*GraphApi* | [**systemTraverseUserGroup**](docs/GraphApi.md#systemTraverseUserGroup) | **GET** /systems/{system_id}/usergroups | List the User Groups bound to a System
*GraphApi* | [**systemsList**](docs/GraphApi.md#systemsList) | **GET** /systems/{system_id}/policystatuses | List the policy statuses for a system
*GraphApi* | [**userAssociationsList**](docs/GraphApi.md#userAssociationsList) | **GET** /users/{user_id}/associations | List the associations of a User
*GraphApi* | [**userAssociationsPost**](docs/GraphApi.md#userAssociationsPost) | **POST** /users/{user_id}/associations | Manage the associations of a User
*GraphApi* | [**userGroupAssociationsList**](docs/GraphApi.md#userGroupAssociationsList) | **GET** /usergroups/{group_id}/associations | List the associations of a User Group.
*GraphApi* | [**userGroupAssociationsPost**](docs/GraphApi.md#userGroupAssociationsPost) | **POST** /usergroups/{group_id}/associations | Manage the associations of a User Group
*GraphApi* | [**userGroupMembersList**](docs/GraphApi.md#userGroupMembersList) | **GET** /usergroups/{group_id}/members | List the members of a User Group
*GraphApi* | [**userGroupMembersPost**](docs/GraphApi.md#userGroupMembersPost) | **POST** /usergroups/{group_id}/members | Manage the members of a User Group
*GraphApi* | [**userGroupMembership**](docs/GraphApi.md#userGroupMembership) | **GET** /usergroups/{group_id}/membership | List the User Group&#39;s membership
*GraphApi* | [**userGroupTraverseActiveDirectory**](docs/GraphApi.md#userGroupTraverseActiveDirectory) | **GET** /usergroups/{group_id}/activedirectories | List the Active Directories bound to a User Group
*GraphApi* | [**userGroupTraverseApplication**](docs/GraphApi.md#userGroupTraverseApplication) | **GET** /usergroups/{group_id}/applications | List the Applications bound to a User Group
*GraphApi* | [**userGroupTraverseDirectory**](docs/GraphApi.md#userGroupTraverseDirectory) | **GET** /usergroups/{group_id}/directories | List the Directories bound to a User Group
*GraphApi* | [**userGroupTraverseGSuite**](docs/GraphApi.md#userGroupTraverseGSuite) | **GET** /usergroups/{group_id}/gsuites | List the G Suite instances bound to a User Group
*GraphApi* | [**userGroupTraverseLdapServer**](docs/GraphApi.md#userGroupTraverseLdapServer) | **GET** /usergroups/{group_id}/ldapservers | List the LDAP Servers bound to a User Group
*GraphApi* | [**userGroupTraverseOffice365**](docs/GraphApi.md#userGroupTraverseOffice365) | **GET** /usergroups/{group_id}/office365s | List the Office 365 instances bound to a User Group
*GraphApi* | [**userGroupTraverseRadiusServer**](docs/GraphApi.md#userGroupTraverseRadiusServer) | **GET** /usergroups/{group_id}/radiusservers | List the RADIUS Servers bound to a User Group
*GraphApi* | [**userGroupTraverseSystem**](docs/GraphApi.md#userGroupTraverseSystem) | **GET** /usergroups/{group_id}/systems | List the Systems bound to a User Group
*GraphApi* | [**userGroupTraverseSystemGroup**](docs/GraphApi.md#userGroupTraverseSystemGroup) | **GET** /usergroups/{group_id}/systemgroups | List the System Groups bound to User Groups
*GraphApi* | [**userMemberOf**](docs/GraphApi.md#userMemberOf) | **GET** /users/{user_id}/memberof | List the parent Groups of a User
*GraphApi* | [**userTraverseActiveDirectory**](docs/GraphApi.md#userTraverseActiveDirectory) | **GET** /users/{user_id}/activedirectories | List the Active Directory instances bound to a User
*GraphApi* | [**userTraverseApplication**](docs/GraphApi.md#userTraverseApplication) | **GET** /users/{user_id}/applications | List the Applications bound to a User
*GraphApi* | [**userTraverseDirectory**](docs/GraphApi.md#userTraverseDirectory) | **GET** /users/{user_id}/directories | List the Directories bound to a User
*GraphApi* | [**userTraverseGSuite**](docs/GraphApi.md#userTraverseGSuite) | **GET** /users/{user_id}/gsuites | List the G Suite instances bound to a User
*GraphApi* | [**userTraverseLdapServer**](docs/GraphApi.md#userTraverseLdapServer) | **GET** /users/{user_id}/ldapservers | List the LDAP servers bound to a User
*GraphApi* | [**userTraverseOffice365**](docs/GraphApi.md#userTraverseOffice365) | **GET** /users/{user_id}/office365s | List the Office 365 instances bound to a User
*GraphApi* | [**userTraverseRadiusServer**](docs/GraphApi.md#userTraverseRadiusServer) | **GET** /users/{user_id}/radiusservers | List the RADIUS Servers bound to a User
*GraphApi* | [**userTraverseSystem**](docs/GraphApi.md#userTraverseSystem) | **GET** /users/{user_id}/systems | List the Systems bound to a User
*GraphApi* | [**userTraverseSystemGroup**](docs/GraphApi.md#userTraverseSystemGroup) | **GET** /users/{user_id}/systemgroups | List the System Groups bound to a User
*GroupsApi* | [**list**](docs/GroupsApi.md#list) | **GET** /groups | List All Groups
*IpListsApi* | [**delete**](docs/IpListsApi.md#delete) | **DELETE** /iplists/{id} | Delete an IP list
*IpListsApi* | [**get**](docs/IpListsApi.md#get) | **GET** /iplists/{id} | Get an IP list
*IpListsApi* | [**list**](docs/IpListsApi.md#list) | **GET** /iplists | List IP Lists
*IpListsApi* | [**patch**](docs/IpListsApi.md#patch) | **PATCH** /iplists/{id} | Update an IP list
*IpListsApi* | [**post**](docs/IpListsApi.md#post) | **POST** /iplists | Create IP List
*IpListsApi* | [**put**](docs/IpListsApi.md#put) | **PUT** /iplists/{id} | Replace an IP list
*IdentityProvidersApi* | [**idpRoutingPolicyAssociationsList**](docs/IdentityProvidersApi.md#idpRoutingPolicyAssociationsList) | **GET** /identity-provider/policies/{idp_routing_policy_id}/associations | List the associations of a Routing Policy
*IdentityProvidersApi* | [**idpRoutingPolicyAssociationsPost**](docs/IdentityProvidersApi.md#idpRoutingPolicyAssociationsPost) | **POST** /identity-provider/policies/{idp_routing_policy_id}/associations | Manage the associations of a routing policy
*IdentityProvidersApi* | [**idpRoutingPolicyTraverseUser**](docs/IdentityProvidersApi.md#idpRoutingPolicyTraverseUser) | **GET** /identity-provider/policies/{idp_routing_policy_id}/associations/users | List the Users bound to a Routing Policy
*IdentityProvidersApi* | [**idpRoutingPolicyTraverseUserGroup**](docs/IdentityProvidersApi.md#idpRoutingPolicyTraverseUserGroup) | **GET** /identity-provider/policies/{idp_routing_policy_id}/associations/usergroups | List the User Groups bound to a Routing Policy
*ImageApi* | [**deleteLogo**](docs/ImageApi.md#deleteLogo) | **DELETE** /applications/{application_id}/logo | Delete application image
*IngressoApi* | [**createAccessRequest**](docs/IngressoApi.md#createAccessRequest) | **POST** /accessrequests | Create Access Request
*IngressoApi* | [**getAccessRequest**](docs/IngressoApi.md#getAccessRequest) | **GET** /accessrequests/{accessId} | Get all Access Requests by Access Id
*IngressoApi* | [**revokeAccessRequest**](docs/IngressoApi.md#revokeAccessRequest) | **POST** /accessrequests/{accessId}/revoke | Revoke access request by id
*IngressoApi* | [**updateAccessRequest**](docs/IngressoApi.md#updateAccessRequest) | **PUT** /accessrequests/{accessId} | Update access request by id
*LdapServersApi* | [**get**](docs/LdapServersApi.md#get) | **GET** /ldapservers/{id} | Get LDAP Server
*LdapServersApi* | [**ldapServerAssociationsList**](docs/LdapServersApi.md#ldapServerAssociationsList) | **GET** /ldapservers/{ldapserver_id}/associations | List the associations of a LDAP Server
*LdapServersApi* | [**ldapServerAssociationsPost**](docs/LdapServersApi.md#ldapServerAssociationsPost) | **POST** /ldapservers/{ldapserver_id}/associations | Manage the associations of a LDAP Server
*LdapServersApi* | [**ldapServerTraverseUser**](docs/LdapServersApi.md#ldapServerTraverseUser) | **GET** /ldapservers/{ldapserver_id}/users | List the Users bound to a LDAP Server
*LdapServersApi* | [**ldapServerTraverseUserGroup**](docs/LdapServersApi.md#ldapServerTraverseUserGroup) | **GET** /ldapservers/{ldapserver_id}/usergroups | List the User Groups bound to a LDAP Server
*LdapServersApi* | [**list**](docs/LdapServersApi.md#list) | **GET** /ldapservers | List LDAP Servers
*LdapServersApi* | [**patch**](docs/LdapServersApi.md#patch) | **PATCH** /ldapservers/{id} | Update existing LDAP server
*LogosApi* | [**get**](docs/LogosApi.md#get) | **GET** /logos/{id} | Get the logo associated with the specified id
*ManagedServiceProviderApi* | [**casesMetadata**](docs/ManagedServiceProviderApi.md#casesMetadata) | **GET** /cases/metadata | Get the metadata for cases
*ManagedServiceProviderApi* | [**createByAdministrator**](docs/ManagedServiceProviderApi.md#createByAdministrator) | **POST** /administrators/{id}/organizationlinks | Allow Adminstrator access to an Organization.
*ManagedServiceProviderApi* | [**createOrg**](docs/ManagedServiceProviderApi.md#createOrg) | **POST** /providers/{provider_id}/organizations | Create Provider Organization
*ManagedServiceProviderApi* | [**delete**](docs/ManagedServiceProviderApi.md#delete) | **DELETE** /providers/{provider_id}/policygrouptemplates/{id} | Deletes policy group template.
*ManagedServiceProviderApi* | [**get**](docs/ManagedServiceProviderApi.md#get) | **GET** /providers/{provider_id}/policygrouptemplates/{id} | Gets a provider&#39;s policy group template.
*ManagedServiceProviderApi* | [**getConfiguredPolicyTemplate**](docs/ManagedServiceProviderApi.md#getConfiguredPolicyTemplate) | **GET** /providers/{provider_id}/configuredpolicytemplates/{id} | Retrieve a configured policy template by id.
*ManagedServiceProviderApi* | [**getProvider**](docs/ManagedServiceProviderApi.md#getProvider) | **GET** /providers/{provider_id} | Retrieve Provider
*ManagedServiceProviderApi* | [**list**](docs/ManagedServiceProviderApi.md#list) | **GET** /providers/{provider_id}/policygrouptemplates | List a provider&#39;s policy group templates.
*ManagedServiceProviderApi* | [**listAdministrators**](docs/ManagedServiceProviderApi.md#listAdministrators) | **GET** /providers/{provider_id}/administrators | List Provider Administrators
*ManagedServiceProviderApi* | [**listByAdministrator**](docs/ManagedServiceProviderApi.md#listByAdministrator) | **GET** /administrators/{id}/organizationlinks | List the association links between an Administrator and Organizations.
*ManagedServiceProviderApi* | [**listByOrganization**](docs/ManagedServiceProviderApi.md#listByOrganization) | **GET** /organizations/{id}/administratorlinks | List the association links between an Organization and Administrators.
*ManagedServiceProviderApi* | [**listConfiguredPolicyTemplates**](docs/ManagedServiceProviderApi.md#listConfiguredPolicyTemplates) | **GET** /providers/{provider_id}/configuredpolicytemplates | List a provider&#39;s configured policy templates.
*ManagedServiceProviderApi* | [**listMembers**](docs/ManagedServiceProviderApi.md#listMembers) | **GET** /providers/{provider_id}/policygrouptemplates/{id}/members | Gets the list of members from a policy group template.
*ManagedServiceProviderApi* | [**listOrganizations**](docs/ManagedServiceProviderApi.md#listOrganizations) | **GET** /providers/{provider_id}/organizations | List Provider Organizations
*ManagedServiceProviderApi* | [**postAdmins**](docs/ManagedServiceProviderApi.md#postAdmins) | **POST** /providers/{provider_id}/administrators | Create a new Provider Administrator
*ManagedServiceProviderApi* | [**providerListCase**](docs/ManagedServiceProviderApi.md#providerListCase) | **GET** /providers/{provider_id}/cases | Get all cases (Support/Feature requests) for provider
*ManagedServiceProviderApi* | [**removeByAdministrator**](docs/ManagedServiceProviderApi.md#removeByAdministrator) | **DELETE** /administrators/{administrator_id}/organizationlinks/{id} | Remove association between an Administrator and an Organization.
*ManagedServiceProviderApi* | [**retrieveInvoice**](docs/ManagedServiceProviderApi.md#retrieveInvoice) | **GET** /providers/{provider_id}/invoices/{ID} | Download a provider&#39;s invoice.
*ManagedServiceProviderApi* | [**retrieveInvoices**](docs/ManagedServiceProviderApi.md#retrieveInvoices) | **GET** /providers/{provider_id}/invoices | List a provider&#39;s invoices.
*ManagedServiceProviderApi* | [**updateOrg**](docs/ManagedServiceProviderApi.md#updateOrg) | **PUT** /providers/{provider_id}/organizations/{id} | Update Provider Organization
*MicrosoftMdmApi* | [**downloadConfigFiles**](docs/MicrosoftMdmApi.md#downloadConfigFiles) | **POST** /microsoft-mdm/configuration-files | This endpoint provides a zipped configuration file intended for use with Microsoft Configuration Designer to create a Provisioning Package (PPKG) for bulk MDM enrollment.
*Office365Api* | [**delete**](docs/Office365Api.md#delete) | **DELETE** /office365s/{office365_id}/domains/{domain_id} | Delete a domain from an Office 365 instance
*Office365Api* | [**get**](docs/Office365Api.md#get) | **GET** /office365s/{office365_id} | Get Office 365 instance
*Office365Api* | [**insert**](docs/Office365Api.md#insert) | **POST** /office365s/{office365_id}/domains | Add a domain to an Office 365 instance
*Office365Api* | [**list**](docs/Office365Api.md#list) | **GET** /office365s/{office365_id}/domains | List all domains configured for an Office 365 instance
*Office365Api* | [**listImportUsers**](docs/Office365Api.md#listImportUsers) | **GET** /office365s/{office365_id}/import/users | Get a list of users to import from an Office 365 instance
*Office365Api* | [**office365AssociationsList**](docs/Office365Api.md#office365AssociationsList) | **GET** /office365s/{office365_id}/associations | List the associations of an Office 365 instance
*Office365Api* | [**office365AssociationsPost**](docs/Office365Api.md#office365AssociationsPost) | **POST** /office365s/{office365_id}/associations | Manage the associations of an Office 365 instance
*Office365Api* | [**office365Delete**](docs/Office365Api.md#office365Delete) | **DELETE** /office365s/{office365_id}/translationrules/{id} | Deletes a Office 365 translation rule
*Office365Api* | [**office365Get**](docs/Office365Api.md#office365Get) | **GET** /office365s/{office365_id}/translationrules/{id} | Gets a specific Office 365 translation rule
*Office365Api* | [**office365List**](docs/Office365Api.md#office365List) | **GET** /office365s/{office365_id}/translationrules | List all the Office 365 Translation Rules
*Office365Api* | [**office365Post**](docs/Office365Api.md#office365Post) | **POST** /office365s/{office365_id}/translationrules | Create a new Office 365 Translation Rule
*Office365Api* | [**office365TraverseUser**](docs/Office365Api.md#office365TraverseUser) | **GET** /office365s/{office365_id}/users | List the Users bound to an Office 365 instance
*Office365Api* | [**office365TraverseUserGroup**](docs/Office365Api.md#office365TraverseUserGroup) | **GET** /office365s/{office365_id}/usergroups | List the User Groups bound to an Office 365 instance
*Office365Api* | [**patch**](docs/Office365Api.md#patch) | **PATCH** /office365s/{office365_id} | Update existing Office 365 instance.
*Office365ImportApi* | [**listImportUsers**](docs/Office365ImportApi.md#listImportUsers) | **GET** /office365s/{office365_id}/import/users | Get a list of users to import from an Office 365 instance
*OrganizationsApi* | [**createByAdministrator**](docs/OrganizationsApi.md#createByAdministrator) | **POST** /administrators/{id}/organizationlinks | Allow Adminstrator access to an Organization.
*OrganizationsApi* | [**listByAdministrator**](docs/OrganizationsApi.md#listByAdministrator) | **GET** /administrators/{id}/organizationlinks | List the association links between an Administrator and Organizations.
*OrganizationsApi* | [**listByOrganization**](docs/OrganizationsApi.md#listByOrganization) | **GET** /organizations/{id}/administratorlinks | List the association links between an Organization and Administrators.
*OrganizationsApi* | [**orgListCases**](docs/OrganizationsApi.md#orgListCases) | **GET** /organizations/cases | Get all cases (Support/Feature requests) for organization
*OrganizationsApi* | [**removeByAdministrator**](docs/OrganizationsApi.md#removeByAdministrator) | **DELETE** /administrators/{administrator_id}/organizationlinks/{id} | Remove association between an Administrator and an Organization.
*PasswordManagerApi* | [**getDevice**](docs/PasswordManagerApi.md#getDevice) | **GET** /passwordmanager/devices/{UUID} | 
*PasswordManagerApi* | [**listDevices**](docs/PasswordManagerApi.md#listDevices) | **GET** /passwordmanager/devices | 
*PoliciesApi* | [**delete**](docs/PoliciesApi.md#delete) | **DELETE** /policies/{id} | Deletes a Policy
*PoliciesApi* | [**get**](docs/PoliciesApi.md#get) | **GET** /policies/{id} | Gets a specific Policy.
*PoliciesApi* | [**get_0**](docs/PoliciesApi.md#get_0) | **GET** /policyresults/{id} | Get a specific Policy Result.
*PoliciesApi* | [**get_1**](docs/PoliciesApi.md#get_1) | **GET** /policytemplates/{id} | Get a specific Policy Template
*PoliciesApi* | [**list**](docs/PoliciesApi.md#list) | **GET** /policies | Lists all the Policies
*PoliciesApi* | [**listAllPolicyResults**](docs/PoliciesApi.md#listAllPolicyResults) | **GET** /policyresults | Lists all of the policy results for an organization.
*PoliciesApi* | [**list_0**](docs/PoliciesApi.md#list_0) | **GET** /policies/{policy_id}/policyresults | Lists all the policy results of a policy.
*PoliciesApi* | [**list_1**](docs/PoliciesApi.md#list_1) | **GET** /policytemplates | Lists all of the Policy Templates
*PoliciesApi* | [**policiesList**](docs/PoliciesApi.md#policiesList) | **GET** /policies/{policy_id}/policystatuses | Lists the latest policy results of a policy.
*PoliciesApi* | [**policyAssociationsList**](docs/PoliciesApi.md#policyAssociationsList) | **GET** /policies/{policy_id}/associations | List the associations of a Policy
*PoliciesApi* | [**policyAssociationsPost**](docs/PoliciesApi.md#policyAssociationsPost) | **POST** /policies/{policy_id}/associations | Manage the associations of a Policy
*PoliciesApi* | [**policyMemberOf**](docs/PoliciesApi.md#policyMemberOf) | **GET** /policies/{policy_id}/memberof | List the parent Groups of a Policy
*PoliciesApi* | [**policyTraverseSystem**](docs/PoliciesApi.md#policyTraverseSystem) | **GET** /policies/{policy_id}/systems | List the Systems bound to a Policy
*PoliciesApi* | [**policyTraverseSystemGroup**](docs/PoliciesApi.md#policyTraverseSystemGroup) | **GET** /policies/{policy_id}/systemgroups | List the System Groups bound to a Policy
*PoliciesApi* | [**post**](docs/PoliciesApi.md#post) | **POST** /policies | Create a new Policy
*PoliciesApi* | [**put**](docs/PoliciesApi.md#put) | **PUT** /policies/{id} | Update an existing Policy
*PoliciesApi* | [**systemsList**](docs/PoliciesApi.md#systemsList) | **GET** /systems/{system_id}/policystatuses | List the policy statuses for a system
*PolicyGroupAssociationsApi* | [**policyGroupAssociationsList**](docs/PolicyGroupAssociationsApi.md#policyGroupAssociationsList) | **GET** /policygroups/{group_id}/associations | List the associations of a Policy Group.
*PolicyGroupAssociationsApi* | [**policyGroupAssociationsPost**](docs/PolicyGroupAssociationsApi.md#policyGroupAssociationsPost) | **POST** /policygroups/{group_id}/associations | Manage the associations of a Policy Group
*PolicyGroupAssociationsApi* | [**policyGroupTraverseSystem**](docs/PolicyGroupAssociationsApi.md#policyGroupTraverseSystem) | **GET** /policygroups/{group_id}/systems | List the Systems bound to a Policy Group
*PolicyGroupAssociationsApi* | [**policyGroupTraverseSystemGroup**](docs/PolicyGroupAssociationsApi.md#policyGroupTraverseSystemGroup) | **GET** /policygroups/{group_id}/systemgroups | List the System Groups bound to Policy Groups
*PolicyGroupMembersMembershipApi* | [**policyGroupMembersList**](docs/PolicyGroupMembersMembershipApi.md#policyGroupMembersList) | **GET** /policygroups/{group_id}/members | List the members of a Policy Group
*PolicyGroupMembersMembershipApi* | [**policyGroupMembersPost**](docs/PolicyGroupMembersMembershipApi.md#policyGroupMembersPost) | **POST** /policygroups/{group_id}/members | Manage the members of a Policy Group
*PolicyGroupMembersMembershipApi* | [**policyGroupMembership**](docs/PolicyGroupMembersMembershipApi.md#policyGroupMembership) | **GET** /policygroups/{group_id}/membership | List the Policy Group&#39;s membership
*PolicyGroupTemplatesApi* | [**delete**](docs/PolicyGroupTemplatesApi.md#delete) | **DELETE** /providers/{provider_id}/policygrouptemplates/{id} | Deletes policy group template.
*PolicyGroupTemplatesApi* | [**get**](docs/PolicyGroupTemplatesApi.md#get) | **GET** /providers/{provider_id}/policygrouptemplates/{id} | Gets a provider&#39;s policy group template.
*PolicyGroupTemplatesApi* | [**getConfiguredPolicyTemplate**](docs/PolicyGroupTemplatesApi.md#getConfiguredPolicyTemplate) | **GET** /providers/{provider_id}/configuredpolicytemplates/{id} | Retrieve a configured policy template by id.
*PolicyGroupTemplatesApi* | [**list**](docs/PolicyGroupTemplatesApi.md#list) | **GET** /providers/{provider_id}/policygrouptemplates | List a provider&#39;s policy group templates.
*PolicyGroupTemplatesApi* | [**listConfiguredPolicyTemplates**](docs/PolicyGroupTemplatesApi.md#listConfiguredPolicyTemplates) | **GET** /providers/{provider_id}/configuredpolicytemplates | List a provider&#39;s configured policy templates.
*PolicyGroupTemplatesApi* | [**listMembers**](docs/PolicyGroupTemplatesApi.md#listMembers) | **GET** /providers/{provider_id}/policygrouptemplates/{id}/members | Gets the list of members from a policy group template.
*PolicyGroupsApi* | [**createNew**](docs/PolicyGroupsApi.md#createNew) | **POST** /policygroups | Create a new Policy Group
*PolicyGroupsApi* | [**deleteGroup**](docs/PolicyGroupsApi.md#deleteGroup) | **DELETE** /policygroups/{id} | Delete a Policy Group
*PolicyGroupsApi* | [**getDetails**](docs/PolicyGroupsApi.md#getDetails) | **GET** /policygroups/{id} | View an individual Policy Group details
*PolicyGroupsApi* | [**listAll**](docs/PolicyGroupsApi.md#listAll) | **GET** /policygroups | List all Policy Groups
*PolicyGroupsApi* | [**policyGroupAssociationsList**](docs/PolicyGroupsApi.md#policyGroupAssociationsList) | **GET** /policygroups/{group_id}/associations | List the associations of a Policy Group.
*PolicyGroupsApi* | [**policyGroupAssociationsPost**](docs/PolicyGroupsApi.md#policyGroupAssociationsPost) | **POST** /policygroups/{group_id}/associations | Manage the associations of a Policy Group
*PolicyGroupsApi* | [**policyGroupMembersList**](docs/PolicyGroupsApi.md#policyGroupMembersList) | **GET** /policygroups/{group_id}/members | List the members of a Policy Group
*PolicyGroupsApi* | [**policyGroupMembersPost**](docs/PolicyGroupsApi.md#policyGroupMembersPost) | **POST** /policygroups/{group_id}/members | Manage the members of a Policy Group
*PolicyGroupsApi* | [**policyGroupMembership**](docs/PolicyGroupsApi.md#policyGroupMembership) | **GET** /policygroups/{group_id}/membership | List the Policy Group&#39;s membership
*PolicyGroupsApi* | [**policyGroupTraverseSystem**](docs/PolicyGroupsApi.md#policyGroupTraverseSystem) | **GET** /policygroups/{group_id}/systems | List the Systems bound to a Policy Group
*PolicyGroupsApi* | [**policyGroupTraverseSystemGroup**](docs/PolicyGroupsApi.md#policyGroupTraverseSystemGroup) | **GET** /policygroups/{group_id}/systemgroups | List the System Groups bound to Policy Groups
*PolicyGroupsApi* | [**updatePolicyGroup**](docs/PolicyGroupsApi.md#updatePolicyGroup) | **PUT** /policygroups/{id} | Update a Policy Group
*PolicytemplatesApi* | [**get**](docs/PolicytemplatesApi.md#get) | **GET** /policytemplates/{id} | Get a specific Policy Template
*PolicytemplatesApi* | [**list**](docs/PolicytemplatesApi.md#list) | **GET** /policytemplates | Lists all of the Policy Templates
*ProvidersApi* | [**casesMetadata**](docs/ProvidersApi.md#casesMetadata) | **GET** /cases/metadata | Get the metadata for cases
*ProvidersApi* | [**createConfiguration**](docs/ProvidersApi.md#createConfiguration) | **POST** /providers/{provider_id}/integrations/autotask | Creates a new Autotask integration for the provider
*ProvidersApi* | [**createConfiguration_0**](docs/ProvidersApi.md#createConfiguration_0) | **POST** /providers/{provider_id}/integrations/connectwise | Creates a new ConnectWise integration for the provider
*ProvidersApi* | [**createConfiguration_1**](docs/ProvidersApi.md#createConfiguration_1) | **POST** /providers/{provider_id}/integrations/syncro | Creates a new Syncro integration for the provider
*ProvidersApi* | [**createOrg**](docs/ProvidersApi.md#createOrg) | **POST** /providers/{provider_id}/organizations | Create Provider Organization
*ProvidersApi* | [**delete**](docs/ProvidersApi.md#delete) | **DELETE** /providers/{provider_id}/policygrouptemplates/{id} | Deletes policy group template.
*ProvidersApi* | [**deleteConfiguration**](docs/ProvidersApi.md#deleteConfiguration) | **DELETE** /integrations/autotask/{UUID} | Delete Autotask Integration
*ProvidersApi* | [**deleteConfiguration_0**](docs/ProvidersApi.md#deleteConfiguration_0) | **DELETE** /integrations/connectwise/{UUID} | Delete ConnectWise Integration
*ProvidersApi* | [**deleteConfiguration_1**](docs/ProvidersApi.md#deleteConfiguration_1) | **DELETE** /integrations/syncro/{UUID} | Delete Syncro Integration
*ProvidersApi* | [**get**](docs/ProvidersApi.md#get) | **GET** /providers/{provider_id}/policygrouptemplates/{id} | Gets a provider&#39;s policy group template.
*ProvidersApi* | [**getConfiguration**](docs/ProvidersApi.md#getConfiguration) | **GET** /integrations/autotask/{UUID} | Retrieve Autotask Integration Configuration
*ProvidersApi* | [**getConfiguration_0**](docs/ProvidersApi.md#getConfiguration_0) | **GET** /integrations/connectwise/{UUID} | Retrieve ConnectWise Integration Configuration
*ProvidersApi* | [**getConfiguration_1**](docs/ProvidersApi.md#getConfiguration_1) | **GET** /integrations/syncro/{UUID} | Retrieve Syncro Integration Configuration
*ProvidersApi* | [**getConfiguredPolicyTemplate**](docs/ProvidersApi.md#getConfiguredPolicyTemplate) | **GET** /providers/{provider_id}/configuredpolicytemplates/{id} | Retrieve a configured policy template by id.
*ProvidersApi* | [**getContract**](docs/ProvidersApi.md#getContract) | **GET** /providers/{provider_id}/billing/contract | Retrieve contract for a Provider
*ProvidersApi* | [**getDetails**](docs/ProvidersApi.md#getDetails) | **GET** /providers/{provider_id}/billing/details | Retrieve billing details for a Provider
*ProvidersApi* | [**getProvider**](docs/ProvidersApi.md#getProvider) | **GET** /providers/{provider_id} | Retrieve Provider
*ProvidersApi* | [**list**](docs/ProvidersApi.md#list) | **GET** /providers/{provider_id}/policygrouptemplates | List a provider&#39;s policy group templates.
*ProvidersApi* | [**listAdministrators**](docs/ProvidersApi.md#listAdministrators) | **GET** /providers/{provider_id}/administrators | List Provider Administrators
*ProvidersApi* | [**listConfiguredPolicyTemplates**](docs/ProvidersApi.md#listConfiguredPolicyTemplates) | **GET** /providers/{provider_id}/configuredpolicytemplates | List a provider&#39;s configured policy templates.
*ProvidersApi* | [**listMembers**](docs/ProvidersApi.md#listMembers) | **GET** /providers/{provider_id}/policygrouptemplates/{id}/members | Gets the list of members from a policy group template.
*ProvidersApi* | [**listOrganizations**](docs/ProvidersApi.md#listOrganizations) | **GET** /providers/{provider_id}/organizations | List Provider Organizations
*ProvidersApi* | [**patchMappings**](docs/ProvidersApi.md#patchMappings) | **PATCH** /integrations/autotask/{UUID}/mappings | Create, edit, and/or delete Autotask Mappings
*ProvidersApi* | [**patchMappings_0**](docs/ProvidersApi.md#patchMappings_0) | **PATCH** /integrations/connectwise/{UUID}/mappings | Create, edit, and/or delete ConnectWise Mappings
*ProvidersApi* | [**patchMappings_1**](docs/ProvidersApi.md#patchMappings_1) | **PATCH** /integrations/syncro/{UUID}/mappings | Create, edit, and/or delete Syncro Mappings
*ProvidersApi* | [**patchSettings**](docs/ProvidersApi.md#patchSettings) | **PATCH** /integrations/autotask/{UUID}/settings | Create, edit, and/or delete Autotask Integration settings
*ProvidersApi* | [**patchSettings_0**](docs/ProvidersApi.md#patchSettings_0) | **PATCH** /integrations/connectwise/{UUID}/settings | Create, edit, and/or delete ConnectWise Integration settings
*ProvidersApi* | [**patchSettings_1**](docs/ProvidersApi.md#patchSettings_1) | **PATCH** /integrations/syncro/{UUID}/settings | Create, edit, and/or delete Syncro Integration settings
*ProvidersApi* | [**postAdmins**](docs/ProvidersApi.md#postAdmins) | **POST** /providers/{provider_id}/administrators | Create a new Provider Administrator
*ProvidersApi* | [**providerListCase**](docs/ProvidersApi.md#providerListCase) | **GET** /providers/{provider_id}/cases | Get all cases (Support/Feature requests) for provider
*ProvidersApi* | [**removeAdministrator**](docs/ProvidersApi.md#removeAdministrator) | **DELETE** /providers/{provider_id}/administrators/{id} | Delete Provider Administrator
*ProvidersApi* | [**retrieveAdditions**](docs/ProvidersApi.md#retrieveAdditions) | **GET** /integrations/connectwise/{UUID}/agreements/{agreement_ID}/additions | Retrieve ConnectWise Additions
*ProvidersApi* | [**retrieveAgreements**](docs/ProvidersApi.md#retrieveAgreements) | **GET** /integrations/connectwise/{UUID}/agreements | Retrieve ConnectWise Agreements
*ProvidersApi* | [**retrieveAlerts**](docs/ProvidersApi.md#retrieveAlerts) | **GET** /providers/{provider_id}/integrations/ticketing/alerts | Get all ticketing alerts available for a provider&#39;s ticketing integration.
*ProvidersApi* | [**retrieveAllAlertConfigurationOptions**](docs/ProvidersApi.md#retrieveAllAlertConfigurationOptions) | **GET** /providers/{provider_id}/integrations/autotask/alerts/configuration/options | Get all Autotask ticketing alert configuration options for a provider
*ProvidersApi* | [**retrieveAllAlertConfigurationOptions_0**](docs/ProvidersApi.md#retrieveAllAlertConfigurationOptions_0) | **GET** /providers/{provider_id}/integrations/connectwise/alerts/configuration/options | Get all ConnectWise ticketing alert configuration options for a provider
*ProvidersApi* | [**retrieveAllAlertConfigurationOptions_1**](docs/ProvidersApi.md#retrieveAllAlertConfigurationOptions_1) | **GET** /providers/{provider_id}/integrations/syncro/alerts/configuration/options | Get all Syncro ticketing alert configuration options for a provider
*ProvidersApi* | [**retrieveAllAlertConfigurations**](docs/ProvidersApi.md#retrieveAllAlertConfigurations) | **GET** /providers/{provider_id}/integrations/autotask/alerts/configuration | Get all Autotask ticketing alert configurations for a provider
*ProvidersApi* | [**retrieveAllAlertConfigurations_0**](docs/ProvidersApi.md#retrieveAllAlertConfigurations_0) | **GET** /providers/{provider_id}/integrations/connectwise/alerts/configuration | Get all ConnectWise ticketing alert configurations for a provider
*ProvidersApi* | [**retrieveAllAlertConfigurations_1**](docs/ProvidersApi.md#retrieveAllAlertConfigurations_1) | **GET** /providers/{provider_id}/integrations/syncro/alerts/configuration | Get all Syncro ticketing alert configurations for a provider
*ProvidersApi* | [**retrieveBillingMappingConfigurationOptions**](docs/ProvidersApi.md#retrieveBillingMappingConfigurationOptions) | **GET** /integrations/syncro/{UUID}/billing_mapping_configuration_options | Retrieve Syncro billing mappings dependencies
*ProvidersApi* | [**retrieveCompanies**](docs/ProvidersApi.md#retrieveCompanies) | **GET** /integrations/autotask/{UUID}/companies | Retrieve Autotask Companies
*ProvidersApi* | [**retrieveCompanies_0**](docs/ProvidersApi.md#retrieveCompanies_0) | **GET** /integrations/connectwise/{UUID}/companies | Retrieve ConnectWise Companies
*ProvidersApi* | [**retrieveCompanies_1**](docs/ProvidersApi.md#retrieveCompanies_1) | **GET** /integrations/syncro/{UUID}/companies | Retrieve Syncro Companies
*ProvidersApi* | [**retrieveCompanyTypes**](docs/ProvidersApi.md#retrieveCompanyTypes) | **GET** /integrations/autotask/{UUID}/companytypes | Retrieve Autotask Company Types
*ProvidersApi* | [**retrieveCompanyTypes_0**](docs/ProvidersApi.md#retrieveCompanyTypes_0) | **GET** /integrations/connectwise/{UUID}/companytypes | Retrieve ConnectWise Company Types
*ProvidersApi* | [**retrieveContracts**](docs/ProvidersApi.md#retrieveContracts) | **GET** /integrations/autotask/{UUID}/contracts | Retrieve Autotask Contracts
*ProvidersApi* | [**retrieveContractsFields**](docs/ProvidersApi.md#retrieveContractsFields) | **GET** /integrations/autotask/{UUID}/contracts/fields | Retrieve Autotask Contract Fields
*ProvidersApi* | [**retrieveIntegrations**](docs/ProvidersApi.md#retrieveIntegrations) | **GET** /providers/{provider_id}/integrations | Retrieve Integrations for Provider
*ProvidersApi* | [**retrieveInvoice**](docs/ProvidersApi.md#retrieveInvoice) | **GET** /providers/{provider_id}/invoices/{ID} | Download a provider&#39;s invoice.
*ProvidersApi* | [**retrieveInvoices**](docs/ProvidersApi.md#retrieveInvoices) | **GET** /providers/{provider_id}/invoices | List a provider&#39;s invoices.
*ProvidersApi* | [**retrieveMappings**](docs/ProvidersApi.md#retrieveMappings) | **GET** /integrations/autotask/{UUID}/mappings | Retrieve Autotask mappings
*ProvidersApi* | [**retrieveMappings_0**](docs/ProvidersApi.md#retrieveMappings_0) | **GET** /integrations/connectwise/{UUID}/mappings | Retrieve ConnectWise mappings
*ProvidersApi* | [**retrieveMappings_1**](docs/ProvidersApi.md#retrieveMappings_1) | **GET** /integrations/syncro/{UUID}/mappings | Retrieve Syncro mappings
*ProvidersApi* | [**retrieveServices**](docs/ProvidersApi.md#retrieveServices) | **GET** /integrations/autotask/{UUID}/contracts/services | Retrieve Autotask Contract Services
*ProvidersApi* | [**retrieveSettings**](docs/ProvidersApi.md#retrieveSettings) | **GET** /integrations/autotask/{UUID}/settings | Retrieve Autotask Integration settings
*ProvidersApi* | [**retrieveSettings_0**](docs/ProvidersApi.md#retrieveSettings_0) | **GET** /integrations/connectwise/{UUID}/settings | Retrieve ConnectWise Integration settings
*ProvidersApi* | [**retrieveSettings_1**](docs/ProvidersApi.md#retrieveSettings_1) | **GET** /integrations/syncro/{UUID}/settings | Retrieve Syncro Integration settings
*ProvidersApi* | [**retrieveSyncErrors**](docs/ProvidersApi.md#retrieveSyncErrors) | **GET** /integrations/{integration_type}/{UUID}/errors | Retrieve Recent Integration Sync Errors
*ProvidersApi* | [**updateAlertConfiguration**](docs/ProvidersApi.md#updateAlertConfiguration) | **PUT** /providers/{provider_id}/integrations/autotask/alerts/{alert_UUID}/configuration | Update an Autotask ticketing alert&#39;s configuration
*ProvidersApi* | [**updateAlertConfiguration_0**](docs/ProvidersApi.md#updateAlertConfiguration_0) | **PUT** /providers/{provider_id}/integrations/connectwise/alerts/{alert_UUID}/configuration | Update a ConnectWise ticketing alert&#39;s configuration
*ProvidersApi* | [**updateAlertConfiguration_1**](docs/ProvidersApi.md#updateAlertConfiguration_1) | **PUT** /providers/{provider_id}/integrations/syncro/alerts/{alert_UUID}/configuration | Update a Syncro ticketing alert&#39;s configuration
*ProvidersApi* | [**updateConfiguration**](docs/ProvidersApi.md#updateConfiguration) | **PATCH** /integrations/autotask/{UUID} | Update Autotask Integration configuration
*ProvidersApi* | [**updateConfiguration_0**](docs/ProvidersApi.md#updateConfiguration_0) | **PATCH** /integrations/connectwise/{UUID} | Update ConnectWise Integration configuration
*ProvidersApi* | [**updateConfiguration_1**](docs/ProvidersApi.md#updateConfiguration_1) | **PATCH** /integrations/syncro/{UUID} | Update Syncro Integration configuration
*ProvidersApi* | [**updateOrg**](docs/ProvidersApi.md#updateOrg) | **PUT** /providers/{provider_id}/organizations/{id} | Update Provider Organization
*PushVerificationApi* | [**get**](docs/PushVerificationApi.md#get) | **GET** /pushendpoints/verifications/{verificationId} | Get Push Verification status
*PushVerificationApi* | [**start**](docs/PushVerificationApi.md#start) | **POST** /users/{userId}/pushendpoints/{pushEndpointId}/verify | Send Push Verification message
*RadiusServersApi* | [**radiusServerAssociationsList**](docs/RadiusServersApi.md#radiusServerAssociationsList) | **GET** /radiusservers/{radiusserver_id}/associations | List the associations of a RADIUS  Server
*RadiusServersApi* | [**radiusServerAssociationsPost**](docs/RadiusServersApi.md#radiusServerAssociationsPost) | **POST** /radiusservers/{radiusserver_id}/associations | Manage the associations of a RADIUS Server
*RadiusServersApi* | [**radiusServerTraverseUser**](docs/RadiusServersApi.md#radiusServerTraverseUser) | **GET** /radiusservers/{radiusserver_id}/users | List the Users bound to a RADIUS  Server
*RadiusServersApi* | [**radiusServerTraverseUserGroup**](docs/RadiusServersApi.md#radiusServerTraverseUserGroup) | **GET** /radiusservers/{radiusserver_id}/usergroups | List the User Groups bound to a RADIUS  Server
*ScimImportApi* | [**users**](docs/ScimImportApi.md#users) | **GET** /applications/{application_id}/import/users | Get a list of users to import from an Application IdM service provider
*SambaDomainsApi* | [**sambaDomainsDelete**](docs/SambaDomainsApi.md#sambaDomainsDelete) | **DELETE** /ldapservers/{ldapserver_id}/sambadomains/{id} | Delete Samba Domain
*SambaDomainsApi* | [**sambaDomainsGet**](docs/SambaDomainsApi.md#sambaDomainsGet) | **GET** /ldapservers/{ldapserver_id}/sambadomains/{id} | Get Samba Domain
*SambaDomainsApi* | [**sambaDomainsList**](docs/SambaDomainsApi.md#sambaDomainsList) | **GET** /ldapservers/{ldapserver_id}/sambadomains | List Samba Domains
*SambaDomainsApi* | [**sambaDomainsPost**](docs/SambaDomainsApi.md#sambaDomainsPost) | **POST** /ldapservers/{ldapserver_id}/sambadomains | Create Samba Domain
*SambaDomainsApi* | [**sambaDomainsPut**](docs/SambaDomainsApi.md#sambaDomainsPut) | **PUT** /ldapservers/{ldapserver_id}/sambadomains/{id} | Update Samba Domain
*SoftwareAppsApi* | [**delete**](docs/SoftwareAppsApi.md#delete) | **DELETE** /softwareapps/{id} | Delete a configured Software Application
*SoftwareAppsApi* | [**get**](docs/SoftwareAppsApi.md#get) | **GET** /softwareapps/{id} | Retrieve a configured Software Application.
*SoftwareAppsApi* | [**list**](docs/SoftwareAppsApi.md#list) | **GET** /softwareapps | Get all configured Software Applications.
*SoftwareAppsApi* | [**list_0**](docs/SoftwareAppsApi.md#list_0) | **GET** /softwareapps/{software_app_id}/statuses | Get the status of the provided Software Application
*SoftwareAppsApi* | [**post**](docs/SoftwareAppsApi.md#post) | **POST** /softwareapps | Create a Software Application that will be managed by JumpCloud.
*SoftwareAppsApi* | [**reclaimLicenses**](docs/SoftwareAppsApi.md#reclaimLicenses) | **POST** /softwareapps/{software_app_id}/reclaim-licenses | Reclaim Licenses for a Software Application.
*SoftwareAppsApi* | [**retryInstallation**](docs/SoftwareAppsApi.md#retryInstallation) | **POST** /softwareapps/{software_app_id}/retry-installation | Retry Installation for a Software Application
*SoftwareAppsApi* | [**softwareappsAssociationsList**](docs/SoftwareAppsApi.md#softwareappsAssociationsList) | **GET** /softwareapps/{software_app_id}/associations | List the associations of a Software Application
*SoftwareAppsApi* | [**softwareappsAssociationsPost**](docs/SoftwareAppsApi.md#softwareappsAssociationsPost) | **POST** /softwareapps/{software_app_id}/associations | Manage the associations of a software application.
*SoftwareAppsApi* | [**softwareappsTraverseSystem**](docs/SoftwareAppsApi.md#softwareappsTraverseSystem) | **GET** /softwareapps/{software_app_id}/systems | List the Systems bound to a Software App.
*SoftwareAppsApi* | [**softwareappsTraverseSystemGroup**](docs/SoftwareAppsApi.md#softwareappsTraverseSystemGroup) | **GET** /softwareapps/{software_app_id}/systemgroups | List the System Groups bound to a Software App.
*SoftwareAppsApi* | [**update**](docs/SoftwareAppsApi.md#update) | **PUT** /softwareapps/{id} | Update a Software Application Configuration.
*SoftwareAppsApi* | [**validateApplicationInstallPackage**](docs/SoftwareAppsApi.md#validateApplicationInstallPackage) | **POST** /softwareapps/validate | Validate Installation Packages
*SubscriptionsApi* | [**get**](docs/SubscriptionsApi.md#get) | **GET** /subscriptions | Lists all the Pricing &amp; Packaging Subscriptions
*SystemGroupAssociationsApi* | [**systemGroupAssociationsList**](docs/SystemGroupAssociationsApi.md#systemGroupAssociationsList) | **GET** /systemgroups/{group_id}/associations | List the associations of a System Group
*SystemGroupAssociationsApi* | [**systemGroupAssociationsPost**](docs/SystemGroupAssociationsApi.md#systemGroupAssociationsPost) | **POST** /systemgroups/{group_id}/associations | Manage the associations of a System Group
*SystemGroupAssociationsApi* | [**systemGroupTraverseCommand**](docs/SystemGroupAssociationsApi.md#systemGroupTraverseCommand) | **GET** /systemgroups/{group_id}/commands | List the Commands bound to a System Group
*SystemGroupAssociationsApi* | [**systemGroupTraversePolicy**](docs/SystemGroupAssociationsApi.md#systemGroupTraversePolicy) | **GET** /systemgroups/{group_id}/policies | List the Policies bound to a System Group
*SystemGroupAssociationsApi* | [**systemGroupTraversePolicyGroup**](docs/SystemGroupAssociationsApi.md#systemGroupTraversePolicyGroup) | **GET** /systemgroups/{group_id}/policygroups | List the Policy Groups bound to a System Group
*SystemGroupAssociationsApi* | [**systemGroupTraverseUser**](docs/SystemGroupAssociationsApi.md#systemGroupTraverseUser) | **GET** /systemgroups/{group_id}/users | List the Users bound to a System Group
*SystemGroupAssociationsApi* | [**systemGroupTraverseUserGroup**](docs/SystemGroupAssociationsApi.md#systemGroupTraverseUserGroup) | **GET** /systemgroups/{group_id}/usergroups | List the User Groups bound to a System Group
*SystemGroupMembersMembershipApi* | [**systemGroupMembersList**](docs/SystemGroupMembersMembershipApi.md#systemGroupMembersList) | **GET** /systemgroups/{group_id}/members | List the members of a System Group
*SystemGroupMembersMembershipApi* | [**systemGroupMembersPost**](docs/SystemGroupMembersMembershipApi.md#systemGroupMembersPost) | **POST** /systemgroups/{group_id}/members | Manage the members of a System Group
*SystemGroupMembersMembershipApi* | [**systemGroupMembership**](docs/SystemGroupMembersMembershipApi.md#systemGroupMembership) | **GET** /systemgroups/{group_id}/membership | List the System Group&#39;s membership
*SystemGroupsApi* | [**applySuggestions**](docs/SystemGroupsApi.md#applySuggestions) | **POST** /systemgroups/{group_id}/suggestions | Apply Suggestions for a System Group
*SystemGroupsApi* | [**createNewGroup**](docs/SystemGroupsApi.md#createNewGroup) | **POST** /systemgroups | Create a new System Group
*SystemGroupsApi* | [**deleteGroup**](docs/SystemGroupsApi.md#deleteGroup) | **DELETE** /systemgroups/{id} | Delete a System Group
*SystemGroupsApi* | [**listAll**](docs/SystemGroupsApi.md#listAll) | **GET** /systemgroups | List all System Groups
*SystemGroupsApi* | [**listSuggestions**](docs/SystemGroupsApi.md#listSuggestions) | **GET** /systemgroups/{group_id}/suggestions | List Suggestions for a System Group
*SystemGroupsApi* | [**systemGroupAssociationsList**](docs/SystemGroupsApi.md#systemGroupAssociationsList) | **GET** /systemgroups/{group_id}/associations | List the associations of a System Group
*SystemGroupsApi* | [**systemGroupAssociationsPost**](docs/SystemGroupsApi.md#systemGroupAssociationsPost) | **POST** /systemgroups/{group_id}/associations | Manage the associations of a System Group
*SystemGroupsApi* | [**systemGroupMembersList**](docs/SystemGroupsApi.md#systemGroupMembersList) | **GET** /systemgroups/{group_id}/members | List the members of a System Group
*SystemGroupsApi* | [**systemGroupMembersPost**](docs/SystemGroupsApi.md#systemGroupMembersPost) | **POST** /systemgroups/{group_id}/members | Manage the members of a System Group
*SystemGroupsApi* | [**systemGroupMembership**](docs/SystemGroupsApi.md#systemGroupMembership) | **GET** /systemgroups/{group_id}/membership | List the System Group&#39;s membership
*SystemGroupsApi* | [**systemGroupTraversePolicy**](docs/SystemGroupsApi.md#systemGroupTraversePolicy) | **GET** /systemgroups/{group_id}/policies | List the Policies bound to a System Group
*SystemGroupsApi* | [**systemGroupTraversePolicyGroup**](docs/SystemGroupsApi.md#systemGroupTraversePolicyGroup) | **GET** /systemgroups/{group_id}/policygroups | List the Policy Groups bound to a System Group
*SystemGroupsApi* | [**systemGroupTraverseUser**](docs/SystemGroupsApi.md#systemGroupTraverseUser) | **GET** /systemgroups/{group_id}/users | List the Users bound to a System Group
*SystemGroupsApi* | [**systemGroupTraverseUserGroup**](docs/SystemGroupsApi.md#systemGroupTraverseUserGroup) | **GET** /systemgroups/{group_id}/usergroups | List the User Groups bound to a System Group
*SystemGroupsApi* | [**updateGroup**](docs/SystemGroupsApi.md#updateGroup) | **PUT** /systemgroups/{id} | Update a System Group
*SystemGroupsApi* | [**viewDetails**](docs/SystemGroupsApi.md#viewDetails) | **GET** /systemgroups/{id} | View an individual System Group details
*SystemInsightsApi* | [**getChassisInfo**](docs/SystemInsightsApi.md#getChassisInfo) | **GET** /systeminsights/chassis_info | List System Insights Chassis Info
*SystemInsightsApi* | [**getDiskInfo**](docs/SystemInsightsApi.md#getDiskInfo) | **GET** /systeminsights/disk_info | List System Insights Disk Info
*SystemInsightsApi* | [**getIEExtensionsList**](docs/SystemInsightsApi.md#getIEExtensionsList) | **GET** /systeminsights/ie_extensions | List System Insights IE Extensions
*SystemInsightsApi* | [**getKernelInfo**](docs/SystemInsightsApi.md#getKernelInfo) | **GET** /systeminsights/kernel_info | List System Insights Kernel Info
*SystemInsightsApi* | [**getOsVersion**](docs/SystemInsightsApi.md#getOsVersion) | **GET** /systeminsights/os_version | List System Insights OS Version
*SystemInsightsApi* | [**getSipConfig**](docs/SystemInsightsApi.md#getSipConfig) | **GET** /systeminsights/sip_config | List System Insights SIP Config
*SystemInsightsApi* | [**getSystemInfoList**](docs/SystemInsightsApi.md#getSystemInfoList) | **GET** /systeminsights/system_info | List System Insights System Info
*SystemInsightsApi* | [**getTpmInfo**](docs/SystemInsightsApi.md#getTpmInfo) | **GET** /systeminsights/tpm_info | List System Insights TPM Info
*SystemInsightsApi* | [**getUserGroups**](docs/SystemInsightsApi.md#getUserGroups) | **GET** /systeminsights/user_groups | List System Insights User Groups
*SystemInsightsApi* | [**listAlf**](docs/SystemInsightsApi.md#listAlf) | **GET** /systeminsights/alf | List System Insights ALF
*SystemInsightsApi* | [**listAlfExceptions**](docs/SystemInsightsApi.md#listAlfExceptions) | **GET** /systeminsights/alf_exceptions | List System Insights ALF Exceptions
*SystemInsightsApi* | [**listAlfExplicitAuths**](docs/SystemInsightsApi.md#listAlfExplicitAuths) | **GET** /systeminsights/alf_explicit_auths | List System Insights ALF Explicit Authentications
*SystemInsightsApi* | [**listAppcompatShims**](docs/SystemInsightsApi.md#listAppcompatShims) | **GET** /systeminsights/appcompat_shims | List System Insights Application Compatibility Shims
*SystemInsightsApi* | [**listApps**](docs/SystemInsightsApi.md#listApps) | **GET** /systeminsights/apps | List System Insights Apps
*SystemInsightsApi* | [**listAuthorizedKeys**](docs/SystemInsightsApi.md#listAuthorizedKeys) | **GET** /systeminsights/authorized_keys | List System Insights Authorized Keys
*SystemInsightsApi* | [**listAzureInstanceMetadata**](docs/SystemInsightsApi.md#listAzureInstanceMetadata) | **GET** /systeminsights/azure_instance_metadata | List System Insights Azure Instance Metadata
*SystemInsightsApi* | [**listAzureInstanceTags**](docs/SystemInsightsApi.md#listAzureInstanceTags) | **GET** /systeminsights/azure_instance_tags | List System Insights Azure Instance Tags
*SystemInsightsApi* | [**listBatteryData**](docs/SystemInsightsApi.md#listBatteryData) | **GET** /systeminsights/battery | List System Insights Battery
*SystemInsightsApi* | [**listBitlockerInfo**](docs/SystemInsightsApi.md#listBitlockerInfo) | **GET** /systeminsights/bitlocker_info | List System Insights Bitlocker Info
*SystemInsightsApi* | [**listBrowserPlugins**](docs/SystemInsightsApi.md#listBrowserPlugins) | **GET** /systeminsights/browser_plugins | List System Insights Browser Plugins
*SystemInsightsApi* | [**listCertificates**](docs/SystemInsightsApi.md#listCertificates) | **GET** /systeminsights/certificates | List System Insights Certificates
*SystemInsightsApi* | [**listChromeExtensions**](docs/SystemInsightsApi.md#listChromeExtensions) | **GET** /systeminsights/chrome_extensions | List System Insights Chrome Extensions
*SystemInsightsApi* | [**listConnectivity**](docs/SystemInsightsApi.md#listConnectivity) | **GET** /systeminsights/connectivity | List System Insights Connectivity
*SystemInsightsApi* | [**listCrashes**](docs/SystemInsightsApi.md#listCrashes) | **GET** /systeminsights/crashes | List System Insights Crashes
*SystemInsightsApi* | [**listCupsDestinations**](docs/SystemInsightsApi.md#listCupsDestinations) | **GET** /systeminsights/cups_destinations | List System Insights CUPS Destinations
*SystemInsightsApi* | [**listDiskEncryption**](docs/SystemInsightsApi.md#listDiskEncryption) | **GET** /systeminsights/disk_encryption | List System Insights Disk Encryption
*SystemInsightsApi* | [**listDnsResolvers**](docs/SystemInsightsApi.md#listDnsResolvers) | **GET** /systeminsights/dns_resolvers | List System Insights DNS Resolvers
*SystemInsightsApi* | [**listEtcHosts**](docs/SystemInsightsApi.md#listEtcHosts) | **GET** /systeminsights/etc_hosts | List System Insights Etc Hosts
*SystemInsightsApi* | [**listFirefoxAddons**](docs/SystemInsightsApi.md#listFirefoxAddons) | **GET** /systeminsights/firefox_addons | List System Insights Firefox Addons
*SystemInsightsApi* | [**listGroups**](docs/SystemInsightsApi.md#listGroups) | **GET** /systeminsights/groups | List System Insights Groups
*SystemInsightsApi* | [**listInterfaceAddresses**](docs/SystemInsightsApi.md#listInterfaceAddresses) | **GET** /systeminsights/interface_addresses | List System Insights Interface Addresses
*SystemInsightsApi* | [**listInterfaceDetails**](docs/SystemInsightsApi.md#listInterfaceDetails) | **GET** /systeminsights/interface_details | List System Insights Interface Details
*SystemInsightsApi* | [**listLaunchd**](docs/SystemInsightsApi.md#listLaunchd) | **GET** /systeminsights/launchd | List System Insights Launchd
*SystemInsightsApi* | [**listLinuxPackages**](docs/SystemInsightsApi.md#listLinuxPackages) | **GET** /systeminsights/linux_packages | List System Insights Linux Packages
*SystemInsightsApi* | [**listLoggedInUsers**](docs/SystemInsightsApi.md#listLoggedInUsers) | **GET** /systeminsights/logged_in_users | List System Insights Logged-In Users
*SystemInsightsApi* | [**listLogicalDrives**](docs/SystemInsightsApi.md#listLogicalDrives) | **GET** /systeminsights/logical_drives | List System Insights Logical Drives
*SystemInsightsApi* | [**listManagedPolicies**](docs/SystemInsightsApi.md#listManagedPolicies) | **GET** /systeminsights/managed_policies | List System Insights Managed Policies
*SystemInsightsApi* | [**listMounts**](docs/SystemInsightsApi.md#listMounts) | **GET** /systeminsights/mounts | List System Insights Mounts
*SystemInsightsApi* | [**listPatches**](docs/SystemInsightsApi.md#listPatches) | **GET** /systeminsights/patches | List System Insights Patches
*SystemInsightsApi* | [**listPrograms**](docs/SystemInsightsApi.md#listPrograms) | **GET** /systeminsights/programs | List System Insights Programs
*SystemInsightsApi* | [**listPythonPackages**](docs/SystemInsightsApi.md#listPythonPackages) | **GET** /systeminsights/python_packages | List System Insights Python Packages
*SystemInsightsApi* | [**listSafariExtensions**](docs/SystemInsightsApi.md#listSafariExtensions) | **GET** /systeminsights/safari_extensions | List System Insights Safari Extensions
*SystemInsightsApi* | [**listScheduledTasks**](docs/SystemInsightsApi.md#listScheduledTasks) | **GET** /systeminsights/scheduled_tasks | List System Insights Scheduled Tasks
*SystemInsightsApi* | [**listSecureBoot**](docs/SystemInsightsApi.md#listSecureBoot) | **GET** /systeminsights/secureboot | List System Insights Secure Boot
*SystemInsightsApi* | [**listServices**](docs/SystemInsightsApi.md#listServices) | **GET** /systeminsights/services | List System Insights Services
*SystemInsightsApi* | [**listShadowData**](docs/SystemInsightsApi.md#listShadowData) | **GET** /systeminsights/shadow | LIst System Insights Shadow
*SystemInsightsApi* | [**listSharedFolders**](docs/SystemInsightsApi.md#listSharedFolders) | **GET** /systeminsights/shared_folders | List System Insights Shared Folders
*SystemInsightsApi* | [**listSharedResources**](docs/SystemInsightsApi.md#listSharedResources) | **GET** /systeminsights/shared_resources | List System Insights Shared Resources
*SystemInsightsApi* | [**listSharingPreferences**](docs/SystemInsightsApi.md#listSharingPreferences) | **GET** /systeminsights/sharing_preferences | List System Insights Sharing Preferences
*SystemInsightsApi* | [**listStartupItems**](docs/SystemInsightsApi.md#listStartupItems) | **GET** /systeminsights/startup_items | List System Insights Startup Items
*SystemInsightsApi* | [**listSystemControls**](docs/SystemInsightsApi.md#listSystemControls) | **GET** /systeminsights/system_controls | List System Insights System Control
*SystemInsightsApi* | [**listUptime**](docs/SystemInsightsApi.md#listUptime) | **GET** /systeminsights/uptime | List System Insights Uptime
*SystemInsightsApi* | [**listUsbDevices**](docs/SystemInsightsApi.md#listUsbDevices) | **GET** /systeminsights/usb_devices | List System Insights USB Devices
*SystemInsightsApi* | [**listUserAssist**](docs/SystemInsightsApi.md#listUserAssist) | **GET** /systeminsights/userassist | List System Insights User Assist
*SystemInsightsApi* | [**listUserSshKeys**](docs/SystemInsightsApi.md#listUserSshKeys) | **GET** /systeminsights/user_ssh_keys | List System Insights User SSH Keys
*SystemInsightsApi* | [**listUsers**](docs/SystemInsightsApi.md#listUsers) | **GET** /systeminsights/users | List System Insights Users
*SystemInsightsApi* | [**listWifiNetworks**](docs/SystemInsightsApi.md#listWifiNetworks) | **GET** /systeminsights/wifi_networks | List System Insights WiFi Networks
*SystemInsightsApi* | [**listWifiStatus**](docs/SystemInsightsApi.md#listWifiStatus) | **GET** /systeminsights/wifi_status | List System Insights WiFi Status
*SystemInsightsApi* | [**listWindowsSecurityCenter**](docs/SystemInsightsApi.md#listWindowsSecurityCenter) | **GET** /systeminsights/windows_security_center | List System Insights Windows Security Center
*SystemInsightsApi* | [**listWindowsSecurityProducts**](docs/SystemInsightsApi.md#listWindowsSecurityProducts) | **GET** /systeminsights/windows_security_products | List System Insights Windows Security Products
*SystemsApi* | [**getFDEKey**](docs/SystemsApi.md#getFDEKey) | **GET** /systems/{system_id}/fdekey | Get System FDE Key
*SystemsApi* | [**listSoftwareAppsWithStatuses**](docs/SystemsApi.md#listSoftwareAppsWithStatuses) | **GET** /systems/{system_id}/softwareappstatuses | List the associated Software Application Statuses of a System
*SystemsApi* | [**systemAssociationsList**](docs/SystemsApi.md#systemAssociationsList) | **GET** /systems/{system_id}/associations | List the associations of a System
*SystemsApi* | [**systemAssociationsPost**](docs/SystemsApi.md#systemAssociationsPost) | **POST** /systems/{system_id}/associations | Manage associations of a System
*SystemsApi* | [**systemMemberOf**](docs/SystemsApi.md#systemMemberOf) | **GET** /systems/{system_id}/memberof | List the parent Groups of a System
*SystemsApi* | [**systemTraverseCommand**](docs/SystemsApi.md#systemTraverseCommand) | **GET** /systems/{system_id}/commands | List the Commands bound to a System
*SystemsApi* | [**systemTraversePolicy**](docs/SystemsApi.md#systemTraversePolicy) | **GET** /systems/{system_id}/policies | List the Policies bound to a System
*SystemsApi* | [**systemTraversePolicyGroup**](docs/SystemsApi.md#systemTraversePolicyGroup) | **GET** /systems/{system_id}/policygroups | List the Policy Groups bound to a System
*SystemsApi* | [**systemTraverseUser**](docs/SystemsApi.md#systemTraverseUser) | **GET** /systems/{system_id}/users | List the Users bound to a System
*SystemsApi* | [**systemTraverseUserGroup**](docs/SystemsApi.md#systemTraverseUserGroup) | **GET** /systems/{system_id}/usergroups | List the User Groups bound to a System
*SystemsOrganizationSettingsApi* | [**getDefaultPasswordSyncSettings**](docs/SystemsOrganizationSettingsApi.md#getDefaultPasswordSyncSettings) | **GET** /devices/settings/defaultpasswordsync | Get the Default Password Sync Setting
*SystemsOrganizationSettingsApi* | [**getSignInWithJumpCloudSettings**](docs/SystemsOrganizationSettingsApi.md#getSignInWithJumpCloudSettings) | **GET** /devices/settings/signinwithjumpcloud | Get the Sign In with JumpCloud Settings
*SystemsOrganizationSettingsApi* | [**setDefaultPasswordSyncSettings**](docs/SystemsOrganizationSettingsApi.md#setDefaultPasswordSyncSettings) | **PUT** /devices/settings/defaultpasswordsync | Set the Default Password Sync Setting
*SystemsOrganizationSettingsApi* | [**setSignInWithJumpCloudSettings**](docs/SystemsOrganizationSettingsApi.md#setSignInWithJumpCloudSettings) | **PUT** /devices/settings/signinwithjumpcloud | Set the Sign In with JumpCloud Settings
*UserGroupAssociationsApi* | [**userGroupAssociationsList**](docs/UserGroupAssociationsApi.md#userGroupAssociationsList) | **GET** /usergroups/{group_id}/associations | List the associations of a User Group.
*UserGroupAssociationsApi* | [**userGroupAssociationsPost**](docs/UserGroupAssociationsApi.md#userGroupAssociationsPost) | **POST** /usergroups/{group_id}/associations | Manage the associations of a User Group
*UserGroupAssociationsApi* | [**userGroupTraverseActiveDirectory**](docs/UserGroupAssociationsApi.md#userGroupTraverseActiveDirectory) | **GET** /usergroups/{group_id}/activedirectories | List the Active Directories bound to a User Group
*UserGroupAssociationsApi* | [**userGroupTraverseApplication**](docs/UserGroupAssociationsApi.md#userGroupTraverseApplication) | **GET** /usergroups/{group_id}/applications | List the Applications bound to a User Group
*UserGroupAssociationsApi* | [**userGroupTraverseDirectory**](docs/UserGroupAssociationsApi.md#userGroupTraverseDirectory) | **GET** /usergroups/{group_id}/directories | List the Directories bound to a User Group
*UserGroupAssociationsApi* | [**userGroupTraverseGSuite**](docs/UserGroupAssociationsApi.md#userGroupTraverseGSuite) | **GET** /usergroups/{group_id}/gsuites | List the G Suite instances bound to a User Group
*UserGroupAssociationsApi* | [**userGroupTraverseLdapServer**](docs/UserGroupAssociationsApi.md#userGroupTraverseLdapServer) | **GET** /usergroups/{group_id}/ldapservers | List the LDAP Servers bound to a User Group
*UserGroupAssociationsApi* | [**userGroupTraverseOffice365**](docs/UserGroupAssociationsApi.md#userGroupTraverseOffice365) | **GET** /usergroups/{group_id}/office365s | List the Office 365 instances bound to a User Group
*UserGroupAssociationsApi* | [**userGroupTraverseRadiusServer**](docs/UserGroupAssociationsApi.md#userGroupTraverseRadiusServer) | **GET** /usergroups/{group_id}/radiusservers | List the RADIUS Servers bound to a User Group
*UserGroupAssociationsApi* | [**userGroupTraverseSystem**](docs/UserGroupAssociationsApi.md#userGroupTraverseSystem) | **GET** /usergroups/{group_id}/systems | List the Systems bound to a User Group
*UserGroupAssociationsApi* | [**userGroupTraverseSystemGroup**](docs/UserGroupAssociationsApi.md#userGroupTraverseSystemGroup) | **GET** /usergroups/{group_id}/systemgroups | List the System Groups bound to User Groups
*UserGroupMembersMembershipApi* | [**userGroupMembersList**](docs/UserGroupMembersMembershipApi.md#userGroupMembersList) | **GET** /usergroups/{group_id}/members | List the members of a User Group
*UserGroupMembersMembershipApi* | [**userGroupMembersPost**](docs/UserGroupMembersMembershipApi.md#userGroupMembersPost) | **POST** /usergroups/{group_id}/members | Manage the members of a User Group
*UserGroupMembersMembershipApi* | [**userGroupMembership**](docs/UserGroupMembersMembershipApi.md#userGroupMembership) | **GET** /usergroups/{group_id}/membership | List the User Group&#39;s membership
*UserGroupsApi* | [**applySuggestions**](docs/UserGroupsApi.md#applySuggestions) | **POST** /usergroups/{group_id}/suggestions | Apply Suggestions for a User Group
*UserGroupsApi* | [**createNewGroup**](docs/UserGroupsApi.md#createNewGroup) | **POST** /usergroups | Create a new User Group
*UserGroupsApi* | [**deleteGroup**](docs/UserGroupsApi.md#deleteGroup) | **DELETE** /usergroups/{id} | Delete a User Group
*UserGroupsApi* | [**getDetails**](docs/UserGroupsApi.md#getDetails) | **GET** /usergroups/{id} | View an individual User Group details
*UserGroupsApi* | [**getSuggestions**](docs/UserGroupsApi.md#getSuggestions) | **GET** /usergroups/{group_id}/suggestions | List Suggestions for a User Group
*UserGroupsApi* | [**listAll**](docs/UserGroupsApi.md#listAll) | **GET** /usergroups | List all User Groups
*UserGroupsApi* | [**updateGroup**](docs/UserGroupsApi.md#updateGroup) | **PUT** /usergroups/{id} | Update a User Group
*UserGroupsApi* | [**userGroupAssociationsList**](docs/UserGroupsApi.md#userGroupAssociationsList) | **GET** /usergroups/{group_id}/associations | List the associations of a User Group.
*UserGroupsApi* | [**userGroupAssociationsPost**](docs/UserGroupsApi.md#userGroupAssociationsPost) | **POST** /usergroups/{group_id}/associations | Manage the associations of a User Group
*UserGroupsApi* | [**userGroupMembersList**](docs/UserGroupsApi.md#userGroupMembersList) | **GET** /usergroups/{group_id}/members | List the members of a User Group
*UserGroupsApi* | [**userGroupMembersPost**](docs/UserGroupsApi.md#userGroupMembersPost) | **POST** /usergroups/{group_id}/members | Manage the members of a User Group
*UserGroupsApi* | [**userGroupMembership**](docs/UserGroupsApi.md#userGroupMembership) | **GET** /usergroups/{group_id}/membership | List the User Group&#39;s membership
*UserGroupsApi* | [**userGroupTraverseActiveDirectory**](docs/UserGroupsApi.md#userGroupTraverseActiveDirectory) | **GET** /usergroups/{group_id}/activedirectories | List the Active Directories bound to a User Group
*UserGroupsApi* | [**userGroupTraverseApplication**](docs/UserGroupsApi.md#userGroupTraverseApplication) | **GET** /usergroups/{group_id}/applications | List the Applications bound to a User Group
*UserGroupsApi* | [**userGroupTraverseDirectory**](docs/UserGroupsApi.md#userGroupTraverseDirectory) | **GET** /usergroups/{group_id}/directories | List the Directories bound to a User Group
*UserGroupsApi* | [**userGroupTraverseGSuite**](docs/UserGroupsApi.md#userGroupTraverseGSuite) | **GET** /usergroups/{group_id}/gsuites | List the G Suite instances bound to a User Group
*UserGroupsApi* | [**userGroupTraverseLdapServer**](docs/UserGroupsApi.md#userGroupTraverseLdapServer) | **GET** /usergroups/{group_id}/ldapservers | List the LDAP Servers bound to a User Group
*UserGroupsApi* | [**userGroupTraverseOffice365**](docs/UserGroupsApi.md#userGroupTraverseOffice365) | **GET** /usergroups/{group_id}/office365s | List the Office 365 instances bound to a User Group
*UserGroupsApi* | [**userGroupTraverseRadiusServer**](docs/UserGroupsApi.md#userGroupTraverseRadiusServer) | **GET** /usergroups/{group_id}/radiusservers | List the RADIUS Servers bound to a User Group
*UserGroupsApi* | [**userGroupTraverseSystem**](docs/UserGroupsApi.md#userGroupTraverseSystem) | **GET** /usergroups/{group_id}/systems | List the Systems bound to a User Group
*UserGroupsApi* | [**userGroupTraverseSystemGroup**](docs/UserGroupsApi.md#userGroupTraverseSystemGroup) | **GET** /usergroups/{group_id}/systemgroups | List the System Groups bound to User Groups
*UsersApi* | [**delete**](docs/UsersApi.md#delete) | **DELETE** /users/{user_id}/pushendpoints/{push_endpoint_id} | Delete a Push Endpoint associated with a User
*UsersApi* | [**get**](docs/UsersApi.md#get) | **GET** /users/{user_id}/pushendpoints/{push_endpoint_id} | Get a push endpoint associated with a User
*UsersApi* | [**list**](docs/UsersApi.md#list) | **GET** /users/{user_id}/pushendpoints | List Push Endpoints associated with a User
*UsersApi* | [**patch**](docs/UsersApi.md#patch) | **PATCH** /users/{user_id}/pushendpoints/{push_endpoint_id} | Update a push endpoint associated with a User
*UsersApi* | [**userAssociationsList**](docs/UsersApi.md#userAssociationsList) | **GET** /users/{user_id}/associations | List the associations of a User
*UsersApi* | [**userAssociationsPost**](docs/UsersApi.md#userAssociationsPost) | **POST** /users/{user_id}/associations | Manage the associations of a User
*UsersApi* | [**userMemberOf**](docs/UsersApi.md#userMemberOf) | **GET** /users/{user_id}/memberof | List the parent Groups of a User
*UsersApi* | [**userTraverseActiveDirectory**](docs/UsersApi.md#userTraverseActiveDirectory) | **GET** /users/{user_id}/activedirectories | List the Active Directory instances bound to a User
*UsersApi* | [**userTraverseApplication**](docs/UsersApi.md#userTraverseApplication) | **GET** /users/{user_id}/applications | List the Applications bound to a User
*UsersApi* | [**userTraverseDirectory**](docs/UsersApi.md#userTraverseDirectory) | **GET** /users/{user_id}/directories | List the Directories bound to a User
*UsersApi* | [**userTraverseGSuite**](docs/UsersApi.md#userTraverseGSuite) | **GET** /users/{user_id}/gsuites | List the G Suite instances bound to a User
*UsersApi* | [**userTraverseLdapServer**](docs/UsersApi.md#userTraverseLdapServer) | **GET** /users/{user_id}/ldapservers | List the LDAP servers bound to a User
*UsersApi* | [**userTraverseOffice365**](docs/UsersApi.md#userTraverseOffice365) | **GET** /users/{user_id}/office365s | List the Office 365 instances bound to a User
*UsersApi* | [**userTraverseRadiusServer**](docs/UsersApi.md#userTraverseRadiusServer) | **GET** /users/{user_id}/radiusservers | List the RADIUS Servers bound to a User
*UsersApi* | [**userTraverseSystem**](docs/UsersApi.md#userTraverseSystem) | **GET** /users/{user_id}/systems | List the Systems bound to a User
*UsersApi* | [**userTraverseSystemGroup**](docs/UsersApi.md#userTraverseSystemGroup) | **GET** /users/{user_id}/systemgroups | List the System Groups bound to a User
*WorkdayImportApi* | [**authorize**](docs/WorkdayImportApi.md#authorize) | **POST** /workdays/{workday_id}/auth | Authorize Workday
*WorkdayImportApi* | [**callImport**](docs/WorkdayImportApi.md#callImport) | **POST** /workdays/{workday_id}/import | Workday Import
*WorkdayImportApi* | [**deauthorize**](docs/WorkdayImportApi.md#deauthorize) | **DELETE** /workdays/{workday_id}/auth | Deauthorize Workday
*WorkdayImportApi* | [**get**](docs/WorkdayImportApi.md#get) | **GET** /workdays/{id} | Get Workday
*WorkdayImportApi* | [**importresults**](docs/WorkdayImportApi.md#importresults) | **GET** /workdays/{id}/import/{job_id}/results | List Import Results
*WorkdayImportApi* | [**list**](docs/WorkdayImportApi.md#list) | **GET** /workdays | List Workdays
*WorkdayImportApi* | [**post**](docs/WorkdayImportApi.md#post) | **POST** /workdays | Create new Workday
*WorkdayImportApi* | [**put**](docs/WorkdayImportApi.md#put) | **PUT** /workdays/{id} | Update Workday
*WorkdayImportApi* | [**workers**](docs/WorkdayImportApi.md#workers) | **GET** /workdays/{workday_id}/workers | List Workday Workers
*FdeApi* | [**getFDEKey**](docs/FdeApi.md#getFDEKey) | **GET** /systems/{system_id}/fdekey | Get System FDE Key


## Documentation for Models

 - [ADE](docs/ADE.md)
 - [ADES](docs/ADES.md)
 - [AccessRequestApiUpdateAccessRequestRequest](docs/AccessRequestApiUpdateAccessRequestRequest.md)
 - [ActiveDirectory](docs/ActiveDirectory.md)
 - [ActiveDirectoryAgent](docs/ActiveDirectoryAgent.md)
 - [ActiveDirectoryAgentGet](docs/ActiveDirectoryAgentGet.md)
 - [ActiveDirectoryAgentList](docs/ActiveDirectoryAgentList.md)
 - [Address](docs/Address.md)
 - [Administrator](docs/Administrator.md)
 - [AdministratorOrganizationLink](docs/AdministratorOrganizationLink.md)
 - [AdministratorOrganizationLinkReq](docs/AdministratorOrganizationLinkReq.md)
 - [AppleMDM](docs/AppleMDM.md)
 - [AppleMdmDevice](docs/AppleMdmDevice.md)
 - [AppleMdmDeviceInfo](docs/AppleMdmDeviceInfo.md)
 - [AppleMdmDeviceSecurityInfo](docs/AppleMdmDeviceSecurityInfo.md)
 - [AppleMdmPatch](docs/AppleMdmPatch.md)
 - [ApplemdmsDeviceseraseRequest](docs/ApplemdmsDeviceseraseRequest.md)
 - [ApplemdmsDeviceslockRequest](docs/ApplemdmsDeviceslockRequest.md)
 - [ApplemdmsDevicesrestartRequest](docs/ApplemdmsDevicesrestartRequest.md)
 - [ApplicationsPostLogoRequest](docs/ApplicationsPostLogoRequest.md)
 - [AppsInner](docs/AppsInner.md)
 - [AuthInfo](docs/AuthInfo.md)
 - [AuthInput](docs/AuthInput.md)
 - [AuthInputBasic](docs/AuthInputBasic.md)
 - [AuthInputOauth](docs/AuthInputOauth.md)
 - [AuthInputObject](docs/AuthInputObject.md)
 - [AuthnPolicy](docs/AuthnPolicy.md)
 - [AuthnPolicyEffect](docs/AuthnPolicyEffect.md)
 - [AuthnPolicyObligations](docs/AuthnPolicyObligations.md)
 - [AuthnPolicyObligationsMfa](docs/AuthnPolicyObligationsMfa.md)
 - [AuthnPolicyObligationsUserVerification](docs/AuthnPolicyObligationsUserVerification.md)
 - [AuthnPolicyResourceTarget](docs/AuthnPolicyResourceTarget.md)
 - [AuthnPolicyTargets](docs/AuthnPolicyTargets.md)
 - [AuthnPolicyType](docs/AuthnPolicyType.md)
 - [AuthnPolicyUserAttributeFilter](docs/AuthnPolicyUserAttributeFilter.md)
 - [AuthnPolicyUserAttributeTarget](docs/AuthnPolicyUserAttributeTarget.md)
 - [AuthnPolicyUserGroupTarget](docs/AuthnPolicyUserGroupTarget.md)
 - [AuthnPolicyUserTarget](docs/AuthnPolicyUserTarget.md)
 - [AutotaskCompany](docs/AutotaskCompany.md)
 - [AutotaskCompanyResp](docs/AutotaskCompanyResp.md)
 - [AutotaskCompanyTypeResp](docs/AutotaskCompanyTypeResp.md)
 - [AutotaskContract](docs/AutotaskContract.md)
 - [AutotaskContractField](docs/AutotaskContractField.md)
 - [AutotaskContractFieldValuesInner](docs/AutotaskContractFieldValuesInner.md)
 - [AutotaskCreateConfigurationResponse](docs/AutotaskCreateConfigurationResponse.md)
 - [AutotaskIntegration](docs/AutotaskIntegration.md)
 - [AutotaskIntegrationPatchReq](docs/AutotaskIntegrationPatchReq.md)
 - [AutotaskIntegrationReq](docs/AutotaskIntegrationReq.md)
 - [AutotaskMappingRequest](docs/AutotaskMappingRequest.md)
 - [AutotaskMappingRequestCompany](docs/AutotaskMappingRequestCompany.md)
 - [AutotaskMappingRequestContract](docs/AutotaskMappingRequestContract.md)
 - [AutotaskMappingRequestDataInner](docs/AutotaskMappingRequestDataInner.md)
 - [AutotaskMappingRequestOrganization](docs/AutotaskMappingRequestOrganization.md)
 - [AutotaskMappingRequestService](docs/AutotaskMappingRequestService.md)
 - [AutotaskMappingResponse](docs/AutotaskMappingResponse.md)
 - [AutotaskMappingResponseCompany](docs/AutotaskMappingResponseCompany.md)
 - [AutotaskMappingResponseContract](docs/AutotaskMappingResponseContract.md)
 - [AutotaskMappingResponseOrganization](docs/AutotaskMappingResponseOrganization.md)
 - [AutotaskMappingResponseService](docs/AutotaskMappingResponseService.md)
 - [AutotaskRetrieveContractsFieldsResponse](docs/AutotaskRetrieveContractsFieldsResponse.md)
 - [AutotaskRetrieveContractsResponse](docs/AutotaskRetrieveContractsResponse.md)
 - [AutotaskRetrieveMappingsResponse](docs/AutotaskRetrieveMappingsResponse.md)
 - [AutotaskRetrieveServicesResponse](docs/AutotaskRetrieveServicesResponse.md)
 - [AutotaskService](docs/AutotaskService.md)
 - [AutotaskSettings](docs/AutotaskSettings.md)
 - [AutotaskSettingsPatchReq](docs/AutotaskSettingsPatchReq.md)
 - [AutotaskTicketingAlertConfiguration](docs/AutotaskTicketingAlertConfiguration.md)
 - [AutotaskTicketingAlertConfigurationList](docs/AutotaskTicketingAlertConfigurationList.md)
 - [AutotaskTicketingAlertConfigurationListRecordsInner](docs/AutotaskTicketingAlertConfigurationListRecordsInner.md)
 - [AutotaskTicketingAlertConfigurationListRecordsInnerAllOf](docs/AutotaskTicketingAlertConfigurationListRecordsInnerAllOf.md)
 - [AutotaskTicketingAlertConfigurationOption](docs/AutotaskTicketingAlertConfigurationOption.md)
 - [AutotaskTicketingAlertConfigurationOptionValuesInner](docs/AutotaskTicketingAlertConfigurationOptionValuesInner.md)
 - [AutotaskTicketingAlertConfigurationOptions](docs/AutotaskTicketingAlertConfigurationOptions.md)
 - [AutotaskTicketingAlertConfigurationPriority](docs/AutotaskTicketingAlertConfigurationPriority.md)
 - [AutotaskTicketingAlertConfigurationRequest](docs/AutotaskTicketingAlertConfigurationRequest.md)
 - [AutotaskTicketingAlertConfigurationResource](docs/AutotaskTicketingAlertConfigurationResource.md)
 - [BillingIntegrationCompanyType](docs/BillingIntegrationCompanyType.md)
 - [BulkScheduledStatechangeCreate](docs/BulkScheduledStatechangeCreate.md)
 - [BulkUserCreate](docs/BulkUserCreate.md)
 - [BulkUserExpire](docs/BulkUserExpire.md)
 - [BulkUserStatesGetNextScheduledResponse](docs/BulkUserStatesGetNextScheduledResponse.md)
 - [BulkUserUnlock](docs/BulkUserUnlock.md)
 - [BulkUserUpdate](docs/BulkUserUpdate.md)
 - [CasesMetadataResponse](docs/CasesMetadataResponse.md)
 - [CasesMetadataResponseResults](docs/CasesMetadataResponseResults.md)
 - [CasesResponse](docs/CasesResponse.md)
 - [CommandsGraphObjectWithPaths](docs/CommandsGraphObjectWithPaths.md)
 - [ConfiguredPolicyTemplate](docs/ConfiguredPolicyTemplate.md)
 - [ConfiguredPolicyTemplateValue](docs/ConfiguredPolicyTemplateValue.md)
 - [ConnectWiseMappingRequest](docs/ConnectWiseMappingRequest.md)
 - [ConnectWiseMappingRequestDataInner](docs/ConnectWiseMappingRequestDataInner.md)
 - [ConnectWiseMappingRequestDataInnerAddition](docs/ConnectWiseMappingRequestDataInnerAddition.md)
 - [ConnectWiseMappingRequestDataInnerCompany](docs/ConnectWiseMappingRequestDataInnerCompany.md)
 - [ConnectWiseMappingRequestDataInnerOrganization](docs/ConnectWiseMappingRequestDataInnerOrganization.md)
 - [ConnectWiseMappingResponse](docs/ConnectWiseMappingResponse.md)
 - [ConnectWiseMappingResponseAddition](docs/ConnectWiseMappingResponseAddition.md)
 - [ConnectWiseSettings](docs/ConnectWiseSettings.md)
 - [ConnectWiseSettingsPatchReq](docs/ConnectWiseSettingsPatchReq.md)
 - [ConnectWiseTicketingAlertConfiguration](docs/ConnectWiseTicketingAlertConfiguration.md)
 - [ConnectWiseTicketingAlertConfigurationList](docs/ConnectWiseTicketingAlertConfigurationList.md)
 - [ConnectWiseTicketingAlertConfigurationListRecordsInner](docs/ConnectWiseTicketingAlertConfigurationListRecordsInner.md)
 - [ConnectWiseTicketingAlertConfigurationOption](docs/ConnectWiseTicketingAlertConfigurationOption.md)
 - [ConnectWiseTicketingAlertConfigurationOptions](docs/ConnectWiseTicketingAlertConfigurationOptions.md)
 - [ConnectWiseTicketingAlertConfigurationRequest](docs/ConnectWiseTicketingAlertConfigurationRequest.md)
 - [ConnectwiseAddition](docs/ConnectwiseAddition.md)
 - [ConnectwiseAgreement](docs/ConnectwiseAgreement.md)
 - [ConnectwiseCompany](docs/ConnectwiseCompany.md)
 - [ConnectwiseCompanyResp](docs/ConnectwiseCompanyResp.md)
 - [ConnectwiseCompanyTypeResp](docs/ConnectwiseCompanyTypeResp.md)
 - [ConnectwiseCreateConfigurationResponse](docs/ConnectwiseCreateConfigurationResponse.md)
 - [ConnectwiseIntegration](docs/ConnectwiseIntegration.md)
 - [ConnectwiseIntegrationPatchReq](docs/ConnectwiseIntegrationPatchReq.md)
 - [ConnectwiseIntegrationReq](docs/ConnectwiseIntegrationReq.md)
 - [ConnectwiseRetrieveAdditionsResponse](docs/ConnectwiseRetrieveAdditionsResponse.md)
 - [ConnectwiseRetrieveAgreementsResponse](docs/ConnectwiseRetrieveAgreementsResponse.md)
 - [ConnectwiseRetrieveMappingsResponse](docs/ConnectwiseRetrieveMappingsResponse.md)
 - [CreateOrganization](docs/CreateOrganization.md)
 - [CustomEmail](docs/CustomEmail.md)
 - [CustomEmailTemplate](docs/CustomEmailTemplate.md)
 - [CustomEmailTemplateField](docs/CustomEmailTemplateField.md)
 - [CustomEmailType](docs/CustomEmailType.md)
 - [DEP](docs/DEP.md)
 - [DEPSetupAssistantOption](docs/DEPSetupAssistantOption.md)
 - [DEPWelcomeScreen](docs/DEPWelcomeScreen.md)
 - [DefaultDomain](docs/DefaultDomain.md)
 - [DevicePackageV1Device](docs/DevicePackageV1Device.md)
 - [DevicePackageV1ListDevicesResponse](docs/DevicePackageV1ListDevicesResponse.md)
 - [DevicesAggregatedPolicyCount](docs/DevicesAggregatedPolicyCount.md)
 - [DevicesAggregatedPolicyResultResponse](docs/DevicesAggregatedPolicyResultResponse.md)
 - [DevicesGetDefaultPasswordSyncSettingsResponse](docs/DevicesGetDefaultPasswordSyncSettingsResponse.md)
 - [DevicesGetSignInWithJumpCloudSettingsResponse](docs/DevicesGetSignInWithJumpCloudSettingsResponse.md)
 - [DevicesResetPasswordRequest](docs/DevicesResetPasswordRequest.md)
 - [DevicesSetDefaultPasswordSyncSettingsRequest](docs/DevicesSetDefaultPasswordSyncSettingsRequest.md)
 - [DevicesSetSignInWithJumpCloudSettingsRequest](docs/DevicesSetSignInWithJumpCloudSettingsRequest.md)
 - [DevicesSignInWithJumpCloudSetting](docs/DevicesSignInWithJumpCloudSetting.md)
 - [DevicesSignInWithJumpCloudSettingOSFamily](docs/DevicesSignInWithJumpCloudSettingOSFamily.md)
 - [DevicesSignInWithJumpCloudSettingPermission](docs/DevicesSignInWithJumpCloudSettingPermission.md)
 - [Directory](docs/Directory.md)
 - [DirectoryDefaultDomain](docs/DirectoryDefaultDomain.md)
 - [DomainsInsertRequest](docs/DomainsInsertRequest.md)
 - [DuoAccount](docs/DuoAccount.md)
 - [DuoApplication](docs/DuoApplication.md)
 - [DuoApplicationReq](docs/DuoApplicationReq.md)
 - [DuoApplicationUpdateReq](docs/DuoApplicationUpdateReq.md)
 - [EnrollmentTokensCreateEnterprisesEnrollmentTokenRequest](docs/EnrollmentTokensCreateEnterprisesEnrollmentTokenRequest.md)
 - [EnterprisesPatchEnterpriseRequest](docs/EnterprisesPatchEnterpriseRequest.md)
 - [Error](docs/Error.md)
 - [ErrorDetails](docs/ErrorDetails.md)
 - [ErrorDetailsAllOf](docs/ErrorDetailsAllOf.md)
 - [Feature](docs/Feature.md)
 - [FeatureTrialData](docs/FeatureTrialData.md)
 - [GSuiteBuiltinTranslation](docs/GSuiteBuiltinTranslation.md)
 - [GSuiteDirectionTranslation](docs/GSuiteDirectionTranslation.md)
 - [GSuiteTranslationRule](docs/GSuiteTranslationRule.md)
 - [GSuiteTranslationRuleRequest](docs/GSuiteTranslationRuleRequest.md)
 - [GoogleProtobufAny](docs/GoogleProtobufAny.md)
 - [GoogleRpcStatus](docs/GoogleRpcStatus.md)
 - [GraphAttributeLdapGroups](docs/GraphAttributeLdapGroups.md)
 - [GraphAttributePosixGroups](docs/GraphAttributePosixGroups.md)
 - [GraphAttributePosixGroupsPosixGroupsInner](docs/GraphAttributePosixGroupsPosixGroupsInner.md)
 - [GraphAttributeRadius](docs/GraphAttributeRadius.md)
 - [GraphAttributeRadiusRadius](docs/GraphAttributeRadiusRadius.md)
 - [GraphAttributeRadiusRadiusReplyInner](docs/GraphAttributeRadiusRadiusReplyInner.md)
 - [GraphAttributeSambaEnabled](docs/GraphAttributeSambaEnabled.md)
 - [GraphAttributeSudo](docs/GraphAttributeSudo.md)
 - [GraphAttributeSudoSudo](docs/GraphAttributeSudoSudo.md)
 - [GraphConnection](docs/GraphConnection.md)
 - [GraphObject](docs/GraphObject.md)
 - [GraphObjectWithPaths](docs/GraphObjectWithPaths.md)
 - [GraphOperation](docs/GraphOperation.md)
 - [GraphOperationActiveDirectory](docs/GraphOperationActiveDirectory.md)
 - [GraphOperationActiveDirectoryAllOf](docs/GraphOperationActiveDirectoryAllOf.md)
 - [GraphOperationApplication](docs/GraphOperationApplication.md)
 - [GraphOperationApplicationAllOf](docs/GraphOperationApplicationAllOf.md)
 - [GraphOperationCommand](docs/GraphOperationCommand.md)
 - [GraphOperationCommandAllOf](docs/GraphOperationCommandAllOf.md)
 - [GraphOperationGSuite](docs/GraphOperationGSuite.md)
 - [GraphOperationGSuiteAllOf](docs/GraphOperationGSuiteAllOf.md)
 - [GraphOperationIDPRoutingPolicy](docs/GraphOperationIDPRoutingPolicy.md)
 - [GraphOperationIDPRoutingPolicyAllOf](docs/GraphOperationIDPRoutingPolicyAllOf.md)
 - [GraphOperationLdapServer](docs/GraphOperationLdapServer.md)
 - [GraphOperationLdapServerAllOf](docs/GraphOperationLdapServerAllOf.md)
 - [GraphOperationOffice365](docs/GraphOperationOffice365.md)
 - [GraphOperationOffice365AllOf](docs/GraphOperationOffice365AllOf.md)
 - [GraphOperationPolicy](docs/GraphOperationPolicy.md)
 - [GraphOperationPolicyAllOf](docs/GraphOperationPolicyAllOf.md)
 - [GraphOperationPolicyGroup](docs/GraphOperationPolicyGroup.md)
 - [GraphOperationPolicyGroupAllOf](docs/GraphOperationPolicyGroupAllOf.md)
 - [GraphOperationPolicyGroupMember](docs/GraphOperationPolicyGroupMember.md)
 - [GraphOperationPolicyGroupMemberAllOf](docs/GraphOperationPolicyGroupMemberAllOf.md)
 - [GraphOperationRadiusServer](docs/GraphOperationRadiusServer.md)
 - [GraphOperationRadiusServerAllOf](docs/GraphOperationRadiusServerAllOf.md)
 - [GraphOperationSoftwareApp](docs/GraphOperationSoftwareApp.md)
 - [GraphOperationSoftwareAppAllOf](docs/GraphOperationSoftwareAppAllOf.md)
 - [GraphOperationSystem](docs/GraphOperationSystem.md)
 - [GraphOperationSystemAllOf](docs/GraphOperationSystemAllOf.md)
 - [GraphOperationSystemGroup](docs/GraphOperationSystemGroup.md)
 - [GraphOperationSystemGroupAllOf](docs/GraphOperationSystemGroupAllOf.md)
 - [GraphOperationSystemGroupMember](docs/GraphOperationSystemGroupMember.md)
 - [GraphOperationSystemGroupMemberAllOf](docs/GraphOperationSystemGroupMemberAllOf.md)
 - [GraphOperationUser](docs/GraphOperationUser.md)
 - [GraphOperationUserAllOf](docs/GraphOperationUserAllOf.md)
 - [GraphOperationUserGroup](docs/GraphOperationUserGroup.md)
 - [GraphOperationUserGroupAllOf](docs/GraphOperationUserGroupAllOf.md)
 - [GraphOperationUserGroupMember](docs/GraphOperationUserGroupMember.md)
 - [GraphOperationUserGroupMemberAllOf](docs/GraphOperationUserGroupMemberAllOf.md)
 - [GraphType](docs/GraphType.md)
 - [Group](docs/Group.md)
 - [GroupAttributesUserGroup](docs/GroupAttributesUserGroup.md)
 - [GroupMembershipMethodType](docs/GroupMembershipMethodType.md)
 - [GroupPwm](docs/GroupPwm.md)
 - [GroupType](docs/GroupType.md)
 - [GroupsInner](docs/GroupsInner.md)
 - [Gsuite](docs/Gsuite.md)
 - [GsuitesListImportJumpcloudUsersResponse](docs/GsuitesListImportJumpcloudUsersResponse.md)
 - [GsuitesListImportUsersResponse](docs/GsuitesListImportUsersResponse.md)
 - [GsuitesListImportUsersResponseUsersInner](docs/GsuitesListImportUsersResponseUsersInner.md)
 - [IPList](docs/IPList.md)
 - [IPListRequest](docs/IPListRequest.md)
 - [ImportOperation](docs/ImportOperation.md)
 - [ImportUser](docs/ImportUser.md)
 - [ImportUserAddress](docs/ImportUserAddress.md)
 - [ImportUserPhoneNumber](docs/ImportUserPhoneNumber.md)
 - [ImportUsersRequest](docs/ImportUsersRequest.md)
 - [ImportUsersResponse](docs/ImportUsersResponse.md)
 - [InstallActionType](docs/InstallActionType.md)
 - [Integration](docs/Integration.md)
 - [IntegrationSyncError](docs/IntegrationSyncError.md)
 - [IntegrationSyncErrorResp](docs/IntegrationSyncErrorResp.md)
 - [IntegrationType](docs/IntegrationType.md)
 - [IntegrationsResponse](docs/IntegrationsResponse.md)
 - [JobId](docs/JobId.md)
 - [JobWorkresult](docs/JobWorkresult.md)
 - [JumpcloudAuthPushVerification](docs/JumpcloudAuthPushVerification.md)
 - [JumpcloudAuthPushVerificationStatus](docs/JumpcloudAuthPushVerificationStatus.md)
 - [JumpcloudGappsDomain](docs/JumpcloudGappsDomain.md)
 - [JumpcloudGappsDomainListResponse](docs/JumpcloudGappsDomainListResponse.md)
 - [JumpcloudGappsDomainResponse](docs/JumpcloudGappsDomainResponse.md)
 - [JumpcloudGoogleEmmAllowPersonalUsage](docs/JumpcloudGoogleEmmAllowPersonalUsage.md)
 - [JumpcloudGoogleEmmCommonCriteriaModeInfo](docs/JumpcloudGoogleEmmCommonCriteriaModeInfo.md)
 - [JumpcloudGoogleEmmConnectionStatus](docs/JumpcloudGoogleEmmConnectionStatus.md)
 - [JumpcloudGoogleEmmCreateEnrollmentTokenRequest](docs/JumpcloudGoogleEmmCreateEnrollmentTokenRequest.md)
 - [JumpcloudGoogleEmmCreateEnrollmentTokenResponse](docs/JumpcloudGoogleEmmCreateEnrollmentTokenResponse.md)
 - [JumpcloudGoogleEmmCreateEnterpriseRequest](docs/JumpcloudGoogleEmmCreateEnterpriseRequest.md)
 - [JumpcloudGoogleEmmCreateWebTokenRequest](docs/JumpcloudGoogleEmmCreateWebTokenRequest.md)
 - [JumpcloudGoogleEmmCreatedWhere](docs/JumpcloudGoogleEmmCreatedWhere.md)
 - [JumpcloudGoogleEmmDeleteEnrollmentTokenResponse](docs/JumpcloudGoogleEmmDeleteEnrollmentTokenResponse.md)
 - [JumpcloudGoogleEmmDevice](docs/JumpcloudGoogleEmmDevice.md)
 - [JumpcloudGoogleEmmDeviceAndroidPolicy](docs/JumpcloudGoogleEmmDeviceAndroidPolicy.md)
 - [JumpcloudGoogleEmmDeviceData](docs/JumpcloudGoogleEmmDeviceData.md)
 - [JumpcloudGoogleEmmDeviceInformation](docs/JumpcloudGoogleEmmDeviceInformation.md)
 - [JumpcloudGoogleEmmDeviceSettings](docs/JumpcloudGoogleEmmDeviceSettings.md)
 - [JumpcloudGoogleEmmDeviceStateInfo](docs/JumpcloudGoogleEmmDeviceStateInfo.md)
 - [JumpcloudGoogleEmmEMMEnrollmentInfo](docs/JumpcloudGoogleEmmEMMEnrollmentInfo.md)
 - [JumpcloudGoogleEmmEnrollmentToken](docs/JumpcloudGoogleEmmEnrollmentToken.md)
 - [JumpcloudGoogleEmmEnrollmentType](docs/JumpcloudGoogleEmmEnrollmentType.md)
 - [JumpcloudGoogleEmmEnterprise](docs/JumpcloudGoogleEmmEnterprise.md)
 - [JumpcloudGoogleEmmFeature](docs/JumpcloudGoogleEmmFeature.md)
 - [JumpcloudGoogleEmmHardwareInfo](docs/JumpcloudGoogleEmmHardwareInfo.md)
 - [JumpcloudGoogleEmmListDevicesResponse](docs/JumpcloudGoogleEmmListDevicesResponse.md)
 - [JumpcloudGoogleEmmListEnrollmentTokensResponse](docs/JumpcloudGoogleEmmListEnrollmentTokensResponse.md)
 - [JumpcloudGoogleEmmListEnterprisesResponse](docs/JumpcloudGoogleEmmListEnterprisesResponse.md)
 - [JumpcloudGoogleEmmMemoryInfo](docs/JumpcloudGoogleEmmMemoryInfo.md)
 - [JumpcloudGoogleEmmNetworkInfo](docs/JumpcloudGoogleEmmNetworkInfo.md)
 - [JumpcloudGoogleEmmProvisioningExtras](docs/JumpcloudGoogleEmmProvisioningExtras.md)
 - [JumpcloudGoogleEmmSecurityPosture](docs/JumpcloudGoogleEmmSecurityPosture.md)
 - [JumpcloudGoogleEmmSignupURL](docs/JumpcloudGoogleEmmSignupURL.md)
 - [JumpcloudGoogleEmmSoftwareInfo](docs/JumpcloudGoogleEmmSoftwareInfo.md)
 - [JumpcloudGoogleEmmSystemUpdateInfo](docs/JumpcloudGoogleEmmSystemUpdateInfo.md)
 - [JumpcloudGoogleEmmTelephonyInfo](docs/JumpcloudGoogleEmmTelephonyInfo.md)
 - [JumpcloudGoogleEmmWebToken](docs/JumpcloudGoogleEmmWebToken.md)
 - [JumpcloudGoogleEmmWifiSecurityType](docs/JumpcloudGoogleEmmWifiSecurityType.md)
 - [JumpcloudIngressoCreateAccessRequestsRequest](docs/JumpcloudIngressoCreateAccessRequestsRequest.md)
 - [JumpcloudIngressoCreateAccessRequestsResponse](docs/JumpcloudIngressoCreateAccessRequestsResponse.md)
 - [JumpcloudIngressoGetAccessRequestResponse](docs/JumpcloudIngressoGetAccessRequestResponse.md)
 - [JumpcloudMspGetDetailsResponse](docs/JumpcloudMspGetDetailsResponse.md)
 - [JumpcloudMspProduct](docs/JumpcloudMspProduct.md)
 - [JumpcloudPackageValidatorApplePackageDetails](docs/JumpcloudPackageValidatorApplePackageDetails.md)
 - [JumpcloudPackageValidatorValidateApplicationInstallPackageRequest](docs/JumpcloudPackageValidatorValidateApplicationInstallPackageRequest.md)
 - [JumpcloudPackageValidatorValidateApplicationInstallPackageResponse](docs/JumpcloudPackageValidatorValidateApplicationInstallPackageResponse.md)
 - [LdapGroup](docs/LdapGroup.md)
 - [LdapServer](docs/LdapServer.md)
 - [LdapServerAction](docs/LdapServerAction.md)
 - [LdapserversPatchRequest](docs/LdapserversPatchRequest.md)
 - [LdapserversPatchResponse](docs/LdapserversPatchResponse.md)
 - [MemberQuery](docs/MemberQuery.md)
 - [MemberQuery1](docs/MemberQuery1.md)
 - [MemberSuggestion](docs/MemberSuggestion.md)
 - [MemberSuggestionsPostResult](docs/MemberSuggestionsPostResult.md)
 - [ModelCase](docs/ModelCase.md)
 - [O365Domain](docs/O365Domain.md)
 - [O365DomainResponse](docs/O365DomainResponse.md)
 - [O365DomainsListResponse](docs/O365DomainsListResponse.md)
 - [OSRestriction](docs/OSRestriction.md)
 - [OSRestrictionAppleRestrictions](docs/OSRestrictionAppleRestrictions.md)
 - [ObjectStorageItem](docs/ObjectStorageItem.md)
 - [ObjectStorageVersion](docs/ObjectStorageVersion.md)
 - [Office365](docs/Office365.md)
 - [Office365BuiltinTranslation](docs/Office365BuiltinTranslation.md)
 - [Office365DirectionTranslation](docs/Office365DirectionTranslation.md)
 - [Office365SListImportUsersResponse](docs/Office365SListImportUsersResponse.md)
 - [Office365SListImportUsersResponseUsersInner](docs/Office365SListImportUsersResponseUsersInner.md)
 - [Office365TranslationRule](docs/Office365TranslationRule.md)
 - [Office365TranslationRuleRequest](docs/Office365TranslationRuleRequest.md)
 - [Organization](docs/Organization.md)
 - [PasswordsSecurity](docs/PasswordsSecurity.md)
 - [PhoneNumber](docs/PhoneNumber.md)
 - [Policy](docs/Policy.md)
 - [PolicyCreateRequest](docs/PolicyCreateRequest.md)
 - [PolicyCreateRequestTemplate](docs/PolicyCreateRequestTemplate.md)
 - [PolicyGroup](docs/PolicyGroup.md)
 - [PolicyGroupData](docs/PolicyGroupData.md)
 - [PolicyGroupTemplate](docs/PolicyGroupTemplate.md)
 - [PolicyGroupTemplateMember](docs/PolicyGroupTemplateMember.md)
 - [PolicyGroupTemplateMembers](docs/PolicyGroupTemplateMembers.md)
 - [PolicyGroupTemplates](docs/PolicyGroupTemplates.md)
 - [PolicyGroupTemplatesListConfiguredPolicyTemplatesResponse](docs/PolicyGroupTemplatesListConfiguredPolicyTemplatesResponse.md)
 - [PolicyResult](docs/PolicyResult.md)
 - [PolicyTemplate](docs/PolicyTemplate.md)
 - [PolicyTemplateConfigField](docs/PolicyTemplateConfigField.md)
 - [PolicyTemplateConfigFieldTooltip](docs/PolicyTemplateConfigFieldTooltip.md)
 - [PolicyTemplateConfigFieldTooltipVariables](docs/PolicyTemplateConfigFieldTooltipVariables.md)
 - [PolicyTemplateWithDetails](docs/PolicyTemplateWithDetails.md)
 - [PolicyUpdateRequest](docs/PolicyUpdateRequest.md)
 - [PolicyValue](docs/PolicyValue.md)
 - [PolicyWithDetails](docs/PolicyWithDetails.md)
 - [Provider](docs/Provider.md)
 - [ProviderAdminReq](docs/ProviderAdminReq.md)
 - [ProviderInvoice](docs/ProviderInvoice.md)
 - [ProviderInvoiceResponse](docs/ProviderInvoiceResponse.md)
 - [ProvidersListAdministratorsResponse](docs/ProvidersListAdministratorsResponse.md)
 - [ProvidersListOrganizationsResponse](docs/ProvidersListOrganizationsResponse.md)
 - [PushEndpointResponse](docs/PushEndpointResponse.md)
 - [PushEndpointResponseDevice](docs/PushEndpointResponseDevice.md)
 - [PushEndpointsPatchRequest](docs/PushEndpointsPatchRequest.md)
 - [PushVerificationsStartRequest](docs/PushVerificationsStartRequest.md)
 - [PwmAllUsers](docs/PwmAllUsers.md)
 - [PwmAllUsersResultsInner](docs/PwmAllUsersResultsInner.md)
 - [PwmCloudBackupRestores](docs/PwmCloudBackupRestores.md)
 - [PwmItemHistory](docs/PwmItemHistory.md)
 - [PwmItemsCountByType](docs/PwmItemsCountByType.md)
 - [PwmItemsMetadata](docs/PwmItemsMetadata.md)
 - [PwmItemsMetadataResultsInner](docs/PwmItemsMetadataResultsInner.md)
 - [PwmOverviewAppVersions](docs/PwmOverviewAppVersions.md)
 - [PwmOverviewAppVersionsResultsInner](docs/PwmOverviewAppVersionsResultsInner.md)
 - [PwmOverviewMain](docs/PwmOverviewMain.md)
 - [PwmOverviewMainDevicesInner](docs/PwmOverviewMainDevicesInner.md)
 - [PwmUser](docs/PwmUser.md)
 - [PwmUserById](docs/PwmUserById.md)
 - [PwmUserItem](docs/PwmUserItem.md)
 - [PwmUserItems](docs/PwmUserItems.md)
 - [PwmUserSharedFolders](docs/PwmUserSharedFolders.md)
 - [PwmUserSharedFoldersResultsInner](docs/PwmUserSharedFoldersResultsInner.md)
 - [Query](docs/Query.md)
 - [QueuedCommandList](docs/QueuedCommandList.md)
 - [QueuedCommandListResultsInner](docs/QueuedCommandListResultsInner.md)
 - [SambaDomain](docs/SambaDomain.md)
 - [ScheduleOSUpdate](docs/ScheduleOSUpdate.md)
 - [ScheduledUserstateResult](docs/ScheduledUserstateResult.md)
 - [SetupAssistantOption](docs/SetupAssistantOption.md)
 - [SharedFolder](docs/SharedFolder.md)
 - [SharedFolderAccessLevels](docs/SharedFolderAccessLevels.md)
 - [SharedFolderAccessLevelsResultsInner](docs/SharedFolderAccessLevelsResultsInner.md)
 - [SharedFolderDetails](docs/SharedFolderDetails.md)
 - [SharedFolderGroups](docs/SharedFolderGroups.md)
 - [SharedFolderUsers](docs/SharedFolderUsers.md)
 - [SharedFolderUsersResultsInner](docs/SharedFolderUsersResultsInner.md)
 - [SharedFoldersList](docs/SharedFoldersList.md)
 - [SoftwareApp](docs/SoftwareApp.md)
 - [SoftwareAppAppleVpp](docs/SoftwareAppAppleVpp.md)
 - [SoftwareAppCreate](docs/SoftwareAppCreate.md)
 - [SoftwareAppGoogleAndroid](docs/SoftwareAppGoogleAndroid.md)
 - [SoftwareAppMicrosoftStore](docs/SoftwareAppMicrosoftStore.md)
 - [SoftwareAppPermissionGrants](docs/SoftwareAppPermissionGrants.md)
 - [SoftwareAppReclaimLicenses](docs/SoftwareAppReclaimLicenses.md)
 - [SoftwareAppSettings](docs/SoftwareAppSettings.md)
 - [SoftwareAppStatus](docs/SoftwareAppStatus.md)
 - [SoftwareAppWithStatus](docs/SoftwareAppWithStatus.md)
 - [SoftwareAppsRetryInstallationRequest](docs/SoftwareAppsRetryInstallationRequest.md)
 - [Subscription](docs/Subscription.md)
 - [SuggestionCounts](docs/SuggestionCounts.md)
 - [SyncroBillingMappingConfigurationOption](docs/SyncroBillingMappingConfigurationOption.md)
 - [SyncroBillingMappingConfigurationOptionValue](docs/SyncroBillingMappingConfigurationOptionValue.md)
 - [SyncroBillingMappingConfigurationOptionValueLine](docs/SyncroBillingMappingConfigurationOptionValueLine.md)
 - [SyncroBillingMappingConfigurationOptionsResp](docs/SyncroBillingMappingConfigurationOptionsResp.md)
 - [SyncroCompany](docs/SyncroCompany.md)
 - [SyncroCompanyResp](docs/SyncroCompanyResp.md)
 - [SyncroCreateConfigurationResponse](docs/SyncroCreateConfigurationResponse.md)
 - [SyncroIntegration](docs/SyncroIntegration.md)
 - [SyncroIntegrationPatchReq](docs/SyncroIntegrationPatchReq.md)
 - [SyncroIntegrationReq](docs/SyncroIntegrationReq.md)
 - [SyncroMappingRequest](docs/SyncroMappingRequest.md)
 - [SyncroMappingRequestDataInner](docs/SyncroMappingRequestDataInner.md)
 - [SyncroMappingRequestDataInnerBillingConfigurations](docs/SyncroMappingRequestDataInnerBillingConfigurations.md)
 - [SyncroMappingRequestDataInnerBillingConfigurationsFields](docs/SyncroMappingRequestDataInnerBillingConfigurationsFields.md)
 - [SyncroMappingRequestDataInnerBillingConfigurationsFieldsLineItemId](docs/SyncroMappingRequestDataInnerBillingConfigurationsFieldsLineItemId.md)
 - [SyncroMappingRequestDataInnerBillingConfigurationsFieldsLineItemName](docs/SyncroMappingRequestDataInnerBillingConfigurationsFieldsLineItemName.md)
 - [SyncroMappingResponse](docs/SyncroMappingResponse.md)
 - [SyncroRetrieveMappingsResponse](docs/SyncroRetrieveMappingsResponse.md)
 - [SyncroSettings](docs/SyncroSettings.md)
 - [SyncroSettingsPatchReq](docs/SyncroSettingsPatchReq.md)
 - [SyncroTicketingAlertConfiguration](docs/SyncroTicketingAlertConfiguration.md)
 - [SyncroTicketingAlertConfigurationList](docs/SyncroTicketingAlertConfigurationList.md)
 - [SyncroTicketingAlertConfigurationListRecordsInner](docs/SyncroTicketingAlertConfigurationListRecordsInner.md)
 - [SyncroTicketingAlertConfigurationOption](docs/SyncroTicketingAlertConfigurationOption.md)
 - [SyncroTicketingAlertConfigurationOptions](docs/SyncroTicketingAlertConfigurationOptions.md)
 - [SyncroTicketingAlertConfigurationRequest](docs/SyncroTicketingAlertConfigurationRequest.md)
 - [SystemGroup](docs/SystemGroup.md)
 - [SystemGroupPost](docs/SystemGroupPost.md)
 - [SystemGroupPut](docs/SystemGroupPut.md)
 - [SystemGroupsApplySuggestionsRequest](docs/SystemGroupsApplySuggestionsRequest.md)
 - [SystemInsightsAlf](docs/SystemInsightsAlf.md)
 - [SystemInsightsAlfExceptions](docs/SystemInsightsAlfExceptions.md)
 - [SystemInsightsAlfExplicitAuths](docs/SystemInsightsAlfExplicitAuths.md)
 - [SystemInsightsAppcompatShims](docs/SystemInsightsAppcompatShims.md)
 - [SystemInsightsApps](docs/SystemInsightsApps.md)
 - [SystemInsightsAuthorizedKeys](docs/SystemInsightsAuthorizedKeys.md)
 - [SystemInsightsAzureInstanceMetadata](docs/SystemInsightsAzureInstanceMetadata.md)
 - [SystemInsightsAzureInstanceTags](docs/SystemInsightsAzureInstanceTags.md)
 - [SystemInsightsBattery](docs/SystemInsightsBattery.md)
 - [SystemInsightsBitlockerInfo](docs/SystemInsightsBitlockerInfo.md)
 - [SystemInsightsBrowserPlugins](docs/SystemInsightsBrowserPlugins.md)
 - [SystemInsightsCertificates](docs/SystemInsightsCertificates.md)
 - [SystemInsightsChassisInfo](docs/SystemInsightsChassisInfo.md)
 - [SystemInsightsChromeExtensions](docs/SystemInsightsChromeExtensions.md)
 - [SystemInsightsConnectivity](docs/SystemInsightsConnectivity.md)
 - [SystemInsightsCrashes](docs/SystemInsightsCrashes.md)
 - [SystemInsightsCupsDestinations](docs/SystemInsightsCupsDestinations.md)
 - [SystemInsightsDiskEncryption](docs/SystemInsightsDiskEncryption.md)
 - [SystemInsightsDiskInfo](docs/SystemInsightsDiskInfo.md)
 - [SystemInsightsDnsResolvers](docs/SystemInsightsDnsResolvers.md)
 - [SystemInsightsEtcHosts](docs/SystemInsightsEtcHosts.md)
 - [SystemInsightsFirefoxAddons](docs/SystemInsightsFirefoxAddons.md)
 - [SystemInsightsGroups](docs/SystemInsightsGroups.md)
 - [SystemInsightsIeExtensions](docs/SystemInsightsIeExtensions.md)
 - [SystemInsightsInterfaceAddresses](docs/SystemInsightsInterfaceAddresses.md)
 - [SystemInsightsInterfaceDetails](docs/SystemInsightsInterfaceDetails.md)
 - [SystemInsightsKernelInfo](docs/SystemInsightsKernelInfo.md)
 - [SystemInsightsLaunchd](docs/SystemInsightsLaunchd.md)
 - [SystemInsightsLinuxPackages](docs/SystemInsightsLinuxPackages.md)
 - [SystemInsightsLoggedInUsers](docs/SystemInsightsLoggedInUsers.md)
 - [SystemInsightsLogicalDrives](docs/SystemInsightsLogicalDrives.md)
 - [SystemInsightsManagedPolicies](docs/SystemInsightsManagedPolicies.md)
 - [SystemInsightsMounts](docs/SystemInsightsMounts.md)
 - [SystemInsightsOsVersion](docs/SystemInsightsOsVersion.md)
 - [SystemInsightsPatches](docs/SystemInsightsPatches.md)
 - [SystemInsightsPrograms](docs/SystemInsightsPrograms.md)
 - [SystemInsightsPythonPackages](docs/SystemInsightsPythonPackages.md)
 - [SystemInsightsSafariExtensions](docs/SystemInsightsSafariExtensions.md)
 - [SystemInsightsScheduledTasks](docs/SystemInsightsScheduledTasks.md)
 - [SystemInsightsSecureboot](docs/SystemInsightsSecureboot.md)
 - [SystemInsightsServices](docs/SystemInsightsServices.md)
 - [SystemInsightsShadow](docs/SystemInsightsShadow.md)
 - [SystemInsightsSharedFolders](docs/SystemInsightsSharedFolders.md)
 - [SystemInsightsSharedResources](docs/SystemInsightsSharedResources.md)
 - [SystemInsightsSharingPreferences](docs/SystemInsightsSharingPreferences.md)
 - [SystemInsightsSipConfig](docs/SystemInsightsSipConfig.md)
 - [SystemInsightsStartupItems](docs/SystemInsightsStartupItems.md)
 - [SystemInsightsSystemControls](docs/SystemInsightsSystemControls.md)
 - [SystemInsightsSystemInfo](docs/SystemInsightsSystemInfo.md)
 - [SystemInsightsTpmInfo](docs/SystemInsightsTpmInfo.md)
 - [SystemInsightsUptime](docs/SystemInsightsUptime.md)
 - [SystemInsightsUsbDevices](docs/SystemInsightsUsbDevices.md)
 - [SystemInsightsUserGroups](docs/SystemInsightsUserGroups.md)
 - [SystemInsightsUserSshKeys](docs/SystemInsightsUserSshKeys.md)
 - [SystemInsightsUserassist](docs/SystemInsightsUserassist.md)
 - [SystemInsightsUsers](docs/SystemInsightsUsers.md)
 - [SystemInsightsWifiNetworks](docs/SystemInsightsWifiNetworks.md)
 - [SystemInsightsWifiStatus](docs/SystemInsightsWifiStatus.md)
 - [SystemInsightsWindowsSecurityCenter](docs/SystemInsightsWindowsSecurityCenter.md)
 - [SystemInsightsWindowsSecurityProducts](docs/SystemInsightsWindowsSecurityProducts.md)
 - [Systemfdekey](docs/Systemfdekey.md)
 - [TicketingIntegrationAlert](docs/TicketingIntegrationAlert.md)
 - [TicketingIntegrationAlertsResp](docs/TicketingIntegrationAlertsResp.md)
 - [User](docs/User.md)
 - [UserGroup](docs/UserGroup.md)
 - [UserGroupPost](docs/UserGroupPost.md)
 - [UserGroupPut](docs/UserGroupPut.md)
 - [UserGroupsApplySuggestionsRequest](docs/UserGroupsApplySuggestionsRequest.md)
 - [WorkdayFields](docs/WorkdayFields.md)
 - [WorkdayInput](docs/WorkdayInput.md)
 - [WorkdayOutput](docs/WorkdayOutput.md)
 - [WorkdayOutputAuth](docs/WorkdayOutputAuth.md)
 - [WorkdayWorker](docs/WorkdayWorker.md)


## Author
This Java package is automatically generated by [Konfig](https://konfigthis.com)
