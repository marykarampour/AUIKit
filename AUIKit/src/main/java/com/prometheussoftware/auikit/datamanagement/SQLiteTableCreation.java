package com.prometheussoftware.auikit.datamanagement;

import java.util.HashMap;

public interface SQLiteTableCreation {
    /** The key is table name, and the value is a create statement to be used in this format:
     * "CREATE TABLE IF NOT EXISTS %s %s ;"
     * */
    HashMap<String, String> tb_create_pairs();
}
