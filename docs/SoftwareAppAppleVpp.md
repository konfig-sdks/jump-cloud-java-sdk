

# SoftwareAppAppleVpp

appleVpp is an optional attribute, it will only be present on apps with a 'setting' 'package_manager' type of 'APPLE_VPP'.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**appConfiguration** | **String** | Text sent to configure the application, the text should be a valid plist.  Returned only by &#39;GET /softwareapps/{id}&#39;. |  [optional] |
|**assignedLicenses** | **Integer** |  |  [optional] |
|**availableLicenses** | **Integer** |  |  [optional] |
|**details** | **Object** | App details returned by iTunes API. See example. The properties in this field are out of our control and we cannot guarantee consistency, so it should be checked by the client and manage the details accordingly. |  [optional] |
|**isConfigEnabled** | **Boolean** | Denotes if configuration has been enabled for the application.  Returned only by &#39;&#39;GET /softwareapps/{id}&#39;&#39;. |  [optional] |
|**supportedDeviceFamilies** | [**List&lt;SupportedDeviceFamiliesEnum&gt;**](#List&lt;SupportedDeviceFamiliesEnum&gt;) | The supported device families for this VPP Application. |  [optional] |
|**totalLicenses** | **Integer** |  |  [optional] |



## Enum: List&lt;SupportedDeviceFamiliesEnum&gt;

| Name | Value |
|---- | -----|
| IPAD | &quot;IPAD&quot; |
| IPHONE | &quot;IPHONE&quot; |
| IPOD | &quot;IPOD&quot; |
| MAC | &quot;MAC&quot; |



