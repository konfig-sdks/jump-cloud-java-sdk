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


import com.konfigthis.client.model.Administrator;
import com.konfigthis.client.model.AdministratorOrganizationLink;
import com.konfigthis.client.model.AdministratorOrganizationLinkReq;
import com.konfigthis.client.model.CasesMetadataResponse;
import com.konfigthis.client.model.CasesResponse;
import com.konfigthis.client.model.CreateOrganization;
import java.io.File;
import com.konfigthis.client.model.Organization;
import com.konfigthis.client.model.PolicyGroupTemplate;
import com.konfigthis.client.model.PolicyGroupTemplateMembers;
import com.konfigthis.client.model.PolicyGroupTemplates;
import com.konfigthis.client.model.PolicyGroupTemplatesListConfiguredPolicyTemplatesResponse;
import com.konfigthis.client.model.Provider;
import com.konfigthis.client.model.ProviderAdminReq;
import com.konfigthis.client.model.ProviderInvoiceResponse;
import com.konfigthis.client.model.ProvidersListAdministratorsResponse;
import com.konfigthis.client.model.ProvidersListOrganizationsResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;

public class ManagedServiceProviderApiGenerated {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public ManagedServiceProviderApiGenerated() throws IllegalArgumentException {
        this(Configuration.getDefaultApiClient());
    }

