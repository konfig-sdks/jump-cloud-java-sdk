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


import com.konfigthis.client.model.AccessRequestApiUpdateAccessRequestRequest;
import com.konfigthis.client.model.GoogleRpcStatus;
import com.konfigthis.client.model.JumpcloudIngressoCreateAccessRequestsRequest;
import com.konfigthis.client.model.JumpcloudIngressoCreateAccessRequestsResponse;
import com.konfigthis.client.model.JumpcloudIngressoGetAccessRequestResponse;
import java.time.OffsetDateTime;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;

public class IngressoApiGenerated {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public IngressoApiGenerated() throws IllegalArgumentException {
        this(Configuration.getDefaultApiClient());
    }

    public IngressoApiGenerated(ApiClient apiClient) throws IllegalArgumentException {
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

    private okhttp3.Call createAccessRequestCall(JumpcloudIngressoCreateAccessRequestsRequest jumpcloudIngressoCreateAccessRequestsRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = jumpcloudIngressoCreateAccessRequestsRequest;

        // create path and map variables
        String localVarPath = "/accessrequests";

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
    private okhttp3.Call createAccessRequestValidateBeforeCall(JumpcloudIngressoCreateAccessRequestsRequest jumpcloudIngressoCreateAccessRequestsRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'jumpcloudIngressoCreateAccessRequestsRequest' is set
        if (jumpcloudIngressoCreateAccessRequestsRequest == null) {
            throw new ApiException("Missing the required parameter 'jumpcloudIngressoCreateAccessRequestsRequest' when calling createAccessRequest(Async)");
        }

        return createAccessRequestCall(jumpcloudIngressoCreateAccessRequestsRequest, _callback);

    }


    private ApiResponse<JumpcloudIngressoCreateAccessRequestsResponse> createAccessRequestWithHttpInfo(JumpcloudIngressoCreateAccessRequestsRequest jumpcloudIngressoCreateAccessRequestsRequest) throws ApiException {
        okhttp3.Call localVarCall = createAccessRequestValidateBeforeCall(jumpcloudIngressoCreateAccessRequestsRequest, null);
        Type localVarReturnType = new TypeToken<JumpcloudIngressoCreateAccessRequestsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call createAccessRequestAsync(JumpcloudIngressoCreateAccessRequestsRequest jumpcloudIngressoCreateAccessRequestsRequest, final ApiCallback<JumpcloudIngressoCreateAccessRequestsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = createAccessRequestValidateBeforeCall(jumpcloudIngressoCreateAccessRequestsRequest, _callback);
        Type localVarReturnType = new TypeToken<JumpcloudIngressoCreateAccessRequestsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class CreateAccessRequestRequestBuilder {
        private String operationId;
        private Object additionalAttributes;
        private String applicationIntId;
        private OffsetDateTime expiry;
        private byte[] organizationObjectId;
        private String remarks;
        private String requestorId;
        private String resourceId;
        private String resourceType;

        private CreateAccessRequestRequestBuilder() {
        }

        /**
         * Set operationId
         * @param operationId  (optional)
         * @return CreateAccessRequestRequestBuilder
         */
        public CreateAccessRequestRequestBuilder operationId(String operationId) {
            this.operationId = operationId;
            return this;
        }
        
        /**
         * Set additionalAttributes
         * @param additionalAttributes  (optional)
         * @return CreateAccessRequestRequestBuilder
         */
        public CreateAccessRequestRequestBuilder additionalAttributes(Object additionalAttributes) {
            this.additionalAttributes = additionalAttributes;
            return this;
        }
        
        /**
         * Set applicationIntId
         * @param applicationIntId  (optional)
         * @return CreateAccessRequestRequestBuilder
         */
        public CreateAccessRequestRequestBuilder applicationIntId(String applicationIntId) {
            this.applicationIntId = applicationIntId;
            return this;
        }
        
        /**
         * Set expiry
         * @param expiry  (optional)
         * @return CreateAccessRequestRequestBuilder
         */
        public CreateAccessRequestRequestBuilder expiry(OffsetDateTime expiry) {
            this.expiry = expiry;
            return this;
        }
        
        /**
         * Set organizationObjectId
         * @param organizationObjectId  (optional)
         * @return CreateAccessRequestRequestBuilder
         */
        public CreateAccessRequestRequestBuilder organizationObjectId(byte[] organizationObjectId) {
            this.organizationObjectId = organizationObjectId;
            return this;
        }
        
        /**
         * Set remarks
         * @param remarks  (optional)
         * @return CreateAccessRequestRequestBuilder
         */
        public CreateAccessRequestRequestBuilder remarks(String remarks) {
            this.remarks = remarks;
            return this;
        }
        
        /**
         * Set requestorId
         * @param requestorId  (optional)
         * @return CreateAccessRequestRequestBuilder
         */
        public CreateAccessRequestRequestBuilder requestorId(String requestorId) {
            this.requestorId = requestorId;
            return this;
        }
        
        /**
         * Set resourceId
         * @param resourceId  (optional)
         * @return CreateAccessRequestRequestBuilder
         */
        public CreateAccessRequestRequestBuilder resourceId(String resourceId) {
            this.resourceId = resourceId;
            return this;
        }
        
        /**
         * Set resourceType
         * @param resourceType  (optional)
         * @return CreateAccessRequestRequestBuilder
         */
        public CreateAccessRequestRequestBuilder resourceType(String resourceType) {
            this.resourceType = resourceType;
            return this;
        }
        
        /**
         * Build call for createAccessRequest
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            JumpcloudIngressoCreateAccessRequestsRequest jumpcloudIngressoCreateAccessRequestsRequest = buildBodyParams();
            return createAccessRequestCall(jumpcloudIngressoCreateAccessRequestsRequest, _callback);
        }

        private JumpcloudIngressoCreateAccessRequestsRequest buildBodyParams() {
            JumpcloudIngressoCreateAccessRequestsRequest jumpcloudIngressoCreateAccessRequestsRequest = new JumpcloudIngressoCreateAccessRequestsRequest();
            jumpcloudIngressoCreateAccessRequestsRequest.operationId(this.operationId);
            jumpcloudIngressoCreateAccessRequestsRequest.additionalAttributes(this.additionalAttributes);
            jumpcloudIngressoCreateAccessRequestsRequest.applicationIntId(this.applicationIntId);
            jumpcloudIngressoCreateAccessRequestsRequest.expiry(this.expiry);
            jumpcloudIngressoCreateAccessRequestsRequest.organizationObjectId(this.organizationObjectId);
            jumpcloudIngressoCreateAccessRequestsRequest.remarks(this.remarks);
            jumpcloudIngressoCreateAccessRequestsRequest.requestorId(this.requestorId);
            jumpcloudIngressoCreateAccessRequestsRequest.resourceId(this.resourceId);
            jumpcloudIngressoCreateAccessRequestsRequest.resourceType(this.resourceType);
            return jumpcloudIngressoCreateAccessRequestsRequest;
        }

        /**
         * Execute createAccessRequest request
         * @return JumpcloudIngressoCreateAccessRequestsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public JumpcloudIngressoCreateAccessRequestsResponse execute() throws ApiException {
            JumpcloudIngressoCreateAccessRequestsRequest jumpcloudIngressoCreateAccessRequestsRequest = buildBodyParams();
            ApiResponse<JumpcloudIngressoCreateAccessRequestsResponse> localVarResp = createAccessRequestWithHttpInfo(jumpcloudIngressoCreateAccessRequestsRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute createAccessRequest request with HTTP info returned
         * @return ApiResponse&lt;JumpcloudIngressoCreateAccessRequestsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<JumpcloudIngressoCreateAccessRequestsResponse> executeWithHttpInfo() throws ApiException {
            JumpcloudIngressoCreateAccessRequestsRequest jumpcloudIngressoCreateAccessRequestsRequest = buildBodyParams();
            return createAccessRequestWithHttpInfo(jumpcloudIngressoCreateAccessRequestsRequest);
        }

        /**
         * Execute createAccessRequest request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<JumpcloudIngressoCreateAccessRequestsResponse> _callback) throws ApiException {
            JumpcloudIngressoCreateAccessRequestsRequest jumpcloudIngressoCreateAccessRequestsRequest = buildBodyParams();
            return createAccessRequestAsync(jumpcloudIngressoCreateAccessRequestsRequest, _callback);
        }
    }

    /**
     * Create Access Request
     * Endpoint for adding a new access request
     * @param jumpcloudIngressoCreateAccessRequestsRequest  (required)
     * @return CreateAccessRequestRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
        <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
     </table>
     */
    public CreateAccessRequestRequestBuilder createAccessRequest() throws IllegalArgumentException {
        return new CreateAccessRequestRequestBuilder();
    }
    private okhttp3.Call getAccessRequestCall(String accessId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/accessrequests/{accessId}"
            .replace("{" + "accessId" + "}", localVarApiClient.escapeString(accessId.toString()));

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
    private okhttp3.Call getAccessRequestValidateBeforeCall(String accessId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'accessId' is set
        if (accessId == null) {
            throw new ApiException("Missing the required parameter 'accessId' when calling getAccessRequest(Async)");
        }

        return getAccessRequestCall(accessId, _callback);

    }


    private ApiResponse<JumpcloudIngressoGetAccessRequestResponse> getAccessRequestWithHttpInfo(String accessId) throws ApiException {
        okhttp3.Call localVarCall = getAccessRequestValidateBeforeCall(accessId, null);
        Type localVarReturnType = new TypeToken<JumpcloudIngressoGetAccessRequestResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getAccessRequestAsync(String accessId, final ApiCallback<JumpcloudIngressoGetAccessRequestResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getAccessRequestValidateBeforeCall(accessId, _callback);
        Type localVarReturnType = new TypeToken<JumpcloudIngressoGetAccessRequestResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetAccessRequestRequestBuilder {
        private final String accessId;

        private GetAccessRequestRequestBuilder(String accessId) {
            this.accessId = accessId;
        }

        /**
         * Build call for getAccessRequest
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getAccessRequestCall(accessId, _callback);
        }


        /**
         * Execute getAccessRequest request
         * @return JumpcloudIngressoGetAccessRequestResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public JumpcloudIngressoGetAccessRequestResponse execute() throws ApiException {
            ApiResponse<JumpcloudIngressoGetAccessRequestResponse> localVarResp = getAccessRequestWithHttpInfo(accessId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getAccessRequest request with HTTP info returned
         * @return ApiResponse&lt;JumpcloudIngressoGetAccessRequestResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<JumpcloudIngressoGetAccessRequestResponse> executeWithHttpInfo() throws ApiException {
            return getAccessRequestWithHttpInfo(accessId);
        }

        /**
         * Execute getAccessRequest request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<JumpcloudIngressoGetAccessRequestResponse> _callback) throws ApiException {
            return getAccessRequestAsync(accessId, _callback);
        }
    }

    /**
     * Get all Access Requests by Access Id
     * Endpoint for getting all access requests by access id
     * @param accessId  (required)
     * @return GetAccessRequestRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
        <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
     </table>
     */
    public GetAccessRequestRequestBuilder getAccessRequest(String accessId) throws IllegalArgumentException {
        if (accessId == null) throw new IllegalArgumentException("\"accessId\" is required but got null");
            

        return new GetAccessRequestRequestBuilder(accessId);
    }
    private okhttp3.Call revokeAccessRequestCall(String accessId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/accessrequests/{accessId}/revoke"
            .replace("{" + "accessId" + "}", localVarApiClient.escapeString(accessId.toString()));

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
    private okhttp3.Call revokeAccessRequestValidateBeforeCall(String accessId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'accessId' is set
        if (accessId == null) {
            throw new ApiException("Missing the required parameter 'accessId' when calling revokeAccessRequest(Async)");
        }

        return revokeAccessRequestCall(accessId, _callback);

    }


    private ApiResponse<Object> revokeAccessRequestWithHttpInfo(String accessId) throws ApiException {
        okhttp3.Call localVarCall = revokeAccessRequestValidateBeforeCall(accessId, null);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call revokeAccessRequestAsync(String accessId, final ApiCallback<Object> _callback) throws ApiException {

        okhttp3.Call localVarCall = revokeAccessRequestValidateBeforeCall(accessId, _callback);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class RevokeAccessRequestRequestBuilder {
        private final String accessId;

        private RevokeAccessRequestRequestBuilder(String accessId) {
            this.accessId = accessId;
        }

        /**
         * Build call for revokeAccessRequest
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return revokeAccessRequestCall(accessId, _callback);
        }


        /**
         * Execute revokeAccessRequest request
         * @return Object
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public Object execute() throws ApiException {
            ApiResponse<Object> localVarResp = revokeAccessRequestWithHttpInfo(accessId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute revokeAccessRequest request with HTTP info returned
         * @return ApiResponse&lt;Object&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Object> executeWithHttpInfo() throws ApiException {
            return revokeAccessRequestWithHttpInfo(accessId);
        }

        /**
         * Execute revokeAccessRequest request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Object> _callback) throws ApiException {
            return revokeAccessRequestAsync(accessId, _callback);
        }
    }

    /**
     * Revoke access request by id
     * Endpoint for revoking access request by id
     * @param accessId  (required)
     * @return RevokeAccessRequestRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
        <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
     </table>
     */
    public RevokeAccessRequestRequestBuilder revokeAccessRequest(String accessId) throws IllegalArgumentException {
        if (accessId == null) throw new IllegalArgumentException("\"accessId\" is required but got null");
            

        return new RevokeAccessRequestRequestBuilder(accessId);
    }
    private okhttp3.Call updateAccessRequestCall(String accessId, AccessRequestApiUpdateAccessRequestRequest accessRequestApiUpdateAccessRequestRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = accessRequestApiUpdateAccessRequestRequest;

        // create path and map variables
        String localVarPath = "/accessrequests/{accessId}"
            .replace("{" + "accessId" + "}", localVarApiClient.escapeString(accessId.toString()));

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
    private okhttp3.Call updateAccessRequestValidateBeforeCall(String accessId, AccessRequestApiUpdateAccessRequestRequest accessRequestApiUpdateAccessRequestRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'accessId' is set
        if (accessId == null) {
            throw new ApiException("Missing the required parameter 'accessId' when calling updateAccessRequest(Async)");
        }

        // verify the required parameter 'accessRequestApiUpdateAccessRequestRequest' is set
        if (accessRequestApiUpdateAccessRequestRequest == null) {
            throw new ApiException("Missing the required parameter 'accessRequestApiUpdateAccessRequestRequest' when calling updateAccessRequest(Async)");
        }

        return updateAccessRequestCall(accessId, accessRequestApiUpdateAccessRequestRequest, _callback);

    }


    private ApiResponse<Object> updateAccessRequestWithHttpInfo(String accessId, AccessRequestApiUpdateAccessRequestRequest accessRequestApiUpdateAccessRequestRequest) throws ApiException {
        okhttp3.Call localVarCall = updateAccessRequestValidateBeforeCall(accessId, accessRequestApiUpdateAccessRequestRequest, null);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call updateAccessRequestAsync(String accessId, AccessRequestApiUpdateAccessRequestRequest accessRequestApiUpdateAccessRequestRequest, final ApiCallback<Object> _callback) throws ApiException {

        okhttp3.Call localVarCall = updateAccessRequestValidateBeforeCall(accessId, accessRequestApiUpdateAccessRequestRequest, _callback);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UpdateAccessRequestRequestBuilder {
        private final String accessId;
        private Object additionalAttributes;
        private OffsetDateTime expiry;
        private byte[] organizationObjectId;
        private String remarks;

        private UpdateAccessRequestRequestBuilder(String accessId) {
            this.accessId = accessId;
        }

        /**
         * Set additionalAttributes
         * @param additionalAttributes  (optional)
         * @return UpdateAccessRequestRequestBuilder
         */
        public UpdateAccessRequestRequestBuilder additionalAttributes(Object additionalAttributes) {
            this.additionalAttributes = additionalAttributes;
            return this;
        }
        
        /**
         * Set expiry
         * @param expiry  (optional)
         * @return UpdateAccessRequestRequestBuilder
         */
        public UpdateAccessRequestRequestBuilder expiry(OffsetDateTime expiry) {
            this.expiry = expiry;
            return this;
        }
        
        /**
         * Set organizationObjectId
         * @param organizationObjectId  (optional)
         * @return UpdateAccessRequestRequestBuilder
         */
        public UpdateAccessRequestRequestBuilder organizationObjectId(byte[] organizationObjectId) {
            this.organizationObjectId = organizationObjectId;
            return this;
        }
        
        /**
         * Set remarks
         * @param remarks  (optional)
         * @return UpdateAccessRequestRequestBuilder
         */
        public UpdateAccessRequestRequestBuilder remarks(String remarks) {
            this.remarks = remarks;
            return this;
        }
        
        /**
         * Build call for updateAccessRequest
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            AccessRequestApiUpdateAccessRequestRequest accessRequestApiUpdateAccessRequestRequest = buildBodyParams();
            return updateAccessRequestCall(accessId, accessRequestApiUpdateAccessRequestRequest, _callback);
        }

        private AccessRequestApiUpdateAccessRequestRequest buildBodyParams() {
            AccessRequestApiUpdateAccessRequestRequest accessRequestApiUpdateAccessRequestRequest = new AccessRequestApiUpdateAccessRequestRequest();
            accessRequestApiUpdateAccessRequestRequest.additionalAttributes(this.additionalAttributes);
            accessRequestApiUpdateAccessRequestRequest.expiry(this.expiry);
            accessRequestApiUpdateAccessRequestRequest.organizationObjectId(this.organizationObjectId);
            accessRequestApiUpdateAccessRequestRequest.remarks(this.remarks);
            return accessRequestApiUpdateAccessRequestRequest;
        }

        /**
         * Execute updateAccessRequest request
         * @return Object
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public Object execute() throws ApiException {
            AccessRequestApiUpdateAccessRequestRequest accessRequestApiUpdateAccessRequestRequest = buildBodyParams();
            ApiResponse<Object> localVarResp = updateAccessRequestWithHttpInfo(accessId, accessRequestApiUpdateAccessRequestRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute updateAccessRequest request with HTTP info returned
         * @return ApiResponse&lt;Object&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Object> executeWithHttpInfo() throws ApiException {
            AccessRequestApiUpdateAccessRequestRequest accessRequestApiUpdateAccessRequestRequest = buildBodyParams();
            return updateAccessRequestWithHttpInfo(accessId, accessRequestApiUpdateAccessRequestRequest);
        }

        /**
         * Execute updateAccessRequest request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Object> _callback) throws ApiException {
            AccessRequestApiUpdateAccessRequestRequest accessRequestApiUpdateAccessRequestRequest = buildBodyParams();
            return updateAccessRequestAsync(accessId, accessRequestApiUpdateAccessRequestRequest, _callback);
        }
    }

    /**
     * Update access request by id
     * Endpoint for updating access request by id
     * @param accessId  (required)
     * @param accessRequestApiUpdateAccessRequestRequest  (required)
     * @return UpdateAccessRequestRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
        <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
     </table>
     */
    public UpdateAccessRequestRequestBuilder updateAccessRequest(String accessId) throws IllegalArgumentException {
        if (accessId == null) throw new IllegalArgumentException("\"accessId\" is required but got null");
            

        return new UpdateAccessRequestRequestBuilder(accessId);
    }
}
