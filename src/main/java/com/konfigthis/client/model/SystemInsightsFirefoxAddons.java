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
 * SystemInsightsFirefoxAddons
 */@javax.annotation.Generated(value = "Generated by https://konfigthis.com")
public class SystemInsightsFirefoxAddons {
  public static final String SERIALIZED_NAME_DESCRIPTION = "description";
  @SerializedName(SERIALIZED_NAME_DESCRIPTION)
  private String description;

  public static final String SERIALIZED_NAME_VERSION = "version";
  @SerializedName(SERIALIZED_NAME_VERSION)
  private String version;

  public static final String SERIALIZED_NAME_ACTIVE = "active";
  @SerializedName(SERIALIZED_NAME_ACTIVE)
  private Integer active;

  public static final String SERIALIZED_NAME_AUTOUPDATE = "autoupdate";
  @SerializedName(SERIALIZED_NAME_AUTOUPDATE)
  private Integer autoupdate;

  public static final String SERIALIZED_NAME_COLLECTION_TIME = "collection_time";
  @SerializedName(SERIALIZED_NAME_COLLECTION_TIME)
  private String collectionTime;

  public static final String SERIALIZED_NAME_CREATOR = "creator";
  @SerializedName(SERIALIZED_NAME_CREATOR)
  private String creator;

  public static final String SERIALIZED_NAME_DISABLED = "disabled";
  @SerializedName(SERIALIZED_NAME_DISABLED)
  private Integer disabled;

  public static final String SERIALIZED_NAME_IDENTIFIER = "identifier";
  @SerializedName(SERIALIZED_NAME_IDENTIFIER)
  private String identifier;

  public static final String SERIALIZED_NAME_LOCATION = "location";
  @SerializedName(SERIALIZED_NAME_LOCATION)
  private String location;

  public static final String SERIALIZED_NAME_NAME = "name";
  @SerializedName(SERIALIZED_NAME_NAME)
  private String name;

  public static final String SERIALIZED_NAME_PATH = "path";
  @SerializedName(SERIALIZED_NAME_PATH)
  private String path;

  public static final String SERIALIZED_NAME_SOURCE_URL = "source_url";
  @SerializedName(SERIALIZED_NAME_SOURCE_URL)
  private String sourceUrl;

  public static final String SERIALIZED_NAME_SYSTEM_ID = "system_id";
  @SerializedName(SERIALIZED_NAME_SYSTEM_ID)
  private String systemId;

  public static final String SERIALIZED_NAME_TYPE = "type";
  @SerializedName(SERIALIZED_NAME_TYPE)
  private String type;

  public static final String SERIALIZED_NAME_UID = "uid";
  @SerializedName(SERIALIZED_NAME_UID)
  private String uid;

  public static final String SERIALIZED_NAME_VISIBLE = "visible";
  @SerializedName(SERIALIZED_NAME_VISIBLE)
  private Integer visible;

  public SystemInsightsFirefoxAddons() {
  }

  public SystemInsightsFirefoxAddons description(String description) {
    
    
    
    
    this.description = description;
    return this;
  }

