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

    public SDLibraryException(String message, Throwable cause) {
        super(message, cause);
    }

    public SDLibraryException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        if(resp != null) {
            return String.format("%s%n%s", super.getMessage(), resp.getStatus() + " " + resp.getStatusText());
        } else {
            return super.getMessage();
        }
    }
}
