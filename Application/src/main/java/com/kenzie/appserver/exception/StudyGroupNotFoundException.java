package com.kenzie.appserver.exception;

import java.util.HashMap;
import java.util.Map;

public class StudyGroupNotFoundException extends RuntimeException{
    public StudyGroupNotFoundException(String msg) {
        super(msg);
    }

    public Map<String, Object> errorPayload() {
        Map<String, Object> errorPayload = new HashMap();
        errorPayload.put("errorType", "invalid_data");
        errorPayload.put("message", this.getMessage());
        return errorPayload;
    }
}
