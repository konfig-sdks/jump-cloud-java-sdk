# CustomEmailsApi

All URIs are relative to *https://console.jumpcloud.com/api/v2*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**create**](CustomEmailsApi.md#create) | **POST** /customemails | Create custom email configuration |
| [**destroy**](CustomEmailsApi.md#destroy) | **DELETE** /customemails/{custom_email_type} | Delete custom email configuration |
| [**getTemplates**](CustomEmailsApi.md#getTemplates) | **GET** /customemail/templates | List custom email templates |
| [**read**](CustomEmailsApi.md#read) | **GET** /customemails/{custom_email_type} | Get custom email configuration |
| [**update**](CustomEmailsApi.md#update) | **PUT** /customemails/{custom_email_type} | Update custom email configuration |


<a name="create"></a>
# **create**
> CustomEmail create().xOrgId(xOrgId).customEmail(customEmail).execute();

Create custom email configuration

Create the custom email configuration for the specified custom email type.  This action is only available to paying customers.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.CustomEmailsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String subject = "subject_example";
    CustomEmailType type = CustomEmailType.fromValue("activate_gapps_user");
    String title = "title_example";
    String body = "body_example";
    String button = "button_example";
    String header = "header_example";
    String id = "id_example";
    String nextStepContactInfo = "nextStepContactInfo_example";
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      CustomEmail result = client
              .customEmails
              .create(subject, type)
              .title(title)
              .body(body)
              .button(button)
              .header(header)
              .id(id)
              .nextStepContactInfo(nextStepContactInfo)
              .xOrgId(xOrgId)
              .execute();
      System.out.println(result);
      System.out.println(result.getTitle());
      System.out.println(result.getBody());
      System.out.println(result.getButton());
      System.out.println(result.getHeader());
      System.out.println(result.getId());
      System.out.println(result.getNextStepContactInfo());
      System.out.println(result.getSubject());
      System.out.println(result.getType());
    } catch (ApiException e) {
      System.err.println("Exception when calling CustomEmailsApi#create");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<CustomEmail> response = client
              .customEmails
              .create(subject, type)
              .title(title)
              .body(body)
              .button(button)
              .header(header)
              .id(id)
              .nextStepContactInfo(nextStepContactInfo)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling CustomEmailsApi#create");
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
| **customEmail** | [**CustomEmail**](CustomEmail.md)|  | [optional] |

### Return type

[**CustomEmail**](CustomEmail.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | OK |  -  |

<a name="destroy"></a>
# **destroy**
> destroy(customEmailType).xOrgId(xOrgId).execute();

Delete custom email configuration

Delete the custom email configuration for the specified custom email type

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.CustomEmailsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String customEmailType = "customEmailType_example";
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      client
              .customEmails
              .destroy(customEmailType)
              .xOrgId(xOrgId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling CustomEmailsApi#destroy");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .customEmails
              .destroy(customEmailType)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling CustomEmailsApi#destroy");
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
| **customEmailType** | **String**|  | |
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

<a name="getTemplates"></a>
# **getTemplates**
> List&lt;CustomEmailTemplate&gt; getTemplates().execute();

List custom email templates

Get the list of custom email templates

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.CustomEmailsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    try {
      List<CustomEmailTemplate> result = client
              .customEmails
              .getTemplates()
              .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CustomEmailsApi#getTemplates");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<List<CustomEmailTemplate>> response = client
              .customEmails
              .getTemplates()
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling CustomEmailsApi#getTemplates");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;CustomEmailTemplate&gt;**](CustomEmailTemplate.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="read"></a>
# **read**
> CustomEmail read(customEmailType).xOrgId(xOrgId).execute();

Get custom email configuration

Get the custom email configuration for the specified custom email type

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.CustomEmailsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String customEmailType = "customEmailType_example";
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      CustomEmail result = client
              .customEmails
              .read(customEmailType)
              .xOrgId(xOrgId)
              .execute();
      System.out.println(result);
      System.out.println(result.getTitle());
      System.out.println(result.getBody());
      System.out.println(result.getButton());
      System.out.println(result.getHeader());
      System.out.println(result.getId());
      System.out.println(result.getNextStepContactInfo());
      System.out.println(result.getSubject());
      System.out.println(result.getType());
    } catch (ApiException e) {
      System.err.println("Exception when calling CustomEmailsApi#read");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<CustomEmail> response = client
              .customEmails
              .read(customEmailType)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling CustomEmailsApi#read");
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
| **customEmailType** | **String**|  | |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |

### Return type

[**CustomEmail**](CustomEmail.md)

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
> CustomEmail update(customEmailType).xOrgId(xOrgId).customEmail(customEmail).execute();

Update custom email configuration

Update the custom email configuration for the specified custom email type.  This action is only available to paying customers.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.CustomEmailsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String subject = "subject_example";
    CustomEmailType type = CustomEmailType.fromValue("activate_gapps_user");
    String customEmailType = "customEmailType_example";
    String title = "title_example";
    String body = "body_example";
    String button = "button_example";
    String header = "header_example";
    String id = "id_example";
    String nextStepContactInfo = "nextStepContactInfo_example";
    String xOrgId = "xOrgId_example"; // Organization identifier that can be obtained from console settings.
    try {
      CustomEmail result = client
              .customEmails
              .update(subject, type, customEmailType)
              .title(title)
              .body(body)
              .button(button)
              .header(header)
              .id(id)
              .nextStepContactInfo(nextStepContactInfo)
              .xOrgId(xOrgId)
              .execute();
      System.out.println(result);
      System.out.println(result.getTitle());
      System.out.println(result.getBody());
      System.out.println(result.getButton());
      System.out.println(result.getHeader());
      System.out.println(result.getId());
      System.out.println(result.getNextStepContactInfo());
      System.out.println(result.getSubject());
      System.out.println(result.getType());
    } catch (ApiException e) {
      System.err.println("Exception when calling CustomEmailsApi#update");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<CustomEmail> response = client
              .customEmails
              .update(subject, type, customEmailType)
              .title(title)
              .body(body)
              .button(button)
              .header(header)
              .id(id)
              .nextStepContactInfo(nextStepContactInfo)
              .xOrgId(xOrgId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling CustomEmailsApi#update");
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
| **customEmailType** | **String**|  | |
| **xOrgId** | **String**| Organization identifier that can be obtained from console settings. | [optional] |
| **customEmail** | [**CustomEmail**](CustomEmail.md)|  | [optional] |

### Return type

[**CustomEmail**](CustomEmail.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

