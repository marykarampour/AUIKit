package com.prometheussoftware.auikit.common;

public interface LifeCycleDelegate {

    interface ViewController {

        /** This is where subclasses should create their custom view hierarchy.
         * Should never be called directly. */
        void loadView();

        void viewWillUnload();

        /** Called after the view controller's view is released and set to nil.
         * For example, a memory warning which causes the view to be purged.*/
        void viewDidUnload();

        /** Called after the view controller's view is loaded.
         * Subclass must call super first */
        void viewDidLoad();

        /** Called when the view is about to be made visible.
         * Default does nothing */
        default void viewWillAppear(boolean animated) {}

        /** Called when the view has been fully transitioned onto the screen.
         * Default does nothing */
        default void viewDidAppear(boolean animated) {}

        /** Called when the view is dismissed, covered or otherwise hidden.
         * Default does nothing */
        default void viewWillDisappear(boolean animated) {}

        /** Called after the view was dismissed, covered or otherwise hidden.
         * Default does nothing */
        default void viewDidDisappear(boolean animated) {}

        /** Called just before the view controller's view's layoutSubviews method is invoked.
         * Subclasses can implement as necessary. The default is a nop. */
        default void viewWillLayoutSubviews() {}

        /** Called just after the view controller's view's layoutSubviews method is invoked.
         * Subclasses can implement as necessary. The default is a nop. */
        default void viewDidLayoutSubviews() {}
    }

    interface View {

        /** Called after view is detached from window. */
        void viewIsLoaded();

        /** Called after view is attached to window and before it is loaded.
         * Subclass must call super first */
        void viewWillBeUnloaded();

        /** Called before view detached from window. */
        void viewWillBeLoaded();

        /** Called after view is detached from widow and unloaded.
         * Subclass must call super first */
        void viewIsUnloaded();
    }
}
