package io.jenkins.plugins.sdelements;

import hudson.model.Job;
import hudson.model.ProminentProjectAction;

/**
 * Created by mads on 4/12/18.
 */
public class SDElementsRiskIndicatorProjectAction implements ProminentProjectAction {

    private transient Job<?,?> job;
    private boolean riskIndicator;

    public SDElementsRiskIndicatorProjectAction(Job<?,?> job, boolean riskIndicator) {
        this.job = job;
        this.riskIndicator = riskIndicator;
    }

    @Override
    public String getIconFileName() {
        return riskIndicator ? "/plugin/sdelements/icons/pass.png" : "/plugin/sdelements/icons/fail.png";
    }

    @Override
    public String getDisplayName() {
        return "SDElements: "+(riskIndicator ? "PASS" : "FAIL");
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
