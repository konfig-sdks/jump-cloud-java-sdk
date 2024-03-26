

# GraphOperationLdapServer


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **String** | The ObjectID of graph object being added or removed as an association. |  |
|**op** | [**OpEnum**](#OpEnum) | How to modify the graph connection. |  |
|**attributes** | **Map&lt;String, Object&gt;** | The graph attributes. |  [optional] |
|**type** | [**TypeEnum**](#TypeEnum) | Targets which a \&quot;ldap_server\&quot; can be associated to. |  |



## Enum: OpEnum

| Name | Value |
|---- | -----|
| ADD | &quot;add&quot; |
| REMOVE | &quot;remove&quot; |
| UPDATE | &quot;update&quot; |



## Enum: TypeEnum

| Name | Value |
|---- | -----|
| USER | &quot;user&quot; |
| USER_GROUP | &quot;user_group&quot; |



