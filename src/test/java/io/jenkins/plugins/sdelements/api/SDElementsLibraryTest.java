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
        SDElementsLibrary lib = new SDElementsLibrary(token, url);
        RiskPolicyCompliance compliance = lib.getProjectCompliance(1743);
    }

    @Test
    public void testUnknownHost() throws Exception {
        Assume.assumeNotNull(token);
        expectedException.expect(SDLibraryException.class);
        expectedException.expectMessage("Host not found: "+url);
        SDElementsLibrary lib = new SDElementsLibrary(token, url);
        RiskPolicyCompliance compliance = lib.getProjectCompliance(1743);
    }

}
