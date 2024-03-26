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
import com.konfigthis.client.model.DomainsInsertRequest;
import com.konfigthis.client.model.GoogleRpcStatus;
import com.konfigthis.client.model.GraphConnection;
import com.konfigthis.client.model.GraphObjectWithPaths;
import com.konfigthis.client.model.GraphOperationOffice365;
import com.konfigthis.client.model.O365DomainResponse;
import com.konfigthis.client.model.O365DomainsListResponse;
import com.konfigthis.client.model.Office365;
import com.konfigthis.client.model.Office365BuiltinTranslation;
import com.konfigthis.client.model.Office365DirectionTranslation;
import com.konfigthis.client.model.Office365SListImportUsersResponse;
import com.konfigthis.client.model.Office365TranslationRule;
import com.konfigthis.client.model.Office365TranslationRuleRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;

public class Office365ApiGenerated {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public Office365ApiGenerated() throws IllegalArgumentException {
        this(Configuration.getDefaultApiClient());
    }

    public Office365ApiGenerated(ApiClient apiClient) throws IllegalArgumentException {
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

    private okhttp3.Call deleteCall(byte[] office365Id, byte[] domainId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/office365s/{office365_id}/domains/{domain_id}"
            .replace("{" + "office365_id" + "}", localVarApiClient.escapeString(office365Id.toString()))
            .replace("{" + "domain_id" + "}", localVarApiClient.escapeString(domainId.toString()));

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
    private okhttp3.Call deleteValidateBeforeCall(byte[] office365Id, byte[] domainId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'office365Id' is set
        if (office365Id == null) {
            throw new ApiException("Missing the required parameter 'office365Id' when calling delete(Async)");
        }

        // verify the required parameter 'domainId' is set
        if (domainId == null) {
            throw new ApiException("Missing the required parameter 'domainId' when calling delete(Async)");
        }

        return deleteCall(office365Id, domainId, _callback);

    }


    private ApiResponse<O365DomainResponse> deleteWithHttpInfo(byte[] office365Id, byte[] domainId) throws ApiException {
        okhttp3.Call localVarCall = deleteValidateBeforeCall(office365Id, domainId, null);
        Type localVarReturnType = new TypeToken<O365DomainResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call deleteAsync(byte[] office365Id, byte[] domainId, final ApiCallback<O365DomainResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteValidateBeforeCall(office365Id, domainId, _callback);
        Type localVarReturnType = new TypeToken<O365DomainResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class DeleteRequestBuilder {
        private final byte[] office365Id;
        private final byte[] domainId;

        private DeleteRequestBuilder(byte[] office365Id, byte[] domainId) {
            this.office365Id = office365Id;
            this.domainId = domainId;
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
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return deleteCall(office365Id, domainId, _callback);
        }


        /**
         * Execute delete request
         * @return O365DomainResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public O365DomainResponse execute() throws ApiException {
            ApiResponse<O365DomainResponse> localVarResp = deleteWithHttpInfo(office365Id, domainId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute delete request with HTTP info returned
         * @return ApiResponse&lt;O365DomainResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<O365DomainResponse> executeWithHttpInfo() throws ApiException {
            return deleteWithHttpInfo(office365Id, domainId);
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
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<O365DomainResponse> _callback) throws ApiException {
            return deleteAsync(office365Id, domainId, _callback);
        }
    }

    /**
     * Delete a domain from an Office 365 instance
     * Delete a domain from a specific M365/Azure AD directory sync integration instance.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/office365s/{OFFICE365_ID}/domains/{DOMAIN_ID} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param office365Id Id for the specific M365/Azure AD directory sync integration instance. (required)
     * @param domainId ObjectID of the domain to be deleted. (required)
     * @return DeleteRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
        <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
     </table>
     */
    public DeleteRequestBuilder delete(byte[] office365Id, byte[] domainId) throws IllegalArgumentException {
        if (office365Id == null) throw new IllegalArgumentException("\"office365Id\" is required but got null");
        if (domainId == null) throw new IllegalArgumentException("\"domainId\" is required but got null");
        return new DeleteRequestBuilder(office365Id, domainId);
    }
    private okhttp3.Call getCall(String office365Id, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/office365s/{office365_id}"
            .replace("{" + "office365_id" + "}", localVarApiClient.escapeString(office365Id.toString()));

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
    private okhttp3.Call getValidateBeforeCall(String office365Id, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'office365Id' is set
        if (office365Id == null) {
            throw new ApiException("Missing the required parameter 'office365Id' when calling get(Async)");
        }

        return getCall(office365Id, xOrgId, _callback);

    }


    private ApiResponse<Office365> getWithHttpInfo(String office365Id, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = getValidateBeforeCall(office365Id, xOrgId, null);
        Type localVarReturnType = new TypeToken<Office365>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getAsync(String office365Id, String xOrgId, final ApiCallback<Office365> _callback) throws ApiException {

        okhttp3.Call localVarCall = getValidateBeforeCall(office365Id, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<Office365>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetRequestBuilder {
        private final String office365Id;
        private String xOrgId;

        private GetRequestBuilder(String office365Id) {
            this.office365Id = office365Id;
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
            return getCall(office365Id, xOrgId, _callback);
        }


        /**
         * Execute get request
         * @return Office365
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public Office365 execute() throws ApiException {
            ApiResponse<Office365> localVarResp = getWithHttpInfo(office365Id, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute get request with HTTP info returned
         * @return ApiResponse&lt;Office365&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Office365> executeWithHttpInfo() throws ApiException {
            return getWithHttpInfo(office365Id, xOrgId);
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
        public okhttp3.Call executeAsync(final ApiCallback<Office365> _callback) throws ApiException {
            return getAsync(office365Id, xOrgId, _callback);
        }
    }

    /**
     * Get Office 365 instance
     * This endpoint returns a specific Office 365 instance.  #####  Sample Request  &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/office365s/{OFFICE365_ID} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;  &#x60;&#x60;&#x60;
     * @param office365Id ObjectID of the Office 365 instance. (required)
     * @return GetRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public GetRequestBuilder get(String office365Id) throws IllegalArgumentException {
        if (office365Id == null) throw new IllegalArgumentException("\"office365Id\" is required but got null");
            

        return new GetRequestBuilder(office365Id);
    }
    private okhttp3.Call insertCall(byte[] office365Id, DomainsInsertRequest domainsInsertRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = domainsInsertRequest;

        // create path and map variables
        String localVarPath = "/office365s/{office365_id}/domains"
            .replace("{" + "office365_id" + "}", localVarApiClient.escapeString(office365Id.toString()));

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
    private okhttp3.Call insertValidateBeforeCall(byte[] office365Id, DomainsInsertRequest domainsInsertRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'office365Id' is set
        if (office365Id == null) {
            throw new ApiException("Missing the required parameter 'office365Id' when calling insert(Async)");
        }

        // verify the required parameter 'domainsInsertRequest' is set
        if (domainsInsertRequest == null) {
            throw new ApiException("Missing the required parameter 'domainsInsertRequest' when calling insert(Async)");
        }

        return insertCall(office365Id, domainsInsertRequest, _callback);

    }


    private ApiResponse<O365DomainResponse> insertWithHttpInfo(byte[] office365Id, DomainsInsertRequest domainsInsertRequest) throws ApiException {
        okhttp3.Call localVarCall = insertValidateBeforeCall(office365Id, domainsInsertRequest, null);
        Type localVarReturnType = new TypeToken<O365DomainResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call insertAsync(byte[] office365Id, DomainsInsertRequest domainsInsertRequest, final ApiCallback<O365DomainResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = insertValidateBeforeCall(office365Id, domainsInsertRequest, _callback);
        Type localVarReturnType = new TypeToken<O365DomainResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class InsertRequestBuilder {
        private final byte[] office365Id;
        private String domain;

        private InsertRequestBuilder(byte[] office365Id) {
            this.office365Id = office365Id;
        }

        /**
         * Set domain
         * @param domain  (optional)
         * @return InsertRequestBuilder
         */
        public InsertRequestBuilder domain(String domain) {
            this.domain = domain;
            return this;
        }
        
        /**
         * Build call for insert
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
            DomainsInsertRequest domainsInsertRequest = buildBodyParams();
            return insertCall(office365Id, domainsInsertRequest, _callback);
        }

        private DomainsInsertRequest buildBodyParams() {
            DomainsInsertRequest domainsInsertRequest = new DomainsInsertRequest();
            domainsInsertRequest.domain(this.domain);
            return domainsInsertRequest;
        }

        /**
         * Execute insert request
         * @return O365DomainResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
            <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public O365DomainResponse execute() throws ApiException {
            DomainsInsertRequest domainsInsertRequest = buildBodyParams();
            ApiResponse<O365DomainResponse> localVarResp = insertWithHttpInfo(office365Id, domainsInsertRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute insert request with HTTP info returned
         * @return ApiResponse&lt;O365DomainResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
            <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<O365DomainResponse> executeWithHttpInfo() throws ApiException {
            DomainsInsertRequest domainsInsertRequest = buildBodyParams();
            return insertWithHttpInfo(office365Id, domainsInsertRequest);
        }

        /**
         * Execute insert request (asynchronously)
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
        public okhttp3.Call executeAsync(final ApiCallback<O365DomainResponse> _callback) throws ApiException {
            DomainsInsertRequest domainsInsertRequest = buildBodyParams();
            return insertAsync(office365Id, domainsInsertRequest, _callback);
        }
    }

    /**
     * Add a domain to an Office 365 instance
     * Add a domain to a specific M365/Azure AD directory sync integration instance. The domain must be a verified domain in M365/Azure AD.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/office365s/{OFFICE365_ID}/domains \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{\&quot;domain\&quot;: \&quot;{domain name}\&quot;}&#39; &#x60;&#x60;&#x60;
     * @param office365Id Id for the specific M365/Azure AD directory sync integration instance. (required)
     * @param domainsInsertRequest  (required)
     * @return InsertRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A successful response. </td><td>  -  </td></tr>
        <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
        <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
     </table>
     */
    public InsertRequestBuilder insert(byte[] office365Id) throws IllegalArgumentException {
        if (office365Id == null) throw new IllegalArgumentException("\"office365Id\" is required but got null");
        return new InsertRequestBuilder(office365Id);
    }
    private okhttp3.Call listCall(byte[] office365Id, String limit, String skip, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/office365s/{office365_id}/domains"
            .replace("{" + "office365_id" + "}", localVarApiClient.escapeString(office365Id.toString()));

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
    private okhttp3.Call listValidateBeforeCall(byte[] office365Id, String limit, String skip, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'office365Id' is set
        if (office365Id == null) {
            throw new ApiException("Missing the required parameter 'office365Id' when calling list(Async)");
        }

        return listCall(office365Id, limit, skip, _callback);

    }


    private ApiResponse<O365DomainsListResponse> listWithHttpInfo(byte[] office365Id, String limit, String skip) throws ApiException {
        okhttp3.Call localVarCall = listValidateBeforeCall(office365Id, limit, skip, null);
        Type localVarReturnType = new TypeToken<O365DomainsListResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listAsync(byte[] office365Id, String limit, String skip, final ApiCallback<O365DomainsListResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listValidateBeforeCall(office365Id, limit, skip, _callback);
        Type localVarReturnType = new TypeToken<O365DomainsListResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListRequestBuilder {
        private final byte[] office365Id;
        private String limit;
        private String skip;

        private ListRequestBuilder(byte[] office365Id) {
            this.office365Id = office365Id;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 100)
         * @return ListRequestBuilder
         */
        public ListRequestBuilder limit(String limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListRequestBuilder
         */
        public ListRequestBuilder skip(String skip) {
            this.skip = skip;
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
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listCall(office365Id, limit, skip, _callback);
        }


        /**
         * Execute list request
         * @return O365DomainsListResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public O365DomainsListResponse execute() throws ApiException {
            ApiResponse<O365DomainsListResponse> localVarResp = listWithHttpInfo(office365Id, limit, skip);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute list request with HTTP info returned
         * @return ApiResponse&lt;O365DomainsListResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<O365DomainsListResponse> executeWithHttpInfo() throws ApiException {
            return listWithHttpInfo(office365Id, limit, skip);
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
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<O365DomainsListResponse> _callback) throws ApiException {
            return listAsync(office365Id, limit, skip, _callback);
        }
    }

    /**
     * List all domains configured for an Office 365 instance
     * List the domains configured for a specific M365/Azure AD directory sync integration instance.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/office365s/{OFFICE365_ID}/domains \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param office365Id Id for the specific M365/Azure AD directory sync integration instance. (required)
     * @return ListRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
        <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
     </table>
     */
    public ListRequestBuilder list(byte[] office365Id) throws IllegalArgumentException {
        if (office365Id == null) throw new IllegalArgumentException("\"office365Id\" is required but got null");
        return new ListRequestBuilder(office365Id);
    }
    private okhttp3.Call listImportUsersCall(String office365Id, String consistencyLevel, Integer top, String skipToken, String filter, String search, String orderby, Boolean count, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/office365s/{office365_id}/import/users"
            .replace("{" + "office365_id" + "}", localVarApiClient.escapeString(office365Id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (top != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("top", top));
        }

        if (skipToken != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("skipToken", skipToken));
        }

        if (filter != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("filter", filter));
        }

        if (search != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("search", search));
        }

        if (orderby != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("orderby", orderby));
        }

        if (count != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("count", count));
        }

        if (consistencyLevel != null) {
            localVarHeaderParams.put("ConsistencyLevel", localVarApiClient.parameterToString(consistencyLevel));
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
    private okhttp3.Call listImportUsersValidateBeforeCall(String office365Id, String consistencyLevel, Integer top, String skipToken, String filter, String search, String orderby, Boolean count, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'office365Id' is set
        if (office365Id == null) {
            throw new ApiException("Missing the required parameter 'office365Id' when calling listImportUsers(Async)");
        }

        return listImportUsersCall(office365Id, consistencyLevel, top, skipToken, filter, search, orderby, count, _callback);

    }


    private ApiResponse<Office365SListImportUsersResponse> listImportUsersWithHttpInfo(String office365Id, String consistencyLevel, Integer top, String skipToken, String filter, String search, String orderby, Boolean count) throws ApiException {
        okhttp3.Call localVarCall = listImportUsersValidateBeforeCall(office365Id, consistencyLevel, top, skipToken, filter, search, orderby, count, null);
        Type localVarReturnType = new TypeToken<Office365SListImportUsersResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listImportUsersAsync(String office365Id, String consistencyLevel, Integer top, String skipToken, String filter, String search, String orderby, Boolean count, final ApiCallback<Office365SListImportUsersResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listImportUsersValidateBeforeCall(office365Id, consistencyLevel, top, skipToken, filter, search, orderby, count, _callback);
        Type localVarReturnType = new TypeToken<Office365SListImportUsersResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListImportUsersRequestBuilder {
        private final String office365Id;
        private String consistencyLevel;
        private Integer top;
        private String skipToken;
        private String filter;
        private String search;
        private String orderby;
        private Boolean count;

        private ListImportUsersRequestBuilder(String office365Id) {
            this.office365Id = office365Id;
        }

        /**
         * Set consistencyLevel
         * @param consistencyLevel Defines the consistency header for O365 requests. See https://docs.microsoft.com/en-us/graph/api/user-list?view&#x3D;graph-rest-1.0&amp;tabs&#x3D;http#request-headers (optional)
         * @return ListImportUsersRequestBuilder
         */
        public ListImportUsersRequestBuilder consistencyLevel(String consistencyLevel) {
            this.consistencyLevel = consistencyLevel;
            return this;
        }
        
        /**
         * Set top
         * @param top Office 365 API maximum number of results per page. See https://docs.microsoft.com/en-us/graph/paging. (optional)
         * @return ListImportUsersRequestBuilder
         */
        public ListImportUsersRequestBuilder top(Integer top) {
            this.top = top;
            return this;
        }
        
        /**
         * Set skipToken
         * @param skipToken Office 365 API token used to access the next page of results. See https://docs.microsoft.com/en-us/graph/paging. (optional)
         * @return ListImportUsersRequestBuilder
         */
        public ListImportUsersRequestBuilder skipToken(String skipToken) {
            this.skipToken = skipToken;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Office 365 API filter parameter. See https://docs.microsoft.com/en-us/graph/api/user-list?view&#x3D;graph-rest-1.0&amp;tabs&#x3D;http#optional-query-parameters. (optional)
         * @return ListImportUsersRequestBuilder
         */
        public ListImportUsersRequestBuilder filter(String filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set search
         * @param search Office 365 API search parameter. See https://docs.microsoft.com/en-us/graph/api/user-list?view&#x3D;graph-rest-1.0&amp;tabs&#x3D;http#optional-query-parameters. (optional)
         * @return ListImportUsersRequestBuilder
         */
        public ListImportUsersRequestBuilder search(String search) {
            this.search = search;
            return this;
        }
        
        /**
         * Set orderby
         * @param orderby Office 365 API orderby parameter. See https://docs.microsoft.com/en-us/graph/api/user-list?view&#x3D;graph-rest-1.0&amp;tabs&#x3D;http#optional-query-parameters. (optional)
         * @return ListImportUsersRequestBuilder
         */
        public ListImportUsersRequestBuilder orderby(String orderby) {
            this.orderby = orderby;
            return this;
        }
        
        /**
         * Set count
         * @param count Office 365 API count parameter. See https://docs.microsoft.com/en-us/graph/api/user-list?view&#x3D;graph-rest-1.0&amp;tabs&#x3D;http#optional-query-parameters. (optional)
         * @return ListImportUsersRequestBuilder
         */
        public ListImportUsersRequestBuilder count(Boolean count) {
            this.count = count;
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
            return listImportUsersCall(office365Id, consistencyLevel, top, skipToken, filter, search, orderby, count, _callback);
        }


        /**
         * Execute listImportUsers request
         * @return Office365SListImportUsersResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public Office365SListImportUsersResponse execute() throws ApiException {
            ApiResponse<Office365SListImportUsersResponse> localVarResp = listImportUsersWithHttpInfo(office365Id, consistencyLevel, top, skipToken, filter, search, orderby, count);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listImportUsers request with HTTP info returned
         * @return ApiResponse&lt;Office365SListImportUsersResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Office365SListImportUsersResponse> executeWithHttpInfo() throws ApiException {
            return listImportUsersWithHttpInfo(office365Id, consistencyLevel, top, skipToken, filter, search, orderby, count);
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
        public okhttp3.Call executeAsync(final ApiCallback<Office365SListImportUsersResponse> _callback) throws ApiException {
            return listImportUsersAsync(office365Id, consistencyLevel, top, skipToken, filter, search, orderby, count, _callback);
        }
    }

    /**
     * Get a list of users to import from an Office 365 instance
     * Lists Office 365 users available for import.
     * @param office365Id  (required)
     * @return ListImportUsersRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public ListImportUsersRequestBuilder listImportUsers(String office365Id) throws IllegalArgumentException {
        if (office365Id == null) throw new IllegalArgumentException("\"office365Id\" is required but got null");
            

        return new ListImportUsersRequestBuilder(office365Id);
    }
    private okhttp3.Call office365AssociationsListCall(String office365Id, List<String> targets, Integer limit, Integer skip, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/office365s/{office365_id}/associations"
            .replace("{" + "office365_id" + "}", localVarApiClient.escapeString(office365Id.toString()));

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
    private okhttp3.Call office365AssociationsListValidateBeforeCall(String office365Id, List<String> targets, Integer limit, Integer skip, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'office365Id' is set
        if (office365Id == null) {
            throw new ApiException("Missing the required parameter 'office365Id' when calling office365AssociationsList(Async)");
        }

        // verify the required parameter 'targets' is set
        if (targets == null) {
            throw new ApiException("Missing the required parameter 'targets' when calling office365AssociationsList(Async)");
        }

        return office365AssociationsListCall(office365Id, targets, limit, skip, xOrgId, _callback);

    }


    private ApiResponse<List<GraphConnection>> office365AssociationsListWithHttpInfo(String office365Id, List<String> targets, Integer limit, Integer skip, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = office365AssociationsListValidateBeforeCall(office365Id, targets, limit, skip, xOrgId, null);
        Type localVarReturnType = new TypeToken<List<GraphConnection>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call office365AssociationsListAsync(String office365Id, List<String> targets, Integer limit, Integer skip, String xOrgId, final ApiCallback<List<GraphConnection>> _callback) throws ApiException {

        okhttp3.Call localVarCall = office365AssociationsListValidateBeforeCall(office365Id, targets, limit, skip, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<List<GraphConnection>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class Office365AssociationsListRequestBuilder {
        private final String office365Id;
        private final List<String> targets;
        private Integer limit;
        private Integer skip;
        private String xOrgId;

        private Office365AssociationsListRequestBuilder(String office365Id, List<String> targets) {
            this.office365Id = office365Id;
            this.targets = targets;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return Office365AssociationsListRequestBuilder
         */
        public Office365AssociationsListRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return Office365AssociationsListRequestBuilder
         */
        public Office365AssociationsListRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return Office365AssociationsListRequestBuilder
         */
        public Office365AssociationsListRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for office365AssociationsList
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
            return office365AssociationsListCall(office365Id, targets, limit, skip, xOrgId, _callback);
        }


        /**
         * Execute office365AssociationsList request
         * @return List&lt;GraphConnection&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphConnection> execute() throws ApiException {
            ApiResponse<List<GraphConnection>> localVarResp = office365AssociationsListWithHttpInfo(office365Id, targets, limit, skip, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute office365AssociationsList request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphConnection&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphConnection>> executeWithHttpInfo() throws ApiException {
            return office365AssociationsListWithHttpInfo(office365Id, targets, limit, skip, xOrgId);
        }

        /**
         * Execute office365AssociationsList request (asynchronously)
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
            return office365AssociationsListAsync(office365Id, targets, limit, skip, xOrgId, _callback);
        }
    }

    /**
     * List the associations of an Office 365 instance
     * This endpoint returns _direct_ associations of an Office 365 instance.   A direct association can be a non-homogeneous relationship between 2 different objects, for example Office 365 and Users.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET &#39;https://console.jumpcloud.com/api/v2/office365s/{OFFICE365_ID}/associations?targets&#x3D;user_group&#39; \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;  &#x60;&#x60;&#x60;
     * @param office365Id ObjectID of the Office 365 instance. (required)
     * @param targets Targets which a \&quot;office_365\&quot; can be associated to. (required)
     * @return Office365AssociationsListRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public Office365AssociationsListRequestBuilder office365AssociationsList(String office365Id, List<String> targets) throws IllegalArgumentException {
        if (office365Id == null) throw new IllegalArgumentException("\"office365Id\" is required but got null");
            

        if (targets == null) throw new IllegalArgumentException("\"targets\" is required but got null");
        return new Office365AssociationsListRequestBuilder(office365Id, targets);
    }
    private okhttp3.Call office365AssociationsPostCall(String office365Id, String xOrgId, GraphOperationOffice365 graphOperationOffice365, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = graphOperationOffice365;

        // create path and map variables
        String localVarPath = "/office365s/{office365_id}/associations"
            .replace("{" + "office365_id" + "}", localVarApiClient.escapeString(office365Id.toString()));

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
    private okhttp3.Call office365AssociationsPostValidateBeforeCall(String office365Id, String xOrgId, GraphOperationOffice365 graphOperationOffice365, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'office365Id' is set
        if (office365Id == null) {
            throw new ApiException("Missing the required parameter 'office365Id' when calling office365AssociationsPost(Async)");
        }

        return office365AssociationsPostCall(office365Id, xOrgId, graphOperationOffice365, _callback);

    }


    private ApiResponse<Void> office365AssociationsPostWithHttpInfo(String office365Id, String xOrgId, GraphOperationOffice365 graphOperationOffice365) throws ApiException {
        okhttp3.Call localVarCall = office365AssociationsPostValidateBeforeCall(office365Id, xOrgId, graphOperationOffice365, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call office365AssociationsPostAsync(String office365Id, String xOrgId, GraphOperationOffice365 graphOperationOffice365, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = office365AssociationsPostValidateBeforeCall(office365Id, xOrgId, graphOperationOffice365, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class Office365AssociationsPostRequestBuilder {
        private final String office365Id;
        private String id;
        private String op;
        private Map<String, Object> attributes;
        private String type;
        private String xOrgId;

        private Office365AssociationsPostRequestBuilder(String office365Id) {
            this.office365Id = office365Id;
        }

        /**
         * Set id
         * @param id The ObjectID of graph object being added or removed as an association. (optional)
         * @return Office365AssociationsPostRequestBuilder
         */
        public Office365AssociationsPostRequestBuilder id(String id) {
            this.id = id;
            return this;
        }
        
        /**
         * Set op
         * @param op How to modify the graph connection. (optional)
         * @return Office365AssociationsPostRequestBuilder
         */
        public Office365AssociationsPostRequestBuilder op(String op) {
            this.op = op;
            return this;
        }
        
        /**
         * Set attributes
         * @param attributes The graph attributes. (optional)
         * @return Office365AssociationsPostRequestBuilder
         */
        public Office365AssociationsPostRequestBuilder attributes(Map<String, Object> attributes) {
            this.attributes = attributes;
            return this;
        }
        
        /**
         * Set type
         * @param type Targets which a \\\&quot;office_365\\\&quot; can be associated to. (optional)
         * @return Office365AssociationsPostRequestBuilder
         */
        public Office365AssociationsPostRequestBuilder type(String type) {
            this.type = type;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return Office365AssociationsPostRequestBuilder
         */
        public Office365AssociationsPostRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for office365AssociationsPost
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
            GraphOperationOffice365 graphOperationOffice365 = buildBodyParams();
            return office365AssociationsPostCall(office365Id, xOrgId, graphOperationOffice365, _callback);
        }

        private GraphOperationOffice365 buildBodyParams() {
            GraphOperationOffice365 graphOperationOffice365 = new GraphOperationOffice365();
            return graphOperationOffice365;
        }

        /**
         * Execute office365AssociationsPost request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            GraphOperationOffice365 graphOperationOffice365 = buildBodyParams();
            office365AssociationsPostWithHttpInfo(office365Id, xOrgId, graphOperationOffice365);
        }

        /**
         * Execute office365AssociationsPost request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            GraphOperationOffice365 graphOperationOffice365 = buildBodyParams();
            return office365AssociationsPostWithHttpInfo(office365Id, xOrgId, graphOperationOffice365);
        }

        /**
         * Execute office365AssociationsPost request (asynchronously)
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
            GraphOperationOffice365 graphOperationOffice365 = buildBodyParams();
            return office365AssociationsPostAsync(office365Id, xOrgId, graphOperationOffice365, _callback);
        }
    }

    /**
     * Manage the associations of an Office 365 instance
     * This endpoint allows you to manage the _direct_ associations of a Office 365 instance.  A direct association can be a non-homogeneous relationship between 2 different objects, for example Office 365 and Users.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/office365s/{OFFICE365_ID}/associations \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;op\&quot;: \&quot;add\&quot;,     \&quot;type\&quot;: \&quot;user_group\&quot;,     \&quot;id\&quot;: \&quot;{Group_ID}\&quot;   }&#39; &#x60;&#x60;&#x60;
     * @param office365Id ObjectID of the Office 365 instance. (required)
     * @return Office365AssociationsPostRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public Office365AssociationsPostRequestBuilder office365AssociationsPost(String office365Id) throws IllegalArgumentException {
        if (office365Id == null) throw new IllegalArgumentException("\"office365Id\" is required but got null");
            

        return new Office365AssociationsPostRequestBuilder(office365Id);
    }
    private okhttp3.Call office365DeleteCall(String office365Id, String id, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/office365s/{office365_id}/translationrules/{id}"
            .replace("{" + "office365_id" + "}", localVarApiClient.escapeString(office365Id.toString()))
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
    private okhttp3.Call office365DeleteValidateBeforeCall(String office365Id, String id, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'office365Id' is set
        if (office365Id == null) {
            throw new ApiException("Missing the required parameter 'office365Id' when calling office365Delete(Async)");
        }

        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling office365Delete(Async)");
        }

        return office365DeleteCall(office365Id, id, _callback);

    }


    private ApiResponse<Void> office365DeleteWithHttpInfo(String office365Id, String id) throws ApiException {
        okhttp3.Call localVarCall = office365DeleteValidateBeforeCall(office365Id, id, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call office365DeleteAsync(String office365Id, String id, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = office365DeleteValidateBeforeCall(office365Id, id, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class Office365DeleteRequestBuilder {
        private final String office365Id;
        private final String id;

        private Office365DeleteRequestBuilder(String office365Id, String id) {
            this.office365Id = office365Id;
            this.id = id;
        }

        /**
         * Build call for office365Delete
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
            return office365DeleteCall(office365Id, id, _callback);
        }


        /**
         * Execute office365Delete request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            office365DeleteWithHttpInfo(office365Id, id);
        }

        /**
         * Execute office365Delete request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            return office365DeleteWithHttpInfo(office365Id, id);
        }

        /**
         * Execute office365Delete request (asynchronously)
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
            return office365DeleteAsync(office365Id, id, _callback);
        }
    }

    /**
     * Deletes a Office 365 translation rule
     * This endpoint allows you to delete a translation rule for a specific Office 365 instance. These rules specify how JumpCloud attributes translate to [Microsoft Graph](https://developer.microsoft.com/en-us/graph) attributes.  #### Sample Request  &#x60;&#x60;&#x60; curl -X DELETE https://console.jumpcloud.com/api/v2/office365s/{office365_id}/translationrules/{id} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;   &#x60;&#x60;&#x60;
     * @param office365Id  (required)
     * @param id  (required)
     * @return Office365DeleteRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public Office365DeleteRequestBuilder office365Delete(String office365Id, String id) throws IllegalArgumentException {
        if (office365Id == null) throw new IllegalArgumentException("\"office365Id\" is required but got null");
            

        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new Office365DeleteRequestBuilder(office365Id, id);
    }
    private okhttp3.Call office365GetCall(String office365Id, String id, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/office365s/{office365_id}/translationrules/{id}"
            .replace("{" + "office365_id" + "}", localVarApiClient.escapeString(office365Id.toString()))
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
    private okhttp3.Call office365GetValidateBeforeCall(String office365Id, String id, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'office365Id' is set
        if (office365Id == null) {
            throw new ApiException("Missing the required parameter 'office365Id' when calling office365Get(Async)");
        }

        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling office365Get(Async)");
        }

        return office365GetCall(office365Id, id, _callback);

    }


    private ApiResponse<Office365TranslationRule> office365GetWithHttpInfo(String office365Id, String id) throws ApiException {
        okhttp3.Call localVarCall = office365GetValidateBeforeCall(office365Id, id, null);
        Type localVarReturnType = new TypeToken<Office365TranslationRule>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call office365GetAsync(String office365Id, String id, final ApiCallback<Office365TranslationRule> _callback) throws ApiException {

        okhttp3.Call localVarCall = office365GetValidateBeforeCall(office365Id, id, _callback);
        Type localVarReturnType = new TypeToken<Office365TranslationRule>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class Office365GetRequestBuilder {
        private final String office365Id;
        private final String id;

        private Office365GetRequestBuilder(String office365Id, String id) {
            this.office365Id = office365Id;
            this.id = id;
        }

        /**
         * Build call for office365Get
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
            return office365GetCall(office365Id, id, _callback);
        }


        /**
         * Execute office365Get request
         * @return Office365TranslationRule
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public Office365TranslationRule execute() throws ApiException {
            ApiResponse<Office365TranslationRule> localVarResp = office365GetWithHttpInfo(office365Id, id);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute office365Get request with HTTP info returned
         * @return ApiResponse&lt;Office365TranslationRule&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Office365TranslationRule> executeWithHttpInfo() throws ApiException {
            return office365GetWithHttpInfo(office365Id, id);
        }

        /**
         * Execute office365Get request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Office365TranslationRule> _callback) throws ApiException {
            return office365GetAsync(office365Id, id, _callback);
        }
    }

    /**
     * Gets a specific Office 365 translation rule
     * This endpoint returns a specific translation rule for a specific Office 365 instance. These rules specify how JumpCloud attributes translate to [Microsoft Graph](https://developer.microsoft.com/en-us/graph) attributes.  ###### Sample Request  &#x60;&#x60;&#x60;   curl -X GET https://console.jumpcloud.com/api/v2/office365s/{office365_id}/translationrules/{id} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;   &#x60;&#x60;&#x60;
     * @param office365Id  (required)
     * @param id  (required)
     * @return Office365GetRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public Office365GetRequestBuilder office365Get(String office365Id, String id) throws IllegalArgumentException {
        if (office365Id == null) throw new IllegalArgumentException("\"office365Id\" is required but got null");
            

        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new Office365GetRequestBuilder(office365Id, id);
    }
    private okhttp3.Call office365ListCall(String office365Id, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/office365s/{office365_id}/translationrules"
            .replace("{" + "office365_id" + "}", localVarApiClient.escapeString(office365Id.toString()));

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
    private okhttp3.Call office365ListValidateBeforeCall(String office365Id, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'office365Id' is set
        if (office365Id == null) {
            throw new ApiException("Missing the required parameter 'office365Id' when calling office365List(Async)");
        }

        return office365ListCall(office365Id, fields, filter, limit, skip, sort, _callback);

    }


    private ApiResponse<List<Office365TranslationRule>> office365ListWithHttpInfo(String office365Id, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort) throws ApiException {
        okhttp3.Call localVarCall = office365ListValidateBeforeCall(office365Id, fields, filter, limit, skip, sort, null);
        Type localVarReturnType = new TypeToken<List<Office365TranslationRule>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call office365ListAsync(String office365Id, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback<List<Office365TranslationRule>> _callback) throws ApiException {

        okhttp3.Call localVarCall = office365ListValidateBeforeCall(office365Id, fields, filter, limit, skip, sort, _callback);
        Type localVarReturnType = new TypeToken<List<Office365TranslationRule>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class Office365ListRequestBuilder {
        private final String office365Id;
        private List<String> fields;
        private List<String> filter;
        private Integer limit;
        private Integer skip;
        private List<String> sort;

        private Office365ListRequestBuilder(String office365Id) {
            this.office365Id = office365Id;
        }

        /**
         * Set fields
         * @param fields The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  (optional)
         * @return Office365ListRequestBuilder
         */
        public Office365ListRequestBuilder fields(List<String> fields) {
            this.fields = fields;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return Office365ListRequestBuilder
         */
        public Office365ListRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return Office365ListRequestBuilder
         */
        public Office365ListRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return Office365ListRequestBuilder
         */
        public Office365ListRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  (optional)
         * @return Office365ListRequestBuilder
         */
        public Office365ListRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Build call for office365List
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
            return office365ListCall(office365Id, fields, filter, limit, skip, sort, _callback);
        }


        /**
         * Execute office365List request
         * @return List&lt;Office365TranslationRule&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<Office365TranslationRule> execute() throws ApiException {
            ApiResponse<List<Office365TranslationRule>> localVarResp = office365ListWithHttpInfo(office365Id, fields, filter, limit, skip, sort);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute office365List request with HTTP info returned
         * @return ApiResponse&lt;List&lt;Office365TranslationRule&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<Office365TranslationRule>> executeWithHttpInfo() throws ApiException {
            return office365ListWithHttpInfo(office365Id, fields, filter, limit, skip, sort);
        }

        /**
         * Execute office365List request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<Office365TranslationRule>> _callback) throws ApiException {
            return office365ListAsync(office365Id, fields, filter, limit, skip, sort, _callback);
        }
    }

    /**
     * List all the Office 365 Translation Rules
     * This endpoint returns all translation rules for a specific Office 365 instance. These rules specify how JumpCloud attributes translate to [Microsoft Graph](https://developer.microsoft.com/en-us/graph) attributes.  ##### Sample Request  &#x60;&#x60;&#x60;  curl -X GET  https://console.jumpcloud.com/api/v2/office365s/{office365_id}/translationrules \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;   &#x60;&#x60;&#x60;
     * @param office365Id  (required)
     * @return Office365ListRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public Office365ListRequestBuilder office365List(String office365Id) throws IllegalArgumentException {
        if (office365Id == null) throw new IllegalArgumentException("\"office365Id\" is required but got null");
            

        return new Office365ListRequestBuilder(office365Id);
    }
    private okhttp3.Call office365PostCall(String office365Id, Office365TranslationRuleRequest office365TranslationRuleRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = office365TranslationRuleRequest;

        // create path and map variables
        String localVarPath = "/office365s/{office365_id}/translationrules"
            .replace("{" + "office365_id" + "}", localVarApiClient.escapeString(office365Id.toString()));

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
    private okhttp3.Call office365PostValidateBeforeCall(String office365Id, Office365TranslationRuleRequest office365TranslationRuleRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'office365Id' is set
        if (office365Id == null) {
            throw new ApiException("Missing the required parameter 'office365Id' when calling office365Post(Async)");
        }

        return office365PostCall(office365Id, office365TranslationRuleRequest, _callback);

    }


    private ApiResponse<Office365TranslationRule> office365PostWithHttpInfo(String office365Id, Office365TranslationRuleRequest office365TranslationRuleRequest) throws ApiException {
        okhttp3.Call localVarCall = office365PostValidateBeforeCall(office365Id, office365TranslationRuleRequest, null);
        Type localVarReturnType = new TypeToken<Office365TranslationRule>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call office365PostAsync(String office365Id, Office365TranslationRuleRequest office365TranslationRuleRequest, final ApiCallback<Office365TranslationRule> _callback) throws ApiException {

        okhttp3.Call localVarCall = office365PostValidateBeforeCall(office365Id, office365TranslationRuleRequest, _callback);
        Type localVarReturnType = new TypeToken<Office365TranslationRule>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class Office365PostRequestBuilder {
        private final String office365Id;
        private Office365BuiltinTranslation builtIn;
        private Office365DirectionTranslation direction;

        private Office365PostRequestBuilder(String office365Id) {
            this.office365Id = office365Id;
        }

        /**
         * Set builtIn
         * @param builtIn  (optional)
         * @return Office365PostRequestBuilder
         */
        public Office365PostRequestBuilder builtIn(Office365BuiltinTranslation builtIn) {
            this.builtIn = builtIn;
            return this;
        }
        
        /**
         * Set direction
         * @param direction  (optional, default to export)
         * @return Office365PostRequestBuilder
         */
        public Office365PostRequestBuilder direction(Office365DirectionTranslation direction) {
            this.direction = direction;
            return this;
        }
        
        /**
         * Build call for office365Post
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
            Office365TranslationRuleRequest office365TranslationRuleRequest = buildBodyParams();
            return office365PostCall(office365Id, office365TranslationRuleRequest, _callback);
        }

        private Office365TranslationRuleRequest buildBodyParams() {
            Office365TranslationRuleRequest office365TranslationRuleRequest = new Office365TranslationRuleRequest();
            office365TranslationRuleRequest.builtIn(this.builtIn);
            office365TranslationRuleRequest.direction(this.direction);
            return office365TranslationRuleRequest;
        }

        /**
         * Execute office365Post request
         * @return Office365TranslationRule
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public Office365TranslationRule execute() throws ApiException {
            Office365TranslationRuleRequest office365TranslationRuleRequest = buildBodyParams();
            ApiResponse<Office365TranslationRule> localVarResp = office365PostWithHttpInfo(office365Id, office365TranslationRuleRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute office365Post request with HTTP info returned
         * @return ApiResponse&lt;Office365TranslationRule&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Office365TranslationRule> executeWithHttpInfo() throws ApiException {
            Office365TranslationRuleRequest office365TranslationRuleRequest = buildBodyParams();
            return office365PostWithHttpInfo(office365Id, office365TranslationRuleRequest);
        }

        /**
         * Execute office365Post request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Office365TranslationRule> _callback) throws ApiException {
            Office365TranslationRuleRequest office365TranslationRuleRequest = buildBodyParams();
            return office365PostAsync(office365Id, office365TranslationRuleRequest, _callback);
        }
    }

    /**
     * Create a new Office 365 Translation Rule
     * This endpoint allows you to create a translation rule for a specific Office 365 instance. These rules specify how JumpCloud attributes translate to [Microsoft Graph](https://developer.microsoft.com/en-us/graph) attributes.  ##### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/office365s/{office365_id}/translationrules \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     {Translation Rule Parameters}   }&#39; &#x60;&#x60;&#x60;
     * @param office365Id  (required)
     * @return Office365PostRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public Office365PostRequestBuilder office365Post(String office365Id) throws IllegalArgumentException {
        if (office365Id == null) throw new IllegalArgumentException("\"office365Id\" is required but got null");
            

        return new Office365PostRequestBuilder(office365Id);
    }
    private okhttp3.Call office365TraverseUserCall(String office365Id, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/office365s/{office365_id}/users"
            .replace("{" + "office365_id" + "}", localVarApiClient.escapeString(office365Id.toString()));

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
    private okhttp3.Call office365TraverseUserValidateBeforeCall(String office365Id, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'office365Id' is set
        if (office365Id == null) {
            throw new ApiException("Missing the required parameter 'office365Id' when calling office365TraverseUser(Async)");
        }

        return office365TraverseUserCall(office365Id, limit, xOrgId, skip, filter, _callback);

    }


    private ApiResponse<List<GraphObjectWithPaths>> office365TraverseUserWithHttpInfo(String office365Id, Integer limit, String xOrgId, Integer skip, List<String> filter) throws ApiException {
        okhttp3.Call localVarCall = office365TraverseUserValidateBeforeCall(office365Id, limit, xOrgId, skip, filter, null);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call office365TraverseUserAsync(String office365Id, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {

        okhttp3.Call localVarCall = office365TraverseUserValidateBeforeCall(office365Id, limit, xOrgId, skip, filter, _callback);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class Office365TraverseUserRequestBuilder {
        private final String office365Id;
        private Integer limit;
        private String xOrgId;
        private Integer skip;
        private List<String> filter;

        private Office365TraverseUserRequestBuilder(String office365Id) {
            this.office365Id = office365Id;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return Office365TraverseUserRequestBuilder
         */
        public Office365TraverseUserRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return Office365TraverseUserRequestBuilder
         */
        public Office365TraverseUserRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return Office365TraverseUserRequestBuilder
         */
        public Office365TraverseUserRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return Office365TraverseUserRequestBuilder
         */
        public Office365TraverseUserRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Build call for office365TraverseUser
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
            return office365TraverseUserCall(office365Id, limit, xOrgId, skip, filter, _callback);
        }


        /**
         * Execute office365TraverseUser request
         * @return List&lt;GraphObjectWithPaths&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphObjectWithPaths> execute() throws ApiException {
            ApiResponse<List<GraphObjectWithPaths>> localVarResp = office365TraverseUserWithHttpInfo(office365Id, limit, xOrgId, skip, filter);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute office365TraverseUser request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphObjectWithPaths&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphObjectWithPaths>> executeWithHttpInfo() throws ApiException {
            return office365TraverseUserWithHttpInfo(office365Id, limit, xOrgId, skip, filter);
        }

        /**
         * Execute office365TraverseUser request (asynchronously)
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
            return office365TraverseUserAsync(office365Id, limit, xOrgId, skip, filter, _callback);
        }
    }

    /**
     * List the Users bound to an Office 365 instance
     * This endpoint will return all Users bound to an Office 365 instance, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this Office 365 instance to the corresponding User; this array represents all grouping and/or associations that would have to be removed to deprovision the User from this Office 365 instance.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/office365s/{OFFICE365_ID}/users \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param office365Id ObjectID of the Office 365 suite. (required)
     * @return Office365TraverseUserRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public Office365TraverseUserRequestBuilder office365TraverseUser(String office365Id) throws IllegalArgumentException {
        if (office365Id == null) throw new IllegalArgumentException("\"office365Id\" is required but got null");
            

        return new Office365TraverseUserRequestBuilder(office365Id);
    }
    private okhttp3.Call office365TraverseUserGroupCall(String office365Id, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/office365s/{office365_id}/usergroups"
            .replace("{" + "office365_id" + "}", localVarApiClient.escapeString(office365Id.toString()));

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
    private okhttp3.Call office365TraverseUserGroupValidateBeforeCall(String office365Id, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'office365Id' is set
        if (office365Id == null) {
            throw new ApiException("Missing the required parameter 'office365Id' when calling office365TraverseUserGroup(Async)");
        }

        return office365TraverseUserGroupCall(office365Id, limit, xOrgId, skip, filter, _callback);

    }


    private ApiResponse<List<GraphObjectWithPaths>> office365TraverseUserGroupWithHttpInfo(String office365Id, Integer limit, String xOrgId, Integer skip, List<String> filter) throws ApiException {
        okhttp3.Call localVarCall = office365TraverseUserGroupValidateBeforeCall(office365Id, limit, xOrgId, skip, filter, null);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call office365TraverseUserGroupAsync(String office365Id, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {

        okhttp3.Call localVarCall = office365TraverseUserGroupValidateBeforeCall(office365Id, limit, xOrgId, skip, filter, _callback);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class Office365TraverseUserGroupRequestBuilder {
        private final String office365Id;
        private Integer limit;
        private String xOrgId;
        private Integer skip;
        private List<String> filter;

        private Office365TraverseUserGroupRequestBuilder(String office365Id) {
            this.office365Id = office365Id;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return Office365TraverseUserGroupRequestBuilder
         */
        public Office365TraverseUserGroupRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return Office365TraverseUserGroupRequestBuilder
         */
        public Office365TraverseUserGroupRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return Office365TraverseUserGroupRequestBuilder
         */
        public Office365TraverseUserGroupRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return Office365TraverseUserGroupRequestBuilder
         */
        public Office365TraverseUserGroupRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Build call for office365TraverseUserGroup
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
            return office365TraverseUserGroupCall(office365Id, limit, xOrgId, skip, filter, _callback);
        }


        /**
         * Execute office365TraverseUserGroup request
         * @return List&lt;GraphObjectWithPaths&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphObjectWithPaths> execute() throws ApiException {
            ApiResponse<List<GraphObjectWithPaths>> localVarResp = office365TraverseUserGroupWithHttpInfo(office365Id, limit, xOrgId, skip, filter);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute office365TraverseUserGroup request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphObjectWithPaths&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphObjectWithPaths>> executeWithHttpInfo() throws ApiException {
            return office365TraverseUserGroupWithHttpInfo(office365Id, limit, xOrgId, skip, filter);
        }

        /**
         * Execute office365TraverseUserGroup request (asynchronously)
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
            return office365TraverseUserGroupAsync(office365Id, limit, xOrgId, skip, filter, _callback);
        }
    }

    /**
     * List the User Groups bound to an Office 365 instance
     * This endpoint will return all Users Groups bound to an Office 365 instance, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the group&#39;s type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this Office 365 instance to the corresponding User Group; this array represents all grouping and/or associations that would have to be removed to deprovision the User Group from this Office 365 instance.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60;   curl -X GET https://console.jumpcloud.com/api/v2/office365s/{OFFICE365_ID/usergroups \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param office365Id ObjectID of the Office 365 suite. (required)
     * @return Office365TraverseUserGroupRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public Office365TraverseUserGroupRequestBuilder office365TraverseUserGroup(String office365Id) throws IllegalArgumentException {
        if (office365Id == null) throw new IllegalArgumentException("\"office365Id\" is required but got null");
            

        return new Office365TraverseUserGroupRequestBuilder(office365Id);
    }
    private okhttp3.Call patchCall(String office365Id, String xOrgId, Office365 office365, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = office365;

        // create path and map variables
        String localVarPath = "/office365s/{office365_id}"
            .replace("{" + "office365_id" + "}", localVarApiClient.escapeString(office365Id.toString()));

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
    private okhttp3.Call patchValidateBeforeCall(String office365Id, String xOrgId, Office365 office365, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'office365Id' is set
        if (office365Id == null) {
            throw new ApiException("Missing the required parameter 'office365Id' when calling patch(Async)");
        }

        return patchCall(office365Id, xOrgId, office365, _callback);

    }


    private ApiResponse<Office365> patchWithHttpInfo(String office365Id, String xOrgId, Office365 office365) throws ApiException {
        okhttp3.Call localVarCall = patchValidateBeforeCall(office365Id, xOrgId, office365, null);
        Type localVarReturnType = new TypeToken<Office365>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call patchAsync(String office365Id, String xOrgId, Office365 office365, final ApiCallback<Office365> _callback) throws ApiException {

        okhttp3.Call localVarCall = patchValidateBeforeCall(office365Id, xOrgId, office365, _callback);
        Type localVarReturnType = new TypeToken<Office365>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class PatchRequestBuilder {
        private final String office365Id;
        private DefaultDomain defaultDomain;
        private Boolean groupsEnabled;
        private String id;
        private String name;
        private String userLockoutAction;
        private String userPasswordExpirationAction;
        private String xOrgId;

        private PatchRequestBuilder(String office365Id) {
            this.office365Id = office365Id;
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
            Office365 office365 = buildBodyParams();
            return patchCall(office365Id, xOrgId, office365, _callback);
        }

        private Office365 buildBodyParams() {
            Office365 office365 = new Office365();
            office365.defaultDomain(this.defaultDomain);
            office365.groupsEnabled(this.groupsEnabled);
            office365.id(this.id);
            office365.name(this.name);
            if (this.userLockoutAction != null)
            office365.userLockoutAction(Office365.UserLockoutActionEnum.fromValue(this.userLockoutAction));
            if (this.userPasswordExpirationAction != null)
            office365.userPasswordExpirationAction(Office365.UserPasswordExpirationActionEnum.fromValue(this.userPasswordExpirationAction));
            return office365;
        }

        /**
         * Execute patch request
         * @return Office365
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public Office365 execute() throws ApiException {
            Office365 office365 = buildBodyParams();
            ApiResponse<Office365> localVarResp = patchWithHttpInfo(office365Id, xOrgId, office365);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute patch request with HTTP info returned
         * @return ApiResponse&lt;Office365&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Office365> executeWithHttpInfo() throws ApiException {
            Office365 office365 = buildBodyParams();
            return patchWithHttpInfo(office365Id, xOrgId, office365);
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
        public okhttp3.Call executeAsync(final ApiCallback<Office365> _callback) throws ApiException {
            Office365 office365 = buildBodyParams();
            return patchAsync(office365Id, xOrgId, office365, _callback);
        }
    }

    /**
     * Update existing Office 365 instance.
     * This endpoint allows updating some attributes of an Office 365 instance.  #####  Sample Request  &#x60;&#x60;&#x60; curl -X PATCH https://console.jumpcloud.com/api/v2/office365s/{OFFICE365_ID} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;userLockoutAction\&quot;: \&quot;maintain\&quot;,     \&quot;userPasswordExpirationAction\&quot;: \&quot;suspend\&quot;,   }&#39; &#x60;&#x60;&#x60;  Sample Request, set a default domain  &#x60;&#x60;&#x60; curl -X PATCH https://console.jumpcloud.com/api/v2/office365s/{OFFICE365_ID} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;defaultDomain\&quot;: {         \&quot;id\&quot;: \&quot;{domainObjectID}\&quot;       }   }&#39; &#x60;&#x60;&#x60;  Sample Request, unset the default domain  &#x60;&#x60;&#x60; curl -X PATCH https://console.jumpcloud.com/api/v2/office365s/{OFFICE365_ID} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;defaultDomain\&quot;: {}   }&#39; &#x60;&#x60;&#x60;
     * @param office365Id ObjectID of the Office 365 instance. (required)
     * @return PatchRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public PatchRequestBuilder patch(String office365Id) throws IllegalArgumentException {
        if (office365Id == null) throw new IllegalArgumentException("\"office365Id\" is required but got null");
            

        return new PatchRequestBuilder(office365Id);
    }
}
