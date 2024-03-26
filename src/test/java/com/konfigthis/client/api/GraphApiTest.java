/*
 * JumpCloud API
 * # Overview  JumpCloud's V2 API. This set of endpoints allows JumpCloud customers to manage objects, groupings and mappings and interact with the JumpCloud Graph.  ## API Best Practices  Read the linked Help Article below for guidance on retrying failed requests to JumpCloud's REST API, as well as best practices for structuring subsequent retry requests. Customizing retry mechanisms based on these recommendations will increase the reliability and dependability of your API calls.  Covered topics include: 1. Important Considerations 2. Supported HTTP Request Methods 3. Response codes 4. API Key rotation 5. Paginating 6. Error handling 7. Retry rates  [JumpCloud Help Center - API Best Practices](https://support.jumpcloud.com/support/s/article/JumpCloud-API-Best-Practices)  # Directory Objects  This API offers the ability to interact with some of our core features; otherwise known as Directory Objects. The Directory Objects are:  * Commands * Policies * Policy Groups * Applications * Systems * Users * User Groups * System Groups * Radius Servers * Directories: Office 365, LDAP,G-Suite, Active Directory * Duo accounts and applications.  The Directory Object is an important concept to understand in order to successfully use JumpCloud API.  ## JumpCloud Graph  We've also introduced the concept of the JumpCloud Graph along with  Directory Objects. The Graph is a powerful aspect of our platform which will enable you to associate objects with each other, or establish membership for certain objects to become members of other objects.  Specific `GET` endpoints will allow you to traverse the JumpCloud Graph to return all indirect and directly bound objects in your organization.  | ![alt text](https://s3.amazonaws.com/jumpcloud-kb/Knowledge+Base+Photos/API+Docs/jumpcloud_graph.png \"JumpCloud Graph Model Example\") | |:--:| | **This diagram highlights our association and membership model as it relates to Directory Objects.** |  # API Key  ## Access Your API Key  To locate your API Key:  1. Log into the [JumpCloud Admin Console](https://console.jumpcloud.com/). 2. Go to the username drop down located in the top-right of the Console. 3. Retrieve your API key from API Settings.  ## API Key Considerations  This API key is associated to the currently logged in administrator. Other admins will have different API keys.  **WARNING** Please keep this API key secret, as it grants full access to any data accessible via your JumpCloud console account.  You can also reset your API key in the same location in the JumpCloud Admin Console.  ## Recycling or Resetting Your API Key  In order to revoke access with the current API key, simply reset your API key. This will render all calls using the previous API key inaccessible.  Your API key will be passed in as a header with the header name \"x-api-key\".  ```bash curl -H \"x-api-key: [YOUR_API_KEY_HERE]\" \"https://console.jumpcloud.com/api/v2/systemgroups\" ```  # System Context  * [Introduction](https://docs.jumpcloud.com) * [Supported endpoints](https://docs.jumpcloud.com) * [Response codes](https://docs.jumpcloud.com) * [Authentication](https://docs.jumpcloud.com) * [Additional examples](https://docs.jumpcloud.com) * [Third party](https://docs.jumpcloud.com)  ## Introduction  JumpCloud System Context Authorization is an alternative way to authenticate with a subset of JumpCloud's REST APIs. Using this method, a system can manage its information and resource associations, allowing modern auto provisioning environments to scale as needed.  **Notes:**   * The following documentation applies to Linux Operating Systems only.  * Systems that have been automatically enrolled using Apple's Device Enrollment Program (DEP) or systems enrolled using the User Portal install are not eligible to use the System Context API to prevent unauthorized access to system groups and resources. If a script that utilizes the System Context API is invoked on a system enrolled in this way, it will display an error.  ## Supported Endpoints  JumpCloud System Context Authorization can be used in conjunction with Systems endpoints found in the V1 API and certain System Group endpoints found in the v2 API.  * A system may fetch, alter, and delete metadata about itself, including manipulating a system's Group and Systemuser associations,   * `/api/systems/{system_id}` | [`GET`](https://docs.jumpcloud.com/api/1.0/index.html#operation/systems_get) [`PUT`](https://docs.jumpcloud.com/api/1.0/index.html#operation/systems_put) * A system may delete itself from your JumpCloud organization   * `/api/systems/{system_id}` | [`DELETE`](https://docs.jumpcloud.com/api/1.0/index.html#operation/systems_delete) * A system may fetch its direct resource associations under v2 (Groups)   * `/api/v2/systems/{system_id}/memberof` | [`GET`](https://docs.jumpcloud.com/api/2.0/index.html#operation/graph_systemGroupMembership)   * `/api/v2/systems/{system_id}/associations` | [`GET`](https://docs.jumpcloud.com/api/2.0/index.html#operation/graph_systemAssociationsList)   * `/api/v2/systems/{system_id}/users` | [`GET`](https://docs.jumpcloud.com/api/2.0/index.html#operation/graph_systemTraverseUser) * A system may alter its direct resource associations under v2 (Groups)   * `/api/v2/systems/{system_id}/associations` | [`POST`](https://docs.jumpcloud.com/api/2.0/index.html#operation/graph_systemAssociationsPost) * A system may alter its System Group associations   * `/api/v2/systemgroups/{group_id}/members` | [`POST`](https://docs.jumpcloud.com/api/2.0/index.html#operation/graph_systemGroupMembersPost)     * _NOTE_ If a system attempts to alter the system group membership of a different system the request will be rejected  ## Response Codes  If endpoints other than those described above are called using the System Context API, the server will return a `401` response.  ## Authentication  To allow for secure access to our APIs, you must authenticate each API request. JumpCloud System Context Authorization uses [HTTP Signatures](https://tools.ietf.org/html/draft-cavage-http-signatures-00) to authenticate API requests. The HTTP Signatures sent with each request are similar to the signatures used by the Amazon Web Services REST API. To help with the request-signing process, we have provided an [example bash script](https://github.com/TheJumpCloud/SystemContextAPI/blob/master/examples/shell/SigningExample.sh). This example API request simply requests the entire system record. You must be root, or have permissions to access the contents of the `/opt/jc` directory to generate a signature.  Here is a breakdown of the example script with explanations.  First, the script extracts the systemKey from the JSON formatted `/opt/jc/jcagent.conf` file.  ```bash #!/bin/bash conf=\"`cat /opt/jc/jcagent.conf`\" regex=\"systemKey\\\":\\\"(\\w+)\\\"\"  if [[ $conf =~ $regex ]] ; then   systemKey=\"${BASH_REMATCH[1]}\" fi ```  Then, the script retrieves the current date in the correct format.  ```bash now=`date -u \"+%a, %d %h %Y %H:%M:%S GMT\"`; ```  Next, we build a signing string to demonstrate the expected signature format. The signed string must consist of the [request-line](https://tools.ietf.org/html/rfc2616#page-35) and the date header, separated by a newline character.  ```bash signstr=\"GET /api/systems/${systemKey} HTTP/1.1\\ndate: ${now}\" ```  The next step is to calculate and apply the signature. This is a two-step process:  1. Create a signature from the signing string using the JumpCloud Agent private key: ``printf \"$signstr\" | openssl dgst -sha256 -sign /opt/jc/client.key`` 2. Then Base64-encode the signature string and trim off the newline characters: ``| openssl enc -e -a | tr -d '\\n'``  The combined steps above result in:  ```bash signature=`printf \"$signstr\" | openssl dgst -sha256 -sign /opt/jc/client.key | openssl enc -e -a | tr -d '\\n'` ; ```  Finally, we make sure the API call sending the signature has the same Authorization and Date header values, HTTP method, and URL that were used in the signing string.  ```bash curl -iq \\   -H \"Accept: application/json\" \\   -H \"Content-Type: application/json\" \\   -H \"Date: ${now}\" \\   -H \"Authorization: Signature keyId=\\\"system/${systemKey}\\\",headers=\\\"request-line date\\\",algorithm=\\\"rsa-sha256\\\",signature=\\\"${signature}\\\"\" \\   --url https://console.jumpcloud.com/api/systems/${systemKey} ```  ### Input Data  All PUT and POST methods should use the HTTP Content-Type header with a value of 'application/json'. PUT methods are used for updating a record. POST methods are used to create a record.  The following example demonstrates how to update the `displayName` of the system.  ```bash signstr=\"PUT /api/systems/${systemKey} HTTP/1.1\\ndate: ${now}\" signature=`printf \"$signstr\" | openssl dgst -sha256 -sign /opt/jc/client.key | openssl enc -e -a | tr -d '\\n'` ;  curl -iq \\   -d \"{\\\"displayName\\\" : \\\"updated-system-name-1\\\"}\" \\   -X \"PUT\" \\   -H \"Content-Type: application/json\" \\   -H \"Accept: application/json\" \\   -H \"Date: ${now}\" \\   -H \"Authorization: Signature keyId=\\\"system/${systemKey}\\\",headers=\\\"request-line date\\\",algorithm=\\\"rsa-sha256\\\",signature=\\\"${signature}\\\"\" \\   --url https://console.jumpcloud.com/api/systems/${systemKey} ```  ### Output Data  All results will be formatted as JSON.  Here is an abbreviated example of response output:  ```json {   \"_id\": \"625ee96f52e144993e000015\",   \"agentServer\": \"lappy386\",   \"agentVersion\": \"0.9.42\",   \"arch\": \"x86_64\",   \"connectionKey\": \"127.0.0.1_51812\",   \"displayName\": \"ubuntu-1204\",   \"firstContact\": \"2013-10-16T19:30:55.611Z\",   \"hostname\": \"ubuntu-1204\"   ... ```  ## Additional Examples  ### Signing Authentication Example  This example demonstrates how to make an authenticated request to fetch the JumpCloud record for this system.  [SigningExample.sh](https://github.com/TheJumpCloud/SystemContextAPI/blob/master/examples/shell/SigningExample.sh)  ### Shutdown Hook  This example demonstrates how to make an authenticated request on system shutdown. Using an init.d script registered at run level 0, you can call the System Context API as the system is shutting down.  [Instance-shutdown-initd](https://github.com/TheJumpCloud/SystemContextAPI/blob/master/examples/instance-shutdown-initd) is an example of an init.d script that only runs at system shutdown.  After customizing the [instance-shutdown-initd](https://github.com/TheJumpCloud/SystemContextAPI/blob/master/examples/instance-shutdown-initd) script, you should install it on the system(s) running the JumpCloud agent.  1. Copy the modified [instance-shutdown-initd](https://github.com/TheJumpCloud/SystemContextAPI/blob/master/examples/instance-shutdown-initd) to `/etc/init.d/instance-shutdown`. 2. On Ubuntu systems, run `update-rc.d instance-shutdown defaults`. On RedHat/CentOS systems, run `chkconfig --add instance-shutdown`.  ## Third Party  ### Chef Cookbooks  [https://github.com/nshenry03/jumpcloud](https://github.com/nshenry03/jumpcloud)  [https://github.com/cjs226/jumpcloud](https://github.com/cjs226/jumpcloud)  # Multi-Tenant Portal Headers  Multi-Tenant Organization API Headers are available for JumpCloud Admins to use when making API requests from Organizations that have multiple managed organizations.  The `x-org-id` is a required header for all multi-tenant admins when making API requests to JumpCloud. This header will define to which organization you would like to make the request.  **NOTE** Single Tenant Admins do not need to provide this header when making an API request.  ## Header Value  `x-org-id`  ## API Response Codes  * `400` Malformed ID. * `400` x-org-id and Organization path ID do not match. * `401` ID not included for multi-tenant admin * `403` ID included on unsupported route. * `404` Organization ID Not Found.  ```bash curl -X GET https://console.jumpcloud.com/api/v2/directories \\   -H 'accept: application/json' \\   -H 'content-type: application/json' \\   -H 'x-api-key: {API_KEY}' \\   -H 'x-org-id: {ORG_ID}'  ```  ## To Obtain an Individual Organization ID via the UI  As a prerequisite, your Primary Organization will need to be setup for Multi-Tenancy. This provides access to the Multi-Tenant Organization Admin Portal.  1. Log into JumpCloud [Admin Console](https://console.jumpcloud.com). If you are a multi-tenant Admin, you will automatically be routed to the Multi-Tenant Admin Portal. 2. From the Multi-Tenant Portal's primary navigation bar, select the Organization you'd like to access. 3. You will automatically be routed to that Organization's Admin Console. 4. Go to Settings in the sub-tenant's primary navigation. 5. You can obtain your Organization ID below your Organization's Contact Information on the Settings page.  ## To Obtain All Organization IDs via the API  * You can make an API request to this endpoint using the API key of your Primary Organization.  `https://console.jumpcloud.com/api/organizations/` This will return all your managed organizations.  ```bash curl -X GET \\   https://console.jumpcloud.com/api/organizations/ \\   -H 'Accept: application/json' \\   -H 'Content-Type: application/json' \\   -H 'x-api-key: {API_KEY}' ```  # SDKs  You can find language specific SDKs that can help you kickstart your Integration with JumpCloud in the following GitHub repositories:  * [Python](https://github.com/TheJumpCloud/jcapi-python) * [Go](https://github.com/TheJumpCloud/jcapi-go) * [Ruby](https://github.com/TheJumpCloud/jcapi-ruby) * [Java](https://github.com/TheJumpCloud/jcapi-java) 
 *
 * The version of the OpenAPI document: 2.0
 * Contact: support@jumpcloud.com
 *
 * NOTE: This class is auto generated by Konfig (https://konfigthis.com).
 * Do not edit the class manually.
 */


