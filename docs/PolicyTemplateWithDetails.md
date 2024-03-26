

# PolicyTemplateWithDetails

The shallow information about a Policy Template.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**description** | **String** | The default description for the Policy. |  [optional] |
|**activation** | **String** | Requirements before the policy can be activated. |  [optional] |
|**behavior** | **String** | Specifics about the behavior of the policy. |  [optional] |
|**configFields** | [**List&lt;PolicyTemplateConfigField&gt;**](PolicyTemplateConfigField.md) | An unordered list of all the fields that can be configured for this Policy Template. |  [optional] |
|**displayName** | **String** | The default display name for the Policy. |  [optional] |
|**id** | **String** | ObjectId uniquely identifying a Policy Template. |  [optional] |
|**name** | **String** | The unique name for the Policy Template. |  [optional] |
|**osMetaFamily** | [**OsMetaFamilyEnum**](#OsMetaFamilyEnum) |  |  [optional] |
|**osRestrictions** | [**List&lt;OSRestriction&gt;**](OSRestriction.md) |  |  [optional] |



## Enum: OsMetaFamilyEnum

| Name | Value |
|---- | -----|
| LINUX | &quot;linux&quot; |
| DARWIN | &quot;darwin&quot; |
| WINDOWS | &quot;windows&quot; |
| IOS | &quot;ios&quot; |
| UNIVERSAL | &quot;universal&quot; |
| ANDROID | &quot;android&quot; |



