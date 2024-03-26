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


import com.konfigthis.client.model.ADES;
import com.konfigthis.client.model.AppleMDM;
import com.konfigthis.client.model.AppleMdmDevice;
import com.konfigthis.client.model.AppleMdmPatch;
import com.konfigthis.client.model.ApplemdmsDeviceseraseRequest;
import com.konfigthis.client.model.ApplemdmsDeviceslockRequest;
import com.konfigthis.client.model.ApplemdmsDevicesrestartRequest;
import com.konfigthis.client.model.DEP;
import com.konfigthis.client.model.InstallActionType;
import com.konfigthis.client.model.ScheduleOSUpdate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;

public class AppleMdmApiGenerated {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public AppleMdmApiGenerated() throws IllegalArgumentException {
        this(Configuration.getDefaultApiClient());
    }

    public AppleMdmApiGenerated(ApiClient apiClient) throws IllegalArgumentException {
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

    private okhttp3.Call csrgetCall(String appleMdmId, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/applemdms/{apple_mdm_id}/csr"
            .replace("{" + "apple_mdm_id" + "}", localVarApiClient.escapeString(appleMdmId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (xOrgId != null) {
            localVarHeaderParams.put("x-org-id", localVarApiClient.parameterToString(xOrgId));
        }

        final String[] localVarAccepts = {
            "application/octet-stream",
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
    private okhttp3.Call csrgetValidateBeforeCall(String appleMdmId, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appleMdmId' is set
        if (appleMdmId == null) {
            throw new ApiException("Missing the required parameter 'appleMdmId' when calling csrget(Async)");
        }

        return csrgetCall(appleMdmId, xOrgId, _callback);

    }


    private ApiResponse<String> csrgetWithHttpInfo(String appleMdmId, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = csrgetValidateBeforeCall(appleMdmId, xOrgId, null);
        Type localVarReturnType = new TypeToken<String>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call csrgetAsync(String appleMdmId, String xOrgId, final ApiCallback<String> _callback) throws ApiException {

        okhttp3.Call localVarCall = csrgetValidateBeforeCall(appleMdmId, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<String>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class CsrgetRequestBuilder {
        private final String appleMdmId;
        private String xOrgId;

        private CsrgetRequestBuilder(String appleMdmId) {
            this.appleMdmId = appleMdmId;
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return CsrgetRequestBuilder
         */
        public CsrgetRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for csrget
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
            return csrgetCall(appleMdmId, xOrgId, _callback);
        }


        /**
         * Execute csrget request
         * @return String
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public String execute() throws ApiException {
            ApiResponse<String> localVarResp = csrgetWithHttpInfo(appleMdmId, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute csrget request with HTTP info returned
         * @return ApiResponse&lt;String&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<String> executeWithHttpInfo() throws ApiException {
            return csrgetWithHttpInfo(appleMdmId, xOrgId);
        }

        /**
         * Execute csrget request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<String> _callback) throws ApiException {
            return csrgetAsync(appleMdmId, xOrgId, _callback);
        }
    }

    /**
     * Get Apple MDM CSR Plist
     * Retrieves an Apple MDM signed CSR Plist for an organization.  The user must supply the returned plist to Apple for signing, and then provide the certificate provided by Apple back into the PUT API.  #### Sample Request &#x60;&#x60;&#x60;   curl -X GET https://console.jumpcloud.com/api/v2/applemdms/{APPLE_MDM_ID}/csr \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param appleMdmId  (required)
     * @return CsrgetRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public CsrgetRequestBuilder csrget(String appleMdmId) throws IllegalArgumentException {
        if (appleMdmId == null) throw new IllegalArgumentException("\"appleMdmId\" is required but got null");
            

        return new CsrgetRequestBuilder(appleMdmId);
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
        String localVarPath = "/applemdms/{id}"
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


    private ApiResponse<AppleMDM> deleteWithHttpInfo(String id, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = deleteValidateBeforeCall(id, xOrgId, null);
        Type localVarReturnType = new TypeToken<AppleMDM>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call deleteAsync(String id, String xOrgId, final ApiCallback<AppleMDM> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteValidateBeforeCall(id, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<AppleMDM>(){}.getType();
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
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return deleteCall(id, xOrgId, _callback);
        }


        /**
         * Execute delete request
         * @return AppleMDM
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public AppleMDM execute() throws ApiException {
            ApiResponse<AppleMDM> localVarResp = deleteWithHttpInfo(id, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute delete request with HTTP info returned
         * @return ApiResponse&lt;AppleMDM&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<AppleMDM> executeWithHttpInfo() throws ApiException {
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
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AppleMDM> _callback) throws ApiException {
            return deleteAsync(id, xOrgId, _callback);
        }
    }

    /**
     * Delete an Apple MDM
     * Removes an Apple MDM configuration.  Warning: This is a destructive operation and will remove your Apple Push Certificates.  We will no longer be able to manage your devices and the only recovery option is to re-register all devices into MDM.  #### Sample Request &#x60;&#x60;&#x60; curl -X DELETE https://console.jumpcloud.com/api/v2/applemdms/{id} \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param id  (required)
     * @return DeleteRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public DeleteRequestBuilder delete(String id) throws IllegalArgumentException {
        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new DeleteRequestBuilder(id);
    }
    private okhttp3.Call deletedeviceCall(String appleMdmId, String deviceId, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/applemdms/{apple_mdm_id}/devices/{device_id}"
            .replace("{" + "apple_mdm_id" + "}", localVarApiClient.escapeString(appleMdmId.toString()))
            .replace("{" + "device_id" + "}", localVarApiClient.escapeString(deviceId.toString()));

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
    private okhttp3.Call deletedeviceValidateBeforeCall(String appleMdmId, String deviceId, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appleMdmId' is set
        if (appleMdmId == null) {
            throw new ApiException("Missing the required parameter 'appleMdmId' when calling deletedevice(Async)");
        }

        // verify the required parameter 'deviceId' is set
        if (deviceId == null) {
            throw new ApiException("Missing the required parameter 'deviceId' when calling deletedevice(Async)");
        }

        return deletedeviceCall(appleMdmId, deviceId, xOrgId, _callback);

    }


    private ApiResponse<AppleMdmDevice> deletedeviceWithHttpInfo(String appleMdmId, String deviceId, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = deletedeviceValidateBeforeCall(appleMdmId, deviceId, xOrgId, null);
        Type localVarReturnType = new TypeToken<AppleMdmDevice>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call deletedeviceAsync(String appleMdmId, String deviceId, String xOrgId, final ApiCallback<AppleMdmDevice> _callback) throws ApiException {

        okhttp3.Call localVarCall = deletedeviceValidateBeforeCall(appleMdmId, deviceId, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<AppleMdmDevice>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class DeletedeviceRequestBuilder {
        private final String appleMdmId;
        private final String deviceId;
        private String xOrgId;

        private DeletedeviceRequestBuilder(String appleMdmId, String deviceId) {
            this.appleMdmId = appleMdmId;
            this.deviceId = deviceId;
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return DeletedeviceRequestBuilder
         */
        public DeletedeviceRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for deletedevice
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
            return deletedeviceCall(appleMdmId, deviceId, xOrgId, _callback);
        }


        /**
         * Execute deletedevice request
         * @return AppleMdmDevice
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public AppleMdmDevice execute() throws ApiException {
            ApiResponse<AppleMdmDevice> localVarResp = deletedeviceWithHttpInfo(appleMdmId, deviceId, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute deletedevice request with HTTP info returned
         * @return ApiResponse&lt;AppleMdmDevice&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<AppleMdmDevice> executeWithHttpInfo() throws ApiException {
            return deletedeviceWithHttpInfo(appleMdmId, deviceId, xOrgId);
        }

        /**
         * Execute deletedevice request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AppleMdmDevice> _callback) throws ApiException {
            return deletedeviceAsync(appleMdmId, deviceId, xOrgId, _callback);
        }
    }

    /**
     * Remove an Apple MDM Device&#39;s Enrollment
     * Remove a single Apple MDM device from MDM enrollment.  #### Sample Request &#x60;&#x60;&#x60;   curl -X DELETE https://console.jumpcloud.com/api/v2/applemdms/{apple_mdm_id}/devices/{device_id} \\   -H &#39;accept: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param appleMdmId  (required)
     * @param deviceId  (required)
     * @return DeletedeviceRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public DeletedeviceRequestBuilder deletedevice(String appleMdmId, String deviceId) throws IllegalArgumentException {
        if (appleMdmId == null) throw new IllegalArgumentException("\"appleMdmId\" is required but got null");
            

        if (deviceId == null) throw new IllegalArgumentException("\"deviceId\" is required but got null");
            

        return new DeletedeviceRequestBuilder(appleMdmId, deviceId);
    }
    private okhttp3.Call depkeygetCall(String appleMdmId, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/applemdms/{apple_mdm_id}/depkey"
            .replace("{" + "apple_mdm_id" + "}", localVarApiClient.escapeString(appleMdmId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (xOrgId != null) {
            localVarHeaderParams.put("x-org-id", localVarApiClient.parameterToString(xOrgId));
        }

        final String[] localVarAccepts = {
            "application/x-pem-file",
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
    private okhttp3.Call depkeygetValidateBeforeCall(String appleMdmId, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appleMdmId' is set
        if (appleMdmId == null) {
            throw new ApiException("Missing the required parameter 'appleMdmId' when calling depkeyget(Async)");
        }

        return depkeygetCall(appleMdmId, xOrgId, _callback);

    }


    private ApiResponse<String> depkeygetWithHttpInfo(String appleMdmId, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = depkeygetValidateBeforeCall(appleMdmId, xOrgId, null);
        Type localVarReturnType = new TypeToken<String>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call depkeygetAsync(String appleMdmId, String xOrgId, final ApiCallback<String> _callback) throws ApiException {

        okhttp3.Call localVarCall = depkeygetValidateBeforeCall(appleMdmId, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<String>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class DepkeygetRequestBuilder {
        private final String appleMdmId;
        private String xOrgId;

        private DepkeygetRequestBuilder(String appleMdmId) {
            this.appleMdmId = appleMdmId;
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return DepkeygetRequestBuilder
         */
        public DepkeygetRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for depkeyget
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
            return depkeygetCall(appleMdmId, xOrgId, _callback);
        }


        /**
         * Execute depkeyget request
         * @return String
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public String execute() throws ApiException {
            ApiResponse<String> localVarResp = depkeygetWithHttpInfo(appleMdmId, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute depkeyget request with HTTP info returned
         * @return ApiResponse&lt;String&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<String> executeWithHttpInfo() throws ApiException {
            return depkeygetWithHttpInfo(appleMdmId, xOrgId);
        }

        /**
         * Execute depkeyget request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<String> _callback) throws ApiException {
            return depkeygetAsync(appleMdmId, xOrgId, _callback);
        }
    }

    /**
     * Get Apple MDM DEP Public Key
     * Retrieves an Apple MDM DEP Public Key.  #### Sample Request  &#x60;&#x60;&#x60; curl https://console.jumpcloud.com/api/v2/applemdms/{APPLE_MDM_ID}/depkey \\   -H &#39;accept: application/x-pem-file&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param appleMdmId  (required)
     * @return DepkeygetRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public DepkeygetRequestBuilder depkeyget(String appleMdmId) throws IllegalArgumentException {
        if (appleMdmId == null) throw new IllegalArgumentException("\"appleMdmId\" is required but got null");
            

        return new DepkeygetRequestBuilder(appleMdmId);
    }
    private okhttp3.Call devicesClearActivationLockCall(String appleMdmId, String deviceId, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/applemdms/{apple_mdm_id}/devices/{device_id}/clearActivationLock"
            .replace("{" + "apple_mdm_id" + "}", localVarApiClient.escapeString(appleMdmId.toString()))
            .replace("{" + "device_id" + "}", localVarApiClient.escapeString(deviceId.toString()));

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
    private okhttp3.Call devicesClearActivationLockValidateBeforeCall(String appleMdmId, String deviceId, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appleMdmId' is set
        if (appleMdmId == null) {
            throw new ApiException("Missing the required parameter 'appleMdmId' when calling devicesClearActivationLock(Async)");
        }

        // verify the required parameter 'deviceId' is set
        if (deviceId == null) {
            throw new ApiException("Missing the required parameter 'deviceId' when calling devicesClearActivationLock(Async)");
        }

        return devicesClearActivationLockCall(appleMdmId, deviceId, xOrgId, _callback);

    }


    private ApiResponse<Void> devicesClearActivationLockWithHttpInfo(String appleMdmId, String deviceId, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = devicesClearActivationLockValidateBeforeCall(appleMdmId, deviceId, xOrgId, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call devicesClearActivationLockAsync(String appleMdmId, String deviceId, String xOrgId, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = devicesClearActivationLockValidateBeforeCall(appleMdmId, deviceId, xOrgId, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DevicesClearActivationLockRequestBuilder {
        private final String appleMdmId;
        private final String deviceId;
        private String xOrgId;

        private DevicesClearActivationLockRequestBuilder(String appleMdmId, String deviceId) {
            this.appleMdmId = appleMdmId;
            this.deviceId = deviceId;
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return DevicesClearActivationLockRequestBuilder
         */
        public DevicesClearActivationLockRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for devicesClearActivationLock
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
            return devicesClearActivationLockCall(appleMdmId, deviceId, xOrgId, _callback);
        }


        /**
         * Execute devicesClearActivationLock request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            devicesClearActivationLockWithHttpInfo(appleMdmId, deviceId, xOrgId);
        }

        /**
         * Execute devicesClearActivationLock request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            return devicesClearActivationLockWithHttpInfo(appleMdmId, deviceId, xOrgId);
        }

        /**
         * Execute devicesClearActivationLock request (asynchronously)
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
            return devicesClearActivationLockAsync(appleMdmId, deviceId, xOrgId, _callback);
        }
    }

    /**
     * Clears the Activation Lock for a Device
     * Clears the activation lock on the specified device.  #### Sample Request &#x60;&#x60;&#x60;   curl -X POST https://console.jumpcloud.com/api/v2/applemdms/{apple_mdm_id}/devices/{device_id}/clearActivationLock \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{}&#39; &#x60;&#x60;&#x60;
     * @param appleMdmId  (required)
     * @param deviceId  (required)
     * @return DevicesClearActivationLockRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public DevicesClearActivationLockRequestBuilder devicesClearActivationLock(String appleMdmId, String deviceId) throws IllegalArgumentException {
        if (appleMdmId == null) throw new IllegalArgumentException("\"appleMdmId\" is required but got null");
            

        if (deviceId == null) throw new IllegalArgumentException("\"deviceId\" is required but got null");
            

        return new DevicesClearActivationLockRequestBuilder(appleMdmId, deviceId);
    }
    private okhttp3.Call devicesOSUpdateStatusCall(String appleMdmId, String deviceId, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/applemdms/{apple_mdm_id}/devices/{device_id}/osUpdateStatus"
            .replace("{" + "apple_mdm_id" + "}", localVarApiClient.escapeString(appleMdmId.toString()))
            .replace("{" + "device_id" + "}", localVarApiClient.escapeString(deviceId.toString()));

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
    private okhttp3.Call devicesOSUpdateStatusValidateBeforeCall(String appleMdmId, String deviceId, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appleMdmId' is set
        if (appleMdmId == null) {
            throw new ApiException("Missing the required parameter 'appleMdmId' when calling devicesOSUpdateStatus(Async)");
        }

        // verify the required parameter 'deviceId' is set
        if (deviceId == null) {
            throw new ApiException("Missing the required parameter 'deviceId' when calling devicesOSUpdateStatus(Async)");
        }

        return devicesOSUpdateStatusCall(appleMdmId, deviceId, xOrgId, _callback);

    }


    private ApiResponse<Void> devicesOSUpdateStatusWithHttpInfo(String appleMdmId, String deviceId, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = devicesOSUpdateStatusValidateBeforeCall(appleMdmId, deviceId, xOrgId, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call devicesOSUpdateStatusAsync(String appleMdmId, String deviceId, String xOrgId, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = devicesOSUpdateStatusValidateBeforeCall(appleMdmId, deviceId, xOrgId, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DevicesOSUpdateStatusRequestBuilder {
        private final String appleMdmId;
        private final String deviceId;
        private String xOrgId;

        private DevicesOSUpdateStatusRequestBuilder(String appleMdmId, String deviceId) {
            this.appleMdmId = appleMdmId;
            this.deviceId = deviceId;
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return DevicesOSUpdateStatusRequestBuilder
         */
        public DevicesOSUpdateStatusRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for devicesOSUpdateStatus
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
            return devicesOSUpdateStatusCall(appleMdmId, deviceId, xOrgId, _callback);
        }


        /**
         * Execute devicesOSUpdateStatus request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            devicesOSUpdateStatusWithHttpInfo(appleMdmId, deviceId, xOrgId);
        }

        /**
         * Execute devicesOSUpdateStatus request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            return devicesOSUpdateStatusWithHttpInfo(appleMdmId, deviceId, xOrgId);
        }

        /**
         * Execute devicesOSUpdateStatus request (asynchronously)
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
            return devicesOSUpdateStatusAsync(appleMdmId, deviceId, xOrgId, _callback);
        }
    }

    /**
     * Request the status of an OS update for a device
     * Pass through to request the status of an OS update #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/applemdms/{apple_mdm_id}/devices/{device_id}/osUpdateStatus \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{}&#39; &#x60;&#x60;&#x60;
     * @param appleMdmId  (required)
     * @param deviceId  (required)
     * @return DevicesOSUpdateStatusRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public DevicesOSUpdateStatusRequestBuilder devicesOSUpdateStatus(String appleMdmId, String deviceId) throws IllegalArgumentException {
        if (appleMdmId == null) throw new IllegalArgumentException("\"appleMdmId\" is required but got null");
            

        if (deviceId == null) throw new IllegalArgumentException("\"deviceId\" is required but got null");
            

        return new DevicesOSUpdateStatusRequestBuilder(appleMdmId, deviceId);
    }
    private okhttp3.Call devicesRefreshActivationLockInformationCall(String appleMdmId, String deviceId, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/applemdms/{apple_mdm_id}/devices/{device_id}/refreshActivationLockInformation"
            .replace("{" + "apple_mdm_id" + "}", localVarApiClient.escapeString(appleMdmId.toString()))
            .replace("{" + "device_id" + "}", localVarApiClient.escapeString(deviceId.toString()));

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
    private okhttp3.Call devicesRefreshActivationLockInformationValidateBeforeCall(String appleMdmId, String deviceId, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appleMdmId' is set
        if (appleMdmId == null) {
            throw new ApiException("Missing the required parameter 'appleMdmId' when calling devicesRefreshActivationLockInformation(Async)");
        }

        // verify the required parameter 'deviceId' is set
        if (deviceId == null) {
            throw new ApiException("Missing the required parameter 'deviceId' when calling devicesRefreshActivationLockInformation(Async)");
        }

        return devicesRefreshActivationLockInformationCall(appleMdmId, deviceId, xOrgId, _callback);

    }


    private ApiResponse<Void> devicesRefreshActivationLockInformationWithHttpInfo(String appleMdmId, String deviceId, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = devicesRefreshActivationLockInformationValidateBeforeCall(appleMdmId, deviceId, xOrgId, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call devicesRefreshActivationLockInformationAsync(String appleMdmId, String deviceId, String xOrgId, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = devicesRefreshActivationLockInformationValidateBeforeCall(appleMdmId, deviceId, xOrgId, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DevicesRefreshActivationLockInformationRequestBuilder {
        private final String appleMdmId;
        private final String deviceId;
        private String xOrgId;

        private DevicesRefreshActivationLockInformationRequestBuilder(String appleMdmId, String deviceId) {
            this.appleMdmId = appleMdmId;
            this.deviceId = deviceId;
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return DevicesRefreshActivationLockInformationRequestBuilder
         */
        public DevicesRefreshActivationLockInformationRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for devicesRefreshActivationLockInformation
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
            return devicesRefreshActivationLockInformationCall(appleMdmId, deviceId, xOrgId, _callback);
        }


        /**
         * Execute devicesRefreshActivationLockInformation request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            devicesRefreshActivationLockInformationWithHttpInfo(appleMdmId, deviceId, xOrgId);
        }

        /**
         * Execute devicesRefreshActivationLockInformation request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            return devicesRefreshActivationLockInformationWithHttpInfo(appleMdmId, deviceId, xOrgId);
        }

        /**
         * Execute devicesRefreshActivationLockInformation request (asynchronously)
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
            return devicesRefreshActivationLockInformationAsync(appleMdmId, deviceId, xOrgId, _callback);
        }
    }

    /**
     * Refresh activation lock information for a device
     * Refreshes the activation lock information for a device  #### Sample Request  &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/applemdms/{apple_mdm_id}/devices/{device_id}/refreshActivationLockInformation \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{}&#39; &#x60;&#x60;&#x60;
     * @param appleMdmId  (required)
     * @param deviceId  (required)
     * @return DevicesRefreshActivationLockInformationRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public DevicesRefreshActivationLockInformationRequestBuilder devicesRefreshActivationLockInformation(String appleMdmId, String deviceId) throws IllegalArgumentException {
        if (appleMdmId == null) throw new IllegalArgumentException("\"appleMdmId\" is required but got null");
            

        if (deviceId == null) throw new IllegalArgumentException("\"deviceId\" is required but got null");
            

        return new DevicesRefreshActivationLockInformationRequestBuilder(appleMdmId, deviceId);
    }
    private okhttp3.Call devicesScheduleOSUpdateCall(String appleMdmId, String deviceId, String xOrgId, ScheduleOSUpdate scheduleOSUpdate, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = scheduleOSUpdate;

        // create path and map variables
        String localVarPath = "/applemdms/{apple_mdm_id}/devices/{device_id}/scheduleOSUpdate"
            .replace("{" + "apple_mdm_id" + "}", localVarApiClient.escapeString(appleMdmId.toString()))
            .replace("{" + "device_id" + "}", localVarApiClient.escapeString(deviceId.toString()));

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
    private okhttp3.Call devicesScheduleOSUpdateValidateBeforeCall(String appleMdmId, String deviceId, String xOrgId, ScheduleOSUpdate scheduleOSUpdate, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appleMdmId' is set
        if (appleMdmId == null) {
            throw new ApiException("Missing the required parameter 'appleMdmId' when calling devicesScheduleOSUpdate(Async)");
        }

        // verify the required parameter 'deviceId' is set
        if (deviceId == null) {
            throw new ApiException("Missing the required parameter 'deviceId' when calling devicesScheduleOSUpdate(Async)");
        }

        return devicesScheduleOSUpdateCall(appleMdmId, deviceId, xOrgId, scheduleOSUpdate, _callback);

    }


    private ApiResponse<Void> devicesScheduleOSUpdateWithHttpInfo(String appleMdmId, String deviceId, String xOrgId, ScheduleOSUpdate scheduleOSUpdate) throws ApiException {
        okhttp3.Call localVarCall = devicesScheduleOSUpdateValidateBeforeCall(appleMdmId, deviceId, xOrgId, scheduleOSUpdate, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call devicesScheduleOSUpdateAsync(String appleMdmId, String deviceId, String xOrgId, ScheduleOSUpdate scheduleOSUpdate, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = devicesScheduleOSUpdateValidateBeforeCall(appleMdmId, deviceId, xOrgId, scheduleOSUpdate, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DevicesScheduleOSUpdateRequestBuilder {
        private final InstallActionType installAction;
        private final String productKey;
        private final String appleMdmId;
        private final String deviceId;
        private Integer maxUserDeferrals;
        private String xOrgId;

        private DevicesScheduleOSUpdateRequestBuilder(InstallActionType installAction, String productKey, String appleMdmId, String deviceId) {
            this.installAction = installAction;
            this.productKey = productKey;
            this.appleMdmId = appleMdmId;
            this.deviceId = deviceId;
        }

        /**
         * Set maxUserDeferrals
         * @param maxUserDeferrals  (optional)
         * @return DevicesScheduleOSUpdateRequestBuilder
         */
        public DevicesScheduleOSUpdateRequestBuilder maxUserDeferrals(Integer maxUserDeferrals) {
            this.maxUserDeferrals = maxUserDeferrals;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return DevicesScheduleOSUpdateRequestBuilder
         */
        public DevicesScheduleOSUpdateRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for devicesScheduleOSUpdate
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
            ScheduleOSUpdate scheduleOSUpdate = buildBodyParams();
            return devicesScheduleOSUpdateCall(appleMdmId, deviceId, xOrgId, scheduleOSUpdate, _callback);
        }

        private ScheduleOSUpdate buildBodyParams() {
            ScheduleOSUpdate scheduleOSUpdate = new ScheduleOSUpdate();
            scheduleOSUpdate.installAction(this.installAction);
            scheduleOSUpdate.maxUserDeferrals(this.maxUserDeferrals);
            scheduleOSUpdate.productKey(this.productKey);
            return scheduleOSUpdate;
        }

        /**
         * Execute devicesScheduleOSUpdate request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            ScheduleOSUpdate scheduleOSUpdate = buildBodyParams();
            devicesScheduleOSUpdateWithHttpInfo(appleMdmId, deviceId, xOrgId, scheduleOSUpdate);
        }

        /**
         * Execute devicesScheduleOSUpdate request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            ScheduleOSUpdate scheduleOSUpdate = buildBodyParams();
            return devicesScheduleOSUpdateWithHttpInfo(appleMdmId, deviceId, xOrgId, scheduleOSUpdate);
        }

        /**
         * Execute devicesScheduleOSUpdate request (asynchronously)
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
            ScheduleOSUpdate scheduleOSUpdate = buildBodyParams();
            return devicesScheduleOSUpdateAsync(appleMdmId, deviceId, xOrgId, scheduleOSUpdate, _callback);
        }
    }

    /**
     * Schedule an OS update for a device
     * Schedules an OS update for a device  #### Sample Request  &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/applemdms/{apple_mdm_id}/devices/{device_id}/scheduleOSUpdate \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{\&quot;install_action\&quot;: \&quot;INSTALL_ASAP\&quot;, \&quot;product_key\&quot;: \&quot;key\&quot;}&#39; &#x60;&#x60;&#x60;
     * @param appleMdmId  (required)
     * @param deviceId  (required)
     * @return DevicesScheduleOSUpdateRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public DevicesScheduleOSUpdateRequestBuilder devicesScheduleOSUpdate(InstallActionType installAction, String productKey, String appleMdmId, String deviceId) throws IllegalArgumentException {
        if (installAction == null) throw new IllegalArgumentException("\"installAction\" is required but got null");
        if (productKey == null) throw new IllegalArgumentException("\"productKey\" is required but got null");
            

        if (appleMdmId == null) throw new IllegalArgumentException("\"appleMdmId\" is required but got null");
            

        if (deviceId == null) throw new IllegalArgumentException("\"deviceId\" is required but got null");
            

        return new DevicesScheduleOSUpdateRequestBuilder(installAction, productKey, appleMdmId, deviceId);
    }
    private okhttp3.Call deviceseraseCall(String appleMdmId, String deviceId, String xOrgId, ApplemdmsDeviceseraseRequest applemdmsDeviceseraseRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = applemdmsDeviceseraseRequest;

        // create path and map variables
        String localVarPath = "/applemdms/{apple_mdm_id}/devices/{device_id}/erase"
            .replace("{" + "apple_mdm_id" + "}", localVarApiClient.escapeString(appleMdmId.toString()))
            .replace("{" + "device_id" + "}", localVarApiClient.escapeString(deviceId.toString()));

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
    private okhttp3.Call deviceseraseValidateBeforeCall(String appleMdmId, String deviceId, String xOrgId, ApplemdmsDeviceseraseRequest applemdmsDeviceseraseRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appleMdmId' is set
        if (appleMdmId == null) {
            throw new ApiException("Missing the required parameter 'appleMdmId' when calling deviceserase(Async)");
        }

        // verify the required parameter 'deviceId' is set
        if (deviceId == null) {
            throw new ApiException("Missing the required parameter 'deviceId' when calling deviceserase(Async)");
        }

        return deviceseraseCall(appleMdmId, deviceId, xOrgId, applemdmsDeviceseraseRequest, _callback);

    }


    private ApiResponse<Void> deviceseraseWithHttpInfo(String appleMdmId, String deviceId, String xOrgId, ApplemdmsDeviceseraseRequest applemdmsDeviceseraseRequest) throws ApiException {
        okhttp3.Call localVarCall = deviceseraseValidateBeforeCall(appleMdmId, deviceId, xOrgId, applemdmsDeviceseraseRequest, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call deviceseraseAsync(String appleMdmId, String deviceId, String xOrgId, ApplemdmsDeviceseraseRequest applemdmsDeviceseraseRequest, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = deviceseraseValidateBeforeCall(appleMdmId, deviceId, xOrgId, applemdmsDeviceseraseRequest, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DeviceseraseRequestBuilder {
        private final String appleMdmId;
        private final String deviceId;
        private String pin;
        private String xOrgId;

        private DeviceseraseRequestBuilder(String appleMdmId, String deviceId) {
            this.appleMdmId = appleMdmId;
            this.deviceId = deviceId;
        }

        /**
         * Set pin
         * @param pin 6-digit PIN, required for MacOS, to erase the device (optional)
         * @return DeviceseraseRequestBuilder
         */
        public DeviceseraseRequestBuilder pin(String pin) {
            this.pin = pin;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return DeviceseraseRequestBuilder
         */
        public DeviceseraseRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for deviceserase
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
            ApplemdmsDeviceseraseRequest applemdmsDeviceseraseRequest = buildBodyParams();
            return deviceseraseCall(appleMdmId, deviceId, xOrgId, applemdmsDeviceseraseRequest, _callback);
        }

        private ApplemdmsDeviceseraseRequest buildBodyParams() {
            ApplemdmsDeviceseraseRequest applemdmsDeviceseraseRequest = new ApplemdmsDeviceseraseRequest();
            applemdmsDeviceseraseRequest.pin(this.pin);
            return applemdmsDeviceseraseRequest;
        }

        /**
         * Execute deviceserase request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            ApplemdmsDeviceseraseRequest applemdmsDeviceseraseRequest = buildBodyParams();
            deviceseraseWithHttpInfo(appleMdmId, deviceId, xOrgId, applemdmsDeviceseraseRequest);
        }

        /**
         * Execute deviceserase request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            ApplemdmsDeviceseraseRequest applemdmsDeviceseraseRequest = buildBodyParams();
            return deviceseraseWithHttpInfo(appleMdmId, deviceId, xOrgId, applemdmsDeviceseraseRequest);
        }

        /**
         * Execute deviceserase request (asynchronously)
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
            ApplemdmsDeviceseraseRequest applemdmsDeviceseraseRequest = buildBodyParams();
            return deviceseraseAsync(appleMdmId, deviceId, xOrgId, applemdmsDeviceseraseRequest, _callback);
        }
    }

    /**
     * Erase Device
     * Erases a DEP-enrolled device.  #### Sample Request &#x60;&#x60;&#x60;   curl -X POST https://console.jumpcloud.com/api/v2/applemdms/{apple_mdm_id}/devices/{device_id}/erase \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{}&#39; &#x60;&#x60;&#x60;
     * @param appleMdmId  (required)
     * @param deviceId  (required)
     * @return DeviceseraseRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public DeviceseraseRequestBuilder deviceserase(String appleMdmId, String deviceId) throws IllegalArgumentException {
        if (appleMdmId == null) throw new IllegalArgumentException("\"appleMdmId\" is required but got null");
            

        if (deviceId == null) throw new IllegalArgumentException("\"deviceId\" is required but got null");
            

        return new DeviceseraseRequestBuilder(appleMdmId, deviceId);
    }
    private okhttp3.Call deviceslistCall(String appleMdmId, Integer limit, String xOrgId, Integer skip, List<String> filter, List<String> sort, Integer xTotalCount, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/applemdms/{apple_mdm_id}/devices"
            .replace("{" + "apple_mdm_id" + "}", localVarApiClient.escapeString(appleMdmId.toString()));

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

        if (sort != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "sort", sort));
        }

        if (xOrgId != null) {
            localVarHeaderParams.put("x-org-id", localVarApiClient.parameterToString(xOrgId));
        }

        if (xTotalCount != null) {
            localVarHeaderParams.put("x-total-count", localVarApiClient.parameterToString(xTotalCount));
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
    private okhttp3.Call deviceslistValidateBeforeCall(String appleMdmId, Integer limit, String xOrgId, Integer skip, List<String> filter, List<String> sort, Integer xTotalCount, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appleMdmId' is set
        if (appleMdmId == null) {
            throw new ApiException("Missing the required parameter 'appleMdmId' when calling deviceslist(Async)");
        }

        return deviceslistCall(appleMdmId, limit, xOrgId, skip, filter, sort, xTotalCount, _callback);

    }


    private ApiResponse<List<AppleMdmDevice>> deviceslistWithHttpInfo(String appleMdmId, Integer limit, String xOrgId, Integer skip, List<String> filter, List<String> sort, Integer xTotalCount) throws ApiException {
        okhttp3.Call localVarCall = deviceslistValidateBeforeCall(appleMdmId, limit, xOrgId, skip, filter, sort, xTotalCount, null);
        Type localVarReturnType = new TypeToken<List<AppleMdmDevice>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call deviceslistAsync(String appleMdmId, Integer limit, String xOrgId, Integer skip, List<String> filter, List<String> sort, Integer xTotalCount, final ApiCallback<List<AppleMdmDevice>> _callback) throws ApiException {

        okhttp3.Call localVarCall = deviceslistValidateBeforeCall(appleMdmId, limit, xOrgId, skip, filter, sort, xTotalCount, _callback);
        Type localVarReturnType = new TypeToken<List<AppleMdmDevice>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class DeviceslistRequestBuilder {
        private final String appleMdmId;
        private Integer limit;
        private String xOrgId;
        private Integer skip;
        private List<String> filter;
        private List<String> sort;
        private Integer xTotalCount;

        private DeviceslistRequestBuilder(String appleMdmId) {
            this.appleMdmId = appleMdmId;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return DeviceslistRequestBuilder
         */
        public DeviceslistRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return DeviceslistRequestBuilder
         */
        public DeviceslistRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return DeviceslistRequestBuilder
         */
        public DeviceslistRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return DeviceslistRequestBuilder
         */
        public DeviceslistRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  (optional)
         * @return DeviceslistRequestBuilder
         */
        public DeviceslistRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set xTotalCount
         * @param xTotalCount  (optional)
         * @return DeviceslistRequestBuilder
         */
        public DeviceslistRequestBuilder xTotalCount(Integer xTotalCount) {
            this.xTotalCount = xTotalCount;
            return this;
        }
        
        /**
         * Build call for deviceslist
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
            return deviceslistCall(appleMdmId, limit, xOrgId, skip, filter, sort, xTotalCount, _callback);
        }


        /**
         * Execute deviceslist request
         * @return List&lt;AppleMdmDevice&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<AppleMdmDevice> execute() throws ApiException {
            ApiResponse<List<AppleMdmDevice>> localVarResp = deviceslistWithHttpInfo(appleMdmId, limit, xOrgId, skip, filter, sort, xTotalCount);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute deviceslist request with HTTP info returned
         * @return ApiResponse&lt;List&lt;AppleMdmDevice&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<AppleMdmDevice>> executeWithHttpInfo() throws ApiException {
            return deviceslistWithHttpInfo(appleMdmId, limit, xOrgId, skip, filter, sort, xTotalCount);
        }

        /**
         * Execute deviceslist request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<AppleMdmDevice>> _callback) throws ApiException {
            return deviceslistAsync(appleMdmId, limit, xOrgId, skip, filter, sort, xTotalCount, _callback);
        }
    }

    /**
     * List AppleMDM Devices
     * Lists all Apple MDM devices.  The filter and sort queries will allow the following fields: &#x60;createdAt&#x60; &#x60;depRegistered&#x60; &#x60;enrolled&#x60; &#x60;id&#x60; &#x60;osVersion&#x60; &#x60;serialNumber&#x60; &#x60;udid&#x60;  #### Sample Request &#x60;&#x60;&#x60;   curl -X GET https://console.jumpcloud.com/api/v2/applemdms/{apple_mdm_id}/devices \\   -H &#39;accept: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{}&#39; &#x60;&#x60;&#x60;
     * @param appleMdmId  (required)
     * @return DeviceslistRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public DeviceslistRequestBuilder deviceslist(String appleMdmId) throws IllegalArgumentException {
        if (appleMdmId == null) throw new IllegalArgumentException("\"appleMdmId\" is required but got null");
            

        return new DeviceslistRequestBuilder(appleMdmId);
    }
    private okhttp3.Call deviceslockCall(String appleMdmId, String deviceId, String xOrgId, ApplemdmsDeviceslockRequest applemdmsDeviceslockRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = applemdmsDeviceslockRequest;

        // create path and map variables
        String localVarPath = "/applemdms/{apple_mdm_id}/devices/{device_id}/lock"
            .replace("{" + "apple_mdm_id" + "}", localVarApiClient.escapeString(appleMdmId.toString()))
            .replace("{" + "device_id" + "}", localVarApiClient.escapeString(deviceId.toString()));

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
    private okhttp3.Call deviceslockValidateBeforeCall(String appleMdmId, String deviceId, String xOrgId, ApplemdmsDeviceslockRequest applemdmsDeviceslockRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appleMdmId' is set
        if (appleMdmId == null) {
            throw new ApiException("Missing the required parameter 'appleMdmId' when calling deviceslock(Async)");
        }

        // verify the required parameter 'deviceId' is set
        if (deviceId == null) {
            throw new ApiException("Missing the required parameter 'deviceId' when calling deviceslock(Async)");
        }

        return deviceslockCall(appleMdmId, deviceId, xOrgId, applemdmsDeviceslockRequest, _callback);

    }


    private ApiResponse<Void> deviceslockWithHttpInfo(String appleMdmId, String deviceId, String xOrgId, ApplemdmsDeviceslockRequest applemdmsDeviceslockRequest) throws ApiException {
        okhttp3.Call localVarCall = deviceslockValidateBeforeCall(appleMdmId, deviceId, xOrgId, applemdmsDeviceslockRequest, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call deviceslockAsync(String appleMdmId, String deviceId, String xOrgId, ApplemdmsDeviceslockRequest applemdmsDeviceslockRequest, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = deviceslockValidateBeforeCall(appleMdmId, deviceId, xOrgId, applemdmsDeviceslockRequest, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DeviceslockRequestBuilder {
        private final String appleMdmId;
        private final String deviceId;
        private String pin;
        private String xOrgId;

        private DeviceslockRequestBuilder(String appleMdmId, String deviceId) {
            this.appleMdmId = appleMdmId;
            this.deviceId = deviceId;
        }

        /**
         * Set pin
         * @param pin 6-digit PIN, required for MacOS, to lock the device (optional)
         * @return DeviceslockRequestBuilder
         */
        public DeviceslockRequestBuilder pin(String pin) {
            this.pin = pin;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return DeviceslockRequestBuilder
         */
        public DeviceslockRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for deviceslock
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
            ApplemdmsDeviceslockRequest applemdmsDeviceslockRequest = buildBodyParams();
            return deviceslockCall(appleMdmId, deviceId, xOrgId, applemdmsDeviceslockRequest, _callback);
        }

        private ApplemdmsDeviceslockRequest buildBodyParams() {
            ApplemdmsDeviceslockRequest applemdmsDeviceslockRequest = new ApplemdmsDeviceslockRequest();
            applemdmsDeviceslockRequest.pin(this.pin);
            return applemdmsDeviceslockRequest;
        }

        /**
         * Execute deviceslock request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            ApplemdmsDeviceslockRequest applemdmsDeviceslockRequest = buildBodyParams();
            deviceslockWithHttpInfo(appleMdmId, deviceId, xOrgId, applemdmsDeviceslockRequest);
        }

        /**
         * Execute deviceslock request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            ApplemdmsDeviceslockRequest applemdmsDeviceslockRequest = buildBodyParams();
            return deviceslockWithHttpInfo(appleMdmId, deviceId, xOrgId, applemdmsDeviceslockRequest);
        }

        /**
         * Execute deviceslock request (asynchronously)
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
            ApplemdmsDeviceslockRequest applemdmsDeviceslockRequest = buildBodyParams();
            return deviceslockAsync(appleMdmId, deviceId, xOrgId, applemdmsDeviceslockRequest, _callback);
        }
    }

    /**
     * Lock Device
     * Locks a DEP-enrolled device.  #### Sample Request &#x60;&#x60;&#x60;   curl -X POST https://console.jumpcloud.com/api/v2/applemdms/{apple_mdm_id}/devices/{device_id}/lock \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{}&#39; &#x60;&#x60;&#x60;
     * @param appleMdmId  (required)
     * @param deviceId  (required)
     * @return DeviceslockRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public DeviceslockRequestBuilder deviceslock(String appleMdmId, String deviceId) throws IllegalArgumentException {
        if (appleMdmId == null) throw new IllegalArgumentException("\"appleMdmId\" is required but got null");
            

        if (deviceId == null) throw new IllegalArgumentException("\"deviceId\" is required but got null");
            

        return new DeviceslockRequestBuilder(appleMdmId, deviceId);
    }
    private okhttp3.Call devicesrestartCall(String appleMdmId, String deviceId, String xOrgId, ApplemdmsDevicesrestartRequest applemdmsDevicesrestartRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = applemdmsDevicesrestartRequest;

        // create path and map variables
        String localVarPath = "/applemdms/{apple_mdm_id}/devices/{device_id}/restart"
            .replace("{" + "apple_mdm_id" + "}", localVarApiClient.escapeString(appleMdmId.toString()))
            .replace("{" + "device_id" + "}", localVarApiClient.escapeString(deviceId.toString()));

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
    private okhttp3.Call devicesrestartValidateBeforeCall(String appleMdmId, String deviceId, String xOrgId, ApplemdmsDevicesrestartRequest applemdmsDevicesrestartRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appleMdmId' is set
        if (appleMdmId == null) {
            throw new ApiException("Missing the required parameter 'appleMdmId' when calling devicesrestart(Async)");
        }

        // verify the required parameter 'deviceId' is set
        if (deviceId == null) {
            throw new ApiException("Missing the required parameter 'deviceId' when calling devicesrestart(Async)");
        }

        return devicesrestartCall(appleMdmId, deviceId, xOrgId, applemdmsDevicesrestartRequest, _callback);

    }


    private ApiResponse<Void> devicesrestartWithHttpInfo(String appleMdmId, String deviceId, String xOrgId, ApplemdmsDevicesrestartRequest applemdmsDevicesrestartRequest) throws ApiException {
        okhttp3.Call localVarCall = devicesrestartValidateBeforeCall(appleMdmId, deviceId, xOrgId, applemdmsDevicesrestartRequest, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call devicesrestartAsync(String appleMdmId, String deviceId, String xOrgId, ApplemdmsDevicesrestartRequest applemdmsDevicesrestartRequest, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = devicesrestartValidateBeforeCall(appleMdmId, deviceId, xOrgId, applemdmsDevicesrestartRequest, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DevicesrestartRequestBuilder {
        private final String appleMdmId;
        private final String deviceId;
        private List<String> kextPaths;
        private String xOrgId;

        private DevicesrestartRequestBuilder(String appleMdmId, String deviceId) {
            this.appleMdmId = appleMdmId;
            this.deviceId = deviceId;
        }

        /**
         * Set kextPaths
         * @param kextPaths The string to pass when doing a restart and performing a RebuildKernelCache. (optional)
         * @return DevicesrestartRequestBuilder
         */
        public DevicesrestartRequestBuilder kextPaths(List<String> kextPaths) {
            this.kextPaths = kextPaths;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return DevicesrestartRequestBuilder
         */
        public DevicesrestartRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for devicesrestart
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
            ApplemdmsDevicesrestartRequest applemdmsDevicesrestartRequest = buildBodyParams();
            return devicesrestartCall(appleMdmId, deviceId, xOrgId, applemdmsDevicesrestartRequest, _callback);
        }

        private ApplemdmsDevicesrestartRequest buildBodyParams() {
            ApplemdmsDevicesrestartRequest applemdmsDevicesrestartRequest = new ApplemdmsDevicesrestartRequest();
            applemdmsDevicesrestartRequest.kextPaths(this.kextPaths);
            return applemdmsDevicesrestartRequest;
        }

        /**
         * Execute devicesrestart request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            ApplemdmsDevicesrestartRequest applemdmsDevicesrestartRequest = buildBodyParams();
            devicesrestartWithHttpInfo(appleMdmId, deviceId, xOrgId, applemdmsDevicesrestartRequest);
        }

        /**
         * Execute devicesrestart request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            ApplemdmsDevicesrestartRequest applemdmsDevicesrestartRequest = buildBodyParams();
            return devicesrestartWithHttpInfo(appleMdmId, deviceId, xOrgId, applemdmsDevicesrestartRequest);
        }

        /**
         * Execute devicesrestart request (asynchronously)
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
            ApplemdmsDevicesrestartRequest applemdmsDevicesrestartRequest = buildBodyParams();
            return devicesrestartAsync(appleMdmId, deviceId, xOrgId, applemdmsDevicesrestartRequest, _callback);
        }
    }

    /**
     * Restart Device
     * Restarts a DEP-enrolled device.  #### Sample Request &#x60;&#x60;&#x60;   curl -X POST https://console.jumpcloud.com/api/v2/applemdms/{apple_mdm_id}/devices/{device_id}/restart \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{\&quot;kextPaths\&quot;: [\&quot;Path1\&quot;, \&quot;Path2\&quot;]}&#39; &#x60;&#x60;&#x60;
     * @param appleMdmId  (required)
     * @param deviceId  (required)
     * @return DevicesrestartRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public DevicesrestartRequestBuilder devicesrestart(String appleMdmId, String deviceId) throws IllegalArgumentException {
        if (appleMdmId == null) throw new IllegalArgumentException("\"appleMdmId\" is required but got null");
            

        if (deviceId == null) throw new IllegalArgumentException("\"deviceId\" is required but got null");
            

        return new DevicesrestartRequestBuilder(appleMdmId, deviceId);
    }
    private okhttp3.Call devicesshutdownCall(String appleMdmId, String deviceId, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/applemdms/{apple_mdm_id}/devices/{device_id}/shutdown"
            .replace("{" + "apple_mdm_id" + "}", localVarApiClient.escapeString(appleMdmId.toString()))
            .replace("{" + "device_id" + "}", localVarApiClient.escapeString(deviceId.toString()));

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
    private okhttp3.Call devicesshutdownValidateBeforeCall(String appleMdmId, String deviceId, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appleMdmId' is set
        if (appleMdmId == null) {
            throw new ApiException("Missing the required parameter 'appleMdmId' when calling devicesshutdown(Async)");
        }

        // verify the required parameter 'deviceId' is set
        if (deviceId == null) {
            throw new ApiException("Missing the required parameter 'deviceId' when calling devicesshutdown(Async)");
        }

        return devicesshutdownCall(appleMdmId, deviceId, xOrgId, _callback);

    }


    private ApiResponse<Void> devicesshutdownWithHttpInfo(String appleMdmId, String deviceId, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = devicesshutdownValidateBeforeCall(appleMdmId, deviceId, xOrgId, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call devicesshutdownAsync(String appleMdmId, String deviceId, String xOrgId, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = devicesshutdownValidateBeforeCall(appleMdmId, deviceId, xOrgId, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DevicesshutdownRequestBuilder {
        private final String appleMdmId;
        private final String deviceId;
        private String xOrgId;

        private DevicesshutdownRequestBuilder(String appleMdmId, String deviceId) {
            this.appleMdmId = appleMdmId;
            this.deviceId = deviceId;
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return DevicesshutdownRequestBuilder
         */
        public DevicesshutdownRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for devicesshutdown
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
            return devicesshutdownCall(appleMdmId, deviceId, xOrgId, _callback);
        }


        /**
         * Execute devicesshutdown request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            devicesshutdownWithHttpInfo(appleMdmId, deviceId, xOrgId);
        }

        /**
         * Execute devicesshutdown request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            return devicesshutdownWithHttpInfo(appleMdmId, deviceId, xOrgId);
        }

        /**
         * Execute devicesshutdown request (asynchronously)
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
            return devicesshutdownAsync(appleMdmId, deviceId, xOrgId, _callback);
        }
    }

    /**
     * Shut Down Device
     * Shuts down a DEP-enrolled device.  #### Sample Request &#x60;&#x60;&#x60;   curl -X POST https://console.jumpcloud.com/api/v2/applemdms/{apple_mdm_id}/devices/{device_id}/shutdown \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{}&#39; &#x60;&#x60;&#x60;
     * @param appleMdmId  (required)
     * @param deviceId  (required)
     * @return DevicesshutdownRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public DevicesshutdownRequestBuilder devicesshutdown(String appleMdmId, String deviceId) throws IllegalArgumentException {
        if (appleMdmId == null) throw new IllegalArgumentException("\"appleMdmId\" is required but got null");
            

        if (deviceId == null) throw new IllegalArgumentException("\"deviceId\" is required but got null");
            

        return new DevicesshutdownRequestBuilder(appleMdmId, deviceId);
    }
    private okhttp3.Call enrollmentprofilesgetCall(String appleMdmId, String id, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/applemdms/{apple_mdm_id}/enrollmentprofiles/{id}"
            .replace("{" + "apple_mdm_id" + "}", localVarApiClient.escapeString(appleMdmId.toString()))
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
            "application/x-apple-aspen-config"
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
    private okhttp3.Call enrollmentprofilesgetValidateBeforeCall(String appleMdmId, String id, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appleMdmId' is set
        if (appleMdmId == null) {
            throw new ApiException("Missing the required parameter 'appleMdmId' when calling enrollmentprofilesget(Async)");
        }

        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling enrollmentprofilesget(Async)");
        }

        return enrollmentprofilesgetCall(appleMdmId, id, xOrgId, _callback);

    }


    private ApiResponse<String> enrollmentprofilesgetWithHttpInfo(String appleMdmId, String id, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = enrollmentprofilesgetValidateBeforeCall(appleMdmId, id, xOrgId, null);
        Type localVarReturnType = new TypeToken<String>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call enrollmentprofilesgetAsync(String appleMdmId, String id, String xOrgId, final ApiCallback<String> _callback) throws ApiException {

        okhttp3.Call localVarCall = enrollmentprofilesgetValidateBeforeCall(appleMdmId, id, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<String>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class EnrollmentprofilesgetRequestBuilder {
        private final String appleMdmId;
        private final String id;
        private String xOrgId;

        private EnrollmentprofilesgetRequestBuilder(String appleMdmId, String id) {
            this.appleMdmId = appleMdmId;
            this.id = id;
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return EnrollmentprofilesgetRequestBuilder
         */
        public EnrollmentprofilesgetRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for enrollmentprofilesget
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
            return enrollmentprofilesgetCall(appleMdmId, id, xOrgId, _callback);
        }


        /**
         * Execute enrollmentprofilesget request
         * @return String
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public String execute() throws ApiException {
            ApiResponse<String> localVarResp = enrollmentprofilesgetWithHttpInfo(appleMdmId, id, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute enrollmentprofilesget request with HTTP info returned
         * @return ApiResponse&lt;String&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<String> executeWithHttpInfo() throws ApiException {
            return enrollmentprofilesgetWithHttpInfo(appleMdmId, id, xOrgId);
        }

        /**
         * Execute enrollmentprofilesget request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<String> _callback) throws ApiException {
            return enrollmentprofilesgetAsync(appleMdmId, id, xOrgId, _callback);
        }
    }

    /**
     * Get an Apple MDM Enrollment Profile
     * Get an enrollment profile  Currently only requesting the mobileconfig is supported.  #### Sample Request  &#x60;&#x60;&#x60; curl https://console.jumpcloud.com/api/v2/applemdms/{APPLE_MDM_ID}/enrollmentprofiles/{ID} \\   -H &#39;accept: application/x-apple-aspen-config&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param appleMdmId  (required)
     * @param id  (required)
     * @return EnrollmentprofilesgetRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public EnrollmentprofilesgetRequestBuilder enrollmentprofilesget(String appleMdmId, String id) throws IllegalArgumentException {
        if (appleMdmId == null) throw new IllegalArgumentException("\"appleMdmId\" is required but got null");
            

        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new EnrollmentprofilesgetRequestBuilder(appleMdmId, id);
    }
    private okhttp3.Call enrollmentprofileslistCall(String appleMdmId, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/applemdms/{apple_mdm_id}/enrollmentprofiles"
            .replace("{" + "apple_mdm_id" + "}", localVarApiClient.escapeString(appleMdmId.toString()));

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
    private okhttp3.Call enrollmentprofileslistValidateBeforeCall(String appleMdmId, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appleMdmId' is set
        if (appleMdmId == null) {
            throw new ApiException("Missing the required parameter 'appleMdmId' when calling enrollmentprofileslist(Async)");
        }

        return enrollmentprofileslistCall(appleMdmId, xOrgId, _callback);

    }


    private ApiResponse<List<AppleMDM>> enrollmentprofileslistWithHttpInfo(String appleMdmId, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = enrollmentprofileslistValidateBeforeCall(appleMdmId, xOrgId, null);
        Type localVarReturnType = new TypeToken<List<AppleMDM>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call enrollmentprofileslistAsync(String appleMdmId, String xOrgId, final ApiCallback<List<AppleMDM>> _callback) throws ApiException {

        okhttp3.Call localVarCall = enrollmentprofileslistValidateBeforeCall(appleMdmId, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<List<AppleMDM>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class EnrollmentprofileslistRequestBuilder {
        private final String appleMdmId;
        private String xOrgId;

        private EnrollmentprofileslistRequestBuilder(String appleMdmId) {
            this.appleMdmId = appleMdmId;
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return EnrollmentprofileslistRequestBuilder
         */
        public EnrollmentprofileslistRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for enrollmentprofileslist
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
            return enrollmentprofileslistCall(appleMdmId, xOrgId, _callback);
        }


        /**
         * Execute enrollmentprofileslist request
         * @return List&lt;AppleMDM&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<AppleMDM> execute() throws ApiException {
            ApiResponse<List<AppleMDM>> localVarResp = enrollmentprofileslistWithHttpInfo(appleMdmId, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute enrollmentprofileslist request with HTTP info returned
         * @return ApiResponse&lt;List&lt;AppleMDM&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<AppleMDM>> executeWithHttpInfo() throws ApiException {
            return enrollmentprofileslistWithHttpInfo(appleMdmId, xOrgId);
        }

        /**
         * Execute enrollmentprofileslist request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<AppleMDM>> _callback) throws ApiException {
            return enrollmentprofileslistAsync(appleMdmId, xOrgId, _callback);
        }
    }

    /**
     * List Apple MDM Enrollment Profiles
     * Get a list of enrollment profiles for an apple mdm.  Note: currently only one enrollment profile is supported.  #### Sample Request &#x60;&#x60;&#x60;  curl https://console.jumpcloud.com/api/v2/applemdms/{APPLE_MDM_ID}/enrollmentprofiles \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param appleMdmId  (required)
     * @return EnrollmentprofileslistRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public EnrollmentprofileslistRequestBuilder enrollmentprofileslist(String appleMdmId) throws IllegalArgumentException {
        if (appleMdmId == null) throw new IllegalArgumentException("\"appleMdmId\" is required but got null");
            

        return new EnrollmentprofileslistRequestBuilder(appleMdmId);
    }
    private okhttp3.Call getdeviceCall(String appleMdmId, String deviceId, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/applemdms/{apple_mdm_id}/devices/{device_id}"
            .replace("{" + "apple_mdm_id" + "}", localVarApiClient.escapeString(appleMdmId.toString()))
            .replace("{" + "device_id" + "}", localVarApiClient.escapeString(deviceId.toString()));

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
    private okhttp3.Call getdeviceValidateBeforeCall(String appleMdmId, String deviceId, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appleMdmId' is set
        if (appleMdmId == null) {
            throw new ApiException("Missing the required parameter 'appleMdmId' when calling getdevice(Async)");
        }

        // verify the required parameter 'deviceId' is set
        if (deviceId == null) {
            throw new ApiException("Missing the required parameter 'deviceId' when calling getdevice(Async)");
        }

        return getdeviceCall(appleMdmId, deviceId, xOrgId, _callback);

    }


    private ApiResponse<AppleMdmDevice> getdeviceWithHttpInfo(String appleMdmId, String deviceId, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = getdeviceValidateBeforeCall(appleMdmId, deviceId, xOrgId, null);
        Type localVarReturnType = new TypeToken<AppleMdmDevice>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getdeviceAsync(String appleMdmId, String deviceId, String xOrgId, final ApiCallback<AppleMdmDevice> _callback) throws ApiException {

        okhttp3.Call localVarCall = getdeviceValidateBeforeCall(appleMdmId, deviceId, xOrgId, _callback);
        Type localVarReturnType = new TypeToken<AppleMdmDevice>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetdeviceRequestBuilder {
        private final String appleMdmId;
        private final String deviceId;
        private String xOrgId;

        private GetdeviceRequestBuilder(String appleMdmId, String deviceId) {
            this.appleMdmId = appleMdmId;
            this.deviceId = deviceId;
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return GetdeviceRequestBuilder
         */
        public GetdeviceRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for getdevice
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
            return getdeviceCall(appleMdmId, deviceId, xOrgId, _callback);
        }


