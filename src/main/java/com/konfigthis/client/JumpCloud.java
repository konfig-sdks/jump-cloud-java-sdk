package com.konfigthis.client;

import com.konfigthis.client.api.ActiveDirectoryApi;
import com.konfigthis.client.api.AdministratorsApi;
import com.konfigthis.client.api.AggregatedPolicyStatsApi;
import com.konfigthis.client.api.AppleMdmApi;
import com.konfigthis.client.api.ApplicationsApi;
import com.konfigthis.client.api.AuthenticationPoliciesApi;
import com.konfigthis.client.api.BulkJobRequestsApi;
import com.konfigthis.client.api.CommandsApi;
import com.konfigthis.client.api.CustomEmailsApi;
import com.konfigthis.client.api.DirectoriesApi;
import com.konfigthis.client.api.DuoApi;
import com.konfigthis.client.api.FeatureTrialsApi;
import com.konfigthis.client.api.GSuiteApi;
import com.konfigthis.client.api.GSuiteImportApi;
import com.konfigthis.client.api.GoogleEmmApi;
import com.konfigthis.client.api.GraphApi;
import com.konfigthis.client.api.GroupsApi;
import com.konfigthis.client.api.IpListsApi;
import com.konfigthis.client.api.IdentityProvidersApi;
import com.konfigthis.client.api.ImageApi;
import com.konfigthis.client.api.IngressoApi;
import com.konfigthis.client.api.LdapServersApi;
import com.konfigthis.client.api.LogosApi;
import com.konfigthis.client.api.ManagedServiceProviderApi;
import com.konfigthis.client.api.MicrosoftMdmApi;
import com.konfigthis.client.api.Office365Api;
import com.konfigthis.client.api.Office365ImportApi;
import com.konfigthis.client.api.OrganizationsApi;
import com.konfigthis.client.api.PasswordManagerApi;
import com.konfigthis.client.api.PoliciesApi;
import com.konfigthis.client.api.PolicyGroupAssociationsApi;
import com.konfigthis.client.api.PolicyGroupMembersMembershipApi;
import com.konfigthis.client.api.PolicyGroupTemplatesApi;
import com.konfigthis.client.api.PolicyGroupsApi;
import com.konfigthis.client.api.PolicytemplatesApi;
import com.konfigthis.client.api.ProvidersApi;
import com.konfigthis.client.api.PushVerificationApi;
import com.konfigthis.client.api.RadiusServersApi;
import com.konfigthis.client.api.ScimImportApi;
import com.konfigthis.client.api.SambaDomainsApi;
import com.konfigthis.client.api.SoftwareAppsApi;
import com.konfigthis.client.api.SubscriptionsApi;
import com.konfigthis.client.api.SystemGroupAssociationsApi;
import com.konfigthis.client.api.SystemGroupMembersMembershipApi;
import com.konfigthis.client.api.SystemGroupsApi;
import com.konfigthis.client.api.SystemInsightsApi;
import com.konfigthis.client.api.SystemsApi;
import com.konfigthis.client.api.SystemsOrganizationSettingsApi;
import com.konfigthis.client.api.UserGroupAssociationsApi;
import com.konfigthis.client.api.UserGroupMembersMembershipApi;
import com.konfigthis.client.api.UserGroupsApi;
import com.konfigthis.client.api.UsersApi;
import com.konfigthis.client.api.WorkdayImportApi;
import com.konfigthis.client.api.FdeApi;

public class JumpCloud {
    private ApiClient apiClient;
    public final ActiveDirectoryApi activeDirectory;
    public final AdministratorsApi administrators;
    public final AggregatedPolicyStatsApi aggregatedPolicyStats;
    public final AppleMdmApi appleMdm;
    public final ApplicationsApi applications;
    public final AuthenticationPoliciesApi authenticationPolicies;
    public final BulkJobRequestsApi bulkJobRequests;
    public final CommandsApi commands;
    public final CustomEmailsApi customEmails;
    public final DirectoriesApi directories;
    public final DuoApi duo;
    public final FeatureTrialsApi featureTrials;
    public final GSuiteApi gSuite;
    public final GSuiteImportApi gSuiteImport;
    public final GoogleEmmApi googleEmm;
    public final GraphApi graph;
    public final GroupsApi groups;
    public final IpListsApi ipLists;
    public final IdentityProvidersApi identityProviders;
    public final ImageApi image;
    public final IngressoApi ingresso;
    public final LdapServersApi ldapServers;
    public final LogosApi logos;
    public final ManagedServiceProviderApi managedServiceProvider;
    public final MicrosoftMdmApi microsoftMdm;
    public final Office365Api office365;
    public final Office365ImportApi office365Import;
    public final OrganizationsApi organizations;
    public final PasswordManagerApi passwordManager;
    public final PoliciesApi policies;
    public final PolicyGroupAssociationsApi policyGroupAssociations;
    public final PolicyGroupMembersMembershipApi policyGroupMembersMembership;
    public final PolicyGroupTemplatesApi policyGroupTemplates;
    public final PolicyGroupsApi policyGroups;
    public final PolicytemplatesApi policytemplates;
    public final ProvidersApi providers;
    public final PushVerificationApi pushVerification;
    public final RadiusServersApi radiusServers;
    public final ScimImportApi scimImport;
    public final SambaDomainsApi sambaDomains;
    public final SoftwareAppsApi softwareApps;
    public final SubscriptionsApi subscriptions;
    public final SystemGroupAssociationsApi systemGroupAssociations;
    public final SystemGroupMembersMembershipApi systemGroupMembersMembership;
    public final SystemGroupsApi systemGroups;
    public final SystemInsightsApi systemInsights;
    public final SystemsApi systems;
    public final SystemsOrganizationSettingsApi systemsOrganizationSettings;
    public final UserGroupAssociationsApi userGroupAssociations;
    public final UserGroupMembersMembershipApi userGroupMembersMembership;
    public final UserGroupsApi userGroups;
    public final UsersApi users;
    public final WorkdayImportApi workdayImport;
    public final FdeApi fde;

