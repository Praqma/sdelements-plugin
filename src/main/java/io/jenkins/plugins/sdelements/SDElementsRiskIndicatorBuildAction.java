package io.jenkins.plugins.sdelements;

import hudson.model.Action;
import hudson.model.Run;
import jenkins.tasks.SimpleBuildStep;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by mads on 4/12/18.
 */
public class SDElementsRiskIndicatorBuildAction implements Action, SimpleBuildStep.LastBuildAction {

    private transient Run<?,?> build;
    private boolean riskIndicator;
    private List<SDElementsRiskIndicatorProjectAction> projectActions;

    public SDElementsRiskIndicatorBuildAction(Run<?,?> build, boolean riskIndicator) {
        this.build = build;
        List<SDElementsRiskIndicatorProjectAction> actions = new ArrayList<>();
        actions.add(new SDElementsRiskIndicatorProjectAction(build.getParent(), riskIndicator));
        this.projectActions = actions;
        this.riskIndicator = riskIndicator;
    }

    @Override
    public Collection<? extends Action> getProjectActions() {
        return this.projectActions;
    }

    @Override
    public String getIconFileName() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public String getUrlName() {
        return null;
    }

    public boolean isRiskIndicator() {
        return riskIndicator;
    }

    public void setRiskIndicator(boolean riskIndicator) {
        this.riskIndicator = riskIndicator;
    }
}
