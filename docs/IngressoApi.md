# IngressoApi

All URIs are relative to *https://console.jumpcloud.com/api/v2*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createAccessRequest**](IngressoApi.md#createAccessRequest) | **POST** /accessrequests | Create Access Request |
| [**getAccessRequest**](IngressoApi.md#getAccessRequest) | **GET** /accessrequests/{accessId} | Get all Access Requests by Access Id |
| [**revokeAccessRequest**](IngressoApi.md#revokeAccessRequest) | **POST** /accessrequests/{accessId}/revoke | Revoke access request by id |
| [**updateAccessRequest**](IngressoApi.md#updateAccessRequest) | **PUT** /accessrequests/{accessId} | Update access request by id |


<a name="createAccessRequest"></a>
# **createAccessRequest**
> JumpcloudIngressoCreateAccessRequestsResponse createAccessRequest(jumpcloudIngressoCreateAccessRequestsRequest).execute();

Create Access Request

Endpoint for adding a new access request

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.IngressoApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String operationId = "operationId_example";
    Object additionalAttributes = null;
    String applicationIntId = "applicationIntId_example";
    OffsetDateTime expiry = OffsetDateTime.now();
    byte[] organizationObjectId = null;
    String remarks = "remarks_example";
    String requestorId = "requestorId_example";
    String resourceId = "resourceId_example";
    String resourceType = "resourceType_example";
    try {
      JumpcloudIngressoCreateAccessRequestsResponse result = client
              .ingresso
              .createAccessRequest()
              .operationId(operationId)
              .additionalAttributes(additionalAttributes)
              .applicationIntId(applicationIntId)
              .expiry(expiry)
              .organizationObjectId(organizationObjectId)
              .remarks(remarks)
              .requestorId(requestorId)
              .resourceId(resourceId)
              .resourceType(resourceType)
              .execute();
      System.out.println(result);
      System.out.println(result.getId());
    } catch (ApiException e) {
      System.err.println("Exception when calling IngressoApi#createAccessRequest");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<JumpcloudIngressoCreateAccessRequestsResponse> response = client
              .ingresso
              .createAccessRequest()
              .operationId(operationId)
              .additionalAttributes(additionalAttributes)
              .applicationIntId(applicationIntId)
              .expiry(expiry)
              .organizationObjectId(organizationObjectId)
              .remarks(remarks)
              .requestorId(requestorId)
              .resourceId(resourceId)
              .resourceType(resourceType)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling IngressoApi#createAccessRequest");
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
| **jumpcloudIngressoCreateAccessRequestsRequest** | [**JumpcloudIngressoCreateAccessRequestsRequest**](JumpcloudIngressoCreateAccessRequestsRequest.md)|  | |

### Return type

[**JumpcloudIngressoCreateAccessRequestsResponse**](JumpcloudIngressoCreateAccessRequestsResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A successful response. |  -  |
| **0** | An unexpected error response. |  -  |

<a name="getAccessRequest"></a>
# **getAccessRequest**
> JumpcloudIngressoGetAccessRequestResponse getAccessRequest(accessId).execute();

Get all Access Requests by Access Id

Endpoint for getting all access requests by access id

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.IngressoApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String accessId = "accessId_example";
    try {
      JumpcloudIngressoGetAccessRequestResponse result = client
              .ingresso
              .getAccessRequest(accessId)
              .execute();
      System.out.println(result);
      System.out.println(result.getOperationId());
      System.out.println(result.getVersion());
      System.out.println(result.getAccessId());
      System.out.println(result.getAccessState());
      System.out.println(result.getAdditionalAttributes());
      System.out.println(result.getApplicationIntId());
      System.out.println(result.getCompanyId());
      System.out.println(result.getCreatedBy());
      System.out.println(result.getDuration());
      System.out.println(result.getExpiry());
      System.out.println(result.getId());
      System.out.println(result.getJobId());
      System.out.println(result.getMetadata());
      System.out.println(result.getOnBehalfOfUserId());
      System.out.println(result.getRemarks());
      System.out.println(result.getRequestorId());
      System.out.println(result.getResourceId());
      System.out.println(result.getResourceType());
      System.out.println(result.getTempGroupId());
      System.out.println(result.getUpdatedBy());
    } catch (ApiException e) {
      System.err.println("Exception when calling IngressoApi#getAccessRequest");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<JumpcloudIngressoGetAccessRequestResponse> response = client
              .ingresso
              .getAccessRequest(accessId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling IngressoApi#getAccessRequest");
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
| **accessId** | **String**|  | |

### Return type

[**JumpcloudIngressoGetAccessRequestResponse**](JumpcloudIngressoGetAccessRequestResponse.md)

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

<a name="revokeAccessRequest"></a>
# **revokeAccessRequest**
> Object revokeAccessRequest(accessId).execute();

Revoke access request by id

Endpoint for revoking access request by id

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.IngressoApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String accessId = "accessId_example";
    try {
      Object result = client
              .ingresso
              .revokeAccessRequest(accessId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling IngressoApi#revokeAccessRequest");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Object> response = client
              .ingresso
              .revokeAccessRequest(accessId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling IngressoApi#revokeAccessRequest");
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
| **accessId** | **String**|  | |

### Return type

**Object**

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

<a name="updateAccessRequest"></a>
# **updateAccessRequest**
> Object updateAccessRequest(accessId, accessRequestApiUpdateAccessRequestRequest).execute();

Update access request by id

Endpoint for updating access request by id

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.IngressoApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String accessId = "accessId_example";
    Object additionalAttributes = null;
    OffsetDateTime expiry = OffsetDateTime.now();
    byte[] organizationObjectId = null;
    String remarks = "remarks_example";
    try {
      Object result = client
              .ingresso
              .updateAccessRequest(accessId)
              .additionalAttributes(additionalAttributes)
              .expiry(expiry)
              .organizationObjectId(organizationObjectId)
              .remarks(remarks)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling IngressoApi#updateAccessRequest");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Object> response = client
              .ingresso
              .updateAccessRequest(accessId)
              .additionalAttributes(additionalAttributes)
              .expiry(expiry)
              .organizationObjectId(organizationObjectId)
              .remarks(remarks)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling IngressoApi#updateAccessRequest");
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
| **accessId** | **String**|  | |
| **accessRequestApiUpdateAccessRequestRequest** | [**AccessRequestApiUpdateAccessRequestRequest**](AccessRequestApiUpdateAccessRequestRequest.md)|  | |

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
| **0** | An unexpected error response. |  -  |

