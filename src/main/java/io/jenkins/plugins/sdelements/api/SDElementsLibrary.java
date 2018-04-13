package io.jenkins.plugins.sdelements.api;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.HashMap;

public class SDElementsLibrary {

    private String accessKey;
    private String url;
    private String apiVersion = "v2";

    public SDElementsLibrary(String accessKey, String url) {
        this.accessKey = accessKey;
        this.url = url;
    }

    public HttpResponse<JsonNode> getProject(int id) throws UnirestException {
        String projects = url + "/api/" + apiVersion + "/projects/";
        System.out.println(projects);
        HashMap<String,String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization","Token "+accessKey);
        HttpResponse<JsonNode> resp = Unirest.get(projects).
                headers(headers).
                queryString("application", id).asJson();
        return resp;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }
}
