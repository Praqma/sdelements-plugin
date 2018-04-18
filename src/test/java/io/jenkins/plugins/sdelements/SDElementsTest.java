package io.jenkins.plugins.sdelements;

import com.cloudbees.plugins.credentials.CredentialsScope;
import com.cloudbees.plugins.credentials.SystemCredentialsProvider;
import hudson.model.Cause;
import hudson.model.FreeStyleProject;
import hudson.model.Result;
import hudson.model.Run;
import hudson.util.Secret;
import io.jenkins.plugins.sdelements.api.RiskPolicyCompliance;
import org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl;
import org.junit.Assume;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import java.util.Arrays;
import java.util.UUID;

/**
 * Created by mads on 4/17/18.
 */
public class SDElementsTest {

    @Rule
    public JenkinsRule jr = new JenkinsRule();

    private String token = System.getProperty("sdtoken");
    private String url = System.getProperty("sdurl");

    private FreeStyleProject createProject(String connectionName, int id) throws Exception {
        FreeStyleProject fsp = jr.createFreeStyleProject(connectionName + UUID.randomUUID().toString());

        SDElements sdelements = new SDElements(id, connectionName);

        SystemCredentialsProvider scp = (SystemCredentialsProvider)jr.getInstance().getExtensionList("com.cloudbees.plugins.credentials.SystemCredentialsProvider").get(0);
        scp.getCredentials().add(new StringCredentialsImpl(CredentialsScope.GLOBAL,"cred-valid","Valid credentials", Secret.fromString(token)));
        scp.getCredentials().add(new StringCredentialsImpl(CredentialsScope.GLOBAL,"cred-invalid","Valid credentials", Secret.fromString("df9di34858fj")));
        scp.save();

        SDElements.DescriptorImpl impl = (SDElements.DescriptorImpl)sdelements.getDescriptor();
        SDElementsConnection validConnection = new SDElementsConnection(url,
                "cred-valid",
                "valid-connection");
        SDElementsConnection wrongCredentials = new SDElementsConnection(url,
                "cred-invalid",
                "invalid-connection");
        SDElementsConnection wrongHostName = new SDElementsConnection("https://www.no-sh3re.net",
                "cred-valid",
                "wrong-connection");

        impl.setConnections(Arrays.asList(validConnection, wrongCredentials, wrongHostName));
        impl.save();

        fsp.getPublishersList().add(new SDElements(id, connectionName));
        return fsp;
    }

    @Test
    public void testActionCanBeSaved() throws Exception {
        FreeStyleProject fp = jr.createFreeStyleProject();
        Run r = jr.buildAndAssertSuccess(fp);
        r.addAction(new SDElementsRiskIndicatorBuildAction(RiskPolicyCompliance.FAIL));
        r.save();
        //This test is pass if no error is thrown.
    }

    @Test
    public void testPassingProject() throws Exception {
        Assume.assumeNotNull(token);
        Assume.assumeNotNull(url);
        FreeStyleProject freeStyleProject = createProject("valid-connection", 1742);
        jr.buildAndAssertSuccess(freeStyleProject);
    }

    @Test
    public void testFailingProject() throws Exception {
        Assume.assumeNotNull(token);
        Assume.assumeNotNull(url);
        FreeStyleProject fsp = createProject("valid-connection", 1739);
        jr.assertBuildStatus(Result.FAILURE, fsp.scheduleBuild2(0, new Cause.UserIdCause()));
    }

    @Test
    public void testUndeterminedProject() throws Exception {
        Assume.assumeNotNull(token);
        Assume.assumeNotNull(url);
        FreeStyleProject fsp = createProject("valid-connection", 1743);
        jr.assertBuildStatus(Result.FAILURE, fsp.scheduleBuild2(0, new Cause.UserIdCause()));
    }

    @Test
    public void testIncorrectHostName() throws Exception {
        Assume.assumeNotNull(token);
        Assume.assumeNotNull(url);
        FreeStyleProject fsp = createProject("wrong-connection", 1742);
        jr.assertBuildStatus(Result.FAILURE, fsp.scheduleBuild2(0, new Cause.UserIdCause()));
    }

    @Test
    public void testIncorrectProjectId() throws Exception {
        Assume.assumeNotNull(token);
        Assume.assumeNotNull(url);
        FreeStyleProject fsp = createProject("valid-connection", 9999);
        jr.assertBuildStatus(Result.FAILURE, fsp.scheduleBuild2(0, new Cause.UserIdCause()));
    }

    @Test
    public void testFailedAuthentication() throws Exception {
        Assume.assumeNotNull(token);
        Assume.assumeNotNull(url);
        FreeStyleProject fsp = createProject("invalid-connection", 1742);
        jr.assertBuildStatus(Result.FAILURE, fsp.scheduleBuild2(0, new Cause.UserIdCause()));
    }
}
