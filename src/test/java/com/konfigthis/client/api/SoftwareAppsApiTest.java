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
import com.konfigthis.client.model.GraphConnection;
import com.konfigthis.client.model.GraphObjectWithPaths;
import com.konfigthis.client.model.GraphOperationSoftwareApp;
import com.konfigthis.client.model.JumpcloudPackageValidatorValidateApplicationInstallPackageRequest;
import com.konfigthis.client.model.JumpcloudPackageValidatorValidateApplicationInstallPackageResponse;
import com.konfigthis.client.model.SoftwareApp;
import com.konfigthis.client.model.SoftwareAppCreate;
import com.konfigthis.client.model.SoftwareAppReclaimLicenses;
import com.konfigthis.client.model.SoftwareAppSettings;
import com.konfigthis.client.model.SoftwareAppStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for SoftwareAppsApi
 */
@Disabled
public class SoftwareAppsApiTest {

    private static SoftwareAppsApi api;

    
    @BeforeAll
    public static void beforeClass() {
        ApiClient apiClient = Configuration.getDefaultApiClient();
        api = new SoftwareAppsApi(apiClient);
    }

    /**
     * Delete a configured Software Application
     *
     * Removes a Software Application configuration.  Warning: This is a destructive operation and will unmanage the application on all affected systems.  #### Sample Request &#x60;&#x60;&#x60; curl -X DELETE https://console.jumpcloud.com/api/v2/softwareapps/{id} \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void deleteTest() throws ApiException {
        String id = null;
        String xOrgId = null;
        api.delete(id)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * Retrieve a configured Software Application.
     *
     * Retrieves a Software Application. The optional isConfigEnabled and appConfiguration apple_vpp attributes are populated in this response.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/softwareapps/{id} \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getTest() throws ApiException {
        String id = null;
        String xOrgId = null;
        SoftwareApp response = api.get(id)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * Get all configured Software Applications.
     *
     * This endpoint allows you to get all configured Software Applications that will be managed by JumpCloud on associated JumpCloud systems. The optional isConfigEnabled and appConfiguration apple_vpp attributes are not included in the response.  #### Sample Request &#x60;&#x60;&#x60; $ curl -X GET https://console.jumpcloud.com/api/v2/softwareapps \\ -H &#39;Accept: application/json&#39; \\ -H &#39;Content-Type: application/json&#39; \\ -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listTest() throws ApiException {
        String xOrgId = null;
        List<String> filter = null;
        Integer limit = null;
        Integer skip = null;
        List<String> sort = null;
        List<SoftwareApp> response = api.list()
                .xOrgId(xOrgId)
                .filter(filter)
                .limit(limit)
                .skip(skip)
                .sort(sort)
                .execute();
        // TODO: test validations
    }

    /**
     * Get the status of the provided Software Application
     *
     * This endpoint allows you to get the status of the provided Software Application on associated JumpCloud systems.  #### Sample Request &#x60;&#x60;&#x60; $ curl -X GET https://console.jumpcloud.com/api/v2/softwareapps/{software_app_id}/statuses \\ -H &#39;Accept: application/json&#39; \\ -H &#39;Content-Type: application/json&#39; \\ -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void list_0Test() throws ApiException {
        String softwareAppId = null;
        String xOrgId = null;
        List<String> filter = null;
        Integer limit = null;
        Integer skip = null;
        List<String> sort = null;
        List<SoftwareAppStatus> response = api.list_0(softwareAppId)
                .xOrgId(xOrgId)
                .filter(filter)
                .limit(limit)
                .skip(skip)
                .sort(sort)
                .execute();
        // TODO: test validations
    }

    /**
     * Create a Software Application that will be managed by JumpCloud.
     *
     * This endpoint allows you to create a Software Application that will be managed by JumpCloud on associated JumpCloud systems. The optional isConfigEnabled and appConfiguration apple_vpp attributes are not included in the response.  #### Sample Request &#x60;&#x60;&#x60; $ curl -X POST https://console.jumpcloud.com/api/v2/softwareapps \\ -H &#39;Accept: application/json&#39; \\ -H &#39;Content-Type: application/json&#39; \\ -H &#39;x-api-key: {API_KEY}&#39; \\ -d &#39;{   \&quot;displayName\&quot;: \&quot;Adobe Reader\&quot;,   \&quot;settings\&quot;: [{\&quot;packageId\&quot;: \&quot;adobereader\&quot;}] }&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void postTest() throws ApiException {
        String displayName = null;
        String id = null;
        List<SoftwareAppSettings> settings = null;
        String xOrgId = null;
        SoftwareAppCreate response = api.post()
                .displayName(displayName)
                .id(id)
                .settings(settings)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * Reclaim Licenses for a Software Application.
     *
     * This endpoint allows you to reclaim the licenses from a software app associated with devices that are deleted. #### Sample Request &#x60;&#x60;&#x60; $ curl -X POST https://console.jumpcloud.com/api/v2/softwareapps/{software_app_id}/reclaim-licenses \\ -H &#39;Accept: application/json&#39; \\ -H &#39;Content-Type: application/json&#39; \\ -H &#39;x-api-key: {API_KEY}&#39; \\ -d &#39;{}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void reclaimLicensesTest() throws ApiException {
        String softwareAppId = null;
        SoftwareAppReclaimLicenses response = api.reclaimLicenses(softwareAppId)
                .execute();
        // TODO: test validations
    }

    /**
     * Retry Installation for a Software Application
     *
     * This endpoints initiates an installation retry of an Apple VPP App for the provided system IDs #### Sample Request &#x60;&#x60;&#x60; $ curl -X POST https://console.jumpcloud.com/api/v2/softwareapps/{software_app_id}/retry-installation \\ -H &#39;Accept: application/json&#39; \\ -H &#39;Content-Type: application/json&#39; \\ -H &#39;x-api-key: {API_KEY}&#39; \\ -d &#39;{\&quot;system_ids\&quot;: \&quot;{&lt;system_id_1&gt;, &lt;system_id_2&gt;, ...}\&quot;}&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void retryInstallationTest() throws ApiException {
        String softwareAppId = null;
        api.retryInstallation(softwareAppId)
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
     * Update a Software Application Configuration.
     *
     * This endpoint updates a specific Software Application configuration for the organization. displayName can be changed alone if no settings are provided. If a setting is provided, it should include all its information since this endpoint will update all the settings&#39; fields. The optional isConfigEnabled and appConfiguration apple_vpp attributes are not included in the response.  #### Sample Request - displayName only &#x60;&#x60;&#x60;  curl -X PUT https://console.jumpcloud.com/api/v2/softwareapps/{id} \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;displayName\&quot;: \&quot;My Software App\&quot;   }&#39; &#x60;&#x60;&#x60;  #### Sample Request - all attributes &#x60;&#x60;&#x60;  curl -X PUT https://console.jumpcloud.com/api/v2/softwareapps/{id} \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;displayName\&quot;: \&quot;My Software App\&quot;,     \&quot;settings\&quot;: [       {         \&quot;packageId\&quot;: \&quot;123456\&quot;,         \&quot;autoUpdate\&quot;: false,         \&quot;allowUpdateDelay\&quot;: false,         \&quot;packageManager\&quot;: \&quot;APPLE_VPP\&quot;,         \&quot;locationObjectId\&quot;: \&quot;123456789012123456789012\&quot;,         \&quot;location\&quot;: \&quot;123456\&quot;,         \&quot;desiredState\&quot;: \&quot;Install\&quot;,         \&quot;appleVpp\&quot;: {           \&quot;appConfiguration\&quot;: \&quot;&lt;?xml version&#x3D;\\\&quot;1.0\\\&quot; encoding&#x3D;\\\&quot;UTF-8\\\&quot;?&gt;&lt;!DOCTYPE plist PUBLIC \\\&quot;-//Apple//DTD PLIST 1.0//EN\\\&quot; \\\&quot;http://www.apple.com/DTDs/PropertyList-1.0.dtd\\\&quot;&gt;&lt;plist version&#x3D;\\\&quot;1.0\\\&quot;&gt;&lt;dict&gt;&lt;key&gt;MyKey&lt;/key&gt;&lt;string&gt;My String&lt;/string&gt;&lt;/dict&gt;&lt;/plist&gt;\&quot;,           \&quot;assignedLicenses\&quot;: 20,           \&quot;availableLicenses\&quot;: 10,           \&quot;details\&quot;: {},           \&quot;isConfigEnabled\&quot;: true,           \&quot;supportedDeviceFamilies\&quot;: [             \&quot;IPAD\&quot;,             \&quot;MAC\&quot;           ],           \&quot;totalLicenses\&quot;: 30         },         \&quot;packageSubtitle\&quot;: \&quot;My package subtitle\&quot;,         \&quot;packageVersion\&quot;: \&quot;1.2.3\&quot;,         \&quot;packageKind\&quot;: \&quot;software-package\&quot;,         \&quot;assetKind\&quot;: \&quot;software\&quot;,         \&quot;assetSha256Size\&quot;: 256,         \&quot;assetSha256Strings\&quot;: [           \&quot;a123b123c123d123\&quot;         ],         \&quot;description\&quot;: \&quot;My app description\&quot;       }     ]   }&#39; &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void updateTest() throws ApiException {
        String id = null;
        String displayName = null;
        String id = null;
        List<SoftwareAppSettings> settings = null;
        String xOrgId = null;
        SoftwareApp response = api.update(id)
                .displayName(displayName)
                .id(id)
                .settings(settings)
                .xOrgId(xOrgId)
                .execute();
        // TODO: test validations
    }

    /**
     * Validate Installation Packages
     *
     * Validates an application install package from the specified URL to calculate the SHA256 hash and extract the installer manifest details. #### Sample Request &#x60;&#x60;&#x60; curl -H &#39;x-api-key: {API_KEY}&#39; \\ -H &#39;Content-Type: application/json&#39; \\ -H &#39;Accept: application/json&#39; \\ -d &#39;{\&quot;url\&quot;: \&quot;https://dl.google.com/dl/chrome/mac/universal/stable/gcem/GoogleChrome.pkg\&quot;}&#39; \\ -i -X POST https://console.jumpcloud.com/api/v2/softwareapps/validate &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void validateApplicationInstallPackageTest() throws ApiException {
        String url = null;
        JumpcloudPackageValidatorValidateApplicationInstallPackageResponse response = api.validateApplicationInstallPackage()
                .url(url)
                .execute();
        // TODO: test validations
    }

}