    public JumpCloud() {
        this(null);
    }

    public JumpCloud(Configuration configuration) {
        this.apiClient = new ApiClient(null, configuration);
        this.activeDirectory = new ActiveDirectoryApi(this.apiClient);
        this.administrators = new AdministratorsApi(this.apiClient);
        this.aggregatedPolicyStats = new AggregatedPolicyStatsApi(this.apiClient);
        this.appleMdm = new AppleMdmApi(this.apiClient);
        this.applications = new ApplicationsApi(this.apiClient);
        this.authenticationPolicies = new AuthenticationPoliciesApi(this.apiClient);
        this.bulkJobRequests = new BulkJobRequestsApi(this.apiClient);
        this.commands = new CommandsApi(this.apiClient);
        this.customEmails = new CustomEmailsApi(this.apiClient);
        this.directories = new DirectoriesApi(this.apiClient);
        this.duo = new DuoApi(this.apiClient);
        this.featureTrials = new FeatureTrialsApi(this.apiClient);
        this.gSuite = new GSuiteApi(this.apiClient);
        this.gSuiteImport = new GSuiteImportApi(this.apiClient);
        this.googleEmm = new GoogleEmmApi(this.apiClient);
        this.graph = new GraphApi(this.apiClient);
        this.groups = new GroupsApi(this.apiClient);
        this.ipLists = new IpListsApi(this.apiClient);
        this.identityProviders = new IdentityProvidersApi(this.apiClient);
        this.image = new ImageApi(this.apiClient);
        this.ingresso = new IngressoApi(this.apiClient);
        this.ldapServers = new LdapServersApi(this.apiClient);
        this.logos = new LogosApi(this.apiClient);
        this.managedServiceProvider = new ManagedServiceProviderApi(this.apiClient);
        this.microsoftMdm = new MicrosoftMdmApi(this.apiClient);
        this.office365 = new Office365Api(this.apiClient);
        this.office365Import = new Office365ImportApi(this.apiClient);
        this.organizations = new OrganizationsApi(this.apiClient);
        this.passwordManager = new PasswordManagerApi(this.apiClient);
        this.policies = new PoliciesApi(this.apiClient);
        this.policyGroupAssociations = new PolicyGroupAssociationsApi(this.apiClient);
        this.policyGroupMembersMembership = new PolicyGroupMembersMembershipApi(this.apiClient);
        this.policyGroupTemplates = new PolicyGroupTemplatesApi(this.apiClient);
        this.policyGroups = new PolicyGroupsApi(this.apiClient);
        this.policytemplates = new PolicytemplatesApi(this.apiClient);
        this.providers = new ProvidersApi(this.apiClient);
        this.pushVerification = new PushVerificationApi(this.apiClient);
        this.radiusServers = new RadiusServersApi(this.apiClient);
        this.scimImport = new ScimImportApi(this.apiClient);
        this.sambaDomains = new SambaDomainsApi(this.apiClient);
        this.softwareApps = new SoftwareAppsApi(this.apiClient);
        this.subscriptions = new SubscriptionsApi(this.apiClient);
        this.systemGroupAssociations = new SystemGroupAssociationsApi(this.apiClient);
        this.systemGroupMembersMembership = new SystemGroupMembersMembershipApi(this.apiClient);
        this.systemGroups = new SystemGroupsApi(this.apiClient);
        this.systemInsights = new SystemInsightsApi(this.apiClient);
        this.systems = new SystemsApi(this.apiClient);
        this.systemsOrganizationSettings = new SystemsOrganizationSettingsApi(this.apiClient);
        this.userGroupAssociations = new UserGroupAssociationsApi(this.apiClient);
        this.userGroupMembersMembership = new UserGroupMembersMembershipApi(this.apiClient);
        this.userGroups = new UserGroupsApi(this.apiClient);
        this.users = new UsersApi(this.apiClient);
        this.workdayImport = new WorkdayImportApi(this.apiClient);
        this.fde = new FdeApi(this.apiClient);
    }

}