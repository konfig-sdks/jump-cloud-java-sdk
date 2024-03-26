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


import com.konfigthis.client.model.DefaultDomain;
import com.konfigthis.client.model.GSuiteBuiltinTranslation;
import com.konfigthis.client.model.GSuiteDirectionTranslation;
import com.konfigthis.client.model.GSuiteTranslationRule;
import com.konfigthis.client.model.GSuiteTranslationRuleRequest;
import com.konfigthis.client.model.GoogleRpcStatus;
import com.konfigthis.client.model.GraphConnection;
import com.konfigthis.client.model.GraphObjectWithPaths;
import com.konfigthis.client.model.GraphOperationGSuite;
import com.konfigthis.client.model.Gsuite;
import com.konfigthis.client.model.GsuitesListImportJumpcloudUsersResponse;
import com.konfigthis.client.model.GsuitesListImportUsersResponse;
import com.konfigthis.client.model.JumpcloudGappsDomainListResponse;
import com.konfigthis.client.model.JumpcloudGappsDomainResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;

public class GSuiteApiGenerated {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public GSuiteApiGenerated() throws IllegalArgumentException {
        this(Configuration.getDefaultApiClient());
    }

    public GSuiteApiGenerated(ApiClient apiClient) throws IllegalArgumentException {
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

    private okhttp3.Call addDomainCall(byte[] gsuiteId, String domain, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/gsuites/{gsuite_id}/domains"
            .replace("{" + "gsuite_id" + "}", localVarApiClient.escapeString(gsuiteId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (domain != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("domain", domain));
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
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call addDomainValidateBeforeCall(byte[] gsuiteId, String domain, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'gsuiteId' is set
        if (gsuiteId == null) {
            throw new ApiException("Missing the required parameter 'gsuiteId' when calling addDomain(Async)");
        }

        return addDomainCall(gsuiteId, domain, _callback);

    }


    private ApiResponse<JumpcloudGappsDomainResponse> addDomainWithHttpInfo(byte[] gsuiteId, String domain) throws ApiException {
        okhttp3.Call localVarCall = addDomainValidateBeforeCall(gsuiteId, domain, null);
        Type localVarReturnType = new TypeToken<JumpcloudGappsDomainResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call addDomainAsync(byte[] gsuiteId, String domain, final ApiCallback<JumpcloudGappsDomainResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = addDomainValidateBeforeCall(gsuiteId, domain, _callback);
        Type localVarReturnType = new TypeToken<JumpcloudGappsDomainResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class AddDomainRequestBuilder {
        private final byte[] gsuiteId;
        private String domain;

        private AddDomainRequestBuilder(byte[] gsuiteId) {
            this.gsuiteId = gsuiteId;
        }

        /**
         * Set domain
         * @param domain  (optional)
         * @return AddDomainRequestBuilder
         */
        public AddDomainRequestBuilder domain(String domain) {
            this.domain = domain;
            return this;
        }
        
        /**
         * Build call for addDomain
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
            <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return addDomainCall(gsuiteId, domain, _callback);
        }


        /**
         * Execute addDomain request
         * @return JumpcloudGappsDomainResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
            <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public JumpcloudGappsDomainResponse execute() throws ApiException {
            ApiResponse<JumpcloudGappsDomainResponse> localVarResp = addDomainWithHttpInfo(gsuiteId, domain);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute addDomain request with HTTP info returned
         * @return ApiResponse&lt;JumpcloudGappsDomainResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
            <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<JumpcloudGappsDomainResponse> executeWithHttpInfo() throws ApiException {
            return addDomainWithHttpInfo(gsuiteId, domain);
        }

        /**
         * Execute addDomain request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
            <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<JumpcloudGappsDomainResponse> _callback) throws ApiException {
            return addDomainAsync(gsuiteId, domain, _callback);
        }
    }

    /**
     * Add a domain to a Google Workspace integration instance
     * Add a domain to a specific Google Workspace directory sync integration instance. The domain must be a verified domain in Google Workspace.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/gsuites/{gsuite_id}/domains \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{\&quot;domain\&quot;: \&quot;{domain name}\&quot;}&#39; &#x60;&#x60;&#x60;
     * @param gsuiteId Id for the specific Google Workspace directory sync integration instance. (required)
     * @return AddDomainRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
        <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
        <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
     </table>
     */
    public AddDomainRequestBuilder addDomain(byte[] gsuiteId) throws IllegalArgumentException {
        if (gsuiteId == null) throw new IllegalArgumentException("\"gsuiteId\" is required but got null");
        return new AddDomainRequestBuilder(gsuiteId);
    }
    private okhttp3.Call configuredDomainsListCall(byte[] gsuiteId, String limit, String skip, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/gsuites/{gsuite_id}/domains"
            .replace("{" + "gsuite_id" + "}", localVarApiClient.escapeString(gsuiteId.toString()));

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
    private okhttp3.Call configuredDomainsListValidateBeforeCall(byte[] gsuiteId, String limit, String skip, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'gsuiteId' is set
        if (gsuiteId == null) {
            throw new ApiException("Missing the required parameter 'gsuiteId' when calling configuredDomainsList(Async)");
        }

        return configuredDomainsListCall(gsuiteId, limit, skip, _callback);

    }


    private ApiResponse<JumpcloudGappsDomainListResponse> configuredDomainsListWithHttpInfo(byte[] gsuiteId, String limit, String skip) throws ApiException {
        okhttp3.Call localVarCall = configuredDomainsListValidateBeforeCall(gsuiteId, limit, skip, null);
        Type localVarReturnType = new TypeToken<JumpcloudGappsDomainListResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call configuredDomainsListAsync(byte[] gsuiteId, String limit, String skip, final ApiCallback<JumpcloudGappsDomainListResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = configuredDomainsListValidateBeforeCall(gsuiteId, limit, skip, _callback);
        Type localVarReturnType = new TypeToken<JumpcloudGappsDomainListResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ConfiguredDomainsListRequestBuilder {
        private final byte[] gsuiteId;
        private String limit;
        private String skip;

        private ConfiguredDomainsListRequestBuilder(byte[] gsuiteId) {
            this.gsuiteId = gsuiteId;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 100)
         * @return ConfiguredDomainsListRequestBuilder
         */
        public ConfiguredDomainsListRequestBuilder limit(String limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ConfiguredDomainsListRequestBuilder
         */
        public ConfiguredDomainsListRequestBuilder skip(String skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Build call for configuredDomainsList
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return configuredDomainsListCall(gsuiteId, limit, skip, _callback);
        }


        /**
         * Execute configuredDomainsList request
         * @return JumpcloudGappsDomainListResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public JumpcloudGappsDomainListResponse execute() throws ApiException {
            ApiResponse<JumpcloudGappsDomainListResponse> localVarResp = configuredDomainsListWithHttpInfo(gsuiteId, limit, skip);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute configuredDomainsList request with HTTP info returned
         * @return ApiResponse&lt;JumpcloudGappsDomainListResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<JumpcloudGappsDomainListResponse> executeWithHttpInfo() throws ApiException {
            return configuredDomainsListWithHttpInfo(gsuiteId, limit, skip);
        }

        /**
         * Execute configuredDomainsList request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<JumpcloudGappsDomainListResponse> _callback) throws ApiException {
            return configuredDomainsListAsync(gsuiteId, limit, skip, _callback);
        }
    }

    /**
     * List all domains configured for the Google Workspace integration instance
     * List the domains configured for a specific Google Workspace directory sync integration instance.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/gsuites/{gsuite_id}/domains \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param gsuiteId Id for the specific Google Workspace directory sync integration instance.. (required)
     * @return ConfiguredDomainsListRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
        <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
     </table>
     */
    public ConfiguredDomainsListRequestBuilder configuredDomainsList(byte[] gsuiteId) throws IllegalArgumentException {
        if (gsuiteId == null) throw new IllegalArgumentException("\"gsuiteId\" is required but got null");
        return new ConfiguredDomainsListRequestBuilder(gsuiteId);
    }
    private okhttp3.Call deleteDomainCall(byte[] gsuiteId, byte[] domainId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/gsuites/{gsuite_id}/domains/{domainId}"
            .replace("{" + "gsuite_id" + "}", localVarApiClient.escapeString(gsuiteId.toString()))
            .replace("{" + "domainId" + "}", localVarApiClient.escapeString(domainId.toString()));

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
    private okhttp3.Call deleteDomainValidateBeforeCall(byte[] gsuiteId, byte[] domainId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'gsuiteId' is set
        if (gsuiteId == null) {
            throw new ApiException("Missing the required parameter 'gsuiteId' when calling deleteDomain(Async)");
        }

        // verify the required parameter 'domainId' is set
        if (domainId == null) {
            throw new ApiException("Missing the required parameter 'domainId' when calling deleteDomain(Async)");
        }

        return deleteDomainCall(gsuiteId, domainId, _callback);

    }


    private ApiResponse<JumpcloudGappsDomainResponse> deleteDomainWithHttpInfo(byte[] gsuiteId, byte[] domainId) throws ApiException {
        okhttp3.Call localVarCall = deleteDomainValidateBeforeCall(gsuiteId, domainId, null);
        Type localVarReturnType = new TypeToken<JumpcloudGappsDomainResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call deleteDomainAsync(byte[] gsuiteId, byte[] domainId, final ApiCallback<JumpcloudGappsDomainResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteDomainValidateBeforeCall(gsuiteId, domainId, _callback);
        Type localVarReturnType = new TypeToken<JumpcloudGappsDomainResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class DeleteDomainRequestBuilder {
        private final byte[] gsuiteId;
        private final byte[] domainId;

        private DeleteDomainRequestBuilder(byte[] gsuiteId, byte[] domainId) {
            this.gsuiteId = gsuiteId;
            this.domainId = domainId;
        }

        /**
         * Build call for deleteDomain
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return deleteDomainCall(gsuiteId, domainId, _callback);
        }


        /**
         * Execute deleteDomain request
         * @return JumpcloudGappsDomainResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public JumpcloudGappsDomainResponse execute() throws ApiException {
            ApiResponse<JumpcloudGappsDomainResponse> localVarResp = deleteDomainWithHttpInfo(gsuiteId, domainId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute deleteDomain request with HTTP info returned
         * @return ApiResponse&lt;JumpcloudGappsDomainResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<JumpcloudGappsDomainResponse> executeWithHttpInfo() throws ApiException {
            return deleteDomainWithHttpInfo(gsuiteId, domainId);
        }

        /**
         * Execute deleteDomain request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<JumpcloudGappsDomainResponse> _callback) throws ApiException {
            return deleteDomainAsync(gsuiteId, domainId, _callback);
        }
    }

    /**
     * Delete a domain from a Google Workspace integration instance
     * Delete a domain from a specific Google Workspace directory sync integration instance.  #### Sample Request &#x60;&#x60;&#x60; curl -X DELETE https://console.jumpcloud.com/api/v2/gsuites/{gsuite_id}/domains/{domainId} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param gsuiteId Id for the specific Google Workspace directory sync integration instance. (required)
     * @param domainId Id for the domain. (required)
     * @return DeleteDomainRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
        <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
     </table>
     */
    public DeleteDomainRequestBuilder deleteDomain(byte[] gsuiteId, byte[] domainId) throws IllegalArgumentException {
        if (gsuiteId == null) throw new IllegalArgumentException("\"gsuiteId\" is required but got null");
        if (domainId == null) throw new IllegalArgumentException("\"domainId\" is required but got null");
        return new DeleteDomainRequestBuilder(gsuiteId, domainId);
    }
    private okhttp3.Call gSuiteAssociationsListCall(String gsuiteId, List<String> targets, Integer limit, Integer skip, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/gsuites/{gsuite_id}/associations"
            .replace("{" + "gsuite_id" + "}", localVarApiClient.escapeString(gsuiteId.toString()));

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
    private okhttp3.Call gSuiteAssociationsListValidateBeforeCall(String gsuiteId, List<String> targets, Integer limit, Integer skip, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'gsuiteId' is set
        if (gsuiteId == null) {
            throw new ApiException("Missing the required parameter 'gsuiteId' when calling gSuiteAssociationsList(Async)");
        }

        // verify the required parameter 'targets' is set
        if (targets == null) {
            throw new ApiException("Missing the required parameter 'targets' when calling gSuiteAssociationsList(Async)");
        }

        return gSuiteAssociationsListCall(gsuiteId, targets, limit, skip, xOrgId, _callback);

    }


    private ApiResponse<List<GraphConnection>> gSuiteAssociationsListWithHttpInfo(String gsuiteId, List<String> targets, Integer limit, Integer skip, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = gSuiteAssociationsListValidateBeforeCall(gsuiteId, targets, limit, skip, xOrgId, null);
        Type localVarReturnType = new TypeToken<List<GraphConnection>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call gSuiteAssociationsListAsync(String gsuiteId, List<String> targets, Integer limit, Integer skip, String xOrgId, final ApiCallback<List<GraphConnection>> _callback) throws ApiException {

        okhttp3.Call localVarCall = gSuiteAssociationsListValidateBeforeCall(gsuiteId, targets, limit, skip, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<List<GraphConnection>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GSuiteAssociationsListRequestBuilder {
        private final String gsuiteId;
        private final List<String> targets;
        private Integer limit;
        private Integer skip;
        private String xOrgId;

        private GSuiteAssociationsListRequestBuilder(String gsuiteId, List<String> targets) {
            this.gsuiteId = gsuiteId;
            this.targets = targets;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return GSuiteAssociationsListRequestBuilder
         */
        public GSuiteAssociationsListRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return GSuiteAssociationsListRequestBuilder
         */
        public GSuiteAssociationsListRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return GSuiteAssociationsListRequestBuilder
         */
        public GSuiteAssociationsListRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for gSuiteAssociationsList
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
            return gSuiteAssociationsListCall(gsuiteId, targets, limit, skip, xOrgId, _callback);
        }


        /**
         * Execute gSuiteAssociationsList request
         * @return List&lt;GraphConnection&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphConnection> execute() throws ApiException {
            ApiResponse<List<GraphConnection>> localVarResp = gSuiteAssociationsListWithHttpInfo(gsuiteId, targets, limit, skip, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute gSuiteAssociationsList request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphConnection&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphConnection>> executeWithHttpInfo() throws ApiException {
            return gSuiteAssociationsListWithHttpInfo(gsuiteId, targets, limit, skip, xOrgId);
        }

        /**
         * Execute gSuiteAssociationsList request (asynchronously)
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
            return gSuiteAssociationsListAsync(gsuiteId, targets, limit, skip, xOrgId, _callback);
        }
    }

    /**
     * List the associations of a G Suite instance
     * This endpoint returns the _direct_ associations of this G Suite instance.  A direct association can be a non-homogeneous relationship between 2 different objects, for example G Suite and Users.   #### Sample Request &#x60;&#x60;&#x60; curl -X GET &#39;https://console.jumpcloud.com/api/v2/gsuites/{Gsuite_ID}/associations?targets&#x3D;user_group \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param gsuiteId ObjectID of the G Suite instance. (required)
     * @param targets Targets which a \&quot;g_suite\&quot; can be associated to. (required)
     * @return GSuiteAssociationsListRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public GSuiteAssociationsListRequestBuilder gSuiteAssociationsList(String gsuiteId, List<String> targets) throws IllegalArgumentException {
        if (gsuiteId == null) throw new IllegalArgumentException("\"gsuiteId\" is required but got null");
            

        if (targets == null) throw new IllegalArgumentException("\"targets\" is required but got null");
        return new GSuiteAssociationsListRequestBuilder(gsuiteId, targets);
    }
    private okhttp3.Call gSuiteAssociationsPostCall(String gsuiteId, String xOrgId, GraphOperationGSuite graphOperationGSuite, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = graphOperationGSuite;

        // create path and map variables
        String localVarPath = "/gsuites/{gsuite_id}/associations"
            .replace("{" + "gsuite_id" + "}", localVarApiClient.escapeString(gsuiteId.toString()));

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
    private okhttp3.Call gSuiteAssociationsPostValidateBeforeCall(String gsuiteId, String xOrgId, GraphOperationGSuite graphOperationGSuite, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'gsuiteId' is set
        if (gsuiteId == null) {
            throw new ApiException("Missing the required parameter 'gsuiteId' when calling gSuiteAssociationsPost(Async)");
        }

        return gSuiteAssociationsPostCall(gsuiteId, xOrgId, graphOperationGSuite, _callback);

    }


    private ApiResponse<Void> gSuiteAssociationsPostWithHttpInfo(String gsuiteId, String xOrgId, GraphOperationGSuite graphOperationGSuite) throws ApiException {
        okhttp3.Call localVarCall = gSuiteAssociationsPostValidateBeforeCall(gsuiteId, xOrgId, graphOperationGSuite, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call gSuiteAssociationsPostAsync(String gsuiteId, String xOrgId, GraphOperationGSuite graphOperationGSuite, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = gSuiteAssociationsPostValidateBeforeCall(gsuiteId, xOrgId, graphOperationGSuite, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class GSuiteAssociationsPostRequestBuilder {
        private final String gsuiteId;
        private String id;
        private String op;
        private Map<String, Object> attributes;
        private String type;
        private String xOrgId;

        private GSuiteAssociationsPostRequestBuilder(String gsuiteId) {
            this.gsuiteId = gsuiteId;
        }

        /**
         * Set id
         * @param id The ObjectID of graph object being added or removed as an association. (optional)
         * @return GSuiteAssociationsPostRequestBuilder
         */
        public GSuiteAssociationsPostRequestBuilder id(String id) {
            this.id = id;
            return this;
        }
        
        /**
         * Set op
         * @param op How to modify the graph connection. (optional)
         * @return GSuiteAssociationsPostRequestBuilder
         */
        public GSuiteAssociationsPostRequestBuilder op(String op) {
            this.op = op;
            return this;
        }
        
        /**
         * Set attributes
         * @param attributes The graph attributes. (optional)
         * @return GSuiteAssociationsPostRequestBuilder
         */
        public GSuiteAssociationsPostRequestBuilder attributes(Map<String, Object> attributes) {
            this.attributes = attributes;
            return this;
        }
        
        /**
         * Set type
         * @param type Targets which a \\\&quot;g_suite\\\&quot; can be associated to. (optional)
         * @return GSuiteAssociationsPostRequestBuilder
         */
        public GSuiteAssociationsPostRequestBuilder type(String type) {
            this.type = type;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return GSuiteAssociationsPostRequestBuilder
         */
        public GSuiteAssociationsPostRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for gSuiteAssociationsPost
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
            GraphOperationGSuite graphOperationGSuite = buildBodyParams();
            return gSuiteAssociationsPostCall(gsuiteId, xOrgId, graphOperationGSuite, _callback);
        }

        private GraphOperationGSuite buildBodyParams() {
            GraphOperationGSuite graphOperationGSuite = new GraphOperationGSuite();
            return graphOperationGSuite;
        }

        /**
         * Execute gSuiteAssociationsPost request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            GraphOperationGSuite graphOperationGSuite = buildBodyParams();
            gSuiteAssociationsPostWithHttpInfo(gsuiteId, xOrgId, graphOperationGSuite);
        }

        /**
         * Execute gSuiteAssociationsPost request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            GraphOperationGSuite graphOperationGSuite = buildBodyParams();
            return gSuiteAssociationsPostWithHttpInfo(gsuiteId, xOrgId, graphOperationGSuite);
        }

        /**
         * Execute gSuiteAssociationsPost request (asynchronously)
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
            GraphOperationGSuite graphOperationGSuite = buildBodyParams();
            return gSuiteAssociationsPostAsync(gsuiteId, xOrgId, graphOperationGSuite, _callback);
        }
    }

    /**
     * Manage the associations of a G Suite instance
     * This endpoint returns the _direct_ associations of this G Suite instance.  A direct association can be a non-homogeneous relationship between 2 different objects, for example G Suite and Users.   #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/gsuites/{Gsuite_ID}/associations \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;op\&quot;: \&quot;add\&quot;,     \&quot;type\&quot;: \&quot;user_group\&quot;,     \&quot;id\&quot;: \&quot;{Group_ID}\&quot;   }&#39; &#x60;&#x60;&#x60;
     * @param gsuiteId ObjectID of the G Suite instance. (required)
     * @return GSuiteAssociationsPostRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public GSuiteAssociationsPostRequestBuilder gSuiteAssociationsPost(String gsuiteId) throws IllegalArgumentException {
        if (gsuiteId == null) throw new IllegalArgumentException("\"gsuiteId\" is required but got null");
            

        return new GSuiteAssociationsPostRequestBuilder(gsuiteId);
    }
    private okhttp3.Call gSuiteDeleteCall(String gsuiteId, String id, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/gsuites/{gsuite_id}/translationrules/{id}"
            .replace("{" + "gsuite_id" + "}", localVarApiClient.escapeString(gsuiteId.toString()))
            .replace("{" + "id" + "}", localVarApiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

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
    private okhttp3.Call gSuiteDeleteValidateBeforeCall(String gsuiteId, String id, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'gsuiteId' is set
        if (gsuiteId == null) {
            throw new ApiException("Missing the required parameter 'gsuiteId' when calling gSuiteDelete(Async)");
        }

        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling gSuiteDelete(Async)");
        }

        return gSuiteDeleteCall(gsuiteId, id, _callback);

    }


    private ApiResponse<Void> gSuiteDeleteWithHttpInfo(String gsuiteId, String id) throws ApiException {
        okhttp3.Call localVarCall = gSuiteDeleteValidateBeforeCall(gsuiteId, id, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call gSuiteDeleteAsync(String gsuiteId, String id, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = gSuiteDeleteValidateBeforeCall(gsuiteId, id, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class GSuiteDeleteRequestBuilder {
        private final String gsuiteId;
        private final String id;

        private GSuiteDeleteRequestBuilder(String gsuiteId, String id) {
            this.gsuiteId = gsuiteId;
            this.id = id;
        }

        /**
         * Build call for gSuiteDelete
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
            return gSuiteDeleteCall(gsuiteId, id, _callback);
        }


        /**
         * Execute gSuiteDelete request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            gSuiteDeleteWithHttpInfo(gsuiteId, id);
        }

        /**
         * Execute gSuiteDelete request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            return gSuiteDeleteWithHttpInfo(gsuiteId, id);
        }

        /**
         * Execute gSuiteDelete request (asynchronously)
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
            return gSuiteDeleteAsync(gsuiteId, id, _callback);
        }
    }

    /**
     * Deletes a G Suite translation rule
     * This endpoint allows you to delete a translation rule for a specific G Suite instance. These rules specify how JumpCloud attributes translate to [G Suite Admin SDK](https://developers.google.com/admin-sdk/directory/) attributes.  #### Sample Request  &#x60;&#x60;&#x60; curl -X DELETE https://console.jumpcloud.com/api/v2/gsuites/{gsuite_id}/translationrules/{id} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;   &#x60;&#x60;&#x60;
     * @param gsuiteId  (required)
     * @param id  (required)
     * @return GSuiteDeleteRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public GSuiteDeleteRequestBuilder gSuiteDelete(String gsuiteId, String id) throws IllegalArgumentException {
        if (gsuiteId == null) throw new IllegalArgumentException("\"gsuiteId\" is required but got null");
            

        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new GSuiteDeleteRequestBuilder(gsuiteId, id);
    }
    private okhttp3.Call gSuiteGetCall(String gsuiteId, String id, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/gsuites/{gsuite_id}/translationrules/{id}"
            .replace("{" + "gsuite_id" + "}", localVarApiClient.escapeString(gsuiteId.toString()))
            .replace("{" + "id" + "}", localVarApiClient.escapeString(id.toString()));

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
    private okhttp3.Call gSuiteGetValidateBeforeCall(String gsuiteId, String id, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'gsuiteId' is set
        if (gsuiteId == null) {
            throw new ApiException("Missing the required parameter 'gsuiteId' when calling gSuiteGet(Async)");
        }

        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling gSuiteGet(Async)");
        }

        return gSuiteGetCall(gsuiteId, id, _callback);

    }


    private ApiResponse<GSuiteTranslationRule> gSuiteGetWithHttpInfo(String gsuiteId, String id) throws ApiException {
        okhttp3.Call localVarCall = gSuiteGetValidateBeforeCall(gsuiteId, id, null);
        Type localVarReturnType = new TypeToken<GSuiteTranslationRule>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call gSuiteGetAsync(String gsuiteId, String id, final ApiCallback<GSuiteTranslationRule> _callback) throws ApiException {

        okhttp3.Call localVarCall = gSuiteGetValidateBeforeCall(gsuiteId, id, _callback);
        Type localVarReturnType = new TypeToken<GSuiteTranslationRule>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GSuiteGetRequestBuilder {
        private final String gsuiteId;
        private final String id;

        private GSuiteGetRequestBuilder(String gsuiteId, String id) {
            this.gsuiteId = gsuiteId;
            this.id = id;
        }

        /**
         * Build call for gSuiteGet
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
            return gSuiteGetCall(gsuiteId, id, _callback);
        }


        /**
         * Execute gSuiteGet request
         * @return GSuiteTranslationRule
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public GSuiteTranslationRule execute() throws ApiException {
            ApiResponse<GSuiteTranslationRule> localVarResp = gSuiteGetWithHttpInfo(gsuiteId, id);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute gSuiteGet request with HTTP info returned
         * @return ApiResponse&lt;GSuiteTranslationRule&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<GSuiteTranslationRule> executeWithHttpInfo() throws ApiException {
            return gSuiteGetWithHttpInfo(gsuiteId, id);
        }

        /**
         * Execute gSuiteGet request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<GSuiteTranslationRule> _callback) throws ApiException {
            return gSuiteGetAsync(gsuiteId, id, _callback);
        }
    }

    /**
     * Gets a specific G Suite translation rule
     * This endpoint returns a specific translation rule for a specific G Suite instance. These rules specify how JumpCloud attributes translate to [G Suite Admin SDK](https://developers.google.com/admin-sdk/directory/) attributes.  ###### Sample Request  &#x60;&#x60;&#x60;   curl -X GET https://console.jumpcloud.com/api/v2/gsuites/{gsuite_id}/translationrules/{id} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;   &#x60;&#x60;&#x60;
     * @param gsuiteId  (required)
     * @param id  (required)
     * @return GSuiteGetRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public GSuiteGetRequestBuilder gSuiteGet(String gsuiteId, String id) throws IllegalArgumentException {
        if (gsuiteId == null) throw new IllegalArgumentException("\"gsuiteId\" is required but got null");
            

        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new GSuiteGetRequestBuilder(gsuiteId, id);
    }
    private okhttp3.Call gSuiteListCall(String gsuiteId, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/gsuites/{gsuite_id}/translationrules"
            .replace("{" + "gsuite_id" + "}", localVarApiClient.escapeString(gsuiteId.toString()));

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
    private okhttp3.Call gSuiteListValidateBeforeCall(String gsuiteId, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'gsuiteId' is set
        if (gsuiteId == null) {
            throw new ApiException("Missing the required parameter 'gsuiteId' when calling gSuiteList(Async)");
        }

        return gSuiteListCall(gsuiteId, fields, filter, limit, skip, sort, _callback);

    }


    private ApiResponse<List<GSuiteTranslationRule>> gSuiteListWithHttpInfo(String gsuiteId, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort) throws ApiException {
        okhttp3.Call localVarCall = gSuiteListValidateBeforeCall(gsuiteId, fields, filter, limit, skip, sort, null);
        Type localVarReturnType = new TypeToken<List<GSuiteTranslationRule>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call gSuiteListAsync(String gsuiteId, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback<List<GSuiteTranslationRule>> _callback) throws ApiException {

        okhttp3.Call localVarCall = gSuiteListValidateBeforeCall(gsuiteId, fields, filter, limit, skip, sort, _callback);
        Type localVarReturnType = new TypeToken<List<GSuiteTranslationRule>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GSuiteListRequestBuilder {
        private final String gsuiteId;
        private List<String> fields;
        private List<String> filter;
        private Integer limit;
        private Integer skip;
        private List<String> sort;

        private GSuiteListRequestBuilder(String gsuiteId) {
            this.gsuiteId = gsuiteId;
        }

        /**
         * Set fields
         * @param fields The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  (optional)
         * @return GSuiteListRequestBuilder
         */
        public GSuiteListRequestBuilder fields(List<String> fields) {
            this.fields = fields;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return GSuiteListRequestBuilder
         */
        public GSuiteListRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return GSuiteListRequestBuilder
         */
        public GSuiteListRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return GSuiteListRequestBuilder
         */
        public GSuiteListRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  (optional)
         * @return GSuiteListRequestBuilder
         */
        public GSuiteListRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Build call for gSuiteList
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
            return gSuiteListCall(gsuiteId, fields, filter, limit, skip, sort, _callback);
        }


        /**
         * Execute gSuiteList request
         * @return List&lt;GSuiteTranslationRule&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<GSuiteTranslationRule> execute() throws ApiException {
            ApiResponse<List<GSuiteTranslationRule>> localVarResp = gSuiteListWithHttpInfo(gsuiteId, fields, filter, limit, skip, sort);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute gSuiteList request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GSuiteTranslationRule&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GSuiteTranslationRule>> executeWithHttpInfo() throws ApiException {
            return gSuiteListWithHttpInfo(gsuiteId, fields, filter, limit, skip, sort);
        }

        /**
         * Execute gSuiteList request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<GSuiteTranslationRule>> _callback) throws ApiException {
            return gSuiteListAsync(gsuiteId, fields, filter, limit, skip, sort, _callback);
        }
    }

    /**
     * List all the G Suite Translation Rules
     * This endpoint returns all graph translation rules for a specific G Suite instance. These rules specify how JumpCloud attributes translate to [G Suite Admin SDK](https://developers.google.com/admin-sdk/directory/) attributes.  ##### Sample Request  &#x60;&#x60;&#x60; curl -X GET  https://console.jumpcloud.com/api/v2/gsuites/{gsuite_id}/translationrules \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;   &#x60;&#x60;&#x60;
     * @param gsuiteId  (required)
     * @return GSuiteListRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public GSuiteListRequestBuilder gSuiteList(String gsuiteId) throws IllegalArgumentException {
        if (gsuiteId == null) throw new IllegalArgumentException("\"gsuiteId\" is required but got null");
            

        return new GSuiteListRequestBuilder(gsuiteId);
    }
    private okhttp3.Call gSuitePostCall(String gsuiteId, GSuiteTranslationRuleRequest gsuiteTranslationRuleRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = gsuiteTranslationRuleRequest;

        // create path and map variables
        String localVarPath = "/gsuites/{gsuite_id}/translationrules"
            .replace("{" + "gsuite_id" + "}", localVarApiClient.escapeString(gsuiteId.toString()));

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
    private okhttp3.Call gSuitePostValidateBeforeCall(String gsuiteId, GSuiteTranslationRuleRequest gsuiteTranslationRuleRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'gsuiteId' is set
        if (gsuiteId == null) {
            throw new ApiException("Missing the required parameter 'gsuiteId' when calling gSuitePost(Async)");
        }

        return gSuitePostCall(gsuiteId, gsuiteTranslationRuleRequest, _callback);

    }


    private ApiResponse<GSuiteTranslationRule> gSuitePostWithHttpInfo(String gsuiteId, GSuiteTranslationRuleRequest gsuiteTranslationRuleRequest) throws ApiException {
        okhttp3.Call localVarCall = gSuitePostValidateBeforeCall(gsuiteId, gsuiteTranslationRuleRequest, null);
        Type localVarReturnType = new TypeToken<GSuiteTranslationRule>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call gSuitePostAsync(String gsuiteId, GSuiteTranslationRuleRequest gsuiteTranslationRuleRequest, final ApiCallback<GSuiteTranslationRule> _callback) throws ApiException {

        okhttp3.Call localVarCall = gSuitePostValidateBeforeCall(gsuiteId, gsuiteTranslationRuleRequest, _callback);
        Type localVarReturnType = new TypeToken<GSuiteTranslationRule>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GSuitePostRequestBuilder {
        private final String gsuiteId;
        private GSuiteBuiltinTranslation builtIn;
        private GSuiteDirectionTranslation direction;

        private GSuitePostRequestBuilder(String gsuiteId) {
            this.gsuiteId = gsuiteId;
        }

        /**
         * Set builtIn
         * @param builtIn  (optional)
         * @return GSuitePostRequestBuilder
         */
        public GSuitePostRequestBuilder builtIn(GSuiteBuiltinTranslation builtIn) {
            this.builtIn = builtIn;
            return this;
        }
        
        /**
         * Set direction
         * @param direction  (optional, default to export)
         * @return GSuitePostRequestBuilder
         */
        public GSuitePostRequestBuilder direction(GSuiteDirectionTranslation direction) {
            this.direction = direction;
            return this;
        }
        
        /**
         * Build call for gSuitePost
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
            GSuiteTranslationRuleRequest gsuiteTranslationRuleRequest = buildBodyParams();
            return gSuitePostCall(gsuiteId, gsuiteTranslationRuleRequest, _callback);
        }

        private GSuiteTranslationRuleRequest buildBodyParams() {
            GSuiteTranslationRuleRequest gsuiteTranslationRuleRequest = new GSuiteTranslationRuleRequest();
            gsuiteTranslationRuleRequest.builtIn(this.builtIn);
            gsuiteTranslationRuleRequest.direction(this.direction);
            return gsuiteTranslationRuleRequest;
        }

        /**
         * Execute gSuitePost request
         * @return GSuiteTranslationRule
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public GSuiteTranslationRule execute() throws ApiException {
            GSuiteTranslationRuleRequest gsuiteTranslationRuleRequest = buildBodyParams();
            ApiResponse<GSuiteTranslationRule> localVarResp = gSuitePostWithHttpInfo(gsuiteId, gsuiteTranslationRuleRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute gSuitePost request with HTTP info returned
         * @return ApiResponse&lt;GSuiteTranslationRule&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<GSuiteTranslationRule> executeWithHttpInfo() throws ApiException {
            GSuiteTranslationRuleRequest gsuiteTranslationRuleRequest = buildBodyParams();
            return gSuitePostWithHttpInfo(gsuiteId, gsuiteTranslationRuleRequest);
        }

        /**
         * Execute gSuitePost request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<GSuiteTranslationRule> _callback) throws ApiException {
            GSuiteTranslationRuleRequest gsuiteTranslationRuleRequest = buildBodyParams();
            return gSuitePostAsync(gsuiteId, gsuiteTranslationRuleRequest, _callback);
        }
    }

    /**
     * Create a new G Suite Translation Rule
     * This endpoint allows you to create a translation rule for a specific G Suite instance. These rules specify how JumpCloud attributes translate to [G Suite Admin SDK](https://developers.google.com/admin-sdk/directory/) attributes.  ##### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/gsuites/{gsuite_id}/translationrules \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     {Translation Rule Parameters}   }&#39; &#x60;&#x60;&#x60;
     * @param gsuiteId  (required)
     * @return GSuitePostRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public GSuitePostRequestBuilder gSuitePost(String gsuiteId) throws IllegalArgumentException {
        if (gsuiteId == null) throw new IllegalArgumentException("\"gsuiteId\" is required but got null");
            

        return new GSuitePostRequestBuilder(gsuiteId);
    }
    private okhttp3.Call gSuiteTraverseUserCall(String gsuiteId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/gsuites/{gsuite_id}/users"
            .replace("{" + "gsuite_id" + "}", localVarApiClient.escapeString(gsuiteId.toString()));

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
    private okhttp3.Call gSuiteTraverseUserValidateBeforeCall(String gsuiteId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'gsuiteId' is set
        if (gsuiteId == null) {
            throw new ApiException("Missing the required parameter 'gsuiteId' when calling gSuiteTraverseUser(Async)");
        }

        return gSuiteTraverseUserCall(gsuiteId, limit, xOrgId, skip, filter, _callback);

    }


    private ApiResponse<List<GraphObjectWithPaths>> gSuiteTraverseUserWithHttpInfo(String gsuiteId, Integer limit, String xOrgId, Integer skip, List<String> filter) throws ApiException {
        okhttp3.Call localVarCall = gSuiteTraverseUserValidateBeforeCall(gsuiteId, limit, xOrgId, skip, filter, null);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call gSuiteTraverseUserAsync(String gsuiteId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {

        okhttp3.Call localVarCall = gSuiteTraverseUserValidateBeforeCall(gsuiteId, limit, xOrgId, skip, filter, _callback);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GSuiteTraverseUserRequestBuilder {
        private final String gsuiteId;
        private Integer limit;
        private String xOrgId;
        private Integer skip;
        private List<String> filter;

        private GSuiteTraverseUserRequestBuilder(String gsuiteId) {
            this.gsuiteId = gsuiteId;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return GSuiteTraverseUserRequestBuilder
         */
        public GSuiteTraverseUserRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return GSuiteTraverseUserRequestBuilder
         */
        public GSuiteTraverseUserRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return GSuiteTraverseUserRequestBuilder
         */
        public GSuiteTraverseUserRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return GSuiteTraverseUserRequestBuilder
         */
        public GSuiteTraverseUserRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Build call for gSuiteTraverseUser
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
            return gSuiteTraverseUserCall(gsuiteId, limit, xOrgId, skip, filter, _callback);
        }


        /**
         * Execute gSuiteTraverseUser request
         * @return List&lt;GraphObjectWithPaths&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphObjectWithPaths> execute() throws ApiException {
            ApiResponse<List<GraphObjectWithPaths>> localVarResp = gSuiteTraverseUserWithHttpInfo(gsuiteId, limit, xOrgId, skip, filter);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute gSuiteTraverseUser request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphObjectWithPaths&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphObjectWithPaths>> executeWithHttpInfo() throws ApiException {
            return gSuiteTraverseUserWithHttpInfo(gsuiteId, limit, xOrgId, skip, filter);
        }

        /**
         * Execute gSuiteTraverseUser request (asynchronously)
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
            return gSuiteTraverseUserAsync(gsuiteId, limit, xOrgId, skip, filter, _callback);
        }
    }

    /**
     * List the Users bound to a G Suite instance
     * This endpoint will return all Users bound to a G Suite instance, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this G Suite instance to the corresponding User; this array represents all grouping and/or associations that would have to be removed to deprovision the User from this G Suite instance.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60;   curl -X GET https://console.jumpcloud.com/api/v2/gsuites/{Gsuite_ID}/users \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param gsuiteId ObjectID of the G Suite instance. (required)
     * @return GSuiteTraverseUserRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public GSuiteTraverseUserRequestBuilder gSuiteTraverseUser(String gsuiteId) throws IllegalArgumentException {
        if (gsuiteId == null) throw new IllegalArgumentException("\"gsuiteId\" is required but got null");
            

        return new GSuiteTraverseUserRequestBuilder(gsuiteId);
    }
    private okhttp3.Call gSuiteTraverseUserGroupCall(String gsuiteId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/gsuites/{gsuite_id}/usergroups"
            .replace("{" + "gsuite_id" + "}", localVarApiClient.escapeString(gsuiteId.toString()));

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
    private okhttp3.Call gSuiteTraverseUserGroupValidateBeforeCall(String gsuiteId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'gsuiteId' is set
        if (gsuiteId == null) {
            throw new ApiException("Missing the required parameter 'gsuiteId' when calling gSuiteTraverseUserGroup(Async)");
        }

        return gSuiteTraverseUserGroupCall(gsuiteId, limit, xOrgId, skip, filter, _callback);

    }


    private ApiResponse<List<GraphObjectWithPaths>> gSuiteTraverseUserGroupWithHttpInfo(String gsuiteId, Integer limit, String xOrgId, Integer skip, List<String> filter) throws ApiException {
        okhttp3.Call localVarCall = gSuiteTraverseUserGroupValidateBeforeCall(gsuiteId, limit, xOrgId, skip, filter, null);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call gSuiteTraverseUserGroupAsync(String gsuiteId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {

        okhttp3.Call localVarCall = gSuiteTraverseUserGroupValidateBeforeCall(gsuiteId, limit, xOrgId, skip, filter, _callback);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GSuiteTraverseUserGroupRequestBuilder {
        private final String gsuiteId;
        private Integer limit;
        private String xOrgId;
        private Integer skip;
        private List<String> filter;

        private GSuiteTraverseUserGroupRequestBuilder(String gsuiteId) {
            this.gsuiteId = gsuiteId;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return GSuiteTraverseUserGroupRequestBuilder
         */
        public GSuiteTraverseUserGroupRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return GSuiteTraverseUserGroupRequestBuilder
         */
        public GSuiteTraverseUserGroupRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return GSuiteTraverseUserGroupRequestBuilder
         */
        public GSuiteTraverseUserGroupRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return GSuiteTraverseUserGroupRequestBuilder
         */
        public GSuiteTraverseUserGroupRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Build call for gSuiteTraverseUserGroup
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
            return gSuiteTraverseUserGroupCall(gsuiteId, limit, xOrgId, skip, filter, _callback);
        }


        /**
         * Execute gSuiteTraverseUserGroup request
         * @return List&lt;GraphObjectWithPaths&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphObjectWithPaths> execute() throws ApiException {
            ApiResponse<List<GraphObjectWithPaths>> localVarResp = gSuiteTraverseUserGroupWithHttpInfo(gsuiteId, limit, xOrgId, skip, filter);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute gSuiteTraverseUserGroup request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphObjectWithPaths&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphObjectWithPaths>> executeWithHttpInfo() throws ApiException {
            return gSuiteTraverseUserGroupWithHttpInfo(gsuiteId, limit, xOrgId, skip, filter);
        }

        /**
         * Execute gSuiteTraverseUserGroup request (asynchronously)
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
            return gSuiteTraverseUserGroupAsync(gsuiteId, limit, xOrgId, skip, filter, _callback);
        }
    }

    /**
     * List the User Groups bound to a G Suite instance
     * This endpoint will return all User Groups bound to an G Suite instance, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the group&#39;s type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this G Suite instance to the corresponding User Group; this array represents all grouping and/or associations that would have to be removed to deprovision the User Group from this G Suite instance.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60;   curl -X GET https://console.jumpcloud.com/api/v2/gsuites/{GSuite_ID}/usergroups \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param gsuiteId ObjectID of the G Suite instance. (required)
     * @return GSuiteTraverseUserGroupRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public GSuiteTraverseUserGroupRequestBuilder gSuiteTraverseUserGroup(String gsuiteId) throws IllegalArgumentException {
        if (gsuiteId == null) throw new IllegalArgumentException("\"gsuiteId\" is required but got null");
            

        return new GSuiteTraverseUserGroupRequestBuilder(gsuiteId);
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
        String localVarPath = "/gsuites/{id}"
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


    private ApiResponse<Gsuite> getWithHttpInfo(String id, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = getValidateBeforeCall(id, xOrgId, null);
        Type localVarReturnType = new TypeToken<Gsuite>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getAsync(String id, String xOrgId, final ApiCallback<Gsuite> _callback) throws ApiException {

        okhttp3.Call localVarCall = getValidateBeforeCall(id, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<Gsuite>(){}.getType();
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
         * @return Gsuite
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public Gsuite execute() throws ApiException {
            ApiResponse<Gsuite> localVarResp = getWithHttpInfo(id, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute get request with HTTP info returned
         * @return ApiResponse&lt;Gsuite&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Gsuite> executeWithHttpInfo() throws ApiException {
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
        public okhttp3.Call executeAsync(final ApiCallback<Gsuite> _callback) throws ApiException {
            return getAsync(id, xOrgId, _callback);
        }
    }

    /**
     * Get G Suite
     * This endpoint returns a specific G Suite.  ##### Sample Request  &#x60;&#x60;&#x60;  curl -X GET https://console.jumpcloud.com/api/v2/gsuites/{GSUITE_ID} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param id Unique identifier of the GSuite. (required)
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
    private okhttp3.Call listImportJumpcloudUsersCall(String gsuiteId, Integer maxResults, String orderBy, String pageToken, String query, String sortOrder, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/gsuites/{gsuite_id}/import/jumpcloudusers"
            .replace("{" + "gsuite_id" + "}", localVarApiClient.escapeString(gsuiteId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (maxResults != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("maxResults", maxResults));
        }

        if (orderBy != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("orderBy", orderBy));
        }

        if (pageToken != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("pageToken", pageToken));
        }

        if (query != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("query", query));
        }

        if (sortOrder != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("sortOrder", sortOrder));
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
    private okhttp3.Call listImportJumpcloudUsersValidateBeforeCall(String gsuiteId, Integer maxResults, String orderBy, String pageToken, String query, String sortOrder, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'gsuiteId' is set
        if (gsuiteId == null) {
            throw new ApiException("Missing the required parameter 'gsuiteId' when calling listImportJumpcloudUsers(Async)");
        }

        return listImportJumpcloudUsersCall(gsuiteId, maxResults, orderBy, pageToken, query, sortOrder, _callback);

    }


    private ApiResponse<GsuitesListImportJumpcloudUsersResponse> listImportJumpcloudUsersWithHttpInfo(String gsuiteId, Integer maxResults, String orderBy, String pageToken, String query, String sortOrder) throws ApiException {
        okhttp3.Call localVarCall = listImportJumpcloudUsersValidateBeforeCall(gsuiteId, maxResults, orderBy, pageToken, query, sortOrder, null);
        Type localVarReturnType = new TypeToken<GsuitesListImportJumpcloudUsersResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listImportJumpcloudUsersAsync(String gsuiteId, Integer maxResults, String orderBy, String pageToken, String query, String sortOrder, final ApiCallback<GsuitesListImportJumpcloudUsersResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listImportJumpcloudUsersValidateBeforeCall(gsuiteId, maxResults, orderBy, pageToken, query, sortOrder, _callback);
        Type localVarReturnType = new TypeToken<GsuitesListImportJumpcloudUsersResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListImportJumpcloudUsersRequestBuilder {
        private final String gsuiteId;
        private Integer maxResults;
        private String orderBy;
        private String pageToken;
        private String query;
        private String sortOrder;

        private ListImportJumpcloudUsersRequestBuilder(String gsuiteId) {
            this.gsuiteId = gsuiteId;
        }

        /**
         * Set maxResults
         * @param maxResults Google Directory API maximum number of results per page. See https://developers.google.com/admin-sdk/directory/reference/rest/v1/users/list. (optional)
         * @return ListImportJumpcloudUsersRequestBuilder
         */
        public ListImportJumpcloudUsersRequestBuilder maxResults(Integer maxResults) {
            this.maxResults = maxResults;
            return this;
        }
        
        /**
         * Set orderBy
         * @param orderBy Google Directory API sort field parameter. See https://developers.google.com/admin-sdk/directory/reference/rest/v1/users/list. (optional)
         * @return ListImportJumpcloudUsersRequestBuilder
         */
        public ListImportJumpcloudUsersRequestBuilder orderBy(String orderBy) {
            this.orderBy = orderBy;
            return this;
        }
        
        /**
         * Set pageToken
         * @param pageToken Google Directory API token used to access the next page of results. See https://developers.google.com/admin-sdk/directory/reference/rest/v1/users/list. (optional)
         * @return ListImportJumpcloudUsersRequestBuilder
         */
        public ListImportJumpcloudUsersRequestBuilder pageToken(String pageToken) {
            this.pageToken = pageToken;
            return this;
        }
        
        /**
         * Set query
         * @param query Google Directory API search parameter. See https://developers.google.com/admin-sdk/directory/v1/guides/search-users. (optional)
         * @return ListImportJumpcloudUsersRequestBuilder
         */
        public ListImportJumpcloudUsersRequestBuilder query(String query) {
            this.query = query;
            return this;
        }
        
        /**
         * Set sortOrder
         * @param sortOrder Google Directory API sort direction parameter. See https://developers.google.com/admin-sdk/directory/reference/rest/v1/users/list. (optional)
         * @return ListImportJumpcloudUsersRequestBuilder
         */
        public ListImportJumpcloudUsersRequestBuilder sortOrder(String sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }
        
        /**
         * Build call for listImportJumpcloudUsers
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
            return listImportJumpcloudUsersCall(gsuiteId, maxResults, orderBy, pageToken, query, sortOrder, _callback);
        }


        /**
         * Execute listImportJumpcloudUsers request
         * @return GsuitesListImportJumpcloudUsersResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public GsuitesListImportJumpcloudUsersResponse execute() throws ApiException {
            ApiResponse<GsuitesListImportJumpcloudUsersResponse> localVarResp = listImportJumpcloudUsersWithHttpInfo(gsuiteId, maxResults, orderBy, pageToken, query, sortOrder);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listImportJumpcloudUsers request with HTTP info returned
         * @return ApiResponse&lt;GsuitesListImportJumpcloudUsersResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<GsuitesListImportJumpcloudUsersResponse> executeWithHttpInfo() throws ApiException {
            return listImportJumpcloudUsersWithHttpInfo(gsuiteId, maxResults, orderBy, pageToken, query, sortOrder);
        }

        /**
         * Execute listImportJumpcloudUsers request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<GsuitesListImportJumpcloudUsersResponse> _callback) throws ApiException {
            return listImportJumpcloudUsersAsync(gsuiteId, maxResults, orderBy, pageToken, query, sortOrder, _callback);
        }
    }

    /**
     * Get a list of users in Jumpcloud format to import from a Google Workspace account.
     * Lists available G Suite users for import, translated to the Jumpcloud user schema.
     * @param gsuiteId  (required)
     * @return ListImportJumpcloudUsersRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public ListImportJumpcloudUsersRequestBuilder listImportJumpcloudUsers(String gsuiteId) throws IllegalArgumentException {
        if (gsuiteId == null) throw new IllegalArgumentException("\"gsuiteId\" is required but got null");
            

        return new ListImportJumpcloudUsersRequestBuilder(gsuiteId);
    }
    private okhttp3.Call listImportUsersCall(String gsuiteId, Integer limit, Integer maxResults, String orderBy, String pageToken, String query, String sortOrder, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/gsuites/{gsuite_id}/import/users"
            .replace("{" + "gsuite_id" + "}", localVarApiClient.escapeString(gsuiteId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
        }

        if (maxResults != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("maxResults", maxResults));
        }

        if (orderBy != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("orderBy", orderBy));
        }

        if (pageToken != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("pageToken", pageToken));
        }

        if (query != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("query", query));
        }

        if (sortOrder != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("sortOrder", sortOrder));
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
    private okhttp3.Call listImportUsersValidateBeforeCall(String gsuiteId, Integer limit, Integer maxResults, String orderBy, String pageToken, String query, String sortOrder, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'gsuiteId' is set
        if (gsuiteId == null) {
            throw new ApiException("Missing the required parameter 'gsuiteId' when calling listImportUsers(Async)");
        }

        return listImportUsersCall(gsuiteId, limit, maxResults, orderBy, pageToken, query, sortOrder, _callback);

    }


    private ApiResponse<GsuitesListImportUsersResponse> listImportUsersWithHttpInfo(String gsuiteId, Integer limit, Integer maxResults, String orderBy, String pageToken, String query, String sortOrder) throws ApiException {
        okhttp3.Call localVarCall = listImportUsersValidateBeforeCall(gsuiteId, limit, maxResults, orderBy, pageToken, query, sortOrder, null);
        Type localVarReturnType = new TypeToken<GsuitesListImportUsersResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listImportUsersAsync(String gsuiteId, Integer limit, Integer maxResults, String orderBy, String pageToken, String query, String sortOrder, final ApiCallback<GsuitesListImportUsersResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listImportUsersValidateBeforeCall(gsuiteId, limit, maxResults, orderBy, pageToken, query, sortOrder, _callback);
        Type localVarReturnType = new TypeToken<GsuitesListImportUsersResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListImportUsersRequestBuilder {
        private final String gsuiteId;
        private Integer limit;
        private Integer maxResults;
        private String orderBy;
        private String pageToken;
        private String query;
        private String sortOrder;

        private ListImportUsersRequestBuilder(String gsuiteId) {
            this.gsuiteId = gsuiteId;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return ListImportUsersRequestBuilder
         */
        public ListImportUsersRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set maxResults
         * @param maxResults Google Directory API maximum number of results per page. See https://developers.google.com/admin-sdk/directory/reference/rest/v1/users/list. (optional)
         * @return ListImportUsersRequestBuilder
         */
        public ListImportUsersRequestBuilder maxResults(Integer maxResults) {
            this.maxResults = maxResults;
            return this;
        }
        
        /**
         * Set orderBy
         * @param orderBy Google Directory API sort field parameter. See https://developers.google.com/admin-sdk/directory/reference/rest/v1/users/list. (optional)
         * @return ListImportUsersRequestBuilder
         */
        public ListImportUsersRequestBuilder orderBy(String orderBy) {
            this.orderBy = orderBy;
            return this;
        }
        
        /**
         * Set pageToken
         * @param pageToken Google Directory API token used to access the next page of results. See https://developers.google.com/admin-sdk/directory/reference/rest/v1/users/list. (optional)
         * @return ListImportUsersRequestBuilder
         */
        public ListImportUsersRequestBuilder pageToken(String pageToken) {
            this.pageToken = pageToken;
            return this;
        }
        
        /**
         * Set query
         * @param query Google Directory API search parameter. See https://developers.google.com/admin-sdk/directory/v1/guides/search-users. (optional)
         * @return ListImportUsersRequestBuilder
         */
        public ListImportUsersRequestBuilder query(String query) {
            this.query = query;
            return this;
        }
        
        /**
         * Set sortOrder
         * @param sortOrder Google Directory API sort direction parameter. See https://developers.google.com/admin-sdk/directory/reference/rest/v1/users/list. (optional)
         * @return ListImportUsersRequestBuilder
         */
        public ListImportUsersRequestBuilder sortOrder(String sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }
        
        /**
         * Build call for listImportUsers
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
            return listImportUsersCall(gsuiteId, limit, maxResults, orderBy, pageToken, query, sortOrder, _callback);
        }


        /**
         * Execute listImportUsers request
         * @return GsuitesListImportUsersResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public GsuitesListImportUsersResponse execute() throws ApiException {
            ApiResponse<GsuitesListImportUsersResponse> localVarResp = listImportUsersWithHttpInfo(gsuiteId, limit, maxResults, orderBy, pageToken, query, sortOrder);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listImportUsers request with HTTP info returned
         * @return ApiResponse&lt;GsuitesListImportUsersResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<GsuitesListImportUsersResponse> executeWithHttpInfo() throws ApiException {
            return listImportUsersWithHttpInfo(gsuiteId, limit, maxResults, orderBy, pageToken, query, sortOrder);
        }

        /**
         * Execute listImportUsers request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<GsuitesListImportUsersResponse> _callback) throws ApiException {
            return listImportUsersAsync(gsuiteId, limit, maxResults, orderBy, pageToken, query, sortOrder, _callback);
        }
    }

    /**
     * Get a list of users to import from a G Suite instance
     * Lists G Suite users available for import.
     * @param gsuiteId  (required)
     * @return ListImportUsersRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public ListImportUsersRequestBuilder listImportUsers(String gsuiteId) throws IllegalArgumentException {
        if (gsuiteId == null) throw new IllegalArgumentException("\"gsuiteId\" is required but got null");
            

        return new ListImportUsersRequestBuilder(gsuiteId);
    }
    private okhttp3.Call patchCall(String id, String xOrgId, Gsuite gsuite, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = gsuite;

        // create path and map variables
        String localVarPath = "/gsuites/{id}"
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

        String[] localVarAuthNames = new String[] {  };
        return localVarApiClient.buildCall(basePath, localVarPath, "PATCH", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call patchValidateBeforeCall(String id, String xOrgId, Gsuite gsuite, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling patch(Async)");
        }

        return patchCall(id, xOrgId, gsuite, _callback);

    }


    private ApiResponse<Gsuite> patchWithHttpInfo(String id, String xOrgId, Gsuite gsuite) throws ApiException {
        okhttp3.Call localVarCall = patchValidateBeforeCall(id, xOrgId, gsuite, null);
        Type localVarReturnType = new TypeToken<Gsuite>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call patchAsync(String id, String xOrgId, Gsuite gsuite, final ApiCallback<Gsuite> _callback) throws ApiException {

        okhttp3.Call localVarCall = patchValidateBeforeCall(id, xOrgId, gsuite, _callback);
        Type localVarReturnType = new TypeToken<Gsuite>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class PatchRequestBuilder {
        private final String id;
        private DefaultDomain defaultDomain;
        private Boolean groupsEnabled;
        private String id;
        private String name;
        private String userLockoutAction;
        private String userPasswordExpirationAction;
        private String xOrgId;

        private PatchRequestBuilder(String id) {
            this.id = id;
        }

        /**
         * Set defaultDomain
         * @param defaultDomain  (optional)
         * @return PatchRequestBuilder
         */
        public PatchRequestBuilder defaultDomain(DefaultDomain defaultDomain) {
            this.defaultDomain = defaultDomain;
            return this;
        }
        
        /**
         * Set groupsEnabled
         * @param groupsEnabled  (optional)
         * @return PatchRequestBuilder
         */
        public PatchRequestBuilder groupsEnabled(Boolean groupsEnabled) {
            this.groupsEnabled = groupsEnabled;
            return this;
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
         * Set name
         * @param name  (optional)
         * @return PatchRequestBuilder
         */
        public PatchRequestBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        /**
         * Set userLockoutAction
         * @param userLockoutAction  (optional)
         * @return PatchRequestBuilder
         */
        public PatchRequestBuilder userLockoutAction(String userLockoutAction) {
            this.userLockoutAction = userLockoutAction;
            return this;
        }
        
        /**
         * Set userPasswordExpirationAction
         * @param userPasswordExpirationAction  (optional)
         * @return PatchRequestBuilder
         */
        public PatchRequestBuilder userPasswordExpirationAction(String userPasswordExpirationAction) {
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
            Gsuite gsuite = buildBodyParams();
            return patchCall(id, xOrgId, gsuite, _callback);
        }

        private Gsuite buildBodyParams() {
            Gsuite gsuite = new Gsuite();
            gsuite.defaultDomain(this.defaultDomain);
            gsuite.groupsEnabled(this.groupsEnabled);
            gsuite.id(this.id);
            gsuite.name(this.name);
            if (this.userLockoutAction != null)
            gsuite.userLockoutAction(Gsuite.UserLockoutActionEnum.fromValue(this.userLockoutAction));
            if (this.userPasswordExpirationAction != null)
            gsuite.userPasswordExpirationAction(Gsuite.UserPasswordExpirationActionEnum.fromValue(this.userPasswordExpirationAction));
            return gsuite;
        }

        /**
         * Execute patch request
         * @return Gsuite
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public Gsuite execute() throws ApiException {
            Gsuite gsuite = buildBodyParams();
            ApiResponse<Gsuite> localVarResp = patchWithHttpInfo(id, xOrgId, gsuite);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute patch request with HTTP info returned
         * @return ApiResponse&lt;Gsuite&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Gsuite> executeWithHttpInfo() throws ApiException {
            Gsuite gsuite = buildBodyParams();
            return patchWithHttpInfo(id, xOrgId, gsuite);
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
        public okhttp3.Call executeAsync(final ApiCallback<Gsuite> _callback) throws ApiException {
            Gsuite gsuite = buildBodyParams();
            return patchAsync(id, xOrgId, gsuite, _callback);
        }
    }

    /**
     * Update existing G Suite
     * This endpoint allows updating some attributes of a G Suite.  ##### Sample Request  &#x60;&#x60;&#x60; curl -X PATCH https://console.jumpcloud.com/api/v2/gsuites/{GSUITE_ID} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;userLockoutAction\&quot;: \&quot;suspend\&quot;,     \&quot;userPasswordExpirationAction\&quot;: \&quot;maintain\&quot;   }&#39; &#x60;&#x60;&#x60; Sample Request, set a default domain  &#x60;&#x60;&#x60; curl -X PATCH https://console.jumpcloud.com/api/v2/gsuites/{GSUITE_ID} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;defaultDomain\&quot;: {         \&quot;id\&quot;: \&quot;{domainObjectID}\&quot;       }   }&#39; &#x60;&#x60;&#x60;  Sample Request, unset the default domain  &#x60;&#x60;&#x60; curl -X PATCH https://console.jumpcloud.com/api/v2/gsuites/{GSUITE_ID} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;defaultDomain\&quot;: {}   }&#39; &#x60;&#x60;&#x60;
     * @param id Unique identifier of the GSuite. (required)
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
