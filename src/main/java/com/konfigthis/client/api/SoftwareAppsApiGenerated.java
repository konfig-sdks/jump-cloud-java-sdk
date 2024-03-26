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
import com.konfigthis.client.model.GraphOperationSoftwareApp;
import com.konfigthis.client.model.JumpcloudPackageValidatorValidateApplicationInstallPackageRequest;
import com.konfigthis.client.model.JumpcloudPackageValidatorValidateApplicationInstallPackageResponse;
import com.konfigthis.client.model.SoftwareApp;
import com.konfigthis.client.model.SoftwareAppCreate;
import com.konfigthis.client.model.SoftwareAppReclaimLicenses;
import com.konfigthis.client.model.SoftwareAppSettings;
import com.konfigthis.client.model.SoftwareAppStatus;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;

public class SoftwareAppsApiGenerated {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public SoftwareAppsApiGenerated() throws IllegalArgumentException {
        this(Configuration.getDefaultApiClient());
    }

    public SoftwareAppsApiGenerated(ApiClient apiClient) throws IllegalArgumentException {
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
        String localVarPath = "/softwareapps/{id}"
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


    private ApiResponse<Void> deleteWithHttpInfo(String id, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = deleteValidateBeforeCall(id, xOrgId, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call deleteAsync(String id, String xOrgId, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteValidateBeforeCall(id, xOrgId, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
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
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return deleteCall(id, xOrgId, _callback);
        }


        /**
         * Execute delete request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            deleteWithHttpInfo(id, xOrgId);
        }

        /**
         * Execute delete request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
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
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Void> _callback) throws ApiException {
            return deleteAsync(id, xOrgId, _callback);
        }
    }

    /**
     * Delete a configured Software Application
     * Removes a Software Application configuration.  Warning: This is a destructive operation and will unmanage the application on all affected systems.  #### Sample Request &#x60;&#x60;&#x60; curl -X DELETE https://console.jumpcloud.com/api/v2/softwareapps/{id} \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param id  (required)
     * @return DeleteRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
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
        String localVarPath = "/softwareapps/{id}"
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


    private ApiResponse<SoftwareApp> getWithHttpInfo(String id, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = getValidateBeforeCall(id, xOrgId, null);
        Type localVarReturnType = new TypeToken<SoftwareApp>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getAsync(String id, String xOrgId, final ApiCallback<SoftwareApp> _callback) throws ApiException {

        okhttp3.Call localVarCall = getValidateBeforeCall(id, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<SoftwareApp>(){}.getType();
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
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getCall(id, xOrgId, _callback);
        }


        /**
         * Execute get request
         * @return SoftwareApp
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public SoftwareApp execute() throws ApiException {
            ApiResponse<SoftwareApp> localVarResp = getWithHttpInfo(id, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute get request with HTTP info returned
         * @return ApiResponse&lt;SoftwareApp&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<SoftwareApp> executeWithHttpInfo() throws ApiException {
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
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<SoftwareApp> _callback) throws ApiException {
            return getAsync(id, xOrgId, _callback);
        }
    }

    /**
     * Retrieve a configured Software Application.
     * Retrieves a Software Application. The optional isConfigEnabled and appConfiguration apple_vpp attributes are populated in this response.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/softwareapps/{id} \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param id  (required)
     * @return GetRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public GetRequestBuilder get(String id) throws IllegalArgumentException {
        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new GetRequestBuilder(id);
    }
    private okhttp3.Call listCall(String xOrgId, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/softwareapps";

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
    private okhttp3.Call listValidateBeforeCall(String xOrgId, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback _callback) throws ApiException {
        return listCall(xOrgId, filter, limit, skip, sort, _callback);

    }


    private ApiResponse<List<SoftwareApp>> listWithHttpInfo(String xOrgId, List<String> filter, Integer limit, Integer skip, List<String> sort) throws ApiException {
        okhttp3.Call localVarCall = listValidateBeforeCall(xOrgId, filter, limit, skip, sort, null);
        Type localVarReturnType = new TypeToken<List<SoftwareApp>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listAsync(String xOrgId, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback<List<SoftwareApp>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listValidateBeforeCall(xOrgId, filter, limit, skip, sort, _callback);
        Type localVarReturnType = new TypeToken<List<SoftwareApp>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListRequestBuilder {
        private String xOrgId;
        private List<String> filter;
        private Integer limit;
        private Integer skip;
        private List<String> sort;

        private ListRequestBuilder() {
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
            return listCall(xOrgId, filter, limit, skip, sort, _callback);
        }


        /**
         * Execute list request
         * @return List&lt;SoftwareApp&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<SoftwareApp> execute() throws ApiException {
            ApiResponse<List<SoftwareApp>> localVarResp = listWithHttpInfo(xOrgId, filter, limit, skip, sort);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute list request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SoftwareApp&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SoftwareApp>> executeWithHttpInfo() throws ApiException {
            return listWithHttpInfo(xOrgId, filter, limit, skip, sort);
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
        public okhttp3.Call executeAsync(final ApiCallback<List<SoftwareApp>> _callback) throws ApiException {
            return listAsync(xOrgId, filter, limit, skip, sort, _callback);
        }
    }

    /**
     * Get all configured Software Applications.
     * This endpoint allows you to get all configured Software Applications that will be managed by JumpCloud on associated JumpCloud systems. The optional isConfigEnabled and appConfiguration apple_vpp attributes are not included in the response.  #### Sample Request &#x60;&#x60;&#x60; $ curl -X GET https://console.jumpcloud.com/api/v2/softwareapps \\ -H &#39;Accept: application/json&#39; \\ -H &#39;Content-Type: application/json&#39; \\ -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     * @return ListRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public ListRequestBuilder list() throws IllegalArgumentException {
        return new ListRequestBuilder();
    }
    private okhttp3.Call list_0Call(String softwareAppId, String xOrgId, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/softwareapps/{software_app_id}/statuses"
            .replace("{" + "software_app_id" + "}", localVarApiClient.escapeString(softwareAppId.toString()));

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
    private okhttp3.Call list_0ValidateBeforeCall(String softwareAppId, String xOrgId, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'softwareAppId' is set
        if (softwareAppId == null) {
            throw new ApiException("Missing the required parameter 'softwareAppId' when calling list_0(Async)");
        }

        return list_0Call(softwareAppId, xOrgId, filter, limit, skip, sort, _callback);

    }


    private ApiResponse<List<SoftwareAppStatus>> list_0WithHttpInfo(String softwareAppId, String xOrgId, List<String> filter, Integer limit, Integer skip, List<String> sort) throws ApiException {
        okhttp3.Call localVarCall = list_0ValidateBeforeCall(softwareAppId, xOrgId, filter, limit, skip, sort, null);
        Type localVarReturnType = new TypeToken<List<SoftwareAppStatus>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call list_0Async(String softwareAppId, String xOrgId, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback<List<SoftwareAppStatus>> _callback) throws ApiException {

        okhttp3.Call localVarCall = list_0ValidateBeforeCall(softwareAppId, xOrgId, filter, limit, skip, sort, _callback);
        Type localVarReturnType = new TypeToken<List<SoftwareAppStatus>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class List0RequestBuilder {
        private final String softwareAppId;
        private String xOrgId;
        private List<String> filter;
        private Integer limit;
        private Integer skip;
        private List<String> sort;

        private List0RequestBuilder(String softwareAppId) {
            this.softwareAppId = softwareAppId;
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return List0RequestBuilder
         */
        public List0RequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return List0RequestBuilder
         */
        public List0RequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return List0RequestBuilder
         */
        public List0RequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return List0RequestBuilder
         */
        public List0RequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  (optional)
         * @return List0RequestBuilder
         */
        public List0RequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Build call for list_0
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
            return list_0Call(softwareAppId, xOrgId, filter, limit, skip, sort, _callback);
        }


        /**
         * Execute list_0 request
         * @return List&lt;SoftwareAppStatus&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<SoftwareAppStatus> execute() throws ApiException {
            ApiResponse<List<SoftwareAppStatus>> localVarResp = list_0WithHttpInfo(softwareAppId, xOrgId, filter, limit, skip, sort);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute list_0 request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SoftwareAppStatus&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SoftwareAppStatus>> executeWithHttpInfo() throws ApiException {
            return list_0WithHttpInfo(softwareAppId, xOrgId, filter, limit, skip, sort);
        }

        /**
         * Execute list_0 request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SoftwareAppStatus>> _callback) throws ApiException {
            return list_0Async(softwareAppId, xOrgId, filter, limit, skip, sort, _callback);
        }
    }

    /**
     * Get the status of the provided Software Application
     * This endpoint allows you to get the status of the provided Software Application on associated JumpCloud systems.  #### Sample Request &#x60;&#x60;&#x60; $ curl -X GET https://console.jumpcloud.com/api/v2/softwareapps/{software_app_id}/statuses \\ -H &#39;Accept: application/json&#39; \\ -H &#39;Content-Type: application/json&#39; \\ -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;
     * @param softwareAppId ObjectID of the Software App. (required)
     * @return List0RequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public List0RequestBuilder list_0(String softwareAppId) throws IllegalArgumentException {
        if (softwareAppId == null) throw new IllegalArgumentException("\"softwareAppId\" is required but got null");
            

        return new List0RequestBuilder(softwareAppId);
    }
    private okhttp3.Call postCall(String xOrgId, SoftwareApp softwareApp, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = softwareApp;

        // create path and map variables
        String localVarPath = "/softwareapps";

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
    private okhttp3.Call postValidateBeforeCall(String xOrgId, SoftwareApp softwareApp, final ApiCallback _callback) throws ApiException {
        return postCall(xOrgId, softwareApp, _callback);

    }


    private ApiResponse<SoftwareAppCreate> postWithHttpInfo(String xOrgId, SoftwareApp softwareApp) throws ApiException {
        okhttp3.Call localVarCall = postValidateBeforeCall(xOrgId, softwareApp, null);
        Type localVarReturnType = new TypeToken<SoftwareAppCreate>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call postAsync(String xOrgId, SoftwareApp softwareApp, final ApiCallback<SoftwareAppCreate> _callback) throws ApiException {

        okhttp3.Call localVarCall = postValidateBeforeCall(xOrgId, softwareApp, _callback);
        Type localVarReturnType = new TypeToken<SoftwareAppCreate>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class PostRequestBuilder {
        private String displayName;
        private String id;
        private List<SoftwareAppSettings> settings;
        private String xOrgId;

        private PostRequestBuilder() {
        }

        /**
         * Set displayName
         * @param displayName  (optional)
         * @return PostRequestBuilder
         */
        public PostRequestBuilder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }
        
        /**
         * Set id
         * @param id  (optional)
         * @return PostRequestBuilder
         */
        public PostRequestBuilder id(String id) {
            this.id = id;
            return this;
        }
        
        /**
         * Set settings
         * @param settings  (optional)
         * @return PostRequestBuilder
         */
        public PostRequestBuilder settings(List<SoftwareAppSettings> settings) {
            this.settings = settings;
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
            <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            SoftwareApp softwareApp = buildBodyParams();
            return postCall(xOrgId, softwareApp, _callback);
        }

        private SoftwareApp buildBodyParams() {
            SoftwareApp softwareApp = new SoftwareApp();
            softwareApp.displayName(this.displayName);
            softwareApp.id(this.id);
            softwareApp.settings(this.settings);
            return softwareApp;
        }

        /**
         * Execute post request
         * @return SoftwareAppCreate
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
         </table>
         */
        public SoftwareAppCreate execute() throws ApiException {
            SoftwareApp softwareApp = buildBodyParams();
            ApiResponse<SoftwareAppCreate> localVarResp = postWithHttpInfo(xOrgId, softwareApp);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute post request with HTTP info returned
         * @return ApiResponse&lt;SoftwareAppCreate&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<SoftwareAppCreate> executeWithHttpInfo() throws ApiException {
            SoftwareApp softwareApp = buildBodyParams();
            return postWithHttpInfo(xOrgId, softwareApp);
        }

        /**
         * Execute post request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<SoftwareAppCreate> _callback) throws ApiException {
            SoftwareApp softwareApp = buildBodyParams();
            return postAsync(xOrgId, softwareApp, _callback);
        }
    }

    /**
     * Create a Software Application that will be managed by JumpCloud.
     * This endpoint allows you to create a Software Application that will be managed by JumpCloud on associated JumpCloud systems. The optional isConfigEnabled and appConfiguration apple_vpp attributes are not included in the response.  #### Sample Request &#x60;&#x60;&#x60; $ curl -X POST https://console.jumpcloud.com/api/v2/softwareapps \\ -H &#39;Accept: application/json&#39; \\ -H &#39;Content-Type: application/json&#39; \\ -H &#39;x-api-key: {API_KEY}&#39; \\ -d &#39;{   \&quot;displayName\&quot;: \&quot;Adobe Reader\&quot;,   \&quot;settings\&quot;: [{\&quot;packageId\&quot;: \&quot;adobereader\&quot;}] }&#39; &#x60;&#x60;&#x60;
     * @return PostRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
     </table>
     */
    public PostRequestBuilder post() throws IllegalArgumentException {
        return new PostRequestBuilder();
    }
    private okhttp3.Call reclaimLicensesCall(String softwareAppId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/softwareapps/{software_app_id}/reclaim-licenses"
            .replace("{" + "software_app_id" + "}", localVarApiClient.escapeString(softwareAppId.toString()));

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
    private okhttp3.Call reclaimLicensesValidateBeforeCall(String softwareAppId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'softwareAppId' is set
        if (softwareAppId == null) {
            throw new ApiException("Missing the required parameter 'softwareAppId' when calling reclaimLicenses(Async)");
        }

        return reclaimLicensesCall(softwareAppId, _callback);

    }


    private ApiResponse<SoftwareAppReclaimLicenses> reclaimLicensesWithHttpInfo(String softwareAppId) throws ApiException {
        okhttp3.Call localVarCall = reclaimLicensesValidateBeforeCall(softwareAppId, null);
        Type localVarReturnType = new TypeToken<SoftwareAppReclaimLicenses>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call reclaimLicensesAsync(String softwareAppId, final ApiCallback<SoftwareAppReclaimLicenses> _callback) throws ApiException {

        okhttp3.Call localVarCall = reclaimLicensesValidateBeforeCall(softwareAppId, _callback);
        Type localVarReturnType = new TypeToken<SoftwareAppReclaimLicenses>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ReclaimLicensesRequestBuilder {
        private final String softwareAppId;

        private ReclaimLicensesRequestBuilder(String softwareAppId) {
            this.softwareAppId = softwareAppId;
        }

        /**
         * Build call for reclaimLicenses
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> Reclaim Licenses Response </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return reclaimLicensesCall(softwareAppId, _callback);
        }


        /**
         * Execute reclaimLicenses request
         * @return SoftwareAppReclaimLicenses
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> Reclaim Licenses Response </td><td>  -  </td></tr>
         </table>
         */
        public SoftwareAppReclaimLicenses execute() throws ApiException {
            ApiResponse<SoftwareAppReclaimLicenses> localVarResp = reclaimLicensesWithHttpInfo(softwareAppId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute reclaimLicenses request with HTTP info returned
         * @return ApiResponse&lt;SoftwareAppReclaimLicenses&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> Reclaim Licenses Response </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<SoftwareAppReclaimLicenses> executeWithHttpInfo() throws ApiException {
            return reclaimLicensesWithHttpInfo(softwareAppId);
        }

        /**
         * Execute reclaimLicenses request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> Reclaim Licenses Response </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<SoftwareAppReclaimLicenses> _callback) throws ApiException {
            return reclaimLicensesAsync(softwareAppId, _callback);
        }
    }

    /**
     * Reclaim Licenses for a Software Application.
     * This endpoint allows you to reclaim the licenses from a software app associated with devices that are deleted. #### Sample Request &#x60;&#x60;&#x60; $ curl -X POST https://console.jumpcloud.com/api/v2/softwareapps/{software_app_id}/reclaim-licenses \\ -H &#39;Accept: application/json&#39; \\ -H &#39;Content-Type: application/json&#39; \\ -H &#39;x-api-key: {API_KEY}&#39; \\ -d &#39;{}&#39; &#x60;&#x60;&#x60;
     * @param softwareAppId  (required)
     * @return ReclaimLicensesRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> Reclaim Licenses Response </td><td>  -  </td></tr>
     </table>
     */
    public ReclaimLicensesRequestBuilder reclaimLicenses(String softwareAppId) throws IllegalArgumentException {
        if (softwareAppId == null) throw new IllegalArgumentException("\"softwareAppId\" is required but got null");
            

        return new ReclaimLicensesRequestBuilder(softwareAppId);
    }
    private okhttp3.Call retryInstallationCall(String softwareAppId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/softwareapps/{software_app_id}/retry-installation"
            .replace("{" + "software_app_id" + "}", localVarApiClient.escapeString(softwareAppId.toString()));

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
    private okhttp3.Call retryInstallationValidateBeforeCall(String softwareAppId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'softwareAppId' is set
        if (softwareAppId == null) {
            throw new ApiException("Missing the required parameter 'softwareAppId' when calling retryInstallation(Async)");
        }

        return retryInstallationCall(softwareAppId, _callback);

    }


    private ApiResponse<Void> retryInstallationWithHttpInfo(String softwareAppId) throws ApiException {
        okhttp3.Call localVarCall = retryInstallationValidateBeforeCall(softwareAppId, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call retryInstallationAsync(String softwareAppId, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = retryInstallationValidateBeforeCall(softwareAppId, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class RetryInstallationRequestBuilder {
        private final String softwareAppId;

        private RetryInstallationRequestBuilder(String softwareAppId) {
            this.softwareAppId = softwareAppId;
        }

        /**
         * Build call for retryInstallation
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
            return retryInstallationCall(softwareAppId, _callback);
        }


        /**
         * Execute retryInstallation request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            retryInstallationWithHttpInfo(softwareAppId);
        }

        /**
         * Execute retryInstallation request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            return retryInstallationWithHttpInfo(softwareAppId);
        }

        /**
         * Execute retryInstallation request (asynchronously)
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
            return retryInstallationAsync(softwareAppId, _callback);
        }
    }

    /**
     * Retry Installation for a Software Application
     * This endpoints initiates an installation retry of an Apple VPP App for the provided system IDs #### Sample Request &#x60;&#x60;&#x60; $ curl -X POST https://console.jumpcloud.com/api/v2/softwareapps/{software_app_id}/retry-installation \\ -H &#39;Accept: application/json&#39; \\ -H &#39;Content-Type: application/json&#39; \\ -H &#39;x-api-key: {API_KEY}&#39; \\ -d &#39;{\&quot;system_ids\&quot;: \&quot;{&lt;system_id_1&gt;, &lt;system_id_2&gt;, ...}\&quot;}&#39; &#x60;&#x60;&#x60;
     * @param softwareAppId  (required)
     * @return RetryInstallationRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
     </table>
     */
    public RetryInstallationRequestBuilder retryInstallation(String softwareAppId) throws IllegalArgumentException {
        if (softwareAppId == null) throw new IllegalArgumentException("\"softwareAppId\" is required but got null");
            

        return new RetryInstallationRequestBuilder(softwareAppId);
    }
    private okhttp3.Call softwareappsAssociationsListCall(String softwareAppId, List<String> targets, Integer limit, Integer skip, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/softwareapps/{software_app_id}/associations"
            .replace("{" + "software_app_id" + "}", localVarApiClient.escapeString(softwareAppId.toString()));

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
    private okhttp3.Call softwareappsAssociationsListValidateBeforeCall(String softwareAppId, List<String> targets, Integer limit, Integer skip, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'softwareAppId' is set
        if (softwareAppId == null) {
            throw new ApiException("Missing the required parameter 'softwareAppId' when calling softwareappsAssociationsList(Async)");
        }

        // verify the required parameter 'targets' is set
        if (targets == null) {
            throw new ApiException("Missing the required parameter 'targets' when calling softwareappsAssociationsList(Async)");
        }

        return softwareappsAssociationsListCall(softwareAppId, targets, limit, skip, xOrgId, _callback);

    }


    private ApiResponse<List<GraphConnection>> softwareappsAssociationsListWithHttpInfo(String softwareAppId, List<String> targets, Integer limit, Integer skip, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = softwareappsAssociationsListValidateBeforeCall(softwareAppId, targets, limit, skip, xOrgId, null);
        Type localVarReturnType = new TypeToken<List<GraphConnection>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call softwareappsAssociationsListAsync(String softwareAppId, List<String> targets, Integer limit, Integer skip, String xOrgId, final ApiCallback<List<GraphConnection>> _callback) throws ApiException {

        okhttp3.Call localVarCall = softwareappsAssociationsListValidateBeforeCall(softwareAppId, targets, limit, skip, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<List<GraphConnection>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class SoftwareappsAssociationsListRequestBuilder {
        private final String softwareAppId;
        private final List<String> targets;
        private Integer limit;
        private Integer skip;
        private String xOrgId;

        private SoftwareappsAssociationsListRequestBuilder(String softwareAppId, List<String> targets) {
            this.softwareAppId = softwareAppId;
            this.targets = targets;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return SoftwareappsAssociationsListRequestBuilder
         */
        public SoftwareappsAssociationsListRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return SoftwareappsAssociationsListRequestBuilder
         */
        public SoftwareappsAssociationsListRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return SoftwareappsAssociationsListRequestBuilder
         */
        public SoftwareappsAssociationsListRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for softwareappsAssociationsList
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
            return softwareappsAssociationsListCall(softwareAppId, targets, limit, skip, xOrgId, _callback);
        }


        /**
         * Execute softwareappsAssociationsList request
         * @return List&lt;GraphConnection&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphConnection> execute() throws ApiException {
            ApiResponse<List<GraphConnection>> localVarResp = softwareappsAssociationsListWithHttpInfo(softwareAppId, targets, limit, skip, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute softwareappsAssociationsList request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphConnection&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphConnection>> executeWithHttpInfo() throws ApiException {
            return softwareappsAssociationsListWithHttpInfo(softwareAppId, targets, limit, skip, xOrgId);
        }

        /**
         * Execute softwareappsAssociationsList request (asynchronously)
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
            return softwareappsAssociationsListAsync(softwareAppId, targets, limit, skip, xOrgId, _callback);
        }
    }

    /**
     * List the associations of a Software Application
     * This endpoint will return the _direct_ associations of a Software Application. A direct association can be a non-homogeneous relationship between 2 different objects, for example Software Application and System Groups.   #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/softwareapps/{software_app_id}/associations?targets&#x3D;system_group \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param softwareAppId ObjectID of the Software App. (required)
     * @param targets Targets which a \&quot;software_app\&quot; can be associated to. (required)
     * @return SoftwareappsAssociationsListRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public SoftwareappsAssociationsListRequestBuilder softwareappsAssociationsList(String softwareAppId, List<String> targets) throws IllegalArgumentException {
        if (softwareAppId == null) throw new IllegalArgumentException("\"softwareAppId\" is required but got null");
            

        if (targets == null) throw new IllegalArgumentException("\"targets\" is required but got null");
        return new SoftwareappsAssociationsListRequestBuilder(softwareAppId, targets);
    }
    private okhttp3.Call softwareappsAssociationsPostCall(String softwareAppId, String xOrgId, GraphOperationSoftwareApp graphOperationSoftwareApp, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = graphOperationSoftwareApp;

        // create path and map variables
        String localVarPath = "/softwareapps/{software_app_id}/associations"
            .replace("{" + "software_app_id" + "}", localVarApiClient.escapeString(softwareAppId.toString()));

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
    private okhttp3.Call softwareappsAssociationsPostValidateBeforeCall(String softwareAppId, String xOrgId, GraphOperationSoftwareApp graphOperationSoftwareApp, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'softwareAppId' is set
        if (softwareAppId == null) {
            throw new ApiException("Missing the required parameter 'softwareAppId' when calling softwareappsAssociationsPost(Async)");
        }

        return softwareappsAssociationsPostCall(softwareAppId, xOrgId, graphOperationSoftwareApp, _callback);

    }


    private ApiResponse<Void> softwareappsAssociationsPostWithHttpInfo(String softwareAppId, String xOrgId, GraphOperationSoftwareApp graphOperationSoftwareApp) throws ApiException {
        okhttp3.Call localVarCall = softwareappsAssociationsPostValidateBeforeCall(softwareAppId, xOrgId, graphOperationSoftwareApp, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call softwareappsAssociationsPostAsync(String softwareAppId, String xOrgId, GraphOperationSoftwareApp graphOperationSoftwareApp, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = softwareappsAssociationsPostValidateBeforeCall(softwareAppId, xOrgId, graphOperationSoftwareApp, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class SoftwareappsAssociationsPostRequestBuilder {
        private final String softwareAppId;
        private String id;
        private String op;
        private Map<String, Object> attributes;
        private String type;
        private String xOrgId;

        private SoftwareappsAssociationsPostRequestBuilder(String softwareAppId) {
            this.softwareAppId = softwareAppId;
        }

        /**
         * Set id
         * @param id The ObjectID of graph object being added or removed as an association. (optional)
         * @return SoftwareappsAssociationsPostRequestBuilder
         */
        public SoftwareappsAssociationsPostRequestBuilder id(String id) {
            this.id = id;
            return this;
        }
        
        /**
         * Set op
         * @param op How to modify the graph connection. (optional)
         * @return SoftwareappsAssociationsPostRequestBuilder
         */
        public SoftwareappsAssociationsPostRequestBuilder op(String op) {
            this.op = op;
            return this;
        }
        
        /**
         * Set attributes
         * @param attributes The graph attributes. (optional)
         * @return SoftwareappsAssociationsPostRequestBuilder
         */
        public SoftwareappsAssociationsPostRequestBuilder attributes(Map<String, Object> attributes) {
            this.attributes = attributes;
            return this;
        }
        
        /**
         * Set type
         * @param type Targets which a \\\&quot;software_app\\\&quot; can be associated to. (optional)
         * @return SoftwareappsAssociationsPostRequestBuilder
         */
        public SoftwareappsAssociationsPostRequestBuilder type(String type) {
            this.type = type;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return SoftwareappsAssociationsPostRequestBuilder
         */
        public SoftwareappsAssociationsPostRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for softwareappsAssociationsPost
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
            GraphOperationSoftwareApp graphOperationSoftwareApp = buildBodyParams();
            return softwareappsAssociationsPostCall(softwareAppId, xOrgId, graphOperationSoftwareApp, _callback);
        }

        private GraphOperationSoftwareApp buildBodyParams() {
            GraphOperationSoftwareApp graphOperationSoftwareApp = new GraphOperationSoftwareApp();
            return graphOperationSoftwareApp;
        }

        /**
         * Execute softwareappsAssociationsPost request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            GraphOperationSoftwareApp graphOperationSoftwareApp = buildBodyParams();
            softwareappsAssociationsPostWithHttpInfo(softwareAppId, xOrgId, graphOperationSoftwareApp);
        }

        /**
         * Execute softwareappsAssociationsPost request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            GraphOperationSoftwareApp graphOperationSoftwareApp = buildBodyParams();
            return softwareappsAssociationsPostWithHttpInfo(softwareAppId, xOrgId, graphOperationSoftwareApp);
        }

        /**
         * Execute softwareappsAssociationsPost request (asynchronously)
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
            GraphOperationSoftwareApp graphOperationSoftwareApp = buildBodyParams();
            return softwareappsAssociationsPostAsync(softwareAppId, xOrgId, graphOperationSoftwareApp, _callback);
        }
    }

    /**
     * Manage the associations of a software application.
     * This endpoint allows you to associate or disassociate a software application to a system or system group.  #### Sample Request &#x60;&#x60;&#x60; $ curl -X POST https://console.jumpcloud.com/api/v2/softwareapps/{software_app_id}/associations \\ -H &#39;Accept: application/json&#39; \\ -H &#39;Content-Type: application/json&#39; \\ -H &#39;x-api-key: {API_KEY}&#39; \\ -d &#39;{   \&quot;id\&quot;: \&quot;&lt;object_id&gt;\&quot;,   \&quot;op\&quot;: \&quot;add\&quot;,   \&quot;type\&quot;: \&quot;system\&quot;  }&#39; &#x60;&#x60;&#x60;
     * @param softwareAppId ObjectID of the Software App. (required)
     * @return SoftwareappsAssociationsPostRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public SoftwareappsAssociationsPostRequestBuilder softwareappsAssociationsPost(String softwareAppId) throws IllegalArgumentException {
        if (softwareAppId == null) throw new IllegalArgumentException("\"softwareAppId\" is required but got null");
            

        return new SoftwareappsAssociationsPostRequestBuilder(softwareAppId);
    }
    private okhttp3.Call softwareappsTraverseSystemCall(String softwareAppId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/softwareapps/{software_app_id}/systems"
            .replace("{" + "software_app_id" + "}", localVarApiClient.escapeString(softwareAppId.toString()));

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
    private okhttp3.Call softwareappsTraverseSystemValidateBeforeCall(String softwareAppId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'softwareAppId' is set
        if (softwareAppId == null) {
            throw new ApiException("Missing the required parameter 'softwareAppId' when calling softwareappsTraverseSystem(Async)");
        }

        return softwareappsTraverseSystemCall(softwareAppId, limit, xOrgId, skip, filter, _callback);

    }


    private ApiResponse<List<GraphObjectWithPaths>> softwareappsTraverseSystemWithHttpInfo(String softwareAppId, Integer limit, String xOrgId, Integer skip, List<String> filter) throws ApiException {
        okhttp3.Call localVarCall = softwareappsTraverseSystemValidateBeforeCall(softwareAppId, limit, xOrgId, skip, filter, null);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call softwareappsTraverseSystemAsync(String softwareAppId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {

        okhttp3.Call localVarCall = softwareappsTraverseSystemValidateBeforeCall(softwareAppId, limit, xOrgId, skip, filter, _callback);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class SoftwareappsTraverseSystemRequestBuilder {
        private final String softwareAppId;
        private Integer limit;
        private String xOrgId;
        private Integer skip;
        private List<String> filter;

        private SoftwareappsTraverseSystemRequestBuilder(String softwareAppId) {
            this.softwareAppId = softwareAppId;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return SoftwareappsTraverseSystemRequestBuilder
         */
        public SoftwareappsTraverseSystemRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return SoftwareappsTraverseSystemRequestBuilder
         */
        public SoftwareappsTraverseSystemRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return SoftwareappsTraverseSystemRequestBuilder
         */
        public SoftwareappsTraverseSystemRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return SoftwareappsTraverseSystemRequestBuilder
         */
        public SoftwareappsTraverseSystemRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Build call for softwareappsTraverseSystem
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
            return softwareappsTraverseSystemCall(softwareAppId, limit, xOrgId, skip, filter, _callback);
        }


        /**
         * Execute softwareappsTraverseSystem request
         * @return List&lt;GraphObjectWithPaths&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphObjectWithPaths> execute() throws ApiException {
            ApiResponse<List<GraphObjectWithPaths>> localVarResp = softwareappsTraverseSystemWithHttpInfo(softwareAppId, limit, xOrgId, skip, filter);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute softwareappsTraverseSystem request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphObjectWithPaths&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphObjectWithPaths>> executeWithHttpInfo() throws ApiException {
            return softwareappsTraverseSystemWithHttpInfo(softwareAppId, limit, xOrgId, skip, filter);
        }

        /**
         * Execute softwareappsTraverseSystem request (asynchronously)
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
            return softwareappsTraverseSystemAsync(softwareAppId, limit, xOrgId, skip, filter, _callback);
        }
    }

    /**
     * List the Systems bound to a Software App.
     * This endpoint will return all Systems bound to a Software App, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this Software App to the corresponding System; this array represents all grouping and/or associations that would have to be removed to deprovision the System from this Software App.  See &#x60;/associations&#x60; endpoint to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/softwareapps/{software_app_id}/systems \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param softwareAppId ObjectID of the Software App. (required)
     * @return SoftwareappsTraverseSystemRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public SoftwareappsTraverseSystemRequestBuilder softwareappsTraverseSystem(String softwareAppId) throws IllegalArgumentException {
        if (softwareAppId == null) throw new IllegalArgumentException("\"softwareAppId\" is required but got null");
            

        return new SoftwareappsTraverseSystemRequestBuilder(softwareAppId);
    }
    private okhttp3.Call softwareappsTraverseSystemGroupCall(String softwareAppId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/softwareapps/{software_app_id}/systemgroups"
            .replace("{" + "software_app_id" + "}", localVarApiClient.escapeString(softwareAppId.toString()));

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
    private okhttp3.Call softwareappsTraverseSystemGroupValidateBeforeCall(String softwareAppId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'softwareAppId' is set
        if (softwareAppId == null) {
            throw new ApiException("Missing the required parameter 'softwareAppId' when calling softwareappsTraverseSystemGroup(Async)");
        }

        return softwareappsTraverseSystemGroupCall(softwareAppId, limit, xOrgId, skip, filter, _callback);

    }


    private ApiResponse<List<GraphObjectWithPaths>> softwareappsTraverseSystemGroupWithHttpInfo(String softwareAppId, Integer limit, String xOrgId, Integer skip, List<String> filter) throws ApiException {
        okhttp3.Call localVarCall = softwareappsTraverseSystemGroupValidateBeforeCall(softwareAppId, limit, xOrgId, skip, filter, null);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call softwareappsTraverseSystemGroupAsync(String softwareAppId, Integer limit, String xOrgId, Integer skip, List<String> filter, final ApiCallback<List<GraphObjectWithPaths>> _callback) throws ApiException {

        okhttp3.Call localVarCall = softwareappsTraverseSystemGroupValidateBeforeCall(softwareAppId, limit, xOrgId, skip, filter, _callback);
        Type localVarReturnType = new TypeToken<List<GraphObjectWithPaths>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class SoftwareappsTraverseSystemGroupRequestBuilder {
        private final String softwareAppId;
        private Integer limit;
        private String xOrgId;
        private Integer skip;
        private List<String> filter;

        private SoftwareappsTraverseSystemGroupRequestBuilder(String softwareAppId) {
            this.softwareAppId = softwareAppId;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return SoftwareappsTraverseSystemGroupRequestBuilder
         */
        public SoftwareappsTraverseSystemGroupRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return SoftwareappsTraverseSystemGroupRequestBuilder
         */
        public SoftwareappsTraverseSystemGroupRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return SoftwareappsTraverseSystemGroupRequestBuilder
         */
        public SoftwareappsTraverseSystemGroupRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return SoftwareappsTraverseSystemGroupRequestBuilder
         */
        public SoftwareappsTraverseSystemGroupRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Build call for softwareappsTraverseSystemGroup
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
            return softwareappsTraverseSystemGroupCall(softwareAppId, limit, xOrgId, skip, filter, _callback);
        }


        /**
         * Execute softwareappsTraverseSystemGroup request
         * @return List&lt;GraphObjectWithPaths&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<GraphObjectWithPaths> execute() throws ApiException {
            ApiResponse<List<GraphObjectWithPaths>> localVarResp = softwareappsTraverseSystemGroupWithHttpInfo(softwareAppId, limit, xOrgId, skip, filter);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute softwareappsTraverseSystemGroup request with HTTP info returned
         * @return ApiResponse&lt;List&lt;GraphObjectWithPaths&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<GraphObjectWithPaths>> executeWithHttpInfo() throws ApiException {
            return softwareappsTraverseSystemGroupWithHttpInfo(softwareAppId, limit, xOrgId, skip, filter);
        }

        /**
         * Execute softwareappsTraverseSystemGroup request (asynchronously)
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
            return softwareappsTraverseSystemGroupAsync(softwareAppId, limit, xOrgId, skip, filter, _callback);
        }
    }

    /**
     * List the System Groups bound to a Software App.
     * This endpoint will return all Systems Groups bound to a Software App, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the group&#39;s type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this Software App to the corresponding System Group; this array represents all grouping and/or associations that would have to be removed to deprovision the System Group from this Software App.  See &#x60;/associations&#x60; endpoint to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET  https://console.jumpcloud.com/api/v2/softwareapps/{software_app_id}/systemgroups \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param softwareAppId ObjectID of the Software App. (required)
     * @return SoftwareappsTraverseSystemGroupRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public SoftwareappsTraverseSystemGroupRequestBuilder softwareappsTraverseSystemGroup(String softwareAppId) throws IllegalArgumentException {
        if (softwareAppId == null) throw new IllegalArgumentException("\"softwareAppId\" is required but got null");
            

        return new SoftwareappsTraverseSystemGroupRequestBuilder(softwareAppId);
    }
    private okhttp3.Call updateCall(String id, String xOrgId, SoftwareApp softwareApp, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = softwareApp;

        // create path and map variables
        String localVarPath = "/softwareapps/{id}"
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
    private okhttp3.Call updateValidateBeforeCall(String id, String xOrgId, SoftwareApp softwareApp, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling update(Async)");
        }

        return updateCall(id, xOrgId, softwareApp, _callback);

    }


    private ApiResponse<SoftwareApp> updateWithHttpInfo(String id, String xOrgId, SoftwareApp softwareApp) throws ApiException {
        okhttp3.Call localVarCall = updateValidateBeforeCall(id, xOrgId, softwareApp, null);
        Type localVarReturnType = new TypeToken<SoftwareApp>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call updateAsync(String id, String xOrgId, SoftwareApp softwareApp, final ApiCallback<SoftwareApp> _callback) throws ApiException {

        okhttp3.Call localVarCall = updateValidateBeforeCall(id, xOrgId, softwareApp, _callback);
        Type localVarReturnType = new TypeToken<SoftwareApp>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UpdateRequestBuilder {
        private final String id;
        private String displayName;
        private String id;
        private List<SoftwareAppSettings> settings;
        private String xOrgId;

        private UpdateRequestBuilder(String id) {
            this.id = id;
        }

        /**
         * Set displayName
         * @param displayName  (optional)
         * @return UpdateRequestBuilder
         */
        public UpdateRequestBuilder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }
        
        /**
         * Set id
         * @param id  (optional)
         * @return UpdateRequestBuilder
         */
        public UpdateRequestBuilder id(String id) {
            this.id = id;
            return this;
        }
        
        /**
         * Set settings
         * @param settings  (optional)
         * @return UpdateRequestBuilder
         */
        public UpdateRequestBuilder settings(List<SoftwareAppSettings> settings) {
            this.settings = settings;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return UpdateRequestBuilder
         */
        public UpdateRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for update
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
            SoftwareApp softwareApp = buildBodyParams();
            return updateCall(id, xOrgId, softwareApp, _callback);
        }

        private SoftwareApp buildBodyParams() {
            SoftwareApp softwareApp = new SoftwareApp();
            softwareApp.displayName(this.displayName);
            softwareApp.id(this.id);
            softwareApp.settings(this.settings);
            return softwareApp;
        }

        /**
         * Execute update request
         * @return SoftwareApp
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public SoftwareApp execute() throws ApiException {
            SoftwareApp softwareApp = buildBodyParams();
            ApiResponse<SoftwareApp> localVarResp = updateWithHttpInfo(id, xOrgId, softwareApp);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute update request with HTTP info returned
         * @return ApiResponse&lt;SoftwareApp&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<SoftwareApp> executeWithHttpInfo() throws ApiException {
            SoftwareApp softwareApp = buildBodyParams();
            return updateWithHttpInfo(id, xOrgId, softwareApp);
        }

        /**
         * Execute update request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<SoftwareApp> _callback) throws ApiException {
            SoftwareApp softwareApp = buildBodyParams();
            return updateAsync(id, xOrgId, softwareApp, _callback);
        }
    }

    /**
     * Update a Software Application Configuration.
     * This endpoint updates a specific Software Application configuration for the organization. displayName can be changed alone if no settings are provided. If a setting is provided, it should include all its information since this endpoint will update all the settings&#39; fields. The optional isConfigEnabled and appConfiguration apple_vpp attributes are not included in the response.  #### Sample Request - displayName only &#x60;&#x60;&#x60;  curl -X PUT https://console.jumpcloud.com/api/v2/softwareapps/{id} \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;displayName\&quot;: \&quot;My Software App\&quot;   }&#39; &#x60;&#x60;&#x60;  #### Sample Request - all attributes &#x60;&#x60;&#x60;  curl -X PUT https://console.jumpcloud.com/api/v2/softwareapps/{id} \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;displayName\&quot;: \&quot;My Software App\&quot;,     \&quot;settings\&quot;: [       {         \&quot;packageId\&quot;: \&quot;123456\&quot;,         \&quot;autoUpdate\&quot;: false,         \&quot;allowUpdateDelay\&quot;: false,         \&quot;packageManager\&quot;: \&quot;APPLE_VPP\&quot;,         \&quot;locationObjectId\&quot;: \&quot;123456789012123456789012\&quot;,         \&quot;location\&quot;: \&quot;123456\&quot;,         \&quot;desiredState\&quot;: \&quot;Install\&quot;,         \&quot;appleVpp\&quot;: {           \&quot;appConfiguration\&quot;: \&quot;&lt;?xml version&#x3D;\\\&quot;1.0\\\&quot; encoding&#x3D;\\\&quot;UTF-8\\\&quot;?&gt;&lt;!DOCTYPE plist PUBLIC \\\&quot;-//Apple//DTD PLIST 1.0//EN\\\&quot; \\\&quot;http://www.apple.com/DTDs/PropertyList-1.0.dtd\\\&quot;&gt;&lt;plist version&#x3D;\\\&quot;1.0\\\&quot;&gt;&lt;dict&gt;&lt;key&gt;MyKey&lt;/key&gt;&lt;string&gt;My String&lt;/string&gt;&lt;/dict&gt;&lt;/plist&gt;\&quot;,           \&quot;assignedLicenses\&quot;: 20,           \&quot;availableLicenses\&quot;: 10,           \&quot;details\&quot;: {},           \&quot;isConfigEnabled\&quot;: true,           \&quot;supportedDeviceFamilies\&quot;: [             \&quot;IPAD\&quot;,             \&quot;MAC\&quot;           ],           \&quot;totalLicenses\&quot;: 30         },         \&quot;packageSubtitle\&quot;: \&quot;My package subtitle\&quot;,         \&quot;packageVersion\&quot;: \&quot;1.2.3\&quot;,         \&quot;packageKind\&quot;: \&quot;software-package\&quot;,         \&quot;assetKind\&quot;: \&quot;software\&quot;,         \&quot;assetSha256Size\&quot;: 256,         \&quot;assetSha256Strings\&quot;: [           \&quot;a123b123c123d123\&quot;         ],         \&quot;description\&quot;: \&quot;My app description\&quot;       }     ]   }&#39; &#x60;&#x60;&#x60;
     * @param id  (required)
     * @return UpdateRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UpdateRequestBuilder update(String id) throws IllegalArgumentException {
        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new UpdateRequestBuilder(id);
    }
    private okhttp3.Call validateApplicationInstallPackageCall(JumpcloudPackageValidatorValidateApplicationInstallPackageRequest jumpcloudPackageValidatorValidateApplicationInstallPackageRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = jumpcloudPackageValidatorValidateApplicationInstallPackageRequest;

        // create path and map variables
        String localVarPath = "/softwareapps/validate";

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
    private okhttp3.Call validateApplicationInstallPackageValidateBeforeCall(JumpcloudPackageValidatorValidateApplicationInstallPackageRequest jumpcloudPackageValidatorValidateApplicationInstallPackageRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'jumpcloudPackageValidatorValidateApplicationInstallPackageRequest' is set
        if (jumpcloudPackageValidatorValidateApplicationInstallPackageRequest == null) {
            throw new ApiException("Missing the required parameter 'jumpcloudPackageValidatorValidateApplicationInstallPackageRequest' when calling validateApplicationInstallPackage(Async)");
        }

        return validateApplicationInstallPackageCall(jumpcloudPackageValidatorValidateApplicationInstallPackageRequest, _callback);

    }


    private ApiResponse<JumpcloudPackageValidatorValidateApplicationInstallPackageResponse> validateApplicationInstallPackageWithHttpInfo(JumpcloudPackageValidatorValidateApplicationInstallPackageRequest jumpcloudPackageValidatorValidateApplicationInstallPackageRequest) throws ApiException {
        okhttp3.Call localVarCall = validateApplicationInstallPackageValidateBeforeCall(jumpcloudPackageValidatorValidateApplicationInstallPackageRequest, null);
        Type localVarReturnType = new TypeToken<JumpcloudPackageValidatorValidateApplicationInstallPackageResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call validateApplicationInstallPackageAsync(JumpcloudPackageValidatorValidateApplicationInstallPackageRequest jumpcloudPackageValidatorValidateApplicationInstallPackageRequest, final ApiCallback<JumpcloudPackageValidatorValidateApplicationInstallPackageResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = validateApplicationInstallPackageValidateBeforeCall(jumpcloudPackageValidatorValidateApplicationInstallPackageRequest, _callback);
        Type localVarReturnType = new TypeToken<JumpcloudPackageValidatorValidateApplicationInstallPackageResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ValidateApplicationInstallPackageRequestBuilder {
        private String url;

        private ValidateApplicationInstallPackageRequestBuilder() {
        }

        /**
         * Set url
         * @param url  (optional)
         * @return ValidateApplicationInstallPackageRequestBuilder
         */
        public ValidateApplicationInstallPackageRequestBuilder url(String url) {
            this.url = url;
            return this;
        }
        
        /**
         * Build call for validateApplicationInstallPackage
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
            JumpcloudPackageValidatorValidateApplicationInstallPackageRequest jumpcloudPackageValidatorValidateApplicationInstallPackageRequest = buildBodyParams();
            return validateApplicationInstallPackageCall(jumpcloudPackageValidatorValidateApplicationInstallPackageRequest, _callback);
        }

        private JumpcloudPackageValidatorValidateApplicationInstallPackageRequest buildBodyParams() {
            JumpcloudPackageValidatorValidateApplicationInstallPackageRequest jumpcloudPackageValidatorValidateApplicationInstallPackageRequest = new JumpcloudPackageValidatorValidateApplicationInstallPackageRequest();
            jumpcloudPackageValidatorValidateApplicationInstallPackageRequest.url(this.url);
            return jumpcloudPackageValidatorValidateApplicationInstallPackageRequest;
        }

        /**
         * Execute validateApplicationInstallPackage request
         * @return JumpcloudPackageValidatorValidateApplicationInstallPackageResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public JumpcloudPackageValidatorValidateApplicationInstallPackageResponse execute() throws ApiException {
            JumpcloudPackageValidatorValidateApplicationInstallPackageRequest jumpcloudPackageValidatorValidateApplicationInstallPackageRequest = buildBodyParams();
            ApiResponse<JumpcloudPackageValidatorValidateApplicationInstallPackageResponse> localVarResp = validateApplicationInstallPackageWithHttpInfo(jumpcloudPackageValidatorValidateApplicationInstallPackageRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute validateApplicationInstallPackage request with HTTP info returned
         * @return ApiResponse&lt;JumpcloudPackageValidatorValidateApplicationInstallPackageResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<JumpcloudPackageValidatorValidateApplicationInstallPackageResponse> executeWithHttpInfo() throws ApiException {
            JumpcloudPackageValidatorValidateApplicationInstallPackageRequest jumpcloudPackageValidatorValidateApplicationInstallPackageRequest = buildBodyParams();
            return validateApplicationInstallPackageWithHttpInfo(jumpcloudPackageValidatorValidateApplicationInstallPackageRequest);
        }

        /**
         * Execute validateApplicationInstallPackage request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<JumpcloudPackageValidatorValidateApplicationInstallPackageResponse> _callback) throws ApiException {
            JumpcloudPackageValidatorValidateApplicationInstallPackageRequest jumpcloudPackageValidatorValidateApplicationInstallPackageRequest = buildBodyParams();
            return validateApplicationInstallPackageAsync(jumpcloudPackageValidatorValidateApplicationInstallPackageRequest, _callback);
        }
    }

    /**
     * Validate Installation Packages
     * Validates an application install package from the specified URL to calculate the SHA256 hash and extract the installer manifest details. #### Sample Request &#x60;&#x60;&#x60; curl -H &#39;x-api-key: {API_KEY}&#39; \\ -H &#39;Content-Type: application/json&#39; \\ -H &#39;Accept: application/json&#39; \\ -d &#39;{\&quot;url\&quot;: \&quot;https://dl.google.com/dl/chrome/mac/universal/stable/gcem/GoogleChrome.pkg\&quot;}&#39; \\ -i -X POST https://console.jumpcloud.com/api/v2/softwareapps/validate &#x60;&#x60;&#x60;
     * @param jumpcloudPackageValidatorValidateApplicationInstallPackageRequest  (required)
     * @return ValidateApplicationInstallPackageRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public ValidateApplicationInstallPackageRequestBuilder validateApplicationInstallPackage() throws IllegalArgumentException {
        return new ValidateApplicationInstallPackageRequestBuilder();
    }
}
