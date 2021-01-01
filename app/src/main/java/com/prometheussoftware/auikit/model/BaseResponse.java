package com.prometheussoftware.auikit.model;

import com.prometheussoftware.auikit.networking.ServerResponse;

import java.util.ArrayList;

public class BaseResponse extends ServerResponse {

    public String status;
    public String code;
    public ArrayList<ExampleError> errors;

    public Object object() {
        return status;
    }

    public Error error() {
        String messages = "";
        for (ExampleError message : errors) {
            messages += message.description;
        }
        return new Error(messages);
    }

    public boolean statusIsOk() {
        return status.equalsIgnoreCase("OK");
    }
}
