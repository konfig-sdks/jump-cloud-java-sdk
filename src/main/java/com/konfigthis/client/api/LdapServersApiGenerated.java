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


import com.konfigthis.client.model.GraphConnection;
import com.konfigthis.client.model.GraphObjectWithPaths;
import com.konfigthis.client.model.GraphOperationLdapServer;
import com.konfigthis.client.model.LdapServer;
import com.konfigthis.client.model.LdapServerAction;
import com.konfigthis.client.model.LdapserversPatchRequest;
import com.konfigthis.client.model.LdapserversPatchResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;

public class LdapServersApiGenerated {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public LdapServersApiGenerated() throws IllegalArgumentException {
        this(Configuration.getDefaultApiClient());
    }

    public LdapServersApiGenerated(ApiClient apiClient) throws IllegalArgumentException {
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

    private okhttp3.Call getCall(String id, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/ldapservers/{id}"
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
    private okhttp3.Call getValidateBeforeCall(String id, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling get(Async)");
        }

        return getCall(id, xOrgId, _callback);

    }


    private ApiResponse<LdapServer> getWithHttpInfo(String id, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = getValidateBeforeCall(id, xOrgId, null);
        Type localVarReturnType = new TypeToken<LdapServer>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getAsync(String id, String xOrgId, final ApiCallback<LdapServer> _callback) throws ApiException {

        okhttp3.Call localVarCall = getValidateBeforeCall(id, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<LdapServer>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetRequestBuilder {
        private final String id;
        private String xOrgId;

        private GetRequestBuilder(String id) {
            this.id = id;
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
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getCall(id, xOrgId, _callback);
        }


        /**
         * Execute get request
         * @return LdapServer
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public LdapServer execute() throws ApiException {
            ApiResponse<LdapServer> localVarResp = getWithHttpInfo(id, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute get request with HTTP info returned
         * @return ApiResponse&lt;LdapServer&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<LdapServer> executeWithHttpInfo() throws ApiException {
            return getWithHttpInfo(id, xOrgId);
        }

        /**
         * Execute get request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<LdapServer> _callback) throws ApiException {
            return getAsync(id, xOrgId, _callback);
        }
    }

    /**
     * Get LDAP Server
     * This endpoint returns a specific LDAP server.  ##### Sample Request  &#x60;&#x60;&#x60;  curl -X GET https://console.jumpcloud.com/api/v2/ldapservers/{LDAP_ID} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param id Unique identifier of the LDAP server. (required)
     * @return GetRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public GetRequestBuilder get(String id) throws IllegalArgumentException {
        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new GetRequestBuilder(id);
    }
    private okhttp3.Call ldapServerAssociationsListCall(String ldapserverId, List<String> targets, Integer limit, Integer skip, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/ldapservers/{ldapserver_id}/associations"
            .replace("{" + "ldapserver_id" + "}", localVarApiClient.escapeString(ldapserverId.toString()));

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
    private okhttp3.Call ldapServerAssociationsListValidateBeforeCall(String ldapserverId, List<String> targets, Integer limit, Integer skip, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'ldapserverId' is set
        if (ldapserverId == null) {
            throw new ApiException("Missing the required parameter 'ldapserverId' when calling ldapServerAssociationsList(Async)");
        }

        // verify the required parameter 'targets' is set
        if (targets == null) {
            throw new ApiException("Missing the required parameter 'targets' when calling ldapServerAssociationsList(Async)");
        }

        return ldapServerAssociationsListCall(ldapserverId, targets, limit, skip, xOrgId, _callback);

    }


    private ApiResponse<List<GraphConnection>> ldapServerAssociationsListWithHttpInfo(String ldapserverId, List<String> targets, Integer limit, Integer skip, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = ldapServerAssociationsListValidateBeforeCall(ldapserverId, targets, limit, skip, xOrgId, null);
        Type localVarReturnType = new TypeToken<List<GraphConnection>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call ldapServerAssociationsListAsync(String ldapserverId, List<String> targets, Integer limit, Integer skip, String xOrgId, final ApiCallback<List<GraphConnection>> _callback) throws ApiException {

        okhttp3.Call localVarCall = ldapServerAssociationsListValidateBeforeCall(ldapserverId, targets, limit, skip, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<List<GraphConnection>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class LdapServerAssociationsListRequestBuilder {
        private final String ldapserverId;
        private final List<String> targets;
        private Integer limit;
        private Integer skip;
        private String xOrgId;

        private LdapServerAssociationsListRequestBuilder(String ldapserverId, List<String> targets) {
            this.ldapserverId = ldapserverId;
            this.targets = targets;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return LdapServerAssociationsListRequestBuilder
         */
        public LdapServerAssociationsListRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return LdapServerAssociationsListRequestBuilder
         */
        public LdapServerAssociationsListRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return LdapServerAssociationsListRequestBuilder
         */
        public LdapServerAssociationsListRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for ldapServerAssociationsList
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
            return ldapServerAssociationsListCall(ldapserverId, targets, limit, skip, xOrgId, _callback);
        }


        /**
         * Execute ldapServerAssociationsList request
         * @return List&lt;GraphConnection&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphConnection> execute() throws ApiException {
            ApiResponse<List<GraphConnection>> localVarResp = ldapServerAssociationsListWithHttpInfo(ldapserverId, targets, limit, skip, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute ldapServerAssociationsList request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphConnection&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphConnection>> executeWithHttpInfo() throws ApiException {
            return ldapServerAssociationsListWithHttpInfo(ldapserverId, targets, limit, skip, xOrgId);
        }

        /**
         * Execute ldapServerAssociationsList request (asynchronously)
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
            return ldapServerAssociationsListAsync(ldapserverId, targets, limit, skip, xOrgId, _callback);
        }
    }

    /**
     * List the associations of a LDAP Server
     * This endpoint returns the _direct_ associations of this LDAP Server.  A direct association can be a non-homogeneous relationship between 2 different objects, for example LDAP and Users.  #### Sample Request  &#x60;&#x60;&#x60;  curl -X GET &#39;https://console.jumpcloud.com/api/v2/ldapservers/{LDAP_ID}/associations?targets&#x3D;user_group \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param ldapserverId ObjectID of the LDAP Server. (required)
     * @param targets Targets which a \&quot;ldap_server\&quot; can be associated to. (required)
     * @return LdapServerAssociationsListRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public LdapServerAssociationsListRequestBuilder ldapServerAssociationsList(String ldapserverId, List<String> targets) throws IllegalArgumentException {
        if (ldapserverId == null) throw new IllegalArgumentException("\"ldapserverId\" is required but got null");
            

        if (targets == null) throw new IllegalArgumentException("\"targets\" is required but got null");
        return new LdapServerAssociationsListRequestBuilder(ldapserverId, targets);
    }
    private okhttp3.Call ldapServerAssociationsPostCall(String ldapserverId, String xOrgId, GraphOperationLdapServer graphOperationLdapServer, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = graphOperationLdapServer;

        // create path and map variables
        String localVarPath = "/ldapservers/{ldapserver_id}/associations"
            .replace("{" + "ldapserver_id" + "}", localVarApiClient.escapeString(ldapserverId.toString()));

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
    private okhttp3.Call ldapServerAssociationsPostValidateBeforeCall(String ldapserverId, String xOrgId, GraphOperationLdapServer graphOperationLdapServer, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'ldapserverId' is set
        if (ldapserverId == null) {
            throw new ApiException("Missing the required parameter 'ldapserverId' when calling ldapServerAssociationsPost(Async)");
        }

        return ldapServerAssociationsPostCall(ldapserverId, xOrgId, graphOperationLdapServer, _callback);

    }


    private ApiResponse<Void> ldapServerAssociationsPostWithHttpInfo(String ldapserverId, String xOrgId, GraphOperationLdapServer graphOperationLdapServer) throws ApiException {
        okhttp3.Call localVarCall = ldapServerAssociationsPostValidateBeforeCall(ldapserverId, xOrgId, graphOperationLdapServer, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call ldapServerAssociationsPostAsync(String ldapserverId, String xOrgId, GraphOperationLdapServer graphOperationLdapServer, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = ldapServerAssociationsPostValidateBeforeCall(ldapserverId, xOrgId, graphOperationLdapServer, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class LdapServerAssociationsPostRequestBuilder {
        private final String ldapserverId;
        private String id;
        private String op;
        private Map<String, Object> attributes;
        private String type;
        private String xOrgId;

        private LdapServerAssociationsPostRequestBuilder(String ldapserverId) {
            this.ldapserverId = ldapserverId;
        }

        /**
         * Set id
         * @param id The ObjectID of graph object being added or removed as an association. (optional)
         * @return LdapServerAssociationsPostRequestBuilder
         */
        public LdapServerAssociationsPostRequestBuilder id(String id) {
            this.id = id;
            return this;
        }
        
        /**
         * Set op
         * @param op How to modify the graph connection. (optional)
         * @return LdapServerAssociationsPostRequestBuilder
         */
        public LdapServerAssociationsPostRequestBuilder op(String op) {
            this.op = op;
            return this;
        }
        
        /**
         * Set attributes
         * @param attributes The graph attributes. (optional)
         * @return LdapServerAssociationsPostRequestBuilder
         */
        public LdapServerAssociationsPostRequestBuilder attributes(Map<String, Object> attributes) {
            this.attributes = attributes;
            return this;
        }
        
        /**
         * Set type
         * @param type Targets which a \\\&quot;ldap_server\\\&quot; can be associated to. (optional)
         * @return LdapServerAssociationsPostRequestBuilder
         */
        public LdapServerAssociationsPostRequestBuilder type(String type) {
            this.type = type;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return LdapServerAssociationsPostRequestBuilder
         */
        public LdapServerAssociationsPostRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for ldapServerAssociationsPost
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
            GraphOperationLdapServer graphOperationLdapServer = buildBodyParams();
            return ldapServerAssociationsPostCall(ldapserverId, xOrgId, graphOperationLdapServer, _callback);
        }

        private GraphOperationLdapServer buildBodyParams() {
            GraphOperationLdapServer graphOperationLdapServer = new GraphOperationLdapServer();
            return graphOperationLdapServer;
        }

        /**
         * Execute ldapServerAssociationsPost request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            GraphOperationLdapServer graphOperationLdapServer = buildBodyParams();
            ldapServerAssociationsPostWithHttpInfo(ldapserverId, xOrgId, graphOperationLdapServer);
        }

        /**
         * Execute ldapServerAssociationsPost request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            GraphOperationLdapServer graphOperationLdapServer = buildBodyParams();
            return ldapServerAssociationsPostWithHttpInfo(ldapserverId, xOrgId, graphOperationLdapServer);
        }

        /**
         * Execute ldapServerAssociationsPost request (asynchronously)
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
            GraphOperationLdapServer graphOperationLdapServer = buildBodyParams();
            return ldapServerAssociationsPostAsync(ldapserverId, xOrgId, graphOperationLdapServer, _callback);
        }
    }

    /**
     * Manage the associations of a LDAP Server
     * This endpoint allows you to manage the _direct_ associations of a LDAP Server.  A direct association can be a non-homogeneous relationship between 2 different objects, for example LDAP and Users.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/ldapservers/{LDAP_ID}/associations \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;op\&quot;: \&quot;add\&quot;,     \&quot;type\&quot;: \&quot;user\&quot;,     \&quot;id\&quot;: \&quot;{User_ID}\&quot;   }&#39; &#x60;&#x60;&#x60;
     * @param ldapserverId ObjectID of the LDAP Server. (required)
     * @return LdapServerAssociationsPostRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public LdapServerAssociationsPostRequestBuilder ldapServerAssociationsPost(String ldapserverId) throws IllegalArgumentException {
        if (ldapserverId == null) throw new IllegalArgumentException("\"ldapserverId\" is required but got null");
            

        return new LdapServerAssociationsPostRequestBuilder(ldapserverId);
    }
    private okhttp3.Call ldapServerTraverseUserCall(String ldapserverId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/ldapservers/{ldapserver_id}/users"
            .replace("{" + "ldapserver_id" + "}", localVarApiClient.escapeString(ldapserverId.toString()));

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
    private okhttp3.Call ldapServerTraverseUserValidateBeforeCall(String ldapserverId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'ldapserverId' is set
        if (ldapserverId == null) {
            throw new ApiException("Missing the required parameter 'ldapserverId' when calling ldapServerTraverseUser(Async)");
        }

        return ldapServerTraverseUserCall(ldapserverId, limit, xOrgId, skip, filter, _callback);

    }


    private ApiResponse<List<GraphObjectWithPaths>> ldapServerTraverseUserWithHttpInfo(String ldapserverId, Integer limit, String xOrgId, Integer skip, List<String> filter) throws ApiException {
        okhttp3.Call localVarCall = ldapServerTraverseUserValidateBeforeCall(ldapserverId, limit, xOrgId, skip, filter, null);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call ldapServerTraverseUserAsync(String ldapserverId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {

        okhttp3.Call localVarCall = ldapServerTraverseUserValidateBeforeCall(ldapserverId, limit, xOrgId, skip, filter, _callback);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class LdapServerTraverseUserRequestBuilder {
        private final String ldapserverId;
        private Integer limit;
        private String xOrgId;
        private Integer skip;
        private List<String> filter;

        private LdapServerTraverseUserRequestBuilder(String ldapserverId) {
            this.ldapserverId = ldapserverId;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return LdapServerTraverseUserRequestBuilder
         */
        public LdapServerTraverseUserRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return LdapServerTraverseUserRequestBuilder
         */
        public LdapServerTraverseUserRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return LdapServerTraverseUserRequestBuilder
         */
        public LdapServerTraverseUserRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return LdapServerTraverseUserRequestBuilder
         */
        public LdapServerTraverseUserRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Build call for ldapServerTraverseUser
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
            return ldapServerTraverseUserCall(ldapserverId, limit, xOrgId, skip, filter, _callback);
        }


        /**
         * Execute ldapServerTraverseUser request
         * @return List&lt;GraphObjectWithPaths&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphObjectWithPaths> execute() throws ApiException {
            ApiResponse<List<GraphObjectWithPaths>> localVarResp = ldapServerTraverseUserWithHttpInfo(ldapserverId, limit, xOrgId, skip, filter);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute ldapServerTraverseUser request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphObjectWithPaths&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphObjectWithPaths>> executeWithHttpInfo() throws ApiException {
            return ldapServerTraverseUserWithHttpInfo(ldapserverId, limit, xOrgId, skip, filter);
        }

        /**
         * Execute ldapServerTraverseUser request (asynchronously)
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
            return ldapServerTraverseUserAsync(ldapserverId, limit, xOrgId, skip, filter, _callback);
        }
    }

    /**
     * List the Users bound to a LDAP Server
     * This endpoint will return all Users bound to an LDAP Server, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this LDAP server instance to the corresponding User; this array represents all grouping and/or associations that would have to be removed to deprovision the User from this LDAP server instance.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/ldapservers/{LDAP_ID}/users \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param ldapserverId ObjectID of the LDAP Server. (required)
     * @return LdapServerTraverseUserRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public LdapServerTraverseUserRequestBuilder ldapServerTraverseUser(String ldapserverId) throws IllegalArgumentException {
        if (ldapserverId == null) throw new IllegalArgumentException("\"ldapserverId\" is required but got null");
            

        return new LdapServerTraverseUserRequestBuilder(ldapserverId);
    }
    private okhttp3.Call ldapServerTraverseUserGroupCall(String ldapserverId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/ldapservers/{ldapserver_id}/usergroups"
            .replace("{" + "ldapserver_id" + "}", localVarApiClient.escapeString(ldapserverId.toString()));

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
    private okhttp3.Call ldapServerTraverseUserGroupValidateBeforeCall(String ldapserverId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'ldapserverId' is set
        if (ldapserverId == null) {
            throw new ApiException("Missing the required parameter 'ldapserverId' when calling ldapServerTraverseUserGroup(Async)");
        }

        return ldapServerTraverseUserGroupCall(ldapserverId, limit, xOrgId, skip, filter, _callback);

    }


    private ApiResponse<List<GraphObjectWithPaths>> ldapServerTraverseUserGroupWithHttpInfo(String ldapserverId, Integer limit, String xOrgId, Integer skip, List<String> filter) throws ApiException {
        okhttp3.Call localVarCall = ldapServerTraverseUserGroupValidateBeforeCall(ldapserverId, limit, xOrgId, skip, filter, null);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call ldapServerTraverseUserGroupAsync(String ldapserverId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {

        okhttp3.Call localVarCall = ldapServerTraverseUserGroupValidateBeforeCall(ldapserverId, limit, xOrgId, skip, filter, _callback);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class LdapServerTraverseUserGroupRequestBuilder {
        private final String ldapserverId;
        private Integer limit;
        private String xOrgId;
        private Integer skip;
        private List<String> filter;

        private LdapServerTraverseUserGroupRequestBuilder(String ldapserverId) {
            this.ldapserverId = ldapserverId;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return LdapServerTraverseUserGroupRequestBuilder
         */
        public LdapServerTraverseUserGroupRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return LdapServerTraverseUserGroupRequestBuilder
         */
        public LdapServerTraverseUserGroupRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return LdapServerTraverseUserGroupRequestBuilder
         */
        public LdapServerTraverseUserGroupRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return LdapServerTraverseUserGroupRequestBuilder
         */
        public LdapServerTraverseUserGroupRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Build call for ldapServerTraverseUserGroup
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
            return ldapServerTraverseUserGroupCall(ldapserverId, limit, xOrgId, skip, filter, _callback);
        }


        /**
         * Execute ldapServerTraverseUserGroup request
         * @return List&lt;GraphObjectWithPaths&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphObjectWithPaths> execute() throws ApiException {
            ApiResponse<List<GraphObjectWithPaths>> localVarResp = ldapServerTraverseUserGroupWithHttpInfo(ldapserverId, limit, xOrgId, skip, filter);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute ldapServerTraverseUserGroup request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphObjectWithPaths&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphObjectWithPaths>> executeWithHttpInfo() throws ApiException {
            return ldapServerTraverseUserGroupWithHttpInfo(ldapserverId, limit, xOrgId, skip, filter);
        }

        /**
         * Execute ldapServerTraverseUserGroup request (asynchronously)
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
            return ldapServerTraverseUserGroupAsync(ldapserverId, limit, xOrgId, skip, filter, _callback);
        }
    }

    /**
     * List the User Groups bound to a LDAP Server
     * This endpoint will return all Users Groups bound to a LDAP Server, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the group&#39;s type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this LDAP server instance to the corresponding User Group; this array represents all grouping and/or associations that would have to be removed to deprovision the User Group from this LDAP server instance.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/ldapservers/{LDAP_ID}/usergroups \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param ldapserverId ObjectID of the LDAP Server. (required)
     * @return LdapServerTraverseUserGroupRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public LdapServerTraverseUserGroupRequestBuilder ldapServerTraverseUserGroup(String ldapserverId) throws IllegalArgumentException {
        if (ldapserverId == null) throw new IllegalArgumentException("\"ldapserverId\" is required but got null");
            

        return new LdapServerTraverseUserGroupRequestBuilder(ldapserverId);
    }
    private okhttp3.Call listCall(List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/ldapservers";

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
    private okhttp3.Call listValidateBeforeCall(List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, String xOrgId, final ApiCallback _callback) throws ApiException {
        return listCall(fields, filter, limit, skip, sort, xOrgId, _callback);

    }


    private ApiResponse<List<LdapServer>> listWithHttpInfo(List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = listValidateBeforeCall(fields, filter, limit, skip, sort, xOrgId, null);
        Type localVarReturnType = new TypeToken<List<LdapServer>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listAsync(List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, String xOrgId, final ApiCallback<List<LdapServer>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listValidateBeforeCall(fields, filter, limit, skip, sort, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<List<LdapServer>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListRequestBuilder {
        private List<String> fields;
        private List<String> filter;
        private Integer limit;
        private Integer skip;
        private List<String> sort;
        private String xOrgId;

        private ListRequestBuilder() {
        }

        /**
         * Set fields
         * @param fields The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  (optional)
         * @return ListRequestBuilder
         */
        public ListRequestBuilder fields(List<String> fields) {
            this.fields = fields;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return ListRequestBuilder
         */
        public ListRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return ListRequestBuilder
         */
        public ListRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListRequestBuilder
         */
        public ListRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  (optional)
         * @return ListRequestBuilder
         */
        public ListRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
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
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listCall(fields, filter, limit, skip, sort, xOrgId, _callback);
        }


        /**
         * Execute list request
         * @return List&lt;LdapServer&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<LdapServer> execute() throws ApiException {
            ApiResponse<List<LdapServer>> localVarResp = listWithHttpInfo(fields, filter, limit, skip, sort, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute list request with HTTP info returned
         * @return ApiResponse&lt;List&lt;LdapServer&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<LdapServer>> executeWithHttpInfo() throws ApiException {
            return listWithHttpInfo(fields, filter, limit, skip, sort, xOrgId);
        }

        /**
         * Execute list request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<LdapServer>> _callback) throws ApiException {
            return listAsync(fields, filter, limit, skip, sort, xOrgId, _callback);
        }
    }

    /**
     * List LDAP Servers
     * This endpoint returns the object IDs of your LDAP servers.   ##### Sample Request  &#x60;&#x60;&#x60;   curl -X GET https://console.jumpcloud.com/api/v2/ldapservers/ \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;
     * @return ListRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListRequestBuilder list() throws IllegalArgumentException {
        return new ListRequestBuilder();
    }
    private okhttp3.Call patchCall(String id, String xOrgId, LdapserversPatchRequest ldapserversPatchRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = ldapserversPatchRequest;

        // create path and map variables
        String localVarPath = "/ldapservers/{id}"
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
        return localVarApiClient.buildCall(basePath, localVarPath, "PATCH", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call patchValidateBeforeCall(String id, String xOrgId, LdapserversPatchRequest ldapserversPatchRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling patch(Async)");
        }

        return patchCall(id, xOrgId, ldapserversPatchRequest, _callback);

    }


    private ApiResponse<LdapserversPatchResponse> patchWithHttpInfo(String id, String xOrgId, LdapserversPatchRequest ldapserversPatchRequest) throws ApiException {
        okhttp3.Call localVarCall = patchValidateBeforeCall(id, xOrgId, ldapserversPatchRequest, null);
        Type localVarReturnType = new TypeToken<LdapserversPatchResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call patchAsync(String id, String xOrgId, LdapserversPatchRequest ldapserversPatchRequest, final ApiCallback<LdapserversPatchResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = patchValidateBeforeCall(id, xOrgId, ldapserversPatchRequest, _callback);
        Type localVarReturnType = new TypeToken<LdapserversPatchResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class PatchRequestBuilder {
        private final String id;
        private String id;
        private LdapServerAction userLockoutAction;
        private LdapServerAction userPasswordExpirationAction;
        private String xOrgId;

        private PatchRequestBuilder(String id) {
            this.id = id;
        }

        /**
         * Set id
         * @param id  (optional)
         * @return PatchRequestBuilder
         */
        public PatchRequestBuilder id(String id) {
            this.id = id;
            return this;
        }
        
        /**
         * Set userLockoutAction
         * @param userLockoutAction  (optional)
         * @return PatchRequestBuilder
         */
        public PatchRequestBuilder userLockoutAction(LdapServerAction userLockoutAction) {
            this.userLockoutAction = userLockoutAction;
            return this;
        }
        
        /**
         * Set userPasswordExpirationAction
         * @param userPasswordExpirationAction  (optional)
         * @return PatchRequestBuilder
         */
        public PatchRequestBuilder userPasswordExpirationAction(LdapServerAction userPasswordExpirationAction) {
            this.userPasswordExpirationAction = userPasswordExpirationAction;
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
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            LdapserversPatchRequest ldapserversPatchRequest = buildBodyParams();
            return patchCall(id, xOrgId, ldapserversPatchRequest, _callback);
        }

        private LdapserversPatchRequest buildBodyParams() {
            LdapserversPatchRequest ldapserversPatchRequest = new LdapserversPatchRequest();
            ldapserversPatchRequest.id(this.id);
            ldapserversPatchRequest.userLockoutAction(this.userLockoutAction);
            ldapserversPatchRequest.userPasswordExpirationAction(this.userPasswordExpirationAction);
            return ldapserversPatchRequest;
        }

        /**
         * Execute patch request
         * @return LdapserversPatchResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public LdapserversPatchResponse execute() throws ApiException {
            LdapserversPatchRequest ldapserversPatchRequest = buildBodyParams();
            ApiResponse<LdapserversPatchResponse> localVarResp = patchWithHttpInfo(id, xOrgId, ldapserversPatchRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute patch request with HTTP info returned
         * @return ApiResponse&lt;LdapserversPatchResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<LdapserversPatchResponse> executeWithHttpInfo() throws ApiException {
            LdapserversPatchRequest ldapserversPatchRequest = buildBodyParams();
            return patchWithHttpInfo(id, xOrgId, ldapserversPatchRequest);
        }

        /**
         * Execute patch request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<LdapserversPatchResponse> _callback) throws ApiException {
            LdapserversPatchRequest ldapserversPatchRequest = buildBodyParams();
            return patchAsync(id, xOrgId, ldapserversPatchRequest, _callback);
        }
    }

    /**
     * Update existing LDAP server
     * This endpoint allows updating some attributes of an LDAP server.  Sample Request  &#x60;&#x60;&#x60; curl -X PATCH https://console.jumpcloud.com/api/v2/ldapservers/{LDAP_ID} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;userLockoutAction\&quot;: \&quot;remove\&quot;,     \&quot;userPasswordExpirationAction\&quot;: \&quot;disable\&quot;   }&#39; &#x60;&#x60;&#x60;
     * @param id Unique identifier of the LDAP server. (required)
     * @return PatchRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public PatchRequestBuilder patch(String id) throws IllegalArgumentException {
        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new PatchRequestBuilder(id);
    }
}