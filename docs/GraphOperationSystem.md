

# GraphOperationSystem


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **String** | The ObjectID of graph object being added or removed as an association. |  |
|**op** | [**OpEnum**](#OpEnum) | How to modify the graph connection. |  |
|**attributes** | [**Map**](Map.md) |  |  [optional] |
|**type** | [**TypeEnum**](#TypeEnum) | Targets which a \&quot;system\&quot; can be associated to. |  |



## Enum: OpEnum

| Name | Value |
|---- | -----|
| ADD | &quot;add&quot; |
| REMOVE | &quot;remove&quot; |
| UPDATE | &quot;update&quot; |



## Enum: TypeEnum

| Name | Value |
|---- | -----|
| COMMAND | &quot;command&quot; |
| POLICY | &quot;policy&quot; |
| POLICY_GROUP | &quot;policy_group&quot; |
| USER | &quot;user&quot; |
| USER_GROUP | &quot;user_group&quot; |



