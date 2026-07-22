package com.prometheussoftware.auikit.classes;

public interface UITargetDelegate {

    interface Sender { }

    @FunctionalInterface
    interface TouchDown extends UITargetDelegate {
        void controlPressed(Sender sender);
    }

    @FunctionalInterface
    interface TouchUp extends UITargetDelegate {
        void controlReleased(Sender sender);
    }

    @FunctionalInterface
    interface KeyDown extends UITargetDelegate {
        void keyPressed(Sender sender, int keyCode);
    }

    @FunctionalInterface
    interface KeyUp extends UITargetDelegate {
        void keyReleased(Sender sender, int keyCode);
    }
}
