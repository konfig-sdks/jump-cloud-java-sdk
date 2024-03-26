# IpListsApi

All URIs are relative to *https://console.jumpcloud.com/api/v2*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**delete**](IpListsApi.md#delete) | **DELETE** /iplists/{id} | Delete an IP list |
| [**get**](IpListsApi.md#get) | **GET** /iplists/{id} | Get an IP list |
| [**list**](IpListsApi.md#list) | **GET** /iplists | List IP Lists |
| [**patch**](IpListsApi.md#patch) | **PATCH** /iplists/{id} | Update an IP list |
| [**post**](IpListsApi.md#post) | **POST** /iplists | Create IP List |
| [**put**](IpListsApi.md#put) | **PUT** /iplists/{id} | Replace an IP list |


<a name="delete"></a>
# **delete**
> IPList delete(id).xOrgId(xOrgId).execute();

Delete an IP list

Delete a specific IP list.  #### Sample Request &#x60;&#x60;&#x60; curl -X DELETE https://console.jumpcloud.com/api/v2/iplists/{id} \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.IpListsApi;
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
      IPList result = client
              .ipLists
              .delete(id)
              .xOrgId(xOrgId)
              .execute();
      System.out.println(result);
      System.out.println(result.getDescription());
      System.out.println(result.getId());
      System.out.println(result.getIps());
      System.out.println(result.getName());
    } catch (ApiException e) {
      System.err.println("Exception when calling IpListsApi#delete");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<IPList> response = client
              .ipLists
              .delete(id)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling IpListsApi#delete");
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

[**IPList**](IPList.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="get"></a>
# **get**
> IPList get(id).xOrgId(xOrgId).execute();

Get an IP list

Return a specific IP list.  #### Sample Request &#x60;&#x60;&#x60; curl https://console.jumpcloud.com/api/v2/iplists/{id} \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.IpListsApi;
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
      IPList result = client
              .ipLists
              .get(id)
              .xOrgId(xOrgId)
              .execute();
      System.out.println(result);
      System.out.println(result.getDescription());
      System.out.println(result.getId());
      System.out.println(result.getIps());
      System.out.println(result.getName());
    } catch (ApiException e) {
      System.err.println("Exception when calling IpListsApi#get");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<IPList> response = client
              .ipLists
              .get(id)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling IpListsApi#get");
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

[**IPList**](IPList.md)

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
> List&lt;IPList&gt; list().xOrgId(xOrgId).xTotalCount(xTotalCount).limit(limit).skip(skip).filter(filter).sort(sort).execute();

List IP Lists

Retrieve all IP lists.  #### Sample Request &#x60;&#x60;&#x60; curl https://console.jumpcloud.com/api/v2/iplists \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.IpListsApi;
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
    Integer xTotalCount = 56;
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    Integer skip = 0; // The offset into the records to return.
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. 
    try {
      List<IPList> result = client
              .ipLists
              .list()
              .xOrgId(xOrgId)
              .xTotalCount(xTotalCount)
              .limit(limit)
              .skip(skip)
              .filter(filter)
              .sort(sort)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling IpListsApi#list");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<IPList>> response = client
              .ipLists
              .list()
              .xOrgId(xOrgId)
              .xTotalCount(xTotalCount)
              .limit(limit)
              .skip(skip)
              .filter(filter)
              .sort(sort)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling IpListsApi#list");
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
| **xTotalCount** | **Integer**|  | [optional] |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **filter** | [**List&lt;String&gt;**](String.md)| A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; | [optional] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  | [optional] |

### Return type

[**List&lt;IPList&gt;**](IPList.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="patch"></a>
# **patch**
> IPList patch(id).xOrgId(xOrgId).ipListRequest(ipListRequest).execute();

Update an IP list

Update a specific IP list.  #### Sample Request &#x60;&#x60;&#x60; curl -X PATCH https://console.jumpcloud.com/api/v2/iplists/{id} \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{\&quot;name\&quot;: \&quot;New IP List Name\&quot;}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.IpListsApi;
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
    String description = "description_example";
    List<String> ips = Arrays.asList();
    String name = "name_example";
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      IPList result = client
              .ipLists
              .patch(id)
              .description(description)
              .ips(ips)
              .name(name)
              .xOrgId(xOrgId)
              .execute();
      System.out.println(result);
      System.out.println(result.getDescription());
      System.out.println(result.getId());
      System.out.println(result.getIps());
      System.out.println(result.getName());
    } catch (ApiException e) {
      System.err.println("Exception when calling IpListsApi#patch");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<IPList> response = client
              .ipLists
              .patch(id)
              .description(description)
              .ips(ips)
              .name(name)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling IpListsApi#patch");
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
| **ipListRequest** | [**IPListRequest**](IPListRequest.md)|  | [optional] |

### Return type

[**IPList**](IPList.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="post"></a>
# **post**
> IPList post().xOrgId(xOrgId).ipListRequest(ipListRequest).execute();

Create IP List

Create an IP list.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/iplists \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;name\&quot;: \&quot;Sample IP List\&quot;,     \&quot;ips\&quot;: [       \&quot;192.168.10.12\&quot;,       \&quot;192.168.10.20 - 192.168.10.30\&quot;,       \&quot;123.225.10.0/32\&quot;     ]   }&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.IpListsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String description = "description_example";
    List<String> ips = Arrays.asList();
    String name = "name_example";
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      IPList result = client
              .ipLists
              .post()
              .description(description)
              .ips(ips)
              .name(name)
              .xOrgId(xOrgId)
              .execute();
      System.out.println(result);
      System.out.println(result.getDescription());
      System.out.println(result.getId());
      System.out.println(result.getIps());
      System.out.println(result.getName());
    } catch (ApiException e) {
      System.err.println("Exception when calling IpListsApi#post");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<IPList> response = client
              .ipLists
              .post()
              .description(description)
              .ips(ips)
              .name(name)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling IpListsApi#post");
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
| **ipListRequest** | [**IPListRequest**](IPListRequest.md)|  | [optional] |

### Return type

[**IPList**](IPList.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Created |  -  |

<a name="put"></a>
# **put**
> IPList put(id).xOrgId(xOrgId).ipListRequest(ipListRequest).execute();

Replace an IP list

Replace a specific IP list.  #### Sample Request &#x60;&#x60;&#x60; curl -X PUT https://console.jumpcloud.com/api/v2/iplists/{id} \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;name\&quot;: \&quot;Sample IP List\&quot;,     \&quot;ips\&quot;: [       \&quot;192.168.10.10\&quot;     ]   }&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.IpListsApi;
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
    String description = "description_example";
    List<String> ips = Arrays.asList();
    String name = "name_example";
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      IPList result = client
              .ipLists
              .put(id)
              .description(description)
              .ips(ips)
              .name(name)
              .xOrgId(xOrgId)
              .execute();
      System.out.println(result);
      System.out.println(result.getDescription());
      System.out.println(result.getId());
      System.out.println(result.getIps());
      System.out.println(result.getName());
    } catch (ApiException e) {
      System.err.println("Exception when calling IpListsApi#put");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<IPList> response = client
              .ipLists
              .put(id)
              .description(description)
              .ips(ips)
              .name(name)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling IpListsApi#put");
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
| **ipListRequest** | [**IPListRequest**](IPListRequest.md)|  | [optional] |

### Return type

[**IPList**](IPList.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

