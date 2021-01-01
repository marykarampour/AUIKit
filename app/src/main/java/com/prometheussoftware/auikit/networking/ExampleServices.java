package com.prometheussoftware.auikit.networking;

import com.prometheussoftware.auikit.model.BaseResponse;
import com.prometheussoftware.auikit.model.LoginResponse;
import com.prometheussoftware.auikit.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ExampleServices extends Services {

    String X_TOKEN_KEY = "x-auth-token";

    @POST(ExampleServerEndpoints.AUTH_URL)
    @ConverterFactory.Default
    Call<LoginResponse> login(@Body User request);

    @GET(ExampleServerEndpoints.LOGOUT_URL)
    @ConverterFactory.Default
    Call<BaseResponse> logout(@Header(X_TOKEN_KEY) String token);

    enum METHOD {
        LOGIN,
        LOGOUT
    }
}
