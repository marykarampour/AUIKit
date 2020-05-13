package com.prometheussoftware.auikit.utility;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.prometheussoftware.auikit.common.AUIKitApplication;
import com.prometheussoftware.auikit.common.Constants;

public class UIAlert {

    public static void OKAlert(String title, String message) {
        AUIKitApplication.getWindow().runOnUiThread(() ->
                new AlertDialog.Builder(AUIKitApplication.getWindow())
                        .setTitle(title)
                        .setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton(Constants.OK_STR(), (DialogInterface d, int w) -> {})
                        .show());
    }

    public static void OKAlert(String title, String message, DialogInterface.OnClickListener listener) {
        AUIKitApplication.getWindow().runOnUiThread(() ->
                new AlertDialog.Builder(AUIKitApplication.getWindow())
                        .setTitle(title)
                        .setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton(Constants.OK_STR(), listener)
                        .show());
    }

    public static void OKAlert(String title) {
        AUIKitApplication.getWindow().runOnUiThread(() ->
                new AlertDialog.Builder(AUIKitApplication.getWindow())
                        .setMessage(title)
                        .setCancelable(false)
                        .setPositiveButton(Constants.OK_STR(), (DialogInterface d, int w) -> {})
                        .show());
    }

    public static void OKAlert(String title, DialogInterface.OnClickListener listener) {
        AUIKitApplication.getWindow().runOnUiThread(() ->
                new AlertDialog.Builder(AUIKitApplication.getWindow())
                        .setMessage(title)
                        .setCancelable(false)
                        .setPositiveButton(Constants.OK_STR(), listener)
                        .setCancelable(false)
                        .show());
    }

    public static void OKCancelAlert(String title, String message, DialogInterface.OnClickListener listener) {
        AUIKitApplication.getWindow().runOnUiThread(() ->
                new AlertDialog.Builder(AUIKitApplication.getWindow())
                        .setTitle(title)
                        .setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton(Constants.OK_STR(), listener)
                        .setNegativeButton(Constants.Cancel_STR(), (DialogInterface d, int w) -> {})
                        .show());
    }

    public static void OKCancelAlert(String title, DialogInterface.OnClickListener listener, DialogInterface.OnClickListener cancelListener) {
        AUIKitApplication.getWindow().runOnUiThread(() ->
                new AlertDialog.Builder(AUIKitApplication.getWindow())
                        .setMessage(title)
                        .setCancelable(false)
                        .setPositiveButton(Constants.OK_STR(), listener)
                        .setNegativeButton(Constants.Cancel_STR(), cancelListener)
                        .show());
    }

    public static void OKCancelAlert(String title, String message, DialogInterface.OnClickListener listener, DialogInterface.OnClickListener cancelListener) {
        AUIKitApplication.getWindow().runOnUiThread(() ->
                new AlertDialog.Builder(AUIKitApplication.getWindow())
                        .setTitle(title)
                        .setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton(Constants.OK_STR(), listener)
                        .setNegativeButton(Constants.Cancel_STR(), cancelListener)
                        .show());
    }

    public static void OKCancelAlert(String title, DialogInterface.OnClickListener listener) {
        AUIKitApplication.getWindow().runOnUiThread(() ->
                new AlertDialog.Builder(AUIKitApplication.getWindow())
                        .setMessage(title)
                        .setCancelable(false)
                        .setPositiveButton(Constants.OK_STR(), listener)
                        .setNegativeButton(Constants.Cancel_STR(), (DialogInterface d, int w) -> {})
                        .show());
    }

    public static void Toast(String title) {
        AUIKitApplication.getWindow().runOnUiThread(() ->
                Toast.makeText(AUIKitApplication.getWindow(), title, Toast.LENGTH_LONG).show());
    }
}
