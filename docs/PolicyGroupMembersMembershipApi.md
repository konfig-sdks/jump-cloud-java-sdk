# PolicyGroupMembersMembershipApi

All URIs are relative to *https://console.jumpcloud.com/api/v2*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**policyGroupMembersList**](PolicyGroupMembersMembershipApi.md#policyGroupMembersList) | **GET** /policygroups/{group_id}/members | List the members of a Policy Group |
| [**policyGroupMembersPost**](PolicyGroupMembersMembershipApi.md#policyGroupMembersPost) | **POST** /policygroups/{group_id}/members | Manage the members of a Policy Group |
| [**policyGroupMembership**](PolicyGroupMembersMembershipApi.md#policyGroupMembership) | **GET** /policygroups/{group_id}/membership | List the Policy Group&#39;s membership |


<a name="policyGroupMembersList"></a>
# **policyGroupMembersList**
> List&lt;GraphConnection&gt; policyGroupMembersList(groupId).limit(limit).skip(skip).xOrgId(xOrgId).execute();

List the members of a Policy Group

This endpoint returns the Policy members of a Policy Group.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/policygroups/{GroupID}/members \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.PolicyGroupMembersMembershipApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String groupId = "groupId_example"; // ObjectID of the Policy Group.
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    Integer skip = 0; // The offset into the records to return.
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      List<GraphConnection> result = client
              .policyGroupMembersMembership
              .policyGroupMembersList(groupId)
              .limit(limit)
              .skip(skip)
              .xOrgId(xOrgId)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling PolicyGroupMembersMembershipApi#policyGroupMembersList");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<GraphConnection>> response = client
              .policyGroupMembersMembership
              .policyGroupMembersList(groupId)
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
      System.err.println("Exception when calling PolicyGroupMembersMembershipApi#policyGroupMembersList");
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
| **groupId** | **String**| ObjectID of the Policy Group. | |
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

<a name="policyGroupMembersPost"></a>
# **policyGroupMembersPost**
> policyGroupMembersPost(groupId).xOrgId(xOrgId).graphOperationPolicyGroupMember(graphOperationPolicyGroupMember).execute();

Manage the members of a Policy Group

This endpoint allows you to manage the Policy members of a Policy Group.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/policygroups/{GroupID}/members \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;op\&quot;: \&quot;add\&quot;,     \&quot;type\&quot;: \&quot;policy\&quot;,     \&quot;id\&quot;: \&quot;{Policy_ID}\&quot;   }&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.PolicyGroupMembersMembershipApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String groupId = "groupId_example"; // ObjectID of the Policy Group.
    String id = "id_example"; // The ObjectID of graph object being added or removed as an association.
    String op = "add"; // How to modify the graph connection.
    Map<String, Object> attributes = new HashMap(); // The graph attributes.
    String type = "policy"; // The member type.
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      client
              .policyGroupMembersMembership
              .policyGroupMembersPost(groupId)
              .id(id)
              .op(op)
              .attributes(attributes)
              .type(type)
              .xOrgId(xOrgId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling PolicyGroupMembersMembershipApi#policyGroupMembersPost");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .policyGroupMembersMembership
              .policyGroupMembersPost(groupId)
              .id(id)
              .op(op)
              .attributes(attributes)
              .type(type)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling PolicyGroupMembersMembershipApi#policyGroupMembersPost");
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
| **groupId** | **String**| ObjectID of the Policy Group. | |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **graphOperationPolicyGroupMember** | [**GraphOperationPolicyGroupMember**](GraphOperationPolicyGroupMember.md)|  | [optional] |

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

<a name="policyGroupMembership"></a>
# **policyGroupMembership**
> List&lt;GraphObjectWithPaths&gt; policyGroupMembership(groupId).filter(filter).limit(limit).skip(skip).sort(sort).xOrgId(xOrgId).execute();

List the Policy Group&#39;s membership

This endpoint returns all Policy members that are a member of this Policy Group.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/policygroups/{GroupID}/membership \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.PolicyGroupMembersMembershipApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String groupId = "groupId_example"; // ObjectID of the Policy Group.
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. 
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      List<GraphObjectWithPaths> result = client
              .policyGroupMembersMembership
              .policyGroupMembership(groupId)
              .filter(filter)
              .limit(limit)
              .skip(skip)
              .sort(sort)
              .xOrgId(xOrgId)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling PolicyGroupMembersMembershipApi#policyGroupMembership");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<GraphObjectWithPaths>> response = client
              .policyGroupMembersMembership
              .policyGroupMembership(groupId)
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
      System.err.println("Exception when calling PolicyGroupMembersMembershipApi#policyGroupMembership");
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
| **groupId** | **String**| ObjectID of the Policy Group. | |
| **filter** | [**List&lt;String&gt;**](String.md)| A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; | [optional] |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  | [optional] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |

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

