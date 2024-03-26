

# BulkScheduledStatechangeCreate

Model to support bulk scheduling of a state change for one or more users

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**activationEmailOverride** | **String** | Send the activation or welcome email to the specified email address upon activation. Can only be used with a single user_id and scheduled activation. This field will be ignored if &#x60;send_activation_emails&#x60; is explicitly set to false. |  [optional] |
|**sendActivationEmails** | **Boolean** | Set to true to send activation or welcome email(s) to each user_id upon activation. Set to false to suppress emails. Can only be used with scheduled activation(s). |  [optional] |
|**startDate** | **OffsetDateTime** | Date and time that scheduled action should occur |  |
|**state** | [**StateEnum**](#StateEnum) | The state to move the user(s) to |  |
|**userIds** | **List&lt;String&gt;** | Array of system user ids to schedule for a state change |  |



## Enum: StateEnum

| Name | Value |
|---- | -----|
| ACTIVATED | &quot;ACTIVATED&quot; |
| SUSPENDED | &quot;SUSPENDED&quot; |



