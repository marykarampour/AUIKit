package com.prometheussoftware.auikit.uiview.protocols;

public interface UIViewProtocol {

    /** Subclass must implement to initialize subviews, call super.initView() first
     * @apiNote Never call directly */
    void initView();

    /** Subclass must implement to add subviews, call super.loadView() first
     * @apiNote Never call directly */
    void loadView();

    /** Subclass must implement to handle events after view is attached to window
     * This is called in onAttachedToWindow
     * Call super.viewDidLoad() first
     * @apiNote Never call directly */
    void viewDidLoad();

    /** Subclass must implement to handle events after view is detached from window
     * This is called in onDetachedFromWindow
     * Call super.unLoadView() first
     * @apiNote Never call directly */
    void unLoadView();

    /** Subclass must implement to constraint subviews, call super.constraintLayout() first
     * @apiNote Never call directly */
    void constraintLayout();
}
