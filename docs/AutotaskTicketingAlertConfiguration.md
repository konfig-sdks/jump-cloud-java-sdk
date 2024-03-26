

# AutotaskTicketingAlertConfiguration

An AutotaskTicketingAlertConfiguration object requires a queueId if the destination is queue. If the destination is resource, resource.id and resource.role.id are required.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
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



