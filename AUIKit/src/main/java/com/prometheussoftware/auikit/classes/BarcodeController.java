package com.prometheussoftware.auikit.classes;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class BarcodeController {

    private String code;

    private BarcodeController() {}

    public String getCode() {
        return code;
    }

    protected void setCode(String code) {
        this.code = code;
    }

    public Bitmap barcode() {
        return null;
    }

    public static BarcodeController reader() {
        return new Reader();
    }

    public static BarcodeController generator(String code, BarcodeFormat format, int width, int height) {
        return new Generator(code, format, width, height);
    }


    static class Reader extends BarcodeController {

        private Reader() {
            super();

        }
    }

    //https://gist.github.com/adrianoluis/fa9374d7f2f8ca1115b00cc83cd7aacd
    static class Generator extends BarcodeController {

        private Bitmap barcode;

        private Generator(String code, BarcodeFormat format, int width, int height) {
            super();
            setCode(code);

            try {
                BitMatrix matrix = new MultiFormatWriter().encode(code, format, width, height);
                final int w = matrix.getWidth();
                final int h = matrix.getHeight();
                final int[] pixels = new int[w * h];

                for (int y = 0; y < h; y++) {
                    final int offset = y * w;
                    for (int x = 0; x < w; x++) {
                        pixels[offset + x] = matrix.get(x, y) ? Color.BLACK : Color.WHITE;
                    }
                }

                barcode = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                barcode.setPixels(pixels, 0, width, 0, 0, w, h);
            }
            catch (WriterException e) {}
        }

        @Override
        public Bitmap barcode() {
            return this.barcode;
        }

        @Override
        protected void setCode(String code) {
            super.setCode(code);
        }
    }
}
