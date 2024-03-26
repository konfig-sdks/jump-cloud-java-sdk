/*
 * JumpCloud API
 * # Overview  JumpCloud's V2 API. This set of endpoints allows JumpCloud customers to manage objects, groupings and mappings and interact with the JumpCloud Graph.  ## API Best Practices  Read the linked Help Article below for guidance on retrying failed requests to JumpCloud's REST API, as well as best practices for structuring subsequent retry requests. Customizing retry mechanisms based on these recommendations will increase the reliability and dependability of your API calls.  Covered topics include: 1. Important Considerations 2. Supported HTTP Request Methods 3. Response codes 4. API Key rotation 5. Paginating 6. Error handling 7. Retry rates  [JumpCloud Help Center - API Best Practices](https://support.jumpcloud.com/support/s/article/JumpCloud-API-Best-Practices)  # Directory Objects  This API offers the ability to interact with some of our core features; otherwise known as Directory Objects. The Directory Objects are:  * Commands * Policies * Policy Groups * Applications * Systems * Users * User Groups * System Groups * Radius Servers * Directories: Office 365, LDAP,G-Suite, Active Directory * Duo accounts and applications.  The Directory Object is an important concept to understand in order to successfully use JumpCloud API.  ## JumpCloud Graph  We've also introduced the concept of the JumpCloud Graph along with  Directory Objects. The Graph is a powerful aspect of our platform which will enable you to associate objects with each other, or establish membership for certain objects to become members of other objects.  Specific `GET` endpoints will allow you to traverse the JumpCloud Graph to return all indirect and directly bound objects in your organization.  | ![alt text](https://s3.amazonaws.com/jumpcloud-kb/Knowledge+Base+Photos/API+Docs/jumpcloud_graph.png \"JumpCloud Graph Model Example\") | |:--:| | **This diagram highlights our association and membership model as it relates to Directory Objects.** |  # API Key  ## Access Your API Key  To locate your API Key:  1. Log into the [JumpCloud Admin Console](https://console.jumpcloud.com/). 2. Go to the username drop down located in the top-right of the Console. 3. Retrieve your API key from API Settings.  ## API Key Considerations  This API key is associated to the currently logged in administrator. Other admins will have different API keys.  **WARNING** Please keep this API key secret, as it grants full access to any data accessible via your JumpCloud console account.  You can also reset your API key in the same location in the JumpCloud Admin Console.  ## Recycling or Resetting Your API Key  In order to revoke access with the current API key, simply reset your API key. This will render all calls using the previous API key inaccessible.  Your API key will be passed in as a header with the header name \"x-api-key\".  ```bash curl -H \"x-api-key: [YOUR_API_KEY_HERE]\" \"https://console.jumpcloud.com/api/v2/systemgroups\" ```  # System Context  * [Introduction](https://docs.jumpcloud.com) * [Supported endpoints](https://docs.jumpcloud.com) * [Response codes](https://docs.jumpcloud.com) * [Authentication](https://docs.jumpcloud.com) * [Additional examples](https://docs.jumpcloud.com) * [Third party](https://docs.jumpcloud.com)  ## Introduction  JumpCloud System Context Authorization is an alternative way to authenticate with a subset of JumpCloud's REST APIs. Using this method, a system can manage its information and resource associations, allowing modern auto provisioning environments to scale as needed.  **Notes:**   * The following documentation applies to Linux Operating Systems only.  * Systems that have been automatically enrolled using Apple's Device Enrollment Program (DEP) or systems enrolled using the User Portal install are not eligible to use the System Context API to prevent unauthorized access to system groups and resources. If a script that utilizes the System Context API is invoked on a system enrolled in this way, it will display an error.  ## Supported Endpoints  JumpCloud System Context Authorization can be used in conjunction with Systems endpoints found in the V1 API and certain System Group endpoints found in the v2 API.  * A system may fetch, alter, and delete metadata about itself, including manipulating a system's Group and Systemuser associations,   * `/api/systems/{system_id}` | [`GET`](https://docs.jumpcloud.com/api/1.0/index.html#operation/systems_get) [`PUT`](https://docs.jumpcloud.com/api/1.0/index.html#operation/systems_put) * A system may delete itself from your JumpCloud organization   * `/api/systems/{system_id}` | [`DELETE`](https://docs.jumpcloud.com/api/1.0/index.html#operation/systems_delete) * A system may fetch its direct resource associations under v2 (Groups)   * `/api/v2/systems/{system_id}/memberof` | [`GET`](https://docs.jumpcloud.com/api/2.0/index.html#operation/graph_systemGroupMembership)   * `/api/v2/systems/{system_id}/associations` | [`GET`](https://docs.jumpcloud.com/api/2.0/index.html#operation/graph_systemAssociationsList)   * `/api/v2/systems/{system_id}/users` | [`GET`](https://docs.jumpcloud.com/api/2.0/index.html#operation/graph_systemTraverseUser) * A system may alter its direct resource associations under v2 (Groups)   * `/api/v2/systems/{system_id}/associations` | [`POST`](https://docs.jumpcloud.com/api/2.0/index.html#operation/graph_systemAssociationsPost) * A system may alter its System Group associations   * `/api/v2/systemgroups/{group_id}/members` | [`POST`](https://docs.jumpcloud.com/api/2.0/index.html#operation/graph_systemGroupMembersPost)     * _NOTE_ If a system attempts to alter the system group membership of a different system the request will be rejected  ## Response Codes  If endpoints other than those described above are called using the System Context API, the server will return a `401` response.  ## Authentication  To allow for secure access to our APIs, you must authenticate each API request. JumpCloud System Context Authorization uses [HTTP Signatures](https://tools.ietf.org/html/draft-cavage-http-signatures-00) to authenticate API requests. The HTTP Signatures sent with each request are similar to the signatures used by the Amazon Web Services REST API. To help with the request-signing process, we have provided an [example bash script](https://github.com/TheJumpCloud/SystemContextAPI/blob/master/examples/shell/SigningExample.sh). This example API request simply requests the entire system record. You must be root, or have permissions to access the contents of the `/opt/jc` directory to generate a signature.  Here is a breakdown of the example script with explanations.  First, the script extracts the systemKey from the JSON formatted `/opt/jc/jcagent.conf` file.  ```bash #!/bin/bash conf=\"`cat /opt/jc/jcagent.conf`\" regex=\"systemKey\\\":\\\"(\\w+)\\\"\"  if [[ $conf =~ $regex ]] ; then   systemKey=\"${BASH_REMATCH[1]}\" fi ```  Then, the script retrieves the current date in the correct format.  ```bash now=`date -u \"+%a, %d %h %Y %H:%M:%S GMT\"`; ```  Next, we build a signing string to demonstrate the expected signature format. The signed string must consist of the [request-line](https://tools.ietf.org/html/rfc2616#page-35) and the date header, separated by a newline character.  ```bash signstr=\"GET /api/systems/${systemKey} HTTP/1.1\\ndate: ${now}\" ```  The next step is to calculate and apply the signature. This is a two-step process:  1. Create a signature from the signing string using the JumpCloud Agent private key: ``printf \"$signstr\" | openssl dgst -sha256 -sign /opt/jc/client.key`` 2. Then Base64-encode the signature string and trim off the newline characters: ``| openssl enc -e -a | tr -d '\\n'``  The combined steps above result in:  ```bash signature=`printf \"$signstr\" | openssl dgst -sha256 -sign /opt/jc/client.key | openssl enc -e -a | tr -d '\\n'` ; ```  Finally, we make sure the API call sending the signature has the same Authorization and Date header values, HTTP method, and URL that were used in the signing string.  ```bash curl -iq \\   -H \"Accept: application/json\" \\   -H \"Content-Type: application/json\" \\   -H \"Date: ${now}\" \\   -H \"Authorization: Signature keyId=\\\"system/${systemKey}\\\",headers=\\\"request-line date\\\",algorithm=\\\"rsa-sha256\\\",signature=\\\"${signature}\\\"\" \\   --url https://console.jumpcloud.com/api/systems/${systemKey} ```  ### Input Data  All PUT and POST methods should use the HTTP Content-Type header with a value of 'application/json'. PUT methods are used for updating a record. POST methods are used to create a record.  The following example demonstrates how to update the `displayName` of the system.  ```bash signstr=\"PUT /api/systems/${systemKey} HTTP/1.1\\ndate: ${now}\" signature=`printf \"$signstr\" | openssl dgst -sha256 -sign /opt/jc/client.key | openssl enc -e -a | tr -d '\\n'` ;  curl -iq \\   -d \"{\\\"displayName\\\" : \\\"updated-system-name-1\\\"}\" \\   -X \"PUT\" \\   -H \"Content-Type: application/json\" \\   -H \"Accept: application/json\" \\   -H \"Date: ${now}\" \\   -H \"Authorization: Signature keyId=\\\"system/${systemKey}\\\",headers=\\\"request-line date\\\",algorithm=\\\"rsa-sha256\\\",signature=\\\"${signature}\\\"\" \\   --url https://console.jumpcloud.com/api/systems/${systemKey} ```  ### Output Data  All results will be formatted as JSON.  Here is an abbreviated example of response output:  ```json {   \"_id\": \"625ee96f52e144993e000015\",   \"agentServer\": \"lappy386\",   \"agentVersion\": \"0.9.42\",   \"arch\": \"x86_64\",   \"connectionKey\": \"127.0.0.1_51812\",   \"displayName\": \"ubuntu-1204\",   \"firstContact\": \"2013-10-16T19:30:55.611Z\",   \"hostname\": \"ubuntu-1204\"   ... ```  ## Additional Examples  ### Signing Authentication Example  This example demonstrates how to make an authenticated request to fetch the JumpCloud record for this system.  [SigningExample.sh](https://github.com/TheJumpCloud/SystemContextAPI/blob/master/examples/shell/SigningExample.sh)  ### Shutdown Hook  This example demonstrates how to make an authenticated request on system shutdown. Using an init.d script registered at run level 0, you can call the System Context API as the system is shutting down.  [Instance-shutdown-initd](https://github.com/TheJumpCloud/SystemContextAPI/blob/master/examples/instance-shutdown-initd) is an example of an init.d script that only runs at system shutdown.  After customizing the [instance-shutdown-initd](https://github.com/TheJumpCloud/SystemContextAPI/blob/master/examples/instance-shutdown-initd) script, you should install it on the system(s) running the JumpCloud agent.  1. Copy the modified [instance-shutdown-initd](https://github.com/TheJumpCloud/SystemContextAPI/blob/master/examples/instance-shutdown-initd) to `/etc/init.d/instance-shutdown`. 2. On Ubuntu systems, run `update-rc.d instance-shutdown defaults`. On RedHat/CentOS systems, run `chkconfig --add instance-shutdown`.  ## Third Party  ### Chef Cookbooks  [https://github.com/nshenry03/jumpcloud](https://github.com/nshenry03/jumpcloud)  [https://github.com/cjs226/jumpcloud](https://github.com/cjs226/jumpcloud)  # Multi-Tenant Portal Headers  Multi-Tenant Organization API Headers are available for JumpCloud Admins to use when making API requests from Organizations that have multiple managed organizations.  The `x-org-id` is a required header for all multi-tenant admins when making API requests to JumpCloud. This header will define to which organization you would like to make the request.  **NOTE** Single Tenant Admins do not need to provide this header when making an API request.  ## Header Value  `x-org-id`  ## API Response Codes  * `400` Malformed ID. * `400` x-org-id and Organization path ID do not match. * `401` ID not included for multi-tenant admin * `403` ID included on unsupported route. * `404` Organization ID Not Found.  ```bash curl -X GET https://console.jumpcloud.com/api/v2/directories \\   -H 'accept: application/json' \\   -H 'content-type: application/json' \\   -H 'x-api-key: {API_KEY}' \\   -H 'x-org-id: {ORG_ID}'  ```  ## To Obtain an Individual Organization ID via the UI  As a prerequisite, your Primary Organization will need to be setup for Multi-Tenancy. This provides access to the Multi-Tenant Organization Admin Portal.  1. Log into JumpCloud [Admin Console](https://console.jumpcloud.com). If you are a multi-tenant Admin, you will automatically be routed to the Multi-Tenant Admin Portal. 2. From the Multi-Tenant Portal's primary navigation bar, select the Organization you'd like to access. 3. You will automatically be routed to that Organization's Admin Console. 4. Go to Settings in the sub-tenant's primary navigation. 5. You can obtain your Organization ID below your Organization's Contact Information on the Settings page.  ## To Obtain All Organization IDs via the API  * You can make an API request to this endpoint using the API key of your Primary Organization.  `https://console.jumpcloud.com/api/organizations/` This will return all your managed organizations.  ```bash curl -X GET \\   https://console.jumpcloud.com/api/organizations/ \\   -H 'Accept: application/json' \\   -H 'Content-Type: application/json' \\   -H 'x-api-key: {API_KEY}' ```  # SDKs  You can find language specific SDKs that can help you kickstart your Integration with JumpCloud in the following GitHub repositories:  * [Python](https://github.com/TheJumpCloud/jcapi-python) * [Go](https://github.com/TheJumpCloud/jcapi-go) * [Ruby](https://github.com/TheJumpCloud/jcapi-ruby) * [Java](https://github.com/TheJumpCloud/jcapi-java) 
 *
 * The version of the OpenAPI document: 2.0
 * Contact: support@jumpcloud.com
 *
 * NOTE: This class is auto generated by Konfig (https://konfigthis.com).
 * Do not edit the class manually.
 */