   /**
   * Get description
   * @return description
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "", value = "")

  public String getDescription() {
    return description;
  }


  public void setDescription(String description) {
    
    
    
    this.description = description;
  }


  public SystemInsightsFirefoxAddons version(String version) {
    
    
    
    
    this.version = version;
    return this;
  }

   /**
   * Get version
   * @return version
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "2.8", value = "")

  public String getVersion() {
    return version;
  }


  public void setVersion(String version) {
    
    
    
    this.version = version;
  }


  public SystemInsightsFirefoxAddons active(Integer active) {
    
    
    
    
    this.active = active;
    return this;
  }

   /**
   * Get active
   * @return active
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "1", value = "")

  public Integer getActive() {
    return active;
  }


  public void setActive(Integer active) {
    
    
    
    this.active = active;
  }


  public SystemInsightsFirefoxAddons autoupdate(Integer autoupdate) {
    
    
    
    
    this.autoupdate = autoupdate;
    return this;
  }

   /**
   * Get autoupdate
   * @return autoupdate
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "1", value = "")

  public Integer getAutoupdate() {
    return autoupdate;
  }


  public void setAutoupdate(Integer autoupdate) {
    
    
    
    this.autoupdate = autoupdate;
  }


  public SystemInsightsFirefoxAddons collectionTime(String collectionTime) {
    
    
    
    
    this.collectionTime = collectionTime;
    return this;
  }

   /**
   * Get collectionTime
   * @return collectionTime
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "2019-06-03T19:41:31.204Z", value = "")

  public String getCollectionTime() {
    return collectionTime;
  }


  public void setCollectionTime(String collectionTime) {
    
    
    
    this.collectionTime = collectionTime;
  }


  public SystemInsightsFirefoxAddons creator(String creator) {
    
    
    
    
    this.creator = creator;
    return this;
  }

   /**
   * Get creator
   * @return creator
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "null", value = "")

  public String getCreator() {
    return creator;
  }


  public void setCreator(String creator) {
    
    
    
    this.creator = creator;
  }


  public SystemInsightsFirefoxAddons disabled(Integer disabled) {
    
    
    
    
    this.disabled = disabled;
    return this;
  }

   /**
   * Get disabled
   * @return disabled
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "0", value = "")

  public Integer getDisabled() {
    return disabled;
  }


  public void setDisabled(Integer disabled) {
    
    
    
    this.disabled = disabled;
  }


  public SystemInsightsFirefoxAddons identifier(String identifier) {
    
    
    
    
    this.identifier = identifier;
    return this;
  }

   /**
   * Get identifier
   * @return identifier
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "fxmonitor@mozilla.org", value = "")

  public String getIdentifier() {
    return identifier;
  }


  public void setIdentifier(String identifier) {
    
    
    
    this.identifier = identifier;
  }


  public SystemInsightsFirefoxAddons location(String location) {
    
    
    
    
    this.location = location;
    return this;
  }

   /**
   * Get location
   * @return location
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "app-system-addons", value = "")

  public String getLocation() {
    return location;
  }


  public void setLocation(String location) {
    
    
    
    this.location = location;
  }


  public SystemInsightsFirefoxAddons name(String name) {
    
    
    
    
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "Firefox Monitor", value = "")

  public String getName() {
    return name;
  }


  public void setName(String name) {
    
    
    
    this.name = name;
  }


  public SystemInsightsFirefoxAddons path(String path) {
    
    
    
    
    this.path = path;
    return this;
  }

   /**
   * Get path
   * @return path
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "", value = "")

  public String getPath() {
    return path;
  }


  public void setPath(String path) {
    
    
    
    this.path = path;
  }


  public SystemInsightsFirefoxAddons sourceUrl(String sourceUrl) {
    
    
    
    
    this.sourceUrl = sourceUrl;
    return this;
  }

   /**
   * Get sourceUrl
   * @return sourceUrl
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "file:///var/folders/dn/zjm_0lzn4lv6bgnt5b3nkgxh0000gn/T/tmpaddon-9fb449", value = "")

  public String getSourceUrl() {
    return sourceUrl;
  }


  public void setSourceUrl(String sourceUrl) {
    
    
    
    this.sourceUrl = sourceUrl;
  }


  public SystemInsightsFirefoxAddons systemId(String systemId) {
    
    
    
    
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


  public SystemInsightsFirefoxAddons type(String type) {
    
    
    
    
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "extension", value = "")

  public String getType() {
    return type;
  }


  public void setType(String type) {
    
    
    
    this.type = type;
  }


  public SystemInsightsFirefoxAddons uid(String uid) {
    
    
    
    
    this.uid = uid;
    return this;
  }

   /**
   * Get uid
   * @return uid
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "501", value = "")

  public String getUid() {
    return uid;
  }


  public void setUid(String uid) {
    
    
    
    this.uid = uid;
  }


  public SystemInsightsFirefoxAddons visible(Integer visible) {
    
    
    
    
    this.visible = visible;
    return this;
  }

   /**
   * Get visible
   * @return visible
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "1", value = "")

  public Integer getVisible() {
    return visible;
  }


  public void setVisible(Integer visible) {
    
    
    
    this.visible = visible;
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
   * @return the SystemInsightsFirefoxAddons instance itself
   */
  public SystemInsightsFirefoxAddons putAdditionalProperty(String key, Object value) {
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
    SystemInsightsFirefoxAddons systemInsightsFirefoxAddons = (SystemInsightsFirefoxAddons) o;
    return Objects.equals(this.description, systemInsightsFirefoxAddons.description) &&
        Objects.equals(this.version, systemInsightsFirefoxAddons.version) &&
        Objects.equals(this.active, systemInsightsFirefoxAddons.active) &&
        Objects.equals(this.autoupdate, systemInsightsFirefoxAddons.autoupdate) &&
        Objects.equals(this.collectionTime, systemInsightsFirefoxAddons.collectionTime) &&
        Objects.equals(this.creator, systemInsightsFirefoxAddons.creator) &&
        Objects.equals(this.disabled, systemInsightsFirefoxAddons.disabled) &&
        Objects.equals(this.identifier, systemInsightsFirefoxAddons.identifier) &&
        Objects.equals(this.location, systemInsightsFirefoxAddons.location) &&
        Objects.equals(this.name, systemInsightsFirefoxAddons.name) &&
        Objects.equals(this.path, systemInsightsFirefoxAddons.path) &&
        Objects.equals(this.sourceUrl, systemInsightsFirefoxAddons.sourceUrl) &&
        Objects.equals(this.systemId, systemInsightsFirefoxAddons.systemId) &&
        Objects.equals(this.type, systemInsightsFirefoxAddons.type) &&
        Objects.equals(this.uid, systemInsightsFirefoxAddons.uid) &&
        Objects.equals(this.visible, systemInsightsFirefoxAddons.visible)&&
        Objects.equals(this.additionalProperties, systemInsightsFirefoxAddons.additionalProperties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(description, version, active, autoupdate, collectionTime, creator, disabled, identifier, location, name, path, sourceUrl, systemId, type, uid, visible, additionalProperties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SystemInsightsFirefoxAddons {\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
    sb.append("    autoupdate: ").append(toIndentedString(autoupdate)).append("\n");
    sb.append("    collectionTime: ").append(toIndentedString(collectionTime)).append("\n");
    sb.append("    creator: ").append(toIndentedString(creator)).append("\n");
    sb.append("    disabled: ").append(toIndentedString(disabled)).append("\n");
    sb.append("    identifier: ").append(toIndentedString(identifier)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    path: ").append(toIndentedString(path)).append("\n");
    sb.append("    sourceUrl: ").append(toIndentedString(sourceUrl)).append("\n");
    sb.append("    systemId: ").append(toIndentedString(systemId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    uid: ").append(toIndentedString(uid)).append("\n");
    sb.append("    visible: ").append(toIndentedString(visible)).append("\n");
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
    openapiFields.add("version");
    openapiFields.add("active");
    openapiFields.add("autoupdate");
    openapiFields.add("collection_time");
    openapiFields.add("creator");
    openapiFields.add("disabled");
    openapiFields.add("identifier");
    openapiFields.add("location");
    openapiFields.add("name");
    openapiFields.add("path");
    openapiFields.add("source_url");
    openapiFields.add("system_id");
    openapiFields.add("type");
    openapiFields.add("uid");
    openapiFields.add("visible");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

 /**
  * Validates the JSON Object and throws an exception if issues found
  *
  * @param jsonObj JSON Object
  * @throws IOException if the JSON Object is invalid with respect to SystemInsightsFirefoxAddons
  */
  public static void validateJsonObject(JsonObject jsonObj) throws IOException {
      if (jsonObj == null) {
        if (!SystemInsightsFirefoxAddons.openapiRequiredFields.isEmpty()) { // has required fields but JSON object is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in SystemInsightsFirefoxAddons is not found in the empty JSON string", SystemInsightsFirefoxAddons.openapiRequiredFields.toString()));
        }
      }
      if ((jsonObj.get("description") != null && !jsonObj.get("description").isJsonNull()) && !jsonObj.get("description").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `description` to be a primitive type in the JSON string but got `%s`", jsonObj.get("description").toString()));
      }
      if ((jsonObj.get("version") != null && !jsonObj.get("version").isJsonNull()) && !jsonObj.get("version").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `version` to be a primitive type in the JSON string but got `%s`", jsonObj.get("version").toString()));
      }
      if ((jsonObj.get("collection_time") != null && !jsonObj.get("collection_time").isJsonNull()) && !jsonObj.get("collection_time").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `collection_time` to be a primitive type in the JSON string but got `%s`", jsonObj.get("collection_time").toString()));
      }
      if ((jsonObj.get("creator") != null && !jsonObj.get("creator").isJsonNull()) && !jsonObj.get("creator").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `creator` to be a primitive type in the JSON string but got `%s`", jsonObj.get("creator").toString()));
      }
      if ((jsonObj.get("identifier") != null && !jsonObj.get("identifier").isJsonNull()) && !jsonObj.get("identifier").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `identifier` to be a primitive type in the JSON string but got `%s`", jsonObj.get("identifier").toString()));
      }
      if ((jsonObj.get("location") != null && !jsonObj.get("location").isJsonNull()) && !jsonObj.get("location").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `location` to be a primitive type in the JSON string but got `%s`", jsonObj.get("location").toString()));
      }
      if ((jsonObj.get("name") != null && !jsonObj.get("name").isJsonNull()) && !jsonObj.get("name").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `name` to be a primitive type in the JSON string but got `%s`", jsonObj.get("name").toString()));
      }
      if ((jsonObj.get("path") != null && !jsonObj.get("path").isJsonNull()) && !jsonObj.get("path").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `path` to be a primitive type in the JSON string but got `%s`", jsonObj.get("path").toString()));
      }
      if ((jsonObj.get("source_url") != null && !jsonObj.get("source_url").isJsonNull()) && !jsonObj.get("source_url").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `source_url` to be a primitive type in the JSON string but got `%s`", jsonObj.get("source_url").toString()));
      }
      if ((jsonObj.get("system_id") != null && !jsonObj.get("system_id").isJsonNull()) && !jsonObj.get("system_id").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `system_id` to be a primitive type in the JSON string but got `%s`", jsonObj.get("system_id").toString()));
      }
      if ((jsonObj.get("type") != null && !jsonObj.get("type").isJsonNull()) && !jsonObj.get("type").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `type` to be a primitive type in the JSON string but got `%s`", jsonObj.get("type").toString()));
      }
      if ((jsonObj.get("uid") != null && !jsonObj.get("uid").isJsonNull()) && !jsonObj.get("uid").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `uid` to be a primitive type in the JSON string but got `%s`", jsonObj.get("uid").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!SystemInsightsFirefoxAddons.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'SystemInsightsFirefoxAddons' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<SystemInsightsFirefoxAddons> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(SystemInsightsFirefoxAddons.class));

       return (TypeAdapter<T>) new TypeAdapter<SystemInsightsFirefoxAddons>() {
           @Override
           public void write(JsonWriter out, SystemInsightsFirefoxAddons value) throws IOException {
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
           public SystemInsightsFirefoxAddons read(JsonReader in) throws IOException {
             JsonObject jsonObj = elementAdapter.read(in).getAsJsonObject();
             validateJsonObject(jsonObj);
             // store additional fields in the deserialized instance
             SystemInsightsFirefoxAddons instance = thisAdapter.fromJsonTree(jsonObj);
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
  * Create an instance of SystemInsightsFirefoxAddons given an JSON string
  *
  * @param jsonString JSON string
  * @return An instance of SystemInsightsFirefoxAddons
  * @throws IOException if the JSON string is invalid with respect to SystemInsightsFirefoxAddons
  */
  public static SystemInsightsFirefoxAddons fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, SystemInsightsFirefoxAddons.class);
  }

 /**
  * Convert an instance of SystemInsightsFirefoxAddons to an JSON string
  *
  * @return JSON string
  */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

