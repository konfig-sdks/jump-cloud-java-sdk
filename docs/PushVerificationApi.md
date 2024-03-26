# PushVerificationApi

All URIs are relative to *https://console.jumpcloud.com/api/v2*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**get**](PushVerificationApi.md#get) | **GET** /pushendpoints/verifications/{verificationId} | Get Push Verification status |
| [**start**](PushVerificationApi.md#start) | **POST** /users/{userId}/pushendpoints/{pushEndpointId}/verify | Send Push Verification message |


<a name="get"></a>
# **get**
> JumpcloudAuthPushVerification get(verificationId).execute();

Get Push Verification status

Endpoint for retrieving a verification push notification status

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.PushVerificationApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String verificationId = "verificationId_example";
    try {
      JumpcloudAuthPushVerification result = client
              .pushVerification
              .get(verificationId)
              .execute();
      System.out.println(result);
      System.out.println(result.getConfirmationCode());
      System.out.println(result.getExpiresAt());
      System.out.println(result.getFailureStatus());
      System.out.println(result.getId());
      System.out.println(result.getInitiatedAt());
      System.out.println(result.getPushEndpointId());
      System.out.println(result.getStatus());
      System.out.println(result.getUserObjectId());
    } catch (ApiException e) {
      System.err.println("Exception when calling PushVerificationApi#get");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<JumpcloudAuthPushVerification> response = client
              .pushVerification
              .get(verificationId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling PushVerificationApi#get");
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
| **verificationId** | **String**|  | |

### Return type

[**JumpcloudAuthPushVerification**](JumpcloudAuthPushVerification.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |
| **0** | An unexpected error response. |  -  |

<a name="start"></a>
# **start**
> JumpcloudAuthPushVerification start(userId, pushEndpointId, pushVerificationsStartRequest).execute();

Send Push Verification message

Endpoint for sending a verification push notification

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.PushVerificationApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    byte[] userId = null;
    byte[] pushEndpointId = null;
    String message = "message_example";
    try {
      JumpcloudAuthPushVerification result = client
              .pushVerification
              .start(userId, pushEndpointId)
              .message(message)
              .execute();
      System.out.println(result);
      System.out.println(result.getConfirmationCode());
      System.out.println(result.getExpiresAt());
      System.out.println(result.getFailureStatus());
      System.out.println(result.getId());
      System.out.println(result.getInitiatedAt());
      System.out.println(result.getPushEndpointId());
      System.out.println(result.getStatus());
      System.out.println(result.getUserObjectId());
    } catch (ApiException e) {
      System.err.println("Exception when calling PushVerificationApi#start");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<JumpcloudAuthPushVerification> response = client
              .pushVerification
              .start(userId, pushEndpointId)
              .message(message)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling PushVerificationApi#start");
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
| **userId** | **byte[]**|  | |
| **pushEndpointId** | **byte[]**|  | |
| **pushVerificationsStartRequest** | [**PushVerificationsStartRequest**](PushVerificationsStartRequest.md)|  | |

### Return type

[**JumpcloudAuthPushVerification**](JumpcloudAuthPushVerification.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |
| **0** | An unexpected error response. |  -  |

