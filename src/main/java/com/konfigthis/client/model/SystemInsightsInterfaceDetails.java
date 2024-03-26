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
 * SystemInsightsInterfaceDetails
 */@javax.annotation.Generated(value = "Generated by https://konfigthis.com")
public class SystemInsightsInterfaceDetails {
  public static final String SERIALIZED_NAME_DESCRIPTION = "description";
  @SerializedName(SERIALIZED_NAME_DESCRIPTION)
  private String description;

  public static final String SERIALIZED_NAME_COLLISIONS = "collisions";
  @SerializedName(SERIALIZED_NAME_COLLISIONS)
  private String collisions;

  public static final String SERIALIZED_NAME_CONNECTION_ID = "connection_id";
  @SerializedName(SERIALIZED_NAME_CONNECTION_ID)
  private String connectionId;

  public static final String SERIALIZED_NAME_CONNECTION_STATUS = "connection_status";
  @SerializedName(SERIALIZED_NAME_CONNECTION_STATUS)
  private String connectionStatus;

  public static final String SERIALIZED_NAME_DHCP_ENABLED = "dhcp_enabled";
  @SerializedName(SERIALIZED_NAME_DHCP_ENABLED)
  private Integer dhcpEnabled;

  public static final String SERIALIZED_NAME_DHCP_LEASE_EXPIRES = "dhcp_lease_expires";
  @SerializedName(SERIALIZED_NAME_DHCP_LEASE_EXPIRES)
  private String dhcpLeaseExpires;

  public static final String SERIALIZED_NAME_DHCP_LEASE_OBTAINED = "dhcp_lease_obtained";
  @SerializedName(SERIALIZED_NAME_DHCP_LEASE_OBTAINED)
  private String dhcpLeaseObtained;

  public static final String SERIALIZED_NAME_DHCP_SERVER = "dhcp_server";
  @SerializedName(SERIALIZED_NAME_DHCP_SERVER)
  private String dhcpServer;

  public static final String SERIALIZED_NAME_DNS_DOMAIN = "dns_domain";
  @SerializedName(SERIALIZED_NAME_DNS_DOMAIN)
  private String dnsDomain;

  public static final String SERIALIZED_NAME_DNS_DOMAIN_SUFFIX_SEARCH_ORDER = "dns_domain_suffix_search_order";
  @SerializedName(SERIALIZED_NAME_DNS_DOMAIN_SUFFIX_SEARCH_ORDER)
  private String dnsDomainSuffixSearchOrder;

  public static final String SERIALIZED_NAME_DNS_HOST_NAME = "dns_host_name";
  @SerializedName(SERIALIZED_NAME_DNS_HOST_NAME)
  private String dnsHostName;

  public static final String SERIALIZED_NAME_DNS_SERVER_SEARCH_ORDER = "dns_server_search_order";
  @SerializedName(SERIALIZED_NAME_DNS_SERVER_SEARCH_ORDER)
  private String dnsServerSearchOrder;

  public static final String SERIALIZED_NAME_ENABLED = "enabled";
  @SerializedName(SERIALIZED_NAME_ENABLED)
  private Integer enabled;

  public static final String SERIALIZED_NAME_FLAGS = "flags";
  @SerializedName(SERIALIZED_NAME_FLAGS)
  private Integer flags;

  public static final String SERIALIZED_NAME_FRIENDLY_NAME = "friendly_name";
  @SerializedName(SERIALIZED_NAME_FRIENDLY_NAME)
  private String friendlyName;

  public static final String SERIALIZED_NAME_IBYTES = "ibytes";
  @SerializedName(SERIALIZED_NAME_IBYTES)
  private String ibytes;

  public static final String SERIALIZED_NAME_IDROPS = "idrops";
  @SerializedName(SERIALIZED_NAME_IDROPS)
  private String idrops;

  public static final String SERIALIZED_NAME_IERRORS = "ierrors";
  @SerializedName(SERIALIZED_NAME_IERRORS)
  private String ierrors;

  public static final String SERIALIZED_NAME_INTERFACE = "interface";
  @SerializedName(SERIALIZED_NAME_INTERFACE)
  private String _interface;

  public static final String SERIALIZED_NAME_IPACKETS = "ipackets";
  @SerializedName(SERIALIZED_NAME_IPACKETS)
  private String ipackets;

  public static final String SERIALIZED_NAME_LAST_CHANGE = "last_change";
  @SerializedName(SERIALIZED_NAME_LAST_CHANGE)
  private String lastChange;

  public static final String SERIALIZED_NAME_LINK_SPEED = "link_speed";
  @SerializedName(SERIALIZED_NAME_LINK_SPEED)
  private String linkSpeed;

  public static final String SERIALIZED_NAME_MAC = "mac";
  @SerializedName(SERIALIZED_NAME_MAC)
  private String mac;

