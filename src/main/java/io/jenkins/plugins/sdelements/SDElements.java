package io.jenkins.plugins.sdelements;

import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.domains.DomainRequirement;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.util.ListBoxModel;
import io.jenkins.plugins.sdelements.api.RiskPolicyCompliance;
import io.jenkins.plugins.sdelements.api.SDElementsLibrary;
import io.jenkins.plugins.sdelements.api.SDLibraryException;
import io.jenkins.plugins.sdelements.api.UnhandledSDLibraryException;
import jenkins.model.Jenkins;
import jenkins.tasks.SimpleBuildStep;
import net.sf.json.JSONObject;
import org.jenkinsci.Symbol;
import org.jenkinsci.plugins.plaincredentials.StringCredentials;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by mads on 4/12/18.
 */
public class SDElements extends Publisher implements SimpleBuildStep {

    private int projectId;
    private String connectionName;
    private boolean markUnstable;
    private static Logger LOG = java.util.logging.Logger.getLogger(SDElements.class.getName());

    @DataBoundConstructor
    public SDElements(int projectId, String connectionName) {
        this.projectId = projectId;
        this.connectionName = connectionName;
    }

    public int getProjectId() {
        return projectId;
    }

    public String getConnectionName() {
        return connectionName;
    }

    private void setBuildResult(boolean markNonComplianceAsUnstable, RiskPolicyCompliance compliance, Run<?,?> run) {
        if(compliance == RiskPolicyCompliance.FAIL && markNonComplianceAsUnstable) {
            if(run != null) {
                run.setResult(Result.UNSTABLE);
            }
        } else {
            if(run != null && compliance != RiskPolicyCompliance.PASS) {
                run.setResult(Result.FAILURE);
            }
        }
    }

    @Override
    public void perform(@Nonnull Run<?, ?> run, @Nonnull FilePath filePath, @Nonnull Launcher launcher, @Nonnull TaskListener taskListener) throws InterruptedException, IOException {
        SDElements.DescriptorImpl imp = null;
        if(Jenkins.getInstance() != null) {
            imp = (SDElements.DescriptorImpl)Jenkins.getInstance().getDescriptorOrDie(SDElements.class);
        }
        SDElementsConnection conn = null;
        if(imp != null) {
            conn = imp.getByName(connectionName);
        }
        RiskPolicyCompliance riskIndicator = null;
        String url = null;
        if(conn != null) {
            String credId = conn.getCredentialsId();
            StringCredentials cred = CredentialsProvider.findCredentialById(credId, StringCredentials.class, run, Collections.<DomainRequirement>emptyList());
            if(cred != null) {
                SDElementsLibrary lib = new SDElementsLibrary(cred.getSecret().getPlainText(), conn.getConnectionString());
                try {
                    riskIndicator = lib.getProjectCompliance(projectId);
                    url = lib.getProjectUrl(projectId);
                } catch (UnhandledSDLibraryException unhandled) {
                    taskListener.getLogger().println(unhandled.getMessage());
                    LOG.log(Level.SEVERE, "Unhandled error caught", unhandled);
                    LOG.log(Level.INFO, unhandled.getMessage());
                    LOG.log(Level.INFO, unhandled.getCause().getMessage());
                } catch (SDLibraryException ex) {
                    taskListener.getLogger().println(ex.getMessage());
                    LOG.log(Level.WARNING, "Exception for SD Elements", ex);
                }
            }
        } else {
            run.addAction(new SDElementsRiskIndicatorBuildAction(riskIndicator, url, null));
            throw new IllegalStateException("Improper connection selected. This is a required setting");
        }
        taskListener.getLogger().println("SD Elements risk status: "+(riskIndicator == null ? "Undetermined" : riskIndicator));
        run.addAction(new SDElementsRiskIndicatorBuildAction(riskIndicator, url, conn.getConnectionString()));
        setBuildResult(markUnstable, riskIndicator, run);
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    public boolean isMarkUnstable() {
        return markUnstable;
    }

    @DataBoundSetter
    public void setMarkUnstable(boolean markUnstable) {
        this.markUnstable = markUnstable;
    }

    @Symbol("sdelements")
    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        private List<SDElementsConnection> connections = new ArrayList<>();

        @DataBoundSetter
        public void setConnections(List<SDElementsConnection> connections) {
            this.connections = connections;
        }

        public List<SDElementsConnection> getConnections() {
            return connections;
        }

        public DescriptorImpl() {
            load();
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
            req.bindJSON(this, json);
            save();
            return true;
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return "SD Elements";
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        public ListBoxModel doFillConnectionNameItems() {
            ListBoxModel lbm = new ListBoxModel();
            for(SDElementsConnection conn : connections) {
                lbm.add(conn.getConnectionName()+ " ("+conn.getConnectionString()+")", conn.getConnectionName());
            }
            return lbm;
        }

        @CheckForNull
        public SDElementsConnection getByName(String name) {
            for(SDElementsConnection conn : connections) {
                if(conn.getConnectionName().equals(name)) {
                    return conn;
                }
            }
            return null;
        }

    }
}
