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


import com.konfigthis.client.model.SystemInsightsAlf;
import com.konfigthis.client.model.SystemInsightsAlfExceptions;
import com.konfigthis.client.model.SystemInsightsAlfExplicitAuths;
import com.konfigthis.client.model.SystemInsightsAppcompatShims;
import com.konfigthis.client.model.SystemInsightsApps;
import com.konfigthis.client.model.SystemInsightsAuthorizedKeys;
import com.konfigthis.client.model.SystemInsightsAzureInstanceMetadata;
import com.konfigthis.client.model.SystemInsightsAzureInstanceTags;
import com.konfigthis.client.model.SystemInsightsBattery;
import com.konfigthis.client.model.SystemInsightsBitlockerInfo;
import com.konfigthis.client.model.SystemInsightsBrowserPlugins;
import com.konfigthis.client.model.SystemInsightsCertificates;
import com.konfigthis.client.model.SystemInsightsChassisInfo;
import com.konfigthis.client.model.SystemInsightsChromeExtensions;
import com.konfigthis.client.model.SystemInsightsConnectivity;
import com.konfigthis.client.model.SystemInsightsCrashes;
import com.konfigthis.client.model.SystemInsightsCupsDestinations;
import com.konfigthis.client.model.SystemInsightsDiskEncryption;
import com.konfigthis.client.model.SystemInsightsDiskInfo;
import com.konfigthis.client.model.SystemInsightsDnsResolvers;
import com.konfigthis.client.model.SystemInsightsEtcHosts;
import com.konfigthis.client.model.SystemInsightsFirefoxAddons;
import com.konfigthis.client.model.SystemInsightsGroups;
import com.konfigthis.client.model.SystemInsightsIeExtensions;
import com.konfigthis.client.model.SystemInsightsInterfaceAddresses;
import com.konfigthis.client.model.SystemInsightsInterfaceDetails;
import com.konfigthis.client.model.SystemInsightsKernelInfo;
import com.konfigthis.client.model.SystemInsightsLaunchd;
import com.konfigthis.client.model.SystemInsightsLinuxPackages;
import com.konfigthis.client.model.SystemInsightsLoggedInUsers;
import com.konfigthis.client.model.SystemInsightsLogicalDrives;
import com.konfigthis.client.model.SystemInsightsManagedPolicies;
import com.konfigthis.client.model.SystemInsightsMounts;
import com.konfigthis.client.model.SystemInsightsOsVersion;
import com.konfigthis.client.model.SystemInsightsPatches;
import com.konfigthis.client.model.SystemInsightsPrograms;
import com.konfigthis.client.model.SystemInsightsPythonPackages;
import com.konfigthis.client.model.SystemInsightsSafariExtensions;
import com.konfigthis.client.model.SystemInsightsScheduledTasks;
import com.konfigthis.client.model.SystemInsightsSecureboot;
import com.konfigthis.client.model.SystemInsightsServices;
import com.konfigthis.client.model.SystemInsightsShadow;
import com.konfigthis.client.model.SystemInsightsSharedFolders;
import com.konfigthis.client.model.SystemInsightsSharedResources;
import com.konfigthis.client.model.SystemInsightsSharingPreferences;
import com.konfigthis.client.model.SystemInsightsSipConfig;
import com.konfigthis.client.model.SystemInsightsStartupItems;
import com.konfigthis.client.model.SystemInsightsSystemControls;
import com.konfigthis.client.model.SystemInsightsSystemInfo;
import com.konfigthis.client.model.SystemInsightsTpmInfo;
import com.konfigthis.client.model.SystemInsightsUptime;
import com.konfigthis.client.model.SystemInsightsUsbDevices;
import com.konfigthis.client.model.SystemInsightsUserGroups;
import com.konfigthis.client.model.SystemInsightsUserSshKeys;
import com.konfigthis.client.model.SystemInsightsUserassist;
import com.konfigthis.client.model.SystemInsightsUsers;
import com.konfigthis.client.model.SystemInsightsWifiNetworks;
import com.konfigthis.client.model.SystemInsightsWifiStatus;
import com.konfigthis.client.model.SystemInsightsWindowsSecurityCenter;
import com.konfigthis.client.model.SystemInsightsWindowsSecurityProducts;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;

public class SystemInsightsApiGenerated {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public SystemInsightsApiGenerated() throws IllegalArgumentException {
        this(Configuration.getDefaultApiClient());
    }