package com.konfigthis.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.konfigthis.client.JSON;

/**
 * SystemInsightsApps
 */@javax.annotation.Generated(value = "Generated by https://konfigthis.com")
public class SystemInsightsApps {
  public static final String SERIALIZED_NAME_APPLESCRIPT_ENABLED = "applescript_enabled";
  @SerializedName(SERIALIZED_NAME_APPLESCRIPT_ENABLED)
  private String applescriptEnabled;

  public static final String SERIALIZED_NAME_BUNDLE_EXECUTABLE = "bundle_executable";
  @SerializedName(SERIALIZED_NAME_BUNDLE_EXECUTABLE)
  private String bundleExecutable;

  public static final String SERIALIZED_NAME_BUNDLE_IDENTIFIER = "bundle_identifier";
  @SerializedName(SERIALIZED_NAME_BUNDLE_IDENTIFIER)
  private String bundleIdentifier;

  public static final String SERIALIZED_NAME_BUNDLE_NAME = "bundle_name";
  @SerializedName(SERIALIZED_NAME_BUNDLE_NAME)
  private String bundleName;

  public static final String SERIALIZED_NAME_BUNDLE_PACKAGE_TYPE = "bundle_package_type";
  @SerializedName(SERIALIZED_NAME_BUNDLE_PACKAGE_TYPE)
  private String bundlePackageType;

