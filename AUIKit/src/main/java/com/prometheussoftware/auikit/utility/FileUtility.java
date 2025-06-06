package com.prometheussoftware.auikit.utility;

import android.content.Context;
import android.content.res.AssetManager;

import com.prometheussoftware.auikit.common.MainApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class FileUtility {

    private static Context context = MainApplication.getContext();

    public static InputStream inputStreamFromBundle (String bundleDir, String file) {

        AssetManager assets = context.getAssets();
        String path = bundleDir + "/" + file;

        try {
            String[] list = context.getResources().getAssets().list(bundleDir);

            for (int i = 0; i < list.length; i++) {
                if (list[i].contains(file)) {
                    try {
                        InputStream stream = assets.open(path);
                        return stream;
                    } catch (IOException e) {
                        DEBUGLOG.s(e);
                    }
                }
            }
        } catch (IOException e) {
            DEBUGLOG.s(e);
        }
        return null;
    }

    public static FileInputStream inputStreamFromPath (String path) {

        try {
            FileInputStream stream = new FileInputStream(path);
            return stream;
        } catch (IOException e) {
            DEBUGLOG.s(e);
        }
        return null;
    }

    public static FileInputStream inputStreamFromFile (File file) {
        return inputStreamFromPath(file.toURI().getPath());
    }

    public static byte[] dataFromFile (File file) throws IOException {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            try {
                return Files.readAllBytes(file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            FileInputStream stream = inputStreamFromPath(file.toURI().getPath());
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            if (stream != null) {
                int length;
                while ((length = stream.read(data)) != -1) {
                    os.write(data, 0, length);
                }
            }
            return os.toByteArray();
        }
        return null;
    }

    /** Returns File */
    public static File writeToFile (byte[] data, String fileName, File fileDir) {
        File file = null;
        try {
            file = new File(fileDir+"/"+fileName);

            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
        }
        catch (IOException e) { }
        return file;
    }

    /** Uses a UUID for filename. */
    public static File writeToFile (byte[] data, File fileDir) {
        String filename = ObjectUtility.GUID() + "." + DataUtility.extension(data);
        return writeToFile(data, filename, fileDir);
    }

    /** Uses a UUID for filename and FilesDir for directory. */
    public static File writeToFile (byte[] data) {
        return writeToFile(data, context.getFilesDir());
    }

    /** Returns file */
    public static File writeToCachedFile (byte[] data, String fileName) {
        return writeToFile(data, fileName, context.getCacheDir());
    }

    public static boolean deleteFile (String path) {
        File file = new File(path);
        return file.delete();
    }

    public static String pathInFilesDir (String fileName, File fileDir) {
        return context.getFilesDir().getPath() + "/" + fileDir + "/" + fileName;
    }

    public static String pathInFilesDir (String fileName) {
        return context.getFilesDir().getPath() + "/" + fileName;
    }

    public static boolean fileExists (String path) {
        return new File(path).exists();
    }

}
