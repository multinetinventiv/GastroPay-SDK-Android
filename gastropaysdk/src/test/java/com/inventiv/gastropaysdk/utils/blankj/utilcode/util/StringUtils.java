package com.inventiv.gastropaysdk.utils.blankj.utilcode.util;

import androidx.annotation.StringRes;

public final class StringUtils {

    private StringUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static String getString(@StringRes int id) {
        return getString(id, (Object[]) null);
    }

    public static String getString(@StringRes int id, Object... formatArgs) {
        return "";
    }
}
