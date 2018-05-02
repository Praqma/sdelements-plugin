package io.jenkins.plugins.sdelements;

import hudson.model.Action;
import io.jenkins.plugins.sdelements.api.RiskPolicyCompliance;

/**
 * Created by mads on 4/12/18.
 */
public class SDElementsRiskIndicatorProjectAction implements Action {

    private RiskPolicyCompliance riskIndicator;
    private String projectUrl;
    private String baseUrl;

    public SDElementsRiskIndicatorProjectAction(RiskPolicyCompliance riskIndicator, String projectUrl, String baseUrl) {
        this.riskIndicator = riskIndicator;
        this.projectUrl = projectUrl;
        this.baseUrl = baseUrl;
    }

    @Override
    public String getIconFileName() {
        return "/plugin/sdelements/icons/sdelements_icon.png";
    }

    @Override
    public String getDisplayName() {
        return "SD Elements";
    }

    @Override
    public String getUrlName() {
        return baseUrl;
    }

    public RiskPolicyCompliance getRiskIndicator() {
        return riskIndicator;
    }

    public void setRiskIndicator(RiskPolicyCompliance riskIndicator) {
        this.riskIndicator = riskIndicator;
    }

    public String getProjectUrl() {
        return projectUrl;
    }

    public void setProjectUrl(String projectUrl) {
        this.projectUrl = projectUrl;
    }

    public String getStatusIcon(RiskPolicyCompliance risk) {
        if(risk == RiskPolicyCompliance.FAIL) {
            return "/plugin/sdelements/icons/fail.png";
        } else if(risk == RiskPolicyCompliance.PASS) {
            return "/plugin/sdelements/icons/pass.png";
        } else {
            return "/plugin/sdelements/icons/none.png";
        }
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
