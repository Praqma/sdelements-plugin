package io.jenkins.plugins.sdelements.api;

import com.mashape.unirest.http.HttpResponse;

/**
 * Created by mads on 4/13/18.
 */
public class SDLibraryException extends Exception {

    private HttpResponse<?> resp;

    public SDLibraryException(String message, HttpResponse<?> resp) {
        super(message);
        this.resp = resp;
    }

    @Override
    public String getMessage() {
        return String.format("%s%n%s",super.getMessage(), resp.getStatus() + " "+ resp.getStatusText());
    }
}
