

# Directory



## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**defaultDomain** | [**DirectoryDefaultDomain**](DirectoryDefaultDomain.md) |  |  [optional] |
|**id** | **String** | The ObjectID of the directory. |  |
|**name** | **String** | The name of the directory. |  |
|**oAuthStatus** | **Object** | the expiry and error status of the bearer token |  [optional] |
|**type** | [**TypeEnum**](#TypeEnum) | The type of directory. |  |



## Enum: TypeEnum

| Name | Value |
|---- | -----|
| ACTIVE_DIRECTORY | &quot;active_directory&quot; |
| G_SUITE | &quot;g_suite&quot; |
| LDAP_SERVER | &quot;ldap_server&quot; |
| OFFICE_365 | &quot;office_365&quot; |
| WORKDAY | &quot;workday&quot; |