    public SystemInsightsApiGenerated(ApiClient apiClient) throws IllegalArgumentException {
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

    private okhttp3.Call getChassisInfoCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/chassis_info";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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

        String[] localVarAuthNames = new String[] {  };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call getChassisInfoValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return getChassisInfoCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsChassisInfo>> getChassisInfoWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = getChassisInfoValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsChassisInfo>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getChassisInfoAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsChassisInfo>> _callback) throws ApiException {

        okhttp3.Call localVarCall = getChassisInfoValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsChassisInfo>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetChassisInfoRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private GetChassisInfoRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return GetChassisInfoRequestBuilder
         */
        public GetChassisInfoRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return GetChassisInfoRequestBuilder
         */
        public GetChassisInfoRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return GetChassisInfoRequestBuilder
         */
        public GetChassisInfoRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return GetChassisInfoRequestBuilder
         */
        public GetChassisInfoRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return GetChassisInfoRequestBuilder
         */
        public GetChassisInfoRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for getChassisInfo
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
            return getChassisInfoCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute getChassisInfo request
         * @return List&lt;SystemInsightsChassisInfo&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsChassisInfo> execute() throws ApiException {
            ApiResponse<List<SystemInsightsChassisInfo>> localVarResp = getChassisInfoWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getChassisInfo request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsChassisInfo&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsChassisInfo>> executeWithHttpInfo() throws ApiException {
            return getChassisInfoWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute getChassisInfo request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsChassisInfo>> _callback) throws ApiException {
            return getChassisInfoAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights Chassis Info
     * Valid filter fields are &#x60;system_id&#x60;.
     * @return GetChassisInfoRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public GetChassisInfoRequestBuilder getChassisInfo() throws IllegalArgumentException {
        return new GetChassisInfoRequestBuilder();
    }
    private okhttp3.Call getDiskInfoCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/disk_info";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call getDiskInfoValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return getDiskInfoCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsDiskInfo>> getDiskInfoWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = getDiskInfoValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsDiskInfo>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getDiskInfoAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsDiskInfo>> _callback) throws ApiException {

        okhttp3.Call localVarCall = getDiskInfoValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsDiskInfo>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetDiskInfoRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private GetDiskInfoRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return GetDiskInfoRequestBuilder
         */
        public GetDiskInfoRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return GetDiskInfoRequestBuilder
         */
        public GetDiskInfoRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return GetDiskInfoRequestBuilder
         */
        public GetDiskInfoRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return GetDiskInfoRequestBuilder
         */
        public GetDiskInfoRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return GetDiskInfoRequestBuilder
         */
        public GetDiskInfoRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for getDiskInfo
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
            return getDiskInfoCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute getDiskInfo request
         * @return List&lt;SystemInsightsDiskInfo&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsDiskInfo> execute() throws ApiException {
            ApiResponse<List<SystemInsightsDiskInfo>> localVarResp = getDiskInfoWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getDiskInfo request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsDiskInfo&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsDiskInfo>> executeWithHttpInfo() throws ApiException {
            return getDiskInfoWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute getDiskInfo request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsDiskInfo>> _callback) throws ApiException {
            return getDiskInfoAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights Disk Info
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;disk_index&#x60;.
     * @return GetDiskInfoRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public GetDiskInfoRequestBuilder getDiskInfo() throws IllegalArgumentException {
        return new GetDiskInfoRequestBuilder();
    }
    private okhttp3.Call getIEExtensionsListCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/ie_extensions";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call getIEExtensionsListValidateBeforeCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
        return getIEExtensionsListCall(xOrgId, skip, sort, filter, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsIeExtensions>> getIEExtensionsListWithHttpInfo(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = getIEExtensionsListValidateBeforeCall(xOrgId, skip, sort, filter, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsIeExtensions>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getIEExtensionsListAsync(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback<List<SystemInsightsIeExtensions>> _callback) throws ApiException {

        okhttp3.Call localVarCall = getIEExtensionsListValidateBeforeCall(xOrgId, skip, sort, filter, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsIeExtensions>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetIEExtensionsListRequestBuilder {
        private String xOrgId;
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private Integer limit;

        private GetIEExtensionsListRequestBuilder() {
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return GetIEExtensionsListRequestBuilder
         */
        public GetIEExtensionsListRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return GetIEExtensionsListRequestBuilder
         */
        public GetIEExtensionsListRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return GetIEExtensionsListRequestBuilder
         */
        public GetIEExtensionsListRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return GetIEExtensionsListRequestBuilder
         */
        public GetIEExtensionsListRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return GetIEExtensionsListRequestBuilder
         */
        public GetIEExtensionsListRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for getIEExtensionsList
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
            return getIEExtensionsListCall(xOrgId, skip, sort, filter, limit, _callback);
        }


        /**
         * Execute getIEExtensionsList request
         * @return List&lt;SystemInsightsIeExtensions&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsIeExtensions> execute() throws ApiException {
            ApiResponse<List<SystemInsightsIeExtensions>> localVarResp = getIEExtensionsListWithHttpInfo(xOrgId, skip, sort, filter, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getIEExtensionsList request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsIeExtensions&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsIeExtensions>> executeWithHttpInfo() throws ApiException {
            return getIEExtensionsListWithHttpInfo(xOrgId, skip, sort, filter, limit);
        }

        /**
         * Execute getIEExtensionsList request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsIeExtensions>> _callback) throws ApiException {
            return getIEExtensionsListAsync(xOrgId, skip, sort, filter, limit, _callback);
        }
    }

    /**
     * List System Insights IE Extensions
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.
     * @return GetIEExtensionsListRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public GetIEExtensionsListRequestBuilder getIEExtensionsList() throws IllegalArgumentException {
        return new GetIEExtensionsListRequestBuilder();
    }
    private okhttp3.Call getKernelInfoCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/kernel_info";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call getKernelInfoValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return getKernelInfoCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsKernelInfo>> getKernelInfoWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = getKernelInfoValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsKernelInfo>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getKernelInfoAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsKernelInfo>> _callback) throws ApiException {

        okhttp3.Call localVarCall = getKernelInfoValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsKernelInfo>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetKernelInfoRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private GetKernelInfoRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return GetKernelInfoRequestBuilder
         */
        public GetKernelInfoRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return GetKernelInfoRequestBuilder
         */
        public GetKernelInfoRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return GetKernelInfoRequestBuilder
         */
        public GetKernelInfoRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return GetKernelInfoRequestBuilder
         */
        public GetKernelInfoRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return GetKernelInfoRequestBuilder
         */
        public GetKernelInfoRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for getKernelInfo
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
            return getKernelInfoCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute getKernelInfo request
         * @return List&lt;SystemInsightsKernelInfo&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsKernelInfo> execute() throws ApiException {
            ApiResponse<List<SystemInsightsKernelInfo>> localVarResp = getKernelInfoWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getKernelInfo request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsKernelInfo&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsKernelInfo>> executeWithHttpInfo() throws ApiException {
            return getKernelInfoWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute getKernelInfo request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsKernelInfo>> _callback) throws ApiException {
            return getKernelInfoAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights Kernel Info
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;version&#x60;.
     * @return GetKernelInfoRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public GetKernelInfoRequestBuilder getKernelInfo() throws IllegalArgumentException {
        return new GetKernelInfoRequestBuilder();
    }
    private okhttp3.Call getOsVersionCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/os_version";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call getOsVersionValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return getOsVersionCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsOsVersion>> getOsVersionWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = getOsVersionValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsOsVersion>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getOsVersionAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsOsVersion>> _callback) throws ApiException {

        okhttp3.Call localVarCall = getOsVersionValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsOsVersion>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetOsVersionRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private GetOsVersionRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return GetOsVersionRequestBuilder
         */
        public GetOsVersionRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return GetOsVersionRequestBuilder
         */
        public GetOsVersionRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return GetOsVersionRequestBuilder
         */
        public GetOsVersionRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return GetOsVersionRequestBuilder
         */
        public GetOsVersionRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return GetOsVersionRequestBuilder
         */
        public GetOsVersionRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for getOsVersion
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
            return getOsVersionCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute getOsVersion request
         * @return List&lt;SystemInsightsOsVersion&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsOsVersion> execute() throws ApiException {
            ApiResponse<List<SystemInsightsOsVersion>> localVarResp = getOsVersionWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getOsVersion request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsOsVersion&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsOsVersion>> executeWithHttpInfo() throws ApiException {
            return getOsVersionWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute getOsVersion request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsOsVersion>> _callback) throws ApiException {
            return getOsVersionAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights OS Version
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;version&#x60;.
     * @return GetOsVersionRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public GetOsVersionRequestBuilder getOsVersion() throws IllegalArgumentException {
        return new GetOsVersionRequestBuilder();
    }
    private okhttp3.Call getSipConfigCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/sip_config";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call getSipConfigValidateBeforeCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
        return getSipConfigCall(xOrgId, skip, sort, filter, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsSipConfig>> getSipConfigWithHttpInfo(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = getSipConfigValidateBeforeCall(xOrgId, skip, sort, filter, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsSipConfig>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getSipConfigAsync(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback<List<SystemInsightsSipConfig>> _callback) throws ApiException {

        okhttp3.Call localVarCall = getSipConfigValidateBeforeCall(xOrgId, skip, sort, filter, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsSipConfig>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetSipConfigRequestBuilder {
        private String xOrgId;
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private Integer limit;

        private GetSipConfigRequestBuilder() {
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return GetSipConfigRequestBuilder
         */
        public GetSipConfigRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return GetSipConfigRequestBuilder
         */
        public GetSipConfigRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return GetSipConfigRequestBuilder
         */
        public GetSipConfigRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return GetSipConfigRequestBuilder
         */
        public GetSipConfigRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return GetSipConfigRequestBuilder
         */
        public GetSipConfigRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for getSipConfig
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
            return getSipConfigCall(xOrgId, skip, sort, filter, limit, _callback);
        }


        /**
         * Execute getSipConfig request
         * @return List&lt;SystemInsightsSipConfig&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsSipConfig> execute() throws ApiException {
            ApiResponse<List<SystemInsightsSipConfig>> localVarResp = getSipConfigWithHttpInfo(xOrgId, skip, sort, filter, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getSipConfig request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsSipConfig&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsSipConfig>> executeWithHttpInfo() throws ApiException {
            return getSipConfigWithHttpInfo(xOrgId, skip, sort, filter, limit);
        }

        /**
         * Execute getSipConfig request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsSipConfig>> _callback) throws ApiException {
            return getSipConfigAsync(xOrgId, skip, sort, filter, limit, _callback);
        }
    }

    /**
     * List System Insights SIP Config
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;enabled&#x60;.
     * @return GetSipConfigRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public GetSipConfigRequestBuilder getSipConfig() throws IllegalArgumentException {
        return new GetSipConfigRequestBuilder();
    }
    private okhttp3.Call getSystemInfoListCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/system_info";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call getSystemInfoListValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return getSystemInfoListCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsSystemInfo>> getSystemInfoListWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = getSystemInfoListValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsSystemInfo>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getSystemInfoListAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsSystemInfo>> _callback) throws ApiException {

        okhttp3.Call localVarCall = getSystemInfoListValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsSystemInfo>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetSystemInfoListRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private GetSystemInfoListRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return GetSystemInfoListRequestBuilder
         */
        public GetSystemInfoListRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return GetSystemInfoListRequestBuilder
         */
        public GetSystemInfoListRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return GetSystemInfoListRequestBuilder
         */
        public GetSystemInfoListRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return GetSystemInfoListRequestBuilder
         */
        public GetSystemInfoListRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return GetSystemInfoListRequestBuilder
         */
        public GetSystemInfoListRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for getSystemInfoList
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
            return getSystemInfoListCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute getSystemInfoList request
         * @return List&lt;SystemInsightsSystemInfo&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsSystemInfo> execute() throws ApiException {
            ApiResponse<List<SystemInsightsSystemInfo>> localVarResp = getSystemInfoListWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getSystemInfoList request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsSystemInfo&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsSystemInfo>> executeWithHttpInfo() throws ApiException {
            return getSystemInfoListWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute getSystemInfoList request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsSystemInfo>> _callback) throws ApiException {
            return getSystemInfoListAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights System Info
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;cpu_subtype&#x60;.
     * @return GetSystemInfoListRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public GetSystemInfoListRequestBuilder getSystemInfoList() throws IllegalArgumentException {
        return new GetSystemInfoListRequestBuilder();
    }
    private okhttp3.Call getTpmInfoCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/tpm_info";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
        }

        if (xOrgId != null) {
            localVarHeaderParams.put("x-org-id", localVarApiClient.parameterToString(xOrgId));
        }

        final String[] localVarAccepts = {
            "text/html"
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

        String[] localVarAuthNames = new String[] {  };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call getTpmInfoValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return getTpmInfoCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsTpmInfo>> getTpmInfoWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = getTpmInfoValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsTpmInfo>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getTpmInfoAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsTpmInfo>> _callback) throws ApiException {

        okhttp3.Call localVarCall = getTpmInfoValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsTpmInfo>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetTpmInfoRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private GetTpmInfoRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return GetTpmInfoRequestBuilder
         */
        public GetTpmInfoRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return GetTpmInfoRequestBuilder
         */
        public GetTpmInfoRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return GetTpmInfoRequestBuilder
         */
        public GetTpmInfoRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return GetTpmInfoRequestBuilder
         */
        public GetTpmInfoRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return GetTpmInfoRequestBuilder
         */
        public GetTpmInfoRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for getTpmInfo
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
            return getTpmInfoCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute getTpmInfo request
         * @return List&lt;SystemInsightsTpmInfo&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsTpmInfo> execute() throws ApiException {
            ApiResponse<List<SystemInsightsTpmInfo>> localVarResp = getTpmInfoWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getTpmInfo request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsTpmInfo&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsTpmInfo>> executeWithHttpInfo() throws ApiException {
            return getTpmInfoWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute getTpmInfo request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsTpmInfo>> _callback) throws ApiException {
            return getTpmInfoAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights TPM Info
     * Valid filter fields are &#x60;system_id&#x60;.
     * @return GetTpmInfoRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public GetTpmInfoRequestBuilder getTpmInfo() throws IllegalArgumentException {
        return new GetTpmInfoRequestBuilder();
    }
    private okhttp3.Call getUserGroupsCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/user_groups";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call getUserGroupsValidateBeforeCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
        return getUserGroupsCall(xOrgId, skip, sort, filter, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsUserGroups>> getUserGroupsWithHttpInfo(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = getUserGroupsValidateBeforeCall(xOrgId, skip, sort, filter, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsUserGroups>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getUserGroupsAsync(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback<List<SystemInsightsUserGroups>> _callback) throws ApiException {

        okhttp3.Call localVarCall = getUserGroupsValidateBeforeCall(xOrgId, skip, sort, filter, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsUserGroups>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetUserGroupsRequestBuilder {
        private String xOrgId;
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private Integer limit;

        private GetUserGroupsRequestBuilder() {
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return GetUserGroupsRequestBuilder
         */
        public GetUserGroupsRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return GetUserGroupsRequestBuilder
         */
        public GetUserGroupsRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return GetUserGroupsRequestBuilder
         */
        public GetUserGroupsRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return GetUserGroupsRequestBuilder
         */
        public GetUserGroupsRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return GetUserGroupsRequestBuilder
         */
        public GetUserGroupsRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for getUserGroups
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
            return getUserGroupsCall(xOrgId, skip, sort, filter, limit, _callback);
        }


        /**
         * Execute getUserGroups request
         * @return List&lt;SystemInsightsUserGroups&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsUserGroups> execute() throws ApiException {
            ApiResponse<List<SystemInsightsUserGroups>> localVarResp = getUserGroupsWithHttpInfo(xOrgId, skip, sort, filter, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getUserGroups request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsUserGroups&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsUserGroups>> executeWithHttpInfo() throws ApiException {
            return getUserGroupsWithHttpInfo(xOrgId, skip, sort, filter, limit);
        }

        /**
         * Execute getUserGroups request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsUserGroups>> _callback) throws ApiException {
            return getUserGroupsAsync(xOrgId, skip, sort, filter, limit, _callback);
        }
    }

    /**
     * List System Insights User Groups
     * Only valid filter field is &#x60;system_id&#x60;.
     * @return GetUserGroupsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public GetUserGroupsRequestBuilder getUserGroups() throws IllegalArgumentException {
        return new GetUserGroupsRequestBuilder();
    }
    private okhttp3.Call listAlfCall(String xOrgId, List<String> filter, Integer skip, List<String> sort, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/alf";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
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
    private okhttp3.Call listAlfValidateBeforeCall(String xOrgId, List<String> filter, Integer skip, List<String> sort, Integer limit, final ApiCallback _callback) throws ApiException {
        return listAlfCall(xOrgId, filter, skip, sort, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsAlf>> listAlfWithHttpInfo(String xOrgId, List<String> filter, Integer skip, List<String> sort, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listAlfValidateBeforeCall(xOrgId, filter, skip, sort, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsAlf>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listAlfAsync(String xOrgId, List<String> filter, Integer skip, List<String> sort, Integer limit, final ApiCallback<List<SystemInsightsAlf>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listAlfValidateBeforeCall(xOrgId, filter, skip, sort, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsAlf>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListAlfRequestBuilder {
        private String xOrgId;
        private List<String> filter;
        private Integer skip;
        private List<String> sort;
        private Integer limit;

        private ListAlfRequestBuilder() {
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListAlfRequestBuilder
         */
        public ListAlfRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListAlfRequestBuilder
         */
        public ListAlfRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListAlfRequestBuilder
         */
        public ListAlfRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListAlfRequestBuilder
         */
        public ListAlfRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListAlfRequestBuilder
         */
        public ListAlfRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listAlf
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
            return listAlfCall(xOrgId, filter, skip, sort, limit, _callback);
        }


        /**
         * Execute listAlf request
         * @return List&lt;SystemInsightsAlf&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsAlf> execute() throws ApiException {
            ApiResponse<List<SystemInsightsAlf>> localVarResp = listAlfWithHttpInfo(xOrgId, filter, skip, sort, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listAlf request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsAlf&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsAlf>> executeWithHttpInfo() throws ApiException {
            return listAlfWithHttpInfo(xOrgId, filter, skip, sort, limit);
        }

        /**
         * Execute listAlf request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsAlf>> _callback) throws ApiException {
            return listAlfAsync(xOrgId, filter, skip, sort, limit, _callback);
        }
    }

    /**
     * List System Insights ALF
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;global_state&#x60;.
     * @return ListAlfRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListAlfRequestBuilder listAlf() throws IllegalArgumentException {
        return new ListAlfRequestBuilder();
    }
    private okhttp3.Call listAlfExceptionsCall(String xOrgId, List<String> filter, Integer skip, List<String> sort, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/alf_exceptions";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
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
    private okhttp3.Call listAlfExceptionsValidateBeforeCall(String xOrgId, List<String> filter, Integer skip, List<String> sort, Integer limit, final ApiCallback _callback) throws ApiException {
        return listAlfExceptionsCall(xOrgId, filter, skip, sort, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsAlfExceptions>> listAlfExceptionsWithHttpInfo(String xOrgId, List<String> filter, Integer skip, List<String> sort, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listAlfExceptionsValidateBeforeCall(xOrgId, filter, skip, sort, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsAlfExceptions>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listAlfExceptionsAsync(String xOrgId, List<String> filter, Integer skip, List<String> sort, Integer limit, final ApiCallback<List<SystemInsightsAlfExceptions>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listAlfExceptionsValidateBeforeCall(xOrgId, filter, skip, sort, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsAlfExceptions>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListAlfExceptionsRequestBuilder {
        private String xOrgId;
        private List<String> filter;
        private Integer skip;
        private List<String> sort;
        private Integer limit;

        private ListAlfExceptionsRequestBuilder() {
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListAlfExceptionsRequestBuilder
         */
        public ListAlfExceptionsRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListAlfExceptionsRequestBuilder
         */
        public ListAlfExceptionsRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListAlfExceptionsRequestBuilder
         */
        public ListAlfExceptionsRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListAlfExceptionsRequestBuilder
         */
        public ListAlfExceptionsRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListAlfExceptionsRequestBuilder
         */
        public ListAlfExceptionsRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listAlfExceptions
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
            return listAlfExceptionsCall(xOrgId, filter, skip, sort, limit, _callback);
        }


        /**
         * Execute listAlfExceptions request
         * @return List&lt;SystemInsightsAlfExceptions&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsAlfExceptions> execute() throws ApiException {
            ApiResponse<List<SystemInsightsAlfExceptions>> localVarResp = listAlfExceptionsWithHttpInfo(xOrgId, filter, skip, sort, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listAlfExceptions request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsAlfExceptions&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsAlfExceptions>> executeWithHttpInfo() throws ApiException {
            return listAlfExceptionsWithHttpInfo(xOrgId, filter, skip, sort, limit);
        }

        /**
         * Execute listAlfExceptions request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsAlfExceptions>> _callback) throws ApiException {
            return listAlfExceptionsAsync(xOrgId, filter, skip, sort, limit, _callback);
        }
    }

    /**
     * List System Insights ALF Exceptions
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;state&#x60;.
     * @return ListAlfExceptionsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListAlfExceptionsRequestBuilder listAlfExceptions() throws IllegalArgumentException {
        return new ListAlfExceptionsRequestBuilder();
    }
    private okhttp3.Call listAlfExplicitAuthsCall(String xOrgId, List<String> filter, Integer skip, List<String> sort, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/alf_explicit_auths";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
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
    private okhttp3.Call listAlfExplicitAuthsValidateBeforeCall(String xOrgId, List<String> filter, Integer skip, List<String> sort, Integer limit, final ApiCallback _callback) throws ApiException {
        return listAlfExplicitAuthsCall(xOrgId, filter, skip, sort, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsAlfExplicitAuths>> listAlfExplicitAuthsWithHttpInfo(String xOrgId, List<String> filter, Integer skip, List<String> sort, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listAlfExplicitAuthsValidateBeforeCall(xOrgId, filter, skip, sort, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsAlfExplicitAuths>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listAlfExplicitAuthsAsync(String xOrgId, List<String> filter, Integer skip, List<String> sort, Integer limit, final ApiCallback<List<SystemInsightsAlfExplicitAuths>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listAlfExplicitAuthsValidateBeforeCall(xOrgId, filter, skip, sort, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsAlfExplicitAuths>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListAlfExplicitAuthsRequestBuilder {
        private String xOrgId;
        private List<String> filter;
        private Integer skip;
        private List<String> sort;
        private Integer limit;

        private ListAlfExplicitAuthsRequestBuilder() {
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListAlfExplicitAuthsRequestBuilder
         */
        public ListAlfExplicitAuthsRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListAlfExplicitAuthsRequestBuilder
         */
        public ListAlfExplicitAuthsRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListAlfExplicitAuthsRequestBuilder
         */
        public ListAlfExplicitAuthsRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListAlfExplicitAuthsRequestBuilder
         */
        public ListAlfExplicitAuthsRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListAlfExplicitAuthsRequestBuilder
         */
        public ListAlfExplicitAuthsRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listAlfExplicitAuths
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
            return listAlfExplicitAuthsCall(xOrgId, filter, skip, sort, limit, _callback);
        }


        /**
         * Execute listAlfExplicitAuths request
         * @return List&lt;SystemInsightsAlfExplicitAuths&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsAlfExplicitAuths> execute() throws ApiException {
            ApiResponse<List<SystemInsightsAlfExplicitAuths>> localVarResp = listAlfExplicitAuthsWithHttpInfo(xOrgId, filter, skip, sort, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listAlfExplicitAuths request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsAlfExplicitAuths&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsAlfExplicitAuths>> executeWithHttpInfo() throws ApiException {
            return listAlfExplicitAuthsWithHttpInfo(xOrgId, filter, skip, sort, limit);
        }

        /**
         * Execute listAlfExplicitAuths request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsAlfExplicitAuths>> _callback) throws ApiException {
            return listAlfExplicitAuthsAsync(xOrgId, filter, skip, sort, limit, _callback);
        }
    }

    /**
     * List System Insights ALF Explicit Authentications
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;process&#x60;.
     * @return ListAlfExplicitAuthsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListAlfExplicitAuthsRequestBuilder listAlfExplicitAuths() throws IllegalArgumentException {
        return new ListAlfExplicitAuthsRequestBuilder();
    }
    private okhttp3.Call listAppcompatShimsCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/appcompat_shims";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listAppcompatShimsValidateBeforeCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
        return listAppcompatShimsCall(xOrgId, skip, sort, filter, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsAppcompatShims>> listAppcompatShimsWithHttpInfo(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listAppcompatShimsValidateBeforeCall(xOrgId, skip, sort, filter, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsAppcompatShims>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listAppcompatShimsAsync(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback<List<SystemInsightsAppcompatShims>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listAppcompatShimsValidateBeforeCall(xOrgId, skip, sort, filter, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsAppcompatShims>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListAppcompatShimsRequestBuilder {
        private String xOrgId;
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private Integer limit;

        private ListAppcompatShimsRequestBuilder() {
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListAppcompatShimsRequestBuilder
         */
        public ListAppcompatShimsRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListAppcompatShimsRequestBuilder
         */
        public ListAppcompatShimsRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListAppcompatShimsRequestBuilder
         */
        public ListAppcompatShimsRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListAppcompatShimsRequestBuilder
         */
        public ListAppcompatShimsRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListAppcompatShimsRequestBuilder
         */
        public ListAppcompatShimsRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listAppcompatShims
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
            return listAppcompatShimsCall(xOrgId, skip, sort, filter, limit, _callback);
        }


        /**
         * Execute listAppcompatShims request
         * @return List&lt;SystemInsightsAppcompatShims&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsAppcompatShims> execute() throws ApiException {
            ApiResponse<List<SystemInsightsAppcompatShims>> localVarResp = listAppcompatShimsWithHttpInfo(xOrgId, skip, sort, filter, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listAppcompatShims request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsAppcompatShims&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsAppcompatShims>> executeWithHttpInfo() throws ApiException {
            return listAppcompatShimsWithHttpInfo(xOrgId, skip, sort, filter, limit);
        }

        /**
         * Execute listAppcompatShims request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsAppcompatShims>> _callback) throws ApiException {
            return listAppcompatShimsAsync(xOrgId, skip, sort, filter, limit, _callback);
        }
    }

    /**
     * List System Insights Application Compatibility Shims
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;enabled&#x60;.
     * @return ListAppcompatShimsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListAppcompatShimsRequestBuilder listAppcompatShims() throws IllegalArgumentException {
        return new ListAppcompatShimsRequestBuilder();
    }
    private okhttp3.Call listAppsCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/apps";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listAppsValidateBeforeCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
        return listAppsCall(xOrgId, skip, sort, filter, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsApps>> listAppsWithHttpInfo(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listAppsValidateBeforeCall(xOrgId, skip, sort, filter, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsApps>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listAppsAsync(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback<List<SystemInsightsApps>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listAppsValidateBeforeCall(xOrgId, skip, sort, filter, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsApps>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListAppsRequestBuilder {
        private String xOrgId;
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private Integer limit;

        private ListAppsRequestBuilder() {
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListAppsRequestBuilder
         */
        public ListAppsRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListAppsRequestBuilder
         */
        public ListAppsRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListAppsRequestBuilder
         */
        public ListAppsRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListAppsRequestBuilder
         */
        public ListAppsRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListAppsRequestBuilder
         */
        public ListAppsRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listApps
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
            return listAppsCall(xOrgId, skip, sort, filter, limit, _callback);
        }


        /**
         * Execute listApps request
         * @return List&lt;SystemInsightsApps&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsApps> execute() throws ApiException {
            ApiResponse<List<SystemInsightsApps>> localVarResp = listAppsWithHttpInfo(xOrgId, skip, sort, filter, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listApps request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsApps&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsApps>> executeWithHttpInfo() throws ApiException {
            return listAppsWithHttpInfo(xOrgId, skip, sort, filter, limit);
        }

        /**
         * Execute listApps request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsApps>> _callback) throws ApiException {
            return listAppsAsync(xOrgId, skip, sort, filter, limit, _callback);
        }
    }

    /**
     * List System Insights Apps
     * Lists all apps for macOS devices. For Windows devices, use [List System Insights Programs](https://docs.jumpcloud.com).  Valid filter fields are &#x60;system_id&#x60; and &#x60;bundle_name&#x60;.
     * @return ListAppsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListAppsRequestBuilder listApps() throws IllegalArgumentException {
        return new ListAppsRequestBuilder();
    }
    private okhttp3.Call listAuthorizedKeysCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/authorized_keys";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listAuthorizedKeysValidateBeforeCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
        return listAuthorizedKeysCall(xOrgId, skip, sort, filter, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsAuthorizedKeys>> listAuthorizedKeysWithHttpInfo(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listAuthorizedKeysValidateBeforeCall(xOrgId, skip, sort, filter, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsAuthorizedKeys>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listAuthorizedKeysAsync(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback<List<SystemInsightsAuthorizedKeys>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listAuthorizedKeysValidateBeforeCall(xOrgId, skip, sort, filter, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsAuthorizedKeys>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListAuthorizedKeysRequestBuilder {
        private String xOrgId;
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private Integer limit;

        private ListAuthorizedKeysRequestBuilder() {
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListAuthorizedKeysRequestBuilder
         */
        public ListAuthorizedKeysRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListAuthorizedKeysRequestBuilder
         */
        public ListAuthorizedKeysRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListAuthorizedKeysRequestBuilder
         */
        public ListAuthorizedKeysRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListAuthorizedKeysRequestBuilder
         */
        public ListAuthorizedKeysRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListAuthorizedKeysRequestBuilder
         */
        public ListAuthorizedKeysRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listAuthorizedKeys
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
            return listAuthorizedKeysCall(xOrgId, skip, sort, filter, limit, _callback);
        }


        /**
         * Execute listAuthorizedKeys request
         * @return List&lt;SystemInsightsAuthorizedKeys&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsAuthorizedKeys> execute() throws ApiException {
            ApiResponse<List<SystemInsightsAuthorizedKeys>> localVarResp = listAuthorizedKeysWithHttpInfo(xOrgId, skip, sort, filter, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listAuthorizedKeys request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsAuthorizedKeys&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsAuthorizedKeys>> executeWithHttpInfo() throws ApiException {
            return listAuthorizedKeysWithHttpInfo(xOrgId, skip, sort, filter, limit);
        }

        /**
         * Execute listAuthorizedKeys request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsAuthorizedKeys>> _callback) throws ApiException {
            return listAuthorizedKeysAsync(xOrgId, skip, sort, filter, limit, _callback);
        }
    }

    /**
     * List System Insights Authorized Keys
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;uid&#x60;.
     * @return ListAuthorizedKeysRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListAuthorizedKeysRequestBuilder listAuthorizedKeys() throws IllegalArgumentException {
        return new ListAuthorizedKeysRequestBuilder();
    }
    private okhttp3.Call listAzureInstanceMetadataCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/azure_instance_metadata";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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

        String[] localVarAuthNames = new String[] {  };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call listAzureInstanceMetadataValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return listAzureInstanceMetadataCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsAzureInstanceMetadata>> listAzureInstanceMetadataWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listAzureInstanceMetadataValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsAzureInstanceMetadata>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listAzureInstanceMetadataAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsAzureInstanceMetadata>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listAzureInstanceMetadataValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsAzureInstanceMetadata>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListAzureInstanceMetadataRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private ListAzureInstanceMetadataRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListAzureInstanceMetadataRequestBuilder
         */
        public ListAzureInstanceMetadataRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListAzureInstanceMetadataRequestBuilder
         */
        public ListAzureInstanceMetadataRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListAzureInstanceMetadataRequestBuilder
         */
        public ListAzureInstanceMetadataRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListAzureInstanceMetadataRequestBuilder
         */
        public ListAzureInstanceMetadataRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListAzureInstanceMetadataRequestBuilder
         */
        public ListAzureInstanceMetadataRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listAzureInstanceMetadata
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
            return listAzureInstanceMetadataCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute listAzureInstanceMetadata request
         * @return List&lt;SystemInsightsAzureInstanceMetadata&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsAzureInstanceMetadata> execute() throws ApiException {
            ApiResponse<List<SystemInsightsAzureInstanceMetadata>> localVarResp = listAzureInstanceMetadataWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listAzureInstanceMetadata request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsAzureInstanceMetadata&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsAzureInstanceMetadata>> executeWithHttpInfo() throws ApiException {
            return listAzureInstanceMetadataWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute listAzureInstanceMetadata request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsAzureInstanceMetadata>> _callback) throws ApiException {
            return listAzureInstanceMetadataAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights Azure Instance Metadata
     * Valid filter fields are &#x60;system_id&#x60;.
     * @return ListAzureInstanceMetadataRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListAzureInstanceMetadataRequestBuilder listAzureInstanceMetadata() throws IllegalArgumentException {
        return new ListAzureInstanceMetadataRequestBuilder();
    }
    private okhttp3.Call listAzureInstanceTagsCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/azure_instance_tags";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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

        String[] localVarAuthNames = new String[] {  };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call listAzureInstanceTagsValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return listAzureInstanceTagsCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsAzureInstanceTags>> listAzureInstanceTagsWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listAzureInstanceTagsValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsAzureInstanceTags>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listAzureInstanceTagsAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsAzureInstanceTags>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listAzureInstanceTagsValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsAzureInstanceTags>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListAzureInstanceTagsRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private ListAzureInstanceTagsRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListAzureInstanceTagsRequestBuilder
         */
        public ListAzureInstanceTagsRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListAzureInstanceTagsRequestBuilder
         */
        public ListAzureInstanceTagsRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListAzureInstanceTagsRequestBuilder
         */
        public ListAzureInstanceTagsRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListAzureInstanceTagsRequestBuilder
         */
        public ListAzureInstanceTagsRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListAzureInstanceTagsRequestBuilder
         */
        public ListAzureInstanceTagsRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listAzureInstanceTags
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
            return listAzureInstanceTagsCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute listAzureInstanceTags request
         * @return List&lt;SystemInsightsAzureInstanceTags&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsAzureInstanceTags> execute() throws ApiException {
            ApiResponse<List<SystemInsightsAzureInstanceTags>> localVarResp = listAzureInstanceTagsWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listAzureInstanceTags request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsAzureInstanceTags&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsAzureInstanceTags>> executeWithHttpInfo() throws ApiException {
            return listAzureInstanceTagsWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute listAzureInstanceTags request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsAzureInstanceTags>> _callback) throws ApiException {
            return listAzureInstanceTagsAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights Azure Instance Tags
     * Valid filter fields are &#x60;system_id&#x60;.
     * @return ListAzureInstanceTagsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListAzureInstanceTagsRequestBuilder listAzureInstanceTags() throws IllegalArgumentException {
        return new ListAzureInstanceTagsRequestBuilder();
    }
    private okhttp3.Call listBatteryDataCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/battery";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listBatteryDataValidateBeforeCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
        return listBatteryDataCall(xOrgId, skip, sort, filter, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsBattery>> listBatteryDataWithHttpInfo(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listBatteryDataValidateBeforeCall(xOrgId, skip, sort, filter, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsBattery>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listBatteryDataAsync(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback<List<SystemInsightsBattery>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listBatteryDataValidateBeforeCall(xOrgId, skip, sort, filter, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsBattery>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListBatteryDataRequestBuilder {
        private String xOrgId;
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private Integer limit;

        private ListBatteryDataRequestBuilder() {
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListBatteryDataRequestBuilder
         */
        public ListBatteryDataRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListBatteryDataRequestBuilder
         */
        public ListBatteryDataRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListBatteryDataRequestBuilder
         */
        public ListBatteryDataRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListBatteryDataRequestBuilder
         */
        public ListBatteryDataRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListBatteryDataRequestBuilder
         */
        public ListBatteryDataRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listBatteryData
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
            return listBatteryDataCall(xOrgId, skip, sort, filter, limit, _callback);
        }


        /**
         * Execute listBatteryData request
         * @return List&lt;SystemInsightsBattery&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsBattery> execute() throws ApiException {
            ApiResponse<List<SystemInsightsBattery>> localVarResp = listBatteryDataWithHttpInfo(xOrgId, skip, sort, filter, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listBatteryData request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsBattery&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsBattery>> executeWithHttpInfo() throws ApiException {
            return listBatteryDataWithHttpInfo(xOrgId, skip, sort, filter, limit);
        }

        /**
         * Execute listBatteryData request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsBattery>> _callback) throws ApiException {
            return listBatteryDataAsync(xOrgId, skip, sort, filter, limit, _callback);
        }
    }

    /**
     * List System Insights Battery
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;health&#x60;.
     * @return ListBatteryDataRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListBatteryDataRequestBuilder listBatteryData() throws IllegalArgumentException {
        return new ListBatteryDataRequestBuilder();
    }
    private okhttp3.Call listBitlockerInfoCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/bitlocker_info";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listBitlockerInfoValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return listBitlockerInfoCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsBitlockerInfo>> listBitlockerInfoWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listBitlockerInfoValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsBitlockerInfo>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listBitlockerInfoAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsBitlockerInfo>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listBitlockerInfoValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsBitlockerInfo>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListBitlockerInfoRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private ListBitlockerInfoRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListBitlockerInfoRequestBuilder
         */
        public ListBitlockerInfoRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListBitlockerInfoRequestBuilder
         */
        public ListBitlockerInfoRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListBitlockerInfoRequestBuilder
         */
        public ListBitlockerInfoRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListBitlockerInfoRequestBuilder
         */
        public ListBitlockerInfoRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListBitlockerInfoRequestBuilder
         */
        public ListBitlockerInfoRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listBitlockerInfo
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
            return listBitlockerInfoCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute listBitlockerInfo request
         * @return List&lt;SystemInsightsBitlockerInfo&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsBitlockerInfo> execute() throws ApiException {
            ApiResponse<List<SystemInsightsBitlockerInfo>> localVarResp = listBitlockerInfoWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listBitlockerInfo request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsBitlockerInfo&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsBitlockerInfo>> executeWithHttpInfo() throws ApiException {
            return listBitlockerInfoWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute listBitlockerInfo request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsBitlockerInfo>> _callback) throws ApiException {
            return listBitlockerInfoAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights Bitlocker Info
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;protection_status&#x60;.
     * @return ListBitlockerInfoRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListBitlockerInfoRequestBuilder listBitlockerInfo() throws IllegalArgumentException {
        return new ListBitlockerInfoRequestBuilder();
    }
    private okhttp3.Call listBrowserPluginsCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/browser_plugins";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listBrowserPluginsValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return listBrowserPluginsCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsBrowserPlugins>> listBrowserPluginsWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listBrowserPluginsValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsBrowserPlugins>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listBrowserPluginsAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsBrowserPlugins>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listBrowserPluginsValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsBrowserPlugins>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListBrowserPluginsRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private ListBrowserPluginsRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListBrowserPluginsRequestBuilder
         */
        public ListBrowserPluginsRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListBrowserPluginsRequestBuilder
         */
        public ListBrowserPluginsRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListBrowserPluginsRequestBuilder
         */
        public ListBrowserPluginsRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListBrowserPluginsRequestBuilder
         */
        public ListBrowserPluginsRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListBrowserPluginsRequestBuilder
         */
        public ListBrowserPluginsRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listBrowserPlugins
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
            return listBrowserPluginsCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute listBrowserPlugins request
         * @return List&lt;SystemInsightsBrowserPlugins&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsBrowserPlugins> execute() throws ApiException {
            ApiResponse<List<SystemInsightsBrowserPlugins>> localVarResp = listBrowserPluginsWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listBrowserPlugins request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsBrowserPlugins&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsBrowserPlugins>> executeWithHttpInfo() throws ApiException {
            return listBrowserPluginsWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute listBrowserPlugins request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsBrowserPlugins>> _callback) throws ApiException {
            return listBrowserPluginsAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights Browser Plugins
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.
     * @return ListBrowserPluginsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListBrowserPluginsRequestBuilder listBrowserPlugins() throws IllegalArgumentException {
        return new ListBrowserPluginsRequestBuilder();
    }
    private okhttp3.Call listCertificatesCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/certificates";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listCertificatesValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return listCertificatesCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsCertificates>> listCertificatesWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listCertificatesValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsCertificates>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listCertificatesAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsCertificates>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listCertificatesValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsCertificates>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListCertificatesRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private ListCertificatesRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListCertificatesRequestBuilder
         */
        public ListCertificatesRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListCertificatesRequestBuilder
         */
        public ListCertificatesRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60; Note: You can only filter by &#x60;system_id&#x60; and &#x60;common_name&#x60;  (optional)
         * @return ListCertificatesRequestBuilder
         */
        public ListCertificatesRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListCertificatesRequestBuilder
         */
        public ListCertificatesRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListCertificatesRequestBuilder
         */
        public ListCertificatesRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listCertificates
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
            return listCertificatesCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute listCertificates request
         * @return List&lt;SystemInsightsCertificates&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsCertificates> execute() throws ApiException {
            ApiResponse<List<SystemInsightsCertificates>> localVarResp = listCertificatesWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listCertificates request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsCertificates&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsCertificates>> executeWithHttpInfo() throws ApiException {
            return listCertificatesWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute listCertificates request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsCertificates>> _callback) throws ApiException {
            return listCertificatesAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights Certificates
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;common_name&#x60;.
     * @return ListCertificatesRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListCertificatesRequestBuilder listCertificates() throws IllegalArgumentException {
        return new ListCertificatesRequestBuilder();
    }
    private okhttp3.Call listChromeExtensionsCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/chrome_extensions";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listChromeExtensionsValidateBeforeCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
        return listChromeExtensionsCall(xOrgId, skip, sort, filter, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsChromeExtensions>> listChromeExtensionsWithHttpInfo(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listChromeExtensionsValidateBeforeCall(xOrgId, skip, sort, filter, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsChromeExtensions>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listChromeExtensionsAsync(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback<List<SystemInsightsChromeExtensions>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listChromeExtensionsValidateBeforeCall(xOrgId, skip, sort, filter, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsChromeExtensions>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListChromeExtensionsRequestBuilder {
        private String xOrgId;
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private Integer limit;

        private ListChromeExtensionsRequestBuilder() {
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListChromeExtensionsRequestBuilder
         */
        public ListChromeExtensionsRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListChromeExtensionsRequestBuilder
         */
        public ListChromeExtensionsRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListChromeExtensionsRequestBuilder
         */
        public ListChromeExtensionsRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListChromeExtensionsRequestBuilder
         */
        public ListChromeExtensionsRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListChromeExtensionsRequestBuilder
         */
        public ListChromeExtensionsRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listChromeExtensions
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
            return listChromeExtensionsCall(xOrgId, skip, sort, filter, limit, _callback);
        }


        /**
         * Execute listChromeExtensions request
         * @return List&lt;SystemInsightsChromeExtensions&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsChromeExtensions> execute() throws ApiException {
            ApiResponse<List<SystemInsightsChromeExtensions>> localVarResp = listChromeExtensionsWithHttpInfo(xOrgId, skip, sort, filter, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listChromeExtensions request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsChromeExtensions&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsChromeExtensions>> executeWithHttpInfo() throws ApiException {
            return listChromeExtensionsWithHttpInfo(xOrgId, skip, sort, filter, limit);
        }

        /**
         * Execute listChromeExtensions request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsChromeExtensions>> _callback) throws ApiException {
            return listChromeExtensionsAsync(xOrgId, skip, sort, filter, limit, _callback);
        }
    }

    /**
     * List System Insights Chrome Extensions
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.
     * @return ListChromeExtensionsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListChromeExtensionsRequestBuilder listChromeExtensions() throws IllegalArgumentException {
        return new ListChromeExtensionsRequestBuilder();
    }
    private okhttp3.Call listConnectivityCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/connectivity";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listConnectivityValidateBeforeCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
        return listConnectivityCall(xOrgId, skip, sort, filter, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsConnectivity>> listConnectivityWithHttpInfo(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listConnectivityValidateBeforeCall(xOrgId, skip, sort, filter, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsConnectivity>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listConnectivityAsync(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback<List<SystemInsightsConnectivity>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listConnectivityValidateBeforeCall(xOrgId, skip, sort, filter, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsConnectivity>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListConnectivityRequestBuilder {
        private String xOrgId;
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private Integer limit;

        private ListConnectivityRequestBuilder() {
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListConnectivityRequestBuilder
         */
        public ListConnectivityRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListConnectivityRequestBuilder
         */
        public ListConnectivityRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListConnectivityRequestBuilder
         */
        public ListConnectivityRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListConnectivityRequestBuilder
         */
        public ListConnectivityRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListConnectivityRequestBuilder
         */
        public ListConnectivityRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listConnectivity
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
            return listConnectivityCall(xOrgId, skip, sort, filter, limit, _callback);
        }


        /**
         * Execute listConnectivity request
         * @return List&lt;SystemInsightsConnectivity&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsConnectivity> execute() throws ApiException {
            ApiResponse<List<SystemInsightsConnectivity>> localVarResp = listConnectivityWithHttpInfo(xOrgId, skip, sort, filter, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listConnectivity request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsConnectivity&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsConnectivity>> executeWithHttpInfo() throws ApiException {
            return listConnectivityWithHttpInfo(xOrgId, skip, sort, filter, limit);
        }

        /**
         * Execute listConnectivity request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsConnectivity>> _callback) throws ApiException {
            return listConnectivityAsync(xOrgId, skip, sort, filter, limit, _callback);
        }
    }

    /**
     * List System Insights Connectivity
     * The only valid filter field is &#x60;system_id&#x60;.
     * @return ListConnectivityRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListConnectivityRequestBuilder listConnectivity() throws IllegalArgumentException {
        return new ListConnectivityRequestBuilder();
    }
    private okhttp3.Call listCrashesCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/crashes";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listCrashesValidateBeforeCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
        return listCrashesCall(xOrgId, skip, sort, filter, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsCrashes>> listCrashesWithHttpInfo(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listCrashesValidateBeforeCall(xOrgId, skip, sort, filter, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsCrashes>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listCrashesAsync(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback<List<SystemInsightsCrashes>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listCrashesValidateBeforeCall(xOrgId, skip, sort, filter, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsCrashes>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListCrashesRequestBuilder {
        private String xOrgId;
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private Integer limit;

        private ListCrashesRequestBuilder() {
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListCrashesRequestBuilder
         */
        public ListCrashesRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListCrashesRequestBuilder
         */
        public ListCrashesRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListCrashesRequestBuilder
         */
        public ListCrashesRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListCrashesRequestBuilder
         */
        public ListCrashesRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListCrashesRequestBuilder
         */
        public ListCrashesRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listCrashes
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
            return listCrashesCall(xOrgId, skip, sort, filter, limit, _callback);
        }


        /**
         * Execute listCrashes request
         * @return List&lt;SystemInsightsCrashes&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsCrashes> execute() throws ApiException {
            ApiResponse<List<SystemInsightsCrashes>> localVarResp = listCrashesWithHttpInfo(xOrgId, skip, sort, filter, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listCrashes request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsCrashes&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsCrashes>> executeWithHttpInfo() throws ApiException {
            return listCrashesWithHttpInfo(xOrgId, skip, sort, filter, limit);
        }

        /**
         * Execute listCrashes request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsCrashes>> _callback) throws ApiException {
            return listCrashesAsync(xOrgId, skip, sort, filter, limit, _callback);
        }
    }

    /**
     * List System Insights Crashes
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;identifier&#x60;.
     * @return ListCrashesRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListCrashesRequestBuilder listCrashes() throws IllegalArgumentException {
        return new ListCrashesRequestBuilder();
    }
    private okhttp3.Call listCupsDestinationsCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/cups_destinations";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listCupsDestinationsValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return listCupsDestinationsCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsCupsDestinations>> listCupsDestinationsWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listCupsDestinationsValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsCupsDestinations>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listCupsDestinationsAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsCupsDestinations>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listCupsDestinationsValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsCupsDestinations>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListCupsDestinationsRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private ListCupsDestinationsRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListCupsDestinationsRequestBuilder
         */
        public ListCupsDestinationsRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListCupsDestinationsRequestBuilder
         */
        public ListCupsDestinationsRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListCupsDestinationsRequestBuilder
         */
        public ListCupsDestinationsRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListCupsDestinationsRequestBuilder
         */
        public ListCupsDestinationsRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListCupsDestinationsRequestBuilder
         */
        public ListCupsDestinationsRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listCupsDestinations
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
            return listCupsDestinationsCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute listCupsDestinations request
         * @return List&lt;SystemInsightsCupsDestinations&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsCupsDestinations> execute() throws ApiException {
            ApiResponse<List<SystemInsightsCupsDestinations>> localVarResp = listCupsDestinationsWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listCupsDestinations request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsCupsDestinations&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsCupsDestinations>> executeWithHttpInfo() throws ApiException {
            return listCupsDestinationsWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute listCupsDestinations request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsCupsDestinations>> _callback) throws ApiException {
            return listCupsDestinationsAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights CUPS Destinations
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.
     * @return ListCupsDestinationsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListCupsDestinationsRequestBuilder listCupsDestinations() throws IllegalArgumentException {
        return new ListCupsDestinationsRequestBuilder();
    }
    private okhttp3.Call listDiskEncryptionCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/disk_encryption";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listDiskEncryptionValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return listDiskEncryptionCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsDiskEncryption>> listDiskEncryptionWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listDiskEncryptionValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsDiskEncryption>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listDiskEncryptionAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsDiskEncryption>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listDiskEncryptionValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsDiskEncryption>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListDiskEncryptionRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private ListDiskEncryptionRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListDiskEncryptionRequestBuilder
         */
        public ListDiskEncryptionRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListDiskEncryptionRequestBuilder
         */
        public ListDiskEncryptionRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListDiskEncryptionRequestBuilder
         */
        public ListDiskEncryptionRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListDiskEncryptionRequestBuilder
         */
        public ListDiskEncryptionRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListDiskEncryptionRequestBuilder
         */
        public ListDiskEncryptionRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listDiskEncryption
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
            return listDiskEncryptionCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute listDiskEncryption request
         * @return List&lt;SystemInsightsDiskEncryption&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsDiskEncryption> execute() throws ApiException {
            ApiResponse<List<SystemInsightsDiskEncryption>> localVarResp = listDiskEncryptionWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listDiskEncryption request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsDiskEncryption&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsDiskEncryption>> executeWithHttpInfo() throws ApiException {
            return listDiskEncryptionWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute listDiskEncryption request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsDiskEncryption>> _callback) throws ApiException {
            return listDiskEncryptionAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights Disk Encryption
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;encryption_status&#x60;.
     * @return ListDiskEncryptionRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListDiskEncryptionRequestBuilder listDiskEncryption() throws IllegalArgumentException {
        return new ListDiskEncryptionRequestBuilder();
    }
    private okhttp3.Call listDnsResolversCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/dns_resolvers";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listDnsResolversValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return listDnsResolversCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsDnsResolvers>> listDnsResolversWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listDnsResolversValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsDnsResolvers>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listDnsResolversAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsDnsResolvers>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listDnsResolversValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsDnsResolvers>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListDnsResolversRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private ListDnsResolversRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListDnsResolversRequestBuilder
         */
        public ListDnsResolversRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListDnsResolversRequestBuilder
         */
        public ListDnsResolversRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListDnsResolversRequestBuilder
         */
        public ListDnsResolversRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListDnsResolversRequestBuilder
         */
        public ListDnsResolversRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListDnsResolversRequestBuilder
         */
        public ListDnsResolversRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listDnsResolvers
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
            return listDnsResolversCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute listDnsResolvers request
         * @return List&lt;SystemInsightsDnsResolvers&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsDnsResolvers> execute() throws ApiException {
            ApiResponse<List<SystemInsightsDnsResolvers>> localVarResp = listDnsResolversWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listDnsResolvers request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsDnsResolvers&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsDnsResolvers>> executeWithHttpInfo() throws ApiException {
            return listDnsResolversWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute listDnsResolvers request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsDnsResolvers>> _callback) throws ApiException {
            return listDnsResolversAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights DNS Resolvers
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;type&#x60;.
     * @return ListDnsResolversRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListDnsResolversRequestBuilder listDnsResolvers() throws IllegalArgumentException {
        return new ListDnsResolversRequestBuilder();
    }
    private okhttp3.Call listEtcHostsCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/etc_hosts";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listEtcHostsValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return listEtcHostsCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsEtcHosts>> listEtcHostsWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listEtcHostsValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsEtcHosts>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listEtcHostsAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsEtcHosts>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listEtcHostsValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsEtcHosts>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListEtcHostsRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private ListEtcHostsRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListEtcHostsRequestBuilder
         */
        public ListEtcHostsRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListEtcHostsRequestBuilder
         */
        public ListEtcHostsRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListEtcHostsRequestBuilder
         */
        public ListEtcHostsRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListEtcHostsRequestBuilder
         */
        public ListEtcHostsRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListEtcHostsRequestBuilder
         */
        public ListEtcHostsRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listEtcHosts
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
            return listEtcHostsCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute listEtcHosts request
         * @return List&lt;SystemInsightsEtcHosts&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsEtcHosts> execute() throws ApiException {
            ApiResponse<List<SystemInsightsEtcHosts>> localVarResp = listEtcHostsWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listEtcHosts request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsEtcHosts&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsEtcHosts>> executeWithHttpInfo() throws ApiException {
            return listEtcHostsWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute listEtcHosts request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsEtcHosts>> _callback) throws ApiException {
            return listEtcHostsAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights Etc Hosts
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;address&#x60;.
     * @return ListEtcHostsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListEtcHostsRequestBuilder listEtcHosts() throws IllegalArgumentException {
        return new ListEtcHostsRequestBuilder();
    }
    private okhttp3.Call listFirefoxAddonsCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/firefox_addons";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listFirefoxAddonsValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return listFirefoxAddonsCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsFirefoxAddons>> listFirefoxAddonsWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listFirefoxAddonsValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsFirefoxAddons>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listFirefoxAddonsAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsFirefoxAddons>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listFirefoxAddonsValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsFirefoxAddons>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListFirefoxAddonsRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private ListFirefoxAddonsRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListFirefoxAddonsRequestBuilder
         */
        public ListFirefoxAddonsRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListFirefoxAddonsRequestBuilder
         */
        public ListFirefoxAddonsRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListFirefoxAddonsRequestBuilder
         */
        public ListFirefoxAddonsRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListFirefoxAddonsRequestBuilder
         */
        public ListFirefoxAddonsRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListFirefoxAddonsRequestBuilder
         */
        public ListFirefoxAddonsRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listFirefoxAddons
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
            return listFirefoxAddonsCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute listFirefoxAddons request
         * @return List&lt;SystemInsightsFirefoxAddons&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsFirefoxAddons> execute() throws ApiException {
            ApiResponse<List<SystemInsightsFirefoxAddons>> localVarResp = listFirefoxAddonsWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listFirefoxAddons request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsFirefoxAddons&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsFirefoxAddons>> executeWithHttpInfo() throws ApiException {
            return listFirefoxAddonsWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute listFirefoxAddons request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsFirefoxAddons>> _callback) throws ApiException {
            return listFirefoxAddonsAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights Firefox Addons
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.
     * @return ListFirefoxAddonsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListFirefoxAddonsRequestBuilder listFirefoxAddons() throws IllegalArgumentException {
        return new ListFirefoxAddonsRequestBuilder();
    }
    private okhttp3.Call listGroupsCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/groups";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listGroupsValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return listGroupsCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsGroups>> listGroupsWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listGroupsValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsGroups>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listGroupsAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsGroups>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listGroupsValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsGroups>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListGroupsRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private ListGroupsRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListGroupsRequestBuilder
         */
        public ListGroupsRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListGroupsRequestBuilder
         */
        public ListGroupsRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListGroupsRequestBuilder
         */
        public ListGroupsRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListGroupsRequestBuilder
         */
        public ListGroupsRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListGroupsRequestBuilder
         */
        public ListGroupsRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listGroups
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
            return listGroupsCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute listGroups request
         * @return List&lt;SystemInsightsGroups&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsGroups> execute() throws ApiException {
            ApiResponse<List<SystemInsightsGroups>> localVarResp = listGroupsWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listGroups request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsGroups&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsGroups>> executeWithHttpInfo() throws ApiException {
            return listGroupsWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute listGroups request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsGroups>> _callback) throws ApiException {
            return listGroupsAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights Groups
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;groupname&#x60;.
     * @return ListGroupsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListGroupsRequestBuilder listGroups() throws IllegalArgumentException {
        return new ListGroupsRequestBuilder();
    }
    private okhttp3.Call listInterfaceAddressesCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/interface_addresses";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listInterfaceAddressesValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return listInterfaceAddressesCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsInterfaceAddresses>> listInterfaceAddressesWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listInterfaceAddressesValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsInterfaceAddresses>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listInterfaceAddressesAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsInterfaceAddresses>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listInterfaceAddressesValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsInterfaceAddresses>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListInterfaceAddressesRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private ListInterfaceAddressesRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListInterfaceAddressesRequestBuilder
         */
        public ListInterfaceAddressesRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListInterfaceAddressesRequestBuilder
         */
        public ListInterfaceAddressesRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListInterfaceAddressesRequestBuilder
         */
        public ListInterfaceAddressesRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListInterfaceAddressesRequestBuilder
         */
        public ListInterfaceAddressesRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListInterfaceAddressesRequestBuilder
         */
        public ListInterfaceAddressesRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listInterfaceAddresses
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
            return listInterfaceAddressesCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute listInterfaceAddresses request
         * @return List&lt;SystemInsightsInterfaceAddresses&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsInterfaceAddresses> execute() throws ApiException {
            ApiResponse<List<SystemInsightsInterfaceAddresses>> localVarResp = listInterfaceAddressesWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listInterfaceAddresses request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsInterfaceAddresses&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsInterfaceAddresses>> executeWithHttpInfo() throws ApiException {
            return listInterfaceAddressesWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute listInterfaceAddresses request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsInterfaceAddresses>> _callback) throws ApiException {
            return listInterfaceAddressesAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights Interface Addresses
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;address&#x60;.
     * @return ListInterfaceAddressesRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListInterfaceAddressesRequestBuilder listInterfaceAddresses() throws IllegalArgumentException {
        return new ListInterfaceAddressesRequestBuilder();
    }
    private okhttp3.Call listInterfaceDetailsCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/interface_details";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listInterfaceDetailsValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return listInterfaceDetailsCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsInterfaceDetails>> listInterfaceDetailsWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listInterfaceDetailsValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsInterfaceDetails>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listInterfaceDetailsAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsInterfaceDetails>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listInterfaceDetailsValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsInterfaceDetails>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListInterfaceDetailsRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private ListInterfaceDetailsRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListInterfaceDetailsRequestBuilder
         */
        public ListInterfaceDetailsRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListInterfaceDetailsRequestBuilder
         */
        public ListInterfaceDetailsRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListInterfaceDetailsRequestBuilder
         */
        public ListInterfaceDetailsRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListInterfaceDetailsRequestBuilder
         */
        public ListInterfaceDetailsRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListInterfaceDetailsRequestBuilder
         */
        public ListInterfaceDetailsRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listInterfaceDetails
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
            return listInterfaceDetailsCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute listInterfaceDetails request
         * @return List&lt;SystemInsightsInterfaceDetails&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsInterfaceDetails> execute() throws ApiException {
            ApiResponse<List<SystemInsightsInterfaceDetails>> localVarResp = listInterfaceDetailsWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listInterfaceDetails request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsInterfaceDetails&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsInterfaceDetails>> executeWithHttpInfo() throws ApiException {
            return listInterfaceDetailsWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute listInterfaceDetails request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsInterfaceDetails>> _callback) throws ApiException {
            return listInterfaceDetailsAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights Interface Details
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;interface&#x60;.
     * @return ListInterfaceDetailsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListInterfaceDetailsRequestBuilder listInterfaceDetails() throws IllegalArgumentException {
        return new ListInterfaceDetailsRequestBuilder();
    }
    private okhttp3.Call listLaunchdCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/launchd";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listLaunchdValidateBeforeCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
        return listLaunchdCall(xOrgId, skip, sort, filter, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsLaunchd>> listLaunchdWithHttpInfo(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listLaunchdValidateBeforeCall(xOrgId, skip, sort, filter, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsLaunchd>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listLaunchdAsync(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback<List<SystemInsightsLaunchd>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listLaunchdValidateBeforeCall(xOrgId, skip, sort, filter, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsLaunchd>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListLaunchdRequestBuilder {
        private String xOrgId;
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private Integer limit;

        private ListLaunchdRequestBuilder() {
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListLaunchdRequestBuilder
         */
        public ListLaunchdRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListLaunchdRequestBuilder
         */
        public ListLaunchdRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListLaunchdRequestBuilder
         */
        public ListLaunchdRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListLaunchdRequestBuilder
         */
        public ListLaunchdRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListLaunchdRequestBuilder
         */
        public ListLaunchdRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listLaunchd
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
            return listLaunchdCall(xOrgId, skip, sort, filter, limit, _callback);
        }


        /**
         * Execute listLaunchd request
         * @return List&lt;SystemInsightsLaunchd&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsLaunchd> execute() throws ApiException {
            ApiResponse<List<SystemInsightsLaunchd>> localVarResp = listLaunchdWithHttpInfo(xOrgId, skip, sort, filter, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listLaunchd request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsLaunchd&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsLaunchd>> executeWithHttpInfo() throws ApiException {
            return listLaunchdWithHttpInfo(xOrgId, skip, sort, filter, limit);
        }

        /**
         * Execute listLaunchd request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsLaunchd>> _callback) throws ApiException {
            return listLaunchdAsync(xOrgId, skip, sort, filter, limit, _callback);
        }
    }

    /**
     * List System Insights Launchd
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.
     * @return ListLaunchdRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListLaunchdRequestBuilder listLaunchd() throws IllegalArgumentException {
        return new ListLaunchdRequestBuilder();
    }
    private okhttp3.Call listLinuxPackagesCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/linux_packages";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listLinuxPackagesValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return listLinuxPackagesCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsLinuxPackages>> listLinuxPackagesWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listLinuxPackagesValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsLinuxPackages>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listLinuxPackagesAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsLinuxPackages>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listLinuxPackagesValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsLinuxPackages>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListLinuxPackagesRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private ListLinuxPackagesRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListLinuxPackagesRequestBuilder
         */
        public ListLinuxPackagesRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListLinuxPackagesRequestBuilder
         */
        public ListLinuxPackagesRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListLinuxPackagesRequestBuilder
         */
        public ListLinuxPackagesRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListLinuxPackagesRequestBuilder
         */
        public ListLinuxPackagesRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListLinuxPackagesRequestBuilder
         */
        public ListLinuxPackagesRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listLinuxPackages
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
            return listLinuxPackagesCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute listLinuxPackages request
         * @return List&lt;SystemInsightsLinuxPackages&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsLinuxPackages> execute() throws ApiException {
            ApiResponse<List<SystemInsightsLinuxPackages>> localVarResp = listLinuxPackagesWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listLinuxPackages request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsLinuxPackages&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsLinuxPackages>> executeWithHttpInfo() throws ApiException {
            return listLinuxPackagesWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute listLinuxPackages request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsLinuxPackages>> _callback) throws ApiException {
            return listLinuxPackagesAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights Linux Packages
     * Lists all programs for Linux devices. For macOS devices, use [List System Insights System Apps](https://docs.jumpcloud.com). For windows devices, use [List System Insights System Apps](https://docs.jumpcloud.com).  Valid filter fields are &#x60;name&#x60; and &#x60;package_format&#x60;.
     * @return ListLinuxPackagesRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListLinuxPackagesRequestBuilder listLinuxPackages() throws IllegalArgumentException {
        return new ListLinuxPackagesRequestBuilder();
    }
    private okhttp3.Call listLoggedInUsersCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/logged_in_users";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listLoggedInUsersValidateBeforeCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
        return listLoggedInUsersCall(xOrgId, skip, sort, filter, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsLoggedInUsers>> listLoggedInUsersWithHttpInfo(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listLoggedInUsersValidateBeforeCall(xOrgId, skip, sort, filter, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsLoggedInUsers>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listLoggedInUsersAsync(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback<List<SystemInsightsLoggedInUsers>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listLoggedInUsersValidateBeforeCall(xOrgId, skip, sort, filter, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsLoggedInUsers>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListLoggedInUsersRequestBuilder {
        private String xOrgId;
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private Integer limit;

        private ListLoggedInUsersRequestBuilder() {
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListLoggedInUsersRequestBuilder
         */
        public ListLoggedInUsersRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListLoggedInUsersRequestBuilder
         */
        public ListLoggedInUsersRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListLoggedInUsersRequestBuilder
         */
        public ListLoggedInUsersRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListLoggedInUsersRequestBuilder
         */
        public ListLoggedInUsersRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListLoggedInUsersRequestBuilder
         */
        public ListLoggedInUsersRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listLoggedInUsers
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
            return listLoggedInUsersCall(xOrgId, skip, sort, filter, limit, _callback);
        }


        /**
         * Execute listLoggedInUsers request
         * @return List&lt;SystemInsightsLoggedInUsers&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsLoggedInUsers> execute() throws ApiException {
            ApiResponse<List<SystemInsightsLoggedInUsers>> localVarResp = listLoggedInUsersWithHttpInfo(xOrgId, skip, sort, filter, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listLoggedInUsers request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsLoggedInUsers&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsLoggedInUsers>> executeWithHttpInfo() throws ApiException {
            return listLoggedInUsersWithHttpInfo(xOrgId, skip, sort, filter, limit);
        }

        /**
         * Execute listLoggedInUsers request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsLoggedInUsers>> _callback) throws ApiException {
            return listLoggedInUsersAsync(xOrgId, skip, sort, filter, limit, _callback);
        }
    }

    /**
     * List System Insights Logged-In Users
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;user&#x60;.
     * @return ListLoggedInUsersRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListLoggedInUsersRequestBuilder listLoggedInUsers() throws IllegalArgumentException {
        return new ListLoggedInUsersRequestBuilder();
    }
    private okhttp3.Call listLogicalDrivesCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/logical_drives";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listLogicalDrivesValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return listLogicalDrivesCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsLogicalDrives>> listLogicalDrivesWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listLogicalDrivesValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsLogicalDrives>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listLogicalDrivesAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsLogicalDrives>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listLogicalDrivesValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsLogicalDrives>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListLogicalDrivesRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private ListLogicalDrivesRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListLogicalDrivesRequestBuilder
         */
        public ListLogicalDrivesRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListLogicalDrivesRequestBuilder
         */
        public ListLogicalDrivesRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListLogicalDrivesRequestBuilder
         */
        public ListLogicalDrivesRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListLogicalDrivesRequestBuilder
         */
        public ListLogicalDrivesRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListLogicalDrivesRequestBuilder
         */
        public ListLogicalDrivesRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listLogicalDrives
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
            return listLogicalDrivesCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute listLogicalDrives request
         * @return List&lt;SystemInsightsLogicalDrives&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsLogicalDrives> execute() throws ApiException {
            ApiResponse<List<SystemInsightsLogicalDrives>> localVarResp = listLogicalDrivesWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listLogicalDrives request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsLogicalDrives&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsLogicalDrives>> executeWithHttpInfo() throws ApiException {
            return listLogicalDrivesWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute listLogicalDrives request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsLogicalDrives>> _callback) throws ApiException {
            return listLogicalDrivesAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights Logical Drives
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;device_id&#x60;.
     * @return ListLogicalDrivesRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListLogicalDrivesRequestBuilder listLogicalDrives() throws IllegalArgumentException {
        return new ListLogicalDrivesRequestBuilder();
    }
    private okhttp3.Call listManagedPoliciesCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/managed_policies";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listManagedPoliciesValidateBeforeCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
        return listManagedPoliciesCall(xOrgId, skip, sort, filter, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsManagedPolicies>> listManagedPoliciesWithHttpInfo(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listManagedPoliciesValidateBeforeCall(xOrgId, skip, sort, filter, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsManagedPolicies>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listManagedPoliciesAsync(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback<List<SystemInsightsManagedPolicies>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listManagedPoliciesValidateBeforeCall(xOrgId, skip, sort, filter, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsManagedPolicies>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListManagedPoliciesRequestBuilder {
        private String xOrgId;
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private Integer limit;

        private ListManagedPoliciesRequestBuilder() {
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListManagedPoliciesRequestBuilder
         */
        public ListManagedPoliciesRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListManagedPoliciesRequestBuilder
         */
        public ListManagedPoliciesRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListManagedPoliciesRequestBuilder
         */
        public ListManagedPoliciesRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListManagedPoliciesRequestBuilder
         */
        public ListManagedPoliciesRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListManagedPoliciesRequestBuilder
         */
        public ListManagedPoliciesRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listManagedPolicies
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
            return listManagedPoliciesCall(xOrgId, skip, sort, filter, limit, _callback);
        }


        /**
         * Execute listManagedPolicies request
         * @return List&lt;SystemInsightsManagedPolicies&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsManagedPolicies> execute() throws ApiException {
            ApiResponse<List<SystemInsightsManagedPolicies>> localVarResp = listManagedPoliciesWithHttpInfo(xOrgId, skip, sort, filter, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listManagedPolicies request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsManagedPolicies&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsManagedPolicies>> executeWithHttpInfo() throws ApiException {
            return listManagedPoliciesWithHttpInfo(xOrgId, skip, sort, filter, limit);
        }

        /**
         * Execute listManagedPolicies request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsManagedPolicies>> _callback) throws ApiException {
            return listManagedPoliciesAsync(xOrgId, skip, sort, filter, limit, _callback);
        }
    }

    /**
     * List System Insights Managed Policies
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;domain&#x60;.
     * @return ListManagedPoliciesRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListManagedPoliciesRequestBuilder listManagedPolicies() throws IllegalArgumentException {
        return new ListManagedPoliciesRequestBuilder();
    }
    private okhttp3.Call listMountsCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/mounts";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listMountsValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return listMountsCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsMounts>> listMountsWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listMountsValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsMounts>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listMountsAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsMounts>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listMountsValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsMounts>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListMountsRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private ListMountsRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListMountsRequestBuilder
         */
        public ListMountsRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListMountsRequestBuilder
         */
        public ListMountsRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListMountsRequestBuilder
         */
        public ListMountsRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListMountsRequestBuilder
         */
        public ListMountsRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListMountsRequestBuilder
         */
        public ListMountsRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listMounts
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
            return listMountsCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute listMounts request
         * @return List&lt;SystemInsightsMounts&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsMounts> execute() throws ApiException {
            ApiResponse<List<SystemInsightsMounts>> localVarResp = listMountsWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listMounts request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsMounts&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsMounts>> executeWithHttpInfo() throws ApiException {
            return listMountsWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute listMounts request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsMounts>> _callback) throws ApiException {
            return listMountsAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights Mounts
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;path&#x60;.
     * @return ListMountsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListMountsRequestBuilder listMounts() throws IllegalArgumentException {
        return new ListMountsRequestBuilder();
    }
    private okhttp3.Call listPatchesCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/patches";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listPatchesValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return listPatchesCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsPatches>> listPatchesWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listPatchesValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsPatches>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listPatchesAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsPatches>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listPatchesValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsPatches>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListPatchesRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private ListPatchesRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListPatchesRequestBuilder
         */
        public ListPatchesRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListPatchesRequestBuilder
         */
        public ListPatchesRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListPatchesRequestBuilder
         */
        public ListPatchesRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListPatchesRequestBuilder
         */
        public ListPatchesRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListPatchesRequestBuilder
         */
        public ListPatchesRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listPatches
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
            return listPatchesCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute listPatches request
         * @return List&lt;SystemInsightsPatches&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsPatches> execute() throws ApiException {
            ApiResponse<List<SystemInsightsPatches>> localVarResp = listPatchesWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listPatches request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsPatches&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsPatches>> executeWithHttpInfo() throws ApiException {
            return listPatchesWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute listPatches request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsPatches>> _callback) throws ApiException {
            return listPatchesAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights Patches
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;hotfix_id&#x60;.
     * @return ListPatchesRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListPatchesRequestBuilder listPatches() throws IllegalArgumentException {
        return new ListPatchesRequestBuilder();
    }
    private okhttp3.Call listProgramsCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/programs";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listProgramsValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return listProgramsCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsPrograms>> listProgramsWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listProgramsValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsPrograms>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listProgramsAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsPrograms>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listProgramsValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsPrograms>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListProgramsRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private ListProgramsRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListProgramsRequestBuilder
         */
        public ListProgramsRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListProgramsRequestBuilder
         */
        public ListProgramsRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListProgramsRequestBuilder
         */
        public ListProgramsRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListProgramsRequestBuilder
         */
        public ListProgramsRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListProgramsRequestBuilder
         */
        public ListProgramsRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listPrograms
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
            return listProgramsCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute listPrograms request
         * @return List&lt;SystemInsightsPrograms&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsPrograms> execute() throws ApiException {
            ApiResponse<List<SystemInsightsPrograms>> localVarResp = listProgramsWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listPrograms request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsPrograms&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsPrograms>> executeWithHttpInfo() throws ApiException {
            return listProgramsWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute listPrograms request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsPrograms>> _callback) throws ApiException {
            return listProgramsAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights Programs
     * Lists all programs for Windows devices. For macOS devices, use [List System Insights Apps](https://docs.jumpcloud.com).  Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.
     * @return ListProgramsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListProgramsRequestBuilder listPrograms() throws IllegalArgumentException {
        return new ListProgramsRequestBuilder();
    }
    private okhttp3.Call listPythonPackagesCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/python_packages";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listPythonPackagesValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return listPythonPackagesCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsPythonPackages>> listPythonPackagesWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listPythonPackagesValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsPythonPackages>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listPythonPackagesAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsPythonPackages>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listPythonPackagesValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsPythonPackages>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListPythonPackagesRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private ListPythonPackagesRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListPythonPackagesRequestBuilder
         */
        public ListPythonPackagesRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListPythonPackagesRequestBuilder
         */
        public ListPythonPackagesRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListPythonPackagesRequestBuilder
         */
        public ListPythonPackagesRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListPythonPackagesRequestBuilder
         */
        public ListPythonPackagesRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListPythonPackagesRequestBuilder
         */
        public ListPythonPackagesRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listPythonPackages
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
            return listPythonPackagesCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute listPythonPackages request
         * @return List&lt;SystemInsightsPythonPackages&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsPythonPackages> execute() throws ApiException {
            ApiResponse<List<SystemInsightsPythonPackages>> localVarResp = listPythonPackagesWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listPythonPackages request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsPythonPackages&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsPythonPackages>> executeWithHttpInfo() throws ApiException {
            return listPythonPackagesWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute listPythonPackages request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsPythonPackages>> _callback) throws ApiException {
            return listPythonPackagesAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights Python Packages
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.
     * @return ListPythonPackagesRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListPythonPackagesRequestBuilder listPythonPackages() throws IllegalArgumentException {
        return new ListPythonPackagesRequestBuilder();
    }
    private okhttp3.Call listSafariExtensionsCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/safari_extensions";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listSafariExtensionsValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return listSafariExtensionsCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsSafariExtensions>> listSafariExtensionsWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listSafariExtensionsValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsSafariExtensions>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listSafariExtensionsAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsSafariExtensions>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listSafariExtensionsValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsSafariExtensions>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListSafariExtensionsRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private ListSafariExtensionsRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListSafariExtensionsRequestBuilder
         */
        public ListSafariExtensionsRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListSafariExtensionsRequestBuilder
         */
        public ListSafariExtensionsRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListSafariExtensionsRequestBuilder
         */
        public ListSafariExtensionsRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListSafariExtensionsRequestBuilder
         */
        public ListSafariExtensionsRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListSafariExtensionsRequestBuilder
         */
        public ListSafariExtensionsRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listSafariExtensions
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
            return listSafariExtensionsCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute listSafariExtensions request
         * @return List&lt;SystemInsightsSafariExtensions&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsSafariExtensions> execute() throws ApiException {
            ApiResponse<List<SystemInsightsSafariExtensions>> localVarResp = listSafariExtensionsWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listSafariExtensions request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsSafariExtensions&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsSafariExtensions>> executeWithHttpInfo() throws ApiException {
            return listSafariExtensionsWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute listSafariExtensions request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsSafariExtensions>> _callback) throws ApiException {
            return listSafariExtensionsAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights Safari Extensions
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.
     * @return ListSafariExtensionsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListSafariExtensionsRequestBuilder listSafariExtensions() throws IllegalArgumentException {
        return new ListSafariExtensionsRequestBuilder();
    }
    private okhttp3.Call listScheduledTasksCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/scheduled_tasks";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listScheduledTasksValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return listScheduledTasksCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsScheduledTasks>> listScheduledTasksWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listScheduledTasksValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsScheduledTasks>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listScheduledTasksAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsScheduledTasks>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listScheduledTasksValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsScheduledTasks>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListScheduledTasksRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private ListScheduledTasksRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListScheduledTasksRequestBuilder
         */
        public ListScheduledTasksRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListScheduledTasksRequestBuilder
         */
        public ListScheduledTasksRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListScheduledTasksRequestBuilder
         */
        public ListScheduledTasksRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListScheduledTasksRequestBuilder
         */
        public ListScheduledTasksRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListScheduledTasksRequestBuilder
         */
        public ListScheduledTasksRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listScheduledTasks
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
            return listScheduledTasksCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute listScheduledTasks request
         * @return List&lt;SystemInsightsScheduledTasks&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsScheduledTasks> execute() throws ApiException {
            ApiResponse<List<SystemInsightsScheduledTasks>> localVarResp = listScheduledTasksWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listScheduledTasks request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsScheduledTasks&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsScheduledTasks>> executeWithHttpInfo() throws ApiException {
            return listScheduledTasksWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute listScheduledTasks request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsScheduledTasks>> _callback) throws ApiException {
            return listScheduledTasksAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights Scheduled Tasks
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;enabled&#x60;.
     * @return ListScheduledTasksRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListScheduledTasksRequestBuilder listScheduledTasks() throws IllegalArgumentException {
        return new ListScheduledTasksRequestBuilder();
    }
    private okhttp3.Call listSecureBootCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/secureboot";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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

        String[] localVarAuthNames = new String[] {  };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call listSecureBootValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return listSecureBootCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsSecureboot>> listSecureBootWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listSecureBootValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsSecureboot>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listSecureBootAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsSecureboot>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listSecureBootValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsSecureboot>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListSecureBootRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private ListSecureBootRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListSecureBootRequestBuilder
         */
        public ListSecureBootRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListSecureBootRequestBuilder
         */
        public ListSecureBootRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListSecureBootRequestBuilder
         */
        public ListSecureBootRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListSecureBootRequestBuilder
         */
        public ListSecureBootRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListSecureBootRequestBuilder
         */
        public ListSecureBootRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listSecureBoot
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
            return listSecureBootCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute listSecureBoot request
         * @return List&lt;SystemInsightsSecureboot&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsSecureboot> execute() throws ApiException {
            ApiResponse<List<SystemInsightsSecureboot>> localVarResp = listSecureBootWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listSecureBoot request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsSecureboot&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsSecureboot>> executeWithHttpInfo() throws ApiException {
            return listSecureBootWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute listSecureBoot request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsSecureboot>> _callback) throws ApiException {
            return listSecureBootAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights Secure Boot
     * Valid filter fields are &#x60;system_id&#x60;.
     * @return ListSecureBootRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListSecureBootRequestBuilder listSecureBoot() throws IllegalArgumentException {
        return new ListSecureBootRequestBuilder();
    }
    private okhttp3.Call listServicesCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/services";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listServicesValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return listServicesCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsServices>> listServicesWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listServicesValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsServices>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listServicesAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsServices>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listServicesValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsServices>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListServicesRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private ListServicesRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListServicesRequestBuilder
         */
        public ListServicesRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListServicesRequestBuilder
         */
        public ListServicesRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListServicesRequestBuilder
         */
        public ListServicesRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListServicesRequestBuilder
         */
        public ListServicesRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListServicesRequestBuilder
         */
        public ListServicesRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listServices
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
            return listServicesCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute listServices request
         * @return List&lt;SystemInsightsServices&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsServices> execute() throws ApiException {
            ApiResponse<List<SystemInsightsServices>> localVarResp = listServicesWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listServices request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsServices&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsServices>> executeWithHttpInfo() throws ApiException {
            return listServicesWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute listServices request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsServices>> _callback) throws ApiException {
            return listServicesAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights Services
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.
     * @return ListServicesRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListServicesRequestBuilder listServices() throws IllegalArgumentException {
        return new ListServicesRequestBuilder();
    }
    private okhttp3.Call listShadowDataCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/shadow";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listShadowDataValidateBeforeCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
        return listShadowDataCall(xOrgId, skip, sort, filter, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsShadow>> listShadowDataWithHttpInfo(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listShadowDataValidateBeforeCall(xOrgId, skip, sort, filter, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsShadow>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listShadowDataAsync(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback<List<SystemInsightsShadow>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listShadowDataValidateBeforeCall(xOrgId, skip, sort, filter, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsShadow>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListShadowDataRequestBuilder {
        private String xOrgId;
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private Integer limit;

        private ListShadowDataRequestBuilder() {
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListShadowDataRequestBuilder
         */
        public ListShadowDataRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListShadowDataRequestBuilder
         */
        public ListShadowDataRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListShadowDataRequestBuilder
         */
        public ListShadowDataRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListShadowDataRequestBuilder
         */
        public ListShadowDataRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListShadowDataRequestBuilder
         */
        public ListShadowDataRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listShadowData
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
            return listShadowDataCall(xOrgId, skip, sort, filter, limit, _callback);
        }


        /**
         * Execute listShadowData request
         * @return List&lt;SystemInsightsShadow&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsShadow> execute() throws ApiException {
            ApiResponse<List<SystemInsightsShadow>> localVarResp = listShadowDataWithHttpInfo(xOrgId, skip, sort, filter, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listShadowData request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsShadow&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsShadow>> executeWithHttpInfo() throws ApiException {
            return listShadowDataWithHttpInfo(xOrgId, skip, sort, filter, limit);
        }

        /**
         * Execute listShadowData request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsShadow>> _callback) throws ApiException {
            return listShadowDataAsync(xOrgId, skip, sort, filter, limit, _callback);
        }
    }

    /**
     * LIst System Insights Shadow
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;username&#x60;.
     * @return ListShadowDataRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListShadowDataRequestBuilder listShadowData() throws IllegalArgumentException {
        return new ListShadowDataRequestBuilder();
    }
    private okhttp3.Call listSharedFoldersCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/shared_folders";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listSharedFoldersValidateBeforeCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
        return listSharedFoldersCall(xOrgId, skip, sort, filter, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsSharedFolders>> listSharedFoldersWithHttpInfo(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listSharedFoldersValidateBeforeCall(xOrgId, skip, sort, filter, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsSharedFolders>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listSharedFoldersAsync(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback<List<SystemInsightsSharedFolders>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listSharedFoldersValidateBeforeCall(xOrgId, skip, sort, filter, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsSharedFolders>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListSharedFoldersRequestBuilder {
        private String xOrgId;
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private Integer limit;

        private ListSharedFoldersRequestBuilder() {
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListSharedFoldersRequestBuilder
         */
        public ListSharedFoldersRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListSharedFoldersRequestBuilder
         */
        public ListSharedFoldersRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListSharedFoldersRequestBuilder
         */
        public ListSharedFoldersRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListSharedFoldersRequestBuilder
         */
        public ListSharedFoldersRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListSharedFoldersRequestBuilder
         */
        public ListSharedFoldersRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listSharedFolders
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
            return listSharedFoldersCall(xOrgId, skip, sort, filter, limit, _callback);
        }


        /**
         * Execute listSharedFolders request
         * @return List&lt;SystemInsightsSharedFolders&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsSharedFolders> execute() throws ApiException {
            ApiResponse<List<SystemInsightsSharedFolders>> localVarResp = listSharedFoldersWithHttpInfo(xOrgId, skip, sort, filter, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listSharedFolders request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsSharedFolders&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsSharedFolders>> executeWithHttpInfo() throws ApiException {
            return listSharedFoldersWithHttpInfo(xOrgId, skip, sort, filter, limit);
        }

        /**
         * Execute listSharedFolders request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsSharedFolders>> _callback) throws ApiException {
            return listSharedFoldersAsync(xOrgId, skip, sort, filter, limit, _callback);
        }
    }

    /**
     * List System Insights Shared Folders
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.
     * @return ListSharedFoldersRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListSharedFoldersRequestBuilder listSharedFolders() throws IllegalArgumentException {
        return new ListSharedFoldersRequestBuilder();
    }
    private okhttp3.Call listSharedResourcesCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/shared_resources";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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

        String[] localVarAuthNames = new String[] {  };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call listSharedResourcesValidateBeforeCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
        return listSharedResourcesCall(xOrgId, skip, sort, filter, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsSharedResources>> listSharedResourcesWithHttpInfo(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listSharedResourcesValidateBeforeCall(xOrgId, skip, sort, filter, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsSharedResources>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listSharedResourcesAsync(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback<List<SystemInsightsSharedResources>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listSharedResourcesValidateBeforeCall(xOrgId, skip, sort, filter, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsSharedResources>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListSharedResourcesRequestBuilder {
        private String xOrgId;
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private Integer limit;

        private ListSharedResourcesRequestBuilder() {
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListSharedResourcesRequestBuilder
         */
        public ListSharedResourcesRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListSharedResourcesRequestBuilder
         */
        public ListSharedResourcesRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListSharedResourcesRequestBuilder
         */
        public ListSharedResourcesRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListSharedResourcesRequestBuilder
         */
        public ListSharedResourcesRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListSharedResourcesRequestBuilder
         */
        public ListSharedResourcesRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listSharedResources
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
            return listSharedResourcesCall(xOrgId, skip, sort, filter, limit, _callback);
        }


        /**
         * Execute listSharedResources request
         * @return List&lt;SystemInsightsSharedResources&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsSharedResources> execute() throws ApiException {
            ApiResponse<List<SystemInsightsSharedResources>> localVarResp = listSharedResourcesWithHttpInfo(xOrgId, skip, sort, filter, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listSharedResources request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsSharedResources&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsSharedResources>> executeWithHttpInfo() throws ApiException {
            return listSharedResourcesWithHttpInfo(xOrgId, skip, sort, filter, limit);
        }

        /**
         * Execute listSharedResources request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsSharedResources>> _callback) throws ApiException {
            return listSharedResourcesAsync(xOrgId, skip, sort, filter, limit, _callback);
        }
    }

    /**
     * List System Insights Shared Resources
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;type&#x60;.
     * @return ListSharedResourcesRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListSharedResourcesRequestBuilder listSharedResources() throws IllegalArgumentException {
        return new ListSharedResourcesRequestBuilder();
    }
    private okhttp3.Call listSharingPreferencesCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/sharing_preferences";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listSharingPreferencesValidateBeforeCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
        return listSharingPreferencesCall(xOrgId, skip, sort, filter, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsSharingPreferences>> listSharingPreferencesWithHttpInfo(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listSharingPreferencesValidateBeforeCall(xOrgId, skip, sort, filter, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsSharingPreferences>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listSharingPreferencesAsync(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback<List<SystemInsightsSharingPreferences>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listSharingPreferencesValidateBeforeCall(xOrgId, skip, sort, filter, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsSharingPreferences>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListSharingPreferencesRequestBuilder {
        private String xOrgId;
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private Integer limit;

        private ListSharingPreferencesRequestBuilder() {
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListSharingPreferencesRequestBuilder
         */
        public ListSharingPreferencesRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListSharingPreferencesRequestBuilder
         */
        public ListSharingPreferencesRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListSharingPreferencesRequestBuilder
         */
        public ListSharingPreferencesRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListSharingPreferencesRequestBuilder
         */
        public ListSharingPreferencesRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListSharingPreferencesRequestBuilder
         */
        public ListSharingPreferencesRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listSharingPreferences
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
            return listSharingPreferencesCall(xOrgId, skip, sort, filter, limit, _callback);
        }


        /**
         * Execute listSharingPreferences request
         * @return List&lt;SystemInsightsSharingPreferences&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsSharingPreferences> execute() throws ApiException {
            ApiResponse<List<SystemInsightsSharingPreferences>> localVarResp = listSharingPreferencesWithHttpInfo(xOrgId, skip, sort, filter, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listSharingPreferences request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsSharingPreferences&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsSharingPreferences>> executeWithHttpInfo() throws ApiException {
            return listSharingPreferencesWithHttpInfo(xOrgId, skip, sort, filter, limit);
        }

        /**
         * Execute listSharingPreferences request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsSharingPreferences>> _callback) throws ApiException {
            return listSharingPreferencesAsync(xOrgId, skip, sort, filter, limit, _callback);
        }
    }

    /**
     * List System Insights Sharing Preferences
     * Only valid filed field is &#x60;system_id&#x60;.
     * @return ListSharingPreferencesRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListSharingPreferencesRequestBuilder listSharingPreferences() throws IllegalArgumentException {
        return new ListSharingPreferencesRequestBuilder();
    }
    private okhttp3.Call listStartupItemsCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/startup_items";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listStartupItemsValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return listStartupItemsCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsStartupItems>> listStartupItemsWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listStartupItemsValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsStartupItems>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listStartupItemsAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsStartupItems>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listStartupItemsValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsStartupItems>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListStartupItemsRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private ListStartupItemsRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListStartupItemsRequestBuilder
         */
        public ListStartupItemsRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListStartupItemsRequestBuilder
         */
        public ListStartupItemsRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListStartupItemsRequestBuilder
         */
        public ListStartupItemsRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListStartupItemsRequestBuilder
         */
        public ListStartupItemsRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListStartupItemsRequestBuilder
         */
        public ListStartupItemsRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listStartupItems
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
            return listStartupItemsCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute listStartupItems request
         * @return List&lt;SystemInsightsStartupItems&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsStartupItems> execute() throws ApiException {
            ApiResponse<List<SystemInsightsStartupItems>> localVarResp = listStartupItemsWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listStartupItems request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsStartupItems&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsStartupItems>> executeWithHttpInfo() throws ApiException {
            return listStartupItemsWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute listStartupItems request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsStartupItems>> _callback) throws ApiException {
            return listStartupItemsAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights Startup Items
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.
     * @return ListStartupItemsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListStartupItemsRequestBuilder listStartupItems() throws IllegalArgumentException {
        return new ListStartupItemsRequestBuilder();
    }
    private okhttp3.Call listSystemControlsCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/system_controls";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listSystemControlsValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return listSystemControlsCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsSystemControls>> listSystemControlsWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listSystemControlsValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsSystemControls>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listSystemControlsAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsSystemControls>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listSystemControlsValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsSystemControls>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListSystemControlsRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private ListSystemControlsRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListSystemControlsRequestBuilder
         */
        public ListSystemControlsRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListSystemControlsRequestBuilder
         */
        public ListSystemControlsRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60; Note: You can only filter by &#x60;system_id&#x60; and &#x60;name&#x60;  (optional)
         * @return ListSystemControlsRequestBuilder
         */
        public ListSystemControlsRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListSystemControlsRequestBuilder
         */
        public ListSystemControlsRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListSystemControlsRequestBuilder
         */
        public ListSystemControlsRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listSystemControls
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
            return listSystemControlsCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute listSystemControls request
         * @return List&lt;SystemInsightsSystemControls&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsSystemControls> execute() throws ApiException {
            ApiResponse<List<SystemInsightsSystemControls>> localVarResp = listSystemControlsWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listSystemControls request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsSystemControls&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsSystemControls>> executeWithHttpInfo() throws ApiException {
            return listSystemControlsWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute listSystemControls request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsSystemControls>> _callback) throws ApiException {
            return listSystemControlsAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights System Control
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.
     * @return ListSystemControlsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListSystemControlsRequestBuilder listSystemControls() throws IllegalArgumentException {
        return new ListSystemControlsRequestBuilder();
    }
    private okhttp3.Call listUptimeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/uptime";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listUptimeValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return listUptimeCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsUptime>> listUptimeWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listUptimeValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsUptime>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listUptimeAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsUptime>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listUptimeValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsUptime>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListUptimeRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private ListUptimeRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListUptimeRequestBuilder
         */
        public ListUptimeRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListUptimeRequestBuilder
         */
        public ListUptimeRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, gte, in. e.g: Filter for single value: &#x60;filter&#x3D;field:gte:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListUptimeRequestBuilder
         */
        public ListUptimeRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListUptimeRequestBuilder
         */
        public ListUptimeRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListUptimeRequestBuilder
         */
        public ListUptimeRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listUptime
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
            return listUptimeCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute listUptime request
         * @return List&lt;SystemInsightsUptime&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsUptime> execute() throws ApiException {
            ApiResponse<List<SystemInsightsUptime>> localVarResp = listUptimeWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listUptime request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsUptime&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsUptime>> executeWithHttpInfo() throws ApiException {
            return listUptimeWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute listUptime request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsUptime>> _callback) throws ApiException {
            return listUptimeAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights Uptime
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;days&#x60;.
     * @return ListUptimeRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListUptimeRequestBuilder listUptime() throws IllegalArgumentException {
        return new ListUptimeRequestBuilder();
    }
    private okhttp3.Call listUsbDevicesCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/usb_devices";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listUsbDevicesValidateBeforeCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
        return listUsbDevicesCall(xOrgId, skip, sort, filter, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsUsbDevices>> listUsbDevicesWithHttpInfo(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listUsbDevicesValidateBeforeCall(xOrgId, skip, sort, filter, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsUsbDevices>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listUsbDevicesAsync(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback<List<SystemInsightsUsbDevices>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listUsbDevicesValidateBeforeCall(xOrgId, skip, sort, filter, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsUsbDevices>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListUsbDevicesRequestBuilder {
        private String xOrgId;
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private Integer limit;

        private ListUsbDevicesRequestBuilder() {
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListUsbDevicesRequestBuilder
         */
        public ListUsbDevicesRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListUsbDevicesRequestBuilder
         */
        public ListUsbDevicesRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListUsbDevicesRequestBuilder
         */
        public ListUsbDevicesRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListUsbDevicesRequestBuilder
         */
        public ListUsbDevicesRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListUsbDevicesRequestBuilder
         */
        public ListUsbDevicesRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listUsbDevices
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
            return listUsbDevicesCall(xOrgId, skip, sort, filter, limit, _callback);
        }


        /**
         * Execute listUsbDevices request
         * @return List&lt;SystemInsightsUsbDevices&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsUsbDevices> execute() throws ApiException {
            ApiResponse<List<SystemInsightsUsbDevices>> localVarResp = listUsbDevicesWithHttpInfo(xOrgId, skip, sort, filter, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listUsbDevices request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsUsbDevices&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsUsbDevices>> executeWithHttpInfo() throws ApiException {
            return listUsbDevicesWithHttpInfo(xOrgId, skip, sort, filter, limit);
        }

        /**
         * Execute listUsbDevices request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsUsbDevices>> _callback) throws ApiException {
            return listUsbDevicesAsync(xOrgId, skip, sort, filter, limit, _callback);
        }
    }

    /**
     * List System Insights USB Devices
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;model&#x60;.
     * @return ListUsbDevicesRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListUsbDevicesRequestBuilder listUsbDevices() throws IllegalArgumentException {
        return new ListUsbDevicesRequestBuilder();
    }
    private okhttp3.Call listUserAssistCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/userassist";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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

        String[] localVarAuthNames = new String[] {  };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call listUserAssistValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return listUserAssistCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsUserassist>> listUserAssistWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listUserAssistValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsUserassist>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listUserAssistAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsUserassist>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listUserAssistValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsUserassist>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListUserAssistRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private ListUserAssistRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListUserAssistRequestBuilder
         */
        public ListUserAssistRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListUserAssistRequestBuilder
         */
        public ListUserAssistRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListUserAssistRequestBuilder
         */
        public ListUserAssistRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListUserAssistRequestBuilder
         */
        public ListUserAssistRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListUserAssistRequestBuilder
         */
        public ListUserAssistRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listUserAssist
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
            return listUserAssistCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute listUserAssist request
         * @return List&lt;SystemInsightsUserassist&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsUserassist> execute() throws ApiException {
            ApiResponse<List<SystemInsightsUserassist>> localVarResp = listUserAssistWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listUserAssist request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsUserassist&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsUserassist>> executeWithHttpInfo() throws ApiException {
            return listUserAssistWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute listUserAssist request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsUserassist>> _callback) throws ApiException {
            return listUserAssistAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights User Assist
     * Valid filter fields are &#x60;system_id&#x60;.
     * @return ListUserAssistRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListUserAssistRequestBuilder listUserAssist() throws IllegalArgumentException {
        return new ListUserAssistRequestBuilder();
    }
    private okhttp3.Call listUserSshKeysCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/user_ssh_keys";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listUserSshKeysValidateBeforeCall(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback _callback) throws ApiException {
        return listUserSshKeysCall(xOrgId, skip, sort, filter, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsUserSshKeys>> listUserSshKeysWithHttpInfo(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listUserSshKeysValidateBeforeCall(xOrgId, skip, sort, filter, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsUserSshKeys>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listUserSshKeysAsync(String xOrgId, Integer skip, List<String> sort, List<String> filter, Integer limit, final ApiCallback<List<SystemInsightsUserSshKeys>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listUserSshKeysValidateBeforeCall(xOrgId, skip, sort, filter, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsUserSshKeys>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListUserSshKeysRequestBuilder {
        private String xOrgId;
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private Integer limit;

        private ListUserSshKeysRequestBuilder() {
        }

        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListUserSshKeysRequestBuilder
         */
        public ListUserSshKeysRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListUserSshKeysRequestBuilder
         */
        public ListUserSshKeysRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListUserSshKeysRequestBuilder
         */
        public ListUserSshKeysRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListUserSshKeysRequestBuilder
         */
        public ListUserSshKeysRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListUserSshKeysRequestBuilder
         */
        public ListUserSshKeysRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listUserSshKeys
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
            return listUserSshKeysCall(xOrgId, skip, sort, filter, limit, _callback);
        }


        /**
         * Execute listUserSshKeys request
         * @return List&lt;SystemInsightsUserSshKeys&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsUserSshKeys> execute() throws ApiException {
            ApiResponse<List<SystemInsightsUserSshKeys>> localVarResp = listUserSshKeysWithHttpInfo(xOrgId, skip, sort, filter, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listUserSshKeys request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsUserSshKeys&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsUserSshKeys>> executeWithHttpInfo() throws ApiException {
            return listUserSshKeysWithHttpInfo(xOrgId, skip, sort, filter, limit);
        }

        /**
         * Execute listUserSshKeys request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsUserSshKeys>> _callback) throws ApiException {
            return listUserSshKeysAsync(xOrgId, skip, sort, filter, limit, _callback);
        }
    }

    /**
     * List System Insights User SSH Keys
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;uid&#x60;.
     * @return ListUserSshKeysRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListUserSshKeysRequestBuilder listUserSshKeys() throws IllegalArgumentException {
        return new ListUserSshKeysRequestBuilder();
    }
    private okhttp3.Call listUsersCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/users";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listUsersValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return listUsersCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsUsers>> listUsersWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listUsersValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsUsers>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listUsersAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsUsers>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listUsersValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsUsers>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListUsersRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private ListUsersRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListUsersRequestBuilder
         */
        public ListUsersRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListUsersRequestBuilder
         */
        public ListUsersRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListUsersRequestBuilder
         */
        public ListUsersRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListUsersRequestBuilder
         */
        public ListUsersRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListUsersRequestBuilder
         */
        public ListUsersRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listUsers
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
            return listUsersCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute listUsers request
         * @return List&lt;SystemInsightsUsers&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsUsers> execute() throws ApiException {
            ApiResponse<List<SystemInsightsUsers>> localVarResp = listUsersWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listUsers request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsUsers&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsUsers>> executeWithHttpInfo() throws ApiException {
            return listUsersWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute listUsers request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsUsers>> _callback) throws ApiException {
            return listUsersAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights Users
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;username&#x60;.
     * @return ListUsersRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListUsersRequestBuilder listUsers() throws IllegalArgumentException {
        return new ListUsersRequestBuilder();
    }
    private okhttp3.Call listWifiNetworksCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/wifi_networks";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listWifiNetworksValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return listWifiNetworksCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsWifiNetworks>> listWifiNetworksWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listWifiNetworksValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsWifiNetworks>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listWifiNetworksAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsWifiNetworks>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listWifiNetworksValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsWifiNetworks>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListWifiNetworksRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private ListWifiNetworksRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListWifiNetworksRequestBuilder
         */
        public ListWifiNetworksRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListWifiNetworksRequestBuilder
         */
        public ListWifiNetworksRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListWifiNetworksRequestBuilder
         */
        public ListWifiNetworksRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListWifiNetworksRequestBuilder
         */
        public ListWifiNetworksRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListWifiNetworksRequestBuilder
         */
        public ListWifiNetworksRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listWifiNetworks
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
            return listWifiNetworksCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute listWifiNetworks request
         * @return List&lt;SystemInsightsWifiNetworks&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsWifiNetworks> execute() throws ApiException {
            ApiResponse<List<SystemInsightsWifiNetworks>> localVarResp = listWifiNetworksWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listWifiNetworks request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsWifiNetworks&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsWifiNetworks>> executeWithHttpInfo() throws ApiException {
            return listWifiNetworksWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute listWifiNetworks request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsWifiNetworks>> _callback) throws ApiException {
            return listWifiNetworksAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights WiFi Networks
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;security_type&#x60;.
     * @return ListWifiNetworksRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListWifiNetworksRequestBuilder listWifiNetworks() throws IllegalArgumentException {
        return new ListWifiNetworksRequestBuilder();
    }
    private okhttp3.Call listWifiStatusCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/wifi_status";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listWifiStatusValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return listWifiStatusCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsWifiStatus>> listWifiStatusWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listWifiStatusValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsWifiStatus>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listWifiStatusAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsWifiStatus>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listWifiStatusValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsWifiStatus>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListWifiStatusRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private ListWifiStatusRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListWifiStatusRequestBuilder
         */
        public ListWifiStatusRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListWifiStatusRequestBuilder
         */
        public ListWifiStatusRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListWifiStatusRequestBuilder
         */
        public ListWifiStatusRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListWifiStatusRequestBuilder
         */
        public ListWifiStatusRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListWifiStatusRequestBuilder
         */
        public ListWifiStatusRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listWifiStatus
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
            return listWifiStatusCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute listWifiStatus request
         * @return List&lt;SystemInsightsWifiStatus&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsWifiStatus> execute() throws ApiException {
            ApiResponse<List<SystemInsightsWifiStatus>> localVarResp = listWifiStatusWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listWifiStatus request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsWifiStatus&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsWifiStatus>> executeWithHttpInfo() throws ApiException {
            return listWifiStatusWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute listWifiStatus request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsWifiStatus>> _callback) throws ApiException {
            return listWifiStatusAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights WiFi Status
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;security_type&#x60;.
     * @return ListWifiStatusRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListWifiStatusRequestBuilder listWifiStatus() throws IllegalArgumentException {
        return new ListWifiStatusRequestBuilder();
    }
    private okhttp3.Call listWindowsSecurityCenterCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/windows_security_center";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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

        String[] localVarAuthNames = new String[] {  };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call listWindowsSecurityCenterValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return listWindowsSecurityCenterCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsWindowsSecurityCenter>> listWindowsSecurityCenterWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listWindowsSecurityCenterValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsWindowsSecurityCenter>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listWindowsSecurityCenterAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsWindowsSecurityCenter>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listWindowsSecurityCenterValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsWindowsSecurityCenter>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListWindowsSecurityCenterRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private ListWindowsSecurityCenterRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListWindowsSecurityCenterRequestBuilder
         */
        public ListWindowsSecurityCenterRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListWindowsSecurityCenterRequestBuilder
         */
        public ListWindowsSecurityCenterRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListWindowsSecurityCenterRequestBuilder
         */
        public ListWindowsSecurityCenterRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListWindowsSecurityCenterRequestBuilder
         */
        public ListWindowsSecurityCenterRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListWindowsSecurityCenterRequestBuilder
         */
        public ListWindowsSecurityCenterRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listWindowsSecurityCenter
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
            return listWindowsSecurityCenterCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute listWindowsSecurityCenter request
         * @return List&lt;SystemInsightsWindowsSecurityCenter&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsWindowsSecurityCenter> execute() throws ApiException {
            ApiResponse<List<SystemInsightsWindowsSecurityCenter>> localVarResp = listWindowsSecurityCenterWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listWindowsSecurityCenter request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsWindowsSecurityCenter&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsWindowsSecurityCenter>> executeWithHttpInfo() throws ApiException {
            return listWindowsSecurityCenterWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute listWindowsSecurityCenter request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsWindowsSecurityCenter>> _callback) throws ApiException {
            return listWindowsSecurityCenterAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights Windows Security Center
     * Valid filter fields are &#x60;system_id&#x60;.
     * @return ListWindowsSecurityCenterRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListWindowsSecurityCenterRequestBuilder listWindowsSecurityCenter() throws IllegalArgumentException {
        return new ListWindowsSecurityCenterRequestBuilder();
    }
    private okhttp3.Call listWindowsSecurityProductsCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/systeminsights/windows_security_products";

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

        if (filter != null) {
            localVarCollectionQueryParams.addAll(localVarApiClient.parameterToPairs("csv", "filter", filter));
        }

        if (limit != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("limit", limit));
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
    private okhttp3.Call listWindowsSecurityProductsValidateBeforeCall(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback _callback) throws ApiException {
        return listWindowsSecurityProductsCall(skip, sort, filter, xOrgId, limit, _callback);

    }


    private ApiResponse<List<SystemInsightsWindowsSecurityProducts>> listWindowsSecurityProductsWithHttpInfo(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit) throws ApiException {
        okhttp3.Call localVarCall = listWindowsSecurityProductsValidateBeforeCall(skip, sort, filter, xOrgId, limit, null);
        Type localVarReturnType = new TypeToken<List<SystemInsightsWindowsSecurityProducts>>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listWindowsSecurityProductsAsync(Integer skip, List<String> sort, List<String> filter, String xOrgId, Integer limit, final ApiCallback<List<SystemInsightsWindowsSecurityProducts>> _callback) throws ApiException {

        okhttp3.Call localVarCall = listWindowsSecurityProductsValidateBeforeCall(skip, sort, filter, xOrgId, limit, _callback);
        Type localVarReturnType = new TypeToken<List<SystemInsightsWindowsSecurityProducts>>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListWindowsSecurityProductsRequestBuilder {
        private Integer skip;
        private List<String> sort;
        private List<String> filter;
        private String xOrgId;
        private Integer limit;

        private ListWindowsSecurityProductsRequestBuilder() {
        }

        /**
         * Set skip
         * @param skip The offset into the records to return. (optional, default to 0)
         * @return ListWindowsSecurityProductsRequestBuilder
         */
        public ListWindowsSecurityProductsRequestBuilder skip(Integer skip) {
            this.skip = skip;
            return this;
        }
        
        /**
         * Set sort
         * @param sort The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  (optional)
         * @return ListWindowsSecurityProductsRequestBuilder
         */
        public ListWindowsSecurityProductsRequestBuilder sort(List<String> sort) {
            this.sort = sort;
            return this;
        }
        
        /**
         * Set filter
         * @param filter Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  (optional)
         * @return ListWindowsSecurityProductsRequestBuilder
         */
        public ListWindowsSecurityProductsRequestBuilder filter(List<String> filter) {
            this.filter = filter;
            return this;
        }
        
        /**
         * Set xOrgId
         * @param xOrgId Organization identifier that can be obtained from console settings. (optional)
         * @return ListWindowsSecurityProductsRequestBuilder
         */
        public ListWindowsSecurityProductsRequestBuilder xOrgId(String xOrgId) {
            this.xOrgId = xOrgId;
            return this;
        }
        
        /**
         * Set limit
         * @param limit  (optional, default to 10)
         * @return ListWindowsSecurityProductsRequestBuilder
         */
        public ListWindowsSecurityProductsRequestBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }
        
        /**
         * Build call for listWindowsSecurityProducts
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
            return listWindowsSecurityProductsCall(skip, sort, filter, xOrgId, limit, _callback);
        }


        /**
         * Execute listWindowsSecurityProducts request
         * @return List&lt;SystemInsightsWindowsSecurityProducts&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public List<SystemInsightsWindowsSecurityProducts> execute() throws ApiException {
            ApiResponse<List<SystemInsightsWindowsSecurityProducts>> localVarResp = listWindowsSecurityProductsWithHttpInfo(skip, sort, filter, xOrgId, limit);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listWindowsSecurityProducts request with HTTP info returned
         * @return ApiResponse&lt;List&lt;SystemInsightsWindowsSecurityProducts&gt;&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public ApiResponse<List<SystemInsightsWindowsSecurityProducts>> executeWithHttpInfo() throws ApiException {
            return listWindowsSecurityProductsWithHttpInfo(skip, sort, filter, xOrgId, limit);
        }

        /**
         * Execute listWindowsSecurityProducts request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<List<SystemInsightsWindowsSecurityProducts>> _callback) throws ApiException {
            return listWindowsSecurityProductsAsync(skip, sort, filter, xOrgId, limit, _callback);
        }
    }

    /**
     * List System Insights Windows Security Products
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;state&#x60;.
     * @return ListWindowsSecurityProductsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
     </table>
     */
    public ListWindowsSecurityProductsRequestBuilder listWindowsSecurityProducts() throws IllegalArgumentException {
        return new ListWindowsSecurityProductsRequestBuilder();
    }
}
