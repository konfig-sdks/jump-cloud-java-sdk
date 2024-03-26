

# SoftwareAppPermissionGrants


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **String** | An opaque string uniquely identifying the Android permission, e.g. android.permission.READ_CALENDAR. |  [optional] |
|**policy** | [**PolicyEnum**](#PolicyEnum) | The policy for granting the permission. |  [optional] |



## Enum: PolicyEnum

| Name | Value |
|---- | -----|
| PROMPT | &quot;PROMPT&quot; |
| GRANT | &quot;GRANT&quot; |
| DENY | &quot;DENY&quot; |



