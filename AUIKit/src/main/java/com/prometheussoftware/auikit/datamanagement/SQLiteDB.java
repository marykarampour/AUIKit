package com.prometheussoftware.auikit.datamanagement;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

import com.prometheussoftware.auikit.common.MainApplication;
import com.prometheussoftware.auikit.model.PairArray;
import com.prometheussoftware.auikit.utility.DEBUGLOG;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SQLiteDB extends SQLiteOpenHelper implements SQLiteTableCreation {

    public SQLiteDB(@Nullable String name, int version) {
        super(MainApplication.getContext(), name, null, version);
        DEBUGLOG.s(this, getReadableDatabase().getPath());
        init();
    }

    public SQLiteDB(@Nullable Context context, @Nullable String name, int version) {
        super(context, name, null, version);
        DEBUGLOG.s(this, getReadableDatabase().getPath());
        init();
    }

    public SQLiteDB(@Nullable Context context, @Nullable String name, int version, boolean init) {
        super(context, name, null, version);
        DEBUGLOG.s(this, getReadableDatabase().getPath());
        if (init) init();
    }

    public SQLiteDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        DEBUGLOG.s(this, getReadableDatabase().getPath());
        init();
    }

    public SQLiteDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        DEBUGLOG.s(this, getReadableDatabase().getPath());
        init();
    }

    protected void init() {
        createTables(this.tb_create_pairs());
    }

    @Override
    public void onCreate(SQLiteDatabase db) { }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.disableWriteAheadLogging();
    }

    @Override
    public HashMap<String, String> tb_create_pairs () {
        return new HashMap<>();
    }

    //DB creation

    public void createTable (String table, String schema) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = String.format(SQLConstants.exec_create_table(), table, schema);

        try {
            db.execSQL(query);
            db.close();
        }
        catch (Throwable ex) {
            DEBUGLOG.s(this, ex.toString());
            db.close();
        }
    }

    public void createTables (HashMap<String, String> tables) {
        for (Map.Entry<String, String> entry : tables.entrySet()) {
            createTable(entry.getKey(), entry.getValue());
        }
    }

    public void  clearTable (String table) {
        String query = String.format(SQLConstants.clear_table(), table);
        executeQuery(query);
    }

    //Execute query

    public boolean executeQuery (String query) {
        return executeQuery(query, null);
    }

    public boolean executeQuery (String query, PairArray<String, String> parameters) {
        return runQuery(query, parameters, true) != null;
    }

    //Select

    public ArrayList<HashMap<String, String>> loadData (String query) {
        return loadData(query, null);
    }

    public ArrayList<HashMap<String, String>> loadData (String query, PairArray<String, String> parameters) {
        return runQuery(query, parameters, false);
    }

    public ArrayList<HashMap<String, String>> runQuery (String query, PairArray<String, String> parameters, boolean isExecutable) {

        boolean success = true;
        ArrayList<HashMap<String, String>> results = new ArrayList<>();
        ArrayList<String> columns = new ArrayList<>();

        synchronized (this) {
            if (!isExecutable) {
                SQLiteDatabase readDB = this.getReadableDatabase();
                String[] selectionArgs = new String[0];

                if (parameters != null) {
                    ArrayList<String> args = new ArrayList<>();

                    for (int i = 0; i < parameters.getArray().size(); i++) {
                        args.add(parameters.getArray().get(i).getSecond());
                    }
                    selectionArgs = args.toArray(new String[0]);
                }

                try (Cursor cursor = readDB.rawQuery(query, selectionArgs)) {
                    while (cursor.moveToNext()) {

                        HashMap<String, String> row = new HashMap<>();

                        for (int i=0; i<cursor.getColumnCount(); i++) {
                            row.put(cursor.getColumnName(i), cursor.getString(i));
                        }
                        results.add(row);
                    }
                }
                catch (SQLiteException e) {
                    e.getLocalizedMessage();
                }
            }
            else {
                SQLiteDatabase writeDB = this.getWritableDatabase();
                SQLiteStatement compliedStatement = writeDB.compileStatement(query);

                if (compliedStatement != null) {
                    if (parameters != null) {
                        for (int i = 0; i <= parameters.getArray().size(); i++) {
                            compliedStatement.bindString(i, parameters.getArray().get(i).getSecond());
                        }
                    }
                    try {
                        compliedStatement.execute();
                    }
                    catch (SQLiteException e) {
                        DEBUGLOG.s(e);
                    }
                }
            }
        }

        return success ? results : null;
    }
}
