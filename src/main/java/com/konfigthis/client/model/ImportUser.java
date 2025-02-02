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
import com.konfigthis.client.model.ImportUserAddress;
import com.konfigthis.client.model.ImportUserPhoneNumber;
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
 * ImportUser
 */@javax.annotation.Generated(value = "Generated by https://konfigthis.com")
public class ImportUser {
  public static final String SERIALIZED_NAME_ADDRESSES = "addresses";
  @SerializedName(SERIALIZED_NAME_ADDRESSES)
  private List<ImportUserAddress> addresses = null;

  public static final String SERIALIZED_NAME_ALTERNATE_EMAIL = "alternateEmail";
  @SerializedName(SERIALIZED_NAME_ALTERNATE_EMAIL)
  private String alternateEmail;

  public static final String SERIALIZED_NAME_COMPANY = "company";
  @SerializedName(SERIALIZED_NAME_COMPANY)
  private String company;

  public static final String SERIALIZED_NAME_COST_CENTER = "costCenter";
  @SerializedName(SERIALIZED_NAME_COST_CENTER)
  private String costCenter;

  public static final String SERIALIZED_NAME_DEPARTMENT = "department";
  @SerializedName(SERIALIZED_NAME_DEPARTMENT)
  private String department;

  public static final String SERIALIZED_NAME_DISPLAYNAME = "displayname";
  @SerializedName(SERIALIZED_NAME_DISPLAYNAME)
  private String displayname;

  public static final String SERIALIZED_NAME_EMAIL = "email";
  @SerializedName(SERIALIZED_NAME_EMAIL)
  private String email;

  public static final String SERIALIZED_NAME_EMPLOYEE_IDENTIFIER = "employeeIdentifier";
  @SerializedName(SERIALIZED_NAME_EMPLOYEE_IDENTIFIER)
  private String employeeIdentifier;

  public static final String SERIALIZED_NAME_EMPLOYEE_TYPE = "employeeType";
  @SerializedName(SERIALIZED_NAME_EMPLOYEE_TYPE)
  private String employeeType;

  public static final String SERIALIZED_NAME_FIRSTNAME = "firstname";
  @SerializedName(SERIALIZED_NAME_FIRSTNAME)
  private String firstname;

  public static final String SERIALIZED_NAME_ID = "id";
  @SerializedName(SERIALIZED_NAME_ID)
  private String id;

  public static final String SERIALIZED_NAME_JOB_TITLE = "jobTitle";
  @SerializedName(SERIALIZED_NAME_JOB_TITLE)
  private String jobTitle;

  public static final String SERIALIZED_NAME_LASTNAME = "lastname";
  @SerializedName(SERIALIZED_NAME_LASTNAME)
  private String lastname;

  public static final String SERIALIZED_NAME_LOCATION = "location";
  @SerializedName(SERIALIZED_NAME_LOCATION)
  private String location;

  public static final String SERIALIZED_NAME_MANAGER = "manager";
  @SerializedName(SERIALIZED_NAME_MANAGER)
  private String manager;

  public static final String SERIALIZED_NAME_MIDDLENAME = "middlename";
  @SerializedName(SERIALIZED_NAME_MIDDLENAME)
  private String middlename;

  public static final String SERIALIZED_NAME_PHONE_NUMBERS = "phoneNumbers";
  @SerializedName(SERIALIZED_NAME_PHONE_NUMBERS)
  private List<ImportUserPhoneNumber> phoneNumbers = null;

  public static final String SERIALIZED_NAME_USERNAME = "username";
  @SerializedName(SERIALIZED_NAME_USERNAME)
  private String username;

  public ImportUser() {
  }

  public ImportUser addresses(List<ImportUserAddress> addresses) {
    
    
    
    
    this.addresses = addresses;
    return this;
  }

  public ImportUser addAddressesItem(ImportUserAddress addressesItem) {
    if (this.addresses == null) {
      this.addresses = new ArrayList<>();
    }
    this.addresses.add(addressesItem);
    return this;
  }

