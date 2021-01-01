package com.prometheussoftware.auikit.networking;

public interface ServiceCallback <T> {
    void onSuccess(T result);
    void onFailure(Error error);
}
