package io.jenkins.plugins.sdelements.api;

/**
 * Created by mads on 4/13/18.
 */
public enum RiskPolicyCompliance {
    PASS("Pass"), //In case of survey compliance
    FAIL("Fail"), //In case of survey failure
    UNDETERMINED("Survey not completed"); //In case of incomplete survey

    private String displayName;

    RiskPolicyCompliance(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
