package com.prometheussoftware.auikit.classes;

import android.os.Handler;

import com.prometheussoftware.auikit.callback.CompletionCallback;

public class Polling {

    private Handler handler = new Handler();
    private Runnable runnable;
    private CompletionCallback callback;
    private long delay = 1000;
    private boolean active;

    public Polling(CompletionCallback callback) {
        this.runnable = () -> {
            callback.done();
            handler.postDelayed(runnable, delay);
        };
    }

    public Polling(CompletionCallback callback, long delay) {
        this.runnable = () -> {
            callback.done();
            handler.postDelayed(runnable, delay);
        };
        this.delay = delay;
    }

    public void start() {
        active = true;
        handler.post(runnable);
    }

    public void stop() {
        active = false;
        handler.removeCallbacks(runnable);
    }

    public boolean isActive() {
        return active;
    }
}
