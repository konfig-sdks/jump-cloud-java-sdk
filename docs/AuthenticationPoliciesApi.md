# AuthenticationPoliciesApi

All URIs are relative to *https://console.jumpcloud.com/api/v2*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**delete**](AuthenticationPoliciesApi.md#delete) | **DELETE** /authn/policies/{id} | Delete Authentication Policy |
| [**get**](AuthenticationPoliciesApi.md#get) | **GET** /authn/policies/{id} | Get an authentication policy |
| [**list**](AuthenticationPoliciesApi.md#list) | **GET** /authn/policies | List Authentication Policies |
| [**patch**](AuthenticationPoliciesApi.md#patch) | **PATCH** /authn/policies/{id} | Patch Authentication Policy |
| [**post**](AuthenticationPoliciesApi.md#post) | **POST** /authn/policies | Create an Authentication Policy |


<a name="delete"></a>
# **delete**
> AuthnPolicy delete(id).xOrgId(xOrgId).execute();

Delete Authentication Policy

Delete the specified authentication policy.  #### Sample Request &#x60;&#x60;&#x60; curl -X DELETE https://console.jumpcloud.com/api/v2/authn/policies/{id} \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AuthenticationPoliciesApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String id = "id_example"; // Unique identifier of the authentication policy
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      AuthnPolicy result = client
              .authenticationPolicies
              .delete(id)
              .xOrgId(xOrgId)
              .execute();
      System.out.println(result);
      System.out.println(result.getDescription());
      System.out.println(result.getConditions());
      System.out.println(result.getDisabled());
      System.out.println(result.getEffect());
      System.out.println(result.getId());
      System.out.println(result.getName());
      System.out.println(result.getTargets());
      System.out.println(result.getType());
    } catch (ApiException e) {
      System.err.println("Exception when calling AuthenticationPoliciesApi#delete");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AuthnPolicy> response = client
              .authenticationPolicies
              .delete(id)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AuthenticationPoliciesApi#delete");
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
| **id** | **String**| Unique identifier of the authentication policy | |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |

### Return type

[**AuthnPolicy**](AuthnPolicy.md)

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
> AuthnPolicy get(id).xOrgId(xOrgId).execute();

Get an authentication policy

Return a specific authentication policy.  #### Sample Request &#x60;&#x60;&#x60; curl https://console.jumpcloud.com/api/v2/authn/policies/{id} \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AuthenticationPoliciesApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String id = "id_example"; // Unique identifier of the authentication policy
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      AuthnPolicy result = client
              .authenticationPolicies
              .get(id)
              .xOrgId(xOrgId)
              .execute();
      System.out.println(result);
      System.out.println(result.getDescription());
      System.out.println(result.getConditions());
      System.out.println(result.getDisabled());
      System.out.println(result.getEffect());
      System.out.println(result.getId());
      System.out.println(result.getName());
      System.out.println(result.getTargets());
      System.out.println(result.getType());
    } catch (ApiException e) {
      System.err.println("Exception when calling AuthenticationPoliciesApi#get");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AuthnPolicy> response = client
              .authenticationPolicies
              .get(id)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AuthenticationPoliciesApi#get");
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
| **id** | **String**| Unique identifier of the authentication policy | |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |

### Return type

[**AuthnPolicy**](AuthnPolicy.md)

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
> List&lt;AuthnPolicy&gt; list().xOrgId(xOrgId).xTotalCount(xTotalCount).limit(limit).skip(skip).filter(filter).sort(sort).execute();

List Authentication Policies

Get a list of all authentication policies.  #### Sample Request &#x60;&#x60;&#x60; curl https://console.jumpcloud.com/api/v2/authn/policies \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AuthenticationPoliciesApi;
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
      List<AuthnPolicy> result = client
              .authenticationPolicies
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
      System.err.println("Exception when calling AuthenticationPoliciesApi#list");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<AuthnPolicy>> response = client
              .authenticationPolicies
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
      System.err.println("Exception when calling AuthenticationPoliciesApi#list");
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

[**List&lt;AuthnPolicy&gt;**](AuthnPolicy.md)

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
> AuthnPolicy patch(id).xOrgId(xOrgId).authnPolicy(authnPolicy).execute();

Patch Authentication Policy

Patch the specified authentication policy.  #### Sample Request &#x60;&#x60;&#x60; curl -X PATCH https://console.jumpcloud.com/api/v2/authn/policies/{id} \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{ \&quot;disabled\&quot;: false }&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AuthenticationPoliciesApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String id = "id_example"; // Unique identifier of the authentication policy
    String description = "description_example";
    Object conditions = null; // Conditions may be added to an authentication policy using the following conditional language:  ``` <conditions> ::= <expression> <expression> ::= <deviceEncrypted> | <deviceManaged> | <ipAddressIn> |                  <locationIn> | <notExpression> | <allExpression> |                  <anyExpression> <deviceEncrypted> ::= { \\\"deviceEncrypted\\\": <boolean> } <deviceManaged> ::= { \\\"deviceManaged\\\": <boolean> } <ipAddressIn> ::= { \\\"ipAddressIn\\\": [ <objectId>, ... ] } <locationIn> ::= { \\\"locationIn\\\": {                      \\\"countries\\\": [                        <iso_3166_country_code>, ...                      ]                    }                  } <notExpression> ::= { \\\"not\\\": <expression> } <allExpression> ::= { \\\"all\\\": [ <expression>, ... ] } <anyExpression> ::= { \\\"any\\\": [ <expression>, ... ] } ```  For example, to add a condition that applies to IP addresses in a given list, the following condition can be added:  ``` {\\\"ipAddressIn\\\": [ <ip_list_object_id> ]} ```  If you would rather exclude IP addresses in the given lists, the following condition could be added:  ``` {   \\\"not\\\": {     \\\"ipAddressIn\\\": [ <ip_list_object_id_1>, <ip_list_object_id_2> ]   } } ```  You may also include more than one condition and choose whether \\\"all\\\" or \\\"any\\\" of them must be met for the policy to apply:  ``` {   \\\"all\\\": [     {       \\\"ipAddressIn\\\": [ <ip_list_object_id>, ... ]     },     {       \\\"deviceManaged\\\": true     },     {       \\\"locationIn\\\": {         countries: [ <iso_3166_country_code>, ... ]       }     }   ] } ```
    Boolean disabled = true;
    AuthnPolicyEffect effect = new AuthnPolicyEffect();
    String id = "id_example";
    String name = "name_example";
    AuthnPolicyTargets targets = new AuthnPolicyTargets();
    AuthnPolicyType type = AuthnPolicyType.fromValue("user_portal");
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      AuthnPolicy result = client
              .authenticationPolicies
              .patch(id)
              .description(description)
              .conditions(conditions)
              .disabled(disabled)
              .effect(effect)
              .id(id)
              .name(name)
              .targets(targets)
              .type(type)
              .xOrgId(xOrgId)
              .execute();
      System.out.println(result);
      System.out.println(result.getDescription());
      System.out.println(result.getConditions());
      System.out.println(result.getDisabled());
      System.out.println(result.getEffect());
      System.out.println(result.getId());
      System.out.println(result.getName());
      System.out.println(result.getTargets());
      System.out.println(result.getType());
    } catch (ApiException e) {
      System.err.println("Exception when calling AuthenticationPoliciesApi#patch");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AuthnPolicy> response = client
              .authenticationPolicies
              .patch(id)
              .description(description)
              .conditions(conditions)
              .disabled(disabled)
              .effect(effect)
              .id(id)
              .name(name)
              .targets(targets)
              .type(type)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AuthenticationPoliciesApi#patch");
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
| **id** | **String**| Unique identifier of the authentication policy | |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **authnPolicy** | [**AuthnPolicy**](AuthnPolicy.md)|  | [optional] |

### Return type

[**AuthnPolicy**](AuthnPolicy.md)

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
> AuthnPolicy post().xOrgId(xOrgId).authnPolicy(authnPolicy).execute();

Create an Authentication Policy

Create an authentication policy.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/authn/policies \\   -H &#39;accept: application/json&#39; \\   -H &#39;content-type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{     \&quot;name\&quot;: \&quot;Sample Policy\&quot;,     \&quot;disabled\&quot;: false,     \&quot;effect\&quot;: {       \&quot;action\&quot;: \&quot;allow\&quot;     },     \&quot;targets\&quot;: {       \&quot;users\&quot;: {         \&quot;inclusions\&quot;: [\&quot;ALL\&quot;]       },       \&quot;userGroups\&quot;: {         \&quot;exclusions\&quot;: [{USER_GROUP_ID}]       },       \&quot;resources\&quot;: [ {\&quot;type\&quot;: \&quot;user_portal\&quot; } ]     },     \&quot;conditions\&quot;:{       \&quot;ipAddressIn\&quot;: [{IP_LIST_ID}]     }   }&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AuthenticationPoliciesApi;
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
    Object conditions = null; // Conditions may be added to an authentication policy using the following conditional language:  ``` <conditions> ::= <expression> <expression> ::= <deviceEncrypted> | <deviceManaged> | <ipAddressIn> |                  <locationIn> | <notExpression> | <allExpression> |                  <anyExpression> <deviceEncrypted> ::= { \\\"deviceEncrypted\\\": <boolean> } <deviceManaged> ::= { \\\"deviceManaged\\\": <boolean> } <ipAddressIn> ::= { \\\"ipAddressIn\\\": [ <objectId>, ... ] } <locationIn> ::= { \\\"locationIn\\\": {                      \\\"countries\\\": [                        <iso_3166_country_code>, ...                      ]                    }                  } <notExpression> ::= { \\\"not\\\": <expression> } <allExpression> ::= { \\\"all\\\": [ <expression>, ... ] } <anyExpression> ::= { \\\"any\\\": [ <expression>, ... ] } ```  For example, to add a condition that applies to IP addresses in a given list, the following condition can be added:  ``` {\\\"ipAddressIn\\\": [ <ip_list_object_id> ]} ```  If you would rather exclude IP addresses in the given lists, the following condition could be added:  ``` {   \\\"not\\\": {     \\\"ipAddressIn\\\": [ <ip_list_object_id_1>, <ip_list_object_id_2> ]   } } ```  You may also include more than one condition and choose whether \\\"all\\\" or \\\"any\\\" of them must be met for the policy to apply:  ``` {   \\\"all\\\": [     {       \\\"ipAddressIn\\\": [ <ip_list_object_id>, ... ]     },     {       \\\"deviceManaged\\\": true     },     {       \\\"locationIn\\\": {         countries: [ <iso_3166_country_code>, ... ]       }     }   ] } ```
    Boolean disabled = true;
    AuthnPolicyEffect effect = new AuthnPolicyEffect();
    String id = "id_example";
    String name = "name_example";
    AuthnPolicyTargets targets = new AuthnPolicyTargets();
    AuthnPolicyType type = AuthnPolicyType.fromValue("user_portal");
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      AuthnPolicy result = client
              .authenticationPolicies
              .post()
              .description(description)
              .conditions(conditions)
              .disabled(disabled)
              .effect(effect)
              .id(id)
              .name(name)
              .targets(targets)
              .type(type)
              .xOrgId(xOrgId)
              .execute();
      System.out.println(result);
      System.out.println(result.getDescription());
      System.out.println(result.getConditions());
      System.out.println(result.getDisabled());
      System.out.println(result.getEffect());
      System.out.println(result.getId());
      System.out.println(result.getName());
      System.out.println(result.getTargets());
      System.out.println(result.getType());
    } catch (ApiException e) {
      System.err.println("Exception when calling AuthenticationPoliciesApi#post");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AuthnPolicy> response = client
              .authenticationPolicies
              .post()
              .description(description)
              .conditions(conditions)
              .disabled(disabled)
              .effect(effect)
              .id(id)
              .name(name)
              .targets(targets)
              .type(type)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AuthenticationPoliciesApi#post");
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
| **authnPolicy** | [**AuthnPolicy**](AuthnPolicy.md)|  | [optional] |

### Return type

[**AuthnPolicy**](AuthnPolicy.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Created |  -  |