        /**
         * Execute getdevice request
         * @return AppleMdmDevice
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public AppleMdmDevice execute() throws ApiException {
            ApiResponse<AppleMdmDevice> localVarResp = getdeviceWithHttpInfo(appleMdmId, deviceId, xOrgId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getdevice request with HTTP info returned
         * @return ApiResponse&lt;AppleMdmDevice&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<AppleMdmDevice> executeWithHttpInfo() throws ApiException {
            return getdeviceWithHttpInfo(appleMdmId, deviceId, xOrgId);
        }

        /**
         * Execute getdevice request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AppleMdmDevice> _callback) throws ApiException {
            return getdeviceAsync(appleMdmId, deviceId, xOrgId, _callback);
        }
    }

    /**
     * Details of an AppleMDM Device
     * Gets a single Apple MDM device.  #### Sample Request &#x60;&#x60;&#x60;   curl -X GET https://console.jumpcloud.com/api/v2/applemdms/{apple_mdm_id}/devices/{device_id} \\   -H &#39;accept: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
     * @param appleMdmId  (required)
     * @param deviceId  (required)
     * @return GetdeviceRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public GetdeviceRequestBuilder getdevice(String appleMdmId, String deviceId) throws IllegalArgumentException {
        if (appleMdmId == null) throw new IllegalArgumentException("\"appleMdmId\" is required but got null");
            

        if (deviceId == null) throw new IllegalArgumentException("\"deviceId\" is required but got null");
            

        return new GetdeviceRequestBuilder(appleMdmId, deviceId);
    }
    private okhttp3.Call listCall(String xOrgId, Integer limit, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/applemdms";

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
    private okhttp3.Call listValidateBeforeCall(String xOrgId, Integer limit, Integer skip, List<String> filter, final ApiCallback _callback) throws ApiException {
        return listCall(xOrgId, limit, skip, filter, _callback);

    }


    private ApiResponse<List<AppleMDM>> listWithHttpInfo(String xOrgId, Integer limit, Integer skip, List<String> filter) throws ApiException {
        okhttp3.Call localVarCall = listValidateBeforeCall(xOrgId, limit, skip, filter, null);
        Type localVarReturnType = new TypeToken<List<AppleMDM>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listAsync(String xOrgId, Integer limit, Integer skip, List<String> filter, final ApiCallback<List<AppleMDM>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listValidateBeforeCall(xOrgId, limit, skip, filter, _callback);
        Type localVarReturnType = new TypeToken<List<AppleMDM>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListRequestBuilder {
        private String xOrgId;
        private Integer limit;
        private Integer skip;
        private List<String> filter;

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
         * Set limit
         * @param limit  (optional, default to 1)
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
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return ListRequestBuilder
         */
        public ListRequestBuilder filter(List<String> filter) {
            this.filter = filter;
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
            return listCall(xOrgId, limit, skip, filter, _callback);
        }


