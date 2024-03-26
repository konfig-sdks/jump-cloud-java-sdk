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
 * SystemInsightsWifiNetworks
 */@javax.annotation.Generated(value = "Generated by https://konfigthis.com")
public class SystemInsightsWifiNetworks {
  public static final String SERIALIZED_NAME_AUTO_LOGIN = "auto_login";
  @SerializedName(SERIALIZED_NAME_AUTO_LOGIN)
  private Double autoLogin;

  public static final String SERIALIZED_NAME_CAPTIVE_PORTAL = "captive_portal";
  @SerializedName(SERIALIZED_NAME_CAPTIVE_PORTAL)
  private Double captivePortal;

  public static final String SERIALIZED_NAME_COLLECTION_TIME = "collection_time";
  @SerializedName(SERIALIZED_NAME_COLLECTION_TIME)
  private String collectionTime;

  public static final String SERIALIZED_NAME_DISABLED = "disabled";
  @SerializedName(SERIALIZED_NAME_DISABLED)
  private Double disabled;

  public static final String SERIALIZED_NAME_LAST_CONNECTED = "last_connected";
  @SerializedName(SERIALIZED_NAME_LAST_CONNECTED)
  private Double lastConnected;

  public static final String SERIALIZED_NAME_NETWORK_NAME = "network_name";
  @SerializedName(SERIALIZED_NAME_NETWORK_NAME)
  private String networkName;

  public static final String SERIALIZED_NAME_PASSPOINT = "passpoint";
  @SerializedName(SERIALIZED_NAME_PASSPOINT)
  private Double passpoint;

  public static final String SERIALIZED_NAME_POSSIBLY_HIDDEN = "possibly_hidden";
  @SerializedName(SERIALIZED_NAME_POSSIBLY_HIDDEN)
  private Double possiblyHidden;

  public static final String SERIALIZED_NAME_ROAMING = "roaming";
  @SerializedName(SERIALIZED_NAME_ROAMING)
  private Double roaming;

  public static final String SERIALIZED_NAME_ROAMING_PROFILE = "roaming_profile";
  @SerializedName(SERIALIZED_NAME_ROAMING_PROFILE)
  private String roamingProfile;

  public static final String SERIALIZED_NAME_SECURITY_TYPE = "security_type";
  @SerializedName(SERIALIZED_NAME_SECURITY_TYPE)
  private String securityType;

  public static final String SERIALIZED_NAME_SSID = "ssid";
  @SerializedName(SERIALIZED_NAME_SSID)
  private String ssid;

  public static final String SERIALIZED_NAME_SYSTEM_ID = "system_id";
  @SerializedName(SERIALIZED_NAME_SYSTEM_ID)
  private String systemId;

  public static final String SERIALIZED_NAME_TEMPORARILY_DISABLED = "temporarily_disabled";
  @SerializedName(SERIALIZED_NAME_TEMPORARILY_DISABLED)
  private Double temporarilyDisabled;

  public SystemInsightsWifiNetworks() {
  }

  public SystemInsightsWifiNetworks autoLogin(Double autoLogin) {
    
    
    
    
    this.autoLogin = autoLogin;
    return this;
  }

  public SystemInsightsWifiNetworks autoLogin(Integer autoLogin) {
    
    
    
    
    this.autoLogin = autoLogin.doubleValue();
    return this;
  }