  public static final String SERIALIZED_NAME_MANUFACTURER = "manufacturer";
  @SerializedName(SERIALIZED_NAME_MANUFACTURER)
  private String manufacturer;

  public static final String SERIALIZED_NAME_METRIC = "metric";
  @SerializedName(SERIALIZED_NAME_METRIC)
  private Integer metric;

  public static final String SERIALIZED_NAME_MTU = "mtu";
  @SerializedName(SERIALIZED_NAME_MTU)
  private Integer mtu;

  public static final String SERIALIZED_NAME_OBYTES = "obytes";
  @SerializedName(SERIALIZED_NAME_OBYTES)
  private String obytes;

  public static final String SERIALIZED_NAME_ODROPS = "odrops";
  @SerializedName(SERIALIZED_NAME_ODROPS)
  private String odrops;

  public static final String SERIALIZED_NAME_OERRORS = "oerrors";
  @SerializedName(SERIALIZED_NAME_OERRORS)
  private String oerrors;

  public static final String SERIALIZED_NAME_OPACKETS = "opackets";
  @SerializedName(SERIALIZED_NAME_OPACKETS)
  private String opackets;

  public static final String SERIALIZED_NAME_PCI_SLOT = "pci_slot";
  @SerializedName(SERIALIZED_NAME_PCI_SLOT)
  private String pciSlot;

  public static final String SERIALIZED_NAME_PHYSICAL_ADAPTER = "physical_adapter";
  @SerializedName(SERIALIZED_NAME_PHYSICAL_ADAPTER)
  private Integer physicalAdapter;

  public static final String SERIALIZED_NAME_SERVICE = "service";
  @SerializedName(SERIALIZED_NAME_SERVICE)
  private String service;

  public static final String SERIALIZED_NAME_SPEED = "speed";
  @SerializedName(SERIALIZED_NAME_SPEED)
  private Integer speed;

  public static final String SERIALIZED_NAME_SYSTEM_ID = "system_id";
  @SerializedName(SERIALIZED_NAME_SYSTEM_ID)
  private String systemId;

  public static final String SERIALIZED_NAME_TYPE = "type";
  @SerializedName(SERIALIZED_NAME_TYPE)
  private Integer type;

  public SystemInsightsInterfaceDetails() {
  }

