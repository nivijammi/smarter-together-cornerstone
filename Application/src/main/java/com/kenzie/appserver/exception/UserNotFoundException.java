package com.kenzie.appserver.exception;

import javax.management.RuntimeErrorException;
import java.util.HashMap;
import java.util.Map;

public class UserNotFoundException extends RuntimeErrorException {
    public UserNotFoundException(Error msg) {
        super(msg);
    }

    public Map<String, Object> errorPayload() {
        Map<String, Object> errorPayload = new HashMap();
        errorPayload.put("errorType", "invalid_data");
        errorPayload.put("message", this.getMessage());
        return errorPayload;
    }
}