   /**
   * Get autoLogin
   * @return autoLogin
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Double getAutoLogin() {
    return autoLogin;
  }


  public void setAutoLogin(Double autoLogin) {
    
    
    
    this.autoLogin = autoLogin;
  }


  public SystemInsightsWifiNetworks captivePortal(Double captivePortal) {
    
    
    
    
    this.captivePortal = captivePortal;
    return this;
  }

  public SystemInsightsWifiNetworks captivePortal(Integer captivePortal) {
    
    
    
    
    this.captivePortal = captivePortal.doubleValue();
    return this;
  }

   /**
   * Get captivePortal
   * @return captivePortal
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Double getCaptivePortal() {
    return captivePortal;
  }


  public void setCaptivePortal(Double captivePortal) {
    
    
    
    this.captivePortal = captivePortal;
  }


  public SystemInsightsWifiNetworks collectionTime(String collectionTime) {
    
    
    
    
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


  public SystemInsightsWifiNetworks disabled(Double disabled) {
    
    
    
    
    this.disabled = disabled;
    return this;
  }

  public SystemInsightsWifiNetworks disabled(Integer disabled) {
    
    
    
    
    this.disabled = disabled.doubleValue();
    return this;
  }

   /**
   * Get disabled
   * @return disabled
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Double getDisabled() {
    return disabled;
  }


  public void setDisabled(Double disabled) {
    
    
    
    this.disabled = disabled;
  }


  public SystemInsightsWifiNetworks lastConnected(Double lastConnected) {
    
    
    
    
    this.lastConnected = lastConnected;
    return this;
  }

  public SystemInsightsWifiNetworks lastConnected(Integer lastConnected) {
    
    
    
    
    this.lastConnected = lastConnected.doubleValue();
    return this;
  }

   /**
   * Get lastConnected
   * @return lastConnected
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Double getLastConnected() {
    return lastConnected;
  }


  public void setLastConnected(Double lastConnected) {
    
    
    
    this.lastConnected = lastConnected;
  }


  public SystemInsightsWifiNetworks networkName(String networkName) {
    
    
    
    
    this.networkName = networkName;
    return this;
  }

   /**
   * Get networkName
   * @return networkName
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getNetworkName() {
    return networkName;
  }


  public void setNetworkName(String networkName) {
    
    
    
    this.networkName = networkName;
  }


  public SystemInsightsWifiNetworks passpoint(Double passpoint) {
    
    
    
    
    this.passpoint = passpoint;
    return this;
  }

  public SystemInsightsWifiNetworks passpoint(Integer passpoint) {
    
    
    
    
    this.passpoint = passpoint.doubleValue();
    return this;
  }

   /**
   * Get passpoint
   * @return passpoint
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Double getPasspoint() {
    return passpoint;
  }


  public void setPasspoint(Double passpoint) {
    
    
    
    this.passpoint = passpoint;
  }


  public SystemInsightsWifiNetworks possiblyHidden(Double possiblyHidden) {
    
    
    
    
    this.possiblyHidden = possiblyHidden;
    return this;
  }

  public SystemInsightsWifiNetworks possiblyHidden(Integer possiblyHidden) {
    
    
    
    
    this.possiblyHidden = possiblyHidden.doubleValue();
    return this;
  }

   /**
   * Get possiblyHidden
   * @return possiblyHidden
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Double getPossiblyHidden() {
    return possiblyHidden;
  }


  public void setPossiblyHidden(Double possiblyHidden) {
    
    
    
    this.possiblyHidden = possiblyHidden;
  }


  public SystemInsightsWifiNetworks roaming(Double roaming) {
    
    
    
    
    this.roaming = roaming;
    return this;
  }

  public SystemInsightsWifiNetworks roaming(Integer roaming) {
    
    
    
    
    this.roaming = roaming.doubleValue();
    return this;
  }

   /**
   * Get roaming
   * @return roaming
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Double getRoaming() {
    return roaming;
  }


  public void setRoaming(Double roaming) {
    
    
    
    this.roaming = roaming;
  }


  public SystemInsightsWifiNetworks roamingProfile(String roamingProfile) {
    
    
    
    
    this.roamingProfile = roamingProfile;
    return this;
  }

   /**
   * Get roamingProfile
   * @return roamingProfile
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getRoamingProfile() {
    return roamingProfile;
  }


  public void setRoamingProfile(String roamingProfile) {
    
    
    
    this.roamingProfile = roamingProfile;
  }


  public SystemInsightsWifiNetworks securityType(String securityType) {
    
    
    
    
    this.securityType = securityType;
    return this;
  }

   /**
   * Get securityType
   * @return securityType
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getSecurityType() {
    return securityType;
  }


  public void setSecurityType(String securityType) {
    
    
    
    this.securityType = securityType;
  }


  public SystemInsightsWifiNetworks ssid(String ssid) {
    
    
    
    
    this.ssid = ssid;
    return this;
  }

   /**
   * Get ssid
   * @return ssid
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getSsid() {
    return ssid;
  }


  public void setSsid(String ssid) {
    
    
    
    this.ssid = ssid;
  }


  public SystemInsightsWifiNetworks systemId(String systemId) {
    
    
    
    
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


  public SystemInsightsWifiNetworks temporarilyDisabled(Double temporarilyDisabled) {
    
    
    
    
    this.temporarilyDisabled = temporarilyDisabled;
    return this;
  }

  public SystemInsightsWifiNetworks temporarilyDisabled(Integer temporarilyDisabled) {
    
    
    
    
    this.temporarilyDisabled = temporarilyDisabled.doubleValue();
    return this;
  }

   /**
   * Get temporarilyDisabled
   * @return temporarilyDisabled
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Double getTemporarilyDisabled() {
    return temporarilyDisabled;
  }


  public void setTemporarilyDisabled(Double temporarilyDisabled) {
    
    
    
    this.temporarilyDisabled = temporarilyDisabled;
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
   * @return the SystemInsightsWifiNetworks instance itself
   */
  public SystemInsightsWifiNetworks putAdditionalProperty(String key, Object value) {
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
    SystemInsightsWifiNetworks systemInsightsWifiNetworks = (SystemInsightsWifiNetworks) o;
    return Objects.equals(this.autoLogin, systemInsightsWifiNetworks.autoLogin) &&
        Objects.equals(this.captivePortal, systemInsightsWifiNetworks.captivePortal) &&
        Objects.equals(this.collectionTime, systemInsightsWifiNetworks.collectionTime) &&
        Objects.equals(this.disabled, systemInsightsWifiNetworks.disabled) &&
        Objects.equals(this.lastConnected, systemInsightsWifiNetworks.lastConnected) &&
        Objects.equals(this.networkName, systemInsightsWifiNetworks.networkName) &&
        Objects.equals(this.passpoint, systemInsightsWifiNetworks.passpoint) &&
        Objects.equals(this.possiblyHidden, systemInsightsWifiNetworks.possiblyHidden) &&
        Objects.equals(this.roaming, systemInsightsWifiNetworks.roaming) &&
        Objects.equals(this.roamingProfile, systemInsightsWifiNetworks.roamingProfile) &&
        Objects.equals(this.securityType, systemInsightsWifiNetworks.securityType) &&
        Objects.equals(this.ssid, systemInsightsWifiNetworks.ssid) &&
        Objects.equals(this.systemId, systemInsightsWifiNetworks.systemId) &&
        Objects.equals(this.temporarilyDisabled, systemInsightsWifiNetworks.temporarilyDisabled)&&
        Objects.equals(this.additionalProperties, systemInsightsWifiNetworks.additionalProperties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(autoLogin, captivePortal, collectionTime, disabled, lastConnected, networkName, passpoint, possiblyHidden, roaming, roamingProfile, securityType, ssid, systemId, temporarilyDisabled, additionalProperties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SystemInsightsWifiNetworks {\n");
    sb.append("    autoLogin: ").append(toIndentedString(autoLogin)).append("\n");
    sb.append("    captivePortal: ").append(toIndentedString(captivePortal)).append("\n");
    sb.append("    collectionTime: ").append(toIndentedString(collectionTime)).append("\n");
    sb.append("    disabled: ").append(toIndentedString(disabled)).append("\n");
    sb.append("    lastConnected: ").append(toIndentedString(lastConnected)).append("\n");
    sb.append("    networkName: ").append(toIndentedString(networkName)).append("\n");
    sb.append("    passpoint: ").append(toIndentedString(passpoint)).append("\n");
    sb.append("    possiblyHidden: ").append(toIndentedString(possiblyHidden)).append("\n");
    sb.append("    roaming: ").append(toIndentedString(roaming)).append("\n");
    sb.append("    roamingProfile: ").append(toIndentedString(roamingProfile)).append("\n");
    sb.append("    securityType: ").append(toIndentedString(securityType)).append("\n");
    sb.append("    ssid: ").append(toIndentedString(ssid)).append("\n");
    sb.append("    systemId: ").append(toIndentedString(systemId)).append("\n");
    sb.append("    temporarilyDisabled: ").append(toIndentedString(temporarilyDisabled)).append("\n");
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
    openapiFields.add("auto_login");
    openapiFields.add("captive_portal");
    openapiFields.add("collection_time");
    openapiFields.add("disabled");
    openapiFields.add("last_connected");
    openapiFields.add("network_name");
    openapiFields.add("passpoint");
    openapiFields.add("possibly_hidden");
    openapiFields.add("roaming");
    openapiFields.add("roaming_profile");
    openapiFields.add("security_type");
    openapiFields.add("ssid");
    openapiFields.add("system_id");
    openapiFields.add("temporarily_disabled");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

 /**
  * Validates the JSON Object and throws an exception if issues found
  *
  * @param jsonObj JSON Object
  * @throws IOException if the JSON Object is invalid with respect to SystemInsightsWifiNetworks
  */
  public static void validateJsonObject(JsonObject jsonObj) throws IOException {
      if (jsonObj == null) {
        if (!SystemInsightsWifiNetworks.openapiRequiredFields.isEmpty()) { // has required fields but JSON object is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in SystemInsightsWifiNetworks is not found in the empty JSON string", SystemInsightsWifiNetworks.openapiRequiredFields.toString()));
        }
      }
      if ((jsonObj.get("collection_time") != null && !jsonObj.get("collection_time").isJsonNull()) && !jsonObj.get("collection_time").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `collection_time` to be a primitive type in the JSON string but got `%s`", jsonObj.get("collection_time").toString()));
      }
      if ((jsonObj.get("network_name") != null && !jsonObj.get("network_name").isJsonNull()) && !jsonObj.get("network_name").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `network_name` to be a primitive type in the JSON string but got `%s`", jsonObj.get("network_name").toString()));
      }
      if ((jsonObj.get("roaming_profile") != null && !jsonObj.get("roaming_profile").isJsonNull()) && !jsonObj.get("roaming_profile").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `roaming_profile` to be a primitive type in the JSON string but got `%s`", jsonObj.get("roaming_profile").toString()));
      }
      if ((jsonObj.get("security_type") != null && !jsonObj.get("security_type").isJsonNull()) && !jsonObj.get("security_type").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `security_type` to be a primitive type in the JSON string but got `%s`", jsonObj.get("security_type").toString()));
      }
      if ((jsonObj.get("ssid") != null && !jsonObj.get("ssid").isJsonNull()) && !jsonObj.get("ssid").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `ssid` to be a primitive type in the JSON string but got `%s`", jsonObj.get("ssid").toString()));
      }
      if ((jsonObj.get("system_id") != null && !jsonObj.get("system_id").isJsonNull()) && !jsonObj.get("system_id").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `system_id` to be a primitive type in the JSON string but got `%s`", jsonObj.get("system_id").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!SystemInsightsWifiNetworks.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'SystemInsightsWifiNetworks' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<SystemInsightsWifiNetworks> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(SystemInsightsWifiNetworks.class));

       return (TypeAdapter<T>) new TypeAdapter<SystemInsightsWifiNetworks>() {
           @Override
           public void write(JsonWriter out, SystemInsightsWifiNetworks value) throws IOException {
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
           public SystemInsightsWifiNetworks read(JsonReader in) throws IOException {
             JsonObject jsonObj = elementAdapter.read(in).getAsJsonObject();
             validateJsonObject(jsonObj);
             // store additional fields in the deserialized instance
             SystemInsightsWifiNetworks instance = thisAdapter.fromJsonTree(jsonObj);
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
  * Create an instance of SystemInsightsWifiNetworks given an JSON string
  *
  * @param jsonString JSON string
  * @return An instance of SystemInsightsWifiNetworks
  * @throws IOException if the JSON string is invalid with respect to SystemInsightsWifiNetworks
  */
  public static SystemInsightsWifiNetworks fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, SystemInsightsWifiNetworks.class);
  }

 /**
  * Convert an instance of SystemInsightsWifiNetworks to an JSON string
  *
  * @return JSON string
  */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

