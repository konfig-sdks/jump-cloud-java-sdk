# AdministratorsApi

All URIs are relative to *https://console.jumpcloud.com/api/v2*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createByAdministrator**](AdministratorsApi.md#createByAdministrator) | **POST** /administrators/{id}/organizationlinks | Allow Adminstrator access to an Organization. |
| [**listByAdministrator**](AdministratorsApi.md#listByAdministrator) | **GET** /administrators/{id}/organizationlinks | List the association links between an Administrator and Organizations. |
| [**listByOrganization**](AdministratorsApi.md#listByOrganization) | **GET** /organizations/{id}/administratorlinks | List the association links between an Organization and Administrators. |
| [**removeByAdministrator**](AdministratorsApi.md#removeByAdministrator) | **DELETE** /administrators/{administrator_id}/organizationlinks/{id} | Remove association between an Administrator and an Organization. |


<a name="createByAdministrator"></a>
# **createByAdministrator**
> AdministratorOrganizationLink createByAdministrator(id).administratorOrganizationLinkReq(administratorOrganizationLinkReq).execute();

Allow Adminstrator access to an Organization.

This endpoint allows you to grant Administrator access to an Organization.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AdministratorsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String id = "id_example";
    String organization = "organization_example"; // The identifier for an organization to link this administrator to.
    try {
      AdministratorOrganizationLink result = client
              .administrators
              .createByAdministrator(id)
              .organization(organization)
              .execute();
      System.out.println(result);
      System.out.println(result.getAdministrator());
      System.out.println(result.getOrganization());
    } catch (ApiException e) {
      System.err.println("Exception when calling AdministratorsApi#createByAdministrator");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AdministratorOrganizationLink> response = client
              .administrators
              .createByAdministrator(id)
              .organization(organization)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AdministratorsApi#createByAdministrator");
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
| **id** | **String**|  | |
| **administratorOrganizationLinkReq** | [**AdministratorOrganizationLinkReq**](AdministratorOrganizationLinkReq.md)|  | [optional] |

### Return type

[**AdministratorOrganizationLink**](AdministratorOrganizationLink.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | OK |  -  |

<a name="listByAdministrator"></a>
# **listByAdministrator**
> List&lt;AdministratorOrganizationLink&gt; listByAdministrator(id).limit(limit).skip(skip).execute();

List the association links between an Administrator and Organizations.

This endpoint returns the association links between an Administrator and Organizations.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AdministratorsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String id = "id_example";
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    Integer skip = 0; // The offset into the records to return.
    try {
      List<AdministratorOrganizationLink> result = client
              .administrators
              .listByAdministrator(id)
              .limit(limit)
              .skip(skip)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling AdministratorsApi#listByAdministrator");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<AdministratorOrganizationLink>> response = client
              .administrators
              .listByAdministrator(id)
              .limit(limit)
              .skip(skip)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AdministratorsApi#listByAdministrator");
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
| **id** | **String**|  | |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |

### Return type

[**List&lt;AdministratorOrganizationLink&gt;**](AdministratorOrganizationLink.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="listByOrganization"></a>
# **listByOrganization**
> List&lt;AdministratorOrganizationLink&gt; listByOrganization(id).limit(limit).skip(skip).execute();

List the association links between an Organization and Administrators.

This endpoint returns the association links between an Organization and Administrators.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AdministratorsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String id = "id_example";
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    Integer skip = 0; // The offset into the records to return.
    try {
      List<AdministratorOrganizationLink> result = client
              .administrators
              .listByOrganization(id)
              .limit(limit)
              .skip(skip)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling AdministratorsApi#listByOrganization");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<AdministratorOrganizationLink>> response = client
              .administrators
              .listByOrganization(id)
              .limit(limit)
              .skip(skip)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AdministratorsApi#listByOrganization");
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
| **id** | **String**|  | |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |

### Return type

[**List&lt;AdministratorOrganizationLink&gt;**](AdministratorOrganizationLink.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="removeByAdministrator"></a>
# **removeByAdministrator**
> removeByAdministrator(administratorId, id).execute();

Remove association between an Administrator and an Organization.

This endpoint removes the association link between an Administrator and an Organization.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AdministratorsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String administratorId = "administratorId_example";
    String id = "id_example";
    try {
      client
              .administrators
              .removeByAdministrator(administratorId, id)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling AdministratorsApi#removeByAdministrator");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .administrators
              .removeByAdministrator(administratorId, id)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling AdministratorsApi#removeByAdministrator");
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
| **administratorId** | **String**|  | |
| **id** | **String**|  | |

### Return type

null (empty response body)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **204** | No Content |  -  |

