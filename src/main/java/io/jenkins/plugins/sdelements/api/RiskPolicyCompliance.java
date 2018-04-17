package io.jenkins.plugins.sdelements.api;

/**
 * Created by mads on 4/13/18.
 */
public enum RiskPolicyCompliance {
    PASS, //In case of survey compliance
    FAIL, //In case of survey failure
    UNDETERMINED //In case of incomplete survey
}
