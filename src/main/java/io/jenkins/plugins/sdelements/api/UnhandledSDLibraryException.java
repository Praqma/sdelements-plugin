package io.jenkins.plugins.sdelements.api;

import com.mashape.unirest.http.HttpResponse;

/**
 * Created by mads on 4/18/18.
 */
public class UnhandledSDLibraryException extends SDLibraryException {

    public UnhandledSDLibraryException(String message, HttpResponse<?> resp) {
        super(message, resp);
    }

    public UnhandledSDLibraryException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnhandledSDLibraryException(String message) {
        super(message);
    }
}
