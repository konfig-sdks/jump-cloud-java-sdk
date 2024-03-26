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


import com.konfigthis.client.model.Error;
import com.konfigthis.client.model.GraphConnection;
import com.konfigthis.client.model.GraphObject;
import com.konfigthis.client.model.GraphObjectWithPaths;
import com.konfigthis.client.model.GraphOperationUserGroup;
import com.konfigthis.client.model.GraphOperationUserGroupMember;
import com.konfigthis.client.model.GroupAttributesUserGroup;
import com.konfigthis.client.model.GroupMembershipMethodType;
import com.konfigthis.client.model.MemberQuery;
import com.konfigthis.client.model.MemberSuggestion;
import com.konfigthis.client.model.UserGroup;
import com.konfigthis.client.model.UserGroupPost;
import com.konfigthis.client.model.UserGroupPut;
import com.konfigthis.client.model.UserGroupsApplySuggestionsRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;

public class UserGroupsApiGenerated {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public UserGroupsApiGenerated() throws IllegalArgumentException {
        this(Configuration.getDefaultApiClient());
    }

    public UserGroupsApiGenerated(ApiClient apiClient) throws IllegalArgumentException {
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

    private okhttp3.Call applySuggestionsCall(String groupId, UserGroupsApplySuggestionsRequest userGroupsApplySuggestionsRequest, String xOrgId, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = userGroupsApplySuggestionsRequest;

        // create path and map variables
        String localVarPath = "/usergroups/{group_id}/suggestions"
            .replace("{" + "group_id" + "}", localVarApiClient.escapeString(groupId.toString()));

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
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call applySuggestionsValidateBeforeCall(String groupId, UserGroupsApplySuggestionsRequest userGroupsApplySuggestionsRequest, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'groupId' is set
        if (groupId == null) {
            throw new ApiException("Missing the required parameter 'groupId' when calling applySuggestions(Async)");
        }

        // verify the required parameter 'userGroupsApplySuggestionsRequest' is set
        if (userGroupsApplySuggestionsRequest == null) {
            throw new ApiException("Missing the required parameter 'userGroupsApplySuggestionsRequest' when calling applySuggestions(Async)");
        }

        return applySuggestionsCall(groupId, userGroupsApplySuggestionsRequest, xOrgId, _callback);

    }


    private ApiResponse<Object> applySuggestionsWithHttpInfo(String groupId, UserGroupsApplySuggestionsRequest userGroupsApplySuggestionsRequest, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = applySuggestionsValidateBeforeCall(groupId, userGroupsApplySuggestionsRequest, xOrgId, null);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call applySuggestionsAsync(String groupId, UserGroupsApplySuggestionsRequest userGroupsApplySuggestionsRequest, String xOrgId, final ApiCallback<Object> _callback) throws ApiException {

        okhttp3.Call localVarCall = applySuggestionsValidateBeforeCall(groupId, userGroupsApplySuggestionsRequest, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ApplySuggestionsRequestBuilder {
        private final String groupId;
        private List<String> userIds;
        private String xOrgId;

        private ApplySuggestionsRequestBuilder(String groupId) {
            this.groupId = groupId;
        }

        /**
         * Set userIds
         * @param userIds  (optional)
         * @return ApplySuggestionsRequestBuilder
         */
        public ApplySuggestionsRequestBuilder userIds(List<String> userIds) {
            this.userIds = userIds;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ApplySuggestionsRequestBuilder
         */
        public ApplySuggestionsRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for applySuggestions
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
            UserGroupsApplySuggestionsRequest userGroupsApplySuggestionsRequest = buildBodyParams();
            return applySuggestionsCall(groupId, userGroupsApplySuggestionsRequest, xOrgId, _callback);
        }

        private UserGroupsApplySuggestionsRequest buildBodyParams() {
            UserGroupsApplySuggestionsRequest userGroupsApplySuggestionsRequest = new UserGroupsApplySuggestionsRequest();
            userGroupsApplySuggestionsRequest.userIds(this.userIds);
            return userGroupsApplySuggestionsRequest;
        }

        /**
         * Execute applySuggestions request
         * @return Object
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public Object execute() throws ApiException {
            UserGroupsApplySuggestionsRequest userGroupsApplySuggestionsRequest = buildBodyParams();
            ApiResponse<Object> localVarResp = applySuggestionsWithHttpInfo(groupId, userGroupsApplySuggestionsRequest, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute applySuggestions request with HTTP info returned
         * @return ApiResponse&lt;Object&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Object> executeWithHttpInfo() throws ApiException {
            UserGroupsApplySuggestionsRequest userGroupsApplySuggestionsRequest = buildBodyParams();
            return applySuggestionsWithHttpInfo(groupId, userGroupsApplySuggestionsRequest, xOrgId);
        }

        /**
         * Execute applySuggestions request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Object> _callback) throws ApiException {
            UserGroupsApplySuggestionsRequest userGroupsApplySuggestionsRequest = buildBodyParams();
            return applySuggestionsAsync(groupId, userGroupsApplySuggestionsRequest, xOrgId, _callback);
        }
    }

    /**
     * Apply Suggestions for a User Group
     * This endpoint applies the suggestions for the specified user group. #### Sample Request &#x60;&#x60;&#x60; curl -X PUT https://console.jumpcloud.com/api/v2/usergroups/{GroupID}/suggestions \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{          \&quot;user_ids\&quot;: [\&quot;212345678901234567890123\&quot;,                       \&quot;123456789012345678901234\&quot;]      }&#39; &#x60;&#x60;&#x60;
     * @param groupId ID of the group (required)
     * @param userGroupsApplySuggestionsRequest  (required)
     * @return ApplySuggestionsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public ApplySuggestionsRequestBuilder applySuggestions(String groupId) throws IllegalArgumentException {
        if (groupId == null) throw new IllegalArgumentException("\"groupId\" is required but got null");
            

        return new ApplySuggestionsRequestBuilder(groupId);
    }
    private okhttp3.Call createNewGroupCall(String xOrgId, UserGroupPost userGroupPost, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = userGroupPost;

        // create path and map variables
        String localVarPath = "/usergroups";

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
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call createNewGroupValidateBeforeCall(String xOrgId, UserGroupPost userGroupPost, final ApiCallback _callback) throws ApiException {
        return createNewGroupCall(xOrgId, userGroupPost, _callback);

    }


    private ApiResponse<UserGroup> createNewGroupWithHttpInfo(String xOrgId, UserGroupPost userGroupPost) throws ApiException {
        okhttp3.Call localVarCall = createNewGroupValidateBeforeCall(xOrgId, userGroupPost, null);
        Type localVarReturnType = new TypeToken<UserGroup>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call createNewGroupAsync(String xOrgId, UserGroupPost userGroupPost, final ApiCallback<UserGroup> _callback) throws ApiException {

        okhttp3.Call localVarCall = createNewGroupValidateBeforeCall(xOrgId, userGroupPost, _callback);
        Type localVarReturnType = new TypeToken<UserGroup>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class CreateNewGroupRequestBuilder {
        private final String name;
        private String description;
        private GroupAttributesUserGroup attributes;
        private String email;
        private MemberQuery memberQuery;
        private List<GraphObject> memberQueryExemptions;
        private Boolean memberSuggestionsNotify;
        private GroupMembershipMethodType membershipMethod;
        private String xOrgId;

        private CreateNewGroupRequestBuilder(String name) {
            this.name = name;
        }

        /**
         * Set description
         * @param description Description of a User Group (optional)
         * @return CreateNewGroupRequestBuilder
         */
        public CreateNewGroupRequestBuilder description(String description) {
            this.description = description;
            return this;
        }
        
        /**
         * Set attributes
         * @param attributes  (optional)
         * @return CreateNewGroupRequestBuilder
         */
        public CreateNewGroupRequestBuilder attributes(GroupAttributesUserGroup attributes) {
            this.attributes = attributes;
            return this;
        }
        
        /**
         * Set email
         * @param email Email address of a User Group (optional)
         * @return CreateNewGroupRequestBuilder
         */
        public CreateNewGroupRequestBuilder email(String email) {
            this.email = email;
            return this;
        }
        
        /**
         * Set memberQuery
         * @param memberQuery  (optional)
         * @return CreateNewGroupRequestBuilder
         */
        public CreateNewGroupRequestBuilder memberQuery(MemberQuery memberQuery) {
            this.memberQuery = memberQuery;
            return this;
        }
        
        /**
         * Set memberQueryExemptions
         * @param memberQueryExemptions Array of GraphObjects exempted from the query (optional)
         * @return CreateNewGroupRequestBuilder
         */
        public CreateNewGroupRequestBuilder memberQueryExemptions(List<GraphObject> memberQueryExemptions) {
            this.memberQueryExemptions = memberQueryExemptions;
            return this;
        }
        
        /**
         * Set memberSuggestionsNotify
         * @param memberSuggestionsNotify True if notification emails are to be sent for membership suggestions. (optional)
         * @return CreateNewGroupRequestBuilder
         */
        public CreateNewGroupRequestBuilder memberSuggestionsNotify(Boolean memberSuggestionsNotify) {
            this.memberSuggestionsNotify = memberSuggestionsNotify;
            return this;
        }
        
        /**
         * Set membershipMethod
         * @param membershipMethod  (optional)
         * @return CreateNewGroupRequestBuilder
         */
        public CreateNewGroupRequestBuilder membershipMethod(GroupMembershipMethodType membershipMethod) {
            this.membershipMethod = membershipMethod;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return CreateNewGroupRequestBuilder
         */
        public CreateNewGroupRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for createNewGroup
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            UserGroupPost userGroupPost = buildBodyParams();
            return createNewGroupCall(xOrgId, userGroupPost, _callback);
        }

        private UserGroupPost buildBodyParams() {
            UserGroupPost userGroupPost = new UserGroupPost();
            userGroupPost.description(this.description);
            userGroupPost.attributes(this.attributes);
            userGroupPost.email(this.email);
            userGroupPost.memberQuery(this.memberQuery);
            userGroupPost.memberQueryExemptions(this.memberQueryExemptions);
            userGroupPost.memberSuggestionsNotify(this.memberSuggestionsNotify);
            userGroupPost.membershipMethod(this.membershipMethod);
            userGroupPost.name(this.name);
            return userGroupPost;
        }

        /**
         * Execute createNewGroup request
         * @return UserGroup
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
         </table>
         */
        public UserGroup execute() throws ApiException {
            UserGroupPost userGroupPost = buildBodyParams();
            ApiResponse<UserGroup> localVarResp = createNewGroupWithHttpInfo(xOrgId, userGroupPost);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute createNewGroup request with HTTP info returned
         * @return ApiResponse&lt;UserGroup&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<UserGroup> executeWithHttpInfo() throws ApiException {
            UserGroupPost userGroupPost = buildBodyParams();
            return createNewGroupWithHttpInfo(xOrgId, userGroupPost);
        }

        /**
         * Execute createNewGroup request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<UserGroup> _callback) throws ApiException {
            UserGroupPost userGroupPost = buildBodyParams();
            return createNewGroupAsync(xOrgId, userGroupPost, _callback);
        }
    }

    /**
     * Create a new User Group
     * This endpoint allows you to create a new User Group.  See the [Dynamic Group Configuration KB article](https://jumpcloud.com/support/configure-dynamic-device-groups) for more details on maintaining a Dynamic Group.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/usergroups \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;name\&quot;: \&quot;{Group_Name}\&quot;   }&#39; &#x60;&#x60;&#x60;
     * @return CreateNewGroupRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
     </table>
     */
    public CreateNewGroupRequestBuilder createNewGroup(String name) throws IllegalArgumentException {
        if (name == null) throw new IllegalArgumentException("\"name\" is required but got null");
            

        return new CreateNewGroupRequestBuilder(name);
    }
    private okhttp3.Call deleteGroupCall(String id, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/usergroups/{id}"
            .replace("{" + "id" + "}", localVarApiClient.escapeString(id.toString()));

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
    private okhttp3.Call deleteGroupValidateBeforeCall(String id, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling deleteGroup(Async)");
        }

        return deleteGroupCall(id, xOrgId, _callback);

    }


    private ApiResponse<UserGroup> deleteGroupWithHttpInfo(String id, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = deleteGroupValidateBeforeCall(id, xOrgId, null);
        Type localVarReturnType = new TypeToken<UserGroup>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call deleteGroupAsync(String id, String xOrgId, final ApiCallback<UserGroup> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteGroupValidateBeforeCall(id, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<UserGroup>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class DeleteGroupRequestBuilder {
        private final String id;
        private String xOrgId;

        private DeleteGroupRequestBuilder(String id) {
            this.id = id;
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return DeleteGroupRequestBuilder
         */
        public DeleteGroupRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for deleteGroup
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
            <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return deleteGroupCall(id, xOrgId, _callback);
        }


        /**
         * Execute deleteGroup request
         * @return UserGroup
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
            <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
         </table>
         */
        public UserGroup execute() throws ApiException {
            ApiResponse<UserGroup> localVarResp = deleteGroupWithHttpInfo(id, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute deleteGroup request with HTTP info returned
         * @return ApiResponse&lt;UserGroup&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
            <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<UserGroup> executeWithHttpInfo() throws ApiException {
            return deleteGroupWithHttpInfo(id, xOrgId);
        }

        /**
         * Execute deleteGroup request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
            <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<UserGroup> _callback) throws ApiException {
            return deleteGroupAsync(id, xOrgId, _callback);
        }
    }

    /**
     * Delete a User Group
     * This endpoint allows you to delete a User Group.  #### Sample Request &#x60;&#x60;&#x60; curl -X DELETE https://console.jumpcloud.com/api/v2/usergroups/{GroupID} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;  &#x60;&#x60;&#x60;
     * @param id ObjectID of the User Group. (required)
     * @return DeleteGroupRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
        <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
     </table>
     */
    public DeleteGroupRequestBuilder deleteGroup(String id) throws IllegalArgumentException {
        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new DeleteGroupRequestBuilder(id);
    }
    private okhttp3.Call getDetailsCall(String id, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/usergroups/{id}"
            .replace("{" + "id" + "}", localVarApiClient.escapeString(id.toString()));

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
    private okhttp3.Call getDetailsValidateBeforeCall(String id, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getDetails(Async)");
        }

        return getDetailsCall(id, xOrgId, _callback);

    }


    private ApiResponse<UserGroup> getDetailsWithHttpInfo(String id, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = getDetailsValidateBeforeCall(id, xOrgId, null);
        Type localVarReturnType = new TypeToken<UserGroup>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getDetailsAsync(String id, String xOrgId, final ApiCallback<UserGroup> _callback) throws ApiException {

        okhttp3.Call localVarCall = getDetailsValidateBeforeCall(id, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<UserGroup>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetDetailsRequestBuilder {
        private final String id;
        private String xOrgId;

        private GetDetailsRequestBuilder(String id) {
            this.id = id;
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return GetDetailsRequestBuilder
         */
        public GetDetailsRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for getDetails
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
            return getDetailsCall(id, xOrgId, _callback);
        }


        /**
         * Execute getDetails request
         * @return UserGroup
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public UserGroup execute() throws ApiException {
            ApiResponse<UserGroup> localVarResp = getDetailsWithHttpInfo(id, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getDetails request with HTTP info returned
         * @return ApiResponse&lt;UserGroup&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<UserGroup> executeWithHttpInfo() throws ApiException {
            return getDetailsWithHttpInfo(id, xOrgId);
        }

        /**
         * Execute getDetails request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<UserGroup> _callback) throws ApiException {
            return getDetailsAsync(id, xOrgId, _callback);
        }
    }

    /**
     * View an individual User Group details
     * This endpoint returns the details of a User Group.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/usergroups/{GroupID} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param id ObjectID of the User Group. (required)
     * @return GetDetailsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public GetDetailsRequestBuilder getDetails(String id) throws IllegalArgumentException {
        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new GetDetailsRequestBuilder(id);
    }
    private okhttp3.Call getSuggestionsCall(String groupId, String xOrgId, Integer limit, Integer skip, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/usergroups/{group_id}/suggestions"
            .replace("{" + "group_id" + "}", localVarApiClient.escapeString(groupId.toString()));

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
    private okhttp3.Call getSuggestionsValidateBeforeCall(String groupId, String xOrgId, Integer limit, Integer skip, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'groupId' is set
        if (groupId == null) {
            throw new ApiException("Missing the required parameter 'groupId' when calling getSuggestions(Async)");
        }

        return getSuggestionsCall(groupId, xOrgId, limit, skip, _callback);

    }


    private ApiResponse<List<MemberSuggestion>> getSuggestionsWithHttpInfo(String groupId, String xOrgId, Integer limit, Integer skip) throws ApiException {
        okhttp3.Call localVarCall = getSuggestionsValidateBeforeCall(groupId, xOrgId, limit, skip, null);
        Type localVarReturnType = new TypeToken<List<MemberSuggestion>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getSuggestionsAsync(String groupId, String xOrgId, Integer limit, Integer skip, final ApiCallback<List<MemberSuggestion>> _callback) throws ApiException {

        okhttp3.Call localVarCall = getSuggestionsValidateBeforeCall(groupId, xOrgId, limit, skip, _callback);
        Type localVarReturnType = new TypeToken<List<MemberSuggestion>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetSuggestionsRequestBuilder {
        private final String groupId;
        private String xOrgId;
        private Integer limit;
        private Integer skip;

        private GetSuggestionsRequestBuilder(String groupId) {
            this.groupId = groupId;
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return GetSuggestionsRequestBuilder
         */
        public GetSuggestionsRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return GetSuggestionsRequestBuilder
         */
        public GetSuggestionsRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return GetSuggestionsRequestBuilder
         */
        public GetSuggestionsRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Build call for getSuggestions
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
            return getSuggestionsCall(groupId, xOrgId, limit, skip, _callback);
        }


        /**
         * Execute getSuggestions request
         * @return List&lt;MemberSuggestion&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<MemberSuggestion> execute() throws ApiException {
            ApiResponse<List<MemberSuggestion>> localVarResp = getSuggestionsWithHttpInfo(groupId, xOrgId, limit, skip);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getSuggestions request with HTTP info returned
         * @return ApiResponse&lt;List&lt;MemberSuggestion&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<MemberSuggestion>> executeWithHttpInfo() throws ApiException {
            return getSuggestionsWithHttpInfo(groupId, xOrgId, limit, skip);
        }

        /**
         * Execute getSuggestions request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<MemberSuggestion>> _callback) throws ApiException {
            return getSuggestionsAsync(groupId, xOrgId, limit, skip, _callback);
        }
    }

    /**
     * List Suggestions for a User Group
     * This endpoint returns available suggestions for a given group #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/usergroups/{GroupID}/suggestions \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;  &#x60;&#x60;&#x60;
     * @param groupId ID of the group (required)
     * @return GetSuggestionsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public GetSuggestionsRequestBuilder getSuggestions(String groupId) throws IllegalArgumentException {
        if (groupId == null) throw new IllegalArgumentException("\"groupId\" is required but got null");
            

        return new GetSuggestionsRequestBuilder(groupId);
    }
    private okhttp3.Call listAllCall(List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/usergroups";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (fields != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "fields", fields));
        }

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
    private okhttp3.Call listAllValidateBeforeCall(List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, String xOrgId, final ApiCallback _callback) throws ApiException {
        return listAllCall(fields, filter, limit, skip, sort, xOrgId, _callback);

    }


    private ApiResponse<List<UserGroup>> listAllWithHttpInfo(List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = listAllValidateBeforeCall(fields, filter, limit, skip, sort, xOrgId, null);
        Type localVarReturnType = new TypeToken<List<UserGroup>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listAllAsync(List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, String xOrgId, final ApiCallback<List<UserGroup>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listAllValidateBeforeCall(fields, filter, limit, skip, sort, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<List<UserGroup>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListAllRequestBuilder {
        private List<String> fields;
        private List<String> filter;
        private Integer limit;
        private Integer skip;
        private List<String> sort;
        private String xOrgId;

        private ListAllRequestBuilder() {
        }

        /**
         * Set fields
         * @param fields The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  (optional)
         * @return ListAllRequestBuilder
         */
        public ListAllRequestBuilder fields(List<String> fields) {
            this.fields = fields;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return ListAllRequestBuilder
         */
        public ListAllRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return ListAllRequestBuilder
         */
        public ListAllRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListAllRequestBuilder
         */
        public ListAllRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  (optional)
         * @return ListAllRequestBuilder
         */
        public ListAllRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListAllRequestBuilder
         */
        public ListAllRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for listAll
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listAllCall(fields, filter, limit, skip, sort, xOrgId, _callback);
        }


        /**
         * Execute listAll request
         * @return List&lt;UserGroup&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  -  </td></tr>
         </table>
         */
        public List<UserGroup> execute() throws ApiException {
            ApiResponse<List<UserGroup>> localVarResp = listAllWithHttpInfo(fields, filter, limit, skip, sort, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listAll request with HTTP info returned
         * @return ApiResponse&lt;List&lt;UserGroup&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<UserGroup>> executeWithHttpInfo() throws ApiException {
            return listAllWithHttpInfo(fields, filter, limit, skip, sort, xOrgId);
        }

        /**
         * Execute listAll request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<UserGroup>> _callback) throws ApiException {
            return listAllAsync(fields, filter, limit, skip, sort, xOrgId, _callback);
        }
    }

    /**
     * List all User Groups
     * This endpoint returns all User Groups.  Available filter fields:   - &#x60;name&#x60;   - &#x60;disabled&#x60;   - &#x60;type&#x60;   - &#x60;membershipMethod&#x60;   - &#x60;suggestionCounts.add&#x60;   - &#x60;suggestionCounts.remove&#x60;   - &#x60;suggestionCounts.total&#x60;   - &#x60;attributes.sudo.enabled&#x60;   - &#x60;attributes.sudo.withoutPassword&#x60;  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/usergroups \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @return ListAllRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  -  </td></tr>
     </table>
     */
    public ListAllRequestBuilder listAll() throws IllegalArgumentException {
        return new ListAllRequestBuilder();
    }
    private okhttp3.Call updateGroupCall(String id, String xOrgId, UserGroupPut userGroupPut, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = userGroupPut;

        // create path and map variables
        String localVarPath = "/usergroups/{id}"
            .replace("{" + "id" + "}", localVarApiClient.escapeString(id.toString()));

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
        return localVarApiClient.buildCall(basePath, localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call updateGroupValidateBeforeCall(String id, String xOrgId, UserGroupPut userGroupPut, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling updateGroup(Async)");
        }

        return updateGroupCall(id, xOrgId, userGroupPut, _callback);

    }


    private ApiResponse<UserGroup> updateGroupWithHttpInfo(String id, String xOrgId, UserGroupPut userGroupPut) throws ApiException {
        okhttp3.Call localVarCall = updateGroupValidateBeforeCall(id, xOrgId, userGroupPut, null);
        Type localVarReturnType = new TypeToken<UserGroup>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call updateGroupAsync(String id, String xOrgId, UserGroupPut userGroupPut, final ApiCallback<UserGroup> _callback) throws ApiException {

        okhttp3.Call localVarCall = updateGroupValidateBeforeCall(id, xOrgId, userGroupPut, _callback);
        Type localVarReturnType = new TypeToken<UserGroup>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UpdateGroupRequestBuilder {
        private final String name;
        private final String id;
        private String description;
        private GroupAttributesUserGroup attributes;
        private String email;
        private MemberQuery memberQuery;
        private List<GraphObject> memberQueryExemptions;
        private Boolean memberSuggestionsNotify;
        private GroupMembershipMethodType membershipMethod;
        private String xOrgId;

        private UpdateGroupRequestBuilder(String name, String id) {
            this.name = name;
            this.id = id;
        }

        /**
         * Set description
         * @param description Description of a User Group (optional)
         * @return UpdateGroupRequestBuilder
         */
        public UpdateGroupRequestBuilder description(String description) {
            this.description = description;
            return this;
        }
        
        /**
         * Set attributes
         * @param attributes  (optional)
         * @return UpdateGroupRequestBuilder
         */
        public UpdateGroupRequestBuilder attributes(GroupAttributesUserGroup attributes) {
            this.attributes = attributes;
            return this;
        }
        
        /**
         * Set email
         * @param email Email address of a User Group (optional)
         * @return UpdateGroupRequestBuilder
         */
        public UpdateGroupRequestBuilder email(String email) {
            this.email = email;
            return this;
        }
        
        /**
         * Set memberQuery
         * @param memberQuery  (optional)
         * @return UpdateGroupRequestBuilder
         */
        public UpdateGroupRequestBuilder memberQuery(MemberQuery memberQuery) {
            this.memberQuery = memberQuery;
            return this;
        }
        
        /**
         * Set memberQueryExemptions
         * @param memberQueryExemptions Array of GraphObjects exempted from the query (optional)
         * @return UpdateGroupRequestBuilder
         */
        public UpdateGroupRequestBuilder memberQueryExemptions(List<GraphObject> memberQueryExemptions) {
            this.memberQueryExemptions = memberQueryExemptions;
            return this;
        }
        
        /**
         * Set memberSuggestionsNotify
         * @param memberSuggestionsNotify True if notification emails are to be sent for membership suggestions. (optional)
         * @return UpdateGroupRequestBuilder
         */
        public UpdateGroupRequestBuilder memberSuggestionsNotify(Boolean memberSuggestionsNotify) {
            this.memberSuggestionsNotify = memberSuggestionsNotify;
            return this;
        }
        
        /**
         * Set membershipMethod
         * @param membershipMethod  (optional)
         * @return UpdateGroupRequestBuilder
         */
        public UpdateGroupRequestBuilder membershipMethod(GroupMembershipMethodType membershipMethod) {
            this.membershipMethod = membershipMethod;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UpdateGroupRequestBuilder
         */
        public UpdateGroupRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for updateGroup
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
            UserGroupPut userGroupPut = buildBodyParams();
            return updateGroupCall(id, xOrgId, userGroupPut, _callback);
        }

        private UserGroupPut buildBodyParams() {
            UserGroupPut userGroupPut = new UserGroupPut();
            userGroupPut.description(this.description);
            userGroupPut.attributes(this.attributes);
            userGroupPut.email(this.email);
            userGroupPut.memberQuery(this.memberQuery);
            userGroupPut.memberQueryExemptions(this.memberQueryExemptions);
            userGroupPut.memberSuggestionsNotify(this.memberSuggestionsNotify);
            userGroupPut.membershipMethod(this.membershipMethod);
            userGroupPut.name(this.name);
            return userGroupPut;
        }

        /**
         * Execute updateGroup request
         * @return UserGroup
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public UserGroup execute() throws ApiException {
            UserGroupPut userGroupPut = buildBodyParams();
            ApiResponse<UserGroup> localVarResp = updateGroupWithHttpInfo(id, xOrgId, userGroupPut);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute updateGroup request with HTTP info returned
         * @return ApiResponse&lt;UserGroup&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<UserGroup> executeWithHttpInfo() throws ApiException {
            UserGroupPut userGroupPut = buildBodyParams();
            return updateGroupWithHttpInfo(id, xOrgId, userGroupPut);
        }

        /**
         * Execute updateGroup request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<UserGroup> _callback) throws ApiException {
            UserGroupPut userGroupPut = buildBodyParams();
            return updateGroupAsync(id, xOrgId, userGroupPut, _callback);
        }
    }

    /**
     * Update a User Group
     * This endpoint allows you to do a full update of the User Group.  See the [Dynamic Group Configuration KB article](https://jumpcloud.com/support/configure-dynamic-device-groups) for more details on maintaining a Dynamic Group.  #### Sample Request &#x60;&#x60;&#x60; curl -X PUT https://console.jumpcloud.com/api/v2/usergroups/{Group_ID} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;name\&quot;: \&quot;group_update\&quot;   }&#39; &#x60;&#x60;&#x60;
     * @param id ObjectID of the User Group. (required)
     * @return UpdateGroupRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UpdateGroupRequestBuilder updateGroup(String name, String id) throws IllegalArgumentException {
        if (name == null) throw new IllegalArgumentException("\"name\" is required but got null");
            

        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new UpdateGroupRequestBuilder(name, id);
    }
    private okhttp3.Call userGroupAssociationsListCall(String groupId, List<String> targets, Integer limit, Integer skip, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/usergroups/{group_id}/associations"
            .replace("{" + "group_id" + "}", localVarApiClient.escapeString(groupId.toString()));

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
    private okhttp3.Call userGroupAssociationsListValidateBeforeCall(String groupId, List<String> targets, Integer limit, Integer skip, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'groupId' is set
        if (groupId == null) {
            throw new ApiException("Missing the required parameter 'groupId' when calling userGroupAssociationsList(Async)");
        }

        // verify the required parameter 'targets' is set
        if (targets == null) {
            throw new ApiException("Missing the required parameter 'targets' when calling userGroupAssociationsList(Async)");
        }

        return userGroupAssociationsListCall(groupId, targets, limit, skip, xOrgId, _callback);

    }


    private ApiResponse<List<GraphConnection>> userGroupAssociationsListWithHttpInfo(String groupId, List<String> targets, Integer limit, Integer skip, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = userGroupAssociationsListValidateBeforeCall(groupId, targets, limit, skip, xOrgId, null);
        Type localVarReturnType = new TypeToken<List<GraphConnection>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call userGroupAssociationsListAsync(String groupId, List<String> targets, Integer limit, Integer skip, String xOrgId, final ApiCallback<List<GraphConnection>> _callback) throws ApiException {

        okhttp3.Call localVarCall = userGroupAssociationsListValidateBeforeCall(groupId, targets, limit, skip, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<List<GraphConnection>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UserGroupAssociationsListRequestBuilder {
        private final String groupId;
        private final List<String> targets;
        private Integer limit;
        private Integer skip;
        private String xOrgId;

        private UserGroupAssociationsListRequestBuilder(String groupId, List<String> targets) {
            this.groupId = groupId;
            this.targets = targets;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return UserGroupAssociationsListRequestBuilder
         */
        public UserGroupAssociationsListRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return UserGroupAssociationsListRequestBuilder
         */
        public UserGroupAssociationsListRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UserGroupAssociationsListRequestBuilder
         */
        public UserGroupAssociationsListRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for userGroupAssociationsList
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
            return userGroupAssociationsListCall(groupId, targets, limit, skip, xOrgId, _callback);
        }


        /**
         * Execute userGroupAssociationsList request
         * @return List&lt;GraphConnection&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphConnection> execute() throws ApiException {
            ApiResponse<List<GraphConnection>> localVarResp = userGroupAssociationsListWithHttpInfo(groupId, targets, limit, skip, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute userGroupAssociationsList request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphConnection&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphConnection>> executeWithHttpInfo() throws ApiException {
            return userGroupAssociationsListWithHttpInfo(groupId, targets, limit, skip, xOrgId);
        }

        /**
         * Execute userGroupAssociationsList request (asynchronously)
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
            return userGroupAssociationsListAsync(groupId, targets, limit, skip, xOrgId, _callback);
        }
    }

    /**
     * List the associations of a User Group.
     * This endpoint returns the _direct_ associations of this User Group.  A direct association can be a non-homogeneous relationship between 2 different objects, for example User Groups and Users.   #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/usergroups/{GroupID}/associations?targets&#x3D;system \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param groupId ObjectID of the User Group. (required)
     * @param targets Targets which a \&quot;user_group\&quot; can be associated to. (required)
     * @return UserGroupAssociationsListRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UserGroupAssociationsListRequestBuilder userGroupAssociationsList(String groupId, List<String> targets) throws IllegalArgumentException {
        if (groupId == null) throw new IllegalArgumentException("\"groupId\" is required but got null");
            

        if (targets == null) throw new IllegalArgumentException("\"targets\" is required but got null");
        return new UserGroupAssociationsListRequestBuilder(groupId, targets);
    }
    private okhttp3.Call userGroupAssociationsPostCall(String groupId, String xOrgId, GraphOperationUserGroup graphOperationUserGroup, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = graphOperationUserGroup;

        // create path and map variables
        String localVarPath = "/usergroups/{group_id}/associations"
            .replace("{" + "group_id" + "}", localVarApiClient.escapeString(groupId.toString()));

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
    private okhttp3.Call userGroupAssociationsPostValidateBeforeCall(String groupId, String xOrgId, GraphOperationUserGroup graphOperationUserGroup, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'groupId' is set
        if (groupId == null) {
            throw new ApiException("Missing the required parameter 'groupId' when calling userGroupAssociationsPost(Async)");
        }

        return userGroupAssociationsPostCall(groupId, xOrgId, graphOperationUserGroup, _callback);

    }


    private ApiResponse<Void> userGroupAssociationsPostWithHttpInfo(String groupId, String xOrgId, GraphOperationUserGroup graphOperationUserGroup) throws ApiException {
        okhttp3.Call localVarCall = userGroupAssociationsPostValidateBeforeCall(groupId, xOrgId, graphOperationUserGroup, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call userGroupAssociationsPostAsync(String groupId, String xOrgId, GraphOperationUserGroup graphOperationUserGroup, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = userGroupAssociationsPostValidateBeforeCall(groupId, xOrgId, graphOperationUserGroup, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class UserGroupAssociationsPostRequestBuilder {
        private final String groupId;
        private String id;
        private String op;
        private Map<String, Object> attributes;
        private String type;
        private String xOrgId;

        private UserGroupAssociationsPostRequestBuilder(String groupId) {
            this.groupId = groupId;
        }

        /**
         * Set id
         * @param id The ObjectID of graph object being added or removed as an association. (optional)
         * @return UserGroupAssociationsPostRequestBuilder
         */
        public UserGroupAssociationsPostRequestBuilder id(String id) {
            this.id = id;
            return this;
        }
        
        /**
         * Set op
         * @param op How to modify the graph connection. (optional)
         * @return UserGroupAssociationsPostRequestBuilder
         */
        public UserGroupAssociationsPostRequestBuilder op(String op) {
            this.op = op;
            return this;
        }
        
        /**
         * Set attributes
         * @param attributes The graph attributes. (optional)
         * @return UserGroupAssociationsPostRequestBuilder
         */
        public UserGroupAssociationsPostRequestBuilder attributes(Map<String, Object> attributes) {
            this.attributes = attributes;
            return this;
        }
        
        /**
         * Set type
         * @param type Targets which a \\\&quot;user_group\\\&quot; can be associated to. (optional)
         * @return UserGroupAssociationsPostRequestBuilder
         */
        public UserGroupAssociationsPostRequestBuilder type(String type) {
            this.type = type;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UserGroupAssociationsPostRequestBuilder
         */
        public UserGroupAssociationsPostRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for userGroupAssociationsPost
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
            GraphOperationUserGroup graphOperationUserGroup = buildBodyParams();
            return userGroupAssociationsPostCall(groupId, xOrgId, graphOperationUserGroup, _callback);
        }

        private GraphOperationUserGroup buildBodyParams() {
            GraphOperationUserGroup graphOperationUserGroup = new GraphOperationUserGroup();
            return graphOperationUserGroup;
        }

        /**
         * Execute userGroupAssociationsPost request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            GraphOperationUserGroup graphOperationUserGroup = buildBodyParams();
            userGroupAssociationsPostWithHttpInfo(groupId, xOrgId, graphOperationUserGroup);
        }

        /**
         * Execute userGroupAssociationsPost request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            GraphOperationUserGroup graphOperationUserGroup = buildBodyParams();
            return userGroupAssociationsPostWithHttpInfo(groupId, xOrgId, graphOperationUserGroup);
        }

        /**
         * Execute userGroupAssociationsPost request (asynchronously)
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
            GraphOperationUserGroup graphOperationUserGroup = buildBodyParams();
            return userGroupAssociationsPostAsync(groupId, xOrgId, graphOperationUserGroup, _callback);
        }
    }

    /**
     * Manage the associations of a User Group
     * This endpoint manages the _direct_ associations of this User Group.  A direct association can be a non-homogeneous relationship between 2 different objects, for example User Groups and Users.   #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/usergroups/{GroupID}/associations \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;op\&quot;: \&quot;add\&quot;,     \&quot;type\&quot;: \&quot;system\&quot;,     \&quot;id\&quot;: \&quot;{SystemID}\&quot;   }&#39; &#x60;&#x60;&#x60;
     * @param groupId ObjectID of the User Group. (required)
     * @return UserGroupAssociationsPostRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UserGroupAssociationsPostRequestBuilder userGroupAssociationsPost(String groupId) throws IllegalArgumentException {
        if (groupId == null) throw new IllegalArgumentException("\"groupId\" is required but got null");
            

        return new UserGroupAssociationsPostRequestBuilder(groupId);
    }
    private okhttp3.Call userGroupMembersListCall(String groupId, Integer limit, Integer skip, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/usergroups/{group_id}/members"
            .replace("{" + "group_id" + "}", localVarApiClient.escapeString(groupId.toString()));

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
    private okhttp3.Call userGroupMembersListValidateBeforeCall(String groupId, Integer limit, Integer skip, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'groupId' is set
        if (groupId == null) {
            throw new ApiException("Missing the required parameter 'groupId' when calling userGroupMembersList(Async)");
        }

        return userGroupMembersListCall(groupId, limit, skip, xOrgId, _callback);

    }


    private ApiResponse<List<GraphConnection>> userGroupMembersListWithHttpInfo(String groupId, Integer limit, Integer skip, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = userGroupMembersListValidateBeforeCall(groupId, limit, skip, xOrgId, null);
        Type localVarReturnType = new TypeToken<List<GraphConnection>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call userGroupMembersListAsync(String groupId, Integer limit, Integer skip, String xOrgId, final ApiCallback<List<GraphConnection>> _callback) throws ApiException {

        okhttp3.Call localVarCall = userGroupMembersListValidateBeforeCall(groupId, limit, skip, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<List<GraphConnection>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UserGroupMembersListRequestBuilder {
        private final String groupId;
        private Integer limit;
        private Integer skip;
        private String xOrgId;

        private UserGroupMembersListRequestBuilder(String groupId) {
            this.groupId = groupId;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return UserGroupMembersListRequestBuilder
         */
        public UserGroupMembersListRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return UserGroupMembersListRequestBuilder
         */
        public UserGroupMembersListRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UserGroupMembersListRequestBuilder
         */
        public UserGroupMembersListRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for userGroupMembersList
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
            return userGroupMembersListCall(groupId, limit, skip, xOrgId, _callback);
        }


        /**
         * Execute userGroupMembersList request
         * @return List&lt;GraphConnection&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphConnection> execute() throws ApiException {
            ApiResponse<List<GraphConnection>> localVarResp = userGroupMembersListWithHttpInfo(groupId, limit, skip, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute userGroupMembersList request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphConnection&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphConnection>> executeWithHttpInfo() throws ApiException {
            return userGroupMembersListWithHttpInfo(groupId, limit, skip, xOrgId);
        }

        /**
         * Execute userGroupMembersList request (asynchronously)
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
            return userGroupMembersListAsync(groupId, limit, skip, xOrgId, _callback);
        }
    }

    /**
     * List the members of a User Group
     * This endpoint returns the user members of a User Group.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/usergroups/{GroupID}/members \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param groupId ObjectID of the User Group. (required)
     * @return UserGroupMembersListRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UserGroupMembersListRequestBuilder userGroupMembersList(String groupId) throws IllegalArgumentException {
        if (groupId == null) throw new IllegalArgumentException("\"groupId\" is required but got null");
            

        return new UserGroupMembersListRequestBuilder(groupId);
    }
    private okhttp3.Call userGroupMembersPostCall(String groupId, String xOrgId, GraphOperationUserGroupMember graphOperationUserGroupMember, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = graphOperationUserGroupMember;

        // create path and map variables
        String localVarPath = "/usergroups/{group_id}/members"
            .replace("{" + "group_id" + "}", localVarApiClient.escapeString(groupId.toString()));

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
    private okhttp3.Call userGroupMembersPostValidateBeforeCall(String groupId, String xOrgId, GraphOperationUserGroupMember graphOperationUserGroupMember, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'groupId' is set
        if (groupId == null) {
            throw new ApiException("Missing the required parameter 'groupId' when calling userGroupMembersPost(Async)");
        }

        return userGroupMembersPostCall(groupId, xOrgId, graphOperationUserGroupMember, _callback);

    }


    private ApiResponse<Void> userGroupMembersPostWithHttpInfo(String groupId, String xOrgId, GraphOperationUserGroupMember graphOperationUserGroupMember) throws ApiException {
        okhttp3.Call localVarCall = userGroupMembersPostValidateBeforeCall(groupId, xOrgId, graphOperationUserGroupMember, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call userGroupMembersPostAsync(String groupId, String xOrgId, GraphOperationUserGroupMember graphOperationUserGroupMember, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = userGroupMembersPostValidateBeforeCall(groupId, xOrgId, graphOperationUserGroupMember, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class UserGroupMembersPostRequestBuilder {
        private final String groupId;
        private String id;
        private String op;
        private Map<String, Object> attributes;
        private String type;
        private String xOrgId;

        private UserGroupMembersPostRequestBuilder(String groupId) {
            this.groupId = groupId;
        }

        /**
         * Set id
         * @param id The ObjectID of graph object being added or removed as an association. (optional)
         * @return UserGroupMembersPostRequestBuilder
         */
        public UserGroupMembersPostRequestBuilder id(String id) {
            this.id = id;
            return this;
        }
        
        /**
         * Set op
         * @param op How to modify the graph connection. (optional)
         * @return UserGroupMembersPostRequestBuilder
         */
        public UserGroupMembersPostRequestBuilder op(String op) {
            this.op = op;
            return this;
        }
        
        /**
         * Set attributes
         * @param attributes The graph attributes. (optional)
         * @return UserGroupMembersPostRequestBuilder
         */
        public UserGroupMembersPostRequestBuilder attributes(Map<String, Object> attributes) {
            this.attributes = attributes;
            return this;
        }
        
        /**
         * Set type
         * @param type The member type. (optional)
         * @return UserGroupMembersPostRequestBuilder
         */
        public UserGroupMembersPostRequestBuilder type(String type) {
            this.type = type;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UserGroupMembersPostRequestBuilder
         */
        public UserGroupMembersPostRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for userGroupMembersPost
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
            GraphOperationUserGroupMember graphOperationUserGroupMember = buildBodyParams();
            return userGroupMembersPostCall(groupId, xOrgId, graphOperationUserGroupMember, _callback);
        }

        private GraphOperationUserGroupMember buildBodyParams() {
            GraphOperationUserGroupMember graphOperationUserGroupMember = new GraphOperationUserGroupMember();
            return graphOperationUserGroupMember;
        }

        /**
         * Execute userGroupMembersPost request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            GraphOperationUserGroupMember graphOperationUserGroupMember = buildBodyParams();
            userGroupMembersPostWithHttpInfo(groupId, xOrgId, graphOperationUserGroupMember);
        }

        /**
         * Execute userGroupMembersPost request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            GraphOperationUserGroupMember graphOperationUserGroupMember = buildBodyParams();
            return userGroupMembersPostWithHttpInfo(groupId, xOrgId, graphOperationUserGroupMember);
        }

        /**
         * Execute userGroupMembersPost request (asynchronously)
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
            GraphOperationUserGroupMember graphOperationUserGroupMember = buildBodyParams();
            return userGroupMembersPostAsync(groupId, xOrgId, graphOperationUserGroupMember, _callback);
        }
    }

    /**
     * Manage the members of a User Group
     * This endpoint allows you to manage the user members of a User Group.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/usergroups/{GroupID}/members \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;op\&quot;: \&quot;add\&quot;,     \&quot;type\&quot;: \&quot;user\&quot;,     \&quot;id\&quot;: \&quot;{User_ID}\&quot;   }&#39; &#x60;&#x60;&#x60;
     * @param groupId ObjectID of the User Group. (required)
     * @return UserGroupMembersPostRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UserGroupMembersPostRequestBuilder userGroupMembersPost(String groupId) throws IllegalArgumentException {
        if (groupId == null) throw new IllegalArgumentException("\"groupId\" is required but got null");
            

        return new UserGroupMembersPostRequestBuilder(groupId);
    }
    private okhttp3.Call userGroupMembershipCall(String groupId, List<String> filter, Integer limit, Integer skip, List<String> sort, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/usergroups/{group_id}/membership"
            .replace("{" + "group_id" + "}", localVarApiClient.escapeString(groupId.toString()));

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
    private okhttp3.Call userGroupMembershipValidateBeforeCall(String groupId, List<String> filter, Integer limit, Integer skip, List<String> sort, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'groupId' is set
        if (groupId == null) {
            throw new ApiException("Missing the required parameter 'groupId' when calling userGroupMembership(Async)");
        }

        return userGroupMembershipCall(groupId, filter, limit, skip, sort, xOrgId, _callback);

    }


    private ApiResponse<List<GraphObjectWithPaths>> userGroupMembershipWithHttpInfo(String groupId, List<String> filter, Integer limit, Integer skip, List<String> sort, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = userGroupMembershipValidateBeforeCall(groupId, filter, limit, skip, sort, xOrgId, null);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call userGroupMembershipAsync(String groupId, List<String> filter, Integer limit, Integer skip, List<String> sort, String xOrgId, final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {

        okhttp3.Call localVarCall = userGroupMembershipValidateBeforeCall(groupId, filter, limit, skip, sort, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UserGroupMembershipRequestBuilder {
        private final String groupId;
        private List<String> filter;
        private Integer limit;
        private Integer skip;
        private List<String> sort;
        private String xOrgId;

        private UserGroupMembershipRequestBuilder(String groupId) {
            this.groupId = groupId;
        }

        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return UserGroupMembershipRequestBuilder
         */
        public UserGroupMembershipRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return UserGroupMembershipRequestBuilder
         */
        public UserGroupMembershipRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return UserGroupMembershipRequestBuilder
         */
        public UserGroupMembershipRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  (optional)
         * @return UserGroupMembershipRequestBuilder
         */
        public UserGroupMembershipRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UserGroupMembershipRequestBuilder
         */
        public UserGroupMembershipRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for userGroupMembership
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
            return userGroupMembershipCall(groupId, filter, limit, skip, sort, xOrgId, _callback);
        }


        /**
         * Execute userGroupMembership request
         * @return List&lt;GraphObjectWithPaths&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphObjectWithPaths> execute() throws ApiException {
            ApiResponse<List<GraphObjectWithPaths>> localVarResp = userGroupMembershipWithHttpInfo(groupId, filter, limit, skip, sort, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute userGroupMembership request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphObjectWithPaths&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphObjectWithPaths>> executeWithHttpInfo() throws ApiException {
            return userGroupMembershipWithHttpInfo(groupId, filter, limit, skip, sort, xOrgId);
        }

        /**
         * Execute userGroupMembership request (asynchronously)
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
            return userGroupMembershipAsync(groupId, filter, limit, skip, sort, xOrgId, _callback);
        }
    }

    /**
     * List the User Group&#39;s membership
     * This endpoint returns all users members that are a member of this User Group.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/usergroups/{GroupID}/membership \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param groupId ObjectID of the User Group. (required)
     * @return UserGroupMembershipRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UserGroupMembershipRequestBuilder userGroupMembership(String groupId) throws IllegalArgumentException {
        if (groupId == null) throw new IllegalArgumentException("\"groupId\" is required but got null");
            

        return new UserGroupMembershipRequestBuilder(groupId);
    }
    private okhttp3.Call userGroupTraverseActiveDirectoryCall(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/usergroups/{group_id}/activedirectories"
            .replace("{" + "group_id" + "}", localVarApiClient.escapeString(groupId.toString()));

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
    private okhttp3.Call userGroupTraverseActiveDirectoryValidateBeforeCall(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'groupId' is set
        if (groupId == null) {
            throw new ApiException("Missing the required parameter 'groupId' when calling userGroupTraverseActiveDirectory(Async)");
        }

        return userGroupTraverseActiveDirectoryCall(groupId, limit, xOrgId, skip, filter, _callback);

    }


    private ApiResponse<List<GraphObjectWithPaths>> userGroupTraverseActiveDirectoryWithHttpInfo(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter) throws ApiException {
        okhttp3.Call localVarCall = userGroupTraverseActiveDirectoryValidateBeforeCall(groupId, limit, xOrgId, skip, filter, null);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call userGroupTraverseActiveDirectoryAsync(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {

        okhttp3.Call localVarCall = userGroupTraverseActiveDirectoryValidateBeforeCall(groupId, limit, xOrgId, skip, filter, _callback);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UserGroupTraverseActiveDirectoryRequestBuilder {
        private final String groupId;
        private Integer limit;
        private String xOrgId;
        private Integer skip;
        private List<String> filter;

        private UserGroupTraverseActiveDirectoryRequestBuilder(String groupId) {
            this.groupId = groupId;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return UserGroupTraverseActiveDirectoryRequestBuilder
         */
        public UserGroupTraverseActiveDirectoryRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UserGroupTraverseActiveDirectoryRequestBuilder
         */
        public UserGroupTraverseActiveDirectoryRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return UserGroupTraverseActiveDirectoryRequestBuilder
         */
        public UserGroupTraverseActiveDirectoryRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return UserGroupTraverseActiveDirectoryRequestBuilder
         */
        public UserGroupTraverseActiveDirectoryRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Build call for userGroupTraverseActiveDirectory
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
            return userGroupTraverseActiveDirectoryCall(groupId, limit, xOrgId, skip, filter, _callback);
        }


        /**
         * Execute userGroupTraverseActiveDirectory request
         * @return List&lt;GraphObjectWithPaths&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphObjectWithPaths> execute() throws ApiException {
            ApiResponse<List<GraphObjectWithPaths>> localVarResp = userGroupTraverseActiveDirectoryWithHttpInfo(groupId, limit, xOrgId, skip, filter);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute userGroupTraverseActiveDirectory request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphObjectWithPaths&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphObjectWithPaths>> executeWithHttpInfo() throws ApiException {
            return userGroupTraverseActiveDirectoryWithHttpInfo(groupId, limit, xOrgId, skip, filter);
        }

        /**
         * Execute userGroupTraverseActiveDirectory request (asynchronously)
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
            return userGroupTraverseActiveDirectoryAsync(groupId, limit, xOrgId, skip, filter, _callback);
        }
    }

    /**
     * List the Active Directories bound to a User Group
     * This endpoint will return all Active Directory Instances bound to a User Group, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User Group to the corresponding Active Directory; this array represents all grouping and/or associations that would have to be removed to deprovision the Active Directory from this User Group.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/usergroups/{GroupID}/activedirectories \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param groupId ObjectID of the User Group. (required)
     * @return UserGroupTraverseActiveDirectoryRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public UserGroupTraverseActiveDirectoryRequestBuilder userGroupTraverseActiveDirectory(String groupId) throws IllegalArgumentException {
        if (groupId == null) throw new IllegalArgumentException("\"groupId\" is required but got null");
            

        return new UserGroupTraverseActiveDirectoryRequestBuilder(groupId);
    }
    private okhttp3.Call userGroupTraverseApplicationCall(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/usergroups/{group_id}/applications"
            .replace("{" + "group_id" + "}", localVarApiClient.escapeString(groupId.toString()));

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
    private okhttp3.Call userGroupTraverseApplicationValidateBeforeCall(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'groupId' is set
        if (groupId == null) {
            throw new ApiException("Missing the required parameter 'groupId' when calling userGroupTraverseApplication(Async)");
        }

        return userGroupTraverseApplicationCall(groupId, limit, xOrgId, skip, filter, _callback);

    }


    private ApiResponse<List<GraphObjectWithPaths>> userGroupTraverseApplicationWithHttpInfo(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter) throws ApiException {
        okhttp3.Call localVarCall = userGroupTraverseApplicationValidateBeforeCall(groupId, limit, xOrgId, skip, filter, null);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call userGroupTraverseApplicationAsync(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {

        okhttp3.Call localVarCall = userGroupTraverseApplicationValidateBeforeCall(groupId, limit, xOrgId, skip, filter, _callback);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UserGroupTraverseApplicationRequestBuilder {
        private final String groupId;
        private Integer limit;
        private String xOrgId;
        private Integer skip;
        private List<String> filter;

        private UserGroupTraverseApplicationRequestBuilder(String groupId) {
            this.groupId = groupId;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return UserGroupTraverseApplicationRequestBuilder
         */
        public UserGroupTraverseApplicationRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UserGroupTraverseApplicationRequestBuilder
         */
        public UserGroupTraverseApplicationRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return UserGroupTraverseApplicationRequestBuilder
         */
        public UserGroupTraverseApplicationRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return UserGroupTraverseApplicationRequestBuilder
         */
        public UserGroupTraverseApplicationRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Build call for userGroupTraverseApplication
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
            return userGroupTraverseApplicationCall(groupId, limit, xOrgId, skip, filter, _callback);
        }


        /**
         * Execute userGroupTraverseApplication request
         * @return List&lt;GraphObjectWithPaths&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphObjectWithPaths> execute() throws ApiException {
            ApiResponse<List<GraphObjectWithPaths>> localVarResp = userGroupTraverseApplicationWithHttpInfo(groupId, limit, xOrgId, skip, filter);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute userGroupTraverseApplication request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphObjectWithPaths&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphObjectWithPaths>> executeWithHttpInfo() throws ApiException {
            return userGroupTraverseApplicationWithHttpInfo(groupId, limit, xOrgId, skip, filter);
        }

        /**
         * Execute userGroupTraverseApplication request (asynchronously)
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
            return userGroupTraverseApplicationAsync(groupId, limit, xOrgId, skip, filter, _callback);
        }
    }

    /**
     * List the Applications bound to a User Group
     * This endpoint will return all Applications bound to a User Group, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User Group to the corresponding Application; this array represents all grouping and/or associations that would have to be removed to deprovision the Application from this User Group.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/usergroups/{GroupID}/applications \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param groupId ObjectID of the User Group. (required)
     * @return UserGroupTraverseApplicationRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UserGroupTraverseApplicationRequestBuilder userGroupTraverseApplication(String groupId) throws IllegalArgumentException {
        if (groupId == null) throw new IllegalArgumentException("\"groupId\" is required but got null");
            

        return new UserGroupTraverseApplicationRequestBuilder(groupId);
    }
    private okhttp3.Call userGroupTraverseDirectoryCall(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/usergroups/{group_id}/directories"
            .replace("{" + "group_id" + "}", localVarApiClient.escapeString(groupId.toString()));

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
    private okhttp3.Call userGroupTraverseDirectoryValidateBeforeCall(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'groupId' is set
        if (groupId == null) {
            throw new ApiException("Missing the required parameter 'groupId' when calling userGroupTraverseDirectory(Async)");
        }

        return userGroupTraverseDirectoryCall(groupId, limit, xOrgId, skip, filter, _callback);

    }


    private ApiResponse<List<GraphObjectWithPaths>> userGroupTraverseDirectoryWithHttpInfo(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter) throws ApiException {
        okhttp3.Call localVarCall = userGroupTraverseDirectoryValidateBeforeCall(groupId, limit, xOrgId, skip, filter, null);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call userGroupTraverseDirectoryAsync(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {

        okhttp3.Call localVarCall = userGroupTraverseDirectoryValidateBeforeCall(groupId, limit, xOrgId, skip, filter, _callback);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UserGroupTraverseDirectoryRequestBuilder {
        private final String groupId;
        private Integer limit;
        private String xOrgId;
        private Integer skip;
        private List<String> filter;

        private UserGroupTraverseDirectoryRequestBuilder(String groupId) {
            this.groupId = groupId;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return UserGroupTraverseDirectoryRequestBuilder
         */
        public UserGroupTraverseDirectoryRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UserGroupTraverseDirectoryRequestBuilder
         */
        public UserGroupTraverseDirectoryRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return UserGroupTraverseDirectoryRequestBuilder
         */
        public UserGroupTraverseDirectoryRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return UserGroupTraverseDirectoryRequestBuilder
         */
        public UserGroupTraverseDirectoryRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Build call for userGroupTraverseDirectory
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
            return userGroupTraverseDirectoryCall(groupId, limit, xOrgId, skip, filter, _callback);
        }


        /**
         * Execute userGroupTraverseDirectory request
         * @return List&lt;GraphObjectWithPaths&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphObjectWithPaths> execute() throws ApiException {
            ApiResponse<List<GraphObjectWithPaths>> localVarResp = userGroupTraverseDirectoryWithHttpInfo(groupId, limit, xOrgId, skip, filter);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute userGroupTraverseDirectory request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphObjectWithPaths&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphObjectWithPaths>> executeWithHttpInfo() throws ApiException {
            return userGroupTraverseDirectoryWithHttpInfo(groupId, limit, xOrgId, skip, filter);
        }

        /**
         * Execute userGroupTraverseDirectory request (asynchronously)
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
            return userGroupTraverseDirectoryAsync(groupId, limit, xOrgId, skip, filter, _callback);
        }
    }

    /**
     * List the Directories bound to a User Group
     * This endpoint will return all Directories bound to a User Group, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User Group to the corresponding Directory; this array represents all grouping and/or associations that would have to be removed to deprovision the Directories from this User Group.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/usergroups/{GroupID}/directories \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;  &#x60;&#x60;&#x60;
     * @param groupId ObjectID of the User Group. (required)
     * @return UserGroupTraverseDirectoryRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UserGroupTraverseDirectoryRequestBuilder userGroupTraverseDirectory(String groupId) throws IllegalArgumentException {
        if (groupId == null) throw new IllegalArgumentException("\"groupId\" is required but got null");
            

        return new UserGroupTraverseDirectoryRequestBuilder(groupId);
    }
    private okhttp3.Call userGroupTraverseGSuiteCall(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/usergroups/{group_id}/gsuites"
            .replace("{" + "group_id" + "}", localVarApiClient.escapeString(groupId.toString()));

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
    private okhttp3.Call userGroupTraverseGSuiteValidateBeforeCall(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'groupId' is set
        if (groupId == null) {
            throw new ApiException("Missing the required parameter 'groupId' when calling userGroupTraverseGSuite(Async)");
        }

        return userGroupTraverseGSuiteCall(groupId, limit, xOrgId, skip, filter, _callback);

    }


    private ApiResponse<List<GraphObjectWithPaths>> userGroupTraverseGSuiteWithHttpInfo(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter) throws ApiException {
        okhttp3.Call localVarCall = userGroupTraverseGSuiteValidateBeforeCall(groupId, limit, xOrgId, skip, filter, null);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call userGroupTraverseGSuiteAsync(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {

        okhttp3.Call localVarCall = userGroupTraverseGSuiteValidateBeforeCall(groupId, limit, xOrgId, skip, filter, _callback);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UserGroupTraverseGSuiteRequestBuilder {
        private final String groupId;
        private Integer limit;
        private String xOrgId;
        private Integer skip;
        private List<String> filter;

        private UserGroupTraverseGSuiteRequestBuilder(String groupId) {
            this.groupId = groupId;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return UserGroupTraverseGSuiteRequestBuilder
         */
        public UserGroupTraverseGSuiteRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UserGroupTraverseGSuiteRequestBuilder
         */
        public UserGroupTraverseGSuiteRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return UserGroupTraverseGSuiteRequestBuilder
         */
        public UserGroupTraverseGSuiteRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return UserGroupTraverseGSuiteRequestBuilder
         */
        public UserGroupTraverseGSuiteRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Build call for userGroupTraverseGSuite
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
            return userGroupTraverseGSuiteCall(groupId, limit, xOrgId, skip, filter, _callback);
        }


        /**
         * Execute userGroupTraverseGSuite request
         * @return List&lt;GraphObjectWithPaths&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphObjectWithPaths> execute() throws ApiException {
            ApiResponse<List<GraphObjectWithPaths>> localVarResp = userGroupTraverseGSuiteWithHttpInfo(groupId, limit, xOrgId, skip, filter);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute userGroupTraverseGSuite request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphObjectWithPaths&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphObjectWithPaths>> executeWithHttpInfo() throws ApiException {
            return userGroupTraverseGSuiteWithHttpInfo(groupId, limit, xOrgId, skip, filter);
        }

        /**
         * Execute userGroupTraverseGSuite request (asynchronously)
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
            return userGroupTraverseGSuiteAsync(groupId, limit, xOrgId, skip, filter, _callback);
        }
    }

    /**
     * List the G Suite instances bound to a User Group
     * This endpoint will return all G Suite Instances bound to a User Group, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User Group to the corresponding G Suite instance; this array represents all grouping and/or associations that would have to be removed to deprovision the G Suite instance from this User Group.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/usergroups/{GroupID/gsuites \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;  &#x60;&#x60;&#x60;
     * @param groupId ObjectID of the User Group. (required)
     * @return UserGroupTraverseGSuiteRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UserGroupTraverseGSuiteRequestBuilder userGroupTraverseGSuite(String groupId) throws IllegalArgumentException {
        if (groupId == null) throw new IllegalArgumentException("\"groupId\" is required but got null");
            

        return new UserGroupTraverseGSuiteRequestBuilder(groupId);
    }
    private okhttp3.Call userGroupTraverseLdapServerCall(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/usergroups/{group_id}/ldapservers"
            .replace("{" + "group_id" + "}", localVarApiClient.escapeString(groupId.toString()));

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
    private okhttp3.Call userGroupTraverseLdapServerValidateBeforeCall(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'groupId' is set
        if (groupId == null) {
            throw new ApiException("Missing the required parameter 'groupId' when calling userGroupTraverseLdapServer(Async)");
        }

        return userGroupTraverseLdapServerCall(groupId, limit, xOrgId, skip, filter, _callback);

    }


    private ApiResponse<List<GraphObjectWithPaths>> userGroupTraverseLdapServerWithHttpInfo(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter) throws ApiException {
        okhttp3.Call localVarCall = userGroupTraverseLdapServerValidateBeforeCall(groupId, limit, xOrgId, skip, filter, null);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call userGroupTraverseLdapServerAsync(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {

        okhttp3.Call localVarCall = userGroupTraverseLdapServerValidateBeforeCall(groupId, limit, xOrgId, skip, filter, _callback);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UserGroupTraverseLdapServerRequestBuilder {
        private final String groupId;
        private Integer limit;
        private String xOrgId;
        private Integer skip;
        private List<String> filter;

        private UserGroupTraverseLdapServerRequestBuilder(String groupId) {
            this.groupId = groupId;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return UserGroupTraverseLdapServerRequestBuilder
         */
        public UserGroupTraverseLdapServerRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UserGroupTraverseLdapServerRequestBuilder
         */
        public UserGroupTraverseLdapServerRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return UserGroupTraverseLdapServerRequestBuilder
         */
        public UserGroupTraverseLdapServerRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return UserGroupTraverseLdapServerRequestBuilder
         */
        public UserGroupTraverseLdapServerRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Build call for userGroupTraverseLdapServer
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
            return userGroupTraverseLdapServerCall(groupId, limit, xOrgId, skip, filter, _callback);
        }


        /**
         * Execute userGroupTraverseLdapServer request
         * @return List&lt;GraphObjectWithPaths&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphObjectWithPaths> execute() throws ApiException {
            ApiResponse<List<GraphObjectWithPaths>> localVarResp = userGroupTraverseLdapServerWithHttpInfo(groupId, limit, xOrgId, skip, filter);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute userGroupTraverseLdapServer request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphObjectWithPaths&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphObjectWithPaths>> executeWithHttpInfo() throws ApiException {
            return userGroupTraverseLdapServerWithHttpInfo(groupId, limit, xOrgId, skip, filter);
        }

        /**
         * Execute userGroupTraverseLdapServer request (asynchronously)
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
            return userGroupTraverseLdapServerAsync(groupId, limit, xOrgId, skip, filter, _callback);
        }
    }

    /**
     * List the LDAP Servers bound to a User Group
     * This endpoint will return all LDAP Servers bound to a User Group, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User Group to the corresponding LDAP Server; this array represents all grouping and/or associations that would have to be removed to deprovision the LDAP Server from this User Group.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/usergroups/{GroupID}/ldapservers \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param groupId ObjectID of the User Group. (required)
     * @return UserGroupTraverseLdapServerRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UserGroupTraverseLdapServerRequestBuilder userGroupTraverseLdapServer(String groupId) throws IllegalArgumentException {
        if (groupId == null) throw new IllegalArgumentException("\"groupId\" is required but got null");
            

        return new UserGroupTraverseLdapServerRequestBuilder(groupId);
    }
    private okhttp3.Call userGroupTraverseOffice365Call(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/usergroups/{group_id}/office365s"
            .replace("{" + "group_id" + "}", localVarApiClient.escapeString(groupId.toString()));

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
    private okhttp3.Call userGroupTraverseOffice365ValidateBeforeCall(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'groupId' is set
        if (groupId == null) {
            throw new ApiException("Missing the required parameter 'groupId' when calling userGroupTraverseOffice365(Async)");
        }

        return userGroupTraverseOffice365Call(groupId, limit, xOrgId, skip, filter, _callback);

    }


    private ApiResponse<List<GraphObjectWithPaths>> userGroupTraverseOffice365WithHttpInfo(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter) throws ApiException {
        okhttp3.Call localVarCall = userGroupTraverseOffice365ValidateBeforeCall(groupId, limit, xOrgId, skip, filter, null);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call userGroupTraverseOffice365Async(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {

        okhttp3.Call localVarCall = userGroupTraverseOffice365ValidateBeforeCall(groupId, limit, xOrgId, skip, filter, _callback);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UserGroupTraverseOffice365RequestBuilder {
        private final String groupId;
        private Integer limit;
        private String xOrgId;
        private Integer skip;
        private List<String> filter;

        private UserGroupTraverseOffice365RequestBuilder(String groupId) {
            this.groupId = groupId;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return UserGroupTraverseOffice365RequestBuilder
         */
        public UserGroupTraverseOffice365RequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UserGroupTraverseOffice365RequestBuilder
         */
        public UserGroupTraverseOffice365RequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return UserGroupTraverseOffice365RequestBuilder
         */
        public UserGroupTraverseOffice365RequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return UserGroupTraverseOffice365RequestBuilder
         */
        public UserGroupTraverseOffice365RequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Build call for userGroupTraverseOffice365
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
            return userGroupTraverseOffice365Call(groupId, limit, xOrgId, skip, filter, _callback);
        }


        /**
         * Execute userGroupTraverseOffice365 request
         * @return List&lt;GraphObjectWithPaths&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphObjectWithPaths> execute() throws ApiException {
            ApiResponse<List<GraphObjectWithPaths>> localVarResp = userGroupTraverseOffice365WithHttpInfo(groupId, limit, xOrgId, skip, filter);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute userGroupTraverseOffice365 request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphObjectWithPaths&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphObjectWithPaths>> executeWithHttpInfo() throws ApiException {
            return userGroupTraverseOffice365WithHttpInfo(groupId, limit, xOrgId, skip, filter);
        }

        /**
         * Execute userGroupTraverseOffice365 request (asynchronously)
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
            return userGroupTraverseOffice365Async(groupId, limit, xOrgId, skip, filter, _callback);
        }
    }

    /**
     * List the Office 365 instances bound to a User Group
     * This endpoint will return all Office 365 instances bound to a User Group, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User Group to the corresponding Office 365 instance; this array represents all grouping and/or associations that would have to be removed to deprovision the Office 365 instance from this User Group.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/usergroups/{GroupID}/office365s \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param groupId ObjectID of the User Group. (required)
     * @return UserGroupTraverseOffice365RequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UserGroupTraverseOffice365RequestBuilder userGroupTraverseOffice365(String groupId) throws IllegalArgumentException {
        if (groupId == null) throw new IllegalArgumentException("\"groupId\" is required but got null");
            

        return new UserGroupTraverseOffice365RequestBuilder(groupId);
    }
    private okhttp3.Call userGroupTraverseRadiusServerCall(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/usergroups/{group_id}/radiusservers"
            .replace("{" + "group_id" + "}", localVarApiClient.escapeString(groupId.toString()));

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
    private okhttp3.Call userGroupTraverseRadiusServerValidateBeforeCall(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'groupId' is set
        if (groupId == null) {
            throw new ApiException("Missing the required parameter 'groupId' when calling userGroupTraverseRadiusServer(Async)");
        }

        return userGroupTraverseRadiusServerCall(groupId, limit, xOrgId, skip, filter, _callback);

    }


    private ApiResponse<List<GraphObjectWithPaths>> userGroupTraverseRadiusServerWithHttpInfo(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter) throws ApiException {
        okhttp3.Call localVarCall = userGroupTraverseRadiusServerValidateBeforeCall(groupId, limit, xOrgId, skip, filter, null);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call userGroupTraverseRadiusServerAsync(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {

        okhttp3.Call localVarCall = userGroupTraverseRadiusServerValidateBeforeCall(groupId, limit, xOrgId, skip, filter, _callback);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UserGroupTraverseRadiusServerRequestBuilder {
        private final String groupId;
        private Integer limit;
        private String xOrgId;
        private Integer skip;
        private List<String> filter;

        private UserGroupTraverseRadiusServerRequestBuilder(String groupId) {
            this.groupId = groupId;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return UserGroupTraverseRadiusServerRequestBuilder
         */
        public UserGroupTraverseRadiusServerRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UserGroupTraverseRadiusServerRequestBuilder
         */
        public UserGroupTraverseRadiusServerRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return UserGroupTraverseRadiusServerRequestBuilder
         */
        public UserGroupTraverseRadiusServerRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return UserGroupTraverseRadiusServerRequestBuilder
         */
        public UserGroupTraverseRadiusServerRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Build call for userGroupTraverseRadiusServer
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
            return userGroupTraverseRadiusServerCall(groupId, limit, xOrgId, skip, filter, _callback);
        }


        /**
         * Execute userGroupTraverseRadiusServer request
         * @return List&lt;GraphObjectWithPaths&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphObjectWithPaths> execute() throws ApiException {
            ApiResponse<List<GraphObjectWithPaths>> localVarResp = userGroupTraverseRadiusServerWithHttpInfo(groupId, limit, xOrgId, skip, filter);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute userGroupTraverseRadiusServer request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphObjectWithPaths&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphObjectWithPaths>> executeWithHttpInfo() throws ApiException {
            return userGroupTraverseRadiusServerWithHttpInfo(groupId, limit, xOrgId, skip, filter);
        }

        /**
         * Execute userGroupTraverseRadiusServer request (asynchronously)
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
            return userGroupTraverseRadiusServerAsync(groupId, limit, xOrgId, skip, filter, _callback);
        }
    }

    /**
     * List the RADIUS Servers bound to a User Group
     * This endpoint will return all RADIUS servers bound to a User Group, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User Group to the corresponding RADIUS Server; this array represents all grouping and/or associations that would have to be removed to deprovision the RADIUS Server from this User Group.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/usergroups/{GroupID}/radiusservers \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;  &#x60;&#x60;&#x60;
     * @param groupId ObjectID of the User Group. (required)
     * @return UserGroupTraverseRadiusServerRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UserGroupTraverseRadiusServerRequestBuilder userGroupTraverseRadiusServer(String groupId) throws IllegalArgumentException {
        if (groupId == null) throw new IllegalArgumentException("\"groupId\" is required but got null");
            

        return new UserGroupTraverseRadiusServerRequestBuilder(groupId);
    }
    private okhttp3.Call userGroupTraverseSystemCall(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/usergroups/{group_id}/systems"
            .replace("{" + "group_id" + "}", localVarApiClient.escapeString(groupId.toString()));

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
    private okhttp3.Call userGroupTraverseSystemValidateBeforeCall(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'groupId' is set
        if (groupId == null) {
            throw new ApiException("Missing the required parameter 'groupId' when calling userGroupTraverseSystem(Async)");
        }

        return userGroupTraverseSystemCall(groupId, limit, xOrgId, skip, filter, _callback);

    }


    private ApiResponse<List<GraphObjectWithPaths>> userGroupTraverseSystemWithHttpInfo(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter) throws ApiException {
        okhttp3.Call localVarCall = userGroupTraverseSystemValidateBeforeCall(groupId, limit, xOrgId, skip, filter, null);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call userGroupTraverseSystemAsync(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {

        okhttp3.Call localVarCall = userGroupTraverseSystemValidateBeforeCall(groupId, limit, xOrgId, skip, filter, _callback);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UserGroupTraverseSystemRequestBuilder {
        private final String groupId;
        private Integer limit;
        private String xOrgId;
        private Integer skip;
        private List<String> filter;

        private UserGroupTraverseSystemRequestBuilder(String groupId) {
            this.groupId = groupId;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return UserGroupTraverseSystemRequestBuilder
         */
        public UserGroupTraverseSystemRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UserGroupTraverseSystemRequestBuilder
         */
        public UserGroupTraverseSystemRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return UserGroupTraverseSystemRequestBuilder
         */
        public UserGroupTraverseSystemRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return UserGroupTraverseSystemRequestBuilder
         */
        public UserGroupTraverseSystemRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Build call for userGroupTraverseSystem
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
            return userGroupTraverseSystemCall(groupId, limit, xOrgId, skip, filter, _callback);
        }


        /**
         * Execute userGroupTraverseSystem request
         * @return List&lt;GraphObjectWithPaths&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphObjectWithPaths> execute() throws ApiException {
            ApiResponse<List<GraphObjectWithPaths>> localVarResp = userGroupTraverseSystemWithHttpInfo(groupId, limit, xOrgId, skip, filter);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute userGroupTraverseSystem request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphObjectWithPaths&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphObjectWithPaths>> executeWithHttpInfo() throws ApiException {
            return userGroupTraverseSystemWithHttpInfo(groupId, limit, xOrgId, skip, filter);
        }

        /**
         * Execute userGroupTraverseSystem request (asynchronously)
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
            return userGroupTraverseSystemAsync(groupId, limit, xOrgId, skip, filter, _callback);
        }
    }

    /**
     * List the Systems bound to a User Group
     * This endpoint will return all Systems bound to a User Group, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User Group to the corresponding System; this array represents all grouping and/or associations that would have to be removed to deprovision the System from this User Group.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/usergroups/{GroupID}/systems \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param groupId ObjectID of the User Group. (required)
     * @return UserGroupTraverseSystemRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UserGroupTraverseSystemRequestBuilder userGroupTraverseSystem(String groupId) throws IllegalArgumentException {
        if (groupId == null) throw new IllegalArgumentException("\"groupId\" is required but got null");
            

        return new UserGroupTraverseSystemRequestBuilder(groupId);
    }
    private okhttp3.Call userGroupTraverseSystemGroupCall(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/usergroups/{group_id}/systemgroups"
            .replace("{" + "group_id" + "}", localVarApiClient.escapeString(groupId.toString()));

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
    private okhttp3.Call userGroupTraverseSystemGroupValidateBeforeCall(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'groupId' is set
        if (groupId == null) {
            throw new ApiException("Missing the required parameter 'groupId' when calling userGroupTraverseSystemGroup(Async)");
        }

        return userGroupTraverseSystemGroupCall(groupId, limit, xOrgId, skip, filter, _callback);

    }


    private ApiResponse<List<GraphObjectWithPaths>> userGroupTraverseSystemGroupWithHttpInfo(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter) throws ApiException {
        okhttp3.Call localVarCall = userGroupTraverseSystemGroupValidateBeforeCall(groupId, limit, xOrgId, skip, filter, null);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call userGroupTraverseSystemGroupAsync(String groupId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {

        okhttp3.Call localVarCall = userGroupTraverseSystemGroupValidateBeforeCall(groupId, limit, xOrgId, skip, filter, _callback);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UserGroupTraverseSystemGroupRequestBuilder {
        private final String groupId;
        private Integer limit;
        private String xOrgId;
        private Integer skip;
        private List<String> filter;

        private UserGroupTraverseSystemGroupRequestBuilder(String groupId) {
            this.groupId = groupId;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return UserGroupTraverseSystemGroupRequestBuilder
         */
        public UserGroupTraverseSystemGroupRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UserGroupTraverseSystemGroupRequestBuilder
         */
        public UserGroupTraverseSystemGroupRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return UserGroupTraverseSystemGroupRequestBuilder
         */
        public UserGroupTraverseSystemGroupRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return UserGroupTraverseSystemGroupRequestBuilder
         */
        public UserGroupTraverseSystemGroupRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Build call for userGroupTraverseSystemGroup
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
            return userGroupTraverseSystemGroupCall(groupId, limit, xOrgId, skip, filter, _callback);
        }


        /**
         * Execute userGroupTraverseSystemGroup request
         * @return List&lt;GraphObjectWithPaths&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphObjectWithPaths> execute() throws ApiException {
            ApiResponse<List<GraphObjectWithPaths>> localVarResp = userGroupTraverseSystemGroupWithHttpInfo(groupId, limit, xOrgId, skip, filter);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute userGroupTraverseSystemGroup request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphObjectWithPaths&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphObjectWithPaths>> executeWithHttpInfo() throws ApiException {
            return userGroupTraverseSystemGroupWithHttpInfo(groupId, limit, xOrgId, skip, filter);
        }

        /**
         * Execute userGroupTraverseSystemGroup request (asynchronously)
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
            return userGroupTraverseSystemGroupAsync(groupId, limit, xOrgId, skip, filter, _callback);
        }
    }

    /**
     * List the System Groups bound to User Groups
     * This endpoint will return all System Groups bound to a User Group, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this User Group to the corresponding System Group; this array represents all grouping and/or associations that would have to be removed to deprovision the System Group from this User Group.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/usergroups/{GroupID}/systemgroups \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param groupId ObjectID of the User Group. (required)
     * @return UserGroupTraverseSystemGroupRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UserGroupTraverseSystemGroupRequestBuilder userGroupTraverseSystemGroup(String groupId) throws IllegalArgumentException {
        if (groupId == null) throw new IllegalArgumentException("\"groupId\" is required but got null");
            

        return new UserGroupTraverseSystemGroupRequestBuilder(groupId);
    }
}
