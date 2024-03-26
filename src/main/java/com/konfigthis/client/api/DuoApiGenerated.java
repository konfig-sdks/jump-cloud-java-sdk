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


import com.konfigthis.client.model.DuoAccount;
import com.konfigthis.client.model.DuoApplication;
import com.konfigthis.client.model.DuoApplicationReq;
import com.konfigthis.client.model.DuoApplicationUpdateReq;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;

public class DuoApiGenerated {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public DuoApiGenerated() throws IllegalArgumentException {
        this(Configuration.getDefaultApiClient());
    }

    public DuoApiGenerated(ApiClient apiClient) throws IllegalArgumentException {
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

    private okhttp3.Call accountDeleteCall(String id, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/duo/accounts/{id}"
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
    private okhttp3.Call accountDeleteValidateBeforeCall(String id, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling accountDelete(Async)");
        }

        return accountDeleteCall(id, xOrgId, _callback);

    }


    private ApiResponse<DuoAccount> accountDeleteWithHttpInfo(String id, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = accountDeleteValidateBeforeCall(id, xOrgId, null);
        Type localVarReturnType = new TypeToken<DuoAccount>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call accountDeleteAsync(String id, String xOrgId, final ApiCallback<DuoAccount> _callback) throws ApiException {

        okhttp3.Call localVarCall = accountDeleteValidateBeforeCall(id, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<DuoAccount>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class AccountDeleteRequestBuilder {
        private final String id;
        private String xOrgId;

        private AccountDeleteRequestBuilder(String id) {
            this.id = id;
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return AccountDeleteRequestBuilder
         */
        public AccountDeleteRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for accountDelete
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
            return accountDeleteCall(id, xOrgId, _callback);
        }


        /**
         * Execute accountDelete request
         * @return DuoAccount
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public DuoAccount execute() throws ApiException {
            ApiResponse<DuoAccount> localVarResp = accountDeleteWithHttpInfo(id, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute accountDelete request with HTTP info returned
         * @return ApiResponse&lt;DuoAccount&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<DuoAccount> executeWithHttpInfo() throws ApiException {
            return accountDeleteWithHttpInfo(id, xOrgId);
        }

        /**
         * Execute accountDelete request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DuoAccount> _callback) throws ApiException {
            return accountDeleteAsync(id, xOrgId, _callback);
        }
    }

    /**
     * Delete a Duo Account
     * Removes the specified Duo account, an error will be returned if the account has some Duo application used in a protected resource.  #### Sample Request &#x60;&#x60;&#x60; curl -X DELETE https://console.jumpcloud.com/api/v2/duo/accounts/{id} \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param id ObjectID of the Duo Account (required)
     * @return AccountDeleteRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public AccountDeleteRequestBuilder accountDelete(String id) throws IllegalArgumentException {
        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new AccountDeleteRequestBuilder(id);
    }
    private okhttp3.Call accountGetCall(String id, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/duo/accounts/{id}"
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
    private okhttp3.Call accountGetValidateBeforeCall(String id, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling accountGet(Async)");
        }

        return accountGetCall(id, xOrgId, _callback);

    }


    private ApiResponse<DuoAccount> accountGetWithHttpInfo(String id, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = accountGetValidateBeforeCall(id, xOrgId, null);
        Type localVarReturnType = new TypeToken<DuoAccount>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call accountGetAsync(String id, String xOrgId, final ApiCallback<DuoAccount> _callback) throws ApiException {

        okhttp3.Call localVarCall = accountGetValidateBeforeCall(id, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<DuoAccount>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class AccountGetRequestBuilder {
        private final String id;
        private String xOrgId;

        private AccountGetRequestBuilder(String id) {
            this.id = id;
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return AccountGetRequestBuilder
         */
        public AccountGetRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for accountGet
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
            return accountGetCall(id, xOrgId, _callback);
        }


        /**
         * Execute accountGet request
         * @return DuoAccount
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public DuoAccount execute() throws ApiException {
            ApiResponse<DuoAccount> localVarResp = accountGetWithHttpInfo(id, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute accountGet request with HTTP info returned
         * @return ApiResponse&lt;DuoAccount&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<DuoAccount> executeWithHttpInfo() throws ApiException {
            return accountGetWithHttpInfo(id, xOrgId);
        }

        /**
         * Execute accountGet request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DuoAccount> _callback) throws ApiException {
            return accountGetAsync(id, xOrgId, _callback);
        }
    }

    /**
     * Get a Duo Acount
     * This endpoint returns a specific Duo account.  #### Sample Request &#x60;&#x60;&#x60; curl https://console.jumpcloud.com/api/v2/duo/accounts/{id} \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param id ObjectID of the Duo Account (required)
     * @return AccountGetRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public AccountGetRequestBuilder accountGet(String id) throws IllegalArgumentException {
        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new AccountGetRequestBuilder(id);
    }
    private okhttp3.Call accountListCall(String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/duo/accounts";

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
    private okhttp3.Call accountListValidateBeforeCall(String xOrgId, final ApiCallback _callback) throws ApiException {
        return accountListCall(xOrgId, _callback);

    }


    private ApiResponse<List<DuoAccount>> accountListWithHttpInfo(String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = accountListValidateBeforeCall(xOrgId, null);
        Type localVarReturnType = new TypeToken<List<DuoAccount>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call accountListAsync(String xOrgId, final ApiCallback<List<DuoAccount>> _callback) throws ApiException {

        okhttp3.Call localVarCall = accountListValidateBeforeCall(xOrgId, _callback);
        Type localVarReturnType = new TypeToken<List<DuoAccount>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class AccountListRequestBuilder {
        private String xOrgId;

        private AccountListRequestBuilder() {
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return AccountListRequestBuilder
         */
        public AccountListRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for accountList
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
            return accountListCall(xOrgId, _callback);
        }


        /**
         * Execute accountList request
         * @return List&lt;DuoAccount&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<DuoAccount> execute() throws ApiException {
            ApiResponse<List<DuoAccount>> localVarResp = accountListWithHttpInfo(xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute accountList request with HTTP info returned
         * @return ApiResponse&lt;List&lt;DuoAccount&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<DuoAccount>> executeWithHttpInfo() throws ApiException {
            return accountListWithHttpInfo(xOrgId);
        }

        /**
         * Execute accountList request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<DuoAccount>> _callback) throws ApiException {
            return accountListAsync(xOrgId, _callback);
        }
    }

    /**
     * List Duo Accounts
     * This endpoint returns all the Duo accounts for your organization. Note: There can currently only be one Duo account for your organization.  #### Sample Request &#x60;&#x60;&#x60; curl https://console.jumpcloud.com/api/v2/duo/accounts \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @return AccountListRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public AccountListRequestBuilder accountList() throws IllegalArgumentException {
        return new AccountListRequestBuilder();
    }
    private okhttp3.Call accountPostCall(String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/duo/accounts";

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
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call accountPostValidateBeforeCall(String xOrgId, final ApiCallback _callback) throws ApiException {
        return accountPostCall(xOrgId, _callback);

    }


    private ApiResponse<DuoAccount> accountPostWithHttpInfo(String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = accountPostValidateBeforeCall(xOrgId, null);
        Type localVarReturnType = new TypeToken<DuoAccount>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call accountPostAsync(String xOrgId, final ApiCallback<DuoAccount> _callback) throws ApiException {

        okhttp3.Call localVarCall = accountPostValidateBeforeCall(xOrgId, _callback);
        Type localVarReturnType = new TypeToken<DuoAccount>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class AccountPostRequestBuilder {
        private String xOrgId;

        private AccountPostRequestBuilder() {
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return AccountPostRequestBuilder
         */
        public AccountPostRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for accountPost
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
            return accountPostCall(xOrgId, _callback);
        }


        /**
         * Execute accountPost request
         * @return DuoAccount
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public DuoAccount execute() throws ApiException {
            ApiResponse<DuoAccount> localVarResp = accountPostWithHttpInfo(xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute accountPost request with HTTP info returned
         * @return ApiResponse&lt;DuoAccount&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<DuoAccount> executeWithHttpInfo() throws ApiException {
            return accountPostWithHttpInfo(xOrgId);
        }

        /**
         * Execute accountPost request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DuoAccount> _callback) throws ApiException {
            return accountPostAsync(xOrgId, _callback);
        }
    }

    /**
     * Create Duo Account
     * Registers a Duo account for an organization. Only one Duo account will be allowed, in case an organization has a Duo account already a 409 (Conflict) code will be returned.  #### Sample Request &#x60;&#x60;&#x60;   curl -X POST https://console.jumpcloud.com/api/v2/duo/accounts \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{}&#39; &#x60;&#x60;&#x60;
     * @return AccountPostRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public AccountPostRequestBuilder accountPost() throws IllegalArgumentException {
        return new AccountPostRequestBuilder();
    }
    private okhttp3.Call applicationDeleteCall(String accountId, String applicationId, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/duo/accounts/{account_id}/applications/{application_id}"
            .replace("{" + "account_id" + "}", localVarApiClient.escapeString(accountId.toString()))
            .replace("{" + "application_id" + "}", localVarApiClient.escapeString(applicationId.toString()));

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
    private okhttp3.Call applicationDeleteValidateBeforeCall(String accountId, String applicationId, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'accountId' is set
        if (accountId == null) {
            throw new ApiException("Missing the required parameter 'accountId' when calling applicationDelete(Async)");
        }

        // verify the required parameter 'applicationId' is set
        if (applicationId == null) {
            throw new ApiException("Missing the required parameter 'applicationId' when calling applicationDelete(Async)");
        }

        return applicationDeleteCall(accountId, applicationId, xOrgId, _callback);

    }


    private ApiResponse<DuoApplication> applicationDeleteWithHttpInfo(String accountId, String applicationId, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = applicationDeleteValidateBeforeCall(accountId, applicationId, xOrgId, null);
        Type localVarReturnType = new TypeToken<DuoApplication>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call applicationDeleteAsync(String accountId, String applicationId, String xOrgId, final ApiCallback<DuoApplication> _callback) throws ApiException {

        okhttp3.Call localVarCall = applicationDeleteValidateBeforeCall(accountId, applicationId, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<DuoApplication>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ApplicationDeleteRequestBuilder {
        private final String accountId;
        private final String applicationId;
        private String xOrgId;

        private ApplicationDeleteRequestBuilder(String accountId, String applicationId) {
            this.accountId = accountId;
            this.applicationId = applicationId;
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ApplicationDeleteRequestBuilder
         */
        public ApplicationDeleteRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for applicationDelete
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
            return applicationDeleteCall(accountId, applicationId, xOrgId, _callback);
        }


        /**
         * Execute applicationDelete request
         * @return DuoApplication
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public DuoApplication execute() throws ApiException {
            ApiResponse<DuoApplication> localVarResp = applicationDeleteWithHttpInfo(accountId, applicationId, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute applicationDelete request with HTTP info returned
         * @return ApiResponse&lt;DuoApplication&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<DuoApplication> executeWithHttpInfo() throws ApiException {
            return applicationDeleteWithHttpInfo(accountId, applicationId, xOrgId);
        }

        /**
         * Execute applicationDelete request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DuoApplication> _callback) throws ApiException {
            return applicationDeleteAsync(accountId, applicationId, xOrgId, _callback);
        }
    }

    /**
     * Delete a Duo Application
     * Deletes the specified Duo application, an error will be returned if the application is used in a protected resource.  #### Sample Request &#x60;&#x60;&#x60;   curl -X DELETE https://console.jumpcloud.com/api/v2/duo/accounts/{ACCOUNT_ID}/applications/{APPLICATION_ID} \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;&#39; &#x60;&#x60;&#x60;
     * @param accountId  (required)
     * @param applicationId  (required)
     * @return ApplicationDeleteRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ApplicationDeleteRequestBuilder applicationDelete(String accountId, String applicationId) throws IllegalArgumentException {
        if (accountId == null) throw new IllegalArgumentException("\"accountId\" is required but got null");
            

        if (applicationId == null) throw new IllegalArgumentException("\"applicationId\" is required but got null");
            

        return new ApplicationDeleteRequestBuilder(accountId, applicationId);
    }
    private okhttp3.Call applicationGetCall(String accountId, String applicationId, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/duo/accounts/{account_id}/applications/{application_id}"
            .replace("{" + "account_id" + "}", localVarApiClient.escapeString(accountId.toString()))
            .replace("{" + "application_id" + "}", localVarApiClient.escapeString(applicationId.toString()));

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
    private okhttp3.Call applicationGetValidateBeforeCall(String accountId, String applicationId, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'accountId' is set
        if (accountId == null) {
            throw new ApiException("Missing the required parameter 'accountId' when calling applicationGet(Async)");
        }

        // verify the required parameter 'applicationId' is set
        if (applicationId == null) {
            throw new ApiException("Missing the required parameter 'applicationId' when calling applicationGet(Async)");
        }

        return applicationGetCall(accountId, applicationId, xOrgId, _callback);

    }


    private ApiResponse<DuoApplication> applicationGetWithHttpInfo(String accountId, String applicationId, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = applicationGetValidateBeforeCall(accountId, applicationId, xOrgId, null);
        Type localVarReturnType = new TypeToken<DuoApplication>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call applicationGetAsync(String accountId, String applicationId, String xOrgId, final ApiCallback<DuoApplication> _callback) throws ApiException {

        okhttp3.Call localVarCall = applicationGetValidateBeforeCall(accountId, applicationId, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<DuoApplication>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ApplicationGetRequestBuilder {
        private final String accountId;
        private final String applicationId;
        private String xOrgId;

        private ApplicationGetRequestBuilder(String accountId, String applicationId) {
            this.accountId = accountId;
            this.applicationId = applicationId;
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ApplicationGetRequestBuilder
         */
        public ApplicationGetRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for applicationGet
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
            return applicationGetCall(accountId, applicationId, xOrgId, _callback);
        }


