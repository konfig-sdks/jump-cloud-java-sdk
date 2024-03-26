# FeatureTrialsApi

All URIs are relative to *https://console.jumpcloud.com/api/v2*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getFeatureTrials**](FeatureTrialsApi.md#getFeatureTrials) | **GET** /featureTrials/{feature_code} | Check current feature trial usage for a specific feature |


<a name="getFeatureTrials"></a>
# **getFeatureTrials**
> FeatureTrialData getFeatureTrials(featureCode).execute();

Check current feature trial usage for a specific feature

This endpoint get&#39;s the current state of a feature trial for an org.  #### Sample Request  &#x60;&#x60;&#x60;   curl -X GET \\   https://console.jumpcloud.local/api/v2/featureTrials/zeroTrust \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.FeatureTrialsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String featureCode = "featureCode_example";
    try {
      FeatureTrialData result = client
              .featureTrials
              .getFeatureTrials(featureCode)
              .execute();
      System.out.println(result);
      System.out.println(result.getEndDate());
      System.out.println(result.getStartDate());
    } catch (ApiException e) {
      System.err.println("Exception when calling FeatureTrialsApi#getFeatureTrials");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<FeatureTrialData> response = client
              .featureTrials
              .getFeatureTrials(featureCode)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling FeatureTrialsApi#getFeatureTrials");
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
| **featureCode** | **String**|  | |

### Return type

[**FeatureTrialData**](FeatureTrialData.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

