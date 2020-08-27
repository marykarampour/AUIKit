package com.prometheussoftware.auikit.networking;

interface ConverterProtocol <C extends ConverterFactory> {

    C converter();
    void initialize();
}
