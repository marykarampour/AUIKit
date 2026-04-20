package com.prometheussoftware.auikit.datamanagement;

public interface SQLiteDBCreation {
    String db_name();
    int db_version();
    default Class db_class() {
        return SQLiteDB.class;
    }
}
