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

import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.Configuration;
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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for SystemInsightsApi
 */
@Disabled
public class SystemInsightsApiTest {

    private static SystemInsightsApi api;

    
    @BeforeAll
    public static void beforeClass() {
        ApiClient apiClient = Configuration.getDefaultApiClient();
        api = new SystemInsightsApi(apiClient);
    }

    /**
     * List System Insights Chassis Info
     *
     * Valid filter fields are &#x60;system_id&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getChassisInfoTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsChassisInfo> response = api.getChassisInfo()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Disk Info
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;disk_index&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getDiskInfoTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsDiskInfo> response = api.getDiskInfo()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights IE Extensions
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getIEExtensionsListTest() throws ApiException {
        String xOrgId = null;
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        Integer limit = null;
        List<SystemInsightsIeExtensions> response = api.getIEExtensionsList()
                .xOrgId(xOrgId)
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Kernel Info
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;version&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getKernelInfoTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsKernelInfo> response = api.getKernelInfo()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights OS Version
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;version&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getOsVersionTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsOsVersion> response = api.getOsVersion()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights SIP Config
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;enabled&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getSipConfigTest() throws ApiException {
        String xOrgId = null;
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        Integer limit = null;
        List<SystemInsightsSipConfig> response = api.getSipConfig()
                .xOrgId(xOrgId)
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights System Info
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;cpu_subtype&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getSystemInfoListTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsSystemInfo> response = api.getSystemInfoList()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights TPM Info
     *
     * Valid filter fields are &#x60;system_id&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getTpmInfoTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsTpmInfo> response = api.getTpmInfo()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights User Groups
     *
     * Only valid filter field is &#x60;system_id&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getUserGroupsTest() throws ApiException {
        String xOrgId = null;
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        Integer limit = null;
        List<SystemInsightsUserGroups> response = api.getUserGroups()
                .xOrgId(xOrgId)
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights ALF
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;global_state&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listAlfTest() throws ApiException {
        String xOrgId = null;
        List<String> filter = null;
        Integer skip = null;
        List<String> sort = null;
        Integer limit = null;
        List<SystemInsightsAlf> response = api.listAlf()
                .xOrgId(xOrgId)
                .filter(filter)
                .skip(skip)
                .sort(sort)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights ALF Exceptions
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;state&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listAlfExceptionsTest() throws ApiException {
        String xOrgId = null;
        List<String> filter = null;
        Integer skip = null;
        List<String> sort = null;
        Integer limit = null;
        List<SystemInsightsAlfExceptions> response = api.listAlfExceptions()
                .xOrgId(xOrgId)
                .filter(filter)
                .skip(skip)
                .sort(sort)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights ALF Explicit Authentications
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;process&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listAlfExplicitAuthsTest() throws ApiException {
        String xOrgId = null;
        List<String> filter = null;
        Integer skip = null;
        List<String> sort = null;
        Integer limit = null;
        List<SystemInsightsAlfExplicitAuths> response = api.listAlfExplicitAuths()
                .xOrgId(xOrgId)
                .filter(filter)
                .skip(skip)
                .sort(sort)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Application Compatibility Shims
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;enabled&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listAppcompatShimsTest() throws ApiException {
        String xOrgId = null;
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        Integer limit = null;
        List<SystemInsightsAppcompatShims> response = api.listAppcompatShims()
                .xOrgId(xOrgId)
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Apps
     *
     * Lists all apps for macOS devices. For Windows devices, use [List System Insights Programs](https://docs.jumpcloud.com).  Valid filter fields are &#x60;system_id&#x60; and &#x60;bundle_name&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listAppsTest() throws ApiException {
        String xOrgId = null;
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        Integer limit = null;
        List<SystemInsightsApps> response = api.listApps()
                .xOrgId(xOrgId)
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Authorized Keys
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;uid&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listAuthorizedKeysTest() throws ApiException {
        String xOrgId = null;
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        Integer limit = null;
        List<SystemInsightsAuthorizedKeys> response = api.listAuthorizedKeys()
                .xOrgId(xOrgId)
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Azure Instance Metadata
     *
     * Valid filter fields are &#x60;system_id&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listAzureInstanceMetadataTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsAzureInstanceMetadata> response = api.listAzureInstanceMetadata()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Azure Instance Tags
     *
     * Valid filter fields are &#x60;system_id&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listAzureInstanceTagsTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsAzureInstanceTags> response = api.listAzureInstanceTags()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Battery
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;health&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listBatteryDataTest() throws ApiException {
        String xOrgId = null;
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        Integer limit = null;
        List<SystemInsightsBattery> response = api.listBatteryData()
                .xOrgId(xOrgId)
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Bitlocker Info
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;protection_status&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listBitlockerInfoTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsBitlockerInfo> response = api.listBitlockerInfo()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Browser Plugins
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listBrowserPluginsTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsBrowserPlugins> response = api.listBrowserPlugins()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Certificates
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;common_name&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listCertificatesTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsCertificates> response = api.listCertificates()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Chrome Extensions
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listChromeExtensionsTest() throws ApiException {
        String xOrgId = null;
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        Integer limit = null;
        List<SystemInsightsChromeExtensions> response = api.listChromeExtensions()
                .xOrgId(xOrgId)
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Connectivity
     *
     * The only valid filter field is &#x60;system_id&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listConnectivityTest() throws ApiException {
        String xOrgId = null;
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        Integer limit = null;
        List<SystemInsightsConnectivity> response = api.listConnectivity()
                .xOrgId(xOrgId)
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Crashes
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;identifier&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listCrashesTest() throws ApiException {
        String xOrgId = null;
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        Integer limit = null;
        List<SystemInsightsCrashes> response = api.listCrashes()
                .xOrgId(xOrgId)
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights CUPS Destinations
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listCupsDestinationsTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsCupsDestinations> response = api.listCupsDestinations()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Disk Encryption
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;encryption_status&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listDiskEncryptionTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsDiskEncryption> response = api.listDiskEncryption()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights DNS Resolvers
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;type&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listDnsResolversTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsDnsResolvers> response = api.listDnsResolvers()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Etc Hosts
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;address&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listEtcHostsTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsEtcHosts> response = api.listEtcHosts()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Firefox Addons
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listFirefoxAddonsTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsFirefoxAddons> response = api.listFirefoxAddons()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Groups
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;groupname&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listGroupsTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsGroups> response = api.listGroups()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Interface Addresses
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;address&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listInterfaceAddressesTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsInterfaceAddresses> response = api.listInterfaceAddresses()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Interface Details
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;interface&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listInterfaceDetailsTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsInterfaceDetails> response = api.listInterfaceDetails()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Launchd
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listLaunchdTest() throws ApiException {
        String xOrgId = null;
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        Integer limit = null;
        List<SystemInsightsLaunchd> response = api.listLaunchd()
                .xOrgId(xOrgId)
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Linux Packages
     *
     * Lists all programs for Linux devices. For macOS devices, use [List System Insights System Apps](https://docs.jumpcloud.com). For windows devices, use [List System Insights System Apps](https://docs.jumpcloud.com).  Valid filter fields are &#x60;name&#x60; and &#x60;package_format&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listLinuxPackagesTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsLinuxPackages> response = api.listLinuxPackages()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Logged-In Users
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;user&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listLoggedInUsersTest() throws ApiException {
        String xOrgId = null;
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        Integer limit = null;
        List<SystemInsightsLoggedInUsers> response = api.listLoggedInUsers()
                .xOrgId(xOrgId)
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Logical Drives
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;device_id&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listLogicalDrivesTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsLogicalDrives> response = api.listLogicalDrives()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Managed Policies
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;domain&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listManagedPoliciesTest() throws ApiException {
        String xOrgId = null;
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        Integer limit = null;
        List<SystemInsightsManagedPolicies> response = api.listManagedPolicies()
                .xOrgId(xOrgId)
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Mounts
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;path&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listMountsTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsMounts> response = api.listMounts()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Patches
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;hotfix_id&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listPatchesTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsPatches> response = api.listPatches()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Programs
     *
     * Lists all programs for Windows devices. For macOS devices, use [List System Insights Apps](https://docs.jumpcloud.com).  Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listProgramsTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsPrograms> response = api.listPrograms()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Python Packages
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listPythonPackagesTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsPythonPackages> response = api.listPythonPackages()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Safari Extensions
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listSafariExtensionsTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsSafariExtensions> response = api.listSafariExtensions()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Scheduled Tasks
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;enabled&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listScheduledTasksTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsScheduledTasks> response = api.listScheduledTasks()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Secure Boot
     *
     * Valid filter fields are &#x60;system_id&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listSecureBootTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsSecureboot> response = api.listSecureBoot()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Services
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listServicesTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsServices> response = api.listServices()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * LIst System Insights Shadow
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;username&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listShadowDataTest() throws ApiException {
        String xOrgId = null;
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        Integer limit = null;
        List<SystemInsightsShadow> response = api.listShadowData()
                .xOrgId(xOrgId)
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Shared Folders
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listSharedFoldersTest() throws ApiException {
        String xOrgId = null;
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        Integer limit = null;
        List<SystemInsightsSharedFolders> response = api.listSharedFolders()
                .xOrgId(xOrgId)
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Shared Resources
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;type&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listSharedResourcesTest() throws ApiException {
        String xOrgId = null;
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        Integer limit = null;
        List<SystemInsightsSharedResources> response = api.listSharedResources()
                .xOrgId(xOrgId)
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Sharing Preferences
     *
     * Only valid filed field is &#x60;system_id&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listSharingPreferencesTest() throws ApiException {
        String xOrgId = null;
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        Integer limit = null;
        List<SystemInsightsSharingPreferences> response = api.listSharingPreferences()
                .xOrgId(xOrgId)
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Startup Items
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listStartupItemsTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsStartupItems> response = api.listStartupItems()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights System Control
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listSystemControlsTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsSystemControls> response = api.listSystemControls()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Uptime
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;days&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listUptimeTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsUptime> response = api.listUptime()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights USB Devices
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;model&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listUsbDevicesTest() throws ApiException {
        String xOrgId = null;
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        Integer limit = null;
        List<SystemInsightsUsbDevices> response = api.listUsbDevices()
                .xOrgId(xOrgId)
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights User Assist
     *
     * Valid filter fields are &#x60;system_id&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listUserAssistTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsUserassist> response = api.listUserAssist()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights User SSH Keys
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;uid&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listUserSshKeysTest() throws ApiException {
        String xOrgId = null;
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        Integer limit = null;
        List<SystemInsightsUserSshKeys> response = api.listUserSshKeys()
                .xOrgId(xOrgId)
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Users
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;username&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listUsersTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsUsers> response = api.listUsers()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights WiFi Networks
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;security_type&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listWifiNetworksTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsWifiNetworks> response = api.listWifiNetworks()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights WiFi Status
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;security_type&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listWifiStatusTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsWifiStatus> response = api.listWifiStatus()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Windows Security Center
     *
     * Valid filter fields are &#x60;system_id&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listWindowsSecurityCenterTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsWindowsSecurityCenter> response = api.listWindowsSecurityCenter()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

    /**
     * List System Insights Windows Security Products
     *
     * Valid filter fields are &#x60;system_id&#x60; and &#x60;state&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listWindowsSecurityProductsTest() throws ApiException {
        Integer skip = null;
        List<String> sort = null;
        List<String> filter = null;
        String xOrgId = null;
        Integer limit = null;
        List<SystemInsightsWindowsSecurityProducts> response = api.listWindowsSecurityProducts()
                .skip(skip)
                .sort(sort)
                .filter(filter)
                .xOrgId(xOrgId)
                .limit(limit)
                .execute();
        // TODO: test validations
    }

}
