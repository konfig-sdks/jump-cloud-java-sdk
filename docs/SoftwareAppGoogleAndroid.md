

# SoftwareAppGoogleAndroid

googleAndroid is an optional attribute, it will only be present on apps with a 'setting' 'package_manager' type of 'GOOGLE_ANDROID'.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**androidFeatures** | **List&lt;String&gt;** | The array of android features for the app. |  [optional] |
|**appPricing** | **String** | Whether this app is free, free with in-app purchases, or paid. |  [optional] |
|**appVersion** | **String** | Latest version currently available for this app. |  [optional] |
|**author** | **String** | The name of the author of this app. |  [optional] |
|**autoUpdateMode** | [**AutoUpdateModeEnum**](#AutoUpdateModeEnum) | Controls the auto-update mode for the app. |  [optional] |
|**category** | **String** | The app category (e.g. COMMUNICATION, SOCIAL, etc.). |  [optional] |
|**contentRating** | **String** | The content rating for this app. |  [optional] |
|**displayMode** | **String** | The display mode of the web app. |  [optional] |
|**distributionChannel** | **String** | How and to whom the package is made available. |  [optional] |
|**fullDescription** | **String** | Full app description, if available. |  [optional] |
|**iconUrl** | **String** | A link to an image that can be used as an icon for the app. |  [optional] |
|**installType** | [**InstallTypeEnum**](#InstallTypeEnum) | The type of installation to perform for an app. |  [optional] |
|**managedConfigurationTemplateId** | **String** | The managed configurations template for the app. |  [optional] |
|**managedProperties** | **Boolean** | Indicates whether this app has managed properties or not. |  [optional] |
|**minSdkVersion** | **Integer** | The minimum Android SDK necessary to run the app. |  [optional] |
|**name** | **String** | The name of the app in the form enterprises/{enterprise}/applications/{packageName}. |  [optional] |
|**permissionGrants** | [**List&lt;SoftwareAppPermissionGrants&gt;**](SoftwareAppPermissionGrants.md) |  |  [optional] |
|**runtimePermission** | [**RuntimePermissionEnum**](#RuntimePermissionEnum) | The policy for granting permission requests to apps. |  [optional] |
|**startUrl** | **String** | The start URL, i.e. the URL that should load when the user opens the application. Applicable only for webapps. |  [optional] |
|**type** | [**TypeEnum**](#TypeEnum) | Type of this android application. |  [optional] |
|**updateTime** | **String** | The approximate time (within 7 days) the app was last published. |  [optional] |
|**versionCode** | **Integer** | The current version of the web app. |  [optional] |



## Enum: AutoUpdateModeEnum

| Name | Value |
|---- | -----|
| DEFAULT | &quot;AUTO_UPDATE_DEFAULT&quot; |
| POSTPONED | &quot;AUTO_UPDATE_POSTPONED&quot; |
| HIGH_PRIORITY | &quot;AUTO_UPDATE_HIGH_PRIORITY&quot; |



## Enum: InstallTypeEnum

| Name | Value |
|---- | -----|
| AVAILABLE | &quot;AVAILABLE&quot; |
| FORCE_INSTALLED | &quot;FORCE_INSTALLED&quot; |
| BLOCKED | &quot;BLOCKED&quot; |



## Enum: RuntimePermissionEnum

| Name | Value |
|---- | -----|
| PROMPT | &quot;PROMPT&quot; |
| GRANT | &quot;GRANT&quot; |
| DENY | &quot;DENY&quot; |



## Enum: TypeEnum

| Name | Value |
|---- | -----|
| APP_TYPE_UNSPECIFIED | &quot;APP_TYPE_UNSPECIFIED&quot; |
| PUBLIC | &quot;PUBLIC&quot; |
| PRIVATE | &quot;PRIVATE&quot; |
| WEBAPP | &quot;WEBAPP&quot; |



