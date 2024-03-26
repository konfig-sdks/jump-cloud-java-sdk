

# UserGroup


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**description** | **String** | Description of a User Group |  [optional] |
|**attributes** | [**GroupAttributesUserGroup**](GroupAttributesUserGroup.md) |  |  [optional] |
|**email** | **String** | Email address of a User Group |  [optional] |
|**id** | **String** | ObjectId uniquely identifying a User Group. |  [optional] |
|**memberQuery** | [**MemberQuery**](MemberQuery.md) |  |  [optional] |
|**memberQueryExemptions** | [**List&lt;GraphObject&gt;**](GraphObject.md) | Array of GraphObjects exempted from the query |  [optional] |
|**memberSuggestionsNotify** | **Boolean** | True if notification emails are to be sent for membership suggestions. |  [optional] |
|**membershipMethod** | **GroupMembershipMethodType** |  |  [optional] |
|**name** | **String** | Display name of a User Group. |  [optional] |
|**suggestionCounts** | [**SuggestionCounts**](SuggestionCounts.md) |  |  [optional] |
|**type** | [**TypeEnum**](#TypeEnum) | The type of the group. |  [optional] |



## Enum: TypeEnum

| Name | Value |
|---- | -----|
| USER_GROUP | &quot;user_group&quot; |



