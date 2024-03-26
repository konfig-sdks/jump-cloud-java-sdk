

# ErrorDetails


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**code** | **Integer** | HTTP status code |  [optional] |
|**message** | **String** | Error message |  [optional] |
|**status** | **String** | HTTP status description |  [optional] |
|**details** | **List&lt;Map&lt;String, Object&gt;&gt;** | Describes a list of objects with more detailed information of the given error. Each detail schema is according to one of the messages defined in Google&#39;s API: https://github.com/googleapis/googleapis/blob/master/google/rpc/error_details.proto |  [optional] |



