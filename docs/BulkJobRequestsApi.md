# BulkJobRequestsApi

All URIs are relative to *https://console.jumpcloud.com/api/v2*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**userExpires**](BulkJobRequestsApi.md#userExpires) | **POST** /bulk/user/expires | Bulk Expire Users |
| [**userStatesCreate**](BulkJobRequestsApi.md#userStatesCreate) | **POST** /bulk/userstates | Create Scheduled Userstate Job |
| [**userStatesDelete**](BulkJobRequestsApi.md#userStatesDelete) | **DELETE** /bulk/userstates/{id} | Delete Scheduled Userstate Job |
| [**userStatesGetNextScheduled**](BulkJobRequestsApi.md#userStatesGetNextScheduled) | **GET** /bulk/userstates/eventlist/next | Get the next scheduled state change for a list of users |
| [**userStatesList**](BulkJobRequestsApi.md#userStatesList) | **GET** /bulk/userstates | List Scheduled Userstate Change Jobs |
| [**userUnlocks**](BulkJobRequestsApi.md#userUnlocks) | **POST** /bulk/user/unlocks | Bulk Unlock Users |
| [**usersCreate**](BulkJobRequestsApi.md#usersCreate) | **POST** /bulk/users | Bulk Users Create |
| [**usersCreateResults**](BulkJobRequestsApi.md#usersCreateResults) | **GET** /bulk/users/{job_id}/results | List Bulk Users Results |
| [**usersUpdate**](BulkJobRequestsApi.md#usersUpdate) | **PATCH** /bulk/users | Bulk Users Update |


<a name="userExpires"></a>
# **userExpires**
> JobId userExpires().xOrgId(xOrgId).bulkUserExpire(bulkUserExpire).execute();

Bulk Expire Users

The endpoint allows you to start a bulk job to asynchronously expire users.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.BulkJobRequestsApi;
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
    try {
      JobId result = client
              .bulkJobRequests
              .userExpires()
              .xOrgId(xOrgId)
              .execute();
      System.out.println(result);
      System.out.println(result.getJobId());
    } catch (ApiException e) {
      System.err.println("Exception when calling BulkJobRequestsApi#userExpires");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<JobId> response = client
              .bulkJobRequests
              .userExpires()
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling BulkJobRequestsApi#userExpires");
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
| **bulkUserExpire** | [**List&lt;BulkUserExpire&gt;**](BulkUserExpire.md)|  | [optional] |

### Return type

[**JobId**](JobId.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Created |  -  |

<a name="userStatesCreate"></a>
# **userStatesCreate**
> List&lt;ScheduledUserstateResult&gt; userStatesCreate().xOrgId(xOrgId).bulkScheduledStatechangeCreate(bulkScheduledStatechangeCreate).execute();

Create Scheduled Userstate Job

This endpoint allows you to create scheduled statechange jobs. #### Sample Request &#x60;&#x60;&#x60; curl -X POST \&quot;https://console.jumpcloud.com/api/v2/bulk/userstates\&quot; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;Accept: application/json&#39; \\   -d &#39;{     \&quot;user_ids\&quot;: [\&quot;{User_ID_1}\&quot;, \&quot;{User_ID_2}\&quot;, \&quot;{User_ID_3}\&quot;],     \&quot;state\&quot;: \&quot;SUSPENDED\&quot;,     \&quot;start_date\&quot;: \&quot;2000-01-01T00:00:00.000Z\&quot;   }&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.BulkJobRequestsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    OffsetDateTime startDate = OffsetDateTime.now(); // Date and time that scheduled action should occur
    String state = "ACTIVATED"; // The state to move the user(s) to
    List<String> userIds = Arrays.asList(); // Array of system user ids to schedule for a state change
    String activationEmailOverride = "activationEmailOverride_example"; // Send the activation or welcome email to the specified email address upon activation. Can only be used with a single user_id and scheduled activation. This field will be ignored if `send_activation_emails` is explicitly set to false.
    Boolean sendActivationEmails = true; // Set to true to send activation or welcome email(s) to each user_id upon activation. Set to false to suppress emails. Can only be used with scheduled activation(s).
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      List<ScheduledUserstateResult> result = client
              .bulkJobRequests
              .userStatesCreate(startDate, state, userIds)
              .activationEmailOverride(activationEmailOverride)
              .sendActivationEmails(sendActivationEmails)
              .xOrgId(xOrgId)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling BulkJobRequestsApi#userStatesCreate");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<ScheduledUserstateResult>> response = client
              .bulkJobRequests
              .userStatesCreate(startDate, state, userIds)
              .activationEmailOverride(activationEmailOverride)
              .sendActivationEmails(sendActivationEmails)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling BulkJobRequestsApi#userStatesCreate");
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
| **bulkScheduledStatechangeCreate** | [**BulkScheduledStatechangeCreate**](BulkScheduledStatechangeCreate.md)|  | [optional] |

### Return type

[**List&lt;ScheduledUserstateResult&gt;**](ScheduledUserstateResult.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Created |  -  |

<a name="userStatesDelete"></a>
# **userStatesDelete**
> userStatesDelete(id).xOrgId(xOrgId).execute();

Delete Scheduled Userstate Job

This endpoint deletes a scheduled statechange job. #### Sample Request &#x60;&#x60;&#x60; curl -X DELETE \&quot;https://console.jumpcloud.com/api/v2/bulk/userstates/{ScheduledJob_ID}\&quot; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;Accept: application/json&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.BulkJobRequestsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String id = "id_example"; // Unique identifier of the scheduled statechange job.
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      client
              .bulkJobRequests
              .userStatesDelete(id)
              .xOrgId(xOrgId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling BulkJobRequestsApi#userStatesDelete");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .bulkJobRequests
              .userStatesDelete(id)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling BulkJobRequestsApi#userStatesDelete");
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
| **id** | **String**| Unique identifier of the scheduled statechange job. | |
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
| **204** | No Content |  -  |

<a name="userStatesGetNextScheduled"></a>
# **userStatesGetNextScheduled**
> BulkUserStatesGetNextScheduledResponse userStatesGetNextScheduled(users).limit(limit).skip(skip).execute();

Get the next scheduled state change for a list of users

This endpoint is used to lookup the next upcoming scheduled state change for each user in the given list. The users parameter is limited to 100 items per request. The results are also limited to 100 items. This endpoint returns a max of 1 event per state per user. For example, if a user has 3 ACTIVATED events scheduled it will return the next upcoming activation event. However, if a user also has a SUSPENDED event scheduled along with the ACTIVATED events it will return the next upcoming activation event _and_ the next upcoming suspension event.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.BulkJobRequestsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    List<String> users = Arrays.asList(); // A list of system user IDs, limited to 100 items.
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    Integer skip = 0; // The offset into the records to return.
    try {
      BulkUserStatesGetNextScheduledResponse result = client
              .bulkJobRequests
              .userStatesGetNextScheduled(users)
              .limit(limit)
              .skip(skip)
              .execute();
      System.out.println(result);
      System.out.println(result.getEventsCount());
      System.out.println(result.getResults());
    } catch (ApiException e) {
      System.err.println("Exception when calling BulkJobRequestsApi#userStatesGetNextScheduled");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<BulkUserStatesGetNextScheduledResponse> response = client
              .bulkJobRequests
              .userStatesGetNextScheduled(users)
              .limit(limit)
              .skip(skip)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling BulkJobRequestsApi#userStatesGetNextScheduled");
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
| **users** | [**List&lt;String&gt;**](String.md)| A list of system user IDs, limited to 100 items. | |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |

### Return type

[**BulkUserStatesGetNextScheduledResponse**](BulkUserStatesGetNextScheduledResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="userStatesList"></a>
# **userStatesList**
> List&lt;ScheduledUserstateResult&gt; userStatesList().limit(limit).filter(filter).skip(skip).xOrgId(xOrgId).userid(userid).execute();

List Scheduled Userstate Change Jobs

The endpoint allows you to list scheduled statechange jobs. #### Sample Request &#x60;&#x60;&#x60; curl -X GET \&quot;https://console.jumpcloud.com/api/v2/bulk/userstates\&quot; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;Accept: application/json&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.BulkJobRequestsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    Integer skip = 0; // The offset into the records to return.
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    String userid = "userid_example"; // The systemuser id to filter by.
    try {
      List<ScheduledUserstateResult> result = client
              .bulkJobRequests
              .userStatesList()
              .limit(limit)
              .filter(filter)
              .skip(skip)
              .xOrgId(xOrgId)
              .userid(userid)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling BulkJobRequestsApi#userStatesList");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<ScheduledUserstateResult>> response = client
              .bulkJobRequests
              .userStatesList()
              .limit(limit)
              .filter(filter)
              .skip(skip)
              .xOrgId(xOrgId)
              .userid(userid)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling BulkJobRequestsApi#userStatesList");
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
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **filter** | [**List&lt;String&gt;**](String.md)| A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; | [optional] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **userid** | **String**| The systemuser id to filter by. | [optional] |

### Return type

[**List&lt;ScheduledUserstateResult&gt;**](ScheduledUserstateResult.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="userUnlocks"></a>
# **userUnlocks**
> JobId userUnlocks().xOrgId(xOrgId).bulkUserUnlock(bulkUserUnlock).execute();

Bulk Unlock Users

The endpoint allows you to start a bulk job to asynchronously unlock users.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.BulkJobRequestsApi;
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
    try {
      JobId result = client
              .bulkJobRequests
              .userUnlocks()
              .xOrgId(xOrgId)
              .execute();
      System.out.println(result);
      System.out.println(result.getJobId());
    } catch (ApiException e) {
      System.err.println("Exception when calling BulkJobRequestsApi#userUnlocks");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<JobId> response = client
              .bulkJobRequests
              .userUnlocks()
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling BulkJobRequestsApi#userUnlocks");
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
| **bulkUserUnlock** | [**List&lt;BulkUserUnlock&gt;**](BulkUserUnlock.md)|  | [optional] |

### Return type

[**JobId**](JobId.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Created |  -  |

<a name="usersCreate"></a>
# **usersCreate**
> JobId usersCreate().xOrgId(xOrgId).creationSource(creationSource).bulkUserCreate(bulkUserCreate).execute();

Bulk Users Create

The endpoint allows you to create a bulk job to asynchronously create users. See [Create a System User](https://docs.jumpcloud.com/api/1.0/index.html#operation/systemusers_post) for the full list of attributes.  #### Default User State The &#x60;state&#x60; of each user in the request can be explicitly passed in or omitted. If &#x60;state&#x60; is omitted, then the user will get created using the value returned from the [Get an Organization](https://docs.jumpcloud.com/api/1.0/index.html#operation/organizations_get) endpoint. The default user state for bulk created users depends on the &#x60;creation-source&#x60; header. For &#x60;creation-source:jumpcloud:bulk&#x60; the default state is stored in &#x60;settings.newSystemUserStateDefaults.csvImport&#x60;. For other &#x60;creation-source&#x60; header values, the default state is stored in &#x60;settings.newSystemUserStateDefaults.applicationImport&#x60;  These default state values can be changed in the admin portal settings or by using the [Update an Organization](https://docs.jumpcloud.com/api/1.0/index.html#operation/organization_put) endpoint.  #### Sample Request  &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/bulk/users \\ -H &#39;Accept: application/json&#39; \\ -H &#39;Content-Type: application/json&#39; \\ -H &#39;x-api-key: {API_KEY}&#39; \\ -d &#39;[   {     \&quot;email\&quot;:\&quot;{email}\&quot;,     \&quot;firstname\&quot;:\&quot;{firstname}\&quot;,     \&quot;lastname\&quot;:\&quot;{firstname}\&quot;,     \&quot;username\&quot;:\&quot;{username}\&quot;,     \&quot;attributes\&quot;:[       {         \&quot;name\&quot;:\&quot;EmployeeID\&quot;,         \&quot;value\&quot;:\&quot;0000\&quot;       },       {         \&quot;name\&quot;:\&quot;Custom\&quot;,         \&quot;value\&quot;:\&quot;attribute\&quot;       }     ]   } ]&#39; &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.BulkJobRequestsApi;
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
    String creationSource = "jumpcloud:gapps"; // Defines the creation-source header for gapps, o365 and workdays requests. If the header isn't sent, the default value is `jumpcloud:bulk`, if you send the header with a malformed value you receive a 400 error. 
    try {
      JobId result = client
              .bulkJobRequests
              .usersCreate()
              .xOrgId(xOrgId)
              .creationSource(creationSource)
              .execute();
      System.out.println(result);
      System.out.println(result.getJobId());
    } catch (ApiException e) {
      System.err.println("Exception when calling BulkJobRequestsApi#usersCreate");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<JobId> response = client
              .bulkJobRequests
              .usersCreate()
              .xOrgId(xOrgId)
              .creationSource(creationSource)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling BulkJobRequestsApi#usersCreate");
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
| **creationSource** | **String**| Defines the creation-source header for gapps, o365 and workdays requests. If the header isn&#39;t sent, the default value is &#x60;jumpcloud:bulk&#x60;, if you send the header with a malformed value you receive a 400 error.  | [optional] [default to jumpcloud:bulk] [enum: jumpcloud:gapps, jumpcloud:o365, jumpcloud:workday, jumpcloud:scim, jumpcloud:bulk, jumpcloud:custom_integration] |
| **bulkUserCreate** | [**List&lt;BulkUserCreate&gt;**](BulkUserCreate.md)|  | [optional] |

### Return type

[**JobId**](JobId.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** |  |  -  |

<a name="usersCreateResults"></a>
# **usersCreateResults**
> List&lt;JobWorkresult&gt; usersCreateResults(jobId).limit(limit).skip(skip).xOrgId(xOrgId).execute();

List Bulk Users Results

This endpoint will return the results of particular user import or update job request.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET \\   https://console.jumpcloud.com/api/v2/bulk/users/{ImportJobID}/results \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39;   &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.BulkJobRequestsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String jobId = "jobId_example";
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    Integer skip = 0; // The offset into the records to return.
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      List<JobWorkresult> result = client
              .bulkJobRequests
              .usersCreateResults(jobId)
              .limit(limit)
              .skip(skip)
              .xOrgId(xOrgId)
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling BulkJobRequestsApi#usersCreateResults");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<JobWorkresult>> response = client
              .bulkJobRequests
              .usersCreateResults(jobId)
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
      System.err.println("Exception when calling BulkJobRequestsApi#usersCreateResults");
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
| **jobId** | **String**|  | |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |

### Return type

[**List&lt;JobWorkresult&gt;**](JobWorkresult.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="usersUpdate"></a>
# **usersUpdate**
> JobId usersUpdate().xOrgId(xOrgId).bulkUserUpdate(bulkUserUpdate).execute();

Bulk Users Update

The endpoint allows you to create a bulk job to asynchronously update users. See [Update a System User](https://docs.jumpcloud.com/api/1.0/index.html#operation/systemusers_put) for full list of attributes.  #### Sample Request  &#x60;&#x60;&#x60; curl -X PATCH https://console.jumpcloud.com/api/v2/bulk/users \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;[  {    \&quot;id\&quot;:\&quot;5be9fb4ddb01290001e85109\&quot;,   \&quot;firstname\&quot;:\&quot;{UPDATED_FIRSTNAME}\&quot;,   \&quot;department\&quot;:\&quot;{UPDATED_DEPARTMENT}\&quot;,   \&quot;attributes\&quot;:[    {\&quot;name\&quot;:\&quot;Custom\&quot;,\&quot;value\&quot;:\&quot;{ATTRIBUTE_VALUE}\&quot;}   ]  },  {    \&quot;id\&quot;:\&quot;5be9fb4ddb01290001e85109\&quot;,   \&quot;firstname\&quot;:\&quot;{UPDATED_FIRSTNAME}\&quot;,   \&quot;costCenter\&quot;:\&quot;{UPDATED_COST_CENTER}\&quot;,   \&quot;phoneNumbers\&quot;:[    {\&quot;type\&quot;:\&quot;home\&quot;,\&quot;number\&quot;:\&quot;{HOME_PHONE_NUMBER}\&quot;},    {\&quot;type\&quot;:\&quot;work\&quot;,\&quot;number\&quot;:\&quot;{WORK_PHONE_NUMBER}\&quot;}   ]  } ] &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.BulkJobRequestsApi;
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
    try {
      JobId result = client
              .bulkJobRequests
              .usersUpdate()
              .xOrgId(xOrgId)
              .execute();
      System.out.println(result);
      System.out.println(result.getJobId());
    } catch (ApiException e) {
      System.err.println("Exception when calling BulkJobRequestsApi#usersUpdate");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<JobId> response = client
              .bulkJobRequests
              .usersUpdate()
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling BulkJobRequestsApi#usersUpdate");
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
| **bulkUserUpdate** | [**List&lt;BulkUserUpdate&gt;**](BulkUserUpdate.md)|  | [optional] |

### Return type

[**JobId**](JobId.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** |  |  -  |

