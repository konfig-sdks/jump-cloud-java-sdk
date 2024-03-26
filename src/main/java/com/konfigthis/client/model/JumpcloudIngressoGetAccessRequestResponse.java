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
 * JumpcloudIngressoGetAccessRequestResponse
 */@javax.annotation.Generated(value = "Generated by https://konfigthis.com")
public class JumpcloudIngressoGetAccessRequestResponse {
  public static final String SERIALIZED_NAME_OPERATION_ID = "operationId";
  @SerializedName(SERIALIZED_NAME_OPERATION_ID)
  private String operationId;

  public static final String SERIALIZED_NAME_VERSION = "version";
  @SerializedName(SERIALIZED_NAME_VERSION)
  private Integer version;

  public static final String SERIALIZED_NAME_ACCESS_ID = "accessId";
  @SerializedName(SERIALIZED_NAME_ACCESS_ID)
  private String accessId;

  public static final String SERIALIZED_NAME_ACCESS_STATE = "accessState";
  @SerializedName(SERIALIZED_NAME_ACCESS_STATE)
  private String accessState;

  public static final String SERIALIZED_NAME_ADDITIONAL_ATTRIBUTES = "additionalAttributes";
  @SerializedName(SERIALIZED_NAME_ADDITIONAL_ATTRIBUTES)
  private byte[] additionalAttributes;

  public static final String SERIALIZED_NAME_APPLICATION_INT_ID = "applicationIntId";
  @SerializedName(SERIALIZED_NAME_APPLICATION_INT_ID)
  private String applicationIntId;

  public static final String SERIALIZED_NAME_COMPANY_ID = "companyId";
  @SerializedName(SERIALIZED_NAME_COMPANY_ID)
  private byte[] companyId;

  public static final String SERIALIZED_NAME_CREATED_BY = "createdBy";
  @SerializedName(SERIALIZED_NAME_CREATED_BY)
  private String createdBy;

  public static final String SERIALIZED_NAME_DURATION = "duration";
  @SerializedName(SERIALIZED_NAME_DURATION)
  private Integer duration;

  public static final String SERIALIZED_NAME_EXPIRY = "expiry";
  @SerializedName(SERIALIZED_NAME_EXPIRY)
  private String expiry;

  public static final String SERIALIZED_NAME_ID = "id";
  @SerializedName(SERIALIZED_NAME_ID)
  private Integer id;

  public static final String SERIALIZED_NAME_JOB_ID = "jobId";
  @SerializedName(SERIALIZED_NAME_JOB_ID)
  private byte[] jobId;

  public static final String SERIALIZED_NAME_METADATA = "metadata";
  @SerializedName(SERIALIZED_NAME_METADATA)
  private byte[] metadata;

  public static final String SERIALIZED_NAME_ON_BEHALF_OF_USER_ID = "onBehalfOfUserId";
  @SerializedName(SERIALIZED_NAME_ON_BEHALF_OF_USER_ID)
  private String onBehalfOfUserId;

  public static final String SERIALIZED_NAME_REMARKS = "remarks";
  @SerializedName(SERIALIZED_NAME_REMARKS)
  private String remarks;

  public static final String SERIALIZED_NAME_REQUESTOR_ID = "requestorId";
  @SerializedName(SERIALIZED_NAME_REQUESTOR_ID)
  private String requestorId;

  public static final String SERIALIZED_NAME_RESOURCE_ID = "resourceId";
  @SerializedName(SERIALIZED_NAME_RESOURCE_ID)
  private String resourceId;

  public static final String SERIALIZED_NAME_RESOURCE_TYPE = "resourceType";
  @SerializedName(SERIALIZED_NAME_RESOURCE_TYPE)
  private String resourceType;

  public static final String SERIALIZED_NAME_TEMP_GROUP_ID = "tempGroupId";
  @SerializedName(SERIALIZED_NAME_TEMP_GROUP_ID)
  private String tempGroupId;

  public static final String SERIALIZED_NAME_UPDATED_BY = "updatedBy";
  @SerializedName(SERIALIZED_NAME_UPDATED_BY)
  private String updatedBy;

  public JumpcloudIngressoGetAccessRequestResponse() {
  }

  public JumpcloudIngressoGetAccessRequestResponse operationId(String operationId) {
    
    
    
    
    this.operationId = operationId;
    return this;
  }