    public ManagedServiceProviderApiGenerated(ApiClient apiClient) throws IllegalArgumentException {
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

    private okhttp3.Call casesMetadataCall(final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/cases/metadata";

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
    private okhttp3.Call casesMetadataValidateBeforeCall(final ApiCallback _callback) throws ApiException {
        return casesMetadataCall(_callback);

    }


    private ApiResponse<CasesMetadataResponse> casesMetadataWithHttpInfo() throws ApiException {
        okhttp3.Call localVarCall = casesMetadataValidateBeforeCall(null);
        Type localVarReturnType = new TypeToken<CasesMetadataResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call casesMetadataAsync(final ApiCallback<CasesMetadataResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = casesMetadataValidateBeforeCall(_callback);
        Type localVarReturnType = new TypeToken<CasesMetadataResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class CasesMetadataRequestBuilder {

        private CasesMetadataRequestBuilder() {
        }

        /**
         * Build call for casesMetadata
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
            return casesMetadataCall(_callback);
        }


        /**
         * Execute casesMetadata request
         * @return CasesMetadataResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public CasesMetadataResponse execute() throws ApiException {
            ApiResponse<CasesMetadataResponse> localVarResp = casesMetadataWithHttpInfo();
            return localVarResp.getResponseBody();
        }

        /**
         * Execute casesMetadata request with HTTP info returned
         * @return ApiResponse&lt;CasesMetadataResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<CasesMetadataResponse> executeWithHttpInfo() throws ApiException {
            return casesMetadataWithHttpInfo();
        }

        /**
         * Execute casesMetadata request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<CasesMetadataResponse> _callback) throws ApiException {
            return casesMetadataAsync(_callback);
        }
    }

    /**
     * Get the metadata for cases
     * This endpoint returns the metadata for cases
     * @return CasesMetadataRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public CasesMetadataRequestBuilder casesMetadata() throws IllegalArgumentException {
        return new CasesMetadataRequestBuilder();
    }
    private okhttp3.Call createByAdministratorCall(String id, AdministratorOrganizationLinkReq administratorOrganizationLinkReq, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = administratorOrganizationLinkReq;

        // create path and map variables
        String localVarPath = "/administrators/{id}/organizationlinks"
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
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call createByAdministratorValidateBeforeCall(String id, AdministratorOrganizationLinkReq administratorOrganizationLinkReq, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling createByAdministrator(Async)");
        }

        return createByAdministratorCall(id, administratorOrganizationLinkReq, _callback);

    }


    private ApiResponse<AdministratorOrganizationLink> createByAdministratorWithHttpInfo(String id, AdministratorOrganizationLinkReq administratorOrganizationLinkReq) throws ApiException {
        okhttp3.Call localVarCall = createByAdministratorValidateBeforeCall(id, administratorOrganizationLinkReq, null);
        Type localVarReturnType = new TypeToken<AdministratorOrganizationLink>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call createByAdministratorAsync(String id, AdministratorOrganizationLinkReq administratorOrganizationLinkReq, final ApiCallback<AdministratorOrganizationLink> _callback) throws ApiException {

        okhttp3.Call localVarCall = createByAdministratorValidateBeforeCall(id, administratorOrganizationLinkReq, _callback);
        Type localVarReturnType = new TypeToken<AdministratorOrganizationLink>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class CreateByAdministratorRequestBuilder {
        private final String id;
        private String organization;

        private CreateByAdministratorRequestBuilder(String id) {
            this.id = id;
        }

        /**
         * Set organization
         * @param organization The identifier for an organization to link this administrator to. (optional)
         * @return CreateByAdministratorRequestBuilder
         */
        public CreateByAdministratorRequestBuilder organization(String organization) {
            this.organization = organization;
            return this;
        }
        
        /**
         * Build call for createByAdministrator
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            AdministratorOrganizationLinkReq administratorOrganizationLinkReq = buildBodyParams();
            return createByAdministratorCall(id, administratorOrganizationLinkReq, _callback);
        }

        private AdministratorOrganizationLinkReq buildBodyParams() {
            AdministratorOrganizationLinkReq administratorOrganizationLinkReq = new AdministratorOrganizationLinkReq();
            administratorOrganizationLinkReq.organization(this.organization);
            return administratorOrganizationLinkReq;
        }

        /**
         * Execute createByAdministrator request
         * @return AdministratorOrganizationLink
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public AdministratorOrganizationLink execute() throws ApiException {
            AdministratorOrganizationLinkReq administratorOrganizationLinkReq = buildBodyParams();
            ApiResponse<AdministratorOrganizationLink> localVarResp = createByAdministratorWithHttpInfo(id, administratorOrganizationLinkReq);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute createByAdministrator request with HTTP info returned
         * @return ApiResponse&lt;AdministratorOrganizationLink&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<AdministratorOrganizationLink> executeWithHttpInfo() throws ApiException {
            AdministratorOrganizationLinkReq administratorOrganizationLinkReq = buildBodyParams();
            return createByAdministratorWithHttpInfo(id, administratorOrganizationLinkReq);
        }

        /**
         * Execute createByAdministrator request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AdministratorOrganizationLink> _callback) throws ApiException {
            AdministratorOrganizationLinkReq administratorOrganizationLinkReq = buildBodyParams();
            return createByAdministratorAsync(id, administratorOrganizationLinkReq, _callback);
        }
    }

    /**
     * Allow Adminstrator access to an Organization.
     * This endpoint allows you to grant Administrator access to an Organization.
     * @param id  (required)
     * @return CreateByAdministratorRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public CreateByAdministratorRequestBuilder createByAdministrator(String id) throws IllegalArgumentException {
        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new CreateByAdministratorRequestBuilder(id);
    }
    private okhttp3.Call createOrgCall(String providerId, CreateOrganization createOrganization, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = createOrganization;

        // create path and map variables
        String localVarPath = "/providers/{provider_id}/organizations"
            .replace("{" + "provider_id" + "}", localVarApiClient.escapeString(providerId.toString()));

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
    private okhttp3.Call createOrgValidateBeforeCall(String providerId, CreateOrganization createOrganization, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'providerId' is set
        if (providerId == null) {
            throw new ApiException("Missing the required parameter 'providerId' when calling createOrg(Async)");
        }

        return createOrgCall(providerId, createOrganization, _callback);

    }


    private ApiResponse<Organization> createOrgWithHttpInfo(String providerId, CreateOrganization createOrganization) throws ApiException {
        okhttp3.Call localVarCall = createOrgValidateBeforeCall(providerId, createOrganization, null);
        Type localVarReturnType = new TypeToken<Organization>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call createOrgAsync(String providerId, CreateOrganization createOrganization, final ApiCallback<Organization> _callback) throws ApiException {

        okhttp3.Call localVarCall = createOrgValidateBeforeCall(providerId, createOrganization, _callback);
        Type localVarReturnType = new TypeToken<Organization>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class CreateOrgRequestBuilder {
        private final String providerId;
        private Integer maxSystemUsers;
        private String name;

        private CreateOrgRequestBuilder(String providerId) {
            this.providerId = providerId;
        }

        /**
         * Set maxSystemUsers
         * @param maxSystemUsers The maximum number of users allowed in this organization. Requires organizations.billing scope to modify. (optional)
         * @return CreateOrgRequestBuilder
         */
        public CreateOrgRequestBuilder maxSystemUsers(Integer maxSystemUsers) {
            this.maxSystemUsers = maxSystemUsers;
            return this;
        }
        
        /**
         * Set name
         * @param name  (optional)
         * @return CreateOrgRequestBuilder
         */
        public CreateOrgRequestBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        /**
         * Build call for createOrg
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> CREATED </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            CreateOrganization createOrganization = buildBodyParams();
            return createOrgCall(providerId, createOrganization, _callback);
        }

        private CreateOrganization buildBodyParams() {
            CreateOrganization createOrganization = new CreateOrganization();
            createOrganization.maxSystemUsers(this.maxSystemUsers);
            createOrganization.name(this.name);
            return createOrganization;
        }

        /**
         * Execute createOrg request
         * @return Organization
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> CREATED </td><td>  -  </td></tr>
         </table>
         */
        public Organization execute() throws ApiException {
            CreateOrganization createOrganization = buildBodyParams();
            ApiResponse<Organization> localVarResp = createOrgWithHttpInfo(providerId, createOrganization);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute createOrg request with HTTP info returned
         * @return ApiResponse&lt;Organization&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> CREATED </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Organization> executeWithHttpInfo() throws ApiException {
            CreateOrganization createOrganization = buildBodyParams();
            return createOrgWithHttpInfo(providerId, createOrganization);
        }

        /**
         * Execute createOrg request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> CREATED </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Organization> _callback) throws ApiException {
            CreateOrganization createOrganization = buildBodyParams();
            return createOrgAsync(providerId, createOrganization, _callback);
        }
    }

    /**
     * Create Provider Organization
     * This endpoint creates a new organization under the provider
     * @param providerId  (required)
     * @return CreateOrgRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td> CREATED </td><td>  -  </td></tr>
     </table>
     */
    public CreateOrgRequestBuilder createOrg(String providerId) throws IllegalArgumentException {
        if (providerId == null) throw new IllegalArgumentException("\"providerId\" is required but got null");
            

        return new CreateOrgRequestBuilder(providerId);
    }
    private okhttp3.Call deleteCall(String providerId, String id, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/providers/{provider_id}/policygrouptemplates/{id}"
            .replace("{" + "provider_id" + "}", localVarApiClient.escapeString(providerId.toString()))
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
        return localVarApiClient.buildCall(basePath, localVarPath, "DELETE", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call deleteValidateBeforeCall(String providerId, String id, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'providerId' is set
        if (providerId == null) {
            throw new ApiException("Missing the required parameter 'providerId' when calling delete(Async)");
        }

        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling delete(Async)");
        }

        return deleteCall(providerId, id, _callback);

    }


    private ApiResponse<Void> deleteWithHttpInfo(String providerId, String id) throws ApiException {
        okhttp3.Call localVarCall = deleteValidateBeforeCall(providerId, id, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call deleteAsync(String providerId, String id, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteValidateBeforeCall(providerId, id, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DeleteRequestBuilder {
        private final String providerId;
        private final String id;

        private DeleteRequestBuilder(String providerId, String id) {
            this.providerId = providerId;
            this.id = id;
        }

        /**
         * Build call for delete
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> NO_CONTENT </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return deleteCall(providerId, id, _callback);
        }


        /**
         * Execute delete request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> NO_CONTENT </td><td>  -  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            deleteWithHttpInfo(providerId, id);
        }

        /**
         * Execute delete request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> NO_CONTENT </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            return deleteWithHttpInfo(providerId, id);
        }

        /**
         * Execute delete request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> NO_CONTENT </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Void> _callback) throws ApiException {
            return deleteAsync(providerId, id, _callback);
        }
    }

    /**
     * Deletes policy group template.
     * Deletes a Policy Group Template.
     * @param providerId  (required)
     * @param id  (required)
     * @return DeleteRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td> NO_CONTENT </td><td>  -  </td></tr>
     </table>
     */
    public DeleteRequestBuilder delete(String providerId, String id) throws IllegalArgumentException {
        if (providerId == null) throw new IllegalArgumentException("\"providerId\" is required but got null");
            

        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new DeleteRequestBuilder(providerId, id);
    }
    private okhttp3.Call getCall(String providerId, String id, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/providers/{provider_id}/policygrouptemplates/{id}"
            .replace("{" + "provider_id" + "}", localVarApiClient.escapeString(providerId.toString()))
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
    private okhttp3.Call getValidateBeforeCall(String providerId, String id, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'providerId' is set
        if (providerId == null) {
            throw new ApiException("Missing the required parameter 'providerId' when calling get(Async)");
        }

        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling get(Async)");
        }

        return getCall(providerId, id, _callback);

    }


    private ApiResponse<PolicyGroupTemplate> getWithHttpInfo(String providerId, String id) throws ApiException {
        okhttp3.Call localVarCall = getValidateBeforeCall(providerId, id, null);
        Type localVarReturnType = new TypeToken<PolicyGroupTemplate>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getAsync(String providerId, String id, final ApiCallback<PolicyGroupTemplate> _callback) throws ApiException {

        okhttp3.Call localVarCall = getValidateBeforeCall(providerId, id, _callback);
        Type localVarReturnType = new TypeToken<PolicyGroupTemplate>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetRequestBuilder {
        private final String providerId;
        private final String id;

        private GetRequestBuilder(String providerId, String id) {
            this.providerId = providerId;
            this.id = id;
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
            return getCall(providerId, id, _callback);
        }


        /**
         * Execute get request
         * @return PolicyGroupTemplate
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public PolicyGroupTemplate execute() throws ApiException {
            ApiResponse<PolicyGroupTemplate> localVarResp = getWithHttpInfo(providerId, id);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute get request with HTTP info returned
         * @return ApiResponse&lt;PolicyGroupTemplate&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<PolicyGroupTemplate> executeWithHttpInfo() throws ApiException {
            return getWithHttpInfo(providerId, id);
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
        public okhttp3.Call executeAsync(final ApiCallback<PolicyGroupTemplate> _callback) throws ApiException {
            return getAsync(providerId, id, _callback);
        }
    }

    /**
     * Gets a provider&#39;s policy group template.
     * Retrieves a Policy Group Template for this provider.
     * @param providerId  (required)
     * @param id  (required)
     * @return GetRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public GetRequestBuilder get(String providerId, String id) throws IllegalArgumentException {
        if (providerId == null) throw new IllegalArgumentException("\"providerId\" is required but got null");
            

        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new GetRequestBuilder(providerId, id);
    }
    private okhttp3.Call getConfiguredPolicyTemplateCall(String providerId, String id, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/providers/{provider_id}/configuredpolicytemplates/{id}"
            .replace("{" + "provider_id" + "}", localVarApiClient.escapeString(providerId.toString()))
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
    private okhttp3.Call getConfiguredPolicyTemplateValidateBeforeCall(String providerId, String id, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'providerId' is set
        if (providerId == null) {
            throw new ApiException("Missing the required parameter 'providerId' when calling getConfiguredPolicyTemplate(Async)");
        }

        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getConfiguredPolicyTemplate(Async)");
        }

        return getConfiguredPolicyTemplateCall(providerId, id, _callback);

    }


    private ApiResponse<Object> getConfiguredPolicyTemplateWithHttpInfo(String providerId, String id) throws ApiException {
        okhttp3.Call localVarCall = getConfiguredPolicyTemplateValidateBeforeCall(providerId, id, null);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getConfiguredPolicyTemplateAsync(String providerId, String id, final ApiCallback<Object> _callback) throws ApiException {

        okhttp3.Call localVarCall = getConfiguredPolicyTemplateValidateBeforeCall(providerId, id, _callback);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetConfiguredPolicyTemplateRequestBuilder {
        private final String providerId;
        private final String id;

        private GetConfiguredPolicyTemplateRequestBuilder(String providerId, String id) {
            this.providerId = providerId;
            this.id = id;
        }

        /**
         * Build call for getConfiguredPolicyTemplate
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
            return getConfiguredPolicyTemplateCall(providerId, id, _callback);
        }


        /**
         * Execute getConfiguredPolicyTemplate request
         * @return Object
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public Object execute() throws ApiException {
            ApiResponse<Object> localVarResp = getConfiguredPolicyTemplateWithHttpInfo(providerId, id);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getConfiguredPolicyTemplate request with HTTP info returned
         * @return ApiResponse&lt;Object&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Object> executeWithHttpInfo() throws ApiException {
            return getConfiguredPolicyTemplateWithHttpInfo(providerId, id);
        }

        /**
         * Execute getConfiguredPolicyTemplate request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Object> _callback) throws ApiException {
            return getConfiguredPolicyTemplateAsync(providerId, id, _callback);
        }
    }

    /**
     * Retrieve a configured policy template by id.
     * Retrieves a Configured Policy Templates for this provider and Id.
     * @param providerId  (required)
     * @param id  (required)
     * @return GetConfiguredPolicyTemplateRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public GetConfiguredPolicyTemplateRequestBuilder getConfiguredPolicyTemplate(String providerId, String id) throws IllegalArgumentException {
        if (providerId == null) throw new IllegalArgumentException("\"providerId\" is required but got null");
            

        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new GetConfiguredPolicyTemplateRequestBuilder(providerId, id);
    }
    private okhttp3.Call getProviderCall(String providerId, List<String> fields, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/providers/{provider_id}"
            .replace("{" + "provider_id" + "}", localVarApiClient.escapeString(providerId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (fields != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "fields", fields));
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
    private okhttp3.Call getProviderValidateBeforeCall(String providerId, List<String> fields, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'providerId' is set
        if (providerId == null) {
            throw new ApiException("Missing the required parameter 'providerId' when calling getProvider(Async)");
        }

        return getProviderCall(providerId, fields, _callback);

    }


    private ApiResponse<Provider> getProviderWithHttpInfo(String providerId, List<String> fields) throws ApiException {
        okhttp3.Call localVarCall = getProviderValidateBeforeCall(providerId, fields, null);
        Type localVarReturnType = new TypeToken<Provider>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getProviderAsync(String providerId, List<String> fields, final ApiCallback<Provider> _callback) throws ApiException {

        okhttp3.Call localVarCall = getProviderValidateBeforeCall(providerId, fields, _callback);
        Type localVarReturnType = new TypeToken<Provider>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetProviderRequestBuilder {
        private final String providerId;
        private List<String> fields;

        private GetProviderRequestBuilder(String providerId) {
            this.providerId = providerId;
        }

        /**
         * Set fields
         * @param fields The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  (optional)
         * @return GetProviderRequestBuilder
         */
        public GetProviderRequestBuilder fields(List<String> fields) {
            this.fields = fields;
            return this;
        }
        
        /**
         * Build call for getProvider
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
            return getProviderCall(providerId, fields, _callback);
        }


        /**
         * Execute getProvider request
         * @return Provider
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public Provider execute() throws ApiException {
            ApiResponse<Provider> localVarResp = getProviderWithHttpInfo(providerId, fields);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getProvider request with HTTP info returned
         * @return ApiResponse&lt;Provider&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Provider> executeWithHttpInfo() throws ApiException {
            return getProviderWithHttpInfo(providerId, fields);
        }

        /**
         * Execute getProvider request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Provider> _callback) throws ApiException {
            return getProviderAsync(providerId, fields, _callback);
        }
    }

    /**
     * Retrieve Provider
     * This endpoint returns details about a provider
     * @param providerId  (required)
     * @return GetProviderRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public GetProviderRequestBuilder getProvider(String providerId) throws IllegalArgumentException {
        if (providerId == null) throw new IllegalArgumentException("\"providerId\" is required but got null");
            

        return new GetProviderRequestBuilder(providerId);
    }
    private okhttp3.Call listCall(String providerId, List<String> fields, Integer skip, List<String> sort, Integer limit, List<String> filter, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/providers/{provider_id}/policygrouptemplates"
            .replace("{" + "provider_id" + "}", localVarApiClient.escapeString(providerId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (fields != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "fields", fields));
        }

        if (skip != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("skip", skip));
        }

        if (sort != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "sort", sort));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
        }

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
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
    private okhttp3.Call listValidateBeforeCall(String providerId, List<String> fields, Integer skip, List<String> sort, Integer limit, List<String> filter, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'providerId' is set
        if (providerId == null) {
            throw new ApiException("Missing the required parameter 'providerId' when calling list(Async)");
        }

        return listCall(providerId, fields, skip, sort, limit, filter, _callback);

    }


    private ApiResponse<PolicyGroupTemplates> listWithHttpInfo(String providerId, List<String> fields, Integer skip, List<String> sort, Integer limit, List<String> filter) throws ApiException {
        okhttp3.Call localVarCall = listValidateBeforeCall(providerId, fields, skip, sort, limit, filter, null);
        Type localVarReturnType = new TypeToken<PolicyGroupTemplates>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listAsync(String providerId, List<String> fields, Integer skip, List<String> sort, Integer limit, List<String> filter, final ApiCallback<PolicyGroupTemplates> _callback) throws ApiException {

        okhttp3.Call localVarCall = listValidateBeforeCall(providerId, fields, skip, sort, limit, filter, _callback);
        Type localVarReturnType = new TypeToken<PolicyGroupTemplates>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListRequestBuilder {
        private final String providerId;
        private List<String> fields;
        private Integer skip;
        private List<String> sort;
        private Integer limit;
        private List<String> filter;

        private ListRequestBuilder(String providerId) {
            this.providerId = providerId;
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
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return ListRequestBuilder
         */
        public ListRequestBuilder limit(Integer limit) {
            this.limit = limit;
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
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listCall(providerId, fields, skip, sort, limit, filter, _callback);
        }


        /**
         * Execute list request
         * @return PolicyGroupTemplates
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public PolicyGroupTemplates execute() throws ApiException {
            ApiResponse<PolicyGroupTemplates> localVarResp = listWithHttpInfo(providerId, fields, skip, sort, limit, filter);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute list request with HTTP info returned
         * @return ApiResponse&lt;PolicyGroupTemplates&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<PolicyGroupTemplates> executeWithHttpInfo() throws ApiException {
            return listWithHttpInfo(providerId, fields, skip, sort, limit, filter);
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
        public okhttp3.Call executeAsync(final ApiCallback<PolicyGroupTemplates> _callback) throws ApiException {
            return listAsync(providerId, fields, skip, sort, limit, filter, _callback);
        }
    }

    /**
     * List a provider&#39;s policy group templates.
     * Retrieves a list of Policy Group Templates for this provider.
     * @param providerId  (required)
     * @return ListRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public ListRequestBuilder list(String providerId) throws IllegalArgumentException {
        if (providerId == null) throw new IllegalArgumentException("\"providerId\" is required but got null");
            

        return new ListRequestBuilder(providerId);
    }
    private okhttp3.Call listAdministratorsCall(String providerId, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, List<String> sortIgnoreCase, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/providers/{provider_id}/administrators"
            .replace("{" + "provider_id" + "}", localVarApiClient.escapeString(providerId.toString()));

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

        if (sortIgnoreCase != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "sortIgnoreCase", sortIgnoreCase));
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
    private okhttp3.Call listAdministratorsValidateBeforeCall(String providerId, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, List<String> sortIgnoreCase, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'providerId' is set
        if (providerId == null) {
            throw new ApiException("Missing the required parameter 'providerId' when calling listAdministrators(Async)");
        }

        return listAdministratorsCall(providerId, fields, filter, limit, skip, sort, sortIgnoreCase, _callback);

    }


    private ApiResponse<ProvidersListAdministratorsResponse> listAdministratorsWithHttpInfo(String providerId, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, List<String> sortIgnoreCase) throws ApiException {
        okhttp3.Call localVarCall = listAdministratorsValidateBeforeCall(providerId, fields, filter, limit, skip, sort, sortIgnoreCase, null);
        Type localVarReturnType = new TypeToken<ProvidersListAdministratorsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listAdministratorsAsync(String providerId, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, List<String> sortIgnoreCase, final ApiCallback<ProvidersListAdministratorsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listAdministratorsValidateBeforeCall(providerId, fields, filter, limit, skip, sort, sortIgnoreCase, _callback);
        Type localVarReturnType = new TypeToken<ProvidersListAdministratorsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListAdministratorsRequestBuilder {
        private final String providerId;
        private List<String> fields;
        private List<String> filter;
        private Integer limit;
        private Integer skip;
        private List<String> sort;
        private List<String> sortIgnoreCase;

        private ListAdministratorsRequestBuilder(String providerId) {
            this.providerId = providerId;
        }

        /**
         * Set fields
         * @param fields The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  (optional)
         * @return ListAdministratorsRequestBuilder
         */
        public ListAdministratorsRequestBuilder fields(List<String> fields) {
            this.fields = fields;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return ListAdministratorsRequestBuilder
         */
        public ListAdministratorsRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return ListAdministratorsRequestBuilder
         */
        public ListAdministratorsRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListAdministratorsRequestBuilder
         */
        public ListAdministratorsRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  (optional)
         * @return ListAdministratorsRequestBuilder
         */
        public ListAdministratorsRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set sortIgnoreCase
         * @param sortIgnoreCase The comma separated fields used to sort the collection, ignoring case. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  (optional)
         * @return ListAdministratorsRequestBuilder
         */
        public ListAdministratorsRequestBuilder sortIgnoreCase(List<String> sortIgnoreCase) {
            this.sortIgnoreCase = sortIgnoreCase;
            return this;
        }
        
        /**
         * Build call for listAdministrators
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
            return listAdministratorsCall(providerId, fields, filter, limit, skip, sort, sortIgnoreCase, _callback);
        }


        /**
         * Execute listAdministrators request
         * @return ProvidersListAdministratorsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ProvidersListAdministratorsResponse execute() throws ApiException {
            ApiResponse<ProvidersListAdministratorsResponse> localVarResp = listAdministratorsWithHttpInfo(providerId, fields, filter, limit, skip, sort, sortIgnoreCase);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listAdministrators request with HTTP info returned
         * @return ApiResponse&lt;ProvidersListAdministratorsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<ProvidersListAdministratorsResponse> executeWithHttpInfo() throws ApiException {
            return listAdministratorsWithHttpInfo(providerId, fields, filter, limit, skip, sort, sortIgnoreCase);
        }

        /**
         * Execute listAdministrators request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ProvidersListAdministratorsResponse> _callback) throws ApiException {
            return listAdministratorsAsync(providerId, fields, filter, limit, skip, sort, sortIgnoreCase, _callback);
        }
    }

    /**
     * List Provider Administrators
     * This endpoint returns a list of the Administrators associated with the Provider. You must be associated with the provider to use this route.
     * @param providerId  (required)
     * @return ListAdministratorsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListAdministratorsRequestBuilder listAdministrators(String providerId) throws IllegalArgumentException {
        if (providerId == null) throw new IllegalArgumentException("\"providerId\" is required but got null");
            

        return new ListAdministratorsRequestBuilder(providerId);
    }
    private okhttp3.Call listByAdministratorCall(String id, Integer limit, Integer skip, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/administrators/{id}/organizationlinks"
            .replace("{" + "id" + "}", localVarApiClient.escapeString(id.toString()));

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
    private okhttp3.Call listByAdministratorValidateBeforeCall(String id, Integer limit, Integer skip, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling listByAdministrator(Async)");
        }

        return listByAdministratorCall(id, limit, skip, _callback);

    }


    private ApiResponse<List<AdministratorOrganizationLink>> listByAdministratorWithHttpInfo(String id, Integer limit, Integer skip) throws ApiException {
        okhttp3.Call localVarCall = listByAdministratorValidateBeforeCall(id, limit, skip, null);
        Type localVarReturnType = new TypeToken<List<AdministratorOrganizationLink>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listByAdministratorAsync(String id, Integer limit, Integer skip, final ApiCallback<List<AdministratorOrganizationLink>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listByAdministratorValidateBeforeCall(id, limit, skip, _callback);
        Type localVarReturnType = new TypeToken<List<AdministratorOrganizationLink>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListByAdministratorRequestBuilder {
        private final String id;
        private Integer limit;
        private Integer skip;

        private ListByAdministratorRequestBuilder(String id) {
            this.id = id;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return ListByAdministratorRequestBuilder
         */
        public ListByAdministratorRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListByAdministratorRequestBuilder
         */
        public ListByAdministratorRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Build call for listByAdministrator
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
            return listByAdministratorCall(id, limit, skip, _callback);
        }


        /**
         * Execute listByAdministrator request
         * @return List&lt;AdministratorOrganizationLink&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public List<AdministratorOrganizationLink> execute() throws ApiException {
            ApiResponse<List<AdministratorOrganizationLink>> localVarResp = listByAdministratorWithHttpInfo(id, limit, skip);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listByAdministrator request with HTTP info returned
         * @return ApiResponse&lt;List&lt;AdministratorOrganizationLink&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<AdministratorOrganizationLink>> executeWithHttpInfo() throws ApiException {
            return listByAdministratorWithHttpInfo(id, limit, skip);
        }

        /**
         * Execute listByAdministrator request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<AdministratorOrganizationLink>> _callback) throws ApiException {
            return listByAdministratorAsync(id, limit, skip, _callback);
        }
    }

    /**
     * List the association links between an Administrator and Organizations.
     * This endpoint returns the association links between an Administrator and Organizations.
     * @param id  (required)
     * @return ListByAdministratorRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public ListByAdministratorRequestBuilder listByAdministrator(String id) throws IllegalArgumentException {
        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new ListByAdministratorRequestBuilder(id);
    }
    private okhttp3.Call listByOrganizationCall(String id, Integer limit, Integer skip, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/organizations/{id}/administratorlinks"
            .replace("{" + "id" + "}", localVarApiClient.escapeString(id.toString()));

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
    private okhttp3.Call listByOrganizationValidateBeforeCall(String id, Integer limit, Integer skip, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling listByOrganization(Async)");
        }

        return listByOrganizationCall(id, limit, skip, _callback);

    }


    private ApiResponse<List<AdministratorOrganizationLink>> listByOrganizationWithHttpInfo(String id, Integer limit, Integer skip) throws ApiException {
        okhttp3.Call localVarCall = listByOrganizationValidateBeforeCall(id, limit, skip, null);
        Type localVarReturnType = new TypeToken<List<AdministratorOrganizationLink>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listByOrganizationAsync(String id, Integer limit, Integer skip, final ApiCallback<List<AdministratorOrganizationLink>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listByOrganizationValidateBeforeCall(id, limit, skip, _callback);
        Type localVarReturnType = new TypeToken<List<AdministratorOrganizationLink>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListByOrganizationRequestBuilder {
        private final String id;
        private Integer limit;
        private Integer skip;

        private ListByOrganizationRequestBuilder(String id) {
            this.id = id;
        }

        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return ListByOrganizationRequestBuilder
         */
        public ListByOrganizationRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListByOrganizationRequestBuilder
         */
        public ListByOrganizationRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Build call for listByOrganization
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
            return listByOrganizationCall(id, limit, skip, _callback);
        }


        /**
         * Execute listByOrganization request
         * @return List&lt;AdministratorOrganizationLink&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<AdministratorOrganizationLink> execute() throws ApiException {
            ApiResponse<List<AdministratorOrganizationLink>> localVarResp = listByOrganizationWithHttpInfo(id, limit, skip);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listByOrganization request with HTTP info returned
         * @return ApiResponse&lt;List&lt;AdministratorOrganizationLink&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<AdministratorOrganizationLink>> executeWithHttpInfo() throws ApiException {
            return listByOrganizationWithHttpInfo(id, limit, skip);
        }

        /**
         * Execute listByOrganization request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<AdministratorOrganizationLink>> _callback) throws ApiException {
            return listByOrganizationAsync(id, limit, skip, _callback);
        }
    }

    /**
     * List the association links between an Organization and Administrators.
     * This endpoint returns the association links between an Organization and Administrators.
     * @param id  (required)
     * @return ListByOrganizationRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListByOrganizationRequestBuilder listByOrganization(String id) throws IllegalArgumentException {
        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new ListByOrganizationRequestBuilder(id);
    }
    private okhttp3.Call listConfiguredPolicyTemplatesCall(String providerId, Integer skip, List<String> sort, Integer limit, List<String> filter, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/providers/{provider_id}/configuredpolicytemplates"
            .replace("{" + "provider_id" + "}", localVarApiClient.escapeString(providerId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (skip != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("skip", skip));
        }

        if (sort != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "sort", sort));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
        }

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
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
    private okhttp3.Call listConfiguredPolicyTemplatesValidateBeforeCall(String providerId, Integer skip, List<String> sort, Integer limit, List<String> filter, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'providerId' is set
        if (providerId == null) {
            throw new ApiException("Missing the required parameter 'providerId' when calling listConfiguredPolicyTemplates(Async)");
        }

        return listConfiguredPolicyTemplatesCall(providerId, skip, sort, limit, filter, _callback);

    }


    private ApiResponse<PolicyGroupTemplatesListConfiguredPolicyTemplatesResponse> listConfiguredPolicyTemplatesWithHttpInfo(String providerId, Integer skip, List<String> sort, Integer limit, List<String> filter) throws ApiException {
        okhttp3.Call localVarCall = listConfiguredPolicyTemplatesValidateBeforeCall(providerId, skip, sort, limit, filter, null);
        Type localVarReturnType = new TypeToken<PolicyGroupTemplatesListConfiguredPolicyTemplatesResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listConfiguredPolicyTemplatesAsync(String providerId, Integer skip, List<String> sort, Integer limit, List<String> filter, final ApiCallback<PolicyGroupTemplatesListConfiguredPolicyTemplatesResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listConfiguredPolicyTemplatesValidateBeforeCall(providerId, skip, sort, limit, filter, _callback);
        Type localVarReturnType = new TypeToken<PolicyGroupTemplatesListConfiguredPolicyTemplatesResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListConfiguredPolicyTemplatesRequestBuilder {
        private final String providerId;
        private Integer skip;
        private List<String> sort;
        private Integer limit;
        private List<String> filter;

        private ListConfiguredPolicyTemplatesRequestBuilder(String providerId) {
            this.providerId = providerId;
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListConfiguredPolicyTemplatesRequestBuilder
         */
        public ListConfiguredPolicyTemplatesRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  (optional)
         * @return ListConfiguredPolicyTemplatesRequestBuilder
         */
        public ListConfiguredPolicyTemplatesRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return ListConfiguredPolicyTemplatesRequestBuilder
         */
        public ListConfiguredPolicyTemplatesRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return ListConfiguredPolicyTemplatesRequestBuilder
         */
        public ListConfiguredPolicyTemplatesRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Build call for listConfiguredPolicyTemplates
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
            return listConfiguredPolicyTemplatesCall(providerId, skip, sort, limit, filter, _callback);
        }


        /**
         * Execute listConfiguredPolicyTemplates request
         * @return PolicyGroupTemplatesListConfiguredPolicyTemplatesResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public PolicyGroupTemplatesListConfiguredPolicyTemplatesResponse execute() throws ApiException {
            ApiResponse<PolicyGroupTemplatesListConfiguredPolicyTemplatesResponse> localVarResp = listConfiguredPolicyTemplatesWithHttpInfo(providerId, skip, sort, limit, filter);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listConfiguredPolicyTemplates request with HTTP info returned
         * @return ApiResponse&lt;PolicyGroupTemplatesListConfiguredPolicyTemplatesResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<PolicyGroupTemplatesListConfiguredPolicyTemplatesResponse> executeWithHttpInfo() throws ApiException {
            return listConfiguredPolicyTemplatesWithHttpInfo(providerId, skip, sort, limit, filter);
        }

        /**
         * Execute listConfiguredPolicyTemplates request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<PolicyGroupTemplatesListConfiguredPolicyTemplatesResponse> _callback) throws ApiException {
            return listConfiguredPolicyTemplatesAsync(providerId, skip, sort, limit, filter, _callback);
        }
    }

    /**
     * List a provider&#39;s configured policy templates.
     * Retrieves a list of Configured Policy Templates for this provider.
     * @param providerId  (required)
     * @return ListConfiguredPolicyTemplatesRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public ListConfiguredPolicyTemplatesRequestBuilder listConfiguredPolicyTemplates(String providerId) throws IllegalArgumentException {
        if (providerId == null) throw new IllegalArgumentException("\"providerId\" is required but got null");
            

        return new ListConfiguredPolicyTemplatesRequestBuilder(providerId);
    }
    private okhttp3.Call listMembersCall(String providerId, String id, Integer skip, List<String> sort, Integer limit, List<String> filter, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/providers/{provider_id}/policygrouptemplates/{id}/members"
            .replace("{" + "provider_id" + "}", localVarApiClient.escapeString(providerId.toString()))
            .replace("{" + "id" + "}", localVarApiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (skip != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("skip", skip));
        }

        if (sort != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "sort", sort));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
        }

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
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
    private okhttp3.Call listMembersValidateBeforeCall(String providerId, String id, Integer skip, List<String> sort, Integer limit, List<String> filter, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'providerId' is set
        if (providerId == null) {
            throw new ApiException("Missing the required parameter 'providerId' when calling listMembers(Async)");
        }

        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling listMembers(Async)");
        }

        return listMembersCall(providerId, id, skip, sort, limit, filter, _callback);

    }


    private ApiResponse<PolicyGroupTemplateMembers> listMembersWithHttpInfo(String providerId, String id, Integer skip, List<String> sort, Integer limit, List<String> filter) throws ApiException {
        okhttp3.Call localVarCall = listMembersValidateBeforeCall(providerId, id, skip, sort, limit, filter, null);
        Type localVarReturnType = new TypeToken<PolicyGroupTemplateMembers>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listMembersAsync(String providerId, String id, Integer skip, List<String> sort, Integer limit, List<String> filter, final ApiCallback<PolicyGroupTemplateMembers> _callback) throws ApiException {

        okhttp3.Call localVarCall = listMembersValidateBeforeCall(providerId, id, skip, sort, limit, filter, _callback);
        Type localVarReturnType = new TypeToken<PolicyGroupTemplateMembers>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListMembersRequestBuilder {
        private final String providerId;
        private final String id;
        private Integer skip;
        private List<String> sort;
        private Integer limit;
        private List<String> filter;

        private ListMembersRequestBuilder(String providerId, String id) {
            this.providerId = providerId;
            this.id = id;
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListMembersRequestBuilder
         */
        public ListMembersRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  (optional)
         * @return ListMembersRequestBuilder
         */
        public ListMembersRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return ListMembersRequestBuilder
         */
        public ListMembersRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return ListMembersRequestBuilder
         */
        public ListMembersRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Build call for listMembers
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
            return listMembersCall(providerId, id, skip, sort, limit, filter, _callback);
        }


        /**
         * Execute listMembers request
         * @return PolicyGroupTemplateMembers
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public PolicyGroupTemplateMembers execute() throws ApiException {
            ApiResponse<PolicyGroupTemplateMembers> localVarResp = listMembersWithHttpInfo(providerId, id, skip, sort, limit, filter);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listMembers request with HTTP info returned
         * @return ApiResponse&lt;PolicyGroupTemplateMembers&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<PolicyGroupTemplateMembers> executeWithHttpInfo() throws ApiException {
            return listMembersWithHttpInfo(providerId, id, skip, sort, limit, filter);
        }

        /**
         * Execute listMembers request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<PolicyGroupTemplateMembers> _callback) throws ApiException {
            return listMembersAsync(providerId, id, skip, sort, limit, filter, _callback);
        }
    }

    /**
     * Gets the list of members from a policy group template.
     * Retrieves a Policy Group Template&#39;s Members.
     * @param providerId  (required)
     * @param id  (required)
     * @return ListMembersRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public ListMembersRequestBuilder listMembers(String providerId, String id) throws IllegalArgumentException {
        if (providerId == null) throw new IllegalArgumentException("\"providerId\" is required but got null");
            

        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new ListMembersRequestBuilder(providerId, id);
    }
    private okhttp3.Call listOrganizationsCall(String providerId, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, List<String> sortIgnoreCase, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/providers/{provider_id}/organizations"
            .replace("{" + "provider_id" + "}", localVarApiClient.escapeString(providerId.toString()));

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

        if (sortIgnoreCase != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "sortIgnoreCase", sortIgnoreCase));
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
    private okhttp3.Call listOrganizationsValidateBeforeCall(String providerId, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, List<String> sortIgnoreCase, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'providerId' is set
        if (providerId == null) {
            throw new ApiException("Missing the required parameter 'providerId' when calling listOrganizations(Async)");
        }

        return listOrganizationsCall(providerId, fields, filter, limit, skip, sort, sortIgnoreCase, _callback);

    }


    private ApiResponse<ProvidersListOrganizationsResponse> listOrganizationsWithHttpInfo(String providerId, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, List<String> sortIgnoreCase) throws ApiException {
        okhttp3.Call localVarCall = listOrganizationsValidateBeforeCall(providerId, fields, filter, limit, skip, sort, sortIgnoreCase, null);
        Type localVarReturnType = new TypeToken<ProvidersListOrganizationsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listOrganizationsAsync(String providerId, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, List<String> sortIgnoreCase, final ApiCallback<ProvidersListOrganizationsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listOrganizationsValidateBeforeCall(providerId, fields, filter, limit, skip, sort, sortIgnoreCase, _callback);
        Type localVarReturnType = new TypeToken<ProvidersListOrganizationsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListOrganizationsRequestBuilder {
        private final String providerId;
        private List<String> fields;
        private List<String> filter;
        private Integer limit;
        private Integer skip;
        private List<String> sort;
        private List<String> sortIgnoreCase;

        private ListOrganizationsRequestBuilder(String providerId) {
            this.providerId = providerId;
        }

        /**
         * Set fields
         * @param fields The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  (optional)
         * @return ListOrganizationsRequestBuilder
         */
        public ListOrganizationsRequestBuilder fields(List<String> fields) {
            this.fields = fields;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return ListOrganizationsRequestBuilder
         */
        public ListOrganizationsRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return ListOrganizationsRequestBuilder
         */
        public ListOrganizationsRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListOrganizationsRequestBuilder
         */
        public ListOrganizationsRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  (optional)
         * @return ListOrganizationsRequestBuilder
         */
        public ListOrganizationsRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set sortIgnoreCase
         * @param sortIgnoreCase The comma separated fields used to sort the collection, ignoring case. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  (optional)
         * @return ListOrganizationsRequestBuilder
         */
        public ListOrganizationsRequestBuilder sortIgnoreCase(List<String> sortIgnoreCase) {
            this.sortIgnoreCase = sortIgnoreCase;
            return this;
        }
        
        /**
         * Build call for listOrganizations
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
            return listOrganizationsCall(providerId, fields, filter, limit, skip, sort, sortIgnoreCase, _callback);
        }


        /**
         * Execute listOrganizations request
         * @return ProvidersListOrganizationsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ProvidersListOrganizationsResponse execute() throws ApiException {
            ApiResponse<ProvidersListOrganizationsResponse> localVarResp = listOrganizationsWithHttpInfo(providerId, fields, filter, limit, skip, sort, sortIgnoreCase);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listOrganizations request with HTTP info returned
         * @return ApiResponse&lt;ProvidersListOrganizationsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<ProvidersListOrganizationsResponse> executeWithHttpInfo() throws ApiException {
            return listOrganizationsWithHttpInfo(providerId, fields, filter, limit, skip, sort, sortIgnoreCase);
        }

        /**
         * Execute listOrganizations request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ProvidersListOrganizationsResponse> _callback) throws ApiException {
            return listOrganizationsAsync(providerId, fields, filter, limit, skip, sort, sortIgnoreCase, _callback);
        }
    }

    /**
     * List Provider Organizations
     * This endpoint returns a list of the Organizations associated with the Provider. You must be associated with the provider to use this route.
     * @param providerId  (required)
     * @return ListOrganizationsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListOrganizationsRequestBuilder listOrganizations(String providerId) throws IllegalArgumentException {
        if (providerId == null) throw new IllegalArgumentException("\"providerId\" is required but got null");
            

        return new ListOrganizationsRequestBuilder(providerId);
    }
    private okhttp3.Call postAdminsCall(String providerId, ProviderAdminReq providerAdminReq, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = providerAdminReq;

        // create path and map variables
        String localVarPath = "/providers/{provider_id}/administrators"
            .replace("{" + "provider_id" + "}", localVarApiClient.escapeString(providerId.toString()));

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
    private okhttp3.Call postAdminsValidateBeforeCall(String providerId, ProviderAdminReq providerAdminReq, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'providerId' is set
        if (providerId == null) {
            throw new ApiException("Missing the required parameter 'providerId' when calling postAdmins(Async)");
        }

        return postAdminsCall(providerId, providerAdminReq, _callback);

    }


    private ApiResponse<Administrator> postAdminsWithHttpInfo(String providerId, ProviderAdminReq providerAdminReq) throws ApiException {
        okhttp3.Call localVarCall = postAdminsValidateBeforeCall(providerId, providerAdminReq, null);
        Type localVarReturnType = new TypeToken<Administrator>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call postAdminsAsync(String providerId, ProviderAdminReq providerAdminReq, final ApiCallback<Administrator> _callback) throws ApiException {

        okhttp3.Call localVarCall = postAdminsValidateBeforeCall(providerId, providerAdminReq, _callback);
        Type localVarReturnType = new TypeToken<Administrator>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class PostAdminsRequestBuilder {
        private final String email;
        private final String providerId;
        private Boolean apiKeyAllowed;
        private Boolean bindNoOrgs;
        private Boolean enableMultiFactor;
        private String firstname;
        private String lastname;
        private String role;
        private String roleName;

        private PostAdminsRequestBuilder(String email, String providerId) {
            this.email = email;
            this.providerId = providerId;
        }

        /**
         * Set apiKeyAllowed
         * @param apiKeyAllowed  (optional)
         * @return PostAdminsRequestBuilder
         */
        public PostAdminsRequestBuilder apiKeyAllowed(Boolean apiKeyAllowed) {
            this.apiKeyAllowed = apiKeyAllowed;
            return this;
        }
        
        /**
         * Set bindNoOrgs
         * @param bindNoOrgs  (optional, default to false)
         * @return PostAdminsRequestBuilder
         */
        public PostAdminsRequestBuilder bindNoOrgs(Boolean bindNoOrgs) {
            this.bindNoOrgs = bindNoOrgs;
            return this;
        }
        
        /**
         * Set enableMultiFactor
         * @param enableMultiFactor  (optional)
         * @return PostAdminsRequestBuilder
         */
        public PostAdminsRequestBuilder enableMultiFactor(Boolean enableMultiFactor) {
            this.enableMultiFactor = enableMultiFactor;
            return this;
        }
        
        /**
         * Set firstname
         * @param firstname  (optional)
         * @return PostAdminsRequestBuilder
         */
        public PostAdminsRequestBuilder firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }
        
        /**
         * Set lastname
         * @param lastname  (optional)
         * @return PostAdminsRequestBuilder
         */
        public PostAdminsRequestBuilder lastname(String lastname) {
            this.lastname = lastname;
            return this;
        }
        
        /**
         * Set role
         * @param role  (optional)
         * @return PostAdminsRequestBuilder
         */
        public PostAdminsRequestBuilder role(String role) {
            this.role = role;
            return this;
        }
        
        /**
         * Set roleName
         * @param roleName  (optional)
         * @return PostAdminsRequestBuilder
         */
        public PostAdminsRequestBuilder roleName(String roleName) {
            this.roleName = roleName;
            return this;
        }
        
        /**
         * Build call for postAdmins
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
            ProviderAdminReq providerAdminReq = buildBodyParams();
            return postAdminsCall(providerId, providerAdminReq, _callback);
        }

        private ProviderAdminReq buildBodyParams() {
            ProviderAdminReq providerAdminReq = new ProviderAdminReq();
            providerAdminReq.apiKeyAllowed(this.apiKeyAllowed);
            providerAdminReq.bindNoOrgs(this.bindNoOrgs);
            providerAdminReq.email(this.email);
            providerAdminReq.enableMultiFactor(this.enableMultiFactor);
            providerAdminReq.firstname(this.firstname);
            providerAdminReq.lastname(this.lastname);
            providerAdminReq.role(this.role);
            providerAdminReq.roleName(this.roleName);
            return providerAdminReq;
        }

        /**
         * Execute postAdmins request
         * @return Administrator
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public Administrator execute() throws ApiException {
            ProviderAdminReq providerAdminReq = buildBodyParams();
            ApiResponse<Administrator> localVarResp = postAdminsWithHttpInfo(providerId, providerAdminReq);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute postAdmins request with HTTP info returned
         * @return ApiResponse&lt;Administrator&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Administrator> executeWithHttpInfo() throws ApiException {
            ProviderAdminReq providerAdminReq = buildBodyParams();
            return postAdminsWithHttpInfo(providerId, providerAdminReq);
        }

        /**
         * Execute postAdmins request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Administrator> _callback) throws ApiException {
            ProviderAdminReq providerAdminReq = buildBodyParams();
            return postAdminsAsync(providerId, providerAdminReq, _callback);
        }
    }

    /**
     * Create a new Provider Administrator
     * This endpoint allows you to create a provider administrator. You must be associated with the provider to use this route. You must provide either &#x60;role&#x60; or &#x60;roleName&#x60;.
     * @param providerId  (required)
     * @return PostAdminsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public PostAdminsRequestBuilder postAdmins(String email, String providerId) throws IllegalArgumentException {
        if (email == null) throw new IllegalArgumentException("\"email\" is required but got null");
            

        if (providerId == null) throw new IllegalArgumentException("\"providerId\" is required but got null");
            

        return new PostAdminsRequestBuilder(email, providerId);
    }
    private okhttp3.Call providerListCaseCall(String providerId, Integer skip, List<String> sort, Integer limit, List<String> filter, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/providers/{provider_id}/cases"
            .replace("{" + "provider_id" + "}", localVarApiClient.escapeString(providerId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (skip != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("skip", skip));
        }

        if (sort != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "sort", sort));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
        }

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
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
    private okhttp3.Call providerListCaseValidateBeforeCall(String providerId, Integer skip, List<String> sort, Integer limit, List<String> filter, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'providerId' is set
        if (providerId == null) {
            throw new ApiException("Missing the required parameter 'providerId' when calling providerListCase(Async)");
        }

        return providerListCaseCall(providerId, skip, sort, limit, filter, _callback);

    }


    private ApiResponse<CasesResponse> providerListCaseWithHttpInfo(String providerId, Integer skip, List<String> sort, Integer limit, List<String> filter) throws ApiException {
        okhttp3.Call localVarCall = providerListCaseValidateBeforeCall(providerId, skip, sort, limit, filter, null);
        Type localVarReturnType = new TypeToken<CasesResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call providerListCaseAsync(String providerId, Integer skip, List<String> sort, Integer limit, List<String> filter, final ApiCallback<CasesResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = providerListCaseValidateBeforeCall(providerId, skip, sort, limit, filter, _callback);
        Type localVarReturnType = new TypeToken<CasesResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ProviderListCaseRequestBuilder {
        private final String providerId;
        private Integer skip;
        private List<String> sort;
        private Integer limit;
        private List<String> filter;

        private ProviderListCaseRequestBuilder(String providerId) {
            this.providerId = providerId;
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ProviderListCaseRequestBuilder
         */
        public ProviderListCaseRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  (optional)
         * @return ProviderListCaseRequestBuilder
         */
        public ProviderListCaseRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return ProviderListCaseRequestBuilder
         */
        public ProviderListCaseRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return ProviderListCaseRequestBuilder
         */
        public ProviderListCaseRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Build call for providerListCase
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
            return providerListCaseCall(providerId, skip, sort, limit, filter, _callback);
        }


        /**
         * Execute providerListCase request
         * @return CasesResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public CasesResponse execute() throws ApiException {
            ApiResponse<CasesResponse> localVarResp = providerListCaseWithHttpInfo(providerId, skip, sort, limit, filter);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute providerListCase request with HTTP info returned
         * @return ApiResponse&lt;CasesResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<CasesResponse> executeWithHttpInfo() throws ApiException {
            return providerListCaseWithHttpInfo(providerId, skip, sort, limit, filter);
        }

        /**
         * Execute providerListCase request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<CasesResponse> _callback) throws ApiException {
            return providerListCaseAsync(providerId, skip, sort, limit, filter, _callback);
        }
    }

    /**
     * Get all cases (Support/Feature requests) for provider
     * This endpoint returns the cases (Support/Feature requests) for the provider
     * @param providerId  (required)
     * @return ProviderListCaseRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public ProviderListCaseRequestBuilder providerListCase(String providerId) throws IllegalArgumentException {
        if (providerId == null) throw new IllegalArgumentException("\"providerId\" is required but got null");
            

        return new ProviderListCaseRequestBuilder(providerId);
    }
    private okhttp3.Call removeByAdministratorCall(String administratorId, String id, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/administrators/{administrator_id}/organizationlinks/{id}"
            .replace("{" + "administrator_id" + "}", localVarApiClient.escapeString(administratorId.toString()))
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
        return localVarApiClient.buildCall(basePath, localVarPath, "DELETE", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call removeByAdministratorValidateBeforeCall(String administratorId, String id, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'administratorId' is set
        if (administratorId == null) {
            throw new ApiException("Missing the required parameter 'administratorId' when calling removeByAdministrator(Async)");
        }

        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling removeByAdministrator(Async)");
        }

        return removeByAdministratorCall(administratorId, id, _callback);

    }


    private ApiResponse<Void> removeByAdministratorWithHttpInfo(String administratorId, String id) throws ApiException {
        okhttp3.Call localVarCall = removeByAdministratorValidateBeforeCall(administratorId, id, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call removeByAdministratorAsync(String administratorId, String id, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = removeByAdministratorValidateBeforeCall(administratorId, id, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class RemoveByAdministratorRequestBuilder {
        private final String administratorId;
        private final String id;

        private RemoveByAdministratorRequestBuilder(String administratorId, String id) {
            this.administratorId = administratorId;
            this.id = id;
        }

        /**
         * Build call for removeByAdministrator
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
            return removeByAdministratorCall(administratorId, id, _callback);
        }


        /**
         * Execute removeByAdministrator request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            removeByAdministratorWithHttpInfo(administratorId, id);
        }

        /**
         * Execute removeByAdministrator request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            return removeByAdministratorWithHttpInfo(administratorId, id);
        }

        /**
         * Execute removeByAdministrator request (asynchronously)
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
            return removeByAdministratorAsync(administratorId, id, _callback);
        }
    }

    /**
     * Remove association between an Administrator and an Organization.
     * This endpoint removes the association link between an Administrator and an Organization.
     * @param administratorId  (required)
     * @param id  (required)
     * @return RemoveByAdministratorRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
     </table>
     */
    public RemoveByAdministratorRequestBuilder removeByAdministrator(String administratorId, String id) throws IllegalArgumentException {
        if (administratorId == null) throw new IllegalArgumentException("\"administratorId\" is required but got null");
            

        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new RemoveByAdministratorRequestBuilder(administratorId, id);
    }
    private okhttp3.Call retrieveInvoiceCall(String providerId, String ID, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/providers/{provider_id}/invoices/{ID}"
            .replace("{" + "provider_id" + "}", localVarApiClient.escapeString(providerId.toString()))
            .replace("{" + "ID" + "}", localVarApiClient.escapeString(ID.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/pdf",
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
    private okhttp3.Call retrieveInvoiceValidateBeforeCall(String providerId, String ID, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'providerId' is set
        if (providerId == null) {
            throw new ApiException("Missing the required parameter 'providerId' when calling retrieveInvoice(Async)");
        }

        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling retrieveInvoice(Async)");
        }

        return retrieveInvoiceCall(providerId, ID, _callback);

    }


    private ApiResponse<File> retrieveInvoiceWithHttpInfo(String providerId, String ID) throws ApiException {
        okhttp3.Call localVarCall = retrieveInvoiceValidateBeforeCall(providerId, ID, null);
        Type localVarReturnType = new TypeToken<File>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call retrieveInvoiceAsync(String providerId, String ID, final ApiCallback<File> _callback) throws ApiException {

        okhttp3.Call localVarCall = retrieveInvoiceValidateBeforeCall(providerId, ID, _callback);
        Type localVarReturnType = new TypeToken<File>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class RetrieveInvoiceRequestBuilder {
        private final String providerId;
        private final String ID;

        private RetrieveInvoiceRequestBuilder(String providerId, String ID) {
            this.providerId = providerId;
            this.ID = ID;
        }

        /**
         * Build call for retrieveInvoice
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
            return retrieveInvoiceCall(providerId, ID, _callback);
        }


        /**
         * Execute retrieveInvoice request
         * @return File
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public File execute() throws ApiException {
            ApiResponse<File> localVarResp = retrieveInvoiceWithHttpInfo(providerId, ID);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute retrieveInvoice request with HTTP info returned
         * @return ApiResponse&lt;File&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<File> executeWithHttpInfo() throws ApiException {
            return retrieveInvoiceWithHttpInfo(providerId, ID);
        }

        /**
         * Execute retrieveInvoice request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<File> _callback) throws ApiException {
            return retrieveInvoiceAsync(providerId, ID, _callback);
        }
    }

    /**
     * Download a provider&#39;s invoice.
     * Retrieves an invoice for this provider. You must be associated to the provider to use this endpoint.
     * @param providerId  (required)
     * @param ID  (required)
     * @return RetrieveInvoiceRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public RetrieveInvoiceRequestBuilder retrieveInvoice(String providerId, String ID) throws IllegalArgumentException {
        if (providerId == null) throw new IllegalArgumentException("\"providerId\" is required but got null");
            

        if (ID == null) throw new IllegalArgumentException("\"ID\" is required but got null");
            

        return new RetrieveInvoiceRequestBuilder(providerId, ID);
    }
    private okhttp3.Call retrieveInvoicesCall(String providerId, Integer skip, List<String> sort, Integer limit, List<String> filter, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/providers/{provider_id}/invoices"
            .replace("{" + "provider_id" + "}", localVarApiClient.escapeString(providerId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (skip != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("skip", skip));
        }

        if (sort != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "sort", sort));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
        }

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
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
    private okhttp3.Call retrieveInvoicesValidateBeforeCall(String providerId, Integer skip, List<String> sort, Integer limit, List<String> filter, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'providerId' is set
        if (providerId == null) {
            throw new ApiException("Missing the required parameter 'providerId' when calling retrieveInvoices(Async)");
        }

        return retrieveInvoicesCall(providerId, skip, sort, limit, filter, _callback);

    }


    private ApiResponse<ProviderInvoiceResponse> retrieveInvoicesWithHttpInfo(String providerId, Integer skip, List<String> sort, Integer limit, List<String> filter) throws ApiException {
        okhttp3.Call localVarCall = retrieveInvoicesValidateBeforeCall(providerId, skip, sort, limit, filter, null);
        Type localVarReturnType = new TypeToken<ProviderInvoiceResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call retrieveInvoicesAsync(String providerId, Integer skip, List<String> sort, Integer limit, List<String> filter, final ApiCallback<ProviderInvoiceResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = retrieveInvoicesValidateBeforeCall(providerId, skip, sort, limit, filter, _callback);
        Type localVarReturnType = new TypeToken<ProviderInvoiceResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class RetrieveInvoicesRequestBuilder {
        private final String providerId;
        private Integer skip;
        private List<String> sort;
        private Integer limit;
        private List<String> filter;

        private RetrieveInvoicesRequestBuilder(String providerId) {
            this.providerId = providerId;
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return RetrieveInvoicesRequestBuilder
         */
        public RetrieveInvoicesRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  (optional)
         * @return RetrieveInvoicesRequestBuilder
         */
        public RetrieveInvoicesRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return RetrieveInvoicesRequestBuilder
         */
        public RetrieveInvoicesRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return RetrieveInvoicesRequestBuilder
         */
        public RetrieveInvoicesRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Build call for retrieveInvoices
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
            return retrieveInvoicesCall(providerId, skip, sort, limit, filter, _callback);
        }


        /**
         * Execute retrieveInvoices request
         * @return ProviderInvoiceResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ProviderInvoiceResponse execute() throws ApiException {
            ApiResponse<ProviderInvoiceResponse> localVarResp = retrieveInvoicesWithHttpInfo(providerId, skip, sort, limit, filter);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute retrieveInvoices request with HTTP info returned
         * @return ApiResponse&lt;ProviderInvoiceResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<ProviderInvoiceResponse> executeWithHttpInfo() throws ApiException {
            return retrieveInvoicesWithHttpInfo(providerId, skip, sort, limit, filter);
        }

        /**
         * Execute retrieveInvoices request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ProviderInvoiceResponse> _callback) throws ApiException {
            return retrieveInvoicesAsync(providerId, skip, sort, limit, filter, _callback);
        }
    }

    /**
     * List a provider&#39;s invoices.
     * Retrieves a list of invoices for this provider. You must be associated to the provider to use this endpoint.
     * @param providerId  (required)
     * @return RetrieveInvoicesRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public RetrieveInvoicesRequestBuilder retrieveInvoices(String providerId) throws IllegalArgumentException {
        if (providerId == null) throw new IllegalArgumentException("\"providerId\" is required but got null");
            

        return new RetrieveInvoicesRequestBuilder(providerId);
    }
    private okhttp3.Call updateOrgCall(String providerId, String id, Organization organization, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = organization;

        // create path and map variables
        String localVarPath = "/providers/{provider_id}/organizations/{id}"
            .replace("{" + "provider_id" + "}", localVarApiClient.escapeString(providerId.toString()))
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
    private okhttp3.Call updateOrgValidateBeforeCall(String providerId, String id, Organization organization, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'providerId' is set
        if (providerId == null) {
            throw new ApiException("Missing the required parameter 'providerId' when calling updateOrg(Async)");
        }

        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling updateOrg(Async)");
        }

        return updateOrgCall(providerId, id, organization, _callback);

    }


    private ApiResponse<Organization> updateOrgWithHttpInfo(String providerId, String id, Organization organization) throws ApiException {
        okhttp3.Call localVarCall = updateOrgValidateBeforeCall(providerId, id, organization, null);
        Type localVarReturnType = new TypeToken<Organization>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call updateOrgAsync(String providerId, String id, Organization organization, final ApiCallback<Organization> _callback) throws ApiException {

        okhttp3.Call localVarCall = updateOrgValidateBeforeCall(providerId, id, organization, _callback);
        Type localVarReturnType = new TypeToken<Organization>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UpdateOrgRequestBuilder {
        private final String providerId;
        private final String id;
        private String id;
        private Integer maxSystemUsers;
        private String name;

        private UpdateOrgRequestBuilder(String providerId, String id) {
            this.providerId = providerId;
            this.id = id;
        }

        /**
         * Set id
         * @param id  (optional)
         * @return UpdateOrgRequestBuilder
         */
        public UpdateOrgRequestBuilder id(String id) {
            this.id = id;
            return this;
        }
        
        /**
         * Set maxSystemUsers
         * @param maxSystemUsers The maximum number of users allowed in this organization. Requires organizations.billing scope to modify. (optional)
         * @return UpdateOrgRequestBuilder
         */
        public UpdateOrgRequestBuilder maxSystemUsers(Integer maxSystemUsers) {
            this.maxSystemUsers = maxSystemUsers;
            return this;
        }
        
        /**
         * Set name
         * @param name  (optional)
         * @return UpdateOrgRequestBuilder
         */
        public UpdateOrgRequestBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        /**
         * Build call for updateOrg
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
            Organization organization = buildBodyParams();
            return updateOrgCall(providerId, id, organization, _callback);
        }

        private Organization buildBodyParams() {
            Organization organization = new Organization();
            organization.id(this.id);
            organization.maxSystemUsers(this.maxSystemUsers);
            organization.name(this.name);
            return organization;
        }

        /**
         * Execute updateOrg request
         * @return Organization
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public Organization execute() throws ApiException {
            Organization organization = buildBodyParams();
            ApiResponse<Organization> localVarResp = updateOrgWithHttpInfo(providerId, id, organization);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute updateOrg request with HTTP info returned
         * @return ApiResponse&lt;Organization&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Organization> executeWithHttpInfo() throws ApiException {
            Organization organization = buildBodyParams();
            return updateOrgWithHttpInfo(providerId, id, organization);
        }

        /**
         * Execute updateOrg request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Organization> _callback) throws ApiException {
            Organization organization = buildBodyParams();
            return updateOrgAsync(providerId, id, organization, _callback);
        }
    }

    /**
     * Update Provider Organization
     * This endpoint updates a provider&#39;s organization
     * @param providerId  (required)
     * @param id  (required)
     * @return UpdateOrgRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UpdateOrgRequestBuilder updateOrg(String providerId, String id) throws IllegalArgumentException {
        if (providerId == null) throw new IllegalArgumentException("\"providerId\" is required but got null");
            

        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new UpdateOrgRequestBuilder(providerId, id);
    }
}
