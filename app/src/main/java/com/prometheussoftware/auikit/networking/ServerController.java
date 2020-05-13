package com.prometheussoftware.auikit.networking;

public abstract class ServerController <C extends ConverterFactory> implements ConverterProtocol {

    @Override public C converter() {
        return null;
    }

    /** Subclass must implement like:
     * <code>
     *     NetworkManager.getInstance().setBaseURL(App.constants().BaseURL());
     * </code>
     * */
    @Override public abstract void initialize();
}