   /**
   * Get addresses
   * @return addresses
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public List<ImportUserAddress> getAddresses() {
    return addresses;
  }


  public void setAddresses(List<ImportUserAddress> addresses) {
    
    
    
    this.addresses = addresses;
  }


  public ImportUser alternateEmail(String alternateEmail) {
    
    
    
    
    this.alternateEmail = alternateEmail;
    return this;
  }

   /**
   * Get alternateEmail
   * @return alternateEmail
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getAlternateEmail() {
    return alternateEmail;
  }


  public void setAlternateEmail(String alternateEmail) {
    
    
    
    this.alternateEmail = alternateEmail;
  }


  public ImportUser company(String company) {
    
    
    
    
    this.company = company;
    return this;
  }

   /**
   * Get company
   * @return company
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getCompany() {
    return company;
  }


  public void setCompany(String company) {
    
    
    
    this.company = company;
  }


  public ImportUser costCenter(String costCenter) {
    
    
    
    
    this.costCenter = costCenter;
    return this;
  }

   /**
   * Get costCenter
   * @return costCenter
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getCostCenter() {
    return costCenter;
  }


  public void setCostCenter(String costCenter) {
    
    
    
    this.costCenter = costCenter;
  }


  public ImportUser department(String department) {
    
    
    
    
    this.department = department;
    return this;
  }

   /**
   * Get department
   * @return department
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getDepartment() {
    return department;
  }


  public void setDepartment(String department) {
    
    
    
    this.department = department;
  }


  public ImportUser displayname(String displayname) {
    
    
    
    
    this.displayname = displayname;
    return this;
  }

   /**
   * Get displayname
   * @return displayname
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getDisplayname() {
    return displayname;
  }


  public void setDisplayname(String displayname) {
    
    
    
    this.displayname = displayname;
  }


  public ImportUser email(String email) {
    
    
    
    
    this.email = email;
    return this;
  }

   /**
   * Get email
   * @return email
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getEmail() {
    return email;
  }


  public void setEmail(String email) {
    
    
    
    this.email = email;
  }


  public ImportUser employeeIdentifier(String employeeIdentifier) {
    
    
    
    
    this.employeeIdentifier = employeeIdentifier;
    return this;
  }

   /**
   * Get employeeIdentifier
   * @return employeeIdentifier
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getEmployeeIdentifier() {
    return employeeIdentifier;
  }


  public void setEmployeeIdentifier(String employeeIdentifier) {
    
    
    
    this.employeeIdentifier = employeeIdentifier;
  }


  public ImportUser employeeType(String employeeType) {
    
    
    
    
    this.employeeType = employeeType;
    return this;
  }

   /**
   * Get employeeType
   * @return employeeType
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getEmployeeType() {
    return employeeType;
  }


  public void setEmployeeType(String employeeType) {
    
    
    
    this.employeeType = employeeType;
  }


  public ImportUser firstname(String firstname) {
    
    
    
    
    this.firstname = firstname;
    return this;
  }

   /**
   * Get firstname
   * @return firstname
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getFirstname() {
    return firstname;
  }


  public void setFirstname(String firstname) {
    
    
    
    this.firstname = firstname;
  }


  public ImportUser id(String id) {
    
    
    
    
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getId() {
    return id;
  }


  public void setId(String id) {
    
    
    
    this.id = id;
  }


  public ImportUser jobTitle(String jobTitle) {
    
    
    
    
    this.jobTitle = jobTitle;
    return this;
  }

   /**
   * Get jobTitle
   * @return jobTitle
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getJobTitle() {
    return jobTitle;
  }


  public void setJobTitle(String jobTitle) {
    
    
    
    this.jobTitle = jobTitle;
  }


  public ImportUser lastname(String lastname) {
    
    
    
    
    this.lastname = lastname;
    return this;
  }

   /**
   * Get lastname
   * @return lastname
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getLastname() {
    return lastname;
  }


  public void setLastname(String lastname) {
    
    
    
    this.lastname = lastname;
  }


  public ImportUser location(String location) {
    
    
    
    
    this.location = location;
    return this;
  }

   /**
   * Get location
   * @return location
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getLocation() {
    return location;
  }


  public void setLocation(String location) {
    
    
    
    this.location = location;
  }


  public ImportUser manager(String manager) {
    
    
    
    
    this.manager = manager;
    return this;
  }

   /**
   * Get manager
   * @return manager
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getManager() {
    return manager;
  }


  public void setManager(String manager) {
    
    
    
    this.manager = manager;
  }


  public ImportUser middlename(String middlename) {
    
    
    
    
    this.middlename = middlename;
    return this;
  }

   /**
   * Get middlename
   * @return middlename
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getMiddlename() {
    return middlename;
  }


  public void setMiddlename(String middlename) {
    
    
    
    this.middlename = middlename;
  }


  public ImportUser phoneNumbers(List<ImportUserPhoneNumber> phoneNumbers) {
    
    
    
    
    this.phoneNumbers = phoneNumbers;
    return this;
  }

  public ImportUser addPhoneNumbersItem(ImportUserPhoneNumber phoneNumbersItem) {
    if (this.phoneNumbers == null) {
      this.phoneNumbers = new ArrayList<>();
    }
    this.phoneNumbers.add(phoneNumbersItem);
    return this;
  }

   /**
   * Get phoneNumbers
   * @return phoneNumbers
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public List<ImportUserPhoneNumber> getPhoneNumbers() {
    return phoneNumbers;
  }


  public void setPhoneNumbers(List<ImportUserPhoneNumber> phoneNumbers) {
    
    
    
    this.phoneNumbers = phoneNumbers;
  }


  public ImportUser username(String username) {
    
    
    
    
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
   * @return the ImportUser instance itself
   */
  public ImportUser putAdditionalProperty(String key, Object value) {
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
    ImportUser importUser = (ImportUser) o;
    return Objects.equals(this.addresses, importUser.addresses) &&
        Objects.equals(this.alternateEmail, importUser.alternateEmail) &&
        Objects.equals(this.company, importUser.company) &&
        Objects.equals(this.costCenter, importUser.costCenter) &&
        Objects.equals(this.department, importUser.department) &&
        Objects.equals(this.displayname, importUser.displayname) &&
        Objects.equals(this.email, importUser.email) &&
        Objects.equals(this.employeeIdentifier, importUser.employeeIdentifier) &&
        Objects.equals(this.employeeType, importUser.employeeType) &&
        Objects.equals(this.firstname, importUser.firstname) &&
        Objects.equals(this.id, importUser.id) &&
        Objects.equals(this.jobTitle, importUser.jobTitle) &&
        Objects.equals(this.lastname, importUser.lastname) &&
        Objects.equals(this.location, importUser.location) &&
        Objects.equals(this.manager, importUser.manager) &&
        Objects.equals(this.middlename, importUser.middlename) &&
        Objects.equals(this.phoneNumbers, importUser.phoneNumbers) &&
        Objects.equals(this.username, importUser.username)&&
        Objects.equals(this.additionalProperties, importUser.additionalProperties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(addresses, alternateEmail, company, costCenter, department, displayname, email, employeeIdentifier, employeeType, firstname, id, jobTitle, lastname, location, manager, middlename, phoneNumbers, username, additionalProperties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImportUser {\n");
    sb.append("    addresses: ").append(toIndentedString(addresses)).append("\n");
    sb.append("    alternateEmail: ").append(toIndentedString(alternateEmail)).append("\n");
    sb.append("    company: ").append(toIndentedString(company)).append("\n");
    sb.append("    costCenter: ").append(toIndentedString(costCenter)).append("\n");
    sb.append("    department: ").append(toIndentedString(department)).append("\n");
    sb.append("    displayname: ").append(toIndentedString(displayname)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    employeeIdentifier: ").append(toIndentedString(employeeIdentifier)).append("\n");
    sb.append("    employeeType: ").append(toIndentedString(employeeType)).append("\n");
    sb.append("    firstname: ").append(toIndentedString(firstname)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    jobTitle: ").append(toIndentedString(jobTitle)).append("\n");
    sb.append("    lastname: ").append(toIndentedString(lastname)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    manager: ").append(toIndentedString(manager)).append("\n");
    sb.append("    middlename: ").append(toIndentedString(middlename)).append("\n");
    sb.append("    phoneNumbers: ").append(toIndentedString(phoneNumbers)).append("\n");
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
    openapiFields.add("addresses");
    openapiFields.add("alternateEmail");
    openapiFields.add("company");
    openapiFields.add("costCenter");
    openapiFields.add("department");
    openapiFields.add("displayname");
    openapiFields.add("email");
    openapiFields.add("employeeIdentifier");
    openapiFields.add("employeeType");
    openapiFields.add("firstname");
    openapiFields.add("id");
    openapiFields.add("jobTitle");
    openapiFields.add("lastname");
    openapiFields.add("location");
    openapiFields.add("manager");
    openapiFields.add("middlename");
    openapiFields.add("phoneNumbers");
    openapiFields.add("username");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

 /**
  * Validates the JSON Object and throws an exception if issues found
  *
  * @param jsonObj JSON Object
  * @throws IOException if the JSON Object is invalid with respect to ImportUser
  */
  public static void validateJsonObject(JsonObject jsonObj) throws IOException {
      if (jsonObj == null) {
        if (!ImportUser.openapiRequiredFields.isEmpty()) { // has required fields but JSON object is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in ImportUser is not found in the empty JSON string", ImportUser.openapiRequiredFields.toString()));
        }
      }
      if (jsonObj.get("addresses") != null && !jsonObj.get("addresses").isJsonNull()) {
        JsonArray jsonArrayaddresses = jsonObj.getAsJsonArray("addresses");
        if (jsonArrayaddresses != null) {
          // ensure the json data is an array
          if (!jsonObj.get("addresses").isJsonArray()) {
            throw new IllegalArgumentException(String.format("Expected the field `addresses` to be an array in the JSON string but got `%s`", jsonObj.get("addresses").toString()));
          }

          // validate the optional field `addresses` (array)
          for (int i = 0; i < jsonArrayaddresses.size(); i++) {
            ImportUserAddress.validateJsonObject(jsonArrayaddresses.get(i).getAsJsonObject());
          };
        }
      }
      if ((jsonObj.get("alternateEmail") != null && !jsonObj.get("alternateEmail").isJsonNull()) && !jsonObj.get("alternateEmail").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `alternateEmail` to be a primitive type in the JSON string but got `%s`", jsonObj.get("alternateEmail").toString()));
      }
      if ((jsonObj.get("company") != null && !jsonObj.get("company").isJsonNull()) && !jsonObj.get("company").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `company` to be a primitive type in the JSON string but got `%s`", jsonObj.get("company").toString()));
      }
      if ((jsonObj.get("costCenter") != null && !jsonObj.get("costCenter").isJsonNull()) && !jsonObj.get("costCenter").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `costCenter` to be a primitive type in the JSON string but got `%s`", jsonObj.get("costCenter").toString()));
      }
      if ((jsonObj.get("department") != null && !jsonObj.get("department").isJsonNull()) && !jsonObj.get("department").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `department` to be a primitive type in the JSON string but got `%s`", jsonObj.get("department").toString()));
      }
      if ((jsonObj.get("displayname") != null && !jsonObj.get("displayname").isJsonNull()) && !jsonObj.get("displayname").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `displayname` to be a primitive type in the JSON string but got `%s`", jsonObj.get("displayname").toString()));
      }
      if ((jsonObj.get("email") != null && !jsonObj.get("email").isJsonNull()) && !jsonObj.get("email").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `email` to be a primitive type in the JSON string but got `%s`", jsonObj.get("email").toString()));
      }
      if ((jsonObj.get("employeeIdentifier") != null && !jsonObj.get("employeeIdentifier").isJsonNull()) && !jsonObj.get("employeeIdentifier").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `employeeIdentifier` to be a primitive type in the JSON string but got `%s`", jsonObj.get("employeeIdentifier").toString()));
      }
      if ((jsonObj.get("employeeType") != null && !jsonObj.get("employeeType").isJsonNull()) && !jsonObj.get("employeeType").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `employeeType` to be a primitive type in the JSON string but got `%s`", jsonObj.get("employeeType").toString()));
      }
      if ((jsonObj.get("firstname") != null && !jsonObj.get("firstname").isJsonNull()) && !jsonObj.get("firstname").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `firstname` to be a primitive type in the JSON string but got `%s`", jsonObj.get("firstname").toString()));
      }
      if ((jsonObj.get("id") != null && !jsonObj.get("id").isJsonNull()) && !jsonObj.get("id").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `id` to be a primitive type in the JSON string but got `%s`", jsonObj.get("id").toString()));
      }
      if ((jsonObj.get("jobTitle") != null && !jsonObj.get("jobTitle").isJsonNull()) && !jsonObj.get("jobTitle").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `jobTitle` to be a primitive type in the JSON string but got `%s`", jsonObj.get("jobTitle").toString()));
      }
      if ((jsonObj.get("lastname") != null && !jsonObj.get("lastname").isJsonNull()) && !jsonObj.get("lastname").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `lastname` to be a primitive type in the JSON string but got `%s`", jsonObj.get("lastname").toString()));
      }
      if ((jsonObj.get("location") != null && !jsonObj.get("location").isJsonNull()) && !jsonObj.get("location").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `location` to be a primitive type in the JSON string but got `%s`", jsonObj.get("location").toString()));
      }
      if ((jsonObj.get("manager") != null && !jsonObj.get("manager").isJsonNull()) && !jsonObj.get("manager").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `manager` to be a primitive type in the JSON string but got `%s`", jsonObj.get("manager").toString()));
      }
      if ((jsonObj.get("middlename") != null && !jsonObj.get("middlename").isJsonNull()) && !jsonObj.get("middlename").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `middlename` to be a primitive type in the JSON string but got `%s`", jsonObj.get("middlename").toString()));
      }
      if (jsonObj.get("phoneNumbers") != null && !jsonObj.get("phoneNumbers").isJsonNull()) {
        JsonArray jsonArrayphoneNumbers = jsonObj.getAsJsonArray("phoneNumbers");
        if (jsonArrayphoneNumbers != null) {
          // ensure the json data is an array
          if (!jsonObj.get("phoneNumbers").isJsonArray()) {
            throw new IllegalArgumentException(String.format("Expected the field `phoneNumbers` to be an array in the JSON string but got `%s`", jsonObj.get("phoneNumbers").toString()));
          }

          // validate the optional field `phoneNumbers` (array)
          for (int i = 0; i < jsonArrayphoneNumbers.size(); i++) {
            ImportUserPhoneNumber.validateJsonObject(jsonArrayphoneNumbers.get(i).getAsJsonObject());
          };
        }
      }
      if ((jsonObj.get("username") != null && !jsonObj.get("username").isJsonNull()) && !jsonObj.get("username").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `username` to be a primitive type in the JSON string but got `%s`", jsonObj.get("username").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!ImportUser.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'ImportUser' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<ImportUser> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(ImportUser.class));

       return (TypeAdapter<T>) new TypeAdapter<ImportUser>() {
           @Override
           public void write(JsonWriter out, ImportUser value) throws IOException {
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
           public ImportUser read(JsonReader in) throws IOException {
             JsonObject jsonObj = elementAdapter.read(in).getAsJsonObject();
             validateJsonObject(jsonObj);
             // store additional fields in the deserialized instance
             ImportUser instance = thisAdapter.fromJsonTree(jsonObj);
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
  * Create an instance of ImportUser given an JSON string
  *
  * @param jsonString JSON string
  * @return An instance of ImportUser
  * @throws IOException if the JSON string is invalid with respect to ImportUser
  */
  public static ImportUser fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, ImportUser.class);
  }

 /**
  * Convert an instance of ImportUser to an JSON string
  *
  * @return JSON string
  */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

