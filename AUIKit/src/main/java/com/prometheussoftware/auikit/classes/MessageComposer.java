package com.prometheussoftware.auikit.classes;

import android.content.Intent;
import android.net.Uri;
import android.text.Html;

import com.prometheussoftware.auikit.common.MainApplication;

import java.util.ArrayList;

public class MessageComposer {

    public static boolean sendEmail (String title, String message, String[] recipients, String[] BCCRecipients, ArrayList<Uri> attachments, boolean asHTML) {

        Intent mail = new Intent(asHTML ? Intent.ACTION_SEND : Intent.ACTION_SENDTO);
        mail.setData(Uri.parse("mailto:"));
        mail.putExtra(Intent.EXTRA_SUBJECT, title);
        mail.putExtra(Intent.EXTRA_EMAIL, recipients);
        mail.putExtra(Intent.EXTRA_BCC, BCCRecipients);
        mail.putParcelableArrayListExtra(Intent.EXTRA_STREAM, attachments);

        if (asHTML) {
            mail.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(title));
            mail.setType("text/html");
        }
        else {
            mail.putExtra(Intent.EXTRA_TEXT, message);
        }

        if (mail.resolveActivity(MainApplication.getContext().getPackageManager()) == null) return false;

        MainApplication.getWindow().startActivity(mail);
        return true;
    }

    public static boolean sendEmail (String title, String message, String recipient, String[] BCCRecipients, ArrayList<Uri> attachments, boolean asHTML) {
        return sendEmail(title, message, new String[] {recipient}, null, attachments, asHTML);
    }

    public static boolean sendEmail (String title, String message, String[] recipients, ArrayList<Uri> attachments, boolean asHTML) {
        return sendEmail(title, message, recipients, null, attachments, asHTML);
    }

    public static boolean sendEmail (String title, String message, String recipient, ArrayList<Uri> attachments, boolean asHTML) {
        return sendEmail(title, message, new String[] {recipient}, null, attachments, asHTML);
    }

    public static boolean sendEmail (String title, String message, String[] recipients, boolean asHTML) {
        return sendEmail(title, message, recipients, null, null, asHTML);
    }

    public static boolean sendEmail (String title, String message, String recipient, boolean asHTML) {
        return sendEmail(title, message, new String[] {recipient}, null, null, asHTML);
    }

    public static boolean sendEmail (String title, String message, String[] recipients) {
        return sendEmail(title, message, recipients, null, null, false);
    }

    public static boolean sendEmail (String title, String message, String recipient) {
        return sendEmail(title, message, new String[] {recipient}, null, null, false);
    }

    public static boolean sendEmail (String[] recipients) {
        return sendEmail("", "", recipients, null, null, false);
    }

    public static boolean sendEmail (String recipient) {
        return sendEmail("", "", new String[] {recipient}, null, null, false);
    }
}
