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
 * SystemInsightsCertificates
 */@javax.annotation.Generated(value = "Generated by https://konfigthis.com")
public class SystemInsightsCertificates {
  public static final String SERIALIZED_NAME_AUTHORITY_KEY_ID = "authority_key_id";
  @SerializedName(SERIALIZED_NAME_AUTHORITY_KEY_ID)
  private String authorityKeyId;

  public static final String SERIALIZED_NAME_CA = "ca";
  @SerializedName(SERIALIZED_NAME_CA)
  private Integer ca;

  public static final String SERIALIZED_NAME_COMMON_NAME = "common_name";
  @SerializedName(SERIALIZED_NAME_COMMON_NAME)
  private String commonName;

  public static final String SERIALIZED_NAME_ISSUER = "issuer";
  @SerializedName(SERIALIZED_NAME_ISSUER)
  private String issuer;

  public static final String SERIALIZED_NAME_KEY_ALGORITHM = "key_algorithm";
  @SerializedName(SERIALIZED_NAME_KEY_ALGORITHM)
  private String keyAlgorithm;

  public static final String SERIALIZED_NAME_KEY_STRENGTH = "key_strength";
  @SerializedName(SERIALIZED_NAME_KEY_STRENGTH)
  private String keyStrength;

  public static final String SERIALIZED_NAME_KEY_USAGE = "key_usage";
  @SerializedName(SERIALIZED_NAME_KEY_USAGE)
  private String keyUsage;

  public static final String SERIALIZED_NAME_NOT_VALID_AFTER = "not_valid_after";
  @SerializedName(SERIALIZED_NAME_NOT_VALID_AFTER)
  private String notValidAfter;

  public static final String SERIALIZED_NAME_NOT_VALID_BEFORE = "not_valid_before";
  @SerializedName(SERIALIZED_NAME_NOT_VALID_BEFORE)
  private String notValidBefore;

  public static final String SERIALIZED_NAME_PATH = "path";
  @SerializedName(SERIALIZED_NAME_PATH)
  private String path;

  public static final String SERIALIZED_NAME_SELF_SIGNED = "self_signed";
  @SerializedName(SERIALIZED_NAME_SELF_SIGNED)
  private Integer selfSigned;

  public static final String SERIALIZED_NAME_SERIAL = "serial";
  @SerializedName(SERIALIZED_NAME_SERIAL)
  private String serial;

  public static final String SERIALIZED_NAME_SHA1 = "sha1";
  @SerializedName(SERIALIZED_NAME_SHA1)
  private String sha1;

  public static final String SERIALIZED_NAME_SID = "sid";
  @SerializedName(SERIALIZED_NAME_SID)
  private String sid;

  public static final String SERIALIZED_NAME_SIGNING_ALGORITHM = "signing_algorithm";
  @SerializedName(SERIALIZED_NAME_SIGNING_ALGORITHM)
  private String signingAlgorithm;

  public static final String SERIALIZED_NAME_STORE = "store";
  @SerializedName(SERIALIZED_NAME_STORE)
  private String store;

  public static final String SERIALIZED_NAME_STORE_ID = "store_id";
  @SerializedName(SERIALIZED_NAME_STORE_ID)
  private String storeId;

  public static final String SERIALIZED_NAME_STORE_LOCATION = "store_location";
  @SerializedName(SERIALIZED_NAME_STORE_LOCATION)
  private String storeLocation;

  public static final String SERIALIZED_NAME_SUBJECT = "subject";
  @SerializedName(SERIALIZED_NAME_SUBJECT)
  private String subject;

  public static final String SERIALIZED_NAME_SUBJECT_KEY_ID = "subject_key_id";
  @SerializedName(SERIALIZED_NAME_SUBJECT_KEY_ID)
  private String subjectKeyId;

  public static final String SERIALIZED_NAME_SYSTEM_ID = "system_id";
  @SerializedName(SERIALIZED_NAME_SYSTEM_ID)
  private String systemId;

