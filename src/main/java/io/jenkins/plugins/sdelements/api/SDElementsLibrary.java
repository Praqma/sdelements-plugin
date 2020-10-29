package io.jenkins.plugins.sdelements.api;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONObject;
import org.json.JSONException;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Objects;


public class SDElementsLibrary {

    private final String accessKey;
    private final String url;
    private String apiVersion = "v2";

    public SDElementsLibrary(String accessKey, String url) {
        this.accessKey = Objects.requireNonNull(accessKey, "accessKey must not be null");
        this.url = Objects.requireNonNull(url, "url must not be null");
    }

    private JsonNode getProject(int id) throws SDLibraryException {
        String projects = url + "/api/" + apiVersion + "/projects/"+id+"/";
        HashMap<String,String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization","Token "+accessKey);

        HttpResponse<String> response = null;
        try {
            response = Unirest.get(projects)
                              .headers(headers)
                              .asString();
        } catch (UnirestException e) {
           if (e.getCause() instanceof UnknownHostException) {
                throw new SDLibraryException("Host not found: " + url, e);
            }
            String errorMessage = "Unknown exception encountered.";
            if (e.getCause() != null) {
                errorMessage = (
                    errorMessage +
                    "\nException message: " +
                    e.getCause().getMessage()
                );
            }
            throw new UnhandledSDLibraryException(errorMessage, e);
        }

        // attempt to manually parse the original string so we can stil pass
        // along the original body in case of error.
        String body = response.getBody();
        JsonNode node = null;
        try {
            node = new JsonNode(body);
        } catch (JSONException e) {
            throw new SDLibraryException(
                "Unable to parse non-JSON API response.: \n" + body,
                response
            );
        }

        // Raise error for any known error conditions for the request
        int status = response.getStatus();
        if(status == 404 && node != null && node.getObject().getString("detail").equals("Not found.")) {
            throw new SDLibraryException("Project with id "+id+" Not found", response);
        }

        if(status == 401 && node != null && node.getObject().getString("detail").equals("Invalid token.")) {
            throw new SDLibraryException("Invalid token in credentials", response);
        }

        return node;
    }

    public String getProjectUrl(int id) throws SDLibraryException {
        JsonNode node = getProject(id);
        return node.getObject().getString("url");
    }

    /**
     *
     * @param id Project id on SDElements server
     * @return RiskPolicyCompliance.PASS or RiskPolicyCompliance.FAIL. RiskPolicyCompliance.UNDETERMINED if project survey not completed
     * @throws SDLibraryException when we determine that we didn't get a correct result from SDElements. Empty result, or access denied
     */
    public RiskPolicyCompliance getProjectCompliance(int id) throws SDLibraryException {
        JsonNode node = getProject(id);
        JSONObject obj = node.getObject();

        if (obj != null) {
            if (obj.isNull("risk_policy_compliant")) {
                return RiskPolicyCompliance.UNDETERMINED;
            }
            if (obj.getBoolean("risk_policy_compliant")) {
                return RiskPolicyCompliance.PASS;
            } else {
                return RiskPolicyCompliance.FAIL;
            }
        } else {
            throw new UnhandledSDLibraryException("Unknown response detected for project with id: " + id);
        }
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }
}
