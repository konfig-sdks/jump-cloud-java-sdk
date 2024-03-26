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
import com.konfigthis.client.model.DevicesResetPasswordRequest;
import com.konfigthis.client.model.EnrollmentTokensCreateEnterprisesEnrollmentTokenRequest;
import com.konfigthis.client.model.EnterprisesPatchEnterpriseRequest;
import com.konfigthis.client.model.JumpcloudGoogleEmmAllowPersonalUsage;
import com.konfigthis.client.model.JumpcloudGoogleEmmConnectionStatus;
import com.konfigthis.client.model.JumpcloudGoogleEmmCreateEnrollmentTokenRequest;
import com.konfigthis.client.model.JumpcloudGoogleEmmCreateEnrollmentTokenResponse;
import com.konfigthis.client.model.JumpcloudGoogleEmmCreateEnterpriseRequest;
import com.konfigthis.client.model.JumpcloudGoogleEmmCreateWebTokenRequest;
import com.konfigthis.client.model.JumpcloudGoogleEmmCreatedWhere;
import com.konfigthis.client.model.JumpcloudGoogleEmmDeleteEnrollmentTokenResponse;
import com.konfigthis.client.model.JumpcloudGoogleEmmDevice;
import com.konfigthis.client.model.JumpcloudGoogleEmmDeviceAndroidPolicy;
import com.konfigthis.client.model.JumpcloudGoogleEmmEnrollmentToken;
import com.konfigthis.client.model.JumpcloudGoogleEmmEnrollmentType;
import com.konfigthis.client.model.JumpcloudGoogleEmmEnterprise;
import com.konfigthis.client.model.JumpcloudGoogleEmmFeature;
import com.konfigthis.client.model.JumpcloudGoogleEmmListDevicesResponse;
import com.konfigthis.client.model.JumpcloudGoogleEmmListEnrollmentTokensResponse;
import com.konfigthis.client.model.JumpcloudGoogleEmmListEnterprisesResponse;
import com.konfigthis.client.model.JumpcloudGoogleEmmProvisioningExtras;
import com.konfigthis.client.model.JumpcloudGoogleEmmSignupURL;
import com.konfigthis.client.model.JumpcloudGoogleEmmWebToken;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for GoogleEmmApi
 */
@Disabled
public class GoogleEmmApiTest {

    private static GoogleEmmApi api;

    
    @BeforeAll
    public static void beforeClass() {
        ApiClient apiClient = Configuration.getDefaultApiClient();
        api = new GoogleEmmApi(apiClient);
    }

