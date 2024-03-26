

# GraphOperationUserGroup


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **String** | The ObjectID of graph object being added or removed as an association. |  |
|**op** | [**OpEnum**](#OpEnum) | How to modify the graph connection. |  |
|**attributes** | **Map&lt;String, Object&gt;** | The graph attributes. |  [optional] |
|**type** | [**TypeEnum**](#TypeEnum) | Targets which a \&quot;user_group\&quot; can be associated to. |  |



## Enum: OpEnum

| Name | Value |
|---- | -----|
| ADD | &quot;add&quot; |
| REMOVE | &quot;remove&quot; |
| UPDATE | &quot;update&quot; |



## Enum: TypeEnum

| Name | Value |
|---- | -----|
| ACTIVE_DIRECTORY | &quot;active_directory&quot; |
| APPLICATION | &quot;application&quot; |
| G_SUITE | &quot;g_suite&quot; |
| LDAP_SERVER | &quot;ldap_server&quot; |
| OFFICE_365 | &quot;office_365&quot; |
| RADIUS_SERVER | &quot;radius_server&quot; |
| SYSTEM | &quot;system&quot; |
| SYSTEM_GROUP | &quot;system_group&quot; |



