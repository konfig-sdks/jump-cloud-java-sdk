# FdeApi

All URIs are relative to *https://console.jumpcloud.com/api/v2*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getFDEKey**](FdeApi.md#getFDEKey) | **GET** /systems/{system_id}/fdekey | Get System FDE Key |


<a name="getFDEKey"></a>
# **getFDEKey**
> Systemfdekey getFDEKey(systemId).xOrgId(xOrgId).execute();

Get System FDE Key

This endpoint will return the current (latest) fde key saved for a system.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.FdeApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String systemId = "systemId_example";
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      Systemfdekey result = client
              .fde
              .getFDEKey(systemId)
              .xOrgId(xOrgId)
              .execute();
      System.out.println(result);
      System.out.println(result.getKey());
    } catch (ApiException e) {
      System.err.println("Exception when calling FdeApi#getFDEKey");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Systemfdekey> response = client
              .fde
              .getFDEKey(systemId)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling FdeApi#getFDEKey");
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
| **systemId** | **String**|  | |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |

### Return type

[**Systemfdekey**](Systemfdekey.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

