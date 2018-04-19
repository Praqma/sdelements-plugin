package io.jenkins.plugins.sdelements.api;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

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

    private HttpResponse<JsonNode> getProject(int id) throws SDLibraryException {
        String projects = url + "/api/" + apiVersion + "/projects/"+id+"/";
        HashMap<String,String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization","Token "+accessKey);
        HttpResponse<JsonNode> resp = null;
        try {
            resp = Unirest.get(projects).
                    headers(headers).asJson();
        } catch (UnirestException e) {
            if(e.getCause() instanceof UnknownHostException) {
                throw new SDLibraryException("Host not found: "+url, e);
            }
            else throw new UnhandledSDLibraryException("Unknown error encountered", e);
        }
        return resp;
    }

    /**
     *
     * @param id Project id on SDElements server
     * @return RiskPolicyCompliance.PASS or RiskPolicyCompliance.FAIL. RiskPolicyCompliance.UNDETERMINED if project survey not completed
     * @throws {@link SDLibraryException} when we determine that we didn't get a correct result from SDElements. Empty result, or access denied
     */
    public RiskPolicyCompliance getProjectCompliance(int id) throws SDLibraryException {
        HttpResponse<JsonNode> node = getProject(id);
        int status = node.getStatus();
        JsonNode body = node.getBody();
        if(status == 404 && body != null && body.getObject().getString("detail").equals("Not found.")) {
            throw new SDLibraryException("Project with id "+id+" Not found", node);
        } else if(status == 401 && body != null && body.getObject().getString("detail").equals("Invalid token.")) {
            throw new SDLibraryException("Invalid token in credentials", node);
        } else {
            JSONObject obj = node.getBody().getObject();
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
                throw new UnhandledSDLibraryException("Unknown response detected for project with id: " + id, node);
            }
        }
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }
}
