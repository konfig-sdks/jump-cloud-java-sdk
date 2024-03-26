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
 * SystemInsightsChassisInfo
 */@javax.annotation.Generated(value = "Generated by https://konfigthis.com")
public class SystemInsightsChassisInfo {
  public static final String SERIALIZED_NAME_DESCRIPTION = "description";
  @SerializedName(SERIALIZED_NAME_DESCRIPTION)
  private String description;

  public static final String SERIALIZED_NAME_AUDIBLE_ALARM = "audible_alarm";
  @SerializedName(SERIALIZED_NAME_AUDIBLE_ALARM)
  private String audibleAlarm;

  public static final String SERIALIZED_NAME_BREACH_DESCRIPTION = "breach_description";
  @SerializedName(SERIALIZED_NAME_BREACH_DESCRIPTION)
  private String breachDescription;

  public static final String SERIALIZED_NAME_CHASSIS_TYPES = "chassis_types";
  @SerializedName(SERIALIZED_NAME_CHASSIS_TYPES)
  private String chassisTypes;

  public static final String SERIALIZED_NAME_COLLECTION_TIME = "collection_time";
  @SerializedName(SERIALIZED_NAME_COLLECTION_TIME)
  private String collectionTime;

  public static final String SERIALIZED_NAME_LOCK = "lock";
  @SerializedName(SERIALIZED_NAME_LOCK)
  private String lock;

  public static final String SERIALIZED_NAME_MANUFACTURER = "manufacturer";
  @SerializedName(SERIALIZED_NAME_MANUFACTURER)
  private String manufacturer;

  public static final String SERIALIZED_NAME_MODEL = "model";
  @SerializedName(SERIALIZED_NAME_MODEL)
  private String model;

  public static final String SERIALIZED_NAME_SECURITY_BREACH = "security_breach";
  @SerializedName(SERIALIZED_NAME_SECURITY_BREACH)
  private String securityBreach;

  public static final String SERIALIZED_NAME_SERIAL = "serial";
  @SerializedName(SERIALIZED_NAME_SERIAL)
  private String serial;

  public static final String SERIALIZED_NAME_SKU = "sku";
  @SerializedName(SERIALIZED_NAME_SKU)
  private String sku;

  public static final String SERIALIZED_NAME_SMBIOS_TAG = "smbios_tag";
  @SerializedName(SERIALIZED_NAME_SMBIOS_TAG)
  private String smbiosTag;

  public static final String SERIALIZED_NAME_STATUS = "status";
  @SerializedName(SERIALIZED_NAME_STATUS)
  private String status;

  public static final String SERIALIZED_NAME_SYSTEM_ID = "system_id";
  @SerializedName(SERIALIZED_NAME_SYSTEM_ID)
  private String systemId;

  public static final String SERIALIZED_NAME_VISIBLE_ALARM = "visible_alarm";
  @SerializedName(SERIALIZED_NAME_VISIBLE_ALARM)
  private String visibleAlarm;

  public SystemInsightsChassisInfo() {
  }

  public SystemInsightsChassisInfo description(String description) {
    
    
    
    
    this.description = description;
    return this;
  }

