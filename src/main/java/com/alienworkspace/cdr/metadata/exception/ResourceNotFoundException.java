package com.alienworkspace.cdr.metadata.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a resource is not found.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Construct a new instance with a message.
     *
     * @param message the message to set
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
