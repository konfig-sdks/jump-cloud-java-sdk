# Office365ImportApi

All URIs are relative to *https://console.jumpcloud.com/api/v2*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**listImportUsers**](Office365ImportApi.md#listImportUsers) | **GET** /office365s/{office365_id}/import/users | Get a list of users to import from an Office 365 instance |


<a name="listImportUsers"></a>
# **listImportUsers**
> Office365SListImportUsersResponse listImportUsers(office365Id).consistencyLevel(consistencyLevel).top(top).skipToken(skipToken).filter(filter).search(search).orderby(orderby).count(count).execute();

Get a list of users to import from an Office 365 instance

Lists Office 365 users available for import.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.Office365ImportApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String office365Id = "office365Id_example";
    String consistencyLevel = "consistencyLevel_example"; // Defines the consistency header for O365 requests. See https://docs.microsoft.com/en-us/graph/api/user-list?view=graph-rest-1.0&tabs=http#request-headers
    Integer top = 56; // Office 365 API maximum number of results per page. See https://docs.microsoft.com/en-us/graph/paging.
    String skipToken = "skipToken_example"; // Office 365 API token used to access the next page of results. See https://docs.microsoft.com/en-us/graph/paging.
    String filter = "filter_example"; // Office 365 API filter parameter. See https://docs.microsoft.com/en-us/graph/api/user-list?view=graph-rest-1.0&tabs=http#optional-query-parameters.
    String search = "search_example"; // Office 365 API search parameter. See https://docs.microsoft.com/en-us/graph/api/user-list?view=graph-rest-1.0&tabs=http#optional-query-parameters.
    String orderby = "orderby_example"; // Office 365 API orderby parameter. See https://docs.microsoft.com/en-us/graph/api/user-list?view=graph-rest-1.0&tabs=http#optional-query-parameters.
    Boolean count = true; // Office 365 API count parameter. See https://docs.microsoft.com/en-us/graph/api/user-list?view=graph-rest-1.0&tabs=http#optional-query-parameters.
    try {
      Office365SListImportUsersResponse result = client
              .office365Import
              .listImportUsers(office365Id)
              .consistencyLevel(consistencyLevel)
              .top(top)
              .skipToken(skipToken)
              .filter(filter)
              .search(search)
              .orderby(orderby)
              .count(count)
              .execute();
      System.out.println(result);
      System.out.println(result.getSkipToken());
      System.out.println(result.getTop());
      System.out.println(result.getUsers());
    } catch (ApiException e) {
      System.err.println("Exception when calling Office365ImportApi#listImportUsers");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Office365SListImportUsersResponse> response = client
              .office365Import
              .listImportUsers(office365Id)
              .consistencyLevel(consistencyLevel)
              .top(top)
              .skipToken(skipToken)
              .filter(filter)
              .search(search)
              .orderby(orderby)
              .count(count)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling Office365ImportApi#listImportUsers");
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
| **office365Id** | **String**|  | |
| **consistencyLevel** | **String**| Defines the consistency header for O365 requests. See https://docs.microsoft.com/en-us/graph/api/user-list?view&#x3D;graph-rest-1.0&amp;tabs&#x3D;http#request-headers | [optional] |
| **top** | **Integer**| Office 365 API maximum number of results per page. See https://docs.microsoft.com/en-us/graph/paging. | [optional] |
| **skipToken** | **String**| Office 365 API token used to access the next page of results. See https://docs.microsoft.com/en-us/graph/paging. | [optional] |
| **filter** | **String**| Office 365 API filter parameter. See https://docs.microsoft.com/en-us/graph/api/user-list?view&#x3D;graph-rest-1.0&amp;tabs&#x3D;http#optional-query-parameters. | [optional] |
| **search** | **String**| Office 365 API search parameter. See https://docs.microsoft.com/en-us/graph/api/user-list?view&#x3D;graph-rest-1.0&amp;tabs&#x3D;http#optional-query-parameters. | [optional] |
| **orderby** | **String**| Office 365 API orderby parameter. See https://docs.microsoft.com/en-us/graph/api/user-list?view&#x3D;graph-rest-1.0&amp;tabs&#x3D;http#optional-query-parameters. | [optional] |
| **count** | **Boolean**| Office 365 API count parameter. See https://docs.microsoft.com/en-us/graph/api/user-list?view&#x3D;graph-rest-1.0&amp;tabs&#x3D;http#optional-query-parameters. | [optional] |

### Return type

[**Office365SListImportUsersResponse**](Office365SListImportUsersResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

