package com.prometheussoftware.auikit.tableview.search;

import android.text.SpannableStringBuilder;

public interface BaseCellDataSource {
    String title();
    default String plainTitle() { return title(); }
    default SpannableStringBuilder attributedTitle() { return null; }
}
