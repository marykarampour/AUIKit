package com.prometheussoftware.auikit.utility;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Base64;
import android.util.Size;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class DataUtility {

    public static void decompress(InputStream stream, String destination) {

        try {
            File file = new File(destination);
            file.getParentFile().mkdirs();
            file.createNewFile();

            GZIPInputStream gzip = new GZIPInputStream(stream);
            FileOutputStream fileOutputStream = new FileOutputStream(destination);

            int len;
            byte[] buff = new byte[2048];

            while ((len = gzip.read(buff)) != -1) {
                fileOutputStream.write(buff, 0, len);
            }
            gzip.close();
            stream.close();
            fileOutputStream.close();

        } catch (IOException e) {
            DEBUGLOG.s(e);
        }
    }

    public static void compress(InputStream stream, String destination) {

        try {
            File file = new File(destination);
            file.getParentFile().mkdirs();
            file.createNewFile();

            FileOutputStream fileOutputStream = new FileOutputStream(destination);
            GZIPOutputStream gzip = new GZIPOutputStream(fileOutputStream);

            int len;
            byte[] buffer = new byte[2048];

            while((len = stream.read(buffer)) != -1) {
                gzip.write(buffer, 0, len);
            }

            gzip.flush();
            gzip.close();
            stream.close();
            fileOutputStream.close();

        } catch (IOException e) {
            DEBUGLOG.s(e);
        }
    }

    public static String base64EncodedImage (Bitmap image) {
        if (image != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] bytes = stream.toByteArray();
            return base64EncodedBytes(bytes);
        }
        return null;
    }

    public static String base64EncodedBytes (byte[] data) {
        if (data.length > 0) {
            return Base64.encodeToString(data, Base64.DEFAULT);
        }
        return null;
    }

    public static Bitmap bytestoBitmap (byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    public static byte[] bitmapToBytes (Bitmap image) {
        if (image == null) return new byte[]{};

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap shrinkToImage (byte[] data, int width) {
        Bitmap image = DataUtility.bytestoBitmap(data);
        if (image.getWidth() > width) {
            int height = image.getHeight() * width / image.getWidth();
            image = Bitmap.createScaledBitmap(image, width, height, true);
        }
        return image;
    }

    public static Bitmap shrinkToImage (Bitmap image, int size) {
        if (image == null) return null;

        Size imageSize = new Size(image.getWidth(), image.getHeight());
        Size newSize = MKMath.shrinkToSize(imageSize, size);
        if (!newSize.equals(imageSize)) {
            image = Bitmap.createScaledBitmap(image, newSize.getWidth(), newSize.getHeight(), true);
        }
        return image;
    }

    public static Bitmap shrinkToImage (Bitmap image, Size size) {
        if (image == null) return null;

        Size imageSize = new Size(image.getWidth(), image.getHeight());
        if (!size.equals(imageSize)) {
            image = Bitmap.createScaledBitmap(image, size.getWidth(), size.getHeight(), true);
        }
        return image;
    }

    public static byte[] shrinkToData (byte[] data, int width) {
        Bitmap image = shrinkToImage(data, width);
        data = DataUtility.bitmapToBytes(image);
        return data;
    }

    public static Bitmap imageFromDrawable (Drawable image) {
        if (image instanceof BitmapDrawable) {
            return ((BitmapDrawable)image).getBitmap();
        }
        return null;
    }

    public static Bitmap imageFromData (Intent data, Activity activity) {
        if (data == null) return null;
        return imageFromData(data.getData(), activity);
    }

    public static Bitmap imageFromData (Uri URI, Activity activity) {
        try {
            ParcelFileDescriptor descriptor = activity.getContentResolver().openFileDescriptor(URI, "r");
            FileDescriptor file = descriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(file);
            descriptor.close();

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                try {
                    InputStream inputStream = activity.getContentResolver().openInputStream(URI);
                    ExifInterface exif = new ExifInterface(inputStream);
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                    return ImageUtility.rotateImageIfNeeded(image, orientation);
                }
                catch (IOException e) {
                    DEBUGLOG.s(e);
                }
            }
            return image;
        }
        catch (FileNotFoundException e) {
            DEBUGLOG.s(e);
        }
        catch (IOException e) {
            DEBUGLOG.s(e);
        }
        return null;
    }

    public static Bitmap imageFromPath (String path) {
        return BitmapFactory.decodeFile(path);
    }

    public static String extension (byte[] data) {
        if (data.length == 0) return "";

        switch (data[0]) {
            case (byte) 0xFF: return "jpg";
            case (byte) 0x89: return "png";
            case (byte) 0x49: return "tiff";
            case (byte) 0x4D: return "tiff";
            case (byte) 0x47: return "gif";
            case (byte) 0x25: return "pdf";
            case (byte) 0x7B: return "rtf";
            case (byte) 0x46: return "txt";
            case (byte) 0x50: return "docx";
            default:   return "";
        }
    }

    public static boolean isMedia (byte[] data) {
        if (data.length == 0) return false;

        switch (data[0]) {
            case (byte) 0xFF:
            case (byte) 0x89:
            case (byte) 0x49:
            case (byte) 0x4D:
            case (byte) 0x47:
            case (byte) 0x52:
            case (byte) 0x66:
                return true;
            default:
                return false;
        }
    }

    public static boolean isOctetStream (byte[] data) {
        if (data.length < 4) return false;
        return data[3] == 0x20;
    }
}
