package com.prometheussoftware.auikit.callback;

public interface SuccessErrorCallback <E extends Error> {
    void done(boolean success, E error);
}
