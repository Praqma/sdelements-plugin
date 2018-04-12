package io.jenkins.plugins.sdelements;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Descriptor;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.util.ListBoxModel;
import jenkins.model.Jenkins;
import jenkins.tasks.SimpleBuildStep;
import net.sf.json.JSONObject;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mads on 4/12/18.
 */
public class SDElements extends Recorder implements SimpleBuildStep {

    private int projectId;
    private String connectionName;

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

    @Override
    public void perform(@Nonnull Run<?, ?> run, @Nonnull FilePath filePath, @Nonnull Launcher launcher, @Nonnull TaskListener taskListener) throws InterruptedException, IOException {
        taskListener.getLogger().println("Doing SDElements integration");
        SDElements.DescriptorImpl imp = (SDElements.DescriptorImpl)Jenkins.getInstance().getDescriptor(SDElements.class);
        taskListener.getLogger().println("With credentials id: "+imp.getByName(connectionName).getCredentialsId());
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
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
