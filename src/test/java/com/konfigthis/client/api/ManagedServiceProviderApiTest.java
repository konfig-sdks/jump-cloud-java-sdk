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


package com.konfigthis.client.api;

import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.model.Administrator;
import com.konfigthis.client.model.AdministratorOrganizationLink;
import com.konfigthis.client.model.AdministratorOrganizationLinkReq;
import com.konfigthis.client.model.CasesMetadataResponse;
import com.konfigthis.client.model.CasesResponse;
import com.konfigthis.client.model.CreateOrganization;
import java.io.File;
import com.konfigthis.client.model.Organization;
import com.konfigthis.client.model.PolicyGroupTemplate;
import com.konfigthis.client.model.PolicyGroupTemplateMembers;
import com.konfigthis.client.model.PolicyGroupTemplates;
import com.konfigthis.client.model.PolicyGroupTemplatesListConfiguredPolicyTemplatesResponse;
import com.konfigthis.client.model.Provider;
import com.konfigthis.client.model.ProviderAdminReq;
import com.konfigthis.client.model.ProviderInvoiceResponse;
import com.konfigthis.client.model.ProvidersListAdministratorsResponse;
import com.konfigthis.client.model.ProvidersListOrganizationsResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for ManagedServiceProviderApi
 */
@Disabled
public class ManagedServiceProviderApiTest {

    private static ManagedServiceProviderApi api;

    
    @BeforeAll
    public static void beforeClass() {
        ApiClient apiClient = Configuration.getDefaultApiClient();
        api = new ManagedServiceProviderApi(apiClient);
    }