  public static final String SERIALIZED_NAME_BUNDLE_SHORT_VERSION = "bundle_short_version";
  @SerializedName(SERIALIZED_NAME_BUNDLE_SHORT_VERSION)
  private String bundleShortVersion;

  public static final String SERIALIZED_NAME_BUNDLE_VERSION = "bundle_version";
  @SerializedName(SERIALIZED_NAME_BUNDLE_VERSION)
  private String bundleVersion;

  public static final String SERIALIZED_NAME_CATEGORY = "category";
  @SerializedName(SERIALIZED_NAME_CATEGORY)
  private String category;

  public static final String SERIALIZED_NAME_COLLECTION_TIME = "collection_time";
  @SerializedName(SERIALIZED_NAME_COLLECTION_TIME)
  private String collectionTime;

  public static final String SERIALIZED_NAME_COMPILER = "compiler";
  @SerializedName(SERIALIZED_NAME_COMPILER)
  private String compiler;

  public static final String SERIALIZED_NAME_COPYRIGHT = "copyright";
  @SerializedName(SERIALIZED_NAME_COPYRIGHT)
  private String copyright;

  public static final String SERIALIZED_NAME_DEVELOPMENT_REGION = "development_region";
  @SerializedName(SERIALIZED_NAME_DEVELOPMENT_REGION)
  private String developmentRegion;

