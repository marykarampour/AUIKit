package com.prometheussoftware.auikit.networking;

import com.prometheussoftware.auikit.callback.SuccessCallback;
import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.common.EXConstants;
import com.prometheussoftware.auikit.model.BaseResponse;
import com.prometheussoftware.auikit.model.LoginResponse;
import com.prometheussoftware.auikit.model.User;
import com.prometheussoftware.auikit.utility.DEBUGLOG;
import com.prometheussoftware.auikit.utility.StringUtility;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    //region helpers

    public static void loginRequestWithCallback(User object, SuccessCallback callback) {

        ExampleServices service = NetworkManager.createService(ExampleServices.class);
        Call<LoginResponse> requestCall = service.login(object);

        requestCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response != null && response.code() != 401) {
                    callback.done(true);
                }
                else callback.done(false);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                DEBUGLOG.s("Response error --> ", t.getLocalizedMessage());
                callback.done(false);
            }
        });
    }

    public static <S extends ServerResponse> void genericRequestWithCallback(Object object, ExampleServices.METHOD method, ServiceCallback callback) {

        ExampleServices service = NetworkManager.createService(ExampleServices.class, 0);
        Call<S> requestCall = requestMethod(service, object, method);

        requestCall.enqueue(new Callback<S>() {
            @Override
            public void onResponse(Call<S> call, Response<S> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<S> call, Throwable t) {
                callback.onFailure(error(t));
            }
        });
    }

    protected static void processResult(ServerResponse resultObject, Class modelClass, String key, ServiceCallback callback) {

        if (modelClass.isInstance(resultObject)) {

            if (BaseResponse.class.isInstance(resultObject)) {
                BaseResponse response = (BaseResponse)resultObject;
                Error err = response.error();
                if (StringUtility.isNotEmpty(err.getLocalizedMessage())) {
                    callback.onFailure(err);
                }
                else {
                    callback.onSuccess(response.object());
                }
            }
            else {
                callback.onSuccess(resultObject);
            }
        }
        else {
            callback.onFailure(noContent());
        }
    }

    protected static void processResult (ServerResponse resultObject, Class modelClass, ServiceCallback callback) {
        processResult(resultObject, modelClass, null, callback);
    }

    protected static Error error(Throwable t) {
        return new Error((IOException.class.isInstance(t)) ? "Error!" : t.getLocalizedMessage());
    }

    protected static Error noContent() {
        return new Error("No Content");
    }

    protected static Class resultClass(ExampleServices.METHOD method) {

        switch (method) {
            case LOGIN:       return User.class;
            case LOGOUT:      return BaseResponse.class;
            default:
                return ServerResponse.class;
        }
    }

    protected static <T extends Object, S extends ServerResponse> Call<S> requestMethod(ExampleServices service, T object, ExampleServices.METHOD method) {

        switch (method) {
            case LOGIN: return (Call<S>) service.login((User) object);
            case LOGOUT: return (Call<S>) service.logout(Token());
            default:
                return null;
        }
    }

    private static String Token() {
        return "";
    }

    protected static String authTokenKey() {
        return "x-auth-token";
    }

    //endregion
}
