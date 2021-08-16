package com.prometheussoftware.auikit.datamanagement;

public class SQLConstants {

    public static String exec_create_table () {
        return "CREATE TABLE IF NOT EXISTS %s %s ;";
    }

    public static String load_where_single_value () {
        return "SELECT * FROM %s WHERE %s=\"%s\" ;";
    }

    public static String load_where () {
        return "SELECT * FROM %s WHERE %s ;";
    }

    public static String load_all () {
        return "SELECT * FROM %s ;";
    }

    public static String load_join_table_where_NO_SEMI () {
        return "SELECT * FROM %s WHERE id IN (SELECT %s FROM %s WHERE %s ) ";
    }

    public static String clause_in () {
        return " %s IN (%s) ";
    }
}
