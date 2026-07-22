package com.prometheussoftware.auikit.utility;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.view.View;
import android.widget.Toast;

import com.prometheussoftware.auikit.common.Constants;
import com.prometheussoftware.auikit.common.Dimensions;
import com.prometheussoftware.auikit.common.MainApplication;
import com.prometheussoftware.auikit.uiview.UITextField;
import com.prometheussoftware.auikit.uiview.UITextView;

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

    // TODO: Decide whether to use @StyleRes annotations.
    public static void OKAlert(String title, String message, int styleResId) {
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

    public static void OKAlert(String title, String message, int styleResId, DialogInterface.OnClickListener listener) {
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

    public static void OKAlert(String title, int styleResId) {
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

    public static void OKCancelAlert(String title, int styleResId, DialogInterface.OnClickListener listener) {
        MainApplication.getWindow().runOnUiThread(() ->
                new AlertDialog.Builder(MainApplication.getWindow(), styleResId)
                        .setMessage(title)
                        .setCancelable(false)
                        .setPositiveButton(Constants.OK_STR(), listener)
                        .setNegativeButton(Constants.Cancel_STR(), (DialogInterface d, int w) -> {})
                        .show());
    }

    public static void OKCancelAlert(String title, String hint, Rect rect, UITextView.TextViewDelegate delegate) {

        UITextField view = new UITextField();
        view.setDelegate(delegate);
        view.setHint(hint);
        view.setFrame(rect);

        MainApplication.getWindow().runOnUiThread(() ->
                new AlertDialog.Builder(MainApplication.getWindow())
                        .setMessage(title)
                        .setCancelable(false)
                        .setView(view)
                        .setPositiveButton(Constants.OK_STR(), (DialogInterface d, int w) -> { view.endEditing(); })
                        .setNegativeButton(Constants.Cancel_STR(), (DialogInterface d, int w) -> {})
                        .show());
    }

    /** @brief Default Rect(0, 0, Dimensions.Int_200(), Dimensions.Int_44()) is used for view. */
    public static void OKCancelAlert(String title, String hint, UITextView.TextViewDelegate delegate) {
        OKCancelAlert(title, hint, new Rect(0, 0, Dimensions.Int_200(), Dimensions.Int_44()), delegate);
    }

    public static void OKCancelAlert(String title, View view, DialogInterface.OnClickListener listener) {
        MainApplication.getWindow().runOnUiThread(() ->
                new AlertDialog.Builder(MainApplication.getWindow())
                        .setMessage(title)
                        .setCancelable(false)
                        .setView(view)
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
