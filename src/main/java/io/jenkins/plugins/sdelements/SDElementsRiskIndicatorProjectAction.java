package io.jenkins.plugins.sdelements;

import hudson.model.ProminentProjectAction;
import io.jenkins.plugins.sdelements.api.RiskPolicyCompliance;

/**
 * Created by mads on 4/12/18.
 */
public class SDElementsRiskIndicatorProjectAction implements ProminentProjectAction {

    private RiskPolicyCompliance riskIndicator;

    public SDElementsRiskIndicatorProjectAction(RiskPolicyCompliance riskIndicator) {
        this.riskIndicator = riskIndicator;
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
        return "SD Elements: "+riskIndicator;
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
