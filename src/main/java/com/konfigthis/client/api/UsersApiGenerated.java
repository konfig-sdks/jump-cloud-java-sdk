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


import com.konfigthis.client.model.GraphAttributeSudo;
import com.konfigthis.client.model.GraphConnection;
import com.konfigthis.client.model.GraphObjectWithPaths;
import com.konfigthis.client.model.GraphOperationUser;
import com.konfigthis.client.model.PushEndpointResponse;
import com.konfigthis.client.model.PushEndpointsPatchRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;

public class UsersApiGenerated {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public UsersApiGenerated() throws IllegalArgumentException {
        this(Configuration.getDefaultApiClient());
    }

    public UsersApiGenerated(ApiClient apiClient) throws IllegalArgumentException {
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

    private okhttp3.Call deleteCall(String userId, String pushEndpointId, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/users/{user_id}/pushendpoints/{push_endpoint_id}"
            .replace("{" + "user_id" + "}", localVarApiClient.escapeString(userId.toString()))
            .replace("{" + "push_endpoint_id" + "}", localVarApiClient.escapeString(pushEndpointId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (xOrgId != null) {
            localVarHeaderParams.put("x-org-id", localVarApiClient.parameterToString(xOrgId));
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
        return localVarApiClient.buildCall(basePath, localVarPath, "DELETE", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call deleteValidateBeforeCall(String userId, String pushEndpointId, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling delete(Async)");
        }

        // verify the required parameter 'pushEndpointId' is set
        if (pushEndpointId == null) {
            throw new ApiException("Missing the required parameter 'pushEndpointId' when calling delete(Async)");
        }

        return deleteCall(userId, pushEndpointId, xOrgId, _callback);

    }


    private ApiResponse<PushEndpointResponse> deleteWithHttpInfo(String userId, String pushEndpointId, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = deleteValidateBeforeCall(userId, pushEndpointId, xOrgId, null);
        Type localVarReturnType = new TypeToken<PushEndpointResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call deleteAsync(String userId, String pushEndpointId, String xOrgId, final ApiCallback<PushEndpointResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteValidateBeforeCall(userId, pushEndpointId, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<PushEndpointResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class DeleteRequestBuilder {
        private final String userId;
        private final String pushEndpointId;
        private String xOrgId;

        private DeleteRequestBuilder(String userId, String pushEndpointId) {
            this.userId = userId;
            this.pushEndpointId = pushEndpointId;
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return DeleteRequestBuilder
         */
        public DeleteRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for delete
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return deleteCall(userId, pushEndpointId, xOrgId, _callback);
        }


        /**
         * Execute delete request
         * @return PushEndpointResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public PushEndpointResponse execute() throws ApiException {
            ApiResponse<PushEndpointResponse> localVarResp = deleteWithHttpInfo(userId, pushEndpointId, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute delete request with HTTP info returned
         * @return ApiResponse&lt;PushEndpointResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<PushEndpointResponse> executeWithHttpInfo() throws ApiException {
            return deleteWithHttpInfo(userId, pushEndpointId, xOrgId);
        }

        /**
         * Execute delete request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<PushEndpointResponse> _callback) throws ApiException {
            return deleteAsync(userId, pushEndpointId, xOrgId, _callback);
        }
    }

    /**
     * Delete a Push Endpoint associated with a User
     * This endpoint will delete a push endpoint associated with a user.
     * @param userId  (required)
     * @param pushEndpointId  (required)
     * @return DeleteRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public DeleteRequestBuilder delete(String userId, String pushEndpointId) throws IllegalArgumentException {
        if (userId == null) throw new IllegalArgumentException("\"userId\" is required but got null");
            

        if (pushEndpointId == null) throw new IllegalArgumentException("\"pushEndpointId\" is required but got null");
            

        return new DeleteRequestBuilder(userId, pushEndpointId);
    }
    private okhttp3.Call getCall(String userId, String pushEndpointId, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/users/{user_id}/pushendpoints/{push_endpoint_id}"
            .replace("{" + "user_id" + "}", localVarApiClient.escapeString(userId.toString()))
            .replace("{" + "push_endpoint_id" + "}", localVarApiClient.escapeString(pushEndpointId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (xOrgId != null) {
            localVarHeaderParams.put("x-org-id", localVarApiClient.parameterToString(xOrgId));
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
    private okhttp3.Call getValidateBeforeCall(String userId, String pushEndpointId, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling get(Async)");
        }

        // verify the required parameter 'pushEndpointId' is set
        if (pushEndpointId == null) {
            throw new ApiException("Missing the required parameter 'pushEndpointId' when calling get(Async)");
        }

        return getCall(userId, pushEndpointId, xOrgId, _callback);

    }


    private ApiResponse<PushEndpointResponse> getWithHttpInfo(String userId, String pushEndpointId, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = getValidateBeforeCall(userId, pushEndpointId, xOrgId, null);
        Type localVarReturnType = new TypeToken<PushEndpointResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getAsync(String userId, String pushEndpointId, String xOrgId, final ApiCallback<PushEndpointResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getValidateBeforeCall(userId, pushEndpointId, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<PushEndpointResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetRequestBuilder {
        private final String userId;
        private final String pushEndpointId;
        private String xOrgId;

        private GetRequestBuilder(String userId, String pushEndpointId) {
            this.userId = userId;
            this.pushEndpointId = pushEndpointId;
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return GetRequestBuilder
         */
        public GetRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for get
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getCall(userId, pushEndpointId, xOrgId, _callback);
        }


        /**
         * Execute get request
         * @return PushEndpointResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public PushEndpointResponse execute() throws ApiException {
            ApiResponse<PushEndpointResponse> localVarResp = getWithHttpInfo(userId, pushEndpointId, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute get request with HTTP info returned
         * @return ApiResponse&lt;PushEndpointResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<PushEndpointResponse> executeWithHttpInfo() throws ApiException {
            return getWithHttpInfo(userId, pushEndpointId, xOrgId);
        }

        /**
         * Execute get request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<PushEndpointResponse> _callback) throws ApiException {
            return getAsync(userId, pushEndpointId, xOrgId, _callback);
        }
    }

    /**
     * Get a push endpoint associated with a User
     * This endpoint will retrieve a push endpoint associated with a user.
     * @param userId  (required)
     * @param pushEndpointId  (required)
     * @return GetRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public GetRequestBuilder get(String userId, String pushEndpointId) throws IllegalArgumentException {
        if (userId == null) throw new IllegalArgumentException("\"userId\" is required but got null");
            

        if (pushEndpointId == null) throw new IllegalArgumentException("\"pushEndpointId\" is required but got null");
            

        return new GetRequestBuilder(userId, pushEndpointId);
    }
    private okhttp3.Call listCall(String userId, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/users/{user_id}/pushendpoints"
            .replace("{" + "user_id" + "}", localVarApiClient.escapeString(userId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (xOrgId != null) {
            localVarHeaderParams.put("x-org-id", localVarApiClient.parameterToString(xOrgId));
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
    private okhttp3.Call listValidateBeforeCall(String userId, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling list(Async)");
        }

        return listCall(userId, xOrgId, _callback);

    }


    private ApiResponse<List<PushEndpointResponse>> listWithHttpInfo(String userId, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = listValidateBeforeCall(userId, xOrgId, null);
        Type localVarReturnType = new TypeToken<List<PushEndpointResponse>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listAsync(String userId, String xOrgId, final ApiCallback<List<PushEndpointResponse>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listValidateBeforeCall(userId, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<List<PushEndpointResponse>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListRequestBuilder {
        private final String userId;
        private String xOrgId;

        private ListRequestBuilder(String userId) {
            this.userId = userId;
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListRequestBuilder
         */
        public ListRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for list
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listCall(userId, xOrgId, _callback);
        }


        /**
         * Execute list request
         * @return List&lt;PushEndpointResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<PushEndpointResponse> execute() throws ApiException {
            ApiResponse<List<PushEndpointResponse>> localVarResp = listWithHttpInfo(userId, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute list request with HTTP info returned
         * @return ApiResponse&lt;List&lt;PushEndpointResponse&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<PushEndpointResponse>> executeWithHttpInfo() throws ApiException {
            return listWithHttpInfo(userId, xOrgId);
        }

        /**
         * Execute list request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<PushEndpointResponse>> _callback) throws ApiException {
            return listAsync(userId, xOrgId, _callback);
        }
    }

    /**
     * List Push Endpoints associated with a User
     * This endpoint returns the list of push endpoints associated with a user.  #### Sample Request  &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/users/{UserID}/pushendpoints \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: ${API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param userId  (required)
     * @return ListRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public ListRequestBuilder list(String userId) throws IllegalArgumentException {
        if (userId == null) throw new IllegalArgumentException("\"userId\" is required but got null");
            

        return new ListRequestBuilder(userId);
    }
    private okhttp3.Call patchCall(String userId, String pushEndpointId, String xOrgId, PushEndpointsPatchRequest pushEndpointsPatchRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = pushEndpointsPatchRequest;

        // create path and map variables
        String localVarPath = "/users/{user_id}/pushendpoints/{push_endpoint_id}"
            .replace("{" + "user_id" + "}", localVarApiClient.escapeString(userId.toString()))
            .replace("{" + "push_endpoint_id" + "}", localVarApiClient.escapeString(pushEndpointId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (xOrgId != null) {
            localVarHeaderParams.put("x-org-id", localVarApiClient.parameterToString(xOrgId));
        }

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
    private okhttp3.Call patchValidateBeforeCall(String userId, String pushEndpointId, String xOrgId, PushEndpointsPatchRequest pushEndpointsPatchRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling patch(Async)");
        }

        // verify the required parameter 'pushEndpointId' is set
        if (pushEndpointId == null) {
            throw new ApiException("Missing the required parameter 'pushEndpointId' when calling patch(Async)");
        }

        return patchCall(userId, pushEndpointId, xOrgId, pushEndpointsPatchRequest, _callback);

    }


    private ApiResponse<PushEndpointResponse> patchWithHttpInfo(String userId, String pushEndpointId, String xOrgId, PushEndpointsPatchRequest pushEndpointsPatchRequest) throws ApiException {
        okhttp3.Call localVarCall = patchValidateBeforeCall(userId, pushEndpointId, xOrgId, pushEndpointsPatchRequest, null);
        Type localVarReturnType = new TypeToken<PushEndpointResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call patchAsync(String userId, String pushEndpointId, String xOrgId, PushEndpointsPatchRequest pushEndpointsPatchRequest, final ApiCallback<PushEndpointResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = patchValidateBeforeCall(userId, pushEndpointId, xOrgId, pushEndpointsPatchRequest, _callback);
        Type localVarReturnType = new TypeToken<PushEndpointResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class PatchRequestBuilder {
        private final String userId;
        private final String pushEndpointId;
        private String name;
        private String state;
        private String xOrgId;

        private PatchRequestBuilder(String userId, String pushEndpointId) {
            this.userId = userId;
            this.pushEndpointId = pushEndpointId;
        }

        /**
         * Set name
         * @param name  (optional)
         * @return PatchRequestBuilder
         */
        public PatchRequestBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        /**
         * Set state
         * @param state  (optional)
         * @return PatchRequestBuilder
         */
        public PatchRequestBuilder state(String state) {
            this.state = state;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return PatchRequestBuilder
         */
        public PatchRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for patch
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            PushEndpointsPatchRequest pushEndpointsPatchRequest = buildBodyParams();
            return patchCall(userId, pushEndpointId, xOrgId, pushEndpointsPatchRequest, _callback);
        }

        private PushEndpointsPatchRequest buildBodyParams() {
            PushEndpointsPatchRequest pushEndpointsPatchRequest = new PushEndpointsPatchRequest();
            pushEndpointsPatchRequest.name(this.name);
            if (this.state != null)
            pushEndpointsPatchRequest.state(PushEndpointsPatchRequest.StateEnum.fromValue(this.state));
            return pushEndpointsPatchRequest;
        }

        /**
         * Execute patch request
         * @return PushEndpointResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public PushEndpointResponse execute() throws ApiException {
            PushEndpointsPatchRequest pushEndpointsPatchRequest = buildBodyParams();
            ApiResponse<PushEndpointResponse> localVarResp = patchWithHttpInfo(userId, pushEndpointId, xOrgId, pushEndpointsPatchRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute patch request with HTTP info returned
         * @return ApiResponse&lt;PushEndpointResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<PushEndpointResponse> executeWithHttpInfo() throws ApiException {
            PushEndpointsPatchRequest pushEndpointsPatchRequest = buildBodyParams();
            return patchWithHttpInfo(userId, pushEndpointId, xOrgId, pushEndpointsPatchRequest);
        }

        /**
         * Execute patch request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<PushEndpointResponse> _callback) throws ApiException {
            PushEndpointsPatchRequest pushEndpointsPatchRequest = buildBodyParams();
            return patchAsync(userId, pushEndpointId, xOrgId, pushEndpointsPatchRequest, _callback);
        }
    }

    /**
     * Update a push endpoint associated with a User
     * This endpoint will update a push endpoint associated with a user.
     * @param userId  (required)
     * @param pushEndpointId  (required)
     * @return PatchRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public PatchRequestBuilder patch(String userId, String pushEndpointId) throws IllegalArgumentException {
        if (userId == null) throw new IllegalArgumentException("\"userId\" is required but got null");
            

        if (pushEndpointId == null) throw new IllegalArgumentException("\"pushEndpointId\" is required but got null");
            

        return new PatchRequestBuilder(userId, pushEndpointId);
    }
    private okhttp3.Call userAssociationsListCall(String userId, List<String> targets, Integer limit, Integer skip, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/users/{user_id}/associations"
            .replace("{" + "user_id" + "}", localVarApiClient.escapeString(userId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (targets != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "targets", targets));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
        }

        if (skip != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("skip", skip));
        }

        if (xOrgId != null) {
            localVarHeaderParams.put("x-org-id", localVarApiClient.parameterToString(xOrgId));
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
    private okhttp3.Call userAssociationsListValidateBeforeCall(String userId, List<String> targets, Integer limit, Integer skip, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling userAssociationsList(Async)");
        }

        // verify the required parameter 'targets' is set
        if (targets == null) {
            throw new ApiException("Missing the required parameter 'targets' when calling userAssociationsList(Async)");
        }

        return userAssociationsListCall(userId, targets, limit, skip, xOrgId, _callback);

    }


    private ApiResponse<List<GraphConnection>> userAssociationsListWithHttpInfo(String userId, List<String> targets, Integer limit, Integer skip, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = userAssociationsListValidateBeforeCall(userId, targets, limit, skip, xOrgId, null);
        Type localVarReturnType = new TypeToken<List<GraphConnection>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call userAssociationsListAsync(String userId, List<String> targets, Integer limit, Integer skip, String xOrgId, final ApiCallback<List<GraphConnection>> _callback) throws ApiException {

        okhttp3.Call localVarCall = userAssociationsListValidateBeforeCall(userId, targets, limit, skip, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<List<GraphConnection>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UserAssociationsListRequestBuilder {
        private final String userId;
        private final List<String> targets;
        private Integer limit;
        private Integer skip;
        private String xOrgId;

        private UserAssociationsListRequestBuilder(String userId, List<String> targets) {
            this.userId = userId;
            this.targets = targets;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return UserAssociationsListRequestBuilder
         */
        public UserAssociationsListRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return UserAssociationsListRequestBuilder
         */
        public UserAssociationsListRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UserAssociationsListRequestBuilder
         */
        public UserAssociationsListRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for userAssociationsList
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return userAssociationsListCall(userId, targets, limit, skip, xOrgId, _callback);
        }


        /**
         * Execute userAssociationsList request
         * @return List&lt;GraphConnection&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphConnection> execute() throws ApiException {
            ApiResponse<List<GraphConnection>> localVarResp = userAssociationsListWithHttpInfo(userId, targets, limit, skip, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute userAssociationsList request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphConnection&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphConnection>> executeWithHttpInfo() throws ApiException {
            return userAssociationsListWithHttpInfo(userId, targets, limit, skip, xOrgId);
        }

        /**
         * Execute userAssociationsList request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<GraphConnection>> _callback) throws ApiException {
            return userAssociationsListAsync(userId, targets, limit, skip, xOrgId, _callback);
        }
    }

    /**
     * List the associations of a User
     * This endpoint returns the _direct_ associations of a User.  A direct association can be a non-homogeneous relationship between 2 different objects, for example Users and Systems.   #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/users/{UserID}/associations?targets&#x3D;system_group \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;  &#x60;&#x60;&#x60;
     * @param userId ObjectID of the User. (required)
     * @param targets Targets which a \&quot;user\&quot; can be associated to. (required)
     * @return UserAssociationsListRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UserAssociationsListRequestBuilder userAssociationsList(String userId, List<String> targets) throws IllegalArgumentException {
        if (userId == null) throw new IllegalArgumentException("\"userId\" is required but got null");
            

        if (targets == null) throw new IllegalArgumentException("\"targets\" is required but got null");
        return new UserAssociationsListRequestBuilder(userId, targets);
    }
    private okhttp3.Call userAssociationsPostCall(String userId, String xOrgId, GraphOperationUser graphOperationUser, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = graphOperationUser;

        // create path and map variables
        String localVarPath = "/users/{user_id}/associations"
            .replace("{" + "user_id" + "}", localVarApiClient.escapeString(userId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (xOrgId != null) {
            localVarHeaderParams.put("x-org-id", localVarApiClient.parameterToString(xOrgId));
        }

        final String[] localVarAccepts = {
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
    private okhttp3.Call userAssociationsPostValidateBeforeCall(String userId, String xOrgId, GraphOperationUser graphOperationUser, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling userAssociationsPost(Async)");
        }

        return userAssociationsPostCall(userId, xOrgId, graphOperationUser, _callback);

    }


    private ApiResponse<Void> userAssociationsPostWithHttpInfo(String userId, String xOrgId, GraphOperationUser graphOperationUser) throws ApiException {
        okhttp3.Call localVarCall = userAssociationsPostValidateBeforeCall(userId, xOrgId, graphOperationUser, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call userAssociationsPostAsync(String userId, String xOrgId, GraphOperationUser graphOperationUser, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = userAssociationsPostValidateBeforeCall(userId, xOrgId, graphOperationUser, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class UserAssociationsPostRequestBuilder {
        private final String userId;
        private String id;
        private String op;
        private Map attributes;
        private String type;
        private String xOrgId;

        private UserAssociationsPostRequestBuilder(String userId) {
            this.userId = userId;
        }

        /**
         * Set id
         * @param id The ObjectID of graph object being added or removed as an association. (optional)
         * @return UserAssociationsPostRequestBuilder
         */
        public UserAssociationsPostRequestBuilder id(String id) {
            this.id = id;
            return this;
        }
        
        /**
         * Set op
         * @param op How to modify the graph connection. (optional)
         * @return UserAssociationsPostRequestBuilder
         */
        public UserAssociationsPostRequestBuilder op(String op) {
            this.op = op;
            return this;
        }
        
        /**
         * Set attributes
         * @param attributes  (optional)
         * @return UserAssociationsPostRequestBuilder
         */
        public UserAssociationsPostRequestBuilder attributes(Map attributes) {
            this.attributes = attributes;
            return this;
        }
        
        /**
         * Set type
         * @param type Targets which a \\\&quot;user\\\&quot; can be associated to. (optional)
         * @return UserAssociationsPostRequestBuilder
         */
        public UserAssociationsPostRequestBuilder type(String type) {
            this.type = type;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UserAssociationsPostRequestBuilder
         */
        public UserAssociationsPostRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for userAssociationsPost
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            GraphOperationUser graphOperationUser = buildBodyParams();
            return userAssociationsPostCall(userId, xOrgId, graphOperationUser, _callback);
        }

        private GraphOperationUser buildBodyParams() {
            GraphOperationUser graphOperationUser = new GraphOperationUser();
            return graphOperationUser;
        }

        /**
         * Execute userAssociationsPost request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            GraphOperationUser graphOperationUser = buildBodyParams();
            userAssociationsPostWithHttpInfo(userId, xOrgId, graphOperationUser);
        }

        /**
         * Execute userAssociationsPost request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            GraphOperationUser graphOperationUser = buildBodyParams();
            return userAssociationsPostWithHttpInfo(userId, xOrgId, graphOperationUser);
        }

        /**
         * Execute userAssociationsPost request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Void> _callback) throws ApiException {
            GraphOperationUser graphOperationUser = buildBodyParams();
            return userAssociationsPostAsync(userId, xOrgId, graphOperationUser, _callback);
        }
    }

    /**
     * Manage the associations of a User
     * This endpoint allows you to manage the _direct_ associations of a User.  A direct association can be a non-homogeneous relationship between 2 different objects, for example Users and Systems.   #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/users/{UserID}/associations \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;attributes\&quot;: {       \&quot;sudo\&quot;: {       \&quot;enabled\&quot;: true,         \&quot;withoutPassword\&quot;: false       }     },     \&quot;op\&quot;: \&quot;add\&quot;,     \&quot;type\&quot;: \&quot;system_group\&quot;,     \&quot;id\&quot;: \&quot;{GroupID}\&quot;   }&#39; &#x60;&#x60;&#x60;
     * @param userId ObjectID of the User. (required)
     * @return UserAssociationsPostRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UserAssociationsPostRequestBuilder userAssociationsPost(String userId) throws IllegalArgumentException {
        if (userId == null) throw new IllegalArgumentException("\"userId\" is required but got null");
            

        return new UserAssociationsPostRequestBuilder(userId);
    }
    private okhttp3.Call userMemberOfCall(String userId, List<String> filter, Integer limit, Integer skip, List<String> sort, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/users/{user_id}/memberof"
            .replace("{" + "user_id" + "}", localVarApiClient.escapeString(userId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
        }

        if (skip != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("skip", skip));
        }

        if (sort != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "sort", sort));
        }

        if (xOrgId != null) {
            localVarHeaderParams.put("x-org-id", localVarApiClient.parameterToString(xOrgId));
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
    private okhttp3.Call userMemberOfValidateBeforeCall(String userId, List<String> filter, Integer limit, Integer skip, List<String> sort, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling userMemberOf(Async)");
        }

        return userMemberOfCall(userId, filter, limit, skip, sort, xOrgId, _callback);

    }


    private ApiResponse<List<GraphObjectWithPaths>> userMemberOfWithHttpInfo(String userId, List<String> filter, Integer limit, Integer skip, List<String> sort, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = userMemberOfValidateBeforeCall(userId, filter, limit, skip, sort, xOrgId, null);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call userMemberOfAsync(String userId, List<String> filter, Integer limit, Integer skip, List<String> sort, String xOrgId, final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {

        okhttp3.Call localVarCall = userMemberOfValidateBeforeCall(userId, filter, limit, skip, sort, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UserMemberOfRequestBuilder {
        private final String userId;
        private List<String> filter;
        private Integer limit;
        private Integer skip;
        private List<String> sort;
        private String xOrgId;

        private UserMemberOfRequestBuilder(String userId) {
            this.userId = userId;
        }

        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return UserMemberOfRequestBuilder
         */
        public UserMemberOfRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return UserMemberOfRequestBuilder
         */
        public UserMemberOfRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return UserMemberOfRequestBuilder
         */
        public UserMemberOfRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  (optional)
         * @return UserMemberOfRequestBuilder
         */
        public UserMemberOfRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UserMemberOfRequestBuilder
         */
        public UserMemberOfRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for userMemberOf
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return userMemberOfCall(userId, filter, limit, skip, sort, xOrgId, _callback);
        }


        /**
         * Execute userMemberOf request
         * @return List&lt;GraphObjectWithPaths&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphObjectWithPaths> execute() throws ApiException {
            ApiResponse<List<GraphObjectWithPaths>> localVarResp = userMemberOfWithHttpInfo(userId, filter, limit, skip, sort, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute userMemberOf request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphObjectWithPaths&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphObjectWithPaths>> executeWithHttpInfo() throws ApiException {
            return userMemberOfWithHttpInfo(userId, filter, limit, skip, sort, xOrgId);
        }

        /**
         * Execute userMemberOf request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {
            return userMemberOfAsync(userId, filter, limit, skip, sort, xOrgId, _callback);
        }
    }

    /**
     * List the parent Groups of a User
     * This endpoint returns all the User Groups a User is a member of.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/users/{UserID}/memberof \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param userId ObjectID of the User. (required)
     * @return UserMemberOfRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UserMemberOfRequestBuilder userMemberOf(String userId) throws IllegalArgumentException {
        if (userId == null) throw new IllegalArgumentException("\"userId\" is required but got null");
            

        return new UserMemberOfRequestBuilder(userId);
    }
    private okhttp3.Call userTraverseActiveDirectoryCall(String userId, List<String> filter, Integer limit, String xOrgId, Integer skip, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/users/{user_id}/activedirectories"
            .replace("{" + "user_id" + "}", localVarApiClient.escapeString(userId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
        }

        if (skip != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("skip", skip));
        }

        if (xOrgId != null) {
            localVarHeaderParams.put("x-org-id", localVarApiClient.parameterToString(xOrgId));
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
    private okhttp3.Call userTraverseActiveDirectoryValidateBeforeCall(String userId, List<String> filter, Integer limit, String xOrgId, Integer skip, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling userTraverseActiveDirectory(Async)");
        }

        return userTraverseActiveDirectoryCall(userId, filter, limit, xOrgId, skip, _callback);

    }


    private ApiResponse<List<GraphObjectWithPaths>> userTraverseActiveDirectoryWithHttpInfo(String userId, List<String> filter, Integer limit, String xOrgId, Integer skip) throws ApiException {
        okhttp3.Call localVarCall = userTraverseActiveDirectoryValidateBeforeCall(userId, filter, limit, xOrgId, skip, null);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call userTraverseActiveDirectoryAsync(String userId, List<String> filter, Integer limit, String xOrgId, Integer skip, final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {

        okhttp3.Call localVarCall = userTraverseActiveDirectoryValidateBeforeCall(userId, filter, limit, xOrgId, skip, _callback);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UserTraverseActiveDirectoryRequestBuilder {
        private final String userId;
        private List<String> filter;
        private Integer limit;
        private String xOrgId;
        private Integer skip;

        private UserTraverseActiveDirectoryRequestBuilder(String userId) {
            this.userId = userId;
        }

        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return UserTraverseActiveDirectoryRequestBuilder
         */
        public UserTraverseActiveDirectoryRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return UserTraverseActiveDirectoryRequestBuilder
         */
        public UserTraverseActiveDirectoryRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UserTraverseActiveDirectoryRequestBuilder
         */
        public UserTraverseActiveDirectoryRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return UserTraverseActiveDirectoryRequestBuilder
         */
        public UserTraverseActiveDirectoryRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Build call for userTraverseActiveDirectory
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return userTraverseActiveDirectoryCall(userId, filter, limit, xOrgId, skip, _callback);
        }


        /**
         * Execute userTraverseActiveDirectory request
         * @return List&lt;GraphObjectWithPaths&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphObjectWithPaths> execute() throws ApiException {
            ApiResponse<List<GraphObjectWithPaths>> localVarResp = userTraverseActiveDirectoryWithHttpInfo(userId, filter, limit, xOrgId, skip);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute userTraverseActiveDirectory request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphObjectWithPaths&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphObjectWithPaths>> executeWithHttpInfo() throws ApiException {
            return userTraverseActiveDirectoryWithHttpInfo(userId, filter, limit, xOrgId, skip);
        }

        /**
         * Execute userTraverseActiveDirectory request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {
            return userTraverseActiveDirectoryAsync(userId, filter, limit, xOrgId, skip, _callback);
        }
    }

    /**
     * List the Active Directory instances bound to a User
     * This endpoint will return all Active Directory Instances bound to a User, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User to the corresponding Active Directory instance; this array represents all grouping and/or associations that would have to be removed to deprovision the Active Directory instance from this User.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/users/{UserID}/activedirectories \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param userId ObjectID of the User. (required)
     * @return UserTraverseActiveDirectoryRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public UserTraverseActiveDirectoryRequestBuilder userTraverseActiveDirectory(String userId) throws IllegalArgumentException {
        if (userId == null) throw new IllegalArgumentException("\"userId\" is required but got null");
            

        return new UserTraverseActiveDirectoryRequestBuilder(userId);
    }
    private okhttp3.Call userTraverseApplicationCall(String userId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/users/{user_id}/applications"
            .replace("{" + "user_id" + "}", localVarApiClient.escapeString(userId.toString()));

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
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (xOrgId != null) {
            localVarHeaderParams.put("x-org-id", localVarApiClient.parameterToString(xOrgId));
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
    private okhttp3.Call userTraverseApplicationValidateBeforeCall(String userId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling userTraverseApplication(Async)");
        }

        return userTraverseApplicationCall(userId, limit, xOrgId, skip, filter, _callback);

    }


    private ApiResponse<List<GraphObjectWithPaths>> userTraverseApplicationWithHttpInfo(String userId, Integer limit, String xOrgId, Integer skip, List<String> filter) throws ApiException {
        okhttp3.Call localVarCall = userTraverseApplicationValidateBeforeCall(userId, limit, xOrgId, skip, filter, null);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call userTraverseApplicationAsync(String userId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {

        okhttp3.Call localVarCall = userTraverseApplicationValidateBeforeCall(userId, limit, xOrgId, skip, filter, _callback);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UserTraverseApplicationRequestBuilder {
        private final String userId;
        private Integer limit;
        private String xOrgId;
        private Integer skip;
        private List<String> filter;

        private UserTraverseApplicationRequestBuilder(String userId) {
            this.userId = userId;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return UserTraverseApplicationRequestBuilder
         */
        public UserTraverseApplicationRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UserTraverseApplicationRequestBuilder
         */
        public UserTraverseApplicationRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return UserTraverseApplicationRequestBuilder
         */
        public UserTraverseApplicationRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return UserTraverseApplicationRequestBuilder
         */
        public UserTraverseApplicationRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Build call for userTraverseApplication
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return userTraverseApplicationCall(userId, limit, xOrgId, skip, filter, _callback);
        }


        /**
         * Execute userTraverseApplication request
         * @return List&lt;GraphObjectWithPaths&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphObjectWithPaths> execute() throws ApiException {
            ApiResponse<List<GraphObjectWithPaths>> localVarResp = userTraverseApplicationWithHttpInfo(userId, limit, xOrgId, skip, filter);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute userTraverseApplication request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphObjectWithPaths&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphObjectWithPaths>> executeWithHttpInfo() throws ApiException {
            return userTraverseApplicationWithHttpInfo(userId, limit, xOrgId, skip, filter);
        }

        /**
         * Execute userTraverseApplication request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {
            return userTraverseApplicationAsync(userId, limit, xOrgId, skip, filter, _callback);
        }
    }

    /**
     * List the Applications bound to a User
     * This endpoint will return all Applications bound to a User, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User to the corresponding Application; this array represents all grouping and/or associations that would have to be removed to deprovision the Application from this User.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/users/{UserID}/applications \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param userId ObjectID of the User. (required)
     * @return UserTraverseApplicationRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UserTraverseApplicationRequestBuilder userTraverseApplication(String userId) throws IllegalArgumentException {
        if (userId == null) throw new IllegalArgumentException("\"userId\" is required but got null");
            

        return new UserTraverseApplicationRequestBuilder(userId);
    }
    private okhttp3.Call userTraverseDirectoryCall(String userId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/users/{user_id}/directories"
            .replace("{" + "user_id" + "}", localVarApiClient.escapeString(userId.toString()));

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
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (xOrgId != null) {
            localVarHeaderParams.put("x-org-id", localVarApiClient.parameterToString(xOrgId));
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
    private okhttp3.Call userTraverseDirectoryValidateBeforeCall(String userId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling userTraverseDirectory(Async)");
        }

        return userTraverseDirectoryCall(userId, limit, xOrgId, skip, filter, _callback);

    }


    private ApiResponse<List<GraphObjectWithPaths>> userTraverseDirectoryWithHttpInfo(String userId, Integer limit, String xOrgId, Integer skip, List<String> filter) throws ApiException {
        okhttp3.Call localVarCall = userTraverseDirectoryValidateBeforeCall(userId, limit, xOrgId, skip, filter, null);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call userTraverseDirectoryAsync(String userId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {

        okhttp3.Call localVarCall = userTraverseDirectoryValidateBeforeCall(userId, limit, xOrgId, skip, filter, _callback);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UserTraverseDirectoryRequestBuilder {
        private final String userId;
        private Integer limit;
        private String xOrgId;
        private Integer skip;
        private List<String> filter;

        private UserTraverseDirectoryRequestBuilder(String userId) {
            this.userId = userId;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return UserTraverseDirectoryRequestBuilder
         */
        public UserTraverseDirectoryRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UserTraverseDirectoryRequestBuilder
         */
        public UserTraverseDirectoryRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return UserTraverseDirectoryRequestBuilder
         */
        public UserTraverseDirectoryRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return UserTraverseDirectoryRequestBuilder
         */
        public UserTraverseDirectoryRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Build call for userTraverseDirectory
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return userTraverseDirectoryCall(userId, limit, xOrgId, skip, filter, _callback);
        }


        /**
         * Execute userTraverseDirectory request
         * @return List&lt;GraphObjectWithPaths&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphObjectWithPaths> execute() throws ApiException {
            ApiResponse<List<GraphObjectWithPaths>> localVarResp = userTraverseDirectoryWithHttpInfo(userId, limit, xOrgId, skip, filter);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute userTraverseDirectory request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphObjectWithPaths&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphObjectWithPaths>> executeWithHttpInfo() throws ApiException {
            return userTraverseDirectoryWithHttpInfo(userId, limit, xOrgId, skip, filter);
        }

        /**
         * Execute userTraverseDirectory request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {
            return userTraverseDirectoryAsync(userId, limit, xOrgId, skip, filter, _callback);
        }
    }

    /**
     * List the Directories bound to a User
     * This endpoint will return all Directories bound to a User, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User to the corresponding Directory; this array represents all grouping and/or associations that would have to be removed to deprovision the Directory from this User.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/users/{UserID}/directories \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param userId ObjectID of the User. (required)
     * @return UserTraverseDirectoryRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UserTraverseDirectoryRequestBuilder userTraverseDirectory(String userId) throws IllegalArgumentException {
        if (userId == null) throw new IllegalArgumentException("\"userId\" is required but got null");
            

        return new UserTraverseDirectoryRequestBuilder(userId);
    }
    private okhttp3.Call userTraverseGSuiteCall(String userId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/users/{user_id}/gsuites"
            .replace("{" + "user_id" + "}", localVarApiClient.escapeString(userId.toString()));

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
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (xOrgId != null) {
            localVarHeaderParams.put("x-org-id", localVarApiClient.parameterToString(xOrgId));
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
    private okhttp3.Call userTraverseGSuiteValidateBeforeCall(String userId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling userTraverseGSuite(Async)");
        }

        return userTraverseGSuiteCall(userId, limit, xOrgId, skip, filter, _callback);

    }


    private ApiResponse<List<GraphObjectWithPaths>> userTraverseGSuiteWithHttpInfo(String userId, Integer limit, String xOrgId, Integer skip, List<String> filter) throws ApiException {
        okhttp3.Call localVarCall = userTraverseGSuiteValidateBeforeCall(userId, limit, xOrgId, skip, filter, null);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call userTraverseGSuiteAsync(String userId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {

        okhttp3.Call localVarCall = userTraverseGSuiteValidateBeforeCall(userId, limit, xOrgId, skip, filter, _callback);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UserTraverseGSuiteRequestBuilder {
        private final String userId;
        private Integer limit;
        private String xOrgId;
        private Integer skip;
        private List<String> filter;

        private UserTraverseGSuiteRequestBuilder(String userId) {
            this.userId = userId;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return UserTraverseGSuiteRequestBuilder
         */
        public UserTraverseGSuiteRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UserTraverseGSuiteRequestBuilder
         */
        public UserTraverseGSuiteRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return UserTraverseGSuiteRequestBuilder
         */
        public UserTraverseGSuiteRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return UserTraverseGSuiteRequestBuilder
         */
        public UserTraverseGSuiteRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Build call for userTraverseGSuite
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return userTraverseGSuiteCall(userId, limit, xOrgId, skip, filter, _callback);
        }


        /**
         * Execute userTraverseGSuite request
         * @return List&lt;GraphObjectWithPaths&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphObjectWithPaths> execute() throws ApiException {
            ApiResponse<List<GraphObjectWithPaths>> localVarResp = userTraverseGSuiteWithHttpInfo(userId, limit, xOrgId, skip, filter);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute userTraverseGSuite request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphObjectWithPaths&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphObjectWithPaths>> executeWithHttpInfo() throws ApiException {
            return userTraverseGSuiteWithHttpInfo(userId, limit, xOrgId, skip, filter);
        }

        /**
         * Execute userTraverseGSuite request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {
            return userTraverseGSuiteAsync(userId, limit, xOrgId, skip, filter, _callback);
        }
    }

    /**
     * List the G Suite instances bound to a User
     * This endpoint will return all G-Suite Instances bound to a User, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User to the corresponding G Suite instance; this array represents all grouping and/or associations that would have to be removed to deprovision the G Suite instance from this User.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/users/{UserID}/gsuites \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param userId ObjectID of the User. (required)
     * @return UserTraverseGSuiteRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UserTraverseGSuiteRequestBuilder userTraverseGSuite(String userId) throws IllegalArgumentException {
        if (userId == null) throw new IllegalArgumentException("\"userId\" is required but got null");
            

        return new UserTraverseGSuiteRequestBuilder(userId);
    }
    private okhttp3.Call userTraverseLdapServerCall(String userId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/users/{user_id}/ldapservers"
            .replace("{" + "user_id" + "}", localVarApiClient.escapeString(userId.toString()));

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
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (xOrgId != null) {
            localVarHeaderParams.put("x-org-id", localVarApiClient.parameterToString(xOrgId));
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
    private okhttp3.Call userTraverseLdapServerValidateBeforeCall(String userId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling userTraverseLdapServer(Async)");
        }

        return userTraverseLdapServerCall(userId, limit, xOrgId, skip, filter, _callback);

    }


    private ApiResponse<List<GraphObjectWithPaths>> userTraverseLdapServerWithHttpInfo(String userId, Integer limit, String xOrgId, Integer skip, List<String> filter) throws ApiException {
        okhttp3.Call localVarCall = userTraverseLdapServerValidateBeforeCall(userId, limit, xOrgId, skip, filter, null);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call userTraverseLdapServerAsync(String userId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {

        okhttp3.Call localVarCall = userTraverseLdapServerValidateBeforeCall(userId, limit, xOrgId, skip, filter, _callback);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UserTraverseLdapServerRequestBuilder {
        private final String userId;
        private Integer limit;
        private String xOrgId;
        private Integer skip;
        private List<String> filter;

        private UserTraverseLdapServerRequestBuilder(String userId) {
            this.userId = userId;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return UserTraverseLdapServerRequestBuilder
         */
        public UserTraverseLdapServerRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UserTraverseLdapServerRequestBuilder
         */
        public UserTraverseLdapServerRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return UserTraverseLdapServerRequestBuilder
         */
        public UserTraverseLdapServerRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return UserTraverseLdapServerRequestBuilder
         */
        public UserTraverseLdapServerRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Build call for userTraverseLdapServer
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return userTraverseLdapServerCall(userId, limit, xOrgId, skip, filter, _callback);
        }


