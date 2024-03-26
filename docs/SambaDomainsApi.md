# SambaDomainsApi

All URIs are relative to *https://console.jumpcloud.com/api/v2*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**sambaDomainsDelete**](SambaDomainsApi.md#sambaDomainsDelete) | **DELETE** /ldapservers/{ldapserver_id}/sambadomains/{id} | Delete Samba Domain |
| [**sambaDomainsGet**](SambaDomainsApi.md#sambaDomainsGet) | **GET** /ldapservers/{ldapserver_id}/sambadomains/{id} | Get Samba Domain |
| [**sambaDomainsList**](SambaDomainsApi.md#sambaDomainsList) | **GET** /ldapservers/{ldapserver_id}/sambadomains | List Samba Domains |
| [**sambaDomainsPost**](SambaDomainsApi.md#sambaDomainsPost) | **POST** /ldapservers/{ldapserver_id}/sambadomains | Create Samba Domain |
| [**sambaDomainsPut**](SambaDomainsApi.md#sambaDomainsPut) | **PUT** /ldapservers/{ldapserver_id}/sambadomains/{id} | Update Samba Domain |


<a name="sambaDomainsDelete"></a>
# **sambaDomainsDelete**
> String sambaDomainsDelete(ldapserverId, id).xOrgId(xOrgId).execute();

Delete Samba Domain

This endpoint allows you to delete a samba domain from an LDAP server.  ##### Sample Request &#x60;&#x60;&#x60; curl -X DELETE https://console.jumpcloud.com/api/v2/ldapservers/{LDAP_ID}/sambadomains/{SAMBA_ID} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SambaDomainsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String ldapserverId = "ldapserverId_example"; // Unique identifier of the LDAP server.
    String id = "id_example"; // Unique identifier of the samba domain.
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      String result = client
              .sambaDomains
              .sambaDomainsDelete(ldapserverId, id)
              .xOrgId(xOrgId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling SambaDomainsApi#sambaDomainsDelete");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<String> response = client
              .sambaDomains
              .sambaDomainsDelete(ldapserverId, id)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SambaDomainsApi#sambaDomainsDelete");
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
| **ldapserverId** | **String**| Unique identifier of the LDAP server. | |
| **id** | **String**| Unique identifier of the samba domain. | |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |

### Return type

**String**

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |
| **204** |  |  -  |

<a name="sambaDomainsGet"></a>
# **sambaDomainsGet**
> SambaDomain sambaDomainsGet(ldapserverId, id).xOrgId(xOrgId).execute();

Get Samba Domain

This endpoint returns a specific samba domain for an LDAP server.  ##### Sample Request &#x60;&#x60;&#x60; curl -X GET \\   https://console.jumpcloud.com/api/v2/ldapservers/ldapservers/{LDAP_ID}/sambadomains/{SAMBA_ID} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;   &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SambaDomainsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String ldapserverId = "ldapserverId_example"; // Unique identifier of the LDAP server.
    String id = "id_example"; // Unique identifier of the samba domain.
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      SambaDomain result = client
              .sambaDomains
              .sambaDomainsGet(ldapserverId, id)
              .xOrgId(xOrgId)
              .execute();
      System.out.println(result);
      System.out.println(result.getId());
      System.out.println(result.getName());
      System.out.println(result.getSid());
    } catch (ApiException e) {
      System.err.println("Exception when calling SambaDomainsApi#sambaDomainsGet");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<SambaDomain> response = client
              .sambaDomains
              .sambaDomainsGet(ldapserverId, id)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SambaDomainsApi#sambaDomainsGet");
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
| **ldapserverId** | **String**| Unique identifier of the LDAP server. | |
| **id** | **String**| Unique identifier of the samba domain. | |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |

### Return type

[**SambaDomain**](SambaDomain.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="sambaDomainsList"></a>
# **sambaDomainsList**
> List&lt;SambaDomain&gt; sambaDomainsList(ldapserverId).fields(fields).filter(filter).limit(limit).skip(skip).sort(sort).xOrgId(xOrgId).execute();

List Samba Domains

This endpoint returns all samba domains for an LDAP server.  ##### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/ldapservers/{LDAP_ID}/sambadomains \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;   &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SambaDomainsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String ldapserverId = "ldapserverId_example"; // Unique identifier of the LDAP server.
    List<String> fields = Arrays.asList(); // The comma separated fields included in the returned records. If omitted, the default list of fields will be returned. 
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      List<SambaDomain> result = client
              .sambaDomains
              .sambaDomainsList(ldapserverId)
              .fields(fields)
              .filter(filter)
              .limit(limit)
              .skip(skip)
              .sort(sort)
              .xOrgId(xOrgId)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SambaDomainsApi#sambaDomainsList");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<SambaDomain>> response = client
              .sambaDomains
              .sambaDomainsList(ldapserverId)
              .fields(fields)
              .filter(filter)
              .limit(limit)
              .skip(skip)
              .sort(sort)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SambaDomainsApi#sambaDomainsList");
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
| **ldapserverId** | **String**| Unique identifier of the LDAP server. | |
| **fields** | [**List&lt;String&gt;**](String.md)| The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; | [optional] |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |

### Return type

[**List&lt;SambaDomain&gt;**](SambaDomain.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |
| **0** |  |  -  |

<a name="sambaDomainsPost"></a>
# **sambaDomainsPost**
> SambaDomain sambaDomainsPost(ldapserverId).xOrgId(xOrgId).sambaDomain(sambaDomain).execute();

Create Samba Domain

This endpoint allows you to create a samba domain for an LDAP server.  ##### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/ldapservers/{LDAP_ID}/sambadomains \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;sid\&quot;:\&quot;{SID_ID}\&quot;,     \&quot;name\&quot;:\&quot;{WORKGROUP_NAME}\&quot;   }&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SambaDomainsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String name = "name_example"; // Name of this domain's WorkGroup
    String sid = "sid_example"; // Security identifier of this domain
    String ldapserverId = "ldapserverId_example"; // Unique identifier of the LDAP server.
    String id = "id_example"; // Unique identifier of this domain
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      SambaDomain result = client
              .sambaDomains
              .sambaDomainsPost(name, sid, ldapserverId)
              .id(id)
              .xOrgId(xOrgId)
              .execute();
      System.out.println(result);
      System.out.println(result.getId());
      System.out.println(result.getName());
      System.out.println(result.getSid());
    } catch (ApiException e) {
      System.err.println("Exception when calling SambaDomainsApi#sambaDomainsPost");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<SambaDomain> response = client
              .sambaDomains
              .sambaDomainsPost(name, sid, ldapserverId)
              .id(id)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SambaDomainsApi#sambaDomainsPost");
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
| **ldapserverId** | **String**| Unique identifier of the LDAP server. | |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **sambaDomain** | [**SambaDomain**](SambaDomain.md)|  | [optional] |

### Return type

[**SambaDomain**](SambaDomain.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** |  |  -  |

<a name="sambaDomainsPut"></a>
# **sambaDomainsPut**
> SambaDomain sambaDomainsPut(ldapserverId, id).sambaDomain(sambaDomain).execute();

Update Samba Domain

This endpoint allows you to update the samba domain information for an LDAP server.  ##### Sample Request &#x60;&#x60;&#x60; curl -X PUT https://console.jumpcloud.com/api/v2/ldapservers/{LDAP_ID}/sambadomains/{SAMBA_ID} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;sid\&quot;:\&quot;{SID_ID}\&quot;,     \&quot;name\&quot;:\&quot;{WORKGROUP_NAME}\&quot;   }&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SambaDomainsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String name = "name_example"; // Name of this domain's WorkGroup
    String sid = "sid_example"; // Security identifier of this domain
    String ldapserverId = "ldapserverId_example"; // Unique identifier of the LDAP server.
    String id = "id_example"; // Unique identifier of the samba domain.
    String id = "id_example"; // Unique identifier of this domain
    try {
      SambaDomain result = client
              .sambaDomains
              .sambaDomainsPut(name, sid, ldapserverId, id)
              .id(id)
              .execute();
      System.out.println(result);
      System.out.println(result.getId());
      System.out.println(result.getName());
      System.out.println(result.getSid());
    } catch (ApiException e) {
      System.err.println("Exception when calling SambaDomainsApi#sambaDomainsPut");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<SambaDomain> response = client
              .sambaDomains
              .sambaDomainsPut(name, sid, ldapserverId, id)
              .id(id)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SambaDomainsApi#sambaDomainsPut");
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
| **ldapserverId** | **String**| Unique identifier of the LDAP server. | |
| **id** | **String**| Unique identifier of the samba domain. | |
| **sambaDomain** | [**SambaDomain**](SambaDomain.md)|  | [optional] |

### Return type

[**SambaDomain**](SambaDomain.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

