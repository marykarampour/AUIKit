package com.prometheussoftware.auikit;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.prometheussoftware.auikit.common.MainApplication;
import com.prometheussoftware.auikit.datamanagement.DBController;
import com.prometheussoftware.auikit.model.BaseModel;

import java.util.ArrayList;
import java.util.HashMap;

@RunWith(AndroidJUnit4.class)
public class DatabaseUnitTest {
    DBController dbController = new DBController(MainApplication.getContext());

    static {
        BaseModel.Register(TableObject.class);
    }

    @After
    public void tearDown() {
        dbController.dropDB();
    }

    @Test
    public void database_construction() {
        assertTrue(dbController.getSqLiteDB() != null);
    }

    @Test
    public void database_insert() {
        HashMap<String, String> map = new HashMap<>();
        map.put("table_object" , "(id INTEGER PRIMARY KEY, label varchar(255) NOT NULL UNIQUE, dt_modified integer(4) NOT NULL DEFAULT (strftime('%s','now')), operation_type CHAR CHECK(operation_type IN ('I', 'U', 'D', 'N')) NOT NULL DEFAULT 'I')");
        dbController.getSqLiteDB().createTables(map);

        ArrayList<TableObject> arr = new ArrayList<>();
        arr.add(new TableObject(1, "object 1"));
        arr.add(new TableObject(2, "object 2"));
        arr.add(new TableObject(3, "object 3"));

        assertTrue(dbController.executeWithObjects(arr, "table_object"));
    }

    @Test
    public void database_insert_load() {
        DBController dbController = new DBController(MainApplication.getContext());

        HashMap<String, String> map = new HashMap<>();
        map.put("table_object" , "(id INTEGER PRIMARY KEY, label varchar(255) NOT NULL UNIQUE, dt_modified integer(4) NOT NULL DEFAULT (strftime('%s','now')), operation_type CHAR CHECK(operation_type IN ('I', 'U', 'D', 'N')) NOT NULL DEFAULT 'I')");
        dbController.getSqLiteDB().createTables(map);

        ArrayList<TableObject> arr = new ArrayList<>();
        arr.add(new TableObject(1, "object 1"));
        arr.add(new TableObject(2, "object 2"));
        arr.add(new TableObject(3, "object 3"));

        dbController.executeWithObjects(arr, "table_object");
        ArrayList<TableObject> objects = dbController.loadAllDataOfClass(TableObject.class);

        assertEquals(arr, objects);
    }
}



