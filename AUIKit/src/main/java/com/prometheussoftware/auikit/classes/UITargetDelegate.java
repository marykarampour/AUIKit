package com.prometheussoftware.auikit.classes;

public interface UITargetDelegate {

    public interface Sender { }

    @FunctionalInterface
    public interface TouchDown extends UITargetDelegate {
        void controlPressed(Sender sender);
    }

    @FunctionalInterface
    public interface TouchUp extends UITargetDelegate {
        void controlReleased(Sender sender);
    }

    @FunctionalInterface
    public interface KeyDown extends UITargetDelegate {
        void keyPressed(Sender sender, int keyCode);
    }

    @FunctionalInterface
    public interface KeyUp extends UITargetDelegate {
        void keyReleased(Sender sender, int keyCode);
    }
}
