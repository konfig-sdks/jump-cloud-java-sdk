# GoogleEmmApi

All URIs are relative to *https://console.jumpcloud.com/api/v2*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**create**](GoogleEmmApi.md#create) | **POST** /google-emm/signup-urls | Get a Signup URL to enroll Google enterprise |
| [**createEnrollmentToken**](GoogleEmmApi.md#createEnrollmentToken) | **POST** /google-emm/enrollment-tokens | Create an enrollment token |
| [**createEnterprise**](GoogleEmmApi.md#createEnterprise) | **POST** /google-emm/enterprises | Create a Google Enterprise |
| [**createEnterprisesEnrollmentToken**](GoogleEmmApi.md#createEnterprisesEnrollmentToken) | **POST** /google-emm/enterprises/{enterpriseObjectId}/enrollment-tokens | Create an enrollment token for a given enterprise |
| [**createWebToken**](GoogleEmmApi.md#createWebToken) | **POST** /google-emm/web-tokens | Get a web token to render Google Play iFrame |
| [**deleteEnrollmentToken**](GoogleEmmApi.md#deleteEnrollmentToken) | **DELETE** /google-emm/enterprises/{enterpriseId}/enrollment-tokens/{tokenId} | Deletes an enrollment token for a given enterprise and token id |
| [**deleteEnterprise**](GoogleEmmApi.md#deleteEnterprise) | **DELETE** /google-emm/enterprises/{enterpriseId} | Delete a Google Enterprise |
| [**eraseDevice**](GoogleEmmApi.md#eraseDevice) | **POST** /google-emm/devices/{deviceId}/erase-device | Erase the Android Device |
| [**getConnectionStatus**](GoogleEmmApi.md#getConnectionStatus) | **GET** /google-emm/enterprises/{enterpriseId}/connection-status | Test connection with Google |
| [**getDevice**](GoogleEmmApi.md#getDevice) | **GET** /google-emm/devices/{deviceId} | Get device |
| [**getDeviceAndroidPolicy**](GoogleEmmApi.md#getDeviceAndroidPolicy) | **GET** /google-emm/devices/{deviceId}/policy_results | Get the policy JSON of a device |
| [**listDevices**](GoogleEmmApi.md#listDevices) | **GET** /google-emm/enterprises/{enterpriseObjectId}/devices | List devices |
| [**listEnrollmentTokens**](GoogleEmmApi.md#listEnrollmentTokens) | **GET** /google-emm/enterprises/{enterpriseObjectId}/enrollment-tokens | List enrollment tokens |
| [**listEnterprises**](GoogleEmmApi.md#listEnterprises) | **GET** /google-emm/enterprises | List Google Enterprises |
| [**lockDevice**](GoogleEmmApi.md#lockDevice) | **POST** /google-emm/devices/{deviceId}/lock | Lock device |
| [**patchEnterprise**](GoogleEmmApi.md#patchEnterprise) | **PATCH** /google-emm/enterprises/{enterpriseId} | Update a Google Enterprise |
| [**rebootDevice**](GoogleEmmApi.md#rebootDevice) | **POST** /google-emm/devices/{deviceId}/reboot | Reboot device |
| [**resetPassword**](GoogleEmmApi.md#resetPassword) | **POST** /google-emm/devices/{deviceId}/resetpassword | Reset Password of a device |


<a name="create"></a>
# **create**
> JumpcloudGoogleEmmSignupURL create().execute();

Get a Signup URL to enroll Google enterprise

Creates a Google EMM enterprise signup URL.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/google-emm/signup-urls \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.GoogleEmmApi;
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
      JumpcloudGoogleEmmSignupURL result = client
              .googleEmm
              .create()
              .execute();
      System.out.println(result);
      System.out.println(result.getName());
      System.out.println(result.getUrl());
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#create");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<JumpcloudGoogleEmmSignupURL> response = client
              .googleEmm
              .create()
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#create");
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

[**JumpcloudGoogleEmmSignupURL**](JumpcloudGoogleEmmSignupURL.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A successful response. |  -  |

<a name="createEnrollmentToken"></a>
# **createEnrollmentToken**
> JumpcloudGoogleEmmCreateEnrollmentTokenResponse createEnrollmentToken(jumpcloudGoogleEmmCreateEnrollmentTokenRequest).execute();

Create an enrollment token

Gets an enrollment token to enroll a device into Google EMM.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/google-emm/enrollment-tokens \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.GoogleEmmApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    JumpcloudGoogleEmmAllowPersonalUsage allowPersonalUsage = JumpcloudGoogleEmmAllowPersonalUsage.fromValue("PERSONAL_USAGE_ALLOWED");
    JumpcloudGoogleEmmCreatedWhere createdWhere = JumpcloudGoogleEmmCreatedWhere.fromValue("API");
    String displayName = "displayName_example";
    String duration = "duration_example";
    JumpcloudGoogleEmmEnrollmentType enrollmentType = JumpcloudGoogleEmmEnrollmentType.fromValue("WORK_PROFILE");
    byte[] enterpriseObjectId = null;
    Boolean oneTimeOnly = true;
    JumpcloudGoogleEmmProvisioningExtras provisioningExtras = new JumpcloudGoogleEmmProvisioningExtras();
    byte[] userObjectId = null;
    Boolean zeroTouch = true;
    try {
      JumpcloudGoogleEmmCreateEnrollmentTokenResponse result = client
              .googleEmm
              .createEnrollmentToken()
              .allowPersonalUsage(allowPersonalUsage)
              .createdWhere(createdWhere)
              .displayName(displayName)
              .duration(duration)
              .enrollmentType(enrollmentType)
              .enterpriseObjectId(enterpriseObjectId)
              .oneTimeOnly(oneTimeOnly)
              .provisioningExtras(provisioningExtras)
              .userObjectId(userObjectId)
              .zeroTouch(zeroTouch)
              .execute();
      System.out.println(result);
      System.out.println(result.getEnrollmentLink());
      System.out.println(result.getExpirationTime());
      System.out.println(result.getMetadata());
      System.out.println(result.getName());
      System.out.println(result.getQrCodeImage());
      System.out.println(result.getValue());
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#createEnrollmentToken");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<JumpcloudGoogleEmmCreateEnrollmentTokenResponse> response = client
              .googleEmm
              .createEnrollmentToken()
              .allowPersonalUsage(allowPersonalUsage)
              .createdWhere(createdWhere)
              .displayName(displayName)
              .duration(duration)
              .enrollmentType(enrollmentType)
              .enterpriseObjectId(enterpriseObjectId)
              .oneTimeOnly(oneTimeOnly)
              .provisioningExtras(provisioningExtras)
              .userObjectId(userObjectId)
              .zeroTouch(zeroTouch)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#createEnrollmentToken");
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
| **jumpcloudGoogleEmmCreateEnrollmentTokenRequest** | [**JumpcloudGoogleEmmCreateEnrollmentTokenRequest**](JumpcloudGoogleEmmCreateEnrollmentTokenRequest.md)|  | |

### Return type

[**JumpcloudGoogleEmmCreateEnrollmentTokenResponse**](JumpcloudGoogleEmmCreateEnrollmentTokenResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A successful response. |  -  |

<a name="createEnterprise"></a>
# **createEnterprise**
> JumpcloudGoogleEmmEnterprise createEnterprise(jumpcloudGoogleEmmCreateEnterpriseRequest).execute();

Create a Google Enterprise

Creates a Google EMM enterprise.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/google-emm/enterprises \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{ &#39;signupUrlName&#39;: &#39;string&#39;, &#39;enrollmentToken&#39;: &#39;string&#39; }&#39; \\ &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.GoogleEmmApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String enrollmentToken = "enrollmentToken_example";
    String signupUrlName = "signupUrlName_example";
    try {
      JumpcloudGoogleEmmEnterprise result = client
              .googleEmm
              .createEnterprise()
              .enrollmentToken(enrollmentToken)
              .signupUrlName(signupUrlName)
              .execute();
      System.out.println(result);
      System.out.println(result.getAllowDeviceEnrollment());
      System.out.println(result.getContactEmail());
      System.out.println(result.getCreatedAt());
      System.out.println(result.getDeviceGroupId());
      System.out.println(result.getDisplayName());
      System.out.println(result.getName());
      System.out.println(result.getObjectId());
      System.out.println(result.getOrganizationObjectId());
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#createEnterprise");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<JumpcloudGoogleEmmEnterprise> response = client
              .googleEmm
              .createEnterprise()
              .enrollmentToken(enrollmentToken)
              .signupUrlName(signupUrlName)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#createEnterprise");
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
| **jumpcloudGoogleEmmCreateEnterpriseRequest** | [**JumpcloudGoogleEmmCreateEnterpriseRequest**](JumpcloudGoogleEmmCreateEnterpriseRequest.md)|  | |

### Return type

[**JumpcloudGoogleEmmEnterprise**](JumpcloudGoogleEmmEnterprise.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A successful response. |  -  |

<a name="createEnterprisesEnrollmentToken"></a>
# **createEnterprisesEnrollmentToken**
> JumpcloudGoogleEmmEnrollmentToken createEnterprisesEnrollmentToken(enterpriseObjectId, enrollmentTokensCreateEnterprisesEnrollmentTokenRequest).execute();

Create an enrollment token for a given enterprise

Gets an enrollment token to enroll a device into Google EMM.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/google-emm/enterpries/{enterprise_object_id}/enrollment-tokens \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.GoogleEmmApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    byte[] enterpriseObjectId = null;
    JumpcloudGoogleEmmAllowPersonalUsage allowPersonalUsage = JumpcloudGoogleEmmAllowPersonalUsage.fromValue("PERSONAL_USAGE_ALLOWED");
    JumpcloudGoogleEmmCreatedWhere createdWhere = JumpcloudGoogleEmmCreatedWhere.fromValue("API");
    String displayName = "displayName_example";
    String duration = "duration_example";
    JumpcloudGoogleEmmEnrollmentType enrollmentType = JumpcloudGoogleEmmEnrollmentType.fromValue("WORK_PROFILE");
    Boolean oneTimeOnly = true;
    JumpcloudGoogleEmmProvisioningExtras provisioningExtras = new JumpcloudGoogleEmmProvisioningExtras();
    byte[] userObjectId = null;
    Boolean zeroTouch = true;
    try {
      JumpcloudGoogleEmmEnrollmentToken result = client
              .googleEmm
              .createEnterprisesEnrollmentToken(enterpriseObjectId)
              .allowPersonalUsage(allowPersonalUsage)
              .createdWhere(createdWhere)
              .displayName(displayName)
              .duration(duration)
              .enrollmentType(enrollmentType)
              .oneTimeOnly(oneTimeOnly)
              .provisioningExtras(provisioningExtras)
              .userObjectId(userObjectId)
              .zeroTouch(zeroTouch)
              .execute();
      System.out.println(result);
      System.out.println(result.getCreatedBy());
      System.out.println(result.getCreatedWhere());
      System.out.println(result.getDisplayName());
      System.out.println(result.getEnrollmentLink());
      System.out.println(result.getEnrollmentType());
      System.out.println(result.getExpirationTime());
      System.out.println(result.getId());
      System.out.println(result.getMetadata());
      System.out.println(result.getName());
      System.out.println(result.getOneTimeOnly());
      System.out.println(result.getQrCodeImage());
      System.out.println(result.getValue());
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#createEnterprisesEnrollmentToken");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<JumpcloudGoogleEmmEnrollmentToken> response = client
              .googleEmm
              .createEnterprisesEnrollmentToken(enterpriseObjectId)
              .allowPersonalUsage(allowPersonalUsage)
              .createdWhere(createdWhere)
              .displayName(displayName)
              .duration(duration)
              .enrollmentType(enrollmentType)
              .oneTimeOnly(oneTimeOnly)
              .provisioningExtras(provisioningExtras)
              .userObjectId(userObjectId)
              .zeroTouch(zeroTouch)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#createEnterprisesEnrollmentToken");
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
| **enterpriseObjectId** | **byte[]**|  | |
| **enrollmentTokensCreateEnterprisesEnrollmentTokenRequest** | [**EnrollmentTokensCreateEnterprisesEnrollmentTokenRequest**](EnrollmentTokensCreateEnterprisesEnrollmentTokenRequest.md)|  | |

### Return type

[**JumpcloudGoogleEmmEnrollmentToken**](JumpcloudGoogleEmmEnrollmentToken.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A successful response. |  -  |

<a name="createWebToken"></a>
# **createWebToken**
> JumpcloudGoogleEmmWebToken createWebToken(jumpcloudGoogleEmmCreateWebTokenRequest).execute();

Get a web token to render Google Play iFrame

Creates a web token to access an embeddable managed Google Play web UI for a given Google EMM enterprise.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/google-emm/web-tokens \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.GoogleEmmApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    byte[] enterpriseObjectId = null;
    JumpcloudGoogleEmmFeature iframeFeature = JumpcloudGoogleEmmFeature.fromValue("SOFTWARE_MANAGEMENT");
    String parentFrameUrl = "parentFrameUrl_example";
    try {
      JumpcloudGoogleEmmWebToken result = client
              .googleEmm
              .createWebToken()
              .enterpriseObjectId(enterpriseObjectId)
              .iframeFeature(iframeFeature)
              .parentFrameUrl(parentFrameUrl)
              .execute();
      System.out.println(result);
      System.out.println(result.getName());
      System.out.println(result.getValue());
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#createWebToken");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<JumpcloudGoogleEmmWebToken> response = client
              .googleEmm
              .createWebToken()
              .enterpriseObjectId(enterpriseObjectId)
              .iframeFeature(iframeFeature)
              .parentFrameUrl(parentFrameUrl)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#createWebToken");
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
| **jumpcloudGoogleEmmCreateWebTokenRequest** | [**JumpcloudGoogleEmmCreateWebTokenRequest**](JumpcloudGoogleEmmCreateWebTokenRequest.md)|  | |

### Return type

[**JumpcloudGoogleEmmWebToken**](JumpcloudGoogleEmmWebToken.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A successful response. |  -  |

<a name="deleteEnrollmentToken"></a>
# **deleteEnrollmentToken**
> JumpcloudGoogleEmmDeleteEnrollmentTokenResponse deleteEnrollmentToken(enterpriseId, tokenId).execute();

Deletes an enrollment token for a given enterprise and token id

Removes an Enrollment token for a given enterprise and token id.  #### Sample Request &#x60;&#x60;&#x60; curl -X DELETE https://console.jumpcloud.com/api/v2/enterprises/{enterprise_id}/enrollment-tokens/{token_id} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.GoogleEmmApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    byte[] enterpriseId = null;
    String tokenId = "tokenId_example";
    try {
      JumpcloudGoogleEmmDeleteEnrollmentTokenResponse result = client
              .googleEmm
              .deleteEnrollmentToken(enterpriseId, tokenId)
              .execute();
      System.out.println(result);
      System.out.println(result.getName());
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#deleteEnrollmentToken");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<JumpcloudGoogleEmmDeleteEnrollmentTokenResponse> response = client
              .googleEmm
              .deleteEnrollmentToken(enterpriseId, tokenId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#deleteEnrollmentToken");
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
| **enterpriseId** | **byte[]**|  | |
| **tokenId** | **String**|  | |

### Return type

[**JumpcloudGoogleEmmDeleteEnrollmentTokenResponse**](JumpcloudGoogleEmmDeleteEnrollmentTokenResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A successful response. |  -  |

<a name="deleteEnterprise"></a>
# **deleteEnterprise**
> Object deleteEnterprise(enterpriseId).execute();

Delete a Google Enterprise

Removes a Google EMM enterprise.   Warning: This is a destructive operation and will remove all data associated with Google EMM enterprise from JumpCloud including devices and applications associated with the given enterprise.  #### Sample Request &#x60;&#x60;&#x60; curl -X DELETE https://console.jumpcloud.com/api/v2/google-emm/devices/{enterpriseId} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.GoogleEmmApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    byte[] enterpriseId = null;
    try {
      Object result = client
              .googleEmm
              .deleteEnterprise(enterpriseId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#deleteEnterprise");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Object> response = client
              .googleEmm
              .deleteEnterprise(enterpriseId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#deleteEnterprise");
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
| **enterpriseId** | **byte[]**|  | |

### Return type

**Object**

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A successful response. |  -  |

<a name="eraseDevice"></a>
# **eraseDevice**
> Object eraseDevice(deviceId, body).execute();

Erase the Android Device

Removes the work profile and all policies from a personal/company-owned Android 8.0+ device. Company owned devices will be relinquished for personal use. Apps and data associated with the personal profile(s) are preserved.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/google-emm/devices/{deviceId}/erase-device \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.GoogleEmmApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    byte[] deviceId = null;
    try {
      Object result = client
              .googleEmm
              .eraseDevice(deviceId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#eraseDevice");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Object> response = client
              .googleEmm
              .eraseDevice(deviceId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#eraseDevice");
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
| **deviceId** | **byte[]**|  | |
| **body** | **Object**|  | |

### Return type

**Object**

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A successful response. |  -  |

<a name="getConnectionStatus"></a>
# **getConnectionStatus**
> JumpcloudGoogleEmmConnectionStatus getConnectionStatus(enterpriseId).execute();

Test connection with Google

Gives a connection status between JumpCloud and Google.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/google-emm/devices/{enterpriseId}/connection-status \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.GoogleEmmApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    byte[] enterpriseId = null;
    try {
      JumpcloudGoogleEmmConnectionStatus result = client
              .googleEmm
              .getConnectionStatus(enterpriseId)
              .execute();
      System.out.println(result);
      System.out.println(result.getEnterpriseId());
      System.out.println(result.getIsConnected());
      System.out.println(result.getOrganizationObjectId());
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#getConnectionStatus");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<JumpcloudGoogleEmmConnectionStatus> response = client
              .googleEmm
              .getConnectionStatus(enterpriseId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#getConnectionStatus");
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
| **enterpriseId** | **byte[]**|  | |

### Return type

[**JumpcloudGoogleEmmConnectionStatus**](JumpcloudGoogleEmmConnectionStatus.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A successful response. |  -  |

<a name="getDevice"></a>
# **getDevice**
> JumpcloudGoogleEmmDevice getDevice(deviceId).execute();

Get device

Gets a Google EMM enrolled device details.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/google-emm/devices/{deviceId} \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.GoogleEmmApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    byte[] deviceId = null;
    try {
      JumpcloudGoogleEmmDevice result = client
              .googleEmm
              .getDevice(deviceId)
              .execute();
      System.out.println(result);
      System.out.println(result.getDeviceId());
      System.out.println(result.getDeviceInformation());
      System.out.println(result.getName());
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#getDevice");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<JumpcloudGoogleEmmDevice> response = client
              .googleEmm
              .getDevice(deviceId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#getDevice");
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
| **deviceId** | **byte[]**|  | |

### Return type

[**JumpcloudGoogleEmmDevice**](JumpcloudGoogleEmmDevice.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A successful response. |  -  |

<a name="getDeviceAndroidPolicy"></a>
# **getDeviceAndroidPolicy**
> JumpcloudGoogleEmmDeviceAndroidPolicy getDeviceAndroidPolicy(deviceId).execute();

Get the policy JSON of a device

Gets an android JSON policy for a Google EMM enrolled device.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/google-emm/devices/{deviceId}/policy_results \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.GoogleEmmApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    byte[] deviceId = null;
    try {
      JumpcloudGoogleEmmDeviceAndroidPolicy result = client
              .googleEmm
              .getDeviceAndroidPolicy(deviceId)
              .execute();
      System.out.println(result);
      System.out.println(result.getPolicy());
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#getDeviceAndroidPolicy");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<JumpcloudGoogleEmmDeviceAndroidPolicy> response = client
              .googleEmm
              .getDeviceAndroidPolicy(deviceId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#getDeviceAndroidPolicy");
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
| **deviceId** | **byte[]**|  | |

### Return type

[**JumpcloudGoogleEmmDeviceAndroidPolicy**](JumpcloudGoogleEmmDeviceAndroidPolicy.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A successful response. |  -  |

<a name="listDevices"></a>
# **listDevices**
> JumpcloudGoogleEmmListDevicesResponse listDevices(enterpriseObjectId).limit(limit).skip(skip).filter(filter).execute();

List devices

Lists google EMM enrolled devices.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/google-emm/enterprises/{enterprise_object_id}/devices \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.GoogleEmmApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    byte[] enterpriseObjectId = null;
    String limit = "100"; // The number of records to return at once. Limited to 100.
    String skip = "0"; // The offset into the records to return.
    List<String> filter = Arrays.asList();
    try {
      JumpcloudGoogleEmmListDevicesResponse result = client
              .googleEmm
              .listDevices(enterpriseObjectId)
              .limit(limit)
              .skip(skip)
              .filter(filter)
              .execute();
      System.out.println(result);
      System.out.println(result.getCount());
      System.out.println(result.getDevices());
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#listDevices");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<JumpcloudGoogleEmmListDevicesResponse> response = client
              .googleEmm
              .listDevices(enterpriseObjectId)
              .limit(limit)
              .skip(skip)
              .filter(filter)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#listDevices");
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
| **enterpriseObjectId** | **byte[]**|  | |
| **limit** | **String**| The number of records to return at once. Limited to 100. | [optional] [default to 100] |
| **skip** | **String**| The offset into the records to return. | [optional] [default to 0] |
| **filter** | [**List&lt;String&gt;**](String.md)|  | [optional] |

### Return type

[**JumpcloudGoogleEmmListDevicesResponse**](JumpcloudGoogleEmmListDevicesResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A successful response. |  -  |

<a name="listEnrollmentTokens"></a>
# **listEnrollmentTokens**
> JumpcloudGoogleEmmListEnrollmentTokensResponse listEnrollmentTokens(enterpriseObjectId).limit(limit).skip(skip).filter(filter).sort(sort).execute();

List enrollment tokens

Lists active, unexpired enrollement tokens for a given enterprise.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/google-emm/enterprises/{enterprise_object_id}/enrollment-tokens \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.GoogleEmmApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    byte[] enterpriseObjectId = null;
    String limit = "100"; // The number of records to return at once. Limited to 100.
    String skip = "0"; // The offset into the records to return.
    List<String> filter = Arrays.asList();
    String sort = "sort_example"; // Use space separated sort parameters to sort the collection. Default sort is ascending. Prefix with - to sort descending.
    try {
      JumpcloudGoogleEmmListEnrollmentTokensResponse result = client
              .googleEmm
              .listEnrollmentTokens(enterpriseObjectId)
              .limit(limit)
              .skip(skip)
              .filter(filter)
              .sort(sort)
              .execute();
      System.out.println(result);
      System.out.println(result.getResults());
      System.out.println(result.getTotalCount());
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#listEnrollmentTokens");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<JumpcloudGoogleEmmListEnrollmentTokensResponse> response = client
              .googleEmm
              .listEnrollmentTokens(enterpriseObjectId)
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
      System.err.println("Exception when calling GoogleEmmApi#listEnrollmentTokens");
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
| **enterpriseObjectId** | **byte[]**|  | |
| **limit** | **String**| The number of records to return at once. Limited to 100. | [optional] [default to 100] |
| **skip** | **String**| The offset into the records to return. | [optional] [default to 0] |
| **filter** | [**List&lt;String&gt;**](String.md)|  | [optional] |
| **sort** | **String**| Use space separated sort parameters to sort the collection. Default sort is ascending. Prefix with - to sort descending. | [optional] |

### Return type

[**JumpcloudGoogleEmmListEnrollmentTokensResponse**](JumpcloudGoogleEmmListEnrollmentTokensResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A successful response. |  -  |

<a name="listEnterprises"></a>
# **listEnterprises**
> JumpcloudGoogleEmmListEnterprisesResponse listEnterprises().limit(limit).skip(skip).execute();

List Google Enterprises

Lists all Google EMM enterprises. An empty list indicates that the Organization is not configured with a Google EMM enterprise yet.    Note: Currently only one Google Enterprise per Organization is supported.  #### Sample Request &#x60;&#x60;&#x60; curl -X GET https://console.jumpcloud.com/api/v2/google-emm/enterprises \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.GoogleEmmApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String limit = "100"; // The number of records to return at once. Limited to 100.
    String skip = "0"; // The offset into the records to return.
    try {
      JumpcloudGoogleEmmListEnterprisesResponse result = client
              .googleEmm
              .listEnterprises()
              .limit(limit)
              .skip(skip)
              .execute();
      System.out.println(result);
      System.out.println(result.getCount());
      System.out.println(result.getEnterprises());
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#listEnterprises");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<JumpcloudGoogleEmmListEnterprisesResponse> response = client
              .googleEmm
              .listEnterprises()
              .limit(limit)
              .skip(skip)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#listEnterprises");
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
| **limit** | **String**| The number of records to return at once. Limited to 100. | [optional] [default to 100] |
| **skip** | **String**| The offset into the records to return. | [optional] [default to 0] |

### Return type

[**JumpcloudGoogleEmmListEnterprisesResponse**](JumpcloudGoogleEmmListEnterprisesResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A successful response. |  -  |

<a name="lockDevice"></a>
# **lockDevice**
> Object lockDevice(deviceId, body).execute();

Lock device

Locks a Google EMM enrolled device, as if the lock screen timeout had expired.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/google-emm/devices/{deviceId}/lock \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.GoogleEmmApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    byte[] deviceId = null;
    try {
      Object result = client
              .googleEmm
              .lockDevice(deviceId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#lockDevice");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Object> response = client
              .googleEmm
              .lockDevice(deviceId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#lockDevice");
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
| **deviceId** | **byte[]**|  | |
| **body** | **Object**|  | |

### Return type

**Object**

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A successful response. |  -  |

<a name="patchEnterprise"></a>
# **patchEnterprise**
> JumpcloudGoogleEmmEnterprise patchEnterprise(enterpriseId, enterprisesPatchEnterpriseRequest).execute();

Update a Google Enterprise

Updates a Google EMM enterprise details.  #### Sample Request &#x60;&#x60;&#x60; curl -X PATCH https://console.jumpcloud.com/api/v2/google-emm/enterprises \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{ &#39;allowDeviceEnrollment&#39;: true, &#39;deviceGroupId&#39;: &#39;string&#39; }&#39; \\ &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.GoogleEmmApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    byte[] enterpriseId = null;
    Boolean allowDeviceEnrollment = true;
    byte[] deviceGroupId = null;
    try {
      JumpcloudGoogleEmmEnterprise result = client
              .googleEmm
              .patchEnterprise(enterpriseId)
              .allowDeviceEnrollment(allowDeviceEnrollment)
              .deviceGroupId(deviceGroupId)
              .execute();
      System.out.println(result);
      System.out.println(result.getAllowDeviceEnrollment());
      System.out.println(result.getContactEmail());
      System.out.println(result.getCreatedAt());
      System.out.println(result.getDeviceGroupId());
      System.out.println(result.getDisplayName());
      System.out.println(result.getName());
      System.out.println(result.getObjectId());
      System.out.println(result.getOrganizationObjectId());
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#patchEnterprise");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<JumpcloudGoogleEmmEnterprise> response = client
              .googleEmm
              .patchEnterprise(enterpriseId)
              .allowDeviceEnrollment(allowDeviceEnrollment)
              .deviceGroupId(deviceGroupId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#patchEnterprise");
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
| **enterpriseId** | **byte[]**|  | |
| **enterprisesPatchEnterpriseRequest** | [**EnterprisesPatchEnterpriseRequest**](EnterprisesPatchEnterpriseRequest.md)|  | |

### Return type

[**JumpcloudGoogleEmmEnterprise**](JumpcloudGoogleEmmEnterprise.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A successful response. |  -  |

<a name="rebootDevice"></a>
# **rebootDevice**
> Object rebootDevice(deviceId, body).execute();

Reboot device

Reboots a Google EMM enrolled device. Only supported on fully managed devices running Android 7.0 or higher.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/google-emm/devices/{deviceId}/reboot \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\ &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.GoogleEmmApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    byte[] deviceId = null;
    try {
      Object result = client
              .googleEmm
              .rebootDevice(deviceId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#rebootDevice");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Object> response = client
              .googleEmm
              .rebootDevice(deviceId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#rebootDevice");
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
| **deviceId** | **byte[]**|  | |
| **body** | **Object**|  | |

### Return type

**Object**

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A successful response. |  -  |

<a name="resetPassword"></a>
# **resetPassword**
> Object resetPassword(deviceId, devicesResetPasswordRequest).execute();

Reset Password of a device

Reset the user&#39;s password of a Google EMM enrolled device.  #### Sample Request &#x60;&#x60;&#x60; curl -X POST https://console.jumpcloud.com/api/v2/google-emm/devices/{deviceId}/resetpassword \\   -H &#39;Accept: application/json&#39; \\   -H &#39;Content-Type: application/json&#39; \\   -H &#39;x-api-key: {API_KEY}&#39; \\   -d &#39;{ &#39;new_password&#39; : &#39;string&#39; }&#39; \\ &#x60;&#x60;&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.GoogleEmmApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    byte[] deviceId = null;
    List<String> flags = Arrays.asList();
    String newPassword = "newPassword_example"; // Not logging as it contains sensitive information.
    try {
      Object result = client
              .googleEmm
              .resetPassword(deviceId)
              .flags(flags)
              .newPassword(newPassword)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#resetPassword");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Object> response = client
              .googleEmm
              .resetPassword(deviceId)
              .flags(flags)
              .newPassword(newPassword)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling GoogleEmmApi#resetPassword");
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
| **deviceId** | **byte[]**|  | |
| **devicesResetPasswordRequest** | [**DevicesResetPasswordRequest**](DevicesResetPasswordRequest.md)|  | |

### Return type

**Object**

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A successful response. |  -  |

