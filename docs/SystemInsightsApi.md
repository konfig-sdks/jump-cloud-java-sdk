# SystemInsightsApi

All URIs are relative to *https://console.jumpcloud.com/api/v2*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getChassisInfo**](SystemInsightsApi.md#getChassisInfo) | **GET** /systeminsights/chassis_info | List System Insights Chassis Info |
| [**getDiskInfo**](SystemInsightsApi.md#getDiskInfo) | **GET** /systeminsights/disk_info | List System Insights Disk Info |
| [**getIEExtensionsList**](SystemInsightsApi.md#getIEExtensionsList) | **GET** /systeminsights/ie_extensions | List System Insights IE Extensions |
| [**getKernelInfo**](SystemInsightsApi.md#getKernelInfo) | **GET** /systeminsights/kernel_info | List System Insights Kernel Info |
| [**getOsVersion**](SystemInsightsApi.md#getOsVersion) | **GET** /systeminsights/os_version | List System Insights OS Version |
| [**getSipConfig**](SystemInsightsApi.md#getSipConfig) | **GET** /systeminsights/sip_config | List System Insights SIP Config |
| [**getSystemInfoList**](SystemInsightsApi.md#getSystemInfoList) | **GET** /systeminsights/system_info | List System Insights System Info |
| [**getTpmInfo**](SystemInsightsApi.md#getTpmInfo) | **GET** /systeminsights/tpm_info | List System Insights TPM Info |
| [**getUserGroups**](SystemInsightsApi.md#getUserGroups) | **GET** /systeminsights/user_groups | List System Insights User Groups |
| [**listAlf**](SystemInsightsApi.md#listAlf) | **GET** /systeminsights/alf | List System Insights ALF |
| [**listAlfExceptions**](SystemInsightsApi.md#listAlfExceptions) | **GET** /systeminsights/alf_exceptions | List System Insights ALF Exceptions |
| [**listAlfExplicitAuths**](SystemInsightsApi.md#listAlfExplicitAuths) | **GET** /systeminsights/alf_explicit_auths | List System Insights ALF Explicit Authentications |
| [**listAppcompatShims**](SystemInsightsApi.md#listAppcompatShims) | **GET** /systeminsights/appcompat_shims | List System Insights Application Compatibility Shims |
| [**listApps**](SystemInsightsApi.md#listApps) | **GET** /systeminsights/apps | List System Insights Apps |
| [**listAuthorizedKeys**](SystemInsightsApi.md#listAuthorizedKeys) | **GET** /systeminsights/authorized_keys | List System Insights Authorized Keys |
| [**listAzureInstanceMetadata**](SystemInsightsApi.md#listAzureInstanceMetadata) | **GET** /systeminsights/azure_instance_metadata | List System Insights Azure Instance Metadata |
| [**listAzureInstanceTags**](SystemInsightsApi.md#listAzureInstanceTags) | **GET** /systeminsights/azure_instance_tags | List System Insights Azure Instance Tags |
| [**listBatteryData**](SystemInsightsApi.md#listBatteryData) | **GET** /systeminsights/battery | List System Insights Battery |
| [**listBitlockerInfo**](SystemInsightsApi.md#listBitlockerInfo) | **GET** /systeminsights/bitlocker_info | List System Insights Bitlocker Info |
| [**listBrowserPlugins**](SystemInsightsApi.md#listBrowserPlugins) | **GET** /systeminsights/browser_plugins | List System Insights Browser Plugins |
| [**listCertificates**](SystemInsightsApi.md#listCertificates) | **GET** /systeminsights/certificates | List System Insights Certificates |
| [**listChromeExtensions**](SystemInsightsApi.md#listChromeExtensions) | **GET** /systeminsights/chrome_extensions | List System Insights Chrome Extensions |
| [**listConnectivity**](SystemInsightsApi.md#listConnectivity) | **GET** /systeminsights/connectivity | List System Insights Connectivity |
| [**listCrashes**](SystemInsightsApi.md#listCrashes) | **GET** /systeminsights/crashes | List System Insights Crashes |
| [**listCupsDestinations**](SystemInsightsApi.md#listCupsDestinations) | **GET** /systeminsights/cups_destinations | List System Insights CUPS Destinations |
| [**listDiskEncryption**](SystemInsightsApi.md#listDiskEncryption) | **GET** /systeminsights/disk_encryption | List System Insights Disk Encryption |
| [**listDnsResolvers**](SystemInsightsApi.md#listDnsResolvers) | **GET** /systeminsights/dns_resolvers | List System Insights DNS Resolvers |
| [**listEtcHosts**](SystemInsightsApi.md#listEtcHosts) | **GET** /systeminsights/etc_hosts | List System Insights Etc Hosts |
| [**listFirefoxAddons**](SystemInsightsApi.md#listFirefoxAddons) | **GET** /systeminsights/firefox_addons | List System Insights Firefox Addons |
| [**listGroups**](SystemInsightsApi.md#listGroups) | **GET** /systeminsights/groups | List System Insights Groups |
| [**listInterfaceAddresses**](SystemInsightsApi.md#listInterfaceAddresses) | **GET** /systeminsights/interface_addresses | List System Insights Interface Addresses |
| [**listInterfaceDetails**](SystemInsightsApi.md#listInterfaceDetails) | **GET** /systeminsights/interface_details | List System Insights Interface Details |
| [**listLaunchd**](SystemInsightsApi.md#listLaunchd) | **GET** /systeminsights/launchd | List System Insights Launchd |
| [**listLinuxPackages**](SystemInsightsApi.md#listLinuxPackages) | **GET** /systeminsights/linux_packages | List System Insights Linux Packages |
| [**listLoggedInUsers**](SystemInsightsApi.md#listLoggedInUsers) | **GET** /systeminsights/logged_in_users | List System Insights Logged-In Users |
| [**listLogicalDrives**](SystemInsightsApi.md#listLogicalDrives) | **GET** /systeminsights/logical_drives | List System Insights Logical Drives |
| [**listManagedPolicies**](SystemInsightsApi.md#listManagedPolicies) | **GET** /systeminsights/managed_policies | List System Insights Managed Policies |
| [**listMounts**](SystemInsightsApi.md#listMounts) | **GET** /systeminsights/mounts | List System Insights Mounts |
| [**listPatches**](SystemInsightsApi.md#listPatches) | **GET** /systeminsights/patches | List System Insights Patches |
| [**listPrograms**](SystemInsightsApi.md#listPrograms) | **GET** /systeminsights/programs | List System Insights Programs |
| [**listPythonPackages**](SystemInsightsApi.md#listPythonPackages) | **GET** /systeminsights/python_packages | List System Insights Python Packages |
| [**listSafariExtensions**](SystemInsightsApi.md#listSafariExtensions) | **GET** /systeminsights/safari_extensions | List System Insights Safari Extensions |
| [**listScheduledTasks**](SystemInsightsApi.md#listScheduledTasks) | **GET** /systeminsights/scheduled_tasks | List System Insights Scheduled Tasks |
| [**listSecureBoot**](SystemInsightsApi.md#listSecureBoot) | **GET** /systeminsights/secureboot | List System Insights Secure Boot |
| [**listServices**](SystemInsightsApi.md#listServices) | **GET** /systeminsights/services | List System Insights Services |
| [**listShadowData**](SystemInsightsApi.md#listShadowData) | **GET** /systeminsights/shadow | LIst System Insights Shadow |
| [**listSharedFolders**](SystemInsightsApi.md#listSharedFolders) | **GET** /systeminsights/shared_folders | List System Insights Shared Folders |
| [**listSharedResources**](SystemInsightsApi.md#listSharedResources) | **GET** /systeminsights/shared_resources | List System Insights Shared Resources |
| [**listSharingPreferences**](SystemInsightsApi.md#listSharingPreferences) | **GET** /systeminsights/sharing_preferences | List System Insights Sharing Preferences |
| [**listStartupItems**](SystemInsightsApi.md#listStartupItems) | **GET** /systeminsights/startup_items | List System Insights Startup Items |
| [**listSystemControls**](SystemInsightsApi.md#listSystemControls) | **GET** /systeminsights/system_controls | List System Insights System Control |
| [**listUptime**](SystemInsightsApi.md#listUptime) | **GET** /systeminsights/uptime | List System Insights Uptime |
| [**listUsbDevices**](SystemInsightsApi.md#listUsbDevices) | **GET** /systeminsights/usb_devices | List System Insights USB Devices |
| [**listUserAssist**](SystemInsightsApi.md#listUserAssist) | **GET** /systeminsights/userassist | List System Insights User Assist |
| [**listUserSshKeys**](SystemInsightsApi.md#listUserSshKeys) | **GET** /systeminsights/user_ssh_keys | List System Insights User SSH Keys |
| [**listUsers**](SystemInsightsApi.md#listUsers) | **GET** /systeminsights/users | List System Insights Users |
| [**listWifiNetworks**](SystemInsightsApi.md#listWifiNetworks) | **GET** /systeminsights/wifi_networks | List System Insights WiFi Networks |
| [**listWifiStatus**](SystemInsightsApi.md#listWifiStatus) | **GET** /systeminsights/wifi_status | List System Insights WiFi Status |
| [**listWindowsSecurityCenter**](SystemInsightsApi.md#listWindowsSecurityCenter) | **GET** /systeminsights/windows_security_center | List System Insights Windows Security Center |
| [**listWindowsSecurityProducts**](SystemInsightsApi.md#listWindowsSecurityProducts) | **GET** /systeminsights/windows_security_products | List System Insights Windows Security Products |


<a name="getChassisInfo"></a>
# **getChassisInfo**
> List&lt;SystemInsightsChassisInfo&gt; getChassisInfo().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights Chassis Info

Valid filter fields are &#x60;system_id&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsChassisInfo> result = client
              .systemInsights
              .getChassisInfo()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#getChassisInfo");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsChassisInfo>> response = client
              .systemInsights
              .getChassisInfo()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#getChassisInfo");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsChassisInfo&gt;**](SystemInsightsChassisInfo.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="getDiskInfo"></a>
# **getDiskInfo**
> List&lt;SystemInsightsDiskInfo&gt; getDiskInfo().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights Disk Info

Valid filter fields are &#x60;system_id&#x60; and &#x60;disk_index&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsDiskInfo> result = client
              .systemInsights
              .getDiskInfo()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#getDiskInfo");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsDiskInfo>> response = client
              .systemInsights
              .getDiskInfo()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#getDiskInfo");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsDiskInfo&gt;**](SystemInsightsDiskInfo.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="getIEExtensionsList"></a>
# **getIEExtensionsList**
> List&lt;SystemInsightsIeExtensions&gt; getIEExtensionsList().xOrgId(xOrgId).skip(skip).sort(sort).filter(filter).limit(limit).execute();

List System Insights IE Extensions

Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    Integer limit = 10;
    try {
      List<SystemInsightsIeExtensions> result = client
              .systemInsights
              .getIEExtensionsList()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#getIEExtensionsList");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsIeExtensions>> response = client
              .systemInsights
              .getIEExtensionsList()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#getIEExtensionsList");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsIeExtensions&gt;**](SystemInsightsIeExtensions.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="getKernelInfo"></a>
# **getKernelInfo**
> List&lt;SystemInsightsKernelInfo&gt; getKernelInfo().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights Kernel Info

Valid filter fields are &#x60;system_id&#x60; and &#x60;version&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsKernelInfo> result = client
              .systemInsights
              .getKernelInfo()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#getKernelInfo");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsKernelInfo>> response = client
              .systemInsights
              .getKernelInfo()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#getKernelInfo");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsKernelInfo&gt;**](SystemInsightsKernelInfo.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="getOsVersion"></a>
# **getOsVersion**
> List&lt;SystemInsightsOsVersion&gt; getOsVersion().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights OS Version

Valid filter fields are &#x60;system_id&#x60; and &#x60;version&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsOsVersion> result = client
              .systemInsights
              .getOsVersion()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#getOsVersion");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsOsVersion>> response = client
              .systemInsights
              .getOsVersion()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#getOsVersion");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsOsVersion&gt;**](SystemInsightsOsVersion.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="getSipConfig"></a>
# **getSipConfig**
> List&lt;SystemInsightsSipConfig&gt; getSipConfig().xOrgId(xOrgId).skip(skip).sort(sort).filter(filter).limit(limit).execute();

List System Insights SIP Config

Valid filter fields are &#x60;system_id&#x60; and &#x60;enabled&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    Integer limit = 10;
    try {
      List<SystemInsightsSipConfig> result = client
              .systemInsights
              .getSipConfig()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#getSipConfig");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsSipConfig>> response = client
              .systemInsights
              .getSipConfig()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#getSipConfig");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsSipConfig&gt;**](SystemInsightsSipConfig.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="getSystemInfoList"></a>
# **getSystemInfoList**
> List&lt;SystemInsightsSystemInfo&gt; getSystemInfoList().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights System Info

Valid filter fields are &#x60;system_id&#x60; and &#x60;cpu_subtype&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsSystemInfo> result = client
              .systemInsights
              .getSystemInfoList()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#getSystemInfoList");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsSystemInfo>> response = client
              .systemInsights
              .getSystemInfoList()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#getSystemInfoList");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsSystemInfo&gt;**](SystemInsightsSystemInfo.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="getTpmInfo"></a>
# **getTpmInfo**
> List&lt;SystemInsightsTpmInfo&gt; getTpmInfo().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights TPM Info

Valid filter fields are &#x60;system_id&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsTpmInfo> result = client
              .systemInsights
              .getTpmInfo()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#getTpmInfo");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsTpmInfo>> response = client
              .systemInsights
              .getTpmInfo()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#getTpmInfo");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsTpmInfo&gt;**](SystemInsightsTpmInfo.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/html

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="getUserGroups"></a>
# **getUserGroups**
> List&lt;SystemInsightsUserGroups&gt; getUserGroups().xOrgId(xOrgId).skip(skip).sort(sort).filter(filter).limit(limit).execute();

List System Insights User Groups

Only valid filter field is &#x60;system_id&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    Integer limit = 10;
    try {
      List<SystemInsightsUserGroups> result = client
              .systemInsights
              .getUserGroups()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#getUserGroups");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsUserGroups>> response = client
              .systemInsights
              .getUserGroups()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#getUserGroups");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsUserGroups&gt;**](SystemInsightsUserGroups.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listAlf"></a>
# **listAlf**
> List&lt;SystemInsightsAlf&gt; listAlf().xOrgId(xOrgId).filter(filter).skip(skip).sort(sort).limit(limit).execute();

List System Insights ALF

Valid filter fields are &#x60;system_id&#x60; and &#x60;global_state&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    Integer limit = 10;
    try {
      List<SystemInsightsAlf> result = client
              .systemInsights
              .listAlf()
              .xOrgId(xOrgId)
              .filter(filter)
              .skip(skip)
              .sort(sort)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listAlf");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsAlf>> response = client
              .systemInsights
              .listAlf()
              .xOrgId(xOrgId)
              .filter(filter)
              .skip(skip)
              .sort(sort)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listAlf");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsAlf&gt;**](SystemInsightsAlf.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listAlfExceptions"></a>
# **listAlfExceptions**
> List&lt;SystemInsightsAlfExceptions&gt; listAlfExceptions().xOrgId(xOrgId).filter(filter).skip(skip).sort(sort).limit(limit).execute();

List System Insights ALF Exceptions

Valid filter fields are &#x60;system_id&#x60; and &#x60;state&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    Integer limit = 10;
    try {
      List<SystemInsightsAlfExceptions> result = client
              .systemInsights
              .listAlfExceptions()
              .xOrgId(xOrgId)
              .filter(filter)
              .skip(skip)
              .sort(sort)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listAlfExceptions");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsAlfExceptions>> response = client
              .systemInsights
              .listAlfExceptions()
              .xOrgId(xOrgId)
              .filter(filter)
              .skip(skip)
              .sort(sort)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listAlfExceptions");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsAlfExceptions&gt;**](SystemInsightsAlfExceptions.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listAlfExplicitAuths"></a>
# **listAlfExplicitAuths**
> List&lt;SystemInsightsAlfExplicitAuths&gt; listAlfExplicitAuths().xOrgId(xOrgId).filter(filter).skip(skip).sort(sort).limit(limit).execute();

List System Insights ALF Explicit Authentications

Valid filter fields are &#x60;system_id&#x60; and &#x60;process&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    Integer limit = 10;
    try {
      List<SystemInsightsAlfExplicitAuths> result = client
              .systemInsights
              .listAlfExplicitAuths()
              .xOrgId(xOrgId)
              .filter(filter)
              .skip(skip)
              .sort(sort)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listAlfExplicitAuths");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsAlfExplicitAuths>> response = client
              .systemInsights
              .listAlfExplicitAuths()
              .xOrgId(xOrgId)
              .filter(filter)
              .skip(skip)
              .sort(sort)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listAlfExplicitAuths");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsAlfExplicitAuths&gt;**](SystemInsightsAlfExplicitAuths.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listAppcompatShims"></a>
# **listAppcompatShims**
> List&lt;SystemInsightsAppcompatShims&gt; listAppcompatShims().xOrgId(xOrgId).skip(skip).sort(sort).filter(filter).limit(limit).execute();

List System Insights Application Compatibility Shims

Valid filter fields are &#x60;system_id&#x60; and &#x60;enabled&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    Integer limit = 10;
    try {
      List<SystemInsightsAppcompatShims> result = client
              .systemInsights
              .listAppcompatShims()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listAppcompatShims");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsAppcompatShims>> response = client
              .systemInsights
              .listAppcompatShims()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listAppcompatShims");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsAppcompatShims&gt;**](SystemInsightsAppcompatShims.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listApps"></a>
# **listApps**
> List&lt;SystemInsightsApps&gt; listApps().xOrgId(xOrgId).skip(skip).sort(sort).filter(filter).limit(limit).execute();

List System Insights Apps

Lists all apps for macOS devices. For Windows devices, use [List System Insights Programs](https://docs.jumpcloud.com).  Valid filter fields are &#x60;system_id&#x60; and &#x60;bundle_name&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    Integer limit = 10;
    try {
      List<SystemInsightsApps> result = client
              .systemInsights
              .listApps()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listApps");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsApps>> response = client
              .systemInsights
              .listApps()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listApps");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsApps&gt;**](SystemInsightsApps.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listAuthorizedKeys"></a>
# **listAuthorizedKeys**
> List&lt;SystemInsightsAuthorizedKeys&gt; listAuthorizedKeys().xOrgId(xOrgId).skip(skip).sort(sort).filter(filter).limit(limit).execute();

List System Insights Authorized Keys

Valid filter fields are &#x60;system_id&#x60; and &#x60;uid&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    Integer limit = 10;
    try {
      List<SystemInsightsAuthorizedKeys> result = client
              .systemInsights
              .listAuthorizedKeys()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listAuthorizedKeys");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsAuthorizedKeys>> response = client
              .systemInsights
              .listAuthorizedKeys()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listAuthorizedKeys");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsAuthorizedKeys&gt;**](SystemInsightsAuthorizedKeys.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listAzureInstanceMetadata"></a>
# **listAzureInstanceMetadata**
> List&lt;SystemInsightsAzureInstanceMetadata&gt; listAzureInstanceMetadata().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights Azure Instance Metadata

Valid filter fields are &#x60;system_id&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsAzureInstanceMetadata> result = client
              .systemInsights
              .listAzureInstanceMetadata()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listAzureInstanceMetadata");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsAzureInstanceMetadata>> response = client
              .systemInsights
              .listAzureInstanceMetadata()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listAzureInstanceMetadata");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsAzureInstanceMetadata&gt;**](SystemInsightsAzureInstanceMetadata.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listAzureInstanceTags"></a>
# **listAzureInstanceTags**
> List&lt;SystemInsightsAzureInstanceTags&gt; listAzureInstanceTags().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights Azure Instance Tags

Valid filter fields are &#x60;system_id&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsAzureInstanceTags> result = client
              .systemInsights
              .listAzureInstanceTags()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listAzureInstanceTags");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsAzureInstanceTags>> response = client
              .systemInsights
              .listAzureInstanceTags()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listAzureInstanceTags");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsAzureInstanceTags&gt;**](SystemInsightsAzureInstanceTags.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listBatteryData"></a>
# **listBatteryData**
> List&lt;SystemInsightsBattery&gt; listBatteryData().xOrgId(xOrgId).skip(skip).sort(sort).filter(filter).limit(limit).execute();

List System Insights Battery

Valid filter fields are &#x60;system_id&#x60; and &#x60;health&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    Integer limit = 10;
    try {
      List<SystemInsightsBattery> result = client
              .systemInsights
              .listBatteryData()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listBatteryData");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsBattery>> response = client
              .systemInsights
              .listBatteryData()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listBatteryData");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsBattery&gt;**](SystemInsightsBattery.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listBitlockerInfo"></a>
# **listBitlockerInfo**
> List&lt;SystemInsightsBitlockerInfo&gt; listBitlockerInfo().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights Bitlocker Info

Valid filter fields are &#x60;system_id&#x60; and &#x60;protection_status&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsBitlockerInfo> result = client
              .systemInsights
              .listBitlockerInfo()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listBitlockerInfo");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsBitlockerInfo>> response = client
              .systemInsights
              .listBitlockerInfo()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listBitlockerInfo");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsBitlockerInfo&gt;**](SystemInsightsBitlockerInfo.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listBrowserPlugins"></a>
# **listBrowserPlugins**
> List&lt;SystemInsightsBrowserPlugins&gt; listBrowserPlugins().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights Browser Plugins

Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsBrowserPlugins> result = client
              .systemInsights
              .listBrowserPlugins()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listBrowserPlugins");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsBrowserPlugins>> response = client
              .systemInsights
              .listBrowserPlugins()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listBrowserPlugins");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsBrowserPlugins&gt;**](SystemInsightsBrowserPlugins.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listCertificates"></a>
# **listCertificates**
> List&lt;SystemInsightsCertificates&gt; listCertificates().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights Certificates

Valid filter fields are &#x60;system_id&#x60; and &#x60;common_name&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` Note: You can only filter by `system_id` and `common_name` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsCertificates> result = client
              .systemInsights
              .listCertificates()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listCertificates");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsCertificates>> response = client
              .systemInsights
              .listCertificates()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listCertificates");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60; Note: You can only filter by &#x60;system_id&#x60; and &#x60;common_name&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsCertificates&gt;**](SystemInsightsCertificates.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listChromeExtensions"></a>
# **listChromeExtensions**
> List&lt;SystemInsightsChromeExtensions&gt; listChromeExtensions().xOrgId(xOrgId).skip(skip).sort(sort).filter(filter).limit(limit).execute();

List System Insights Chrome Extensions

Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    Integer limit = 10;
    try {
      List<SystemInsightsChromeExtensions> result = client
              .systemInsights
              .listChromeExtensions()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listChromeExtensions");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsChromeExtensions>> response = client
              .systemInsights
              .listChromeExtensions()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listChromeExtensions");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsChromeExtensions&gt;**](SystemInsightsChromeExtensions.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listConnectivity"></a>
# **listConnectivity**
> List&lt;SystemInsightsConnectivity&gt; listConnectivity().xOrgId(xOrgId).skip(skip).sort(sort).filter(filter).limit(limit).execute();

List System Insights Connectivity

The only valid filter field is &#x60;system_id&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    Integer limit = 10;
    try {
      List<SystemInsightsConnectivity> result = client
              .systemInsights
              .listConnectivity()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listConnectivity");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsConnectivity>> response = client
              .systemInsights
              .listConnectivity()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listConnectivity");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsConnectivity&gt;**](SystemInsightsConnectivity.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listCrashes"></a>
# **listCrashes**
> List&lt;SystemInsightsCrashes&gt; listCrashes().xOrgId(xOrgId).skip(skip).sort(sort).filter(filter).limit(limit).execute();

List System Insights Crashes

Valid filter fields are &#x60;system_id&#x60; and &#x60;identifier&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    Integer limit = 10;
    try {
      List<SystemInsightsCrashes> result = client
              .systemInsights
              .listCrashes()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listCrashes");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsCrashes>> response = client
              .systemInsights
              .listCrashes()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listCrashes");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsCrashes&gt;**](SystemInsightsCrashes.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listCupsDestinations"></a>
# **listCupsDestinations**
> List&lt;SystemInsightsCupsDestinations&gt; listCupsDestinations().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights CUPS Destinations

Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsCupsDestinations> result = client
              .systemInsights
              .listCupsDestinations()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listCupsDestinations");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsCupsDestinations>> response = client
              .systemInsights
              .listCupsDestinations()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listCupsDestinations");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsCupsDestinations&gt;**](SystemInsightsCupsDestinations.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listDiskEncryption"></a>
# **listDiskEncryption**
> List&lt;SystemInsightsDiskEncryption&gt; listDiskEncryption().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights Disk Encryption

Valid filter fields are &#x60;system_id&#x60; and &#x60;encryption_status&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsDiskEncryption> result = client
              .systemInsights
              .listDiskEncryption()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listDiskEncryption");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsDiskEncryption>> response = client
              .systemInsights
              .listDiskEncryption()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listDiskEncryption");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsDiskEncryption&gt;**](SystemInsightsDiskEncryption.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listDnsResolvers"></a>
# **listDnsResolvers**
> List&lt;SystemInsightsDnsResolvers&gt; listDnsResolvers().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights DNS Resolvers

Valid filter fields are &#x60;system_id&#x60; and &#x60;type&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsDnsResolvers> result = client
              .systemInsights
              .listDnsResolvers()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listDnsResolvers");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsDnsResolvers>> response = client
              .systemInsights
              .listDnsResolvers()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listDnsResolvers");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsDnsResolvers&gt;**](SystemInsightsDnsResolvers.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listEtcHosts"></a>
# **listEtcHosts**
> List&lt;SystemInsightsEtcHosts&gt; listEtcHosts().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights Etc Hosts

Valid filter fields are &#x60;system_id&#x60; and &#x60;address&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsEtcHosts> result = client
              .systemInsights
              .listEtcHosts()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listEtcHosts");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsEtcHosts>> response = client
              .systemInsights
              .listEtcHosts()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listEtcHosts");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsEtcHosts&gt;**](SystemInsightsEtcHosts.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listFirefoxAddons"></a>
# **listFirefoxAddons**
> List&lt;SystemInsightsFirefoxAddons&gt; listFirefoxAddons().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights Firefox Addons

Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsFirefoxAddons> result = client
              .systemInsights
              .listFirefoxAddons()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listFirefoxAddons");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsFirefoxAddons>> response = client
              .systemInsights
              .listFirefoxAddons()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listFirefoxAddons");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsFirefoxAddons&gt;**](SystemInsightsFirefoxAddons.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listGroups"></a>
# **listGroups**
> List&lt;SystemInsightsGroups&gt; listGroups().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights Groups

Valid filter fields are &#x60;system_id&#x60; and &#x60;groupname&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsGroups> result = client
              .systemInsights
              .listGroups()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listGroups");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsGroups>> response = client
              .systemInsights
              .listGroups()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listGroups");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsGroups&gt;**](SystemInsightsGroups.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listInterfaceAddresses"></a>
# **listInterfaceAddresses**
> List&lt;SystemInsightsInterfaceAddresses&gt; listInterfaceAddresses().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights Interface Addresses

Valid filter fields are &#x60;system_id&#x60; and &#x60;address&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsInterfaceAddresses> result = client
              .systemInsights
              .listInterfaceAddresses()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listInterfaceAddresses");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsInterfaceAddresses>> response = client
              .systemInsights
              .listInterfaceAddresses()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listInterfaceAddresses");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsInterfaceAddresses&gt;**](SystemInsightsInterfaceAddresses.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listInterfaceDetails"></a>
# **listInterfaceDetails**
> List&lt;SystemInsightsInterfaceDetails&gt; listInterfaceDetails().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights Interface Details

Valid filter fields are &#x60;system_id&#x60; and &#x60;interface&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsInterfaceDetails> result = client
              .systemInsights
              .listInterfaceDetails()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listInterfaceDetails");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsInterfaceDetails>> response = client
              .systemInsights
              .listInterfaceDetails()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listInterfaceDetails");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsInterfaceDetails&gt;**](SystemInsightsInterfaceDetails.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listLaunchd"></a>
# **listLaunchd**
> List&lt;SystemInsightsLaunchd&gt; listLaunchd().xOrgId(xOrgId).skip(skip).sort(sort).filter(filter).limit(limit).execute();

List System Insights Launchd

Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    Integer limit = 10;
    try {
      List<SystemInsightsLaunchd> result = client
              .systemInsights
              .listLaunchd()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listLaunchd");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsLaunchd>> response = client
              .systemInsights
              .listLaunchd()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listLaunchd");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsLaunchd&gt;**](SystemInsightsLaunchd.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listLinuxPackages"></a>
# **listLinuxPackages**
> List&lt;SystemInsightsLinuxPackages&gt; listLinuxPackages().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights Linux Packages

Lists all programs for Linux devices. For macOS devices, use [List System Insights System Apps](https://docs.jumpcloud.com). For windows devices, use [List System Insights System Apps](https://docs.jumpcloud.com).  Valid filter fields are &#x60;name&#x60; and &#x60;package_format&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsLinuxPackages> result = client
              .systemInsights
              .listLinuxPackages()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listLinuxPackages");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsLinuxPackages>> response = client
              .systemInsights
              .listLinuxPackages()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listLinuxPackages");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsLinuxPackages&gt;**](SystemInsightsLinuxPackages.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listLoggedInUsers"></a>
# **listLoggedInUsers**
> List&lt;SystemInsightsLoggedInUsers&gt; listLoggedInUsers().xOrgId(xOrgId).skip(skip).sort(sort).filter(filter).limit(limit).execute();

List System Insights Logged-In Users

Valid filter fields are &#x60;system_id&#x60; and &#x60;user&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    Integer limit = 10;
    try {
      List<SystemInsightsLoggedInUsers> result = client
              .systemInsights
              .listLoggedInUsers()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listLoggedInUsers");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsLoggedInUsers>> response = client
              .systemInsights
              .listLoggedInUsers()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listLoggedInUsers");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsLoggedInUsers&gt;**](SystemInsightsLoggedInUsers.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listLogicalDrives"></a>
# **listLogicalDrives**
> List&lt;SystemInsightsLogicalDrives&gt; listLogicalDrives().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights Logical Drives

Valid filter fields are &#x60;system_id&#x60; and &#x60;device_id&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsLogicalDrives> result = client
              .systemInsights
              .listLogicalDrives()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listLogicalDrives");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsLogicalDrives>> response = client
              .systemInsights
              .listLogicalDrives()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listLogicalDrives");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsLogicalDrives&gt;**](SystemInsightsLogicalDrives.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listManagedPolicies"></a>
# **listManagedPolicies**
> List&lt;SystemInsightsManagedPolicies&gt; listManagedPolicies().xOrgId(xOrgId).skip(skip).sort(sort).filter(filter).limit(limit).execute();

List System Insights Managed Policies

Valid filter fields are &#x60;system_id&#x60; and &#x60;domain&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    Integer limit = 10;
    try {
      List<SystemInsightsManagedPolicies> result = client
              .systemInsights
              .listManagedPolicies()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listManagedPolicies");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsManagedPolicies>> response = client
              .systemInsights
              .listManagedPolicies()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listManagedPolicies");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsManagedPolicies&gt;**](SystemInsightsManagedPolicies.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listMounts"></a>
# **listMounts**
> List&lt;SystemInsightsMounts&gt; listMounts().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights Mounts

Valid filter fields are &#x60;system_id&#x60; and &#x60;path&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsMounts> result = client
              .systemInsights
              .listMounts()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listMounts");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsMounts>> response = client
              .systemInsights
              .listMounts()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listMounts");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsMounts&gt;**](SystemInsightsMounts.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listPatches"></a>
# **listPatches**
> List&lt;SystemInsightsPatches&gt; listPatches().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights Patches

Valid filter fields are &#x60;system_id&#x60; and &#x60;hotfix_id&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsPatches> result = client
              .systemInsights
              .listPatches()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listPatches");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsPatches>> response = client
              .systemInsights
              .listPatches()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listPatches");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsPatches&gt;**](SystemInsightsPatches.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listPrograms"></a>
# **listPrograms**
> List&lt;SystemInsightsPrograms&gt; listPrograms().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights Programs

Lists all programs for Windows devices. For macOS devices, use [List System Insights Apps](https://docs.jumpcloud.com).  Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsPrograms> result = client
              .systemInsights
              .listPrograms()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listPrograms");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsPrograms>> response = client
              .systemInsights
              .listPrograms()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listPrograms");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsPrograms&gt;**](SystemInsightsPrograms.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listPythonPackages"></a>
# **listPythonPackages**
> List&lt;SystemInsightsPythonPackages&gt; listPythonPackages().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights Python Packages

Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsPythonPackages> result = client
              .systemInsights
              .listPythonPackages()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listPythonPackages");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsPythonPackages>> response = client
              .systemInsights
              .listPythonPackages()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listPythonPackages");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsPythonPackages&gt;**](SystemInsightsPythonPackages.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listSafariExtensions"></a>
# **listSafariExtensions**
> List&lt;SystemInsightsSafariExtensions&gt; listSafariExtensions().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights Safari Extensions

Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsSafariExtensions> result = client
              .systemInsights
              .listSafariExtensions()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listSafariExtensions");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsSafariExtensions>> response = client
              .systemInsights
              .listSafariExtensions()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listSafariExtensions");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsSafariExtensions&gt;**](SystemInsightsSafariExtensions.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listScheduledTasks"></a>
# **listScheduledTasks**
> List&lt;SystemInsightsScheduledTasks&gt; listScheduledTasks().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights Scheduled Tasks

Valid filter fields are &#x60;system_id&#x60; and &#x60;enabled&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsScheduledTasks> result = client
              .systemInsights
              .listScheduledTasks()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listScheduledTasks");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsScheduledTasks>> response = client
              .systemInsights
              .listScheduledTasks()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listScheduledTasks");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsScheduledTasks&gt;**](SystemInsightsScheduledTasks.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listSecureBoot"></a>
# **listSecureBoot**
> List&lt;SystemInsightsSecureboot&gt; listSecureBoot().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights Secure Boot

Valid filter fields are &#x60;system_id&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsSecureboot> result = client
              .systemInsights
              .listSecureBoot()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listSecureBoot");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsSecureboot>> response = client
              .systemInsights
              .listSecureBoot()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listSecureBoot");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsSecureboot&gt;**](SystemInsightsSecureboot.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listServices"></a>
# **listServices**
> List&lt;SystemInsightsServices&gt; listServices().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights Services

Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsServices> result = client
              .systemInsights
              .listServices()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listServices");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsServices>> response = client
              .systemInsights
              .listServices()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listServices");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsServices&gt;**](SystemInsightsServices.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listShadowData"></a>
# **listShadowData**
> List&lt;SystemInsightsShadow&gt; listShadowData().xOrgId(xOrgId).skip(skip).sort(sort).filter(filter).limit(limit).execute();

LIst System Insights Shadow

Valid filter fields are &#x60;system_id&#x60; and &#x60;username&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    Integer limit = 10;
    try {
      List<SystemInsightsShadow> result = client
              .systemInsights
              .listShadowData()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listShadowData");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsShadow>> response = client
              .systemInsights
              .listShadowData()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listShadowData");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsShadow&gt;**](SystemInsightsShadow.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listSharedFolders"></a>
# **listSharedFolders**
> List&lt;SystemInsightsSharedFolders&gt; listSharedFolders().xOrgId(xOrgId).skip(skip).sort(sort).filter(filter).limit(limit).execute();

List System Insights Shared Folders

Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    Integer limit = 10;
    try {
      List<SystemInsightsSharedFolders> result = client
              .systemInsights
              .listSharedFolders()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listSharedFolders");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsSharedFolders>> response = client
              .systemInsights
              .listSharedFolders()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listSharedFolders");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsSharedFolders&gt;**](SystemInsightsSharedFolders.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listSharedResources"></a>
# **listSharedResources**
> List&lt;SystemInsightsSharedResources&gt; listSharedResources().xOrgId(xOrgId).skip(skip).sort(sort).filter(filter).limit(limit).execute();

List System Insights Shared Resources

Valid filter fields are &#x60;system_id&#x60; and &#x60;type&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    JumpCloud client = new JumpCloud(configuration);
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    Integer limit = 10;
    try {
      List<SystemInsightsSharedResources> result = client
              .systemInsights
              .listSharedResources()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listSharedResources");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsSharedResources>> response = client
              .systemInsights
              .listSharedResources()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listSharedResources");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsSharedResources&gt;**](SystemInsightsSharedResources.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listSharingPreferences"></a>
# **listSharingPreferences**
> List&lt;SystemInsightsSharingPreferences&gt; listSharingPreferences().xOrgId(xOrgId).skip(skip).sort(sort).filter(filter).limit(limit).execute();

List System Insights Sharing Preferences

Only valid filed field is &#x60;system_id&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    Integer limit = 10;
    try {
      List<SystemInsightsSharingPreferences> result = client
              .systemInsights
              .listSharingPreferences()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listSharingPreferences");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsSharingPreferences>> response = client
              .systemInsights
              .listSharingPreferences()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listSharingPreferences");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsSharingPreferences&gt;**](SystemInsightsSharingPreferences.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listStartupItems"></a>
# **listStartupItems**
> List&lt;SystemInsightsStartupItems&gt; listStartupItems().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights Startup Items

Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsStartupItems> result = client
              .systemInsights
              .listStartupItems()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listStartupItems");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsStartupItems>> response = client
              .systemInsights
              .listStartupItems()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listStartupItems");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsStartupItems&gt;**](SystemInsightsStartupItems.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listSystemControls"></a>
# **listSystemControls**
> List&lt;SystemInsightsSystemControls&gt; listSystemControls().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights System Control

Valid filter fields are &#x60;system_id&#x60; and &#x60;name&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` Note: You can only filter by `system_id` and `name` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsSystemControls> result = client
              .systemInsights
              .listSystemControls()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listSystemControls");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsSystemControls>> response = client
              .systemInsights
              .listSystemControls()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listSystemControls");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60; Note: You can only filter by &#x60;system_id&#x60; and &#x60;name&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsSystemControls&gt;**](SystemInsightsSystemControls.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listUptime"></a>
# **listUptime**
> List&lt;SystemInsightsUptime&gt; listUptime().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights Uptime

Valid filter fields are &#x60;system_id&#x60; and &#x60;days&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, gte, in. e.g: Filter for single value: `filter=field:gte:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsUptime> result = client
              .systemInsights
              .listUptime()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listUptime");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsUptime>> response = client
              .systemInsights
              .listUptime()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listUptime");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, gte, in. e.g: Filter for single value: &#x60;filter&#x3D;field:gte:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsUptime&gt;**](SystemInsightsUptime.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listUsbDevices"></a>
# **listUsbDevices**
> List&lt;SystemInsightsUsbDevices&gt; listUsbDevices().xOrgId(xOrgId).skip(skip).sort(sort).filter(filter).limit(limit).execute();

List System Insights USB Devices

Valid filter fields are &#x60;system_id&#x60; and &#x60;model&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    Integer limit = 10;
    try {
      List<SystemInsightsUsbDevices> result = client
              .systemInsights
              .listUsbDevices()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listUsbDevices");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsUsbDevices>> response = client
              .systemInsights
              .listUsbDevices()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listUsbDevices");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsUsbDevices&gt;**](SystemInsightsUsbDevices.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listUserAssist"></a>
# **listUserAssist**
> List&lt;SystemInsightsUserassist&gt; listUserAssist().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights User Assist

Valid filter fields are &#x60;system_id&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsUserassist> result = client
              .systemInsights
              .listUserAssist()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listUserAssist");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsUserassist>> response = client
              .systemInsights
              .listUserAssist()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listUserAssist");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsUserassist&gt;**](SystemInsightsUserassist.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listUserSshKeys"></a>
# **listUserSshKeys**
> List&lt;SystemInsightsUserSshKeys&gt; listUserSshKeys().xOrgId(xOrgId).skip(skip).sort(sort).filter(filter).limit(limit).execute();

List System Insights User SSH Keys

Valid filter fields are &#x60;system_id&#x60; and &#x60;uid&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    Integer limit = 10;
    try {
      List<SystemInsightsUserSshKeys> result = client
              .systemInsights
              .listUserSshKeys()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listUserSshKeys");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsUserSshKeys>> response = client
              .systemInsights
              .listUserSshKeys()
              .xOrgId(xOrgId)
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listUserSshKeys");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsUserSshKeys&gt;**](SystemInsightsUserSshKeys.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listUsers"></a>
# **listUsers**
> List&lt;SystemInsightsUsers&gt; listUsers().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights Users

Valid filter fields are &#x60;system_id&#x60; and &#x60;username&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsUsers> result = client
              .systemInsights
              .listUsers()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listUsers");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsUsers>> response = client
              .systemInsights
              .listUsers()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listUsers");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsUsers&gt;**](SystemInsightsUsers.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listWifiNetworks"></a>
# **listWifiNetworks**
> List&lt;SystemInsightsWifiNetworks&gt; listWifiNetworks().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights WiFi Networks

Valid filter fields are &#x60;system_id&#x60; and &#x60;security_type&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsWifiNetworks> result = client
              .systemInsights
              .listWifiNetworks()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listWifiNetworks");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsWifiNetworks>> response = client
              .systemInsights
              .listWifiNetworks()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listWifiNetworks");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsWifiNetworks&gt;**](SystemInsightsWifiNetworks.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listWifiStatus"></a>
# **listWifiStatus**
> List&lt;SystemInsightsWifiStatus&gt; listWifiStatus().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights WiFi Status

Valid filter fields are &#x60;system_id&#x60; and &#x60;security_type&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsWifiStatus> result = client
              .systemInsights
              .listWifiStatus()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listWifiStatus");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsWifiStatus>> response = client
              .systemInsights
              .listWifiStatus()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listWifiStatus");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsWifiStatus&gt;**](SystemInsightsWifiStatus.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listWindowsSecurityCenter"></a>
# **listWindowsSecurityCenter**
> List&lt;SystemInsightsWindowsSecurityCenter&gt; listWindowsSecurityCenter().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights Windows Security Center

Valid filter fields are &#x60;system_id&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsWindowsSecurityCenter> result = client
              .systemInsights
              .listWindowsSecurityCenter()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listWindowsSecurityCenter");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsWindowsSecurityCenter>> response = client
              .systemInsights
              .listWindowsSecurityCenter()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listWindowsSecurityCenter");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsWindowsSecurityCenter&gt;**](SystemInsightsWindowsSecurityCenter.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listWindowsSecurityProducts"></a>
# **listWindowsSecurityProducts**
> List&lt;SystemInsightsWindowsSecurityProducts&gt; listWindowsSecurityProducts().skip(skip).sort(sort).filter(filter).xOrgId(xOrgId).limit(limit).execute();

List System Insights Windows Security Products

Valid filter fields are &#x60;system_id&#x60; and &#x60;state&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemInsightsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. e.g: Sort by single field: `sort=field` Sort descending by single field: `sort=-field` Sort by multiple fields: `sort=field1,-field2,field3` 
    List<String> filter = Arrays.asList(); // Supported operators are: eq, in. e.g: Filter for single value: `filter=field:eq:value` Filter for any value in a list: (note \"pipe\" character: `|` separating values) `filter=field:in:value1|value2|value3` 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10;
    try {
      List<SystemInsightsWindowsSecurityProducts> result = client
              .systemInsights
              .listWindowsSecurityProducts()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listWindowsSecurityProducts");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SystemInsightsWindowsSecurityProducts>> response = client
              .systemInsights
              .listWindowsSecurityProducts()
              .skip(skip)
              .sort(sort)
              .filter(filter)
              .xOrgId(xOrgId)
              .limit(limit)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemInsightsApi#listWindowsSecurityProducts");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending. e.g: Sort by single field: &#x60;sort&#x3D;field&#x60; Sort descending by single field: &#x60;sort&#x3D;-field&#x60; Sort by multiple fields: &#x60;sort&#x3D;field1,-field2,field3&#x60;  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| Supported operators are: eq, in. e.g: Filter for single value: &#x60;filter&#x3D;field:eq:value&#x60; Filter for any value in a list: (note \&quot;pipe\&quot; character: &#x60;|&#x60; separating values) &#x60;filter&#x3D;field:in:value1|value2|value3&#x60;  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**|  | [optional] [default to 10] |

### Return type

[**List&lt;SystemInsightsWindowsSecurityProducts&gt;**](SystemInsightsWindowsSecurityProducts.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

