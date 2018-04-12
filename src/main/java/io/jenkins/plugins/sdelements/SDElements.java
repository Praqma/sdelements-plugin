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
import jenkins.tasks.SimpleBuildStep;
import net.sf.json.JSONObject;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mads on 4/12/18.
 */
public class SDElements extends Recorder implements SimpleBuildStep {

    private int projectId;

    @DataBoundConstructor
    public SDElements(int projectId) {
        this.projectId = projectId;
    }

    public int getProjectId() {
        return projectId;
    }

    @Override
    public void perform(@Nonnull Run<?, ?> run, @Nonnull FilePath filePath, @Nonnull Launcher launcher, @Nonnull TaskListener taskListener) throws InterruptedException, IOException {

    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return null;
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
    }
}
