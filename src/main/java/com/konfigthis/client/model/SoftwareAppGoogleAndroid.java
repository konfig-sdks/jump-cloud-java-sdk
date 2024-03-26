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
import com.konfigthis.client.model.SoftwareAppPermissionGrants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
 * googleAndroid is an optional attribute, it will only be present on apps with a &#39;setting&#39; &#39;package_manager&#39; type of &#39;GOOGLE_ANDROID&#39;.
 */
@ApiModel(description = "googleAndroid is an optional attribute, it will only be present on apps with a 'setting' 'package_manager' type of 'GOOGLE_ANDROID'.")@javax.annotation.Generated(value = "Generated by https://konfigthis.com")
public class SoftwareAppGoogleAndroid {
  public static final String SERIALIZED_NAME_ANDROID_FEATURES = "androidFeatures";
  @SerializedName(SERIALIZED_NAME_ANDROID_FEATURES)
  private List<String> androidFeatures = null;

  public static final String SERIALIZED_NAME_APP_PRICING = "appPricing";
  @SerializedName(SERIALIZED_NAME_APP_PRICING)
  private String appPricing;

  public static final String SERIALIZED_NAME_APP_VERSION = "appVersion";
  @SerializedName(SERIALIZED_NAME_APP_VERSION)
  private String appVersion;

  public static final String SERIALIZED_NAME_AUTHOR = "author";
  @SerializedName(SERIALIZED_NAME_AUTHOR)
  private String author;

  /**
   * Controls the auto-update mode for the app.
   */
  @JsonAdapter(AutoUpdateModeEnum.Adapter.class)
 public enum AutoUpdateModeEnum {
    DEFAULT("AUTO_UPDATE_DEFAULT"),
    
    POSTPONED("AUTO_UPDATE_POSTPONED"),
    
    HIGH_PRIORITY("AUTO_UPDATE_HIGH_PRIORITY");

    private String value;

    AutoUpdateModeEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static AutoUpdateModeEnum fromValue(String value) {
      for (AutoUpdateModeEnum b : AutoUpdateModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static class Adapter extends TypeAdapter<AutoUpdateModeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final AutoUpdateModeEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public AutoUpdateModeEnum read(final JsonReader jsonReader) throws IOException {
        String value =  jsonReader.nextString();
        return AutoUpdateModeEnum.fromValue(value);
      }
    }
  }

  public static final String SERIALIZED_NAME_AUTO_UPDATE_MODE = "autoUpdateMode";
  @SerializedName(SERIALIZED_NAME_AUTO_UPDATE_MODE)
  private AutoUpdateModeEnum autoUpdateMode;

  public static final String SERIALIZED_NAME_CATEGORY = "category";
  @SerializedName(SERIALIZED_NAME_CATEGORY)
  private String category;

  public static final String SERIALIZED_NAME_CONTENT_RATING = "contentRating";
  @SerializedName(SERIALIZED_NAME_CONTENT_RATING)
  private String contentRating;

  public static final String SERIALIZED_NAME_DISPLAY_MODE = "displayMode";
  @SerializedName(SERIALIZED_NAME_DISPLAY_MODE)
  private String displayMode;

  public static final String SERIALIZED_NAME_DISTRIBUTION_CHANNEL = "distributionChannel";
  @SerializedName(SERIALIZED_NAME_DISTRIBUTION_CHANNEL)
  private String distributionChannel;

  public static final String SERIALIZED_NAME_FULL_DESCRIPTION = "fullDescription";
  @SerializedName(SERIALIZED_NAME_FULL_DESCRIPTION)
  private String fullDescription;

  public static final String SERIALIZED_NAME_ICON_URL = "iconUrl";
  @SerializedName(SERIALIZED_NAME_ICON_URL)
  private String iconUrl;

  /**
   * The type of installation to perform for an app.
   */
  @JsonAdapter(InstallTypeEnum.Adapter.class)
 public enum InstallTypeEnum {
    AVAILABLE("AVAILABLE"),
    
