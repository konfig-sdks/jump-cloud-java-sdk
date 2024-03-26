

# AutotaskTicketingAlertConfigurationRequest

An AutotaskTicketingAlertConfigurationRequest object requires a queueId if the destination is queue. If the destination is resource, resource.id and resource.role.id are required.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**destination** | [**DestinationEnum**](#DestinationEnum) |  |  |
|**dueDays** | **Integer** |  |  |
|**priority** | [**AutotaskTicketingAlertConfigurationPriority**](AutotaskTicketingAlertConfigurationPriority.md) |  |  |
|**queue** | [**AutotaskTicketingAlertConfigurationPriority**](AutotaskTicketingAlertConfigurationPriority.md) |  |  [optional] |
|**resource** | [**AutotaskTicketingAlertConfigurationResource**](AutotaskTicketingAlertConfigurationResource.md) |  |  [optional] |
|**shouldCreateTickets** | **Boolean** |  |  |
|**source** | [**AutotaskTicketingAlertConfigurationPriority**](AutotaskTicketingAlertConfigurationPriority.md) |  |  [optional] |
|**status** | [**AutotaskTicketingAlertConfigurationPriority**](AutotaskTicketingAlertConfigurationPriority.md) |  |  |



## Enum: DestinationEnum

| Name | Value |
|---- | -----|
| QUEUE | &quot;queue&quot; |
| RESOURCE | &quot;resource&quot; |



