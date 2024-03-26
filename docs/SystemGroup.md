

# SystemGroup


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**description** | **String** | Description of a System Group |  [optional] |
|**attributes** | **Map&lt;String, Object&gt;** | The graph attributes. |  [optional] |
|**email** | **String** | E-mail address associated with a System Group |  [optional] |
|**id** | **String** | ObjectId uniquely identifying a System Group. |  [optional] |
|**memberQuery** | [**MemberQuery**](MemberQuery.md) |  |  [optional] |
|**memberQueryExemptions** | [**List&lt;GraphObject&gt;**](GraphObject.md) | Array of GraphObjects exempted from the query |  [optional] |
|**memberSuggestionsNotify** | **Boolean** | True if notification emails are to be sent for membership suggestions. |  [optional] |
|**membershipMethod** | **GroupMembershipMethodType** |  |  [optional] |
|**name** | **String** | Display name of a System Group. |  [optional] |
|**type** | [**TypeEnum**](#TypeEnum) | The type of the group; always &#39;system&#39; for a System Group. |  [optional] |



## Enum: TypeEnum

| Name | Value |
|---- | -----|
| SYSTEM_GROUP | &quot;system_group&quot; |