    FORCE_INSTALLED("FORCE_INSTALLED"),
    
    BLOCKED("BLOCKED");

    private String value;

    InstallTypeEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static InstallTypeEnum fromValue(String value) {
      for (InstallTypeEnum b : InstallTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static class Adapter extends TypeAdapter<InstallTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final InstallTypeEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public InstallTypeEnum read(final JsonReader jsonReader) throws IOException {
        String value =  jsonReader.nextString();
        return InstallTypeEnum.fromValue(value);
      }
    }
  }

  public static final String SERIALIZED_NAME_INSTALL_TYPE = "installType";
  @SerializedName(SERIALIZED_NAME_INSTALL_TYPE)
  private InstallTypeEnum installType;

  public static final String SERIALIZED_NAME_MANAGED_CONFIGURATION_TEMPLATE_ID = "managedConfigurationTemplateId";
  @SerializedName(SERIALIZED_NAME_MANAGED_CONFIGURATION_TEMPLATE_ID)
  private String managedConfigurationTemplateId;

  public static final String SERIALIZED_NAME_MANAGED_PROPERTIES = "managedProperties";
  @SerializedName(SERIALIZED_NAME_MANAGED_PROPERTIES)
  private Boolean managedProperties;

  public static final String SERIALIZED_NAME_MIN_SDK_VERSION = "minSdkVersion";
  @SerializedName(SERIALIZED_NAME_MIN_SDK_VERSION)
  private Integer minSdkVersion;

  public static final String SERIALIZED_NAME_NAME = "name";
  @SerializedName(SERIALIZED_NAME_NAME)
  private String name;

  public static final String SERIALIZED_NAME_PERMISSION_GRANTS = "permissionGrants";
  @SerializedName(SERIALIZED_NAME_PERMISSION_GRANTS)
  private List<SoftwareAppPermissionGrants> permissionGrants = null;

  /**
   * The policy for granting permission requests to apps.
   */
  @JsonAdapter(RuntimePermissionEnum.Adapter.class)
 public enum RuntimePermissionEnum {
    PROMPT("PROMPT"),
    
    GRANT("GRANT"),
    
    DENY("DENY");

    private String value;

    RuntimePermissionEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static RuntimePermissionEnum fromValue(String value) {
      for (RuntimePermissionEnum b : RuntimePermissionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static class Adapter extends TypeAdapter<RuntimePermissionEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final RuntimePermissionEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public RuntimePermissionEnum read(final JsonReader jsonReader) throws IOException {
        String value =  jsonReader.nextString();
        return RuntimePermissionEnum.fromValue(value);
      }
    }
  }

  public static final String SERIALIZED_NAME_RUNTIME_PERMISSION = "runtimePermission";
  @SerializedName(SERIALIZED_NAME_RUNTIME_PERMISSION)
  private RuntimePermissionEnum runtimePermission;

  public static final String SERIALIZED_NAME_START_URL = "startUrl";
  @SerializedName(SERIALIZED_NAME_START_URL)
  private String startUrl;

  /**
   * Type of this android application.
   */
  @JsonAdapter(TypeEnum.Adapter.class)
 public enum TypeEnum {
    APP_TYPE_UNSPECIFIED("APP_TYPE_UNSPECIFIED"),
    
    PUBLIC("PUBLIC"),
    
    PRIVATE("PRIVATE"),
    
    WEBAPP("WEBAPP");

    private String value;

    TypeEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static class Adapter extends TypeAdapter<TypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final TypeEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public TypeEnum read(final JsonReader jsonReader) throws IOException {
        String value =  jsonReader.nextString();
        return TypeEnum.fromValue(value);
      }
    }
  }

  public static final String SERIALIZED_NAME_TYPE = "type";
  @SerializedName(SERIALIZED_NAME_TYPE)
  private TypeEnum type;

  public static final String SERIALIZED_NAME_UPDATE_TIME = "updateTime";
  @SerializedName(SERIALIZED_NAME_UPDATE_TIME)
  private String updateTime;

