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

import com.konfigthis.client.ApiCallback;
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.Pair;
import com.konfigthis.client.ProgressRequestBody;
import com.konfigthis.client.ProgressResponseBody;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;


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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;

public class GoogleEmmApiGenerated {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public GoogleEmmApiGenerated() throws IllegalArgumentException {
        this(Configuration.getDefaultApiClient());
    }

    public GoogleEmmApiGenerated(ApiClient apiClient) throws IllegalArgumentException {
        this.localVarApiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return localVarApiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.localVarApiClient = apiClient;
    }

    public int getHostIndex() {
        return localHostIndex;
    }

    public void setHostIndex(int hostIndex) {
        this.localHostIndex = hostIndex;
    }

    public String getCustomBaseUrl() {
        return localCustomBaseUrl;
    }

    public void setCustomBaseUrl(String customBaseUrl) {
        this.localCustomBaseUrl = customBaseUrl;
    }

    private okhttp3.Call createCall(final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/google-emm/signup-urls";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "x-api-key" };
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call createValidateBeforeCall(final ApiCallback _callback) throws ApiException {
        return createCall(_callback);

    }


    private ApiResponse<JumpcloudGoogleEmmSignupURL> createWithHttpInfo() throws ApiException {
        okhttp3.Call localVarCall = createValidateBeforeCall(null);
        Type localVarReturnType = new TypeToken<JumpcloudGoogleEmmSignupURL>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call createAsync(final ApiCallback<JumpcloudGoogleEmmSignupURL> _callback) throws ApiException {

        okhttp3.Call localVarCall = createValidateBeforeCall(_callback);
        Type localVarReturnType = new TypeToken<JumpcloudGoogleEmmSignupURL>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class CreateRequestBuilder {

        private CreateRequestBuilder() {
        }

        /**
         * Build call for create
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return createCall(_callback);
        }


        /**
         * Execute create request
         * @return JumpcloudGoogleEmmSignupURL
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public JumpcloudGoogleEmmSignupURL execute() throws ApiException {
            ApiResponse<JumpcloudGoogleEmmSignupURL> localVarResp = createWithHttpInfo();
            return localVarResp.getResponseBody();
        }

        /**
         * Execute create request with HTTP info returned
         * @return ApiResponse&lt;JumpcloudGoogleEmmSignupURL&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<JumpcloudGoogleEmmSignupURL> executeWithHttpInfo() throws ApiException {
            return createWithHttpInfo();
        }

        /**
         * Execute create request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<JumpcloudGoogleEmmSignupURL> _callback) throws ApiException {
            return createAsync(_callback);
        }
    }

    /**
     * Get a Signup URL to enroll Google enterprise
     * Creates a Google EMM enterprise signup URL.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/google-emm/signup-urls \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     * @return CreateRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
     </table>
     */
    public CreateRequestBuilder create() throws IllegalArgumentException {
        return new CreateRequestBuilder();
    }
    private okhttp3.Call createEnrollmentTokenCall(JumpcloudGoogleEmmCreateEnrollmentTokenRequest jumpcloudGoogleEmmCreateEnrollmentTokenRequest, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = jumpcloudGoogleEmmCreateEnrollmentTokenRequest;

        // create path and map variables
        String localVarPath = "/google-emm/enrollment-tokens";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "x-api-key" };
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call createEnrollmentTokenValidateBeforeCall(JumpcloudGoogleEmmCreateEnrollmentTokenRequest jumpcloudGoogleEmmCreateEnrollmentTokenRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'jumpcloudGoogleEmmCreateEnrollmentTokenRequest' is set
        if (jumpcloudGoogleEmmCreateEnrollmentTokenRequest == null) {
            throw new ApiException("Missing the required parameter 'jumpcloudGoogleEmmCreateEnrollmentTokenRequest' when calling createEnrollmentToken(Async)");
        }

        return createEnrollmentTokenCall(jumpcloudGoogleEmmCreateEnrollmentTokenRequest, _callback);

    }


    private ApiResponse<JumpcloudGoogleEmmCreateEnrollmentTokenResponse> createEnrollmentTokenWithHttpInfo(JumpcloudGoogleEmmCreateEnrollmentTokenRequest jumpcloudGoogleEmmCreateEnrollmentTokenRequest) throws ApiException {
        okhttp3.Call localVarCall = createEnrollmentTokenValidateBeforeCall(jumpcloudGoogleEmmCreateEnrollmentTokenRequest, null);
        Type localVarReturnType = new TypeToken<JumpcloudGoogleEmmCreateEnrollmentTokenResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call createEnrollmentTokenAsync(JumpcloudGoogleEmmCreateEnrollmentTokenRequest jumpcloudGoogleEmmCreateEnrollmentTokenRequest, final ApiCallback<JumpcloudGoogleEmmCreateEnrollmentTokenResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = createEnrollmentTokenValidateBeforeCall(jumpcloudGoogleEmmCreateEnrollmentTokenRequest, _callback);
        Type localVarReturnType = new TypeToken<JumpcloudGoogleEmmCreateEnrollmentTokenResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class CreateEnrollmentTokenRequestBuilder {
        private JumpcloudGoogleEmmAllowPersonalUsage allowPersonalUsage;
        private JumpcloudGoogleEmmCreatedWhere createdWhere;
        private String displayName;
        private String duration;
        private JumpcloudGoogleEmmEnrollmentType enrollmentType;
        private byte[] enterpriseObjectId;
        private Boolean oneTimeOnly;
        private JumpcloudGoogleEmmProvisioningExtras provisioningExtras;
        private byte[] userObjectId;
        private Boolean zeroTouch;

        private CreateEnrollmentTokenRequestBuilder() {
        }

        /**
         * Set allowPersonalUsage
         * @param allowPersonalUsage  (optional, default to PERSONAL_USAGE_ALLOWED)
         * @return CreateEnrollmentTokenRequestBuilder
         */
        public CreateEnrollmentTokenRequestBuilder allowPersonalUsage(JumpcloudGoogleEmmAllowPersonalUsage allowPersonalUsage) {
            this.allowPersonalUsage = allowPersonalUsage;
            return this;
        }
        
        /**
         * Set createdWhere
         * @param createdWhere  (optional, default to API)
         * @return CreateEnrollmentTokenRequestBuilder
         */
        public CreateEnrollmentTokenRequestBuilder createdWhere(JumpcloudGoogleEmmCreatedWhere createdWhere) {
            this.createdWhere = createdWhere;
            return this;
        }
        
        /**
         * Set displayName
         * @param displayName  (optional)
         * @return CreateEnrollmentTokenRequestBuilder
         */
        public CreateEnrollmentTokenRequestBuilder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }
        
        /**
         * Set duration
         * @param duration  (optional)
         * @return CreateEnrollmentTokenRequestBuilder
         */
        public CreateEnrollmentTokenRequestBuilder duration(String duration) {
            this.duration = duration;
            return this;
        }
        
        /**
         * Set enrollmentType
         * @param enrollmentType  (optional, default to WORK_PROFILE)
         * @return CreateEnrollmentTokenRequestBuilder
         */
        public CreateEnrollmentTokenRequestBuilder enrollmentType(JumpcloudGoogleEmmEnrollmentType enrollmentType) {
            this.enrollmentType = enrollmentType;
            return this;
        }
        
        /**
         * Set enterpriseObjectId
         * @param enterpriseObjectId  (optional)
         * @return CreateEnrollmentTokenRequestBuilder
         */
        public CreateEnrollmentTokenRequestBuilder enterpriseObjectId(byte[] enterpriseObjectId) {
            this.enterpriseObjectId = enterpriseObjectId;
            return this;
        }
        
        /**
         * Set oneTimeOnly
         * @param oneTimeOnly  (optional)
         * @return CreateEnrollmentTokenRequestBuilder
         */
        public CreateEnrollmentTokenRequestBuilder oneTimeOnly(Boolean oneTimeOnly) {
            this.oneTimeOnly = oneTimeOnly;
            return this;
        }
        
        /**
         * Set provisioningExtras
         * @param provisioningExtras  (optional)
         * @return CreateEnrollmentTokenRequestBuilder
         */
        public CreateEnrollmentTokenRequestBuilder provisioningExtras(JumpcloudGoogleEmmProvisioningExtras provisioningExtras) {
            this.provisioningExtras = provisioningExtras;
            return this;
        }
        
        /**
         * Set userObjectId
         * @param userObjectId  (optional)
         * @return CreateEnrollmentTokenRequestBuilder
         */
        public CreateEnrollmentTokenRequestBuilder userObjectId(byte[] userObjectId) {
            this.userObjectId = userObjectId;
            return this;
        }
        
        /**
         * Set zeroTouch
         * @param zeroTouch  (optional)
         * @return CreateEnrollmentTokenRequestBuilder
         */
        public CreateEnrollmentTokenRequestBuilder zeroTouch(Boolean zeroTouch) {
            this.zeroTouch = zeroTouch;
            return this;
        }
        
        /**
         * Build call for createEnrollmentToken
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            JumpcloudGoogleEmmCreateEnrollmentTokenRequest jumpcloudGoogleEmmCreateEnrollmentTokenRequest = buildBodyParams();
            return createEnrollmentTokenCall(jumpcloudGoogleEmmCreateEnrollmentTokenRequest, _callback);
        }

        private JumpcloudGoogleEmmCreateEnrollmentTokenRequest buildBodyParams() {
            JumpcloudGoogleEmmCreateEnrollmentTokenRequest jumpcloudGoogleEmmCreateEnrollmentTokenRequest = new JumpcloudGoogleEmmCreateEnrollmentTokenRequest();
            jumpcloudGoogleEmmCreateEnrollmentTokenRequest.allowPersonalUsage(this.allowPersonalUsage);
            jumpcloudGoogleEmmCreateEnrollmentTokenRequest.createdWhere(this.createdWhere);
            jumpcloudGoogleEmmCreateEnrollmentTokenRequest.displayName(this.displayName);
            jumpcloudGoogleEmmCreateEnrollmentTokenRequest.duration(this.duration);
            jumpcloudGoogleEmmCreateEnrollmentTokenRequest.enrollmentType(this.enrollmentType);
            jumpcloudGoogleEmmCreateEnrollmentTokenRequest.enterpriseObjectId(this.enterpriseObjectId);
            jumpcloudGoogleEmmCreateEnrollmentTokenRequest.oneTimeOnly(this.oneTimeOnly);
            jumpcloudGoogleEmmCreateEnrollmentTokenRequest.provisioningExtras(this.provisioningExtras);
            jumpcloudGoogleEmmCreateEnrollmentTokenRequest.userObjectId(this.userObjectId);
            jumpcloudGoogleEmmCreateEnrollmentTokenRequest.zeroTouch(this.zeroTouch);
            return jumpcloudGoogleEmmCreateEnrollmentTokenRequest;
        }

        /**
         * Execute createEnrollmentToken request
         * @return JumpcloudGoogleEmmCreateEnrollmentTokenResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public JumpcloudGoogleEmmCreateEnrollmentTokenResponse execute() throws ApiException {
            JumpcloudGoogleEmmCreateEnrollmentTokenRequest jumpcloudGoogleEmmCreateEnrollmentTokenRequest = buildBodyParams();
            ApiResponse<JumpcloudGoogleEmmCreateEnrollmentTokenResponse> localVarResp = createEnrollmentTokenWithHttpInfo(jumpcloudGoogleEmmCreateEnrollmentTokenRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute createEnrollmentToken request with HTTP info returned
         * @return ApiResponse&lt;JumpcloudGoogleEmmCreateEnrollmentTokenResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<JumpcloudGoogleEmmCreateEnrollmentTokenResponse> executeWithHttpInfo() throws ApiException {
            JumpcloudGoogleEmmCreateEnrollmentTokenRequest jumpcloudGoogleEmmCreateEnrollmentTokenRequest = buildBodyParams();
            return createEnrollmentTokenWithHttpInfo(jumpcloudGoogleEmmCreateEnrollmentTokenRequest);
        }

        /**
         * Execute createEnrollmentToken request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<JumpcloudGoogleEmmCreateEnrollmentTokenResponse> _callback) throws ApiException {
            JumpcloudGoogleEmmCreateEnrollmentTokenRequest jumpcloudGoogleEmmCreateEnrollmentTokenRequest = buildBodyParams();
            return createEnrollmentTokenAsync(jumpcloudGoogleEmmCreateEnrollmentTokenRequest, _callback);
        }
    }

    /**
     * Create an enrollment token
     * Gets an enrollment token to enroll a device into Google EMM.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/google-emm/enrollment-tokens \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     * @param jumpcloudGoogleEmmCreateEnrollmentTokenRequest  (required)
     * @return CreateEnrollmentTokenRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
     </table>
     */
    public CreateEnrollmentTokenRequestBuilder createEnrollmentToken() throws IllegalArgumentException {
        return new CreateEnrollmentTokenRequestBuilder();
    }
    private okhttp3.Call createEnterpriseCall(JumpcloudGoogleEmmCreateEnterpriseRequest jumpcloudGoogleEmmCreateEnterpriseRequest, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = jumpcloudGoogleEmmCreateEnterpriseRequest;

        // create path and map variables
        String localVarPath = "/google-emm/enterprises";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "x-api-key" };
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call createEnterpriseValidateBeforeCall(JumpcloudGoogleEmmCreateEnterpriseRequest jumpcloudGoogleEmmCreateEnterpriseRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'jumpcloudGoogleEmmCreateEnterpriseRequest' is set
        if (jumpcloudGoogleEmmCreateEnterpriseRequest == null) {
            throw new ApiException("Missing the required parameter 'jumpcloudGoogleEmmCreateEnterpriseRequest' when calling createEnterprise(Async)");
        }

        return createEnterpriseCall(jumpcloudGoogleEmmCreateEnterpriseRequest, _callback);

    }


    private ApiResponse<JumpcloudGoogleEmmEnterprise> createEnterpriseWithHttpInfo(JumpcloudGoogleEmmCreateEnterpriseRequest jumpcloudGoogleEmmCreateEnterpriseRequest) throws ApiException {
        okhttp3.Call localVarCall = createEnterpriseValidateBeforeCall(jumpcloudGoogleEmmCreateEnterpriseRequest, null);
        Type localVarReturnType = new TypeToken<JumpcloudGoogleEmmEnterprise>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call createEnterpriseAsync(JumpcloudGoogleEmmCreateEnterpriseRequest jumpcloudGoogleEmmCreateEnterpriseRequest, final ApiCallback<JumpcloudGoogleEmmEnterprise> _callback) throws ApiException {

        okhttp3.Call localVarCall = createEnterpriseValidateBeforeCall(jumpcloudGoogleEmmCreateEnterpriseRequest, _callback);
        Type localVarReturnType = new TypeToken<JumpcloudGoogleEmmEnterprise>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class CreateEnterpriseRequestBuilder {
        private String enrollmentToken;
        private String signupUrlName;

        private CreateEnterpriseRequestBuilder() {
        }

        /**
         * Set enrollmentToken
         * @param enrollmentToken  (optional)
         * @return CreateEnterpriseRequestBuilder
         */
        public CreateEnterpriseRequestBuilder enrollmentToken(String enrollmentToken) {
            this.enrollmentToken = enrollmentToken;
            return this;
        }
        
        /**
         * Set signupUrlName
         * @param signupUrlName  (optional)
         * @return CreateEnterpriseRequestBuilder
         */
        public CreateEnterpriseRequestBuilder signupUrlName(String signupUrlName) {
            this.signupUrlName = signupUrlName;
            return this;
        }
        
        /**
         * Build call for createEnterprise
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            JumpcloudGoogleEmmCreateEnterpriseRequest jumpcloudGoogleEmmCreateEnterpriseRequest = buildBodyParams();
            return createEnterpriseCall(jumpcloudGoogleEmmCreateEnterpriseRequest, _callback);
        }

        private JumpcloudGoogleEmmCreateEnterpriseRequest buildBodyParams() {
            JumpcloudGoogleEmmCreateEnterpriseRequest jumpcloudGoogleEmmCreateEnterpriseRequest = new JumpcloudGoogleEmmCreateEnterpriseRequest();
            jumpcloudGoogleEmmCreateEnterpriseRequest.enrollmentToken(this.enrollmentToken);
            jumpcloudGoogleEmmCreateEnterpriseRequest.signupUrlName(this.signupUrlName);
            return jumpcloudGoogleEmmCreateEnterpriseRequest;
        }

        /**
         * Execute createEnterprise request
         * @return JumpcloudGoogleEmmEnterprise
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public JumpcloudGoogleEmmEnterprise execute() throws ApiException {
            JumpcloudGoogleEmmCreateEnterpriseRequest jumpcloudGoogleEmmCreateEnterpriseRequest = buildBodyParams();
            ApiResponse<JumpcloudGoogleEmmEnterprise> localVarResp = createEnterpriseWithHttpInfo(jumpcloudGoogleEmmCreateEnterpriseRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute createEnterprise request with HTTP info returned
         * @return ApiResponse&lt;JumpcloudGoogleEmmEnterprise&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<JumpcloudGoogleEmmEnterprise> executeWithHttpInfo() throws ApiException {
            JumpcloudGoogleEmmCreateEnterpriseRequest jumpcloudGoogleEmmCreateEnterpriseRequest = buildBodyParams();
            return createEnterpriseWithHttpInfo(jumpcloudGoogleEmmCreateEnterpriseRequest);
        }

        /**
         * Execute createEnterprise request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<JumpcloudGoogleEmmEnterprise> _callback) throws ApiException {
            JumpcloudGoogleEmmCreateEnterpriseRequest jumpcloudGoogleEmmCreateEnterpriseRequest = buildBodyParams();
            return createEnterpriseAsync(jumpcloudGoogleEmmCreateEnterpriseRequest, _callback);
        }
    }

    /**
     * Create a Google Enterprise
     * Creates a Google EMM enterprise.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/google-emm/enterprises \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{ &#39;signupUrlName&#39;: &#39;string&#39;, &#39;enrollmentToken&#39;: &#39;string&#39; }&#39; \\ &#x60;&#x60;&#x60;
     * @param jumpcloudGoogleEmmCreateEnterpriseRequest  (required)
     * @return CreateEnterpriseRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
     </table>
     */
    public CreateEnterpriseRequestBuilder createEnterprise() throws IllegalArgumentException {
        return new CreateEnterpriseRequestBuilder();
    }
    private okhttp3.Call createEnterprisesEnrollmentTokenCall(byte[] enterpriseObjectId, EnrollmentTokensCreateEnterprisesEnrollmentTokenRequest enrollmentTokensCreateEnterprisesEnrollmentTokenRequest, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = enrollmentTokensCreateEnterprisesEnrollmentTokenRequest;

        // create path and map variables
        String localVarPath = "/google-emm/enterprises/{enterpriseObjectId}/enrollment-tokens"
            .replace("{" + "enterpriseObjectId" + "}", localVarApiClient.escapeString(enterpriseObjectId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "x-api-key" };
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call createEnterprisesEnrollmentTokenValidateBeforeCall(byte[] enterpriseObjectId, EnrollmentTokensCreateEnterprisesEnrollmentTokenRequest enrollmentTokensCreateEnterprisesEnrollmentTokenRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'enterpriseObjectId' is set
        if (enterpriseObjectId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseObjectId' when calling createEnterprisesEnrollmentToken(Async)");
        }

        // verify the required parameter 'enrollmentTokensCreateEnterprisesEnrollmentTokenRequest' is set
        if (enrollmentTokensCreateEnterprisesEnrollmentTokenRequest == null) {
            throw new ApiException("Missing the required parameter 'enrollmentTokensCreateEnterprisesEnrollmentTokenRequest' when calling createEnterprisesEnrollmentToken(Async)");
        }

        return createEnterprisesEnrollmentTokenCall(enterpriseObjectId, enrollmentTokensCreateEnterprisesEnrollmentTokenRequest, _callback);

    }


    private ApiResponse<JumpcloudGoogleEmmEnrollmentToken> createEnterprisesEnrollmentTokenWithHttpInfo(byte[] enterpriseObjectId, EnrollmentTokensCreateEnterprisesEnrollmentTokenRequest enrollmentTokensCreateEnterprisesEnrollmentTokenRequest) throws ApiException {
        okhttp3.Call localVarCall = createEnterprisesEnrollmentTokenValidateBeforeCall(enterpriseObjectId, enrollmentTokensCreateEnterprisesEnrollmentTokenRequest, null);
        Type localVarReturnType = new TypeToken<JumpcloudGoogleEmmEnrollmentToken>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call createEnterprisesEnrollmentTokenAsync(byte[] enterpriseObjectId, EnrollmentTokensCreateEnterprisesEnrollmentTokenRequest enrollmentTokensCreateEnterprisesEnrollmentTokenRequest, final ApiCallback<JumpcloudGoogleEmmEnrollmentToken> _callback) throws ApiException {

        okhttp3.Call localVarCall = createEnterprisesEnrollmentTokenValidateBeforeCall(enterpriseObjectId, enrollmentTokensCreateEnterprisesEnrollmentTokenRequest, _callback);
        Type localVarReturnType = new TypeToken<JumpcloudGoogleEmmEnrollmentToken>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class CreateEnterprisesEnrollmentTokenRequestBuilder {
        private final byte[] enterpriseObjectId;
        private JumpcloudGoogleEmmAllowPersonalUsage allowPersonalUsage;
        private JumpcloudGoogleEmmCreatedWhere createdWhere;
        private String displayName;
        private String duration;
        private JumpcloudGoogleEmmEnrollmentType enrollmentType;
        private Boolean oneTimeOnly;
        private JumpcloudGoogleEmmProvisioningExtras provisioningExtras;
        private byte[] userObjectId;
        private Boolean zeroTouch;

        private CreateEnterprisesEnrollmentTokenRequestBuilder(byte[] enterpriseObjectId) {
            this.enterpriseObjectId = enterpriseObjectId;
        }

        /**
         * Set allowPersonalUsage
         * @param allowPersonalUsage  (optional, default to PERSONAL_USAGE_ALLOWED)
         * @return CreateEnterprisesEnrollmentTokenRequestBuilder
         */
        public CreateEnterprisesEnrollmentTokenRequestBuilder allowPersonalUsage(JumpcloudGoogleEmmAllowPersonalUsage allowPersonalUsage) {
            this.allowPersonalUsage = allowPersonalUsage;
            return this;
        }
        
        /**
         * Set createdWhere
         * @param createdWhere  (optional, default to API)
         * @return CreateEnterprisesEnrollmentTokenRequestBuilder
         */
        public CreateEnterprisesEnrollmentTokenRequestBuilder createdWhere(JumpcloudGoogleEmmCreatedWhere createdWhere) {
            this.createdWhere = createdWhere;
            return this;
        }
        
        /**
         * Set displayName
         * @param displayName  (optional)
         * @return CreateEnterprisesEnrollmentTokenRequestBuilder
         */
        public CreateEnterprisesEnrollmentTokenRequestBuilder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }
        
        /**
         * Set duration
         * @param duration  (optional)
         * @return CreateEnterprisesEnrollmentTokenRequestBuilder
         */
        public CreateEnterprisesEnrollmentTokenRequestBuilder duration(String duration) {
            this.duration = duration;
            return this;
        }
        
        /**
         * Set enrollmentType
         * @param enrollmentType  (optional, default to WORK_PROFILE)
         * @return CreateEnterprisesEnrollmentTokenRequestBuilder
         */
        public CreateEnterprisesEnrollmentTokenRequestBuilder enrollmentType(JumpcloudGoogleEmmEnrollmentType enrollmentType) {
            this.enrollmentType = enrollmentType;
            return this;
        }
        
        /**
         * Set oneTimeOnly
         * @param oneTimeOnly  (optional)
         * @return CreateEnterprisesEnrollmentTokenRequestBuilder
         */
        public CreateEnterprisesEnrollmentTokenRequestBuilder oneTimeOnly(Boolean oneTimeOnly) {
            this.oneTimeOnly = oneTimeOnly;
            return this;
        }
        
        /**
         * Set provisioningExtras
         * @param provisioningExtras  (optional)
         * @return CreateEnterprisesEnrollmentTokenRequestBuilder
         */
        public CreateEnterprisesEnrollmentTokenRequestBuilder provisioningExtras(JumpcloudGoogleEmmProvisioningExtras provisioningExtras) {
            this.provisioningExtras = provisioningExtras;
            return this;
        }
        
        /**
         * Set userObjectId
         * @param userObjectId  (optional)
         * @return CreateEnterprisesEnrollmentTokenRequestBuilder
         */
        public CreateEnterprisesEnrollmentTokenRequestBuilder userObjectId(byte[] userObjectId) {
            this.userObjectId = userObjectId;
            return this;
        }
        
        /**
         * Set zeroTouch
         * @param zeroTouch  (optional)
         * @return CreateEnterprisesEnrollmentTokenRequestBuilder
         */
        public CreateEnterprisesEnrollmentTokenRequestBuilder zeroTouch(Boolean zeroTouch) {
            this.zeroTouch = zeroTouch;
            return this;
        }
        
        /**
         * Build call for createEnterprisesEnrollmentToken
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            EnrollmentTokensCreateEnterprisesEnrollmentTokenRequest enrollmentTokensCreateEnterprisesEnrollmentTokenRequest = buildBodyParams();
            return createEnterprisesEnrollmentTokenCall(enterpriseObjectId, enrollmentTokensCreateEnterprisesEnrollmentTokenRequest, _callback);
        }

        private EnrollmentTokensCreateEnterprisesEnrollmentTokenRequest buildBodyParams() {
            EnrollmentTokensCreateEnterprisesEnrollmentTokenRequest enrollmentTokensCreateEnterprisesEnrollmentTokenRequest = new EnrollmentTokensCreateEnterprisesEnrollmentTokenRequest();
            enrollmentTokensCreateEnterprisesEnrollmentTokenRequest.allowPersonalUsage(this.allowPersonalUsage);
            enrollmentTokensCreateEnterprisesEnrollmentTokenRequest.createdWhere(this.createdWhere);
            enrollmentTokensCreateEnterprisesEnrollmentTokenRequest.displayName(this.displayName);
            enrollmentTokensCreateEnterprisesEnrollmentTokenRequest.duration(this.duration);
            enrollmentTokensCreateEnterprisesEnrollmentTokenRequest.enrollmentType(this.enrollmentType);
            enrollmentTokensCreateEnterprisesEnrollmentTokenRequest.oneTimeOnly(this.oneTimeOnly);
            enrollmentTokensCreateEnterprisesEnrollmentTokenRequest.provisioningExtras(this.provisioningExtras);
            enrollmentTokensCreateEnterprisesEnrollmentTokenRequest.userObjectId(this.userObjectId);
            enrollmentTokensCreateEnterprisesEnrollmentTokenRequest.zeroTouch(this.zeroTouch);
            return enrollmentTokensCreateEnterprisesEnrollmentTokenRequest;
        }

        /**
         * Execute createEnterprisesEnrollmentToken request
         * @return JumpcloudGoogleEmmEnrollmentToken
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public JumpcloudGoogleEmmEnrollmentToken execute() throws ApiException {
            EnrollmentTokensCreateEnterprisesEnrollmentTokenRequest enrollmentTokensCreateEnterprisesEnrollmentTokenRequest = buildBodyParams();
            ApiResponse<JumpcloudGoogleEmmEnrollmentToken> localVarResp = createEnterprisesEnrollmentTokenWithHttpInfo(enterpriseObjectId, enrollmentTokensCreateEnterprisesEnrollmentTokenRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute createEnterprisesEnrollmentToken request with HTTP info returned
         * @return ApiResponse&lt;JumpcloudGoogleEmmEnrollmentToken&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<JumpcloudGoogleEmmEnrollmentToken> executeWithHttpInfo() throws ApiException {
            EnrollmentTokensCreateEnterprisesEnrollmentTokenRequest enrollmentTokensCreateEnterprisesEnrollmentTokenRequest = buildBodyParams();
            return createEnterprisesEnrollmentTokenWithHttpInfo(enterpriseObjectId, enrollmentTokensCreateEnterprisesEnrollmentTokenRequest);
        }

        /**
         * Execute createEnterprisesEnrollmentToken request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<JumpcloudGoogleEmmEnrollmentToken> _callback) throws ApiException {
            EnrollmentTokensCreateEnterprisesEnrollmentTokenRequest enrollmentTokensCreateEnterprisesEnrollmentTokenRequest = buildBodyParams();
            return createEnterprisesEnrollmentTokenAsync(enterpriseObjectId, enrollmentTokensCreateEnterprisesEnrollmentTokenRequest, _callback);
        }
    }

    /**
     * Create an enrollment token for a given enterprise
     * Gets an enrollment token to enroll a device into Google EMM.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/google-emm/enterpries/{enterprise_object_id}/enrollment-tokens \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     * @param enterpriseObjectId  (required)
     * @param enrollmentTokensCreateEnterprisesEnrollmentTokenRequest  (required)
     * @return CreateEnterprisesEnrollmentTokenRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
     </table>
     */
    public CreateEnterprisesEnrollmentTokenRequestBuilder createEnterprisesEnrollmentToken(byte[] enterpriseObjectId) throws IllegalArgumentException {
        if (enterpriseObjectId == null) throw new IllegalArgumentException("\"enterpriseObjectId\" is required but got null");
        return new CreateEnterprisesEnrollmentTokenRequestBuilder(enterpriseObjectId);
    }
    private okhttp3.Call createWebTokenCall(JumpcloudGoogleEmmCreateWebTokenRequest jumpcloudGoogleEmmCreateWebTokenRequest, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = jumpcloudGoogleEmmCreateWebTokenRequest;

        // create path and map variables
        String localVarPath = "/google-emm/web-tokens";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "x-api-key" };
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call createWebTokenValidateBeforeCall(JumpcloudGoogleEmmCreateWebTokenRequest jumpcloudGoogleEmmCreateWebTokenRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'jumpcloudGoogleEmmCreateWebTokenRequest' is set
        if (jumpcloudGoogleEmmCreateWebTokenRequest == null) {
            throw new ApiException("Missing the required parameter 'jumpcloudGoogleEmmCreateWebTokenRequest' when calling createWebToken(Async)");
        }

        return createWebTokenCall(jumpcloudGoogleEmmCreateWebTokenRequest, _callback);

    }


    private ApiResponse<JumpcloudGoogleEmmWebToken> createWebTokenWithHttpInfo(JumpcloudGoogleEmmCreateWebTokenRequest jumpcloudGoogleEmmCreateWebTokenRequest) throws ApiException {
        okhttp3.Call localVarCall = createWebTokenValidateBeforeCall(jumpcloudGoogleEmmCreateWebTokenRequest, null);
        Type localVarReturnType = new TypeToken<JumpcloudGoogleEmmWebToken>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call createWebTokenAsync(JumpcloudGoogleEmmCreateWebTokenRequest jumpcloudGoogleEmmCreateWebTokenRequest, final ApiCallback<JumpcloudGoogleEmmWebToken> _callback) throws ApiException {

        okhttp3.Call localVarCall = createWebTokenValidateBeforeCall(jumpcloudGoogleEmmCreateWebTokenRequest, _callback);
        Type localVarReturnType = new TypeToken<JumpcloudGoogleEmmWebToken>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class CreateWebTokenRequestBuilder {
        private byte[] enterpriseObjectId;
        private JumpcloudGoogleEmmFeature iframeFeature;
        private String parentFrameUrl;

        private CreateWebTokenRequestBuilder() {
        }

        /**
         * Set enterpriseObjectId
         * @param enterpriseObjectId  (optional)
         * @return CreateWebTokenRequestBuilder
         */
        public CreateWebTokenRequestBuilder enterpriseObjectId(byte[] enterpriseObjectId) {
            this.enterpriseObjectId = enterpriseObjectId;
            return this;
        }
        
        /**
         * Set iframeFeature
         * @param iframeFeature  (optional, default to SOFTWARE_MANAGEMENT)
         * @return CreateWebTokenRequestBuilder
         */
        public CreateWebTokenRequestBuilder iframeFeature(JumpcloudGoogleEmmFeature iframeFeature) {
            this.iframeFeature = iframeFeature;
            return this;
        }
        
        /**
         * Set parentFrameUrl
         * @param parentFrameUrl  (optional)
         * @return CreateWebTokenRequestBuilder
         */
        public CreateWebTokenRequestBuilder parentFrameUrl(String parentFrameUrl) {
            this.parentFrameUrl = parentFrameUrl;
            return this;
        }
        
        /**
         * Build call for createWebToken
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            JumpcloudGoogleEmmCreateWebTokenRequest jumpcloudGoogleEmmCreateWebTokenRequest = buildBodyParams();
            return createWebTokenCall(jumpcloudGoogleEmmCreateWebTokenRequest, _callback);
        }

        private JumpcloudGoogleEmmCreateWebTokenRequest buildBodyParams() {
            JumpcloudGoogleEmmCreateWebTokenRequest jumpcloudGoogleEmmCreateWebTokenRequest = new JumpcloudGoogleEmmCreateWebTokenRequest();
            jumpcloudGoogleEmmCreateWebTokenRequest.enterpriseObjectId(this.enterpriseObjectId);
            jumpcloudGoogleEmmCreateWebTokenRequest.iframeFeature(this.iframeFeature);
            jumpcloudGoogleEmmCreateWebTokenRequest.parentFrameUrl(this.parentFrameUrl);
            return jumpcloudGoogleEmmCreateWebTokenRequest;
        }

        /**
         * Execute createWebToken request
         * @return JumpcloudGoogleEmmWebToken
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public JumpcloudGoogleEmmWebToken execute() throws ApiException {
            JumpcloudGoogleEmmCreateWebTokenRequest jumpcloudGoogleEmmCreateWebTokenRequest = buildBodyParams();
            ApiResponse<JumpcloudGoogleEmmWebToken> localVarResp = createWebTokenWithHttpInfo(jumpcloudGoogleEmmCreateWebTokenRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute createWebToken request with HTTP info returned
         * @return ApiResponse&lt;JumpcloudGoogleEmmWebToken&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<JumpcloudGoogleEmmWebToken> executeWithHttpInfo() throws ApiException {
            JumpcloudGoogleEmmCreateWebTokenRequest jumpcloudGoogleEmmCreateWebTokenRequest = buildBodyParams();
            return createWebTokenWithHttpInfo(jumpcloudGoogleEmmCreateWebTokenRequest);
        }

        /**
         * Execute createWebToken request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<JumpcloudGoogleEmmWebToken> _callback) throws ApiException {
            JumpcloudGoogleEmmCreateWebTokenRequest jumpcloudGoogleEmmCreateWebTokenRequest = buildBodyParams();
            return createWebTokenAsync(jumpcloudGoogleEmmCreateWebTokenRequest, _callback);
        }
    }

    /**
     * Get a web token to render Google Play iFrame
     * Creates a web token to access an embeddable managed Google Play web UI for a given Google EMM enterprise.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/google-emm/web-tokens \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     * @param jumpcloudGoogleEmmCreateWebTokenRequest  (required)
     * @return CreateWebTokenRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
     </table>
     */
    public CreateWebTokenRequestBuilder createWebToken() throws IllegalArgumentException {
        return new CreateWebTokenRequestBuilder();
    }
    private okhttp3.Call deleteEnrollmentTokenCall(byte[] enterpriseId, String tokenId, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/google-emm/enterprises/{enterpriseId}/enrollment-tokens/{tokenId}"
            .replace("{" + "enterpriseId" + "}", localVarApiClient.escapeString(enterpriseId.toString()))
            .replace("{" + "tokenId" + "}", localVarApiClient.escapeString(tokenId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "x-api-key" };
        return localVarApiClient.buildCall(basePath, localVarPath, "DELETE", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call deleteEnrollmentTokenValidateBeforeCall(byte[] enterpriseId, String tokenId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'enterpriseId' is set
        if (enterpriseId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseId' when calling deleteEnrollmentToken(Async)");
        }

        // verify the required parameter 'tokenId' is set
        if (tokenId == null) {
            throw new ApiException("Missing the required parameter 'tokenId' when calling deleteEnrollmentToken(Async)");
        }

        return deleteEnrollmentTokenCall(enterpriseId, tokenId, _callback);

    }


    private ApiResponse<JumpcloudGoogleEmmDeleteEnrollmentTokenResponse> deleteEnrollmentTokenWithHttpInfo(byte[] enterpriseId, String tokenId) throws ApiException {
        okhttp3.Call localVarCall = deleteEnrollmentTokenValidateBeforeCall(enterpriseId, tokenId, null);
        Type localVarReturnType = new TypeToken<JumpcloudGoogleEmmDeleteEnrollmentTokenResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call deleteEnrollmentTokenAsync(byte[] enterpriseId, String tokenId, final ApiCallback<JumpcloudGoogleEmmDeleteEnrollmentTokenResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteEnrollmentTokenValidateBeforeCall(enterpriseId, tokenId, _callback);
        Type localVarReturnType = new TypeToken<JumpcloudGoogleEmmDeleteEnrollmentTokenResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class DeleteEnrollmentTokenRequestBuilder {
        private final byte[] enterpriseId;
        private final String tokenId;

        private DeleteEnrollmentTokenRequestBuilder(byte[] enterpriseId, String tokenId) {
            this.enterpriseId = enterpriseId;
            this.tokenId = tokenId;
        }

        /**
         * Build call for deleteEnrollmentToken
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return deleteEnrollmentTokenCall(enterpriseId, tokenId, _callback);
        }


        /**
         * Execute deleteEnrollmentToken request
         * @return JumpcloudGoogleEmmDeleteEnrollmentTokenResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public JumpcloudGoogleEmmDeleteEnrollmentTokenResponse execute() throws ApiException {
            ApiResponse<JumpcloudGoogleEmmDeleteEnrollmentTokenResponse> localVarResp = deleteEnrollmentTokenWithHttpInfo(enterpriseId, tokenId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute deleteEnrollmentToken request with HTTP info returned
         * @return ApiResponse&lt;JumpcloudGoogleEmmDeleteEnrollmentTokenResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<JumpcloudGoogleEmmDeleteEnrollmentTokenResponse> executeWithHttpInfo() throws ApiException {
            return deleteEnrollmentTokenWithHttpInfo(enterpriseId, tokenId);
        }

        /**
         * Execute deleteEnrollmentToken request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<JumpcloudGoogleEmmDeleteEnrollmentTokenResponse> _callback) throws ApiException {
            return deleteEnrollmentTokenAsync(enterpriseId, tokenId, _callback);
        }
    }

    /**
     * Deletes an enrollment token for a given enterprise and token id
     * Removes an Enrollment token for a given enterprise and token id.  #### Sample Request &#x60;&#x60;&#x60; curl -X DELETE https://console.jumpcloud.com/api/v2/enterprises/{enterprise_id}/enrollment-tokens/{token_id} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     * @param enterpriseId  (required)
     * @param tokenId  (required)
     * @return DeleteEnrollmentTokenRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
     </table>
     */
    public DeleteEnrollmentTokenRequestBuilder deleteEnrollmentToken(byte[] enterpriseId, String tokenId) throws IllegalArgumentException {
        if (enterpriseId == null) throw new IllegalArgumentException("\"enterpriseId\" is required but got null");
        if (tokenId == null) throw new IllegalArgumentException("\"tokenId\" is required but got null");
            

        return new DeleteEnrollmentTokenRequestBuilder(enterpriseId, tokenId);
    }
    private okhttp3.Call deleteEnterpriseCall(byte[] enterpriseId, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/google-emm/enterprises/{enterpriseId}"
            .replace("{" + "enterpriseId" + "}", localVarApiClient.escapeString(enterpriseId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "x-api-key" };
        return localVarApiClient.buildCall(basePath, localVarPath, "DELETE", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call deleteEnterpriseValidateBeforeCall(byte[] enterpriseId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'enterpriseId' is set
        if (enterpriseId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseId' when calling deleteEnterprise(Async)");
        }

        return deleteEnterpriseCall(enterpriseId, _callback);

    }


    private ApiResponse<Object> deleteEnterpriseWithHttpInfo(byte[] enterpriseId) throws ApiException {
        okhttp3.Call localVarCall = deleteEnterpriseValidateBeforeCall(enterpriseId, null);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call deleteEnterpriseAsync(byte[] enterpriseId, final ApiCallback<Object> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteEnterpriseValidateBeforeCall(enterpriseId, _callback);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class DeleteEnterpriseRequestBuilder {
        private final byte[] enterpriseId;

        private DeleteEnterpriseRequestBuilder(byte[] enterpriseId) {
            this.enterpriseId = enterpriseId;
        }

        /**
         * Build call for deleteEnterprise
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return deleteEnterpriseCall(enterpriseId, _callback);
        }


        /**
         * Execute deleteEnterprise request
         * @return Object
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public Object execute() throws ApiException {
            ApiResponse<Object> localVarResp = deleteEnterpriseWithHttpInfo(enterpriseId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute deleteEnterprise request with HTTP info returned
         * @return ApiResponse&lt;Object&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Object> executeWithHttpInfo() throws ApiException {
            return deleteEnterpriseWithHttpInfo(enterpriseId);
        }

        /**
         * Execute deleteEnterprise request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Object> _callback) throws ApiException {
            return deleteEnterpriseAsync(enterpriseId, _callback);
        }
    }

    /**
     * Delete a Google Enterprise
     * Removes a Google EMM enterprise.   Warning: This is a destructive operation and will remove all data associated with Google EMM enterprise from JumpCloud including devices and applications associated with the given enterprise.  #### Sample Request &#x60;&#x60;&#x60; curl -X DELETE https://console.jumpcloud.com/api/v2/google-emm/devices/{enterpriseId} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     * @param enterpriseId  (required)
     * @return DeleteEnterpriseRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
     </table>
     */
    public DeleteEnterpriseRequestBuilder deleteEnterprise(byte[] enterpriseId) throws IllegalArgumentException {
        if (enterpriseId == null) throw new IllegalArgumentException("\"enterpriseId\" is required but got null");
        return new DeleteEnterpriseRequestBuilder(enterpriseId);
    }
    private okhttp3.Call eraseDeviceCall(byte[] deviceId, Object body, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = body;

        // create path and map variables
        String localVarPath = "/google-emm/devices/{deviceId}/erase-device"
            .replace("{" + "deviceId" + "}", localVarApiClient.escapeString(deviceId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "x-api-key" };
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call eraseDeviceValidateBeforeCall(byte[] deviceId, Object body, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'deviceId' is set
        if (deviceId == null) {
            throw new ApiException("Missing the required parameter 'deviceId' when calling eraseDevice(Async)");
        }

        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling eraseDevice(Async)");
        }

        return eraseDeviceCall(deviceId, body, _callback);

    }


    private ApiResponse<Object> eraseDeviceWithHttpInfo(byte[] deviceId, Object body) throws ApiException {
        okhttp3.Call localVarCall = eraseDeviceValidateBeforeCall(deviceId, body, null);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call eraseDeviceAsync(byte[] deviceId, Object body, final ApiCallback<Object> _callback) throws ApiException {

        okhttp3.Call localVarCall = eraseDeviceValidateBeforeCall(deviceId, body, _callback);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class EraseDeviceRequestBuilder {
        private final byte[] deviceId;

        private EraseDeviceRequestBuilder(byte[] deviceId) {
            this.deviceId = deviceId;
        }

        /**
         * Build call for eraseDevice
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            Object body = buildBodyParams();
            return eraseDeviceCall(deviceId, body, _callback);
        }

        private Object buildBodyParams() {
            Object body = new Object();
            return body;
        }

        /**
         * Execute eraseDevice request
         * @return Object
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public Object execute() throws ApiException {
            Object body = buildBodyParams();
            ApiResponse<Object> localVarResp = eraseDeviceWithHttpInfo(deviceId, body);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute eraseDevice request with HTTP info returned
         * @return ApiResponse&lt;Object&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Object> executeWithHttpInfo() throws ApiException {
            Object body = buildBodyParams();
            return eraseDeviceWithHttpInfo(deviceId, body);
        }

        /**
         * Execute eraseDevice request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Object> _callback) throws ApiException {
            Object body = buildBodyParams();
            return eraseDeviceAsync(deviceId, body, _callback);
        }
    }

    /**
     * Erase the Android Device
     * Removes the work profile and all policies from a personal/company-owned Android 8.0+ device. Company owned devices will be relinquished for personal use. Apps and data associated with the personal profile(s) are preserved.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/google-emm/devices/{deviceId}/erase-device \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     * @param deviceId  (required)
     * @param body  (required)
     * @return EraseDeviceRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
     </table>
     */
    public EraseDeviceRequestBuilder eraseDevice(byte[] deviceId) throws IllegalArgumentException {
        if (deviceId == null) throw new IllegalArgumentException("\"deviceId\" is required but got null");
        return new EraseDeviceRequestBuilder(deviceId);
    }
    private okhttp3.Call getConnectionStatusCall(byte[] enterpriseId, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/google-emm/enterprises/{enterpriseId}/connection-status"
            .replace("{" + "enterpriseId" + "}", localVarApiClient.escapeString(enterpriseId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "x-api-key" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call getConnectionStatusValidateBeforeCall(byte[] enterpriseId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'enterpriseId' is set
        if (enterpriseId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseId' when calling getConnectionStatus(Async)");
        }

        return getConnectionStatusCall(enterpriseId, _callback);

    }


    private ApiResponse<JumpcloudGoogleEmmConnectionStatus> getConnectionStatusWithHttpInfo(byte[] enterpriseId) throws ApiException {
        okhttp3.Call localVarCall = getConnectionStatusValidateBeforeCall(enterpriseId, null);
        Type localVarReturnType = new TypeToken<JumpcloudGoogleEmmConnectionStatus>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getConnectionStatusAsync(byte[] enterpriseId, final ApiCallback<JumpcloudGoogleEmmConnectionStatus> _callback) throws ApiException {

        okhttp3.Call localVarCall = getConnectionStatusValidateBeforeCall(enterpriseId, _callback);
        Type localVarReturnType = new TypeToken<JumpcloudGoogleEmmConnectionStatus>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetConnectionStatusRequestBuilder {
        private final byte[] enterpriseId;

        private GetConnectionStatusRequestBuilder(byte[] enterpriseId) {
            this.enterpriseId = enterpriseId;
        }

        /**
         * Build call for getConnectionStatus
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getConnectionStatusCall(enterpriseId, _callback);
        }


        /**
         * Execute getConnectionStatus request
         * @return JumpcloudGoogleEmmConnectionStatus
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public JumpcloudGoogleEmmConnectionStatus execute() throws ApiException {
            ApiResponse<JumpcloudGoogleEmmConnectionStatus> localVarResp = getConnectionStatusWithHttpInfo(enterpriseId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getConnectionStatus request with HTTP info returned
         * @return ApiResponse&lt;JumpcloudGoogleEmmConnectionStatus&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<JumpcloudGoogleEmmConnectionStatus> executeWithHttpInfo() throws ApiException {
            return getConnectionStatusWithHttpInfo(enterpriseId);
        }

        /**
         * Execute getConnectionStatus request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<JumpcloudGoogleEmmConnectionStatus> _callback) throws ApiException {
            return getConnectionStatusAsync(enterpriseId, _callback);
        }
    }

    /**
     * Test connection with Google
     * Gives a connection status between JumpCloud and Google.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/google-emm/devices/{enterpriseId}/connection-status \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     * @param enterpriseId  (required)
     * @return GetConnectionStatusRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
     </table>
     */
    public GetConnectionStatusRequestBuilder getConnectionStatus(byte[] enterpriseId) throws IllegalArgumentException {
        if (enterpriseId == null) throw new IllegalArgumentException("\"enterpriseId\" is required but got null");
        return new GetConnectionStatusRequestBuilder(enterpriseId);
    }
    private okhttp3.Call getDeviceCall(byte[] deviceId, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/google-emm/devices/{deviceId}"
            .replace("{" + "deviceId" + "}", localVarApiClient.escapeString(deviceId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "x-api-key" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call getDeviceValidateBeforeCall(byte[] deviceId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'deviceId' is set
        if (deviceId == null) {
            throw new ApiException("Missing the required parameter 'deviceId' when calling getDevice(Async)");
        }

        return getDeviceCall(deviceId, _callback);

    }


    private ApiResponse<JumpcloudGoogleEmmDevice> getDeviceWithHttpInfo(byte[] deviceId) throws ApiException {
        okhttp3.Call localVarCall = getDeviceValidateBeforeCall(deviceId, null);
        Type localVarReturnType = new TypeToken<JumpcloudGoogleEmmDevice>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getDeviceAsync(byte[] deviceId, final ApiCallback<JumpcloudGoogleEmmDevice> _callback) throws ApiException {

        okhttp3.Call localVarCall = getDeviceValidateBeforeCall(deviceId, _callback);
        Type localVarReturnType = new TypeToken<JumpcloudGoogleEmmDevice>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetDeviceRequestBuilder {
        private final byte[] deviceId;

        private GetDeviceRequestBuilder(byte[] deviceId) {
            this.deviceId = deviceId;
        }

        /**
         * Build call for getDevice
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getDeviceCall(deviceId, _callback);
        }


        /**
         * Execute getDevice request
         * @return JumpcloudGoogleEmmDevice
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public JumpcloudGoogleEmmDevice execute() throws ApiException {
            ApiResponse<JumpcloudGoogleEmmDevice> localVarResp = getDeviceWithHttpInfo(deviceId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getDevice request with HTTP info returned
         * @return ApiResponse&lt;JumpcloudGoogleEmmDevice&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<JumpcloudGoogleEmmDevice> executeWithHttpInfo() throws ApiException {
            return getDeviceWithHttpInfo(deviceId);
        }

        /**
         * Execute getDevice request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<JumpcloudGoogleEmmDevice> _callback) throws ApiException {
            return getDeviceAsync(deviceId, _callback);
        }
    }

    /**
     * Get device
     * Gets a Google EMM enrolled device details.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/google-emm/devices/{deviceId} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     * @param deviceId  (required)
     * @return GetDeviceRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
     </table>
     */
    public GetDeviceRequestBuilder getDevice(byte[] deviceId) throws IllegalArgumentException {
        if (deviceId == null) throw new IllegalArgumentException("\"deviceId\" is required but got null");
        return new GetDeviceRequestBuilder(deviceId);
    }
    private okhttp3.Call getDeviceAndroidPolicyCall(byte[] deviceId, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/google-emm/devices/{deviceId}/policy_results"
            .replace("{" + "deviceId" + "}", localVarApiClient.escapeString(deviceId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "x-api-key" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call getDeviceAndroidPolicyValidateBeforeCall(byte[] deviceId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'deviceId' is set
        if (deviceId == null) {
            throw new ApiException("Missing the required parameter 'deviceId' when calling getDeviceAndroidPolicy(Async)");
        }

        return getDeviceAndroidPolicyCall(deviceId, _callback);

    }


    private ApiResponse<JumpcloudGoogleEmmDeviceAndroidPolicy> getDeviceAndroidPolicyWithHttpInfo(byte[] deviceId) throws ApiException {
        okhttp3.Call localVarCall = getDeviceAndroidPolicyValidateBeforeCall(deviceId, null);
        Type localVarReturnType = new TypeToken<JumpcloudGoogleEmmDeviceAndroidPolicy>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getDeviceAndroidPolicyAsync(byte[] deviceId, final ApiCallback<JumpcloudGoogleEmmDeviceAndroidPolicy> _callback) throws ApiException {

        okhttp3.Call localVarCall = getDeviceAndroidPolicyValidateBeforeCall(deviceId, _callback);
        Type localVarReturnType = new TypeToken<JumpcloudGoogleEmmDeviceAndroidPolicy>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetDeviceAndroidPolicyRequestBuilder {
        private final byte[] deviceId;

        private GetDeviceAndroidPolicyRequestBuilder(byte[] deviceId) {
            this.deviceId = deviceId;
        }

        /**
         * Build call for getDeviceAndroidPolicy
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getDeviceAndroidPolicyCall(deviceId, _callback);
        }


        /**
         * Execute getDeviceAndroidPolicy request
         * @return JumpcloudGoogleEmmDeviceAndroidPolicy
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public JumpcloudGoogleEmmDeviceAndroidPolicy execute() throws ApiException {
            ApiResponse<JumpcloudGoogleEmmDeviceAndroidPolicy> localVarResp = getDeviceAndroidPolicyWithHttpInfo(deviceId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getDeviceAndroidPolicy request with HTTP info returned
         * @return ApiResponse&lt;JumpcloudGoogleEmmDeviceAndroidPolicy&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<JumpcloudGoogleEmmDeviceAndroidPolicy> executeWithHttpInfo() throws ApiException {
            return getDeviceAndroidPolicyWithHttpInfo(deviceId);
        }

        /**
         * Execute getDeviceAndroidPolicy request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<JumpcloudGoogleEmmDeviceAndroidPolicy> _callback) throws ApiException {
            return getDeviceAndroidPolicyAsync(deviceId, _callback);
        }
    }

    /**
     * Get the policy JSON of a device
     * Gets an android JSON policy for a Google EMM enrolled device.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/google-emm/devices/{deviceId}/policy_results \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     * @param deviceId  (required)
     * @return GetDeviceAndroidPolicyRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
     </table>
     */
    public GetDeviceAndroidPolicyRequestBuilder getDeviceAndroidPolicy(byte[] deviceId) throws IllegalArgumentException {
        if (deviceId == null) throw new IllegalArgumentException("\"deviceId\" is required but got null");
        return new GetDeviceAndroidPolicyRequestBuilder(deviceId);
    }
    private okhttp3.Call listDevicesCall(byte[] enterpriseObjectId, String limit, String skip, List<String> filter, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/google-emm/enterprises/{enterpriseObjectId}/devices"
            .replace("{" + "enterpriseObjectId" + "}", localVarApiClient.escapeString(enterpriseObjectId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
        }

        if (skip != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("skip", skip));
        }

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("multi", "filter", filter));
        }

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "x-api-key" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call listDevicesValidateBeforeCall(byte[] enterpriseObjectId, String limit, String skip, List<String> filter, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'enterpriseObjectId' is set
        if (enterpriseObjectId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseObjectId' when calling listDevices(Async)");
        }

        return listDevicesCall(enterpriseObjectId, limit, skip, filter, _callback);

    }


    private ApiResponse<JumpcloudGoogleEmmListDevicesResponse> listDevicesWithHttpInfo(byte[] enterpriseObjectId, String limit, String skip, List<String> filter) throws ApiException {
        okhttp3.Call localVarCall = listDevicesValidateBeforeCall(enterpriseObjectId, limit, skip, filter, null);
        Type localVarReturnType = new TypeToken<JumpcloudGoogleEmmListDevicesResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listDevicesAsync(byte[] enterpriseObjectId, String limit, String skip, List<String> filter, final ApiCallback<JumpcloudGoogleEmmListDevicesResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listDevicesValidateBeforeCall(enterpriseObjectId, limit, skip, filter, _callback);
        Type localVarReturnType = new TypeToken<JumpcloudGoogleEmmListDevicesResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListDevicesRequestBuilder {
        private final byte[] enterpriseObjectId;
        private String limit;
        private String skip;
        private List<String> filter;

        private ListDevicesRequestBuilder(byte[] enterpriseObjectId) {
            this.enterpriseObjectId = enterpriseObjectId;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 100)
         * @return ListDevicesRequestBuilder
         */
        public ListDevicesRequestBuilder limit(String limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListDevicesRequestBuilder
         */
        public ListDevicesRequestBuilder skip(String skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set filter
         * @param filter  (optional)
         * @return ListDevicesRequestBuilder
         */
        public ListDevicesRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Build call for listDevices
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listDevicesCall(enterpriseObjectId, limit, skip, filter, _callback);
        }


        /**
         * Execute listDevices request
         * @return JumpcloudGoogleEmmListDevicesResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public JumpcloudGoogleEmmListDevicesResponse execute() throws ApiException {
            ApiResponse<JumpcloudGoogleEmmListDevicesResponse> localVarResp = listDevicesWithHttpInfo(enterpriseObjectId, limit, skip, filter);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listDevices request with HTTP info returned
         * @return ApiResponse&lt;JumpcloudGoogleEmmListDevicesResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<JumpcloudGoogleEmmListDevicesResponse> executeWithHttpInfo() throws ApiException {
            return listDevicesWithHttpInfo(enterpriseObjectId, limit, skip, filter);
        }

        /**
         * Execute listDevices request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<JumpcloudGoogleEmmListDevicesResponse> _callback) throws ApiException {
            return listDevicesAsync(enterpriseObjectId, limit, skip, filter, _callback);
        }
    }

    /**
     * List devices
     * Lists google EMM enrolled devices.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/google-emm/enterprises/{enterprise_object_id}/devices \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     * @param enterpriseObjectId  (required)
     * @return ListDevicesRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
     </table>
     */
    public ListDevicesRequestBuilder listDevices(byte[] enterpriseObjectId) throws IllegalArgumentException {
        if (enterpriseObjectId == null) throw new IllegalArgumentException("\"enterpriseObjectId\" is required but got null");
        return new ListDevicesRequestBuilder(enterpriseObjectId);
    }
    private okhttp3.Call listEnrollmentTokensCall(byte[] enterpriseObjectId, String limit, String skip, List<String> filter, String sort, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/google-emm/enterprises/{enterpriseObjectId}/enrollment-tokens"
            .replace("{" + "enterpriseObjectId" + "}", localVarApiClient.escapeString(enterpriseObjectId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
        }

        if (skip != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("skip", skip));
        }

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("multi", "filter", filter));
        }

        if (sort != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("sort", sort));
        }

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "x-api-key" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call listEnrollmentTokensValidateBeforeCall(byte[] enterpriseObjectId, String limit, String skip, List<String> filter, String sort, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'enterpriseObjectId' is set
        if (enterpriseObjectId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseObjectId' when calling listEnrollmentTokens(Async)");
        }

        return listEnrollmentTokensCall(enterpriseObjectId, limit, skip, filter, sort, _callback);

    }


    private ApiResponse<JumpcloudGoogleEmmListEnrollmentTokensResponse> listEnrollmentTokensWithHttpInfo(byte[] enterpriseObjectId, String limit, String skip, List<String> filter, String sort) throws ApiException {
        okhttp3.Call localVarCall = listEnrollmentTokensValidateBeforeCall(enterpriseObjectId, limit, skip, filter, sort, null);
        Type localVarReturnType = new TypeToken<JumpcloudGoogleEmmListEnrollmentTokensResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listEnrollmentTokensAsync(byte[] enterpriseObjectId, String limit, String skip, List<String> filter, String sort, final ApiCallback<JumpcloudGoogleEmmListEnrollmentTokensResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listEnrollmentTokensValidateBeforeCall(enterpriseObjectId, limit, skip, filter, sort, _callback);
        Type localVarReturnType = new TypeToken<JumpcloudGoogleEmmListEnrollmentTokensResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListEnrollmentTokensRequestBuilder {
        private final byte[] enterpriseObjectId;
        private String limit;
        private String skip;
        private List<String> filter;
        private String sort;

        private ListEnrollmentTokensRequestBuilder(byte[] enterpriseObjectId) {
            this.enterpriseObjectId = enterpriseObjectId;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 100)
         * @return ListEnrollmentTokensRequestBuilder
         */
        public ListEnrollmentTokensRequestBuilder limit(String limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListEnrollmentTokensRequestBuilder
         */
        public ListEnrollmentTokensRequestBuilder skip(String skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set filter
         * @param filter  (optional)
         * @return ListEnrollmentTokensRequestBuilder
         */
        public ListEnrollmentTokensRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set sort
         * @param sort Use space separated sort parameters to sort the collection. Default sort is ascending. Prefix with - to sort descending. (optional)
         * @return ListEnrollmentTokensRequestBuilder
         */
        public ListEnrollmentTokensRequestBuilder sort(String sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Build call for listEnrollmentTokens
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listEnrollmentTokensCall(enterpriseObjectId, limit, skip, filter, sort, _callback);
        }


        /**
         * Execute listEnrollmentTokens request
         * @return JumpcloudGoogleEmmListEnrollmentTokensResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public JumpcloudGoogleEmmListEnrollmentTokensResponse execute() throws ApiException {
            ApiResponse<JumpcloudGoogleEmmListEnrollmentTokensResponse> localVarResp = listEnrollmentTokensWithHttpInfo(enterpriseObjectId, limit, skip, filter, sort);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listEnrollmentTokens request with HTTP info returned
         * @return ApiResponse&lt;JumpcloudGoogleEmmListEnrollmentTokensResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<JumpcloudGoogleEmmListEnrollmentTokensResponse> executeWithHttpInfo() throws ApiException {
            return listEnrollmentTokensWithHttpInfo(enterpriseObjectId, limit, skip, filter, sort);
        }

        /**
         * Execute listEnrollmentTokens request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<JumpcloudGoogleEmmListEnrollmentTokensResponse> _callback) throws ApiException {
            return listEnrollmentTokensAsync(enterpriseObjectId, limit, skip, filter, sort, _callback);
        }
    }

    /**
     * List enrollment tokens
     * Lists active, unexpired enrollement tokens for a given enterprise.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/google-emm/enterprises/{enterprise_object_id}/enrollment-tokens \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     * @param enterpriseObjectId  (required)
     * @return ListEnrollmentTokensRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
     </table>
     */
    public ListEnrollmentTokensRequestBuilder listEnrollmentTokens(byte[] enterpriseObjectId) throws IllegalArgumentException {
        if (enterpriseObjectId == null) throw new IllegalArgumentException("\"enterpriseObjectId\" is required but got null");
        return new ListEnrollmentTokensRequestBuilder(enterpriseObjectId);
    }
    private okhttp3.Call listEnterprisesCall(String limit, String skip, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/google-emm/enterprises";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
        }

        if (skip != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("skip", skip));
        }

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "x-api-key" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call listEnterprisesValidateBeforeCall(String limit, String skip, final ApiCallback _callback) throws ApiException {
        return listEnterprisesCall(limit, skip, _callback);

    }


    private ApiResponse<JumpcloudGoogleEmmListEnterprisesResponse> listEnterprisesWithHttpInfo(String limit, String skip) throws ApiException {
        okhttp3.Call localVarCall = listEnterprisesValidateBeforeCall(limit, skip, null);
        Type localVarReturnType = new TypeToken<JumpcloudGoogleEmmListEnterprisesResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listEnterprisesAsync(String limit, String skip, final ApiCallback<JumpcloudGoogleEmmListEnterprisesResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listEnterprisesValidateBeforeCall(limit, skip, _callback);
        Type localVarReturnType = new TypeToken<JumpcloudGoogleEmmListEnterprisesResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListEnterprisesRequestBuilder {
        private String limit;
        private String skip;

        private ListEnterprisesRequestBuilder() {
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 100)
         * @return ListEnterprisesRequestBuilder
         */
        public ListEnterprisesRequestBuilder limit(String limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListEnterprisesRequestBuilder
         */
        public ListEnterprisesRequestBuilder skip(String skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Build call for listEnterprises
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listEnterprisesCall(limit, skip, _callback);
        }


        /**
         * Execute listEnterprises request
         * @return JumpcloudGoogleEmmListEnterprisesResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public JumpcloudGoogleEmmListEnterprisesResponse execute() throws ApiException {
            ApiResponse<JumpcloudGoogleEmmListEnterprisesResponse> localVarResp = listEnterprisesWithHttpInfo(limit, skip);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listEnterprises request with HTTP info returned
         * @return ApiResponse&lt;JumpcloudGoogleEmmListEnterprisesResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<JumpcloudGoogleEmmListEnterprisesResponse> executeWithHttpInfo() throws ApiException {
            return listEnterprisesWithHttpInfo(limit, skip);
        }

        /**
         * Execute listEnterprises request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<JumpcloudGoogleEmmListEnterprisesResponse> _callback) throws ApiException {
            return listEnterprisesAsync(limit, skip, _callback);
        }
    }

    /**
     * List Google Enterprises
     * Lists all Google EMM enterprises. An empty list indicates that the Organization is not configured with a Google EMM enterprise yet.    Note: Currently only one Google Enterprise per Organization is supported.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/google-emm/enterprises \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     * @return ListEnterprisesRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
     </table>
     */
    public ListEnterprisesRequestBuilder listEnterprises() throws IllegalArgumentException {
        return new ListEnterprisesRequestBuilder();
    }
    private okhttp3.Call lockDeviceCall(byte[] deviceId, Object body, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = body;

        // create path and map variables
        String localVarPath = "/google-emm/devices/{deviceId}/lock"
            .replace("{" + "deviceId" + "}", localVarApiClient.escapeString(deviceId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "x-api-key" };
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call lockDeviceValidateBeforeCall(byte[] deviceId, Object body, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'deviceId' is set
        if (deviceId == null) {
            throw new ApiException("Missing the required parameter 'deviceId' when calling lockDevice(Async)");
        }

        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling lockDevice(Async)");
        }

        return lockDeviceCall(deviceId, body, _callback);

    }


    private ApiResponse<Object> lockDeviceWithHttpInfo(byte[] deviceId, Object body) throws ApiException {
        okhttp3.Call localVarCall = lockDeviceValidateBeforeCall(deviceId, body, null);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call lockDeviceAsync(byte[] deviceId, Object body, final ApiCallback<Object> _callback) throws ApiException {

        okhttp3.Call localVarCall = lockDeviceValidateBeforeCall(deviceId, body, _callback);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class LockDeviceRequestBuilder {
        private final byte[] deviceId;

        private LockDeviceRequestBuilder(byte[] deviceId) {
            this.deviceId = deviceId;
        }

        /**
         * Build call for lockDevice
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            Object body = buildBodyParams();
            return lockDeviceCall(deviceId, body, _callback);
        }

        private Object buildBodyParams() {
            Object body = new Object();
            return body;
        }

        /**
         * Execute lockDevice request
         * @return Object
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public Object execute() throws ApiException {
            Object body = buildBodyParams();
            ApiResponse<Object> localVarResp = lockDeviceWithHttpInfo(deviceId, body);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute lockDevice request with HTTP info returned
         * @return ApiResponse&lt;Object&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Object> executeWithHttpInfo() throws ApiException {
            Object body = buildBodyParams();
            return lockDeviceWithHttpInfo(deviceId, body);
        }

        /**
         * Execute lockDevice request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Object> _callback) throws ApiException {
            Object body = buildBodyParams();
            return lockDeviceAsync(deviceId, body, _callback);
        }
    }

    /**
     * Lock device
     * Locks a Google EMM enrolled device, as if the lock screen timeout had expired.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/google-emm/devices/{deviceId}/lock \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     * @param deviceId  (required)
     * @param body  (required)
     * @return LockDeviceRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
     </table>
     */
    public LockDeviceRequestBuilder lockDevice(byte[] deviceId) throws IllegalArgumentException {
        if (deviceId == null) throw new IllegalArgumentException("\"deviceId\" is required but got null");
        return new LockDeviceRequestBuilder(deviceId);
    }
    private okhttp3.Call patchEnterpriseCall(byte[] enterpriseId, EnterprisesPatchEnterpriseRequest enterprisesPatchEnterpriseRequest, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = enterprisesPatchEnterpriseRequest;

        // create path and map variables
        String localVarPath = "/google-emm/enterprises/{enterpriseId}"
            .replace("{" + "enterpriseId" + "}", localVarApiClient.escapeString(enterpriseId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "x-api-key" };
        return localVarApiClient.buildCall(basePath, localVarPath, "PATCH", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call patchEnterpriseValidateBeforeCall(byte[] enterpriseId, EnterprisesPatchEnterpriseRequest enterprisesPatchEnterpriseRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'enterpriseId' is set
        if (enterpriseId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseId' when calling patchEnterprise(Async)");
        }

        // verify the required parameter 'enterprisesPatchEnterpriseRequest' is set
        if (enterprisesPatchEnterpriseRequest == null) {
            throw new ApiException("Missing the required parameter 'enterprisesPatchEnterpriseRequest' when calling patchEnterprise(Async)");
        }

        return patchEnterpriseCall(enterpriseId, enterprisesPatchEnterpriseRequest, _callback);

    }


    private ApiResponse<JumpcloudGoogleEmmEnterprise> patchEnterpriseWithHttpInfo(byte[] enterpriseId, EnterprisesPatchEnterpriseRequest enterprisesPatchEnterpriseRequest) throws ApiException {
        okhttp3.Call localVarCall = patchEnterpriseValidateBeforeCall(enterpriseId, enterprisesPatchEnterpriseRequest, null);
        Type localVarReturnType = new TypeToken<JumpcloudGoogleEmmEnterprise>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call patchEnterpriseAsync(byte[] enterpriseId, EnterprisesPatchEnterpriseRequest enterprisesPatchEnterpriseRequest, final ApiCallback<JumpcloudGoogleEmmEnterprise> _callback) throws ApiException {

        okhttp3.Call localVarCall = patchEnterpriseValidateBeforeCall(enterpriseId, enterprisesPatchEnterpriseRequest, _callback);
        Type localVarReturnType = new TypeToken<JumpcloudGoogleEmmEnterprise>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class PatchEnterpriseRequestBuilder {
        private final byte[] enterpriseId;
        private Boolean allowDeviceEnrollment;
        private byte[] deviceGroupId;

        private PatchEnterpriseRequestBuilder(byte[] enterpriseId) {
            this.enterpriseId = enterpriseId;
        }

        /**
         * Set allowDeviceEnrollment
         * @param allowDeviceEnrollment  (optional)
         * @return PatchEnterpriseRequestBuilder
         */
        public PatchEnterpriseRequestBuilder allowDeviceEnrollment(Boolean allowDeviceEnrollment) {
            this.allowDeviceEnrollment = allowDeviceEnrollment;
            return this;
        }
        
        /**
         * Set deviceGroupId
         * @param deviceGroupId  (optional)
         * @return PatchEnterpriseRequestBuilder
         */
        public PatchEnterpriseRequestBuilder deviceGroupId(byte[] deviceGroupId) {
            this.deviceGroupId = deviceGroupId;
            return this;
        }
        
        /**
         * Build call for patchEnterprise
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            EnterprisesPatchEnterpriseRequest enterprisesPatchEnterpriseRequest = buildBodyParams();
            return patchEnterpriseCall(enterpriseId, enterprisesPatchEnterpriseRequest, _callback);
        }

        private EnterprisesPatchEnterpriseRequest buildBodyParams() {
            EnterprisesPatchEnterpriseRequest enterprisesPatchEnterpriseRequest = new EnterprisesPatchEnterpriseRequest();
            enterprisesPatchEnterpriseRequest.allowDeviceEnrollment(this.allowDeviceEnrollment);
            enterprisesPatchEnterpriseRequest.deviceGroupId(this.deviceGroupId);
            return enterprisesPatchEnterpriseRequest;
        }

        /**
         * Execute patchEnterprise request
         * @return JumpcloudGoogleEmmEnterprise
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public JumpcloudGoogleEmmEnterprise execute() throws ApiException {
            EnterprisesPatchEnterpriseRequest enterprisesPatchEnterpriseRequest = buildBodyParams();
            ApiResponse<JumpcloudGoogleEmmEnterprise> localVarResp = patchEnterpriseWithHttpInfo(enterpriseId, enterprisesPatchEnterpriseRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute patchEnterprise request with HTTP info returned
         * @return ApiResponse&lt;JumpcloudGoogleEmmEnterprise&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<JumpcloudGoogleEmmEnterprise> executeWithHttpInfo() throws ApiException {
            EnterprisesPatchEnterpriseRequest enterprisesPatchEnterpriseRequest = buildBodyParams();
            return patchEnterpriseWithHttpInfo(enterpriseId, enterprisesPatchEnterpriseRequest);
        }

        /**
         * Execute patchEnterprise request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<JumpcloudGoogleEmmEnterprise> _callback) throws ApiException {
            EnterprisesPatchEnterpriseRequest enterprisesPatchEnterpriseRequest = buildBodyParams();
            return patchEnterpriseAsync(enterpriseId, enterprisesPatchEnterpriseRequest, _callback);
        }
    }

    /**
     * Update a Google Enterprise
     * Updates a Google EMM enterprise details.  #### Sample Request &#x60;&#x60;&#x60; curl -X PATCH https://console.jumpcloud.com/api/v2/google-emm/enterprises \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{ &#39;allowDeviceEnrollment&#39;: true, &#39;deviceGroupId&#39;: &#39;string&#39; }&#39; \\ &#x60;&#x60;&#x60;
     * @param enterpriseId  (required)
     * @param enterprisesPatchEnterpriseRequest  (required)
     * @return PatchEnterpriseRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
     </table>
     */
    public PatchEnterpriseRequestBuilder patchEnterprise(byte[] enterpriseId) throws IllegalArgumentException {
        if (enterpriseId == null) throw new IllegalArgumentException("\"enterpriseId\" is required but got null");
        return new PatchEnterpriseRequestBuilder(enterpriseId);
    }
    private okhttp3.Call rebootDeviceCall(byte[] deviceId, Object body, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = body;

        // create path and map variables
        String localVarPath = "/google-emm/devices/{deviceId}/reboot"
            .replace("{" + "deviceId" + "}", localVarApiClient.escapeString(deviceId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "x-api-key" };
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call rebootDeviceValidateBeforeCall(byte[] deviceId, Object body, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'deviceId' is set
        if (deviceId == null) {
            throw new ApiException("Missing the required parameter 'deviceId' when calling rebootDevice(Async)");
        }

        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling rebootDevice(Async)");
        }

        return rebootDeviceCall(deviceId, body, _callback);

    }


    private ApiResponse<Object> rebootDeviceWithHttpInfo(byte[] deviceId, Object body) throws ApiException {
        okhttp3.Call localVarCall = rebootDeviceValidateBeforeCall(deviceId, body, null);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call rebootDeviceAsync(byte[] deviceId, Object body, final ApiCallback<Object> _callback) throws ApiException {

        okhttp3.Call localVarCall = rebootDeviceValidateBeforeCall(deviceId, body, _callback);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class RebootDeviceRequestBuilder {
        private final byte[] deviceId;

        private RebootDeviceRequestBuilder(byte[] deviceId) {
            this.deviceId = deviceId;
        }

        /**
         * Build call for rebootDevice
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            Object body = buildBodyParams();
            return rebootDeviceCall(deviceId, body, _callback);
        }

        private Object buildBodyParams() {
            Object body = new Object();
            return body;
        }

        /**
         * Execute rebootDevice request
         * @return Object
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public Object execute() throws ApiException {
            Object body = buildBodyParams();
            ApiResponse<Object> localVarResp = rebootDeviceWithHttpInfo(deviceId, body);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute rebootDevice request with HTTP info returned
         * @return ApiResponse&lt;Object&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Object> executeWithHttpInfo() throws ApiException {
            Object body = buildBodyParams();
            return rebootDeviceWithHttpInfo(deviceId, body);
        }

        /**
         * Execute rebootDevice request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Object> _callback) throws ApiException {
            Object body = buildBodyParams();
            return rebootDeviceAsync(deviceId, body, _callback);
        }
    }

    /**
     * Reboot device
     * Reboots a Google EMM enrolled device. Only supported on fully managed devices running Android 7.0 or higher.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/google-emm/devices/{deviceId}/reboot \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     * @param deviceId  (required)
     * @param body  (required)
     * @return RebootDeviceRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
     </table>
     */
    public RebootDeviceRequestBuilder rebootDevice(byte[] deviceId) throws IllegalArgumentException {
        if (deviceId == null) throw new IllegalArgumentException("\"deviceId\" is required but got null");
        return new RebootDeviceRequestBuilder(deviceId);
    }
    private okhttp3.Call resetPasswordCall(byte[] deviceId, DevicesResetPasswordRequest devicesResetPasswordRequest, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = devicesResetPasswordRequest;

        // create path and map variables
        String localVarPath = "/google-emm/devices/{deviceId}/resetpassword"
            .replace("{" + "deviceId" + "}", localVarApiClient.escapeString(deviceId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "x-api-key" };
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call resetPasswordValidateBeforeCall(byte[] deviceId, DevicesResetPasswordRequest devicesResetPasswordRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'deviceId' is set
        if (deviceId == null) {
            throw new ApiException("Missing the required parameter 'deviceId' when calling resetPassword(Async)");
        }

        // verify the required parameter 'devicesResetPasswordRequest' is set
        if (devicesResetPasswordRequest == null) {
            throw new ApiException("Missing the required parameter 'devicesResetPasswordRequest' when calling resetPassword(Async)");
        }

        return resetPasswordCall(deviceId, devicesResetPasswordRequest, _callback);

    }


    private ApiResponse<Object> resetPasswordWithHttpInfo(byte[] deviceId, DevicesResetPasswordRequest devicesResetPasswordRequest) throws ApiException {
        okhttp3.Call localVarCall = resetPasswordValidateBeforeCall(deviceId, devicesResetPasswordRequest, null);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call resetPasswordAsync(byte[] deviceId, DevicesResetPasswordRequest devicesResetPasswordRequest, final ApiCallback<Object> _callback) throws ApiException {

        okhttp3.Call localVarCall = resetPasswordValidateBeforeCall(deviceId, devicesResetPasswordRequest, _callback);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ResetPasswordRequestBuilder {
        private final byte[] deviceId;
        private List<String> flags;
        private String newPassword;

        private ResetPasswordRequestBuilder(byte[] deviceId) {
            this.deviceId = deviceId;
        }

        /**
         * Set flags
         * @param flags  (optional)
         * @return ResetPasswordRequestBuilder
         */
        public ResetPasswordRequestBuilder flags(List<String> flags) {
            this.flags = flags;
            return this;
        }
        
        /**
         * Set newPassword
         * @param newPassword Not logging as it contains sensitive information. (optional)
         * @return ResetPasswordRequestBuilder
         */
        public ResetPasswordRequestBuilder newPassword(String newPassword) {
            this.newPassword = newPassword;
            return this;
        }
        
        /**
         * Build call for resetPassword
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            DevicesResetPasswordRequest devicesResetPasswordRequest = buildBodyParams();
            return resetPasswordCall(deviceId, devicesResetPasswordRequest, _callback);
        }

        private DevicesResetPasswordRequest buildBodyParams() {
            DevicesResetPasswordRequest devicesResetPasswordRequest = new DevicesResetPasswordRequest();
            devicesResetPasswordRequest.flags(this.flags);
            devicesResetPasswordRequest.newPassword(this.newPassword);
            return devicesResetPasswordRequest;
        }

        /**
         * Execute resetPassword request
         * @return Object
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public Object execute() throws ApiException {
            DevicesResetPasswordRequest devicesResetPasswordRequest = buildBodyParams();
            ApiResponse<Object> localVarResp = resetPasswordWithHttpInfo(deviceId, devicesResetPasswordRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute resetPassword request with HTTP info returned
         * @return ApiResponse&lt;Object&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Object> executeWithHttpInfo() throws ApiException {
            DevicesResetPasswordRequest devicesResetPasswordRequest = buildBodyParams();
            return resetPasswordWithHttpInfo(deviceId, devicesResetPasswordRequest);
        }

        /**
         * Execute resetPassword request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Object> _callback) throws ApiException {
            DevicesResetPasswordRequest devicesResetPasswordRequest = buildBodyParams();
            return resetPasswordAsync(deviceId, devicesResetPasswordRequest, _callback);
        }
    }

    /**
     * Reset Password of a device
     * Reset the user&#39;s password of a Google EMM enrolled device.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/google-emm/devices/{deviceId}/resetpassword \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{ &#39;new_password&#39; : &#39;string&#39; }&#39; \\ &#x60;&#x60;&#x60;
     * @param deviceId  (required)
     * @param devicesResetPasswordRequest  (required)
     * @return ResetPasswordRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
     </table>
     */
    public ResetPasswordRequestBuilder resetPassword(byte[] deviceId) throws IllegalArgumentException {
        if (deviceId == null) throw new IllegalArgumentException("\"deviceId\" is required but got null");
        return new ResetPasswordRequestBuilder(deviceId);
    }
}
