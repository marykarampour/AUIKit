package com.prometheussoftware.auikit.datamanagement;

import java.util.HashMap;
import java.util.Map;

public class SQLConstants {

    public enum QUERY_TYPE {
        NONE('N', ""),
        INSERT('I', execute_insert()),
        UPDATE('U', execute_update()),
        DELETE('D', execute_soft_delete());

        QUERY_TYPE(char type, String query) {
            this.type = type;
            this.query = query;
        }

        private final char type;
        private final String query;

        public char getType() {
            return type;
        }

        public String getQuery() {
            return query;
        }

        private static final Map map = new HashMap<>();

        static {
            for (QUERY_TYPE qt : values()) {
                map.put(qt.type, qt.query);
            }
        }

        public static QUERY_TYPE valueOf(char type) {
            return (QUERY_TYPE)map.get(type);
        }
    }

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

    public static String execute_insert () {
        return "INSERT INTO %s (%s) VALUES (%s) ;";
    }

    public static String execute_update () {
        return "UPDATE %s SET %s WHERE %s ;";
    }

    public static String execute_soft_delete () {
        return "UPDATE %s SET operation_type=\"D\" WHERE %s ;";
    }

    public static String execute_hard_delete () {
        return "DELETE FROM %s WHERE %s ;";
    }

    public static String clause_in () {
        return " %s IN (%s) ";
    }

    public static String clear_table () {
        return "DELETE FROM  %s ;";
    }

    public static String drop_table () {
        return "DROP TABLE  %s ;";
    }

    public static String table_constraints () {
        return "SELECT sql from sqlite_master WHERE type=\"table\"";
    }
    public static String execute_PRAGMA_table () {
        return "PRAGMA table_info(\"%s\")";
    }
    public static String execute_PRAGMA_foreign_key_list () {
        return "PRAGMA foreign_key_list(\"%s\")";
    }

}