        /**
         * Execute applicationGet request
         * @return DuoApplication
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public DuoApplication execute() throws ApiException {
            ApiResponse<DuoApplication> localVarResp = applicationGetWithHttpInfo(accountId, applicationId, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute applicationGet request with HTTP info returned
         * @return ApiResponse&lt;DuoApplication&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<DuoApplication> executeWithHttpInfo() throws ApiException {
            return applicationGetWithHttpInfo(accountId, applicationId, xOrgId);
        }

        /**
         * Execute applicationGet request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DuoApplication> _callback) throws ApiException {
            return applicationGetAsync(accountId, applicationId, xOrgId, _callback);
        }
    }

    /**
     * Get a Duo application
     * This endpoint returns a specific Duo application that is associated with the specified Duo account.  #### Sample Request &#x60;&#x60;&#x60;   curl https://console.jumpcloud.com/api/v2/duo/accounts/{ACCOUNT_ID}/applications/{APPLICATION_ID} \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param accountId  (required)
     * @param applicationId  (required)
     * @return ApplicationGetRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ApplicationGetRequestBuilder applicationGet(String accountId, String applicationId) throws IllegalArgumentException {
        if (accountId == null) throw new IllegalArgumentException("\"accountId\" is required but got null");
            

        if (applicationId == null) throw new IllegalArgumentException("\"applicationId\" is required but got null");
            

        return new ApplicationGetRequestBuilder(accountId, applicationId);
    }
    private okhttp3.Call applicationListCall(String accountId, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/duo/accounts/{account_id}/applications"
            .replace("{" + "account_id" + "}", localVarApiClient.escapeString(accountId.toString()));

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
    private okhttp3.Call applicationListValidateBeforeCall(String accountId, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'accountId' is set
        if (accountId == null) {
            throw new ApiException("Missing the required parameter 'accountId' when calling applicationList(Async)");
        }

        return applicationListCall(accountId, xOrgId, _callback);

    }


    private ApiResponse<List<DuoApplication>> applicationListWithHttpInfo(String accountId, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = applicationListValidateBeforeCall(accountId, xOrgId, null);
        Type localVarReturnType = new TypeToken<List<DuoApplication>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call applicationListAsync(String accountId, String xOrgId, final ApiCallback<List<DuoApplication>> _callback) throws ApiException {

        okhttp3.Call localVarCall = applicationListValidateBeforeCall(accountId, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<List<DuoApplication>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ApplicationListRequestBuilder {
        private final String accountId;
        private String xOrgId;

        private ApplicationListRequestBuilder(String accountId) {
            this.accountId = accountId;
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ApplicationListRequestBuilder
         */
        public ApplicationListRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for applicationList
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
            return applicationListCall(accountId, xOrgId, _callback);
        }


        /**
         * Execute applicationList request
         * @return List&lt;DuoApplication&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<DuoApplication> execute() throws ApiException {
            ApiResponse<List<DuoApplication>> localVarResp = applicationListWithHttpInfo(accountId, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute applicationList request with HTTP info returned
         * @return ApiResponse&lt;List&lt;DuoApplication&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<DuoApplication>> executeWithHttpInfo() throws ApiException {
            return applicationListWithHttpInfo(accountId, xOrgId);
        }

        /**
         * Execute applicationList request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<DuoApplication>> _callback) throws ApiException {
            return applicationListAsync(accountId, xOrgId, _callback);
        }
    }

    /**
     * List Duo Applications
     * This endpoint returns all the Duo applications for the specified Duo account. Note: There can currently only be one Duo application for your organization.  #### Sample Request &#x60;&#x60;&#x60;   curl https://console.jumpcloud.com/api/v2/duo/accounts/{ACCOUNT_ID}/applications \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param accountId  (required)
     * @return ApplicationListRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ApplicationListRequestBuilder applicationList(String accountId) throws IllegalArgumentException {
        if (accountId == null) throw new IllegalArgumentException("\"accountId\" is required but got null");
            

        return new ApplicationListRequestBuilder(accountId);
    }
    private okhttp3.Call applicationPostCall(String accountId, String xOrgId, DuoApplicationReq duoApplicationReq, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = duoApplicationReq;

        // create path and map variables
        String localVarPath = "/duo/accounts/{account_id}/applications"
            .replace("{" + "account_id" + "}", localVarApiClient.escapeString(accountId.toString()));

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
    private okhttp3.Call applicationPostValidateBeforeCall(String accountId, String xOrgId, DuoApplicationReq duoApplicationReq, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'accountId' is set
        if (accountId == null) {
            throw new ApiException("Missing the required parameter 'accountId' when calling applicationPost(Async)");
        }

        return applicationPostCall(accountId, xOrgId, duoApplicationReq, _callback);

    }


    private ApiResponse<DuoApplication> applicationPostWithHttpInfo(String accountId, String xOrgId, DuoApplicationReq duoApplicationReq) throws ApiException {
        okhttp3.Call localVarCall = applicationPostValidateBeforeCall(accountId, xOrgId, duoApplicationReq, null);
        Type localVarReturnType = new TypeToken<DuoApplication>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call applicationPostAsync(String accountId, String xOrgId, DuoApplicationReq duoApplicationReq, final ApiCallback<DuoApplication> _callback) throws ApiException {

        okhttp3.Call localVarCall = applicationPostValidateBeforeCall(accountId, xOrgId, duoApplicationReq, _callback);
        Type localVarReturnType = new TypeToken<DuoApplication>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ApplicationPostRequestBuilder {
        private final String apiHost;
        private final String integrationKey;
        private final String name;
        private final String secretKey;
        private final String accountId;
        private String xOrgId;

        private ApplicationPostRequestBuilder(String apiHost, String integrationKey, String name, String secretKey, String accountId) {
            this.apiHost = apiHost;
            this.integrationKey = integrationKey;
            this.name = name;
            this.secretKey = secretKey;
            this.accountId = accountId;
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ApplicationPostRequestBuilder
         */
        public ApplicationPostRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for applicationPost
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
            DuoApplicationReq duoApplicationReq = buildBodyParams();
            return applicationPostCall(accountId, xOrgId, duoApplicationReq, _callback);
        }

        private DuoApplicationReq buildBodyParams() {
            DuoApplicationReq duoApplicationReq = new DuoApplicationReq();
            duoApplicationReq.apiHost(this.apiHost);
            duoApplicationReq.integrationKey(this.integrationKey);
            duoApplicationReq.name(this.name);
            duoApplicationReq.secretKey(this.secretKey);
            return duoApplicationReq;
        }

        /**
         * Execute applicationPost request
         * @return DuoApplication
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public DuoApplication execute() throws ApiException {
            DuoApplicationReq duoApplicationReq = buildBodyParams();
            ApiResponse<DuoApplication> localVarResp = applicationPostWithHttpInfo(accountId, xOrgId, duoApplicationReq);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute applicationPost request with HTTP info returned
         * @return ApiResponse&lt;DuoApplication&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<DuoApplication> executeWithHttpInfo() throws ApiException {
            DuoApplicationReq duoApplicationReq = buildBodyParams();
            return applicationPostWithHttpInfo(accountId, xOrgId, duoApplicationReq);
        }

        /**
         * Execute applicationPost request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DuoApplication> _callback) throws ApiException {
            DuoApplicationReq duoApplicationReq = buildBodyParams();
            return applicationPostAsync(accountId, xOrgId, duoApplicationReq, _callback);
        }
    }

    /**
     * Create Duo Application
     * Creates a Duo application for your organization and the specified account.  #### Sample Request &#x60;&#x60;&#x60;   curl -X POST https://console.jumpcloud.com/api/v2/duo/accounts/{ACCOUNT_ID}/applications \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;name\&quot;: \&quot;Application Name\&quot;,     \&quot;apiHost\&quot;: \&quot;api-1234.duosecurity.com\&quot;,     \&quot;integrationKey\&quot;: \&quot;1234\&quot;,     \&quot;secretKey\&quot;: \&quot;5678\&quot;   }&#39; &#x60;&#x60;&#x60;
     * @param accountId  (required)
     * @return ApplicationPostRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ApplicationPostRequestBuilder applicationPost(String apiHost, String integrationKey, String name, String secretKey, String accountId) throws IllegalArgumentException {
        if (apiHost == null) throw new IllegalArgumentException("\"apiHost\" is required but got null");
            

        if (integrationKey == null) throw new IllegalArgumentException("\"integrationKey\" is required but got null");
            

        if (name == null) throw new IllegalArgumentException("\"name\" is required but got null");
            

        if (secretKey == null) throw new IllegalArgumentException("\"secretKey\" is required but got null");
            

        if (accountId == null) throw new IllegalArgumentException("\"accountId\" is required but got null");
            

        return new ApplicationPostRequestBuilder(apiHost, integrationKey, name, secretKey, accountId);
    }
    private okhttp3.Call applicationUpdateCall(String accountId, String applicationId, String xOrgId, DuoApplicationUpdateReq duoApplicationUpdateReq, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = duoApplicationUpdateReq;

        // create path and map variables
        String localVarPath = "/duo/accounts/{account_id}/applications/{application_id}"
            .replace("{" + "account_id" + "}", localVarApiClient.escapeString(accountId.toString()))
            .replace("{" + "application_id" + "}", localVarApiClient.escapeString(applicationId.toString()));

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
    private okhttp3.Call applicationUpdateValidateBeforeCall(String accountId, String applicationId, String xOrgId, DuoApplicationUpdateReq duoApplicationUpdateReq, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'accountId' is set
        if (accountId == null) {
            throw new ApiException("Missing the required parameter 'accountId' when calling applicationUpdate(Async)");
        }

        // verify the required parameter 'applicationId' is set
        if (applicationId == null) {
            throw new ApiException("Missing the required parameter 'applicationId' when calling applicationUpdate(Async)");
        }

        return applicationUpdateCall(accountId, applicationId, xOrgId, duoApplicationUpdateReq, _callback);

    }


    private ApiResponse<DuoApplication> applicationUpdateWithHttpInfo(String accountId, String applicationId, String xOrgId, DuoApplicationUpdateReq duoApplicationUpdateReq) throws ApiException {
        okhttp3.Call localVarCall = applicationUpdateValidateBeforeCall(accountId, applicationId, xOrgId, duoApplicationUpdateReq, null);
        Type localVarReturnType = new TypeToken<DuoApplication>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call applicationUpdateAsync(String accountId, String applicationId, String xOrgId, DuoApplicationUpdateReq duoApplicationUpdateReq, final ApiCallback<DuoApplication> _callback) throws ApiException {

        okhttp3.Call localVarCall = applicationUpdateValidateBeforeCall(accountId, applicationId, xOrgId, duoApplicationUpdateReq, _callback);
        Type localVarReturnType = new TypeToken<DuoApplication>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ApplicationUpdateRequestBuilder {
        private final String apiHost;
        private final String integrationKey;
        private final String name;
        private final String accountId;
        private final String applicationId;
        private String secretKey;
        private String xOrgId;

        private ApplicationUpdateRequestBuilder(String apiHost, String integrationKey, String name, String accountId, String applicationId) {
            this.apiHost = apiHost;
            this.integrationKey = integrationKey;
            this.name = name;
            this.accountId = accountId;
            this.applicationId = applicationId;
        }

        /**
         * Set secretKey
         * @param secretKey  (optional)
         * @return ApplicationUpdateRequestBuilder
         */
        public ApplicationUpdateRequestBuilder secretKey(String secretKey) {
            this.secretKey = secretKey;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ApplicationUpdateRequestBuilder
         */
        public ApplicationUpdateRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for applicationUpdate
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
            DuoApplicationUpdateReq duoApplicationUpdateReq = buildBodyParams();
            return applicationUpdateCall(accountId, applicationId, xOrgId, duoApplicationUpdateReq, _callback);
        }

        private DuoApplicationUpdateReq buildBodyParams() {
            DuoApplicationUpdateReq duoApplicationUpdateReq = new DuoApplicationUpdateReq();
            duoApplicationUpdateReq.apiHost(this.apiHost);
            duoApplicationUpdateReq.integrationKey(this.integrationKey);
            duoApplicationUpdateReq.name(this.name);
            duoApplicationUpdateReq.secretKey(this.secretKey);
            return duoApplicationUpdateReq;
        }

        /**
         * Execute applicationUpdate request
         * @return DuoApplication
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public DuoApplication execute() throws ApiException {
            DuoApplicationUpdateReq duoApplicationUpdateReq = buildBodyParams();
            ApiResponse<DuoApplication> localVarResp = applicationUpdateWithHttpInfo(accountId, applicationId, xOrgId, duoApplicationUpdateReq);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute applicationUpdate request with HTTP info returned
         * @return ApiResponse&lt;DuoApplication&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<DuoApplication> executeWithHttpInfo() throws ApiException {
            DuoApplicationUpdateReq duoApplicationUpdateReq = buildBodyParams();
            return applicationUpdateWithHttpInfo(accountId, applicationId, xOrgId, duoApplicationUpdateReq);
        }

        /**
         * Execute applicationUpdate request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DuoApplication> _callback) throws ApiException {
            DuoApplicationUpdateReq duoApplicationUpdateReq = buildBodyParams();
            return applicationUpdateAsync(accountId, applicationId, xOrgId, duoApplicationUpdateReq, _callback);
        }
    }

    /**
     * Update Duo Application
     * Updates the specified Duo application.  #### Sample Request &#x60;&#x60;&#x60;   curl -X PUT https://console.jumpcloud.com/api/v2/duo/accounts/{ACCOUNT_ID}/applications/{APPLICATION_ID} \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;name\&quot;: \&quot;Application Name\&quot;,     \&quot;apiHost\&quot;: \&quot;api-1234.duosecurity.com\&quot;,     \&quot;integrationKey\&quot;: \&quot;1234\&quot;,     \&quot;secretKey\&quot;: \&quot;5678\&quot;   }&#39; &#x60;&#x60;&#x60;
     * @param accountId  (required)
     * @param applicationId  (required)
     * @return ApplicationUpdateRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ApplicationUpdateRequestBuilder applicationUpdate(String apiHost, String integrationKey, String name, String accountId, String applicationId) throws IllegalArgumentException {
        if (apiHost == null) throw new IllegalArgumentException("\"apiHost\" is required but got null");
            

        if (integrationKey == null) throw new IllegalArgumentException("\"integrationKey\" is required but got null");
            

        if (name == null) throw new IllegalArgumentException("\"name\" is required but got null");
            

        if (accountId == null) throw new IllegalArgumentException("\"accountId\" is required but got null");
            

        if (applicationId == null) throw new IllegalArgumentException("\"applicationId\" is required but got null");
            

        return new ApplicationUpdateRequestBuilder(apiHost, integrationKey, name, accountId, applicationId);
    }
}
