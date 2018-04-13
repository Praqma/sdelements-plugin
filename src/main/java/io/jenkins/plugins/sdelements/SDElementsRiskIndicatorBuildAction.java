package io.jenkins.plugins.sdelements;

import hudson.model.Action;
import io.jenkins.plugins.sdelements.api.RiskPolicyCompliance;
import jenkins.tasks.SimpleBuildStep;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by mads on 4/12/18.
 */
public class SDElementsRiskIndicatorBuildAction implements Action, SimpleBuildStep.LastBuildAction {

    private RiskPolicyCompliance riskIndicator;
    private List<SDElementsRiskIndicatorProjectAction> projectActions;

    public SDElementsRiskIndicatorBuildAction(RiskPolicyCompliance riskIndicator) {
        List<SDElementsRiskIndicatorProjectAction> actions = new ArrayList<>();
        actions.add(new SDElementsRiskIndicatorProjectAction(riskIndicator));
        this.projectActions = actions;
        this.riskIndicator = riskIndicator;
    }

    @Override
    public Collection<? extends Action> getProjectActions() {
        return this.projectActions;
    }

    @Override
    public String getIconFileName() {
        if(riskIndicator == RiskPolicyCompliance.UNDETERMINED) {
            return "/plugin/sdelements/icons/none.png";
        }
        return riskIndicator == RiskPolicyCompliance.PASS ? "/plugin/sdelements/icons/pass.png" : "/plugin/sdelements/icons/fail.png";
    }

    @Override
    public String getDisplayName() {
        return "SDElements";
    }

    @Override
    public String getUrlName() {
        return null;
    }

    public RiskPolicyCompliance getRiskIndicator() {
        return riskIndicator;
    }

    public void setRiskIndicator(RiskPolicyCompliance riskIndicator) {
        this.riskIndicator = riskIndicator;
    }
}