  public SystemInsightsInterfaceDetails description(String description) {
    
    
    
    
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


  public SystemInsightsInterfaceDetails collisions(String collisions) {
    
    
    
    
    this.collisions = collisions;
    return this;
  }

   /**
   * Get collisions
   * @return collisions
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getCollisions() {
    return collisions;
  }


  public void setCollisions(String collisions) {
    
    
    
    this.collisions = collisions;
  }


  public SystemInsightsInterfaceDetails connectionId(String connectionId) {
    
    
    
    
    this.connectionId = connectionId;
    return this;
  }

   /**
   * Get connectionId
   * @return connectionId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getConnectionId() {
    return connectionId;
  }


  public void setConnectionId(String connectionId) {
    
    
    
    this.connectionId = connectionId;
  }


  public SystemInsightsInterfaceDetails connectionStatus(String connectionStatus) {
    
    
    
    
    this.connectionStatus = connectionStatus;
    return this;
  }

   /**
   * Get connectionStatus
   * @return connectionStatus
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getConnectionStatus() {
    return connectionStatus;
  }


  public void setConnectionStatus(String connectionStatus) {
    
    
    
    this.connectionStatus = connectionStatus;
  }


  public SystemInsightsInterfaceDetails dhcpEnabled(Integer dhcpEnabled) {
    
    
    
    
    this.dhcpEnabled = dhcpEnabled;
    return this;
  }

   /**
   * Get dhcpEnabled
   * @return dhcpEnabled
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Integer getDhcpEnabled() {
    return dhcpEnabled;
  }


  public void setDhcpEnabled(Integer dhcpEnabled) {
    
    
    
    this.dhcpEnabled = dhcpEnabled;
  }


  public SystemInsightsInterfaceDetails dhcpLeaseExpires(String dhcpLeaseExpires) {
    
    
    
    
    this.dhcpLeaseExpires = dhcpLeaseExpires;
    return this;
  }

   /**
   * Get dhcpLeaseExpires
   * @return dhcpLeaseExpires
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getDhcpLeaseExpires() {
    return dhcpLeaseExpires;
  }


  public void setDhcpLeaseExpires(String dhcpLeaseExpires) {
    
    
    
    this.dhcpLeaseExpires = dhcpLeaseExpires;
  }


  public SystemInsightsInterfaceDetails dhcpLeaseObtained(String dhcpLeaseObtained) {
    
    
    
    
    this.dhcpLeaseObtained = dhcpLeaseObtained;
    return this;
  }

   /**
   * Get dhcpLeaseObtained
   * @return dhcpLeaseObtained
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getDhcpLeaseObtained() {
    return dhcpLeaseObtained;
  }


  public void setDhcpLeaseObtained(String dhcpLeaseObtained) {
    
    
    
    this.dhcpLeaseObtained = dhcpLeaseObtained;
  }


  public SystemInsightsInterfaceDetails dhcpServer(String dhcpServer) {
    
    
    
    
    this.dhcpServer = dhcpServer;
    return this;
  }

   /**
   * Get dhcpServer
   * @return dhcpServer
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getDhcpServer() {
    return dhcpServer;
  }


  public void setDhcpServer(String dhcpServer) {
    
    
    
    this.dhcpServer = dhcpServer;
  }


  public SystemInsightsInterfaceDetails dnsDomain(String dnsDomain) {
    
    
    
    
    this.dnsDomain = dnsDomain;
    return this;
  }

   /**
   * Get dnsDomain
   * @return dnsDomain
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getDnsDomain() {
    return dnsDomain;
  }


  public void setDnsDomain(String dnsDomain) {
    
    
    
    this.dnsDomain = dnsDomain;
  }


  public SystemInsightsInterfaceDetails dnsDomainSuffixSearchOrder(String dnsDomainSuffixSearchOrder) {
    
    
    
    
    this.dnsDomainSuffixSearchOrder = dnsDomainSuffixSearchOrder;
    return this;
  }

   /**
   * Get dnsDomainSuffixSearchOrder
   * @return dnsDomainSuffixSearchOrder
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getDnsDomainSuffixSearchOrder() {
    return dnsDomainSuffixSearchOrder;
  }


  public void setDnsDomainSuffixSearchOrder(String dnsDomainSuffixSearchOrder) {
    
    
    
    this.dnsDomainSuffixSearchOrder = dnsDomainSuffixSearchOrder;
  }


  public SystemInsightsInterfaceDetails dnsHostName(String dnsHostName) {
    
    
    
    
    this.dnsHostName = dnsHostName;
    return this;
  }

   /**
   * Get dnsHostName
   * @return dnsHostName
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getDnsHostName() {
    return dnsHostName;
  }


  public void setDnsHostName(String dnsHostName) {
    
    
    
    this.dnsHostName = dnsHostName;
  }


  public SystemInsightsInterfaceDetails dnsServerSearchOrder(String dnsServerSearchOrder) {
    
    
    
    
    this.dnsServerSearchOrder = dnsServerSearchOrder;
    return this;
  }

   /**
   * Get dnsServerSearchOrder
   * @return dnsServerSearchOrder
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getDnsServerSearchOrder() {
    return dnsServerSearchOrder;
  }


  public void setDnsServerSearchOrder(String dnsServerSearchOrder) {
    
    
    
    this.dnsServerSearchOrder = dnsServerSearchOrder;
  }


  public SystemInsightsInterfaceDetails enabled(Integer enabled) {
    
    
    
    
    this.enabled = enabled;
    return this;
  }

   /**
   * Get enabled
   * @return enabled
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Integer getEnabled() {
    return enabled;
  }


  public void setEnabled(Integer enabled) {
    
    
    
    this.enabled = enabled;
  }


  public SystemInsightsInterfaceDetails flags(Integer flags) {
    
    
    
    
    this.flags = flags;
    return this;
  }

   /**
   * Get flags
   * @return flags
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Integer getFlags() {
    return flags;
  }


  public void setFlags(Integer flags) {
    
    
    
    this.flags = flags;
  }


  public SystemInsightsInterfaceDetails friendlyName(String friendlyName) {
    
    
    
    
    this.friendlyName = friendlyName;
    return this;
  }

   /**
   * Get friendlyName
   * @return friendlyName
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getFriendlyName() {
    return friendlyName;
  }


  public void setFriendlyName(String friendlyName) {
    
    
    
    this.friendlyName = friendlyName;
  }


  public SystemInsightsInterfaceDetails ibytes(String ibytes) {
    
    
    
    
    this.ibytes = ibytes;
    return this;
  }

   /**
   * Get ibytes
   * @return ibytes
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getIbytes() {
    return ibytes;
  }


  public void setIbytes(String ibytes) {
    
    
    
    this.ibytes = ibytes;
  }


  public SystemInsightsInterfaceDetails idrops(String idrops) {
    
    
    
    
    this.idrops = idrops;
    return this;
  }

   /**
   * Get idrops
   * @return idrops
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getIdrops() {
    return idrops;
  }


  public void setIdrops(String idrops) {
    
    
    
    this.idrops = idrops;
  }


  public SystemInsightsInterfaceDetails ierrors(String ierrors) {
    
    
    
    
    this.ierrors = ierrors;
    return this;
  }

   /**
   * Get ierrors
   * @return ierrors
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getIerrors() {
    return ierrors;
  }


  public void setIerrors(String ierrors) {
    
    
    
    this.ierrors = ierrors;
  }


  public SystemInsightsInterfaceDetails _interface(String _interface) {
    
    
    
    
    this._interface = _interface;
    return this;
  }

   /**
   * Get _interface
   * @return _interface
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getInterface() {
    return _interface;
  }


  public void setInterface(String _interface) {
    
    
    
    this._interface = _interface;
  }


  public SystemInsightsInterfaceDetails ipackets(String ipackets) {
    
    
    
    
    this.ipackets = ipackets;
    return this;
  }

   /**
   * Get ipackets
   * @return ipackets
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getIpackets() {
    return ipackets;
  }


  public void setIpackets(String ipackets) {
    
    
    
    this.ipackets = ipackets;
  }


  public SystemInsightsInterfaceDetails lastChange(String lastChange) {
    
    
    
    
    this.lastChange = lastChange;
    return this;
  }

   /**
   * Get lastChange
   * @return lastChange
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getLastChange() {
    return lastChange;
  }


  public void setLastChange(String lastChange) {
    
    
    
    this.lastChange = lastChange;
  }


  public SystemInsightsInterfaceDetails linkSpeed(String linkSpeed) {
    
    
    
    
    this.linkSpeed = linkSpeed;
    return this;
  }

   /**
   * Get linkSpeed
   * @return linkSpeed
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getLinkSpeed() {
    return linkSpeed;
  }


  public void setLinkSpeed(String linkSpeed) {
    
    
    
    this.linkSpeed = linkSpeed;
  }


  public SystemInsightsInterfaceDetails mac(String mac) {
    
    
    
    
    this.mac = mac;
    return this;
  }

   /**
   * Get mac
   * @return mac
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getMac() {
    return mac;
  }


  public void setMac(String mac) {
    
    
    
    this.mac = mac;
  }


  public SystemInsightsInterfaceDetails manufacturer(String manufacturer) {
    
    
    
    
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


  public SystemInsightsInterfaceDetails metric(Integer metric) {
    
    
    
    
    this.metric = metric;
    return this;
  }

   /**
   * Get metric
   * @return metric
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Integer getMetric() {
    return metric;
  }


  public void setMetric(Integer metric) {
    
    
    
    this.metric = metric;
  }


  public SystemInsightsInterfaceDetails mtu(Integer mtu) {
    
    
    
    
    this.mtu = mtu;
    return this;
  }

   /**
   * Get mtu
   * @return mtu
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Integer getMtu() {
    return mtu;
  }


  public void setMtu(Integer mtu) {
    
    
    
    this.mtu = mtu;
  }


  public SystemInsightsInterfaceDetails obytes(String obytes) {
    
    
    
    
    this.obytes = obytes;
    return this;
  }

   /**
   * Get obytes
   * @return obytes
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getObytes() {
    return obytes;
  }


  public void setObytes(String obytes) {
    
    
    
    this.obytes = obytes;
  }


  public SystemInsightsInterfaceDetails odrops(String odrops) {
    
    
    
    
    this.odrops = odrops;
    return this;
  }

   /**
   * Get odrops
   * @return odrops
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getOdrops() {
    return odrops;
  }


  public void setOdrops(String odrops) {
    
    
    
    this.odrops = odrops;
  }


  public SystemInsightsInterfaceDetails oerrors(String oerrors) {
    
    
    
    
    this.oerrors = oerrors;
    return this;
  }

   /**
   * Get oerrors
   * @return oerrors
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getOerrors() {
    return oerrors;
  }


  public void setOerrors(String oerrors) {
    
    
    
    this.oerrors = oerrors;
  }


  public SystemInsightsInterfaceDetails opackets(String opackets) {
    
    
    
    
    this.opackets = opackets;
    return this;
  }

   /**
   * Get opackets
   * @return opackets
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getOpackets() {
    return opackets;
  }


  public void setOpackets(String opackets) {
    
    
    
    this.opackets = opackets;
  }


  public SystemInsightsInterfaceDetails pciSlot(String pciSlot) {
    
    
    
    
    this.pciSlot = pciSlot;
    return this;
  }

   /**
   * Get pciSlot
   * @return pciSlot
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getPciSlot() {
    return pciSlot;
  }


  public void setPciSlot(String pciSlot) {
    
    
    
    this.pciSlot = pciSlot;
  }


  public SystemInsightsInterfaceDetails physicalAdapter(Integer physicalAdapter) {
    
    
    
    
    this.physicalAdapter = physicalAdapter;
    return this;
  }

   /**
   * Get physicalAdapter
   * @return physicalAdapter
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Integer getPhysicalAdapter() {
    return physicalAdapter;
  }


  public void setPhysicalAdapter(Integer physicalAdapter) {
    
    
    
    this.physicalAdapter = physicalAdapter;
  }


  public SystemInsightsInterfaceDetails service(String service) {
    
    
    
    
    this.service = service;
    return this;
  }

   /**
   * Get service
   * @return service
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getService() {
    return service;
  }


  public void setService(String service) {
    
    
    
    this.service = service;
  }


  public SystemInsightsInterfaceDetails speed(Integer speed) {
    
    
    
    
    this.speed = speed;
    return this;
  }

   /**
   * Get speed
   * @return speed
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Integer getSpeed() {
    return speed;
  }


  public void setSpeed(Integer speed) {
    
    
    
    this.speed = speed;
  }


  public SystemInsightsInterfaceDetails systemId(String systemId) {
    
    
    
    
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


  public SystemInsightsInterfaceDetails type(Integer type) {
    
    
    
    
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Integer getType() {
    return type;
  }


  public void setType(Integer type) {
    
    
    
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
   * @return the SystemInsightsInterfaceDetails instance itself
   */
  public SystemInsightsInterfaceDetails putAdditionalProperty(String key, Object value) {
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
    SystemInsightsInterfaceDetails systemInsightsInterfaceDetails = (SystemInsightsInterfaceDetails) o;
    return Objects.equals(this.description, systemInsightsInterfaceDetails.description) &&
        Objects.equals(this.collisions, systemInsightsInterfaceDetails.collisions) &&
        Objects.equals(this.connectionId, systemInsightsInterfaceDetails.connectionId) &&
        Objects.equals(this.connectionStatus, systemInsightsInterfaceDetails.connectionStatus) &&
        Objects.equals(this.dhcpEnabled, systemInsightsInterfaceDetails.dhcpEnabled) &&
        Objects.equals(this.dhcpLeaseExpires, systemInsightsInterfaceDetails.dhcpLeaseExpires) &&
        Objects.equals(this.dhcpLeaseObtained, systemInsightsInterfaceDetails.dhcpLeaseObtained) &&
        Objects.equals(this.dhcpServer, systemInsightsInterfaceDetails.dhcpServer) &&
        Objects.equals(this.dnsDomain, systemInsightsInterfaceDetails.dnsDomain) &&
        Objects.equals(this.dnsDomainSuffixSearchOrder, systemInsightsInterfaceDetails.dnsDomainSuffixSearchOrder) &&
        Objects.equals(this.dnsHostName, systemInsightsInterfaceDetails.dnsHostName) &&
        Objects.equals(this.dnsServerSearchOrder, systemInsightsInterfaceDetails.dnsServerSearchOrder) &&
        Objects.equals(this.enabled, systemInsightsInterfaceDetails.enabled) &&
        Objects.equals(this.flags, systemInsightsInterfaceDetails.flags) &&
        Objects.equals(this.friendlyName, systemInsightsInterfaceDetails.friendlyName) &&
        Objects.equals(this.ibytes, systemInsightsInterfaceDetails.ibytes) &&
        Objects.equals(this.idrops, systemInsightsInterfaceDetails.idrops) &&
        Objects.equals(this.ierrors, systemInsightsInterfaceDetails.ierrors) &&
        Objects.equals(this._interface, systemInsightsInterfaceDetails._interface) &&
        Objects.equals(this.ipackets, systemInsightsInterfaceDetails.ipackets) &&
        Objects.equals(this.lastChange, systemInsightsInterfaceDetails.lastChange) &&
        Objects.equals(this.linkSpeed, systemInsightsInterfaceDetails.linkSpeed) &&
        Objects.equals(this.mac, systemInsightsInterfaceDetails.mac) &&
        Objects.equals(this.manufacturer, systemInsightsInterfaceDetails.manufacturer) &&
        Objects.equals(this.metric, systemInsightsInterfaceDetails.metric) &&
        Objects.equals(this.mtu, systemInsightsInterfaceDetails.mtu) &&
        Objects.equals(this.obytes, systemInsightsInterfaceDetails.obytes) &&
        Objects.equals(this.odrops, systemInsightsInterfaceDetails.odrops) &&
        Objects.equals(this.oerrors, systemInsightsInterfaceDetails.oerrors) &&
        Objects.equals(this.opackets, systemInsightsInterfaceDetails.opackets) &&
        Objects.equals(this.pciSlot, systemInsightsInterfaceDetails.pciSlot) &&
        Objects.equals(this.physicalAdapter, systemInsightsInterfaceDetails.physicalAdapter) &&
        Objects.equals(this.service, systemInsightsInterfaceDetails.service) &&
        Objects.equals(this.speed, systemInsightsInterfaceDetails.speed) &&
        Objects.equals(this.systemId, systemInsightsInterfaceDetails.systemId) &&
        Objects.equals(this.type, systemInsightsInterfaceDetails.type)&&
        Objects.equals(this.additionalProperties, systemInsightsInterfaceDetails.additionalProperties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(description, collisions, connectionId, connectionStatus, dhcpEnabled, dhcpLeaseExpires, dhcpLeaseObtained, dhcpServer, dnsDomain, dnsDomainSuffixSearchOrder, dnsHostName, dnsServerSearchOrder, enabled, flags, friendlyName, ibytes, idrops, ierrors, _interface, ipackets, lastChange, linkSpeed, mac, manufacturer, metric, mtu, obytes, odrops, oerrors, opackets, pciSlot, physicalAdapter, service, speed, systemId, type, additionalProperties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SystemInsightsInterfaceDetails {\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    collisions: ").append(toIndentedString(collisions)).append("\n");
    sb.append("    connectionId: ").append(toIndentedString(connectionId)).append("\n");
    sb.append("    connectionStatus: ").append(toIndentedString(connectionStatus)).append("\n");
    sb.append("    dhcpEnabled: ").append(toIndentedString(dhcpEnabled)).append("\n");
    sb.append("    dhcpLeaseExpires: ").append(toIndentedString(dhcpLeaseExpires)).append("\n");
    sb.append("    dhcpLeaseObtained: ").append(toIndentedString(dhcpLeaseObtained)).append("\n");
    sb.append("    dhcpServer: ").append(toIndentedString(dhcpServer)).append("\n");
    sb.append("    dnsDomain: ").append(toIndentedString(dnsDomain)).append("\n");
    sb.append("    dnsDomainSuffixSearchOrder: ").append(toIndentedString(dnsDomainSuffixSearchOrder)).append("\n");
    sb.append("    dnsHostName: ").append(toIndentedString(dnsHostName)).append("\n");
    sb.append("    dnsServerSearchOrder: ").append(toIndentedString(dnsServerSearchOrder)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    flags: ").append(toIndentedString(flags)).append("\n");
    sb.append("    friendlyName: ").append(toIndentedString(friendlyName)).append("\n");
    sb.append("    ibytes: ").append(toIndentedString(ibytes)).append("\n");
    sb.append("    idrops: ").append(toIndentedString(idrops)).append("\n");
    sb.append("    ierrors: ").append(toIndentedString(ierrors)).append("\n");
    sb.append("    _interface: ").append(toIndentedString(_interface)).append("\n");
    sb.append("    ipackets: ").append(toIndentedString(ipackets)).append("\n");
    sb.append("    lastChange: ").append(toIndentedString(lastChange)).append("\n");
    sb.append("    linkSpeed: ").append(toIndentedString(linkSpeed)).append("\n");
    sb.append("    mac: ").append(toIndentedString(mac)).append("\n");
    sb.append("    manufacturer: ").append(toIndentedString(manufacturer)).append("\n");
    sb.append("    metric: ").append(toIndentedString(metric)).append("\n");
    sb.append("    mtu: ").append(toIndentedString(mtu)).append("\n");
    sb.append("    obytes: ").append(toIndentedString(obytes)).append("\n");
    sb.append("    odrops: ").append(toIndentedString(odrops)).append("\n");
    sb.append("    oerrors: ").append(toIndentedString(oerrors)).append("\n");
    sb.append("    opackets: ").append(toIndentedString(opackets)).append("\n");
    sb.append("    pciSlot: ").append(toIndentedString(pciSlot)).append("\n");
    sb.append("    physicalAdapter: ").append(toIndentedString(physicalAdapter)).append("\n");
    sb.append("    service: ").append(toIndentedString(service)).append("\n");
    sb.append("    speed: ").append(toIndentedString(speed)).append("\n");
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
    openapiFields.add("description");
    openapiFields.add("collisions");
    openapiFields.add("connection_id");
    openapiFields.add("connection_status");
    openapiFields.add("dhcp_enabled");
    openapiFields.add("dhcp_lease_expires");
    openapiFields.add("dhcp_lease_obtained");
    openapiFields.add("dhcp_server");
    openapiFields.add("dns_domain");
    openapiFields.add("dns_domain_suffix_search_order");
    openapiFields.add("dns_host_name");
    openapiFields.add("dns_server_search_order");
    openapiFields.add("enabled");
    openapiFields.add("flags");
    openapiFields.add("friendly_name");
    openapiFields.add("ibytes");
    openapiFields.add("idrops");
    openapiFields.add("ierrors");
    openapiFields.add("interface");
    openapiFields.add("ipackets");
    openapiFields.add("last_change");
    openapiFields.add("link_speed");
    openapiFields.add("mac");
    openapiFields.add("manufacturer");
    openapiFields.add("metric");
    openapiFields.add("mtu");
    openapiFields.add("obytes");
    openapiFields.add("odrops");
    openapiFields.add("oerrors");
    openapiFields.add("opackets");
    openapiFields.add("pci_slot");
    openapiFields.add("physical_adapter");
    openapiFields.add("service");
    openapiFields.add("speed");
    openapiFields.add("system_id");
    openapiFields.add("type");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

 /**
  * Validates the JSON Object and throws an exception if issues found
  *
  * @param jsonObj JSON Object
  * @throws IOException if the JSON Object is invalid with respect to SystemInsightsInterfaceDetails
  */
  public static void validateJsonObject(JsonObject jsonObj) throws IOException {
      if (jsonObj == null) {
        if (!SystemInsightsInterfaceDetails.openapiRequiredFields.isEmpty()) { // has required fields but JSON object is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in SystemInsightsInterfaceDetails is not found in the empty JSON string", SystemInsightsInterfaceDetails.openapiRequiredFields.toString()));
        }
      }
      if ((jsonObj.get("description") != null && !jsonObj.get("description").isJsonNull()) && !jsonObj.get("description").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `description` to be a primitive type in the JSON string but got `%s`", jsonObj.get("description").toString()));
      }
      if ((jsonObj.get("collisions") != null && !jsonObj.get("collisions").isJsonNull()) && !jsonObj.get("collisions").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `collisions` to be a primitive type in the JSON string but got `%s`", jsonObj.get("collisions").toString()));
      }
      if ((jsonObj.get("connection_id") != null && !jsonObj.get("connection_id").isJsonNull()) && !jsonObj.get("connection_id").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `connection_id` to be a primitive type in the JSON string but got `%s`", jsonObj.get("connection_id").toString()));
      }
      if ((jsonObj.get("connection_status") != null && !jsonObj.get("connection_status").isJsonNull()) && !jsonObj.get("connection_status").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `connection_status` to be a primitive type in the JSON string but got `%s`", jsonObj.get("connection_status").toString()));
      }
      if ((jsonObj.get("dhcp_lease_expires") != null && !jsonObj.get("dhcp_lease_expires").isJsonNull()) && !jsonObj.get("dhcp_lease_expires").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `dhcp_lease_expires` to be a primitive type in the JSON string but got `%s`", jsonObj.get("dhcp_lease_expires").toString()));
      }
      if ((jsonObj.get("dhcp_lease_obtained") != null && !jsonObj.get("dhcp_lease_obtained").isJsonNull()) && !jsonObj.get("dhcp_lease_obtained").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `dhcp_lease_obtained` to be a primitive type in the JSON string but got `%s`", jsonObj.get("dhcp_lease_obtained").toString()));
      }
      if ((jsonObj.get("dhcp_server") != null && !jsonObj.get("dhcp_server").isJsonNull()) && !jsonObj.get("dhcp_server").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `dhcp_server` to be a primitive type in the JSON string but got `%s`", jsonObj.get("dhcp_server").toString()));
      }
      if ((jsonObj.get("dns_domain") != null && !jsonObj.get("dns_domain").isJsonNull()) && !jsonObj.get("dns_domain").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `dns_domain` to be a primitive type in the JSON string but got `%s`", jsonObj.get("dns_domain").toString()));
      }
      if ((jsonObj.get("dns_domain_suffix_search_order") != null && !jsonObj.get("dns_domain_suffix_search_order").isJsonNull()) && !jsonObj.get("dns_domain_suffix_search_order").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `dns_domain_suffix_search_order` to be a primitive type in the JSON string but got `%s`", jsonObj.get("dns_domain_suffix_search_order").toString()));
      }
      if ((jsonObj.get("dns_host_name") != null && !jsonObj.get("dns_host_name").isJsonNull()) && !jsonObj.get("dns_host_name").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `dns_host_name` to be a primitive type in the JSON string but got `%s`", jsonObj.get("dns_host_name").toString()));
      }
      if ((jsonObj.get("dns_server_search_order") != null && !jsonObj.get("dns_server_search_order").isJsonNull()) && !jsonObj.get("dns_server_search_order").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `dns_server_search_order` to be a primitive type in the JSON string but got `%s`", jsonObj.get("dns_server_search_order").toString()));
      }
      if ((jsonObj.get("friendly_name") != null && !jsonObj.get("friendly_name").isJsonNull()) && !jsonObj.get("friendly_name").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `friendly_name` to be a primitive type in the JSON string but got `%s`", jsonObj.get("friendly_name").toString()));
      }
      if ((jsonObj.get("ibytes") != null && !jsonObj.get("ibytes").isJsonNull()) && !jsonObj.get("ibytes").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `ibytes` to be a primitive type in the JSON string but got `%s`", jsonObj.get("ibytes").toString()));
      }
      if ((jsonObj.get("idrops") != null && !jsonObj.get("idrops").isJsonNull()) && !jsonObj.get("idrops").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `idrops` to be a primitive type in the JSON string but got `%s`", jsonObj.get("idrops").toString()));
      }
      if ((jsonObj.get("ierrors") != null && !jsonObj.get("ierrors").isJsonNull()) && !jsonObj.get("ierrors").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `ierrors` to be a primitive type in the JSON string but got `%s`", jsonObj.get("ierrors").toString()));
      }
      if ((jsonObj.get("interface") != null && !jsonObj.get("interface").isJsonNull()) && !jsonObj.get("interface").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `interface` to be a primitive type in the JSON string but got `%s`", jsonObj.get("interface").toString()));
      }
      if ((jsonObj.get("ipackets") != null && !jsonObj.get("ipackets").isJsonNull()) && !jsonObj.get("ipackets").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `ipackets` to be a primitive type in the JSON string but got `%s`", jsonObj.get("ipackets").toString()));
      }
      if ((jsonObj.get("last_change") != null && !jsonObj.get("last_change").isJsonNull()) && !jsonObj.get("last_change").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `last_change` to be a primitive type in the JSON string but got `%s`", jsonObj.get("last_change").toString()));
      }
      if ((jsonObj.get("link_speed") != null && !jsonObj.get("link_speed").isJsonNull()) && !jsonObj.get("link_speed").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `link_speed` to be a primitive type in the JSON string but got `%s`", jsonObj.get("link_speed").toString()));
      }
      if ((jsonObj.get("mac") != null && !jsonObj.get("mac").isJsonNull()) && !jsonObj.get("mac").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `mac` to be a primitive type in the JSON string but got `%s`", jsonObj.get("mac").toString()));
      }
      if ((jsonObj.get("manufacturer") != null && !jsonObj.get("manufacturer").isJsonNull()) && !jsonObj.get("manufacturer").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `manufacturer` to be a primitive type in the JSON string but got `%s`", jsonObj.get("manufacturer").toString()));
      }
      if ((jsonObj.get("obytes") != null && !jsonObj.get("obytes").isJsonNull()) && !jsonObj.get("obytes").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `obytes` to be a primitive type in the JSON string but got `%s`", jsonObj.get("obytes").toString()));
      }
      if ((jsonObj.get("odrops") != null && !jsonObj.get("odrops").isJsonNull()) && !jsonObj.get("odrops").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `odrops` to be a primitive type in the JSON string but got `%s`", jsonObj.get("odrops").toString()));
      }
      if ((jsonObj.get("oerrors") != null && !jsonObj.get("oerrors").isJsonNull()) && !jsonObj.get("oerrors").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `oerrors` to be a primitive type in the JSON string but got `%s`", jsonObj.get("oerrors").toString()));
      }
      if ((jsonObj.get("opackets") != null && !jsonObj.get("opackets").isJsonNull()) && !jsonObj.get("opackets").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `opackets` to be a primitive type in the JSON string but got `%s`", jsonObj.get("opackets").toString()));
      }
      if ((jsonObj.get("pci_slot") != null && !jsonObj.get("pci_slot").isJsonNull()) && !jsonObj.get("pci_slot").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `pci_slot` to be a primitive type in the JSON string but got `%s`", jsonObj.get("pci_slot").toString()));
      }
      if ((jsonObj.get("service") != null && !jsonObj.get("service").isJsonNull()) && !jsonObj.get("service").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `service` to be a primitive type in the JSON string but got `%s`", jsonObj.get("service").toString()));
      }
      if ((jsonObj.get("system_id") != null && !jsonObj.get("system_id").isJsonNull()) && !jsonObj.get("system_id").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `system_id` to be a primitive type in the JSON string but got `%s`", jsonObj.get("system_id").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!SystemInsightsInterfaceDetails.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'SystemInsightsInterfaceDetails' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<SystemInsightsInterfaceDetails> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(SystemInsightsInterfaceDetails.class));

       return (TypeAdapter<T>) new TypeAdapter<SystemInsightsInterfaceDetails>() {
           @Override
           public void write(JsonWriter out, SystemInsightsInterfaceDetails value) throws IOException {
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
           public SystemInsightsInterfaceDetails read(JsonReader in) throws IOException {
             JsonObject jsonObj = elementAdapter.read(in).getAsJsonObject();
             validateJsonObject(jsonObj);
             // store additional fields in the deserialized instance
             SystemInsightsInterfaceDetails instance = thisAdapter.fromJsonTree(jsonObj);
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
  * Create an instance of SystemInsightsInterfaceDetails given an JSON string
  *
  * @param jsonString JSON string
  * @return An instance of SystemInsightsInterfaceDetails
  * @throws IOException if the JSON string is invalid with respect to SystemInsightsInterfaceDetails
  */
  public static SystemInsightsInterfaceDetails fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, SystemInsightsInterfaceDetails.class);
  }

 /**
  * Convert an instance of SystemInsightsInterfaceDetails to an JSON string
  *
  * @return JSON string
  */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

