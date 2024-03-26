# ScimImportApi

All URIs are relative to *https://console.jumpcloud.com/api/v2*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**users**](ScimImportApi.md#users) | **GET** /applications/{application_id}/import/users | Get a list of users to import from an Application IdM service provider |


<a name="users"></a>
# **users**
> ImportUsersResponse users(applicationId).filter(filter).query(query).sort(sort).sortOrder(sortOrder).xOrgId(xOrgId).limit(limit).skip(skip).execute();

Get a list of users to import from an Application IdM service provider

Get a list of users to import from an Application IdM service provider.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ScimImportApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String applicationId = "applicationId_example"; // ObjectID of the Application.
    String filter = ""; // Filter users by a search term
    String query = ""; // URL query to merge with the service provider request
    String sort = ""; // Sort users by supported fields
    String sortOrder = "asc";
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    Integer skip = 0; // The offset into the records to return.
    try {
      ImportUsersResponse result = client
              .scimImport
              .users(applicationId)
              .filter(filter)
              .query(query)
              .sort(sort)
              .sortOrder(sortOrder)
              .xOrgId(xOrgId)
              .limit(limit)
              .skip(skip)
              .execute();
      System.out.println(result);
      System.out.println(result.getTotalCount());
      System.out.println(result.getUsers());
    } catch (ApiException e) {
      System.err.println("Exception when calling ScimImportApi#users");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ImportUsersResponse> response = client
              .scimImport
              .users(applicationId)
              .filter(filter)
              .query(query)
              .sort(sort)
              .sortOrder(sortOrder)
              .xOrgId(xOrgId)
              .limit(limit)
              .skip(skip)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ScimImportApi#users");
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
| **applicationId** | **String**| ObjectID of the Application. | |
| **filter** | **String**| Filter users by a search term | [optional] [default to ] |
| **query** | **String**| URL query to merge with the service provider request | [optional] [default to ] |
| **sort** | **String**| Sort users by supported fields | [optional] [default to ] [enum: , firstname, lastname, email] |
| **sortOrder** | **String**|  | [optional] [default to asc] [enum: asc, desc] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |

### Return type

[**ImportUsersResponse**](ImportUsersResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

