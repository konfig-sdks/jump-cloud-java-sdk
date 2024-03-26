

# PolicyTemplateConfigField


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**defaultValue** | **String** | The default value for this field. |  [optional] |
|**displayOptions** | **Object** | The options that correspond to the display_type. |  [optional] |
|**displayType** | [**DisplayTypeEnum**](#DisplayTypeEnum) | The default rendering for this field. |  [optional] |
|**id** | **String** | ObjectId uniquely identifying a Policy Template Configuration Field |  |
|**label** | **String** | The default label for this field. |  [optional] |
|**name** | **String** | A unique name identifying this config field. |  |
|**position** | **Double** | The default position to render this field. |  [optional] |
|**readOnly** | **Boolean** | If an admin is allowed to modify this field. |  [optional] |
|**required** | **Boolean** | If this field is required for this field. |  [optional] |
|**sensitive** | **Boolean** | Defines if the policy template config field is sensitive or not. |  [optional] |
|**tooltip** | [**PolicyTemplateConfigFieldTooltip**](PolicyTemplateConfigFieldTooltip.md) |  |  [optional] |
|**validators** | **Object** | Descriptors to perform extended assertions on the supplied config field value. |  [optional] |



## Enum: DisplayTypeEnum

| Name | Value |
|---- | -----|
| CHECKBOX | &quot;checkbox&quot; |
| DATE | &quot;date&quot; |
| EMAIL | &quot;email&quot; |
| FILE | &quot;file&quot; |
| NUMBER | &quot;number&quot; |
| SELECT | &quot;select&quot; |
| TEXT | &quot;text&quot; |
| TEXTAREA | &quot;textarea&quot; |
| SINGLELISTBOX | &quot;singlelistbox&quot; |
| DOUBLELISTBOX | &quot;doublelistbox&quot; |
| TABLE | &quot;table&quot; |
| SEGMENTEDBUTTON | &quot;segmentedbutton&quot; |
| RADIO | &quot;radio&quot; |
| COPYWELL | &quot;copywell&quot; |
| TIMEINPUT | &quot;timeinput&quot; |
| DATEPICKERRANGE | &quot;datepickerrange&quot; |
| MULTILIST | &quot;multilist&quot; |



