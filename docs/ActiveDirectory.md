

# ActiveDirectory


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**domain** | **String** | Domain name for this Active Directory instance. |  [optional] |
|**id** | **String** | ObjectID of this Active Directory instance. |  [optional] [readonly] |
|**primaryAgent** | **String** | ObjectID of the primary agent of domain. |  [optional] [readonly] |
|**useCase** | [**UseCaseEnum**](#UseCaseEnum) |  |  [optional] |



## Enum: UseCaseEnum

| Name | Value |
|---- | -----|
| UNSET | &quot;UNSET&quot; |
| TWOWAYSYNC | &quot;TWOWAYSYNC&quot; |
| JCASAUTHORITY | &quot;JCASAUTHORITY&quot; |
| ADASAUTHORITY | &quot;ADASAUTHORITY&quot; |



