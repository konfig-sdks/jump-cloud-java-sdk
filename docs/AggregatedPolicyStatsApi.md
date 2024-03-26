# AggregatedPolicyStatsApi

All URIs are relative to *https://console.jumpcloud.com/api/v2*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**get**](AggregatedPolicyStatsApi.md#get) | **GET** /systems/{systemObjectId}/aggregated-policy-stats | Get the Aggregated Policy Stats for a System |


<a name="get"></a>
# **get**
> DevicesAggregatedPolicyResultResponse get(systemObjectId).organizationObjectId(organizationObjectId).execute();

Get the Aggregated Policy Stats for a System

Gets the aggregated policy stats for a system.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/systems/{system_object_id}/aggregated-policy-stats \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key:{API_KEY}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AggregatedPolicyStatsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    byte[] systemObjectId = null;
    byte[] organizationObjectId = null;
    try {
      DevicesAggregatedPolicyResultResponse result = client
              .aggregatedPolicyStats
              .get(systemObjectId)
              .organizationObjectId(organizationObjectId)
              .execute();
      System.out.println(result);
      System.out.println(result.getFailedPolicies());
      System.out.println(result.getPendingPolicies());
      System.out.println(result.getPolicyCountData());
    } catch (ApiException e) {
      System.err.println("Exception when calling AggregatedPolicyStatsApi#get");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DevicesAggregatedPolicyResultResponse> response = client
              .aggregatedPolicyStats
              .get(systemObjectId)
              .organizationObjectId(organizationObjectId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AggregatedPolicyStatsApi#get");
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
| **systemObjectId** | **byte[]**|  | |
| **organizationObjectId** | **byte[]**|  | [optional] |

### Return type

[**DevicesAggregatedPolicyResultResponse**](DevicesAggregatedPolicyResultResponse.md)

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

