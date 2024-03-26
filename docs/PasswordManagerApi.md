# PasswordManagerApi

All URIs are relative to *https://console.jumpcloud.com/api/v2*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getDevice**](PasswordManagerApi.md#getDevice) | **GET** /passwordmanager/devices/{UUID} |  |
| [**listDevices**](PasswordManagerApi.md#listDevices) | **GET** /passwordmanager/devices |  |


<a name="getDevice"></a>
# **getDevice**
> DevicePackageV1Device getDevice(UUID).execute();



Get Device

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.PasswordManagerApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    try {
      DevicePackageV1Device result = client
              .passwordManager
              .getDevice(UUID)
              .execute();
      System.out.println(result);
      System.out.println(result.getAppVersion());
      System.out.println(result.getCreatedAt());
      System.out.println(result.getId());
      System.out.println(result.getName());
      System.out.println(result.getOsName());
      System.out.println(result.getPublicKey());
      System.out.println(result.getUpdatedAt());
      System.out.println(result.getUserUuid());
    } catch (ApiException e) {
      System.err.println("Exception when calling PasswordManagerApi#getDevice");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DevicePackageV1Device> response = client
              .passwordManager
              .getDevice(UUID)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling PasswordManagerApi#getDevice");
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
| **UUID** | **String**|  | |

### Return type

[**DevicePackageV1Device**](DevicePackageV1Device.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A successful response. |  -  |
| **0** | An unexpected error response. |  -  |

<a name="listDevices"></a>
# **listDevices**
> DevicePackageV1ListDevicesResponse listDevices().limit(limit).skip(skip).sort(sort).fields(fields).filter(filter).execute();



List Devices

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.PasswordManagerApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Integer limit = 56;
    Integer skip = 56;
    String sort = "sort_example";
    List<String> fields = Arrays.asList();
    List<String> filter = Arrays.asList();
    try {
      DevicePackageV1ListDevicesResponse result = client
              .passwordManager
              .listDevices()
              .limit(limit)
              .skip(skip)
              .sort(sort)
              .fields(fields)
              .filter(filter)
              .execute();
      System.out.println(result);
      System.out.println(result.getResults());
      System.out.println(result.getTotalCount());
    } catch (ApiException e) {
      System.err.println("Exception when calling PasswordManagerApi#listDevices");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DevicePackageV1ListDevicesResponse> response = client
              .passwordManager
              .listDevices()
              .limit(limit)
              .skip(skip)
              .sort(sort)
              .fields(fields)
              .filter(filter)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling PasswordManagerApi#listDevices");
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
| **limit** | **Integer**|  | [optional] |
| **skip** | **Integer**|  | [optional] |
| **sort** | **String**|  | [optional] |
| **fields** | [**List&lt;String&gt;**](String.md)|  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)|  | [optional] |

### Return type

[**DevicePackageV1ListDevicesResponse**](DevicePackageV1ListDevicesResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A successful response. |  -  |
| **0** | An unexpected error response. |  -  |