    /**
     * Get the metadata for cases
     *
     * This endpoint returns the metadata for cases
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void casesMetadataTest() throws ApiException {
        CasesMetadataResponse response = api.casesMetadata()
                .execute();
        // TODO: test validations
    }

    /**
     * Allow Adminstrator access to an Organization.
     *
     * This endpoint allows you to grant Administrator access to an Organization.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void createByAdministratorTest() throws ApiException {
        String id = null;
        String organization = null;
        AdministratorOrganizationLink response = api.createByAdministrator(id)
                .organization(organization)
                .execute();
        // TODO: test validations
    }

    /**
     * Create Provider Organization
     *
     * This endpoint creates a new organization under the provider
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void createOrgTest() throws ApiException {
        String providerId = null;
        Integer maxSystemUsers = null;
        String name = null;
        Organization response = api.createOrg(providerId)
                .maxSystemUsers(maxSystemUsers)
                .name(name)
                .execute();
        // TODO: test validations
    }

    /**
     * Deletes policy group template.
     *
     * Deletes a Policy Group Template.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void deleteTest() throws ApiException {
        String providerId = null;
        String id = null;
        api.delete(providerId, id)
                .execute();
        // TODO: test validations
    }

    /**
     * Gets a provider&#39;s policy group template.
     *
     * Retrieves a Policy Group Template for this provider.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getTest() throws ApiException {
        String providerId = null;
        String id = null;
        PolicyGroupTemplate response = api.get(providerId, id)
                .execute();
        // TODO: test validations
    }

    /**
     * Retrieve a configured policy template by id.
     *
     * Retrieves a Configured Policy Templates for this provider and Id.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getConfiguredPolicyTemplateTest() throws ApiException {
        String providerId = null;
        String id = null;
        Object response = api.getConfiguredPolicyTemplate(providerId, id)
                .execute();
        // TODO: test validations
    }

    /**
     * Retrieve Provider
     *
     * This endpoint returns details about a provider
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getProviderTest() throws ApiException {
        String providerId = null;
        List<String> fields = null;
        Provider response = api.getProvider(providerId)
                .fields(fields)
                .execute();
        // TODO: test validations
    }

    /**
     * List a provider&#39;s policy group templates.
     *
     * Retrieves a list of Policy Group Templates for this provider.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listTest() throws ApiException {
        String providerId = null;
        List<String> fields = null;
        Integer skip = null;
        List<String> sort = null;
        Integer limit = null;
        List<String> filter = null;
        PolicyGroupTemplates response = api.list(providerId)
                .fields(fields)
                .skip(skip)
                .sort(sort)
                .limit(limit)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List Provider Administrators
     *
     * This endpoint returns a list of the Administrators associated with the Provider. You must be associated with the provider to use this route.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listAdministratorsTest() throws ApiException {
        String providerId = null;
        List<String> fields = null;
        List<String> filter = null;
        Integer limit = null;
        Integer skip = null;
        List<String> sort = null;
        List<String> sortIgnoreCase = null;
        ProvidersListAdministratorsResponse response = api.listAdministrators(providerId)
                .fields(fields)
                .filter(filter)
                .limit(limit)
                .skip(skip)
                .sort(sort)
                .sortIgnoreCase(sortIgnoreCase)
                .execute();
        // TODO: test validations
    }

    /**
     * List the association links between an Administrator and Organizations.
     *
     * This endpoint returns the association links between an Administrator and Organizations.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listByAdministratorTest() throws ApiException {
        String id = null;
        Integer limit = null;
        Integer skip = null;
        List<AdministratorOrganizationLink> response = api.listByAdministrator(id)
                .limit(limit)
                .skip(skip)
                .execute();
        // TODO: test validations
    }

    /**
     * List the association links between an Organization and Administrators.
     *
     * This endpoint returns the association links between an Organization and Administrators.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listByOrganizationTest() throws ApiException {
        String id = null;
        Integer limit = null;
        Integer skip = null;
        List<AdministratorOrganizationLink> response = api.listByOrganization(id)
                .limit(limit)
                .skip(skip)
                .execute();
        // TODO: test validations
    }

    /**
     * List a provider&#39;s configured policy templates.
     *
     * Retrieves a list of Configured Policy Templates for this provider.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listConfiguredPolicyTemplatesTest() throws ApiException {
        String providerId = null;
        Integer skip = null;
        List<String> sort = null;
        Integer limit = null;
        List<String> filter = null;
        PolicyGroupTemplatesListConfiguredPolicyTemplatesResponse response = api.listConfiguredPolicyTemplates(providerId)
                .skip(skip)
                .sort(sort)
                .limit(limit)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * Gets the list of members from a policy group template.
     *
     * Retrieves a Policy Group Template&#39;s Members.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listMembersTest() throws ApiException {
        String providerId = null;
        String id = null;
        Integer skip = null;
        List<String> sort = null;
        Integer limit = null;
        List<String> filter = null;
        PolicyGroupTemplateMembers response = api.listMembers(providerId, id)
                .skip(skip)
                .sort(sort)
                .limit(limit)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * List Provider Organizations
     *
     * This endpoint returns a list of the Organizations associated with the Provider. You must be associated with the provider to use this route.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listOrganizationsTest() throws ApiException {
        String providerId = null;
        List<String> fields = null;
        List<String> filter = null;
        Integer limit = null;
        Integer skip = null;
        List<String> sort = null;
        List<String> sortIgnoreCase = null;
        ProvidersListOrganizationsResponse response = api.listOrganizations(providerId)
                .fields(fields)
                .filter(filter)
                .limit(limit)
                .skip(skip)
                .sort(sort)
                .sortIgnoreCase(sortIgnoreCase)
                .execute();
        // TODO: test validations
    }

    /**
     * Create a new Provider Administrator
     *
     * This endpoint allows you to create a provider administrator. You must be associated with the provider to use this route. You must provide either &#x60;role&#x60; or &#x60;roleName&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void postAdminsTest() throws ApiException {
        String email = null;
        String providerId = null;
        Boolean apiKeyAllowed = null;
        Boolean bindNoOrgs = null;
        Boolean enableMultiFactor = null;
        String firstname = null;
        String lastname = null;
        String role = null;
        String roleName = null;
        Administrator response = api.postAdmins(email, providerId)
                .apiKeyAllowed(apiKeyAllowed)
                .bindNoOrgs(bindNoOrgs)
                .enableMultiFactor(enableMultiFactor)
                .firstname(firstname)
                .lastname(lastname)
                .role(role)
                .roleName(roleName)
                .execute();
        // TODO: test validations
    }

    /**
     * Get all cases (Support/Feature requests) for provider
     *
     * This endpoint returns the cases (Support/Feature requests) for the provider
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void providerListCaseTest() throws ApiException {
        String providerId = null;
        Integer skip = null;
        List<String> sort = null;
        Integer limit = null;
        List<String> filter = null;
        CasesResponse response = api.providerListCase(providerId)
                .skip(skip)
                .sort(sort)
                .limit(limit)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * Remove association between an Administrator and an Organization.
     *
     * This endpoint removes the association link between an Administrator and an Organization.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void removeByAdministratorTest() throws ApiException {
        String administratorId = null;
        String id = null;
        api.removeByAdministrator(administratorId, id)
                .execute();
        // TODO: test validations
    }

    /**
     * Download a provider&#39;s invoice.
     *
     * Retrieves an invoice for this provider. You must be associated to the provider to use this endpoint.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void retrieveInvoiceTest() throws ApiException {
        String providerId = null;
        String ID = null;
        File response = api.retrieveInvoice(providerId, ID)
                .execute();
        // TODO: test validations
    }

    /**
     * List a provider&#39;s invoices.
     *
     * Retrieves a list of invoices for this provider. You must be associated to the provider to use this endpoint.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void retrieveInvoicesTest() throws ApiException {
        String providerId = null;
        Integer skip = null;
        List<String> sort = null;
        Integer limit = null;
        List<String> filter = null;
        ProviderInvoiceResponse response = api.retrieveInvoices(providerId)
                .skip(skip)
                .sort(sort)
                .limit(limit)
                .filter(filter)
                .execute();
        // TODO: test validations
    }

    /**
     * Update Provider Organization
     *
     * This endpoint updates a provider&#39;s organization
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void updateOrgTest() throws ApiException {
        String providerId = null;
        String id = null;
        String id = null;
        Integer maxSystemUsers = null;
        String name = null;
        Organization response = api.updateOrg(providerId, id)
                .id(id)
                .maxSystemUsers(maxSystemUsers)
                .name(name)
                .execute();
        // TODO: test validations
    }

}