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


import com.konfigthis.client.model.BulkScheduledStatechangeCreate;
import com.konfigthis.client.model.BulkUserCreate;
import com.konfigthis.client.model.BulkUserExpire;
import com.konfigthis.client.model.BulkUserStatesGetNextScheduledResponse;
import com.konfigthis.client.model.BulkUserUnlock;
import com.konfigthis.client.model.BulkUserUpdate;
import com.konfigthis.client.model.JobId;
import com.konfigthis.client.model.JobWorkresult;
import java.time.OffsetDateTime;
import com.konfigthis.client.model.ScheduledUserstateResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;

public class BulkJobRequestsApiGenerated {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public BulkJobRequestsApiGenerated() throws IllegalArgumentException {
        this(Configuration.getDefaultApiClient());
    }

    public BulkJobRequestsApiGenerated(ApiClient apiClient) throws IllegalArgumentException {
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

    private okhttp3.Call userExpiresCall(String xOrgId, List<BulkUserExpire> bulkUserExpire, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = bulkUserExpire;

        // create path and map variables
        String localVarPath = "/bulk/user/expires";

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
    private okhttp3.Call userExpiresValidateBeforeCall(String xOrgId, List<BulkUserExpire> bulkUserExpire, final ApiCallback _callback) throws ApiException {
        return userExpiresCall(xOrgId, bulkUserExpire, _callback);

    }


    private ApiResponse<JobId> userExpiresWithHttpInfo(String xOrgId, List<BulkUserExpire> bulkUserExpire) throws ApiException {
        okhttp3.Call localVarCall = userExpiresValidateBeforeCall(xOrgId, bulkUserExpire, null);
        Type localVarReturnType = new TypeToken<JobId>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call userExpiresAsync(String xOrgId, List<BulkUserExpire> bulkUserExpire, final ApiCallback<JobId> _callback) throws ApiException {

        okhttp3.Call localVarCall = userExpiresValidateBeforeCall(xOrgId, bulkUserExpire, _callback);
        Type localVarReturnType = new TypeToken<JobId>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UserExpiresRequestBuilder {
        private String xOrgId;
        private List<BulkUserExpire> bulkUserExpire;

        private UserExpiresRequestBuilder() {
        }

        /**
         * Set bulkUserExpire
         * @param bulkUserExpire  (optional)
         * @return UserExpiresRequestBuilder
         */
        public UserExpiresRequestBuilder bulkUserExpire(List<BulkUserExpire> bulkUserExpire) {
            this.bulkUserExpire = bulkUserExpire;
            return this;
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UserExpiresRequestBuilder
         */
        public UserExpiresRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for userExpires
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
            List<BulkUserExpire> bulkUserExpire = buildBodyParams();
            return userExpiresCall(xOrgId, bulkUserExpire, _callback);
        }

        private List<BulkUserExpire> buildBodyParams() {
            return this.bulkUserExpire;
        }

        /**
         * Execute userExpires request
         * @return JobId
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
         </table>
         */
        public JobId execute() throws ApiException {
            List<BulkUserExpire> bulkUserExpire = buildBodyParams();
            ApiResponse<JobId> localVarResp = userExpiresWithHttpInfo(xOrgId, bulkUserExpire);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute userExpires request with HTTP info returned
         * @return ApiResponse&lt;JobId&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<JobId> executeWithHttpInfo() throws ApiException {
            List<BulkUserExpire> bulkUserExpire = buildBodyParams();
            return userExpiresWithHttpInfo(xOrgId, bulkUserExpire);
        }

        /**
         * Execute userExpires request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<JobId> _callback) throws ApiException {
            List<BulkUserExpire> bulkUserExpire = buildBodyParams();
            return userExpiresAsync(xOrgId, bulkUserExpire, _callback);
        }
    }

    /**
     * Bulk Expire Users
     * The endpoint allows you to start a bulk job to asynchronously expire users.
     * @return UserExpiresRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
     </table>
     */
    public UserExpiresRequestBuilder userExpires() throws IllegalArgumentException {
        return new UserExpiresRequestBuilder();
    }
    private okhttp3.Call userStatesCreateCall(String xOrgId, BulkScheduledStatechangeCreate bulkScheduledStatechangeCreate, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = bulkScheduledStatechangeCreate;

        // create path and map variables
        String localVarPath = "/bulk/userstates";

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
    private okhttp3.Call userStatesCreateValidateBeforeCall(String xOrgId, BulkScheduledStatechangeCreate bulkScheduledStatechangeCreate, final ApiCallback _callback) throws ApiException {
        return userStatesCreateCall(xOrgId, bulkScheduledStatechangeCreate, _callback);

    }


    private ApiResponse<List<ScheduledUserstateResult>> userStatesCreateWithHttpInfo(String xOrgId, BulkScheduledStatechangeCreate bulkScheduledStatechangeCreate) throws ApiException {
        okhttp3.Call localVarCall = userStatesCreateValidateBeforeCall(xOrgId, bulkScheduledStatechangeCreate, null);
        Type localVarReturnType = new TypeToken<List<ScheduledUserstateResult>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call userStatesCreateAsync(String xOrgId, BulkScheduledStatechangeCreate bulkScheduledStatechangeCreate, final ApiCallback<List<ScheduledUserstateResult>> _callback) throws ApiException {

        okhttp3.Call localVarCall = userStatesCreateValidateBeforeCall(xOrgId, bulkScheduledStatechangeCreate, _callback);
        Type localVarReturnType = new TypeToken<List<ScheduledUserstateResult>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UserStatesCreateRequestBuilder {
        private final OffsetDateTime startDate;
        private final String state;
        private final List<String> userIds;
        private String activationEmailOverride;
        private Boolean sendActivationEmails;
        private String xOrgId;

        private UserStatesCreateRequestBuilder(OffsetDateTime startDate, String state, List<String> userIds) {
            this.startDate = startDate;
            this.state = state;
            this.userIds = userIds;
        }

        /**
         * Set activationEmailOverride
         * @param activationEmailOverride Send the activation or welcome email to the specified email address upon activation. Can only be used with a single user_id and scheduled activation. This field will be ignored if &#x60;send_activation_emails&#x60; is explicitly set to false. (optional)
         * @return UserStatesCreateRequestBuilder
         */
        public UserStatesCreateRequestBuilder activationEmailOverride(String activationEmailOverride) {
            this.activationEmailOverride = activationEmailOverride;
            return this;
        }
        
        /**
         * Set sendActivationEmails
         * @param sendActivationEmails Set to true to send activation or welcome email(s) to each user_id upon activation. Set to false to suppress emails. Can only be used with scheduled activation(s). (optional)
         * @return UserStatesCreateRequestBuilder
         */
        public UserStatesCreateRequestBuilder sendActivationEmails(Boolean sendActivationEmails) {
            this.sendActivationEmails = sendActivationEmails;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UserStatesCreateRequestBuilder
         */
        public UserStatesCreateRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for userStatesCreate
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
            BulkScheduledStatechangeCreate bulkScheduledStatechangeCreate = buildBodyParams();
            return userStatesCreateCall(xOrgId, bulkScheduledStatechangeCreate, _callback);
        }

        private BulkScheduledStatechangeCreate buildBodyParams() {
            BulkScheduledStatechangeCreate bulkScheduledStatechangeCreate = new BulkScheduledStatechangeCreate();
            bulkScheduledStatechangeCreate.activationEmailOverride(this.activationEmailOverride);
            bulkScheduledStatechangeCreate.sendActivationEmails(this.sendActivationEmails);
            bulkScheduledStatechangeCreate.startDate(this.startDate);
            if (this.state != null)
            bulkScheduledStatechangeCreate.state(BulkScheduledStatechangeCreate.StateEnum.fromValue(this.state));
            bulkScheduledStatechangeCreate.userIds(this.userIds);
            return bulkScheduledStatechangeCreate;
        }

        /**
         * Execute userStatesCreate request
         * @return List&lt;ScheduledUserstateResult&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
         </table>
         */
        public List<ScheduledUserstateResult> execute() throws ApiException {
            BulkScheduledStatechangeCreate bulkScheduledStatechangeCreate = buildBodyParams();
            ApiResponse<List<ScheduledUserstateResult>> localVarResp = userStatesCreateWithHttpInfo(xOrgId, bulkScheduledStatechangeCreate);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute userStatesCreate request with HTTP info returned
         * @return ApiResponse&lt;List&lt;ScheduledUserstateResult&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<ScheduledUserstateResult>> executeWithHttpInfo() throws ApiException {
            BulkScheduledStatechangeCreate bulkScheduledStatechangeCreate = buildBodyParams();
            return userStatesCreateWithHttpInfo(xOrgId, bulkScheduledStatechangeCreate);
        }

        /**
         * Execute userStatesCreate request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<ScheduledUserstateResult>> _callback) throws ApiException {
            BulkScheduledStatechangeCreate bulkScheduledStatechangeCreate = buildBodyParams();
            return userStatesCreateAsync(xOrgId, bulkScheduledStatechangeCreate, _callback);
        }
    }

    /**
     * Create Scheduled Userstate Job
     * This endpoint allows you to create scheduled statechange jobs. #### Sample Request &#x60;&#x60;&#x60; curl -X POST \&quot;https://console.jumpcloud.com/api/v2/bulk/userstates\&quot; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;Accept: application/json&#39; \\   -d &#39;{     \&quot;user_ids\&quot;: [\&quot;{User_ID_1}\&quot;, \&quot;{User_ID_2}\&quot;, \&quot;{User_ID_3}\&quot;],     \&quot;state\&quot;: \&quot;SUSPENDED\&quot;,     \&quot;start_date\&quot;: \&quot;2000-01-01T00:00:00.000Z\&quot;   }&#39; &#x60;&#x60;&#x60;
     * @return UserStatesCreateRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
     </table>
     */
    public UserStatesCreateRequestBuilder userStatesCreate(OffsetDateTime startDate, String state, List<String> userIds) throws IllegalArgumentException {
        if (startDate == null) throw new IllegalArgumentException("\"startDate\" is required but got null");
        if (state == null) throw new IllegalArgumentException("\"state\" is required but got null");
            

        if (userIds == null) throw new IllegalArgumentException("\"userIds\" is required but got null");
        return new UserStatesCreateRequestBuilder(startDate, state, userIds);
    }
    private okhttp3.Call userStatesDeleteCall(String id, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/bulk/userstates/{id}"
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
    private okhttp3.Call userStatesDeleteValidateBeforeCall(String id, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling userStatesDelete(Async)");
        }

        return userStatesDeleteCall(id, xOrgId, _callback);

    }


    private ApiResponse<Void> userStatesDeleteWithHttpInfo(String id, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = userStatesDeleteValidateBeforeCall(id, xOrgId, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call userStatesDeleteAsync(String id, String xOrgId, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = userStatesDeleteValidateBeforeCall(id, xOrgId, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class UserStatesDeleteRequestBuilder {
        private final String id;
        private String xOrgId;

        private UserStatesDeleteRequestBuilder(String id) {
            this.id = id;
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UserStatesDeleteRequestBuilder
         */
        public UserStatesDeleteRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for userStatesDelete
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return userStatesDeleteCall(id, xOrgId, _callback);
        }


        /**
         * Execute userStatesDelete request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            userStatesDeleteWithHttpInfo(id, xOrgId);
        }

        /**
         * Execute userStatesDelete request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            return userStatesDeleteWithHttpInfo(id, xOrgId);
        }

        /**
         * Execute userStatesDelete request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Void> _callback) throws ApiException {
            return userStatesDeleteAsync(id, xOrgId, _callback);
        }
    }

    /**
     * Delete Scheduled Userstate Job
     * This endpoint deletes a scheduled statechange job. #### Sample Request &#x60;&#x60;&#x60; curl -X DELETE \&quot;https://console.jumpcloud.com/api/v2/bulk/userstates/{ScheduledJob_ID}\&quot; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;Accept: application/json&#39; &#x60;&#x60;&#x60;
     * @param id Unique identifier of the scheduled statechange job. (required)
     * @return UserStatesDeleteRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
     </table>
     */
    public UserStatesDeleteRequestBuilder userStatesDelete(String id) throws IllegalArgumentException {
        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new UserStatesDeleteRequestBuilder(id);
    }
    private okhttp3.Call userStatesGetNextScheduledCall(List<String> users, Integer limit, Integer skip, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/bulk/userstates/eventlist/next";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (users != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "users", users));
        }

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
    private okhttp3.Call userStatesGetNextScheduledValidateBeforeCall(List<String> users, Integer limit, Integer skip, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'users' is set
        if (users == null) {
            throw new ApiException("Missing the required parameter 'users' when calling userStatesGetNextScheduled(Async)");
        }

        return userStatesGetNextScheduledCall(users, limit, skip, _callback);

    }


    private ApiResponse<BulkUserStatesGetNextScheduledResponse> userStatesGetNextScheduledWithHttpInfo(List<String> users, Integer limit, Integer skip) throws ApiException {
        okhttp3.Call localVarCall = userStatesGetNextScheduledValidateBeforeCall(users, limit, skip, null);
        Type localVarReturnType = new TypeToken<BulkUserStatesGetNextScheduledResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call userStatesGetNextScheduledAsync(List<String> users, Integer limit, Integer skip, final ApiCallback<BulkUserStatesGetNextScheduledResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = userStatesGetNextScheduledValidateBeforeCall(users, limit, skip, _callback);
        Type localVarReturnType = new TypeToken<BulkUserStatesGetNextScheduledResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UserStatesGetNextScheduledRequestBuilder {
        private final List<String> users;
        private Integer limit;
        private Integer skip;

        private UserStatesGetNextScheduledRequestBuilder(List<String> users) {
            this.users = users;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return UserStatesGetNextScheduledRequestBuilder
         */
        public UserStatesGetNextScheduledRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return UserStatesGetNextScheduledRequestBuilder
         */
        public UserStatesGetNextScheduledRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Build call for userStatesGetNextScheduled
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
            return userStatesGetNextScheduledCall(users, limit, skip, _callback);
        }


        /**
         * Execute userStatesGetNextScheduled request
         * @return BulkUserStatesGetNextScheduledResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public BulkUserStatesGetNextScheduledResponse execute() throws ApiException {
            ApiResponse<BulkUserStatesGetNextScheduledResponse> localVarResp = userStatesGetNextScheduledWithHttpInfo(users, limit, skip);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute userStatesGetNextScheduled request with HTTP info returned
         * @return ApiResponse&lt;BulkUserStatesGetNextScheduledResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<BulkUserStatesGetNextScheduledResponse> executeWithHttpInfo() throws ApiException {
            return userStatesGetNextScheduledWithHttpInfo(users, limit, skip);
        }

        /**
         * Execute userStatesGetNextScheduled request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<BulkUserStatesGetNextScheduledResponse> _callback) throws ApiException {
            return userStatesGetNextScheduledAsync(users, limit, skip, _callback);
        }
    }

    /**
     * Get the next scheduled state change for a list of users
     * This endpoint is used to lookup the next upcoming scheduled state change for each user in the given list. The users parameter is limited to 100 items per request. The results are also limited to 100 items. This endpoint returns a max of 1 event per state per user. For example, if a user has 3 ACTIVATED events scheduled it will return the next upcoming activation event. However, if a user also has a SUSPENDED event scheduled along with the ACTIVATED events it will return the next upcoming activation event _and_ the next upcoming suspension event.
     * @param users A list of system user IDs, limited to 100 items. (required)
     * @return UserStatesGetNextScheduledRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UserStatesGetNextScheduledRequestBuilder userStatesGetNextScheduled(List<String> users) throws IllegalArgumentException {
        if (users == null) throw new IllegalArgumentException("\"users\" is required but got null");
        return new UserStatesGetNextScheduledRequestBuilder(users);
    }
    private okhttp3.Call userStatesListCall(Integer limit, List<String> filter, Integer skip, String xOrgId, String userid, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/bulk/userstates";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
        }

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (skip != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("skip", skip));
        }

        if (userid != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("userid", userid));
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
    private okhttp3.Call userStatesListValidateBeforeCall(Integer limit, List<String> filter, Integer skip, String xOrgId, String userid, final ApiCallback _callback) throws ApiException {
        return userStatesListCall(limit, filter, skip, xOrgId, userid, _callback);

    }


    private ApiResponse<List<ScheduledUserstateResult>> userStatesListWithHttpInfo(Integer limit, List<String> filter, Integer skip, String xOrgId, String userid) throws ApiException {
        okhttp3.Call localVarCall = userStatesListValidateBeforeCall(limit, filter, skip, xOrgId, userid, null);
        Type localVarReturnType = new TypeToken<List<ScheduledUserstateResult>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call userStatesListAsync(Integer limit, List<String> filter, Integer skip, String xOrgId, String userid, final ApiCallback<List<ScheduledUserstateResult>> _callback) throws ApiException {

        okhttp3.Call localVarCall = userStatesListValidateBeforeCall(limit, filter, skip, xOrgId, userid, _callback);
        Type localVarReturnType = new TypeToken<List<ScheduledUserstateResult>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UserStatesListRequestBuilder {
        private Integer limit;
        private List<String> filter;
        private Integer skip;
        private String xOrgId;
        private String userid;

        private UserStatesListRequestBuilder() {
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return UserStatesListRequestBuilder
         */
        public UserStatesListRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return UserStatesListRequestBuilder
         */
        public UserStatesListRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return UserStatesListRequestBuilder
         */
        public UserStatesListRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UserStatesListRequestBuilder
         */
        public UserStatesListRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set userid
         * @param userid The systemuser id to filter by. (optional)
         * @return UserStatesListRequestBuilder
         */
        public UserStatesListRequestBuilder userid(String userid) {
            this.userid = userid;
            return this;
        }
        
        /**
         * Build call for userStatesList
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
            return userStatesListCall(limit, filter, skip, xOrgId, userid, _callback);
        }


        /**
         * Execute userStatesList request
         * @return List&lt;ScheduledUserstateResult&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<ScheduledUserstateResult> execute() throws ApiException {
            ApiResponse<List<ScheduledUserstateResult>> localVarResp = userStatesListWithHttpInfo(limit, filter, skip, xOrgId, userid);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute userStatesList request with HTTP info returned
         * @return ApiResponse&lt;List&lt;ScheduledUserstateResult&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<ScheduledUserstateResult>> executeWithHttpInfo() throws ApiException {
            return userStatesListWithHttpInfo(limit, filter, skip, xOrgId, userid);
        }

        /**
         * Execute userStatesList request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<ScheduledUserstateResult>> _callback) throws ApiException {
            return userStatesListAsync(limit, filter, skip, xOrgId, userid, _callback);
        }
    }

    /**
     * List Scheduled Userstate Change Jobs
     * The endpoint allows you to list scheduled statechange jobs. #### Sample Request &#x60;&#x60;&#x60; curl -X GET \&quot;https://console.jumpcloud.com/api/v2/bulk/userstates\&quot; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;Accept: application/json&#39; &#x60;&#x60;&#x60;
     * @return UserStatesListRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UserStatesListRequestBuilder userStatesList() throws IllegalArgumentException {
        return new UserStatesListRequestBuilder();
    }
    private okhttp3.Call userUnlocksCall(String xOrgId, List<BulkUserUnlock> bulkUserUnlock, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = bulkUserUnlock;

        // create path and map variables
        String localVarPath = "/bulk/user/unlocks";

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
    private okhttp3.Call userUnlocksValidateBeforeCall(String xOrgId, List<BulkUserUnlock> bulkUserUnlock, final ApiCallback _callback) throws ApiException {
        return userUnlocksCall(xOrgId, bulkUserUnlock, _callback);

    }


    private ApiResponse<JobId> userUnlocksWithHttpInfo(String xOrgId, List<BulkUserUnlock> bulkUserUnlock) throws ApiException {
        okhttp3.Call localVarCall = userUnlocksValidateBeforeCall(xOrgId, bulkUserUnlock, null);
        Type localVarReturnType = new TypeToken<JobId>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call userUnlocksAsync(String xOrgId, List<BulkUserUnlock> bulkUserUnlock, final ApiCallback<JobId> _callback) throws ApiException {

        okhttp3.Call localVarCall = userUnlocksValidateBeforeCall(xOrgId, bulkUserUnlock, _callback);
        Type localVarReturnType = new TypeToken<JobId>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UserUnlocksRequestBuilder {
        private String xOrgId;
        private List<BulkUserUnlock> bulkUserUnlock;

        private UserUnlocksRequestBuilder() {
        }

        /**
         * Set bulkUserUnlock
         * @param bulkUserUnlock  (optional)
         * @return UserUnlocksRequestBuilder
         */
        public UserUnlocksRequestBuilder bulkUserUnlock(List<BulkUserUnlock> bulkUserUnlock) {
            this.bulkUserUnlock = bulkUserUnlock;
            return this;
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UserUnlocksRequestBuilder
         */
        public UserUnlocksRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for userUnlocks
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
            List<BulkUserUnlock> bulkUserUnlock = buildBodyParams();
            return userUnlocksCall(xOrgId, bulkUserUnlock, _callback);
        }

        private List<BulkUserUnlock> buildBodyParams() {
            return this.bulkUserUnlock;
        }

        /**
         * Execute userUnlocks request
         * @return JobId
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
         </table>
         */
        public JobId execute() throws ApiException {
            List<BulkUserUnlock> bulkUserUnlock = buildBodyParams();
            ApiResponse<JobId> localVarResp = userUnlocksWithHttpInfo(xOrgId, bulkUserUnlock);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute userUnlocks request with HTTP info returned
         * @return ApiResponse&lt;JobId&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<JobId> executeWithHttpInfo() throws ApiException {
            List<BulkUserUnlock> bulkUserUnlock = buildBodyParams();
            return userUnlocksWithHttpInfo(xOrgId, bulkUserUnlock);
        }

        /**
         * Execute userUnlocks request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<JobId> _callback) throws ApiException {
            List<BulkUserUnlock> bulkUserUnlock = buildBodyParams();
            return userUnlocksAsync(xOrgId, bulkUserUnlock, _callback);
        }
    }

    /**
     * Bulk Unlock Users
     * The endpoint allows you to start a bulk job to asynchronously unlock users.
     * @return UserUnlocksRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
     </table>
     */
    public UserUnlocksRequestBuilder userUnlocks() throws IllegalArgumentException {
        return new UserUnlocksRequestBuilder();
    }
    private okhttp3.Call usersCreateCall(String xOrgId, String creationSource, List<BulkUserCreate> bulkUserCreate, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = bulkUserCreate;

        // create path and map variables
        String localVarPath = "/bulk/users";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (xOrgId != null) {
            localVarHeaderParams.put("x-org-id", localVarApiClient.parameterToString(xOrgId));
        }

        if (creationSource != null) {
            localVarHeaderParams.put("creation-source", localVarApiClient.parameterToString(creationSource));
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
    private okhttp3.Call usersCreateValidateBeforeCall(String xOrgId, String creationSource, List<BulkUserCreate> bulkUserCreate, final ApiCallback _callback) throws ApiException {
        return usersCreateCall(xOrgId, creationSource, bulkUserCreate, _callback);

    }


    private ApiResponse<JobId> usersCreateWithHttpInfo(String xOrgId, String creationSource, List<BulkUserCreate> bulkUserCreate) throws ApiException {
        okhttp3.Call localVarCall = usersCreateValidateBeforeCall(xOrgId, creationSource, bulkUserCreate, null);
        Type localVarReturnType = new TypeToken<JobId>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call usersCreateAsync(String xOrgId, String creationSource, List<BulkUserCreate> bulkUserCreate, final ApiCallback<JobId> _callback) throws ApiException {

        okhttp3.Call localVarCall = usersCreateValidateBeforeCall(xOrgId, creationSource, bulkUserCreate, _callback);
        Type localVarReturnType = new TypeToken<JobId>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UsersCreateRequestBuilder {
        private String xOrgId;
        private String creationSource;
        private List<BulkUserCreate> bulkUserCreate;

        private UsersCreateRequestBuilder() {
        }

        /**
         * Set bulkUserCreate
         * @param bulkUserCreate  (optional)
         * @return UsersCreateRequestBuilder
         */
        public UsersCreateRequestBuilder bulkUserCreate(List<BulkUserCreate> bulkUserCreate) {
            this.bulkUserCreate = bulkUserCreate;
            return this;
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UsersCreateRequestBuilder
         */
        public UsersCreateRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set creationSource
         * @param creationSource Defines the creation-source header for gapps, o365 and workdays requests. If the header isn&#39;t sent, the default value is &#x60;jumpcloud:bulk&#x60;, if you send the header with a malformed value you receive a 400 error.  (optional, default to jumpcloud:bulk)
         * @return UsersCreateRequestBuilder
         */
        public UsersCreateRequestBuilder creationSource(String creationSource) {
            this.creationSource = creationSource;
            return this;
        }
        
        /**
         * Build call for usersCreate
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            List<BulkUserCreate> bulkUserCreate = buildBodyParams();
            return usersCreateCall(xOrgId, creationSource, bulkUserCreate, _callback);
        }

        private List<BulkUserCreate> buildBodyParams() {
            return this.bulkUserCreate;
        }

        /**
         * Execute usersCreate request
         * @return JobId
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public JobId execute() throws ApiException {
            List<BulkUserCreate> bulkUserCreate = buildBodyParams();
            ApiResponse<JobId> localVarResp = usersCreateWithHttpInfo(xOrgId, creationSource, bulkUserCreate);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute usersCreate request with HTTP info returned
         * @return ApiResponse&lt;JobId&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<JobId> executeWithHttpInfo() throws ApiException {
            List<BulkUserCreate> bulkUserCreate = buildBodyParams();
            return usersCreateWithHttpInfo(xOrgId, creationSource, bulkUserCreate);
        }

        /**
         * Execute usersCreate request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<JobId> _callback) throws ApiException {
            List<BulkUserCreate> bulkUserCreate = buildBodyParams();
            return usersCreateAsync(xOrgId, creationSource, bulkUserCreate, _callback);
        }
    }

    /**
     * Bulk Users Create
     * The endpoint allows you to create a bulk job to asynchronously create users. See [Create a System User](https://docs.jumpcloud.com/api/1.0/index.html#operation/systemusers_post) for the full list of attributes.  #### Default User State The &#x60;state&#x60; of each user in the request can be explicitly passed in or omitted. If &#x60;state&#x60; is omitted, then the user will get created using the value returned from the [Get an Organization](https://docs.jumpcloud.com/api/1.0/index.html#operation/organizations_get) endpoint. The default user state for bulk created users depends on the &#x60;creation-source&#x60; header. For &#x60;creation-source:jumpcloud:bulk&#x60; the default state is stored in &#x60;settings.newSystemUserStateDefaults.csvImport&#x60;. For other &#x60;creation-source&#x60; header values, the default state is stored in &#x60;settings.newSystemUserStateDefaults.applicationImport&#x60;  These default state values can be changed in the admin portal settings or by using the [Update an Organization](https://docs.jumpcloud.com/api/1.0/index.html#operation/organization_put) endpoint.  #### Sample Request  &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/bulk/users \\ -H &#39;Accept: application/json&#39; \\ -H &#39;Content-Type: application/json&#39; \\ -H &#39;x-api-key: {API_KEY}&#39; \\ -d &#39;[   {     \&quot;email\&quot;:\&quot;{email}\&quot;,     \&quot;firstname\&quot;:\&quot;{firstname}\&quot;,     \&quot;lastname\&quot;:\&quot;{firstname}\&quot;,     \&quot;username\&quot;:\&quot;{username}\&quot;,     \&quot;attributes\&quot;:[       {         \&quot;name\&quot;:\&quot;EmployeeID\&quot;,         \&quot;value\&quot;:\&quot;0000\&quot;       },       {         \&quot;name\&quot;:\&quot;Custom\&quot;,         \&quot;value\&quot;:\&quot;attribute\&quot;       }     ]   } ]&#39; &#x60;&#x60;&#x60;
     * @return UsersCreateRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public UsersCreateRequestBuilder usersCreate() throws IllegalArgumentException {
        return new UsersCreateRequestBuilder();
    }
    private okhttp3.Call usersCreateResultsCall(String jobId, Integer limit, Integer skip, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/bulk/users/{job_id}/results"
            .replace("{" + "job_id" + "}", localVarApiClient.escapeString(jobId.toString()));

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
    private okhttp3.Call usersCreateResultsValidateBeforeCall(String jobId, Integer limit, Integer skip, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'jobId' is set
        if (jobId == null) {
            throw new ApiException("Missing the required parameter 'jobId' when calling usersCreateResults(Async)");
        }

        return usersCreateResultsCall(jobId, limit, skip, xOrgId, _callback);

    }


    private ApiResponse<List<JobWorkresult>> usersCreateResultsWithHttpInfo(String jobId, Integer limit, Integer skip, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = usersCreateResultsValidateBeforeCall(jobId, limit, skip, xOrgId, null);
        Type localVarReturnType = new TypeToken<List<JobWorkresult>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call usersCreateResultsAsync(String jobId, Integer limit, Integer skip, String xOrgId, final ApiCallback<List<JobWorkresult>> _callback) throws ApiException {

        okhttp3.Call localVarCall = usersCreateResultsValidateBeforeCall(jobId, limit, skip, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<List<JobWorkresult>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UsersCreateResultsRequestBuilder {
        private final String jobId;
        private Integer limit;
        private Integer skip;
        private String xOrgId;

        private UsersCreateResultsRequestBuilder(String jobId) {
            this.jobId = jobId;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return UsersCreateResultsRequestBuilder
         */
        public UsersCreateResultsRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return UsersCreateResultsRequestBuilder
         */
        public UsersCreateResultsRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UsersCreateResultsRequestBuilder
         */
        public UsersCreateResultsRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for usersCreateResults
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
            return usersCreateResultsCall(jobId, limit, skip, xOrgId, _callback);
        }


        /**
         * Execute usersCreateResults request
         * @return List&lt;JobWorkresult&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<JobWorkresult> execute() throws ApiException {
            ApiResponse<List<JobWorkresult>> localVarResp = usersCreateResultsWithHttpInfo(jobId, limit, skip, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute usersCreateResults request with HTTP info returned
         * @return ApiResponse&lt;List&lt;JobWorkresult&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<JobWorkresult>> executeWithHttpInfo() throws ApiException {
            return usersCreateResultsWithHttpInfo(jobId, limit, skip, xOrgId);
        }

        /**
         * Execute usersCreateResults request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<JobWorkresult>> _callback) throws ApiException {
            return usersCreateResultsAsync(jobId, limit, skip, xOrgId, _callback);
        }
    }

    /**
     * List Bulk Users Results
     * This endpoint will return the results of particular user import or update job request.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET \\   https://console.jumpcloud.com/api/v2/bulk/users/{ImportJobID}/results \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;   &#x60;&#x60;&#x60;
     * @param jobId  (required)
     * @return UsersCreateResultsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public UsersCreateResultsRequestBuilder usersCreateResults(String jobId) throws IllegalArgumentException {
        if (jobId == null) throw new IllegalArgumentException("\"jobId\" is required but got null");
            

        return new UsersCreateResultsRequestBuilder(jobId);
    }
    private okhttp3.Call usersUpdateCall(String xOrgId, List<BulkUserUpdate> bulkUserUpdate, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = bulkUserUpdate;

        // create path and map variables
        String localVarPath = "/bulk/users";

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
    private okhttp3.Call usersUpdateValidateBeforeCall(String xOrgId, List<BulkUserUpdate> bulkUserUpdate, final ApiCallback _callback) throws ApiException {
        return usersUpdateCall(xOrgId, bulkUserUpdate, _callback);

    }


    private ApiResponse<JobId> usersUpdateWithHttpInfo(String xOrgId, List<BulkUserUpdate> bulkUserUpdate) throws ApiException {
        okhttp3.Call localVarCall = usersUpdateValidateBeforeCall(xOrgId, bulkUserUpdate, null);
        Type localVarReturnType = new TypeToken<JobId>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call usersUpdateAsync(String xOrgId, List<BulkUserUpdate> bulkUserUpdate, final ApiCallback<JobId> _callback) throws ApiException {

        okhttp3.Call localVarCall = usersUpdateValidateBeforeCall(xOrgId, bulkUserUpdate, _callback);
        Type localVarReturnType = new TypeToken<JobId>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UsersUpdateRequestBuilder {
        private String xOrgId;
        private List<BulkUserUpdate> bulkUserUpdate;

        private UsersUpdateRequestBuilder() {
        }

        /**
         * Set bulkUserUpdate
         * @param bulkUserUpdate  (optional)
         * @return UsersUpdateRequestBuilder
         */
        public UsersUpdateRequestBuilder bulkUserUpdate(List<BulkUserUpdate> bulkUserUpdate) {
            this.bulkUserUpdate = bulkUserUpdate;
            return this;
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UsersUpdateRequestBuilder
         */
        public UsersUpdateRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for usersUpdate
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            List<BulkUserUpdate> bulkUserUpdate = buildBodyParams();
            return usersUpdateCall(xOrgId, bulkUserUpdate, _callback);
        }

        private List<BulkUserUpdate> buildBodyParams() {
            return this.bulkUserUpdate;
        }

        /**
         * Execute usersUpdate request
         * @return JobId
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public JobId execute() throws ApiException {
            List<BulkUserUpdate> bulkUserUpdate = buildBodyParams();
            ApiResponse<JobId> localVarResp = usersUpdateWithHttpInfo(xOrgId, bulkUserUpdate);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute usersUpdate request with HTTP info returned
         * @return ApiResponse&lt;JobId&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<JobId> executeWithHttpInfo() throws ApiException {
            List<BulkUserUpdate> bulkUserUpdate = buildBodyParams();
            return usersUpdateWithHttpInfo(xOrgId, bulkUserUpdate);
        }

        /**
         * Execute usersUpdate request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<JobId> _callback) throws ApiException {
            List<BulkUserUpdate> bulkUserUpdate = buildBodyParams();
            return usersUpdateAsync(xOrgId, bulkUserUpdate, _callback);
        }
    }

    /**
     * Bulk Users Update
     * The endpoint allows you to create a bulk job to asynchronously update users. See [Update a System User](https://docs.jumpcloud.com/api/1.0/index.html#operation/systemusers_put) for full list of attributes.  #### Sample Request  &#x60;&#x60;&#x60; curl -X PATCH https://console.jumpcloud.com/api/v2/bulk/users \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;[  {    \&quot;id\&quot;:\&quot;5be9fb4ddb01290001e85109\&quot;,   \&quot;firstname\&quot;:\&quot;{UPDATED_FIRSTNAME}\&quot;,   \&quot;department\&quot;:\&quot;{UPDATED_DEPARTMENT}\&quot;,   \&quot;attributes\&quot;:[    {\&quot;name\&quot;:\&quot;Custom\&quot;,\&quot;value\&quot;:\&quot;{ATTRIBUTE_VALUE}\&quot;}   ]  },  {    \&quot;id\&quot;:\&quot;5be9fb4ddb01290001e85109\&quot;,   \&quot;firstname\&quot;:\&quot;{UPDATED_FIRSTNAME}\&quot;,   \&quot;costCenter\&quot;:\&quot;{UPDATED_COST_CENTER}\&quot;,   \&quot;phoneNumbers\&quot;:[    {\&quot;type\&quot;:\&quot;home\&quot;,\&quot;number\&quot;:\&quot;{HOME_PHONE_NUMBER}\&quot;},    {\&quot;type\&quot;:\&quot;work\&quot;,\&quot;number\&quot;:\&quot;{WORK_PHONE_NUMBER}\&quot;}   ]  } ] &#x60;&#x60;&#x60;
     * @return UsersUpdateRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public UsersUpdateRequestBuilder usersUpdate() throws IllegalArgumentException {
        return new UsersUpdateRequestBuilder();
    }
}
