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
2. In project's directory: git submodule add https://git.prometheussoftware.ca/androidlibraries/androiduikit.git
3. In Android Studio: File -> Project structure -> Modules -> + -> Import Gradle Project -> select directory of lib (exclude example, i.e. app folder, only select the AUIKit folder) 
4. In project’s app build.gradle: implementation project(path: ':AUIKit')
5. Add common and viewcontrollers packages under java main package folder and add other files similar to the AUIKit's example, including `MainWindow` subclass of `BaseWindow`
6. Modify the manifest file to use the custom application and base activity similar to example
7. Add material lib to the app: implementation 'com.google.android.material:material:1.3.0-alpha02'
8. Change styles file to use MaterialComponents
9. Override `createRootViewController` of the `MainWindow` to return the root view controller of the porject. This is the first view controller that appears at launch.
