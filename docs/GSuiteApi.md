# GSuiteApi

All URIs are relative to *https://console.jumpcloud.com/api/v2*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**addDomain**](GSuiteApi.md#addDomain) | **POST** /gsuites/{gsuite_id}/domains | Add a domain to a Google Workspace integration instance |
| [**configuredDomainsList**](GSuiteApi.md#configuredDomainsList) | **GET** /gsuites/{gsuite_id}/domains | List all domains configured for the Google Workspace integration instance |
| [**deleteDomain**](GSuiteApi.md#deleteDomain) | **DELETE** /gsuites/{gsuite_id}/domains/{domainId} | Delete a domain from a Google Workspace integration instance |
| [**gSuiteAssociationsList**](GSuiteApi.md#gSuiteAssociationsList) | **GET** /gsuites/{gsuite_id}/associations | List the associations of a G Suite instance |
| [**gSuiteAssociationsPost**](GSuiteApi.md#gSuiteAssociationsPost) | **POST** /gsuites/{gsuite_id}/associations | Manage the associations of a G Suite instance |
| [**gSuiteDelete**](GSuiteApi.md#gSuiteDelete) | **DELETE** /gsuites/{gsuite_id}/translationrules/{id} | Deletes a G Suite translation rule |
| [**gSuiteGet**](GSuiteApi.md#gSuiteGet) | **GET** /gsuites/{gsuite_id}/translationrules/{id} | Gets a specific G Suite translation rule |
| [**gSuiteList**](GSuiteApi.md#gSuiteList) | **GET** /gsuites/{gsuite_id}/translationrules | List all the G Suite Translation Rules |
| [**gSuitePost**](GSuiteApi.md#gSuitePost) | **POST** /gsuites/{gsuite_id}/translationrules | Create a new G Suite Translation Rule |
| [**gSuiteTraverseUser**](GSuiteApi.md#gSuiteTraverseUser) | **GET** /gsuites/{gsuite_id}/users | List the Users bound to a G Suite instance |
| [**gSuiteTraverseUserGroup**](GSuiteApi.md#gSuiteTraverseUserGroup) | **GET** /gsuites/{gsuite_id}/usergroups | List the User Groups bound to a G Suite instance |
| [**get**](GSuiteApi.md#get) | **GET** /gsuites/{id} | Get G Suite |
| [**listImportJumpcloudUsers**](GSuiteApi.md#listImportJumpcloudUsers) | **GET** /gsuites/{gsuite_id}/import/jumpcloudusers | Get a list of users in Jumpcloud format to import from a Google Workspace account. |
| [**listImportUsers**](GSuiteApi.md#listImportUsers) | **GET** /gsuites/{gsuite_id}/import/users | Get a list of users to import from a G Suite instance |
| [**patch**](GSuiteApi.md#patch) | **PATCH** /gsuites/{id} | Update existing G Suite |


<a name="addDomain"></a>
# **addDomain**
> JumpcloudGappsDomainResponse addDomain(gsuiteId).domain(domain).execute();

Add a domain to a Google Workspace integration instance

Add a domain to a specific Google Workspace directory sync integration instance. The domain must be a verified domain in Google Workspace.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/gsuites/{gsuite_id}/domains \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{\&quot;domain\&quot;: \&quot;{domain name}\&quot;}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.GSuiteApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    byte[] gsuiteId = null; // Id for the specific Google Workspace directory sync integration instance.
    String domain = "domain_example";
    try {
      JumpcloudGappsDomainResponse result = client
              .gSuite
              .addDomain(gsuiteId)
              .domain(domain)
              .execute();
      System.out.println(result);
      System.out.println(result.getDomain());
    } catch (ApiException e) {
      System.err.println("Exception when calling GSuiteApi#addDomain");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<JumpcloudGappsDomainResponse> response = client
              .gSuite
              .addDomain(gsuiteId)
              .domain(domain)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling GSuiteApi#addDomain");
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
| **gsuiteId** | **byte[]**| Id for the specific Google Workspace directory sync integration instance. | |
| **domain** | **String**|  | [optional] |

### Return type

[**JumpcloudGappsDomainResponse**](JumpcloudGappsDomainResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A successful response. |  -  |
| **201** | Created |  -  |
| **0** | An unexpected error response. |  -  |

<a name="configuredDomainsList"></a>
# **configuredDomainsList**
> JumpcloudGappsDomainListResponse configuredDomainsList(gsuiteId).limit(limit).skip(skip).execute();

List all domains configured for the Google Workspace integration instance

List the domains configured for a specific Google Workspace directory sync integration instance.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/gsuites/{gsuite_id}/domains \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.GSuiteApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    byte[] gsuiteId = null; // Id for the specific Google Workspace directory sync integration instance..
    String limit = "100"; // The number of records to return at once. Limited to 100.
    String skip = "0"; // The offset into the records to return.
    try {
      JumpcloudGappsDomainListResponse result = client
              .gSuite
              .configuredDomainsList(gsuiteId)
              .limit(limit)
              .skip(skip)
              .execute();
      System.out.println(result);
      System.out.println(result.getDomains());
      System.out.println(result.getTotalCount());
    } catch (ApiException e) {
      System.err.println("Exception when calling GSuiteApi#configuredDomainsList");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<JumpcloudGappsDomainListResponse> response = client
              .gSuite
              .configuredDomainsList(gsuiteId)
              .limit(limit)
              .skip(skip)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling GSuiteApi#configuredDomainsList");
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
| **gsuiteId** | **byte[]**| Id for the specific Google Workspace directory sync integration instance.. | |
| **limit** | **String**| The number of records to return at once. Limited to 100. | [optional] [default to 100] |
| **skip** | **String**| The offset into the records to return. | [optional] [default to 0] |

### Return type

[**JumpcloudGappsDomainListResponse**](JumpcloudGappsDomainListResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |
| **0** | An unexpected error response. |  -  |

<a name="deleteDomain"></a>
# **deleteDomain**
> JumpcloudGappsDomainResponse deleteDomain(gsuiteId, domainId).execute();

Delete a domain from a Google Workspace integration instance

Delete a domain from a specific Google Workspace directory sync integration instance.  #### Sample Request &#x60;&#x60;&#x60; curl -X DELETE https://console.jumpcloud.com/api/v2/gsuites/{gsuite_id}/domains/{domainId} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.GSuiteApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    byte[] gsuiteId = null; // Id for the specific Google Workspace directory sync integration instance.
    byte[] domainId = null; // Id for the domain.
    try {
      JumpcloudGappsDomainResponse result = client
              .gSuite
              .deleteDomain(gsuiteId, domainId)
              .execute();
      System.out.println(result);
      System.out.println(result.getDomain());
    } catch (ApiException e) {
      System.err.println("Exception when calling GSuiteApi#deleteDomain");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<JumpcloudGappsDomainResponse> response = client
              .gSuite
              .deleteDomain(gsuiteId, domainId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling GSuiteApi#deleteDomain");
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
| **gsuiteId** | **byte[]**| Id for the specific Google Workspace directory sync integration instance. | |
| **domainId** | **byte[]**| Id for the domain. | |

### Return type

[**JumpcloudGappsDomainResponse**](JumpcloudGappsDomainResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |
| **0** | An unexpected error response. |  -  |

<a name="gSuiteAssociationsList"></a>
# **gSuiteAssociationsList**
> List&lt;GraphConnection&gt; gSuiteAssociationsList(gsuiteId, targets).limit(limit).skip(skip).xOrgId(xOrgId).execute();

List the associations of a G Suite instance

This endpoint returns the _direct_ associations of this G Suite instance.  A direct association can be a non-homogeneous relationship between 2 different objects, for example G Suite and Users.   #### Sample Request &#x60;&#x60;&#x60; curl -X GET &#39;https://console.jumpcloud.com/api/v2/gsuites/{Gsuite_ID}/associations?targets&#x3D;user_group \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.GSuiteApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String gsuiteId = "gsuiteId_example"; // ObjectID of the G Suite instance.
    List<String> targets = Arrays.asList(); // Targets which a \"g_suite\" can be associated to.
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    Integer skip = 0; // The offset into the records to return.
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      List<GraphConnection> result = client
              .gSuite
              .gSuiteAssociationsList(gsuiteId, targets)
              .limit(limit)
              .skip(skip)
              .xOrgId(xOrgId)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling GSuiteApi#gSuiteAssociationsList");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<GraphConnection>> response = client
              .gSuite
              .gSuiteAssociationsList(gsuiteId, targets)
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
      System.err.println("Exception when calling GSuiteApi#gSuiteAssociationsList");
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
| **gsuiteId** | **String**| ObjectID of the G Suite instance. | |
| **targets** | [**List&lt;String&gt;**](String.md)| Targets which a \&quot;g_suite\&quot; can be associated to. | [enum: user, user_group] |
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

<a name="gSuiteAssociationsPost"></a>
# **gSuiteAssociationsPost**
> gSuiteAssociationsPost(gsuiteId).xOrgId(xOrgId).graphOperationGSuite(graphOperationGSuite).execute();

Manage the associations of a G Suite instance

This endpoint returns the _direct_ associations of this G Suite instance.  A direct association can be a non-homogeneous relationship between 2 different objects, for example G Suite and Users.   #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/gsuites/{Gsuite_ID}/associations \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;op\&quot;: \&quot;add\&quot;,     \&quot;type\&quot;: \&quot;user_group\&quot;,     \&quot;id\&quot;: \&quot;{Group_ID}\&quot;   }&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.GSuiteApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String gsuiteId = "gsuiteId_example"; // ObjectID of the G Suite instance.
    String id = "id_example"; // The ObjectID of graph object being added or removed as an association.
    String op = "add"; // How to modify the graph connection.
    Map<String, Object> attributes = new HashMap(); // The graph attributes.
    String type = "user"; // Targets which a \\\"g_suite\\\" can be associated to.
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      client
              .gSuite
              .gSuiteAssociationsPost(gsuiteId)
              .id(id)
              .op(op)
              .attributes(attributes)
              .type(type)
              .xOrgId(xOrgId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling GSuiteApi#gSuiteAssociationsPost");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .gSuite
              .gSuiteAssociationsPost(gsuiteId)
              .id(id)
              .op(op)
              .attributes(attributes)
              .type(type)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling GSuiteApi#gSuiteAssociationsPost");
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
| **gsuiteId** | **String**| ObjectID of the G Suite instance. | |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **graphOperationGSuite** | [**GraphOperationGSuite**](GraphOperationGSuite.md)|  | [optional] |

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
| **204** | OK |  -  |

<a name="gSuiteDelete"></a>
# **gSuiteDelete**
> gSuiteDelete(gsuiteId, id).execute();

Deletes a G Suite translation rule

This endpoint allows you to delete a translation rule for a specific G Suite instance. These rules specify how JumpCloud attributes translate to [G Suite Admin SDK](https://developers.google.com/admin-sdk/directory/) attributes.  #### Sample Request  &#x60;&#x60;&#x60; curl -X DELETE https://console.jumpcloud.com/api/v2/gsuites/{gsuite_id}/translationrules/{id} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;   &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.GSuiteApi;
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
    String id = "id_example";
    try {
      client
              .gSuite
              .gSuiteDelete(gsuiteId, id)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling GSuiteApi#gSuiteDelete");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .gSuite
              .gSuiteDelete(gsuiteId, id)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling GSuiteApi#gSuiteDelete");
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
| **id** | **String**|  | |

### Return type

null (empty response body)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **204** |  |  -  |

<a name="gSuiteGet"></a>
# **gSuiteGet**
> GSuiteTranslationRule gSuiteGet(gsuiteId, id).execute();

Gets a specific G Suite translation rule

This endpoint returns a specific translation rule for a specific G Suite instance. These rules specify how JumpCloud attributes translate to [G Suite Admin SDK](https://developers.google.com/admin-sdk/directory/) attributes.  ###### Sample Request  &#x60;&#x60;&#x60;   curl -X GET https://console.jumpcloud.com/api/v2/gsuites/{gsuite_id}/translationrules/{id} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;   &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.GSuiteApi;
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
    String id = "id_example";
    try {
      GSuiteTranslationRule result = client
              .gSuite
              .gSuiteGet(gsuiteId, id)
              .execute();
      System.out.println(result);
      System.out.println(result.getBuiltIn());
      System.out.println(result.getDirection());
      System.out.println(result.getId());
    } catch (ApiException e) {
      System.err.println("Exception when calling GSuiteApi#gSuiteGet");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<GSuiteTranslationRule> response = client
              .gSuite
              .gSuiteGet(gsuiteId, id)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling GSuiteApi#gSuiteGet");
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
| **id** | **String**|  | |

### Return type

[**GSuiteTranslationRule**](GSuiteTranslationRule.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="gSuiteList"></a>
# **gSuiteList**
> List&lt;GSuiteTranslationRule&gt; gSuiteList(gsuiteId).fields(fields).filter(filter).limit(limit).skip(skip).sort(sort).execute();

List all the G Suite Translation Rules

This endpoint returns all graph translation rules for a specific G Suite instance. These rules specify how JumpCloud attributes translate to [G Suite Admin SDK](https://developers.google.com/admin-sdk/directory/) attributes.  ##### Sample Request  &#x60;&#x60;&#x60; curl -X GET  https://console.jumpcloud.com/api/v2/gsuites/{gsuite_id}/translationrules \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;   &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.GSuiteApi;
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
    List<String> fields = Arrays.asList(); // The comma separated fields included in the returned records. If omitted, the default list of fields will be returned. 
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. 
    try {
      List<GSuiteTranslationRule> result = client
              .gSuite
              .gSuiteList(gsuiteId)
              .fields(fields)
              .filter(filter)
              .limit(limit)
              .skip(skip)
              .sort(sort)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling GSuiteApi#gSuiteList");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<GSuiteTranslationRule>> response = client
              .gSuite
              .gSuiteList(gsuiteId)
              .fields(fields)
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
      System.err.println("Exception when calling GSuiteApi#gSuiteList");
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
| **fields** | [**List&lt;String&gt;**](String.md)| The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; | [optional] |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  | [optional] |

### Return type

[**List&lt;GSuiteTranslationRule&gt;**](GSuiteTranslationRule.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="gSuitePost"></a>
# **gSuitePost**
> GSuiteTranslationRule gSuitePost(gsuiteId).gsuiteTranslationRuleRequest(gsuiteTranslationRuleRequest).execute();

Create a new G Suite Translation Rule

This endpoint allows you to create a translation rule for a specific G Suite instance. These rules specify how JumpCloud attributes translate to [G Suite Admin SDK](https://developers.google.com/admin-sdk/directory/) attributes.  ##### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/gsuites/{gsuite_id}/translationrules \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     {Translation Rule Parameters}   }&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.GSuiteApi;
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
    GSuiteBuiltinTranslation builtIn = GSuiteBuiltinTranslation.fromValue("user_home_addresses");
    GSuiteDirectionTranslation direction = GSuiteDirectionTranslation.fromValue("export");
    try {
      GSuiteTranslationRule result = client
              .gSuite
              .gSuitePost(gsuiteId)
              .builtIn(builtIn)
              .direction(direction)
              .execute();
      System.out.println(result);
      System.out.println(result.getBuiltIn());
      System.out.println(result.getDirection());
      System.out.println(result.getId());
    } catch (ApiException e) {
      System.err.println("Exception when calling GSuiteApi#gSuitePost");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<GSuiteTranslationRule> response = client
              .gSuite
              .gSuitePost(gsuiteId)
              .builtIn(builtIn)
              .direction(direction)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling GSuiteApi#gSuitePost");
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
| **gsuiteTranslationRuleRequest** | [**GSuiteTranslationRuleRequest**](GSuiteTranslationRuleRequest.md)|  | [optional] |

### Return type

[**GSuiteTranslationRule**](GSuiteTranslationRule.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** |  |  -  |

<a name="gSuiteTraverseUser"></a>
# **gSuiteTraverseUser**
> List&lt;GraphObjectWithPaths&gt; gSuiteTraverseUser(gsuiteId).limit(limit).xOrgId(xOrgId).skip(skip).filter(filter).execute();

List the Users bound to a G Suite instance

This endpoint will return all Users bound to a G Suite instance, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this G Suite instance to the corresponding User; this array represents all grouping and/or associations that would have to be removed to deprovision the User from this G Suite instance.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60;   curl -X GET https://console.jumpcloud.com/api/v2/gsuites/{Gsuite_ID}/users \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.GSuiteApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String gsuiteId = "gsuiteId_example"; // ObjectID of the G Suite instance.
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer skip = 0; // The offset into the records to return.
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    try {
      List<GraphObjectWithPaths> result = client
              .gSuite
              .gSuiteTraverseUser(gsuiteId)
              .limit(limit)
              .xOrgId(xOrgId)
              .skip(skip)
              .filter(filter)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling GSuiteApi#gSuiteTraverseUser");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<GraphObjectWithPaths>> response = client
              .gSuite
              .gSuiteTraverseUser(gsuiteId)
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
      System.err.println("Exception when calling GSuiteApi#gSuiteTraverseUser");
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
| **gsuiteId** | **String**| ObjectID of the G Suite instance. | |
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

<a name="gSuiteTraverseUserGroup"></a>
# **gSuiteTraverseUserGroup**
> List&lt;GraphObjectWithPaths&gt; gSuiteTraverseUserGroup(gsuiteId).limit(limit).xOrgId(xOrgId).skip(skip).filter(filter).execute();

List the User Groups bound to a G Suite instance

This endpoint will return all User Groups bound to an G Suite instance, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the group&#39;s type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this G Suite instance to the corresponding User Group; this array represents all grouping and/or associations that would have to be removed to deprovision the User Group from this G Suite instance.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60;   curl -X GET https://console.jumpcloud.com/api/v2/gsuites/{GSuite_ID}/usergroups \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.GSuiteApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String gsuiteId = "gsuiteId_example"; // ObjectID of the G Suite instance.
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer skip = 0; // The offset into the records to return.
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    try {
      List<GraphObjectWithPaths> result = client
              .gSuite
              .gSuiteTraverseUserGroup(gsuiteId)
              .limit(limit)
              .xOrgId(xOrgId)
              .skip(skip)
              .filter(filter)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling GSuiteApi#gSuiteTraverseUserGroup");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<GraphObjectWithPaths>> response = client
              .gSuite
              .gSuiteTraverseUserGroup(gsuiteId)
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
      System.err.println("Exception when calling GSuiteApi#gSuiteTraverseUserGroup");
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
| **gsuiteId** | **String**| ObjectID of the G Suite instance. | |
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

<a name="get"></a>
# **get**
> Gsuite get(id).xOrgId(xOrgId).execute();

Get G Suite

This endpoint returns a specific G Suite.  ##### Sample Request  &#x60;&#x60;&#x60;  curl -X GET https://console.jumpcloud.com/api/v2/gsuites/{GSUITE_ID} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.GSuiteApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String id = "id_example"; // Unique identifier of the GSuite.
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      Gsuite result = client
              .gSuite
              .get(id)
              .xOrgId(xOrgId)
              .execute();
      System.out.println(result);
      System.out.println(result.getDefaultDomain());
      System.out.println(result.getGroupsEnabled());
      System.out.println(result.getId());
      System.out.println(result.getName());
      System.out.println(result.getUserLockoutAction());
      System.out.println(result.getUserPasswordExpirationAction());
    } catch (ApiException e) {
      System.err.println("Exception when calling GSuiteApi#get");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Gsuite> response = client
              .gSuite
              .get(id)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling GSuiteApi#get");
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
| **id** | **String**| Unique identifier of the GSuite. | |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |

### Return type

[**Gsuite**](Gsuite.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

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
import com.konfigthis.client.api.GSuiteApi;
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
              .gSuite
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
      System.err.println("Exception when calling GSuiteApi#listImportJumpcloudUsers");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<GsuitesListImportJumpcloudUsersResponse> response = client
              .gSuite
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
      System.err.println("Exception when calling GSuiteApi#listImportJumpcloudUsers");
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
import com.konfigthis.client.api.GSuiteApi;
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
              .gSuite
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
      System.err.println("Exception when calling GSuiteApi#listImportUsers");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<GsuitesListImportUsersResponse> response = client
              .gSuite
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
      System.err.println("Exception when calling GSuiteApi#listImportUsers");
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

<a name="patch"></a>
# **patch**
> Gsuite patch(id).xOrgId(xOrgId).gsuite(gsuite).execute();

Update existing G Suite

This endpoint allows updating some attributes of a G Suite.  ##### Sample Request  &#x60;&#x60;&#x60; curl -X PATCH https://console.jumpcloud.com/api/v2/gsuites/{GSUITE_ID} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;userLockoutAction\&quot;: \&quot;suspend\&quot;,     \&quot;userPasswordExpirationAction\&quot;: \&quot;maintain\&quot;   }&#39; &#x60;&#x60;&#x60; Sample Request, set a default domain  &#x60;&#x60;&#x60; curl -X PATCH https://console.jumpcloud.com/api/v2/gsuites/{GSUITE_ID} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;defaultDomain\&quot;: {         \&quot;id\&quot;: \&quot;{domainObjectID}\&quot;       }   }&#39; &#x60;&#x60;&#x60;  Sample Request, unset the default domain  &#x60;&#x60;&#x60; curl -X PATCH https://console.jumpcloud.com/api/v2/gsuites/{GSUITE_ID} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;defaultDomain\&quot;: {}   }&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.GSuiteApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    JumpCloud client = new JumpCloud(configuration);
    String id = "id_example"; // Unique identifier of the GSuite.
    DefaultDomain defaultDomain = new DefaultDomain();
    Boolean groupsEnabled = true;
    String id = "id_example";
    String name = "name_example";
    String userLockoutAction = "suspend";
    String userPasswordExpirationAction = "suspend";
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      Gsuite result = client
              .gSuite
              .patch(id)
              .defaultDomain(defaultDomain)
              .groupsEnabled(groupsEnabled)
              .id(id)
              .name(name)
              .userLockoutAction(userLockoutAction)
              .userPasswordExpirationAction(userPasswordExpirationAction)
              .xOrgId(xOrgId)
              .execute();
      System.out.println(result);
      System.out.println(result.getDefaultDomain());
      System.out.println(result.getGroupsEnabled());
      System.out.println(result.getId());
      System.out.println(result.getName());
      System.out.println(result.getUserLockoutAction());
      System.out.println(result.getUserPasswordExpirationAction());
    } catch (ApiException e) {
      System.err.println("Exception when calling GSuiteApi#patch");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Gsuite> response = client
              .gSuite
              .patch(id)
              .defaultDomain(defaultDomain)
              .groupsEnabled(groupsEnabled)
              .id(id)
              .name(name)
              .userLockoutAction(userLockoutAction)
              .userPasswordExpirationAction(userPasswordExpirationAction)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling GSuiteApi#patch");
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
| **id** | **String**| Unique identifier of the GSuite. | |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **gsuite** | [**Gsuite**](Gsuite.md)|  | [optional] |

### Return type

[**Gsuite**](Gsuite.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

