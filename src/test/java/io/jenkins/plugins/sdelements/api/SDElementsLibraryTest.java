package io.jenkins.plugins.sdelements.api;

import org.junit.Assume;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Created by mads on 4/18/18.
 */
public class SDElementsLibraryTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    String token = System.getProperty("sdtoken");
    String url = System.getProperty("sdurl");

    @Test
    public void testInvalidToken() throws Exception {
        Assume.assumeNotNull(token);
        expectedException.expect(SDLibraryException.class);
        expectedException.expectMessage("Invalid token in credentials");
        SDElementsLibrary lib = new SDElementsLibrary("bosod857fj", url);
        RiskPolicyCompliance compliance = lib.getProjectCompliance(1743);
    }

    @Test
    public void testUnknownHost() throws Exception {
        Assume.assumeNotNull(token);
        expectedException.expect(SDLibraryException.class);
        String invalidHost = "https://bogus.site.somewhere.com";
        expectedException.expectMessage("Host not found: "+invalidHost);
        SDElementsLibrary lib = new SDElementsLibrary(token, invalidHost);
        RiskPolicyCompliance compliance = lib.getProjectCompliance(1743);
    }

    @Test
    public void testWrongProjectId() throws Exception {
        Assume.assumeNotNull(token);
        expectedException.expect(SDLibraryException.class);
        SDElementsLibrary lib = new SDElementsLibrary(token, url);
        String idString = "Project with id "+2000+" Not found.";
        expectedException.expectMessage(idString);
        RiskPolicyCompliance compliance = lib.getProjectCompliance(2000);
    }

}
