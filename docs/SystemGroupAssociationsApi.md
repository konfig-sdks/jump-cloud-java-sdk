# SystemGroupAssociationsApi

All URIs are relative to *https://console.jumpcloud.com/api/v2*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**systemGroupAssociationsList**](SystemGroupAssociationsApi.md#systemGroupAssociationsList) | **GET** /systemgroups/{group_id}/associations | List the associations of a System Group |
| [**systemGroupAssociationsPost**](SystemGroupAssociationsApi.md#systemGroupAssociationsPost) | **POST** /systemgroups/{group_id}/associations | Manage the associations of a System Group |
| [**systemGroupTraverseCommand**](SystemGroupAssociationsApi.md#systemGroupTraverseCommand) | **GET** /systemgroups/{group_id}/commands | List the Commands bound to a System Group |
| [**systemGroupTraversePolicy**](SystemGroupAssociationsApi.md#systemGroupTraversePolicy) | **GET** /systemgroups/{group_id}/policies | List the Policies bound to a System Group |
| [**systemGroupTraversePolicyGroup**](SystemGroupAssociationsApi.md#systemGroupTraversePolicyGroup) | **GET** /systemgroups/{group_id}/policygroups | List the Policy Groups bound to a System Group |
| [**systemGroupTraverseUser**](SystemGroupAssociationsApi.md#systemGroupTraverseUser) | **GET** /systemgroups/{group_id}/users | List the Users bound to a System Group |
| [**systemGroupTraverseUserGroup**](SystemGroupAssociationsApi.md#systemGroupTraverseUserGroup) | **GET** /systemgroups/{group_id}/usergroups | List the User Groups bound to a System Group |


<a name="systemGroupAssociationsList"></a>
# **systemGroupAssociationsList**
> List&lt;GraphConnection&gt; systemGroupAssociationsList(groupId, targets).limit(limit).skip(skip).xOrgId(xOrgId).execute();

List the associations of a System Group

This endpoint returns the _direct_ associations of a System Group.  A direct association can be a non-homogeneous relationship between 2 different objects, for example System Groups and Users.   #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/systemgroups/{GroupID}/associations?targets&#x3D;user \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemGroupAssociationsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String groupId = "groupId_example"; // ObjectID of the System Group.
    List<String> targets = Arrays.asList(); // Targets which a \"system_group\" can be associated to.
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    Integer skip = 0; // The offset into the records to return.
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      List<GraphConnection> result = client
              .systemGroupAssociations
              .systemGroupAssociationsList(groupId, targets)
              .limit(limit)
              .skip(skip)
              .xOrgId(xOrgId)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemGroupAssociationsApi#systemGroupAssociationsList");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<GraphConnection>> response = client
              .systemGroupAssociations
              .systemGroupAssociationsList(groupId, targets)
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
      System.err.println("Exception when calling SystemGroupAssociationsApi#systemGroupAssociationsList");
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
| **groupId** | **String**| ObjectID of the System Group. | |
| **targets** | [**List&lt;String&gt;**](String.md)| Targets which a \&quot;system_group\&quot; can be associated to. | [enum: command, policy, policy_group, user, user_group] |
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

<a name="systemGroupAssociationsPost"></a>
# **systemGroupAssociationsPost**
> systemGroupAssociationsPost(groupId).xOrgId(xOrgId).graphOperationSystemGroup(graphOperationSystemGroup).execute();

Manage the associations of a System Group

This endpoint allows you to manage the _direct_ associations of a System Group.  A direct association can be a non-homogeneous relationship between 2 different objects, for example System Groups and Users.   #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/systemgroups/{GroupID}/associations \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;op\&quot;: \&quot;add\&quot;,     \&quot;type\&quot;: \&quot;user\&quot;,     \&quot;id\&quot;: \&quot;{UserID}\&quot;   }&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemGroupAssociationsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String groupId = "groupId_example"; // ObjectID of the System Group.
    String id = "id_example"; // The ObjectID of graph object being added or removed as an association.
    String op = "add"; // How to modify the graph connection.
    Map<String, Object> attributes = new HashMap(); // The graph attributes.
    String type = "command"; // Targets which a \\\"system_group\\\" can be associated to.
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      client
              .systemGroupAssociations
              .systemGroupAssociationsPost(groupId)
              .id(id)
              .op(op)
              .attributes(attributes)
              .type(type)
              .xOrgId(xOrgId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemGroupAssociationsApi#systemGroupAssociationsPost");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .systemGroupAssociations
              .systemGroupAssociationsPost(groupId)
              .id(id)
              .op(op)
              .attributes(attributes)
              .type(type)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemGroupAssociationsApi#systemGroupAssociationsPost");
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
| **groupId** | **String**| ObjectID of the System Group. | |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **graphOperationSystemGroup** | [**GraphOperationSystemGroup**](GraphOperationSystemGroup.md)|  | [optional] |

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

<a name="systemGroupTraverseCommand"></a>
# **systemGroupTraverseCommand**
> List&lt;CommandsGraphObjectWithPaths&gt; systemGroupTraverseCommand(groupId).limit(limit).xOrgId(xOrgId).skip(skip).filter(filter).details(details).execute();

List the Commands bound to a System Group

This endpoint will return all Commands bound to a System Group, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the group&#39;s type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this System Group to the corresponding Command; this array represents all grouping and/or associations that would have to be removed to deprovision the Command from this System Group.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/systemgroups/{GroupID}/commands \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemGroupAssociationsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String groupId = "groupId_example"; // ObjectID of the System Group.
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer skip = 0; // The offset into the records to return.
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    String details = "v1"; // This will provide detail descriptive response for the request.
    try {
      List<CommandsGraphObjectWithPaths> result = client
              .systemGroupAssociations
              .systemGroupTraverseCommand(groupId)
              .limit(limit)
              .xOrgId(xOrgId)
              .skip(skip)
              .filter(filter)
              .details(details)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemGroupAssociationsApi#systemGroupTraverseCommand");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<CommandsGraphObjectWithPaths>> response = client
              .systemGroupAssociations
              .systemGroupTraverseCommand(groupId)
              .limit(limit)
              .xOrgId(xOrgId)
              .skip(skip)
              .filter(filter)
              .details(details)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemGroupAssociationsApi#systemGroupTraverseCommand");
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
| **groupId** | **String**| ObjectID of the System Group. | |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **filter** | [**List&lt;String&gt;**](String.md)| A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; | [optional] |
| **details** | **String**| This will provide detail descriptive response for the request. | [optional] [enum: v1] |

### Return type

[**List&lt;CommandsGraphObjectWithPaths&gt;**](CommandsGraphObjectWithPaths.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="systemGroupTraversePolicy"></a>
# **systemGroupTraversePolicy**
> List&lt;GraphObjectWithPaths&gt; systemGroupTraversePolicy(groupId).limit(limit).xOrgId(xOrgId).skip(skip).filter(filter).execute();

List the Policies bound to a System Group

This endpoint will return all Policies bound to a System Group, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this System Group to the corresponding Policy; this array represents all grouping and/or associations that would have to be removed to deprovision the Policy from this System Group.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  This endpoint is not public yet as we haven&#39;t finished the code.  ##### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/systemgroups/{GroupID}/policies \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemGroupAssociationsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String groupId = "groupId_example"; // ObjectID of the System Group.
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer skip = 0; // The offset into the records to return.
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    try {
      List<GraphObjectWithPaths> result = client
              .systemGroupAssociations
              .systemGroupTraversePolicy(groupId)
              .limit(limit)
              .xOrgId(xOrgId)
              .skip(skip)
              .filter(filter)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemGroupAssociationsApi#systemGroupTraversePolicy");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<GraphObjectWithPaths>> response = client
              .systemGroupAssociations
              .systemGroupTraversePolicy(groupId)
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
      System.err.println("Exception when calling SystemGroupAssociationsApi#systemGroupTraversePolicy");
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
| **groupId** | **String**| ObjectID of the System Group. | |
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

<a name="systemGroupTraversePolicyGroup"></a>
# **systemGroupTraversePolicyGroup**
> List&lt;GraphObjectWithPaths&gt; systemGroupTraversePolicyGroup(groupId).limit(limit).xOrgId(xOrgId).skip(skip).filter(filter).execute();

List the Policy Groups bound to a System Group

This endpoint will return all Policy Groups bound to a System Group, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this System Group to the corresponding Policy Group; this array represents all grouping and/or associations that would have to be removed to deprovision the Policy Group from this System Group.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/systemgroups/{GroupID}/policygroups \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;  &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemGroupAssociationsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String groupId = "groupId_example"; // ObjectID of the System Group.
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer skip = 0; // The offset into the records to return.
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    try {
      List<GraphObjectWithPaths> result = client
              .systemGroupAssociations
              .systemGroupTraversePolicyGroup(groupId)
              .limit(limit)
              .xOrgId(xOrgId)
              .skip(skip)
              .filter(filter)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemGroupAssociationsApi#systemGroupTraversePolicyGroup");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<GraphObjectWithPaths>> response = client
              .systemGroupAssociations
              .systemGroupTraversePolicyGroup(groupId)
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
      System.err.println("Exception when calling SystemGroupAssociationsApi#systemGroupTraversePolicyGroup");
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
| **groupId** | **String**| ObjectID of the System Group. | |
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

<a name="systemGroupTraverseUser"></a>
# **systemGroupTraverseUser**
> List&lt;GraphObjectWithPaths&gt; systemGroupTraverseUser(groupId).limit(limit).xOrgId(xOrgId).skip(skip).filter(filter).execute();

List the Users bound to a System Group

This endpoint will return all Users bound to a System Group, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this System Group to the corresponding User; this array represents all grouping and/or associations that would have to be removed to deprovision the User from this System Group.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/systemgroups/{GroupID}/users \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemGroupAssociationsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String groupId = "groupId_example"; // ObjectID of the System Group.
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer skip = 0; // The offset into the records to return.
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    try {
      List<GraphObjectWithPaths> result = client
              .systemGroupAssociations
              .systemGroupTraverseUser(groupId)
              .limit(limit)
              .xOrgId(xOrgId)
              .skip(skip)
              .filter(filter)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemGroupAssociationsApi#systemGroupTraverseUser");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<GraphObjectWithPaths>> response = client
              .systemGroupAssociations
              .systemGroupTraverseUser(groupId)
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
      System.err.println("Exception when calling SystemGroupAssociationsApi#systemGroupTraverseUser");
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
| **groupId** | **String**| ObjectID of the System Group. | |
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

<a name="systemGroupTraverseUserGroup"></a>
# **systemGroupTraverseUserGroup**
> List&lt;GraphObjectWithPaths&gt; systemGroupTraverseUserGroup(groupId).limit(limit).xOrgId(xOrgId).skip(skip).filter(filter).execute();

List the User Groups bound to a System Group

This endpoint will return all User Groups bound to a System Group, either directly or indirectly, essentially traversing the JumpCloud Graph for your Organization.  Each element will contain the type, id, attributes and paths.  The &#x60;attributes&#x60; object is a key/value hash of compiled graph attributes for all paths followed.  The &#x60;paths&#x60; array enumerates each path from this System Group to the corresponding User Group; this array represents all grouping and/or associations that would have to be removed to deprovision the User Group from this System Group.  See &#x60;/members&#x60; and &#x60;/associations&#x60; endpoints to manage those collections.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/systemgroups/{GroupID}/usergroups \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;  &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SystemGroupAssociationsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String groupId = "groupId_example"; // ObjectID of the System Group.
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    Integer skip = 0; // The offset into the records to return.
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    try {
      List<GraphObjectWithPaths> result = client
              .systemGroupAssociations
              .systemGroupTraverseUserGroup(groupId)
              .limit(limit)
              .xOrgId(xOrgId)
              .skip(skip)
              .filter(filter)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SystemGroupAssociationsApi#systemGroupTraverseUserGroup");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<GraphObjectWithPaths>> response = client
              .systemGroupAssociations
              .systemGroupTraverseUserGroup(groupId)
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
      System.err.println("Exception when calling SystemGroupAssociationsApi#systemGroupTraverseUserGroup");
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
| **groupId** | **String**| ObjectID of the System Group. | |
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