  public static final String SERIALIZED_NAME_VERSION_CODE = "versionCode";
  @SerializedName(SERIALIZED_NAME_VERSION_CODE)
  private Integer versionCode;

  public SoftwareAppGoogleAndroid() {
  }

  public SoftwareAppGoogleAndroid androidFeatures(List<String> androidFeatures) {
    
    
    
    
    this.androidFeatures = androidFeatures;
    return this;
  }

  public SoftwareAppGoogleAndroid addAndroidFeaturesItem(String androidFeaturesItem) {
    if (this.androidFeatures == null) {
      this.androidFeatures = new ArrayList<>();
    }
    this.androidFeatures.add(androidFeaturesItem);
    return this;
  }

   /**
   * The array of android features for the app.
   * @return androidFeatures
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The array of android features for the app.")

  public List<String> getAndroidFeatures() {
    return androidFeatures;
  }


  public void setAndroidFeatures(List<String> androidFeatures) {
    
    
    
    this.androidFeatures = androidFeatures;
  }


  public SoftwareAppGoogleAndroid appPricing(String appPricing) {
    
    
    
    
    this.appPricing = appPricing;
    return this;
  }

   /**
   * Whether this app is free, free with in-app purchases, or paid.
   * @return appPricing
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Whether this app is free, free with in-app purchases, or paid.")

  public String getAppPricing() {
    return appPricing;
  }


  public void setAppPricing(String appPricing) {
    
    
    
    this.appPricing = appPricing;
  }


  public SoftwareAppGoogleAndroid appVersion(String appVersion) {
    
    
    
    
    this.appVersion = appVersion;
    return this;
  }

   /**
   * Latest version currently available for this app.
   * @return appVersion
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Latest version currently available for this app.")

  public String getAppVersion() {
    return appVersion;
  }


  public void setAppVersion(String appVersion) {
    
    
    
    this.appVersion = appVersion;
  }


  public SoftwareAppGoogleAndroid author(String author) {
    
    
    
    
    this.author = author;
    return this;
  }

   /**
   * The name of the author of this app.
   * @return author
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The name of the author of this app.")

  public String getAuthor() {
    return author;
  }


  public void setAuthor(String author) {
    
    
    
    this.author = author;
  }


  public SoftwareAppGoogleAndroid autoUpdateMode(AutoUpdateModeEnum autoUpdateMode) {
    
    
    
    
    this.autoUpdateMode = autoUpdateMode;
    return this;
  }

   /**
   * Controls the auto-update mode for the app.
   * @return autoUpdateMode
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Controls the auto-update mode for the app.")

  public AutoUpdateModeEnum getAutoUpdateMode() {
    return autoUpdateMode;
  }


  public void setAutoUpdateMode(AutoUpdateModeEnum autoUpdateMode) {
    
    
    
    this.autoUpdateMode = autoUpdateMode;
  }


  public SoftwareAppGoogleAndroid category(String category) {
    
    
    
    
    this.category = category;
    return this;
  }

   /**
   * The app category (e.g. COMMUNICATION, SOCIAL, etc.).
   * @return category
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The app category (e.g. COMMUNICATION, SOCIAL, etc.).")

  public String getCategory() {
    return category;
  }


  public void setCategory(String category) {
    
    
    
    this.category = category;
  }


  public SoftwareAppGoogleAndroid contentRating(String contentRating) {
    
    
    
    
    this.contentRating = contentRating;
    return this;
  }

   /**
   * The content rating for this app.
   * @return contentRating
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The content rating for this app.")

  public String getContentRating() {
    return contentRating;
  }


  public void setContentRating(String contentRating) {
    
    
    
    this.contentRating = contentRating;
  }


  public SoftwareAppGoogleAndroid displayMode(String displayMode) {
    
    
    
    
    this.displayMode = displayMode;
    return this;
  }

   /**
   * The display mode of the web app.
   * @return displayMode
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The display mode of the web app.")

  public String getDisplayMode() {
    return displayMode;
  }


  public void setDisplayMode(String displayMode) {
    
    
    
    this.displayMode = displayMode;
  }


  public SoftwareAppGoogleAndroid distributionChannel(String distributionChannel) {
    
    
    
    
    this.distributionChannel = distributionChannel;
    return this;
  }

   /**
   * How and to whom the package is made available.
   * @return distributionChannel
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "How and to whom the package is made available.")

  public String getDistributionChannel() {
    return distributionChannel;
  }


  public void setDistributionChannel(String distributionChannel) {
    
    
    
    this.distributionChannel = distributionChannel;
  }


  public SoftwareAppGoogleAndroid fullDescription(String fullDescription) {
    
    
    
    
    this.fullDescription = fullDescription;
    return this;
  }

   /**
   * Full app description, if available.
   * @return fullDescription
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Full app description, if available.")

  public String getFullDescription() {
    return fullDescription;
  }


  public void setFullDescription(String fullDescription) {
    
    
    
    this.fullDescription = fullDescription;
  }


  public SoftwareAppGoogleAndroid iconUrl(String iconUrl) {
    
    
    
    
    this.iconUrl = iconUrl;
    return this;
  }

   /**
   * A link to an image that can be used as an icon for the app.
   * @return iconUrl
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "A link to an image that can be used as an icon for the app.")

  public String getIconUrl() {
    return iconUrl;
  }


  public void setIconUrl(String iconUrl) {
    
    
    
    this.iconUrl = iconUrl;
  }


  public SoftwareAppGoogleAndroid installType(InstallTypeEnum installType) {
    
    
    
    
    this.installType = installType;
    return this;
  }

   /**
   * The type of installation to perform for an app.
   * @return installType
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The type of installation to perform for an app.")

  public InstallTypeEnum getInstallType() {
    return installType;
  }


  public void setInstallType(InstallTypeEnum installType) {
    
    
    
    this.installType = installType;
  }


  public SoftwareAppGoogleAndroid managedConfigurationTemplateId(String managedConfigurationTemplateId) {
    
    
    
    
    this.managedConfigurationTemplateId = managedConfigurationTemplateId;
    return this;
  }

   /**
   * The managed configurations template for the app.
   * @return managedConfigurationTemplateId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The managed configurations template for the app.")

  public String getManagedConfigurationTemplateId() {
    return managedConfigurationTemplateId;
  }


  public void setManagedConfigurationTemplateId(String managedConfigurationTemplateId) {
    
    
    
    this.managedConfigurationTemplateId = managedConfigurationTemplateId;
  }


  public SoftwareAppGoogleAndroid managedProperties(Boolean managedProperties) {
    
    
    
    
    this.managedProperties = managedProperties;
    return this;
  }

   /**
   * Indicates whether this app has managed properties or not.
   * @return managedProperties
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Indicates whether this app has managed properties or not.")

  public Boolean getManagedProperties() {
    return managedProperties;
  }


  public void setManagedProperties(Boolean managedProperties) {
    
    
    
    this.managedProperties = managedProperties;
  }


  public SoftwareAppGoogleAndroid minSdkVersion(Integer minSdkVersion) {
    
    
    
    
    this.minSdkVersion = minSdkVersion;
    return this;
  }

   /**
   * The minimum Android SDK necessary to run the app.
   * @return minSdkVersion
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The minimum Android SDK necessary to run the app.")

  public Integer getMinSdkVersion() {
    return minSdkVersion;
  }


  public void setMinSdkVersion(Integer minSdkVersion) {
    
    
    
    this.minSdkVersion = minSdkVersion;
  }


  public SoftwareAppGoogleAndroid name(String name) {
    
    
    
    
    this.name = name;
    return this;
  }

   /**
   * The name of the app in the form enterprises/{enterprise}/applications/{packageName}.
   * @return name
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The name of the app in the form enterprises/{enterprise}/applications/{packageName}.")

  public String getName() {
    return name;
  }


  public void setName(String name) {
    
    
    
    this.name = name;
  }


  public SoftwareAppGoogleAndroid permissionGrants(List<SoftwareAppPermissionGrants> permissionGrants) {
    
    
    
    
    this.permissionGrants = permissionGrants;
    return this;
  }

  public SoftwareAppGoogleAndroid addPermissionGrantsItem(SoftwareAppPermissionGrants permissionGrantsItem) {
    if (this.permissionGrants == null) {
      this.permissionGrants = new ArrayList<>();
    }
    this.permissionGrants.add(permissionGrantsItem);
    return this;
  }

   /**
   * Get permissionGrants
   * @return permissionGrants
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public List<SoftwareAppPermissionGrants> getPermissionGrants() {
    return permissionGrants;
  }


  public void setPermissionGrants(List<SoftwareAppPermissionGrants> permissionGrants) {
    
    
    
    this.permissionGrants = permissionGrants;
  }


  public SoftwareAppGoogleAndroid runtimePermission(RuntimePermissionEnum runtimePermission) {
    
    
    
    
    this.runtimePermission = runtimePermission;
    return this;
  }

   /**
   * The policy for granting permission requests to apps.
   * @return runtimePermission
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The policy for granting permission requests to apps.")

  public RuntimePermissionEnum getRuntimePermission() {
    return runtimePermission;
  }


  public void setRuntimePermission(RuntimePermissionEnum runtimePermission) {
    
    
    
    this.runtimePermission = runtimePermission;
  }


  public SoftwareAppGoogleAndroid startUrl(String startUrl) {
    
    
    
    
    this.startUrl = startUrl;
    return this;
  }

   /**
   * The start URL, i.e. the URL that should load when the user opens the application. Applicable only for webapps.
   * @return startUrl
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The start URL, i.e. the URL that should load when the user opens the application. Applicable only for webapps.")

  public String getStartUrl() {
    return startUrl;
  }


  public void setStartUrl(String startUrl) {
    
    
    
    this.startUrl = startUrl;
  }


  public SoftwareAppGoogleAndroid type(TypeEnum type) {
    
    
    
    
    this.type = type;
    return this;
  }

   /**
   * Type of this android application.
   * @return type
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Type of this android application.")

  public TypeEnum getType() {
    return type;
  }


  public void setType(TypeEnum type) {
    
    
    
    this.type = type;
  }


  public SoftwareAppGoogleAndroid updateTime(String updateTime) {
    
    
    
    
    this.updateTime = updateTime;
    return this;
  }

   /**
   * The approximate time (within 7 days) the app was last published.
   * @return updateTime
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The approximate time (within 7 days) the app was last published.")

  public String getUpdateTime() {
    return updateTime;
  }


  public void setUpdateTime(String updateTime) {
    
    
    
    this.updateTime = updateTime;
  }


  public SoftwareAppGoogleAndroid versionCode(Integer versionCode) {
    
    
    
    
    this.versionCode = versionCode;
    return this;
  }

   /**
   * The current version of the web app.
   * @return versionCode
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The current version of the web app.")

  public Integer getVersionCode() {
    return versionCode;
  }


  public void setVersionCode(Integer versionCode) {
    
    
    
    this.versionCode = versionCode;
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
   * @return the SoftwareAppGoogleAndroid instance itself
   */
  public SoftwareAppGoogleAndroid putAdditionalProperty(String key, Object value) {
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
    SoftwareAppGoogleAndroid softwareAppGoogleAndroid = (SoftwareAppGoogleAndroid) o;
    return Objects.equals(this.androidFeatures, softwareAppGoogleAndroid.androidFeatures) &&
        Objects.equals(this.appPricing, softwareAppGoogleAndroid.appPricing) &&
        Objects.equals(this.appVersion, softwareAppGoogleAndroid.appVersion) &&
        Objects.equals(this.author, softwareAppGoogleAndroid.author) &&
        Objects.equals(this.autoUpdateMode, softwareAppGoogleAndroid.autoUpdateMode) &&
        Objects.equals(this.category, softwareAppGoogleAndroid.category) &&
        Objects.equals(this.contentRating, softwareAppGoogleAndroid.contentRating) &&
        Objects.equals(this.displayMode, softwareAppGoogleAndroid.displayMode) &&
        Objects.equals(this.distributionChannel, softwareAppGoogleAndroid.distributionChannel) &&
        Objects.equals(this.fullDescription, softwareAppGoogleAndroid.fullDescription) &&
        Objects.equals(this.iconUrl, softwareAppGoogleAndroid.iconUrl) &&
        Objects.equals(this.installType, softwareAppGoogleAndroid.installType) &&
        Objects.equals(this.managedConfigurationTemplateId, softwareAppGoogleAndroid.managedConfigurationTemplateId) &&
        Objects.equals(this.managedProperties, softwareAppGoogleAndroid.managedProperties) &&
        Objects.equals(this.minSdkVersion, softwareAppGoogleAndroid.minSdkVersion) &&
        Objects.equals(this.name, softwareAppGoogleAndroid.name) &&
        Objects.equals(this.permissionGrants, softwareAppGoogleAndroid.permissionGrants) &&
        Objects.equals(this.runtimePermission, softwareAppGoogleAndroid.runtimePermission) &&
        Objects.equals(this.startUrl, softwareAppGoogleAndroid.startUrl) &&
        Objects.equals(this.type, softwareAppGoogleAndroid.type) &&
        Objects.equals(this.updateTime, softwareAppGoogleAndroid.updateTime) &&
        Objects.equals(this.versionCode, softwareAppGoogleAndroid.versionCode)&&
        Objects.equals(this.additionalProperties, softwareAppGoogleAndroid.additionalProperties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(androidFeatures, appPricing, appVersion, author, autoUpdateMode, category, contentRating, displayMode, distributionChannel, fullDescription, iconUrl, installType, managedConfigurationTemplateId, managedProperties, minSdkVersion, name, permissionGrants, runtimePermission, startUrl, type, updateTime, versionCode, additionalProperties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SoftwareAppGoogleAndroid {\n");
    sb.append("    androidFeatures: ").append(toIndentedString(androidFeatures)).append("\n");
    sb.append("    appPricing: ").append(toIndentedString(appPricing)).append("\n");
    sb.append("    appVersion: ").append(toIndentedString(appVersion)).append("\n");
    sb.append("    author: ").append(toIndentedString(author)).append("\n");
    sb.append("    autoUpdateMode: ").append(toIndentedString(autoUpdateMode)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    contentRating: ").append(toIndentedString(contentRating)).append("\n");
    sb.append("    displayMode: ").append(toIndentedString(displayMode)).append("\n");
    sb.append("    distributionChannel: ").append(toIndentedString(distributionChannel)).append("\n");
    sb.append("    fullDescription: ").append(toIndentedString(fullDescription)).append("\n");
    sb.append("    iconUrl: ").append(toIndentedString(iconUrl)).append("\n");
    sb.append("    installType: ").append(toIndentedString(installType)).append("\n");
    sb.append("    managedConfigurationTemplateId: ").append(toIndentedString(managedConfigurationTemplateId)).append("\n");
    sb.append("    managedProperties: ").append(toIndentedString(managedProperties)).append("\n");
    sb.append("    minSdkVersion: ").append(toIndentedString(minSdkVersion)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    permissionGrants: ").append(toIndentedString(permissionGrants)).append("\n");
    sb.append("    runtimePermission: ").append(toIndentedString(runtimePermission)).append("\n");
    sb.append("    startUrl: ").append(toIndentedString(startUrl)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    updateTime: ").append(toIndentedString(updateTime)).append("\n");
    sb.append("    versionCode: ").append(toIndentedString(versionCode)).append("\n");
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
    openapiFields.add("androidFeatures");
    openapiFields.add("appPricing");
    openapiFields.add("appVersion");
    openapiFields.add("author");
    openapiFields.add("autoUpdateMode");
    openapiFields.add("category");
    openapiFields.add("contentRating");
    openapiFields.add("displayMode");
    openapiFields.add("distributionChannel");
    openapiFields.add("fullDescription");
    openapiFields.add("iconUrl");
    openapiFields.add("installType");
    openapiFields.add("managedConfigurationTemplateId");
    openapiFields.add("managedProperties");
    openapiFields.add("minSdkVersion");
    openapiFields.add("name");
    openapiFields.add("permissionGrants");
    openapiFields.add("runtimePermission");
    openapiFields.add("startUrl");
    openapiFields.add("type");
    openapiFields.add("updateTime");
    openapiFields.add("versionCode");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

 /**
  * Validates the JSON Object and throws an exception if issues found
  *
  * @param jsonObj JSON Object
  * @throws IOException if the JSON Object is invalid with respect to SoftwareAppGoogleAndroid
  */
  public static void validateJsonObject(JsonObject jsonObj) throws IOException {
      if (jsonObj == null) {
        if (!SoftwareAppGoogleAndroid.openapiRequiredFields.isEmpty()) { // has required fields but JSON object is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in SoftwareAppGoogleAndroid is not found in the empty JSON string", SoftwareAppGoogleAndroid.openapiRequiredFields.toString()));
        }
      }
      // ensure the optional json data is an array if present
      if (jsonObj.get("androidFeatures") != null && !jsonObj.get("androidFeatures").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `androidFeatures` to be an array in the JSON string but got `%s`", jsonObj.get("androidFeatures").toString()));
      }
      if ((jsonObj.get("appPricing") != null && !jsonObj.get("appPricing").isJsonNull()) && !jsonObj.get("appPricing").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `appPricing` to be a primitive type in the JSON string but got `%s`", jsonObj.get("appPricing").toString()));
      }
      if ((jsonObj.get("appVersion") != null && !jsonObj.get("appVersion").isJsonNull()) && !jsonObj.get("appVersion").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `appVersion` to be a primitive type in the JSON string but got `%s`", jsonObj.get("appVersion").toString()));
      }
      if ((jsonObj.get("author") != null && !jsonObj.get("author").isJsonNull()) && !jsonObj.get("author").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `author` to be a primitive type in the JSON string but got `%s`", jsonObj.get("author").toString()));
      }
      if ((jsonObj.get("autoUpdateMode") != null && !jsonObj.get("autoUpdateMode").isJsonNull()) && !jsonObj.get("autoUpdateMode").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `autoUpdateMode` to be a primitive type in the JSON string but got `%s`", jsonObj.get("autoUpdateMode").toString()));
      }
      if ((jsonObj.get("category") != null && !jsonObj.get("category").isJsonNull()) && !jsonObj.get("category").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `category` to be a primitive type in the JSON string but got `%s`", jsonObj.get("category").toString()));
      }
      if ((jsonObj.get("contentRating") != null && !jsonObj.get("contentRating").isJsonNull()) && !jsonObj.get("contentRating").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `contentRating` to be a primitive type in the JSON string but got `%s`", jsonObj.get("contentRating").toString()));
      }
      if ((jsonObj.get("displayMode") != null && !jsonObj.get("displayMode").isJsonNull()) && !jsonObj.get("displayMode").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `displayMode` to be a primitive type in the JSON string but got `%s`", jsonObj.get("displayMode").toString()));
      }
      if ((jsonObj.get("distributionChannel") != null && !jsonObj.get("distributionChannel").isJsonNull()) && !jsonObj.get("distributionChannel").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `distributionChannel` to be a primitive type in the JSON string but got `%s`", jsonObj.get("distributionChannel").toString()));
      }
      if ((jsonObj.get("fullDescription") != null && !jsonObj.get("fullDescription").isJsonNull()) && !jsonObj.get("fullDescription").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `fullDescription` to be a primitive type in the JSON string but got `%s`", jsonObj.get("fullDescription").toString()));
      }
      if ((jsonObj.get("iconUrl") != null && !jsonObj.get("iconUrl").isJsonNull()) && !jsonObj.get("iconUrl").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `iconUrl` to be a primitive type in the JSON string but got `%s`", jsonObj.get("iconUrl").toString()));
      }
      if ((jsonObj.get("installType") != null && !jsonObj.get("installType").isJsonNull()) && !jsonObj.get("installType").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `installType` to be a primitive type in the JSON string but got `%s`", jsonObj.get("installType").toString()));
      }
      if ((jsonObj.get("managedConfigurationTemplateId") != null && !jsonObj.get("managedConfigurationTemplateId").isJsonNull()) && !jsonObj.get("managedConfigurationTemplateId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `managedConfigurationTemplateId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("managedConfigurationTemplateId").toString()));
      }
      if ((jsonObj.get("name") != null && !jsonObj.get("name").isJsonNull()) && !jsonObj.get("name").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `name` to be a primitive type in the JSON string but got `%s`", jsonObj.get("name").toString()));
      }
      if (jsonObj.get("permissionGrants") != null && !jsonObj.get("permissionGrants").isJsonNull()) {
        JsonArray jsonArraypermissionGrants = jsonObj.getAsJsonArray("permissionGrants");
        if (jsonArraypermissionGrants != null) {
          // ensure the json data is an array
          if (!jsonObj.get("permissionGrants").isJsonArray()) {
            throw new IllegalArgumentException(String.format("Expected the field `permissionGrants` to be an array in the JSON string but got `%s`", jsonObj.get("permissionGrants").toString()));
          }

          // validate the optional field `permissionGrants` (array)
          for (int i = 0; i < jsonArraypermissionGrants.size(); i++) {
            SoftwareAppPermissionGrants.validateJsonObject(jsonArraypermissionGrants.get(i).getAsJsonObject());
          };
        }
      }
      if ((jsonObj.get("runtimePermission") != null && !jsonObj.get("runtimePermission").isJsonNull()) && !jsonObj.get("runtimePermission").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `runtimePermission` to be a primitive type in the JSON string but got `%s`", jsonObj.get("runtimePermission").toString()));
      }
      if ((jsonObj.get("startUrl") != null && !jsonObj.get("startUrl").isJsonNull()) && !jsonObj.get("startUrl").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `startUrl` to be a primitive type in the JSON string but got `%s`", jsonObj.get("startUrl").toString()));
      }
      if ((jsonObj.get("type") != null && !jsonObj.get("type").isJsonNull()) && !jsonObj.get("type").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `type` to be a primitive type in the JSON string but got `%s`", jsonObj.get("type").toString()));
      }
      if ((jsonObj.get("updateTime") != null && !jsonObj.get("updateTime").isJsonNull()) && !jsonObj.get("updateTime").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `updateTime` to be a primitive type in the JSON string but got `%s`", jsonObj.get("updateTime").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!SoftwareAppGoogleAndroid.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'SoftwareAppGoogleAndroid' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<SoftwareAppGoogleAndroid> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(SoftwareAppGoogleAndroid.class));

       return (TypeAdapter<T>) new TypeAdapter<SoftwareAppGoogleAndroid>() {
           @Override
           public void write(JsonWriter out, SoftwareAppGoogleAndroid value) throws IOException {
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
           public SoftwareAppGoogleAndroid read(JsonReader in) throws IOException {
             JsonObject jsonObj = elementAdapter.read(in).getAsJsonObject();
             validateJsonObject(jsonObj);
             // store additional fields in the deserialized instance
             SoftwareAppGoogleAndroid instance = thisAdapter.fromJsonTree(jsonObj);
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
  * Create an instance of SoftwareAppGoogleAndroid given an JSON string
  *
  * @param jsonString JSON string
  * @return An instance of SoftwareAppGoogleAndroid
  * @throws IOException if the JSON string is invalid with respect to SoftwareAppGoogleAndroid
  */
  public static SoftwareAppGoogleAndroid fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, SoftwareAppGoogleAndroid.class);
  }

 /**
  * Convert an instance of SoftwareAppGoogleAndroid to an JSON string
  *
  * @return JSON string
  */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