   /**
   * Get operationId
   * @return operationId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getOperationId() {
    return operationId;
  }


  public void setOperationId(String operationId) {
    
    
    
    this.operationId = operationId;
  }


  public JumpcloudIngressoGetAccessRequestResponse version(Integer version) {
    
    
    
    
    this.version = version;
    return this;
  }

   /**
   * Get version
   * @return version
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Integer getVersion() {
    return version;
  }


  public void setVersion(Integer version) {
    
    
    
    this.version = version;
  }


  public JumpcloudIngressoGetAccessRequestResponse accessId(String accessId) {
    
    
    
    
    this.accessId = accessId;
    return this;
  }

   /**
   * Get accessId
   * @return accessId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getAccessId() {
    return accessId;
  }


  public void setAccessId(String accessId) {
    
    
    
    this.accessId = accessId;
  }


  public JumpcloudIngressoGetAccessRequestResponse accessState(String accessState) {
    
    
    
    
    this.accessState = accessState;
    return this;
  }

   /**
   * Get accessState
   * @return accessState
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getAccessState() {
    return accessState;
  }


  public void setAccessState(String accessState) {
    
    
    
    this.accessState = accessState;
  }


  public JumpcloudIngressoGetAccessRequestResponse additionalAttributes(byte[] additionalAttributes) {
    
    
    
    
    this.additionalAttributes = additionalAttributes;
    return this;
  }

   /**
   * Get additionalAttributes
   * @return additionalAttributes
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public byte[] getAdditionalAttributes() {
    return additionalAttributes;
  }


  public void setAdditionalAttributes(byte[] additionalAttributes) {
    
    
    
    this.additionalAttributes = additionalAttributes;
  }


  public JumpcloudIngressoGetAccessRequestResponse applicationIntId(String applicationIntId) {
    
    
    
    
    this.applicationIntId = applicationIntId;
    return this;
  }

   /**
   * Get applicationIntId
   * @return applicationIntId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getApplicationIntId() {
    return applicationIntId;
  }


  public void setApplicationIntId(String applicationIntId) {
    
    
    
    this.applicationIntId = applicationIntId;
  }


  public JumpcloudIngressoGetAccessRequestResponse companyId(byte[] companyId) {
    
    
    
    
    this.companyId = companyId;
    return this;
  }

   /**
   * Get companyId
   * @return companyId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public byte[] getCompanyId() {
    return companyId;
  }


  public void setCompanyId(byte[] companyId) {
    
    
    
    this.companyId = companyId;
  }


  public JumpcloudIngressoGetAccessRequestResponse createdBy(String createdBy) {
    
    
    
    
    this.createdBy = createdBy;
    return this;
  }

   /**
   * Get createdBy
   * @return createdBy
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getCreatedBy() {
    return createdBy;
  }


  public void setCreatedBy(String createdBy) {
    
    
    
    this.createdBy = createdBy;
  }


  public JumpcloudIngressoGetAccessRequestResponse duration(Integer duration) {
    
    
    
    
    this.duration = duration;
    return this;
  }

   /**
   * Get duration
   * @return duration
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Integer getDuration() {
    return duration;
  }


  public void setDuration(Integer duration) {
    
    
    
    this.duration = duration;
  }


  public JumpcloudIngressoGetAccessRequestResponse expiry(String expiry) {
    
    
    
    
    this.expiry = expiry;
    return this;
  }

   /**
   * Get expiry
   * @return expiry
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getExpiry() {
    return expiry;
  }


  public void setExpiry(String expiry) {
    
    
    
    this.expiry = expiry;
  }


  public JumpcloudIngressoGetAccessRequestResponse id(Integer id) {
    
    
    
    
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Integer getId() {
    return id;
  }


  public void setId(Integer id) {
    
    
    
    this.id = id;
  }


  public JumpcloudIngressoGetAccessRequestResponse jobId(byte[] jobId) {
    
    
    
    
    this.jobId = jobId;
    return this;
  }

   /**
   * Get jobId
   * @return jobId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public byte[] getJobId() {
    return jobId;
  }


  public void setJobId(byte[] jobId) {
    
    
    
    this.jobId = jobId;
  }


  public JumpcloudIngressoGetAccessRequestResponse metadata(byte[] metadata) {
    
    
    
    
    this.metadata = metadata;
    return this;
  }

   /**
   * Get metadata
   * @return metadata
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public byte[] getMetadata() {
    return metadata;
  }


  public void setMetadata(byte[] metadata) {
    
    
    
    this.metadata = metadata;
  }


  public JumpcloudIngressoGetAccessRequestResponse onBehalfOfUserId(String onBehalfOfUserId) {
    
    
    
    
    this.onBehalfOfUserId = onBehalfOfUserId;
    return this;
  }

   /**
   * Get onBehalfOfUserId
   * @return onBehalfOfUserId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getOnBehalfOfUserId() {
    return onBehalfOfUserId;
  }


  public void setOnBehalfOfUserId(String onBehalfOfUserId) {
    
    
    
    this.onBehalfOfUserId = onBehalfOfUserId;
  }


  public JumpcloudIngressoGetAccessRequestResponse remarks(String remarks) {
    
    
    
    
    this.remarks = remarks;
    return this;
  }

   /**
   * Get remarks
   * @return remarks
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getRemarks() {
    return remarks;
  }


  public void setRemarks(String remarks) {
    
    
    
    this.remarks = remarks;
  }


  public JumpcloudIngressoGetAccessRequestResponse requestorId(String requestorId) {
    
    
    
    
    this.requestorId = requestorId;
    return this;
  }

   /**
   * Get requestorId
   * @return requestorId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getRequestorId() {
    return requestorId;
  }


  public void setRequestorId(String requestorId) {
    
    
    
    this.requestorId = requestorId;
  }


  public JumpcloudIngressoGetAccessRequestResponse resourceId(String resourceId) {
    
    
    
    
    this.resourceId = resourceId;
    return this;
  }

   /**
   * Get resourceId
   * @return resourceId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getResourceId() {
    return resourceId;
  }


  public void setResourceId(String resourceId) {
    
    
    
    this.resourceId = resourceId;
  }


  public JumpcloudIngressoGetAccessRequestResponse resourceType(String resourceType) {
    
    
    
    
    this.resourceType = resourceType;
    return this;
  }

   /**
   * Get resourceType
   * @return resourceType
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getResourceType() {
    return resourceType;
  }


  public void setResourceType(String resourceType) {
    
    
    
    this.resourceType = resourceType;
  }


  public JumpcloudIngressoGetAccessRequestResponse tempGroupId(String tempGroupId) {
    
    
    
    
    this.tempGroupId = tempGroupId;
    return this;
  }

   /**
   * Get tempGroupId
   * @return tempGroupId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getTempGroupId() {
    return tempGroupId;
  }


  public void setTempGroupId(String tempGroupId) {
    
    
    
    this.tempGroupId = tempGroupId;
  }


  public JumpcloudIngressoGetAccessRequestResponse updatedBy(String updatedBy) {
    
    
    
    
    this.updatedBy = updatedBy;
    return this;
  }

   /**
   * Get updatedBy
   * @return updatedBy
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getUpdatedBy() {
    return updatedBy;
  }


  public void setUpdatedBy(String updatedBy) {
    
    
    
    this.updatedBy = updatedBy;
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
   * @return the JumpcloudIngressoGetAccessRequestResponse instance itself
   */
  public JumpcloudIngressoGetAccessRequestResponse putAdditionalProperty(String key, Object value) {
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
    JumpcloudIngressoGetAccessRequestResponse jumpcloudIngressoGetAccessRequestResponse = (JumpcloudIngressoGetAccessRequestResponse) o;
    return Objects.equals(this.operationId, jumpcloudIngressoGetAccessRequestResponse.operationId) &&
        Objects.equals(this.version, jumpcloudIngressoGetAccessRequestResponse.version) &&
        Objects.equals(this.accessId, jumpcloudIngressoGetAccessRequestResponse.accessId) &&
        Objects.equals(this.accessState, jumpcloudIngressoGetAccessRequestResponse.accessState) &&
        Arrays.equals(this.additionalAttributes, jumpcloudIngressoGetAccessRequestResponse.additionalAttributes) &&
        Objects.equals(this.applicationIntId, jumpcloudIngressoGetAccessRequestResponse.applicationIntId) &&
        Arrays.equals(this.companyId, jumpcloudIngressoGetAccessRequestResponse.companyId) &&
        Objects.equals(this.createdBy, jumpcloudIngressoGetAccessRequestResponse.createdBy) &&
        Objects.equals(this.duration, jumpcloudIngressoGetAccessRequestResponse.duration) &&
        Objects.equals(this.expiry, jumpcloudIngressoGetAccessRequestResponse.expiry) &&
        Objects.equals(this.id, jumpcloudIngressoGetAccessRequestResponse.id) &&
        Arrays.equals(this.jobId, jumpcloudIngressoGetAccessRequestResponse.jobId) &&
        Arrays.equals(this.metadata, jumpcloudIngressoGetAccessRequestResponse.metadata) &&
        Objects.equals(this.onBehalfOfUserId, jumpcloudIngressoGetAccessRequestResponse.onBehalfOfUserId) &&
        Objects.equals(this.remarks, jumpcloudIngressoGetAccessRequestResponse.remarks) &&
        Objects.equals(this.requestorId, jumpcloudIngressoGetAccessRequestResponse.requestorId) &&
        Objects.equals(this.resourceId, jumpcloudIngressoGetAccessRequestResponse.resourceId) &&
        Objects.equals(this.resourceType, jumpcloudIngressoGetAccessRequestResponse.resourceType) &&
        Objects.equals(this.tempGroupId, jumpcloudIngressoGetAccessRequestResponse.tempGroupId) &&
        Objects.equals(this.updatedBy, jumpcloudIngressoGetAccessRequestResponse.updatedBy)&&
        Objects.equals(this.additionalProperties, jumpcloudIngressoGetAccessRequestResponse.additionalProperties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(operationId, version, accessId, accessState, Arrays.hashCode(additionalAttributes), applicationIntId, Arrays.hashCode(companyId), createdBy, duration, expiry, id, Arrays.hashCode(jobId), Arrays.hashCode(metadata), onBehalfOfUserId, remarks, requestorId, resourceId, resourceType, tempGroupId, updatedBy, additionalProperties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class JumpcloudIngressoGetAccessRequestResponse {\n");
    sb.append("    operationId: ").append(toIndentedString(operationId)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    accessId: ").append(toIndentedString(accessId)).append("\n");
    sb.append("    accessState: ").append(toIndentedString(accessState)).append("\n");
    sb.append("    additionalAttributes: ").append(toIndentedString(additionalAttributes)).append("\n");
    sb.append("    applicationIntId: ").append(toIndentedString(applicationIntId)).append("\n");
    sb.append("    companyId: ").append(toIndentedString(companyId)).append("\n");
    sb.append("    createdBy: ").append(toIndentedString(createdBy)).append("\n");
    sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
    sb.append("    expiry: ").append(toIndentedString(expiry)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    jobId: ").append(toIndentedString(jobId)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("    onBehalfOfUserId: ").append(toIndentedString(onBehalfOfUserId)).append("\n");
    sb.append("    remarks: ").append(toIndentedString(remarks)).append("\n");
    sb.append("    requestorId: ").append(toIndentedString(requestorId)).append("\n");
    sb.append("    resourceId: ").append(toIndentedString(resourceId)).append("\n");
    sb.append("    resourceType: ").append(toIndentedString(resourceType)).append("\n");
    sb.append("    tempGroupId: ").append(toIndentedString(tempGroupId)).append("\n");
    sb.append("    updatedBy: ").append(toIndentedString(updatedBy)).append("\n");
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
    openapiFields.add("operationId");
    openapiFields.add("version");
    openapiFields.add("accessId");
    openapiFields.add("accessState");
    openapiFields.add("additionalAttributes");
    openapiFields.add("applicationIntId");
    openapiFields.add("companyId");
    openapiFields.add("createdBy");
    openapiFields.add("duration");
    openapiFields.add("expiry");
    openapiFields.add("id");
    openapiFields.add("jobId");
    openapiFields.add("metadata");
    openapiFields.add("onBehalfOfUserId");
    openapiFields.add("remarks");
    openapiFields.add("requestorId");
    openapiFields.add("resourceId");
    openapiFields.add("resourceType");
    openapiFields.add("tempGroupId");
    openapiFields.add("updatedBy");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

 /**
  * Validates the JSON Object and throws an exception if issues found
  *
  * @param jsonObj JSON Object
  * @throws IOException if the JSON Object is invalid with respect to JumpcloudIngressoGetAccessRequestResponse
  */
  public static void validateJsonObject(JsonObject jsonObj) throws IOException {
      if (jsonObj == null) {
        if (!JumpcloudIngressoGetAccessRequestResponse.openapiRequiredFields.isEmpty()) { // has required fields but JSON object is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in JumpcloudIngressoGetAccessRequestResponse is not found in the empty JSON string", JumpcloudIngressoGetAccessRequestResponse.openapiRequiredFields.toString()));
        }
      }
      if ((jsonObj.get("operationId") != null && !jsonObj.get("operationId").isJsonNull()) && !jsonObj.get("operationId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `operationId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("operationId").toString()));
      }
      if ((jsonObj.get("accessId") != null && !jsonObj.get("accessId").isJsonNull()) && !jsonObj.get("accessId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `accessId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("accessId").toString()));
      }
      if ((jsonObj.get("accessState") != null && !jsonObj.get("accessState").isJsonNull()) && !jsonObj.get("accessState").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `accessState` to be a primitive type in the JSON string but got `%s`", jsonObj.get("accessState").toString()));
      }
      if ((jsonObj.get("applicationIntId") != null && !jsonObj.get("applicationIntId").isJsonNull()) && !jsonObj.get("applicationIntId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `applicationIntId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("applicationIntId").toString()));
      }
      if ((jsonObj.get("createdBy") != null && !jsonObj.get("createdBy").isJsonNull()) && !jsonObj.get("createdBy").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `createdBy` to be a primitive type in the JSON string but got `%s`", jsonObj.get("createdBy").toString()));
      }
      if ((jsonObj.get("expiry") != null && !jsonObj.get("expiry").isJsonNull()) && !jsonObj.get("expiry").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `expiry` to be a primitive type in the JSON string but got `%s`", jsonObj.get("expiry").toString()));
      }
      if ((jsonObj.get("onBehalfOfUserId") != null && !jsonObj.get("onBehalfOfUserId").isJsonNull()) && !jsonObj.get("onBehalfOfUserId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `onBehalfOfUserId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("onBehalfOfUserId").toString()));
      }
      if ((jsonObj.get("remarks") != null && !jsonObj.get("remarks").isJsonNull()) && !jsonObj.get("remarks").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `remarks` to be a primitive type in the JSON string but got `%s`", jsonObj.get("remarks").toString()));
      }
      if ((jsonObj.get("requestorId") != null && !jsonObj.get("requestorId").isJsonNull()) && !jsonObj.get("requestorId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `requestorId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("requestorId").toString()));
      }
      if ((jsonObj.get("resourceId") != null && !jsonObj.get("resourceId").isJsonNull()) && !jsonObj.get("resourceId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `resourceId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("resourceId").toString()));
      }
      if ((jsonObj.get("resourceType") != null && !jsonObj.get("resourceType").isJsonNull()) && !jsonObj.get("resourceType").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `resourceType` to be a primitive type in the JSON string but got `%s`", jsonObj.get("resourceType").toString()));
      }
      if ((jsonObj.get("tempGroupId") != null && !jsonObj.get("tempGroupId").isJsonNull()) && !jsonObj.get("tempGroupId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `tempGroupId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("tempGroupId").toString()));
      }
      if ((jsonObj.get("updatedBy") != null && !jsonObj.get("updatedBy").isJsonNull()) && !jsonObj.get("updatedBy").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `updatedBy` to be a primitive type in the JSON string but got `%s`", jsonObj.get("updatedBy").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!JumpcloudIngressoGetAccessRequestResponse.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'JumpcloudIngressoGetAccessRequestResponse' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<JumpcloudIngressoGetAccessRequestResponse> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(JumpcloudIngressoGetAccessRequestResponse.class));

       return (TypeAdapter<T>) new TypeAdapter<JumpcloudIngressoGetAccessRequestResponse>() {
           @Override
           public void write(JsonWriter out, JumpcloudIngressoGetAccessRequestResponse value) throws IOException {
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
           public JumpcloudIngressoGetAccessRequestResponse read(JsonReader in) throws IOException {
             JsonObject jsonObj = elementAdapter.read(in).getAsJsonObject();
             validateJsonObject(jsonObj);
             // store additional fields in the deserialized instance
             JumpcloudIngressoGetAccessRequestResponse instance = thisAdapter.fromJsonTree(jsonObj);
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
  * Create an instance of JumpcloudIngressoGetAccessRequestResponse given an JSON string
  *
  * @param jsonString JSON string
  * @return An instance of JumpcloudIngressoGetAccessRequestResponse
  * @throws IOException if the JSON string is invalid with respect to JumpcloudIngressoGetAccessRequestResponse
  */
  public static JumpcloudIngressoGetAccessRequestResponse fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, JumpcloudIngressoGetAccessRequestResponse.class);
  }

 /**
  * Convert an instance of JumpcloudIngressoGetAccessRequestResponse to an JSON string
  *
  * @return JSON string
  */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

