

# GraphOperationPolicyGroupMember


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **String** | The ObjectID of graph object being added or removed as an association. |  |
|**op** | [**OpEnum**](#OpEnum) | How to modify the graph connection. |  |
|**attributes** | **Map&lt;String, Object&gt;** | The graph attributes. |  [optional] |
|**type** | [**TypeEnum**](#TypeEnum) | The member type. |  |



## Enum: OpEnum

| Name | Value |
|---- | -----|
| ADD | &quot;add&quot; |
| REMOVE | &quot;remove&quot; |
| UPDATE | &quot;update&quot; |



## Enum: TypeEnum

| Name | Value |
|---- | -----|
| POLICY | &quot;policy&quot; |