    /**
     * Get a Signup URL to enroll Google enterprise
     *
     * Creates a Google EMM enterprise signup URL.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/google-emm/signup-urls \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void createTest() throws ApiException {
        JumpcloudGoogleEmmSignupURL response = api.create()
                .execute();
        // TODO: test validations
    }

    /**
     * Create an enrollment token
     *
     * Gets an enrollment token to enroll a device into Google EMM.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/google-emm/enrollment-tokens \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void createEnrollmentTokenTest() throws ApiException {
        JumpcloudGoogleEmmAllowPersonalUsage allowPersonalUsage = null;
        JumpcloudGoogleEmmCreatedWhere createdWhere = null;
        String displayName = null;
        String duration = null;
        JumpcloudGoogleEmmEnrollmentType enrollmentType = null;
        byte[] enterpriseObjectId = null;
        Boolean oneTimeOnly = null;
        JumpcloudGoogleEmmProvisioningExtras provisioningExtras = null;
        byte[] userObjectId = null;
        Boolean zeroTouch = null;
        JumpcloudGoogleEmmCreateEnrollmentTokenResponse response = api.createEnrollmentToken()
                .allowPersonalUsage(allowPersonalUsage)
                .createdWhere(createdWhere)
                .displayName(displayName)
                .duration(duration)
                .enrollmentType(enrollmentType)
                .enterpriseObjectId(enterpriseObjectId)
                .oneTimeOnly(oneTimeOnly)
                .provisioningExtras(provisioningExtras)
                .userObjectId(userObjectId)
                .zeroTouch(zeroTouch)
                .execute();
        // TODO: test validations
    }

    /**
     * Create a Google Enterprise
     *
     * Creates a Google EMM enterprise.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/google-emm/enterprises \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{ &#39;signupUrlName&#39;: &#39;string&#39;, &#39;enrollmentToken&#39;: &#39;string&#39; }&#39; \\ &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void createEnterpriseTest() throws ApiException {
        String enrollmentToken = null;
        String signupUrlName = null;
        JumpcloudGoogleEmmEnterprise response = api.createEnterprise()
                .enrollmentToken(enrollmentToken)
                .signupUrlName(signupUrlName)
                .execute();
        // TODO: test validations
    }

    /**
     * Create an enrollment token for a given enterprise
     *
     * Gets an enrollment token to enroll a device into Google EMM.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/google-emm/enterpries/{enterprise_object_id}/enrollment-tokens \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void createEnterprisesEnrollmentTokenTest() throws ApiException {
        byte[] enterpriseObjectId = null;
        JumpcloudGoogleEmmAllowPersonalUsage allowPersonalUsage = null;
        JumpcloudGoogleEmmCreatedWhere createdWhere = null;
        String displayName = null;
        String duration = null;
        JumpcloudGoogleEmmEnrollmentType enrollmentType = null;
        Boolean oneTimeOnly = null;
        JumpcloudGoogleEmmProvisioningExtras provisioningExtras = null;
        byte[] userObjectId = null;
        Boolean zeroTouch = null;
        JumpcloudGoogleEmmEnrollmentToken response = api.createEnterprisesEnrollmentToken(enterpriseObjectId)
                .allowPersonalUsage(allowPersonalUsage)
                .createdWhere(createdWhere)
                .displayName(displayName)
                .duration(duration)
                .enrollmentType(enrollmentType)
                .oneTimeOnly(oneTimeOnly)
                .provisioningExtras(provisioningExtras)
                .userObjectId(userObjectId)
                .zeroTouch(zeroTouch)
                .execute();
        // TODO: test validations
    }

    /**
     * Get a web token to render Google Play iFrame
     *
     * Creates a web token to access an embeddable managed Google Play web UI for a given Google EMM enterprise.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/google-emm/web-tokens \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void createWebTokenTest() throws ApiException {
        byte[] enterpriseObjectId = null;
        JumpcloudGoogleEmmFeature iframeFeature = null;
        String parentFrameUrl = null;
        JumpcloudGoogleEmmWebToken response = api.createWebToken()
                .enterpriseObjectId(enterpriseObjectId)
                .iframeFeature(iframeFeature)
                .parentFrameUrl(parentFrameUrl)
                .execute();
        // TODO: test validations
    }

    /**
     * Deletes an enrollment token for a given enterprise and token id
     *
     * Removes an Enrollment token for a given enterprise and token id.  #### Sample Request &#x60;&#x60;&#x60; curl -X DELETE https://console.jumpcloud.com/api/v2/enterprises/{enterprise_id}/enrollment-tokens/{token_id} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void deleteEnrollmentTokenTest() throws ApiException {
        byte[] enterpriseId = null;
        String tokenId = null;
        JumpcloudGoogleEmmDeleteEnrollmentTokenResponse response = api.deleteEnrollmentToken(enterpriseId, tokenId)
                .execute();
        // TODO: test validations
    }

    /**
     * Delete a Google Enterprise
     *
     * Removes a Google EMM enterprise.   Warning: This is a destructive operation and will remove all data associated with Google EMM enterprise from JumpCloud including devices and applications associated with the given enterprise.  #### Sample Request &#x60;&#x60;&#x60; curl -X DELETE https://console.jumpcloud.com/api/v2/google-emm/devices/{enterpriseId} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void deleteEnterpriseTest() throws ApiException {
        byte[] enterpriseId = null;
        Object response = api.deleteEnterprise(enterpriseId)
                .execute();
        // TODO: test validations
    }

    /**
     * Erase the Android Device
     *
     * Removes the work profile and all policies from a personal/company-owned Android 8.0+ device. Company owned devices will be relinquished for personal use. Apps and data associated with the personal profile(s) are preserved.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/google-emm/devices/{deviceId}/erase-device \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void eraseDeviceTest() throws ApiException {
        byte[] deviceId = null;
        Object response = api.eraseDevice(deviceId)
                .execute();
        // TODO: test validations
    }

    /**
     * Test connection with Google
     *
     * Gives a connection status between JumpCloud and Google.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/google-emm/devices/{enterpriseId}/connection-status \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getConnectionStatusTest() throws ApiException {
        byte[] enterpriseId = null;
        JumpcloudGoogleEmmConnectionStatus response = api.getConnectionStatus(enterpriseId)
                .execute();
        // TODO: test validations
    }

    /**
     * Get device
     *
     * Gets a Google EMM enrolled device details.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/google-emm/devices/{deviceId} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getDeviceTest() throws ApiException {
        byte[] deviceId = null;
        JumpcloudGoogleEmmDevice response = api.getDevice(deviceId)
                .execute();
        // TODO: test validations
    }

    /**
     * Get the policy JSON of a device
     *
     * Gets an android JSON policy for a Google EMM enrolled device.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/google-emm/devices/{deviceId}/policy_results \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getDeviceAndroidPolicyTest() throws ApiException {
        byte[] deviceId = null;
        JumpcloudGoogleEmmDeviceAndroidPolicy response = api.getDeviceAndroidPolicy(deviceId)
                .execute();
        // TODO: test validations
    }

    /**
     * List devices
     *
     * Lists google EMM enrolled devices.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/google-emm/enterprises/{enterprise_object_id}/devices \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listDevicesTest() throws ApiException {
        byte[] enterpriseObjectId = null;
        String limit = null;
        String skip = null;
        List<String> filter = null;
        JumpcloudGoogleEmmListDevicesResponse response = api.listDevices(enterpriseObjectId)
                .limit(limit)
                .skip(skip)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List enrollment tokens
     *
     * Lists active, unexpired enrollement tokens for a given enterprise.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/google-emm/enterprises/{enterprise_object_id}/enrollment-tokens \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listEnrollmentTokensTest() throws ApiException {
        byte[] enterpriseObjectId = null;
        String limit = null;
        String skip = null;
        List<String> filter = null;
        String sort = null;
        JumpcloudGoogleEmmListEnrollmentTokensResponse response = api.listEnrollmentTokens(enterpriseObjectId)
                .limit(limit)
                .skip(skip)
                .filter(filter)
                .sort(sort)
                .execute();
        // TODO: test validations
    }

    /**
     * List Google Enterprises
     *
     * Lists all Google EMM enterprises. An empty list indicates that the Organization is not configured with a Google EMM enterprise yet.    Note: Currently only one Google Enterprise per Organization is supported.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/google-emm/enterprises \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listEnterprisesTest() throws ApiException {
        String limit = null;
        String skip = null;
        JumpcloudGoogleEmmListEnterprisesResponse response = api.listEnterprises()
                .limit(limit)
                .skip(skip)
                .execute();
        // TODO: test validations
    }

    /**
     * Lock device
     *
     * Locks a Google EMM enrolled device, as if the lock screen timeout had expired.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/google-emm/devices/{deviceId}/lock \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void lockDeviceTest() throws ApiException {
        byte[] deviceId = null;
        Object response = api.lockDevice(deviceId)
                .execute();
        // TODO: test validations
    }

    /**
     * Update a Google Enterprise
     *
     * Updates a Google EMM enterprise details.  #### Sample Request &#x60;&#x60;&#x60; curl -X PATCH https://console.jumpcloud.com/api/v2/google-emm/enterprises \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{ &#39;allowDeviceEnrollment&#39;: true, &#39;deviceGroupId&#39;: &#39;string&#39; }&#39; \\ &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void patchEnterpriseTest() throws ApiException {
        byte[] enterpriseId = null;
        Boolean allowDeviceEnrollment = null;
        byte[] deviceGroupId = null;
        JumpcloudGoogleEmmEnterprise response = api.patchEnterprise(enterpriseId)
                .allowDeviceEnrollment(allowDeviceEnrollment)
                .deviceGroupId(deviceGroupId)
                .execute();
        // TODO: test validations
    }

    /**
     * Reboot device
     *
     * Reboots a Google EMM enrolled device. Only supported on fully managed devices running Android 7.0 or higher.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/google-emm/devices/{deviceId}/reboot \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void rebootDeviceTest() throws ApiException {
        byte[] deviceId = null;
        Object response = api.rebootDevice(deviceId)
                .execute();
        // TODO: test validations
    }

    /**
     * Reset Password of a device
     *
     * Reset the user&#39;s password of a Google EMM enrolled device.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/google-emm/devices/{deviceId}/resetpassword \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{ &#39;new_password&#39; : &#39;string&#39; }&#39; \\ &#x60;&#x60;&#x60;
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void resetPasswordTest() throws ApiException {
        byte[] deviceId = null;
        List<String> flags = null;
        String newPassword = null;
        Object response = api.resetPassword(deviceId)
                .flags(flags)
                .newPassword(newPassword)
                .execute();
        // TODO: test validations
    }

}
