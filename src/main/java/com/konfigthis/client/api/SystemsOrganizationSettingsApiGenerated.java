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


import com.konfigthis.client.model.DevicesGetDefaultPasswordSyncSettingsResponse;
import com.konfigthis.client.model.DevicesGetSignInWithJumpCloudSettingsResponse;
import com.konfigthis.client.model.DevicesSetDefaultPasswordSyncSettingsRequest;
import com.konfigthis.client.model.DevicesSetSignInWithJumpCloudSettingsRequest;
import com.konfigthis.client.model.DevicesSignInWithJumpCloudSetting;
import com.konfigthis.client.model.GoogleRpcStatus;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;

public class SystemsOrganizationSettingsApiGenerated {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public SystemsOrganizationSettingsApiGenerated() throws IllegalArgumentException {
        this(Configuration.getDefaultApiClient());
    }

    public SystemsOrganizationSettingsApiGenerated(ApiClient apiClient) throws IllegalArgumentException {
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

    private okhttp3.Call getDefaultPasswordSyncSettingsCall(byte[] organizationObjectId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/devices/settings/defaultpasswordsync";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (organizationObjectId != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("organizationObjectId", organizationObjectId));
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
    private okhttp3.Call getDefaultPasswordSyncSettingsValidateBeforeCall(byte[] organizationObjectId, final ApiCallback _callback) throws ApiException {
        return getDefaultPasswordSyncSettingsCall(organizationObjectId, _callback);

    }


    private ApiResponse<DevicesGetDefaultPasswordSyncSettingsResponse> getDefaultPasswordSyncSettingsWithHttpInfo(byte[] organizationObjectId) throws ApiException {
        okhttp3.Call localVarCall = getDefaultPasswordSyncSettingsValidateBeforeCall(organizationObjectId, null);
        Type localVarReturnType = new TypeToken<DevicesGetDefaultPasswordSyncSettingsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getDefaultPasswordSyncSettingsAsync(byte[] organizationObjectId, final ApiCallback<DevicesGetDefaultPasswordSyncSettingsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getDefaultPasswordSyncSettingsValidateBeforeCall(organizationObjectId, _callback);
        Type localVarReturnType = new TypeToken<DevicesGetDefaultPasswordSyncSettingsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetDefaultPasswordSyncSettingsRequestBuilder {
        private byte[] organizationObjectId;

        private GetDefaultPasswordSyncSettingsRequestBuilder() {
        }

        /**
         * Set organizationObjectId
         * @param organizationObjectId  (optional)
         * @return GetDefaultPasswordSyncSettingsRequestBuilder
         */
        public GetDefaultPasswordSyncSettingsRequestBuilder organizationObjectId(byte[] organizationObjectId) {
            this.organizationObjectId = organizationObjectId;
            return this;
        }
        
        /**
         * Build call for getDefaultPasswordSyncSettings
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> Setting successfully retrieved. </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getDefaultPasswordSyncSettingsCall(organizationObjectId, _callback);
        }


        /**
         * Execute getDefaultPasswordSyncSettings request
         * @return DevicesGetDefaultPasswordSyncSettingsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> Setting successfully retrieved. </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public DevicesGetDefaultPasswordSyncSettingsResponse execute() throws ApiException {
            ApiResponse<DevicesGetDefaultPasswordSyncSettingsResponse> localVarResp = getDefaultPasswordSyncSettingsWithHttpInfo(organizationObjectId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getDefaultPasswordSyncSettings request with HTTP info returned
         * @return ApiResponse&lt;DevicesGetDefaultPasswordSyncSettingsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> Setting successfully retrieved. </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<DevicesGetDefaultPasswordSyncSettingsResponse> executeWithHttpInfo() throws ApiException {
            return getDefaultPasswordSyncSettingsWithHttpInfo(organizationObjectId);
        }

        /**
         * Execute getDefaultPasswordSyncSettings request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> Setting successfully retrieved. </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DevicesGetDefaultPasswordSyncSettingsResponse> _callback) throws ApiException {
            return getDefaultPasswordSyncSettingsAsync(organizationObjectId, _callback);
        }
    }

    /**
     * Get the Default Password Sync Setting
     * Gets the Default Password Sync Setting for an Organization.
     * @return GetDefaultPasswordSyncSettingsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Setting successfully retrieved. </td><td>  -  </td></tr>
        <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
     </table>
     */
    public GetDefaultPasswordSyncSettingsRequestBuilder getDefaultPasswordSyncSettings() throws IllegalArgumentException {
        return new GetDefaultPasswordSyncSettingsRequestBuilder();
    }
    private okhttp3.Call getSignInWithJumpCloudSettingsCall(byte[] organizationObjectId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/devices/settings/signinwithjumpcloud";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (organizationObjectId != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("organizationObjectId", organizationObjectId));
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
    private okhttp3.Call getSignInWithJumpCloudSettingsValidateBeforeCall(byte[] organizationObjectId, final ApiCallback _callback) throws ApiException {
        return getSignInWithJumpCloudSettingsCall(organizationObjectId, _callback);

    }


    private ApiResponse<DevicesGetSignInWithJumpCloudSettingsResponse> getSignInWithJumpCloudSettingsWithHttpInfo(byte[] organizationObjectId) throws ApiException {
        okhttp3.Call localVarCall = getSignInWithJumpCloudSettingsValidateBeforeCall(organizationObjectId, null);
        Type localVarReturnType = new TypeToken<DevicesGetSignInWithJumpCloudSettingsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getSignInWithJumpCloudSettingsAsync(byte[] organizationObjectId, final ApiCallback<DevicesGetSignInWithJumpCloudSettingsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getSignInWithJumpCloudSettingsValidateBeforeCall(organizationObjectId, _callback);
        Type localVarReturnType = new TypeToken<DevicesGetSignInWithJumpCloudSettingsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetSignInWithJumpCloudSettingsRequestBuilder {
        private byte[] organizationObjectId;

        private GetSignInWithJumpCloudSettingsRequestBuilder() {
        }

        /**
         * Set organizationObjectId
         * @param organizationObjectId  (optional)
         * @return GetSignInWithJumpCloudSettingsRequestBuilder
         */
        public GetSignInWithJumpCloudSettingsRequestBuilder organizationObjectId(byte[] organizationObjectId) {
            this.organizationObjectId = organizationObjectId;
            return this;
        }
        
        /**
         * Build call for getSignInWithJumpCloudSettings
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> Settings successfully retrieved. </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getSignInWithJumpCloudSettingsCall(organizationObjectId, _callback);
        }


        /**
         * Execute getSignInWithJumpCloudSettings request
         * @return DevicesGetSignInWithJumpCloudSettingsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> Settings successfully retrieved. </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public DevicesGetSignInWithJumpCloudSettingsResponse execute() throws ApiException {
            ApiResponse<DevicesGetSignInWithJumpCloudSettingsResponse> localVarResp = getSignInWithJumpCloudSettingsWithHttpInfo(organizationObjectId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getSignInWithJumpCloudSettings request with HTTP info returned
         * @return ApiResponse&lt;DevicesGetSignInWithJumpCloudSettingsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> Settings successfully retrieved. </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<DevicesGetSignInWithJumpCloudSettingsResponse> executeWithHttpInfo() throws ApiException {
            return getSignInWithJumpCloudSettingsWithHttpInfo(organizationObjectId);
        }

        /**
         * Execute getSignInWithJumpCloudSettings request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> Settings successfully retrieved. </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DevicesGetSignInWithJumpCloudSettingsResponse> _callback) throws ApiException {
            return getSignInWithJumpCloudSettingsAsync(organizationObjectId, _callback);
        }
    }

    /**
     * Get the Sign In with JumpCloud Settings
     * Gets the Sign In with JumpCloud Settings for an Organization.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/devices/settings/signinwithjumpcloud \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key:{API_KEY}&#39; &#x60;&#x60;&#x60;
     * @return GetSignInWithJumpCloudSettingsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Settings successfully retrieved. </td><td>  -  </td></tr>
        <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
     </table>
     */
    public GetSignInWithJumpCloudSettingsRequestBuilder getSignInWithJumpCloudSettings() throws IllegalArgumentException {
        return new GetSignInWithJumpCloudSettingsRequestBuilder();
    }
    private okhttp3.Call setDefaultPasswordSyncSettingsCall(DevicesSetDefaultPasswordSyncSettingsRequest devicesSetDefaultPasswordSyncSettingsRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = devicesSetDefaultPasswordSyncSettingsRequest;

        // create path and map variables
        String localVarPath = "/devices/settings/defaultpasswordsync";

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
        return localVarApiClient.buildCall(basePath, localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call setDefaultPasswordSyncSettingsValidateBeforeCall(DevicesSetDefaultPasswordSyncSettingsRequest devicesSetDefaultPasswordSyncSettingsRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'devicesSetDefaultPasswordSyncSettingsRequest' is set
        if (devicesSetDefaultPasswordSyncSettingsRequest == null) {
            throw new ApiException("Missing the required parameter 'devicesSetDefaultPasswordSyncSettingsRequest' when calling setDefaultPasswordSyncSettings(Async)");
        }

        return setDefaultPasswordSyncSettingsCall(devicesSetDefaultPasswordSyncSettingsRequest, _callback);

    }


    private ApiResponse<Object> setDefaultPasswordSyncSettingsWithHttpInfo(DevicesSetDefaultPasswordSyncSettingsRequest devicesSetDefaultPasswordSyncSettingsRequest) throws ApiException {
        okhttp3.Call localVarCall = setDefaultPasswordSyncSettingsValidateBeforeCall(devicesSetDefaultPasswordSyncSettingsRequest, null);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call setDefaultPasswordSyncSettingsAsync(DevicesSetDefaultPasswordSyncSettingsRequest devicesSetDefaultPasswordSyncSettingsRequest, final ApiCallback<Object> _callback) throws ApiException {

        okhttp3.Call localVarCall = setDefaultPasswordSyncSettingsValidateBeforeCall(devicesSetDefaultPasswordSyncSettingsRequest, _callback);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class SetDefaultPasswordSyncSettingsRequestBuilder {
        private Boolean enabled;
        private byte[] organizationObjectId;

        private SetDefaultPasswordSyncSettingsRequestBuilder() {
        }

        /**
         * Set enabled
         * @param enabled  (optional)
         * @return SetDefaultPasswordSyncSettingsRequestBuilder
         */
        public SetDefaultPasswordSyncSettingsRequestBuilder enabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }
        
        /**
         * Set organizationObjectId
         * @param organizationObjectId  (optional)
         * @return SetDefaultPasswordSyncSettingsRequestBuilder
         */
        public SetDefaultPasswordSyncSettingsRequestBuilder organizationObjectId(byte[] organizationObjectId) {
            this.organizationObjectId = organizationObjectId;
            return this;
        }
        
        /**
         * Build call for setDefaultPasswordSyncSettings
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
            <tr><td> 204 </td><td> Setting successfully changed. </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            DevicesSetDefaultPasswordSyncSettingsRequest devicesSetDefaultPasswordSyncSettingsRequest = buildBodyParams();
            return setDefaultPasswordSyncSettingsCall(devicesSetDefaultPasswordSyncSettingsRequest, _callback);
        }

        private DevicesSetDefaultPasswordSyncSettingsRequest buildBodyParams() {
            DevicesSetDefaultPasswordSyncSettingsRequest devicesSetDefaultPasswordSyncSettingsRequest = new DevicesSetDefaultPasswordSyncSettingsRequest();
            devicesSetDefaultPasswordSyncSettingsRequest.enabled(this.enabled);
            devicesSetDefaultPasswordSyncSettingsRequest.organizationObjectId(this.organizationObjectId);
            return devicesSetDefaultPasswordSyncSettingsRequest;
        }

        /**
         * Execute setDefaultPasswordSyncSettings request
         * @return Object
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
            <tr><td> 204 </td><td> Setting successfully changed. </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public Object execute() throws ApiException {
            DevicesSetDefaultPasswordSyncSettingsRequest devicesSetDefaultPasswordSyncSettingsRequest = buildBodyParams();
            ApiResponse<Object> localVarResp = setDefaultPasswordSyncSettingsWithHttpInfo(devicesSetDefaultPasswordSyncSettingsRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute setDefaultPasswordSyncSettings request with HTTP info returned
         * @return ApiResponse&lt;Object&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
            <tr><td> 204 </td><td> Setting successfully changed. </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Object> executeWithHttpInfo() throws ApiException {
            DevicesSetDefaultPasswordSyncSettingsRequest devicesSetDefaultPasswordSyncSettingsRequest = buildBodyParams();
            return setDefaultPasswordSyncSettingsWithHttpInfo(devicesSetDefaultPasswordSyncSettingsRequest);
        }

        /**
         * Execute setDefaultPasswordSyncSettings request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
            <tr><td> 204 </td><td> Setting successfully changed. </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Object> _callback) throws ApiException {
            DevicesSetDefaultPasswordSyncSettingsRequest devicesSetDefaultPasswordSyncSettingsRequest = buildBodyParams();
            return setDefaultPasswordSyncSettingsAsync(devicesSetDefaultPasswordSyncSettingsRequest, _callback);
        }
    }

    /**
     * Set the Default Password Sync Setting
     * Sets the Default Password Sync Setting for an Organization.
     * @param devicesSetDefaultPasswordSyncSettingsRequest  (required)
     * @return SetDefaultPasswordSyncSettingsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
        <tr><td> 204 </td><td> Setting successfully changed. </td><td>  -  </td></tr>
        <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
     </table>
     */
    public SetDefaultPasswordSyncSettingsRequestBuilder setDefaultPasswordSyncSettings() throws IllegalArgumentException {
        return new SetDefaultPasswordSyncSettingsRequestBuilder();
    }
    private okhttp3.Call setSignInWithJumpCloudSettingsCall(DevicesSetSignInWithJumpCloudSettingsRequest devicesSetSignInWithJumpCloudSettingsRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = devicesSetSignInWithJumpCloudSettingsRequest;

        // create path and map variables
        String localVarPath = "/devices/settings/signinwithjumpcloud";

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
        return localVarApiClient.buildCall(basePath, localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call setSignInWithJumpCloudSettingsValidateBeforeCall(DevicesSetSignInWithJumpCloudSettingsRequest devicesSetSignInWithJumpCloudSettingsRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'devicesSetSignInWithJumpCloudSettingsRequest' is set
        if (devicesSetSignInWithJumpCloudSettingsRequest == null) {
            throw new ApiException("Missing the required parameter 'devicesSetSignInWithJumpCloudSettingsRequest' when calling setSignInWithJumpCloudSettings(Async)");
        }

        return setSignInWithJumpCloudSettingsCall(devicesSetSignInWithJumpCloudSettingsRequest, _callback);

    }


    private ApiResponse<Object> setSignInWithJumpCloudSettingsWithHttpInfo(DevicesSetSignInWithJumpCloudSettingsRequest devicesSetSignInWithJumpCloudSettingsRequest) throws ApiException {
        okhttp3.Call localVarCall = setSignInWithJumpCloudSettingsValidateBeforeCall(devicesSetSignInWithJumpCloudSettingsRequest, null);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call setSignInWithJumpCloudSettingsAsync(DevicesSetSignInWithJumpCloudSettingsRequest devicesSetSignInWithJumpCloudSettingsRequest, final ApiCallback<Object> _callback) throws ApiException {

        okhttp3.Call localVarCall = setSignInWithJumpCloudSettingsValidateBeforeCall(devicesSetSignInWithJumpCloudSettingsRequest, _callback);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class SetSignInWithJumpCloudSettingsRequestBuilder {
        private byte[] organizationObjectId;
        private List<DevicesSignInWithJumpCloudSetting> settings;

        private SetSignInWithJumpCloudSettingsRequestBuilder() {
        }

        /**
         * Set organizationObjectId
         * @param organizationObjectId  (optional)
         * @return SetSignInWithJumpCloudSettingsRequestBuilder
         */
        public SetSignInWithJumpCloudSettingsRequestBuilder organizationObjectId(byte[] organizationObjectId) {
            this.organizationObjectId = organizationObjectId;
            return this;
        }
        
        /**
         * Set settings
         * @param settings  (optional)
         * @return SetSignInWithJumpCloudSettingsRequestBuilder
         */
        public SetSignInWithJumpCloudSettingsRequestBuilder settings(List<DevicesSignInWithJumpCloudSetting> settings) {
            this.settings = settings;
            return this;
        }
        
        /**
         * Build call for setSignInWithJumpCloudSettings
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
            <tr><td> 204 </td><td> Settings successfully changed. </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            DevicesSetSignInWithJumpCloudSettingsRequest devicesSetSignInWithJumpCloudSettingsRequest = buildBodyParams();
            return setSignInWithJumpCloudSettingsCall(devicesSetSignInWithJumpCloudSettingsRequest, _callback);
        }

        private DevicesSetSignInWithJumpCloudSettingsRequest buildBodyParams() {
            DevicesSetSignInWithJumpCloudSettingsRequest devicesSetSignInWithJumpCloudSettingsRequest = new DevicesSetSignInWithJumpCloudSettingsRequest();
            devicesSetSignInWithJumpCloudSettingsRequest.organizationObjectId(this.organizationObjectId);
            devicesSetSignInWithJumpCloudSettingsRequest.settings(this.settings);
            return devicesSetSignInWithJumpCloudSettingsRequest;
        }

        /**
         * Execute setSignInWithJumpCloudSettings request
         * @return Object
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
            <tr><td> 204 </td><td> Settings successfully changed. </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public Object execute() throws ApiException {
            DevicesSetSignInWithJumpCloudSettingsRequest devicesSetSignInWithJumpCloudSettingsRequest = buildBodyParams();
            ApiResponse<Object> localVarResp = setSignInWithJumpCloudSettingsWithHttpInfo(devicesSetSignInWithJumpCloudSettingsRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute setSignInWithJumpCloudSettings request with HTTP info returned
         * @return ApiResponse&lt;Object&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
            <tr><td> 204 </td><td> Settings successfully changed. </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Object> executeWithHttpInfo() throws ApiException {
            DevicesSetSignInWithJumpCloudSettingsRequest devicesSetSignInWithJumpCloudSettingsRequest = buildBodyParams();
            return setSignInWithJumpCloudSettingsWithHttpInfo(devicesSetSignInWithJumpCloudSettingsRequest);
        }

        /**
         * Execute setSignInWithJumpCloudSettings request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
            <tr><td> 204 </td><td> Settings successfully changed. </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Object> _callback) throws ApiException {
            DevicesSetSignInWithJumpCloudSettingsRequest devicesSetSignInWithJumpCloudSettingsRequest = buildBodyParams();
            return setSignInWithJumpCloudSettingsAsync(devicesSetSignInWithJumpCloudSettingsRequest, _callback);
        }
    }

    /**
     * Set the Sign In with JumpCloud Settings
     * Sets the Sign In with JumpCloud Settings for an Organization.  #### Sample Request &#x60;&#x60;&#x60; curl -X PUT https://console.jumpcloud.com/api/v2/devices/settings/signinwithjumpcloud \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key:{API_KEY}&#39; \\   -d &#39;{\&quot;settings\&quot;:[{\&quot;osFamily\&quot;:\&quot;WINDOWS\&quot;,\&quot;enabled\&quot;:true,\&quot;defaultPermission\&quot;:\&quot;STANDARD\&quot;}]}&#39; &#x60;&#x60;&#x60;
     * @param devicesSetSignInWithJumpCloudSettingsRequest  (required)
     * @return SetSignInWithJumpCloudSettingsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
        <tr><td> 204 </td><td> Settings successfully changed. </td><td>  -  </td></tr>
        <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
     </table>
     */
    public SetSignInWithJumpCloudSettingsRequestBuilder setSignInWithJumpCloudSettings() throws IllegalArgumentException {
        return new SetSignInWithJumpCloudSettingsRequestBuilder();
    }
}
