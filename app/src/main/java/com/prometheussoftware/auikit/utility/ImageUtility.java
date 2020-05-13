package com.prometheussoftware.auikit.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Handler;
import android.util.Size;
import android.view.PixelCopy;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.prometheussoftware.auikit.callback.SuccessCallback;
import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.common.Constants;
import com.prometheussoftware.auikit.common.MainApplication;
import com.prometheussoftware.auikit.uiview.UIView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ImageUtility {

    public interface BitmapCopy {
        void finishedWithResult(Bitmap bitmap);
        default void failed() {}
    }

    public static void loadImageWithPathPlaceholerTint (ImageView view, String path, int placeholder, int tintColor) {
        int icon = placeholder;
        Drawable iconDrawable = ContextCompat.getDrawable(MainApplication.getContext(), icon);
        DrawableCompat.setTint(iconDrawable, tintColor);
        if (path == null || path.length() == 0) {
            view.setImageResource(icon);
            return;
        }

        Picasso.get()
                .load(path)
                .fit()
                .centerCrop()
                .placeholder(icon)
                .into(view);
    }

    public static void loadImageWithPath (ImageView view, String path, int height, int placeholder) {
        if (path == null || path.length() == 0) {
            view.setImageResource(placeholder);
            return;
        }

        Picasso.get()
                .load(path)
                .fit()
                .centerCrop()
                //.resize(0, height)
                .placeholder(placeholder)
                .into(view);

    }

    public static void loadImageWithURLWithCallback (ImageView view, String URL, int height, int placeholder, Callback callback) {
        if (URL == null || URL.length() == 0) {
            view.setImageResource(placeholder);
            return;
        }

        Picasso.get()
                .load(URL)
                .resize(0, height)
                .placeholder(placeholder)
                .into(view, callback);
    }

    public static void loadImageWithURLWithCallback (ImageView view, String URL, int height, int placeholder, int tintColor, Callback callback) {
        int icon = placeholder;
        Drawable iconDrawable = ContextCompat.getDrawable(MainApplication.getContext(), icon);
        DrawableCompat.setTint(iconDrawable, tintColor);
        if (URL == null || URL.length() == 0) {
            view.setImageResource(placeholder);
            return;
        }

        Picasso.get()
                .load(URL)
                .resize(0, height)
                .placeholder(icon)
                .into(view, callback);
    }

    public static void loadImageWithURLWithCallback (Context context, String URL, Callback callback) {
        if (URL == null || URL.length() == 0) {
            callback.onError(new Exception("URL is empty"));
        }

        Picasso.get().load(URL).into(new ImageView(context), callback);
    }

    public static void loadImageWithURL (String URL) {
        if (URL == null || URL.length() == 0) {
            return;
        }

        Picasso.get().load(URL);
    }

    public static void setImageWithURLString (String URL, ImageView imageView) {
        Picasso.get().load(URL).into(imageView);
    }

    public static void setImageWithURLString (String URL, ImageView imageView, Drawable placeholder) {
        if (URL == null || URL.length() == 0) {
            imageView.setImageDrawable(placeholder);
            return;
        }
        Picasso.get().load(URL).into(imageView);
    }

    public static void setImageWithURLString (String URL, ImageView imageView, SuccessCallback callback) {
        Picasso.get().load(URL).into(imageView, new Callback() {
            @Override public void onSuccess() {
                callback.done(true);
            }

            @Override public void onError(Exception e) {
                callback.done(false);
            }
        });
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        if (angle == 0) return source;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static Bitmap rotateImageIfNeeded(Bitmap bitmap, int orientation) {

        float angle = 0.0f;
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90: angle = 90.0f;  break;
            case ExifInterface.ORIENTATION_ROTATE_180:angle = 180.0f; break;
            case ExifInterface.ORIENTATION_ROTATE_270:angle = 270.0f; break;
            case ExifInterface.ORIENTATION_NORMAL:
            default:
                break;
        }
        return rotateImage(bitmap, angle);
    }

    public static void imageFromView (UIView view, Handler handler, BitmapCopy bitmapCopy) {
        imageFromView(view, App.constants().Max_Transition_Bitmap_Size(), handler, bitmapCopy);
    }

    @SuppressLint("NewApi")
    public static void imageFromView (UIView view, int maxSize, Handler handler, BitmapCopy bitmapCopy) {

        int width = view.getWidth() > 0 ? view.getWidth() : Constants.Screen_Size().getWidth();
        int height = view.getHeight() > 0 ? view.getHeight() : Constants.Screen_Size().getHeight();
        Size largeSize = new Size(width, height);
        Size size = 0 < maxSize ? MKMath.shrinkToSize(largeSize, maxSize) : largeSize;
        boolean shouldResize = !size.equals(largeSize);

        final Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        boolean canCopy = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && view.getWindow().getWindow().peekDecorView() != null;

        if (canCopy) {
            try {
                int[] location = new int[2];
                view.getLocationInWindow(location);
                Rect rect = new Rect(location[0], location[1], location[0]+width, location[1]+height);

                PixelCopy.request(view.getWindow().getWindow(), rect, bitmap, (int copyResult) -> {

                    Bitmap smallBitmap = shouldResize ? DataUtility.shrinkToImage(bitmap, size) : bitmap;
                    if (copyResult == PixelCopy.SUCCESS) {
                        bitmapCopy.finishedWithResult(smallBitmap);
                    } else {
                        bitmapCopy.failed();
                    }
                }, handler);
            } catch (Exception e) {
                DEBUGLOG.s(e);
                canCopy = false;
            }
        }

        if (!canCopy) {
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
            Bitmap smallBitmap = shouldResize ? DataUtility.shrinkToImage(bitmap, size) : bitmap;
            bitmapCopy.finishedWithResult(smallBitmap);
        }
    }
}
