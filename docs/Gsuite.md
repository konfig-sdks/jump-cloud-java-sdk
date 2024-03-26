

# Gsuite


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**defaultDomain** | [**DefaultDomain**](DefaultDomain.md) |  |  [optional] |
|**groupsEnabled** | **Boolean** |  |  [optional] |
|**id** | **String** |  |  [optional] [readonly] |
|**name** | **String** |  |  [optional] |
|**userLockoutAction** | [**UserLockoutActionEnum**](#UserLockoutActionEnum) |  |  [optional] |
|**userPasswordExpirationAction** | [**UserPasswordExpirationActionEnum**](#UserPasswordExpirationActionEnum) |  |  [optional] |



## Enum: UserLockoutActionEnum

| Name | Value |
|---- | -----|
| SUSPEND | &quot;suspend&quot; |
| MAINTAIN | &quot;maintain&quot; |



## Enum: UserPasswordExpirationActionEnum

| Name | Value |
|---- | -----|
| SUSPEND | &quot;suspend&quot; |
| MAINTAIN | &quot;maintain&quot; |
| REMOVE_ACCESS | &quot;remove_access&quot; |