  public static final String SERIALIZED_NAME_DISPLAY_NAME = "display_name";
  @SerializedName(SERIALIZED_NAME_DISPLAY_NAME)
  private String displayName;

  public static final String SERIALIZED_NAME_ELEMENT = "element";
  @SerializedName(SERIALIZED_NAME_ELEMENT)
  private String element;

  public static final String SERIALIZED_NAME_ENVIRONMENT = "environment";
  @SerializedName(SERIALIZED_NAME_ENVIRONMENT)
  private String environment;

  public static final String SERIALIZED_NAME_INFO_STRING = "info_string";
  @SerializedName(SERIALIZED_NAME_INFO_STRING)
  private String infoString;

  public static final String SERIALIZED_NAME_LAST_OPENED_TIME = "last_opened_time";
  @SerializedName(SERIALIZED_NAME_LAST_OPENED_TIME)
  private Double lastOpenedTime;

  public static final String SERIALIZED_NAME_MINIMUM_SYSTEM_VERSION = "minimum_system_version";
  @SerializedName(SERIALIZED_NAME_MINIMUM_SYSTEM_VERSION)
  private String minimumSystemVersion;

  public static final String SERIALIZED_NAME_NAME = "name";
  @SerializedName(SERIALIZED_NAME_NAME)
  private String name;

  public static final String SERIALIZED_NAME_PATH = "path";
  @SerializedName(SERIALIZED_NAME_PATH)
  private String path;

  public static final String SERIALIZED_NAME_SYSTEM_ID = "system_id";
  @SerializedName(SERIALIZED_NAME_SYSTEM_ID)
  private String systemId;

  public SystemInsightsApps() {
  }

  public SystemInsightsApps applescriptEnabled(String applescriptEnabled) {
    
    
    
    
    this.applescriptEnabled = applescriptEnabled;
    return this;
  }