        /**
         * Execute list request
         * @return List&lt;AppleMDM&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<AppleMDM> execute() throws ApiException {
            ApiResponse<List<AppleMDM>> localVarResp = listWithHttpInfo(xOrgId, limit, skip, filter);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute list request with HTTP info returned
         * @return ApiResponse&lt;List&lt;AppleMDM&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<AppleMDM>> executeWithHttpInfo() throws ApiException {
            return listWithHttpInfo(xOrgId, limit, skip, filter);
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
        public okhttp3.Call executeAsync(final ApiCallback<List<AppleMDM>> _callback) throws ApiException {
            return listAsync(xOrgId, limit, skip, filter, _callback);
        }
    }

    /**
     * List Apple MDMs
     * Get a list of all Apple MDM configurations.  An empty topic indicates that a signed certificate from Apple has not been provided to the PUT endpoint yet.  Note: currently only one MDM configuration per organization is supported.  #### Sample Request &#x60;&#x60;&#x60; curl https://console.jumpcloud.com/api/v2/applemdms \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;
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
    private okhttp3.Call putCall(String id, String xOrgId, AppleMdmPatch appleMdmPatch, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = appleMdmPatch;

        // create path and map variables
        String localVarPath = "/applemdms/{id}"
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
    private okhttp3.Call putValidateBeforeCall(String id, String xOrgId, AppleMdmPatch appleMdmPatch, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling put(Async)");
        }

        return putCall(id, xOrgId, appleMdmPatch, _callback);

    }


    private ApiResponse<AppleMDM> putWithHttpInfo(String id, String xOrgId, AppleMdmPatch appleMdmPatch) throws ApiException {
        okhttp3.Call localVarCall = putValidateBeforeCall(id, xOrgId, appleMdmPatch, null);
        Type localVarReturnType = new TypeToken<AppleMDM>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call putAsync(String id, String xOrgId, AppleMdmPatch appleMdmPatch, final ApiCallback<AppleMDM> _callback) throws ApiException {

        okhttp3.Call localVarCall = putValidateBeforeCall(id, xOrgId, appleMdmPatch, _callback);
        Type localVarReturnType = new TypeToken<AppleMDM>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class PutRequestBuilder {
        private final String id;
        private ADES ades;
        private Boolean allowMobileUserEnrollment;
        private String appleCertCreatorAppleID;
        private String appleSignedCert;
        private String defaultIosUserEnrollmentDeviceGroupID;
        private String defaultSystemGroupID;
        private DEP dep;
        private String encryptedDepServerToken;
        private String name;
        private String xOrgId;

        private PutRequestBuilder(String id) {
            this.id = id;
        }

        /**
         * Set ades
         * @param ades  (optional)
         * @return PutRequestBuilder
         */
        public PutRequestBuilder ades(ADES ades) {
            this.ades = ades;
            return this;
        }
        
