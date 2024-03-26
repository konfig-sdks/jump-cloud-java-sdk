

# ImportUsersRequest


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**allowUserReactivation** | **Boolean** | A boolean value to allow the reactivation of suspended users |  [optional] |
|**operations** | **List&lt;ImportOperation&gt;** | Operations to be performed on the user list returned from the application |  [optional] |
|**queryString** | **String** | Query string to filter and sort the user list returned from the application.  The supported filtering and sorting varies by application.  If no value is sent, all users are returned. **Example:** \&quot;location&#x3D;Chicago&amp;department&#x3D;IT\&quot;Query string used to retrieve users from service |  [optional] |



