

# AutotaskTicketingAlertConfigurationListRecordsInner


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**alertId** | **String** |  |  [optional] |
|**description** | **String** |  |  [optional] |
|**category** | **String** |  |  [optional] |
|**destination** | [**DestinationEnum**](#DestinationEnum) |  |  [optional] |
|**displayName** | **String** |  |  [optional] |
|**dueDays** | **Integer** |  |  [optional] |
|**id** | **Integer** |  |  [optional] |
|**priority** | [**AutotaskTicketingAlertConfigurationPriority**](AutotaskTicketingAlertConfigurationPriority.md) |  |  [optional] |
|**queue** | [**AutotaskTicketingAlertConfigurationPriority**](AutotaskTicketingAlertConfigurationPriority.md) |  |  [optional] |
|**resource** | [**AutotaskTicketingAlertConfigurationResource**](AutotaskTicketingAlertConfigurationResource.md) |  |  [optional] |
|**shouldCreateTickets** | **Boolean** |  |  [optional] |
|**source** | [**AutotaskTicketingAlertConfigurationPriority**](AutotaskTicketingAlertConfigurationPriority.md) |  |  [optional] |
|**status** | [**AutotaskTicketingAlertConfigurationPriority**](AutotaskTicketingAlertConfigurationPriority.md) |  |  [optional] |



## Enum: DestinationEnum

| Name | Value |
|---- | -----|
| QUEUE | &quot;queue&quot; |
| RESOURCE | &quot;resource&quot; |



