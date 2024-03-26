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
import com.konfigthis.client.model.SambaDomain;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;

public class SambaDomainsApiGenerated {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public SambaDomainsApiGenerated() throws IllegalArgumentException {
        this(Configuration.getDefaultApiClient());
    }

    public SambaDomainsApiGenerated(ApiClient apiClient) throws IllegalArgumentException {
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

    private okhttp3.Call sambaDomainsDeleteCall(String ldapserverId, String id, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/ldapservers/{ldapserver_id}/sambadomains/{id}"
            .replace("{" + "ldapserver_id" + "}", localVarApiClient.escapeString(ldapserverId.toString()))
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
    private okhttp3.Call sambaDomainsDeleteValidateBeforeCall(String ldapserverId, String id, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'ldapserverId' is set
        if (ldapserverId == null) {
            throw new ApiException("Missing the required parameter 'ldapserverId' when calling sambaDomainsDelete(Async)");
        }

        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling sambaDomainsDelete(Async)");
        }

        return sambaDomainsDeleteCall(ldapserverId, id, xOrgId, _callback);

    }


    private ApiResponse<String> sambaDomainsDeleteWithHttpInfo(String ldapserverId, String id, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = sambaDomainsDeleteValidateBeforeCall(ldapserverId, id, xOrgId, null);
        Type localVarReturnType = new TypeToken<String>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call sambaDomainsDeleteAsync(String ldapserverId, String id, String xOrgId, final ApiCallback<String> _callback) throws ApiException {

        okhttp3.Call localVarCall = sambaDomainsDeleteValidateBeforeCall(ldapserverId, id, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<String>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class SambaDomainsDeleteRequestBuilder {
        private final String ldapserverId;
        private final String id;
        private String xOrgId;

        private SambaDomainsDeleteRequestBuilder(String ldapserverId, String id) {
            this.ldapserverId = ldapserverId;
            this.id = id;
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return SambaDomainsDeleteRequestBuilder
         */
        public SambaDomainsDeleteRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for sambaDomainsDelete
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return sambaDomainsDeleteCall(ldapserverId, id, xOrgId, _callback);
        }


        /**
         * Execute sambaDomainsDelete request
         * @return String
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public String execute() throws ApiException {
            ApiResponse<String> localVarResp = sambaDomainsDeleteWithHttpInfo(ldapserverId, id, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute sambaDomainsDelete request with HTTP info returned
         * @return ApiResponse&lt;String&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<String> executeWithHttpInfo() throws ApiException {
            return sambaDomainsDeleteWithHttpInfo(ldapserverId, id, xOrgId);
        }

        /**
         * Execute sambaDomainsDelete request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<String> _callback) throws ApiException {
            return sambaDomainsDeleteAsync(ldapserverId, id, xOrgId, _callback);
        }
    }

    /**
     * Delete Samba Domain
     * This endpoint allows you to delete a samba domain from an LDAP server.  ##### Sample Request &#x60;&#x60;&#x60; curl -X DELETE https://console.jumpcloud.com/api/v2/ldapservers/{LDAP_ID}/sambadomains/{SAMBA_ID} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param ldapserverId Unique identifier of the LDAP server. (required)
     * @param id Unique identifier of the samba domain. (required)
     * @return SambaDomainsDeleteRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public SambaDomainsDeleteRequestBuilder sambaDomainsDelete(String ldapserverId, String id) throws IllegalArgumentException {
        if (ldapserverId == null) throw new IllegalArgumentException("\"ldapserverId\" is required but got null");
            

        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new SambaDomainsDeleteRequestBuilder(ldapserverId, id);
    }
    private okhttp3.Call sambaDomainsGetCall(String ldapserverId, String id, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/ldapservers/{ldapserver_id}/sambadomains/{id}"
            .replace("{" + "ldapserver_id" + "}", localVarApiClient.escapeString(ldapserverId.toString()))
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
    private okhttp3.Call sambaDomainsGetValidateBeforeCall(String ldapserverId, String id, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'ldapserverId' is set
        if (ldapserverId == null) {
            throw new ApiException("Missing the required parameter 'ldapserverId' when calling sambaDomainsGet(Async)");
        }

        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling sambaDomainsGet(Async)");
        }

        return sambaDomainsGetCall(ldapserverId, id, xOrgId, _callback);

    }


    private ApiResponse<SambaDomain> sambaDomainsGetWithHttpInfo(String ldapserverId, String id, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = sambaDomainsGetValidateBeforeCall(ldapserverId, id, xOrgId, null);
        Type localVarReturnType = new TypeToken<SambaDomain>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call sambaDomainsGetAsync(String ldapserverId, String id, String xOrgId, final ApiCallback<SambaDomain> _callback) throws ApiException {

        okhttp3.Call localVarCall = sambaDomainsGetValidateBeforeCall(ldapserverId, id, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<SambaDomain>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class SambaDomainsGetRequestBuilder {
        private final String ldapserverId;
        private final String id;
        private String xOrgId;

        private SambaDomainsGetRequestBuilder(String ldapserverId, String id) {
            this.ldapserverId = ldapserverId;
            this.id = id;
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return SambaDomainsGetRequestBuilder
         */
        public SambaDomainsGetRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for sambaDomainsGet
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
            return sambaDomainsGetCall(ldapserverId, id, xOrgId, _callback);
        }


        /**
         * Execute sambaDomainsGet request
         * @return SambaDomain
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public SambaDomain execute() throws ApiException {
            ApiResponse<SambaDomain> localVarResp = sambaDomainsGetWithHttpInfo(ldapserverId, id, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute sambaDomainsGet request with HTTP info returned
         * @return ApiResponse&lt;SambaDomain&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<SambaDomain> executeWithHttpInfo() throws ApiException {
            return sambaDomainsGetWithHttpInfo(ldapserverId, id, xOrgId);
        }

        /**
         * Execute sambaDomainsGet request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<SambaDomain> _callback) throws ApiException {
            return sambaDomainsGetAsync(ldapserverId, id, xOrgId, _callback);
        }
    }

    /**
     * Get Samba Domain
     * This endpoint returns a specific samba domain for an LDAP server.  ##### Sample Request &#x60;&#x60;&#x60; curl -X GET \\   https://console.jumpcloud.com/api/v2/ldapservers/ldapservers/{LDAP_ID}/sambadomains/{SAMBA_ID} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;   &#x60;&#x60;&#x60;
     * @param ldapserverId Unique identifier of the LDAP server. (required)
     * @param id Unique identifier of the samba domain. (required)
     * @return SambaDomainsGetRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public SambaDomainsGetRequestBuilder sambaDomainsGet(String ldapserverId, String id) throws IllegalArgumentException {
        if (ldapserverId == null) throw new IllegalArgumentException("\"ldapserverId\" is required but got null");
            

        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new SambaDomainsGetRequestBuilder(ldapserverId, id);
    }
    private okhttp3.Call sambaDomainsListCall(String ldapserverId, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/ldapservers/{ldapserver_id}/sambadomains"
            .replace("{" + "ldapserver_id" + "}", localVarApiClient.escapeString(ldapserverId.toString()));

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
    private okhttp3.Call sambaDomainsListValidateBeforeCall(String ldapserverId, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'ldapserverId' is set
        if (ldapserverId == null) {
            throw new ApiException("Missing the required parameter 'ldapserverId' when calling sambaDomainsList(Async)");
        }

        return sambaDomainsListCall(ldapserverId, fields, filter, limit, skip, sort, xOrgId, _callback);

    }


    private ApiResponse<List<SambaDomain>> sambaDomainsListWithHttpInfo(String ldapserverId, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = sambaDomainsListValidateBeforeCall(ldapserverId, fields, filter, limit, skip, sort, xOrgId, null);
        Type localVarReturnType = new TypeToken<List<SambaDomain>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call sambaDomainsListAsync(String ldapserverId, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, String xOrgId, final ApiCallback<List<SambaDomain>> _callback) throws ApiException {

        okhttp3.Call localVarCall = sambaDomainsListValidateBeforeCall(ldapserverId, fields, filter, limit, skip, sort, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<List<SambaDomain>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class SambaDomainsListRequestBuilder {
        private final String ldapserverId;
        private List<String> fields;
        private List<String> filter;
        private Integer limit;
        private Integer skip;
        private List<String> sort;
        private String xOrgId;

        private SambaDomainsListRequestBuilder(String ldapserverId) {
            this.ldapserverId = ldapserverId;
        }

        /**
         * Set fields
         * @param fields The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  (optional)
         * @return SambaDomainsListRequestBuilder
         */
        public SambaDomainsListRequestBuilder fields(List<String> fields) {
            this.fields = fields;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return SambaDomainsListRequestBuilder
         */
        public SambaDomainsListRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return SambaDomainsListRequestBuilder
         */
        public SambaDomainsListRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return SambaDomainsListRequestBuilder
         */
        public SambaDomainsListRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  (optional)
         * @return SambaDomainsListRequestBuilder
         */
        public SambaDomainsListRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return SambaDomainsListRequestBuilder
         */
        public SambaDomainsListRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for sambaDomainsList
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
            <tr><td> 0 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return sambaDomainsListCall(ldapserverId, fields, filter, limit, skip, sort, xOrgId, _callback);
        }


        /**
         * Execute sambaDomainsList request
         * @return List&lt;SambaDomain&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
            <tr><td> 0 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SambaDomain> execute() throws ApiException {
            ApiResponse<List<SambaDomain>> localVarResp = sambaDomainsListWithHttpInfo(ldapserverId, fields, filter, limit, skip, sort, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute sambaDomainsList request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SambaDomain&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
            <tr><td> 0 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SambaDomain>> executeWithHttpInfo() throws ApiException {
            return sambaDomainsListWithHttpInfo(ldapserverId, fields, filter, limit, skip, sort, xOrgId);
        }

        /**
         * Execute sambaDomainsList request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
            <tr><td> 0 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SambaDomain>> _callback) throws ApiException {
            return sambaDomainsListAsync(ldapserverId, fields, filter, limit, skip, sort, xOrgId, _callback);
        }
    }

    /**
     * List Samba Domains
     * This endpoint returns all samba domains for an LDAP server.  ##### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/ldapservers/{LDAP_ID}/sambadomains \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;   &#x60;&#x60;&#x60;
     * @param ldapserverId Unique identifier of the LDAP server. (required)
     * @return SambaDomainsListRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 0 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public SambaDomainsListRequestBuilder sambaDomainsList(String ldapserverId) throws IllegalArgumentException {
        if (ldapserverId == null) throw new IllegalArgumentException("\"ldapserverId\" is required but got null");
            

        return new SambaDomainsListRequestBuilder(ldapserverId);
    }
    private okhttp3.Call sambaDomainsPostCall(String ldapserverId, String xOrgId, SambaDomain sambaDomain, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = sambaDomain;

        // create path and map variables
        String localVarPath = "/ldapservers/{ldapserver_id}/sambadomains"
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
    private okhttp3.Call sambaDomainsPostValidateBeforeCall(String ldapserverId, String xOrgId, SambaDomain sambaDomain, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'ldapserverId' is set
        if (ldapserverId == null) {
            throw new ApiException("Missing the required parameter 'ldapserverId' when calling sambaDomainsPost(Async)");
        }

        return sambaDomainsPostCall(ldapserverId, xOrgId, sambaDomain, _callback);

    }


    private ApiResponse<SambaDomain> sambaDomainsPostWithHttpInfo(String ldapserverId, String xOrgId, SambaDomain sambaDomain) throws ApiException {
        okhttp3.Call localVarCall = sambaDomainsPostValidateBeforeCall(ldapserverId, xOrgId, sambaDomain, null);
        Type localVarReturnType = new TypeToken<SambaDomain>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call sambaDomainsPostAsync(String ldapserverId, String xOrgId, SambaDomain sambaDomain, final ApiCallback<SambaDomain> _callback) throws ApiException {

        okhttp3.Call localVarCall = sambaDomainsPostValidateBeforeCall(ldapserverId, xOrgId, sambaDomain, _callback);
        Type localVarReturnType = new TypeToken<SambaDomain>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class SambaDomainsPostRequestBuilder {
        private final String name;
        private final String sid;
        private final String ldapserverId;
        private String id;
        private String xOrgId;

        private SambaDomainsPostRequestBuilder(String name, String sid, String ldapserverId) {
            this.name = name;
            this.sid = sid;
            this.ldapserverId = ldapserverId;
        }

        /**
         * Set id
         * @param id Unique identifier of this domain (optional)
         * @return SambaDomainsPostRequestBuilder
         */
        public SambaDomainsPostRequestBuilder id(String id) {
            this.id = id;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return SambaDomainsPostRequestBuilder
         */
        public SambaDomainsPostRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for sambaDomainsPost
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
            SambaDomain sambaDomain = buildBodyParams();
            return sambaDomainsPostCall(ldapserverId, xOrgId, sambaDomain, _callback);
        }

        private SambaDomain buildBodyParams() {
            SambaDomain sambaDomain = new SambaDomain();
            sambaDomain.id(this.id);
            sambaDomain.name(this.name);
            sambaDomain.sid(this.sid);
            return sambaDomain;
        }

        /**
         * Execute sambaDomainsPost request
         * @return SambaDomain
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public SambaDomain execute() throws ApiException {
            SambaDomain sambaDomain = buildBodyParams();
            ApiResponse<SambaDomain> localVarResp = sambaDomainsPostWithHttpInfo(ldapserverId, xOrgId, sambaDomain);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute sambaDomainsPost request with HTTP info returned
         * @return ApiResponse&lt;SambaDomain&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<SambaDomain> executeWithHttpInfo() throws ApiException {
            SambaDomain sambaDomain = buildBodyParams();
            return sambaDomainsPostWithHttpInfo(ldapserverId, xOrgId, sambaDomain);
        }

        /**
         * Execute sambaDomainsPost request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<SambaDomain> _callback) throws ApiException {
            SambaDomain sambaDomain = buildBodyParams();
            return sambaDomainsPostAsync(ldapserverId, xOrgId, sambaDomain, _callback);
        }
    }

    /**
     * Create Samba Domain
     * This endpoint allows you to create a samba domain for an LDAP server.  ##### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/ldapservers/{LDAP_ID}/sambadomains \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;sid\&quot;:\&quot;{SID_ID}\&quot;,     \&quot;name\&quot;:\&quot;{WORKGROUP_NAME}\&quot;   }&#39; &#x60;&#x60;&#x60;
     * @param ldapserverId Unique identifier of the LDAP server. (required)
     * @return SambaDomainsPostRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public SambaDomainsPostRequestBuilder sambaDomainsPost(String name, String sid, String ldapserverId) throws IllegalArgumentException {
        if (name == null) throw new IllegalArgumentException("\"name\" is required but got null");
            

        if (sid == null) throw new IllegalArgumentException("\"sid\" is required but got null");
            

        if (ldapserverId == null) throw new IllegalArgumentException("\"ldapserverId\" is required but got null");
            

        return new SambaDomainsPostRequestBuilder(name, sid, ldapserverId);
    }
    private okhttp3.Call sambaDomainsPutCall(String ldapserverId, String id, SambaDomain sambaDomain, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = sambaDomain;

        // create path and map variables
        String localVarPath = "/ldapservers/{ldapserver_id}/sambadomains/{id}"
            .replace("{" + "ldapserver_id" + "}", localVarApiClient.escapeString(ldapserverId.toString()))
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
    private okhttp3.Call sambaDomainsPutValidateBeforeCall(String ldapserverId, String id, SambaDomain sambaDomain, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'ldapserverId' is set
        if (ldapserverId == null) {
            throw new ApiException("Missing the required parameter 'ldapserverId' when calling sambaDomainsPut(Async)");
        }

        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling sambaDomainsPut(Async)");
        }

        return sambaDomainsPutCall(ldapserverId, id, sambaDomain, _callback);

    }


    private ApiResponse<SambaDomain> sambaDomainsPutWithHttpInfo(String ldapserverId, String id, SambaDomain sambaDomain) throws ApiException {
        okhttp3.Call localVarCall = sambaDomainsPutValidateBeforeCall(ldapserverId, id, sambaDomain, null);
        Type localVarReturnType = new TypeToken<SambaDomain>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call sambaDomainsPutAsync(String ldapserverId, String id, SambaDomain sambaDomain, final ApiCallback<SambaDomain> _callback) throws ApiException {

        okhttp3.Call localVarCall = sambaDomainsPutValidateBeforeCall(ldapserverId, id, sambaDomain, _callback);
        Type localVarReturnType = new TypeToken<SambaDomain>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class SambaDomainsPutRequestBuilder {
        private final String name;
        private final String sid;
        private final String ldapserverId;
        private final String id;
        private String id;

        private SambaDomainsPutRequestBuilder(String name, String sid, String ldapserverId, String id) {
            this.name = name;
            this.sid = sid;
            this.ldapserverId = ldapserverId;
            this.id = id;
        }

        /**
         * Set id
         * @param id Unique identifier of this domain (optional)
         * @return SambaDomainsPutRequestBuilder
         */
        public SambaDomainsPutRequestBuilder id(String id) {
            this.id = id;
            return this;
        }
        
        /**
         * Build call for sambaDomainsPut
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
            SambaDomain sambaDomain = buildBodyParams();
            return sambaDomainsPutCall(ldapserverId, id, sambaDomain, _callback);
        }

        private SambaDomain buildBodyParams() {
            SambaDomain sambaDomain = new SambaDomain();
            sambaDomain.id(this.id);
            sambaDomain.name(this.name);
            sambaDomain.sid(this.sid);
            return sambaDomain;
        }

        /**
         * Execute sambaDomainsPut request
         * @return SambaDomain
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public SambaDomain execute() throws ApiException {
            SambaDomain sambaDomain = buildBodyParams();
            ApiResponse<SambaDomain> localVarResp = sambaDomainsPutWithHttpInfo(ldapserverId, id, sambaDomain);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute sambaDomainsPut request with HTTP info returned
         * @return ApiResponse&lt;SambaDomain&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<SambaDomain> executeWithHttpInfo() throws ApiException {
            SambaDomain sambaDomain = buildBodyParams();
            return sambaDomainsPutWithHttpInfo(ldapserverId, id, sambaDomain);
        }

        /**
         * Execute sambaDomainsPut request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<SambaDomain> _callback) throws ApiException {
            SambaDomain sambaDomain = buildBodyParams();
            return sambaDomainsPutAsync(ldapserverId, id, sambaDomain, _callback);
        }
    }

    /**
     * Update Samba Domain
     * This endpoint allows you to update the samba domain information for an LDAP server.  ##### Sample Request &#x60;&#x60;&#x60; curl -X PUT https://console.jumpcloud.com/api/v2/ldapservers/{LDAP_ID}/sambadomains/{SAMBA_ID} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;sid\&quot;:\&quot;{SID_ID}\&quot;,     \&quot;name\&quot;:\&quot;{WORKGROUP_NAME}\&quot;   }&#39; &#x60;&#x60;&#x60;
     * @param ldapserverId Unique identifier of the LDAP server. (required)
     * @param id Unique identifier of the samba domain. (required)
     * @return SambaDomainsPutRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public SambaDomainsPutRequestBuilder sambaDomainsPut(String name, String sid, String ldapserverId, String id) throws IllegalArgumentException {
        if (name == null) throw new IllegalArgumentException("\"name\" is required but got null");
            

        if (sid == null) throw new IllegalArgumentException("\"sid\" is required but got null");
            

        if (ldapserverId == null) throw new IllegalArgumentException("\"ldapserverId\" is required but got null");
            

        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new SambaDomainsPutRequestBuilder(name, sid, ldapserverId, id);
    }
}