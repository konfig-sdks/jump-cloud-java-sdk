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


import com.konfigthis.client.model.ActiveDirectory;
import com.konfigthis.client.model.ActiveDirectoryAgent;
import com.konfigthis.client.model.ActiveDirectoryAgentGet;
import com.konfigthis.client.model.ActiveDirectoryAgentList;
import com.konfigthis.client.model.GraphConnection;
import com.konfigthis.client.model.GraphObjectWithPaths;
import com.konfigthis.client.model.GraphOperationActiveDirectory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;

public class ActiveDirectoryApiGenerated {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public ActiveDirectoryApiGenerated() throws IllegalArgumentException {
        this(Configuration.getDefaultApiClient());
    }

    public ActiveDirectoryApiGenerated(ApiClient apiClient) throws IllegalArgumentException {
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

    private okhttp3.Call activeDirectoryAssociationsListCall(String activedirectoryId, List<String> targets, Integer limit, Integer skip, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/activedirectories/{activedirectory_id}/associations"
            .replace("{" + "activedirectory_id" + "}", localVarApiClient.escapeString(activedirectoryId.toString()));

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
    private okhttp3.Call activeDirectoryAssociationsListValidateBeforeCall(String activedirectoryId, List<String> targets, Integer limit, Integer skip, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'activedirectoryId' is set
        if (activedirectoryId == null) {
            throw new ApiException("Missing the required parameter 'activedirectoryId' when calling activeDirectoryAssociationsList(Async)");
        }

        // verify the required parameter 'targets' is set
        if (targets == null) {
            throw new ApiException("Missing the required parameter 'targets' when calling activeDirectoryAssociationsList(Async)");
        }

        return activeDirectoryAssociationsListCall(activedirectoryId, targets, limit, skip, xOrgId, _callback);

    }


    private ApiResponse<List<GraphConnection>> activeDirectoryAssociationsListWithHttpInfo(String activedirectoryId, List<String> targets, Integer limit, Integer skip, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = activeDirectoryAssociationsListValidateBeforeCall(activedirectoryId, targets, limit, skip, xOrgId, null);
        Type localVarReturnType = new TypeToken<List<GraphConnection>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call activeDirectoryAssociationsListAsync(String activedirectoryId, List<String> targets, Integer limit, Integer skip, String xOrgId, final ApiCallback<List<GraphConnection>> _callback) throws ApiException {

        okhttp3.Call localVarCall = activeDirectoryAssociationsListValidateBeforeCall(activedirectoryId, targets, limit, skip, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<List<GraphConnection>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ActiveDirectoryAssociationsListRequestBuilder {
        private final String activedirectoryId;
        private final List<String> targets;
        private Integer limit;
        private Integer skip;
        private String xOrgId;

        private ActiveDirectoryAssociationsListRequestBuilder(String activedirectoryId, List<String> targets) {
            this.activedirectoryId = activedirectoryId;
            this.targets = targets;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return ActiveDirectoryAssociationsListRequestBuilder
         */
        public ActiveDirectoryAssociationsListRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ActiveDirectoryAssociationsListRequestBuilder
         */
        public ActiveDirectoryAssociationsListRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ActiveDirectoryAssociationsListRequestBuilder
         */
        public ActiveDirectoryAssociationsListRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for activeDirectoryAssociationsList
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
            return activeDirectoryAssociationsListCall(activedirectoryId, targets, limit, skip, xOrgId, _callback);
        }


        /**
         * Execute activeDirectoryAssociationsList request
         * @return List&lt;GraphConnection&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphConnection> execute() throws ApiException {
            ApiResponse<List<GraphConnection>> localVarResp = activeDirectoryAssociationsListWithHttpInfo(activedirectoryId, targets, limit, skip, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute activeDirectoryAssociationsList request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphConnection&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphConnection>> executeWithHttpInfo() throws ApiException {
            return activeDirectoryAssociationsListWithHttpInfo(activedirectoryId, targets, limit, skip, xOrgId);
        }

        /**
         * Execute activeDirectoryAssociationsList request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<GraphConnection>> _callback) throws ApiException {
            return activeDirectoryAssociationsListAsync(activedirectoryId, targets, limit, skip, xOrgId, _callback);
        }
    }

    /**
     * List the associations of an Active Directory instance
     * This endpoint returns the direct associations of this Active Directory instance.  A direct association can be a non-homogeneous relationship between 2 different objects, for example Active Directory and Users.   #### Sample Request &#x60;&#x60;&#x60; curl -X GET &#39;https://console.jumpcloud.com/api/v2/activedirectories/{ActiveDirectory_ID}/associations?targets&#x3D;user \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param activedirectoryId  (required)
     * @param targets Targets which a \&quot;active_directory\&quot; can be associated to. (required)
     * @return ActiveDirectoryAssociationsListRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ActiveDirectoryAssociationsListRequestBuilder activeDirectoryAssociationsList(String activedirectoryId, List<String> targets) throws IllegalArgumentException {
        if (activedirectoryId == null) throw new IllegalArgumentException("\"activedirectoryId\" is required but got null");
            

        if (targets == null) throw new IllegalArgumentException("\"targets\" is required but got null");
        return new ActiveDirectoryAssociationsListRequestBuilder(activedirectoryId, targets);
    }
    private okhttp3.Call activeDirectoryAssociationsPostCall(String activedirectoryId, String xOrgId, GraphOperationActiveDirectory graphOperationActiveDirectory, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = graphOperationActiveDirectory;

        // create path and map variables
        String localVarPath = "/activedirectories/{activedirectory_id}/associations"
            .replace("{" + "activedirectory_id" + "}", localVarApiClient.escapeString(activedirectoryId.toString()));

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
    private okhttp3.Call activeDirectoryAssociationsPostValidateBeforeCall(String activedirectoryId, String xOrgId, GraphOperationActiveDirectory graphOperationActiveDirectory, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'activedirectoryId' is set
        if (activedirectoryId == null) {
            throw new ApiException("Missing the required parameter 'activedirectoryId' when calling activeDirectoryAssociationsPost(Async)");
        }

        return activeDirectoryAssociationsPostCall(activedirectoryId, xOrgId, graphOperationActiveDirectory, _callback);

    }


    private ApiResponse<Void> activeDirectoryAssociationsPostWithHttpInfo(String activedirectoryId, String xOrgId, GraphOperationActiveDirectory graphOperationActiveDirectory) throws ApiException {
        okhttp3.Call localVarCall = activeDirectoryAssociationsPostValidateBeforeCall(activedirectoryId, xOrgId, graphOperationActiveDirectory, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call activeDirectoryAssociationsPostAsync(String activedirectoryId, String xOrgId, GraphOperationActiveDirectory graphOperationActiveDirectory, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = activeDirectoryAssociationsPostValidateBeforeCall(activedirectoryId, xOrgId, graphOperationActiveDirectory, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class ActiveDirectoryAssociationsPostRequestBuilder {
        private final String activedirectoryId;
        private String id;
        private String op;
        private Map<String, Object> attributes;
        private String type;
        private String xOrgId;

        private ActiveDirectoryAssociationsPostRequestBuilder(String activedirectoryId) {
            this.activedirectoryId = activedirectoryId;
        }

        /**
         * Set id
         * @param id The ObjectID of graph object being added or removed as an association. (optional)
         * @return ActiveDirectoryAssociationsPostRequestBuilder
         */
        public ActiveDirectoryAssociationsPostRequestBuilder id(String id) {
            this.id = id;
            return this;
        }
        
        /**
         * Set op
         * @param op How to modify the graph connection. (optional)
         * @return ActiveDirectoryAssociationsPostRequestBuilder
         */
        public ActiveDirectoryAssociationsPostRequestBuilder op(String op) {
            this.op = op;
            return this;
        }
        
        /**
         * Set attributes
         * @param attributes The graph attributes. (optional)
         * @return ActiveDirectoryAssociationsPostRequestBuilder
         */
        public ActiveDirectoryAssociationsPostRequestBuilder attributes(Map<String, Object> attributes) {
            this.attributes = attributes;
            return this;
        }
        
        /**
         * Set type
         * @param type Targets which a \\\&quot;active_directory\\\&quot; can be associated to. (optional)
         * @return ActiveDirectoryAssociationsPostRequestBuilder
         */
        public ActiveDirectoryAssociationsPostRequestBuilder type(String type) {
            this.type = type;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ActiveDirectoryAssociationsPostRequestBuilder
         */
        public ActiveDirectoryAssociationsPostRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for activeDirectoryAssociationsPost
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            GraphOperationActiveDirectory graphOperationActiveDirectory = buildBodyParams();
            return activeDirectoryAssociationsPostCall(activedirectoryId, xOrgId, graphOperationActiveDirectory, _callback);
        }

        private GraphOperationActiveDirectory buildBodyParams() {
            GraphOperationActiveDirectory graphOperationActiveDirectory = new GraphOperationActiveDirectory();
            return graphOperationActiveDirectory;
        }

        /**
         * Execute activeDirectoryAssociationsPost request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            GraphOperationActiveDirectory graphOperationActiveDirectory = buildBodyParams();
            activeDirectoryAssociationsPostWithHttpInfo(activedirectoryId, xOrgId, graphOperationActiveDirectory);
        }

        /**
         * Execute activeDirectoryAssociationsPost request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            GraphOperationActiveDirectory graphOperationActiveDirectory = buildBodyParams();
            return activeDirectoryAssociationsPostWithHttpInfo(activedirectoryId, xOrgId, graphOperationActiveDirectory);
        }

        /**
         * Execute activeDirectoryAssociationsPost request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Void> _callback) throws ApiException {
            GraphOperationActiveDirectory graphOperationActiveDirectory = buildBodyParams();
            return activeDirectoryAssociationsPostAsync(activedirectoryId, xOrgId, graphOperationActiveDirectory, _callback);
        }
    }

    /**
     * Manage the associations of an Active Directory instance
     * This endpoint allows you to manage the _direct_ associations of an Active Directory instance.  A direct association can be a non-homogeneous relationship between 2 different objects, for example Active Directory and Users.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/activedirectories/{AD_Instance_ID}/associations \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;op\&quot;: \&quot;add\&quot;,     \&quot;type\&quot;: \&quot;user\&quot;,     \&quot;id\&quot;: \&quot;{User_ID}\&quot;   }&#39; &#x60;&#x60;&#x60;
     * @param activedirectoryId  (required)
     * @return ActiveDirectoryAssociationsPostRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ActiveDirectoryAssociationsPostRequestBuilder activeDirectoryAssociationsPost(String activedirectoryId) throws IllegalArgumentException {
        if (activedirectoryId == null) throw new IllegalArgumentException("\"activedirectoryId\" is required but got null");
            

        return new ActiveDirectoryAssociationsPostRequestBuilder(activedirectoryId);
    }
    private okhttp3.Call activeDirectoryTraverseUserCall(String activedirectoryId, List<String> filter, Integer limit, String xOrgId, Integer skip, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/activedirectories/{activedirectory_id}/users"
            .replace("{" + "activedirectory_id" + "}", localVarApiClient.escapeString(activedirectoryId.toString()));

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
    private okhttp3.Call activeDirectoryTraverseUserValidateBeforeCall(String activedirectoryId, List<String> filter, Integer limit, String xOrgId, Integer skip, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'activedirectoryId' is set
        if (activedirectoryId == null) {
            throw new ApiException("Missing the required parameter 'activedirectoryId' when calling activeDirectoryTraverseUser(Async)");
        }

        return activeDirectoryTraverseUserCall(activedirectoryId, filter, limit, xOrgId, skip, _callback);

    }


    private ApiResponse<List<GraphObjectWithPaths>> activeDirectoryTraverseUserWithHttpInfo(String activedirectoryId, List<String> filter, Integer limit, String xOrgId, Integer skip) throws ApiException {
        okhttp3.Call localVarCall = activeDirectoryTraverseUserValidateBeforeCall(activedirectoryId, filter, limit, xOrgId, skip, null);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call activeDirectoryTraverseUserAsync(String activedirectoryId, List<String> filter, Integer limit, String xOrgId, Integer skip, final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {

        okhttp3.Call localVarCall = activeDirectoryTraverseUserValidateBeforeCall(activedirectoryId, filter, limit, xOrgId, skip, _callback);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ActiveDirectoryTraverseUserRequestBuilder {
        private final String activedirectoryId;
        private List<String> filter;
        private Integer limit;
        private String xOrgId;
        private Integer skip;

        private ActiveDirectoryTraverseUserRequestBuilder(String activedirectoryId) {
            this.activedirectoryId = activedirectoryId;
        }

        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return ActiveDirectoryTraverseUserRequestBuilder
         */
        public ActiveDirectoryTraverseUserRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return ActiveDirectoryTraverseUserRequestBuilder
         */
        public ActiveDirectoryTraverseUserRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ActiveDirectoryTraverseUserRequestBuilder
         */
        public ActiveDirectoryTraverseUserRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ActiveDirectoryTraverseUserRequestBuilder
         */
        public ActiveDirectoryTraverseUserRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Build call for activeDirectoryTraverseUser
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
            return activeDirectoryTraverseUserCall(activedirectoryId, filter, limit, xOrgId, skip, _callback);
        }


        /**
         * Execute activeDirectoryTraverseUser request
         * @return List&lt;GraphObjectWithPaths&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphObjectWithPaths> execute() throws ApiException {
            ApiResponse<List<GraphObjectWithPaths>> localVarResp = activeDirectoryTraverseUserWithHttpInfo(activedirectoryId, filter, limit, xOrgId, skip);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute activeDirectoryTraverseUser request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphObjectWithPaths&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphObjectWithPaths>> executeWithHttpInfo() throws ApiException {
            return activeDirectoryTraverseUserWithHttpInfo(activedirectoryId, filter, limit, xOrgId, skip);
        }

        /**
         * Execute activeDirectoryTraverseUser request (asynchronously)
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
            return activeDirectoryTraverseUserAsync(activedirectoryId, filter, limit, xOrgId, skip, _callback);
        }
    }

    /**
     * List the Users bound to an Active Directory instance
     * This endpoint will return all Users bound to an Active Directory instance, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this Active Directory instance to the corresponding User; this array represents all grouping and/or associations that would have to be removed to deprovision the User from this Active Directory instance.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/activedirectories/{ActiveDirectory_ID}/users \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param activedirectoryId ObjectID of the Active Directory instance. (required)
     * @return ActiveDirectoryTraverseUserRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ActiveDirectoryTraverseUserRequestBuilder activeDirectoryTraverseUser(String activedirectoryId) throws IllegalArgumentException {
        if (activedirectoryId == null) throw new IllegalArgumentException("\"activedirectoryId\" is required but got null");
            

        return new ActiveDirectoryTraverseUserRequestBuilder(activedirectoryId);
    }
    private okhttp3.Call activeDirectoryTraverseUserGroupCall(String activedirectoryId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/activedirectories/{activedirectory_id}/usergroups"
            .replace("{" + "activedirectory_id" + "}", localVarApiClient.escapeString(activedirectoryId.toString()));

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
    private okhttp3.Call activeDirectoryTraverseUserGroupValidateBeforeCall(String activedirectoryId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'activedirectoryId' is set
        if (activedirectoryId == null) {
            throw new ApiException("Missing the required parameter 'activedirectoryId' when calling activeDirectoryTraverseUserGroup(Async)");
        }

        return activeDirectoryTraverseUserGroupCall(activedirectoryId, limit, xOrgId, skip, filter, _callback);

    }


    private ApiResponse<List<GraphObjectWithPaths>> activeDirectoryTraverseUserGroupWithHttpInfo(String activedirectoryId, Integer limit, String xOrgId, Integer skip, List<String> filter) throws ApiException {
        okhttp3.Call localVarCall = activeDirectoryTraverseUserGroupValidateBeforeCall(activedirectoryId, limit, xOrgId, skip, filter, null);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call activeDirectoryTraverseUserGroupAsync(String activedirectoryId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {

        okhttp3.Call localVarCall = activeDirectoryTraverseUserGroupValidateBeforeCall(activedirectoryId, limit, xOrgId, skip, filter, _callback);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ActiveDirectoryTraverseUserGroupRequestBuilder {
        private final String activedirectoryId;
        private Integer limit;
        private String xOrgId;
        private Integer skip;
        private List<String> filter;

        private ActiveDirectoryTraverseUserGroupRequestBuilder(String activedirectoryId) {
            this.activedirectoryId = activedirectoryId;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return ActiveDirectoryTraverseUserGroupRequestBuilder
         */
        public ActiveDirectoryTraverseUserGroupRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ActiveDirectoryTraverseUserGroupRequestBuilder
         */
        public ActiveDirectoryTraverseUserGroupRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ActiveDirectoryTraverseUserGroupRequestBuilder
         */
        public ActiveDirectoryTraverseUserGroupRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return ActiveDirectoryTraverseUserGroupRequestBuilder
         */
        public ActiveDirectoryTraverseUserGroupRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Build call for activeDirectoryTraverseUserGroup
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
            return activeDirectoryTraverseUserGroupCall(activedirectoryId, limit, xOrgId, skip, filter, _callback);
        }


        /**
         * Execute activeDirectoryTraverseUserGroup request
         * @return List&lt;GraphObjectWithPaths&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphObjectWithPaths> execute() throws ApiException {
            ApiResponse<List<GraphObjectWithPaths>> localVarResp = activeDirectoryTraverseUserGroupWithHttpInfo(activedirectoryId, limit, xOrgId, skip, filter);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute activeDirectoryTraverseUserGroup request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphObjectWithPaths&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphObjectWithPaths>> executeWithHttpInfo() throws ApiException {
            return activeDirectoryTraverseUserGroupWithHttpInfo(activedirectoryId, limit, xOrgId, skip, filter);
        }

        /**
         * Execute activeDirectoryTraverseUserGroup request (asynchronously)
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
            return activeDirectoryTraverseUserGroupAsync(activedirectoryId, limit, xOrgId, skip, filter, _callback);
        }
    }

    /**
     * List the User Groups bound to an Active Directory instance
     * This endpoint will return all Users Groups bound to an Active Directory instance, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the group&#39;s type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this Active Directory instance to the corresponding User Group; this array represents all grouping and/or associations that would have to be removed to deprovision the User Group from this Active Directory instance.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/activedirectories/{ActiveDirectory_ID}/usergroups \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param activedirectoryId ObjectID of the Active Directory instance. (required)
     * @return ActiveDirectoryTraverseUserGroupRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ActiveDirectoryTraverseUserGroupRequestBuilder activeDirectoryTraverseUserGroup(String activedirectoryId) throws IllegalArgumentException {
        if (activedirectoryId == null) throw new IllegalArgumentException("\"activedirectoryId\" is required but got null");
            

        return new ActiveDirectoryTraverseUserGroupRequestBuilder(activedirectoryId);
    }
    private okhttp3.Call agentsDeleteCall(String activedirectoryId, String agentId, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/activedirectories/{activedirectory_id}/agents/{agent_id}"
            .replace("{" + "activedirectory_id" + "}", localVarApiClient.escapeString(activedirectoryId.toString()))
            .replace("{" + "agent_id" + "}", localVarApiClient.escapeString(agentId.toString()));

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
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "x-api-key" };
        return localVarApiClient.buildCall(basePath, localVarPath, "DELETE", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call agentsDeleteValidateBeforeCall(String activedirectoryId, String agentId, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'activedirectoryId' is set
        if (activedirectoryId == null) {
            throw new ApiException("Missing the required parameter 'activedirectoryId' when calling agentsDelete(Async)");
        }

        // verify the required parameter 'agentId' is set
        if (agentId == null) {
            throw new ApiException("Missing the required parameter 'agentId' when calling agentsDelete(Async)");
        }

        return agentsDeleteCall(activedirectoryId, agentId, xOrgId, _callback);

    }


    private ApiResponse<Void> agentsDeleteWithHttpInfo(String activedirectoryId, String agentId, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = agentsDeleteValidateBeforeCall(activedirectoryId, agentId, xOrgId, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call agentsDeleteAsync(String activedirectoryId, String agentId, String xOrgId, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = agentsDeleteValidateBeforeCall(activedirectoryId, agentId, xOrgId, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class AgentsDeleteRequestBuilder {
        private final String activedirectoryId;
        private final String agentId;
        private String xOrgId;

        private AgentsDeleteRequestBuilder(String activedirectoryId, String agentId) {
            this.activedirectoryId = activedirectoryId;
            this.agentId = agentId;
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return AgentsDeleteRequestBuilder
         */
        public AgentsDeleteRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for agentsDelete
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return agentsDeleteCall(activedirectoryId, agentId, xOrgId, _callback);
        }


        /**
         * Execute agentsDelete request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            agentsDeleteWithHttpInfo(activedirectoryId, agentId, xOrgId);
        }

        /**
         * Execute agentsDelete request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            return agentsDeleteWithHttpInfo(activedirectoryId, agentId, xOrgId);
        }

        /**
         * Execute agentsDelete request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Void> _callback) throws ApiException {
            return agentsDeleteAsync(activedirectoryId, agentId, xOrgId, _callback);
        }
    }

    /**
     * Delete Active Directory Agent
     * This endpoint deletes an Active Directory agent.  #### Sample Request &#x60;&#x60;&#x60; curl -X DELETE https://console.jumpcloud.com/api/v2/activedirectories/{activedirectory_id}/agents/{agent_id} \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param activedirectoryId  (required)
     * @param agentId  (required)
     * @return AgentsDeleteRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public AgentsDeleteRequestBuilder agentsDelete(String activedirectoryId, String agentId) throws IllegalArgumentException {
        if (activedirectoryId == null) throw new IllegalArgumentException("\"activedirectoryId\" is required but got null");
            

        if (agentId == null) throw new IllegalArgumentException("\"agentId\" is required but got null");
            

        return new AgentsDeleteRequestBuilder(activedirectoryId, agentId);
    }
    private okhttp3.Call agentsGetCall(String activedirectoryId, String agentId, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/activedirectories/{activedirectory_id}/agents/{agent_id}"
            .replace("{" + "activedirectory_id" + "}", localVarApiClient.escapeString(activedirectoryId.toString()))
            .replace("{" + "agent_id" + "}", localVarApiClient.escapeString(agentId.toString()));

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
    private okhttp3.Call agentsGetValidateBeforeCall(String activedirectoryId, String agentId, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'activedirectoryId' is set
        if (activedirectoryId == null) {
            throw new ApiException("Missing the required parameter 'activedirectoryId' when calling agentsGet(Async)");
        }

        // verify the required parameter 'agentId' is set
        if (agentId == null) {
            throw new ApiException("Missing the required parameter 'agentId' when calling agentsGet(Async)");
        }

        return agentsGetCall(activedirectoryId, agentId, xOrgId, _callback);

    }


    private ApiResponse<ActiveDirectoryAgentList> agentsGetWithHttpInfo(String activedirectoryId, String agentId, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = agentsGetValidateBeforeCall(activedirectoryId, agentId, xOrgId, null);
        Type localVarReturnType = new TypeToken<ActiveDirectoryAgentList>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call agentsGetAsync(String activedirectoryId, String agentId, String xOrgId, final ApiCallback<ActiveDirectoryAgentList> _callback) throws ApiException {

        okhttp3.Call localVarCall = agentsGetValidateBeforeCall(activedirectoryId, agentId, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<ActiveDirectoryAgentList>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class AgentsGetRequestBuilder {
        private final String activedirectoryId;
        private final String agentId;
        private String xOrgId;

        private AgentsGetRequestBuilder(String activedirectoryId, String agentId) {
            this.activedirectoryId = activedirectoryId;
            this.agentId = agentId;
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return AgentsGetRequestBuilder
         */
        public AgentsGetRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for agentsGet
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
            return agentsGetCall(activedirectoryId, agentId, xOrgId, _callback);
        }


        /**
         * Execute agentsGet request
         * @return ActiveDirectoryAgentList
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ActiveDirectoryAgentList execute() throws ApiException {
            ApiResponse<ActiveDirectoryAgentList> localVarResp = agentsGetWithHttpInfo(activedirectoryId, agentId, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute agentsGet request with HTTP info returned
         * @return ApiResponse&lt;ActiveDirectoryAgentList&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<ActiveDirectoryAgentList> executeWithHttpInfo() throws ApiException {
            return agentsGetWithHttpInfo(activedirectoryId, agentId, xOrgId);
        }

        /**
         * Execute agentsGet request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ActiveDirectoryAgentList> _callback) throws ApiException {
            return agentsGetAsync(activedirectoryId, agentId, xOrgId, _callback);
        }
    }

    /**
     * Get Active Directory Agent
     * This endpoint returns an Active Directory agent.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/activedirectories/{activedirectory_id}/agents/{agent_id} \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param activedirectoryId  (required)
     * @param agentId  (required)
     * @return AgentsGetRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public AgentsGetRequestBuilder agentsGet(String activedirectoryId, String agentId) throws IllegalArgumentException {
        if (activedirectoryId == null) throw new IllegalArgumentException("\"activedirectoryId\" is required but got null");
            

        if (agentId == null) throw new IllegalArgumentException("\"agentId\" is required but got null");
            

        return new AgentsGetRequestBuilder(activedirectoryId, agentId);
    }
    private okhttp3.Call agentsListCall(String activedirectoryId, Integer limit, Integer skip, List<String> sort, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/activedirectories/{activedirectory_id}/agents"
            .replace("{" + "activedirectory_id" + "}", localVarApiClient.escapeString(activedirectoryId.toString()));

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
    private okhttp3.Call agentsListValidateBeforeCall(String activedirectoryId, Integer limit, Integer skip, List<String> sort, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'activedirectoryId' is set
        if (activedirectoryId == null) {
            throw new ApiException("Missing the required parameter 'activedirectoryId' when calling agentsList(Async)");
        }

        return agentsListCall(activedirectoryId, limit, skip, sort, xOrgId, _callback);

    }


    private ApiResponse<List<ActiveDirectoryAgentList>> agentsListWithHttpInfo(String activedirectoryId, Integer limit, Integer skip, List<String> sort, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = agentsListValidateBeforeCall(activedirectoryId, limit, skip, sort, xOrgId, null);
        Type localVarReturnType = new TypeToken<List<ActiveDirectoryAgentList>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call agentsListAsync(String activedirectoryId, Integer limit, Integer skip, List<String> sort, String xOrgId, final ApiCallback<List<ActiveDirectoryAgentList>> _callback) throws ApiException {

        okhttp3.Call localVarCall = agentsListValidateBeforeCall(activedirectoryId, limit, skip, sort, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<List<ActiveDirectoryAgentList>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class AgentsListRequestBuilder {
        private final String activedirectoryId;
        private Integer limit;
        private Integer skip;
        private List<String> sort;
        private String xOrgId;

        private AgentsListRequestBuilder(String activedirectoryId) {
            this.activedirectoryId = activedirectoryId;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return AgentsListRequestBuilder
         */
        public AgentsListRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return AgentsListRequestBuilder
         */
        public AgentsListRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  (optional)
         * @return AgentsListRequestBuilder
         */
        public AgentsListRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return AgentsListRequestBuilder
         */
        public AgentsListRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for agentsList
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
            return agentsListCall(activedirectoryId, limit, skip, sort, xOrgId, _callback);
        }


        /**
         * Execute agentsList request
         * @return List&lt;ActiveDirectoryAgentList&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<ActiveDirectoryAgentList> execute() throws ApiException {
            ApiResponse<List<ActiveDirectoryAgentList>> localVarResp = agentsListWithHttpInfo(activedirectoryId, limit, skip, sort, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute agentsList request with HTTP info returned
         * @return ApiResponse&lt;List&lt;ActiveDirectoryAgentList&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<ActiveDirectoryAgentList>> executeWithHttpInfo() throws ApiException {
            return agentsListWithHttpInfo(activedirectoryId, limit, skip, sort, xOrgId);
        }

        /**
         * Execute agentsList request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<ActiveDirectoryAgentList>> _callback) throws ApiException {
            return agentsListAsync(activedirectoryId, limit, skip, sort, xOrgId, _callback);
        }
    }

    /**
     * List Active Directory Agents
     * This endpoint allows you to list all your Active Directory Agents for a given Instance.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/activedirectories/{activedirectory_id}/agents \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;   &#x60;&#x60;&#x60;
     * @param activedirectoryId  (required)
     * @return AgentsListRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public AgentsListRequestBuilder agentsList(String activedirectoryId) throws IllegalArgumentException {
        if (activedirectoryId == null) throw new IllegalArgumentException("\"activedirectoryId\" is required but got null");
            

        return new AgentsListRequestBuilder(activedirectoryId);
    }
    private okhttp3.Call agentsPostCall(String activedirectoryId, String xOrgId, ActiveDirectoryAgent activeDirectoryAgent, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = activeDirectoryAgent;

        // create path and map variables
        String localVarPath = "/activedirectories/{activedirectory_id}/agents"
            .replace("{" + "activedirectory_id" + "}", localVarApiClient.escapeString(activedirectoryId.toString()));

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
    private okhttp3.Call agentsPostValidateBeforeCall(String activedirectoryId, String xOrgId, ActiveDirectoryAgent activeDirectoryAgent, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'activedirectoryId' is set
        if (activedirectoryId == null) {
            throw new ApiException("Missing the required parameter 'activedirectoryId' when calling agentsPost(Async)");
        }

        return agentsPostCall(activedirectoryId, xOrgId, activeDirectoryAgent, _callback);

    }


    private ApiResponse<ActiveDirectoryAgentGet> agentsPostWithHttpInfo(String activedirectoryId, String xOrgId, ActiveDirectoryAgent activeDirectoryAgent) throws ApiException {
        okhttp3.Call localVarCall = agentsPostValidateBeforeCall(activedirectoryId, xOrgId, activeDirectoryAgent, null);
        Type localVarReturnType = new TypeToken<ActiveDirectoryAgentGet>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call agentsPostAsync(String activedirectoryId, String xOrgId, ActiveDirectoryAgent activeDirectoryAgent, final ApiCallback<ActiveDirectoryAgentGet> _callback) throws ApiException {

        okhttp3.Call localVarCall = agentsPostValidateBeforeCall(activedirectoryId, xOrgId, activeDirectoryAgent, _callback);
        Type localVarReturnType = new TypeToken<ActiveDirectoryAgentGet>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class AgentsPostRequestBuilder {
        private final String activedirectoryId;
        private String agentType;
        private String xOrgId;

        private AgentsPostRequestBuilder(String activedirectoryId) {
            this.activedirectoryId = activedirectoryId;
        }

        /**
         * Set agentType
         * @param agentType  (optional)
         * @return AgentsPostRequestBuilder
         */
        public AgentsPostRequestBuilder agentType(String agentType) {
            this.agentType = agentType;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return AgentsPostRequestBuilder
         */
        public AgentsPostRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for agentsPost
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
            ActiveDirectoryAgent activeDirectoryAgent = buildBodyParams();
            return agentsPostCall(activedirectoryId, xOrgId, activeDirectoryAgent, _callback);
        }

        private ActiveDirectoryAgent buildBodyParams() {
            ActiveDirectoryAgent activeDirectoryAgent = new ActiveDirectoryAgent();
            if (this.agentType != null)
            activeDirectoryAgent.agentType(ActiveDirectoryAgent.AgentTypeEnum.fromValue(this.agentType));
            return activeDirectoryAgent;
        }

        /**
         * Execute agentsPost request
         * @return ActiveDirectoryAgentGet
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ActiveDirectoryAgentGet execute() throws ApiException {
            ActiveDirectoryAgent activeDirectoryAgent = buildBodyParams();
            ApiResponse<ActiveDirectoryAgentGet> localVarResp = agentsPostWithHttpInfo(activedirectoryId, xOrgId, activeDirectoryAgent);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute agentsPost request with HTTP info returned
         * @return ApiResponse&lt;ActiveDirectoryAgentGet&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<ActiveDirectoryAgentGet> executeWithHttpInfo() throws ApiException {
            ActiveDirectoryAgent activeDirectoryAgent = buildBodyParams();
            return agentsPostWithHttpInfo(activedirectoryId, xOrgId, activeDirectoryAgent);
        }

        /**
         * Execute agentsPost request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ActiveDirectoryAgentGet> _callback) throws ApiException {
            ActiveDirectoryAgent activeDirectoryAgent = buildBodyParams();
            return agentsPostAsync(activedirectoryId, xOrgId, activeDirectoryAgent, _callback);
        }
    }

    /**
     * Create a new Active Directory Agent
     * This endpoint allows you to create a new Active Directory Agent.   #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/activedirectories/{activedirectory_id}/agents \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{ \&quot;agent_type\&quot;:\&quot;{SYNC}\&quot; }&#39; &#x60;&#x60;&#x60;
     * @param activedirectoryId  (required)
     * @return AgentsPostRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public AgentsPostRequestBuilder agentsPost(String activedirectoryId) throws IllegalArgumentException {
        if (activedirectoryId == null) throw new IllegalArgumentException("\"activedirectoryId\" is required but got null");
            

        return new AgentsPostRequestBuilder(activedirectoryId);
    }
    private okhttp3.Call deleteCall(String id, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/activedirectories/{id}"
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
    private okhttp3.Call deleteValidateBeforeCall(String id, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling delete(Async)");
        }

        return deleteCall(id, xOrgId, _callback);

    }


    private ApiResponse<ActiveDirectory> deleteWithHttpInfo(String id, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = deleteValidateBeforeCall(id, xOrgId, null);
        Type localVarReturnType = new TypeToken<ActiveDirectory>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call deleteAsync(String id, String xOrgId, final ApiCallback<ActiveDirectory> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteValidateBeforeCall(id, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<ActiveDirectory>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class DeleteRequestBuilder {
        private final String id;
        private String xOrgId;

        private DeleteRequestBuilder(String id) {
            this.id = id;
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
            return deleteCall(id, xOrgId, _callback);
        }


        /**
         * Execute delete request
         * @return ActiveDirectory
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ActiveDirectory execute() throws ApiException {
            ApiResponse<ActiveDirectory> localVarResp = deleteWithHttpInfo(id, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute delete request with HTTP info returned
         * @return ApiResponse&lt;ActiveDirectory&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<ActiveDirectory> executeWithHttpInfo() throws ApiException {
            return deleteWithHttpInfo(id, xOrgId);
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
        public okhttp3.Call executeAsync(final ApiCallback<ActiveDirectory> _callback) throws ApiException {
            return deleteAsync(id, xOrgId, _callback);
        }
    }

    /**
     * Delete an Active Directory
     * This endpoint allows you to delete an Active Directory Instance.  #### Sample Request &#x60;&#x60;&#x60; curl -X DELETE https://console.jumpcloud.com/api/v2/activedirectories/{ActiveDirectory_ID} \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;   &#x60;&#x60;&#x60;
     * @param id ObjectID of this Active Directory instance. (required)
     * @return DeleteRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public DeleteRequestBuilder delete(String id) throws IllegalArgumentException {
        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new DeleteRequestBuilder(id);
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
        String localVarPath = "/activedirectories/{id}"
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


    private ApiResponse<ActiveDirectory> getWithHttpInfo(String id, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = getValidateBeforeCall(id, xOrgId, null);
        Type localVarReturnType = new TypeToken<ActiveDirectory>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getAsync(String id, String xOrgId, final ApiCallback<ActiveDirectory> _callback) throws ApiException {

        okhttp3.Call localVarCall = getValidateBeforeCall(id, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<ActiveDirectory>(){}.getType();
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
         * @return ActiveDirectory
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ActiveDirectory execute() throws ApiException {
            ApiResponse<ActiveDirectory> localVarResp = getWithHttpInfo(id, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute get request with HTTP info returned
         * @return ApiResponse&lt;ActiveDirectory&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<ActiveDirectory> executeWithHttpInfo() throws ApiException {
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
        public okhttp3.Call executeAsync(final ApiCallback<ActiveDirectory> _callback) throws ApiException {
            return getAsync(id, xOrgId, _callback);
        }
    }

    /**
     * Get an Active Directory
     * This endpoint returns a specific Active Directory.  #### Sample Request  &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/activedirectories/{ActiveDirectory_ID} \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;   &#x60;&#x60;&#x60;
     * @param id ObjectID of this Active Directory instance. (required)
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
        String localVarPath = "/activedirectories";

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


    private ApiResponse<List<ActiveDirectory>> listWithHttpInfo(List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = listValidateBeforeCall(fields, filter, limit, skip, sort, xOrgId, null);
        Type localVarReturnType = new TypeToken<List<ActiveDirectory>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listAsync(List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, String xOrgId, final ApiCallback<List<ActiveDirectory>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listValidateBeforeCall(fields, filter, limit, skip, sort, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<List<ActiveDirectory>>(){}.getType();
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
         * @return List&lt;ActiveDirectory&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<ActiveDirectory> execute() throws ApiException {
            ApiResponse<List<ActiveDirectory>> localVarResp = listWithHttpInfo(fields, filter, limit, skip, sort, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute list request with HTTP info returned
         * @return ApiResponse&lt;List&lt;ActiveDirectory&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<ActiveDirectory>> executeWithHttpInfo() throws ApiException {
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
        public okhttp3.Call executeAsync(final ApiCallback<List<ActiveDirectory>> _callback) throws ApiException {
            return listAsync(fields, filter, limit, skip, sort, xOrgId, _callback);
        }
    }

    /**
     * List Active Directories
     * This endpoint allows you to list all your Active Directory Instances.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/activedirectories/ \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;   &#x60;&#x60;&#x60;
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
    private okhttp3.Call patchCall(String id, String xOrgId, ActiveDirectory activeDirectory, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = activeDirectory;

        // create path and map variables
        String localVarPath = "/activedirectories/{id}"
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
    private okhttp3.Call patchValidateBeforeCall(String id, String xOrgId, ActiveDirectory activeDirectory, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling patch(Async)");
        }

        return patchCall(id, xOrgId, activeDirectory, _callback);

    }


    private ApiResponse<ActiveDirectory> patchWithHttpInfo(String id, String xOrgId, ActiveDirectory activeDirectory) throws ApiException {
        okhttp3.Call localVarCall = patchValidateBeforeCall(id, xOrgId, activeDirectory, null);
        Type localVarReturnType = new TypeToken<ActiveDirectory>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call patchAsync(String id, String xOrgId, ActiveDirectory activeDirectory, final ApiCallback<ActiveDirectory> _callback) throws ApiException {

        okhttp3.Call localVarCall = patchValidateBeforeCall(id, xOrgId, activeDirectory, _callback);
        Type localVarReturnType = new TypeToken<ActiveDirectory>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class PatchRequestBuilder {
        private final String id;
        private String domain;
        private String id;
        private String primaryAgent;
        private String useCase;
        private String xOrgId;

        private PatchRequestBuilder(String id) {
            this.id = id;
        }

        /**
         * Set domain
         * @param domain Domain name for this Active Directory instance. (optional)
         * @return PatchRequestBuilder
         */
        public PatchRequestBuilder domain(String domain) {
            this.domain = domain;
            return this;
        }
        
        /**
         * Set id
         * @param id ObjectID of this Active Directory instance. (optional)
         * @return PatchRequestBuilder
         */
        public PatchRequestBuilder id(String id) {
            this.id = id;
            return this;
        }
        
        /**
         * Set primaryAgent
         * @param primaryAgent ObjectID of the primary agent of domain. (optional)
         * @return PatchRequestBuilder
         */
        public PatchRequestBuilder primaryAgent(String primaryAgent) {
            this.primaryAgent = primaryAgent;
            return this;
        }
        
        /**
         * Set useCase
         * @param useCase  (optional)
         * @return PatchRequestBuilder
         */
        public PatchRequestBuilder useCase(String useCase) {
            this.useCase = useCase;
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
            ActiveDirectory activeDirectory = buildBodyParams();
            return patchCall(id, xOrgId, activeDirectory, _callback);
        }

        private ActiveDirectory buildBodyParams() {
            ActiveDirectory activeDirectory = new ActiveDirectory();
            activeDirectory.domain(this.domain);
            activeDirectory.id(this.id);
            activeDirectory.primaryAgent(this.primaryAgent);
            if (this.useCase != null)
            activeDirectory.useCase(ActiveDirectory.UseCaseEnum.fromValue(this.useCase));
            return activeDirectory;
        }

        /**
         * Execute patch request
         * @return ActiveDirectory
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ActiveDirectory execute() throws ApiException {
            ActiveDirectory activeDirectory = buildBodyParams();
            ApiResponse<ActiveDirectory> localVarResp = patchWithHttpInfo(id, xOrgId, activeDirectory);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute patch request with HTTP info returned
         * @return ApiResponse&lt;ActiveDirectory&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<ActiveDirectory> executeWithHttpInfo() throws ApiException {
            ActiveDirectory activeDirectory = buildBodyParams();
            return patchWithHttpInfo(id, xOrgId, activeDirectory);
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
        public okhttp3.Call executeAsync(final ApiCallback<ActiveDirectory> _callback) throws ApiException {
            ActiveDirectory activeDirectory = buildBodyParams();
            return patchAsync(id, xOrgId, activeDirectory, _callback);
        }
    }

    /**
     * Update Active Directory
     * This endpoint allows you to update Active Directory.  - AD as Authority - password in AD - 2-way sync use case is selected. - JC as Authority - one-way sync from AD. - Two way sync -  2-way sync use case is selected   #### Sample Request &#x60;&#x60;&#x60; curl -X PATCH https://console.jumpcloud.com/api/v2/activedirectories/{Domain_ID} \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{         \&quot;domain\&quot;: \&quot;{DC&#x3D;DOMAIN;DC&#x3D;COM}\&quot;,         \&quot;use_case\&quot;: \&quot;{ADASAUTHORITY}\&quot;     }&#39; \\ &#x60;&#x60;&#x60;
     * @param id ObjectID of this Active Directory instance. (required)
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
    private okhttp3.Call postCall(String xOrgId, ActiveDirectory activeDirectory, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = activeDirectory;

        // create path and map variables
        String localVarPath = "/activedirectories";

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
    private okhttp3.Call postValidateBeforeCall(String xOrgId, ActiveDirectory activeDirectory, final ApiCallback _callback) throws ApiException {
        return postCall(xOrgId, activeDirectory, _callback);

    }


    private ApiResponse<ActiveDirectory> postWithHttpInfo(String xOrgId, ActiveDirectory activeDirectory) throws ApiException {
        okhttp3.Call localVarCall = postValidateBeforeCall(xOrgId, activeDirectory, null);
        Type localVarReturnType = new TypeToken<ActiveDirectory>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call postAsync(String xOrgId, ActiveDirectory activeDirectory, final ApiCallback<ActiveDirectory> _callback) throws ApiException {

        okhttp3.Call localVarCall = postValidateBeforeCall(xOrgId, activeDirectory, _callback);
        Type localVarReturnType = new TypeToken<ActiveDirectory>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class PostRequestBuilder {
        private String domain;
        private String id;
        private String primaryAgent;
        private String useCase;
        private String xOrgId;

        private PostRequestBuilder() {
        }

        /**
         * Set domain
         * @param domain Domain name for this Active Directory instance. (optional)
         * @return PostRequestBuilder
         */
        public PostRequestBuilder domain(String domain) {
            this.domain = domain;
            return this;
        }
        
        /**
         * Set id
         * @param id ObjectID of this Active Directory instance. (optional)
         * @return PostRequestBuilder
         */
        public PostRequestBuilder id(String id) {
            this.id = id;
            return this;
        }
        
        /**
         * Set primaryAgent
         * @param primaryAgent ObjectID of the primary agent of domain. (optional)
         * @return PostRequestBuilder
         */
        public PostRequestBuilder primaryAgent(String primaryAgent) {
            this.primaryAgent = primaryAgent;
            return this;
        }
        
        /**
         * Set useCase
         * @param useCase  (optional)
         * @return PostRequestBuilder
         */
        public PostRequestBuilder useCase(String useCase) {
            this.useCase = useCase;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return PostRequestBuilder
         */
        public PostRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for post
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
            ActiveDirectory activeDirectory = buildBodyParams();
            return postCall(xOrgId, activeDirectory, _callback);
        }

        private ActiveDirectory buildBodyParams() {
            ActiveDirectory activeDirectory = new ActiveDirectory();
            activeDirectory.domain(this.domain);
            activeDirectory.id(this.id);
            activeDirectory.primaryAgent(this.primaryAgent);
            if (this.useCase != null)
            activeDirectory.useCase(ActiveDirectory.UseCaseEnum.fromValue(this.useCase));
            return activeDirectory;
        }

        /**
         * Execute post request
         * @return ActiveDirectory
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ActiveDirectory execute() throws ApiException {
            ActiveDirectory activeDirectory = buildBodyParams();
            ApiResponse<ActiveDirectory> localVarResp = postWithHttpInfo(xOrgId, activeDirectory);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute post request with HTTP info returned
         * @return ApiResponse&lt;ActiveDirectory&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<ActiveDirectory> executeWithHttpInfo() throws ApiException {
            ActiveDirectory activeDirectory = buildBodyParams();
            return postWithHttpInfo(xOrgId, activeDirectory);
        }

        /**
         * Execute post request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ActiveDirectory> _callback) throws ApiException {
            ActiveDirectory activeDirectory = buildBodyParams();
            return postAsync(xOrgId, activeDirectory, _callback);
        }
    }

    /**
     * Create a new Active Directory
     * This endpoint allows you to create a new Active Directory.   #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/activedirectories/ \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;domain\&quot;: \&quot;{DC&#x3D;AD_domain_name;DC&#x3D;com}\&quot;   }&#39; &#x60;&#x60;&#x60;
     * @return PostRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public PostRequestBuilder post() throws IllegalArgumentException {
        return new PostRequestBuilder();
    }
}
