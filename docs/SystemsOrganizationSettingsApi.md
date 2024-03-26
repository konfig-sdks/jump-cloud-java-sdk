# SystemsOrganizationSettingsApi

All URIs are relative to *https://console.jumpcloud.com/api/v2*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getDefaultPasswordSyncSettings**](SystemsOrganizationSettingsApi.md#getDefaultPasswordSyncSettings) | **GET** /devices/settings/defaultpasswordsync | Get the Default Password Sync Setting |
| [**getSignInWithJumpCloudSettings**](SystemsOrganizationSettingsApi.md#getSignInWithJumpCloudSettings) | **GET** /devices/settings/signinwithjumpcloud | Get the Sign In with JumpCloud Settings |
| [**setDefaultPasswordSyncSettings**](SystemsOrganizationSettingsApi.md#setDefaultPasswordSyncSettings) | **PUT** /devices/settings/defaultpasswordsync | Set the Default Password Sync Setting |
| [**setSignInWithJumpCloudSettings**](SystemsOrganizationSettingsApi.md#setSignInWithJumpCloudSettings) | **PUT** /devices/settings/signinwithjumpcloud | Set the Sign In with JumpCloud Settings |


<a name="getDefaultPasswordSyncSettings"></a>
# **getDefaultPasswordSyncSettings**
> DevicesGetDefaultPasswordSyncSettingsResponse getDefaultPasswordSyncSettings().organizationObjectId(organizationObjectId).execute();

Get the Default Password Sync Setting

Gets the Default Password Sync Setting for an Organization.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemsOrganizationSettingsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    byte[] organizationObjectId = null;
    try {
      DevicesGetDefaultPasswordSyncSettingsResponse result = client
              .systemsOrganizationSettings
              .getDefaultPasswordSyncSettings()
              .organizationObjectId(organizationObjectId)
              .execute();
      System.out.println(result);
      System.out.println(result.getEnabled());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemsOrganizationSettingsApi#getDefaultPasswordSyncSettings");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DevicesGetDefaultPasswordSyncSettingsResponse> response = client
              .systemsOrganizationSettings
              .getDefaultPasswordSyncSettings()
              .organizationObjectId(organizationObjectId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemsOrganizationSettingsApi#getDefaultPasswordSyncSettings");
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
| **organizationObjectId** | **byte[]**|  | [optional] |

### Return type

[**DevicesGetDefaultPasswordSyncSettingsResponse**](DevicesGetDefaultPasswordSyncSettingsResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Setting successfully retrieved. |  -  |
| **0** | An unexpected error response. |  -  |

<a name="getSignInWithJumpCloudSettings"></a>
# **getSignInWithJumpCloudSettings**
> DevicesGetSignInWithJumpCloudSettingsResponse getSignInWithJumpCloudSettings().organizationObjectId(organizationObjectId).execute();

Get the Sign In with JumpCloud Settings

Gets the Sign In with JumpCloud Settings for an Organization.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/devices/settings/signinwithjumpcloud \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key:{API_KEY}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemsOrganizationSettingsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    byte[] organizationObjectId = null;
    try {
      DevicesGetSignInWithJumpCloudSettingsResponse result = client
              .systemsOrganizationSettings
              .getSignInWithJumpCloudSettings()
              .organizationObjectId(organizationObjectId)
              .execute();
      System.out.println(result);
      System.out.println(result.getOrganizationObjectId());
      System.out.println(result.getSettings());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemsOrganizationSettingsApi#getSignInWithJumpCloudSettings");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DevicesGetSignInWithJumpCloudSettingsResponse> response = client
              .systemsOrganizationSettings
              .getSignInWithJumpCloudSettings()
              .organizationObjectId(organizationObjectId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemsOrganizationSettingsApi#getSignInWithJumpCloudSettings");
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
| **organizationObjectId** | **byte[]**|  | [optional] |

### Return type

[**DevicesGetSignInWithJumpCloudSettingsResponse**](DevicesGetSignInWithJumpCloudSettingsResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Settings successfully retrieved. |  -  |
| **0** | An unexpected error response. |  -  |

<a name="setDefaultPasswordSyncSettings"></a>
# **setDefaultPasswordSyncSettings**
> Object setDefaultPasswordSyncSettings(devicesSetDefaultPasswordSyncSettingsRequest).execute();

Set the Default Password Sync Setting

Sets the Default Password Sync Setting for an Organization.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemsOrganizationSettingsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Boolean enabled = true;
    byte[] organizationObjectId = null;
    try {
      Object result = client
              .systemsOrganizationSettings
              .setDefaultPasswordSyncSettings()
              .enabled(enabled)
              .organizationObjectId(organizationObjectId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemsOrganizationSettingsApi#setDefaultPasswordSyncSettings");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Object> response = client
              .systemsOrganizationSettings
              .setDefaultPasswordSyncSettings()
              .enabled(enabled)
              .organizationObjectId(organizationObjectId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemsOrganizationSettingsApi#setDefaultPasswordSyncSettings");
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
| **devicesSetDefaultPasswordSyncSettingsRequest** | [**DevicesSetDefaultPasswordSyncSettingsRequest**](DevicesSetDefaultPasswordSyncSettingsRequest.md)|  | |

### Return type

**Object**

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A successful response. |  -  |
| **204** | Setting successfully changed. |  -  |
| **0** | An unexpected error response. |  -  |

<a name="setSignInWithJumpCloudSettings"></a>
# **setSignInWithJumpCloudSettings**
> Object setSignInWithJumpCloudSettings(devicesSetSignInWithJumpCloudSettingsRequest).execute();

Set the Sign In with JumpCloud Settings

Sets the Sign In with JumpCloud Settings for an Organization.  #### Sample Request &#x60;&#x60;&#x60; curl -X PUT https://console.jumpcloud.com/api/v2/devices/settings/signinwithjumpcloud \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key:{API_KEY}&#39; \\   -d &#39;{\&quot;settings\&quot;:[{\&quot;osFamily\&quot;:\&quot;WINDOWS\&quot;,\&quot;enabled\&quot;:true,\&quot;defaultPermission\&quot;:\&quot;STANDARD\&quot;}]}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemsOrganizationSettingsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    byte[] organizationObjectId = null;
    List<DevicesSignInWithJumpCloudSetting> settings = Arrays.asList();
    try {
      Object result = client
              .systemsOrganizationSettings
              .setSignInWithJumpCloudSettings()
              .organizationObjectId(organizationObjectId)
              .settings(settings)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemsOrganizationSettingsApi#setSignInWithJumpCloudSettings");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Object> response = client
              .systemsOrganizationSettings
              .setSignInWithJumpCloudSettings()
              .organizationObjectId(organizationObjectId)
              .settings(settings)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemsOrganizationSettingsApi#setSignInWithJumpCloudSettings");
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
| **devicesSetSignInWithJumpCloudSettingsRequest** | [**DevicesSetSignInWithJumpCloudSettingsRequest**](DevicesSetSignInWithJumpCloudSettingsRequest.md)|  | |

### Return type

**Object**

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A successful response. |  -  |
| **204** | Settings successfully changed. |  -  |
| **0** | An unexpected error response. |  -  |

