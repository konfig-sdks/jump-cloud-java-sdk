

# SoftwareAppSettings


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**description** | **String** | The software app description. |  [optional] |
|**allowUpdateDelay** | **Boolean** |  |  [optional] |
|**appleVpp** | [**SoftwareAppAppleVpp**](SoftwareAppAppleVpp.md) |  |  [optional] |
|**assetKind** | **String** | The manifest asset kind (ex: software). |  [optional] |
|**assetSha256Size** | **Integer** | The incremental size to use for summing the package as it is downloaded. |  [optional] |
|**assetSha256Strings** | **List&lt;String&gt;** | The array of checksums, one each for the hash size up to the total size of the package. |  [optional] |
|**autoUpdate** | **Boolean** |  |  [optional] |
|**commandLineArguments** | **String** | Command line arguments to use with the application. |  [optional] |
|**desiredState** | **String** | State of Install or Uninstall |  [optional] |
|**enterpriseObjectId** | **String** | ID of the Enterprise with which this app is associated |  [optional] |
|**googleAndroid** | [**SoftwareAppGoogleAndroid**](SoftwareAppGoogleAndroid.md) |  |  [optional] |
|**location** | **String** | Repository where the app is located within the package manager |  [optional] |
|**locationObjectId** | **String** | ID of the repository where the app is located within the package manager |  [optional] |
|**microsoftStore** | [**SoftwareAppMicrosoftStore**](SoftwareAppMicrosoftStore.md) |  |  [optional] |
|**packageId** | **String** |  |  [optional] |
|**packageKind** | **String** | The package manifest kind (ex: software-package). |  [optional] |
|**packageManager** | **String** | App store serving the app: APPLE_VPP, CHOCOLATEY, etc. |  [optional] |
|**packageSubtitle** | **String** | The package manifest subtitle. |  [optional] |
|**packageVersion** | **String** | The package manifest version. |  [optional] |
|**storedPackage** | [**ObjectStorageItem**](ObjectStorageItem.md) |  |  [optional] |
|**storedPackageObjectId** | **String** | ID of the stored package this app uses to reference the stored install media. |  [optional] |



