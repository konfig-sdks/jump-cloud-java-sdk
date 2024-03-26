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
 * SystemInsightsMounts
 */@javax.annotation.Generated(value = "Generated by https://konfigthis.com")
public class SystemInsightsMounts {
  public static final String SERIALIZED_NAME_BLOCKS = "blocks";
  @SerializedName(SERIALIZED_NAME_BLOCKS)
  private String blocks;

  public static final String SERIALIZED_NAME_BLOCKS_AVAILABLE = "blocks_available";
  @SerializedName(SERIALIZED_NAME_BLOCKS_AVAILABLE)
  private String blocksAvailable;

  public static final String SERIALIZED_NAME_BLOCKS_FREE = "blocks_free";
  @SerializedName(SERIALIZED_NAME_BLOCKS_FREE)
  private String blocksFree;

  public static final String SERIALIZED_NAME_BLOCKS_SIZE = "blocks_size";
  @SerializedName(SERIALIZED_NAME_BLOCKS_SIZE)
  private String blocksSize;

  public static final String SERIALIZED_NAME_COLLECTION_TIME = "collection_time";
  @SerializedName(SERIALIZED_NAME_COLLECTION_TIME)
  private String collectionTime;

  public static final String SERIALIZED_NAME_DEVICE = "device";
  @SerializedName(SERIALIZED_NAME_DEVICE)
  private String device;

  public static final String SERIALIZED_NAME_DEVICE_ALIAS = "device_alias";
  @SerializedName(SERIALIZED_NAME_DEVICE_ALIAS)
  private String deviceAlias;

  public static final String SERIALIZED_NAME_FLAGS = "flags";
  @SerializedName(SERIALIZED_NAME_FLAGS)
  private String flags;

  public static final String SERIALIZED_NAME_INODES = "inodes";
  @SerializedName(SERIALIZED_NAME_INODES)
  private String inodes;

  public static final String SERIALIZED_NAME_INODES_FREE = "inodes_free";
  @SerializedName(SERIALIZED_NAME_INODES_FREE)
  private String inodesFree;

  public static final String SERIALIZED_NAME_PATH = "path";
  @SerializedName(SERIALIZED_NAME_PATH)
  private String path;

  public static final String SERIALIZED_NAME_SYSTEM_ID = "system_id";
  @SerializedName(SERIALIZED_NAME_SYSTEM_ID)
  private String systemId;

  public static final String SERIALIZED_NAME_TYPE = "type";
  @SerializedName(SERIALIZED_NAME_TYPE)
  private String type;

  public SystemInsightsMounts() {
  }

  public SystemInsightsMounts blocks(String blocks) {
    
    
    
    
    this.blocks = blocks;
    return this;
  }

