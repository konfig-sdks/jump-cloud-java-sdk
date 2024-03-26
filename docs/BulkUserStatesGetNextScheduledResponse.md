

# BulkUserStatesGetNextScheduledResponse


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**eventsCount** | **Integer** | The total number of ACTIVATED and SUSPENDED events to a max depth of 1 for all of the users in the query. A value larger than the limit specified on the query indicates that additional calls are needed, using a skip greater than 0, to retrieve the full set of results. |  [optional] |
|**results** | [**List&lt;ScheduledUserstateResult&gt;**](ScheduledUserstateResult.md) |  |  [optional] |


