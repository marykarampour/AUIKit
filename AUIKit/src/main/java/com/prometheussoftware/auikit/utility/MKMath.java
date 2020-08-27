package com.prometheussoftware.auikit.utility;

import android.util.Size;

public class MKMath {

    public static Size shrinkToSize(Size size, int maxSize) {

        boolean resizeWidth = size.getHeight() <= size.getWidth();
        boolean shouldResize;
        int width;
        int height;

        if (resizeWidth) {
            shouldResize = size.getWidth() != maxSize;
            width = maxSize;
            height = (int) Math.floor(size.getHeight() * ((float)maxSize / size.getWidth()));
        }
        else {
            shouldResize = size.getHeight() != maxSize;
            height = maxSize;
            width = (int) Math.floor(size.getWidth() * ((float)maxSize / size.getHeight()));
        }

        return shouldResize ? new Size(width, height) : size;
    }
}
