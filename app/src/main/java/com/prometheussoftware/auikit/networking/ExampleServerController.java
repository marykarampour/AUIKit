package com.prometheussoftware.auikit.networking;

import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.common.EXConstants;

public class ExampleServerController extends ServerController <ExampleConverterFactory> {

    @Override public ExampleConverterFactory converter() {

        return new ExampleConverterFactory.Builder()
                .add(ExampleConverterFactory.Default.class,
                        ExampleConverterFactory.ConverterConstructor.ofType(ExampleConverterFactory.TYPE.EX.intValue()).converter())
                .add(ExampleConverterFactory.EXE.class,
                        ExampleConverterFactory.ConverterConstructor.ofType(ExampleConverterFactory.TYPE.EXE.intValue()).converter())
                .create();
    }

    @Override
    public void initialize() {
        NetworkManager.getInstance().setBaseURL(App.constants().BaseURL(), converter());
        NetworkManager.getInstance().addBaseURL(EXConstants.EXBaseURL(), ExampleConverterFactory.TYPE.EXE.intValue(), converter());
    }
}
