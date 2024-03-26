# Office365Api

All URIs are relative to *https://console.jumpcloud.com/api/v2*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**delete**](Office365Api.md#delete) | **DELETE** /office365s/{office365_id}/domains/{domain_id} | Delete a domain from an Office 365 instance |
| [**get**](Office365Api.md#get) | **GET** /office365s/{office365_id} | Get Office 365 instance |
| [**insert**](Office365Api.md#insert) | **POST** /office365s/{office365_id}/domains | Add a domain to an Office 365 instance |
| [**list**](Office365Api.md#list) | **GET** /office365s/{office365_id}/domains | List all domains configured for an Office 365 instance |
| [**listImportUsers**](Office365Api.md#listImportUsers) | **GET** /office365s/{office365_id}/import/users | Get a list of users to import from an Office 365 instance |
| [**office365AssociationsList**](Office365Api.md#office365AssociationsList) | **GET** /office365s/{office365_id}/associations | List the associations of an Office 365 instance |
| [**office365AssociationsPost**](Office365Api.md#office365AssociationsPost) | **POST** /office365s/{office365_id}/associations | Manage the associations of an Office 365 instance |
| [**office365Delete**](Office365Api.md#office365Delete) | **DELETE** /office365s/{office365_id}/translationrules/{id} | Deletes a Office 365 translation rule |
| [**office365Get**](Office365Api.md#office365Get) | **GET** /office365s/{office365_id}/translationrules/{id} | Gets a specific Office 365 translation rule |
| [**office365List**](Office365Api.md#office365List) | **GET** /office365s/{office365_id}/translationrules | List all the Office 365 Translation Rules |
| [**office365Post**](Office365Api.md#office365Post) | **POST** /office365s/{office365_id}/translationrules | Create a new Office 365 Translation Rule |
| [**office365TraverseUser**](Office365Api.md#office365TraverseUser) | **GET** /office365s/{office365_id}/users | List the Users bound to an Office 365 instance |
| [**office365TraverseUserGroup**](Office365Api.md#office365TraverseUserGroup) | **GET** /office365s/{office365_id}/usergroups | List the User Groups bound to an Office 365 instance |
| [**patch**](Office365Api.md#patch) | **PATCH** /office365s/{office365_id} | Update existing Office 365 instance. |


<a name="delete"></a>
# **delete**
> O365DomainResponse delete(office365Id, domainId).execute();

Delete a domain from an Office 365 instance

Delete a domain from a specific M365/Azure AD directory sync integration instance.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/office365s/{OFFICE365_ID}/domains/{DOMAIN_ID} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.Office365Api;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    byte[] office365Id = null; // Id for the specific M365/Azure AD directory sync integration instance.
    byte[] domainId = null; // ObjectID of the domain to be deleted.
    try {
      O365DomainResponse result = client
              .office365
              .delete(office365Id, domainId)
              .execute();
      System.out.println(result);
      System.out.println(result.getDomain());
    } catch (ApiException e) {
      System.err.println("Exception when calling Office365Api#delete");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<O365DomainResponse> response = client
              .office365
              .delete(office365Id, domainId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling Office365Api#delete");
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
| **office365Id** | **byte[]**| Id for the specific M365/Azure AD directory sync integration instance. | |
| **domainId** | **byte[]**| ObjectID of the domain to be deleted. | |

### Return type

[**O365DomainResponse**](O365DomainResponse.md)

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

<a name="get"></a>
# **get**
> Office365 get(office365Id).xOrgId(xOrgId).execute();

Get Office 365 instance

This endpoint returns a specific Office 365 instance.  #####  Sample Request  &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/office365s/{OFFICE365_ID} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;  &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.Office365Api;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String office365Id = "office365Id_example"; // ObjectID of the Office 365 instance.
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      Office365 result = client
              .office365
              .get(office365Id)
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
      System.err.println("Exception when calling Office365Api#get");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Office365> response = client
              .office365
              .get(office365Id)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling Office365Api#get");
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
| **office365Id** | **String**| ObjectID of the Office 365 instance. | |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |

### Return type

[**Office365**](Office365.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="insert"></a>
# **insert**
> O365DomainResponse insert(office365Id, domainsInsertRequest).execute();

Add a domain to an Office 365 instance

Add a domain to a specific M365/Azure AD directory sync integration instance. The domain must be a verified domain in M365/Azure AD.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/office365s/{OFFICE365_ID}/domains \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{\&quot;domain\&quot;: \&quot;{domain name}\&quot;}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.Office365Api;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    byte[] office365Id = null; // Id for the specific M365/Azure AD directory sync integration instance.
    String domain = "domain_example";
    try {
      O365DomainResponse result = client
              .office365
              .insert(office365Id)
              .domain(domain)
              .execute();
      System.out.println(result);
      System.out.println(result.getDomain());
    } catch (ApiException e) {
      System.err.println("Exception when calling Office365Api#insert");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<O365DomainResponse> response = client
              .office365
              .insert(office365Id)
              .domain(domain)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling Office365Api#insert");
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
| **office365Id** | **byte[]**| Id for the specific M365/Azure AD directory sync integration instance. | |
| **domainsInsertRequest** | [**DomainsInsertRequest**](DomainsInsertRequest.md)|  | |

### Return type

[**O365DomainResponse**](O365DomainResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A successful response. |  -  |
| **201** | Created |  -  |
| **0** | An unexpected error response. |  -  |

<a name="list"></a>
# **list**
> O365DomainsListResponse list(office365Id).limit(limit).skip(skip).execute();

List all domains configured for an Office 365 instance

List the domains configured for a specific M365/Azure AD directory sync integration instance.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/office365s/{OFFICE365_ID}/domains \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.Office365Api;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    byte[] office365Id = null; // Id for the specific M365/Azure AD directory sync integration instance.
    String limit = "100"; // The number of records to return at once. Limited to 100.
    String skip = "0"; // The offset into the records to return.
    try {
      O365DomainsListResponse result = client
              .office365
              .list(office365Id)
              .limit(limit)
              .skip(skip)
              .execute();
      System.out.println(result);
      System.out.println(result.getDomains());
      System.out.println(result.getTotalCount());
    } catch (ApiException e) {
      System.err.println("Exception when calling Office365Api#list");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<O365DomainsListResponse> response = client
              .office365
              .list(office365Id)
              .limit(limit)
              .skip(skip)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling Office365Api#list");
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
| **office365Id** | **byte[]**| Id for the specific M365/Azure AD directory sync integration instance. | |
| **limit** | **String**| The number of records to return at once. Limited to 100. | [optional] [default to 100] |
| **skip** | **String**| The offset into the records to return. | [optional] [default to 0] |

### Return type

[**O365DomainsListResponse**](O365DomainsListResponse.md)

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
import com.konfigthis.client.api.Office365Api;
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
              .office365
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
      System.err.println("Exception when calling Office365Api#listImportUsers");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Office365SListImportUsersResponse> response = client
              .office365
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
      System.err.println("Exception when calling Office365Api#listImportUsers");
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

<a name="office365AssociationsList"></a>
# **office365AssociationsList**
> List&lt;GraphConnection&gt; office365AssociationsList(office365Id, targets).limit(limit).skip(skip).xOrgId(xOrgId).execute();

List the associations of an Office 365 instance

This endpoint returns _direct_ associations of an Office 365 instance.   A direct association can be a non-homogeneous relationship between 2 different objects, for example Office 365 and Users.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET &#39;https://console.jumpcloud.com/api/v2/office365s/{OFFICE365_ID}/associations?targets&#x3D;user_group&#39; \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;  &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.Office365Api;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String office365Id = "office365Id_example"; // ObjectID of the Office 365 instance.
    List<String> targets = Arrays.asList(); // Targets which a \"office_365\" can be associated to.
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    Integer skip = 0; // The offset into the records to return.
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      List<GraphConnection> result = client
              .office365
              .office365AssociationsList(office365Id, targets)
              .limit(limit)
              .skip(skip)
              .xOrgId(xOrgId)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling Office365Api#office365AssociationsList");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<GraphConnection>> response = client
              .office365
              .office365AssociationsList(office365Id, targets)
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
      System.err.println("Exception when calling Office365Api#office365AssociationsList");
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
| **office365Id** | **String**| ObjectID of the Office 365 instance. | |
| **targets** | [**List&lt;String&gt;**](String.md)| Targets which a \&quot;office_365\&quot; can be associated to. | [enum: user, user_group] |
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

<a name="office365AssociationsPost"></a>
# **office365AssociationsPost**
> office365AssociationsPost(office365Id).xOrgId(xOrgId).graphOperationOffice365(graphOperationOffice365).execute();

Manage the associations of an Office 365 instance

This endpoint allows you to manage the _direct_ associations of a Office 365 instance.  A direct association can be a non-homogeneous relationship between 2 different objects, for example Office 365 and Users.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/office365s/{OFFICE365_ID}/associations \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;op\&quot;: \&quot;add\&quot;,     \&quot;type\&quot;: \&quot;user_group\&quot;,     \&quot;id\&quot;: \&quot;{Group_ID}\&quot;   }&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.Office365Api;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String office365Id = "office365Id_example"; // ObjectID of the Office 365 instance.
    String id = "id_example"; // The ObjectID of graph object being added or removed as an association.
    String op = "add"; // How to modify the graph connection.
    Map<String, Object> attributes = new HashMap(); // The graph attributes.
    String type = "user"; // Targets which a \\\"office_365\\\" can be associated to.
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      client
              .office365
              .office365AssociationsPost(office365Id)
              .id(id)
              .op(op)
              .attributes(attributes)
              .type(type)
              .xOrgId(xOrgId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling Office365Api#office365AssociationsPost");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .office365
              .office365AssociationsPost(office365Id)
              .id(id)
              .op(op)
              .attributes(attributes)
              .type(type)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling Office365Api#office365AssociationsPost");
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
| **office365Id** | **String**| ObjectID of the Office 365 instance. | |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **graphOperationOffice365** | [**GraphOperationOffice365**](GraphOperationOffice365.md)|  | [optional] |

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

<a name="office365Delete"></a>
# **office365Delete**
> office365Delete(office365Id, id).execute();

Deletes a Office 365 translation rule

This endpoint allows you to delete a translation rule for a specific Office 365 instance. These rules specify how JumpCloud attributes translate to [Microsoft Graph](https://developer.microsoft.com/en-us/graph) attributes.  #### Sample Request  &#x60;&#x60;&#x60; curl -X DELETE https://console.jumpcloud.com/api/v2/office365s/{office365_id}/translationrules/{id} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;   &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.Office365Api;
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
    String id = "id_example";
    try {
      client
              .office365
              .office365Delete(office365Id, id)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling Office365Api#office365Delete");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .office365
              .office365Delete(office365Id, id)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling Office365Api#office365Delete");
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

<a name="office365Get"></a>
# **office365Get**
> Office365TranslationRule office365Get(office365Id, id).execute();

Gets a specific Office 365 translation rule

This endpoint returns a specific translation rule for a specific Office 365 instance. These rules specify how JumpCloud attributes translate to [Microsoft Graph](https://developer.microsoft.com/en-us/graph) attributes.  ###### Sample Request  &#x60;&#x60;&#x60;   curl -X GET https://console.jumpcloud.com/api/v2/office365s/{office365_id}/translationrules/{id} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;   &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.Office365Api;
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
    String id = "id_example";
    try {
      Office365TranslationRule result = client
              .office365
              .office365Get(office365Id, id)
              .execute();
      System.out.println(result);
      System.out.println(result.getBuiltIn());
      System.out.println(result.getDirection());
      System.out.println(result.getId());
    } catch (ApiException e) {
      System.err.println("Exception when calling Office365Api#office365Get");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Office365TranslationRule> response = client
              .office365
              .office365Get(office365Id, id)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling Office365Api#office365Get");
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
| **id** | **String**|  | |

### Return type

[**Office365TranslationRule**](Office365TranslationRule.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="office365List"></a>
# **office365List**
> List&lt;Office365TranslationRule&gt; office365List(office365Id).fields(fields).filter(filter).limit(limit).skip(skip).sort(sort).execute();

List all the Office 365 Translation Rules

This endpoint returns all translation rules for a specific Office 365 instance. These rules specify how JumpCloud attributes translate to [Microsoft Graph](https://developer.microsoft.com/en-us/graph) attributes.  ##### Sample Request  &#x60;&#x60;&#x60;  curl -X GET  https://console.jumpcloud.com/api/v2/office365s/{office365_id}/translationrules \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;   &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.Office365Api;
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
    List<String> fields = Arrays.asList(); // The comma separated fields included in the returned records. If omitted, the default list of fields will be returned. 
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. 
    try {
      List<Office365TranslationRule> result = client
              .office365
              .office365List(office365Id)
              .fields(fields)
              .filter(filter)
              .limit(limit)
              .skip(skip)
              .sort(sort)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling Office365Api#office365List");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<Office365TranslationRule>> response = client
              .office365
              .office365List(office365Id)
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
      System.err.println("Exception when calling Office365Api#office365List");
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
| **fields** | [**List&lt;String&gt;**](String.md)| The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; | [optional] |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  | [optional] |

### Return type

[**List&lt;Office365TranslationRule&gt;**](Office365TranslationRule.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="office365Post"></a>
# **office365Post**
> Office365TranslationRule office365Post(office365Id).office365TranslationRuleRequest(office365TranslationRuleRequest).execute();

Create a new Office 365 Translation Rule

This endpoint allows you to create a translation rule for a specific Office 365 instance. These rules specify how JumpCloud attributes translate to [Microsoft Graph](https://developer.microsoft.com/en-us/graph) attributes.  ##### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/office365s/{office365_id}/translationrules \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     {Translation Rule Parameters}   }&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.Office365Api;
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
    Office365BuiltinTranslation builtIn = Office365BuiltinTranslation.fromValue("user_alternate_email");
    Office365DirectionTranslation direction = Office365DirectionTranslation.fromValue("export");
    try {
      Office365TranslationRule result = client
              .office365
              .office365Post(office365Id)
              .builtIn(builtIn)
              .direction(direction)
              .execute();
      System.out.println(result);
      System.out.println(result.getBuiltIn());
      System.out.println(result.getDirection());
      System.out.println(result.getId());
    } catch (ApiException e) {
      System.err.println("Exception when calling Office365Api#office365Post");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Office365TranslationRule> response = client
              .office365
              .office365Post(office365Id)
              .builtIn(builtIn)
              .direction(direction)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling Office365Api#office365Post");
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
| **office365TranslationRuleRequest** | [**Office365TranslationRuleRequest**](Office365TranslationRuleRequest.md)|  | [optional] |

### Return type

[**Office365TranslationRule**](Office365TranslationRule.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** |  |  -  |

<a name="office365TraverseUser"></a>
# **office365TraverseUser**
> List&lt;GraphObjectWithPaths&gt; office365TraverseUser(office365Id).limit(limit).xOrgId(xOrgId).skip(skip).filter(filter).execute();

List the Users bound to an Office 365 instance

This endpoint will return all Users bound to an Office 365 instance, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this Office 365 instance to the corresponding User; this array represents all grouping and/or associations that would have to be removed to deprovision the User from this Office 365 instance.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/office365s/{OFFICE365_ID}/users \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.Office365Api;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String office365Id = "office365Id_example"; // ObjectID of the Office 365 suite.
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer skip = 0; // The offset into the records to return.
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    try {
      List<GraphObjectWithPaths> result = client
              .office365
              .office365TraverseUser(office365Id)
              .limit(limit)
              .xOrgId(xOrgId)
              .skip(skip)
              .filter(filter)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling Office365Api#office365TraverseUser");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<GraphObjectWithPaths>> response = client
              .office365
              .office365TraverseUser(office365Id)
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
      System.err.println("Exception when calling Office365Api#office365TraverseUser");
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
| **office365Id** | **String**| ObjectID of the Office 365 suite. | |
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

<a name="office365TraverseUserGroup"></a>
# **office365TraverseUserGroup**
> List&lt;GraphObjectWithPaths&gt; office365TraverseUserGroup(office365Id).limit(limit).xOrgId(xOrgId).skip(skip).filter(filter).execute();

List the User Groups bound to an Office 365 instance

This endpoint will return all Users Groups bound to an Office 365 instance, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the group&#39;s type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this Office 365 instance to the corresponding User Group; this array represents all grouping and/or associations that would have to be removed to deprovision the User Group from this Office 365 instance.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60;   curl -X GET https://console.jumpcloud.com/api/v2/office365s/{OFFICE365_ID/usergroups \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.Office365Api;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String office365Id = "office365Id_example"; // ObjectID of the Office 365 suite.
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer skip = 0; // The offset into the records to return.
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    try {
      List<GraphObjectWithPaths> result = client
              .office365
              .office365TraverseUserGroup(office365Id)
              .limit(limit)
              .xOrgId(xOrgId)
              .skip(skip)
              .filter(filter)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling Office365Api#office365TraverseUserGroup");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<GraphObjectWithPaths>> response = client
              .office365
              .office365TraverseUserGroup(office365Id)
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
      System.err.println("Exception when calling Office365Api#office365TraverseUserGroup");
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
| **office365Id** | **String**| ObjectID of the Office 365 suite. | |
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

<a name="patch"></a>
# **patch**
> Office365 patch(office365Id).xOrgId(xOrgId).office365(office365).execute();

Update existing Office 365 instance.

This endpoint allows updating some attributes of an Office 365 instance.  #####  Sample Request  &#x60;&#x60;&#x60; curl -X PATCH https://console.jumpcloud.com/api/v2/office365s/{OFFICE365_ID} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;userLockoutAction\&quot;: \&quot;maintain\&quot;,     \&quot;userPasswordExpirationAction\&quot;: \&quot;suspend\&quot;,   }&#39; &#x60;&#x60;&#x60;  Sample Request, set a default domain  &#x60;&#x60;&#x60; curl -X PATCH https://console.jumpcloud.com/api/v2/office365s/{OFFICE365_ID} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;defaultDomain\&quot;: {         \&quot;id\&quot;: \&quot;{domainObjectID}\&quot;       }   }&#39; &#x60;&#x60;&#x60;  Sample Request, unset the default domain  &#x60;&#x60;&#x60; curl -X PATCH https://console.jumpcloud.com/api/v2/office365s/{OFFICE365_ID} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;defaultDomain\&quot;: {}   }&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.Office365Api;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String office365Id = "office365Id_example"; // ObjectID of the Office 365 instance.
    DefaultDomain defaultDomain = new DefaultDomain();
    Boolean groupsEnabled = true;
    String id = "id_example";
    String name = "name_example";
    String userLockoutAction = "suspend";
    String userPasswordExpirationAction = "suspend";
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      Office365 result = client
              .office365
              .patch(office365Id)
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
      System.err.println("Exception when calling Office365Api#patch");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Office365> response = client
              .office365
              .patch(office365Id)
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
      System.err.println("Exception when calling Office365Api#patch");
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
| **office365Id** | **String**| ObjectID of the Office 365 instance. | |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **office365** | [**Office365**](Office365.md)|  | [optional] |

### Return type

[**Office365**](Office365.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