        /**
         * Set allowMobileUserEnrollment
         * @param allowMobileUserEnrollment A toggle to allow mobile device enrollment for an organization. (optional)
         * @return PutRequestBuilder
         */
        public PutRequestBuilder allowMobileUserEnrollment(Boolean allowMobileUserEnrollment) {
            this.allowMobileUserEnrollment = allowMobileUserEnrollment;
            return this;
        }
        
        /**
         * Set appleCertCreatorAppleID
         * @param appleCertCreatorAppleID The Apple ID of the admin who created the Apple signed certificate. (optional)
         * @return PutRequestBuilder
         */
        public PutRequestBuilder appleCertCreatorAppleID(String appleCertCreatorAppleID) {
            this.appleCertCreatorAppleID = appleCertCreatorAppleID;
            return this;
        }
        
        /**
         * Set appleSignedCert
         * @param appleSignedCert A signed certificate obtained from Apple after providing Apple with the plist file provided on POST. (optional)
         * @return PutRequestBuilder
         */
        public PutRequestBuilder appleSignedCert(String appleSignedCert) {
            this.appleSignedCert = appleSignedCert;
            return this;
        }
        
        /**
         * Set defaultIosUserEnrollmentDeviceGroupID
         * @param defaultIosUserEnrollmentDeviceGroupID ObjectId uniquely identifying the MDM default iOS user enrollment device group. (optional)
         * @return PutRequestBuilder
         */
        public PutRequestBuilder defaultIosUserEnrollmentDeviceGroupID(String defaultIosUserEnrollmentDeviceGroupID) {
            this.defaultIosUserEnrollmentDeviceGroupID = defaultIosUserEnrollmentDeviceGroupID;
            return this;
        }
        
