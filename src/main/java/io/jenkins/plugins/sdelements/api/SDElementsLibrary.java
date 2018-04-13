package io.jenkins.plugins.sdelements.api;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

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

    private HttpResponse<JsonNode> getProject(int id) throws UnirestException {
        String projects = url + "/api/" + apiVersion + "/projects/"+id+"/";
        HashMap<String,String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization","Token "+accessKey);
        HttpResponse<JsonNode> resp = Unirest.get(projects).
                headers(headers).asJson();
        return resp;
    }

    /**
     *
     * @param id Project id on SDElements server
     * @return RiskPolicyCompliance.PASS or RiskPolicyCompliance.FAIL. RiskPolicyCompliance.UNDETERMINED is never
     *         returned as it is used as a failure state and a default value prior to check.
     * @throws UnirestException when our rest api decides to throw an error. Only seen happening when point to a non existing host
     * @throws SDLibraryException when we determine that we didn't get a correct result from SDElements. Empty result, or access denied
     */
    public RiskPolicyCompliance getProjectCompliance(int id) throws UnirestException, SDLibraryException {
        HttpResponse<JsonNode> node = getProject(id);
        if(node.getStatus() != 200) {
            throw new SDLibraryException("Failed to retrieve project compliance level for project with id "+id, node);
        }
        JSONObject obj = node.getBody().getObject();
        if(obj != null) {
             if(obj.getBoolean("risk_policy_compliant")) {
                 return RiskPolicyCompliance.PASS;
             } else {
                 return RiskPolicyCompliance.FAIL;
             }
        } else {
            throw new SDLibraryException("Unable to find project with id: "+id, node);
        }
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }
}
