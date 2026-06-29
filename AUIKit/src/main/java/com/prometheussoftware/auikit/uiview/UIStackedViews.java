package com.prometheussoftware.auikit.uiview;

import android.util.Size;

import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.common.Constants;
import com.prometheussoftware.auikit.uiview.protocols.UIStackedViewProtocol;
import com.prometheussoftware.auikit.uiview.protocols.ViewCreation;

import java.util.ArrayList;
import java.util.Map;

public class UIStackedViews <T extends UIView> extends UIView implements UIStackedViewProtocol {

    protected ArrayList<T> views;

    public UIStackedViews(int count, ViewCreation<T> handler) {
        this(count, 0, handler);
    }

    public UIStackedViews(int count, int padding, ViewCreation<T> handler) {
        this(count, padding, padding, handler);
    }

    public UIStackedViews(int count, int interItemSpacing, int horizontalMargin, int verticalMargin, ViewCreation<T> handler) {
        this(count, interItemSpacing, horizontalMargin, verticalMargin, null, handler);
    }

    public UIStackedViews(int count, int interItemSpacing, int horizontalMargin, int verticalMargin, Map<Integer, Integer> sizes, ViewCreation<T> handler) {
        super();
        initViewsWithCount(count, handler);
        constraintViews(sizes, interItemSpacing, horizontalMargin, verticalMargin);
        applyConstraints();
    }

    public UIStackedViews(int count, int padding, int interItemMargin, ViewCreation<T> handler) {
        super();
        initViewsWithCount(count, handler);
        constraintViews(padding, interItemMargin);
        applyConstraints();
    }

    public UIStackedViews(ArrayList<ViewCreation<T>> handlers) {
        super();

        this.views = new ArrayList<>();

        for (int i = 0; i < handlers.size(); i++) {
            T view = handlers.get(i).view(0);
            if (view == null) continue;

            addSubview(view);
            this.views.add(view);
        }

        constraintViews();
        applyConstraints();
    }

    private void initViewsWithCount(int count, ViewCreation<T> handler) {
        views = new ArrayList<>();

        if (handler == null) return;

        for (int i = 0; i < count; i++) {
            T view = handler.view(i);
            if (view == null) continue;

            addSubview(view);
            this.views.add(view);
        }
    }

    public static float defaultPadding() {
        return 4.0f;
    }

    public T viewAtIndex(int index) {
        if (index >= views.size()) return null;
        return views.get(index);
    }

    public ArrayList<T> contentViews() {
        return views;
    }

    public int count() {
        return views.size();
    }

    @Override
    public void constraintViews(Map<Integer, Integer> sizes, int interItemSpacing, int horizontalMargin, int verticalMargin) {
    }

    @Override
    public void constraintViews(int interItemSpacing, int horizontalMargin, int verticalMargin) {
        constraintViews(null, interItemSpacing, horizontalMargin, verticalMargin);
    }

    @Override
    public void constraintViews(int padding, int interItemMargin) {
    }

    @Override
    public void constraintViews(int padding) {
        constraintViews(padding, padding);
    }

    @Override
    public void constraintViews() {
        constraintViews(0, 0);
    }

    public static class Horizontal<T extends UIView> extends UIStackedViews<T> {
        public Horizontal(int count, ViewCreation <T> handler) {
            super(count, handler);
        }

        public Horizontal(int count, int padding, ViewCreation<T> handler) {
            super(count, padding, handler);
        }

        public Horizontal(int count, int interItemSpacing, int horizontalMargin, int verticalMargin, ViewCreation<T> handler) {
            super(count, interItemSpacing, horizontalMargin, verticalMargin, handler);
        }

        public Horizontal(int count, int interItemSpacing, int horizontalMargin, int verticalMargin, Map<Integer, Integer> sizes, ViewCreation<T> handler) {
            super(count, interItemSpacing, horizontalMargin, verticalMargin, sizes, handler);
        }

        public Horizontal(int count, int padding, int interItemMargin, ViewCreation<T> handler) {
            super(count, padding, interItemMargin, handler);
        }

        public Horizontal(ArrayList<ViewCreation<T>> singleIndexViewCreationHandlers) {
            super(singleIndexViewCreationHandlers);
        }

        @Override
        public void constraintViews(Map<Integer, Integer> sizes, int interItemSpacing, int horizontalMargin, int verticalMargin) {
            boolean isSizesEmpty = (sizes == null || sizes.isEmpty());
            constraintHorizontally(contentViews(), interItemSpacing, horizontalMargin, verticalMargin, isSizesEmpty);

            if (!isSizesEmpty) {
                for (Map.Entry<Integer, Integer> entry : sizes.entrySet()) {
                    int index = entry.getKey();
                    int width = entry.getValue();

                    if (index < contentViews().size() && width > 0) {
                        constraintWidthForView(contentViews().get(index), width);
                    }
                }
            }
        }

        @Override
        public void constraintViews(int padding, int interItemMargin) {
            constraintViews(interItemMargin, padding, padding);
        }
    }

    public static class Vertical<T extends UIView> extends UIStackedViews<T> {

        public Vertical(int count, ViewCreation<T> handler) {
            super(count, handler);
        }

        public Vertical(int count, int padding, ViewCreation<T> handler) {
            super(count, padding, handler);
        }

        public Vertical(int count, int interItemSpacing, int horizontalMargin, int verticalMargin, ViewCreation<T> handler) {
            super(count, interItemSpacing, horizontalMargin, verticalMargin, handler);
        }

        public Vertical(int count, int interItemSpacing, int horizontalMargin, int verticalMargin, Map<Integer, Integer> sizes, ViewCreation<T> handler) {
            super(count, interItemSpacing, horizontalMargin, verticalMargin, sizes, handler);
        }

        public Vertical(int count, int padding, int interItemMargin, ViewCreation<T> handler) {
            super(count, padding, interItemMargin, handler);
        }

        public Vertical(ArrayList<ViewCreation<T>> handlers) {
            super(handlers);
        }

        @Override
        public void constraintViews(Map<Integer, Integer> sizes, int interItemSpacing, int horizontalMargin, int verticalMargin) {
            boolean isSizesEmpty = (sizes == null || sizes.isEmpty());
            constraintVertically(contentViews(), interItemSpacing, horizontalMargin, verticalMargin, isSizesEmpty);

            if (sizes != null) {
                for (Map.Entry<Integer, Integer> entry : sizes.entrySet()) {
                    int index = entry.getKey();
                    int height = entry.getValue();

                    if (index < contentViews().size() && height > 0) {
                        constraintHeightForView(contentViews().get(index), height);
                    }
                }
            }
        }

        @Override
        public void constraintViews(int padding, int interItemMargin) {
            constraintViews(interItemMargin, padding, padding);
        }

        @Override
        public Size estimatedSize() {
            return new Size(Constants.Screen_Size().getWidth(), App.constants().TextField_Height() * views.size());
        }
    }
}