   /**
   * Get applescriptEnabled
   * @return applescriptEnabled
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "1", value = "")

  public String getApplescriptEnabled() {
    return applescriptEnabled;
  }


  public void setApplescriptEnabled(String applescriptEnabled) {
    
    
    
    this.applescriptEnabled = applescriptEnabled;
  }


  public SystemInsightsApps bundleExecutable(String bundleExecutable) {
    
    
    
    
    this.bundleExecutable = bundleExecutable;
    return this;
  }

   /**
   * Get bundleExecutable
   * @return bundleExecutable
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "App Store", value = "")

  public String getBundleExecutable() {
    return bundleExecutable;
  }


  public void setBundleExecutable(String bundleExecutable) {
    
    
    
    this.bundleExecutable = bundleExecutable;
  }


  public SystemInsightsApps bundleIdentifier(String bundleIdentifier) {
    
    
    
    
    this.bundleIdentifier = bundleIdentifier;
    return this;
  }

   /**
   * Get bundleIdentifier
   * @return bundleIdentifier
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "com.apple.appstore", value = "")

  public String getBundleIdentifier() {
    return bundleIdentifier;
  }


  public void setBundleIdentifier(String bundleIdentifier) {
    
    
    
    this.bundleIdentifier = bundleIdentifier;
  }


  public SystemInsightsApps bundleName(String bundleName) {
    
    
    
    
    this.bundleName = bundleName;
    return this;
  }

   /**
   * Get bundleName
   * @return bundleName
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "App Store", value = "")

  public String getBundleName() {
    return bundleName;
  }


  public void setBundleName(String bundleName) {
    
    
    
    this.bundleName = bundleName;
  }


  public SystemInsightsApps bundlePackageType(String bundlePackageType) {
    
    
    
    
    this.bundlePackageType = bundlePackageType;
    return this;
  }

   /**
   * Get bundlePackageType
   * @return bundlePackageType
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "APPL", value = "")

  public String getBundlePackageType() {
    return bundlePackageType;
  }


  public void setBundlePackageType(String bundlePackageType) {
    
    
    
    this.bundlePackageType = bundlePackageType;
  }


  public SystemInsightsApps bundleShortVersion(String bundleShortVersion) {
    
    
    
    
    this.bundleShortVersion = bundleShortVersion;
    return this;
  }

   /**
   * Get bundleShortVersion
   * @return bundleShortVersion
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "2.4", value = "")

  public String getBundleShortVersion() {
    return bundleShortVersion;
  }


  public void setBundleShortVersion(String bundleShortVersion) {
    
    
    
    this.bundleShortVersion = bundleShortVersion;
  }


  public SystemInsightsApps bundleVersion(String bundleVersion) {
    
    
    
    
    this.bundleVersion = bundleVersion;
    return this;
  }

   /**
   * Get bundleVersion
   * @return bundleVersion
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "658.1", value = "")

  public String getBundleVersion() {
    return bundleVersion;
  }


  public void setBundleVersion(String bundleVersion) {
    
    
    
    this.bundleVersion = bundleVersion;
  }


  public SystemInsightsApps category(String category) {
    
    
    
    
    this.category = category;
    return this;
  }

   /**
   * Get category
   * @return category
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "public.app-category.utilities", value = "")

  public String getCategory() {
    return category;
  }


  public void setCategory(String category) {
    
    
    
    this.category = category;
  }


  public SystemInsightsApps collectionTime(String collectionTime) {
    
    
    
    
    this.collectionTime = collectionTime;
    return this;
  }

   /**
   * Get collectionTime
   * @return collectionTime
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "2019-06-03T19:41:30.658Z", value = "")

  public String getCollectionTime() {
    return collectionTime;
  }


  public void setCollectionTime(String collectionTime) {
    
    
    
    this.collectionTime = collectionTime;
  }


  public SystemInsightsApps compiler(String compiler) {
    
    
    
    
    this.compiler = compiler;
    return this;
  }

   /**
   * Get compiler
   * @return compiler
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "com.apple.compilers.llvm.clang.1_0", value = "")

  public String getCompiler() {
    return compiler;
  }


  public void setCompiler(String compiler) {
    
    
    
    this.compiler = compiler;
  }


  public SystemInsightsApps copyright(String copyright) {
    
    
    
    
    this.copyright = copyright;
    return this;
  }

   /**
   * Get copyright
   * @return copyright
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "", value = "")

  public String getCopyright() {
    return copyright;
  }


  public void setCopyright(String copyright) {
    
    
    
    this.copyright = copyright;
  }


  public SystemInsightsApps developmentRegion(String developmentRegion) {
    
    
    
    
    this.developmentRegion = developmentRegion;
    return this;
  }

   /**
   * Get developmentRegion
   * @return developmentRegion
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "English", value = "")

  public String getDevelopmentRegion() {
    return developmentRegion;
  }


  public void setDevelopmentRegion(String developmentRegion) {
    
    
    
    this.developmentRegion = developmentRegion;
  }


  public SystemInsightsApps displayName(String displayName) {
    
    
    
    
    this.displayName = displayName;
    return this;
  }

   /**
   * Get displayName
   * @return displayName
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "App Store", value = "")

  public String getDisplayName() {
    return displayName;
  }


  public void setDisplayName(String displayName) {
    
    
    
    this.displayName = displayName;
  }


  public SystemInsightsApps element(String element) {
    
    
    
    
    this.element = element;
    return this;
  }

   /**
   * Get element
   * @return element
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "", value = "")

  public String getElement() {
    return element;
  }


  public void setElement(String element) {
    
    
    
    this.element = element;
  }


  public SystemInsightsApps environment(String environment) {
    
    
    
    
    this.environment = environment;
    return this;
  }

   /**
   * Get environment
   * @return environment
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "", value = "")

  public String getEnvironment() {
    return environment;
  }


  public void setEnvironment(String environment) {
    
    
    
    this.environment = environment;
  }


  public SystemInsightsApps infoString(String infoString) {
    
    
    
    
    this.infoString = infoString;
    return this;
  }

   /**
   * Get infoString
   * @return infoString
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "", value = "")

  public String getInfoString() {
    return infoString;
  }


  public void setInfoString(String infoString) {
    
    
    
    this.infoString = infoString;
  }


  public SystemInsightsApps lastOpenedTime(Double lastOpenedTime) {
    
    
    
    
    this.lastOpenedTime = lastOpenedTime;
    return this;
  }

  public SystemInsightsApps lastOpenedTime(Integer lastOpenedTime) {
    
    
    
    
    this.lastOpenedTime = lastOpenedTime.doubleValue();
    return this;
  }

   /**
   * Get lastOpenedTime
   * @return lastOpenedTime
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "1556688963.50383", value = "")

  public Double getLastOpenedTime() {
    return lastOpenedTime;
  }


  public void setLastOpenedTime(Double lastOpenedTime) {
    
    
    
    this.lastOpenedTime = lastOpenedTime;
  }


  public SystemInsightsApps minimumSystemVersion(String minimumSystemVersion) {
    
    
    
    
    this.minimumSystemVersion = minimumSystemVersion;
    return this;
  }

   /**
   * Get minimumSystemVersion
   * @return minimumSystemVersion
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "10.13", value = "")

  public String getMinimumSystemVersion() {
    return minimumSystemVersion;
  }


  public void setMinimumSystemVersion(String minimumSystemVersion) {
    
    
    
    this.minimumSystemVersion = minimumSystemVersion;
  }


  public SystemInsightsApps name(String name) {
    
    
    
    
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "App Store.app", value = "")

  public String getName() {
    return name;
  }


  public void setName(String name) {
    
    
    
    this.name = name;
  }


  public SystemInsightsApps path(String path) {
    
    
    
    
    this.path = path;
    return this;
  }

   /**
   * Get path
   * @return path
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "/Applications/App Store.app", value = "")

  public String getPath() {
    return path;
  }


  public void setPath(String path) {
    
    
    
    this.path = path;
  }


  public SystemInsightsApps systemId(String systemId) {
    
    
    
    
    this.systemId = systemId;
    return this;
  }

   /**
   * Get systemId
   * @return systemId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "5c9e51a13c5146f89bae12d9", value = "")

  public String getSystemId() {
    return systemId;
  }


  public void setSystemId(String systemId) {
    
    
    
    this.systemId = systemId;
  }

  /**
   * A container for additional, undeclared properties.
   * This is a holder for any undeclared properties as specified with
   * the 'additionalProperties' keyword in the OAS document.
   */
  private Map<String, Object> additionalProperties;