        /**
         * Set defaultSystemGroupID
         * @param defaultSystemGroupID ObjectId uniquely identifying the MDM default System Group. (optional)
         * @return PutRequestBuilder
         */
        public PutRequestBuilder defaultSystemGroupID(String defaultSystemGroupID) {
            this.defaultSystemGroupID = defaultSystemGroupID;
            return this;
        }
        
        /**
         * Set dep
         * @param dep  (optional)
         * @return PutRequestBuilder
         */
        public PutRequestBuilder dep(DEP dep) {
            this.dep = dep;
            return this;
        }
        
        /**
         * Set encryptedDepServerToken
         * @param encryptedDepServerToken The S/MIME encoded DEP Server Token returned by Apple Business Manager when creating an MDM instance. (optional)
         * @return PutRequestBuilder
         */
        public PutRequestBuilder encryptedDepServerToken(String encryptedDepServerToken) {
            this.encryptedDepServerToken = encryptedDepServerToken;
            return this;
        }
        
        /**
         * Set name
         * @param name A new name for the Apple MDM configuration. (optional)
         * @return PutRequestBuilder
         */
        public PutRequestBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return PutRequestBuilder
         */
        public PutRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for put
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
            AppleMdmPatch appleMdmPatch = buildBodyParams();
            return putCall(id, xOrgId, appleMdmPatch, _callback);
        }

        private AppleMdmPatch buildBodyParams() {
            AppleMdmPatch appleMdmPatch = new AppleMdmPatch();
            appleMdmPatch.ades(this.ades);
            appleMdmPatch.allowMobileUserEnrollment(this.allowMobileUserEnrollment);
            appleMdmPatch.appleCertCreatorAppleID(this.appleCertCreatorAppleID);
            appleMdmPatch.appleSignedCert(this.appleSignedCert);
            appleMdmPatch.defaultIosUserEnrollmentDeviceGroupID(this.defaultIosUserEnrollmentDeviceGroupID);
            appleMdmPatch.defaultSystemGroupID(this.defaultSystemGroupID);
            appleMdmPatch.dep(this.dep);
            appleMdmPatch.encryptedDepServerToken(this.encryptedDepServerToken);
            appleMdmPatch.name(this.name);
            return appleMdmPatch;
        }

        /**
         * Execute put request
         * @return AppleMDM
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public AppleMDM execute() throws ApiException {
            AppleMdmPatch appleMdmPatch = buildBodyParams();
            ApiResponse<AppleMDM> localVarResp = putWithHttpInfo(id, xOrgId, appleMdmPatch);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute put request with HTTP info returned
         * @return ApiResponse&lt;AppleMDM&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<AppleMDM> executeWithHttpInfo() throws ApiException {
            AppleMdmPatch appleMdmPatch = buildBodyParams();
            return putWithHttpInfo(id, xOrgId, appleMdmPatch);
        }

        /**
         * Execute put request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AppleMDM> _callback) throws ApiException {
            AppleMdmPatch appleMdmPatch = buildBodyParams();
            return putAsync(id, xOrgId, appleMdmPatch, _callback);
        }
    }

    /**
     * Update an Apple MDM
     * Updates an Apple MDM configuration.  This endpoint is used to supply JumpCloud with a signed certificate from Apple in order to finalize the setup and allow JumpCloud to manage your devices.  It may also be used to update the DEP Settings.  #### Sample Request &#x60;&#x60;&#x60;   curl -X PUT https://console.jumpcloud.com/api/v2/applemdms/{ID} \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;name\&quot;: \&quot;MDM name\&quot;,     \&quot;appleSignedCert\&quot;: \&quot;{CERTIFICATE}\&quot;,     \&quot;encryptedDepServerToken\&quot;: \&quot;{SERVER_TOKEN}\&quot;,     \&quot;dep\&quot;: {       \&quot;welcomeScreen\&quot;: {         \&quot;title\&quot;: \&quot;Welcome\&quot;,         \&quot;paragraph\&quot;: \&quot;In just a few steps, you will be working securely from your Mac.\&quot;,         \&quot;button\&quot;: \&quot;continue\&quot;,       },     },   }&#39; &#x60;&#x60;&#x60;
     * @param id  (required)
     * @return PutRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public PutRequestBuilder put(String id) throws IllegalArgumentException {
        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new PutRequestBuilder(id);
    }
    private okhttp3.Call refreshdepdevicesCall(String appleMdmId, String xOrgId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/applemdms/{apple_mdm_id}/refreshdepdevices"
            .replace("{" + "apple_mdm_id" + "}", localVarApiClient.escapeString(appleMdmId.toString()));

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
    private okhttp3.Call refreshdepdevicesValidateBeforeCall(String appleMdmId, String xOrgId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appleMdmId' is set
        if (appleMdmId == null) {
            throw new ApiException("Missing the required parameter 'appleMdmId' when calling refreshdepdevices(Async)");
        }

        return refreshdepdevicesCall(appleMdmId, xOrgId, _callback);

    }


    private ApiResponse<Void> refreshdepdevicesWithHttpInfo(String appleMdmId, String xOrgId) throws ApiException {
        okhttp3.Call localVarCall = refreshdepdevicesValidateBeforeCall(appleMdmId, xOrgId, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call refreshdepdevicesAsync(String appleMdmId, String xOrgId, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = refreshdepdevicesValidateBeforeCall(appleMdmId, xOrgId, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class RefreshdepdevicesRequestBuilder {
        private final String appleMdmId;
        private String xOrgId;

        private RefreshdepdevicesRequestBuilder(String appleMdmId) {
            this.appleMdmId = appleMdmId;
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return RefreshdepdevicesRequestBuilder
         */
        public RefreshdepdevicesRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Build call for refreshdepdevices
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
            return refreshdepdevicesCall(appleMdmId, xOrgId, _callback);
        }


        /**
         * Execute refreshdepdevices request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            refreshdepdevicesWithHttpInfo(appleMdmId, xOrgId);
        }

        /**
         * Execute refreshdepdevices request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            return refreshdepdevicesWithHttpInfo(appleMdmId, xOrgId);
        }

        /**
         * Execute refreshdepdevices request (asynchronously)
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
            return refreshdepdevicesAsync(appleMdmId, xOrgId, _callback);
        }
    }

    /**
     * Refresh DEP Devices
     * Refreshes the list of devices that a JumpCloud admin has added to their virtual MDM in Apple Business Manager - ABM so that they can be DEP enrolled with JumpCloud.  #### Sample Request &#x60;&#x60;&#x60;   curl -X POST https://console.jumpcloud.com/api/v2/applemdms/{apple_mdm_id}/refreshdepdevices \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{}&#39; &#x60;&#x60;&#x60;
     * @param appleMdmId  (required)
     * @return RefreshdepdevicesRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public RefreshdepdevicesRequestBuilder refreshdepdevices(String appleMdmId) throws IllegalArgumentException {
        if (appleMdmId == null) throw new IllegalArgumentException("\"appleMdmId\" is required but got null");
            

        return new RefreshdepdevicesRequestBuilder(appleMdmId);
    }
}