        /**
         * Execute userTraverseLdapServer request
         * @return List&lt;GraphObjectWithPaths&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphObjectWithPaths> execute() throws ApiException {
            ApiResponse<List<GraphObjectWithPaths>> localVarResp = userTraverseLdapServerWithHttpInfo(userId, limit, xOrgId, skip, filter);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute userTraverseLdapServer request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphObjectWithPaths&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphObjectWithPaths>> executeWithHttpInfo() throws ApiException {
            return userTraverseLdapServerWithHttpInfo(userId, limit, xOrgId, skip, filter);
        }

        /**
         * Execute userTraverseLdapServer request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {
            return userTraverseLdapServerAsync(userId, limit, xOrgId, skip, filter, _callback);
        }
    }

    /**
     * List the LDAP servers bound to a User
     * This endpoint will return all LDAP Servers bound to a User, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User to the corresponding LDAP Server; this array represents all grouping and/or associations that would have to be removed to deprovision the LDAP Server from this User.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/users/{UserID}/ldapservers \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param userId ObjectID of the User. (required)
     * @return UserTraverseLdapServerRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UserTraverseLdapServerRequestBuilder userTraverseLdapServer(String userId) throws IllegalArgumentException {
        if (userId == null) throw new IllegalArgumentException("\"userId\" is required but got null");
            

        return new UserTraverseLdapServerRequestBuilder(userId);
    }
    private okhttp3.Call userTraverseOffice365Call(String userId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/users/{user_id}/office365s"
            .replace("{" + "user_id" + "}", localVarApiClient.escapeString(userId.toString()));

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
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (xOrgId != null) {
            localVarHeaderParams.put("x-org-id", localVarApiClient.parameterToString(xOrgId));
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
    private okhttp3.Call userTraverseOffice365ValidateBeforeCall(String userId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling userTraverseOffice365(Async)");
        }

        return userTraverseOffice365Call(userId, limit, xOrgId, skip, filter, _callback);

    }


    private ApiResponse<List<GraphObjectWithPaths>> userTraverseOffice365WithHttpInfo(String userId, Integer limit, String xOrgId, Integer skip, List<String> filter) throws ApiException {
        okhttp3.Call localVarCall = userTraverseOffice365ValidateBeforeCall(userId, limit, xOrgId, skip, filter, null);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call userTraverseOffice365Async(String userId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {

        okhttp3.Call localVarCall = userTraverseOffice365ValidateBeforeCall(userId, limit, xOrgId, skip, filter, _callback);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UserTraverseOffice365RequestBuilder {
        private final String userId;
        private Integer limit;
        private String xOrgId;
        private Integer skip;
        private List<String> filter;

        private UserTraverseOffice365RequestBuilder(String userId) {
            this.userId = userId;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return UserTraverseOffice365RequestBuilder
         */
        public UserTraverseOffice365RequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UserTraverseOffice365RequestBuilder
         */
        public UserTraverseOffice365RequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return UserTraverseOffice365RequestBuilder
         */
        public UserTraverseOffice365RequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return UserTraverseOffice365RequestBuilder
         */
        public UserTraverseOffice365RequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Build call for userTraverseOffice365
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return userTraverseOffice365Call(userId, limit, xOrgId, skip, filter, _callback);
        }


        /**
         * Execute userTraverseOffice365 request
         * @return List&lt;GraphObjectWithPaths&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphObjectWithPaths> execute() throws ApiException {
            ApiResponse<List<GraphObjectWithPaths>> localVarResp = userTraverseOffice365WithHttpInfo(userId, limit, xOrgId, skip, filter);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute userTraverseOffice365 request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphObjectWithPaths&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphObjectWithPaths>> executeWithHttpInfo() throws ApiException {
            return userTraverseOffice365WithHttpInfo(userId, limit, xOrgId, skip, filter);
        }

        /**
         * Execute userTraverseOffice365 request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {
            return userTraverseOffice365Async(userId, limit, xOrgId, skip, filter, _callback);
        }
    }

    /**
     * List the Office 365 instances bound to a User
     * This endpoint will return all Office 365 Instances bound to a User, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User to the corresponding Office 365 instance; this array represents all grouping and/or associations that would have to be removed to deprovision the Office 365 instance from this User.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/users/{UserID}/office365s \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param userId ObjectID of the User. (required)
     * @return UserTraverseOffice365RequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UserTraverseOffice365RequestBuilder userTraverseOffice365(String userId) throws IllegalArgumentException {
        if (userId == null) throw new IllegalArgumentException("\"userId\" is required but got null");
            

        return new UserTraverseOffice365RequestBuilder(userId);
    }
    private okhttp3.Call userTraverseRadiusServerCall(String userId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/users/{user_id}/radiusservers"
            .replace("{" + "user_id" + "}", localVarApiClient.escapeString(userId.toString()));

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
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (xOrgId != null) {
            localVarHeaderParams.put("x-org-id", localVarApiClient.parameterToString(xOrgId));
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
    private okhttp3.Call userTraverseRadiusServerValidateBeforeCall(String userId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling userTraverseRadiusServer(Async)");
        }

        return userTraverseRadiusServerCall(userId, limit, xOrgId, skip, filter, _callback);

    }


    private ApiResponse<List<GraphObjectWithPaths>> userTraverseRadiusServerWithHttpInfo(String userId, Integer limit, String xOrgId, Integer skip, List<String> filter) throws ApiException {
        okhttp3.Call localVarCall = userTraverseRadiusServerValidateBeforeCall(userId, limit, xOrgId, skip, filter, null);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call userTraverseRadiusServerAsync(String userId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {

        okhttp3.Call localVarCall = userTraverseRadiusServerValidateBeforeCall(userId, limit, xOrgId, skip, filter, _callback);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UserTraverseRadiusServerRequestBuilder {
        private final String userId;
        private Integer limit;
        private String xOrgId;
        private Integer skip;
        private List<String> filter;

        private UserTraverseRadiusServerRequestBuilder(String userId) {
            this.userId = userId;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return UserTraverseRadiusServerRequestBuilder
         */
        public UserTraverseRadiusServerRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UserTraverseRadiusServerRequestBuilder
         */
        public UserTraverseRadiusServerRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return UserTraverseRadiusServerRequestBuilder
         */
        public UserTraverseRadiusServerRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return UserTraverseRadiusServerRequestBuilder
         */
        public UserTraverseRadiusServerRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Build call for userTraverseRadiusServer
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return userTraverseRadiusServerCall(userId, limit, xOrgId, skip, filter, _callback);
        }


        /**
         * Execute userTraverseRadiusServer request
         * @return List&lt;GraphObjectWithPaths&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphObjectWithPaths> execute() throws ApiException {
            ApiResponse<List<GraphObjectWithPaths>> localVarResp = userTraverseRadiusServerWithHttpInfo(userId, limit, xOrgId, skip, filter);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute userTraverseRadiusServer request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphObjectWithPaths&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphObjectWithPaths>> executeWithHttpInfo() throws ApiException {
            return userTraverseRadiusServerWithHttpInfo(userId, limit, xOrgId, skip, filter);
        }

        /**
         * Execute userTraverseRadiusServer request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {
            return userTraverseRadiusServerAsync(userId, limit, xOrgId, skip, filter, _callback);
        }
    }

    /**
     * List the RADIUS Servers bound to a User
     * This endpoint will return all RADIUS Servers bound to a User, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User to the corresponding RADIUS Server; this array represents all grouping and/or associations that would have to be removed to deprovision the RADIUS Server from this User.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/users/{UserID}/radiusservers \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param userId ObjectID of the User. (required)
     * @return UserTraverseRadiusServerRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UserTraverseRadiusServerRequestBuilder userTraverseRadiusServer(String userId) throws IllegalArgumentException {
        if (userId == null) throw new IllegalArgumentException("\"userId\" is required but got null");
            

        return new UserTraverseRadiusServerRequestBuilder(userId);
    }
    private okhttp3.Call userTraverseSystemCall(String userId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/users/{user_id}/systems"
            .replace("{" + "user_id" + "}", localVarApiClient.escapeString(userId.toString()));

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
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (xOrgId != null) {
            localVarHeaderParams.put("x-org-id", localVarApiClient.parameterToString(xOrgId));
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
    private okhttp3.Call userTraverseSystemValidateBeforeCall(String userId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling userTraverseSystem(Async)");
        }

        return userTraverseSystemCall(userId, limit, xOrgId, skip, filter, _callback);

    }


    private ApiResponse<List<GraphObjectWithPaths>> userTraverseSystemWithHttpInfo(String userId, Integer limit, String xOrgId, Integer skip, List<String> filter) throws ApiException {
        okhttp3.Call localVarCall = userTraverseSystemValidateBeforeCall(userId, limit, xOrgId, skip, filter, null);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call userTraverseSystemAsync(String userId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {

        okhttp3.Call localVarCall = userTraverseSystemValidateBeforeCall(userId, limit, xOrgId, skip, filter, _callback);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UserTraverseSystemRequestBuilder {
        private final String userId;
        private Integer limit;
        private String xOrgId;
        private Integer skip;
        private List<String> filter;

        private UserTraverseSystemRequestBuilder(String userId) {
            this.userId = userId;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return UserTraverseSystemRequestBuilder
         */
        public UserTraverseSystemRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UserTraverseSystemRequestBuilder
         */
        public UserTraverseSystemRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return UserTraverseSystemRequestBuilder
         */
        public UserTraverseSystemRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return UserTraverseSystemRequestBuilder
         */
        public UserTraverseSystemRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Build call for userTraverseSystem
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return userTraverseSystemCall(userId, limit, xOrgId, skip, filter, _callback);
        }


        /**
         * Execute userTraverseSystem request
         * @return List&lt;GraphObjectWithPaths&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphObjectWithPaths> execute() throws ApiException {
            ApiResponse<List<GraphObjectWithPaths>> localVarResp = userTraverseSystemWithHttpInfo(userId, limit, xOrgId, skip, filter);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute userTraverseSystem request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphObjectWithPaths&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphObjectWithPaths>> executeWithHttpInfo() throws ApiException {
            return userTraverseSystemWithHttpInfo(userId, limit, xOrgId, skip, filter);
        }

        /**
         * Execute userTraverseSystem request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {
            return userTraverseSystemAsync(userId, limit, xOrgId, skip, filter, _callback);
        }
    }

    /**
     * List the Systems bound to a User
     * This endpoint will return all Systems bound to a User, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User to the corresponding System; this array represents all grouping and/or associations that would have to be removed to deprovision the System from this User.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/users/{UserID}/systems\\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param userId ObjectID of the User. (required)
     * @return UserTraverseSystemRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UserTraverseSystemRequestBuilder userTraverseSystem(String userId) throws IllegalArgumentException {
        if (userId == null) throw new IllegalArgumentException("\"userId\" is required but got null");
            

        return new UserTraverseSystemRequestBuilder(userId);
    }
    private okhttp3.Call userTraverseSystemGroupCall(String userId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/users/{user_id}/systemgroups"
            .replace("{" + "user_id" + "}", localVarApiClient.escapeString(userId.toString()));

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
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (xOrgId != null) {
            localVarHeaderParams.put("x-org-id", localVarApiClient.parameterToString(xOrgId));
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
    private okhttp3.Call userTraverseSystemGroupValidateBeforeCall(String userId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new ApiException("Missing the required parameter 'userId' when calling userTraverseSystemGroup(Async)");
        }

        return userTraverseSystemGroupCall(userId, limit, xOrgId, skip, filter, _callback);

    }


    private ApiResponse<List<GraphObjectWithPaths>> userTraverseSystemGroupWithHttpInfo(String userId, Integer limit, String xOrgId, Integer skip, List<String> filter) throws ApiException {
        okhttp3.Call localVarCall = userTraverseSystemGroupValidateBeforeCall(userId, limit, xOrgId, skip, filter, null);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call userTraverseSystemGroupAsync(String userId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {

        okhttp3.Call localVarCall = userTraverseSystemGroupValidateBeforeCall(userId, limit, xOrgId, skip, filter, _callback);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UserTraverseSystemGroupRequestBuilder {
        private final String userId;
        private Integer limit;
        private String xOrgId;
        private Integer skip;
        private List<String> filter;

        private UserTraverseSystemGroupRequestBuilder(String userId) {
            this.userId = userId;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return UserTraverseSystemGroupRequestBuilder
         */
        public UserTraverseSystemGroupRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UserTraverseSystemGroupRequestBuilder
         */
        public UserTraverseSystemGroupRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return UserTraverseSystemGroupRequestBuilder
         */
        public UserTraverseSystemGroupRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return UserTraverseSystemGroupRequestBuilder
         */
        public UserTraverseSystemGroupRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Build call for userTraverseSystemGroup
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return userTraverseSystemGroupCall(userId, limit, xOrgId, skip, filter, _callback);
        }


        /**
         * Execute userTraverseSystemGroup request
         * @return List&lt;GraphObjectWithPaths&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphObjectWithPaths> execute() throws ApiException {
            ApiResponse<List<GraphObjectWithPaths>> localVarResp = userTraverseSystemGroupWithHttpInfo(userId, limit, xOrgId, skip, filter);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute userTraverseSystemGroup request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphObjectWithPaths&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphObjectWithPaths>> executeWithHttpInfo() throws ApiException {
            return userTraverseSystemGroupWithHttpInfo(userId, limit, xOrgId, skip, filter);
        }

        /**
         * Execute userTraverseSystemGroup request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {
            return userTraverseSystemGroupAsync(userId, limit, xOrgId, skip, filter, _callback);
        }
    }

    /**
     * List the System Groups bound to a User
     * This endpoint will return all System Groups bound to a User, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User to the corresponding System Group; this array represents all grouping and/or associations that would have to be removed to deprovision the System Group from this User.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/users/{UserID}/systemgroups\\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param userId ObjectID of the User. (required)
     * @return UserTraverseSystemGroupRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UserTraverseSystemGroupRequestBuilder userTraverseSystemGroup(String userId) throws IllegalArgumentException {
        if (userId == null) throw new IllegalArgumentException("\"userId\" is required but got null");
            

        return new UserTraverseSystemGroupRequestBuilder(userId);
    }
}
