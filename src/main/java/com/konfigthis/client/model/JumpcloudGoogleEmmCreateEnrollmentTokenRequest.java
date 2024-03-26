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
import com.konfigthis.client.model.JumpcloudGoogleEmmAllowPersonalUsage;
import com.konfigthis.client.model.JumpcloudGoogleEmmCreatedWhere;
import com.konfigthis.client.model.JumpcloudGoogleEmmEnrollmentType;
import com.konfigthis.client.model.JumpcloudGoogleEmmProvisioningExtras;
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
 * JumpcloudGoogleEmmCreateEnrollmentTokenRequest
 */@javax.annotation.Generated(value = "Generated by https://konfigthis.com")
public class JumpcloudGoogleEmmCreateEnrollmentTokenRequest {
  public static final String SERIALIZED_NAME_ALLOW_PERSONAL_USAGE = "allowPersonalUsage";
  @SerializedName(SERIALIZED_NAME_ALLOW_PERSONAL_USAGE)
  private JumpcloudGoogleEmmAllowPersonalUsage allowPersonalUsage = JumpcloudGoogleEmmAllowPersonalUsage.PERSONAL_USAGE_ALLOWED;

  public static final String SERIALIZED_NAME_CREATED_WHERE = "createdWhere";
  @SerializedName(SERIALIZED_NAME_CREATED_WHERE)
  private JumpcloudGoogleEmmCreatedWhere createdWhere = JumpcloudGoogleEmmCreatedWhere.API;

  public static final String SERIALIZED_NAME_DISPLAY_NAME = "displayName";
  @SerializedName(SERIALIZED_NAME_DISPLAY_NAME)
  private String displayName;

  public static final String SERIALIZED_NAME_DURATION = "duration";
  @SerializedName(SERIALIZED_NAME_DURATION)
  private String duration;

  public static final String SERIALIZED_NAME_ENROLLMENT_TYPE = "enrollmentType";
  @SerializedName(SERIALIZED_NAME_ENROLLMENT_TYPE)
  private JumpcloudGoogleEmmEnrollmentType enrollmentType = JumpcloudGoogleEmmEnrollmentType.WORK_PROFILE;

  public static final String SERIALIZED_NAME_ENTERPRISE_OBJECT_ID = "enterpriseObjectId";
  @SerializedName(SERIALIZED_NAME_ENTERPRISE_OBJECT_ID)
  private byte[] enterpriseObjectId;

  public static final String SERIALIZED_NAME_ONE_TIME_ONLY = "oneTimeOnly";
  @SerializedName(SERIALIZED_NAME_ONE_TIME_ONLY)
  private Boolean oneTimeOnly;

  public static final String SERIALIZED_NAME_PROVISIONING_EXTRAS = "provisioningExtras";
  @SerializedName(SERIALIZED_NAME_PROVISIONING_EXTRAS)
  private JumpcloudGoogleEmmProvisioningExtras provisioningExtras;

  public static final String SERIALIZED_NAME_USER_OBJECT_ID = "userObjectId";
  @SerializedName(SERIALIZED_NAME_USER_OBJECT_ID)
  private byte[] userObjectId;

  public static final String SERIALIZED_NAME_ZERO_TOUCH = "zeroTouch";
  @SerializedName(SERIALIZED_NAME_ZERO_TOUCH)
  private Boolean zeroTouch;

  public JumpcloudGoogleEmmCreateEnrollmentTokenRequest() {
  }

  public JumpcloudGoogleEmmCreateEnrollmentTokenRequest allowPersonalUsage(JumpcloudGoogleEmmAllowPersonalUsage allowPersonalUsage) {
    
    
    
    
    this.allowPersonalUsage = allowPersonalUsage;
    return this;
  }

