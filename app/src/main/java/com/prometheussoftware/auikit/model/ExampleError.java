package com.prometheussoftware.auikit.model;

public class ExampleError extends BaseModel {

    public String id;
    public String description;
    public int code;

    public String userMessage() {
        return code < 300 ? description : "";
    }

    public static ExampleError errorWithError(java.lang.Error error) {
        ExampleError err = new ExampleError();
        err.id = "";
        err.description = error.getLocalizedMessage();
        err.code = 0;
        return err;
    }

    public static ExampleError errorWithMessage(String message) {
        ExampleError err = new ExampleError();
        err.id = "";
        err.description = message;
        err.code = 0;
        return err;
    }
}
