# MicrosoftMdmApi

All URIs are relative to *https://console.jumpcloud.com/api/v2*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**downloadConfigFiles**](MicrosoftMdmApi.md#downloadConfigFiles) | **POST** /microsoft-mdm/configuration-files | This endpoint provides a zipped configuration file intended for use with Microsoft Configuration Designer to create a Provisioning Package (PPKG) for bulk MDM enrollment. |


<a name="downloadConfigFiles"></a>
# **downloadConfigFiles**
> File downloadConfigFiles(body).execute();

This endpoint provides a zipped configuration file intended for use with Microsoft Configuration Designer to create a Provisioning Package (PPKG) for bulk MDM enrollment.

This endpoint allows you to download the config file.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.MicrosoftMdmApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    try {
      File result = client
              .microsoftMdm
              .downloadConfigFiles()
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling MicrosoftMdmApi#downloadConfigFiles");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<File> response = client
              .microsoftMdm
              .downloadConfigFiles()
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling MicrosoftMdmApi#downloadConfigFiles");
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
| **body** | **Object**|  | |

### Return type

[**File**](File.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/zip

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

