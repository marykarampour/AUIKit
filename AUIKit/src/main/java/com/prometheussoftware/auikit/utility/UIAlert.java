package com.prometheussoftware.auikit.utility;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.annotation.StyleRes;

import com.prometheussoftware.auikit.common.Constants;
import com.prometheussoftware.auikit.common.MainApplication;

public class UIAlert {

    public static void OKAlert(String title, String message) {
        MainApplication.getWindow().runOnUiThread(() ->
                new AlertDialog.Builder(MainApplication.getWindow())
                        .setTitle(title)
                        .setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton(Constants.OK_STR(), (DialogInterface d, int w) -> {})
                        .show());
    }

    public static void OKAlert(String title, String message, @StyleRes int styleResId) {
        MainApplication.getWindow().runOnUiThread(() ->
                new AlertDialog.Builder(MainApplication.getWindow(), styleResId)
                        .setTitle(title)
                        .setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton(Constants.OK_STR(), (DialogInterface d, int w) -> {})
                        .show());
    }

    public static void OKAlert(String title, String message, DialogInterface.OnClickListener listener) {
        MainApplication.getWindow().runOnUiThread(() ->
                new AlertDialog.Builder(MainApplication.getWindow())
                        .setTitle(title)
                        .setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton(Constants.OK_STR(), listener)
                        .show());
    }

    public static void OKAlert(String title, String message, @StyleRes int styleResId, DialogInterface.OnClickListener listener) {
        MainApplication.getWindow().runOnUiThread(() ->
                new AlertDialog.Builder(MainApplication.getWindow(), styleResId)
                        .setTitle(title)
                        .setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton(Constants.OK_STR(), listener)
                        .show());
    }

    public static void OKAlert(String title) {
        MainApplication.getWindow().runOnUiThread(() ->
                new AlertDialog.Builder(MainApplication.getWindow())
                        .setMessage(title)
                        .setCancelable(false)
                        .setPositiveButton(Constants.OK_STR(), (DialogInterface d, int w) -> {})
                        .show());
    }

    public static void OKAlert(String title, @StyleRes int styleResId) {
        MainApplication.getWindow().runOnUiThread(() ->
                new AlertDialog.Builder(MainApplication.getWindow(), styleResId)
                        .setMessage(title)
                        .setCancelable(false)
                        .setPositiveButton(Constants.OK_STR(), (DialogInterface d, int w) -> {})
                        .show());
    }

    public static void OKAlert(String title, DialogInterface.OnClickListener listener) {
        MainApplication.getWindow().runOnUiThread(() ->
                new AlertDialog.Builder(MainApplication.getWindow())
                        .setMessage(title)
                        .setCancelable(false)
                        .setPositiveButton(Constants.OK_STR(), listener)
                        .setCancelable(false)
                        .show());
    }

    public static void OKCancelAlert(String title, String message, DialogInterface.OnClickListener listener) {
        MainApplication.getWindow().runOnUiThread(() ->
                new AlertDialog.Builder(MainApplication.getWindow())
                        .setTitle(title)
                        .setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton(Constants.OK_STR(), listener)
                        .setNegativeButton(Constants.Cancel_STR(), (DialogInterface d, int w) -> {})
                        .show());
    }

    public static void OKCancelAlert(String title, DialogInterface.OnClickListener listener, DialogInterface.OnClickListener cancelListener) {
        MainApplication.getWindow().runOnUiThread(() ->
                new AlertDialog.Builder(MainApplication.getWindow())
                        .setMessage(title)
                        .setCancelable(false)
                        .setPositiveButton(Constants.OK_STR(), listener)
                        .setNegativeButton(Constants.Cancel_STR(), cancelListener)
                        .show());
    }

    public static void OKCancelAlert(String title, String message, DialogInterface.OnClickListener listener, DialogInterface.OnClickListener cancelListener) {
        MainApplication.getWindow().runOnUiThread(() ->
                new AlertDialog.Builder(MainApplication.getWindow())
                        .setTitle(title)
                        .setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton(Constants.OK_STR(), listener)
                        .setNegativeButton(Constants.Cancel_STR(), cancelListener)
                        .show());
    }

    public static void OKCancelAlert(String title, DialogInterface.OnClickListener listener) {
        MainApplication.getWindow().runOnUiThread(() ->
                new AlertDialog.Builder(MainApplication.getWindow())
                        .setMessage(title)
                        .setCancelable(false)
                        .setPositiveButton(Constants.OK_STR(), listener)
                        .setNegativeButton(Constants.Cancel_STR(), (DialogInterface d, int w) -> {})
                        .show());
    }

    public static void OKCancelAlert(String title, @StyleRes int styleResId, DialogInterface.OnClickListener listener) {
        MainApplication.getWindow().runOnUiThread(() ->
                new AlertDialog.Builder(MainApplication.getWindow(), styleResId)
                        .setMessage(title)
                        .setCancelable(false)
                        .setPositiveButton(Constants.OK_STR(), listener)
                        .setNegativeButton(Constants.Cancel_STR(), (DialogInterface d, int w) -> {})
                        .show());
    }

    public static void Toast(String title) {
        MainApplication.getWindow().runOnUiThread(() ->
                Toast.makeText(MainApplication.getWindow(), title, Toast.LENGTH_LONG).show());
    }

    public static void ShortToast(String title) {
        MainApplication.getWindow().runOnUiThread(() ->
                Toast.makeText(MainApplication.getWindow(), title, Toast.LENGTH_SHORT).show());
    }
}
