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
import com.konfigthis.client.model.AutotaskCompanyResp;
import com.konfigthis.client.model.AutotaskCompanyTypeResp;
import com.konfigthis.client.model.AutotaskCreateConfigurationResponse;
import com.konfigthis.client.model.AutotaskIntegration;
import com.konfigthis.client.model.AutotaskIntegrationPatchReq;
import com.konfigthis.client.model.AutotaskIntegrationReq;
import com.konfigthis.client.model.AutotaskMappingRequest;
import com.konfigthis.client.model.AutotaskMappingRequestDataInner;
import com.konfigthis.client.model.AutotaskMappingResponse;
import com.konfigthis.client.model.AutotaskRetrieveContractsFieldsResponse;
import com.konfigthis.client.model.AutotaskRetrieveContractsResponse;
import com.konfigthis.client.model.AutotaskRetrieveMappingsResponse;
import com.konfigthis.client.model.AutotaskRetrieveServicesResponse;
import com.konfigthis.client.model.AutotaskSettings;
import com.konfigthis.client.model.AutotaskSettingsPatchReq;
import com.konfigthis.client.model.AutotaskTicketingAlertConfiguration;
import com.konfigthis.client.model.AutotaskTicketingAlertConfigurationList;
import com.konfigthis.client.model.AutotaskTicketingAlertConfigurationOptions;
import com.konfigthis.client.model.AutotaskTicketingAlertConfigurationPriority;
import com.konfigthis.client.model.AutotaskTicketingAlertConfigurationRequest;
import com.konfigthis.client.model.AutotaskTicketingAlertConfigurationResource;
import com.konfigthis.client.model.CasesMetadataResponse;
import com.konfigthis.client.model.CasesResponse;
import com.konfigthis.client.model.ConnectWiseMappingRequest;
import com.konfigthis.client.model.ConnectWiseMappingRequestDataInner;
import com.konfigthis.client.model.ConnectWiseSettings;
import com.konfigthis.client.model.ConnectWiseSettingsPatchReq;
import com.konfigthis.client.model.ConnectWiseTicketingAlertConfiguration;
import com.konfigthis.client.model.ConnectWiseTicketingAlertConfigurationList;
import com.konfigthis.client.model.ConnectWiseTicketingAlertConfigurationOptions;
import com.konfigthis.client.model.ConnectWiseTicketingAlertConfigurationRequest;
import com.konfigthis.client.model.ConnectwiseCompanyResp;
import com.konfigthis.client.model.ConnectwiseCompanyTypeResp;
import com.konfigthis.client.model.ConnectwiseCreateConfigurationResponse;
import com.konfigthis.client.model.ConnectwiseIntegration;
import com.konfigthis.client.model.ConnectwiseIntegrationPatchReq;
import com.konfigthis.client.model.ConnectwiseIntegrationReq;
import com.konfigthis.client.model.ConnectwiseRetrieveAdditionsResponse;
import com.konfigthis.client.model.ConnectwiseRetrieveAgreementsResponse;
import com.konfigthis.client.model.ConnectwiseRetrieveMappingsResponse;
import com.konfigthis.client.model.CreateOrganization;
import java.io.File;
import com.konfigthis.client.model.GoogleRpcStatus;
import com.konfigthis.client.model.IntegrationSyncErrorResp;
import com.konfigthis.client.model.IntegrationsResponse;
import com.konfigthis.client.model.JumpcloudMspGetDetailsResponse;
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
import com.konfigthis.client.model.SyncroBillingMappingConfigurationOptionsResp;
import com.konfigthis.client.model.SyncroCompanyResp;
import com.konfigthis.client.model.SyncroCreateConfigurationResponse;
import com.konfigthis.client.model.SyncroIntegration;
import com.konfigthis.client.model.SyncroIntegrationPatchReq;
import com.konfigthis.client.model.SyncroIntegrationReq;
import com.konfigthis.client.model.SyncroMappingRequest;
import com.konfigthis.client.model.SyncroMappingRequestDataInner;
import com.konfigthis.client.model.SyncroRetrieveMappingsResponse;
import com.konfigthis.client.model.SyncroSettings;
import com.konfigthis.client.model.SyncroSettingsPatchReq;
import com.konfigthis.client.model.SyncroTicketingAlertConfiguration;
import com.konfigthis.client.model.SyncroTicketingAlertConfigurationList;
import com.konfigthis.client.model.SyncroTicketingAlertConfigurationOptions;
import com.konfigthis.client.model.SyncroTicketingAlertConfigurationRequest;
import com.konfigthis.client.model.TicketingIntegrationAlertsResp;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;

public class ProvidersApiGenerated {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public ProvidersApiGenerated() throws IllegalArgumentException {
        this(Configuration.getDefaultApiClient());
    }