package com.konfigthis.client.api;

import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.model.CommandsGraphObjectWithPaths;
import com.konfigthis.client.model.GraphAttributeSudo;
import com.konfigthis.client.model.GraphConnection;
import com.konfigthis.client.model.GraphObjectWithPaths;
import com.konfigthis.client.model.GraphOperationActiveDirectory;
import com.konfigthis.client.model.GraphOperationApplication;
import com.konfigthis.client.model.GraphOperationCommand;
import com.konfigthis.client.model.GraphOperationGSuite;
import com.konfigthis.client.model.GraphOperationIDPRoutingPolicy;
import com.konfigthis.client.model.GraphOperationLdapServer;
import com.konfigthis.client.model.GraphOperationOffice365;
import com.konfigthis.client.model.GraphOperationPolicy;
import com.konfigthis.client.model.GraphOperationPolicyGroup;
import com.konfigthis.client.model.GraphOperationPolicyGroupMember;
import com.konfigthis.client.model.GraphOperationRadiusServer;
import com.konfigthis.client.model.GraphOperationSoftwareApp;
import com.konfigthis.client.model.GraphOperationSystem;
import com.konfigthis.client.model.GraphOperationSystemGroup;
import com.konfigthis.client.model.GraphOperationSystemGroupMember;
import com.konfigthis.client.model.GraphOperationUser;
import com.konfigthis.client.model.GraphOperationUserGroup;
import com.konfigthis.client.model.GraphOperationUserGroupMember;
import com.konfigthis.client.model.PolicyResult;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for GraphApi
 */
@Disabled
public class GraphApiTest {

    private static GraphApi api;

    
    @BeforeAll
    public static void beforeClass() {
        ApiClient apiClient = Configuration.getDefaultApiClient();
        api = new GraphApi(apiClient);
    }