   /**
   * Get blocks
   * @return blocks
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "388086", value = "")

  public String getBlocks() {
    return blocks;
  }


  public void setBlocks(String blocks) {
    
    
    
    this.blocks = blocks;
  }


  public SystemInsightsMounts blocksAvailable(String blocksAvailable) {
    
    
    
    
    this.blocksAvailable = blocksAvailable;
    return this;
  }

   /**
   * Get blocksAvailable
   * @return blocksAvailable
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "95135", value = "")

  public String getBlocksAvailable() {
    return blocksAvailable;
  }


  public void setBlocksAvailable(String blocksAvailable) {
    
    
    
    this.blocksAvailable = blocksAvailable;
  }


  public SystemInsightsMounts blocksFree(String blocksFree) {
    
    
    
    
    this.blocksFree = blocksFree;
    return this;
  }

   /**
   * Get blocksFree
   * @return blocksFree
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "95135", value = "")

  public String getBlocksFree() {
    return blocksFree;
  }


  public void setBlocksFree(String blocksFree) {
    
    
    
    this.blocksFree = blocksFree;
  }


  public SystemInsightsMounts blocksSize(String blocksSize) {
    
    
    
    
    this.blocksSize = blocksSize;
    return this;
  }

   /**
   * Get blocksSize
   * @return blocksSize
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "4096", value = "")

  public String getBlocksSize() {
    return blocksSize;
  }


  public void setBlocksSize(String blocksSize) {
    
    
    
    this.blocksSize = blocksSize;
  }


  public SystemInsightsMounts collectionTime(String collectionTime) {
    
    
    
    
    this.collectionTime = collectionTime;
    return this;
  }

   /**
   * Get collectionTime
   * @return collectionTime
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "2019-06-03T19:41:31.746Z", value = "")

  public String getCollectionTime() {
    return collectionTime;
  }


  public void setCollectionTime(String collectionTime) {
    
    
    
    this.collectionTime = collectionTime;
  }


  public SystemInsightsMounts device(String device) {
    
    
    
    
    this.device = device;
    return this;
  }

   /**
   * Get device
   * @return device
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "/dev/disk5s1", value = "")

  public String getDevice() {
    return device;
  }


  public void setDevice(String device) {
    
    
    
    this.device = device;
  }


  public SystemInsightsMounts deviceAlias(String deviceAlias) {
    
    
    
    
    this.deviceAlias = deviceAlias;
    return this;
  }

   /**
   * Get deviceAlias
   * @return deviceAlias
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "/dev/disk5s1", value = "")

  public String getDeviceAlias() {
    return deviceAlias;
  }


  public void setDeviceAlias(String deviceAlias) {
    
    
    
    this.deviceAlias = deviceAlias;
  }


  public SystemInsightsMounts flags(String flags) {
    
    
    
    
    this.flags = flags;
    return this;
  }

   /**
   * Get flags
   * @return flags
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "70292505", value = "")

  public String getFlags() {
    return flags;
  }


  public void setFlags(String flags) {
    
    
    
    this.flags = flags;
  }


  public SystemInsightsMounts inodes(String inodes) {
    
    
    
    
    this.inodes = inodes;
    return this;
  }

   /**
   * Get inodes
   * @return inodes
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "4294967279", value = "")

  public String getInodes() {
    return inodes;
  }


  public void setInodes(String inodes) {
    
    
    
    this.inodes = inodes;
  }


  public SystemInsightsMounts inodesFree(String inodesFree) {
    
    
    
    
    this.inodesFree = inodesFree;
    return this;
  }

   /**
   * Get inodesFree
   * @return inodesFree
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "4294966853", value = "")

  public String getInodesFree() {
    return inodesFree;
  }


  public void setInodesFree(String inodesFree) {
    
    
    
    this.inodesFree = inodesFree;
  }


  public SystemInsightsMounts path(String path) {
    
    
    
    
    this.path = path;
    return this;
  }

   /**
   * Get path
   * @return path
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "/private/var/folders/zz/zyxvpxvq6csfxvn_n0000000000000/T/.vbfolder_1dQavK", value = "")

  public String getPath() {
    return path;
  }


  public void setPath(String path) {
    
    
    
    this.path = path;
  }


  public SystemInsightsMounts systemId(String systemId) {
    
    
    
    
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


  public SystemInsightsMounts type(String type) {
    
    
    
    
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "hfs", value = "")

  public String getType() {
    return type;
  }


  public void setType(String type) {
    
    
    
    this.type = type;
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
   * @return the SystemInsightsMounts instance itself
   */
  public SystemInsightsMounts putAdditionalProperty(String key, Object value) {
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
    SystemInsightsMounts systemInsightsMounts = (SystemInsightsMounts) o;
    return Objects.equals(this.blocks, systemInsightsMounts.blocks) &&
        Objects.equals(this.blocksAvailable, systemInsightsMounts.blocksAvailable) &&
        Objects.equals(this.blocksFree, systemInsightsMounts.blocksFree) &&
        Objects.equals(this.blocksSize, systemInsightsMounts.blocksSize) &&
        Objects.equals(this.collectionTime, systemInsightsMounts.collectionTime) &&
        Objects.equals(this.device, systemInsightsMounts.device) &&
        Objects.equals(this.deviceAlias, systemInsightsMounts.deviceAlias) &&
        Objects.equals(this.flags, systemInsightsMounts.flags) &&
        Objects.equals(this.inodes, systemInsightsMounts.inodes) &&
        Objects.equals(this.inodesFree, systemInsightsMounts.inodesFree) &&
        Objects.equals(this.path, systemInsightsMounts.path) &&
        Objects.equals(this.systemId, systemInsightsMounts.systemId) &&
        Objects.equals(this.type, systemInsightsMounts.type)&&
        Objects.equals(this.additionalProperties, systemInsightsMounts.additionalProperties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(blocks, blocksAvailable, blocksFree, blocksSize, collectionTime, device, deviceAlias, flags, inodes, inodesFree, path, systemId, type, additionalProperties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SystemInsightsMounts {\n");
    sb.append("    blocks: ").append(toIndentedString(blocks)).append("\n");
    sb.append("    blocksAvailable: ").append(toIndentedString(blocksAvailable)).append("\n");
    sb.append("    blocksFree: ").append(toIndentedString(blocksFree)).append("\n");
    sb.append("    blocksSize: ").append(toIndentedString(blocksSize)).append("\n");
    sb.append("    collectionTime: ").append(toIndentedString(collectionTime)).append("\n");
    sb.append("    device: ").append(toIndentedString(device)).append("\n");
    sb.append("    deviceAlias: ").append(toIndentedString(deviceAlias)).append("\n");
    sb.append("    flags: ").append(toIndentedString(flags)).append("\n");
    sb.append("    inodes: ").append(toIndentedString(inodes)).append("\n");
    sb.append("    inodesFree: ").append(toIndentedString(inodesFree)).append("\n");
    sb.append("    path: ").append(toIndentedString(path)).append("\n");
    sb.append("    systemId: ").append(toIndentedString(systemId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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
    openapiFields.add("blocks");
    openapiFields.add("blocks_available");
    openapiFields.add("blocks_free");
    openapiFields.add("blocks_size");
    openapiFields.add("collection_time");
    openapiFields.add("device");
    openapiFields.add("device_alias");
    openapiFields.add("flags");
    openapiFields.add("inodes");
    openapiFields.add("inodes_free");
    openapiFields.add("path");
    openapiFields.add("system_id");
    openapiFields.add("type");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

 /**
  * Validates the JSON Object and throws an exception if issues found
  *
  * @param jsonObj JSON Object
  * @throws IOException if the JSON Object is invalid with respect to SystemInsightsMounts
  */
  public static void validateJsonObject(JsonObject jsonObj) throws IOException {
      if (jsonObj == null) {
        if (!SystemInsightsMounts.openapiRequiredFields.isEmpty()) { // has required fields but JSON object is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in SystemInsightsMounts is not found in the empty JSON string", SystemInsightsMounts.openapiRequiredFields.toString()));
        }
      }
      if ((jsonObj.get("blocks") != null && !jsonObj.get("blocks").isJsonNull()) && !jsonObj.get("blocks").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `blocks` to be a primitive type in the JSON string but got `%s`", jsonObj.get("blocks").toString()));
      }
      if ((jsonObj.get("blocks_available") != null && !jsonObj.get("blocks_available").isJsonNull()) && !jsonObj.get("blocks_available").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `blocks_available` to be a primitive type in the JSON string but got `%s`", jsonObj.get("blocks_available").toString()));
      }
      if ((jsonObj.get("blocks_free") != null && !jsonObj.get("blocks_free").isJsonNull()) && !jsonObj.get("blocks_free").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `blocks_free` to be a primitive type in the JSON string but got `%s`", jsonObj.get("blocks_free").toString()));
      }
      if ((jsonObj.get("blocks_size") != null && !jsonObj.get("blocks_size").isJsonNull()) && !jsonObj.get("blocks_size").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `blocks_size` to be a primitive type in the JSON string but got `%s`", jsonObj.get("blocks_size").toString()));
      }
      if ((jsonObj.get("collection_time") != null && !jsonObj.get("collection_time").isJsonNull()) && !jsonObj.get("collection_time").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `collection_time` to be a primitive type in the JSON string but got `%s`", jsonObj.get("collection_time").toString()));
      }
      if ((jsonObj.get("device") != null && !jsonObj.get("device").isJsonNull()) && !jsonObj.get("device").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `device` to be a primitive type in the JSON string but got `%s`", jsonObj.get("device").toString()));
      }
      if ((jsonObj.get("device_alias") != null && !jsonObj.get("device_alias").isJsonNull()) && !jsonObj.get("device_alias").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `device_alias` to be a primitive type in the JSON string but got `%s`", jsonObj.get("device_alias").toString()));
      }
      if ((jsonObj.get("flags") != null && !jsonObj.get("flags").isJsonNull()) && !jsonObj.get("flags").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `flags` to be a primitive type in the JSON string but got `%s`", jsonObj.get("flags").toString()));
      }
      if ((jsonObj.get("inodes") != null && !jsonObj.get("inodes").isJsonNull()) && !jsonObj.get("inodes").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `inodes` to be a primitive type in the JSON string but got `%s`", jsonObj.get("inodes").toString()));
      }
      if ((jsonObj.get("inodes_free") != null && !jsonObj.get("inodes_free").isJsonNull()) && !jsonObj.get("inodes_free").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `inodes_free` to be a primitive type in the JSON string but got `%s`", jsonObj.get("inodes_free").toString()));
      }
      if ((jsonObj.get("path") != null && !jsonObj.get("path").isJsonNull()) && !jsonObj.get("path").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `path` to be a primitive type in the JSON string but got `%s`", jsonObj.get("path").toString()));
      }
      if ((jsonObj.get("system_id") != null && !jsonObj.get("system_id").isJsonNull()) && !jsonObj.get("system_id").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `system_id` to be a primitive type in the JSON string but got `%s`", jsonObj.get("system_id").toString()));
      }
      if ((jsonObj.get("type") != null && !jsonObj.get("type").isJsonNull()) && !jsonObj.get("type").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `type` to be a primitive type in the JSON string but got `%s`", jsonObj.get("type").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!SystemInsightsMounts.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'SystemInsightsMounts' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<SystemInsightsMounts> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(SystemInsightsMounts.class));

       return (TypeAdapter<T>) new TypeAdapter<SystemInsightsMounts>() {
           @Override
           public void write(JsonWriter out, SystemInsightsMounts value) throws IOException {
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
           public SystemInsightsMounts read(JsonReader in) throws IOException {
             JsonObject jsonObj = elementAdapter.read(in).getAsJsonObject();
             validateJsonObject(jsonObj);
             // store additional fields in the deserialized instance
             SystemInsightsMounts instance = thisAdapter.fromJsonTree(jsonObj);
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
  * Create an instance of SystemInsightsMounts given an JSON string
  *
  * @param jsonString JSON string
  * @return An instance of SystemInsightsMounts
  * @throws IOException if the JSON string is invalid with respect to SystemInsightsMounts
  */
  public static SystemInsightsMounts fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, SystemInsightsMounts.class);
  }

 /**
  * Convert an instance of SystemInsightsMounts to an JSON string
  *
  * @return JSON string
  */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

