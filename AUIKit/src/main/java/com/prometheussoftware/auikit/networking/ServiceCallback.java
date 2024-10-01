package com.prometheussoftware.auikit.networking;

public interface ServiceCallback <T, E extends Error> {
    void onSuccess(T result);
    void onFailure(E error);
}
