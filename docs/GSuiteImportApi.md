# GSuiteImportApi

All URIs are relative to *https://console.jumpcloud.com/api/v2*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**listImportJumpcloudUsers**](GSuiteImportApi.md#listImportJumpcloudUsers) | **GET** /gsuites/{gsuite_id}/import/jumpcloudusers | Get a list of users in Jumpcloud format to import from a Google Workspace account. |
| [**listImportUsers**](GSuiteImportApi.md#listImportUsers) | **GET** /gsuites/{gsuite_id}/import/users | Get a list of users to import from a G Suite instance |


<a name="listImportJumpcloudUsers"></a>
# **listImportJumpcloudUsers**
> GsuitesListImportJumpcloudUsersResponse listImportJumpcloudUsers(gsuiteId).maxResults(maxResults).orderBy(orderBy).pageToken(pageToken).query(query).sortOrder(sortOrder).execute();

Get a list of users in Jumpcloud format to import from a Google Workspace account.

Lists available G Suite users for import, translated to the Jumpcloud user schema.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.GSuiteImportApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String gsuiteId = "gsuiteId_example";
    Integer maxResults = 56; // Google Directory API maximum number of results per page. See https://developers.google.com/admin-sdk/directory/reference/rest/v1/users/list.
    String orderBy = "orderBy_example"; // Google Directory API sort field parameter. See https://developers.google.com/admin-sdk/directory/reference/rest/v1/users/list.
    String pageToken = "pageToken_example"; // Google Directory API token used to access the next page of results. See https://developers.google.com/admin-sdk/directory/reference/rest/v1/users/list.
    String query = "query_example"; // Google Directory API search parameter. See https://developers.google.com/admin-sdk/directory/v1/guides/search-users.
    String sortOrder = "sortOrder_example"; // Google Directory API sort direction parameter. See https://developers.google.com/admin-sdk/directory/reference/rest/v1/users/list.
    try {
      GsuitesListImportJumpcloudUsersResponse result = client
              .gSuiteImport
              .listImportJumpcloudUsers(gsuiteId)
              .maxResults(maxResults)
              .orderBy(orderBy)
              .pageToken(pageToken)
              .query(query)
              .sortOrder(sortOrder)
              .execute();
      System.out.println(result);
      System.out.println(result.getNextPageToken());
      System.out.println(result.getUsers());
    } catch (ApiException e) {
      System.err.println("Exception when calling GSuiteImportApi#listImportJumpcloudUsers");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<GsuitesListImportJumpcloudUsersResponse> response = client
              .gSuiteImport
              .listImportJumpcloudUsers(gsuiteId)
              .maxResults(maxResults)
              .orderBy(orderBy)
              .pageToken(pageToken)
              .query(query)
              .sortOrder(sortOrder)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling GSuiteImportApi#listImportJumpcloudUsers");
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
| **gsuiteId** | **String**|  | |
| **maxResults** | **Integer**| Google Directory API maximum number of results per page. See https://developers.google.com/admin-sdk/directory/reference/rest/v1/users/list. | [optional] |
| **orderBy** | **String**| Google Directory API sort field parameter. See https://developers.google.com/admin-sdk/directory/reference/rest/v1/users/list. | [optional] |
| **pageToken** | **String**| Google Directory API token used to access the next page of results. See https://developers.google.com/admin-sdk/directory/reference/rest/v1/users/list. | [optional] |
| **query** | **String**| Google Directory API search parameter. See https://developers.google.com/admin-sdk/directory/v1/guides/search-users. | [optional] |
| **sortOrder** | **String**| Google Directory API sort direction parameter. See https://developers.google.com/admin-sdk/directory/reference/rest/v1/users/list. | [optional] |

### Return type

[**GsuitesListImportJumpcloudUsersResponse**](GsuitesListImportJumpcloudUsersResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="listImportUsers"></a>
# **listImportUsers**
> GsuitesListImportUsersResponse listImportUsers(gsuiteId).limit(limit).maxResults(maxResults).orderBy(orderBy).pageToken(pageToken).query(query).sortOrder(sortOrder).execute();

Get a list of users to import from a G Suite instance

Lists G Suite users available for import.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.GSuiteImportApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String gsuiteId = "gsuiteId_example";
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    Integer maxResults = 56; // Google Directory API maximum number of results per page. See https://developers.google.com/admin-sdk/directory/reference/rest/v1/users/list.
    String orderBy = "orderBy_example"; // Google Directory API sort field parameter. See https://developers.google.com/admin-sdk/directory/reference/rest/v1/users/list.
    String pageToken = "pageToken_example"; // Google Directory API token used to access the next page of results. See https://developers.google.com/admin-sdk/directory/reference/rest/v1/users/list.
    String query = "query_example"; // Google Directory API search parameter. See https://developers.google.com/admin-sdk/directory/v1/guides/search-users.
    String sortOrder = "sortOrder_example"; // Google Directory API sort direction parameter. See https://developers.google.com/admin-sdk/directory/reference/rest/v1/users/list.
    try {
      GsuitesListImportUsersResponse result = client
              .gSuiteImport
              .listImportUsers(gsuiteId)
              .limit(limit)
              .maxResults(maxResults)
              .orderBy(orderBy)
              .pageToken(pageToken)
              .query(query)
              .sortOrder(sortOrder)
              .execute();
      System.out.println(result);
      System.out.println(result.getNextPageToken());
      System.out.println(result.getUsers());
    } catch (ApiException e) {
      System.err.println("Exception when calling GSuiteImportApi#listImportUsers");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<GsuitesListImportUsersResponse> response = client
              .gSuiteImport
              .listImportUsers(gsuiteId)
              .limit(limit)
              .maxResults(maxResults)
              .orderBy(orderBy)
              .pageToken(pageToken)
              .query(query)
              .sortOrder(sortOrder)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling GSuiteImportApi#listImportUsers");
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
| **gsuiteId** | **String**|  | |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **maxResults** | **Integer**| Google Directory API maximum number of results per page. See https://developers.google.com/admin-sdk/directory/reference/rest/v1/users/list. | [optional] |
| **orderBy** | **String**| Google Directory API sort field parameter. See https://developers.google.com/admin-sdk/directory/reference/rest/v1/users/list. | [optional] |
| **pageToken** | **String**| Google Directory API token used to access the next page of results. See https://developers.google.com/admin-sdk/directory/reference/rest/v1/users/list. | [optional] |
| **query** | **String**| Google Directory API search parameter. See https://developers.google.com/admin-sdk/directory/v1/guides/search-users. | [optional] |
| **sortOrder** | **String**| Google Directory API sort direction parameter. See https://developers.google.com/admin-sdk/directory/reference/rest/v1/users/list. | [optional] |

### Return type

[**GsuitesListImportUsersResponse**](GsuitesListImportUsersResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