    public ProvidersApiGenerated(ApiClient apiClient) throws IllegalArgumentException {
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
    private okhttp3.Call createConfigurationCall(String providerId, AutotaskIntegrationReq autotaskIntegrationReq, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = autotaskIntegrationReq;

        // create path and map variables
        String localVarPath = "/providers/{provider_id}/integrations/autotask"
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
    private okhttp3.Call createConfigurationValidateBeforeCall(String providerId, AutotaskIntegrationReq autotaskIntegrationReq, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'providerId' is set
        if (providerId == null) {
            throw new ApiException("Missing the required parameter 'providerId' when calling createConfiguration(Async)");
        }

        return createConfigurationCall(providerId, autotaskIntegrationReq, _callback);

    }


    private ApiResponse<AutotaskCreateConfigurationResponse> createConfigurationWithHttpInfo(String providerId, AutotaskIntegrationReq autotaskIntegrationReq) throws ApiException {
        okhttp3.Call localVarCall = createConfigurationValidateBeforeCall(providerId, autotaskIntegrationReq, null);
        Type localVarReturnType = new TypeToken<AutotaskCreateConfigurationResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call createConfigurationAsync(String providerId, AutotaskIntegrationReq autotaskIntegrationReq, final ApiCallback<AutotaskCreateConfigurationResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = createConfigurationValidateBeforeCall(providerId, autotaskIntegrationReq, _callback);
        Type localVarReturnType = new TypeToken<AutotaskCreateConfigurationResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class CreateConfigurationRequestBuilder {
        private final String secret;
        private final String username;
        private final String providerId;

        private CreateConfigurationRequestBuilder(String secret, String username, String providerId) {
            this.secret = secret;
            this.username = username;
            this.providerId = providerId;
        }

        /**
         * Build call for createConfiguration
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
            AutotaskIntegrationReq autotaskIntegrationReq = buildBodyParams();
            return createConfigurationCall(providerId, autotaskIntegrationReq, _callback);
        }

        private AutotaskIntegrationReq buildBodyParams() {
            AutotaskIntegrationReq autotaskIntegrationReq = new AutotaskIntegrationReq();
            autotaskIntegrationReq.secret(this.secret);
            autotaskIntegrationReq.username(this.username);
            return autotaskIntegrationReq;
        }

        /**
         * Execute createConfiguration request
         * @return AutotaskCreateConfigurationResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
         </table>
         */
        public AutotaskCreateConfigurationResponse execute() throws ApiException {
            AutotaskIntegrationReq autotaskIntegrationReq = buildBodyParams();
            ApiResponse<AutotaskCreateConfigurationResponse> localVarResp = createConfigurationWithHttpInfo(providerId, autotaskIntegrationReq);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute createConfiguration request with HTTP info returned
         * @return ApiResponse&lt;AutotaskCreateConfigurationResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<AutotaskCreateConfigurationResponse> executeWithHttpInfo() throws ApiException {
            AutotaskIntegrationReq autotaskIntegrationReq = buildBodyParams();
            return createConfigurationWithHttpInfo(providerId, autotaskIntegrationReq);
        }

        /**
         * Execute createConfiguration request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AutotaskCreateConfigurationResponse> _callback) throws ApiException {
            AutotaskIntegrationReq autotaskIntegrationReq = buildBodyParams();
            return createConfigurationAsync(providerId, autotaskIntegrationReq, _callback);
        }
    }

    /**
     * Creates a new Autotask integration for the provider
     * Creates a new Autotask integration for the provider. You must be associated with the provider to use this route. A 422 Unprocessable Entity response means the server failed to validate with Autotask.
     * @param providerId  (required)
     * @return CreateConfigurationRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
     </table>
     */
    public CreateConfigurationRequestBuilder createConfiguration(String secret, String username, String providerId) throws IllegalArgumentException {
        if (secret == null) throw new IllegalArgumentException("\"secret\" is required but got null");
            

        if (username == null) throw new IllegalArgumentException("\"username\" is required but got null");
            

        if (providerId == null) throw new IllegalArgumentException("\"providerId\" is required but got null");
            

        return new CreateConfigurationRequestBuilder(secret, username, providerId);
    }
    private okhttp3.Call createConfiguration_0Call(String providerId, ConnectwiseIntegrationReq connectwiseIntegrationReq, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = connectwiseIntegrationReq;

        // create path and map variables
        String localVarPath = "/providers/{provider_id}/integrations/connectwise"
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
    private okhttp3.Call createConfiguration_0ValidateBeforeCall(String providerId, ConnectwiseIntegrationReq connectwiseIntegrationReq, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'providerId' is set
        if (providerId == null) {
            throw new ApiException("Missing the required parameter 'providerId' when calling createConfiguration_0(Async)");
        }

        return createConfiguration_0Call(providerId, connectwiseIntegrationReq, _callback);

    }


    private ApiResponse<ConnectwiseCreateConfigurationResponse> createConfiguration_0WithHttpInfo(String providerId, ConnectwiseIntegrationReq connectwiseIntegrationReq) throws ApiException {
        okhttp3.Call localVarCall = createConfiguration_0ValidateBeforeCall(providerId, connectwiseIntegrationReq, null);
        Type localVarReturnType = new TypeToken<ConnectwiseCreateConfigurationResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call createConfiguration_0Async(String providerId, ConnectwiseIntegrationReq connectwiseIntegrationReq, final ApiCallback<ConnectwiseCreateConfigurationResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = createConfiguration_0ValidateBeforeCall(providerId, connectwiseIntegrationReq, _callback);
        Type localVarReturnType = new TypeToken<ConnectwiseCreateConfigurationResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class CreateConfiguration0RequestBuilder {
        private final String companyId;
        private final String privateKey;
        private final String publicKey;
        private final String url;
        private final String providerId;

        private CreateConfiguration0RequestBuilder(String companyId, String privateKey, String publicKey, String url, String providerId) {
            this.companyId = companyId;
            this.privateKey = privateKey;
            this.publicKey = publicKey;
            this.url = url;
            this.providerId = providerId;
        }

        /**
         * Build call for createConfiguration_0
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
            ConnectwiseIntegrationReq connectwiseIntegrationReq = buildBodyParams();
            return createConfiguration_0Call(providerId, connectwiseIntegrationReq, _callback);
        }

        private ConnectwiseIntegrationReq buildBodyParams() {
            ConnectwiseIntegrationReq connectwiseIntegrationReq = new ConnectwiseIntegrationReq();
            connectwiseIntegrationReq.companyId(this.companyId);
            connectwiseIntegrationReq.privateKey(this.privateKey);
            connectwiseIntegrationReq.publicKey(this.publicKey);
            connectwiseIntegrationReq.url(this.url);
            return connectwiseIntegrationReq;
        }

        /**
         * Execute createConfiguration_0 request
         * @return ConnectwiseCreateConfigurationResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
         </table>
         */
        public ConnectwiseCreateConfigurationResponse execute() throws ApiException {
            ConnectwiseIntegrationReq connectwiseIntegrationReq = buildBodyParams();
            ApiResponse<ConnectwiseCreateConfigurationResponse> localVarResp = createConfiguration_0WithHttpInfo(providerId, connectwiseIntegrationReq);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute createConfiguration_0 request with HTTP info returned
         * @return ApiResponse&lt;ConnectwiseCreateConfigurationResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<ConnectwiseCreateConfigurationResponse> executeWithHttpInfo() throws ApiException {
            ConnectwiseIntegrationReq connectwiseIntegrationReq = buildBodyParams();
            return createConfiguration_0WithHttpInfo(providerId, connectwiseIntegrationReq);
        }

        /**
         * Execute createConfiguration_0 request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ConnectwiseCreateConfigurationResponse> _callback) throws ApiException {
            ConnectwiseIntegrationReq connectwiseIntegrationReq = buildBodyParams();
            return createConfiguration_0Async(providerId, connectwiseIntegrationReq, _callback);
        }
    }

    /**
     * Creates a new ConnectWise integration for the provider
     * Creates a new ConnectWise integration for the provider. You must be associated with the provider to use this route. A 422 Unprocessable Entity response means the server failed to validate with ConnectWise.
     * @param providerId  (required)
     * @return CreateConfiguration0RequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
     </table>
     */
    public CreateConfiguration0RequestBuilder createConfiguration_0(String companyId, String privateKey, String publicKey, String url, String providerId) throws IllegalArgumentException {
        if (companyId == null) throw new IllegalArgumentException("\"companyId\" is required but got null");
            

        if (privateKey == null) throw new IllegalArgumentException("\"privateKey\" is required but got null");
            

        if (publicKey == null) throw new IllegalArgumentException("\"publicKey\" is required but got null");
            

        if (url == null) throw new IllegalArgumentException("\"url\" is required but got null");
            

        if (providerId == null) throw new IllegalArgumentException("\"providerId\" is required but got null");
            

        return new CreateConfiguration0RequestBuilder(companyId, privateKey, publicKey, url, providerId);
    }
    private okhttp3.Call createConfiguration_1Call(String providerId, SyncroIntegrationReq syncroIntegrationReq, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = syncroIntegrationReq;

        // create path and map variables
        String localVarPath = "/providers/{provider_id}/integrations/syncro"
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
    private okhttp3.Call createConfiguration_1ValidateBeforeCall(String providerId, SyncroIntegrationReq syncroIntegrationReq, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'providerId' is set
        if (providerId == null) {
            throw new ApiException("Missing the required parameter 'providerId' when calling createConfiguration_1(Async)");
        }

        return createConfiguration_1Call(providerId, syncroIntegrationReq, _callback);

    }


    private ApiResponse<SyncroCreateConfigurationResponse> createConfiguration_1WithHttpInfo(String providerId, SyncroIntegrationReq syncroIntegrationReq) throws ApiException {
        okhttp3.Call localVarCall = createConfiguration_1ValidateBeforeCall(providerId, syncroIntegrationReq, null);
        Type localVarReturnType = new TypeToken<SyncroCreateConfigurationResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call createConfiguration_1Async(String providerId, SyncroIntegrationReq syncroIntegrationReq, final ApiCallback<SyncroCreateConfigurationResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = createConfiguration_1ValidateBeforeCall(providerId, syncroIntegrationReq, _callback);
        Type localVarReturnType = new TypeToken<SyncroCreateConfigurationResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class CreateConfiguration1RequestBuilder {
        private final String apiToken;
        private final String subdomain;
        private final String providerId;

        private CreateConfiguration1RequestBuilder(String apiToken, String subdomain, String providerId) {
            this.apiToken = apiToken;
            this.subdomain = subdomain;
            this.providerId = providerId;
        }

        /**
         * Build call for createConfiguration_1
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
            SyncroIntegrationReq syncroIntegrationReq = buildBodyParams();
            return createConfiguration_1Call(providerId, syncroIntegrationReq, _callback);
        }

        private SyncroIntegrationReq buildBodyParams() {
            SyncroIntegrationReq syncroIntegrationReq = new SyncroIntegrationReq();
            syncroIntegrationReq.apiToken(this.apiToken);
            syncroIntegrationReq.subdomain(this.subdomain);
            return syncroIntegrationReq;
        }

        /**
         * Execute createConfiguration_1 request
         * @return SyncroCreateConfigurationResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
         </table>
         */
        public SyncroCreateConfigurationResponse execute() throws ApiException {
            SyncroIntegrationReq syncroIntegrationReq = buildBodyParams();
            ApiResponse<SyncroCreateConfigurationResponse> localVarResp = createConfiguration_1WithHttpInfo(providerId, syncroIntegrationReq);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute createConfiguration_1 request with HTTP info returned
         * @return ApiResponse&lt;SyncroCreateConfigurationResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<SyncroCreateConfigurationResponse> executeWithHttpInfo() throws ApiException {
            SyncroIntegrationReq syncroIntegrationReq = buildBodyParams();
            return createConfiguration_1WithHttpInfo(providerId, syncroIntegrationReq);
        }

        /**
         * Execute createConfiguration_1 request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<SyncroCreateConfigurationResponse> _callback) throws ApiException {
            SyncroIntegrationReq syncroIntegrationReq = buildBodyParams();
            return createConfiguration_1Async(providerId, syncroIntegrationReq, _callback);
        }
    }

    /**
     * Creates a new Syncro integration for the provider
     * Creates a new Syncro integration for the provider. You must be associated with the provider to use this route. A 422 Unprocessable Entity response means the server failed to validate with Syncro.
     * @param providerId  (required)
     * @return CreateConfiguration1RequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>
     </table>
     */
    public CreateConfiguration1RequestBuilder createConfiguration_1(String apiToken, String subdomain, String providerId) throws IllegalArgumentException {
        if (apiToken == null) throw new IllegalArgumentException("\"apiToken\" is required but got null");
            

        if (subdomain == null) throw new IllegalArgumentException("\"subdomain\" is required but got null");
            

        if (providerId == null) throw new IllegalArgumentException("\"providerId\" is required but got null");
            

        return new CreateConfiguration1RequestBuilder(apiToken, subdomain, providerId);
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
    private okhttp3.Call deleteConfigurationCall(String UUID, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/integrations/autotask/{UUID}"
            .replace("{" + "UUID" + "}", localVarApiClient.escapeString(UUID.toString()));

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
    private okhttp3.Call deleteConfigurationValidateBeforeCall(String UUID, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'UUID' is set
        if (UUID == null) {
            throw new ApiException("Missing the required parameter 'UUID' when calling deleteConfiguration(Async)");
        }

        return deleteConfigurationCall(UUID, _callback);

    }


    private ApiResponse<Void> deleteConfigurationWithHttpInfo(String UUID) throws ApiException {
        okhttp3.Call localVarCall = deleteConfigurationValidateBeforeCall(UUID, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call deleteConfigurationAsync(String UUID, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteConfigurationValidateBeforeCall(UUID, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DeleteConfigurationRequestBuilder {
        private final String UUID;

        private DeleteConfigurationRequestBuilder(String UUID) {
            this.UUID = UUID;
        }

        /**
         * Build call for deleteConfiguration
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
            return deleteConfigurationCall(UUID, _callback);
        }


        /**
         * Execute deleteConfiguration request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            deleteConfigurationWithHttpInfo(UUID);
        }

        /**
         * Execute deleteConfiguration request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            return deleteConfigurationWithHttpInfo(UUID);
        }

        /**
         * Execute deleteConfiguration request (asynchronously)
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
            return deleteConfigurationAsync(UUID, _callback);
        }
    }

    /**
     * Delete Autotask Integration
     * Removes a Autotask integration.
     * @param UUID  (required)
     * @return DeleteConfigurationRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
     </table>
     */
    public DeleteConfigurationRequestBuilder deleteConfiguration(String UUID) throws IllegalArgumentException {
        if (UUID == null) throw new IllegalArgumentException("\"UUID\" is required but got null");
            

        return new DeleteConfigurationRequestBuilder(UUID);
    }
    private okhttp3.Call deleteConfiguration_0Call(String UUID, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/integrations/connectwise/{UUID}"
            .replace("{" + "UUID" + "}", localVarApiClient.escapeString(UUID.toString()));

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
    private okhttp3.Call deleteConfiguration_0ValidateBeforeCall(String UUID, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'UUID' is set
        if (UUID == null) {
            throw new ApiException("Missing the required parameter 'UUID' when calling deleteConfiguration_0(Async)");
        }

        return deleteConfiguration_0Call(UUID, _callback);

    }


    private ApiResponse<Void> deleteConfiguration_0WithHttpInfo(String UUID) throws ApiException {
        okhttp3.Call localVarCall = deleteConfiguration_0ValidateBeforeCall(UUID, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call deleteConfiguration_0Async(String UUID, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteConfiguration_0ValidateBeforeCall(UUID, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DeleteConfiguration0RequestBuilder {
        private final String UUID;

        private DeleteConfiguration0RequestBuilder(String UUID) {
            this.UUID = UUID;
        }

        /**
         * Build call for deleteConfiguration_0
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
            return deleteConfiguration_0Call(UUID, _callback);
        }


        /**
         * Execute deleteConfiguration_0 request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            deleteConfiguration_0WithHttpInfo(UUID);
        }

        /**
         * Execute deleteConfiguration_0 request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            return deleteConfiguration_0WithHttpInfo(UUID);
        }

        /**
         * Execute deleteConfiguration_0 request (asynchronously)
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
            return deleteConfiguration_0Async(UUID, _callback);
        }
    }

    /**
     * Delete ConnectWise Integration
     * Removes a ConnectWise integration.
     * @param UUID  (required)
     * @return DeleteConfiguration0RequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
     </table>
     */
    public DeleteConfiguration0RequestBuilder deleteConfiguration_0(String UUID) throws IllegalArgumentException {
        if (UUID == null) throw new IllegalArgumentException("\"UUID\" is required but got null");
            

        return new DeleteConfiguration0RequestBuilder(UUID);
    }
    private okhttp3.Call deleteConfiguration_1Call(String UUID, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/integrations/syncro/{UUID}"
            .replace("{" + "UUID" + "}", localVarApiClient.escapeString(UUID.toString()));

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
    private okhttp3.Call deleteConfiguration_1ValidateBeforeCall(String UUID, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'UUID' is set
        if (UUID == null) {
            throw new ApiException("Missing the required parameter 'UUID' when calling deleteConfiguration_1(Async)");
        }

        return deleteConfiguration_1Call(UUID, _callback);

    }


    private ApiResponse<Void> deleteConfiguration_1WithHttpInfo(String UUID) throws ApiException {
        okhttp3.Call localVarCall = deleteConfiguration_1ValidateBeforeCall(UUID, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call deleteConfiguration_1Async(String UUID, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteConfiguration_1ValidateBeforeCall(UUID, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DeleteConfiguration1RequestBuilder {
        private final String UUID;

        private DeleteConfiguration1RequestBuilder(String UUID) {
            this.UUID = UUID;
        }

        /**
         * Build call for deleteConfiguration_1
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
            return deleteConfiguration_1Call(UUID, _callback);
        }


        /**
         * Execute deleteConfiguration_1 request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            deleteConfiguration_1WithHttpInfo(UUID);
        }

        /**
         * Execute deleteConfiguration_1 request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            return deleteConfiguration_1WithHttpInfo(UUID);
        }

        /**
         * Execute deleteConfiguration_1 request (asynchronously)
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
            return deleteConfiguration_1Async(UUID, _callback);
        }
    }

    /**
     * Delete Syncro Integration
     * Removes a Syncro integration.
     * @param UUID  (required)
     * @return DeleteConfiguration1RequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
     </table>
     */
    public DeleteConfiguration1RequestBuilder deleteConfiguration_1(String UUID) throws IllegalArgumentException {
        if (UUID == null) throw new IllegalArgumentException("\"UUID\" is required but got null");
            

        return new DeleteConfiguration1RequestBuilder(UUID);
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
    private okhttp3.Call getConfigurationCall(String UUID, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/integrations/autotask/{UUID}"
            .replace("{" + "UUID" + "}", localVarApiClient.escapeString(UUID.toString()));

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
    private okhttp3.Call getConfigurationValidateBeforeCall(String UUID, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'UUID' is set
        if (UUID == null) {
            throw new ApiException("Missing the required parameter 'UUID' when calling getConfiguration(Async)");
        }

        return getConfigurationCall(UUID, _callback);

    }


    private ApiResponse<AutotaskIntegration> getConfigurationWithHttpInfo(String UUID) throws ApiException {
        okhttp3.Call localVarCall = getConfigurationValidateBeforeCall(UUID, null);
        Type localVarReturnType = new TypeToken<AutotaskIntegration>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getConfigurationAsync(String UUID, final ApiCallback<AutotaskIntegration> _callback) throws ApiException {

        okhttp3.Call localVarCall = getConfigurationValidateBeforeCall(UUID, _callback);
        Type localVarReturnType = new TypeToken<AutotaskIntegration>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetConfigurationRequestBuilder {
        private final String UUID;

        private GetConfigurationRequestBuilder(String UUID) {
            this.UUID = UUID;
        }

        /**
         * Build call for getConfiguration
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
            return getConfigurationCall(UUID, _callback);
        }


        /**
         * Execute getConfiguration request
         * @return AutotaskIntegration
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public AutotaskIntegration execute() throws ApiException {
            ApiResponse<AutotaskIntegration> localVarResp = getConfigurationWithHttpInfo(UUID);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getConfiguration request with HTTP info returned
         * @return ApiResponse&lt;AutotaskIntegration&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<AutotaskIntegration> executeWithHttpInfo() throws ApiException {
            return getConfigurationWithHttpInfo(UUID);
        }

        /**
         * Execute getConfiguration request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AutotaskIntegration> _callback) throws ApiException {
            return getConfigurationAsync(UUID, _callback);
        }
    }

    /**
     * Retrieve Autotask Integration Configuration
     * Retrieves configuration for given Autotask integration id. You must be associated to the provider the integration is tied to in order to use this api.
     * @param UUID  (required)
     * @return GetConfigurationRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public GetConfigurationRequestBuilder getConfiguration(String UUID) throws IllegalArgumentException {
        if (UUID == null) throw new IllegalArgumentException("\"UUID\" is required but got null");
            

        return new GetConfigurationRequestBuilder(UUID);
    }
    private okhttp3.Call getConfiguration_0Call(String UUID, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/integrations/connectwise/{UUID}"
            .replace("{" + "UUID" + "}", localVarApiClient.escapeString(UUID.toString()));

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
    private okhttp3.Call getConfiguration_0ValidateBeforeCall(String UUID, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'UUID' is set
        if (UUID == null) {
            throw new ApiException("Missing the required parameter 'UUID' when calling getConfiguration_0(Async)");
        }

        return getConfiguration_0Call(UUID, _callback);

    }


    private ApiResponse<ConnectwiseIntegration> getConfiguration_0WithHttpInfo(String UUID) throws ApiException {
        okhttp3.Call localVarCall = getConfiguration_0ValidateBeforeCall(UUID, null);
        Type localVarReturnType = new TypeToken<ConnectwiseIntegration>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getConfiguration_0Async(String UUID, final ApiCallback<ConnectwiseIntegration> _callback) throws ApiException {

        okhttp3.Call localVarCall = getConfiguration_0ValidateBeforeCall(UUID, _callback);
        Type localVarReturnType = new TypeToken<ConnectwiseIntegration>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetConfiguration0RequestBuilder {
        private final String UUID;

        private GetConfiguration0RequestBuilder(String UUID) {
            this.UUID = UUID;
        }

        /**
         * Build call for getConfiguration_0
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
            return getConfiguration_0Call(UUID, _callback);
        }


        /**
         * Execute getConfiguration_0 request
         * @return ConnectwiseIntegration
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ConnectwiseIntegration execute() throws ApiException {
            ApiResponse<ConnectwiseIntegration> localVarResp = getConfiguration_0WithHttpInfo(UUID);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getConfiguration_0 request with HTTP info returned
         * @return ApiResponse&lt;ConnectwiseIntegration&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<ConnectwiseIntegration> executeWithHttpInfo() throws ApiException {
            return getConfiguration_0WithHttpInfo(UUID);
        }

        /**
         * Execute getConfiguration_0 request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ConnectwiseIntegration> _callback) throws ApiException {
            return getConfiguration_0Async(UUID, _callback);
        }
    }

    /**
     * Retrieve ConnectWise Integration Configuration
     * Retrieves configuration for given ConnectWise integration id. You must be associated to the provider the integration is tied to in order to use this api.
     * @param UUID  (required)
     * @return GetConfiguration0RequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public GetConfiguration0RequestBuilder getConfiguration_0(String UUID) throws IllegalArgumentException {
        if (UUID == null) throw new IllegalArgumentException("\"UUID\" is required but got null");
            

        return new GetConfiguration0RequestBuilder(UUID);
    }
    private okhttp3.Call getConfiguration_1Call(String UUID, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/integrations/syncro/{UUID}"
            .replace("{" + "UUID" + "}", localVarApiClient.escapeString(UUID.toString()));

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
    private okhttp3.Call getConfiguration_1ValidateBeforeCall(String UUID, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'UUID' is set
        if (UUID == null) {
            throw new ApiException("Missing the required parameter 'UUID' when calling getConfiguration_1(Async)");
        }

        return getConfiguration_1Call(UUID, _callback);

    }


    private ApiResponse<SyncroIntegration> getConfiguration_1WithHttpInfo(String UUID) throws ApiException {
        okhttp3.Call localVarCall = getConfiguration_1ValidateBeforeCall(UUID, null);
        Type localVarReturnType = new TypeToken<SyncroIntegration>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getConfiguration_1Async(String UUID, final ApiCallback<SyncroIntegration> _callback) throws ApiException {

        okhttp3.Call localVarCall = getConfiguration_1ValidateBeforeCall(UUID, _callback);
        Type localVarReturnType = new TypeToken<SyncroIntegration>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetConfiguration1RequestBuilder {
        private final String UUID;

        private GetConfiguration1RequestBuilder(String UUID) {
            this.UUID = UUID;
        }

        /**
         * Build call for getConfiguration_1
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
            return getConfiguration_1Call(UUID, _callback);
        }


        /**
         * Execute getConfiguration_1 request
         * @return SyncroIntegration
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public SyncroIntegration execute() throws ApiException {
            ApiResponse<SyncroIntegration> localVarResp = getConfiguration_1WithHttpInfo(UUID);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getConfiguration_1 request with HTTP info returned
         * @return ApiResponse&lt;SyncroIntegration&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<SyncroIntegration> executeWithHttpInfo() throws ApiException {
            return getConfiguration_1WithHttpInfo(UUID);
        }

        /**
         * Execute getConfiguration_1 request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<SyncroIntegration> _callback) throws ApiException {
            return getConfiguration_1Async(UUID, _callback);
        }
    }

    /**
     * Retrieve Syncro Integration Configuration
     * Retrieves configuration for given Syncro integration id. You must be associated to the provider the integration is tied to in order to use this api.
     * @param UUID  (required)
     * @return GetConfiguration1RequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public GetConfiguration1RequestBuilder getConfiguration_1(String UUID) throws IllegalArgumentException {
        if (UUID == null) throw new IllegalArgumentException("\"UUID\" is required but got null");
            

        return new GetConfiguration1RequestBuilder(UUID);
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
    private okhttp3.Call getContractCall(byte[] providerId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/providers/{provider_id}/billing/contract"
            .replace("{" + "provider_id" + "}", localVarApiClient.escapeString(providerId.toString()));

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
    private okhttp3.Call getContractValidateBeforeCall(byte[] providerId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'providerId' is set
        if (providerId == null) {
            throw new ApiException("Missing the required parameter 'providerId' when calling getContract(Async)");
        }

        return getContractCall(providerId, _callback);

    }


    private ApiResponse<File> getContractWithHttpInfo(byte[] providerId) throws ApiException {
        okhttp3.Call localVarCall = getContractValidateBeforeCall(providerId, null);
        Type localVarReturnType = new TypeToken<File>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getContractAsync(byte[] providerId, final ApiCallback<File> _callback) throws ApiException {

        okhttp3.Call localVarCall = getContractValidateBeforeCall(providerId, _callback);
        Type localVarReturnType = new TypeToken<File>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetContractRequestBuilder {
        private final byte[] providerId;

        private GetContractRequestBuilder(byte[] providerId) {
            this.providerId = providerId;
        }

        /**
         * Build call for getContract
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
            return getContractCall(providerId, _callback);
        }


        /**
         * Execute getContract request
         * @return File
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public File execute() throws ApiException {
            ApiResponse<File> localVarResp = getContractWithHttpInfo(providerId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getContract request with HTTP info returned
         * @return ApiResponse&lt;File&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<File> executeWithHttpInfo() throws ApiException {
            return getContractWithHttpInfo(providerId);
        }

        /**
         * Execute getContract request (asynchronously)
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
        public okhttp3.Call executeAsync(final ApiCallback<File> _callback) throws ApiException {
            return getContractAsync(providerId, _callback);
        }
    }

    /**
     * Retrieve contract for a Provider
     * Retrieve contract for a Provider
     * @param providerId  (required)
     * @return GetContractRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
        <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
     </table>
     */
    public GetContractRequestBuilder getContract(byte[] providerId) throws IllegalArgumentException {
        if (providerId == null) throw new IllegalArgumentException("\"providerId\" is required but got null");
        return new GetContractRequestBuilder(providerId);
    }
    private okhttp3.Call getDetailsCall(byte[] providerId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/providers/{provider_id}/billing/details"
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
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "x-api-key" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call getDetailsValidateBeforeCall(byte[] providerId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'providerId' is set
        if (providerId == null) {
            throw new ApiException("Missing the required parameter 'providerId' when calling getDetails(Async)");
        }

        return getDetailsCall(providerId, _callback);

    }


    private ApiResponse<JumpcloudMspGetDetailsResponse> getDetailsWithHttpInfo(byte[] providerId) throws ApiException {
        okhttp3.Call localVarCall = getDetailsValidateBeforeCall(providerId, null);
        Type localVarReturnType = new TypeToken<JumpcloudMspGetDetailsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getDetailsAsync(byte[] providerId, final ApiCallback<JumpcloudMspGetDetailsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getDetailsValidateBeforeCall(providerId, _callback);
        Type localVarReturnType = new TypeToken<JumpcloudMspGetDetailsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetDetailsRequestBuilder {
        private final byte[] providerId;

        private GetDetailsRequestBuilder(byte[] providerId) {
            this.providerId = providerId;
        }

        /**
         * Build call for getDetails
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
            return getDetailsCall(providerId, _callback);
        }


        /**
         * Execute getDetails request
         * @return JumpcloudMspGetDetailsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public JumpcloudMspGetDetailsResponse execute() throws ApiException {
            ApiResponse<JumpcloudMspGetDetailsResponse> localVarResp = getDetailsWithHttpInfo(providerId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getDetails request with HTTP info returned
         * @return ApiResponse&lt;JumpcloudMspGetDetailsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
            <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<JumpcloudMspGetDetailsResponse> executeWithHttpInfo() throws ApiException {
            return getDetailsWithHttpInfo(providerId);
        }

        /**
         * Execute getDetails request (asynchronously)
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
        public okhttp3.Call executeAsync(final ApiCallback<JumpcloudMspGetDetailsResponse> _callback) throws ApiException {
            return getDetailsAsync(providerId, _callback);
        }
    }

    /**
     * Retrieve billing details for a Provider
     * Retrieve billing details for a Provider
     * @param providerId  (required)
     * @return GetDetailsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
        <tr><td> 0 </td><td> An unexpected error response. </td><td>  -  </td></tr>
     </table>
     */
    public GetDetailsRequestBuilder getDetails(byte[] providerId) throws IllegalArgumentException {
        if (providerId == null) throw new IllegalArgumentException("\"providerId\" is required but got null");
        return new GetDetailsRequestBuilder(providerId);
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
    private okhttp3.Call patchMappingsCall(String UUID, AutotaskMappingRequest autotaskMappingRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = autotaskMappingRequest;

        // create path and map variables
        String localVarPath = "/integrations/autotask/{UUID}/mappings"
            .replace("{" + "UUID" + "}", localVarApiClient.escapeString(UUID.toString()));

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
        return localVarApiClient.buildCall(basePath, localVarPath, "PATCH", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call patchMappingsValidateBeforeCall(String UUID, AutotaskMappingRequest autotaskMappingRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'UUID' is set
        if (UUID == null) {
            throw new ApiException("Missing the required parameter 'UUID' when calling patchMappings(Async)");
        }

        return patchMappingsCall(UUID, autotaskMappingRequest, _callback);

    }


    private ApiResponse<AutotaskMappingResponse> patchMappingsWithHttpInfo(String UUID, AutotaskMappingRequest autotaskMappingRequest) throws ApiException {
        okhttp3.Call localVarCall = patchMappingsValidateBeforeCall(UUID, autotaskMappingRequest, null);
        Type localVarReturnType = new TypeToken<AutotaskMappingResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call patchMappingsAsync(String UUID, AutotaskMappingRequest autotaskMappingRequest, final ApiCallback<AutotaskMappingResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = patchMappingsValidateBeforeCall(UUID, autotaskMappingRequest, _callback);
        Type localVarReturnType = new TypeToken<AutotaskMappingResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class PatchMappingsRequestBuilder {
        private final String UUID;
        private List<AutotaskMappingRequestDataInner> data;

        private PatchMappingsRequestBuilder(String UUID) {
            this.UUID = UUID;
        }

        /**
         * Set data
         * @param data  (optional)
         * @return PatchMappingsRequestBuilder
         */
        public PatchMappingsRequestBuilder data(List<AutotaskMappingRequestDataInner> data) {
            this.data = data;
            return this;
        }
        
        /**
         * Build call for patchMappings
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
            AutotaskMappingRequest autotaskMappingRequest = buildBodyParams();
            return patchMappingsCall(UUID, autotaskMappingRequest, _callback);
        }

        private AutotaskMappingRequest buildBodyParams() {
            AutotaskMappingRequest autotaskMappingRequest = new AutotaskMappingRequest();
            autotaskMappingRequest.data(this.data);
            return autotaskMappingRequest;
        }

        /**
         * Execute patchMappings request
         * @return AutotaskMappingResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
         </table>
         */
        public AutotaskMappingResponse execute() throws ApiException {
            AutotaskMappingRequest autotaskMappingRequest = buildBodyParams();
            ApiResponse<AutotaskMappingResponse> localVarResp = patchMappingsWithHttpInfo(UUID, autotaskMappingRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute patchMappings request with HTTP info returned
         * @return ApiResponse&lt;AutotaskMappingResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<AutotaskMappingResponse> executeWithHttpInfo() throws ApiException {
            AutotaskMappingRequest autotaskMappingRequest = buildBodyParams();
            return patchMappingsWithHttpInfo(UUID, autotaskMappingRequest);
        }

        /**
         * Execute patchMappings request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AutotaskMappingResponse> _callback) throws ApiException {
            AutotaskMappingRequest autotaskMappingRequest = buildBodyParams();
            return patchMappingsAsync(UUID, autotaskMappingRequest, _callback);
        }
    }

    /**
     * Create, edit, and/or delete Autotask Mappings
     * Create, edit, and/or delete mappings between Jumpcloud organizations and Autotask companies/contracts/services. You must be associated to the same provider as the Autotask integration to use this api.
     * @param UUID  (required)
     * @return PatchMappingsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
     </table>
     */
    public PatchMappingsRequestBuilder patchMappings(String UUID) throws IllegalArgumentException {
        if (UUID == null) throw new IllegalArgumentException("\"UUID\" is required but got null");
            

        return new PatchMappingsRequestBuilder(UUID);
    }
    private okhttp3.Call patchMappings_0Call(String UUID, ConnectWiseMappingRequest connectWiseMappingRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = connectWiseMappingRequest;

        // create path and map variables
        String localVarPath = "/integrations/connectwise/{UUID}/mappings"
            .replace("{" + "UUID" + "}", localVarApiClient.escapeString(UUID.toString()));

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
        return localVarApiClient.buildCall(basePath, localVarPath, "PATCH", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call patchMappings_0ValidateBeforeCall(String UUID, ConnectWiseMappingRequest connectWiseMappingRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'UUID' is set
        if (UUID == null) {
            throw new ApiException("Missing the required parameter 'UUID' when calling patchMappings_0(Async)");
        }

        return patchMappings_0Call(UUID, connectWiseMappingRequest, _callback);

    }


    private ApiResponse<ConnectWiseMappingRequest> patchMappings_0WithHttpInfo(String UUID, ConnectWiseMappingRequest connectWiseMappingRequest) throws ApiException {
        okhttp3.Call localVarCall = patchMappings_0ValidateBeforeCall(UUID, connectWiseMappingRequest, null);
        Type localVarReturnType = new TypeToken<ConnectWiseMappingRequest>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call patchMappings_0Async(String UUID, ConnectWiseMappingRequest connectWiseMappingRequest, final ApiCallback<ConnectWiseMappingRequest> _callback) throws ApiException {

        okhttp3.Call localVarCall = patchMappings_0ValidateBeforeCall(UUID, connectWiseMappingRequest, _callback);
        Type localVarReturnType = new TypeToken<ConnectWiseMappingRequest>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class PatchMappings0RequestBuilder {
        private final String UUID;
        private List<ConnectWiseMappingRequestDataInner> data;

        private PatchMappings0RequestBuilder(String UUID) {
            this.UUID = UUID;
        }

        /**
         * Set data
         * @param data  (optional)
         * @return PatchMappings0RequestBuilder
         */
        public PatchMappings0RequestBuilder data(List<ConnectWiseMappingRequestDataInner> data) {
            this.data = data;
            return this;
        }
        
        /**
         * Build call for patchMappings_0
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
            ConnectWiseMappingRequest connectWiseMappingRequest = buildBodyParams();
            return patchMappings_0Call(UUID, connectWiseMappingRequest, _callback);
        }

        private ConnectWiseMappingRequest buildBodyParams() {
            ConnectWiseMappingRequest connectWiseMappingRequest = new ConnectWiseMappingRequest();
            connectWiseMappingRequest.data(this.data);
            return connectWiseMappingRequest;
        }

        /**
         * Execute patchMappings_0 request
         * @return ConnectWiseMappingRequest
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
         </table>
         */
        public ConnectWiseMappingRequest execute() throws ApiException {
            ConnectWiseMappingRequest connectWiseMappingRequest = buildBodyParams();
            ApiResponse<ConnectWiseMappingRequest> localVarResp = patchMappings_0WithHttpInfo(UUID, connectWiseMappingRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute patchMappings_0 request with HTTP info returned
         * @return ApiResponse&lt;ConnectWiseMappingRequest&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<ConnectWiseMappingRequest> executeWithHttpInfo() throws ApiException {
            ConnectWiseMappingRequest connectWiseMappingRequest = buildBodyParams();
            return patchMappings_0WithHttpInfo(UUID, connectWiseMappingRequest);
        }

        /**
         * Execute patchMappings_0 request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ConnectWiseMappingRequest> _callback) throws ApiException {
            ConnectWiseMappingRequest connectWiseMappingRequest = buildBodyParams();
            return patchMappings_0Async(UUID, connectWiseMappingRequest, _callback);
        }
    }

    /**
     * Create, edit, and/or delete ConnectWise Mappings
     * Create, edit, and/or delete mappings between Jumpcloud organizations and ConnectWise companies/agreements/additions. You must be associated to the same provider as the ConnectWise integration to use this api.
     * @param UUID  (required)
     * @return PatchMappings0RequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
     </table>
     */
    public PatchMappings0RequestBuilder patchMappings_0(String UUID) throws IllegalArgumentException {
        if (UUID == null) throw new IllegalArgumentException("\"UUID\" is required but got null");
            

        return new PatchMappings0RequestBuilder(UUID);
    }
    private okhttp3.Call patchMappings_1Call(String UUID, SyncroMappingRequest syncroMappingRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = syncroMappingRequest;

        // create path and map variables
        String localVarPath = "/integrations/syncro/{UUID}/mappings"
            .replace("{" + "UUID" + "}", localVarApiClient.escapeString(UUID.toString()));

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
        return localVarApiClient.buildCall(basePath, localVarPath, "PATCH", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call patchMappings_1ValidateBeforeCall(String UUID, SyncroMappingRequest syncroMappingRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'UUID' is set
        if (UUID == null) {
            throw new ApiException("Missing the required parameter 'UUID' when calling patchMappings_1(Async)");
        }

        return patchMappings_1Call(UUID, syncroMappingRequest, _callback);

    }


    private ApiResponse<SyncroMappingRequest> patchMappings_1WithHttpInfo(String UUID, SyncroMappingRequest syncroMappingRequest) throws ApiException {
        okhttp3.Call localVarCall = patchMappings_1ValidateBeforeCall(UUID, syncroMappingRequest, null);
        Type localVarReturnType = new TypeToken<SyncroMappingRequest>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call patchMappings_1Async(String UUID, SyncroMappingRequest syncroMappingRequest, final ApiCallback<SyncroMappingRequest> _callback) throws ApiException {

        okhttp3.Call localVarCall = patchMappings_1ValidateBeforeCall(UUID, syncroMappingRequest, _callback);
        Type localVarReturnType = new TypeToken<SyncroMappingRequest>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class PatchMappings1RequestBuilder {
        private final String UUID;
        private List<SyncroMappingRequestDataInner> data;

        private PatchMappings1RequestBuilder(String UUID) {
            this.UUID = UUID;
        }

        /**
         * Set data
         * @param data  (optional)
         * @return PatchMappings1RequestBuilder
         */
        public PatchMappings1RequestBuilder data(List<SyncroMappingRequestDataInner> data) {
            this.data = data;
            return this;
        }
        
        /**
         * Build call for patchMappings_1
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
            SyncroMappingRequest syncroMappingRequest = buildBodyParams();
            return patchMappings_1Call(UUID, syncroMappingRequest, _callback);
        }

        private SyncroMappingRequest buildBodyParams() {
            SyncroMappingRequest syncroMappingRequest = new SyncroMappingRequest();
            syncroMappingRequest.data(this.data);
            return syncroMappingRequest;
        }

        /**
         * Execute patchMappings_1 request
         * @return SyncroMappingRequest
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
         </table>
         */
        public SyncroMappingRequest execute() throws ApiException {
            SyncroMappingRequest syncroMappingRequest = buildBodyParams();
            ApiResponse<SyncroMappingRequest> localVarResp = patchMappings_1WithHttpInfo(UUID, syncroMappingRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute patchMappings_1 request with HTTP info returned
         * @return ApiResponse&lt;SyncroMappingRequest&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<SyncroMappingRequest> executeWithHttpInfo() throws ApiException {
            SyncroMappingRequest syncroMappingRequest = buildBodyParams();
            return patchMappings_1WithHttpInfo(UUID, syncroMappingRequest);
        }

        /**
         * Execute patchMappings_1 request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<SyncroMappingRequest> _callback) throws ApiException {
            SyncroMappingRequest syncroMappingRequest = buildBodyParams();
            return patchMappings_1Async(UUID, syncroMappingRequest, _callback);
        }
    }

    /**
     * Create, edit, and/or delete Syncro Mappings
     * Create, edit, and/or delete mappings between Jumpcloud organizations and Syncro companies. You must be associated to the same provider as the Syncro integration to use this api.
     * @param UUID  (required)
     * @return PatchMappings1RequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
     </table>
     */
    public PatchMappings1RequestBuilder patchMappings_1(String UUID) throws IllegalArgumentException {
        if (UUID == null) throw new IllegalArgumentException("\"UUID\" is required but got null");
            

        return new PatchMappings1RequestBuilder(UUID);
    }
    private okhttp3.Call patchSettingsCall(String UUID, AutotaskSettingsPatchReq autotaskSettingsPatchReq, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = autotaskSettingsPatchReq;

        // create path and map variables
        String localVarPath = "/integrations/autotask/{UUID}/settings"
            .replace("{" + "UUID" + "}", localVarApiClient.escapeString(UUID.toString()));

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
        return localVarApiClient.buildCall(basePath, localVarPath, "PATCH", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call patchSettingsValidateBeforeCall(String UUID, AutotaskSettingsPatchReq autotaskSettingsPatchReq, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'UUID' is set
        if (UUID == null) {
            throw new ApiException("Missing the required parameter 'UUID' when calling patchSettings(Async)");
        }

        return patchSettingsCall(UUID, autotaskSettingsPatchReq, _callback);

    }


    private ApiResponse<AutotaskSettings> patchSettingsWithHttpInfo(String UUID, AutotaskSettingsPatchReq autotaskSettingsPatchReq) throws ApiException {
        okhttp3.Call localVarCall = patchSettingsValidateBeforeCall(UUID, autotaskSettingsPatchReq, null);
        Type localVarReturnType = new TypeToken<AutotaskSettings>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call patchSettingsAsync(String UUID, AutotaskSettingsPatchReq autotaskSettingsPatchReq, final ApiCallback<AutotaskSettings> _callback) throws ApiException {

        okhttp3.Call localVarCall = patchSettingsValidateBeforeCall(UUID, autotaskSettingsPatchReq, _callback);
        Type localVarReturnType = new TypeToken<AutotaskSettings>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class PatchSettingsRequestBuilder {
        private final String UUID;
        private Boolean automaticTicketing;
        private List<Integer> companyTypeIds;

        private PatchSettingsRequestBuilder(String UUID) {
            this.UUID = UUID;
        }

        /**
         * Set automaticTicketing
         * @param automaticTicketing Determine whether Autotask uses automatic ticketing (optional)
         * @return PatchSettingsRequestBuilder
         */
        public PatchSettingsRequestBuilder automaticTicketing(Boolean automaticTicketing) {
            this.automaticTicketing = automaticTicketing;
            return this;
        }
        
        /**
         * Set companyTypeIds
         * @param companyTypeIds The array of Autotask companyType IDs applicable to the Provider. (optional)
         * @return PatchSettingsRequestBuilder
         */
        public PatchSettingsRequestBuilder companyTypeIds(List<Integer> companyTypeIds) {
            this.companyTypeIds = companyTypeIds;
            return this;
        }
        
        /**
         * Build call for patchSettings
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
            AutotaskSettingsPatchReq autotaskSettingsPatchReq = buildBodyParams();
            return patchSettingsCall(UUID, autotaskSettingsPatchReq, _callback);
        }

        private AutotaskSettingsPatchReq buildBodyParams() {
            AutotaskSettingsPatchReq autotaskSettingsPatchReq = new AutotaskSettingsPatchReq();
            autotaskSettingsPatchReq.automaticTicketing(this.automaticTicketing);
            autotaskSettingsPatchReq.companyTypeIds(this.companyTypeIds);
            return autotaskSettingsPatchReq;
        }

        /**
         * Execute patchSettings request
         * @return AutotaskSettings
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public AutotaskSettings execute() throws ApiException {
            AutotaskSettingsPatchReq autotaskSettingsPatchReq = buildBodyParams();
            ApiResponse<AutotaskSettings> localVarResp = patchSettingsWithHttpInfo(UUID, autotaskSettingsPatchReq);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute patchSettings request with HTTP info returned
         * @return ApiResponse&lt;AutotaskSettings&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<AutotaskSettings> executeWithHttpInfo() throws ApiException {
            AutotaskSettingsPatchReq autotaskSettingsPatchReq = buildBodyParams();
            return patchSettingsWithHttpInfo(UUID, autotaskSettingsPatchReq);
        }

        /**
         * Execute patchSettings request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AutotaskSettings> _callback) throws ApiException {
            AutotaskSettingsPatchReq autotaskSettingsPatchReq = buildBodyParams();
            return patchSettingsAsync(UUID, autotaskSettingsPatchReq, _callback);
        }
    }

    /**
     * Create, edit, and/or delete Autotask Integration settings
     * Create, edit, and/or delete Autotask settings. You must be associated to the same provider as the Autotask integration to use this endpoint.
     * @param UUID  (required)
     * @return PatchSettingsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public PatchSettingsRequestBuilder patchSettings(String UUID) throws IllegalArgumentException {
        if (UUID == null) throw new IllegalArgumentException("\"UUID\" is required but got null");
            

        return new PatchSettingsRequestBuilder(UUID);
    }
    private okhttp3.Call patchSettings_0Call(String UUID, ConnectWiseSettingsPatchReq connectWiseSettingsPatchReq, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = connectWiseSettingsPatchReq;

        // create path and map variables
        String localVarPath = "/integrations/connectwise/{UUID}/settings"
            .replace("{" + "UUID" + "}", localVarApiClient.escapeString(UUID.toString()));

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
        return localVarApiClient.buildCall(basePath, localVarPath, "PATCH", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call patchSettings_0ValidateBeforeCall(String UUID, ConnectWiseSettingsPatchReq connectWiseSettingsPatchReq, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'UUID' is set
        if (UUID == null) {
            throw new ApiException("Missing the required parameter 'UUID' when calling patchSettings_0(Async)");
        }

        return patchSettings_0Call(UUID, connectWiseSettingsPatchReq, _callback);

    }


    private ApiResponse<ConnectWiseSettings> patchSettings_0WithHttpInfo(String UUID, ConnectWiseSettingsPatchReq connectWiseSettingsPatchReq) throws ApiException {
        okhttp3.Call localVarCall = patchSettings_0ValidateBeforeCall(UUID, connectWiseSettingsPatchReq, null);
        Type localVarReturnType = new TypeToken<ConnectWiseSettings>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call patchSettings_0Async(String UUID, ConnectWiseSettingsPatchReq connectWiseSettingsPatchReq, final ApiCallback<ConnectWiseSettings> _callback) throws ApiException {

        okhttp3.Call localVarCall = patchSettings_0ValidateBeforeCall(UUID, connectWiseSettingsPatchReq, _callback);
        Type localVarReturnType = new TypeToken<ConnectWiseSettings>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class PatchSettings0RequestBuilder {
        private final String UUID;
        private Boolean automaticTicketing;
        private List<Integer> companyTypeIds;

        private PatchSettings0RequestBuilder(String UUID) {
            this.UUID = UUID;
        }

        /**
         * Set automaticTicketing
         * @param automaticTicketing Determine whether ConnectWise uses automatic ticketing (optional)
         * @return PatchSettings0RequestBuilder
         */
        public PatchSettings0RequestBuilder automaticTicketing(Boolean automaticTicketing) {
            this.automaticTicketing = automaticTicketing;
            return this;
        }
        
        /**
         * Set companyTypeIds
         * @param companyTypeIds The array of ConnectWise companyType IDs applicable to the Provider. (optional)
         * @return PatchSettings0RequestBuilder
         */
        public PatchSettings0RequestBuilder companyTypeIds(List<Integer> companyTypeIds) {
            this.companyTypeIds = companyTypeIds;
            return this;
        }
        
        /**
         * Build call for patchSettings_0
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
            ConnectWiseSettingsPatchReq connectWiseSettingsPatchReq = buildBodyParams();
            return patchSettings_0Call(UUID, connectWiseSettingsPatchReq, _callback);
        }

        private ConnectWiseSettingsPatchReq buildBodyParams() {
            ConnectWiseSettingsPatchReq connectWiseSettingsPatchReq = new ConnectWiseSettingsPatchReq();
            connectWiseSettingsPatchReq.automaticTicketing(this.automaticTicketing);
            connectWiseSettingsPatchReq.companyTypeIds(this.companyTypeIds);
            return connectWiseSettingsPatchReq;
        }

        /**
         * Execute patchSettings_0 request
         * @return ConnectWiseSettings
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ConnectWiseSettings execute() throws ApiException {
            ConnectWiseSettingsPatchReq connectWiseSettingsPatchReq = buildBodyParams();
            ApiResponse<ConnectWiseSettings> localVarResp = patchSettings_0WithHttpInfo(UUID, connectWiseSettingsPatchReq);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute patchSettings_0 request with HTTP info returned
         * @return ApiResponse&lt;ConnectWiseSettings&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<ConnectWiseSettings> executeWithHttpInfo() throws ApiException {
            ConnectWiseSettingsPatchReq connectWiseSettingsPatchReq = buildBodyParams();
            return patchSettings_0WithHttpInfo(UUID, connectWiseSettingsPatchReq);
        }

        /**
         * Execute patchSettings_0 request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ConnectWiseSettings> _callback) throws ApiException {
            ConnectWiseSettingsPatchReq connectWiseSettingsPatchReq = buildBodyParams();
            return patchSettings_0Async(UUID, connectWiseSettingsPatchReq, _callback);
        }
    }

    /**
     * Create, edit, and/or delete ConnectWise Integration settings
     * Create, edit, and/or delete ConnectWiseIntegration settings. You must be associated to the same provider as the ConnectWise integration to use this endpoint.
     * @param UUID  (required)
     * @return PatchSettings0RequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public PatchSettings0RequestBuilder patchSettings_0(String UUID) throws IllegalArgumentException {
        if (UUID == null) throw new IllegalArgumentException("\"UUID\" is required but got null");
            

        return new PatchSettings0RequestBuilder(UUID);
    }
    private okhttp3.Call patchSettings_1Call(String UUID, SyncroSettingsPatchReq syncroSettingsPatchReq, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = syncroSettingsPatchReq;

        // create path and map variables
        String localVarPath = "/integrations/syncro/{UUID}/settings"
            .replace("{" + "UUID" + "}", localVarApiClient.escapeString(UUID.toString()));

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
        return localVarApiClient.buildCall(basePath, localVarPath, "PATCH", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call patchSettings_1ValidateBeforeCall(String UUID, SyncroSettingsPatchReq syncroSettingsPatchReq, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'UUID' is set
        if (UUID == null) {
            throw new ApiException("Missing the required parameter 'UUID' when calling patchSettings_1(Async)");
        }

        return patchSettings_1Call(UUID, syncroSettingsPatchReq, _callback);

    }


    private ApiResponse<SyncroSettings> patchSettings_1WithHttpInfo(String UUID, SyncroSettingsPatchReq syncroSettingsPatchReq) throws ApiException {
        okhttp3.Call localVarCall = patchSettings_1ValidateBeforeCall(UUID, syncroSettingsPatchReq, null);
        Type localVarReturnType = new TypeToken<SyncroSettings>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call patchSettings_1Async(String UUID, SyncroSettingsPatchReq syncroSettingsPatchReq, final ApiCallback<SyncroSettings> _callback) throws ApiException {

        okhttp3.Call localVarCall = patchSettings_1ValidateBeforeCall(UUID, syncroSettingsPatchReq, _callback);
        Type localVarReturnType = new TypeToken<SyncroSettings>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class PatchSettings1RequestBuilder {
        private final String UUID;
        private Boolean automaticTicketing;

        private PatchSettings1RequestBuilder(String UUID) {
            this.UUID = UUID;
        }

        /**
         * Set automaticTicketing
         * @param automaticTicketing Determine whether Syncro uses automatic ticketing (optional)
         * @return PatchSettings1RequestBuilder
         */
        public PatchSettings1RequestBuilder automaticTicketing(Boolean automaticTicketing) {
            this.automaticTicketing = automaticTicketing;
            return this;
        }
        
        /**
         * Build call for patchSettings_1
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
            SyncroSettingsPatchReq syncroSettingsPatchReq = buildBodyParams();
            return patchSettings_1Call(UUID, syncroSettingsPatchReq, _callback);
        }

        private SyncroSettingsPatchReq buildBodyParams() {
            SyncroSettingsPatchReq syncroSettingsPatchReq = new SyncroSettingsPatchReq();
            syncroSettingsPatchReq.automaticTicketing(this.automaticTicketing);
            return syncroSettingsPatchReq;
        }

        /**
         * Execute patchSettings_1 request
         * @return SyncroSettings
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public SyncroSettings execute() throws ApiException {
            SyncroSettingsPatchReq syncroSettingsPatchReq = buildBodyParams();
            ApiResponse<SyncroSettings> localVarResp = patchSettings_1WithHttpInfo(UUID, syncroSettingsPatchReq);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute patchSettings_1 request with HTTP info returned
         * @return ApiResponse&lt;SyncroSettings&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<SyncroSettings> executeWithHttpInfo() throws ApiException {
            SyncroSettingsPatchReq syncroSettingsPatchReq = buildBodyParams();
            return patchSettings_1WithHttpInfo(UUID, syncroSettingsPatchReq);
        }

        /**
         * Execute patchSettings_1 request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<SyncroSettings> _callback) throws ApiException {
            SyncroSettingsPatchReq syncroSettingsPatchReq = buildBodyParams();
            return patchSettings_1Async(UUID, syncroSettingsPatchReq, _callback);
        }
    }

    /**
     * Create, edit, and/or delete Syncro Integration settings
     * Create, edit, and/or delete SyncroIntegration settings. You must be associated to the same provider as the Syncro integration to use this endpoint.
     * @param UUID  (required)
     * @return PatchSettings1RequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public PatchSettings1RequestBuilder patchSettings_1(String UUID) throws IllegalArgumentException {
        if (UUID == null) throw new IllegalArgumentException("\"UUID\" is required but got null");
            

        return new PatchSettings1RequestBuilder(UUID);
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
    private okhttp3.Call removeAdministratorCall(String providerId, String id, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/providers/{provider_id}/administrators/{id}"
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
    private okhttp3.Call removeAdministratorValidateBeforeCall(String providerId, String id, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'providerId' is set
        if (providerId == null) {
            throw new ApiException("Missing the required parameter 'providerId' when calling removeAdministrator(Async)");
        }

        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling removeAdministrator(Async)");
        }

        return removeAdministratorCall(providerId, id, _callback);

    }


    private ApiResponse<Void> removeAdministratorWithHttpInfo(String providerId, String id) throws ApiException {
        okhttp3.Call localVarCall = removeAdministratorValidateBeforeCall(providerId, id, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call removeAdministratorAsync(String providerId, String id, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = removeAdministratorValidateBeforeCall(providerId, id, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class RemoveAdministratorRequestBuilder {
        private final String providerId;
        private final String id;

        private RemoveAdministratorRequestBuilder(String providerId, String id) {
            this.providerId = providerId;
            this.id = id;
        }

        /**
         * Build call for removeAdministrator
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
            return removeAdministratorCall(providerId, id, _callback);
        }


        /**
         * Execute removeAdministrator request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            removeAdministratorWithHttpInfo(providerId, id);
        }

        /**
         * Execute removeAdministrator request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            return removeAdministratorWithHttpInfo(providerId, id);
        }

        /**
         * Execute removeAdministrator request (asynchronously)
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
            return removeAdministratorAsync(providerId, id, _callback);
        }
    }

    /**
     * Delete Provider Administrator
     * This endpoint removes an Administrator associated with the Provider. You must be associated with the provider to use this route.
     * @param providerId  (required)
     * @param id  (required)
     * @return RemoveAdministratorRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> No Content </td><td>  -  </td></tr>
     </table>
     */
    public RemoveAdministratorRequestBuilder removeAdministrator(String providerId, String id) throws IllegalArgumentException {
        if (providerId == null) throw new IllegalArgumentException("\"providerId\" is required but got null");
            

        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new RemoveAdministratorRequestBuilder(providerId, id);
    }
    private okhttp3.Call retrieveAdditionsCall(String UUID, String agreementID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/integrations/connectwise/{UUID}/agreements/{agreement_ID}/additions"
            .replace("{" + "UUID" + "}", localVarApiClient.escapeString(UUID.toString()))
            .replace("{" + "agreement_ID" + "}", localVarApiClient.escapeString(agreementID.toString()));

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
    private okhttp3.Call retrieveAdditionsValidateBeforeCall(String UUID, String agreementID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'UUID' is set
        if (UUID == null) {
            throw new ApiException("Missing the required parameter 'UUID' when calling retrieveAdditions(Async)");
        }

        // verify the required parameter 'agreementID' is set
        if (agreementID == null) {
            throw new ApiException("Missing the required parameter 'agreementID' when calling retrieveAdditions(Async)");
        }

        return retrieveAdditionsCall(UUID, agreementID, fields, filter, limit, skip, sort, _callback);

    }


    private ApiResponse<ConnectwiseRetrieveAdditionsResponse> retrieveAdditionsWithHttpInfo(String UUID, String agreementID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort) throws ApiException {
        okhttp3.Call localVarCall = retrieveAdditionsValidateBeforeCall(UUID, agreementID, fields, filter, limit, skip, sort, null);
        Type localVarReturnType = new TypeToken<ConnectwiseRetrieveAdditionsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call retrieveAdditionsAsync(String UUID, String agreementID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback<ConnectwiseRetrieveAdditionsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = retrieveAdditionsValidateBeforeCall(UUID, agreementID, fields, filter, limit, skip, sort, _callback);
        Type localVarReturnType = new TypeToken<ConnectwiseRetrieveAdditionsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class RetrieveAdditionsRequestBuilder {
        private final String UUID;
        private final String agreementID;
        private List<String> fields;
        private List<String> filter;
        private Integer limit;
        private Integer skip;
        private List<String> sort;

        private RetrieveAdditionsRequestBuilder(String UUID, String agreementID) {
            this.UUID = UUID;
            this.agreementID = agreementID;
        }

        /**
         * Set fields
         * @param fields The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  (optional)
         * @return RetrieveAdditionsRequestBuilder
         */
        public RetrieveAdditionsRequestBuilder fields(List<String> fields) {
            this.fields = fields;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return RetrieveAdditionsRequestBuilder
         */
        public RetrieveAdditionsRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return RetrieveAdditionsRequestBuilder
         */
        public RetrieveAdditionsRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return RetrieveAdditionsRequestBuilder
         */
        public RetrieveAdditionsRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  (optional)
         * @return RetrieveAdditionsRequestBuilder
         */
        public RetrieveAdditionsRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Build call for retrieveAdditions
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
            return retrieveAdditionsCall(UUID, agreementID, fields, filter, limit, skip, sort, _callback);
        }


        /**
         * Execute retrieveAdditions request
         * @return ConnectwiseRetrieveAdditionsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ConnectwiseRetrieveAdditionsResponse execute() throws ApiException {
            ApiResponse<ConnectwiseRetrieveAdditionsResponse> localVarResp = retrieveAdditionsWithHttpInfo(UUID, agreementID, fields, filter, limit, skip, sort);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute retrieveAdditions request with HTTP info returned
         * @return ApiResponse&lt;ConnectwiseRetrieveAdditionsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<ConnectwiseRetrieveAdditionsResponse> executeWithHttpInfo() throws ApiException {
            return retrieveAdditionsWithHttpInfo(UUID, agreementID, fields, filter, limit, skip, sort);
        }

        /**
         * Execute retrieveAdditions request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ConnectwiseRetrieveAdditionsResponse> _callback) throws ApiException {
            return retrieveAdditionsAsync(UUID, agreementID, fields, filter, limit, skip, sort, _callback);
        }
    }

    /**
     * Retrieve ConnectWise Additions
     * Retrieves a list of ConnectWise additions for the given ConnectWise id and Agreement id. You must be associated to the same provider as the ConnectWise integration to use this endpoint.
     * @param UUID  (required)
     * @param agreementID  (required)
     * @return RetrieveAdditionsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public RetrieveAdditionsRequestBuilder retrieveAdditions(String UUID, String agreementID) throws IllegalArgumentException {
        if (UUID == null) throw new IllegalArgumentException("\"UUID\" is required but got null");
            

        if (agreementID == null) throw new IllegalArgumentException("\"agreementID\" is required but got null");
            

        return new RetrieveAdditionsRequestBuilder(UUID, agreementID);
    }
    private okhttp3.Call retrieveAgreementsCall(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/integrations/connectwise/{UUID}/agreements"
            .replace("{" + "UUID" + "}", localVarApiClient.escapeString(UUID.toString()));

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
    private okhttp3.Call retrieveAgreementsValidateBeforeCall(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'UUID' is set
        if (UUID == null) {
            throw new ApiException("Missing the required parameter 'UUID' when calling retrieveAgreements(Async)");
        }

        return retrieveAgreementsCall(UUID, fields, filter, limit, skip, sort, _callback);

    }


    private ApiResponse<ConnectwiseRetrieveAgreementsResponse> retrieveAgreementsWithHttpInfo(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort) throws ApiException {
        okhttp3.Call localVarCall = retrieveAgreementsValidateBeforeCall(UUID, fields, filter, limit, skip, sort, null);
        Type localVarReturnType = new TypeToken<ConnectwiseRetrieveAgreementsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call retrieveAgreementsAsync(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback<ConnectwiseRetrieveAgreementsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = retrieveAgreementsValidateBeforeCall(UUID, fields, filter, limit, skip, sort, _callback);
        Type localVarReturnType = new TypeToken<ConnectwiseRetrieveAgreementsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class RetrieveAgreementsRequestBuilder {
        private final String UUID;
        private List<String> fields;
        private List<String> filter;
        private Integer limit;
        private Integer skip;
        private List<String> sort;

        private RetrieveAgreementsRequestBuilder(String UUID) {
            this.UUID = UUID;
        }

        /**
         * Set fields
         * @param fields The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  (optional)
         * @return RetrieveAgreementsRequestBuilder
         */
        public RetrieveAgreementsRequestBuilder fields(List<String> fields) {
            this.fields = fields;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return RetrieveAgreementsRequestBuilder
         */
        public RetrieveAgreementsRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return RetrieveAgreementsRequestBuilder
         */
        public RetrieveAgreementsRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return RetrieveAgreementsRequestBuilder
         */
        public RetrieveAgreementsRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  (optional)
         * @return RetrieveAgreementsRequestBuilder
         */
        public RetrieveAgreementsRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Build call for retrieveAgreements
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
            return retrieveAgreementsCall(UUID, fields, filter, limit, skip, sort, _callback);
        }


        /**
         * Execute retrieveAgreements request
         * @return ConnectwiseRetrieveAgreementsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ConnectwiseRetrieveAgreementsResponse execute() throws ApiException {
            ApiResponse<ConnectwiseRetrieveAgreementsResponse> localVarResp = retrieveAgreementsWithHttpInfo(UUID, fields, filter, limit, skip, sort);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute retrieveAgreements request with HTTP info returned
         * @return ApiResponse&lt;ConnectwiseRetrieveAgreementsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<ConnectwiseRetrieveAgreementsResponse> executeWithHttpInfo() throws ApiException {
            return retrieveAgreementsWithHttpInfo(UUID, fields, filter, limit, skip, sort);
        }

        /**
         * Execute retrieveAgreements request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ConnectwiseRetrieveAgreementsResponse> _callback) throws ApiException {
            return retrieveAgreementsAsync(UUID, fields, filter, limit, skip, sort, _callback);
        }
    }

    /**
     * Retrieve ConnectWise Agreements
     * Retrieves a list of ConnectWise agreements for the given ConnectWise id. You must be associated to the same provider as the ConnectWise integration to use this endpoint.
     * @param UUID  (required)
     * @return RetrieveAgreementsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public RetrieveAgreementsRequestBuilder retrieveAgreements(String UUID) throws IllegalArgumentException {
        if (UUID == null) throw new IllegalArgumentException("\"UUID\" is required but got null");
            

        return new RetrieveAgreementsRequestBuilder(UUID);
    }
    private okhttp3.Call retrieveAlertsCall(String providerId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/providers/{provider_id}/integrations/ticketing/alerts"
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
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "x-api-key" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call retrieveAlertsValidateBeforeCall(String providerId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'providerId' is set
        if (providerId == null) {
            throw new ApiException("Missing the required parameter 'providerId' when calling retrieveAlerts(Async)");
        }

        return retrieveAlertsCall(providerId, _callback);

    }


    private ApiResponse<TicketingIntegrationAlertsResp> retrieveAlertsWithHttpInfo(String providerId) throws ApiException {
        okhttp3.Call localVarCall = retrieveAlertsValidateBeforeCall(providerId, null);
        Type localVarReturnType = new TypeToken<TicketingIntegrationAlertsResp>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call retrieveAlertsAsync(String providerId, final ApiCallback<TicketingIntegrationAlertsResp> _callback) throws ApiException {

        okhttp3.Call localVarCall = retrieveAlertsValidateBeforeCall(providerId, _callback);
        Type localVarReturnType = new TypeToken<TicketingIntegrationAlertsResp>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class RetrieveAlertsRequestBuilder {
        private final String providerId;

        private RetrieveAlertsRequestBuilder(String providerId) {
            this.providerId = providerId;
        }

        /**
         * Build call for retrieveAlerts
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
            return retrieveAlertsCall(providerId, _callback);
        }


        /**
         * Execute retrieveAlerts request
         * @return TicketingIntegrationAlertsResp
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public TicketingIntegrationAlertsResp execute() throws ApiException {
            ApiResponse<TicketingIntegrationAlertsResp> localVarResp = retrieveAlertsWithHttpInfo(providerId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute retrieveAlerts request with HTTP info returned
         * @return ApiResponse&lt;TicketingIntegrationAlertsResp&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<TicketingIntegrationAlertsResp> executeWithHttpInfo() throws ApiException {
            return retrieveAlertsWithHttpInfo(providerId);
        }

        /**
         * Execute retrieveAlerts request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<TicketingIntegrationAlertsResp> _callback) throws ApiException {
            return retrieveAlertsAsync(providerId, _callback);
        }
    }

    /**
     * Get all ticketing alerts available for a provider&#39;s ticketing integration.
     * Get all ticketing alerts available for a provider&#39;s ticketing integration.
     * @param providerId  (required)
     * @return RetrieveAlertsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public RetrieveAlertsRequestBuilder retrieveAlerts(String providerId) throws IllegalArgumentException {
        if (providerId == null) throw new IllegalArgumentException("\"providerId\" is required but got null");
            

        return new RetrieveAlertsRequestBuilder(providerId);
    }
    private okhttp3.Call retrieveAllAlertConfigurationOptionsCall(String providerId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/providers/{provider_id}/integrations/autotask/alerts/configuration/options"
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
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "x-api-key" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call retrieveAllAlertConfigurationOptionsValidateBeforeCall(String providerId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'providerId' is set
        if (providerId == null) {
            throw new ApiException("Missing the required parameter 'providerId' when calling retrieveAllAlertConfigurationOptions(Async)");
        }

        return retrieveAllAlertConfigurationOptionsCall(providerId, _callback);

    }


    private ApiResponse<AutotaskTicketingAlertConfigurationOptions> retrieveAllAlertConfigurationOptionsWithHttpInfo(String providerId) throws ApiException {
        okhttp3.Call localVarCall = retrieveAllAlertConfigurationOptionsValidateBeforeCall(providerId, null);
        Type localVarReturnType = new TypeToken<AutotaskTicketingAlertConfigurationOptions>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call retrieveAllAlertConfigurationOptionsAsync(String providerId, final ApiCallback<AutotaskTicketingAlertConfigurationOptions> _callback) throws ApiException {

        okhttp3.Call localVarCall = retrieveAllAlertConfigurationOptionsValidateBeforeCall(providerId, _callback);
        Type localVarReturnType = new TypeToken<AutotaskTicketingAlertConfigurationOptions>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class RetrieveAllAlertConfigurationOptionsRequestBuilder {
        private final String providerId;

        private RetrieveAllAlertConfigurationOptionsRequestBuilder(String providerId) {
            this.providerId = providerId;
        }

        /**
         * Build call for retrieveAllAlertConfigurationOptions
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
            return retrieveAllAlertConfigurationOptionsCall(providerId, _callback);
        }


        /**
         * Execute retrieveAllAlertConfigurationOptions request
         * @return AutotaskTicketingAlertConfigurationOptions
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public AutotaskTicketingAlertConfigurationOptions execute() throws ApiException {
            ApiResponse<AutotaskTicketingAlertConfigurationOptions> localVarResp = retrieveAllAlertConfigurationOptionsWithHttpInfo(providerId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute retrieveAllAlertConfigurationOptions request with HTTP info returned
         * @return ApiResponse&lt;AutotaskTicketingAlertConfigurationOptions&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<AutotaskTicketingAlertConfigurationOptions> executeWithHttpInfo() throws ApiException {
            return retrieveAllAlertConfigurationOptionsWithHttpInfo(providerId);
        }

        /**
         * Execute retrieveAllAlertConfigurationOptions request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AutotaskTicketingAlertConfigurationOptions> _callback) throws ApiException {
            return retrieveAllAlertConfigurationOptionsAsync(providerId, _callback);
        }
    }

    /**
     * Get all Autotask ticketing alert configuration options for a provider
     * Get all Autotask ticketing alert configuration options for a provider.
     * @param providerId  (required)
     * @return RetrieveAllAlertConfigurationOptionsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public RetrieveAllAlertConfigurationOptionsRequestBuilder retrieveAllAlertConfigurationOptions(String providerId) throws IllegalArgumentException {
        if (providerId == null) throw new IllegalArgumentException("\"providerId\" is required but got null");
            

        return new RetrieveAllAlertConfigurationOptionsRequestBuilder(providerId);
    }
    private okhttp3.Call retrieveAllAlertConfigurationOptions_0Call(String providerId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/providers/{provider_id}/integrations/connectwise/alerts/configuration/options"
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
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "x-api-key" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call retrieveAllAlertConfigurationOptions_0ValidateBeforeCall(String providerId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'providerId' is set
        if (providerId == null) {
            throw new ApiException("Missing the required parameter 'providerId' when calling retrieveAllAlertConfigurationOptions_0(Async)");
        }

        return retrieveAllAlertConfigurationOptions_0Call(providerId, _callback);

    }


    private ApiResponse<ConnectWiseTicketingAlertConfigurationOptions> retrieveAllAlertConfigurationOptions_0WithHttpInfo(String providerId) throws ApiException {
        okhttp3.Call localVarCall = retrieveAllAlertConfigurationOptions_0ValidateBeforeCall(providerId, null);
        Type localVarReturnType = new TypeToken<ConnectWiseTicketingAlertConfigurationOptions>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call retrieveAllAlertConfigurationOptions_0Async(String providerId, final ApiCallback<ConnectWiseTicketingAlertConfigurationOptions> _callback) throws ApiException {

        okhttp3.Call localVarCall = retrieveAllAlertConfigurationOptions_0ValidateBeforeCall(providerId, _callback);
        Type localVarReturnType = new TypeToken<ConnectWiseTicketingAlertConfigurationOptions>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class RetrieveAllAlertConfigurationOptions0RequestBuilder {
        private final String providerId;

        private RetrieveAllAlertConfigurationOptions0RequestBuilder(String providerId) {
            this.providerId = providerId;
        }

        /**
         * Build call for retrieveAllAlertConfigurationOptions_0
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
            return retrieveAllAlertConfigurationOptions_0Call(providerId, _callback);
        }


        /**
         * Execute retrieveAllAlertConfigurationOptions_0 request
         * @return ConnectWiseTicketingAlertConfigurationOptions
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ConnectWiseTicketingAlertConfigurationOptions execute() throws ApiException {
            ApiResponse<ConnectWiseTicketingAlertConfigurationOptions> localVarResp = retrieveAllAlertConfigurationOptions_0WithHttpInfo(providerId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute retrieveAllAlertConfigurationOptions_0 request with HTTP info returned
         * @return ApiResponse&lt;ConnectWiseTicketingAlertConfigurationOptions&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<ConnectWiseTicketingAlertConfigurationOptions> executeWithHttpInfo() throws ApiException {
            return retrieveAllAlertConfigurationOptions_0WithHttpInfo(providerId);
        }

        /**
         * Execute retrieveAllAlertConfigurationOptions_0 request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ConnectWiseTicketingAlertConfigurationOptions> _callback) throws ApiException {
            return retrieveAllAlertConfigurationOptions_0Async(providerId, _callback);
        }
    }

    /**
     * Get all ConnectWise ticketing alert configuration options for a provider
     * Get all ConnectWise ticketing alert configuration options for a provider.
     * @param providerId  (required)
     * @return RetrieveAllAlertConfigurationOptions0RequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public RetrieveAllAlertConfigurationOptions0RequestBuilder retrieveAllAlertConfigurationOptions_0(String providerId) throws IllegalArgumentException {
        if (providerId == null) throw new IllegalArgumentException("\"providerId\" is required but got null");
            

        return new RetrieveAllAlertConfigurationOptions0RequestBuilder(providerId);
    }
    private okhttp3.Call retrieveAllAlertConfigurationOptions_1Call(String providerId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/providers/{provider_id}/integrations/syncro/alerts/configuration/options"
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
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "x-api-key" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call retrieveAllAlertConfigurationOptions_1ValidateBeforeCall(String providerId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'providerId' is set
        if (providerId == null) {
            throw new ApiException("Missing the required parameter 'providerId' when calling retrieveAllAlertConfigurationOptions_1(Async)");
        }

        return retrieveAllAlertConfigurationOptions_1Call(providerId, _callback);

    }


    private ApiResponse<SyncroTicketingAlertConfigurationOptions> retrieveAllAlertConfigurationOptions_1WithHttpInfo(String providerId) throws ApiException {
        okhttp3.Call localVarCall = retrieveAllAlertConfigurationOptions_1ValidateBeforeCall(providerId, null);
        Type localVarReturnType = new TypeToken<SyncroTicketingAlertConfigurationOptions>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call retrieveAllAlertConfigurationOptions_1Async(String providerId, final ApiCallback<SyncroTicketingAlertConfigurationOptions> _callback) throws ApiException {

        okhttp3.Call localVarCall = retrieveAllAlertConfigurationOptions_1ValidateBeforeCall(providerId, _callback);
        Type localVarReturnType = new TypeToken<SyncroTicketingAlertConfigurationOptions>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class RetrieveAllAlertConfigurationOptions1RequestBuilder {
        private final String providerId;

        private RetrieveAllAlertConfigurationOptions1RequestBuilder(String providerId) {
            this.providerId = providerId;
        }

        /**
         * Build call for retrieveAllAlertConfigurationOptions_1
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
            return retrieveAllAlertConfigurationOptions_1Call(providerId, _callback);
        }


        /**
         * Execute retrieveAllAlertConfigurationOptions_1 request
         * @return SyncroTicketingAlertConfigurationOptions
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public SyncroTicketingAlertConfigurationOptions execute() throws ApiException {
            ApiResponse<SyncroTicketingAlertConfigurationOptions> localVarResp = retrieveAllAlertConfigurationOptions_1WithHttpInfo(providerId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute retrieveAllAlertConfigurationOptions_1 request with HTTP info returned
         * @return ApiResponse&lt;SyncroTicketingAlertConfigurationOptions&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<SyncroTicketingAlertConfigurationOptions> executeWithHttpInfo() throws ApiException {
            return retrieveAllAlertConfigurationOptions_1WithHttpInfo(providerId);
        }

        /**
         * Execute retrieveAllAlertConfigurationOptions_1 request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<SyncroTicketingAlertConfigurationOptions> _callback) throws ApiException {
            return retrieveAllAlertConfigurationOptions_1Async(providerId, _callback);
        }
    }

    /**
     * Get all Syncro ticketing alert configuration options for a provider
     * Get all Syncro ticketing alert configuration options for a provider.
     * @param providerId  (required)
     * @return RetrieveAllAlertConfigurationOptions1RequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public RetrieveAllAlertConfigurationOptions1RequestBuilder retrieveAllAlertConfigurationOptions_1(String providerId) throws IllegalArgumentException {
        if (providerId == null) throw new IllegalArgumentException("\"providerId\" is required but got null");
            

        return new RetrieveAllAlertConfigurationOptions1RequestBuilder(providerId);
    }
    private okhttp3.Call retrieveAllAlertConfigurationsCall(String providerId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/providers/{provider_id}/integrations/autotask/alerts/configuration"
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
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "x-api-key" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call retrieveAllAlertConfigurationsValidateBeforeCall(String providerId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'providerId' is set
        if (providerId == null) {
            throw new ApiException("Missing the required parameter 'providerId' when calling retrieveAllAlertConfigurations(Async)");
        }

        return retrieveAllAlertConfigurationsCall(providerId, _callback);

    }


    private ApiResponse<AutotaskTicketingAlertConfigurationList> retrieveAllAlertConfigurationsWithHttpInfo(String providerId) throws ApiException {
        okhttp3.Call localVarCall = retrieveAllAlertConfigurationsValidateBeforeCall(providerId, null);
        Type localVarReturnType = new TypeToken<AutotaskTicketingAlertConfigurationList>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call retrieveAllAlertConfigurationsAsync(String providerId, final ApiCallback<AutotaskTicketingAlertConfigurationList> _callback) throws ApiException {

        okhttp3.Call localVarCall = retrieveAllAlertConfigurationsValidateBeforeCall(providerId, _callback);
        Type localVarReturnType = new TypeToken<AutotaskTicketingAlertConfigurationList>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class RetrieveAllAlertConfigurationsRequestBuilder {
        private final String providerId;

        private RetrieveAllAlertConfigurationsRequestBuilder(String providerId) {
            this.providerId = providerId;
        }

        /**
         * Build call for retrieveAllAlertConfigurations
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
            return retrieveAllAlertConfigurationsCall(providerId, _callback);
        }


        /**
         * Execute retrieveAllAlertConfigurations request
         * @return AutotaskTicketingAlertConfigurationList
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public AutotaskTicketingAlertConfigurationList execute() throws ApiException {
            ApiResponse<AutotaskTicketingAlertConfigurationList> localVarResp = retrieveAllAlertConfigurationsWithHttpInfo(providerId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute retrieveAllAlertConfigurations request with HTTP info returned
         * @return ApiResponse&lt;AutotaskTicketingAlertConfigurationList&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<AutotaskTicketingAlertConfigurationList> executeWithHttpInfo() throws ApiException {
            return retrieveAllAlertConfigurationsWithHttpInfo(providerId);
        }

        /**
         * Execute retrieveAllAlertConfigurations request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AutotaskTicketingAlertConfigurationList> _callback) throws ApiException {
            return retrieveAllAlertConfigurationsAsync(providerId, _callback);
        }
    }

    /**
     * Get all Autotask ticketing alert configurations for a provider
     * Get all Autotask ticketing alert configurations for a provider.
     * @param providerId  (required)
     * @return RetrieveAllAlertConfigurationsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public RetrieveAllAlertConfigurationsRequestBuilder retrieveAllAlertConfigurations(String providerId) throws IllegalArgumentException {
        if (providerId == null) throw new IllegalArgumentException("\"providerId\" is required but got null");
            

        return new RetrieveAllAlertConfigurationsRequestBuilder(providerId);
    }
    private okhttp3.Call retrieveAllAlertConfigurations_0Call(String providerId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/providers/{provider_id}/integrations/connectwise/alerts/configuration"
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
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "x-api-key" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call retrieveAllAlertConfigurations_0ValidateBeforeCall(String providerId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'providerId' is set
        if (providerId == null) {
            throw new ApiException("Missing the required parameter 'providerId' when calling retrieveAllAlertConfigurations_0(Async)");
        }

        return retrieveAllAlertConfigurations_0Call(providerId, _callback);

    }


    private ApiResponse<ConnectWiseTicketingAlertConfigurationList> retrieveAllAlertConfigurations_0WithHttpInfo(String providerId) throws ApiException {
        okhttp3.Call localVarCall = retrieveAllAlertConfigurations_0ValidateBeforeCall(providerId, null);
        Type localVarReturnType = new TypeToken<ConnectWiseTicketingAlertConfigurationList>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call retrieveAllAlertConfigurations_0Async(String providerId, final ApiCallback<ConnectWiseTicketingAlertConfigurationList> _callback) throws ApiException {

        okhttp3.Call localVarCall = retrieveAllAlertConfigurations_0ValidateBeforeCall(providerId, _callback);
        Type localVarReturnType = new TypeToken<ConnectWiseTicketingAlertConfigurationList>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class RetrieveAllAlertConfigurations0RequestBuilder {
        private final String providerId;

        private RetrieveAllAlertConfigurations0RequestBuilder(String providerId) {
            this.providerId = providerId;
        }

        /**
         * Build call for retrieveAllAlertConfigurations_0
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
            return retrieveAllAlertConfigurations_0Call(providerId, _callback);
        }


        /**
         * Execute retrieveAllAlertConfigurations_0 request
         * @return ConnectWiseTicketingAlertConfigurationList
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ConnectWiseTicketingAlertConfigurationList execute() throws ApiException {
            ApiResponse<ConnectWiseTicketingAlertConfigurationList> localVarResp = retrieveAllAlertConfigurations_0WithHttpInfo(providerId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute retrieveAllAlertConfigurations_0 request with HTTP info returned
         * @return ApiResponse&lt;ConnectWiseTicketingAlertConfigurationList&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<ConnectWiseTicketingAlertConfigurationList> executeWithHttpInfo() throws ApiException {
            return retrieveAllAlertConfigurations_0WithHttpInfo(providerId);
        }

        /**
         * Execute retrieveAllAlertConfigurations_0 request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ConnectWiseTicketingAlertConfigurationList> _callback) throws ApiException {
            return retrieveAllAlertConfigurations_0Async(providerId, _callback);
        }
    }

    /**
     * Get all ConnectWise ticketing alert configurations for a provider
     * Get all ConnectWise ticketing alert configurations for a provider.
     * @param providerId  (required)
     * @return RetrieveAllAlertConfigurations0RequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public RetrieveAllAlertConfigurations0RequestBuilder retrieveAllAlertConfigurations_0(String providerId) throws IllegalArgumentException {
        if (providerId == null) throw new IllegalArgumentException("\"providerId\" is required but got null");
            

        return new RetrieveAllAlertConfigurations0RequestBuilder(providerId);
    }
    private okhttp3.Call retrieveAllAlertConfigurations_1Call(String providerId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/providers/{provider_id}/integrations/syncro/alerts/configuration"
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
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "x-api-key" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call retrieveAllAlertConfigurations_1ValidateBeforeCall(String providerId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'providerId' is set
        if (providerId == null) {
            throw new ApiException("Missing the required parameter 'providerId' when calling retrieveAllAlertConfigurations_1(Async)");
        }

        return retrieveAllAlertConfigurations_1Call(providerId, _callback);

    }


    private ApiResponse<SyncroTicketingAlertConfigurationList> retrieveAllAlertConfigurations_1WithHttpInfo(String providerId) throws ApiException {
        okhttp3.Call localVarCall = retrieveAllAlertConfigurations_1ValidateBeforeCall(providerId, null);
        Type localVarReturnType = new TypeToken<SyncroTicketingAlertConfigurationList>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call retrieveAllAlertConfigurations_1Async(String providerId, final ApiCallback<SyncroTicketingAlertConfigurationList> _callback) throws ApiException {

        okhttp3.Call localVarCall = retrieveAllAlertConfigurations_1ValidateBeforeCall(providerId, _callback);
        Type localVarReturnType = new TypeToken<SyncroTicketingAlertConfigurationList>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class RetrieveAllAlertConfigurations1RequestBuilder {
        private final String providerId;

        private RetrieveAllAlertConfigurations1RequestBuilder(String providerId) {
            this.providerId = providerId;
        }

        /**
         * Build call for retrieveAllAlertConfigurations_1
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
            return retrieveAllAlertConfigurations_1Call(providerId, _callback);
        }


        /**
         * Execute retrieveAllAlertConfigurations_1 request
         * @return SyncroTicketingAlertConfigurationList
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public SyncroTicketingAlertConfigurationList execute() throws ApiException {
            ApiResponse<SyncroTicketingAlertConfigurationList> localVarResp = retrieveAllAlertConfigurations_1WithHttpInfo(providerId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute retrieveAllAlertConfigurations_1 request with HTTP info returned
         * @return ApiResponse&lt;SyncroTicketingAlertConfigurationList&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<SyncroTicketingAlertConfigurationList> executeWithHttpInfo() throws ApiException {
            return retrieveAllAlertConfigurations_1WithHttpInfo(providerId);
        }

        /**
         * Execute retrieveAllAlertConfigurations_1 request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<SyncroTicketingAlertConfigurationList> _callback) throws ApiException {
            return retrieveAllAlertConfigurations_1Async(providerId, _callback);
        }
    }

    /**
     * Get all Syncro ticketing alert configurations for a provider
     * Get all Syncro ticketing alert configurations for a provider.
     * @param providerId  (required)
     * @return RetrieveAllAlertConfigurations1RequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public RetrieveAllAlertConfigurations1RequestBuilder retrieveAllAlertConfigurations_1(String providerId) throws IllegalArgumentException {
        if (providerId == null) throw new IllegalArgumentException("\"providerId\" is required but got null");
            

        return new RetrieveAllAlertConfigurations1RequestBuilder(providerId);
    }
    private okhttp3.Call retrieveBillingMappingConfigurationOptionsCall(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/integrations/syncro/{UUID}/billing_mapping_configuration_options"
            .replace("{" + "UUID" + "}", localVarApiClient.escapeString(UUID.toString()));

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
    private okhttp3.Call retrieveBillingMappingConfigurationOptionsValidateBeforeCall(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'UUID' is set
        if (UUID == null) {
            throw new ApiException("Missing the required parameter 'UUID' when calling retrieveBillingMappingConfigurationOptions(Async)");
        }

        return retrieveBillingMappingConfigurationOptionsCall(UUID, fields, filter, limit, skip, sort, _callback);

    }


    private ApiResponse<SyncroBillingMappingConfigurationOptionsResp> retrieveBillingMappingConfigurationOptionsWithHttpInfo(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort) throws ApiException {
        okhttp3.Call localVarCall = retrieveBillingMappingConfigurationOptionsValidateBeforeCall(UUID, fields, filter, limit, skip, sort, null);
        Type localVarReturnType = new TypeToken<SyncroBillingMappingConfigurationOptionsResp>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call retrieveBillingMappingConfigurationOptionsAsync(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback<SyncroBillingMappingConfigurationOptionsResp> _callback) throws ApiException {

        okhttp3.Call localVarCall = retrieveBillingMappingConfigurationOptionsValidateBeforeCall(UUID, fields, filter, limit, skip, sort, _callback);
        Type localVarReturnType = new TypeToken<SyncroBillingMappingConfigurationOptionsResp>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class RetrieveBillingMappingConfigurationOptionsRequestBuilder {
        private final String UUID;
        private List<String> fields;
        private List<String> filter;
        private Integer limit;
        private Integer skip;
        private List<String> sort;

        private RetrieveBillingMappingConfigurationOptionsRequestBuilder(String UUID) {
            this.UUID = UUID;
        }

        /**
         * Set fields
         * @param fields The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  (optional)
         * @return RetrieveBillingMappingConfigurationOptionsRequestBuilder
         */
        public RetrieveBillingMappingConfigurationOptionsRequestBuilder fields(List<String> fields) {
            this.fields = fields;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return RetrieveBillingMappingConfigurationOptionsRequestBuilder
         */
        public RetrieveBillingMappingConfigurationOptionsRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return RetrieveBillingMappingConfigurationOptionsRequestBuilder
         */
        public RetrieveBillingMappingConfigurationOptionsRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return RetrieveBillingMappingConfigurationOptionsRequestBuilder
         */
        public RetrieveBillingMappingConfigurationOptionsRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  (optional)
         * @return RetrieveBillingMappingConfigurationOptionsRequestBuilder
         */
        public RetrieveBillingMappingConfigurationOptionsRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Build call for retrieveBillingMappingConfigurationOptions
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
            return retrieveBillingMappingConfigurationOptionsCall(UUID, fields, filter, limit, skip, sort, _callback);
        }


        /**
         * Execute retrieveBillingMappingConfigurationOptions request
         * @return SyncroBillingMappingConfigurationOptionsResp
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public SyncroBillingMappingConfigurationOptionsResp execute() throws ApiException {
            ApiResponse<SyncroBillingMappingConfigurationOptionsResp> localVarResp = retrieveBillingMappingConfigurationOptionsWithHttpInfo(UUID, fields, filter, limit, skip, sort);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute retrieveBillingMappingConfigurationOptions request with HTTP info returned
         * @return ApiResponse&lt;SyncroBillingMappingConfigurationOptionsResp&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<SyncroBillingMappingConfigurationOptionsResp> executeWithHttpInfo() throws ApiException {
            return retrieveBillingMappingConfigurationOptionsWithHttpInfo(UUID, fields, filter, limit, skip, sort);
        }

        /**
         * Execute retrieveBillingMappingConfigurationOptions request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<SyncroBillingMappingConfigurationOptionsResp> _callback) throws ApiException {
            return retrieveBillingMappingConfigurationOptionsAsync(UUID, fields, filter, limit, skip, sort, _callback);
        }
    }

    /**
     * Retrieve Syncro billing mappings dependencies
     * Retrieves a list of dependencies for Syncro billing mappings.
     * @param UUID  (required)
     * @return RetrieveBillingMappingConfigurationOptionsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public RetrieveBillingMappingConfigurationOptionsRequestBuilder retrieveBillingMappingConfigurationOptions(String UUID) throws IllegalArgumentException {
        if (UUID == null) throw new IllegalArgumentException("\"UUID\" is required but got null");
            

        return new RetrieveBillingMappingConfigurationOptionsRequestBuilder(UUID);
    }
    private okhttp3.Call retrieveCompaniesCall(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/integrations/autotask/{UUID}/companies"
            .replace("{" + "UUID" + "}", localVarApiClient.escapeString(UUID.toString()));

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
    private okhttp3.Call retrieveCompaniesValidateBeforeCall(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'UUID' is set
        if (UUID == null) {
            throw new ApiException("Missing the required parameter 'UUID' when calling retrieveCompanies(Async)");
        }

        return retrieveCompaniesCall(UUID, fields, filter, limit, skip, sort, _callback);

    }


    private ApiResponse<AutotaskCompanyResp> retrieveCompaniesWithHttpInfo(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort) throws ApiException {
        okhttp3.Call localVarCall = retrieveCompaniesValidateBeforeCall(UUID, fields, filter, limit, skip, sort, null);
        Type localVarReturnType = new TypeToken<AutotaskCompanyResp>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call retrieveCompaniesAsync(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback<AutotaskCompanyResp> _callback) throws ApiException {

        okhttp3.Call localVarCall = retrieveCompaniesValidateBeforeCall(UUID, fields, filter, limit, skip, sort, _callback);
        Type localVarReturnType = new TypeToken<AutotaskCompanyResp>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class RetrieveCompaniesRequestBuilder {
        private final String UUID;
        private List<String> fields;
        private List<String> filter;
        private Integer limit;
        private Integer skip;
        private List<String> sort;

        private RetrieveCompaniesRequestBuilder(String UUID) {
            this.UUID = UUID;
        }

        /**
         * Set fields
         * @param fields The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  (optional)
         * @return RetrieveCompaniesRequestBuilder
         */
        public RetrieveCompaniesRequestBuilder fields(List<String> fields) {
            this.fields = fields;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return RetrieveCompaniesRequestBuilder
         */
        public RetrieveCompaniesRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return RetrieveCompaniesRequestBuilder
         */
        public RetrieveCompaniesRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return RetrieveCompaniesRequestBuilder
         */
        public RetrieveCompaniesRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  (optional)
         * @return RetrieveCompaniesRequestBuilder
         */
        public RetrieveCompaniesRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Build call for retrieveCompanies
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
            return retrieveCompaniesCall(UUID, fields, filter, limit, skip, sort, _callback);
        }


        /**
         * Execute retrieveCompanies request
         * @return AutotaskCompanyResp
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public AutotaskCompanyResp execute() throws ApiException {
            ApiResponse<AutotaskCompanyResp> localVarResp = retrieveCompaniesWithHttpInfo(UUID, fields, filter, limit, skip, sort);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute retrieveCompanies request with HTTP info returned
         * @return ApiResponse&lt;AutotaskCompanyResp&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<AutotaskCompanyResp> executeWithHttpInfo() throws ApiException {
            return retrieveCompaniesWithHttpInfo(UUID, fields, filter, limit, skip, sort);
        }

        /**
         * Execute retrieveCompanies request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AutotaskCompanyResp> _callback) throws ApiException {
            return retrieveCompaniesAsync(UUID, fields, filter, limit, skip, sort, _callback);
        }
    }

    /**
     * Retrieve Autotask Companies
     * Retrieves a list of Autotask companies for the given Autotask id. You must be associated to the same provider as the Autotask integration to use this endpoint.
     * @param UUID  (required)
     * @return RetrieveCompaniesRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public RetrieveCompaniesRequestBuilder retrieveCompanies(String UUID) throws IllegalArgumentException {
        if (UUID == null) throw new IllegalArgumentException("\"UUID\" is required but got null");
            

        return new RetrieveCompaniesRequestBuilder(UUID);
    }
    private okhttp3.Call retrieveCompanies_0Call(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/integrations/connectwise/{UUID}/companies"
            .replace("{" + "UUID" + "}", localVarApiClient.escapeString(UUID.toString()));

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
    private okhttp3.Call retrieveCompanies_0ValidateBeforeCall(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'UUID' is set
        if (UUID == null) {
            throw new ApiException("Missing the required parameter 'UUID' when calling retrieveCompanies_0(Async)");
        }

        return retrieveCompanies_0Call(UUID, fields, filter, limit, skip, sort, _callback);

    }


    private ApiResponse<ConnectwiseCompanyResp> retrieveCompanies_0WithHttpInfo(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort) throws ApiException {
        okhttp3.Call localVarCall = retrieveCompanies_0ValidateBeforeCall(UUID, fields, filter, limit, skip, sort, null);
        Type localVarReturnType = new TypeToken<ConnectwiseCompanyResp>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call retrieveCompanies_0Async(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback<ConnectwiseCompanyResp> _callback) throws ApiException {

        okhttp3.Call localVarCall = retrieveCompanies_0ValidateBeforeCall(UUID, fields, filter, limit, skip, sort, _callback);
        Type localVarReturnType = new TypeToken<ConnectwiseCompanyResp>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class RetrieveCompanies0RequestBuilder {
        private final String UUID;
        private List<String> fields;
        private List<String> filter;
        private Integer limit;
        private Integer skip;
        private List<String> sort;

        private RetrieveCompanies0RequestBuilder(String UUID) {
            this.UUID = UUID;
        }

        /**
         * Set fields
         * @param fields The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  (optional)
         * @return RetrieveCompanies0RequestBuilder
         */
        public RetrieveCompanies0RequestBuilder fields(List<String> fields) {
            this.fields = fields;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return RetrieveCompanies0RequestBuilder
         */
        public RetrieveCompanies0RequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return RetrieveCompanies0RequestBuilder
         */
        public RetrieveCompanies0RequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return RetrieveCompanies0RequestBuilder
         */
        public RetrieveCompanies0RequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  (optional)
         * @return RetrieveCompanies0RequestBuilder
         */
        public RetrieveCompanies0RequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Build call for retrieveCompanies_0
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
            return retrieveCompanies_0Call(UUID, fields, filter, limit, skip, sort, _callback);
        }


        /**
         * Execute retrieveCompanies_0 request
         * @return ConnectwiseCompanyResp
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ConnectwiseCompanyResp execute() throws ApiException {
            ApiResponse<ConnectwiseCompanyResp> localVarResp = retrieveCompanies_0WithHttpInfo(UUID, fields, filter, limit, skip, sort);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute retrieveCompanies_0 request with HTTP info returned
         * @return ApiResponse&lt;ConnectwiseCompanyResp&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<ConnectwiseCompanyResp> executeWithHttpInfo() throws ApiException {
            return retrieveCompanies_0WithHttpInfo(UUID, fields, filter, limit, skip, sort);
        }

        /**
         * Execute retrieveCompanies_0 request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ConnectwiseCompanyResp> _callback) throws ApiException {
            return retrieveCompanies_0Async(UUID, fields, filter, limit, skip, sort, _callback);
        }
    }

    /**
     * Retrieve ConnectWise Companies
     * Retrieves a list of ConnectWise companies for the given ConnectWise id. You must be associated to the same provider as the ConnectWise integration to use this endpoint.
     * @param UUID  (required)
     * @return RetrieveCompanies0RequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public RetrieveCompanies0RequestBuilder retrieveCompanies_0(String UUID) throws IllegalArgumentException {
        if (UUID == null) throw new IllegalArgumentException("\"UUID\" is required but got null");
            

        return new RetrieveCompanies0RequestBuilder(UUID);
    }
    private okhttp3.Call retrieveCompanies_1Call(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/integrations/syncro/{UUID}/companies"
            .replace("{" + "UUID" + "}", localVarApiClient.escapeString(UUID.toString()));

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
    private okhttp3.Call retrieveCompanies_1ValidateBeforeCall(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'UUID' is set
        if (UUID == null) {
            throw new ApiException("Missing the required parameter 'UUID' when calling retrieveCompanies_1(Async)");
        }

        return retrieveCompanies_1Call(UUID, fields, filter, limit, skip, sort, _callback);

    }


    private ApiResponse<SyncroCompanyResp> retrieveCompanies_1WithHttpInfo(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort) throws ApiException {
        okhttp3.Call localVarCall = retrieveCompanies_1ValidateBeforeCall(UUID, fields, filter, limit, skip, sort, null);
        Type localVarReturnType = new TypeToken<SyncroCompanyResp>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call retrieveCompanies_1Async(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback<SyncroCompanyResp> _callback) throws ApiException {

        okhttp3.Call localVarCall = retrieveCompanies_1ValidateBeforeCall(UUID, fields, filter, limit, skip, sort, _callback);
        Type localVarReturnType = new TypeToken<SyncroCompanyResp>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class RetrieveCompanies1RequestBuilder {
        private final String UUID;
        private List<String> fields;
        private List<String> filter;
        private Integer limit;
        private Integer skip;
        private List<String> sort;

        private RetrieveCompanies1RequestBuilder(String UUID) {
            this.UUID = UUID;
        }

        /**
         * Set fields
         * @param fields The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  (optional)
         * @return RetrieveCompanies1RequestBuilder
         */
        public RetrieveCompanies1RequestBuilder fields(List<String> fields) {
            this.fields = fields;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return RetrieveCompanies1RequestBuilder
         */
        public RetrieveCompanies1RequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return RetrieveCompanies1RequestBuilder
         */
        public RetrieveCompanies1RequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return RetrieveCompanies1RequestBuilder
         */
        public RetrieveCompanies1RequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  (optional)
         * @return RetrieveCompanies1RequestBuilder
         */
        public RetrieveCompanies1RequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Build call for retrieveCompanies_1
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
            return retrieveCompanies_1Call(UUID, fields, filter, limit, skip, sort, _callback);
        }


        /**
         * Execute retrieveCompanies_1 request
         * @return SyncroCompanyResp
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public SyncroCompanyResp execute() throws ApiException {
            ApiResponse<SyncroCompanyResp> localVarResp = retrieveCompanies_1WithHttpInfo(UUID, fields, filter, limit, skip, sort);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute retrieveCompanies_1 request with HTTP info returned
         * @return ApiResponse&lt;SyncroCompanyResp&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<SyncroCompanyResp> executeWithHttpInfo() throws ApiException {
            return retrieveCompanies_1WithHttpInfo(UUID, fields, filter, limit, skip, sort);
        }

        /**
         * Execute retrieveCompanies_1 request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<SyncroCompanyResp> _callback) throws ApiException {
            return retrieveCompanies_1Async(UUID, fields, filter, limit, skip, sort, _callback);
        }
    }

    /**
     * Retrieve Syncro Companies
     * Retrieves a list of Syncro companies for the given Syncro id. You must be associated to the same provider as the Syncro integration to use this endpoint.
     * @param UUID  (required)
     * @return RetrieveCompanies1RequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public RetrieveCompanies1RequestBuilder retrieveCompanies_1(String UUID) throws IllegalArgumentException {
        if (UUID == null) throw new IllegalArgumentException("\"UUID\" is required but got null");
            

        return new RetrieveCompanies1RequestBuilder(UUID);
    }
    private okhttp3.Call retrieveCompanyTypesCall(String UUID, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/integrations/autotask/{UUID}/companytypes"
            .replace("{" + "UUID" + "}", localVarApiClient.escapeString(UUID.toString()));

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
    private okhttp3.Call retrieveCompanyTypesValidateBeforeCall(String UUID, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'UUID' is set
        if (UUID == null) {
            throw new ApiException("Missing the required parameter 'UUID' when calling retrieveCompanyTypes(Async)");
        }

        return retrieveCompanyTypesCall(UUID, _callback);

    }


    private ApiResponse<AutotaskCompanyTypeResp> retrieveCompanyTypesWithHttpInfo(String UUID) throws ApiException {
        okhttp3.Call localVarCall = retrieveCompanyTypesValidateBeforeCall(UUID, null);
        Type localVarReturnType = new TypeToken<AutotaskCompanyTypeResp>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call retrieveCompanyTypesAsync(String UUID, final ApiCallback<AutotaskCompanyTypeResp> _callback) throws ApiException {

        okhttp3.Call localVarCall = retrieveCompanyTypesValidateBeforeCall(UUID, _callback);
        Type localVarReturnType = new TypeToken<AutotaskCompanyTypeResp>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class RetrieveCompanyTypesRequestBuilder {
        private final String UUID;

        private RetrieveCompanyTypesRequestBuilder(String UUID) {
            this.UUID = UUID;
        }

        /**
         * Build call for retrieveCompanyTypes
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
            return retrieveCompanyTypesCall(UUID, _callback);
        }


        /**
         * Execute retrieveCompanyTypes request
         * @return AutotaskCompanyTypeResp
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public AutotaskCompanyTypeResp execute() throws ApiException {
            ApiResponse<AutotaskCompanyTypeResp> localVarResp = retrieveCompanyTypesWithHttpInfo(UUID);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute retrieveCompanyTypes request with HTTP info returned
         * @return ApiResponse&lt;AutotaskCompanyTypeResp&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<AutotaskCompanyTypeResp> executeWithHttpInfo() throws ApiException {
            return retrieveCompanyTypesWithHttpInfo(UUID);
        }

        /**
         * Execute retrieveCompanyTypes request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AutotaskCompanyTypeResp> _callback) throws ApiException {
            return retrieveCompanyTypesAsync(UUID, _callback);
        }
    }

    /**
     * Retrieve Autotask Company Types
     * Retrieves a list of user defined company types from Autotask for the given Autotask id.
     * @param UUID  (required)
     * @return RetrieveCompanyTypesRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public RetrieveCompanyTypesRequestBuilder retrieveCompanyTypes(String UUID) throws IllegalArgumentException {
        if (UUID == null) throw new IllegalArgumentException("\"UUID\" is required but got null");
            

        return new RetrieveCompanyTypesRequestBuilder(UUID);
    }
    private okhttp3.Call retrieveCompanyTypes_0Call(String UUID, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/integrations/connectwise/{UUID}/companytypes"
            .replace("{" + "UUID" + "}", localVarApiClient.escapeString(UUID.toString()));

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
    private okhttp3.Call retrieveCompanyTypes_0ValidateBeforeCall(String UUID, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'UUID' is set
        if (UUID == null) {
            throw new ApiException("Missing the required parameter 'UUID' when calling retrieveCompanyTypes_0(Async)");
        }

        return retrieveCompanyTypes_0Call(UUID, _callback);

    }


    private ApiResponse<ConnectwiseCompanyTypeResp> retrieveCompanyTypes_0WithHttpInfo(String UUID) throws ApiException {
        okhttp3.Call localVarCall = retrieveCompanyTypes_0ValidateBeforeCall(UUID, null);
        Type localVarReturnType = new TypeToken<ConnectwiseCompanyTypeResp>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call retrieveCompanyTypes_0Async(String UUID, final ApiCallback<ConnectwiseCompanyTypeResp> _callback) throws ApiException {

        okhttp3.Call localVarCall = retrieveCompanyTypes_0ValidateBeforeCall(UUID, _callback);
        Type localVarReturnType = new TypeToken<ConnectwiseCompanyTypeResp>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class RetrieveCompanyTypes0RequestBuilder {
        private final String UUID;

        private RetrieveCompanyTypes0RequestBuilder(String UUID) {
            this.UUID = UUID;
        }

        /**
         * Build call for retrieveCompanyTypes_0
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
            return retrieveCompanyTypes_0Call(UUID, _callback);
        }


        /**
         * Execute retrieveCompanyTypes_0 request
         * @return ConnectwiseCompanyTypeResp
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ConnectwiseCompanyTypeResp execute() throws ApiException {
            ApiResponse<ConnectwiseCompanyTypeResp> localVarResp = retrieveCompanyTypes_0WithHttpInfo(UUID);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute retrieveCompanyTypes_0 request with HTTP info returned
         * @return ApiResponse&lt;ConnectwiseCompanyTypeResp&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<ConnectwiseCompanyTypeResp> executeWithHttpInfo() throws ApiException {
            return retrieveCompanyTypes_0WithHttpInfo(UUID);
        }

        /**
         * Execute retrieveCompanyTypes_0 request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ConnectwiseCompanyTypeResp> _callback) throws ApiException {
            return retrieveCompanyTypes_0Async(UUID, _callback);
        }
    }

    /**
     * Retrieve ConnectWise Company Types
     * Retrieves a list of user defined company types from ConnectWise for the given ConnectWise id.
     * @param UUID  (required)
     * @return RetrieveCompanyTypes0RequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public RetrieveCompanyTypes0RequestBuilder retrieveCompanyTypes_0(String UUID) throws IllegalArgumentException {
        if (UUID == null) throw new IllegalArgumentException("\"UUID\" is required but got null");
            

        return new RetrieveCompanyTypes0RequestBuilder(UUID);
    }
    private okhttp3.Call retrieveContractsCall(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/integrations/autotask/{UUID}/contracts"
            .replace("{" + "UUID" + "}", localVarApiClient.escapeString(UUID.toString()));

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
    private okhttp3.Call retrieveContractsValidateBeforeCall(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'UUID' is set
        if (UUID == null) {
            throw new ApiException("Missing the required parameter 'UUID' when calling retrieveContracts(Async)");
        }

        return retrieveContractsCall(UUID, fields, filter, limit, skip, sort, _callback);

    }


    private ApiResponse<AutotaskRetrieveContractsResponse> retrieveContractsWithHttpInfo(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort) throws ApiException {
        okhttp3.Call localVarCall = retrieveContractsValidateBeforeCall(UUID, fields, filter, limit, skip, sort, null);
        Type localVarReturnType = new TypeToken<AutotaskRetrieveContractsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call retrieveContractsAsync(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback<AutotaskRetrieveContractsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = retrieveContractsValidateBeforeCall(UUID, fields, filter, limit, skip, sort, _callback);
        Type localVarReturnType = new TypeToken<AutotaskRetrieveContractsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class RetrieveContractsRequestBuilder {
        private final String UUID;
        private List<String> fields;
        private List<String> filter;
        private Integer limit;
        private Integer skip;
        private List<String> sort;

        private RetrieveContractsRequestBuilder(String UUID) {
            this.UUID = UUID;
        }

        /**
         * Set fields
         * @param fields The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  (optional)
         * @return RetrieveContractsRequestBuilder
         */
        public RetrieveContractsRequestBuilder fields(List<String> fields) {
            this.fields = fields;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return RetrieveContractsRequestBuilder
         */
        public RetrieveContractsRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return RetrieveContractsRequestBuilder
         */
        public RetrieveContractsRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return RetrieveContractsRequestBuilder
         */
        public RetrieveContractsRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  (optional)
         * @return RetrieveContractsRequestBuilder
         */
        public RetrieveContractsRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Build call for retrieveContracts
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
            return retrieveContractsCall(UUID, fields, filter, limit, skip, sort, _callback);
        }


        /**
         * Execute retrieveContracts request
         * @return AutotaskRetrieveContractsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public AutotaskRetrieveContractsResponse execute() throws ApiException {
            ApiResponse<AutotaskRetrieveContractsResponse> localVarResp = retrieveContractsWithHttpInfo(UUID, fields, filter, limit, skip, sort);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute retrieveContracts request with HTTP info returned
         * @return ApiResponse&lt;AutotaskRetrieveContractsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<AutotaskRetrieveContractsResponse> executeWithHttpInfo() throws ApiException {
            return retrieveContractsWithHttpInfo(UUID, fields, filter, limit, skip, sort);
        }

        /**
         * Execute retrieveContracts request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AutotaskRetrieveContractsResponse> _callback) throws ApiException {
            return retrieveContractsAsync(UUID, fields, filter, limit, skip, sort, _callback);
        }
    }

    /**
     * Retrieve Autotask Contracts
     * Retrieves a list of Autotask contracts for the given Autotask integration id. You must be associated to the same provider as the Autotask integration to use this endpoint.
     * @param UUID  (required)
     * @return RetrieveContractsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public RetrieveContractsRequestBuilder retrieveContracts(String UUID) throws IllegalArgumentException {
        if (UUID == null) throw new IllegalArgumentException("\"UUID\" is required but got null");
            

        return new RetrieveContractsRequestBuilder(UUID);
    }
    private okhttp3.Call retrieveContractsFieldsCall(String UUID, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/integrations/autotask/{UUID}/contracts/fields"
            .replace("{" + "UUID" + "}", localVarApiClient.escapeString(UUID.toString()));

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
    private okhttp3.Call retrieveContractsFieldsValidateBeforeCall(String UUID, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'UUID' is set
        if (UUID == null) {
            throw new ApiException("Missing the required parameter 'UUID' when calling retrieveContractsFields(Async)");
        }

        return retrieveContractsFieldsCall(UUID, _callback);

    }


    private ApiResponse<AutotaskRetrieveContractsFieldsResponse> retrieveContractsFieldsWithHttpInfo(String UUID) throws ApiException {
        okhttp3.Call localVarCall = retrieveContractsFieldsValidateBeforeCall(UUID, null);
        Type localVarReturnType = new TypeToken<AutotaskRetrieveContractsFieldsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call retrieveContractsFieldsAsync(String UUID, final ApiCallback<AutotaskRetrieveContractsFieldsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = retrieveContractsFieldsValidateBeforeCall(UUID, _callback);
        Type localVarReturnType = new TypeToken<AutotaskRetrieveContractsFieldsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class RetrieveContractsFieldsRequestBuilder {
        private final String UUID;

        private RetrieveContractsFieldsRequestBuilder(String UUID) {
            this.UUID = UUID;
        }

        /**
         * Build call for retrieveContractsFields
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
            return retrieveContractsFieldsCall(UUID, _callback);
        }


        /**
         * Execute retrieveContractsFields request
         * @return AutotaskRetrieveContractsFieldsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public AutotaskRetrieveContractsFieldsResponse execute() throws ApiException {
            ApiResponse<AutotaskRetrieveContractsFieldsResponse> localVarResp = retrieveContractsFieldsWithHttpInfo(UUID);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute retrieveContractsFields request with HTTP info returned
         * @return ApiResponse&lt;AutotaskRetrieveContractsFieldsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<AutotaskRetrieveContractsFieldsResponse> executeWithHttpInfo() throws ApiException {
            return retrieveContractsFieldsWithHttpInfo(UUID);
        }

        /**
         * Execute retrieveContractsFields request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AutotaskRetrieveContractsFieldsResponse> _callback) throws ApiException {
            return retrieveContractsFieldsAsync(UUID, _callback);
        }
    }

    /**
     * Retrieve Autotask Contract Fields
     * Retrieves a list of Autotask contract fields for the given Autotask integration id. You must be associated to the same provider as the Autotask integration to use this endpoint.
     * @param UUID  (required)
     * @return RetrieveContractsFieldsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public RetrieveContractsFieldsRequestBuilder retrieveContractsFields(String UUID) throws IllegalArgumentException {
        if (UUID == null) throw new IllegalArgumentException("\"UUID\" is required but got null");
            

        return new RetrieveContractsFieldsRequestBuilder(UUID);
    }
    private okhttp3.Call retrieveIntegrationsCall(String providerId, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/providers/{provider_id}/integrations"
            .replace("{" + "provider_id" + "}", localVarApiClient.escapeString(providerId.toString()));

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
    private okhttp3.Call retrieveIntegrationsValidateBeforeCall(String providerId, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'providerId' is set
        if (providerId == null) {
            throw new ApiException("Missing the required parameter 'providerId' when calling retrieveIntegrations(Async)");
        }

        return retrieveIntegrationsCall(providerId, filter, limit, skip, sort, _callback);

    }


    private ApiResponse<IntegrationsResponse> retrieveIntegrationsWithHttpInfo(String providerId, List<String> filter, Integer limit, Integer skip, List<String> sort) throws ApiException {
        okhttp3.Call localVarCall = retrieveIntegrationsValidateBeforeCall(providerId, filter, limit, skip, sort, null);
        Type localVarReturnType = new TypeToken<IntegrationsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call retrieveIntegrationsAsync(String providerId, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback<IntegrationsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = retrieveIntegrationsValidateBeforeCall(providerId, filter, limit, skip, sort, _callback);
        Type localVarReturnType = new TypeToken<IntegrationsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class RetrieveIntegrationsRequestBuilder {
        private final String providerId;
        private List<String> filter;
        private Integer limit;
        private Integer skip;
        private List<String> sort;

        private RetrieveIntegrationsRequestBuilder(String providerId) {
            this.providerId = providerId;
        }

        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return RetrieveIntegrationsRequestBuilder
         */
        public RetrieveIntegrationsRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return RetrieveIntegrationsRequestBuilder
         */
        public RetrieveIntegrationsRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return RetrieveIntegrationsRequestBuilder
         */
        public RetrieveIntegrationsRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  (optional)
         * @return RetrieveIntegrationsRequestBuilder
         */
        public RetrieveIntegrationsRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Build call for retrieveIntegrations
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
            return retrieveIntegrationsCall(providerId, filter, limit, skip, sort, _callback);
        }


        /**
         * Execute retrieveIntegrations request
         * @return IntegrationsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public IntegrationsResponse execute() throws ApiException {
            ApiResponse<IntegrationsResponse> localVarResp = retrieveIntegrationsWithHttpInfo(providerId, filter, limit, skip, sort);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute retrieveIntegrations request with HTTP info returned
         * @return ApiResponse&lt;IntegrationsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<IntegrationsResponse> executeWithHttpInfo() throws ApiException {
            return retrieveIntegrationsWithHttpInfo(providerId, filter, limit, skip, sort);
        }

        /**
         * Execute retrieveIntegrations request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<IntegrationsResponse> _callback) throws ApiException {
            return retrieveIntegrationsAsync(providerId, filter, limit, skip, sort, _callback);
        }
    }

    /**
     * Retrieve Integrations for Provider
     * Retrieves a list of integrations this provider has configured. You must be associated to the provider to use this endpoint.
     * @param providerId  (required)
     * @return RetrieveIntegrationsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public RetrieveIntegrationsRequestBuilder retrieveIntegrations(String providerId) throws IllegalArgumentException {
        if (providerId == null) throw new IllegalArgumentException("\"providerId\" is required but got null");
            

        return new RetrieveIntegrationsRequestBuilder(providerId);
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
    private okhttp3.Call retrieveMappingsCall(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/integrations/autotask/{UUID}/mappings"
            .replace("{" + "UUID" + "}", localVarApiClient.escapeString(UUID.toString()));

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
    private okhttp3.Call retrieveMappingsValidateBeforeCall(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'UUID' is set
        if (UUID == null) {
            throw new ApiException("Missing the required parameter 'UUID' when calling retrieveMappings(Async)");
        }

        return retrieveMappingsCall(UUID, fields, filter, limit, skip, sort, _callback);

    }


    private ApiResponse<AutotaskRetrieveMappingsResponse> retrieveMappingsWithHttpInfo(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort) throws ApiException {
        okhttp3.Call localVarCall = retrieveMappingsValidateBeforeCall(UUID, fields, filter, limit, skip, sort, null);
        Type localVarReturnType = new TypeToken<AutotaskRetrieveMappingsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call retrieveMappingsAsync(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback<AutotaskRetrieveMappingsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = retrieveMappingsValidateBeforeCall(UUID, fields, filter, limit, skip, sort, _callback);
        Type localVarReturnType = new TypeToken<AutotaskRetrieveMappingsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class RetrieveMappingsRequestBuilder {
        private final String UUID;
        private List<String> fields;
        private List<String> filter;
        private Integer limit;
        private Integer skip;
        private List<String> sort;

        private RetrieveMappingsRequestBuilder(String UUID) {
            this.UUID = UUID;
        }

        /**
         * Set fields
         * @param fields The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  (optional)
         * @return RetrieveMappingsRequestBuilder
         */
        public RetrieveMappingsRequestBuilder fields(List<String> fields) {
            this.fields = fields;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return RetrieveMappingsRequestBuilder
         */
        public RetrieveMappingsRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return RetrieveMappingsRequestBuilder
         */
        public RetrieveMappingsRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return RetrieveMappingsRequestBuilder
         */
        public RetrieveMappingsRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  (optional)
         * @return RetrieveMappingsRequestBuilder
         */
        public RetrieveMappingsRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Build call for retrieveMappings
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
            return retrieveMappingsCall(UUID, fields, filter, limit, skip, sort, _callback);
        }


        /**
         * Execute retrieveMappings request
         * @return AutotaskRetrieveMappingsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public AutotaskRetrieveMappingsResponse execute() throws ApiException {
            ApiResponse<AutotaskRetrieveMappingsResponse> localVarResp = retrieveMappingsWithHttpInfo(UUID, fields, filter, limit, skip, sort);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute retrieveMappings request with HTTP info returned
         * @return ApiResponse&lt;AutotaskRetrieveMappingsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<AutotaskRetrieveMappingsResponse> executeWithHttpInfo() throws ApiException {
            return retrieveMappingsWithHttpInfo(UUID, fields, filter, limit, skip, sort);
        }

        /**
         * Execute retrieveMappings request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AutotaskRetrieveMappingsResponse> _callback) throws ApiException {
            return retrieveMappingsAsync(UUID, fields, filter, limit, skip, sort, _callback);
        }
    }

    /**
     * Retrieve Autotask mappings
     * Retrieves the list of mappings for this Autotask integration. You must be associated to the same provider as the Autotask integration to use this api.
     * @param UUID  (required)
     * @return RetrieveMappingsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public RetrieveMappingsRequestBuilder retrieveMappings(String UUID) throws IllegalArgumentException {
        if (UUID == null) throw new IllegalArgumentException("\"UUID\" is required but got null");
            

        return new RetrieveMappingsRequestBuilder(UUID);
    }
    private okhttp3.Call retrieveMappings_0Call(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/integrations/connectwise/{UUID}/mappings"
            .replace("{" + "UUID" + "}", localVarApiClient.escapeString(UUID.toString()));

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
    private okhttp3.Call retrieveMappings_0ValidateBeforeCall(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'UUID' is set
        if (UUID == null) {
            throw new ApiException("Missing the required parameter 'UUID' when calling retrieveMappings_0(Async)");
        }

        return retrieveMappings_0Call(UUID, fields, filter, limit, skip, sort, _callback);

    }


    private ApiResponse<ConnectwiseRetrieveMappingsResponse> retrieveMappings_0WithHttpInfo(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort) throws ApiException {
        okhttp3.Call localVarCall = retrieveMappings_0ValidateBeforeCall(UUID, fields, filter, limit, skip, sort, null);
        Type localVarReturnType = new TypeToken<ConnectwiseRetrieveMappingsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call retrieveMappings_0Async(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback<ConnectwiseRetrieveMappingsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = retrieveMappings_0ValidateBeforeCall(UUID, fields, filter, limit, skip, sort, _callback);
        Type localVarReturnType = new TypeToken<ConnectwiseRetrieveMappingsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class RetrieveMappings0RequestBuilder {
        private final String UUID;
        private List<String> fields;
        private List<String> filter;
        private Integer limit;
        private Integer skip;
        private List<String> sort;

        private RetrieveMappings0RequestBuilder(String UUID) {
            this.UUID = UUID;
        }

        /**
         * Set fields
         * @param fields The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  (optional)
         * @return RetrieveMappings0RequestBuilder
         */
        public RetrieveMappings0RequestBuilder fields(List<String> fields) {
            this.fields = fields;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return RetrieveMappings0RequestBuilder
         */
        public RetrieveMappings0RequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return RetrieveMappings0RequestBuilder
         */
        public RetrieveMappings0RequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return RetrieveMappings0RequestBuilder
         */
        public RetrieveMappings0RequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  (optional)
         * @return RetrieveMappings0RequestBuilder
         */
        public RetrieveMappings0RequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Build call for retrieveMappings_0
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
            return retrieveMappings_0Call(UUID, fields, filter, limit, skip, sort, _callback);
        }


        /**
         * Execute retrieveMappings_0 request
         * @return ConnectwiseRetrieveMappingsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ConnectwiseRetrieveMappingsResponse execute() throws ApiException {
            ApiResponse<ConnectwiseRetrieveMappingsResponse> localVarResp = retrieveMappings_0WithHttpInfo(UUID, fields, filter, limit, skip, sort);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute retrieveMappings_0 request with HTTP info returned
         * @return ApiResponse&lt;ConnectwiseRetrieveMappingsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<ConnectwiseRetrieveMappingsResponse> executeWithHttpInfo() throws ApiException {
            return retrieveMappings_0WithHttpInfo(UUID, fields, filter, limit, skip, sort);
        }

        /**
         * Execute retrieveMappings_0 request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ConnectwiseRetrieveMappingsResponse> _callback) throws ApiException {
            return retrieveMappings_0Async(UUID, fields, filter, limit, skip, sort, _callback);
        }
    }

    /**
     * Retrieve ConnectWise mappings
     * Retrieves the list of mappings for this ConnectWise integration. You must be associated to the same provider as the ConnectWise integration to use this api.
     * @param UUID  (required)
     * @return RetrieveMappings0RequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public RetrieveMappings0RequestBuilder retrieveMappings_0(String UUID) throws IllegalArgumentException {
        if (UUID == null) throw new IllegalArgumentException("\"UUID\" is required but got null");
            

        return new RetrieveMappings0RequestBuilder(UUID);
    }
    private okhttp3.Call retrieveMappings_1Call(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/integrations/syncro/{UUID}/mappings"
            .replace("{" + "UUID" + "}", localVarApiClient.escapeString(UUID.toString()));

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
    private okhttp3.Call retrieveMappings_1ValidateBeforeCall(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'UUID' is set
        if (UUID == null) {
            throw new ApiException("Missing the required parameter 'UUID' when calling retrieveMappings_1(Async)");
        }

        return retrieveMappings_1Call(UUID, fields, filter, limit, skip, sort, _callback);

    }


    private ApiResponse<SyncroRetrieveMappingsResponse> retrieveMappings_1WithHttpInfo(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort) throws ApiException {
        okhttp3.Call localVarCall = retrieveMappings_1ValidateBeforeCall(UUID, fields, filter, limit, skip, sort, null);
        Type localVarReturnType = new TypeToken<SyncroRetrieveMappingsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call retrieveMappings_1Async(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback<SyncroRetrieveMappingsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = retrieveMappings_1ValidateBeforeCall(UUID, fields, filter, limit, skip, sort, _callback);
        Type localVarReturnType = new TypeToken<SyncroRetrieveMappingsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class RetrieveMappings1RequestBuilder {
        private final String UUID;
        private List<String> fields;
        private List<String> filter;
        private Integer limit;
        private Integer skip;
        private List<String> sort;

        private RetrieveMappings1RequestBuilder(String UUID) {
            this.UUID = UUID;
        }

        /**
         * Set fields
         * @param fields The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  (optional)
         * @return RetrieveMappings1RequestBuilder
         */
        public RetrieveMappings1RequestBuilder fields(List<String> fields) {
            this.fields = fields;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return RetrieveMappings1RequestBuilder
         */
        public RetrieveMappings1RequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return RetrieveMappings1RequestBuilder
         */
        public RetrieveMappings1RequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return RetrieveMappings1RequestBuilder
         */
        public RetrieveMappings1RequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  (optional)
         * @return RetrieveMappings1RequestBuilder
         */
        public RetrieveMappings1RequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Build call for retrieveMappings_1
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
            return retrieveMappings_1Call(UUID, fields, filter, limit, skip, sort, _callback);
        }


        /**
         * Execute retrieveMappings_1 request
         * @return SyncroRetrieveMappingsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public SyncroRetrieveMappingsResponse execute() throws ApiException {
            ApiResponse<SyncroRetrieveMappingsResponse> localVarResp = retrieveMappings_1WithHttpInfo(UUID, fields, filter, limit, skip, sort);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute retrieveMappings_1 request with HTTP info returned
         * @return ApiResponse&lt;SyncroRetrieveMappingsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<SyncroRetrieveMappingsResponse> executeWithHttpInfo() throws ApiException {
            return retrieveMappings_1WithHttpInfo(UUID, fields, filter, limit, skip, sort);
        }

        /**
         * Execute retrieveMappings_1 request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<SyncroRetrieveMappingsResponse> _callback) throws ApiException {
            return retrieveMappings_1Async(UUID, fields, filter, limit, skip, sort, _callback);
        }
    }

    /**
     * Retrieve Syncro mappings
     * Retrieves the list of mappings for this Syncro integration. You must be associated to the same provider as the Syncro integration to use this api.
     * @param UUID  (required)
     * @return RetrieveMappings1RequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public RetrieveMappings1RequestBuilder retrieveMappings_1(String UUID) throws IllegalArgumentException {
        if (UUID == null) throw new IllegalArgumentException("\"UUID\" is required but got null");
            

        return new RetrieveMappings1RequestBuilder(UUID);
    }
    private okhttp3.Call retrieveServicesCall(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/integrations/autotask/{UUID}/contracts/services"
            .replace("{" + "UUID" + "}", localVarApiClient.escapeString(UUID.toString()));

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
    private okhttp3.Call retrieveServicesValidateBeforeCall(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'UUID' is set
        if (UUID == null) {
            throw new ApiException("Missing the required parameter 'UUID' when calling retrieveServices(Async)");
        }

        return retrieveServicesCall(UUID, fields, filter, limit, skip, sort, _callback);

    }


    private ApiResponse<AutotaskRetrieveServicesResponse> retrieveServicesWithHttpInfo(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort) throws ApiException {
        okhttp3.Call localVarCall = retrieveServicesValidateBeforeCall(UUID, fields, filter, limit, skip, sort, null);
        Type localVarReturnType = new TypeToken<AutotaskRetrieveServicesResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call retrieveServicesAsync(String UUID, List<String> fields, List<String> filter, Integer limit, Integer skip, List<String> sort, final ApiCallback<AutotaskRetrieveServicesResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = retrieveServicesValidateBeforeCall(UUID, fields, filter, limit, skip, sort, _callback);
        Type localVarReturnType = new TypeToken<AutotaskRetrieveServicesResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class RetrieveServicesRequestBuilder {
        private final String UUID;
        private List<String> fields;
        private List<String> filter;
        private Integer limit;
        private Integer skip;
        private List<String> sort;

        private RetrieveServicesRequestBuilder(String UUID) {
            this.UUID = UUID;
        }

        /**
         * Set fields
         * @param fields The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  (optional)
         * @return RetrieveServicesRequestBuilder
         */
        public RetrieveServicesRequestBuilder fields(List<String> fields) {
            this.fields = fields;
            return this;
        }
        
        /**
         * Set filter
         * @param filter A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; (optional)
         * @return RetrieveServicesRequestBuilder
         */
        public RetrieveServicesRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit The number of records to return at once. Limited to 100. (optional, default to 10)
         * @return RetrieveServicesRequestBuilder
         */
        public RetrieveServicesRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return RetrieveServicesRequestBuilder
         */
        public RetrieveServicesRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  (optional)
         * @return RetrieveServicesRequestBuilder
         */
        public RetrieveServicesRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Build call for retrieveServices
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
            return retrieveServicesCall(UUID, fields, filter, limit, skip, sort, _callback);
        }


        /**
         * Execute retrieveServices request
         * @return AutotaskRetrieveServicesResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public AutotaskRetrieveServicesResponse execute() throws ApiException {
            ApiResponse<AutotaskRetrieveServicesResponse> localVarResp = retrieveServicesWithHttpInfo(UUID, fields, filter, limit, skip, sort);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute retrieveServices request with HTTP info returned
         * @return ApiResponse&lt;AutotaskRetrieveServicesResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<AutotaskRetrieveServicesResponse> executeWithHttpInfo() throws ApiException {
            return retrieveServicesWithHttpInfo(UUID, fields, filter, limit, skip, sort);
        }

        /**
         * Execute retrieveServices request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AutotaskRetrieveServicesResponse> _callback) throws ApiException {
            return retrieveServicesAsync(UUID, fields, filter, limit, skip, sort, _callback);
        }
    }

    /**
     * Retrieve Autotask Contract Services
     * Retrieves a list of Autotask contract services for the given Autotask integration id. You must be associated to the same provider as the Autotask integration to use this endpoint.
     * @param UUID  (required)
     * @return RetrieveServicesRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public RetrieveServicesRequestBuilder retrieveServices(String UUID) throws IllegalArgumentException {
        if (UUID == null) throw new IllegalArgumentException("\"UUID\" is required but got null");
            

        return new RetrieveServicesRequestBuilder(UUID);
    }
    private okhttp3.Call retrieveSettingsCall(String UUID, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/integrations/autotask/{UUID}/settings"
            .replace("{" + "UUID" + "}", localVarApiClient.escapeString(UUID.toString()));

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
    private okhttp3.Call retrieveSettingsValidateBeforeCall(String UUID, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'UUID' is set
        if (UUID == null) {
            throw new ApiException("Missing the required parameter 'UUID' when calling retrieveSettings(Async)");
        }

        return retrieveSettingsCall(UUID, _callback);

    }


    private ApiResponse<AutotaskSettings> retrieveSettingsWithHttpInfo(String UUID) throws ApiException {
        okhttp3.Call localVarCall = retrieveSettingsValidateBeforeCall(UUID, null);
        Type localVarReturnType = new TypeToken<AutotaskSettings>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call retrieveSettingsAsync(String UUID, final ApiCallback<AutotaskSettings> _callback) throws ApiException {

        okhttp3.Call localVarCall = retrieveSettingsValidateBeforeCall(UUID, _callback);
        Type localVarReturnType = new TypeToken<AutotaskSettings>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class RetrieveSettingsRequestBuilder {
        private final String UUID;

        private RetrieveSettingsRequestBuilder(String UUID) {
            this.UUID = UUID;
        }

        /**
         * Build call for retrieveSettings
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
            return retrieveSettingsCall(UUID, _callback);
        }


        /**
         * Execute retrieveSettings request
         * @return AutotaskSettings
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public AutotaskSettings execute() throws ApiException {
            ApiResponse<AutotaskSettings> localVarResp = retrieveSettingsWithHttpInfo(UUID);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute retrieveSettings request with HTTP info returned
         * @return ApiResponse&lt;AutotaskSettings&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<AutotaskSettings> executeWithHttpInfo() throws ApiException {
            return retrieveSettingsWithHttpInfo(UUID);
        }

        /**
         * Execute retrieveSettings request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AutotaskSettings> _callback) throws ApiException {
            return retrieveSettingsAsync(UUID, _callback);
        }
    }

    /**
     * Retrieve Autotask Integration settings
     * Retrieve the Autotask integration settings. You must be associated to the same provider as the Autotask integration to use this endpoint.
     * @param UUID  (required)
     * @return RetrieveSettingsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public RetrieveSettingsRequestBuilder retrieveSettings(String UUID) throws IllegalArgumentException {
        if (UUID == null) throw new IllegalArgumentException("\"UUID\" is required but got null");
            

        return new RetrieveSettingsRequestBuilder(UUID);
    }
    private okhttp3.Call retrieveSettings_0Call(String UUID, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/integrations/connectwise/{UUID}/settings"
            .replace("{" + "UUID" + "}", localVarApiClient.escapeString(UUID.toString()));

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
    private okhttp3.Call retrieveSettings_0ValidateBeforeCall(String UUID, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'UUID' is set
        if (UUID == null) {
            throw new ApiException("Missing the required parameter 'UUID' when calling retrieveSettings_0(Async)");
        }

        return retrieveSettings_0Call(UUID, _callback);

    }


    private ApiResponse<ConnectWiseSettings> retrieveSettings_0WithHttpInfo(String UUID) throws ApiException {
        okhttp3.Call localVarCall = retrieveSettings_0ValidateBeforeCall(UUID, null);
        Type localVarReturnType = new TypeToken<ConnectWiseSettings>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call retrieveSettings_0Async(String UUID, final ApiCallback<ConnectWiseSettings> _callback) throws ApiException {

        okhttp3.Call localVarCall = retrieveSettings_0ValidateBeforeCall(UUID, _callback);
        Type localVarReturnType = new TypeToken<ConnectWiseSettings>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class RetrieveSettings0RequestBuilder {
        private final String UUID;

        private RetrieveSettings0RequestBuilder(String UUID) {
            this.UUID = UUID;
        }

        /**
         * Build call for retrieveSettings_0
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
            return retrieveSettings_0Call(UUID, _callback);
        }


        /**
         * Execute retrieveSettings_0 request
         * @return ConnectWiseSettings
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ConnectWiseSettings execute() throws ApiException {
            ApiResponse<ConnectWiseSettings> localVarResp = retrieveSettings_0WithHttpInfo(UUID);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute retrieveSettings_0 request with HTTP info returned
         * @return ApiResponse&lt;ConnectWiseSettings&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<ConnectWiseSettings> executeWithHttpInfo() throws ApiException {
            return retrieveSettings_0WithHttpInfo(UUID);
        }

        /**
         * Execute retrieveSettings_0 request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ConnectWiseSettings> _callback) throws ApiException {
            return retrieveSettings_0Async(UUID, _callback);
        }
    }

    /**
     * Retrieve ConnectWise Integration settings
     * Retrieve the ConnectWise integration settings. You must be associated to the same provider as the ConnectWise integration to use this endpoint.
     * @param UUID  (required)
     * @return RetrieveSettings0RequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public RetrieveSettings0RequestBuilder retrieveSettings_0(String UUID) throws IllegalArgumentException {
        if (UUID == null) throw new IllegalArgumentException("\"UUID\" is required but got null");
            

        return new RetrieveSettings0RequestBuilder(UUID);
    }
    private okhttp3.Call retrieveSettings_1Call(String UUID, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/integrations/syncro/{UUID}/settings"
            .replace("{" + "UUID" + "}", localVarApiClient.escapeString(UUID.toString()));

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
    private okhttp3.Call retrieveSettings_1ValidateBeforeCall(String UUID, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'UUID' is set
        if (UUID == null) {
            throw new ApiException("Missing the required parameter 'UUID' when calling retrieveSettings_1(Async)");
        }

        return retrieveSettings_1Call(UUID, _callback);

    }


    private ApiResponse<SyncroSettings> retrieveSettings_1WithHttpInfo(String UUID) throws ApiException {
        okhttp3.Call localVarCall = retrieveSettings_1ValidateBeforeCall(UUID, null);
        Type localVarReturnType = new TypeToken<SyncroSettings>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call retrieveSettings_1Async(String UUID, final ApiCallback<SyncroSettings> _callback) throws ApiException {

        okhttp3.Call localVarCall = retrieveSettings_1ValidateBeforeCall(UUID, _callback);
        Type localVarReturnType = new TypeToken<SyncroSettings>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class RetrieveSettings1RequestBuilder {
        private final String UUID;

        private RetrieveSettings1RequestBuilder(String UUID) {
            this.UUID = UUID;
        }

        /**
         * Build call for retrieveSettings_1
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
            return retrieveSettings_1Call(UUID, _callback);
        }


        /**
         * Execute retrieveSettings_1 request
         * @return SyncroSettings
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public SyncroSettings execute() throws ApiException {
            ApiResponse<SyncroSettings> localVarResp = retrieveSettings_1WithHttpInfo(UUID);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute retrieveSettings_1 request with HTTP info returned
         * @return ApiResponse&lt;SyncroSettings&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<SyncroSettings> executeWithHttpInfo() throws ApiException {
            return retrieveSettings_1WithHttpInfo(UUID);
        }

        /**
         * Execute retrieveSettings_1 request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<SyncroSettings> _callback) throws ApiException {
            return retrieveSettings_1Async(UUID, _callback);
        }
    }

    /**
     * Retrieve Syncro Integration settings
     * Retrieve the Syncro integration settings. You must be associated to the same provider as the Syncro integration to use this endpoint.
     * @param UUID  (required)
     * @return RetrieveSettings1RequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public RetrieveSettings1RequestBuilder retrieveSettings_1(String UUID) throws IllegalArgumentException {
        if (UUID == null) throw new IllegalArgumentException("\"UUID\" is required but got null");
            

        return new RetrieveSettings1RequestBuilder(UUID);
    }
    private okhttp3.Call retrieveSyncErrorsCall(String UUID, String integrationType, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/integrations/{integration_type}/{UUID}/errors"
            .replace("{" + "UUID" + "}", localVarApiClient.escapeString(UUID.toString()))
            .replace("{" + "integration_type" + "}", localVarApiClient.escapeString(integrationType.toString()));

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
    private okhttp3.Call retrieveSyncErrorsValidateBeforeCall(String UUID, String integrationType, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'UUID' is set
        if (UUID == null) {
            throw new ApiException("Missing the required parameter 'UUID' when calling retrieveSyncErrors(Async)");
        }

        // verify the required parameter 'integrationType' is set
        if (integrationType == null) {
            throw new ApiException("Missing the required parameter 'integrationType' when calling retrieveSyncErrors(Async)");
        }

        return retrieveSyncErrorsCall(UUID, integrationType, _callback);

    }


    private ApiResponse<IntegrationSyncErrorResp> retrieveSyncErrorsWithHttpInfo(String UUID, String integrationType) throws ApiException {
        okhttp3.Call localVarCall = retrieveSyncErrorsValidateBeforeCall(UUID, integrationType, null);
        Type localVarReturnType = new TypeToken<IntegrationSyncErrorResp>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call retrieveSyncErrorsAsync(String UUID, String integrationType, final ApiCallback<IntegrationSyncErrorResp> _callback) throws ApiException {

        okhttp3.Call localVarCall = retrieveSyncErrorsValidateBeforeCall(UUID, integrationType, _callback);
        Type localVarReturnType = new TypeToken<IntegrationSyncErrorResp>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class RetrieveSyncErrorsRequestBuilder {
        private final String UUID;
        private final String integrationType;

        private RetrieveSyncErrorsRequestBuilder(String UUID, String integrationType) {
            this.UUID = UUID;
            this.integrationType = integrationType;
        }

        /**
         * Build call for retrieveSyncErrors
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
            return retrieveSyncErrorsCall(UUID, integrationType, _callback);
        }


        /**
         * Execute retrieveSyncErrors request
         * @return IntegrationSyncErrorResp
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public IntegrationSyncErrorResp execute() throws ApiException {
            ApiResponse<IntegrationSyncErrorResp> localVarResp = retrieveSyncErrorsWithHttpInfo(UUID, integrationType);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute retrieveSyncErrors request with HTTP info returned
         * @return ApiResponse&lt;IntegrationSyncErrorResp&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<IntegrationSyncErrorResp> executeWithHttpInfo() throws ApiException {
            return retrieveSyncErrorsWithHttpInfo(UUID, integrationType);
        }

        /**
         * Execute retrieveSyncErrors request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<IntegrationSyncErrorResp> _callback) throws ApiException {
            return retrieveSyncErrorsAsync(UUID, integrationType, _callback);
        }
    }

    /**
     * Retrieve Recent Integration Sync Errors
     * Retrieves recent sync errors for given integration type and integration id. You must be associated to the provider the integration is tied to in order to use this api.
     * @param UUID  (required)
     * @param integrationType  (required)
     * @return RetrieveSyncErrorsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public RetrieveSyncErrorsRequestBuilder retrieveSyncErrors(String UUID, String integrationType) throws IllegalArgumentException {
        if (UUID == null) throw new IllegalArgumentException("\"UUID\" is required but got null");
            

        if (integrationType == null) throw new IllegalArgumentException("\"integrationType\" is required but got null");
            

        return new RetrieveSyncErrorsRequestBuilder(UUID, integrationType);
    }
    private okhttp3.Call updateAlertConfigurationCall(String providerId, String alertUUID, AutotaskTicketingAlertConfigurationRequest autotaskTicketingAlertConfigurationRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = autotaskTicketingAlertConfigurationRequest;

        // create path and map variables
        String localVarPath = "/providers/{provider_id}/integrations/autotask/alerts/{alert_UUID}/configuration"
            .replace("{" + "provider_id" + "}", localVarApiClient.escapeString(providerId.toString()))
            .replace("{" + "alert_UUID" + "}", localVarApiClient.escapeString(alertUUID.toString()));

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
    private okhttp3.Call updateAlertConfigurationValidateBeforeCall(String providerId, String alertUUID, AutotaskTicketingAlertConfigurationRequest autotaskTicketingAlertConfigurationRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'providerId' is set
        if (providerId == null) {
            throw new ApiException("Missing the required parameter 'providerId' when calling updateAlertConfiguration(Async)");
        }

        // verify the required parameter 'alertUUID' is set
        if (alertUUID == null) {
            throw new ApiException("Missing the required parameter 'alertUUID' when calling updateAlertConfiguration(Async)");
        }

        return updateAlertConfigurationCall(providerId, alertUUID, autotaskTicketingAlertConfigurationRequest, _callback);

    }


    private ApiResponse<AutotaskTicketingAlertConfiguration> updateAlertConfigurationWithHttpInfo(String providerId, String alertUUID, AutotaskTicketingAlertConfigurationRequest autotaskTicketingAlertConfigurationRequest) throws ApiException {
        okhttp3.Call localVarCall = updateAlertConfigurationValidateBeforeCall(providerId, alertUUID, autotaskTicketingAlertConfigurationRequest, null);
        Type localVarReturnType = new TypeToken<AutotaskTicketingAlertConfiguration>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call updateAlertConfigurationAsync(String providerId, String alertUUID, AutotaskTicketingAlertConfigurationRequest autotaskTicketingAlertConfigurationRequest, final ApiCallback<AutotaskTicketingAlertConfiguration> _callback) throws ApiException {

        okhttp3.Call localVarCall = updateAlertConfigurationValidateBeforeCall(providerId, alertUUID, autotaskTicketingAlertConfigurationRequest, _callback);
        Type localVarReturnType = new TypeToken<AutotaskTicketingAlertConfiguration>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UpdateAlertConfigurationRequestBuilder {
        private final String destination;
        private final Integer dueDays;
        private final AutotaskTicketingAlertConfigurationPriority priority;
        private final Boolean shouldCreateTickets;
        private final AutotaskTicketingAlertConfigurationPriority status;
        private final String providerId;
        private final String alertUUID;
        private AutotaskTicketingAlertConfigurationPriority queue;
        private AutotaskTicketingAlertConfigurationResource resource;
        private AutotaskTicketingAlertConfigurationPriority source;

        private UpdateAlertConfigurationRequestBuilder(String destination, Integer dueDays, AutotaskTicketingAlertConfigurationPriority priority, Boolean shouldCreateTickets, AutotaskTicketingAlertConfigurationPriority status, String providerId, String alertUUID) {
            this.destination = destination;
            this.dueDays = dueDays;
            this.priority = priority;
            this.shouldCreateTickets = shouldCreateTickets;
            this.status = status;
            this.providerId = providerId;
            this.alertUUID = alertUUID;
        }

        /**
         * Set queue
         * @param queue  (optional)
         * @return UpdateAlertConfigurationRequestBuilder
         */
        public UpdateAlertConfigurationRequestBuilder queue(AutotaskTicketingAlertConfigurationPriority queue) {
            this.queue = queue;
            return this;
        }
        
        /**
         * Set resource
         * @param resource  (optional)
         * @return UpdateAlertConfigurationRequestBuilder
         */
        public UpdateAlertConfigurationRequestBuilder resource(AutotaskTicketingAlertConfigurationResource resource) {
            this.resource = resource;
            return this;
        }
        
        /**
         * Set source
         * @param source  (optional)
         * @return UpdateAlertConfigurationRequestBuilder
         */
        public UpdateAlertConfigurationRequestBuilder source(AutotaskTicketingAlertConfigurationPriority source) {
            this.source = source;
            return this;
        }
        
        /**
         * Build call for updateAlertConfiguration
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
            AutotaskTicketingAlertConfigurationRequest autotaskTicketingAlertConfigurationRequest = buildBodyParams();
            return updateAlertConfigurationCall(providerId, alertUUID, autotaskTicketingAlertConfigurationRequest, _callback);
        }

        private AutotaskTicketingAlertConfigurationRequest buildBodyParams() {
            AutotaskTicketingAlertConfigurationRequest autotaskTicketingAlertConfigurationRequest = new AutotaskTicketingAlertConfigurationRequest();
            if (this.destination != null)
            autotaskTicketingAlertConfigurationRequest.destination(AutotaskTicketingAlertConfigurationRequest.DestinationEnum.fromValue(this.destination));
            autotaskTicketingAlertConfigurationRequest.dueDays(this.dueDays);
            autotaskTicketingAlertConfigurationRequest.priority(this.priority);
            autotaskTicketingAlertConfigurationRequest.queue(this.queue);
            autotaskTicketingAlertConfigurationRequest.resource(this.resource);
            autotaskTicketingAlertConfigurationRequest.shouldCreateTickets(this.shouldCreateTickets);
            autotaskTicketingAlertConfigurationRequest.source(this.source);
            autotaskTicketingAlertConfigurationRequest.status(this.status);
            return autotaskTicketingAlertConfigurationRequest;
        }

        /**
         * Execute updateAlertConfiguration request
         * @return AutotaskTicketingAlertConfiguration
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public AutotaskTicketingAlertConfiguration execute() throws ApiException {
            AutotaskTicketingAlertConfigurationRequest autotaskTicketingAlertConfigurationRequest = buildBodyParams();
            ApiResponse<AutotaskTicketingAlertConfiguration> localVarResp = updateAlertConfigurationWithHttpInfo(providerId, alertUUID, autotaskTicketingAlertConfigurationRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute updateAlertConfiguration request with HTTP info returned
         * @return ApiResponse&lt;AutotaskTicketingAlertConfiguration&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<AutotaskTicketingAlertConfiguration> executeWithHttpInfo() throws ApiException {
            AutotaskTicketingAlertConfigurationRequest autotaskTicketingAlertConfigurationRequest = buildBodyParams();
            return updateAlertConfigurationWithHttpInfo(providerId, alertUUID, autotaskTicketingAlertConfigurationRequest);
        }

        /**
         * Execute updateAlertConfiguration request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AutotaskTicketingAlertConfiguration> _callback) throws ApiException {
            AutotaskTicketingAlertConfigurationRequest autotaskTicketingAlertConfigurationRequest = buildBodyParams();
            return updateAlertConfigurationAsync(providerId, alertUUID, autotaskTicketingAlertConfigurationRequest, _callback);
        }
    }

    /**
     * Update an Autotask ticketing alert&#39;s configuration
     * Update an Autotask ticketing alert&#39;s configuration
     * @param providerId  (required)
     * @param alertUUID  (required)
     * @return UpdateAlertConfigurationRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UpdateAlertConfigurationRequestBuilder updateAlertConfiguration(String destination, Integer dueDays, AutotaskTicketingAlertConfigurationPriority priority, Boolean shouldCreateTickets, AutotaskTicketingAlertConfigurationPriority status, String providerId, String alertUUID) throws IllegalArgumentException {
        if (destination == null) throw new IllegalArgumentException("\"destination\" is required but got null");
            

        if (dueDays == null) throw new IllegalArgumentException("\"dueDays\" is required but got null");
        if (priority == null) throw new IllegalArgumentException("\"priority\" is required but got null");
        if (shouldCreateTickets == null) throw new IllegalArgumentException("\"shouldCreateTickets\" is required but got null");
        if (status == null) throw new IllegalArgumentException("\"status\" is required but got null");
        if (providerId == null) throw new IllegalArgumentException("\"providerId\" is required but got null");
            

        if (alertUUID == null) throw new IllegalArgumentException("\"alertUUID\" is required but got null");
            

        return new UpdateAlertConfigurationRequestBuilder(destination, dueDays, priority, shouldCreateTickets, status, providerId, alertUUID);
    }
    private okhttp3.Call updateAlertConfiguration_0Call(String providerId, String alertUUID, ConnectWiseTicketingAlertConfigurationRequest connectWiseTicketingAlertConfigurationRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = connectWiseTicketingAlertConfigurationRequest;

        // create path and map variables
        String localVarPath = "/providers/{provider_id}/integrations/connectwise/alerts/{alert_UUID}/configuration"
            .replace("{" + "provider_id" + "}", localVarApiClient.escapeString(providerId.toString()))
            .replace("{" + "alert_UUID" + "}", localVarApiClient.escapeString(alertUUID.toString()));

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
    private okhttp3.Call updateAlertConfiguration_0ValidateBeforeCall(String providerId, String alertUUID, ConnectWiseTicketingAlertConfigurationRequest connectWiseTicketingAlertConfigurationRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'providerId' is set
        if (providerId == null) {
            throw new ApiException("Missing the required parameter 'providerId' when calling updateAlertConfiguration_0(Async)");
        }

        // verify the required parameter 'alertUUID' is set
        if (alertUUID == null) {
            throw new ApiException("Missing the required parameter 'alertUUID' when calling updateAlertConfiguration_0(Async)");
        }

        return updateAlertConfiguration_0Call(providerId, alertUUID, connectWiseTicketingAlertConfigurationRequest, _callback);

    }


    private ApiResponse<ConnectWiseTicketingAlertConfiguration> updateAlertConfiguration_0WithHttpInfo(String providerId, String alertUUID, ConnectWiseTicketingAlertConfigurationRequest connectWiseTicketingAlertConfigurationRequest) throws ApiException {
        okhttp3.Call localVarCall = updateAlertConfiguration_0ValidateBeforeCall(providerId, alertUUID, connectWiseTicketingAlertConfigurationRequest, null);
        Type localVarReturnType = new TypeToken<ConnectWiseTicketingAlertConfiguration>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call updateAlertConfiguration_0Async(String providerId, String alertUUID, ConnectWiseTicketingAlertConfigurationRequest connectWiseTicketingAlertConfigurationRequest, final ApiCallback<ConnectWiseTicketingAlertConfiguration> _callback) throws ApiException {

        okhttp3.Call localVarCall = updateAlertConfiguration_0ValidateBeforeCall(providerId, alertUUID, connectWiseTicketingAlertConfigurationRequest, _callback);
        Type localVarReturnType = new TypeToken<ConnectWiseTicketingAlertConfiguration>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UpdateAlertConfiguration0RequestBuilder {
        private final Boolean shouldCreateTickets;
        private final String providerId;
        private final String alertUUID;
        private Integer dueDays;
        private AutotaskTicketingAlertConfigurationPriority priority;
        private AutotaskTicketingAlertConfigurationPriority source;

        private UpdateAlertConfiguration0RequestBuilder(Boolean shouldCreateTickets, String providerId, String alertUUID) {
            this.shouldCreateTickets = shouldCreateTickets;
            this.providerId = providerId;
            this.alertUUID = alertUUID;
        }

        /**
         * Set dueDays
         * @param dueDays  (optional)
         * @return UpdateAlertConfiguration0RequestBuilder
         */
        public UpdateAlertConfiguration0RequestBuilder dueDays(Integer dueDays) {
            this.dueDays = dueDays;
            return this;
        }
        
        /**
         * Set priority
         * @param priority  (optional)
         * @return UpdateAlertConfiguration0RequestBuilder
         */
        public UpdateAlertConfiguration0RequestBuilder priority(AutotaskTicketingAlertConfigurationPriority priority) {
            this.priority = priority;
            return this;
        }
        
        /**
         * Set source
         * @param source  (optional)
         * @return UpdateAlertConfiguration0RequestBuilder
         */
        public UpdateAlertConfiguration0RequestBuilder source(AutotaskTicketingAlertConfigurationPriority source) {
            this.source = source;
            return this;
        }
        
        /**
         * Build call for updateAlertConfiguration_0
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
            ConnectWiseTicketingAlertConfigurationRequest connectWiseTicketingAlertConfigurationRequest = buildBodyParams();
            return updateAlertConfiguration_0Call(providerId, alertUUID, connectWiseTicketingAlertConfigurationRequest, _callback);
        }

        private ConnectWiseTicketingAlertConfigurationRequest buildBodyParams() {
            ConnectWiseTicketingAlertConfigurationRequest connectWiseTicketingAlertConfigurationRequest = new ConnectWiseTicketingAlertConfigurationRequest();
            connectWiseTicketingAlertConfigurationRequest.dueDays(this.dueDays);
            connectWiseTicketingAlertConfigurationRequest.priority(this.priority);
            connectWiseTicketingAlertConfigurationRequest.shouldCreateTickets(this.shouldCreateTickets);
            connectWiseTicketingAlertConfigurationRequest.source(this.source);
            return connectWiseTicketingAlertConfigurationRequest;
        }

        /**
         * Execute updateAlertConfiguration_0 request
         * @return ConnectWiseTicketingAlertConfiguration
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ConnectWiseTicketingAlertConfiguration execute() throws ApiException {
            ConnectWiseTicketingAlertConfigurationRequest connectWiseTicketingAlertConfigurationRequest = buildBodyParams();
            ApiResponse<ConnectWiseTicketingAlertConfiguration> localVarResp = updateAlertConfiguration_0WithHttpInfo(providerId, alertUUID, connectWiseTicketingAlertConfigurationRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute updateAlertConfiguration_0 request with HTTP info returned
         * @return ApiResponse&lt;ConnectWiseTicketingAlertConfiguration&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<ConnectWiseTicketingAlertConfiguration> executeWithHttpInfo() throws ApiException {
            ConnectWiseTicketingAlertConfigurationRequest connectWiseTicketingAlertConfigurationRequest = buildBodyParams();
            return updateAlertConfiguration_0WithHttpInfo(providerId, alertUUID, connectWiseTicketingAlertConfigurationRequest);
        }

        /**
         * Execute updateAlertConfiguration_0 request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ConnectWiseTicketingAlertConfiguration> _callback) throws ApiException {
            ConnectWiseTicketingAlertConfigurationRequest connectWiseTicketingAlertConfigurationRequest = buildBodyParams();
            return updateAlertConfiguration_0Async(providerId, alertUUID, connectWiseTicketingAlertConfigurationRequest, _callback);
        }
    }

    /**
     * Update a ConnectWise ticketing alert&#39;s configuration
     * Update a ConnectWise ticketing alert&#39;s configuration.
     * @param providerId  (required)
     * @param alertUUID  (required)
     * @return UpdateAlertConfiguration0RequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UpdateAlertConfiguration0RequestBuilder updateAlertConfiguration_0(Boolean shouldCreateTickets, String providerId, String alertUUID) throws IllegalArgumentException {
        if (shouldCreateTickets == null) throw new IllegalArgumentException("\"shouldCreateTickets\" is required but got null");
        if (providerId == null) throw new IllegalArgumentException("\"providerId\" is required but got null");
            

        if (alertUUID == null) throw new IllegalArgumentException("\"alertUUID\" is required but got null");
            

        return new UpdateAlertConfiguration0RequestBuilder(shouldCreateTickets, providerId, alertUUID);
    }
    private okhttp3.Call updateAlertConfiguration_1Call(String providerId, String alertUUID, SyncroTicketingAlertConfigurationRequest syncroTicketingAlertConfigurationRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = syncroTicketingAlertConfigurationRequest;

        // create path and map variables
        String localVarPath = "/providers/{provider_id}/integrations/syncro/alerts/{alert_UUID}/configuration"
            .replace("{" + "provider_id" + "}", localVarApiClient.escapeString(providerId.toString()))
            .replace("{" + "alert_UUID" + "}", localVarApiClient.escapeString(alertUUID.toString()));

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
    private okhttp3.Call updateAlertConfiguration_1ValidateBeforeCall(String providerId, String alertUUID, SyncroTicketingAlertConfigurationRequest syncroTicketingAlertConfigurationRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'providerId' is set
        if (providerId == null) {
            throw new ApiException("Missing the required parameter 'providerId' when calling updateAlertConfiguration_1(Async)");
        }

        // verify the required parameter 'alertUUID' is set
        if (alertUUID == null) {
            throw new ApiException("Missing the required parameter 'alertUUID' when calling updateAlertConfiguration_1(Async)");
        }

        return updateAlertConfiguration_1Call(providerId, alertUUID, syncroTicketingAlertConfigurationRequest, _callback);

    }


    private ApiResponse<SyncroTicketingAlertConfiguration> updateAlertConfiguration_1WithHttpInfo(String providerId, String alertUUID, SyncroTicketingAlertConfigurationRequest syncroTicketingAlertConfigurationRequest) throws ApiException {
        okhttp3.Call localVarCall = updateAlertConfiguration_1ValidateBeforeCall(providerId, alertUUID, syncroTicketingAlertConfigurationRequest, null);
        Type localVarReturnType = new TypeToken<SyncroTicketingAlertConfiguration>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call updateAlertConfiguration_1Async(String providerId, String alertUUID, SyncroTicketingAlertConfigurationRequest syncroTicketingAlertConfigurationRequest, final ApiCallback<SyncroTicketingAlertConfiguration> _callback) throws ApiException {

        okhttp3.Call localVarCall = updateAlertConfiguration_1ValidateBeforeCall(providerId, alertUUID, syncroTicketingAlertConfigurationRequest, _callback);
        Type localVarReturnType = new TypeToken<SyncroTicketingAlertConfiguration>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UpdateAlertConfiguration1RequestBuilder {
        private final String problemType;
        private final Boolean shouldCreateTickets;
        private final String providerId;
        private final String alertUUID;
        private Integer dueDays;
        private String priority;
        private String status;
        private Double userId;
        private String username;

        private UpdateAlertConfiguration1RequestBuilder(String problemType, Boolean shouldCreateTickets, String providerId, String alertUUID) {
            this.problemType = problemType;
            this.shouldCreateTickets = shouldCreateTickets;
            this.providerId = providerId;
            this.alertUUID = alertUUID;
        }

        /**
         * Set dueDays
         * @param dueDays  (optional)
         * @return UpdateAlertConfiguration1RequestBuilder
         */
        public UpdateAlertConfiguration1RequestBuilder dueDays(Integer dueDays) {
            this.dueDays = dueDays;
            return this;
        }
        
        /**
         * Set priority
         * @param priority  (optional)
         * @return UpdateAlertConfiguration1RequestBuilder
         */
        public UpdateAlertConfiguration1RequestBuilder priority(String priority) {
            this.priority = priority;
            return this;
        }
        
        /**
         * Set status
         * @param status  (optional)
         * @return UpdateAlertConfiguration1RequestBuilder
         */
        public UpdateAlertConfiguration1RequestBuilder status(String status) {
            this.status = status;
            return this;
        }
        
        /**
         * Set userId
         * @param userId  (optional)
         * @return UpdateAlertConfiguration1RequestBuilder
         */
        public UpdateAlertConfiguration1RequestBuilder userId(Double userId) {
            this.userId = userId;
            return this;
        }
        
        /**
         * Set username
         * @param username  (optional)
         * @return UpdateAlertConfiguration1RequestBuilder
         */
        public UpdateAlertConfiguration1RequestBuilder username(String username) {
            this.username = username;
            return this;
        }
        
        /**
         * Build call for updateAlertConfiguration_1
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
            SyncroTicketingAlertConfigurationRequest syncroTicketingAlertConfigurationRequest = buildBodyParams();
            return updateAlertConfiguration_1Call(providerId, alertUUID, syncroTicketingAlertConfigurationRequest, _callback);
        }

        private SyncroTicketingAlertConfigurationRequest buildBodyParams() {
            SyncroTicketingAlertConfigurationRequest syncroTicketingAlertConfigurationRequest = new SyncroTicketingAlertConfigurationRequest();
            syncroTicketingAlertConfigurationRequest.dueDays(this.dueDays);
            syncroTicketingAlertConfigurationRequest.priority(this.priority);
            syncroTicketingAlertConfigurationRequest.problemType(this.problemType);
            syncroTicketingAlertConfigurationRequest.shouldCreateTickets(this.shouldCreateTickets);
            syncroTicketingAlertConfigurationRequest.status(this.status);
            syncroTicketingAlertConfigurationRequest.userId(this.userId);
            syncroTicketingAlertConfigurationRequest.username(this.username);
            return syncroTicketingAlertConfigurationRequest;
        }

        /**
         * Execute updateAlertConfiguration_1 request
         * @return SyncroTicketingAlertConfiguration
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public SyncroTicketingAlertConfiguration execute() throws ApiException {
            SyncroTicketingAlertConfigurationRequest syncroTicketingAlertConfigurationRequest = buildBodyParams();
            ApiResponse<SyncroTicketingAlertConfiguration> localVarResp = updateAlertConfiguration_1WithHttpInfo(providerId, alertUUID, syncroTicketingAlertConfigurationRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute updateAlertConfiguration_1 request with HTTP info returned
         * @return ApiResponse&lt;SyncroTicketingAlertConfiguration&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<SyncroTicketingAlertConfiguration> executeWithHttpInfo() throws ApiException {
            SyncroTicketingAlertConfigurationRequest syncroTicketingAlertConfigurationRequest = buildBodyParams();
            return updateAlertConfiguration_1WithHttpInfo(providerId, alertUUID, syncroTicketingAlertConfigurationRequest);
        }

        /**
         * Execute updateAlertConfiguration_1 request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<SyncroTicketingAlertConfiguration> _callback) throws ApiException {
            SyncroTicketingAlertConfigurationRequest syncroTicketingAlertConfigurationRequest = buildBodyParams();
            return updateAlertConfiguration_1Async(providerId, alertUUID, syncroTicketingAlertConfigurationRequest, _callback);
        }
    }

    /**
     * Update a Syncro ticketing alert&#39;s configuration
     * Update a Syncro ticketing alert&#39;s configuration
     * @param providerId  (required)
     * @param alertUUID  (required)
     * @return UpdateAlertConfiguration1RequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UpdateAlertConfiguration1RequestBuilder updateAlertConfiguration_1(String problemType, Boolean shouldCreateTickets, String providerId, String alertUUID) throws IllegalArgumentException {
        if (problemType == null) throw new IllegalArgumentException("\"problemType\" is required but got null");
            

        if (shouldCreateTickets == null) throw new IllegalArgumentException("\"shouldCreateTickets\" is required but got null");
        if (providerId == null) throw new IllegalArgumentException("\"providerId\" is required but got null");
            

        if (alertUUID == null) throw new IllegalArgumentException("\"alertUUID\" is required but got null");
            

        return new UpdateAlertConfiguration1RequestBuilder(problemType, shouldCreateTickets, providerId, alertUUID);
    }
    private okhttp3.Call updateConfigurationCall(String UUID, AutotaskIntegrationPatchReq autotaskIntegrationPatchReq, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = autotaskIntegrationPatchReq;

        // create path and map variables
        String localVarPath = "/integrations/autotask/{UUID}"
            .replace("{" + "UUID" + "}", localVarApiClient.escapeString(UUID.toString()));

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
        return localVarApiClient.buildCall(basePath, localVarPath, "PATCH", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call updateConfigurationValidateBeforeCall(String UUID, AutotaskIntegrationPatchReq autotaskIntegrationPatchReq, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'UUID' is set
        if (UUID == null) {
            throw new ApiException("Missing the required parameter 'UUID' when calling updateConfiguration(Async)");
        }

        return updateConfigurationCall(UUID, autotaskIntegrationPatchReq, _callback);

    }


    private ApiResponse<AutotaskIntegration> updateConfigurationWithHttpInfo(String UUID, AutotaskIntegrationPatchReq autotaskIntegrationPatchReq) throws ApiException {
        okhttp3.Call localVarCall = updateConfigurationValidateBeforeCall(UUID, autotaskIntegrationPatchReq, null);
        Type localVarReturnType = new TypeToken<AutotaskIntegration>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call updateConfigurationAsync(String UUID, AutotaskIntegrationPatchReq autotaskIntegrationPatchReq, final ApiCallback<AutotaskIntegration> _callback) throws ApiException {

        okhttp3.Call localVarCall = updateConfigurationValidateBeforeCall(UUID, autotaskIntegrationPatchReq, _callback);
        Type localVarReturnType = new TypeToken<AutotaskIntegration>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UpdateConfigurationRequestBuilder {
        private final String UUID;
        private String secret;
        private String username;

        private UpdateConfigurationRequestBuilder(String UUID) {
            this.UUID = UUID;
        }

        /**
         * Set secret
         * @param secret The secret for connecting to Autotask. (optional)
         * @return UpdateConfigurationRequestBuilder
         */
        public UpdateConfigurationRequestBuilder secret(String secret) {
            this.secret = secret;
            return this;
        }
        
        /**
         * Set username
         * @param username The username for connecting to Autotask. (optional)
         * @return UpdateConfigurationRequestBuilder
         */
        public UpdateConfigurationRequestBuilder username(String username) {
            this.username = username;
            return this;
        }
        
        /**
         * Build call for updateConfiguration
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
            AutotaskIntegrationPatchReq autotaskIntegrationPatchReq = buildBodyParams();
            return updateConfigurationCall(UUID, autotaskIntegrationPatchReq, _callback);
        }

        private AutotaskIntegrationPatchReq buildBodyParams() {
            AutotaskIntegrationPatchReq autotaskIntegrationPatchReq = new AutotaskIntegrationPatchReq();
            autotaskIntegrationPatchReq.secret(this.secret);
            autotaskIntegrationPatchReq.username(this.username);
            return autotaskIntegrationPatchReq;
        }

        /**
         * Execute updateConfiguration request
         * @return AutotaskIntegration
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public AutotaskIntegration execute() throws ApiException {
            AutotaskIntegrationPatchReq autotaskIntegrationPatchReq = buildBodyParams();
            ApiResponse<AutotaskIntegration> localVarResp = updateConfigurationWithHttpInfo(UUID, autotaskIntegrationPatchReq);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute updateConfiguration request with HTTP info returned
         * @return ApiResponse&lt;AutotaskIntegration&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<AutotaskIntegration> executeWithHttpInfo() throws ApiException {
            AutotaskIntegrationPatchReq autotaskIntegrationPatchReq = buildBodyParams();
            return updateConfigurationWithHttpInfo(UUID, autotaskIntegrationPatchReq);
        }

        /**
         * Execute updateConfiguration request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AutotaskIntegration> _callback) throws ApiException {
            AutotaskIntegrationPatchReq autotaskIntegrationPatchReq = buildBodyParams();
            return updateConfigurationAsync(UUID, autotaskIntegrationPatchReq, _callback);
        }
    }

    /**
     * Update Autotask Integration configuration
     * Update the Autotask integration configuration. A 422 Unprocessable Entity response means the server failed to validate with Autotask.
     * @param UUID  (required)
     * @return UpdateConfigurationRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UpdateConfigurationRequestBuilder updateConfiguration(String UUID) throws IllegalArgumentException {
        if (UUID == null) throw new IllegalArgumentException("\"UUID\" is required but got null");
            

        return new UpdateConfigurationRequestBuilder(UUID);
    }
    private okhttp3.Call updateConfiguration_0Call(String UUID, ConnectwiseIntegrationPatchReq connectwiseIntegrationPatchReq, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = connectwiseIntegrationPatchReq;

        // create path and map variables
        String localVarPath = "/integrations/connectwise/{UUID}"
            .replace("{" + "UUID" + "}", localVarApiClient.escapeString(UUID.toString()));

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
        return localVarApiClient.buildCall(basePath, localVarPath, "PATCH", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call updateConfiguration_0ValidateBeforeCall(String UUID, ConnectwiseIntegrationPatchReq connectwiseIntegrationPatchReq, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'UUID' is set
        if (UUID == null) {
            throw new ApiException("Missing the required parameter 'UUID' when calling updateConfiguration_0(Async)");
        }

        return updateConfiguration_0Call(UUID, connectwiseIntegrationPatchReq, _callback);

    }


    private ApiResponse<ConnectwiseIntegration> updateConfiguration_0WithHttpInfo(String UUID, ConnectwiseIntegrationPatchReq connectwiseIntegrationPatchReq) throws ApiException {
        okhttp3.Call localVarCall = updateConfiguration_0ValidateBeforeCall(UUID, connectwiseIntegrationPatchReq, null);
        Type localVarReturnType = new TypeToken<ConnectwiseIntegration>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call updateConfiguration_0Async(String UUID, ConnectwiseIntegrationPatchReq connectwiseIntegrationPatchReq, final ApiCallback<ConnectwiseIntegration> _callback) throws ApiException {

        okhttp3.Call localVarCall = updateConfiguration_0ValidateBeforeCall(UUID, connectwiseIntegrationPatchReq, _callback);
        Type localVarReturnType = new TypeToken<ConnectwiseIntegration>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UpdateConfiguration0RequestBuilder {
        private final String UUID;
        private String companyId;
        private String privateKey;
        private String publicKey;
        private String url;

        private UpdateConfiguration0RequestBuilder(String UUID) {
            this.UUID = UUID;
        }

        /**
         * Set companyId
         * @param companyId The ConnectWise company identifier. (optional)
         * @return UpdateConfiguration0RequestBuilder
         */
        public UpdateConfiguration0RequestBuilder companyId(String companyId) {
            this.companyId = companyId;
            return this;
        }
        
        /**
         * Set privateKey
         * @param privateKey The ConnectWise private key for authentication (optional)
         * @return UpdateConfiguration0RequestBuilder
         */
        public UpdateConfiguration0RequestBuilder privateKey(String privateKey) {
            this.privateKey = privateKey;
            return this;
        }
        
        /**
         * Set publicKey
         * @param publicKey The ConnectWise public key for authentication. (optional)
         * @return UpdateConfiguration0RequestBuilder
         */
        public UpdateConfiguration0RequestBuilder publicKey(String publicKey) {
            this.publicKey = publicKey;
            return this;
        }
        
        /**
         * Set url
         * @param url The base url for connecting to ConnectWise. (optional)
         * @return UpdateConfiguration0RequestBuilder
         */
        public UpdateConfiguration0RequestBuilder url(String url) {
            this.url = url;
            return this;
        }
        
        /**
         * Build call for updateConfiguration_0
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
            ConnectwiseIntegrationPatchReq connectwiseIntegrationPatchReq = buildBodyParams();
            return updateConfiguration_0Call(UUID, connectwiseIntegrationPatchReq, _callback);
        }

        private ConnectwiseIntegrationPatchReq buildBodyParams() {
            ConnectwiseIntegrationPatchReq connectwiseIntegrationPatchReq = new ConnectwiseIntegrationPatchReq();
            connectwiseIntegrationPatchReq.companyId(this.companyId);
            connectwiseIntegrationPatchReq.privateKey(this.privateKey);
            connectwiseIntegrationPatchReq.publicKey(this.publicKey);
            connectwiseIntegrationPatchReq.url(this.url);
            return connectwiseIntegrationPatchReq;
        }

        /**
         * Execute updateConfiguration_0 request
         * @return ConnectwiseIntegration
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ConnectwiseIntegration execute() throws ApiException {
            ConnectwiseIntegrationPatchReq connectwiseIntegrationPatchReq = buildBodyParams();
            ApiResponse<ConnectwiseIntegration> localVarResp = updateConfiguration_0WithHttpInfo(UUID, connectwiseIntegrationPatchReq);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute updateConfiguration_0 request with HTTP info returned
         * @return ApiResponse&lt;ConnectwiseIntegration&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<ConnectwiseIntegration> executeWithHttpInfo() throws ApiException {
            ConnectwiseIntegrationPatchReq connectwiseIntegrationPatchReq = buildBodyParams();
            return updateConfiguration_0WithHttpInfo(UUID, connectwiseIntegrationPatchReq);
        }

        /**
         * Execute updateConfiguration_0 request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ConnectwiseIntegration> _callback) throws ApiException {
            ConnectwiseIntegrationPatchReq connectwiseIntegrationPatchReq = buildBodyParams();
            return updateConfiguration_0Async(UUID, connectwiseIntegrationPatchReq, _callback);
        }
    }

    /**
     * Update ConnectWise Integration configuration
     * Update the ConnectWise integration configuration. A 422 Unprocessable Entity response means the server failed to validate with ConnectWise.
     * @param UUID  (required)
     * @return UpdateConfiguration0RequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UpdateConfiguration0RequestBuilder updateConfiguration_0(String UUID) throws IllegalArgumentException {
        if (UUID == null) throw new IllegalArgumentException("\"UUID\" is required but got null");
            

        return new UpdateConfiguration0RequestBuilder(UUID);
    }
    private okhttp3.Call updateConfiguration_1Call(String UUID, SyncroIntegrationPatchReq syncroIntegrationPatchReq, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = syncroIntegrationPatchReq;

        // create path and map variables
        String localVarPath = "/integrations/syncro/{UUID}"
            .replace("{" + "UUID" + "}", localVarApiClient.escapeString(UUID.toString()));

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
        return localVarApiClient.buildCall(basePath, localVarPath, "PATCH", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call updateConfiguration_1ValidateBeforeCall(String UUID, SyncroIntegrationPatchReq syncroIntegrationPatchReq, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'UUID' is set
        if (UUID == null) {
            throw new ApiException("Missing the required parameter 'UUID' when calling updateConfiguration_1(Async)");
        }

        return updateConfiguration_1Call(UUID, syncroIntegrationPatchReq, _callback);

    }


    private ApiResponse<SyncroIntegration> updateConfiguration_1WithHttpInfo(String UUID, SyncroIntegrationPatchReq syncroIntegrationPatchReq) throws ApiException {
        okhttp3.Call localVarCall = updateConfiguration_1ValidateBeforeCall(UUID, syncroIntegrationPatchReq, null);
        Type localVarReturnType = new TypeToken<SyncroIntegration>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call updateConfiguration_1Async(String UUID, SyncroIntegrationPatchReq syncroIntegrationPatchReq, final ApiCallback<SyncroIntegration> _callback) throws ApiException {

        okhttp3.Call localVarCall = updateConfiguration_1ValidateBeforeCall(UUID, syncroIntegrationPatchReq, _callback);
        Type localVarReturnType = new TypeToken<SyncroIntegration>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UpdateConfiguration1RequestBuilder {
        private final String UUID;
        private String apiToken;
        private String subdomain;

        private UpdateConfiguration1RequestBuilder(String UUID) {
            this.UUID = UUID;
        }

        /**
         * Set apiToken
         * @param apiToken The Syncro API token for authentication (optional)
         * @return UpdateConfiguration1RequestBuilder
         */
        public UpdateConfiguration1RequestBuilder apiToken(String apiToken) {
            this.apiToken = apiToken;
            return this;
        }
        
        /**
         * Set subdomain
         * @param subdomain The subdomain for the URL to connect to Syncro. (optional)
         * @return UpdateConfiguration1RequestBuilder
         */
        public UpdateConfiguration1RequestBuilder subdomain(String subdomain) {
            this.subdomain = subdomain;
            return this;
        }
        
        /**
         * Build call for updateConfiguration_1
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
            SyncroIntegrationPatchReq syncroIntegrationPatchReq = buildBodyParams();
            return updateConfiguration_1Call(UUID, syncroIntegrationPatchReq, _callback);
        }

        private SyncroIntegrationPatchReq buildBodyParams() {
            SyncroIntegrationPatchReq syncroIntegrationPatchReq = new SyncroIntegrationPatchReq();
            syncroIntegrationPatchReq.apiToken(this.apiToken);
            syncroIntegrationPatchReq.subdomain(this.subdomain);
            return syncroIntegrationPatchReq;
        }

        /**
         * Execute updateConfiguration_1 request
         * @return SyncroIntegration
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public SyncroIntegration execute() throws ApiException {
            SyncroIntegrationPatchReq syncroIntegrationPatchReq = buildBodyParams();
            ApiResponse<SyncroIntegration> localVarResp = updateConfiguration_1WithHttpInfo(UUID, syncroIntegrationPatchReq);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute updateConfiguration_1 request with HTTP info returned
         * @return ApiResponse&lt;SyncroIntegration&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<SyncroIntegration> executeWithHttpInfo() throws ApiException {
            SyncroIntegrationPatchReq syncroIntegrationPatchReq = buildBodyParams();
            return updateConfiguration_1WithHttpInfo(UUID, syncroIntegrationPatchReq);
        }

        /**
         * Execute updateConfiguration_1 request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<SyncroIntegration> _callback) throws ApiException {
            SyncroIntegrationPatchReq syncroIntegrationPatchReq = buildBodyParams();
            return updateConfiguration_1Async(UUID, syncroIntegrationPatchReq, _callback);
        }
    }

    /**
     * Update Syncro Integration configuration
     * Update the Syncro integration configuration. A 422 Unprocessable Entity response means the server failed to validate with Syncro.
     * @param UUID  (required)
     * @return UpdateConfiguration1RequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>
     </table>
     */
    public UpdateConfiguration1RequestBuilder updateConfiguration_1(String UUID) throws IllegalArgumentException {
        if (UUID == null) throw new IllegalArgumentException("\"UUID\" is required but got null");
            

        return new UpdateConfiguration1RequestBuilder(UUID);
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