   /**
   * Get description
   * @return description
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getDescription() {
    return description;
  }


  public void setDescription(String description) {
    
    
    
    this.description = description;
  }


  public SystemInsightsChassisInfo audibleAlarm(String audibleAlarm) {
    
    
    
    
    this.audibleAlarm = audibleAlarm;
    return this;
  }

   /**
   * Get audibleAlarm
   * @return audibleAlarm
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getAudibleAlarm() {
    return audibleAlarm;
  }


  public void setAudibleAlarm(String audibleAlarm) {
    
    
    
    this.audibleAlarm = audibleAlarm;
  }


  public SystemInsightsChassisInfo breachDescription(String breachDescription) {
    
    
    
    
    this.breachDescription = breachDescription;
    return this;
  }

   /**
   * Get breachDescription
   * @return breachDescription
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getBreachDescription() {
    return breachDescription;
  }


  public void setBreachDescription(String breachDescription) {
    
    
    
    this.breachDescription = breachDescription;
  }


  public SystemInsightsChassisInfo chassisTypes(String chassisTypes) {
    
    
    
    
    this.chassisTypes = chassisTypes;
    return this;
  }

   /**
   * Get chassisTypes
   * @return chassisTypes
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getChassisTypes() {
    return chassisTypes;
  }


  public void setChassisTypes(String chassisTypes) {
    
    
    
    this.chassisTypes = chassisTypes;
  }


  public SystemInsightsChassisInfo collectionTime(String collectionTime) {
    
    
    
    
    this.collectionTime = collectionTime;
    return this;
  }

   /**
   * Get collectionTime
   * @return collectionTime
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getCollectionTime() {
    return collectionTime;
  }


  public void setCollectionTime(String collectionTime) {
    
    
    
    this.collectionTime = collectionTime;
  }


  public SystemInsightsChassisInfo lock(String lock) {
    
    
    
    
    this.lock = lock;
    return this;
  }

   /**
   * Get lock
   * @return lock
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getLock() {
    return lock;
  }


  public void setLock(String lock) {
    
    
    
    this.lock = lock;
  }


  public SystemInsightsChassisInfo manufacturer(String manufacturer) {
    
    
    
    
    this.manufacturer = manufacturer;
    return this;
  }

   /**
   * Get manufacturer
   * @return manufacturer
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getManufacturer() {
    return manufacturer;
  }


  public void setManufacturer(String manufacturer) {
    
    
    
    this.manufacturer = manufacturer;
  }


  public SystemInsightsChassisInfo model(String model) {
    
    
    
    
    this.model = model;
    return this;
  }

   /**
   * Get model
   * @return model
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getModel() {
    return model;
  }


  public void setModel(String model) {
    
    
    
    this.model = model;
  }


  public SystemInsightsChassisInfo securityBreach(String securityBreach) {
    
    
    
    
    this.securityBreach = securityBreach;
    return this;
  }

   /**
   * Get securityBreach
   * @return securityBreach
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getSecurityBreach() {
    return securityBreach;
  }


  public void setSecurityBreach(String securityBreach) {
    
    
    
    this.securityBreach = securityBreach;
  }


  public SystemInsightsChassisInfo serial(String serial) {
    
    
    
    
    this.serial = serial;
    return this;
  }

   /**
   * Get serial
   * @return serial
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getSerial() {
    return serial;
  }


  public void setSerial(String serial) {
    
    
    
    this.serial = serial;
  }


  public SystemInsightsChassisInfo sku(String sku) {
    
    
    
    
    this.sku = sku;
    return this;
  }

   /**
   * Get sku
   * @return sku
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getSku() {
    return sku;
  }


  public void setSku(String sku) {
    
    
    
    this.sku = sku;
  }


  public SystemInsightsChassisInfo smbiosTag(String smbiosTag) {
    
    
    
    
    this.smbiosTag = smbiosTag;
    return this;
  }

   /**
   * Get smbiosTag
   * @return smbiosTag
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getSmbiosTag() {
    return smbiosTag;
  }


  public void setSmbiosTag(String smbiosTag) {
    
    
    
    this.smbiosTag = smbiosTag;
  }


  public SystemInsightsChassisInfo status(String status) {
    
    
    
    
    this.status = status;
    return this;
  }

   /**
   * Get status
   * @return status
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getStatus() {
    return status;
  }


  public void setStatus(String status) {
    
    
    
    this.status = status;
  }


  public SystemInsightsChassisInfo systemId(String systemId) {
    
    
    
    
    this.systemId = systemId;
    return this;
  }

   /**
   * Get systemId
   * @return systemId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getSystemId() {
    return systemId;
  }


  public void setSystemId(String systemId) {
    
    
    
    this.systemId = systemId;
  }


  public SystemInsightsChassisInfo visibleAlarm(String visibleAlarm) {
    
    
    
    
    this.visibleAlarm = visibleAlarm;
    return this;
  }

   /**
   * Get visibleAlarm
   * @return visibleAlarm
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getVisibleAlarm() {
    return visibleAlarm;
  }


  public void setVisibleAlarm(String visibleAlarm) {
    
    
    
    this.visibleAlarm = visibleAlarm;
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
   * @return the SystemInsightsChassisInfo instance itself
   */
  public SystemInsightsChassisInfo putAdditionalProperty(String key, Object value) {
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
    SystemInsightsChassisInfo systemInsightsChassisInfo = (SystemInsightsChassisInfo) o;
    return Objects.equals(this.description, systemInsightsChassisInfo.description) &&
        Objects.equals(this.audibleAlarm, systemInsightsChassisInfo.audibleAlarm) &&
        Objects.equals(this.breachDescription, systemInsightsChassisInfo.breachDescription) &&
        Objects.equals(this.chassisTypes, systemInsightsChassisInfo.chassisTypes) &&
        Objects.equals(this.collectionTime, systemInsightsChassisInfo.collectionTime) &&
        Objects.equals(this.lock, systemInsightsChassisInfo.lock) &&
        Objects.equals(this.manufacturer, systemInsightsChassisInfo.manufacturer) &&
        Objects.equals(this.model, systemInsightsChassisInfo.model) &&
        Objects.equals(this.securityBreach, systemInsightsChassisInfo.securityBreach) &&
        Objects.equals(this.serial, systemInsightsChassisInfo.serial) &&
        Objects.equals(this.sku, systemInsightsChassisInfo.sku) &&
        Objects.equals(this.smbiosTag, systemInsightsChassisInfo.smbiosTag) &&
        Objects.equals(this.status, systemInsightsChassisInfo.status) &&
        Objects.equals(this.systemId, systemInsightsChassisInfo.systemId) &&
        Objects.equals(this.visibleAlarm, systemInsightsChassisInfo.visibleAlarm)&&
        Objects.equals(this.additionalProperties, systemInsightsChassisInfo.additionalProperties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(description, audibleAlarm, breachDescription, chassisTypes, collectionTime, lock, manufacturer, model, securityBreach, serial, sku, smbiosTag, status, systemId, visibleAlarm, additionalProperties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SystemInsightsChassisInfo {\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    audibleAlarm: ").append(toIndentedString(audibleAlarm)).append("\n");
    sb.append("    breachDescription: ").append(toIndentedString(breachDescription)).append("\n");
    sb.append("    chassisTypes: ").append(toIndentedString(chassisTypes)).append("\n");
    sb.append("    collectionTime: ").append(toIndentedString(collectionTime)).append("\n");
    sb.append("    lock: ").append(toIndentedString(lock)).append("\n");
    sb.append("    manufacturer: ").append(toIndentedString(manufacturer)).append("\n");
    sb.append("    model: ").append(toIndentedString(model)).append("\n");
    sb.append("    securityBreach: ").append(toIndentedString(securityBreach)).append("\n");
    sb.append("    serial: ").append(toIndentedString(serial)).append("\n");
    sb.append("    sku: ").append(toIndentedString(sku)).append("\n");
    sb.append("    smbiosTag: ").append(toIndentedString(smbiosTag)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    systemId: ").append(toIndentedString(systemId)).append("\n");
    sb.append("    visibleAlarm: ").append(toIndentedString(visibleAlarm)).append("\n");
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
    openapiFields.add("description");
    openapiFields.add("audible_alarm");
    openapiFields.add("breach_description");
    openapiFields.add("chassis_types");
    openapiFields.add("collection_time");
    openapiFields.add("lock");
    openapiFields.add("manufacturer");
    openapiFields.add("model");
    openapiFields.add("security_breach");
    openapiFields.add("serial");
    openapiFields.add("sku");
    openapiFields.add("smbios_tag");
    openapiFields.add("status");
    openapiFields.add("system_id");
    openapiFields.add("visible_alarm");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

 /**
  * Validates the JSON Object and throws an exception if issues found
  *
  * @param jsonObj JSON Object
  * @throws IOException if the JSON Object is invalid with respect to SystemInsightsChassisInfo
  */
  public static void validateJsonObject(JsonObject jsonObj) throws IOException {
      if (jsonObj == null) {
        if (!SystemInsightsChassisInfo.openapiRequiredFields.isEmpty()) { // has required fields but JSON object is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in SystemInsightsChassisInfo is not found in the empty JSON string", SystemInsightsChassisInfo.openapiRequiredFields.toString()));
        }
      }
      if ((jsonObj.get("description") != null && !jsonObj.get("description").isJsonNull()) && !jsonObj.get("description").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `description` to be a primitive type in the JSON string but got `%s`", jsonObj.get("description").toString()));
      }
      if ((jsonObj.get("audible_alarm") != null && !jsonObj.get("audible_alarm").isJsonNull()) && !jsonObj.get("audible_alarm").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `audible_alarm` to be a primitive type in the JSON string but got `%s`", jsonObj.get("audible_alarm").toString()));
      }
      if ((jsonObj.get("breach_description") != null && !jsonObj.get("breach_description").isJsonNull()) && !jsonObj.get("breach_description").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `breach_description` to be a primitive type in the JSON string but got `%s`", jsonObj.get("breach_description").toString()));
      }
      if ((jsonObj.get("chassis_types") != null && !jsonObj.get("chassis_types").isJsonNull()) && !jsonObj.get("chassis_types").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `chassis_types` to be a primitive type in the JSON string but got `%s`", jsonObj.get("chassis_types").toString()));
      }
      if ((jsonObj.get("collection_time") != null && !jsonObj.get("collection_time").isJsonNull()) && !jsonObj.get("collection_time").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `collection_time` to be a primitive type in the JSON string but got `%s`", jsonObj.get("collection_time").toString()));
      }
      if ((jsonObj.get("lock") != null && !jsonObj.get("lock").isJsonNull()) && !jsonObj.get("lock").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `lock` to be a primitive type in the JSON string but got `%s`", jsonObj.get("lock").toString()));
      }
      if ((jsonObj.get("manufacturer") != null && !jsonObj.get("manufacturer").isJsonNull()) && !jsonObj.get("manufacturer").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `manufacturer` to be a primitive type in the JSON string but got `%s`", jsonObj.get("manufacturer").toString()));
      }
      if ((jsonObj.get("model") != null && !jsonObj.get("model").isJsonNull()) && !jsonObj.get("model").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `model` to be a primitive type in the JSON string but got `%s`", jsonObj.get("model").toString()));
      }
      if ((jsonObj.get("security_breach") != null && !jsonObj.get("security_breach").isJsonNull()) && !jsonObj.get("security_breach").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `security_breach` to be a primitive type in the JSON string but got `%s`", jsonObj.get("security_breach").toString()));
      }
      if ((jsonObj.get("serial") != null && !jsonObj.get("serial").isJsonNull()) && !jsonObj.get("serial").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `serial` to be a primitive type in the JSON string but got `%s`", jsonObj.get("serial").toString()));
      }
      if ((jsonObj.get("sku") != null && !jsonObj.get("sku").isJsonNull()) && !jsonObj.get("sku").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `sku` to be a primitive type in the JSON string but got `%s`", jsonObj.get("sku").toString()));
      }
      if ((jsonObj.get("smbios_tag") != null && !jsonObj.get("smbios_tag").isJsonNull()) && !jsonObj.get("smbios_tag").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `smbios_tag` to be a primitive type in the JSON string but got `%s`", jsonObj.get("smbios_tag").toString()));
      }
      if ((jsonObj.get("status") != null && !jsonObj.get("status").isJsonNull()) && !jsonObj.get("status").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `status` to be a primitive type in the JSON string but got `%s`", jsonObj.get("status").toString()));
      }
      if ((jsonObj.get("system_id") != null && !jsonObj.get("system_id").isJsonNull()) && !jsonObj.get("system_id").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `system_id` to be a primitive type in the JSON string but got `%s`", jsonObj.get("system_id").toString()));
      }
      if ((jsonObj.get("visible_alarm") != null && !jsonObj.get("visible_alarm").isJsonNull()) && !jsonObj.get("visible_alarm").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `visible_alarm` to be a primitive type in the JSON string but got `%s`", jsonObj.get("visible_alarm").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!SystemInsightsChassisInfo.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'SystemInsightsChassisInfo' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<SystemInsightsChassisInfo> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(SystemInsightsChassisInfo.class));

       return (TypeAdapter<T>) new TypeAdapter<SystemInsightsChassisInfo>() {
           @Override
           public void write(JsonWriter out, SystemInsightsChassisInfo value) throws IOException {
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
           public SystemInsightsChassisInfo read(JsonReader in) throws IOException {
             JsonObject jsonObj = elementAdapter.read(in).getAsJsonObject();
             validateJsonObject(jsonObj);
             // store additional fields in the deserialized instance
             SystemInsightsChassisInfo instance = thisAdapter.fromJsonTree(jsonObj);
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
  * Create an instance of SystemInsightsChassisInfo given an JSON string
  *
  * @param jsonString JSON string
  * @return An instance of SystemInsightsChassisInfo
  * @throws IOException if the JSON string is invalid with respect to SystemInsightsChassisInfo
  */
  public static SystemInsightsChassisInfo fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, SystemInsightsChassisInfo.class);
  }

 /**
  * Convert an instance of SystemInsightsChassisInfo to an JSON string
  *
  * @return JSON string
  */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

