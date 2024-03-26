

# LdapServer


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **String** | Unique identifier of this LDAP server |  [optional] [readonly] |
|**name** | **String** | The name of this LDAP server |  [optional] |
|**userLockoutAction** | [**UserLockoutActionEnum**](#UserLockoutActionEnum) | action to take; one of &#39;remove&#39; or &#39;disable&#39; |  [optional] |
|**userPasswordExpirationAction** | [**UserPasswordExpirationActionEnum**](#UserPasswordExpirationActionEnum) | action to take; one of &#39;remove&#39; or &#39;disable&#39; |  [optional] |



## Enum: UserLockoutActionEnum

| Name | Value |
|---- | -----|
| DISABLE | &quot;disable&quot; |
| REMOVE | &quot;remove&quot; |



## Enum: UserPasswordExpirationActionEnum

| Name | Value |
|---- | -----|
| DISABLE | &quot;disable&quot; |
| REMOVE | &quot;remove&quot; |



