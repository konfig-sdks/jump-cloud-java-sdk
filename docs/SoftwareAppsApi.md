# SoftwareAppsApi

All URIs are relative to *https://console.jumpcloud.com/api/v2*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**delete**](SoftwareAppsApi.md#delete) | **DELETE** /softwareapps/{id} | Delete a configured Software Application |
| [**get**](SoftwareAppsApi.md#get) | **GET** /softwareapps/{id} | Retrieve a configured Software Application. |
| [**list**](SoftwareAppsApi.md#list) | **GET** /softwareapps | Get all configured Software Applications. |
| [**list_0**](SoftwareAppsApi.md#list_0) | **GET** /softwareapps/{software_app_id}/statuses | Get the status of the provided Software Application |
| [**post**](SoftwareAppsApi.md#post) | **POST** /softwareapps | Create a Software Application that will be managed by JumpCloud. |
| [**reclaimLicenses**](SoftwareAppsApi.md#reclaimLicenses) | **POST** /softwareapps/{software_app_id}/reclaim-licenses | Reclaim Licenses for a Software Application. |
| [**retryInstallation**](SoftwareAppsApi.md#retryInstallation) | **POST** /softwareapps/{software_app_id}/retry-installation | Retry Installation for a Software Application |
| [**softwareappsAssociationsList**](SoftwareAppsApi.md#softwareappsAssociationsList) | **GET** /softwareapps/{software_app_id}/associations | List the associations of a Software Application |
| [**softwareappsAssociationsPost**](SoftwareAppsApi.md#softwareappsAssociationsPost) | **POST** /softwareapps/{software_app_id}/associations | Manage the associations of a software application. |
| [**softwareappsTraverseSystem**](SoftwareAppsApi.md#softwareappsTraverseSystem) | **GET** /softwareapps/{software_app_id}/systems | List the Systems bound to a Software App. |
| [**softwareappsTraverseSystemGroup**](SoftwareAppsApi.md#softwareappsTraverseSystemGroup) | **GET** /softwareapps/{software_app_id}/systemgroups | List the System Groups bound to a Software App. |
| [**update**](SoftwareAppsApi.md#update) | **PUT** /softwareapps/{id} | Update a Software Application Configuration. |
| [**validateApplicationInstallPackage**](SoftwareAppsApi.md#validateApplicationInstallPackage) | **POST** /softwareapps/validate | Validate Installation Packages |


<a name="delete"></a>
# **delete**
> delete(id).xOrgId(xOrgId).execute();

Delete a configured Software Application

Removes a Software Application configuration.  Warning: This is a destructive operation and will unmanage the application on all affected systems.  #### Sample Request &#x60;&#x60;&#x60; curl -X DELETE https://console.jumpcloud.com/api/v2/softwareapps/{id} \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SoftwareAppsApi;
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
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      client
              .softwareApps
              .delete(id)
              .xOrgId(xOrgId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling SoftwareAppsApi#delete");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .softwareApps
              .delete(id)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling SoftwareAppsApi#delete");
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
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |

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
| **204** |  |  -  |

<a name="get"></a>
# **get**
> SoftwareApp get(id).xOrgId(xOrgId).execute();

Retrieve a configured Software Application.

Retrieves a Software Application. The optional isConfigEnabled and appConfiguration apple_vpp attributes are populated in this response.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/softwareapps/{id} \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SoftwareAppsApi;
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
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      SoftwareApp result = client
              .softwareApps
              .get(id)
              .xOrgId(xOrgId)
              .execute();
      System.out.println(result);
      System.out.println(result.getDisplayName());
      System.out.println(result.getId());
      System.out.println(result.getSettings());
    } catch (ApiException e) {
      System.err.println("Exception when calling SoftwareAppsApi#get");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<SoftwareApp> response = client
              .softwareApps
              .get(id)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SoftwareAppsApi#get");
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
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |

### Return type

[**SoftwareApp**](SoftwareApp.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="list"></a>
# **list**
> List&lt;SoftwareApp&gt; list().xOrgId(xOrgId).filter(filter).limit(limit).skip(skip).sort(sort).execute();

Get all configured Software Applications.

This endpoint allows you to get all configured Software Applications that will be managed by JumpCloud on associated JumpCloud systems. The optional isConfigEnabled and appConfiguration apple_vpp attributes are not included in the response.  #### Sample Request &#x60;&#x60;&#x60; $ curl -X GET https://console.jumpcloud.com/api/v2/softwareapps \\ -H &#39;Accept: application/json&#39; \\ -H &#39;Content-Type: application/json&#39; \\ -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SoftwareAppsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. 
    try {
      List<SoftwareApp> result = client
              .softwareApps
              .list()
              .xOrgId(xOrgId)
              .filter(filter)
              .limit(limit)
              .skip(skip)
              .sort(sort)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SoftwareAppsApi#list");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SoftwareApp>> response = client
              .softwareApps
              .list()
              .xOrgId(xOrgId)
              .filter(filter)
              .limit(limit)
              .skip(skip)
              .sort(sort)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SoftwareAppsApi#list");
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
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; | [optional] |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  | [optional] |

### Return type

[**List&lt;SoftwareApp&gt;**](SoftwareApp.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="list_0"></a>
# **list_0**
> List&lt;SoftwareAppStatus&gt; list_0(softwareAppId).xOrgId(xOrgId).filter(filter).limit(limit).skip(skip).sort(sort).execute();

Get the status of the provided Software Application

This endpoint allows you to get the status of the provided Software Application on associated JumpCloud systems.  #### Sample Request &#x60;&#x60;&#x60; $ curl -X GET https://console.jumpcloud.com/api/v2/softwareapps/{software_app_id}/statuses \\ -H &#39;Accept: application/json&#39; \\ -H &#39;Content-Type: application/json&#39; \\ -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SoftwareAppsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String softwareAppId = "softwareAppId_example"; // ObjectID of the Software App.
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. 
    try {
      List<SoftwareAppStatus> result = client
              .softwareApps
              .list_0(softwareAppId)
              .xOrgId(xOrgId)
              .filter(filter)
              .limit(limit)
              .skip(skip)
              .sort(sort)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SoftwareAppsApi#list_0");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SoftwareAppStatus>> response = client
              .softwareApps
              .list_0(softwareAppId)
              .xOrgId(xOrgId)
              .filter(filter)
              .limit(limit)
              .skip(skip)
              .sort(sort)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SoftwareAppsApi#list_0");
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
| **softwareAppId** | **String**| ObjectID of the Software App. | |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; | [optional] |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  | [optional] |

### Return type

[**List&lt;SoftwareAppStatus&gt;**](SoftwareAppStatus.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="post"></a>
# **post**
> SoftwareAppCreate post().xOrgId(xOrgId).softwareApp(softwareApp).execute();

Create a Software Application that will be managed by JumpCloud.

This endpoint allows you to create a Software Application that will be managed by JumpCloud on associated JumpCloud systems. The optional isConfigEnabled and appConfiguration apple_vpp attributes are not included in the response.  #### Sample Request &#x60;&#x60;&#x60; $ curl -X POST https://console.jumpcloud.com/api/v2/softwareapps \\ -H &#39;Accept: application/json&#39; \\ -H &#39;Content-Type: application/json&#39; \\ -H &#39;x-api-key: {API_KEY}&#39; \\ -d &#39;{   \&quot;displayName\&quot;: \&quot;Adobe Reader\&quot;,   \&quot;settings\&quot;: [{\&quot;packageId\&quot;: \&quot;adobereader\&quot;}] }&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SoftwareAppsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String displayName = "displayName_example";
    String id = "id_example";
    List<SoftwareAppSettings> settings = Arrays.asList();
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      SoftwareAppCreate result = client
              .softwareApps
              .post()
              .displayName(displayName)
              .id(id)
              .settings(settings)
              .xOrgId(xOrgId)
              .execute();
      System.out.println(result);
      System.out.println(result.getDisplayName());
      System.out.println(result.getId());
      System.out.println(result.getSettings());
      System.out.println(result.getUploadUrl());
    } catch (ApiException e) {
      System.err.println("Exception when calling SoftwareAppsApi#post");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<SoftwareAppCreate> response = client
              .softwareApps
              .post()
              .displayName(displayName)
              .id(id)
              .settings(settings)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SoftwareAppsApi#post");
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
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **softwareApp** | [**SoftwareApp**](SoftwareApp.md)|  | [optional] |

### Return type

[**SoftwareAppCreate**](SoftwareAppCreate.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Created |  -  |

<a name="reclaimLicenses"></a>
# **reclaimLicenses**
> SoftwareAppReclaimLicenses reclaimLicenses(softwareAppId).execute();

Reclaim Licenses for a Software Application.

This endpoint allows you to reclaim the licenses from a software app associated with devices that are deleted. #### Sample Request &#x60;&#x60;&#x60; $ curl -X POST https://console.jumpcloud.com/api/v2/softwareapps/{software_app_id}/reclaim-licenses \\ -H &#39;Accept: application/json&#39; \\ -H &#39;Content-Type: application/json&#39; \\ -H &#39;x-api-key: {API_KEY}&#39; \\ -d &#39;{}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SoftwareAppsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String softwareAppId = "softwareAppId_example";
    try {
      SoftwareAppReclaimLicenses result = client
              .softwareApps
              .reclaimLicenses(softwareAppId)
              .execute();
      System.out.println(result);
      System.out.println(result.getAssignedLicenses());
      System.out.println(result.getAvailableLicenses());
      System.out.println(result.getReclaimedLicenses());
      System.out.println(result.getTotalLicenses());
    } catch (ApiException e) {
      System.err.println("Exception when calling SoftwareAppsApi#reclaimLicenses");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<SoftwareAppReclaimLicenses> response = client
              .softwareApps
              .reclaimLicenses(softwareAppId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SoftwareAppsApi#reclaimLicenses");
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
| **softwareAppId** | **String**|  | |

### Return type

[**SoftwareAppReclaimLicenses**](SoftwareAppReclaimLicenses.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Reclaim Licenses Response |  -  |

<a name="retryInstallation"></a>
# **retryInstallation**
> retryInstallation(softwareAppId).execute();

Retry Installation for a Software Application

This endpoints initiates an installation retry of an Apple VPP App for the provided system IDs #### Sample Request &#x60;&#x60;&#x60; $ curl -X POST https://console.jumpcloud.com/api/v2/softwareapps/{software_app_id}/retry-installation \\ -H &#39;Accept: application/json&#39; \\ -H &#39;Content-Type: application/json&#39; \\ -H &#39;x-api-key: {API_KEY}&#39; \\ -d &#39;{\&quot;system_ids\&quot;: \&quot;{&lt;system_id_1&gt;, &lt;system_id_2&gt;, ...}\&quot;}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SoftwareAppsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String softwareAppId = "softwareAppId_example";
    try {
      client
              .softwareApps
              .retryInstallation(softwareAppId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling SoftwareAppsApi#retryInstallation");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .softwareApps
              .retryInstallation(softwareAppId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling SoftwareAppsApi#retryInstallation");
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
| **softwareAppId** | **String**|  | |

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

<a name="softwareappsAssociationsList"></a>
# **softwareappsAssociationsList**
> List&lt;GraphConnection&gt; softwareappsAssociationsList(softwareAppId, targets).limit(limit).skip(skip).xOrgId(xOrgId).execute();

List the associations of a Software Application

This endpoint will return the _direct_ associations of a Software Application. A direct association can be a non-homogeneous relationship between 2 different objects, for example Software Application and System Groups.   #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/softwareapps/{software_app_id}/associations?targets&#x3D;system_group \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SoftwareAppsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String softwareAppId = "softwareAppId_example"; // ObjectID of the Software App.
    List<String> targets = Arrays.asList(); // Targets which a \"software_app\" can be associated to.
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    Integer skip = 0; // The offset into the records to return.
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      List<GraphConnection> result = client
              .softwareApps
              .softwareappsAssociationsList(softwareAppId, targets)
              .limit(limit)
              .skip(skip)
              .xOrgId(xOrgId)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SoftwareAppsApi#softwareappsAssociationsList");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<GraphConnection>> response = client
              .softwareApps
              .softwareappsAssociationsList(softwareAppId, targets)
              .limit(limit)
              .skip(skip)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SoftwareAppsApi#softwareappsAssociationsList");
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
| **softwareAppId** | **String**| ObjectID of the Software App. | |
| **targets** | [**List&lt;String&gt;**](String.md)| Targets which a \&quot;software_app\&quot; can be associated to. | [enum: system, system_group] |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |

### Return type

[**List&lt;GraphConnection&gt;**](GraphConnection.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="softwareappsAssociationsPost"></a>
# **softwareappsAssociationsPost**
> softwareappsAssociationsPost(softwareAppId).xOrgId(xOrgId).graphOperationSoftwareApp(graphOperationSoftwareApp).execute();

Manage the associations of a software application.

This endpoint allows you to associate or disassociate a software application to a system or system group.  #### Sample Request &#x60;&#x60;&#x60; $ curl -X POST https://console.jumpcloud.com/api/v2/softwareapps/{software_app_id}/associations \\ -H &#39;Accept: application/json&#39; \\ -H &#39;Content-Type: application/json&#39; \\ -H &#39;x-api-key: {API_KEY}&#39; \\ -d &#39;{   \&quot;id\&quot;: \&quot;&lt;object_id&gt;\&quot;,   \&quot;op\&quot;: \&quot;add\&quot;,   \&quot;type\&quot;: \&quot;system\&quot;  }&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SoftwareAppsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String softwareAppId = "softwareAppId_example"; // ObjectID of the Software App.
    String id = "id_example"; // The ObjectID of graph object being added or removed as an association.
    String op = "add"; // How to modify the graph connection.
    Map<String, Object> attributes = new HashMap(); // The graph attributes.
    String type = "system"; // Targets which a \\\"software_app\\\" can be associated to.
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      client
              .softwareApps
              .softwareappsAssociationsPost(softwareAppId)
              .id(id)
              .op(op)
              .attributes(attributes)
              .type(type)
              .xOrgId(xOrgId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling SoftwareAppsApi#softwareappsAssociationsPost");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .softwareApps
              .softwareappsAssociationsPost(softwareAppId)
              .id(id)
              .op(op)
              .attributes(attributes)
              .type(type)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling SoftwareAppsApi#softwareappsAssociationsPost");
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
| **softwareAppId** | **String**| ObjectID of the Software App. | |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **graphOperationSoftwareApp** | [**GraphOperationSoftwareApp**](GraphOperationSoftwareApp.md)|  | [optional] |

### Return type

null (empty response body)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **204** |  |  -  |

<a name="softwareappsTraverseSystem"></a>
# **softwareappsTraverseSystem**
> List&lt;GraphObjectWithPaths&gt; softwareappsTraverseSystem(softwareAppId).limit(limit).xOrgId(xOrgId).skip(skip).filter(filter).execute();

List the Systems bound to a Software App.

This endpoint will return all Systems bound to a Software App, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this Software App to the corresponding System; this array represents all grouping and/or associations that would have to be removed to deprovision the System from this Software App.  See &#x60;/associations&#x60; endpoint to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/softwareapps/{software_app_id}/systems \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SoftwareAppsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String softwareAppId = "softwareAppId_example"; // ObjectID of the Software App.
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer skip = 0; // The offset into the records to return.
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    try {
      List<GraphObjectWithPaths> result = client
              .softwareApps
              .softwareappsTraverseSystem(softwareAppId)
              .limit(limit)
              .xOrgId(xOrgId)
              .skip(skip)
              .filter(filter)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SoftwareAppsApi#softwareappsTraverseSystem");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<GraphObjectWithPaths>> response = client
              .softwareApps
              .softwareappsTraverseSystem(softwareAppId)
              .limit(limit)
              .xOrgId(xOrgId)
              .skip(skip)
              .filter(filter)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SoftwareAppsApi#softwareappsTraverseSystem");
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
| **softwareAppId** | **String**| ObjectID of the Software App. | |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **filter** | [**List&lt;String&gt;**](String.md)| A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; | [optional] |

### Return type

[**List&lt;GraphObjectWithPaths&gt;**](GraphObjectWithPaths.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="softwareappsTraverseSystemGroup"></a>
# **softwareappsTraverseSystemGroup**
> List&lt;GraphObjectWithPaths&gt; softwareappsTraverseSystemGroup(softwareAppId).limit(limit).xOrgId(xOrgId).skip(skip).filter(filter).execute();

List the System Groups bound to a Software App.

This endpoint will return all Systems Groups bound to a Software App, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the group&#39;s type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this Software App to the corresponding System Group; this array represents all grouping and/or associations that would have to be removed to deprovision the System Group from this Software App.  See &#x60;/associations&#x60; endpoint to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET  https://console.jumpcloud.com/api/v2/softwareapps/{software_app_id}/systemgroups \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SoftwareAppsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String softwareAppId = "softwareAppId_example"; // ObjectID of the Software App.
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer skip = 0; // The offset into the records to return.
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    try {
      List<GraphObjectWithPaths> result = client
              .softwareApps
              .softwareappsTraverseSystemGroup(softwareAppId)
              .limit(limit)
              .xOrgId(xOrgId)
              .skip(skip)
              .filter(filter)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SoftwareAppsApi#softwareappsTraverseSystemGroup");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<GraphObjectWithPaths>> response = client
              .softwareApps
              .softwareappsTraverseSystemGroup(softwareAppId)
              .limit(limit)
              .xOrgId(xOrgId)
              .skip(skip)
              .filter(filter)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SoftwareAppsApi#softwareappsTraverseSystemGroup");
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
| **softwareAppId** | **String**| ObjectID of the Software App. | |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **filter** | [**List&lt;String&gt;**](String.md)| A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; | [optional] |

### Return type

[**List&lt;GraphObjectWithPaths&gt;**](GraphObjectWithPaths.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="update"></a>
# **update**
> SoftwareApp update(id).xOrgId(xOrgId).softwareApp(softwareApp).execute();

Update a Software Application Configuration.

This endpoint updates a specific Software Application configuration for the organization. displayName can be changed alone if no settings are provided. If a setting is provided, it should include all its information since this endpoint will update all the settings&#39; fields. The optional isConfigEnabled and appConfiguration apple_vpp attributes are not included in the response.  #### Sample Request - displayName only &#x60;&#x60;&#x60;  curl -X PUT https://console.jumpcloud.com/api/v2/softwareapps/{id} \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;displayName\&quot;: \&quot;My Software App\&quot;   }&#39; &#x60;&#x60;&#x60;  #### Sample Request - all attributes &#x60;&#x60;&#x60;  curl -X PUT https://console.jumpcloud.com/api/v2/softwareapps/{id} \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;displayName\&quot;: \&quot;My Software App\&quot;,     \&quot;settings\&quot;: [       {         \&quot;packageId\&quot;: \&quot;123456\&quot;,         \&quot;autoUpdate\&quot;: false,         \&quot;allowUpdateDelay\&quot;: false,         \&quot;packageManager\&quot;: \&quot;APPLE_VPP\&quot;,         \&quot;locationObjectId\&quot;: \&quot;123456789012123456789012\&quot;,         \&quot;location\&quot;: \&quot;123456\&quot;,         \&quot;desiredState\&quot;: \&quot;Install\&quot;,         \&quot;appleVpp\&quot;: {           \&quot;appConfiguration\&quot;: \&quot;&lt;?xml version&#x3D;\\\&quot;1.0\\\&quot; encoding&#x3D;\\\&quot;UTF-8\\\&quot;?&gt;&lt;!DOCTYPE plist PUBLIC \\\&quot;-//Apple//DTD PLIST 1.0//EN\\\&quot; \\\&quot;http://www.apple.com/DTDs/PropertyList-1.0.dtd\\\&quot;&gt;&lt;plist version&#x3D;\\\&quot;1.0\\\&quot;&gt;&lt;dict&gt;&lt;key&gt;MyKey&lt;/key&gt;&lt;string&gt;My String&lt;/string&gt;&lt;/dict&gt;&lt;/plist&gt;\&quot;,           \&quot;assignedLicenses\&quot;: 20,           \&quot;availableLicenses\&quot;: 10,           \&quot;details\&quot;: {},           \&quot;isConfigEnabled\&quot;: true,           \&quot;supportedDeviceFamilies\&quot;: [             \&quot;IPAD\&quot;,             \&quot;MAC\&quot;           ],           \&quot;totalLicenses\&quot;: 30         },         \&quot;packageSubtitle\&quot;: \&quot;My package subtitle\&quot;,         \&quot;packageVersion\&quot;: \&quot;1.2.3\&quot;,         \&quot;packageKind\&quot;: \&quot;software-package\&quot;,         \&quot;assetKind\&quot;: \&quot;software\&quot;,         \&quot;assetSha256Size\&quot;: 256,         \&quot;assetSha256Strings\&quot;: [           \&quot;a123b123c123d123\&quot;         ],         \&quot;description\&quot;: \&quot;My app description\&quot;       }     ]   }&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SoftwareAppsApi;
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
    String displayName = "displayName_example";
    String id = "id_example";
    List<SoftwareAppSettings> settings = Arrays.asList();
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      SoftwareApp result = client
              .softwareApps
              .update(id)
              .displayName(displayName)
              .id(id)
              .settings(settings)
              .xOrgId(xOrgId)
              .execute();
      System.out.println(result);
      System.out.println(result.getDisplayName());
      System.out.println(result.getId());
      System.out.println(result.getSettings());
    } catch (ApiException e) {
      System.err.println("Exception when calling SoftwareAppsApi#update");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<SoftwareApp> response = client
              .softwareApps
              .update(id)
              .displayName(displayName)
              .id(id)
              .settings(settings)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SoftwareAppsApi#update");
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
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **softwareApp** | [**SoftwareApp**](SoftwareApp.md)|  | [optional] |

### Return type

[**SoftwareApp**](SoftwareApp.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="validateApplicationInstallPackage"></a>
# **validateApplicationInstallPackage**
> JumpcloudPackageValidatorValidateApplicationInstallPackageResponse validateApplicationInstallPackage(jumpcloudPackageValidatorValidateApplicationInstallPackageRequest).execute();

Validate Installation Packages

Validates an application install package from the specified URL to calculate the SHA256 hash and extract the installer manifest details. #### Sample Request &#x60;&#x60;&#x60; curl -H &#39;x-api-key: {API_KEY}&#39; \\ -H &#39;Content-Type: application/json&#39; \\ -H &#39;Accept: application/json&#39; \\ -d &#39;{\&quot;url\&quot;: \&quot;https://dl.google.com/dl/chrome/mac/universal/stable/gcem/GoogleChrome.pkg\&quot;}&#39; \\ -i -X POST https://console.jumpcloud.com/api/v2/softwareapps/validate &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SoftwareAppsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String url = "url_example";
    try {
      JumpcloudPackageValidatorValidateApplicationInstallPackageResponse result = client
              .softwareApps
              .validateApplicationInstallPackage()
              .url(url)
              .execute();
      System.out.println(result);
      System.out.println(result.getApplePackageDetails());
    } catch (ApiException e) {
      System.err.println("Exception when calling SoftwareAppsApi#validateApplicationInstallPackage");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<JumpcloudPackageValidatorValidateApplicationInstallPackageResponse> response = client
              .softwareApps
              .validateApplicationInstallPackage()
              .url(url)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SoftwareAppsApi#validateApplicationInstallPackage");
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
| **jumpcloudPackageValidatorValidateApplicationInstallPackageRequest** | [**JumpcloudPackageValidatorValidateApplicationInstallPackageRequest**](JumpcloudPackageValidatorValidateApplicationInstallPackageRequest.md)|  | |

### Return type

[**JumpcloudPackageValidatorValidateApplicationInstallPackageResponse**](JumpcloudPackageValidatorValidateApplicationInstallPackageResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

