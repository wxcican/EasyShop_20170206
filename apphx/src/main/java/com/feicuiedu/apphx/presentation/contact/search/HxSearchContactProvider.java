package com.feicuiedu.apphx.presentation.contact.search;


import android.content.SearchRecentSuggestionsProvider;

/**
 * 根据最近查询/浏览，提供简单的搜索建议
 */
public class HxSearchContactProvider extends SearchRecentSuggestionsProvider{

    public static final int MODE = DATABASE_MODE_QUERIES;

    public static final String AUTHORITY = "com.feicuiedu.apphx.HxSearchContactProvider";

    public HxSearchContactProvider(){
        setupSuggestions(AUTHORITY, MODE);
    }
}
