# AUIKit

## Introduction

AUIKit is a library facilitating the preferred single activity architecture. In this architecture, an Android project has only one activity, i.e. a subclass of `BaseWindow`. All the view life cycle is handled by this one activity. The main building blocks of this architecture are view controllers, they provide their own life cycle. Each screen is represented by a view controller. Views are added to view controller's `view` field. 

This project includes AUIKit as a module, as well as an example app using it. 

## Code Samples

Using AUIKit you can easily create a new screen using:
```
    ViewController vc = new ViewController();
    presentViewController(vc, Navigation.TRANSITION_ANIMATION.DOWNUP, null);
```
This is very simple and convenient, since any view controller is just a Java class, you can have your custom constructor, set values at initialization, etc. no need for dealing with Intetns to pass values around from one screen to the other. 

## How to create a project using AUIKit

1. Create an empty project
2. In project's directory: `git submodule add <repo-url>`
3. In Android Studio: File -> Project structure -> Modules -> + -> Import Gradle Project -> select directory of lib (exclude example, i.e. app folder, only select the AUIKit folder) 
4. In project’s app build.gradle: `implementation project(path: ':AUIKit')`, `implementation 'androidx.core:core-ktx:1.3.2'`, `implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"` and `apply plugin: 'kotlin-android'`
5. Add `classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"` to project's build.gradle dependencies, and add `ext.kotlin_version = '1.4.21'` in buildscript
6. Add common and viewcontrollers packages under java main package folder and add other files similar to the AUIKit's example, including `MainWindow` subclass of `BaseWindow`
7. Modify the manifest file to use the custom application and base activity similar to example
8. Add material lib to the app: implementation 'com.google.android.material:material:1.3.0-alpha02'
9. Change styles file to use MaterialComponents
10. Override `createRootViewController` of the `MainWindow` to return the root view controller of the porject. This is the first view controller that appears at launch.

Note that the project needs to support Java 8. In project’s app build.gradle add:
```
compileOptions {
    sourceCompatibility JavaVersion.VERSION_1_8
    targetCompatibility JavaVersion.VERSION_1_8
}
```

## How to add services to your project

1. Add the following implementations to your app's build.gradle: 
2. implementation 'com.google.code.gson:gson:2.8.6'
3. implementation 'com.squareup.retrofit2:retrofit:2.9.0'
4. implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
5. implementation 'com.google.android.material:material:1.2.1'
6. Create a services package in your project and add the following classes:
7. Subclass ConverterFactory to handle JSON formatting
8. Create a ModelsRegistrar class and in its static method registerModels() register all classes that need to be de/serialized or conform to copy.
8. Subclass ServerController to add your services. 
9. This subclass must override converter() method to return the converter created earlier, and override initialize() to call setBaseURL(url, converter()).
10. In static call ModelsRegistrar.registerModels().
11. Create a ServerEndpoints class to return endpoints as static constants. 
11. Create a subinterface of Services and add the services.
 

Sample service:
```
    @POST(ServerEndpoints.AUTH)
    @ConverterFactory.Default
    Call<User.LoggedInUser> login(@Header(DEVICE_TOKEN) String token, @Header(DEVICE_PLATFORM) String platform, @Body User.Auth request);
```

Sample ServerController service implementation:
```
    public static void login(User.Auth object, SuccessCallback callback) {

        Services service = NetworkManager.createService(Services.class);
        String token = MyApplication.getInstance().getFCMDeviceToken();
        String platform = "FCM";

        Call<User.LoggedInUser> requestCall = service.login(token, platform, object);

        requestCall.enqueue(new Callback<User.LoggedInUser>() {
            @Override
            public void onResponse(Call<User.LoggedInUser> call, Response<User.LoggedInUser> response) {

                if (response != null && response.code() != 401) {
                    if (response.body() instanceof User.LoggedInUser) {

                        User.LoggedInUser body = response.body();
                        User.AppUser.getInstance().setToken(response.headers().get(authTokenKey()));
                        setAppUser(callback);
                    }
                    else callback.done(true);
                }
                else callback.done(false);
            }

            @Override
            public void onFailure(Call<User.LoggedInUser> call, Throwable t) {
                callback.done(false);
            }
        });
    }

```
