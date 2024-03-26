# ProvidersApi

All URIs are relative to *https://console.jumpcloud.com/api/v2*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**casesMetadata**](ProvidersApi.md#casesMetadata) | **GET** /cases/metadata | Get the metadata for cases |
| [**createConfiguration**](ProvidersApi.md#createConfiguration) | **POST** /providers/{provider_id}/integrations/autotask | Creates a new Autotask integration for the provider |
| [**createConfiguration_0**](ProvidersApi.md#createConfiguration_0) | **POST** /providers/{provider_id}/integrations/connectwise | Creates a new ConnectWise integration for the provider |
| [**createConfiguration_1**](ProvidersApi.md#createConfiguration_1) | **POST** /providers/{provider_id}/integrations/syncro | Creates a new Syncro integration for the provider |
| [**createOrg**](ProvidersApi.md#createOrg) | **POST** /providers/{provider_id}/organizations | Create Provider Organization |
| [**delete**](ProvidersApi.md#delete) | **DELETE** /providers/{provider_id}/policygrouptemplates/{id} | Deletes policy group template. |
| [**deleteConfiguration**](ProvidersApi.md#deleteConfiguration) | **DELETE** /integrations/autotask/{UUID} | Delete Autotask Integration |
| [**deleteConfiguration_0**](ProvidersApi.md#deleteConfiguration_0) | **DELETE** /integrations/connectwise/{UUID} | Delete ConnectWise Integration |
| [**deleteConfiguration_1**](ProvidersApi.md#deleteConfiguration_1) | **DELETE** /integrations/syncro/{UUID} | Delete Syncro Integration |
| [**get**](ProvidersApi.md#get) | **GET** /providers/{provider_id}/policygrouptemplates/{id} | Gets a provider&#39;s policy group template. |
| [**getConfiguration**](ProvidersApi.md#getConfiguration) | **GET** /integrations/autotask/{UUID} | Retrieve Autotask Integration Configuration |
| [**getConfiguration_0**](ProvidersApi.md#getConfiguration_0) | **GET** /integrations/connectwise/{UUID} | Retrieve ConnectWise Integration Configuration |
| [**getConfiguration_1**](ProvidersApi.md#getConfiguration_1) | **GET** /integrations/syncro/{UUID} | Retrieve Syncro Integration Configuration |
| [**getConfiguredPolicyTemplate**](ProvidersApi.md#getConfiguredPolicyTemplate) | **GET** /providers/{provider_id}/configuredpolicytemplates/{id} | Retrieve a configured policy template by id. |
| [**getContract**](ProvidersApi.md#getContract) | **GET** /providers/{provider_id}/billing/contract | Retrieve contract for a Provider |
| [**getDetails**](ProvidersApi.md#getDetails) | **GET** /providers/{provider_id}/billing/details | Retrieve billing details for a Provider |
| [**getProvider**](ProvidersApi.md#getProvider) | **GET** /providers/{provider_id} | Retrieve Provider |
| [**list**](ProvidersApi.md#list) | **GET** /providers/{provider_id}/policygrouptemplates | List a provider&#39;s policy group templates. |
| [**listAdministrators**](ProvidersApi.md#listAdministrators) | **GET** /providers/{provider_id}/administrators | List Provider Administrators |
| [**listConfiguredPolicyTemplates**](ProvidersApi.md#listConfiguredPolicyTemplates) | **GET** /providers/{provider_id}/configuredpolicytemplates | List a provider&#39;s configured policy templates. |
| [**listMembers**](ProvidersApi.md#listMembers) | **GET** /providers/{provider_id}/policygrouptemplates/{id}/members | Gets the list of members from a policy group template. |
| [**listOrganizations**](ProvidersApi.md#listOrganizations) | **GET** /providers/{provider_id}/organizations | List Provider Organizations |
| [**patchMappings**](ProvidersApi.md#patchMappings) | **PATCH** /integrations/autotask/{UUID}/mappings | Create, edit, and/or delete Autotask Mappings |
| [**patchMappings_0**](ProvidersApi.md#patchMappings_0) | **PATCH** /integrations/connectwise/{UUID}/mappings | Create, edit, and/or delete ConnectWise Mappings |
| [**patchMappings_1**](ProvidersApi.md#patchMappings_1) | **PATCH** /integrations/syncro/{UUID}/mappings | Create, edit, and/or delete Syncro Mappings |
| [**patchSettings**](ProvidersApi.md#patchSettings) | **PATCH** /integrations/autotask/{UUID}/settings | Create, edit, and/or delete Autotask Integration settings |
| [**patchSettings_0**](ProvidersApi.md#patchSettings_0) | **PATCH** /integrations/connectwise/{UUID}/settings | Create, edit, and/or delete ConnectWise Integration settings |
| [**patchSettings_1**](ProvidersApi.md#patchSettings_1) | **PATCH** /integrations/syncro/{UUID}/settings | Create, edit, and/or delete Syncro Integration settings |
| [**postAdmins**](ProvidersApi.md#postAdmins) | **POST** /providers/{provider_id}/administrators | Create a new Provider Administrator |
| [**providerListCase**](ProvidersApi.md#providerListCase) | **GET** /providers/{provider_id}/cases | Get all cases (Support/Feature requests) for provider |
| [**removeAdministrator**](ProvidersApi.md#removeAdministrator) | **DELETE** /providers/{provider_id}/administrators/{id} | Delete Provider Administrator |
| [**retrieveAdditions**](ProvidersApi.md#retrieveAdditions) | **GET** /integrations/connectwise/{UUID}/agreements/{agreement_ID}/additions | Retrieve ConnectWise Additions |
| [**retrieveAgreements**](ProvidersApi.md#retrieveAgreements) | **GET** /integrations/connectwise/{UUID}/agreements | Retrieve ConnectWise Agreements |
| [**retrieveAlerts**](ProvidersApi.md#retrieveAlerts) | **GET** /providers/{provider_id}/integrations/ticketing/alerts | Get all ticketing alerts available for a provider&#39;s ticketing integration. |
| [**retrieveAllAlertConfigurationOptions**](ProvidersApi.md#retrieveAllAlertConfigurationOptions) | **GET** /providers/{provider_id}/integrations/autotask/alerts/configuration/options | Get all Autotask ticketing alert configuration options for a provider |
| [**retrieveAllAlertConfigurationOptions_0**](ProvidersApi.md#retrieveAllAlertConfigurationOptions_0) | **GET** /providers/{provider_id}/integrations/connectwise/alerts/configuration/options | Get all ConnectWise ticketing alert configuration options for a provider |
| [**retrieveAllAlertConfigurationOptions_1**](ProvidersApi.md#retrieveAllAlertConfigurationOptions_1) | **GET** /providers/{provider_id}/integrations/syncro/alerts/configuration/options | Get all Syncro ticketing alert configuration options for a provider |
| [**retrieveAllAlertConfigurations**](ProvidersApi.md#retrieveAllAlertConfigurations) | **GET** /providers/{provider_id}/integrations/autotask/alerts/configuration | Get all Autotask ticketing alert configurations for a provider |
| [**retrieveAllAlertConfigurations_0**](ProvidersApi.md#retrieveAllAlertConfigurations_0) | **GET** /providers/{provider_id}/integrations/connectwise/alerts/configuration | Get all ConnectWise ticketing alert configurations for a provider |
| [**retrieveAllAlertConfigurations_1**](ProvidersApi.md#retrieveAllAlertConfigurations_1) | **GET** /providers/{provider_id}/integrations/syncro/alerts/configuration | Get all Syncro ticketing alert configurations for a provider |
| [**retrieveBillingMappingConfigurationOptions**](ProvidersApi.md#retrieveBillingMappingConfigurationOptions) | **GET** /integrations/syncro/{UUID}/billing_mapping_configuration_options | Retrieve Syncro billing mappings dependencies |
| [**retrieveCompanies**](ProvidersApi.md#retrieveCompanies) | **GET** /integrations/autotask/{UUID}/companies | Retrieve Autotask Companies |
| [**retrieveCompanies_0**](ProvidersApi.md#retrieveCompanies_0) | **GET** /integrations/connectwise/{UUID}/companies | Retrieve ConnectWise Companies |
| [**retrieveCompanies_1**](ProvidersApi.md#retrieveCompanies_1) | **GET** /integrations/syncro/{UUID}/companies | Retrieve Syncro Companies |
| [**retrieveCompanyTypes**](ProvidersApi.md#retrieveCompanyTypes) | **GET** /integrations/autotask/{UUID}/companytypes | Retrieve Autotask Company Types |
| [**retrieveCompanyTypes_0**](ProvidersApi.md#retrieveCompanyTypes_0) | **GET** /integrations/connectwise/{UUID}/companytypes | Retrieve ConnectWise Company Types |
| [**retrieveContracts**](ProvidersApi.md#retrieveContracts) | **GET** /integrations/autotask/{UUID}/contracts | Retrieve Autotask Contracts |
| [**retrieveContractsFields**](ProvidersApi.md#retrieveContractsFields) | **GET** /integrations/autotask/{UUID}/contracts/fields | Retrieve Autotask Contract Fields |
| [**retrieveIntegrations**](ProvidersApi.md#retrieveIntegrations) | **GET** /providers/{provider_id}/integrations | Retrieve Integrations for Provider |
| [**retrieveInvoice**](ProvidersApi.md#retrieveInvoice) | **GET** /providers/{provider_id}/invoices/{ID} | Download a provider&#39;s invoice. |
| [**retrieveInvoices**](ProvidersApi.md#retrieveInvoices) | **GET** /providers/{provider_id}/invoices | List a provider&#39;s invoices. |
| [**retrieveMappings**](ProvidersApi.md#retrieveMappings) | **GET** /integrations/autotask/{UUID}/mappings | Retrieve Autotask mappings |
| [**retrieveMappings_0**](ProvidersApi.md#retrieveMappings_0) | **GET** /integrations/connectwise/{UUID}/mappings | Retrieve ConnectWise mappings |
| [**retrieveMappings_1**](ProvidersApi.md#retrieveMappings_1) | **GET** /integrations/syncro/{UUID}/mappings | Retrieve Syncro mappings |
| [**retrieveServices**](ProvidersApi.md#retrieveServices) | **GET** /integrations/autotask/{UUID}/contracts/services | Retrieve Autotask Contract Services |
| [**retrieveSettings**](ProvidersApi.md#retrieveSettings) | **GET** /integrations/autotask/{UUID}/settings | Retrieve Autotask Integration settings |
| [**retrieveSettings_0**](ProvidersApi.md#retrieveSettings_0) | **GET** /integrations/connectwise/{UUID}/settings | Retrieve ConnectWise Integration settings |
| [**retrieveSettings_1**](ProvidersApi.md#retrieveSettings_1) | **GET** /integrations/syncro/{UUID}/settings | Retrieve Syncro Integration settings |
| [**retrieveSyncErrors**](ProvidersApi.md#retrieveSyncErrors) | **GET** /integrations/{integration_type}/{UUID}/errors | Retrieve Recent Integration Sync Errors |
| [**updateAlertConfiguration**](ProvidersApi.md#updateAlertConfiguration) | **PUT** /providers/{provider_id}/integrations/autotask/alerts/{alert_UUID}/configuration | Update an Autotask ticketing alert&#39;s configuration |
| [**updateAlertConfiguration_0**](ProvidersApi.md#updateAlertConfiguration_0) | **PUT** /providers/{provider_id}/integrations/connectwise/alerts/{alert_UUID}/configuration | Update a ConnectWise ticketing alert&#39;s configuration |
| [**updateAlertConfiguration_1**](ProvidersApi.md#updateAlertConfiguration_1) | **PUT** /providers/{provider_id}/integrations/syncro/alerts/{alert_UUID}/configuration | Update a Syncro ticketing alert&#39;s configuration |
| [**updateConfiguration**](ProvidersApi.md#updateConfiguration) | **PATCH** /integrations/autotask/{UUID} | Update Autotask Integration configuration |
| [**updateConfiguration_0**](ProvidersApi.md#updateConfiguration_0) | **PATCH** /integrations/connectwise/{UUID} | Update ConnectWise Integration configuration |
| [**updateConfiguration_1**](ProvidersApi.md#updateConfiguration_1) | **PATCH** /integrations/syncro/{UUID} | Update Syncro Integration configuration |
| [**updateOrg**](ProvidersApi.md#updateOrg) | **PUT** /providers/{provider_id}/organizations/{id} | Update Provider Organization |


<a name="casesMetadata"></a>
# **casesMetadata**
> CasesMetadataResponse casesMetadata().execute();

Get the metadata for cases

This endpoint returns the metadata for cases

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
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
      CasesMetadataResponse result = client
              .providers
              .casesMetadata()
              .execute();
      System.out.println(result);
      System.out.println(result.getResults());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#casesMetadata");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<CasesMetadataResponse> response = client
              .providers
              .casesMetadata()
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#casesMetadata");
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

[**CasesMetadataResponse**](CasesMetadataResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="createConfiguration"></a>
# **createConfiguration**
> AutotaskCreateConfigurationResponse createConfiguration(providerId).autotaskIntegrationReq(autotaskIntegrationReq).execute();

Creates a new Autotask integration for the provider

Creates a new Autotask integration for the provider. You must be associated with the provider to use this route. A 422 Unprocessable Entity response means the server failed to validate with Autotask.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String secret = "secret_example"; // The secret for connecting to Autotask.
    String username = "username_example"; // The username for connecting to Autotask.
    String providerId = "providerId_example";
    try {
      AutotaskCreateConfigurationResponse result = client
              .providers
              .createConfiguration(secret, username, providerId)
              .execute();
      System.out.println(result);
      System.out.println(result.getIntegrationId());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#createConfiguration");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AutotaskCreateConfigurationResponse> response = client
              .providers
              .createConfiguration(secret, username, providerId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#createConfiguration");
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
| **providerId** | **String**|  | |
| **autotaskIntegrationReq** | [**AutotaskIntegrationReq**](AutotaskIntegrationReq.md)|  | [optional] |

### Return type

[**AutotaskCreateConfigurationResponse**](AutotaskCreateConfigurationResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Created |  -  |

<a name="createConfiguration_0"></a>
# **createConfiguration_0**
> ConnectwiseCreateConfigurationResponse createConfiguration_0(providerId).connectwiseIntegrationReq(connectwiseIntegrationReq).execute();

Creates a new ConnectWise integration for the provider

Creates a new ConnectWise integration for the provider. You must be associated with the provider to use this route. A 422 Unprocessable Entity response means the server failed to validate with ConnectWise.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String companyId = "companyId_example"; // The ConnectWise company identifier.
    String privateKey = "privateKey_example"; // The ConnectWise private key for authentication
    String publicKey = "publicKey_example"; // The ConnectWise public key for authentication.
    String url = "url_example"; // The base url for connecting to ConnectWise.
    String providerId = "providerId_example";
    try {
      ConnectwiseCreateConfigurationResponse result = client
              .providers
              .createConfiguration_0(companyId, privateKey, publicKey, url, providerId)
              .execute();
      System.out.println(result);
      System.out.println(result.getIntegrationId());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#createConfiguration_0");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ConnectwiseCreateConfigurationResponse> response = client
              .providers
              .createConfiguration_0(companyId, privateKey, publicKey, url, providerId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#createConfiguration_0");
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
| **providerId** | **String**|  | |
| **connectwiseIntegrationReq** | [**ConnectwiseIntegrationReq**](ConnectwiseIntegrationReq.md)|  | [optional] |

### Return type

[**ConnectwiseCreateConfigurationResponse**](ConnectwiseCreateConfigurationResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Created |  -  |

<a name="createConfiguration_1"></a>
# **createConfiguration_1**
> SyncroCreateConfigurationResponse createConfiguration_1(providerId).syncroIntegrationReq(syncroIntegrationReq).execute();

Creates a new Syncro integration for the provider

Creates a new Syncro integration for the provider. You must be associated with the provider to use this route. A 422 Unprocessable Entity response means the server failed to validate with Syncro.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String apiToken = "apiToken_example"; // The Syncro API token for authentication
    String subdomain = "subdomain_example"; // The subdomain for the URL to connect to Syncro.
    String providerId = "providerId_example";
    try {
      SyncroCreateConfigurationResponse result = client
              .providers
              .createConfiguration_1(apiToken, subdomain, providerId)
              .execute();
      System.out.println(result);
      System.out.println(result.getIntegrationId());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#createConfiguration_1");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<SyncroCreateConfigurationResponse> response = client
              .providers
              .createConfiguration_1(apiToken, subdomain, providerId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#createConfiguration_1");
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
| **providerId** | **String**|  | |
| **syncroIntegrationReq** | [**SyncroIntegrationReq**](SyncroIntegrationReq.md)|  | [optional] |

### Return type

[**SyncroCreateConfigurationResponse**](SyncroCreateConfigurationResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Created |  -  |

<a name="createOrg"></a>
# **createOrg**
> Organization createOrg(providerId).createOrganization(createOrganization).execute();

Create Provider Organization

This endpoint creates a new organization under the provider

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String providerId = "providerId_example";
    Integer maxSystemUsers = 56; // The maximum number of users allowed in this organization. Requires organizations.billing scope to modify.
    String name = "name_example";
    try {
      Organization result = client
              .providers
              .createOrg(providerId)
              .maxSystemUsers(maxSystemUsers)
              .name(name)
              .execute();
      System.out.println(result);
      System.out.println(result.getId());
      System.out.println(result.getMaxSystemUsers());
      System.out.println(result.getName());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#createOrg");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Organization> response = client
              .providers
              .createOrg(providerId)
              .maxSystemUsers(maxSystemUsers)
              .name(name)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#createOrg");
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
| **providerId** | **String**|  | |
| **createOrganization** | [**CreateOrganization**](CreateOrganization.md)|  | [optional] |

### Return type

[**Organization**](Organization.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | CREATED |  -  |

<a name="delete"></a>
# **delete**
> delete(providerId, id).execute();

Deletes policy group template.

Deletes a Policy Group Template.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String providerId = "providerId_example";
    String id = "id_example";
    try {
      client
              .providers
              .delete(providerId, id)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#delete");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .providers
              .delete(providerId, id)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#delete");
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
| **providerId** | **String**|  | |
| **id** | **String**|  | |

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
| **201** | NO_CONTENT |  -  |

<a name="deleteConfiguration"></a>
# **deleteConfiguration**
> deleteConfiguration(UUID).execute();

Delete Autotask Integration

Removes a Autotask integration.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    try {
      client
              .providers
              .deleteConfiguration(UUID)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#deleteConfiguration");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .providers
              .deleteConfiguration(UUID)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#deleteConfiguration");
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
| **UUID** | **String**|  | |

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

<a name="deleteConfiguration_0"></a>
# **deleteConfiguration_0**
> deleteConfiguration_0(UUID).execute();

Delete ConnectWise Integration

Removes a ConnectWise integration.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    try {
      client
              .providers
              .deleteConfiguration_0(UUID)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#deleteConfiguration_0");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .providers
              .deleteConfiguration_0(UUID)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#deleteConfiguration_0");
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
| **UUID** | **String**|  | |

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

<a name="deleteConfiguration_1"></a>
# **deleteConfiguration_1**
> deleteConfiguration_1(UUID).execute();

Delete Syncro Integration

Removes a Syncro integration.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    try {
      client
              .providers
              .deleteConfiguration_1(UUID)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#deleteConfiguration_1");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .providers
              .deleteConfiguration_1(UUID)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#deleteConfiguration_1");
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
| **UUID** | **String**|  | |

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

<a name="get"></a>
# **get**
> PolicyGroupTemplate get(providerId, id).execute();

Gets a provider&#39;s policy group template.

Retrieves a Policy Group Template for this provider.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String providerId = "providerId_example";
    String id = "id_example";
    try {
      PolicyGroupTemplate result = client
              .providers
              .get(providerId, id)
              .execute();
      System.out.println(result);
      System.out.println(result.getDescription());
      System.out.println(result.getId());
      System.out.println(result.getMembers());
      System.out.println(result.getName());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#get");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<PolicyGroupTemplate> response = client
              .providers
              .get(providerId, id)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#get");
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
| **providerId** | **String**|  | |
| **id** | **String**|  | |

### Return type

[**PolicyGroupTemplate**](PolicyGroupTemplate.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="getConfiguration"></a>
# **getConfiguration**
> AutotaskIntegration getConfiguration(UUID).execute();

Retrieve Autotask Integration Configuration

Retrieves configuration for given Autotask integration id. You must be associated to the provider the integration is tied to in order to use this api.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    try {
      AutotaskIntegration result = client
              .providers
              .getConfiguration(UUID)
              .execute();
      System.out.println(result);
      System.out.println(result.getId());
      System.out.println(result.getIsMspAuthConfigured());
      System.out.println(result.getUsername());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#getConfiguration");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AutotaskIntegration> response = client
              .providers
              .getConfiguration(UUID)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#getConfiguration");
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
| **UUID** | **String**|  | |

### Return type

[**AutotaskIntegration**](AutotaskIntegration.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="getConfiguration_0"></a>
# **getConfiguration_0**
> ConnectwiseIntegration getConfiguration_0(UUID).execute();

Retrieve ConnectWise Integration Configuration

Retrieves configuration for given ConnectWise integration id. You must be associated to the provider the integration is tied to in order to use this api.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    try {
      ConnectwiseIntegration result = client
              .providers
              .getConfiguration_0(UUID)
              .execute();
      System.out.println(result);
      System.out.println(result.getCompanyId());
      System.out.println(result.getId());
      System.out.println(result.getIsMspAuthConfigured());
      System.out.println(result.getUrl());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#getConfiguration_0");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ConnectwiseIntegration> response = client
              .providers
              .getConfiguration_0(UUID)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#getConfiguration_0");
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
| **UUID** | **String**|  | |

### Return type

[**ConnectwiseIntegration**](ConnectwiseIntegration.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="getConfiguration_1"></a>
# **getConfiguration_1**
> SyncroIntegration getConfiguration_1(UUID).execute();

Retrieve Syncro Integration Configuration

Retrieves configuration for given Syncro integration id. You must be associated to the provider the integration is tied to in order to use this api.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    try {
      SyncroIntegration result = client
              .providers
              .getConfiguration_1(UUID)
              .execute();
      System.out.println(result);
      System.out.println(result.getId());
      System.out.println(result.getIsMspAuthConfigured());
      System.out.println(result.getSubdomain());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#getConfiguration_1");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<SyncroIntegration> response = client
              .providers
              .getConfiguration_1(UUID)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#getConfiguration_1");
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
| **UUID** | **String**|  | |

### Return type

[**SyncroIntegration**](SyncroIntegration.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="getConfiguredPolicyTemplate"></a>
# **getConfiguredPolicyTemplate**
> Object getConfiguredPolicyTemplate(providerId, id).execute();

Retrieve a configured policy template by id.

Retrieves a Configured Policy Templates for this provider and Id.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String providerId = "providerId_example";
    String id = "id_example";
    try {
      Object result = client
              .providers
              .getConfiguredPolicyTemplate(providerId, id)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#getConfiguredPolicyTemplate");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Object> response = client
              .providers
              .getConfiguredPolicyTemplate(providerId, id)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#getConfiguredPolicyTemplate");
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
| **providerId** | **String**|  | |
| **id** | **String**|  | |

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
| **200** | OK |  -  |

<a name="getContract"></a>
# **getContract**
> File getContract(providerId).execute();

Retrieve contract for a Provider

Retrieve contract for a Provider

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    byte[] providerId = null;
    try {
      File result = client
              .providers
              .getContract(providerId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#getContract");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<File> response = client
              .providers
              .getContract(providerId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#getContract");
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
| **providerId** | **byte[]**|  | |

### Return type

[**File**](File.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/pdf, application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |
| **0** | An unexpected error response. |  -  |

<a name="getDetails"></a>
# **getDetails**
> JumpcloudMspGetDetailsResponse getDetails(providerId).execute();

Retrieve billing details for a Provider

Retrieve billing details for a Provider

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    byte[] providerId = null;
    try {
      JumpcloudMspGetDetailsResponse result = client
              .providers
              .getDetails(providerId)
              .execute();
      System.out.println(result);
      System.out.println(result.getAssignedLicenses());
      System.out.println(result.getContractType());
      System.out.println(result.getHasContract());
      System.out.println(result.getProducts());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#getDetails");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<JumpcloudMspGetDetailsResponse> response = client
              .providers
              .getDetails(providerId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#getDetails");
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
| **providerId** | **byte[]**|  | |

### Return type

[**JumpcloudMspGetDetailsResponse**](JumpcloudMspGetDetailsResponse.md)

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

<a name="getProvider"></a>
# **getProvider**
> Provider getProvider(providerId).fields(fields).execute();

Retrieve Provider

This endpoint returns details about a provider

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String providerId = "providerId_example";
    List<String> fields = Arrays.asList(); // The comma separated fields included in the returned records. If omitted, the default list of fields will be returned. 
    try {
      Provider result = client
              .providers
              .getProvider(providerId)
              .fields(fields)
              .execute();
      System.out.println(result);
      System.out.println(result.getDisallowOrgCreation());
      System.out.println(result.getId());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#getProvider");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Provider> response = client
              .providers
              .getProvider(providerId)
              .fields(fields)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#getProvider");
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
| **providerId** | **String**|  | |
| **fields** | [**List&lt;String&gt;**](String.md)| The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  | [optional] |

### Return type

[**Provider**](Provider.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="list"></a>
# **list**
> PolicyGroupTemplates list(providerId).fields(fields).skip(skip).sort(sort).limit(limit).filter(filter).execute();

List a provider&#39;s policy group templates.

Retrieves a list of Policy Group Templates for this provider.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String providerId = "providerId_example";
    List<String> fields = Arrays.asList(); // The comma separated fields included in the returned records. If omitted, the default list of fields will be returned. 
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. 
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    try {
      PolicyGroupTemplates result = client
              .providers
              .list(providerId)
              .fields(fields)
              .skip(skip)
              .sort(sort)
              .limit(limit)
              .filter(filter)
              .execute();
      System.out.println(result);
      System.out.println(result.getResults());
      System.out.println(result.getTotalCount());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#list");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<PolicyGroupTemplates> response = client
              .providers
              .list(providerId)
              .fields(fields)
              .skip(skip)
              .sort(sort)
              .limit(limit)
              .filter(filter)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#list");
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
| **providerId** | **String**|  | |
| **fields** | [**List&lt;String&gt;**](String.md)| The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  | [optional] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  | [optional] |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **filter** | [**List&lt;String&gt;**](String.md)| A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; | [optional] |

### Return type

[**PolicyGroupTemplates**](PolicyGroupTemplates.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="listAdministrators"></a>
# **listAdministrators**
> ProvidersListAdministratorsResponse listAdministrators(providerId).fields(fields).filter(filter).limit(limit).skip(skip).sort(sort).sortIgnoreCase(sortIgnoreCase).execute();

List Provider Administrators

This endpoint returns a list of the Administrators associated with the Provider. You must be associated with the provider to use this route.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String providerId = "providerId_example";
    List<String> fields = Arrays.asList(); // The comma separated fields included in the returned records. If omitted, the default list of fields will be returned. 
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. 
    List<String> sortIgnoreCase = Arrays.asList(); // The comma separated fields used to sort the collection, ignoring case. Default sort is ascending, prefix with `-` to sort descending. 
    try {
      ProvidersListAdministratorsResponse result = client
              .providers
              .listAdministrators(providerId)
              .fields(fields)
              .filter(filter)
              .limit(limit)
              .skip(skip)
              .sort(sort)
              .sortIgnoreCase(sortIgnoreCase)
              .execute();
      System.out.println(result);
      System.out.println(result.getResults());
      System.out.println(result.getTotalCount());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#listAdministrators");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ProvidersListAdministratorsResponse> response = client
              .providers
              .listAdministrators(providerId)
              .fields(fields)
              .filter(filter)
              .limit(limit)
              .skip(skip)
              .sort(sort)
              .sortIgnoreCase(sortIgnoreCase)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#listAdministrators");
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
| **providerId** | **String**|  | |
| **fields** | [**List&lt;String&gt;**](String.md)| The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; | [optional] |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  | [optional] |
| **sortIgnoreCase** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection, ignoring case. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  | [optional] |

### Return type

[**ProvidersListAdministratorsResponse**](ProvidersListAdministratorsResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="listConfiguredPolicyTemplates"></a>
# **listConfiguredPolicyTemplates**
> PolicyGroupTemplatesListConfiguredPolicyTemplatesResponse listConfiguredPolicyTemplates(providerId).skip(skip).sort(sort).limit(limit).filter(filter).execute();

List a provider&#39;s configured policy templates.

Retrieves a list of Configured Policy Templates for this provider.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String providerId = "providerId_example";
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. 
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    try {
      PolicyGroupTemplatesListConfiguredPolicyTemplatesResponse result = client
              .providers
              .listConfiguredPolicyTemplates(providerId)
              .skip(skip)
              .sort(sort)
              .limit(limit)
              .filter(filter)
              .execute();
      System.out.println(result);
      System.out.println(result.getRecords());
      System.out.println(result.getTotalCount());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#listConfiguredPolicyTemplates");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<PolicyGroupTemplatesListConfiguredPolicyTemplatesResponse> response = client
              .providers
              .listConfiguredPolicyTemplates(providerId)
              .skip(skip)
              .sort(sort)
              .limit(limit)
              .filter(filter)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#listConfiguredPolicyTemplates");
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
| **providerId** | **String**|  | |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  | [optional] |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **filter** | [**List&lt;String&gt;**](String.md)| A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; | [optional] |

### Return type

[**PolicyGroupTemplatesListConfiguredPolicyTemplatesResponse**](PolicyGroupTemplatesListConfiguredPolicyTemplatesResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="listMembers"></a>
# **listMembers**
> PolicyGroupTemplateMembers listMembers(providerId, id).skip(skip).sort(sort).limit(limit).filter(filter).execute();

Gets the list of members from a policy group template.

Retrieves a Policy Group Template&#39;s Members.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String providerId = "providerId_example";
    String id = "id_example";
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. 
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    try {
      PolicyGroupTemplateMembers result = client
              .providers
              .listMembers(providerId, id)
              .skip(skip)
              .sort(sort)
              .limit(limit)
              .filter(filter)
              .execute();
      System.out.println(result);
      System.out.println(result.getResults());
      System.out.println(result.getTotalCount());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#listMembers");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<PolicyGroupTemplateMembers> response = client
              .providers
              .listMembers(providerId, id)
              .skip(skip)
              .sort(sort)
              .limit(limit)
              .filter(filter)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#listMembers");
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
| **providerId** | **String**|  | |
| **id** | **String**|  | |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  | [optional] |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **filter** | [**List&lt;String&gt;**](String.md)| A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; | [optional] |

### Return type

[**PolicyGroupTemplateMembers**](PolicyGroupTemplateMembers.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="listOrganizations"></a>
# **listOrganizations**
> ProvidersListOrganizationsResponse listOrganizations(providerId).fields(fields).filter(filter).limit(limit).skip(skip).sort(sort).sortIgnoreCase(sortIgnoreCase).execute();

List Provider Organizations

This endpoint returns a list of the Organizations associated with the Provider. You must be associated with the provider to use this route.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String providerId = "providerId_example";
    List<String> fields = Arrays.asList(); // The comma separated fields included in the returned records. If omitted, the default list of fields will be returned. 
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. 
    List<String> sortIgnoreCase = Arrays.asList(); // The comma separated fields used to sort the collection, ignoring case. Default sort is ascending, prefix with `-` to sort descending. 
    try {
      ProvidersListOrganizationsResponse result = client
              .providers
              .listOrganizations(providerId)
              .fields(fields)
              .filter(filter)
              .limit(limit)
              .skip(skip)
              .sort(sort)
              .sortIgnoreCase(sortIgnoreCase)
              .execute();
      System.out.println(result);
      System.out.println(result.getResults());
      System.out.println(result.getTotalCount());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#listOrganizations");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ProvidersListOrganizationsResponse> response = client
              .providers
              .listOrganizations(providerId)
              .fields(fields)
              .filter(filter)
              .limit(limit)
              .skip(skip)
              .sort(sort)
              .sortIgnoreCase(sortIgnoreCase)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#listOrganizations");
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
| **providerId** | **String**|  | |
| **fields** | [**List&lt;String&gt;**](String.md)| The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; | [optional] |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  | [optional] |
| **sortIgnoreCase** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection, ignoring case. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  | [optional] |

### Return type

[**ProvidersListOrganizationsResponse**](ProvidersListOrganizationsResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="patchMappings"></a>
# **patchMappings**
> AutotaskMappingResponse patchMappings(UUID).autotaskMappingRequest(autotaskMappingRequest).execute();

Create, edit, and/or delete Autotask Mappings

Create, edit, and/or delete mappings between Jumpcloud organizations and Autotask companies/contracts/services. You must be associated to the same provider as the Autotask integration to use this api.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    List<AutotaskMappingRequestDataInner> data = Arrays.asList();
    try {
      AutotaskMappingResponse result = client
              .providers
              .patchMappings(UUID)
              .data(data)
              .execute();
      System.out.println(result);
      System.out.println(result.getCompany());
      System.out.println(result.getContract());
      System.out.println(result.getLastSyncDateTime());
      System.out.println(result.getLastSyncStatus());
      System.out.println(result.getOrganization());
      System.out.println(result.getService());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#patchMappings");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AutotaskMappingResponse> response = client
              .providers
              .patchMappings(UUID)
              .data(data)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#patchMappings");
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
| **UUID** | **String**|  | |
| **autotaskMappingRequest** | [**AutotaskMappingRequest**](AutotaskMappingRequest.md)|  | [optional] |

### Return type

[**AutotaskMappingResponse**](AutotaskMappingResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **204** | No Content |  -  |

<a name="patchMappings_0"></a>
# **patchMappings_0**
> ConnectWiseMappingRequest patchMappings_0(UUID).connectWiseMappingRequest(connectWiseMappingRequest).execute();

Create, edit, and/or delete ConnectWise Mappings

Create, edit, and/or delete mappings between Jumpcloud organizations and ConnectWise companies/agreements/additions. You must be associated to the same provider as the ConnectWise integration to use this api.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    List<ConnectWiseMappingRequestDataInner> data = Arrays.asList();
    try {
      ConnectWiseMappingRequest result = client
              .providers
              .patchMappings_0(UUID)
              .data(data)
              .execute();
      System.out.println(result);
      System.out.println(result.getData());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#patchMappings_0");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ConnectWiseMappingRequest> response = client
              .providers
              .patchMappings_0(UUID)
              .data(data)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#patchMappings_0");
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
| **UUID** | **String**|  | |
| **connectWiseMappingRequest** | [**ConnectWiseMappingRequest**](ConnectWiseMappingRequest.md)|  | [optional] |

### Return type

[**ConnectWiseMappingRequest**](ConnectWiseMappingRequest.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **204** | No Content |  -  |

<a name="patchMappings_1"></a>
# **patchMappings_1**
> SyncroMappingRequest patchMappings_1(UUID).syncroMappingRequest(syncroMappingRequest).execute();

Create, edit, and/or delete Syncro Mappings

Create, edit, and/or delete mappings between Jumpcloud organizations and Syncro companies. You must be associated to the same provider as the Syncro integration to use this api.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    List<SyncroMappingRequestDataInner> data = Arrays.asList();
    try {
      SyncroMappingRequest result = client
              .providers
              .patchMappings_1(UUID)
              .data(data)
              .execute();
      System.out.println(result);
      System.out.println(result.getData());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#patchMappings_1");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<SyncroMappingRequest> response = client
              .providers
              .patchMappings_1(UUID)
              .data(data)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#patchMappings_1");
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
| **UUID** | **String**|  | |
| **syncroMappingRequest** | [**SyncroMappingRequest**](SyncroMappingRequest.md)|  | [optional] |

### Return type

[**SyncroMappingRequest**](SyncroMappingRequest.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **204** | No Content |  -  |

<a name="patchSettings"></a>
# **patchSettings**
> AutotaskSettings patchSettings(UUID).autotaskSettingsPatchReq(autotaskSettingsPatchReq).execute();

Create, edit, and/or delete Autotask Integration settings

Create, edit, and/or delete Autotask settings. You must be associated to the same provider as the Autotask integration to use this endpoint.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    Boolean automaticTicketing = true; // Determine whether Autotask uses automatic ticketing
    List<Integer> companyTypeIds = Arrays.asList(); // The array of Autotask companyType IDs applicable to the Provider.
    try {
      AutotaskSettings result = client
              .providers
              .patchSettings(UUID)
              .automaticTicketing(automaticTicketing)
              .companyTypeIds(companyTypeIds)
              .execute();
      System.out.println(result);
      System.out.println(result.getAutomaticTicketing());
      System.out.println(result.getCompanyTypeIds());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#patchSettings");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AutotaskSettings> response = client
              .providers
              .patchSettings(UUID)
              .automaticTicketing(automaticTicketing)
              .companyTypeIds(companyTypeIds)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#patchSettings");
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
| **UUID** | **String**|  | |
| **autotaskSettingsPatchReq** | [**AutotaskSettingsPatchReq**](AutotaskSettingsPatchReq.md)|  | [optional] |

### Return type

[**AutotaskSettings**](AutotaskSettings.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="patchSettings_0"></a>
# **patchSettings_0**
> ConnectWiseSettings patchSettings_0(UUID).connectWiseSettingsPatchReq(connectWiseSettingsPatchReq).execute();

Create, edit, and/or delete ConnectWise Integration settings

Create, edit, and/or delete ConnectWiseIntegration settings. You must be associated to the same provider as the ConnectWise integration to use this endpoint.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    Boolean automaticTicketing = true; // Determine whether ConnectWise uses automatic ticketing
    List<Integer> companyTypeIds = Arrays.asList(); // The array of ConnectWise companyType IDs applicable to the Provider.
    try {
      ConnectWiseSettings result = client
              .providers
              .patchSettings_0(UUID)
              .automaticTicketing(automaticTicketing)
              .companyTypeIds(companyTypeIds)
              .execute();
      System.out.println(result);
      System.out.println(result.getAutomaticTicketing());
      System.out.println(result.getCompanyTypeIds());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#patchSettings_0");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ConnectWiseSettings> response = client
              .providers
              .patchSettings_0(UUID)
              .automaticTicketing(automaticTicketing)
              .companyTypeIds(companyTypeIds)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#patchSettings_0");
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
| **UUID** | **String**|  | |
| **connectWiseSettingsPatchReq** | [**ConnectWiseSettingsPatchReq**](ConnectWiseSettingsPatchReq.md)|  | [optional] |

### Return type

[**ConnectWiseSettings**](ConnectWiseSettings.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="patchSettings_1"></a>
# **patchSettings_1**
> SyncroSettings patchSettings_1(UUID).syncroSettingsPatchReq(syncroSettingsPatchReq).execute();

Create, edit, and/or delete Syncro Integration settings

Create, edit, and/or delete SyncroIntegration settings. You must be associated to the same provider as the Syncro integration to use this endpoint.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    Boolean automaticTicketing = true; // Determine whether Syncro uses automatic ticketing
    try {
      SyncroSettings result = client
              .providers
              .patchSettings_1(UUID)
              .automaticTicketing(automaticTicketing)
              .execute();
      System.out.println(result);
      System.out.println(result.getAutomaticTicketing());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#patchSettings_1");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<SyncroSettings> response = client
              .providers
              .patchSettings_1(UUID)
              .automaticTicketing(automaticTicketing)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#patchSettings_1");
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
| **UUID** | **String**|  | |
| **syncroSettingsPatchReq** | [**SyncroSettingsPatchReq**](SyncroSettingsPatchReq.md)|  | [optional] |

### Return type

[**SyncroSettings**](SyncroSettings.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="postAdmins"></a>
# **postAdmins**
> Administrator postAdmins(providerId).providerAdminReq(providerAdminReq).execute();

Create a new Provider Administrator

This endpoint allows you to create a provider administrator. You must be associated with the provider to use this route. You must provide either &#x60;role&#x60; or &#x60;roleName&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String email = "email_example";
    String providerId = "providerId_example";
    Boolean apiKeyAllowed = true;
    Boolean bindNoOrgs = false;
    Boolean enableMultiFactor = true;
    String firstname = "firstname_example";
    String lastname = "lastname_example";
    String role = "role_example";
    String roleName = "roleName_example";
    try {
      Administrator result = client
              .providers
              .postAdmins(email, providerId)
              .apiKeyAllowed(apiKeyAllowed)
              .bindNoOrgs(bindNoOrgs)
              .enableMultiFactor(enableMultiFactor)
              .firstname(firstname)
              .lastname(lastname)
              .role(role)
              .roleName(roleName)
              .execute();
      System.out.println(result);
      System.out.println(result.getApiKeyAllowed());
      System.out.println(result.getApiKeySet());
      System.out.println(result.getEmail());
      System.out.println(result.getEnableMultiFactor());
      System.out.println(result.getFirstname());
      System.out.println(result.getId());
      System.out.println(result.getLastname());
      System.out.println(result.getOrganizationAccessTotal());
      System.out.println(result.getRegistered());
      System.out.println(result.getRole());
      System.out.println(result.getRoleName());
      System.out.println(result.getSuspended());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#postAdmins");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Administrator> response = client
              .providers
              .postAdmins(email, providerId)
              .apiKeyAllowed(apiKeyAllowed)
              .bindNoOrgs(bindNoOrgs)
              .enableMultiFactor(enableMultiFactor)
              .firstname(firstname)
              .lastname(lastname)
              .role(role)
              .roleName(roleName)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#postAdmins");
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
| **providerId** | **String**|  | |
| **providerAdminReq** | [**ProviderAdminReq**](ProviderAdminReq.md)|  | [optional] |

### Return type

[**Administrator**](Administrator.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="providerListCase"></a>
# **providerListCase**
> CasesResponse providerListCase(providerId).skip(skip).sort(sort).limit(limit).filter(filter).execute();

Get all cases (Support/Feature requests) for provider

This endpoint returns the cases (Support/Feature requests) for the provider

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String providerId = "providerId_example";
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. 
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    try {
      CasesResponse result = client
              .providers
              .providerListCase(providerId)
              .skip(skip)
              .sort(sort)
              .limit(limit)
              .filter(filter)
              .execute();
      System.out.println(result);
      System.out.println(result.getResults());
      System.out.println(result.getTotalCount());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#providerListCase");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<CasesResponse> response = client
              .providers
              .providerListCase(providerId)
              .skip(skip)
              .sort(sort)
              .limit(limit)
              .filter(filter)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#providerListCase");
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
| **providerId** | **String**|  | |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  | [optional] |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **filter** | [**List&lt;String&gt;**](String.md)| A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; | [optional] |

### Return type

[**CasesResponse**](CasesResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="removeAdministrator"></a>
# **removeAdministrator**
> removeAdministrator(providerId, id).execute();

Delete Provider Administrator

This endpoint removes an Administrator associated with the Provider. You must be associated with the provider to use this route.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String providerId = "providerId_example";
    String id = "id_example";
    try {
      client
              .providers
              .removeAdministrator(providerId, id)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#removeAdministrator");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .providers
              .removeAdministrator(providerId, id)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#removeAdministrator");
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
| **providerId** | **String**|  | |
| **id** | **String**|  | |

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

<a name="retrieveAdditions"></a>
# **retrieveAdditions**
> ConnectwiseRetrieveAdditionsResponse retrieveAdditions(UUID, agreementID).fields(fields).filter(filter).limit(limit).skip(skip).sort(sort).execute();

Retrieve ConnectWise Additions

Retrieves a list of ConnectWise additions for the given ConnectWise id and Agreement id. You must be associated to the same provider as the ConnectWise integration to use this endpoint.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    String agreementID = "agreementID_example";
    List<String> fields = Arrays.asList(); // The comma separated fields included in the returned records. If omitted, the default list of fields will be returned. 
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. 
    try {
      ConnectwiseRetrieveAdditionsResponse result = client
              .providers
              .retrieveAdditions(UUID, agreementID)
              .fields(fields)
              .filter(filter)
              .limit(limit)
              .skip(skip)
              .sort(sort)
              .execute();
      System.out.println(result);
      System.out.println(result.getResults());
      System.out.println(result.getTotalCount());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveAdditions");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ConnectwiseRetrieveAdditionsResponse> response = client
              .providers
              .retrieveAdditions(UUID, agreementID)
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
      System.err.println("Exception when calling ProvidersApi#retrieveAdditions");
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
| **UUID** | **String**|  | |
| **agreementID** | **String**|  | |
| **fields** | [**List&lt;String&gt;**](String.md)| The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; | [optional] |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  | [optional] |

### Return type

[**ConnectwiseRetrieveAdditionsResponse**](ConnectwiseRetrieveAdditionsResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="retrieveAgreements"></a>
# **retrieveAgreements**
> ConnectwiseRetrieveAgreementsResponse retrieveAgreements(UUID).fields(fields).filter(filter).limit(limit).skip(skip).sort(sort).execute();

Retrieve ConnectWise Agreements

Retrieves a list of ConnectWise agreements for the given ConnectWise id. You must be associated to the same provider as the ConnectWise integration to use this endpoint.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    List<String> fields = Arrays.asList(); // The comma separated fields included in the returned records. If omitted, the default list of fields will be returned. 
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. 
    try {
      ConnectwiseRetrieveAgreementsResponse result = client
              .providers
              .retrieveAgreements(UUID)
              .fields(fields)
              .filter(filter)
              .limit(limit)
              .skip(skip)
              .sort(sort)
              .execute();
      System.out.println(result);
      System.out.println(result.getResults());
      System.out.println(result.getTotalCount());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveAgreements");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ConnectwiseRetrieveAgreementsResponse> response = client
              .providers
              .retrieveAgreements(UUID)
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
      System.err.println("Exception when calling ProvidersApi#retrieveAgreements");
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
| **UUID** | **String**|  | |
| **fields** | [**List&lt;String&gt;**](String.md)| The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; | [optional] |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  | [optional] |

### Return type

[**ConnectwiseRetrieveAgreementsResponse**](ConnectwiseRetrieveAgreementsResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="retrieveAlerts"></a>
# **retrieveAlerts**
> TicketingIntegrationAlertsResp retrieveAlerts(providerId).execute();

Get all ticketing alerts available for a provider&#39;s ticketing integration.

Get all ticketing alerts available for a provider&#39;s ticketing integration.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String providerId = "providerId_example";
    try {
      TicketingIntegrationAlertsResp result = client
              .providers
              .retrieveAlerts(providerId)
              .execute();
      System.out.println(result);
      System.out.println(result.getRecords());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveAlerts");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<TicketingIntegrationAlertsResp> response = client
              .providers
              .retrieveAlerts(providerId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveAlerts");
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
| **providerId** | **String**|  | |

### Return type

[**TicketingIntegrationAlertsResp**](TicketingIntegrationAlertsResp.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="retrieveAllAlertConfigurationOptions"></a>
# **retrieveAllAlertConfigurationOptions**
> AutotaskTicketingAlertConfigurationOptions retrieveAllAlertConfigurationOptions(providerId).execute();

Get all Autotask ticketing alert configuration options for a provider

Get all Autotask ticketing alert configuration options for a provider.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String providerId = "providerId_example";
    try {
      AutotaskTicketingAlertConfigurationOptions result = client
              .providers
              .retrieveAllAlertConfigurationOptions(providerId)
              .execute();
      System.out.println(result);
      System.out.println(result.getOptions());
      System.out.println(result.getResources());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveAllAlertConfigurationOptions");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AutotaskTicketingAlertConfigurationOptions> response = client
              .providers
              .retrieveAllAlertConfigurationOptions(providerId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveAllAlertConfigurationOptions");
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
| **providerId** | **String**|  | |

### Return type

[**AutotaskTicketingAlertConfigurationOptions**](AutotaskTicketingAlertConfigurationOptions.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="retrieveAllAlertConfigurationOptions_0"></a>
# **retrieveAllAlertConfigurationOptions_0**
> ConnectWiseTicketingAlertConfigurationOptions retrieveAllAlertConfigurationOptions_0(providerId).execute();

Get all ConnectWise ticketing alert configuration options for a provider

Get all ConnectWise ticketing alert configuration options for a provider.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String providerId = "providerId_example";
    try {
      ConnectWiseTicketingAlertConfigurationOptions result = client
              .providers
              .retrieveAllAlertConfigurationOptions_0(providerId)
              .execute();
      System.out.println(result);
      System.out.println(result.getRecords());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveAllAlertConfigurationOptions_0");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ConnectWiseTicketingAlertConfigurationOptions> response = client
              .providers
              .retrieveAllAlertConfigurationOptions_0(providerId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveAllAlertConfigurationOptions_0");
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
| **providerId** | **String**|  | |

### Return type

[**ConnectWiseTicketingAlertConfigurationOptions**](ConnectWiseTicketingAlertConfigurationOptions.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="retrieveAllAlertConfigurationOptions_1"></a>
# **retrieveAllAlertConfigurationOptions_1**
> SyncroTicketingAlertConfigurationOptions retrieveAllAlertConfigurationOptions_1(providerId).execute();

Get all Syncro ticketing alert configuration options for a provider

Get all Syncro ticketing alert configuration options for a provider.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String providerId = "providerId_example";
    try {
      SyncroTicketingAlertConfigurationOptions result = client
              .providers
              .retrieveAllAlertConfigurationOptions_1(providerId)
              .execute();
      System.out.println(result);
      System.out.println(result.getRecords());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveAllAlertConfigurationOptions_1");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<SyncroTicketingAlertConfigurationOptions> response = client
              .providers
              .retrieveAllAlertConfigurationOptions_1(providerId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveAllAlertConfigurationOptions_1");
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
| **providerId** | **String**|  | |

### Return type

[**SyncroTicketingAlertConfigurationOptions**](SyncroTicketingAlertConfigurationOptions.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="retrieveAllAlertConfigurations"></a>
# **retrieveAllAlertConfigurations**
> AutotaskTicketingAlertConfigurationList retrieveAllAlertConfigurations(providerId).execute();

Get all Autotask ticketing alert configurations for a provider

Get all Autotask ticketing alert configurations for a provider.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String providerId = "providerId_example";
    try {
      AutotaskTicketingAlertConfigurationList result = client
              .providers
              .retrieveAllAlertConfigurations(providerId)
              .execute();
      System.out.println(result);
      System.out.println(result.getRecords());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveAllAlertConfigurations");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AutotaskTicketingAlertConfigurationList> response = client
              .providers
              .retrieveAllAlertConfigurations(providerId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveAllAlertConfigurations");
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
| **providerId** | **String**|  | |

### Return type

[**AutotaskTicketingAlertConfigurationList**](AutotaskTicketingAlertConfigurationList.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="retrieveAllAlertConfigurations_0"></a>
# **retrieveAllAlertConfigurations_0**
> ConnectWiseTicketingAlertConfigurationList retrieveAllAlertConfigurations_0(providerId).execute();

Get all ConnectWise ticketing alert configurations for a provider

Get all ConnectWise ticketing alert configurations for a provider.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String providerId = "providerId_example";
    try {
      ConnectWiseTicketingAlertConfigurationList result = client
              .providers
              .retrieveAllAlertConfigurations_0(providerId)
              .execute();
      System.out.println(result);
      System.out.println(result.getRecords());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveAllAlertConfigurations_0");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ConnectWiseTicketingAlertConfigurationList> response = client
              .providers
              .retrieveAllAlertConfigurations_0(providerId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveAllAlertConfigurations_0");
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
| **providerId** | **String**|  | |

### Return type

[**ConnectWiseTicketingAlertConfigurationList**](ConnectWiseTicketingAlertConfigurationList.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="retrieveAllAlertConfigurations_1"></a>
# **retrieveAllAlertConfigurations_1**
> SyncroTicketingAlertConfigurationList retrieveAllAlertConfigurations_1(providerId).execute();

Get all Syncro ticketing alert configurations for a provider

Get all Syncro ticketing alert configurations for a provider.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String providerId = "providerId_example";
    try {
      SyncroTicketingAlertConfigurationList result = client
              .providers
              .retrieveAllAlertConfigurations_1(providerId)
              .execute();
      System.out.println(result);
      System.out.println(result.getRecords());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveAllAlertConfigurations_1");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<SyncroTicketingAlertConfigurationList> response = client
              .providers
              .retrieveAllAlertConfigurations_1(providerId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveAllAlertConfigurations_1");
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
| **providerId** | **String**|  | |

### Return type

[**SyncroTicketingAlertConfigurationList**](SyncroTicketingAlertConfigurationList.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="retrieveBillingMappingConfigurationOptions"></a>
# **retrieveBillingMappingConfigurationOptions**
> SyncroBillingMappingConfigurationOptionsResp retrieveBillingMappingConfigurationOptions(UUID).fields(fields).filter(filter).limit(limit).skip(skip).sort(sort).execute();

Retrieve Syncro billing mappings dependencies

Retrieves a list of dependencies for Syncro billing mappings.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    List<String> fields = Arrays.asList(); // The comma separated fields included in the returned records. If omitted, the default list of fields will be returned. 
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. 
    try {
      SyncroBillingMappingConfigurationOptionsResp result = client
              .providers
              .retrieveBillingMappingConfigurationOptions(UUID)
              .fields(fields)
              .filter(filter)
              .limit(limit)
              .skip(skip)
              .sort(sort)
              .execute();
      System.out.println(result);
      System.out.println(result.getOptions());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveBillingMappingConfigurationOptions");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<SyncroBillingMappingConfigurationOptionsResp> response = client
              .providers
              .retrieveBillingMappingConfigurationOptions(UUID)
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
      System.err.println("Exception when calling ProvidersApi#retrieveBillingMappingConfigurationOptions");
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
| **UUID** | **String**|  | |
| **fields** | [**List&lt;String&gt;**](String.md)| The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; | [optional] |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  | [optional] |

### Return type

[**SyncroBillingMappingConfigurationOptionsResp**](SyncroBillingMappingConfigurationOptionsResp.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="retrieveCompanies"></a>
# **retrieveCompanies**
> AutotaskCompanyResp retrieveCompanies(UUID).fields(fields).filter(filter).limit(limit).skip(skip).sort(sort).execute();

Retrieve Autotask Companies

Retrieves a list of Autotask companies for the given Autotask id. You must be associated to the same provider as the Autotask integration to use this endpoint.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    List<String> fields = Arrays.asList(); // The comma separated fields included in the returned records. If omitted, the default list of fields will be returned. 
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. 
    try {
      AutotaskCompanyResp result = client
              .providers
              .retrieveCompanies(UUID)
              .fields(fields)
              .filter(filter)
              .limit(limit)
              .skip(skip)
              .sort(sort)
              .execute();
      System.out.println(result);
      System.out.println(result.getRecords());
      System.out.println(result.getTotalCount());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveCompanies");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AutotaskCompanyResp> response = client
              .providers
              .retrieveCompanies(UUID)
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
      System.err.println("Exception when calling ProvidersApi#retrieveCompanies");
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
| **UUID** | **String**|  | |
| **fields** | [**List&lt;String&gt;**](String.md)| The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; | [optional] |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  | [optional] |

### Return type

[**AutotaskCompanyResp**](AutotaskCompanyResp.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="retrieveCompanies_0"></a>
# **retrieveCompanies_0**
> ConnectwiseCompanyResp retrieveCompanies_0(UUID).fields(fields).filter(filter).limit(limit).skip(skip).sort(sort).execute();

Retrieve ConnectWise Companies

Retrieves a list of ConnectWise companies for the given ConnectWise id. You must be associated to the same provider as the ConnectWise integration to use this endpoint.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    List<String> fields = Arrays.asList(); // The comma separated fields included in the returned records. If omitted, the default list of fields will be returned. 
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. 
    try {
      ConnectwiseCompanyResp result = client
              .providers
              .retrieveCompanies_0(UUID)
              .fields(fields)
              .filter(filter)
              .limit(limit)
              .skip(skip)
              .sort(sort)
              .execute();
      System.out.println(result);
      System.out.println(result.getRecords());
      System.out.println(result.getTotalCount());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveCompanies_0");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ConnectwiseCompanyResp> response = client
              .providers
              .retrieveCompanies_0(UUID)
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
      System.err.println("Exception when calling ProvidersApi#retrieveCompanies_0");
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
| **UUID** | **String**|  | |
| **fields** | [**List&lt;String&gt;**](String.md)| The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; | [optional] |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  | [optional] |

### Return type

[**ConnectwiseCompanyResp**](ConnectwiseCompanyResp.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="retrieveCompanies_1"></a>
# **retrieveCompanies_1**
> SyncroCompanyResp retrieveCompanies_1(UUID).fields(fields).filter(filter).limit(limit).skip(skip).sort(sort).execute();

Retrieve Syncro Companies

Retrieves a list of Syncro companies for the given Syncro id. You must be associated to the same provider as the Syncro integration to use this endpoint.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    List<String> fields = Arrays.asList(); // The comma separated fields included in the returned records. If omitted, the default list of fields will be returned. 
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. 
    try {
      SyncroCompanyResp result = client
              .providers
              .retrieveCompanies_1(UUID)
              .fields(fields)
              .filter(filter)
              .limit(limit)
              .skip(skip)
              .sort(sort)
              .execute();
      System.out.println(result);
      System.out.println(result.getRecords());
      System.out.println(result.getTotalCount());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveCompanies_1");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<SyncroCompanyResp> response = client
              .providers
              .retrieveCompanies_1(UUID)
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
      System.err.println("Exception when calling ProvidersApi#retrieveCompanies_1");
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
| **UUID** | **String**|  | |
| **fields** | [**List&lt;String&gt;**](String.md)| The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; | [optional] |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  | [optional] |

### Return type

[**SyncroCompanyResp**](SyncroCompanyResp.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="retrieveCompanyTypes"></a>
# **retrieveCompanyTypes**
> AutotaskCompanyTypeResp retrieveCompanyTypes(UUID).execute();

Retrieve Autotask Company Types

Retrieves a list of user defined company types from Autotask for the given Autotask id.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    try {
      AutotaskCompanyTypeResp result = client
              .providers
              .retrieveCompanyTypes(UUID)
              .execute();
      System.out.println(result);
      System.out.println(result.getRecords());
      System.out.println(result.getTotalCount());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveCompanyTypes");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AutotaskCompanyTypeResp> response = client
              .providers
              .retrieveCompanyTypes(UUID)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveCompanyTypes");
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
| **UUID** | **String**|  | |

### Return type

[**AutotaskCompanyTypeResp**](AutotaskCompanyTypeResp.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="retrieveCompanyTypes_0"></a>
# **retrieveCompanyTypes_0**
> ConnectwiseCompanyTypeResp retrieveCompanyTypes_0(UUID).execute();

Retrieve ConnectWise Company Types

Retrieves a list of user defined company types from ConnectWise for the given ConnectWise id.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    try {
      ConnectwiseCompanyTypeResp result = client
              .providers
              .retrieveCompanyTypes_0(UUID)
              .execute();
      System.out.println(result);
      System.out.println(result.getRecords());
      System.out.println(result.getTotalCount());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveCompanyTypes_0");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ConnectwiseCompanyTypeResp> response = client
              .providers
              .retrieveCompanyTypes_0(UUID)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveCompanyTypes_0");
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
| **UUID** | **String**|  | |

### Return type

[**ConnectwiseCompanyTypeResp**](ConnectwiseCompanyTypeResp.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="retrieveContracts"></a>
# **retrieveContracts**
> AutotaskRetrieveContractsResponse retrieveContracts(UUID).fields(fields).filter(filter).limit(limit).skip(skip).sort(sort).execute();

Retrieve Autotask Contracts

Retrieves a list of Autotask contracts for the given Autotask integration id. You must be associated to the same provider as the Autotask integration to use this endpoint.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    List<String> fields = Arrays.asList(); // The comma separated fields included in the returned records. If omitted, the default list of fields will be returned. 
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. 
    try {
      AutotaskRetrieveContractsResponse result = client
              .providers
              .retrieveContracts(UUID)
              .fields(fields)
              .filter(filter)
              .limit(limit)
              .skip(skip)
              .sort(sort)
              .execute();
      System.out.println(result);
      System.out.println(result.getResults());
      System.out.println(result.getTotalCount());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveContracts");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AutotaskRetrieveContractsResponse> response = client
              .providers
              .retrieveContracts(UUID)
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
      System.err.println("Exception when calling ProvidersApi#retrieveContracts");
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
| **UUID** | **String**|  | |
| **fields** | [**List&lt;String&gt;**](String.md)| The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; | [optional] |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  | [optional] |

### Return type

[**AutotaskRetrieveContractsResponse**](AutotaskRetrieveContractsResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="retrieveContractsFields"></a>
# **retrieveContractsFields**
> AutotaskRetrieveContractsFieldsResponse retrieveContractsFields(UUID).execute();

Retrieve Autotask Contract Fields

Retrieves a list of Autotask contract fields for the given Autotask integration id. You must be associated to the same provider as the Autotask integration to use this endpoint.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    try {
      AutotaskRetrieveContractsFieldsResponse result = client
              .providers
              .retrieveContractsFields(UUID)
              .execute();
      System.out.println(result);
      System.out.println(result.getResults());
      System.out.println(result.getTotalCount());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveContractsFields");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AutotaskRetrieveContractsFieldsResponse> response = client
              .providers
              .retrieveContractsFields(UUID)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveContractsFields");
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
| **UUID** | **String**|  | |

### Return type

[**AutotaskRetrieveContractsFieldsResponse**](AutotaskRetrieveContractsFieldsResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="retrieveIntegrations"></a>
# **retrieveIntegrations**
> IntegrationsResponse retrieveIntegrations(providerId).filter(filter).limit(limit).skip(skip).sort(sort).execute();

Retrieve Integrations for Provider

Retrieves a list of integrations this provider has configured. You must be associated to the provider to use this endpoint.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String providerId = "providerId_example";
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. 
    try {
      IntegrationsResponse result = client
              .providers
              .retrieveIntegrations(providerId)
              .filter(filter)
              .limit(limit)
              .skip(skip)
              .sort(sort)
              .execute();
      System.out.println(result);
      System.out.println(result.getRecords());
      System.out.println(result.getTotalCount());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveIntegrations");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<IntegrationsResponse> response = client
              .providers
              .retrieveIntegrations(providerId)
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
      System.err.println("Exception when calling ProvidersApi#retrieveIntegrations");
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
| **providerId** | **String**|  | |
| **filter** | [**List&lt;String&gt;**](String.md)| A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; | [optional] |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  | [optional] |

### Return type

[**IntegrationsResponse**](IntegrationsResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="retrieveInvoice"></a>
# **retrieveInvoice**
> File retrieveInvoice(providerId, ID).execute();

Download a provider&#39;s invoice.

Retrieves an invoice for this provider. You must be associated to the provider to use this endpoint.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String providerId = "providerId_example";
    String ID = "ID_example";
    try {
      File result = client
              .providers
              .retrieveInvoice(providerId, ID)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveInvoice");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<File> response = client
              .providers
              .retrieveInvoice(providerId, ID)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveInvoice");
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
| **providerId** | **String**|  | |
| **ID** | **String**|  | |

### Return type

[**File**](File.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/pdf, application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="retrieveInvoices"></a>
# **retrieveInvoices**
> ProviderInvoiceResponse retrieveInvoices(providerId).skip(skip).sort(sort).limit(limit).filter(filter).execute();

List a provider&#39;s invoices.

Retrieves a list of invoices for this provider. You must be associated to the provider to use this endpoint.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String providerId = "providerId_example";
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. 
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    try {
      ProviderInvoiceResponse result = client
              .providers
              .retrieveInvoices(providerId)
              .skip(skip)
              .sort(sort)
              .limit(limit)
              .filter(filter)
              .execute();
      System.out.println(result);
      System.out.println(result.getRecords());
      System.out.println(result.getTotalCount());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveInvoices");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ProviderInvoiceResponse> response = client
              .providers
              .retrieveInvoices(providerId)
              .skip(skip)
              .sort(sort)
              .limit(limit)
              .filter(filter)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveInvoices");
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
| **providerId** | **String**|  | |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  | [optional] |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **filter** | [**List&lt;String&gt;**](String.md)| A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; | [optional] |

### Return type

[**ProviderInvoiceResponse**](ProviderInvoiceResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="retrieveMappings"></a>
# **retrieveMappings**
> AutotaskRetrieveMappingsResponse retrieveMappings(UUID).fields(fields).filter(filter).limit(limit).skip(skip).sort(sort).execute();

Retrieve Autotask mappings

Retrieves the list of mappings for this Autotask integration. You must be associated to the same provider as the Autotask integration to use this api.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    List<String> fields = Arrays.asList(); // The comma separated fields included in the returned records. If omitted, the default list of fields will be returned. 
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. 
    try {
      AutotaskRetrieveMappingsResponse result = client
              .providers
              .retrieveMappings(UUID)
              .fields(fields)
              .filter(filter)
              .limit(limit)
              .skip(skip)
              .sort(sort)
              .execute();
      System.out.println(result);
      System.out.println(result.getRecords());
      System.out.println(result.getTotalCount());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveMappings");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AutotaskRetrieveMappingsResponse> response = client
              .providers
              .retrieveMappings(UUID)
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
      System.err.println("Exception when calling ProvidersApi#retrieveMappings");
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
| **UUID** | **String**|  | |
| **fields** | [**List&lt;String&gt;**](String.md)| The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; | [optional] |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  | [optional] |

### Return type

[**AutotaskRetrieveMappingsResponse**](AutotaskRetrieveMappingsResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="retrieveMappings_0"></a>
# **retrieveMappings_0**
> ConnectwiseRetrieveMappingsResponse retrieveMappings_0(UUID).fields(fields).filter(filter).limit(limit).skip(skip).sort(sort).execute();

Retrieve ConnectWise mappings

Retrieves the list of mappings for this ConnectWise integration. You must be associated to the same provider as the ConnectWise integration to use this api.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    List<String> fields = Arrays.asList(); // The comma separated fields included in the returned records. If omitted, the default list of fields will be returned. 
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. 
    try {
      ConnectwiseRetrieveMappingsResponse result = client
              .providers
              .retrieveMappings_0(UUID)
              .fields(fields)
              .filter(filter)
              .limit(limit)
              .skip(skip)
              .sort(sort)
              .execute();
      System.out.println(result);
      System.out.println(result.getRecords());
      System.out.println(result.getTotalCount());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveMappings_0");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ConnectwiseRetrieveMappingsResponse> response = client
              .providers
              .retrieveMappings_0(UUID)
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
      System.err.println("Exception when calling ProvidersApi#retrieveMappings_0");
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
| **UUID** | **String**|  | |
| **fields** | [**List&lt;String&gt;**](String.md)| The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; | [optional] |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  | [optional] |

### Return type

[**ConnectwiseRetrieveMappingsResponse**](ConnectwiseRetrieveMappingsResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="retrieveMappings_1"></a>
# **retrieveMappings_1**
> SyncroRetrieveMappingsResponse retrieveMappings_1(UUID).fields(fields).filter(filter).limit(limit).skip(skip).sort(sort).execute();

Retrieve Syncro mappings

Retrieves the list of mappings for this Syncro integration. You must be associated to the same provider as the Syncro integration to use this api.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    List<String> fields = Arrays.asList(); // The comma separated fields included in the returned records. If omitted, the default list of fields will be returned. 
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. 
    try {
      SyncroRetrieveMappingsResponse result = client
              .providers
              .retrieveMappings_1(UUID)
              .fields(fields)
              .filter(filter)
              .limit(limit)
              .skip(skip)
              .sort(sort)
              .execute();
      System.out.println(result);
      System.out.println(result.getRecords());
      System.out.println(result.getTotalCount());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveMappings_1");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<SyncroRetrieveMappingsResponse> response = client
              .providers
              .retrieveMappings_1(UUID)
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
      System.err.println("Exception when calling ProvidersApi#retrieveMappings_1");
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
| **UUID** | **String**|  | |
| **fields** | [**List&lt;String&gt;**](String.md)| The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; | [optional] |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  | [optional] |

### Return type

[**SyncroRetrieveMappingsResponse**](SyncroRetrieveMappingsResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="retrieveServices"></a>
# **retrieveServices**
> AutotaskRetrieveServicesResponse retrieveServices(UUID).fields(fields).filter(filter).limit(limit).skip(skip).sort(sort).execute();

Retrieve Autotask Contract Services

Retrieves a list of Autotask contract services for the given Autotask integration id. You must be associated to the same provider as the Autotask integration to use this endpoint.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    List<String> fields = Arrays.asList(); // The comma separated fields included in the returned records. If omitted, the default list of fields will be returned. 
    List<String> filter = Arrays.asList(); // A filter to apply to the query.  **Filter structure**: `<field>:<operator>:<value>`.  **field** = Populate with a valid field from an endpoint response.  **operator** =  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** = Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** `GET /api/v2/groups?filter=name:eq:Test+Group`
    Integer limit = 10; // The number of records to return at once. Limited to 100.
    Integer skip = 0; // The offset into the records to return.
    List<String> sort = Arrays.asList(); // The comma separated fields used to sort the collection. Default sort is ascending, prefix with `-` to sort descending. 
    try {
      AutotaskRetrieveServicesResponse result = client
              .providers
              .retrieveServices(UUID)
              .fields(fields)
              .filter(filter)
              .limit(limit)
              .skip(skip)
              .sort(sort)
              .execute();
      System.out.println(result);
      System.out.println(result.getResults());
      System.out.println(result.getTotalCount());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveServices");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AutotaskRetrieveServicesResponse> response = client
              .providers
              .retrieveServices(UUID)
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
      System.err.println("Exception when calling ProvidersApi#retrieveServices");
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
| **UUID** | **String**|  | |
| **fields** | [**List&lt;String&gt;**](String.md)| The comma separated fields included in the returned records. If omitted, the default list of fields will be returned.  | [optional] |
| **filter** | [**List&lt;String&gt;**](String.md)| A filter to apply to the query.  **Filter structure**: &#x60;&lt;field&gt;:&lt;operator&gt;:&lt;value&gt;&#x60;.  **field** &#x3D; Populate with a valid field from an endpoint response.  **operator** &#x3D;  Supported operators are: eq, ne, gt, ge, lt, le, between, search, in. _Note: v1 operators differ from v2 operators._  **value** &#x3D; Populate with the value you want to search for. Is case sensitive. Supports wild cards.  **EX:** &#x60;GET /api/v2/groups?filter&#x3D;name:eq:Test+Group&#x60; | [optional] |
| **limit** | **Integer**| The number of records to return at once. Limited to 100. | [optional] [default to 10] |
| **skip** | **Integer**| The offset into the records to return. | [optional] [default to 0] |
| **sort** | [**List&lt;String&gt;**](String.md)| The comma separated fields used to sort the collection. Default sort is ascending, prefix with &#x60;-&#x60; to sort descending.  | [optional] |

### Return type

[**AutotaskRetrieveServicesResponse**](AutotaskRetrieveServicesResponse.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** |  |  -  |

<a name="retrieveSettings"></a>
# **retrieveSettings**
> AutotaskSettings retrieveSettings(UUID).execute();

Retrieve Autotask Integration settings

Retrieve the Autotask integration settings. You must be associated to the same provider as the Autotask integration to use this endpoint.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    try {
      AutotaskSettings result = client
              .providers
              .retrieveSettings(UUID)
              .execute();
      System.out.println(result);
      System.out.println(result.getAutomaticTicketing());
      System.out.println(result.getCompanyTypeIds());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveSettings");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AutotaskSettings> response = client
              .providers
              .retrieveSettings(UUID)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveSettings");
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
| **UUID** | **String**|  | |

### Return type

[**AutotaskSettings**](AutotaskSettings.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="retrieveSettings_0"></a>
# **retrieveSettings_0**
> ConnectWiseSettings retrieveSettings_0(UUID).execute();

Retrieve ConnectWise Integration settings

Retrieve the ConnectWise integration settings. You must be associated to the same provider as the ConnectWise integration to use this endpoint.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    try {
      ConnectWiseSettings result = client
              .providers
              .retrieveSettings_0(UUID)
              .execute();
      System.out.println(result);
      System.out.println(result.getAutomaticTicketing());
      System.out.println(result.getCompanyTypeIds());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveSettings_0");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ConnectWiseSettings> response = client
              .providers
              .retrieveSettings_0(UUID)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveSettings_0");
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
| **UUID** | **String**|  | |

### Return type

[**ConnectWiseSettings**](ConnectWiseSettings.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="retrieveSettings_1"></a>
# **retrieveSettings_1**
> SyncroSettings retrieveSettings_1(UUID).execute();

Retrieve Syncro Integration settings

Retrieve the Syncro integration settings. You must be associated to the same provider as the Syncro integration to use this endpoint.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    try {
      SyncroSettings result = client
              .providers
              .retrieveSettings_1(UUID)
              .execute();
      System.out.println(result);
      System.out.println(result.getAutomaticTicketing());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveSettings_1");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<SyncroSettings> response = client
              .providers
              .retrieveSettings_1(UUID)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveSettings_1");
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
| **UUID** | **String**|  | |

### Return type

[**SyncroSettings**](SyncroSettings.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="retrieveSyncErrors"></a>
# **retrieveSyncErrors**
> IntegrationSyncErrorResp retrieveSyncErrors(UUID, integrationType).execute();

Retrieve Recent Integration Sync Errors

Retrieves recent sync errors for given integration type and integration id. You must be associated to the provider the integration is tied to in order to use this api.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    String integrationType = "integrationType_example";
    try {
      IntegrationSyncErrorResp result = client
              .providers
              .retrieveSyncErrors(UUID, integrationType)
              .execute();
      System.out.println(result);
      System.out.println(result.getRecords());
      System.out.println(result.getTotalCount());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveSyncErrors");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<IntegrationSyncErrorResp> response = client
              .providers
              .retrieveSyncErrors(UUID, integrationType)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#retrieveSyncErrors");
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
| **UUID** | **String**|  | |
| **integrationType** | **String**|  | |

### Return type

[**IntegrationSyncErrorResp**](IntegrationSyncErrorResp.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="updateAlertConfiguration"></a>
# **updateAlertConfiguration**
> AutotaskTicketingAlertConfiguration updateAlertConfiguration(providerId, alertUUID).autotaskTicketingAlertConfigurationRequest(autotaskTicketingAlertConfigurationRequest).execute();

Update an Autotask ticketing alert&#39;s configuration

Update an Autotask ticketing alert&#39;s configuration

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String destination = "queue";
    Integer dueDays = 56;
    AutotaskTicketingAlertConfigurationPriority priority = new AutotaskTicketingAlertConfigurationPriority();
    Boolean shouldCreateTickets = true;
    AutotaskTicketingAlertConfigurationPriority status = new AutotaskTicketingAlertConfigurationPriority();
    String providerId = "providerId_example";
    String alertUUID = "alertUUID_example";
    AutotaskTicketingAlertConfigurationPriority queue = new AutotaskTicketingAlertConfigurationPriority();
    AutotaskTicketingAlertConfigurationResource resource = new AutotaskTicketingAlertConfigurationResource();
    AutotaskTicketingAlertConfigurationPriority source = new AutotaskTicketingAlertConfigurationPriority();
    try {
      AutotaskTicketingAlertConfiguration result = client
              .providers
              .updateAlertConfiguration(destination, dueDays, priority, shouldCreateTickets, status, providerId, alertUUID)
              .queue(queue)
              .resource(resource)
              .source(source)
              .execute();
      System.out.println(result);
      System.out.println(result.getDescription());
      System.out.println(result.getCategory());
      System.out.println(result.getDestination());
      System.out.println(result.getDisplayName());
      System.out.println(result.getDueDays());
      System.out.println(result.getId());
      System.out.println(result.getPriority());
      System.out.println(result.getQueue());
      System.out.println(result.getResource());
      System.out.println(result.getShouldCreateTickets());
      System.out.println(result.getSource());
      System.out.println(result.getStatus());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#updateAlertConfiguration");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AutotaskTicketingAlertConfiguration> response = client
              .providers
              .updateAlertConfiguration(destination, dueDays, priority, shouldCreateTickets, status, providerId, alertUUID)
              .queue(queue)
              .resource(resource)
              .source(source)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#updateAlertConfiguration");
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
| **providerId** | **String**|  | |
| **alertUUID** | **String**|  | |
| **autotaskTicketingAlertConfigurationRequest** | [**AutotaskTicketingAlertConfigurationRequest**](AutotaskTicketingAlertConfigurationRequest.md)|  | [optional] |

### Return type

[**AutotaskTicketingAlertConfiguration**](AutotaskTicketingAlertConfiguration.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="updateAlertConfiguration_0"></a>
# **updateAlertConfiguration_0**
> ConnectWiseTicketingAlertConfiguration updateAlertConfiguration_0(providerId, alertUUID).connectWiseTicketingAlertConfigurationRequest(connectWiseTicketingAlertConfigurationRequest).execute();

Update a ConnectWise ticketing alert&#39;s configuration

Update a ConnectWise ticketing alert&#39;s configuration.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    Boolean shouldCreateTickets = true;
    String providerId = "providerId_example";
    String alertUUID = "alertUUID_example";
    Integer dueDays = 56;
    AutotaskTicketingAlertConfigurationPriority priority = new AutotaskTicketingAlertConfigurationPriority();
    AutotaskTicketingAlertConfigurationPriority source = new AutotaskTicketingAlertConfigurationPriority();
    try {
      ConnectWiseTicketingAlertConfiguration result = client
              .providers
              .updateAlertConfiguration_0(shouldCreateTickets, providerId, alertUUID)
              .dueDays(dueDays)
              .priority(priority)
              .source(source)
              .execute();
      System.out.println(result);
      System.out.println(result.getDescription());
      System.out.println(result.getCategory());
      System.out.println(result.getDisplayName());
      System.out.println(result.getDueDays());
      System.out.println(result.getId());
      System.out.println(result.getPriority());
      System.out.println(result.getShouldCreateTickets());
      System.out.println(result.getSource());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#updateAlertConfiguration_0");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ConnectWiseTicketingAlertConfiguration> response = client
              .providers
              .updateAlertConfiguration_0(shouldCreateTickets, providerId, alertUUID)
              .dueDays(dueDays)
              .priority(priority)
              .source(source)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#updateAlertConfiguration_0");
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
| **providerId** | **String**|  | |
| **alertUUID** | **String**|  | |
| **connectWiseTicketingAlertConfigurationRequest** | [**ConnectWiseTicketingAlertConfigurationRequest**](ConnectWiseTicketingAlertConfigurationRequest.md)|  | [optional] |

### Return type

[**ConnectWiseTicketingAlertConfiguration**](ConnectWiseTicketingAlertConfiguration.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="updateAlertConfiguration_1"></a>
# **updateAlertConfiguration_1**
> SyncroTicketingAlertConfiguration updateAlertConfiguration_1(providerId, alertUUID).syncroTicketingAlertConfigurationRequest(syncroTicketingAlertConfigurationRequest).execute();

Update a Syncro ticketing alert&#39;s configuration

Update a Syncro ticketing alert&#39;s configuration

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String problemType = "problemType_example";
    Boolean shouldCreateTickets = true;
    String providerId = "providerId_example";
    String alertUUID = "alertUUID_example";
    Integer dueDays = 56;
    String priority = "priority_example";
    String status = "status_example";
    Double userId = 3.4D;
    String username = "username_example";
    try {
      SyncroTicketingAlertConfiguration result = client
              .providers
              .updateAlertConfiguration_1(problemType, shouldCreateTickets, providerId, alertUUID)
              .dueDays(dueDays)
              .priority(priority)
              .status(status)
              .userId(userId)
              .username(username)
              .execute();
      System.out.println(result);
      System.out.println(result.getDescription());
      System.out.println(result.getCategory());
      System.out.println(result.getDisplayName());
      System.out.println(result.getDueDays());
      System.out.println(result.getId());
      System.out.println(result.getPriority());
      System.out.println(result.getProblemType());
      System.out.println(result.getShouldCreateTickets());
      System.out.println(result.getStatus());
      System.out.println(result.getUserId());
      System.out.println(result.getUsername());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#updateAlertConfiguration_1");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<SyncroTicketingAlertConfiguration> response = client
              .providers
              .updateAlertConfiguration_1(problemType, shouldCreateTickets, providerId, alertUUID)
              .dueDays(dueDays)
              .priority(priority)
              .status(status)
              .userId(userId)
              .username(username)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#updateAlertConfiguration_1");
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
| **providerId** | **String**|  | |
| **alertUUID** | **String**|  | |
| **syncroTicketingAlertConfigurationRequest** | [**SyncroTicketingAlertConfigurationRequest**](SyncroTicketingAlertConfigurationRequest.md)|  | [optional] |

### Return type

[**SyncroTicketingAlertConfiguration**](SyncroTicketingAlertConfiguration.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="updateConfiguration"></a>
# **updateConfiguration**
> AutotaskIntegration updateConfiguration(UUID).autotaskIntegrationPatchReq(autotaskIntegrationPatchReq).execute();

Update Autotask Integration configuration

Update the Autotask integration configuration. A 422 Unprocessable Entity response means the server failed to validate with Autotask.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    String secret = "secret_example"; // The secret for connecting to Autotask.
    String username = "username_example"; // The username for connecting to Autotask.
    try {
      AutotaskIntegration result = client
              .providers
              .updateConfiguration(UUID)
              .secret(secret)
              .username(username)
              .execute();
      System.out.println(result);
      System.out.println(result.getId());
      System.out.println(result.getIsMspAuthConfigured());
      System.out.println(result.getUsername());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#updateConfiguration");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AutotaskIntegration> response = client
              .providers
              .updateConfiguration(UUID)
              .secret(secret)
              .username(username)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#updateConfiguration");
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
| **UUID** | **String**|  | |
| **autotaskIntegrationPatchReq** | [**AutotaskIntegrationPatchReq**](AutotaskIntegrationPatchReq.md)|  | [optional] |

### Return type

[**AutotaskIntegration**](AutotaskIntegration.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="updateConfiguration_0"></a>
# **updateConfiguration_0**
> ConnectwiseIntegration updateConfiguration_0(UUID).connectwiseIntegrationPatchReq(connectwiseIntegrationPatchReq).execute();

Update ConnectWise Integration configuration

Update the ConnectWise integration configuration. A 422 Unprocessable Entity response means the server failed to validate with ConnectWise.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    String companyId = "companyId_example"; // The ConnectWise company identifier.
    String privateKey = "privateKey_example"; // The ConnectWise private key for authentication
    String publicKey = "publicKey_example"; // The ConnectWise public key for authentication.
    String url = "url_example"; // The base url for connecting to ConnectWise.
    try {
      ConnectwiseIntegration result = client
              .providers
              .updateConfiguration_0(UUID)
              .companyId(companyId)
              .privateKey(privateKey)
              .publicKey(publicKey)
              .url(url)
              .execute();
      System.out.println(result);
      System.out.println(result.getCompanyId());
      System.out.println(result.getId());
      System.out.println(result.getIsMspAuthConfigured());
      System.out.println(result.getUrl());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#updateConfiguration_0");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ConnectwiseIntegration> response = client
              .providers
              .updateConfiguration_0(UUID)
              .companyId(companyId)
              .privateKey(privateKey)
              .publicKey(publicKey)
              .url(url)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#updateConfiguration_0");
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
| **UUID** | **String**|  | |
| **connectwiseIntegrationPatchReq** | [**ConnectwiseIntegrationPatchReq**](ConnectwiseIntegrationPatchReq.md)|  | [optional] |

### Return type

[**ConnectwiseIntegration**](ConnectwiseIntegration.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="updateConfiguration_1"></a>
# **updateConfiguration_1**
> SyncroIntegration updateConfiguration_1(UUID).syncroIntegrationPatchReq(syncroIntegrationPatchReq).execute();

Update Syncro Integration configuration

Update the Syncro integration configuration. A 422 Unprocessable Entity response means the server failed to validate with Syncro.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String UUID = "UUID_example";
    String apiToken = "apiToken_example"; // The Syncro API token for authentication
    String subdomain = "subdomain_example"; // The subdomain for the URL to connect to Syncro.
    try {
      SyncroIntegration result = client
              .providers
              .updateConfiguration_1(UUID)
              .apiToken(apiToken)
              .subdomain(subdomain)
              .execute();
      System.out.println(result);
      System.out.println(result.getId());
      System.out.println(result.getIsMspAuthConfigured());
      System.out.println(result.getSubdomain());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#updateConfiguration_1");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<SyncroIntegration> response = client
              .providers
              .updateConfiguration_1(UUID)
              .apiToken(apiToken)
              .subdomain(subdomain)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#updateConfiguration_1");
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
| **UUID** | **String**|  | |
| **syncroIntegrationPatchReq** | [**SyncroIntegrationPatchReq**](SyncroIntegrationPatchReq.md)|  | [optional] |

### Return type

[**SyncroIntegration**](SyncroIntegration.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="updateOrg"></a>
# **updateOrg**
> Organization updateOrg(providerId, id).organization(organization).execute();

Update Provider Organization

This endpoint updates a provider&#39;s organization

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.JumpCloud;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProvidersApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://console.jumpcloud.com/api/v2";
    
    configuration.xApiKey  = "YOUR API KEY";
    JumpCloud client = new JumpCloud(configuration);
    String providerId = "providerId_example";
    String id = "id_example";
    String id = "id_example";
    Integer maxSystemUsers = 56; // The maximum number of users allowed in this organization. Requires organizations.billing scope to modify.
    String name = "name_example";
    try {
      Organization result = client
              .providers
              .updateOrg(providerId, id)
              .id(id)
              .maxSystemUsers(maxSystemUsers)
              .name(name)
              .execute();
      System.out.println(result);
      System.out.println(result.getId());
      System.out.println(result.getMaxSystemUsers());
      System.out.println(result.getName());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#updateOrg");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Organization> response = client
              .providers
              .updateOrg(providerId, id)
              .id(id)
              .maxSystemUsers(maxSystemUsers)
              .name(name)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProvidersApi#updateOrg");
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
| **providerId** | **String**|  | |
| **id** | **String**|  | |
| **organization** | [**Organization**](Organization.md)|  | [optional] |

### Return type

[**Organization**](Organization.md)

### Authorization

[x-api-key](../README.md#x-api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