   /**
   * Get allowPersonalUsage
   * @return allowPersonalUsage
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public JumpcloudGoogleEmmAllowPersonalUsage getAllowPersonalUsage() {
    return allowPersonalUsage;
  }


  public void setAllowPersonalUsage(JumpcloudGoogleEmmAllowPersonalUsage allowPersonalUsage) {
    
    
    
    this.allowPersonalUsage = allowPersonalUsage;
  }


  public JumpcloudGoogleEmmCreateEnrollmentTokenRequest createdWhere(JumpcloudGoogleEmmCreatedWhere createdWhere) {
    
    
    
    
    this.createdWhere = createdWhere;
    return this;
  }

   /**
   * Get createdWhere
   * @return createdWhere
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public JumpcloudGoogleEmmCreatedWhere getCreatedWhere() {
    return createdWhere;
  }


  public void setCreatedWhere(JumpcloudGoogleEmmCreatedWhere createdWhere) {
    
    
    
    this.createdWhere = createdWhere;
  }


  public JumpcloudGoogleEmmCreateEnrollmentTokenRequest displayName(String displayName) {
    
    
    
    
    this.displayName = displayName;
    return this;
  }

   /**
   * Get displayName
   * @return displayName
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getDisplayName() {
    return displayName;
  }


  public void setDisplayName(String displayName) {
    
    
    
    this.displayName = displayName;
  }


  public JumpcloudGoogleEmmCreateEnrollmentTokenRequest duration(String duration) {
    
    
    
    
    this.duration = duration;
    return this;
  }

   /**
   * Get duration
   * @return duration
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getDuration() {
    return duration;
  }


  public void setDuration(String duration) {
    
    
    
    this.duration = duration;
  }


  public JumpcloudGoogleEmmCreateEnrollmentTokenRequest enrollmentType(JumpcloudGoogleEmmEnrollmentType enrollmentType) {
    
    
    
    
    this.enrollmentType = enrollmentType;
    return this;
  }

   /**
   * Get enrollmentType
   * @return enrollmentType
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public JumpcloudGoogleEmmEnrollmentType getEnrollmentType() {
    return enrollmentType;
  }


  public void setEnrollmentType(JumpcloudGoogleEmmEnrollmentType enrollmentType) {
    
    
    
    this.enrollmentType = enrollmentType;
  }


  public JumpcloudGoogleEmmCreateEnrollmentTokenRequest enterpriseObjectId(byte[] enterpriseObjectId) {
    
    
    
    
    this.enterpriseObjectId = enterpriseObjectId;
    return this;
  }

   /**
   * Get enterpriseObjectId
   * @return enterpriseObjectId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public byte[] getEnterpriseObjectId() {
    return enterpriseObjectId;
  }


  public void setEnterpriseObjectId(byte[] enterpriseObjectId) {
    
    
    
    this.enterpriseObjectId = enterpriseObjectId;
  }


  public JumpcloudGoogleEmmCreateEnrollmentTokenRequest oneTimeOnly(Boolean oneTimeOnly) {
    
    
    
    
    this.oneTimeOnly = oneTimeOnly;
    return this;
  }

   /**
   * Get oneTimeOnly
   * @return oneTimeOnly
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Boolean getOneTimeOnly() {
    return oneTimeOnly;
  }


  public void setOneTimeOnly(Boolean oneTimeOnly) {
    
    
    
    this.oneTimeOnly = oneTimeOnly;
  }


  public JumpcloudGoogleEmmCreateEnrollmentTokenRequest provisioningExtras(JumpcloudGoogleEmmProvisioningExtras provisioningExtras) {
    
    
    
    
    this.provisioningExtras = provisioningExtras;
    return this;
  }

   /**
   * Get provisioningExtras
   * @return provisioningExtras
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public JumpcloudGoogleEmmProvisioningExtras getProvisioningExtras() {
    return provisioningExtras;
  }


  public void setProvisioningExtras(JumpcloudGoogleEmmProvisioningExtras provisioningExtras) {
    
    
    
    this.provisioningExtras = provisioningExtras;
  }


  public JumpcloudGoogleEmmCreateEnrollmentTokenRequest userObjectId(byte[] userObjectId) {
    
    
    
    
    this.userObjectId = userObjectId;
    return this;
  }

   /**
   * Get userObjectId
   * @return userObjectId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public byte[] getUserObjectId() {
    return userObjectId;
  }


  public void setUserObjectId(byte[] userObjectId) {
    
    
    
    this.userObjectId = userObjectId;
  }


  public JumpcloudGoogleEmmCreateEnrollmentTokenRequest zeroTouch(Boolean zeroTouch) {
    
    
    
    
    this.zeroTouch = zeroTouch;
    return this;
  }

   /**
   * Get zeroTouch
   * @return zeroTouch
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Boolean getZeroTouch() {
    return zeroTouch;
  }


  public void setZeroTouch(Boolean zeroTouch) {
    
    
    
    this.zeroTouch = zeroTouch;
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
   * @return the JumpcloudGoogleEmmCreateEnrollmentTokenRequest instance itself
   */
  public JumpcloudGoogleEmmCreateEnrollmentTokenRequest putAdditionalProperty(String key, Object value) {
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
    JumpcloudGoogleEmmCreateEnrollmentTokenRequest jumpcloudGoogleEmmCreateEnrollmentTokenRequest = (JumpcloudGoogleEmmCreateEnrollmentTokenRequest) o;
    return Objects.equals(this.allowPersonalUsage, jumpcloudGoogleEmmCreateEnrollmentTokenRequest.allowPersonalUsage) &&
        Objects.equals(this.createdWhere, jumpcloudGoogleEmmCreateEnrollmentTokenRequest.createdWhere) &&
        Objects.equals(this.displayName, jumpcloudGoogleEmmCreateEnrollmentTokenRequest.displayName) &&
        Objects.equals(this.duration, jumpcloudGoogleEmmCreateEnrollmentTokenRequest.duration) &&
        Objects.equals(this.enrollmentType, jumpcloudGoogleEmmCreateEnrollmentTokenRequest.enrollmentType) &&
        Arrays.equals(this.enterpriseObjectId, jumpcloudGoogleEmmCreateEnrollmentTokenRequest.enterpriseObjectId) &&
        Objects.equals(this.oneTimeOnly, jumpcloudGoogleEmmCreateEnrollmentTokenRequest.oneTimeOnly) &&
        Objects.equals(this.provisioningExtras, jumpcloudGoogleEmmCreateEnrollmentTokenRequest.provisioningExtras) &&
        Arrays.equals(this.userObjectId, jumpcloudGoogleEmmCreateEnrollmentTokenRequest.userObjectId) &&
        Objects.equals(this.zeroTouch, jumpcloudGoogleEmmCreateEnrollmentTokenRequest.zeroTouch)&&
        Objects.equals(this.additionalProperties, jumpcloudGoogleEmmCreateEnrollmentTokenRequest.additionalProperties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(allowPersonalUsage, createdWhere, displayName, duration, enrollmentType, Arrays.hashCode(enterpriseObjectId), oneTimeOnly, provisioningExtras, Arrays.hashCode(userObjectId), zeroTouch, additionalProperties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class JumpcloudGoogleEmmCreateEnrollmentTokenRequest {\n");
    sb.append("    allowPersonalUsage: ").append(toIndentedString(allowPersonalUsage)).append("\n");
    sb.append("    createdWhere: ").append(toIndentedString(createdWhere)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
    sb.append("    enrollmentType: ").append(toIndentedString(enrollmentType)).append("\n");
    sb.append("    enterpriseObjectId: ").append(toIndentedString(enterpriseObjectId)).append("\n");
    sb.append("    oneTimeOnly: ").append(toIndentedString(oneTimeOnly)).append("\n");
    sb.append("    provisioningExtras: ").append(toIndentedString(provisioningExtras)).append("\n");
    sb.append("    userObjectId: ").append(toIndentedString(userObjectId)).append("\n");
    sb.append("    zeroTouch: ").append(toIndentedString(zeroTouch)).append("\n");
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
    openapiFields.add("allowPersonalUsage");
    openapiFields.add("createdWhere");
    openapiFields.add("displayName");
    openapiFields.add("duration");
    openapiFields.add("enrollmentType");
    openapiFields.add("enterpriseObjectId");
    openapiFields.add("oneTimeOnly");
    openapiFields.add("provisioningExtras");
    openapiFields.add("userObjectId");
    openapiFields.add("zeroTouch");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

 /**
  * Validates the JSON Object and throws an exception if issues found
  *
  * @param jsonObj JSON Object
  * @throws IOException if the JSON Object is invalid with respect to JumpcloudGoogleEmmCreateEnrollmentTokenRequest
  */
  public static void validateJsonObject(JsonObject jsonObj) throws IOException {
      if (jsonObj == null) {
        if (!JumpcloudGoogleEmmCreateEnrollmentTokenRequest.openapiRequiredFields.isEmpty()) { // has required fields but JSON object is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in JumpcloudGoogleEmmCreateEnrollmentTokenRequest is not found in the empty JSON string", JumpcloudGoogleEmmCreateEnrollmentTokenRequest.openapiRequiredFields.toString()));
        }
      }
      if ((jsonObj.get("displayName") != null && !jsonObj.get("displayName").isJsonNull()) && !jsonObj.get("displayName").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `displayName` to be a primitive type in the JSON string but got `%s`", jsonObj.get("displayName").toString()));
      }
      if ((jsonObj.get("duration") != null && !jsonObj.get("duration").isJsonNull()) && !jsonObj.get("duration").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `duration` to be a primitive type in the JSON string but got `%s`", jsonObj.get("duration").toString()));
      }
      // validate the optional field `provisioningExtras`
      if (jsonObj.get("provisioningExtras") != null && !jsonObj.get("provisioningExtras").isJsonNull()) {
        JumpcloudGoogleEmmProvisioningExtras.validateJsonObject(jsonObj.getAsJsonObject("provisioningExtras"));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!JumpcloudGoogleEmmCreateEnrollmentTokenRequest.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'JumpcloudGoogleEmmCreateEnrollmentTokenRequest' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<JumpcloudGoogleEmmCreateEnrollmentTokenRequest> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(JumpcloudGoogleEmmCreateEnrollmentTokenRequest.class));

       return (TypeAdapter<T>) new TypeAdapter<JumpcloudGoogleEmmCreateEnrollmentTokenRequest>() {
           @Override
           public void write(JsonWriter out, JumpcloudGoogleEmmCreateEnrollmentTokenRequest value) throws IOException {
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
           public JumpcloudGoogleEmmCreateEnrollmentTokenRequest read(JsonReader in) throws IOException {
             JsonObject jsonObj = elementAdapter.read(in).getAsJsonObject();
             validateJsonObject(jsonObj);
             // store additional fields in the deserialized instance
             JumpcloudGoogleEmmCreateEnrollmentTokenRequest instance = thisAdapter.fromJsonTree(jsonObj);
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
  * Create an instance of JumpcloudGoogleEmmCreateEnrollmentTokenRequest given an JSON string
  *
  * @param jsonString JSON string
  * @return An instance of JumpcloudGoogleEmmCreateEnrollmentTokenRequest
  * @throws IOException if the JSON string is invalid with respect to JumpcloudGoogleEmmCreateEnrollmentTokenRequest
  */
  public static JumpcloudGoogleEmmCreateEnrollmentTokenRequest fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, JumpcloudGoogleEmmCreateEnrollmentTokenRequest.class);
  }

 /**
  * Convert an instance of JumpcloudGoogleEmmCreateEnrollmentTokenRequest to an JSON string
  *
  * @return JSON string
  */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