  public static final String SERIALIZED_NAME_USERNAME = "username";
  @SerializedName(SERIALIZED_NAME_USERNAME)
  private String username;

  public SystemInsightsCertificates() {
  }

  public SystemInsightsCertificates authorityKeyId(String authorityKeyId) {
    
    
    
    
    this.authorityKeyId = authorityKeyId;
    return this;
  }

   /**
   * Get authorityKeyId
   * @return authorityKeyId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getAuthorityKeyId() {
    return authorityKeyId;
  }


  public void setAuthorityKeyId(String authorityKeyId) {
    
    
    
    this.authorityKeyId = authorityKeyId;
  }


  public SystemInsightsCertificates ca(Integer ca) {
    
    
    
    
    this.ca = ca;
    return this;
  }

   /**
   * Get ca
   * @return ca
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Integer getCa() {
    return ca;
  }


  public void setCa(Integer ca) {
    
    
    
    this.ca = ca;
  }


  public SystemInsightsCertificates commonName(String commonName) {
    
    
    
    
    this.commonName = commonName;
    return this;
  }

   /**
   * Get commonName
   * @return commonName
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getCommonName() {
    return commonName;
  }


  public void setCommonName(String commonName) {
    
    
    
    this.commonName = commonName;
  }


  public SystemInsightsCertificates issuer(String issuer) {
    
    
    
    
    this.issuer = issuer;
    return this;
  }

   /**
   * Get issuer
   * @return issuer
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getIssuer() {
    return issuer;
  }


  public void setIssuer(String issuer) {
    
    
    
    this.issuer = issuer;
  }


  public SystemInsightsCertificates keyAlgorithm(String keyAlgorithm) {
    
    
    
    
    this.keyAlgorithm = keyAlgorithm;
    return this;
  }

   /**
   * Get keyAlgorithm
   * @return keyAlgorithm
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getKeyAlgorithm() {
    return keyAlgorithm;
  }


  public void setKeyAlgorithm(String keyAlgorithm) {
    
    
    
    this.keyAlgorithm = keyAlgorithm;
  }


  public SystemInsightsCertificates keyStrength(String keyStrength) {
    
    
    
    
    this.keyStrength = keyStrength;
    return this;
  }

   /**
   * Get keyStrength
   * @return keyStrength
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getKeyStrength() {
    return keyStrength;
  }


  public void setKeyStrength(String keyStrength) {
    
    
    
    this.keyStrength = keyStrength;
  }


  public SystemInsightsCertificates keyUsage(String keyUsage) {
    
    
    
    
    this.keyUsage = keyUsage;
    return this;
  }

   /**
   * Get keyUsage
   * @return keyUsage
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getKeyUsage() {
    return keyUsage;
  }


  public void setKeyUsage(String keyUsage) {
    
    
    
    this.keyUsage = keyUsage;
  }


  public SystemInsightsCertificates notValidAfter(String notValidAfter) {
    
    
    
    
    this.notValidAfter = notValidAfter;
    return this;
  }

   /**
   * Get notValidAfter
   * @return notValidAfter
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getNotValidAfter() {
    return notValidAfter;
  }


  public void setNotValidAfter(String notValidAfter) {
    
    
    
    this.notValidAfter = notValidAfter;
  }


  public SystemInsightsCertificates notValidBefore(String notValidBefore) {
    
    
    
    
    this.notValidBefore = notValidBefore;
    return this;
  }

   /**
   * Get notValidBefore
   * @return notValidBefore
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getNotValidBefore() {
    return notValidBefore;
  }


  public void setNotValidBefore(String notValidBefore) {
    
    
    
    this.notValidBefore = notValidBefore;
  }


  public SystemInsightsCertificates path(String path) {
    
    
    
    
    this.path = path;
    return this;
  }

   /**
   * Get path
   * @return path
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getPath() {
    return path;
  }


  public void setPath(String path) {
    
    
    
    this.path = path;
  }


  public SystemInsightsCertificates selfSigned(Integer selfSigned) {
    
    
    
    
    this.selfSigned = selfSigned;
    return this;
  }

   /**
   * Get selfSigned
   * @return selfSigned
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Integer getSelfSigned() {
    return selfSigned;
  }


  public void setSelfSigned(Integer selfSigned) {
    
    
    
    this.selfSigned = selfSigned;
  }


  public SystemInsightsCertificates serial(String serial) {
    
    
    
    
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


  public SystemInsightsCertificates sha1(String sha1) {
    
    
    
    
    this.sha1 = sha1;
    return this;
  }

   /**
   * Get sha1
   * @return sha1
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getSha1() {
    return sha1;
  }


  public void setSha1(String sha1) {
    
    
    
    this.sha1 = sha1;
  }


  public SystemInsightsCertificates sid(String sid) {
    
    
    
    
    this.sid = sid;
    return this;
  }

   /**
   * Get sid
   * @return sid
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getSid() {
    return sid;
  }


  public void setSid(String sid) {
    
    
    
    this.sid = sid;
  }


  public SystemInsightsCertificates signingAlgorithm(String signingAlgorithm) {
    
    
    
    
    this.signingAlgorithm = signingAlgorithm;
    return this;
  }

   /**
   * Get signingAlgorithm
   * @return signingAlgorithm
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getSigningAlgorithm() {
    return signingAlgorithm;
  }


  public void setSigningAlgorithm(String signingAlgorithm) {
    
    
    
    this.signingAlgorithm = signingAlgorithm;
  }


  public SystemInsightsCertificates store(String store) {
    
    
    
    
    this.store = store;
    return this;
  }

   /**
   * Get store
   * @return store
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getStore() {
    return store;
  }


  public void setStore(String store) {
    
    
    
    this.store = store;
  }


  public SystemInsightsCertificates storeId(String storeId) {
    
    
    
    
    this.storeId = storeId;
    return this;
  }

   /**
   * Get storeId
   * @return storeId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getStoreId() {
    return storeId;
  }


  public void setStoreId(String storeId) {
    
    
    
    this.storeId = storeId;
  }


  public SystemInsightsCertificates storeLocation(String storeLocation) {
    
    
    
    
    this.storeLocation = storeLocation;
    return this;
  }

   /**
   * Get storeLocation
   * @return storeLocation
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getStoreLocation() {
    return storeLocation;
  }


  public void setStoreLocation(String storeLocation) {
    
    
    
    this.storeLocation = storeLocation;
  }


  public SystemInsightsCertificates subject(String subject) {
    
    
    
    
    this.subject = subject;
    return this;
  }

   /**
   * Get subject
   * @return subject
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getSubject() {
    return subject;
  }


  public void setSubject(String subject) {
    
    
    
    this.subject = subject;
  }


  public SystemInsightsCertificates subjectKeyId(String subjectKeyId) {
    
    
    
    
    this.subjectKeyId = subjectKeyId;
    return this;
  }

   /**
   * Get subjectKeyId
   * @return subjectKeyId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getSubjectKeyId() {
    return subjectKeyId;
  }


  public void setSubjectKeyId(String subjectKeyId) {
    
    
    
    this.subjectKeyId = subjectKeyId;
  }


  public SystemInsightsCertificates systemId(String systemId) {
    
    
    
    
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


  public SystemInsightsCertificates username(String username) {
    
    
    
    
    this.username = username;
    return this;
  }

   /**
   * Get username
   * @return username
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getUsername() {
    return username;
  }


  public void setUsername(String username) {
    
    
    
    this.username = username;
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
   * @return the SystemInsightsCertificates instance itself
   */
  public SystemInsightsCertificates putAdditionalProperty(String key, Object value) {
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
    SystemInsightsCertificates systemInsightsCertificates = (SystemInsightsCertificates) o;
    return Objects.equals(this.authorityKeyId, systemInsightsCertificates.authorityKeyId) &&
        Objects.equals(this.ca, systemInsightsCertificates.ca) &&
        Objects.equals(this.commonName, systemInsightsCertificates.commonName) &&
        Objects.equals(this.issuer, systemInsightsCertificates.issuer) &&
        Objects.equals(this.keyAlgorithm, systemInsightsCertificates.keyAlgorithm) &&
        Objects.equals(this.keyStrength, systemInsightsCertificates.keyStrength) &&
        Objects.equals(this.keyUsage, systemInsightsCertificates.keyUsage) &&
        Objects.equals(this.notValidAfter, systemInsightsCertificates.notValidAfter) &&
        Objects.equals(this.notValidBefore, systemInsightsCertificates.notValidBefore) &&
        Objects.equals(this.path, systemInsightsCertificates.path) &&
        Objects.equals(this.selfSigned, systemInsightsCertificates.selfSigned) &&
        Objects.equals(this.serial, systemInsightsCertificates.serial) &&
        Objects.equals(this.sha1, systemInsightsCertificates.sha1) &&
        Objects.equals(this.sid, systemInsightsCertificates.sid) &&
        Objects.equals(this.signingAlgorithm, systemInsightsCertificates.signingAlgorithm) &&
        Objects.equals(this.store, systemInsightsCertificates.store) &&
        Objects.equals(this.storeId, systemInsightsCertificates.storeId) &&
        Objects.equals(this.storeLocation, systemInsightsCertificates.storeLocation) &&
        Objects.equals(this.subject, systemInsightsCertificates.subject) &&
        Objects.equals(this.subjectKeyId, systemInsightsCertificates.subjectKeyId) &&
        Objects.equals(this.systemId, systemInsightsCertificates.systemId) &&
        Objects.equals(this.username, systemInsightsCertificates.username)&&
        Objects.equals(this.additionalProperties, systemInsightsCertificates.additionalProperties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authorityKeyId, ca, commonName, issuer, keyAlgorithm, keyStrength, keyUsage, notValidAfter, notValidBefore, path, selfSigned, serial, sha1, sid, signingAlgorithm, store, storeId, storeLocation, subject, subjectKeyId, systemId, username, additionalProperties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SystemInsightsCertificates {\n");
    sb.append("    authorityKeyId: ").append(toIndentedString(authorityKeyId)).append("\n");
    sb.append("    ca: ").append(toIndentedString(ca)).append("\n");
    sb.append("    commonName: ").append(toIndentedString(commonName)).append("\n");
    sb.append("    issuer: ").append(toIndentedString(issuer)).append("\n");
    sb.append("    keyAlgorithm: ").append(toIndentedString(keyAlgorithm)).append("\n");
    sb.append("    keyStrength: ").append(toIndentedString(keyStrength)).append("\n");
    sb.append("    keyUsage: ").append(toIndentedString(keyUsage)).append("\n");
    sb.append("    notValidAfter: ").append(toIndentedString(notValidAfter)).append("\n");
    sb.append("    notValidBefore: ").append(toIndentedString(notValidBefore)).append("\n");
    sb.append("    path: ").append(toIndentedString(path)).append("\n");
    sb.append("    selfSigned: ").append(toIndentedString(selfSigned)).append("\n");
    sb.append("    serial: ").append(toIndentedString(serial)).append("\n");
    sb.append("    sha1: ").append(toIndentedString(sha1)).append("\n");
    sb.append("    sid: ").append(toIndentedString(sid)).append("\n");
    sb.append("    signingAlgorithm: ").append(toIndentedString(signingAlgorithm)).append("\n");
    sb.append("    store: ").append(toIndentedString(store)).append("\n");
    sb.append("    storeId: ").append(toIndentedString(storeId)).append("\n");
    sb.append("    storeLocation: ").append(toIndentedString(storeLocation)).append("\n");
    sb.append("    subject: ").append(toIndentedString(subject)).append("\n");
    sb.append("    subjectKeyId: ").append(toIndentedString(subjectKeyId)).append("\n");
    sb.append("    systemId: ").append(toIndentedString(systemId)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
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
    openapiFields.add("authority_key_id");
    openapiFields.add("ca");
    openapiFields.add("common_name");
    openapiFields.add("issuer");
    openapiFields.add("key_algorithm");
    openapiFields.add("key_strength");
    openapiFields.add("key_usage");
    openapiFields.add("not_valid_after");
    openapiFields.add("not_valid_before");
    openapiFields.add("path");
    openapiFields.add("self_signed");
    openapiFields.add("serial");
    openapiFields.add("sha1");
    openapiFields.add("sid");
    openapiFields.add("signing_algorithm");
    openapiFields.add("store");
    openapiFields.add("store_id");
    openapiFields.add("store_location");
    openapiFields.add("subject");
    openapiFields.add("subject_key_id");
    openapiFields.add("system_id");
    openapiFields.add("username");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

 /**
  * Validates the JSON Object and throws an exception if issues found
  *
  * @param jsonObj JSON Object
  * @throws IOException if the JSON Object is invalid with respect to SystemInsightsCertificates
  */
  public static void validateJsonObject(JsonObject jsonObj) throws IOException {
      if (jsonObj == null) {
        if (!SystemInsightsCertificates.openapiRequiredFields.isEmpty()) { // has required fields but JSON object is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in SystemInsightsCertificates is not found in the empty JSON string", SystemInsightsCertificates.openapiRequiredFields.toString()));
        }
      }
      if ((jsonObj.get("authority_key_id") != null && !jsonObj.get("authority_key_id").isJsonNull()) && !jsonObj.get("authority_key_id").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `authority_key_id` to be a primitive type in the JSON string but got `%s`", jsonObj.get("authority_key_id").toString()));
      }
      if ((jsonObj.get("common_name") != null && !jsonObj.get("common_name").isJsonNull()) && !jsonObj.get("common_name").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `common_name` to be a primitive type in the JSON string but got `%s`", jsonObj.get("common_name").toString()));
      }
      if ((jsonObj.get("issuer") != null && !jsonObj.get("issuer").isJsonNull()) && !jsonObj.get("issuer").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `issuer` to be a primitive type in the JSON string but got `%s`", jsonObj.get("issuer").toString()));
      }
      if ((jsonObj.get("key_algorithm") != null && !jsonObj.get("key_algorithm").isJsonNull()) && !jsonObj.get("key_algorithm").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `key_algorithm` to be a primitive type in the JSON string but got `%s`", jsonObj.get("key_algorithm").toString()));
      }
      if ((jsonObj.get("key_strength") != null && !jsonObj.get("key_strength").isJsonNull()) && !jsonObj.get("key_strength").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `key_strength` to be a primitive type in the JSON string but got `%s`", jsonObj.get("key_strength").toString()));
      }
      if ((jsonObj.get("key_usage") != null && !jsonObj.get("key_usage").isJsonNull()) && !jsonObj.get("key_usage").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `key_usage` to be a primitive type in the JSON string but got `%s`", jsonObj.get("key_usage").toString()));
      }
      if ((jsonObj.get("not_valid_after") != null && !jsonObj.get("not_valid_after").isJsonNull()) && !jsonObj.get("not_valid_after").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `not_valid_after` to be a primitive type in the JSON string but got `%s`", jsonObj.get("not_valid_after").toString()));
      }
      if ((jsonObj.get("not_valid_before") != null && !jsonObj.get("not_valid_before").isJsonNull()) && !jsonObj.get("not_valid_before").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `not_valid_before` to be a primitive type in the JSON string but got `%s`", jsonObj.get("not_valid_before").toString()));
      }
      if ((jsonObj.get("path") != null && !jsonObj.get("path").isJsonNull()) && !jsonObj.get("path").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `path` to be a primitive type in the JSON string but got `%s`", jsonObj.get("path").toString()));
      }
      if ((jsonObj.get("serial") != null && !jsonObj.get("serial").isJsonNull()) && !jsonObj.get("serial").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `serial` to be a primitive type in the JSON string but got `%s`", jsonObj.get("serial").toString()));
      }
      if ((jsonObj.get("sha1") != null && !jsonObj.get("sha1").isJsonNull()) && !jsonObj.get("sha1").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `sha1` to be a primitive type in the JSON string but got `%s`", jsonObj.get("sha1").toString()));
      }
      if ((jsonObj.get("sid") != null && !jsonObj.get("sid").isJsonNull()) && !jsonObj.get("sid").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `sid` to be a primitive type in the JSON string but got `%s`", jsonObj.get("sid").toString()));
      }
      if ((jsonObj.get("signing_algorithm") != null && !jsonObj.get("signing_algorithm").isJsonNull()) && !jsonObj.get("signing_algorithm").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `signing_algorithm` to be a primitive type in the JSON string but got `%s`", jsonObj.get("signing_algorithm").toString()));
      }
      if ((jsonObj.get("store") != null && !jsonObj.get("store").isJsonNull()) && !jsonObj.get("store").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `store` to be a primitive type in the JSON string but got `%s`", jsonObj.get("store").toString()));
      }
      if ((jsonObj.get("store_id") != null && !jsonObj.get("store_id").isJsonNull()) && !jsonObj.get("store_id").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `store_id` to be a primitive type in the JSON string but got `%s`", jsonObj.get("store_id").toString()));
      }
      if ((jsonObj.get("store_location") != null && !jsonObj.get("store_location").isJsonNull()) && !jsonObj.get("store_location").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `store_location` to be a primitive type in the JSON string but got `%s`", jsonObj.get("store_location").toString()));
      }
      if ((jsonObj.get("subject") != null && !jsonObj.get("subject").isJsonNull()) && !jsonObj.get("subject").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `subject` to be a primitive type in the JSON string but got `%s`", jsonObj.get("subject").toString()));
      }
      if ((jsonObj.get("subject_key_id") != null && !jsonObj.get("subject_key_id").isJsonNull()) && !jsonObj.get("subject_key_id").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `subject_key_id` to be a primitive type in the JSON string but got `%s`", jsonObj.get("subject_key_id").toString()));
      }
      if ((jsonObj.get("system_id") != null && !jsonObj.get("system_id").isJsonNull()) && !jsonObj.get("system_id").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `system_id` to be a primitive type in the JSON string but got `%s`", jsonObj.get("system_id").toString()));
      }
      if ((jsonObj.get("username") != null && !jsonObj.get("username").isJsonNull()) && !jsonObj.get("username").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `username` to be a primitive type in the JSON string but got `%s`", jsonObj.get("username").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!SystemInsightsCertificates.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'SystemInsightsCertificates' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<SystemInsightsCertificates> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(SystemInsightsCertificates.class));

       return (TypeAdapter<T>) new TypeAdapter<SystemInsightsCertificates>() {
           @Override
           public void write(JsonWriter out, SystemInsightsCertificates value) throws IOException {
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
           public SystemInsightsCertificates read(JsonReader in) throws IOException {
             JsonObject jsonObj = elementAdapter.read(in).getAsJsonObject();
             validateJsonObject(jsonObj);
             // store additional fields in the deserialized instance
             SystemInsightsCertificates instance = thisAdapter.fromJsonTree(jsonObj);
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
  * Create an instance of SystemInsightsCertificates given an JSON string
  *
  * @param jsonString JSON string
  * @return An instance of SystemInsightsCertificates
  * @throws IOException if the JSON string is invalid with respect to SystemInsightsCertificates
  */
  public static SystemInsightsCertificates fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, SystemInsightsCertificates.class);
  }

 /**
  * Convert an instance of SystemInsightsCertificates to an JSON string
  *
  * @return JSON string
  */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