    /**
     * List the associations of an Active Directory instance
     *
     * This endpoint returns the direct associations of this Active Directory instance.  A direct association can be a non-homogeneous relationship between 2 different objects, for example Active Directory and Users.   #### Sample Request &#x60;&#x60;&#x60; curl -X GET &#39;https://console.jumpcloud.com/api/v2/activedirectories/{ActiveDirectory_ID}/associations?targets&#x3D;user \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void activeDirectoryAssociationsListTest() throws ApiException {
        String activedirectoryId = null;
        List<String> targets = null;
        Integer limit = null;
        Integer skip = null;
        String xOrgId = null;
        List<GraphConnection> response = api.activeDirectoryAssociationsList(activedirectoryId, targets)
                .limit(limit)
                .skip(skip)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * Manage the associations of an Active Directory instance
     *
     * This endpoint allows you to manage the _direct_ associations of an Active Directory instance.  A direct association can be a non-homogeneous relationship between 2 different objects, for example Active Directory and Users.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/activedirectories/{AD_Instance_ID}/associations \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;op\&quot;: \&quot;add\&quot;,     \&quot;type\&quot;: \&quot;user\&quot;,     \&quot;id\&quot;: \&quot;{User_ID}\&quot;   }&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void activeDirectoryAssociationsPostTest() throws ApiException {
        String activedirectoryId = null;
        String id = null;
        String op = null;
        Map<String, Object> attributes = null;
        String type = null;
        String xOrgId = null;
        api.activeDirectoryAssociationsPost(activedirectoryId)
                .id(id)
                .op(op)
                .attributes(attributes)
                .type(type)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * List the Users bound to an Active Directory instance
     *
     * This endpoint will return all Users bound to an Active Directory instance, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this Active Directory instance to the corresponding User; this array represents all grouping and/or associations that would have to be removed to deprovision the User from this Active Directory instance.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/activedirectories/{ActiveDirectory_ID}/users \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void activeDirectoryTraverseUserTest() throws ApiException {
        String activedirectoryId = null;
        List<String> filter = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<GraphObjectWithPaths> response = api.activeDirectoryTraverseUser(activedirectoryId)
                .filter(filter)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .execute();
        // TODO: test validations
    }

    /**
     * List the User Groups bound to an Active Directory instance
     *
     * This endpoint will return all Users Groups bound to an Active Directory instance, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the group&#39;s type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this Active Directory instance to the corresponding User Group; this array represents all grouping and/or associations that would have to be removed to deprovision the User Group from this Active Directory instance.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/activedirectories/{ActiveDirectory_ID}/usergroups \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void activeDirectoryTraverseUserGroupTest() throws ApiException {
        String activedirectoryId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.activeDirectoryTraverseUserGroup(activedirectoryId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the associations of an Application
     *
     * This endpoint will return the _direct_ associations of an Application. A direct association can be a non-homogeneous relationship between 2 different objects, for example Applications and User Groups.   #### Sample Request &#x60;&#x60;&#x60; curl -X GET &#39;https://console.jumpcloud.com/api/v2/applications/{Application_ID}/associations?targets&#x3D;user_group \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void applicationAssociationsListTest() throws ApiException {
        String applicationId = null;
        List<String> targets = null;
        Integer limit = null;
        Integer skip = null;
        String xOrgId = null;
        List<GraphConnection> response = api.applicationAssociationsList(applicationId, targets)
                .limit(limit)
                .skip(skip)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * Manage the associations of an Application
     *
     * This endpoint allows you to manage the _direct_ associations of an Application. A direct association can be a non-homogeneous relationship between 2 different objects, for example Application and User Groups.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST &#39;https://console.jumpcloud.com/api/v2/applications/{Application_ID}/associations&#39; \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;op\&quot;: \&quot;add\&quot;,     \&quot;type\&quot;: \&quot;user_group\&quot;,     \&quot;id\&quot;: \&quot;{Group_ID}\&quot;   }&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void applicationAssociationsPostTest() throws ApiException {
        String applicationId = null;
        String id = null;
        String op = null;
        Map<String, Object> attributes = null;
        String type = null;
        String xOrgId = null;
        api.applicationAssociationsPost(applicationId)
                .id(id)
                .op(op)
                .attributes(attributes)
                .type(type)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * List the Users bound to an Application
     *
     * This endpoint will return all Users bound to an Application, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this Application to the corresponding User; this array represents all grouping and/or associations that would have to be removed to deprovision the User from this Application.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/applications/{Application_ID}/users \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void applicationTraverseUserTest() throws ApiException {
        String applicationId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.applicationTraverseUser(applicationId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the User Groups bound to an Application
     *
     * This endpoint will return all Users Groups bound to an Application, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the group&#39;s type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates  each path from this Application to the corresponding User Group; this array represents all grouping and/or associations that would have to be removed to deprovision the User Group from this Application.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/applications/{Application_ID}/usergroups \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void applicationTraverseUserGroupTest() throws ApiException {
        String applicationId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.applicationTraverseUserGroup(applicationId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the associations of a Command
     *
     * This endpoint will return the _direct_ associations of this Command.  A direct association can be a non-homogeneous relationship between 2 different objects, for example Commands and User Groups.   #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/commands/{Command_ID}/associations?targets&#x3D;system_group \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void commandAssociationsListTest() throws ApiException {
        String commandId = null;
        List<String> targets = null;
        Integer limit = null;
        Integer skip = null;
        String xOrgId = null;
        List<GraphConnection> response = api.commandAssociationsList(commandId, targets)
                .limit(limit)
                .skip(skip)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * Manage the associations of a Command
     *
     * This endpoint will allow you to manage the _direct_ associations of this Command.  A direct association can be a non-homogeneous relationship between 2 different objects, for example Commands and User Groups.   #### Sample Request &#x60;&#x60;&#x60;  curl -X POST https://console.jumpcloud.com/api/v2/commands/{Command_ID}/associations \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;op\&quot;: \&quot;add\&quot;,     \&quot;type\&quot;: \&quot;system_group\&quot;,     \&quot;id\&quot;: \&quot;Group_ID\&quot;   }&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void commandAssociationsPostTest() throws ApiException {
        String commandId = null;
        String id = null;
        String op = null;
        Map<String, Object> attributes = null;
        String type = null;
        String xOrgId = null;
        api.commandAssociationsPost(commandId)
                .id(id)
                .op(op)
                .attributes(attributes)
                .type(type)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * List the Systems bound to a Command
     *
     * This endpoint will return all Systems bound to a Command, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this Command to the corresponding System; this array represents all grouping and/or associations that would have to be removed to deprovision the System from this Command.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/commands/{Command_ID}/systems \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void commandTraverseSystemTest() throws ApiException {
        String commandId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.commandTraverseSystem(commandId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the System Groups bound to a Command
     *
     * This endpoint will return all System Groups bound to a Command, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the group&#39;s type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this Command to the corresponding System Group; this array represents all grouping and/or associations that would have to be removed to deprovision the System Group from this Command.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/commands/{Command_ID}/systemgroups \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void commandTraverseSystemGroupTest() throws ApiException {
        String commandId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.commandTraverseSystemGroup(commandId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the associations of a G Suite instance
     *
     * This endpoint returns the _direct_ associations of this G Suite instance.  A direct association can be a non-homogeneous relationship between 2 different objects, for example G Suite and Users.   #### Sample Request &#x60;&#x60;&#x60; curl -X GET &#39;https://console.jumpcloud.com/api/v2/gsuites/{Gsuite_ID}/associations?targets&#x3D;user_group \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void gSuiteAssociationsListTest() throws ApiException {
        String gsuiteId = null;
        List<String> targets = null;
        Integer limit = null;
        Integer skip = null;
        String xOrgId = null;
        List<GraphConnection> response = api.gSuiteAssociationsList(gsuiteId, targets)
                .limit(limit)
                .skip(skip)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * Manage the associations of a G Suite instance
     *
     * This endpoint returns the _direct_ associations of this G Suite instance.  A direct association can be a non-homogeneous relationship between 2 different objects, for example G Suite and Users.   #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/gsuites/{Gsuite_ID}/associations \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;op\&quot;: \&quot;add\&quot;,     \&quot;type\&quot;: \&quot;user_group\&quot;,     \&quot;id\&quot;: \&quot;{Group_ID}\&quot;   }&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void gSuiteAssociationsPostTest() throws ApiException {
        String gsuiteId = null;
        String id = null;
        String op = null;
        Map<String, Object> attributes = null;
        String type = null;
        String xOrgId = null;
        api.gSuiteAssociationsPost(gsuiteId)
                .id(id)
                .op(op)
                .attributes(attributes)
                .type(type)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * List the Users bound to a G Suite instance
     *
     * This endpoint will return all Users bound to a G Suite instance, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this G Suite instance to the corresponding User; this array represents all grouping and/or associations that would have to be removed to deprovision the User from this G Suite instance.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60;   curl -X GET https://console.jumpcloud.com/api/v2/gsuites/{Gsuite_ID}/users \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void gSuiteTraverseUserTest() throws ApiException {
        String gsuiteId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.gSuiteTraverseUser(gsuiteId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the User Groups bound to a G Suite instance
     *
     * This endpoint will return all User Groups bound to an G Suite instance, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the group&#39;s type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this G Suite instance to the corresponding User Group; this array represents all grouping and/or associations that would have to be removed to deprovision the User Group from this G Suite instance.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60;   curl -X GET https://console.jumpcloud.com/api/v2/gsuites/{GSuite_ID}/usergroups \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void gSuiteTraverseUserGroupTest() throws ApiException {
        String gsuiteId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.gSuiteTraverseUserGroup(gsuiteId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the associations of a Routing Policy
     *
     * This endpoint returns the _direct_ associations of a Routing Policy.  A direct association can be a non-homogeneous relationship between 2 different objects, for example a Routing Policy and Users.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/identity-provider/policies/{IDP_ROUTING_POLICY_ID}/associations?targets&#x3D;user_group \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void idpRoutingPolicyAssociationsListTest() throws ApiException {
        String idpRoutingPolicyId = null;
        List<String> targets = null;
        Integer limit = null;
        Integer skip = null;
        String xOrgId = null;
        List<GraphConnection> response = api.idpRoutingPolicyAssociationsList(idpRoutingPolicyId, targets)
                .limit(limit)
                .skip(skip)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * Manage the associations of a routing policy
     *
     * This endpoint manages the _direct_ associations of a Routing Policy.  A direct association can be a non-homogeneous relationship between 2 different objects, for example a Routing Policy and Users.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/identity-provider/policies/{IDP_ROUTING_POLICY_ID}/associations?targets&#x3D;user \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;   -d &#39;{\&quot;type\&quot;:\&quot;user\&quot;, \&quot;id\&quot;:\&quot;{USER_ID}\&quot;, \&quot;op\&quot;:\&quot;add\&quot;}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void idpRoutingPolicyAssociationsPostTest() throws ApiException {
        String idpRoutingPolicyId = null;
        String id = null;
        String op = null;
        Map<String, Object> attributes = null;
        String type = null;
        String xOrgId = null;
        api.idpRoutingPolicyAssociationsPost(idpRoutingPolicyId)
                .id(id)
                .op(op)
                .attributes(attributes)
                .type(type)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * List the Users bound to a Routing Policy
     *
     * This endpoint will return all Users bound to a routing policy, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this routing policy to the corresponding User; this array represents all grouping and/or associations that would have to be removed to deprovision the User from this routing policy.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.   #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/identity-provider/policies/{id}/associations/users \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;   &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void idpRoutingPolicyTraverseUserTest() throws ApiException {
        String idpRoutingPolicyId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.idpRoutingPolicyTraverseUser(idpRoutingPolicyId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the User Groups bound to a Routing Policy
     *
     * This endpoint will return all Users Groups bound to a routing policy, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the group&#39;s type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this routing policy instance to the corresponding User Group; this array represents all grouping and/or associations that would have to be removed to deprovision the User Group from this routing policy.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/identity-provider/policies/{id}/associations/usergroups \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void idpRoutingPolicyTraverseUserGroupTest() throws ApiException {
        String idpRoutingPolicyId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.idpRoutingPolicyTraverseUserGroup(idpRoutingPolicyId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the associations of a LDAP Server
     *
     * This endpoint returns the _direct_ associations of this LDAP Server.  A direct association can be a non-homogeneous relationship between 2 different objects, for example LDAP and Users.  #### Sample Request  &#x60;&#x60;&#x60;  curl -X GET &#39;https://console.jumpcloud.com/api/v2/ldapservers/{LDAP_ID}/associations?targets&#x3D;user_group \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void ldapServerAssociationsListTest() throws ApiException {
        String ldapserverId = null;
        List<String> targets = null;
        Integer limit = null;
        Integer skip = null;
        String xOrgId = null;
        List<GraphConnection> response = api.ldapServerAssociationsList(ldapserverId, targets)
                .limit(limit)
                .skip(skip)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * Manage the associations of a LDAP Server
     *
     * This endpoint allows you to manage the _direct_ associations of a LDAP Server.  A direct association can be a non-homogeneous relationship between 2 different objects, for example LDAP and Users.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/ldapservers/{LDAP_ID}/associations \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;op\&quot;: \&quot;add\&quot;,     \&quot;type\&quot;: \&quot;user\&quot;,     \&quot;id\&quot;: \&quot;{User_ID}\&quot;   }&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void ldapServerAssociationsPostTest() throws ApiException {
        String ldapserverId = null;
        String id = null;
        String op = null;
        Map<String, Object> attributes = null;
        String type = null;
        String xOrgId = null;
        api.ldapServerAssociationsPost(ldapserverId)
                .id(id)
                .op(op)
                .attributes(attributes)
                .type(type)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * List the Users bound to a LDAP Server
     *
     * This endpoint will return all Users bound to an LDAP Server, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this LDAP server instance to the corresponding User; this array represents all grouping and/or associations that would have to be removed to deprovision the User from this LDAP server instance.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/ldapservers/{LDAP_ID}/users \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void ldapServerTraverseUserTest() throws ApiException {
        String ldapserverId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.ldapServerTraverseUser(ldapserverId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the User Groups bound to a LDAP Server
     *
     * This endpoint will return all Users Groups bound to a LDAP Server, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the group&#39;s type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this LDAP server instance to the corresponding User Group; this array represents all grouping and/or associations that would have to be removed to deprovision the User Group from this LDAP server instance.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/ldapservers/{LDAP_ID}/usergroups \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void ldapServerTraverseUserGroupTest() throws ApiException {
        String ldapserverId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.ldapServerTraverseUserGroup(ldapserverId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the associations of an Office 365 instance
     *
     * This endpoint returns _direct_ associations of an Office 365 instance.   A direct association can be a non-homogeneous relationship between 2 different objects, for example Office 365 and Users.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET &#39;https://console.jumpcloud.com/api/v2/office365s/{OFFICE365_ID}/associations?targets&#x3D;user_group&#39; \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;  &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void office365AssociationsListTest() throws ApiException {
        String office365Id = null;
        List<String> targets = null;
        Integer limit = null;
        Integer skip = null;
        String xOrgId = null;
        List<GraphConnection> response = api.office365AssociationsList(office365Id, targets)
                .limit(limit)
                .skip(skip)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * Manage the associations of an Office 365 instance
     *
     * This endpoint allows you to manage the _direct_ associations of a Office 365 instance.  A direct association can be a non-homogeneous relationship between 2 different objects, for example Office 365 and Users.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/office365s/{OFFICE365_ID}/associations \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;op\&quot;: \&quot;add\&quot;,     \&quot;type\&quot;: \&quot;user_group\&quot;,     \&quot;id\&quot;: \&quot;{Group_ID}\&quot;   }&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void office365AssociationsPostTest() throws ApiException {
        String office365Id = null;
        String id = null;
        String op = null;
        Map<String, Object> attributes = null;
        String type = null;
        String xOrgId = null;
        api.office365AssociationsPost(office365Id)
                .id(id)
                .op(op)
                .attributes(attributes)
                .type(type)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * List the Users bound to an Office 365 instance
     *
     * This endpoint will return all Users bound to an Office 365 instance, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this Office 365 instance to the corresponding User; this array represents all grouping and/or associations that would have to be removed to deprovision the User from this Office 365 instance.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/office365s/{OFFICE365_ID}/users \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void office365TraverseUserTest() throws ApiException {
        String office365Id = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.office365TraverseUser(office365Id)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the User Groups bound to an Office 365 instance
     *
     * This endpoint will return all Users Groups bound to an Office 365 instance, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the group&#39;s type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this Office 365 instance to the corresponding User Group; this array represents all grouping and/or associations that would have to be removed to deprovision the User Group from this Office 365 instance.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60;   curl -X GET https://console.jumpcloud.com/api/v2/office365s/{OFFICE365_ID/usergroups \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void office365TraverseUserGroupTest() throws ApiException {
        String office365Id = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.office365TraverseUserGroup(office365Id)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the associations of a Policy
     *
     * This endpoint returns the _direct_ associations of a Policy.  A direct association can be a non-homogeneous relationship between 2 different objects, for example Policies and Systems.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET &#39;https://console.jumpcloud.com/api/v2/policies/{Policy_ID}/associations?targets&#x3D;system_group \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void policyAssociationsListTest() throws ApiException {
        String policyId = null;
        List<String> targets = null;
        Integer limit = null;
        Integer skip = null;
        String xOrgId = null;
        List<GraphConnection> response = api.policyAssociationsList(policyId, targets)
                .limit(limit)
                .skip(skip)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * Manage the associations of a Policy
     *
     * This endpoint allows you to manage the _direct_ associations of a Policy.  A direct association can be a non-homogeneous relationship between 2 different objects, for example Policies and Systems.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/policies/{Policy_ID}/associations/ \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;op\&quot;: \&quot;add\&quot;,     \&quot;type\&quot;: \&quot;system_group\&quot;,     \&quot;id\&quot;: \&quot;{Group_ID}\&quot;   }&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void policyAssociationsPostTest() throws ApiException {
        String policyId = null;
        String id = null;
        String op = null;
        Map<String, Object> attributes = null;
        String type = null;
        String xOrgId = null;
        api.policyAssociationsPost(policyId)
                .id(id)
                .op(op)
                .attributes(attributes)
                .type(type)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * List the associations of a Policy Group.
     *
     * This endpoint returns the _direct_ associations of this Policy Group.  A direct association can be a non-homogeneous relationship between 2 different objects, for example Policy Groups and Policies.   #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/policygroups/{GroupID}/associations?targets&#x3D;system \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void policyGroupAssociationsListTest() throws ApiException {
        String groupId = null;
        List<String> targets = null;
        Integer limit = null;
        Integer skip = null;
        String xOrgId = null;
        List<GraphConnection> response = api.policyGroupAssociationsList(groupId, targets)
                .limit(limit)
                .skip(skip)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * Manage the associations of a Policy Group
     *
     * This endpoint manages the _direct_ associations of this Policy Group.  A direct association can be a non-homogeneous relationship between 2 different objects, for example Policy Groups and Policies.   #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/policygroups/{GroupID}/associations \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;op\&quot;: \&quot;add\&quot;,     \&quot;type\&quot;: \&quot;system\&quot;,     \&quot;id\&quot;: \&quot;{SystemID}\&quot;   }&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void policyGroupAssociationsPostTest() throws ApiException {
        String groupId = null;
        String id = null;
        String op = null;
        Map<String, Object> attributes = null;
        String type = null;
        String xOrgId = null;
        api.policyGroupAssociationsPost(groupId)
                .id(id)
                .op(op)
                .attributes(attributes)
                .type(type)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * List the members of a Policy Group
     *
     * This endpoint returns the Policy members of a Policy Group.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/policygroups/{GroupID}/members \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void policyGroupMembersListTest() throws ApiException {
        String groupId = null;
        Integer limit = null;
        Integer skip = null;
        String xOrgId = null;
        List<GraphConnection> response = api.policyGroupMembersList(groupId)
                .limit(limit)
                .skip(skip)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * Manage the members of a Policy Group
     *
     * This endpoint allows you to manage the Policy members of a Policy Group.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/policygroups/{GroupID}/members \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;op\&quot;: \&quot;add\&quot;,     \&quot;type\&quot;: \&quot;policy\&quot;,     \&quot;id\&quot;: \&quot;{Policy_ID}\&quot;   }&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void policyGroupMembersPostTest() throws ApiException {
        String groupId = null;
        String id = null;
        String op = null;
        Map<String, Object> attributes = null;
        String type = null;
        String xOrgId = null;
        api.policyGroupMembersPost(groupId)
                .id(id)
                .op(op)
                .attributes(attributes)
                .type(type)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * List the Policy Group&#39;s membership
     *
     * This endpoint returns all Policy members that are a member of this Policy Group.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/policygroups/{GroupID}/membership \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void policyGroupMembershipTest() throws ApiException {
        String groupId = null;
        List<String> filter = null;
        Integer limit = null;
        Integer skip = null;
        List<String> sort = null;
        String xOrgId = null;
        List<GraphObjectWithPaths> response = api.policyGroupMembership(groupId)
                .filter(filter)
                .limit(limit)
                .skip(skip)
                .sort(sort)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * List the Systems bound to a Policy Group
     *
     * This endpoint will return all Systems bound to a Policy Group, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this Policy Group to the corresponding System; this array represents all grouping and/or associations that would have to be removed to deprovision the System from this Policy Group.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/policygroups/{GroupID}/systems \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void policyGroupTraverseSystemTest() throws ApiException {
        String groupId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.policyGroupTraverseSystem(groupId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the System Groups bound to Policy Groups
     *
     * This endpoint will return all System Groups bound to a Policy Group, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this Policy Group to the corresponding System Group; this array represents all grouping and/or associations that would have to be removed to deprovision the System Group from this Policy Group.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/policygroups/{GroupID}/systemgroups \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void policyGroupTraverseSystemGroupTest() throws ApiException {
        String groupId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.policyGroupTraverseSystemGroup(groupId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the parent Groups of a Policy
     *
     * This endpoint returns all the Policy Groups a Policy is a member of.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/policies/{Policy_ID}/memberof \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;  &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void policyMemberOfTest() throws ApiException {
        String policyId = null;
        List<String> filter = null;
        Integer limit = null;
        Integer skip = null;
        String date = null;
        String authorization = null;
        List<String> sort = null;
        String xOrgId = null;
        List<GraphObjectWithPaths> response = api.policyMemberOf(policyId)
                .filter(filter)
                .limit(limit)
                .skip(skip)
                .date(date)
                .authorization(authorization)
                .sort(sort)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * List the Systems bound to a Policy
     *
     * This endpoint will return all Systems bound to a Policy, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this Policy to the corresponding System; this array represents all grouping and/or associations that would have to be removed to deprovision the System from this Policy.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/policies/{Policy_ID}/systems \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void policyTraverseSystemTest() throws ApiException {
        String policyId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.policyTraverseSystem(policyId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the System Groups bound to a Policy
     *
     * This endpoint will return all Systems Groups bound to a Policy, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the group&#39;s type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this Policy to the corresponding System Group; this array represents all grouping and/or associations that would have to be removed to deprovision the System Group from this Policy.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET  https://console.jumpcloud.com/api/v2/policies/{Policy_ID}/systemgroups \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void policyTraverseSystemGroupTest() throws ApiException {
        String policyId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.policyTraverseSystemGroup(policyId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the associations of a RADIUS  Server
     *
     * This endpoint returns the _direct_ associations of a Radius Server.  A direct association can be a non-homogeneous relationship between 2 different objects, for example Radius Servers and Users.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/radiusservers/{RADIUS_ID}/associations?targets&#x3D;user_group \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void radiusServerAssociationsListTest() throws ApiException {
        String radiusserverId = null;
        List<String> targets = null;
        Integer limit = null;
        Integer skip = null;
        String xOrgId = null;
        List<GraphConnection> response = api.radiusServerAssociationsList(radiusserverId, targets)
                .limit(limit)
                .skip(skip)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * Manage the associations of a RADIUS Server
     *
     * This endpoint allows you to manage the _direct_ associations of a Radius Server.  A direct association can be a non-homogeneous relationship between 2 different objects, for example Radius Servers and Users.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/radiusservers/{RADIUS_ID}/associations \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{    \&quot;type\&quot;:\&quot;user\&quot;,  \&quot;id\&quot;:\&quot;{USER_ID}\&quot;,  \&quot;op\&quot;:\&quot;add\&quot;   }&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void radiusServerAssociationsPostTest() throws ApiException {
        String radiusserverId = null;
        String id = null;
        String op = null;
        Map<String, Object> attributes = null;
        String type = null;
        String xOrgId = null;
        api.radiusServerAssociationsPost(radiusserverId)
                .id(id)
                .op(op)
                .attributes(attributes)
                .type(type)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * List the Users bound to a RADIUS  Server
     *
     * This endpoint will return all Users bound to a RADIUS Server, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this RADIUS server instance to the corresponding User; this array represents all grouping and/or associations that would have to be removed to deprovision the User from this RADIUS server instance.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.   #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/ldapservers/{LDAP_ID}/users \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;   &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void radiusServerTraverseUserTest() throws ApiException {
        String radiusserverId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.radiusServerTraverseUser(radiusserverId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the User Groups bound to a RADIUS  Server
     *
     * This endpoint will return all Users Groups bound to a RADIUS Server, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the group&#39;s type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this RADIUS server instance to the corresponding User Group; this array represents all grouping and/or associations that would have to be removed to deprovision the User Group from this RADIUS server instance.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/radiusservers/{RADIUS_ID}/usergroups \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void radiusServerTraverseUserGroupTest() throws ApiException {
        String radiusserverId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.radiusServerTraverseUserGroup(radiusserverId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the associations of a Software Application
     *
     * This endpoint will return the _direct_ associations of a Software Application. A direct association can be a non-homogeneous relationship between 2 different objects, for example Software Application and System Groups.   #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/softwareapps/{software_app_id}/associations?targets&#x3D;system_group \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void softwareappsAssociationsListTest() throws ApiException {
        String softwareAppId = null;
        List<String> targets = null;
        Integer limit = null;
        Integer skip = null;
        String xOrgId = null;
        List<GraphConnection> response = api.softwareappsAssociationsList(softwareAppId, targets)
                .limit(limit)
                .skip(skip)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * Manage the associations of a software application.
     *
     * This endpoint allows you to associate or disassociate a software application to a system or system group.  #### Sample Request &#x60;&#x60;&#x60; $ curl -X POST https://console.jumpcloud.com/api/v2/softwareapps/{software_app_id}/associations \\ -H &#39;Accept: application/json&#39; \\ -H &#39;Content-Type: application/json&#39; \\ -H &#39;x-api-key: {API_KEY}&#39; \\ -d &#39;{   \&quot;id\&quot;: \&quot;&lt;object_id&gt;\&quot;,   \&quot;op\&quot;: \&quot;add\&quot;,   \&quot;type\&quot;: \&quot;system\&quot;  }&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void softwareappsAssociationsPostTest() throws ApiException {
        String softwareAppId = null;
        String id = null;
        String op = null;
        Map<String, Object> attributes = null;
        String type = null;
        String xOrgId = null;
        api.softwareappsAssociationsPost(softwareAppId)
                .id(id)
                .op(op)
                .attributes(attributes)
                .type(type)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * List the Systems bound to a Software App.
     *
     * This endpoint will return all Systems bound to a Software App, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this Software App to the corresponding System; this array represents all grouping and/or associations that would have to be removed to deprovision the System from this Software App.  See &#x60;/associations&#x60; endpoint to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/softwareapps/{software_app_id}/systems \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void softwareappsTraverseSystemTest() throws ApiException {
        String softwareAppId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.softwareappsTraverseSystem(softwareAppId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the System Groups bound to a Software App.
     *
     * This endpoint will return all Systems Groups bound to a Software App, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the group&#39;s type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this Software App to the corresponding System Group; this array represents all grouping and/or associations that would have to be removed to deprovision the System Group from this Software App.  See &#x60;/associations&#x60; endpoint to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET  https://console.jumpcloud.com/api/v2/softwareapps/{software_app_id}/systemgroups \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void softwareappsTraverseSystemGroupTest() throws ApiException {
        String softwareAppId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.softwareappsTraverseSystemGroup(softwareAppId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the associations of a System
     *
     * This endpoint returns the _direct_ associations of a System.  A direct association can be a non-homogeneous relationship between 2 different objects, for example Systems and Users.   #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/systems/{System_ID}/associations?targets&#x3D;user \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;  &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void systemAssociationsListTest() throws ApiException {
        String systemId = null;
        List<String> targets = null;
        Integer limit = null;
        Integer skip = null;
        String date = null;
        String authorization = null;
        String xOrgId = null;
        List<GraphConnection> response = api.systemAssociationsList(systemId, targets)
                .limit(limit)
                .skip(skip)
                .date(date)
                .authorization(authorization)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * Manage associations of a System
     *
     * This endpoint allows you to manage the _direct_ associations of a System.  A direct association can be a non-homogeneous relationship between 2 different objects, for example Systems and Users.   #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/systems/{System_ID}/associations \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;attributes\&quot;: {       \&quot;sudo\&quot;: {         \&quot;enabled\&quot;: true,         \&quot;withoutPassword\&quot;: false       }     },     \&quot;op\&quot;: \&quot;add\&quot;,     \&quot;type\&quot;: \&quot;user\&quot;,     \&quot;id\&quot;: \&quot;UserID\&quot;   }&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void systemAssociationsPostTest() throws ApiException {
        String systemId = null;
        String id = null;
        String op = null;
        Map attributes = null;
        String type = null;
        String date = null;
        String authorization = null;
        String xOrgId = null;
        api.systemAssociationsPost(systemId)
                .id(id)
                .op(op)
                .attributes(attributes)
                .type(type)
                .date(date)
                .authorization(authorization)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * List the associations of a System Group
     *
     * This endpoint returns the _direct_ associations of a System Group.  A direct association can be a non-homogeneous relationship between 2 different objects, for example System Groups and Users.   #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/systemgroups/{GroupID}/associations?targets&#x3D;user \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void systemGroupAssociationsListTest() throws ApiException {
        String groupId = null;
        List<String> targets = null;
        Integer limit = null;
        Integer skip = null;
        String xOrgId = null;
        List<GraphConnection> response = api.systemGroupAssociationsList(groupId, targets)
                .limit(limit)
                .skip(skip)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * Manage the associations of a System Group
     *
     * This endpoint allows you to manage the _direct_ associations of a System Group.  A direct association can be a non-homogeneous relationship between 2 different objects, for example System Groups and Users.   #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/systemgroups/{GroupID}/associations \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;op\&quot;: \&quot;add\&quot;,     \&quot;type\&quot;: \&quot;user\&quot;,     \&quot;id\&quot;: \&quot;{UserID}\&quot;   }&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void systemGroupAssociationsPostTest() throws ApiException {
        String groupId = null;
        String id = null;
        String op = null;
        Map<String, Object> attributes = null;
        String type = null;
        String xOrgId = null;
        api.systemGroupAssociationsPost(groupId)
                .id(id)
                .op(op)
                .attributes(attributes)
                .type(type)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * List the members of a System Group
     *
     * This endpoint returns the system members of a System Group.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/systemgroups/{Group_ID}/members \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;  &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void systemGroupMembersListTest() throws ApiException {
        String groupId = null;
        Integer limit = null;
        Integer skip = null;
        String xOrgId = null;
        List<GraphConnection> response = api.systemGroupMembersList(groupId)
                .limit(limit)
                .skip(skip)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * Manage the members of a System Group
     *
     * This endpoint allows you to manage the system members of a System Group.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/systemgroups/{Group_ID}/members \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;op\&quot;: \&quot;add\&quot;,     \&quot;type\&quot;: \&quot;system\&quot;,     \&quot;id\&quot;: \&quot;{System_ID}\&quot;   }&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void systemGroupMembersPostTest() throws ApiException {
        String groupId = null;
        String id = null;
        String op = null;
        Map<String, Object> attributes = null;
        String type = null;
        String date = null;
        String authorization = null;
        String xOrgId = null;
        api.systemGroupMembersPost(groupId)
                .id(id)
                .op(op)
                .attributes(attributes)
                .type(type)
                .date(date)
                .authorization(authorization)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * List the System Group&#39;s membership
     *
     * This endpoint returns all Systems that are a member of this System Group.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/systemgroups/{Group_ID/membership \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;  &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void systemGroupMembershipTest() throws ApiException {
        String groupId = null;
        Integer limit = null;
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        List<GraphObjectWithPaths> response = api.systemGroupMembership(groupId)
                .limit(limit)
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * List the Commands bound to a System Group
     *
     * This endpoint will return all Commands bound to a System Group, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the group&#39;s type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this System Group to the corresponding Command; this array represents all grouping and/or associations that would have to be removed to deprovision the Command from this System Group.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/systemgroups/{GroupID}/commands \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void systemGroupTraverseCommandTest() throws ApiException {
        String groupId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        String details = null;
        List<CommandsGraphObjectWithPaths> response = api.systemGroupTraverseCommand(groupId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .details(details)
                .execute();
        // TODO: test validations
    }

    /**
     * List the Policies bound to a System Group
     *
     * This endpoint will return all Policies bound to a System Group, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this System Group to the corresponding Policy; this array represents all grouping and/or associations that would have to be removed to deprovision the Policy from this System Group.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  This endpoint is not public yet as we haven&#39;t finished the code.  ##### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/systemgroups/{GroupID}/policies \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void systemGroupTraversePolicyTest() throws ApiException {
        String groupId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.systemGroupTraversePolicy(groupId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the Policy Groups bound to a System Group
     *
     * This endpoint will return all Policy Groups bound to a System Group, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this System Group to the corresponding Policy Group; this array represents all grouping and/or associations that would have to be removed to deprovision the Policy Group from this System Group.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/systemgroups/{GroupID}/policygroups \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;  &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void systemGroupTraversePolicyGroupTest() throws ApiException {
        String groupId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.systemGroupTraversePolicyGroup(groupId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the Users bound to a System Group
     *
     * This endpoint will return all Users bound to a System Group, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this System Group to the corresponding User; this array represents all grouping and/or associations that would have to be removed to deprovision the User from this System Group.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/systemgroups/{GroupID}/users \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void systemGroupTraverseUserTest() throws ApiException {
        String groupId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.systemGroupTraverseUser(groupId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the User Groups bound to a System Group
     *
     * This endpoint will return all User Groups bound to a System Group, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this System Group to the corresponding User Group; this array represents all grouping and/or associations that would have to be removed to deprovision the User Group from this System Group.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/systemgroups/{GroupID}/usergroups \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;  &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void systemGroupTraverseUserGroupTest() throws ApiException {
        String groupId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.systemGroupTraverseUserGroup(groupId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the parent Groups of a System
     *
     * This endpoint returns all the System Groups a System is a member of.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/systems/{System_ID}/memberof \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;  &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void systemMemberOfTest() throws ApiException {
        String systemId = null;
        List<String> filter = null;
        Integer limit = null;
        Integer skip = null;
        String date = null;
        String authorization = null;
        List<String> sort = null;
        String xOrgId = null;
        List<GraphObjectWithPaths> response = api.systemMemberOf(systemId)
                .filter(filter)
                .limit(limit)
                .skip(skip)
                .date(date)
                .authorization(authorization)
                .sort(sort)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * List the Commands bound to a System
     *
     * This endpoint will return all Commands bound to a System, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this System to the corresponding Command; this array represents all grouping and/or associations that would have to be removed to deprovision the Command from this System.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/systems/{System_ID}/commands \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;  &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void systemTraverseCommandTest() throws ApiException {
        String systemId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        String details = null;
        List<CommandsGraphObjectWithPaths> response = api.systemTraverseCommand(systemId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .details(details)
                .execute();
        // TODO: test validations
    }

    /**
     * List the Policies bound to a System
     *
     * This endpoint will return all Policies bound to a System, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this System to the corresponding Policy; this array represents all grouping and/or associations that would have to be removed to deprovision the Policy from this System.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  This endpoint is not yet public as we have finish the code.  ##### Sample Request  &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/{System_ID}/policies \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;  &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void systemTraversePolicyTest() throws ApiException {
        String systemId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.systemTraversePolicy(systemId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the Policy Groups bound to a System
     *
     * This endpoint will return all Policy Groups bound to a System, either directly or indirectly essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this System to the corresponding Policy Group; this array represents all grouping and/or associations that would have to be removed to deprovision the Policy Group from this System.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/systems/{System_ID}/policygroups \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;  &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void systemTraversePolicyGroupTest() throws ApiException {
        String systemId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        String date = null;
        String authorization = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.systemTraversePolicyGroup(systemId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .date(date)
                .authorization(authorization)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the Users bound to a System
     *
     * This endpoint will return all Users bound to a System, either directly or indirectly essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this System to the corresponding User; this array represents all grouping and/or associations that would have to be removed to deprovision the User from this System.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/systems/{System_ID}/users \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;  &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void systemTraverseUserTest() throws ApiException {
        String systemId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        String date = null;
        String authorization = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.systemTraverseUser(systemId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .date(date)
                .authorization(authorization)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the User Groups bound to a System
     *
     * This endpoint will return all User Groups bound to a System, either directly or indirectly essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this System to the corresponding User Group; this array represents all grouping and/or associations that would have to be removed to deprovision the User Group from this System.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/systems/{System_ID}/usergroups \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;  &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void systemTraverseUserGroupTest() throws ApiException {
        String systemId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        String date = null;
        String authorization = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.systemTraverseUserGroup(systemId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .date(date)
                .authorization(authorization)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the policy statuses for a system
     *
     * This endpoint returns the policy results for a particular system.  ##### Sample Request  &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/systems/{System_ID}/policystatuses \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;  &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void systemsListTest() throws ApiException {
        String systemId = null;
        List<String> fields = null;
        List<String> filter = null;
        Integer limit = null;
        Integer skip = null;
        List<String> sort = null;
        String xOrgId = null;
        List<PolicyResult> response = api.systemsList(systemId)
                .fields(fields)
                .filter(filter)
                .limit(limit)
                .skip(skip)
                .sort(sort)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * List the associations of a User
     *
     * This endpoint returns the _direct_ associations of a User.  A direct association can be a non-homogeneous relationship between 2 different objects, for example Users and Systems.   #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/users/{UserID}/associations?targets&#x3D;system_group \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;  &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void userAssociationsListTest() throws ApiException {
        String userId = null;
        List<String> targets = null;
        Integer limit = null;
        Integer skip = null;
        String xOrgId = null;
        List<GraphConnection> response = api.userAssociationsList(userId, targets)
                .limit(limit)
                .skip(skip)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * Manage the associations of a User
     *
     * This endpoint allows you to manage the _direct_ associations of a User.  A direct association can be a non-homogeneous relationship between 2 different objects, for example Users and Systems.   #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/users/{UserID}/associations \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;attributes\&quot;: {       \&quot;sudo\&quot;: {       \&quot;enabled\&quot;: true,         \&quot;withoutPassword\&quot;: false       }     },     \&quot;op\&quot;: \&quot;add\&quot;,     \&quot;type\&quot;: \&quot;system_group\&quot;,     \&quot;id\&quot;: \&quot;{GroupID}\&quot;   }&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void userAssociationsPostTest() throws ApiException {
        String userId = null;
        String id = null;
        String op = null;
        Map attributes = null;
        String type = null;
        String xOrgId = null;
        api.userAssociationsPost(userId)
                .id(id)
                .op(op)
                .attributes(attributes)
                .type(type)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * List the associations of a User Group.
     *
     * This endpoint returns the _direct_ associations of this User Group.  A direct association can be a non-homogeneous relationship between 2 different objects, for example User Groups and Users.   #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/usergroups/{GroupID}/associations?targets&#x3D;system \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void userGroupAssociationsListTest() throws ApiException {
        String groupId = null;
        List<String> targets = null;
        Integer limit = null;
        Integer skip = null;
        String xOrgId = null;
        List<GraphConnection> response = api.userGroupAssociationsList(groupId, targets)
                .limit(limit)
                .skip(skip)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * Manage the associations of a User Group
     *
     * This endpoint manages the _direct_ associations of this User Group.  A direct association can be a non-homogeneous relationship between 2 different objects, for example User Groups and Users.   #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/usergroups/{GroupID}/associations \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;op\&quot;: \&quot;add\&quot;,     \&quot;type\&quot;: \&quot;system\&quot;,     \&quot;id\&quot;: \&quot;{SystemID}\&quot;   }&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void userGroupAssociationsPostTest() throws ApiException {
        String groupId = null;
        String id = null;
        String op = null;
        Map<String, Object> attributes = null;
        String type = null;
        String xOrgId = null;
        api.userGroupAssociationsPost(groupId)
                .id(id)
                .op(op)
                .attributes(attributes)
                .type(type)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * List the members of a User Group
     *
     * This endpoint returns the user members of a User Group.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/usergroups/{GroupID}/members \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void userGroupMembersListTest() throws ApiException {
        String groupId = null;
        Integer limit = null;
        Integer skip = null;
        String xOrgId = null;
        List<GraphConnection> response = api.userGroupMembersList(groupId)
                .limit(limit)
                .skip(skip)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * Manage the members of a User Group
     *
     * This endpoint allows you to manage the user members of a User Group.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/usergroups/{GroupID}/members \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;op\&quot;: \&quot;add\&quot;,     \&quot;type\&quot;: \&quot;user\&quot;,     \&quot;id\&quot;: \&quot;{User_ID}\&quot;   }&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void userGroupMembersPostTest() throws ApiException {
        String groupId = null;
        String id = null;
        String op = null;
        Map<String, Object> attributes = null;
        String type = null;
        String xOrgId = null;
        api.userGroupMembersPost(groupId)
                .id(id)
                .op(op)
                .attributes(attributes)
                .type(type)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * List the User Group&#39;s membership
     *
     * This endpoint returns all users members that are a member of this User Group.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/usergroups/{GroupID}/membership \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void userGroupMembershipTest() throws ApiException {
        String groupId = null;
        List<String> filter = null;
        Integer limit = null;
        Integer skip = null;
        List<String> sort = null;
        String xOrgId = null;
        List<GraphObjectWithPaths> response = api.userGroupMembership(groupId)
                .filter(filter)
                .limit(limit)
                .skip(skip)
                .sort(sort)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * List the Active Directories bound to a User Group
     *
     * This endpoint will return all Active Directory Instances bound to a User Group, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User Group to the corresponding Active Directory; this array represents all grouping and/or associations that would have to be removed to deprovision the Active Directory from this User Group.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/usergroups/{GroupID}/activedirectories \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void userGroupTraverseActiveDirectoryTest() throws ApiException {
        String groupId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.userGroupTraverseActiveDirectory(groupId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the Applications bound to a User Group
     *
     * This endpoint will return all Applications bound to a User Group, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User Group to the corresponding Application; this array represents all grouping and/or associations that would have to be removed to deprovision the Application from this User Group.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/usergroups/{GroupID}/applications \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void userGroupTraverseApplicationTest() throws ApiException {
        String groupId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.userGroupTraverseApplication(groupId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the Directories bound to a User Group
     *
     * This endpoint will return all Directories bound to a User Group, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User Group to the corresponding Directory; this array represents all grouping and/or associations that would have to be removed to deprovision the Directories from this User Group.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/usergroups/{GroupID}/directories \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;  &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void userGroupTraverseDirectoryTest() throws ApiException {
        String groupId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.userGroupTraverseDirectory(groupId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the G Suite instances bound to a User Group
     *
     * This endpoint will return all G Suite Instances bound to a User Group, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User Group to the corresponding G Suite instance; this array represents all grouping and/or associations that would have to be removed to deprovision the G Suite instance from this User Group.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/usergroups/{GroupID/gsuites \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;  &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void userGroupTraverseGSuiteTest() throws ApiException {
        String groupId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.userGroupTraverseGSuite(groupId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the LDAP Servers bound to a User Group
     *
     * This endpoint will return all LDAP Servers bound to a User Group, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User Group to the corresponding LDAP Server; this array represents all grouping and/or associations that would have to be removed to deprovision the LDAP Server from this User Group.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/usergroups/{GroupID}/ldapservers \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void userGroupTraverseLdapServerTest() throws ApiException {
        String groupId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.userGroupTraverseLdapServer(groupId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the Office 365 instances bound to a User Group
     *
     * This endpoint will return all Office 365 instances bound to a User Group, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User Group to the corresponding Office 365 instance; this array represents all grouping and/or associations that would have to be removed to deprovision the Office 365 instance from this User Group.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/usergroups/{GroupID}/office365s \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void userGroupTraverseOffice365Test() throws ApiException {
        String groupId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.userGroupTraverseOffice365(groupId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the RADIUS Servers bound to a User Group
     *
     * This endpoint will return all RADIUS servers bound to a User Group, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User Group to the corresponding RADIUS Server; this array represents all grouping and/or associations that would have to be removed to deprovision the RADIUS Server from this User Group.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/usergroups/{GroupID}/radiusservers \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;  &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void userGroupTraverseRadiusServerTest() throws ApiException {
        String groupId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.userGroupTraverseRadiusServer(groupId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the Systems bound to a User Group
     *
     * This endpoint will return all Systems bound to a User Group, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User Group to the corresponding System; this array represents all grouping and/or associations that would have to be removed to deprovision the System from this User Group.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/usergroups/{GroupID}/systems \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void userGroupTraverseSystemTest() throws ApiException {
        String groupId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.userGroupTraverseSystem(groupId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the System Groups bound to User Groups
     *
     * This endpoint will return all System Groups bound to a User Group, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User Group to the corresponding System Group; this array represents all grouping and/or associations that would have to be removed to deprovision the System Group from this User Group.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/usergroups/{GroupID}/systemgroups \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void userGroupTraverseSystemGroupTest() throws ApiException {
        String groupId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.userGroupTraverseSystemGroup(groupId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the parent Groups of a User
     *
     * This endpoint returns all the User Groups a User is a member of.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/users/{UserID}/memberof \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void userMemberOfTest() throws ApiException {
        String userId = null;
        List<String> filter = null;
        Integer limit = null;
        Integer skip = null;
        List<String> sort = null;
        String xOrgId = null;
        List<GraphObjectWithPaths> response = api.userMemberOf(userId)
                .filter(filter)
                .limit(limit)
                .skip(skip)
                .sort(sort)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * List the Active Directory instances bound to a User
     *
     * This endpoint will return all Active Directory Instances bound to a User, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User to the corresponding Active Directory instance; this array represents all grouping and/or associations that would have to be removed to deprovision the Active Directory instance from this User.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/users/{UserID}/activedirectories \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void userTraverseActiveDirectoryTest() throws ApiException {
        String userId = null;
        List<String> filter = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<GraphObjectWithPaths> response = api.userTraverseActiveDirectory(userId)
                .filter(filter)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .execute();
        // TODO: test validations
    }

    /**
     * List the Applications bound to a User
     *
     * This endpoint will return all Applications bound to a User, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User to the corresponding Application; this array represents all grouping and/or associations that would have to be removed to deprovision the Application from this User.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/users/{UserID}/applications \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void userTraverseApplicationTest() throws ApiException {
        String userId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.userTraverseApplication(userId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the Directories bound to a User
     *
     * This endpoint will return all Directories bound to a User, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User to the corresponding Directory; this array represents all grouping and/or associations that would have to be removed to deprovision the Directory from this User.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/users/{UserID}/directories \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void userTraverseDirectoryTest() throws ApiException {
        String userId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.userTraverseDirectory(userId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the G Suite instances bound to a User
     *
     * This endpoint will return all G-Suite Instances bound to a User, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User to the corresponding G Suite instance; this array represents all grouping and/or associations that would have to be removed to deprovision the G Suite instance from this User.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/users/{UserID}/gsuites \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void userTraverseGSuiteTest() throws ApiException {
        String userId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.userTraverseGSuite(userId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the LDAP servers bound to a User
     *
     * This endpoint will return all LDAP Servers bound to a User, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User to the corresponding LDAP Server; this array represents all grouping and/or associations that would have to be removed to deprovision the LDAP Server from this User.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/users/{UserID}/ldapservers \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void userTraverseLdapServerTest() throws ApiException {
        String userId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.userTraverseLdapServer(userId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the Office 365 instances bound to a User
     *
     * This endpoint will return all Office 365 Instances bound to a User, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User to the corresponding Office 365 instance; this array represents all grouping and/or associations that would have to be removed to deprovision the Office 365 instance from this User.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/users/{UserID}/office365s \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void userTraverseOffice365Test() throws ApiException {
        String userId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.userTraverseOffice365(userId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the RADIUS Servers bound to a User
     *
     * This endpoint will return all RADIUS Servers bound to a User, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User to the corresponding RADIUS Server; this array represents all grouping and/or associations that would have to be removed to deprovision the RADIUS Server from this User.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/users/{UserID}/radiusservers \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void userTraverseRadiusServerTest() throws ApiException {
        String userId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.userTraverseRadiusServer(userId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the Systems bound to a User
     *
     * This endpoint will return all Systems bound to a User, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User to the corresponding System; this array represents all grouping and/or associations that would have to be removed to deprovision the System from this User.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/users/{UserID}/systems\\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void userTraverseSystemTest() throws ApiException {
        String userId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.userTraverseSystem(userId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List the System Groups bound to a User
     *
     * This endpoint will return all System Groups bound to a User, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User to the corresponding System Group; this array represents all grouping and/or associations that would have to be removed to deprovision the System Group from this User.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/users/{UserID}/systemgroups\\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void userTraverseSystemGroupTest() throws ApiException {
        String userId = null;
        Integer limit = null;
        String xOrgId = null;
        Integer skip = null;
        List<String> filter = null;
        List<GraphObjectWithPaths> response = api.userTraverseSystemGroup(userId)
                .limit(limit)
                .xOrgId(xOrgId)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

}