  /**
   * Set the additional (undeclared) property with the specified name and value.
   * If the property does not already exist, create it otherwise replace it.
   *
   * @param key name of the property
   * @param value value of the property
   * @return the SystemInsightsApps instance itself
   */
  public SystemInsightsApps putAdditionalProperty(String key, Object value) {
    if (this.additionalProperties == null) {
        this.additionalProperties = new HashMap<String, Object>();
    }
    this.additionalProperties.put(key, value);
    return this;
  }

  /**
   * Return the additional (undeclared) property.
   *
   * @return a map of objects
   */
  public Map<String, Object> getAdditionalProperties() {
    return additionalProperties;
  }

  /**
   * Return the additional (undeclared) property with the specified name.
   *
   * @param key name of the property
   * @return an object
   */
  public Object getAdditionalProperty(String key) {
    if (this.additionalProperties == null) {
        return null;
    }
    return this.additionalProperties.get(key);
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SystemInsightsApps systemInsightsApps = (SystemInsightsApps) o;
    return Objects.equals(this.applescriptEnabled, systemInsightsApps.applescriptEnabled) &&
        Objects.equals(this.bundleExecutable, systemInsightsApps.bundleExecutable) &&
        Objects.equals(this.bundleIdentifier, systemInsightsApps.bundleIdentifier) &&
        Objects.equals(this.bundleName, systemInsightsApps.bundleName) &&
        Objects.equals(this.bundlePackageType, systemInsightsApps.bundlePackageType) &&
        Objects.equals(this.bundleShortVersion, systemInsightsApps.bundleShortVersion) &&
        Objects.equals(this.bundleVersion, systemInsightsApps.bundleVersion) &&
        Objects.equals(this.category, systemInsightsApps.category) &&
        Objects.equals(this.collectionTime, systemInsightsApps.collectionTime) &&
        Objects.equals(this.compiler, systemInsightsApps.compiler) &&
        Objects.equals(this.copyright, systemInsightsApps.copyright) &&
        Objects.equals(this.developmentRegion, systemInsightsApps.developmentRegion) &&
        Objects.equals(this.displayName, systemInsightsApps.displayName) &&
        Objects.equals(this.element, systemInsightsApps.element) &&
        Objects.equals(this.environment, systemInsightsApps.environment) &&
        Objects.equals(this.infoString, systemInsightsApps.infoString) &&
        Objects.equals(this.lastOpenedTime, systemInsightsApps.lastOpenedTime) &&
        Objects.equals(this.minimumSystemVersion, systemInsightsApps.minimumSystemVersion) &&
        Objects.equals(this.name, systemInsightsApps.name) &&
        Objects.equals(this.path, systemInsightsApps.path) &&
        Objects.equals(this.systemId, systemInsightsApps.systemId)&&
        Objects.equals(this.additionalProperties, systemInsightsApps.additionalProperties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(applescriptEnabled, bundleExecutable, bundleIdentifier, bundleName, bundlePackageType, bundleShortVersion, bundleVersion, category, collectionTime, compiler, copyright, developmentRegion, displayName, element, environment, infoString, lastOpenedTime, minimumSystemVersion, name, path, systemId, additionalProperties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SystemInsightsApps {\n");
    sb.append("    applescriptEnabled: ").append(toIndentedString(applescriptEnabled)).append("\n");
    sb.append("    bundleExecutable: ").append(toIndentedString(bundleExecutable)).append("\n");
    sb.append("    bundleIdentifier: ").append(toIndentedString(bundleIdentifier)).append("\n");
    sb.append("    bundleName: ").append(toIndentedString(bundleName)).append("\n");
    sb.append("    bundlePackageType: ").append(toIndentedString(bundlePackageType)).append("\n");
    sb.append("    bundleShortVersion: ").append(toIndentedString(bundleShortVersion)).append("\n");
    sb.append("    bundleVersion: ").append(toIndentedString(bundleVersion)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    collectionTime: ").append(toIndentedString(collectionTime)).append("\n");
    sb.append("    compiler: ").append(toIndentedString(compiler)).append("\n");
    sb.append("    copyright: ").append(toIndentedString(copyright)).append("\n");
    sb.append("    developmentRegion: ").append(toIndentedString(developmentRegion)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    element: ").append(toIndentedString(element)).append("\n");
    sb.append("    environment: ").append(toIndentedString(environment)).append("\n");
    sb.append("    infoString: ").append(toIndentedString(infoString)).append("\n");
    sb.append("    lastOpenedTime: ").append(toIndentedString(lastOpenedTime)).append("\n");
    sb.append("    minimumSystemVersion: ").append(toIndentedString(minimumSystemVersion)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    path: ").append(toIndentedString(path)).append("\n");
    sb.append("    systemId: ").append(toIndentedString(systemId)).append("\n");
    sb.append("    additionalProperties: ").append(toIndentedString(additionalProperties)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }


  public static HashSet<String> openapiFields;
  public static HashSet<String> openapiRequiredFields;

  static {
    // a set of all properties/fields (JSON key names)
    openapiFields = new HashSet<String>();
    openapiFields.add("applescript_enabled");
    openapiFields.add("bundle_executable");
    openapiFields.add("bundle_identifier");
    openapiFields.add("bundle_name");
    openapiFields.add("bundle_package_type");
    openapiFields.add("bundle_short_version");
    openapiFields.add("bundle_version");
    openapiFields.add("category");
    openapiFields.add("collection_time");
    openapiFields.add("compiler");
    openapiFields.add("copyright");
    openapiFields.add("development_region");
    openapiFields.add("display_name");
    openapiFields.add("element");
    openapiFields.add("environment");
    openapiFields.add("info_string");
    openapiFields.add("last_opened_time");
    openapiFields.add("minimum_system_version");
    openapiFields.add("name");
    openapiFields.add("path");
    openapiFields.add("system_id");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

 /**
  * Validates the JSON Object and throws an exception if issues found
  *
  * @param jsonObj JSON Object
  * @throws IOException if the JSON Object is invalid with respect to SystemInsightsApps
  */
  public static void validateJsonObject(JsonObject jsonObj) throws IOException {
      if (jsonObj == null) {
        if (!SystemInsightsApps.openapiRequiredFields.isEmpty()) { // has required fields but JSON object is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in SystemInsightsApps is not found in the empty JSON string", SystemInsightsApps.openapiRequiredFields.toString()));
        }
      }
      if ((jsonObj.get("applescript_enabled") != null && !jsonObj.get("applescript_enabled").isJsonNull()) && !jsonObj.get("applescript_enabled").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `applescript_enabled` to be a primitive type in the JSON string but got `%s`", jsonObj.get("applescript_enabled").toString()));
      }
      if ((jsonObj.get("bundle_executable") != null && !jsonObj.get("bundle_executable").isJsonNull()) && !jsonObj.get("bundle_executable").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `bundle_executable` to be a primitive type in the JSON string but got `%s`", jsonObj.get("bundle_executable").toString()));
      }
      if ((jsonObj.get("bundle_identifier") != null && !jsonObj.get("bundle_identifier").isJsonNull()) && !jsonObj.get("bundle_identifier").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `bundle_identifier` to be a primitive type in the JSON string but got `%s`", jsonObj.get("bundle_identifier").toString()));
      }
      if ((jsonObj.get("bundle_name") != null && !jsonObj.get("bundle_name").isJsonNull()) && !jsonObj.get("bundle_name").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `bundle_name` to be a primitive type in the JSON string but got `%s`", jsonObj.get("bundle_name").toString()));
      }
      if ((jsonObj.get("bundle_package_type") != null && !jsonObj.get("bundle_package_type").isJsonNull()) && !jsonObj.get("bundle_package_type").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `bundle_package_type` to be a primitive type in the JSON string but got `%s`", jsonObj.get("bundle_package_type").toString()));
      }
      if ((jsonObj.get("bundle_short_version") != null && !jsonObj.get("bundle_short_version").isJsonNull()) && !jsonObj.get("bundle_short_version").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `bundle_short_version` to be a primitive type in the JSON string but got `%s`", jsonObj.get("bundle_short_version").toString()));
      }
      if ((jsonObj.get("bundle_version") != null && !jsonObj.get("bundle_version").isJsonNull()) && !jsonObj.get("bundle_version").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `bundle_version` to be a primitive type in the JSON string but got `%s`", jsonObj.get("bundle_version").toString()));
      }
      if ((jsonObj.get("category") != null && !jsonObj.get("category").isJsonNull()) && !jsonObj.get("category").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `category` to be a primitive type in the JSON string but got `%s`", jsonObj.get("category").toString()));
      }
      if ((jsonObj.get("collection_time") != null && !jsonObj.get("collection_time").isJsonNull()) && !jsonObj.get("collection_time").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `collection_time` to be a primitive type in the JSON string but got `%s`", jsonObj.get("collection_time").toString()));
      }
      if ((jsonObj.get("compiler") != null && !jsonObj.get("compiler").isJsonNull()) && !jsonObj.get("compiler").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `compiler` to be a primitive type in the JSON string but got `%s`", jsonObj.get("compiler").toString()));
      }
      if ((jsonObj.get("copyright") != null && !jsonObj.get("copyright").isJsonNull()) && !jsonObj.get("copyright").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `copyright` to be a primitive type in the JSON string but got `%s`", jsonObj.get("copyright").toString()));
      }
      if ((jsonObj.get("development_region") != null && !jsonObj.get("development_region").isJsonNull()) && !jsonObj.get("development_region").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `development_region` to be a primitive type in the JSON string but got `%s`", jsonObj.get("development_region").toString()));
      }
      if ((jsonObj.get("display_name") != null && !jsonObj.get("display_name").isJsonNull()) && !jsonObj.get("display_name").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `display_name` to be a primitive type in the JSON string but got `%s`", jsonObj.get("display_name").toString()));
      }
      if ((jsonObj.get("element") != null && !jsonObj.get("element").isJsonNull()) && !jsonObj.get("element").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `element` to be a primitive type in the JSON string but got `%s`", jsonObj.get("element").toString()));
      }
      if ((jsonObj.get("environment") != null && !jsonObj.get("environment").isJsonNull()) && !jsonObj.get("environment").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `environment` to be a primitive type in the JSON string but got `%s`", jsonObj.get("environment").toString()));
      }
      if ((jsonObj.get("info_string") != null && !jsonObj.get("info_string").isJsonNull()) && !jsonObj.get("info_string").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `info_string` to be a primitive type in the JSON string but got `%s`", jsonObj.get("info_string").toString()));
      }
      if ((jsonObj.get("minimum_system_version") != null && !jsonObj.get("minimum_system_version").isJsonNull()) && !jsonObj.get("minimum_system_version").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `minimum_system_version` to be a primitive type in the JSON string but got `%s`", jsonObj.get("minimum_system_version").toString()));
      }
      if ((jsonObj.get("name") != null && !jsonObj.get("name").isJsonNull()) && !jsonObj.get("name").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `name` to be a primitive type in the JSON string but got `%s`", jsonObj.get("name").toString()));
      }
      if ((jsonObj.get("path") != null && !jsonObj.get("path").isJsonNull()) && !jsonObj.get("path").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `path` to be a primitive type in the JSON string but got `%s`", jsonObj.get("path").toString()));
      }
      if ((jsonObj.get("system_id") != null && !jsonObj.get("system_id").isJsonNull()) && !jsonObj.get("system_id").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `system_id` to be a primitive type in the JSON string but got `%s`", jsonObj.get("system_id").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!SystemInsightsApps.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'SystemInsightsApps' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<SystemInsightsApps> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(SystemInsightsApps.class));

       return (TypeAdapter<T>) new TypeAdapter<SystemInsightsApps>() {
           @Override
           public void write(JsonWriter out, SystemInsightsApps value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             obj.remove("additionalProperties");
             // serialize additonal properties
             if (value.getAdditionalProperties() != null) {
               for (Map.Entry<String, Object> entry : value.getAdditionalProperties().entrySet()) {
                 if (entry.getValue() instanceof String)
                   obj.addProperty(entry.getKey(), (String) entry.getValue());
                 else if (entry.getValue() instanceof Number)
                   obj.addProperty(entry.getKey(), (Number) entry.getValue());
                 else if (entry.getValue() instanceof Boolean)
                   obj.addProperty(entry.getKey(), (Boolean) entry.getValue());
                 else if (entry.getValue() instanceof Character)
                   obj.addProperty(entry.getKey(), (Character) entry.getValue());
                 else {
                   obj.add(entry.getKey(), gson.toJsonTree(entry.getValue()).getAsJsonObject());
                 }
               }
             }
             elementAdapter.write(out, obj);
           }

           @Override
           public SystemInsightsApps read(JsonReader in) throws IOException {
             JsonObject jsonObj = elementAdapter.read(in).getAsJsonObject();
             validateJsonObject(jsonObj);
             // store additional fields in the deserialized instance
             SystemInsightsApps instance = thisAdapter.fromJsonTree(jsonObj);
             for (Map.Entry<String, JsonElement> entry : jsonObj.entrySet()) {
               if (!openapiFields.contains(entry.getKey())) {
                 if (entry.getValue().isJsonPrimitive()) { // primitive type
                   if (entry.getValue().getAsJsonPrimitive().isString())
                     instance.putAdditionalProperty(entry.getKey(), entry.getValue().getAsString());
                   else if (entry.getValue().getAsJsonPrimitive().isNumber())
                     instance.putAdditionalProperty(entry.getKey(), entry.getValue().getAsNumber());
                   else if (entry.getValue().getAsJsonPrimitive().isBoolean())
                     instance.putAdditionalProperty(entry.getKey(), entry.getValue().getAsBoolean());
                   else
                     throw new IllegalArgumentException(String.format("The field `%s` has unknown primitive type. Value: %s", entry.getKey(), entry.getValue().toString()));
                 } else if (entry.getValue().isJsonArray()) {
                     instance.putAdditionalProperty(entry.getKey(), gson.fromJson(entry.getValue(), List.class));
                 } else { // JSON object
                     instance.putAdditionalProperty(entry.getKey(), gson.fromJson(entry.getValue(), HashMap.class));
                 }
               }
             }
             return instance;
           }

       }.nullSafe();
    }
  }

 /**
  * Create an instance of SystemInsightsApps given an JSON string
  *
  * @param jsonString JSON string
  * @return An instance of SystemInsightsApps
  * @throws IOException if the JSON string is invalid with respect to SystemInsightsApps
  */
  public static SystemInsightsApps fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, SystemInsightsApps.class);
  }

 /**
  * Convert an instance of SystemInsightsApps to an JSON string
  *
  * @return JSON string
  */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

