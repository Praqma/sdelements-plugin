package io.jenkins.plugins.sdelements;

import com.cloudbees.plugins.credentials.CredentialsMatcher;
import com.cloudbees.plugins.credentials.CredentialsMatchers;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.common.StandardCredentials;
import com.cloudbees.plugins.credentials.common.StandardListBoxModel;
import com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials;
import com.cloudbees.plugins.credentials.domains.DomainRequirement;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.model.Item;
import hudson.model.ItemGroup;
import hudson.security.ACL;
import hudson.util.ListBoxModel;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import java.util.Collections;
import java.util.List;

/**
 * Class describing a configured connection
 */
public class SDElementsConnection extends AbstractDescribableImpl<SDElementsConnection> {

    private String connectionName;
    private String connectionString;
    private String credentialsId;

    public String getConnectionString() {
        return connectionString;
    }

    public String getCredentialsId() {
        return credentialsId;
    }

    @DataBoundConstructor
    public SDElementsConnection(String connectionString, String credentialsId, String connectionName) {
        this.connectionString = connectionString;
        this.credentialsId = credentialsId;
        this.connectionName = connectionName;
    }

    public String getConnectionName() {
        return connectionName;
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<SDElementsConnection> {
        public String getDisplayName() { return "SDElements connection"; }

        public ListBoxModel doFillCredentialsIdItems(final @AncestorInPath Item item, @QueryParameter String credentialsId) {
            StandardListBoxModel lbm = new StandardListBoxModel();
            return lbm.includeMatchingAs(ACL.SYSTEM,
                    item, StandardCredentials.class,
                    Collections.<DomainRequirement>emptyList(),
                    CredentialsMatchers.always());
        }

    }
}
