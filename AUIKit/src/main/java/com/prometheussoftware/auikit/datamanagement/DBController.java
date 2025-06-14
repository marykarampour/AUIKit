package com.prometheussoftware.auikit.datamanagement;

import android.content.Context;

import com.google.gson.Gson;
import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.common.MainApplication;
import com.prometheussoftware.auikit.model.BaseModel;
import com.prometheussoftware.auikit.model.Pair;
import com.prometheussoftware.auikit.utility.ArrayUtility;
import com.prometheussoftware.auikit.utility.DEBUGLOG;
import com.prometheussoftware.auikit.utility.StringUtility;

import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

/** @note Models consumed by this API should have public columns */
public class DBController <T extends SQLiteDB> implements SQLiteDBCreation {

    protected T sqLiteDB;

    public DBController () {
        this.setSqLiteDB();
    }

    public DBController (Context context) {
        this.setSqLiteDB(context);
    }

    public void setSqLiteDB() { }

    public void setSqLiteDB(Context context) { }

    public T getSqLiteDB() {
        return sqLiteDB;
    }

    @Override
    public String db_name() {
        return "";
    }

    @Override
    public int db_version() {
        return 1;
    }

    //region db version
    
    public static boolean dbVersionIsUpToDate () {
        boolean updated = true;
        String dbVersion = savedDBVersion();
        if (!StringUtility.isNotEmpty(dbVersion) || !dbVersion.equalsIgnoreCase(App.constants().db_version())) {
            updated = false;
        }
        return updated;
    }

    private static String savedDBVersion () {
        return DBUserDefaults.savedSQLiteDBVersion();
    }

    protected void initializeDB () {
        if (dbVersionIsUpToDate())
            createDB();
        else
            updateDB();
    }

    protected void createDB() {
        MainApplication.getContext().openOrCreateDatabase(db_name(), Context.MODE_PRIVATE, null);
    }

    protected void updateDB () {
        //Do the update e.g. updateVersion_1_0_0
        resetDBVersionSyncTime();
    }

    protected void dropDB() {
        MainApplication.getContext().deleteDatabase(db_name());
    }

    private static void resetDBVersionSyncTime () {
        DBUserDefaults.saveSQLiteDBVersion(App.constants().db_version());
        DBUserDefaults.updateLastSQLiteSyncTime(0);
    }

    protected String DBPath () {
        return getSqLiteDB().getReadableDatabase().getPath();
    }
    
    //end region

    //region Querying
    //TODO: formatting is handled by @SerializedName, could use a custom mapper

    public boolean executeWithMaps (ArrayList<HashMap<String, String>> data, String table) {
        Class classO;
        try {
            classO = Class.forName(table);
            if (!classO.isAssignableFrom(DBModel.class) || data.isEmpty()) return false;
        }
        catch (ClassNotFoundException e) {
            return false;
        }

        SQLConstants.QUERY_TYPE insert = SQLConstants.QUERY_TYPE.INSERT;
        SQLConstants.QUERY_TYPE update = SQLConstants.QUERY_TYPE.UPDATE;
        SQLConstants.QUERY_TYPE delete = SQLConstants.QUERY_TYPE.DELETE;
        String table_name = StringUtility.format(table, DBModel.dbColumnNameFormat());
        boolean result = true;

        for (HashMap<String, String> dict : data) {

            String json = new JSONObject(dict).toString();
            DBModel object = (DBModel) new Gson().fromJson(json, classO);

            String queryType = dict.get("operation_type");
            if (queryType == null || queryType.isEmpty()) continue;

            SQLConstants.QUERY_TYPE type = SQLConstants.QUERY_TYPE.valueOf(queryType.charAt(0));
            String query = type.getQuery();
            if (query == null || query.isEmpty()) continue;

            object.operationType = SQLConstants.QUERY_TYPE.NONE.getType();
            object.dtModified = NumberUtils.createInteger(dict.get("dt_modified"));

            if (!executeWithObject(object, type, table_name)) result = false;
        }
        return result;
    }

    public <T extends DBModel> boolean executeWithObjects (ArrayList<T> data, String table) {

        String table_name = StringUtility.format(table, DBModel.dbColumnNameFormat());
        boolean result = true;

        for (T object : data) {
            SQLConstants.QUERY_TYPE type = SQLConstants.QUERY_TYPE.valueOf(object.operationType);
            if (type == SQLConstants.QUERY_TYPE.NONE) continue;
            if (!executeWithObject(object, type, table_name)) result = false;
        }
        return result;
    }

    public <T extends DBModel> boolean executeWithObject (T object, SQLConstants.QUERY_TYPE type, String table_name) {

        String query = type.getQuery();
        if (query == null || query.isEmpty()) return false;

        SQLConstants.QUERY_TYPE insert = SQLConstants.QUERY_TYPE.INSERT;
        SQLConstants.QUERY_TYPE update = SQLConstants.QUERY_TYPE.UPDATE;
        SQLConstants.QUERY_TYPE delete = SQLConstants.QUERY_TYPE.DELETE;

        String stmt = "";
        String whereString = whereStringForCRUD(object, table_name);
        Pair<String, String> keyWValues = object.SQLKeysWithValues();
        String keyEValues = object.SQLKeysEqualValues();

        switch (type) {
            case INSERT: {
                if (!keyWValues.getFirst().isEmpty()) {
                    stmt = String.format(query, table_name, keyWValues.getFirst(), keyWValues.getSecond());
                    if (!sqLiteDB.executeQuery(stmt)) {
                        stmt = String.format(update.getQuery(), table_name, keyEValues, whereString);
                        return sqLiteDB.executeQuery(stmt);
                    }
                }
            }
            break;

            case UPDATE: {
                stmt = String.format(query, table_name, keyEValues, whereString);
                if (!sqLiteDB.executeQuery(stmt) && !keyWValues.getFirst().isEmpty()) {
                    stmt = String.format(insert.getQuery(), table_name, keyWValues.getFirst(), keyWValues.getSecond());
                    return sqLiteDB.executeQuery(stmt);
                }
            }
            break;

            case DELETE: {
                stmt = String.format(delete.getQuery(), table_name, whereString);
                return sqLiteDB.executeQuery(stmt);
            }

            default:
                break;
        }

        DEBUGLOG.s("%s", stmt);
        return true;
    }

    protected String whereStringForCRUD (DBModel object, String table_name) {

        ArrayList<String> columnNames = idColumnsForTable(table_name);
        StringBuilder whereString = new StringBuilder();

        if (columnNames.contains("id")) {
            whereString.append(String.format("id=\"%s\"", object.id));
        }
        else {
            String last = ArrayUtility.lastObject(columnNames);

            for (String name : columnNames) {
                String column = StringUtility.format(name, DBModel.dbColumnNameFormat());

                whereString.append(String.format("%s=\"%s\"", name, object.valueForKey(column)));

                if (name.equalsIgnoreCase(last))
                    whereString.append(" AND ");
            }
        }
        return whereString.toString();
    }

    protected ArrayList<String> idColumnsForTable (String table_name) {

        String query = String.format(SQLConstants.execute_PRAGMA_table(), table_name);
        ArrayList<HashMap<String, String>> columns = sqLiteDB.loadData(query);
        ArrayList<String> columnNames = new ArrayList<>();

        for (HashMap<String, String> column : columns) {
            String name = column.get("name");
            if (name.contains("id"))
                columnNames.add(name);
        }
        return columnNames;
    }

    public <M extends DBModel> boolean insertDataWithObjects (ArrayList<M> data, Class cls) {
        if (!cls.isAssignableFrom(DBModel.class) || data.isEmpty()) return false;

        for (M object : data) {
            Pair<String, String> keyValues = object.SQLKeysWithValues();

            if (!keyValues.getFirst().isEmpty()) {
                String name = StringUtility.format(cls.getName(), DBModel.dbColumnNameFormat());
                String stmt = String.format(SQLConstants.execute_insert(), name, keyValues.getFirst(), keyValues.getSecond());
                DEBUGLOG.s("%s", stmt);
                sqLiteDB.executeQuery(stmt);
            }
        }
        return true;
    }

    public <M extends DBModel> boolean insertDataWithObject (M data, Class cls) {
        if (data == null) return false;

        ArrayList<M> arr = new ArrayList<>();
        arr.add(data);
        return insertDataWithObjects(arr, cls);
    }

    public <M extends DBModel> ArrayList<M> loadDataWithQueryToClass (String query, Class<M> objectClass) {

        ArrayList<HashMap<String, String>> result = sqLiteDB.loadData(query, null);

        ArrayList<M> array = new ArrayList<>();

        for (HashMap map : result) {
            String json = new JSONObject(map).toString();
            Object object = new Gson().fromJson(json, objectClass);
            if (objectClass.isInstance(object)) {
                array.add((M) object);
            }
        }
        return array;
    }

    /** @note Models consumed by this API should have public columns */
    public <M extends DBModel, P extends DBModel.DBPrimaryModelProtocol> ArrayList<M> dataFromParentTable (Class<M> tableClass, ArrayList<P> objects) {

        ArrayList<String> objectsValues = new ArrayList<>();
        String propertyName = DBModel.classIDName(tableClass);

        for (DBModel.DBPrimaryModelProtocol object : objects) {

            Class columnClass = object.getClass();

            try {
                Field field = columnClass.getField(propertyName);
                field.setAccessible(true);
                Object value = field.get(object);
                if (value == null) continue;

                if (value.getClass() == String.class) {
                    objectsValues.add(StringUtility.quotations((String)value));
                }
                else {
                    objectsValues.add(value.toString());
                }

            } catch (NoSuchFieldException e) {
                continue;
            } catch (IllegalAccessException e) {
                continue;
            }
        }

        String objectsValuesStr = ArrayUtility.componentsJoinedByString(objectsValues, ", ");
        String className = DBModel.dbTableName(tableClass);
        String whereStr = String.format(" id IN (%s)", objectsValuesStr);
        String queryString = String.format(SQLConstants.load_where(), className, whereStr);

        return loadDataWithQueryToClass(queryString, tableClass);
    }

    /** @brief SELECT * FROM table WHERE table_id=1 AND parent_column_id=1
     * @note Models consumed by this API should have public columns
     */
    public <M extends DBModel, P extends DBModel.DBPrimaryModelProtocol> ArrayList<M> dataFromChildTable (Class tableClass, ArrayList<P> objects) {

        ArrayList<String> columnNames = new ArrayList<>();

        for (P object : objects) {
            String propertyName = object.getClass() == tableClass ? "parentId" : DBModel.classIDName(object.getClass());

            if (!BaseModel.classHasRegisteredField(tableClass, propertyName)) continue;

            String columnName = StringUtility.format(propertyName, DBModel.dbColumnNameFormat());
            columnName = columnName + "=" + object.IDString();
            columnNames.add(columnName);
        }

        String columnsSeparatedByAND = ArrayUtility.componentsJoinedByString(columnNames, " AND ");
        String className = DBModel.dbTableName(tableClass);
        String queryString = String.format(SQLConstants.load_where(), className, columnsSeparatedByAND);

        return loadDataWithQueryToClass(queryString, tableClass);
    }

    /** Retrieves data from a table for columns given
     * @apiNote  Models consumed by this API should have public columns
     @param objects if values is nil, will check isNull, if isNull = true will check for column being NULL, isNull = false will check for column being not NULL, if column.name = nil it will use class of values array

     //SELECT * FROM table WHERE column_1_id IN (1, 2) AND column_2_id IN (1, 3)

     {@code
     DBModel.DBColumn col1 = new DBModel.DBColumn("name", arr1);
     DBModel.DBColumn col2 = new DBModel.DBColumn(null, arr2);
     ArrayList columns = new ArrayList();
     columns.add(col1);
     columns.add(col2);
     dataFromTableForColumns(Model.class, columns);
     }
     */
    public <M extends DBModel> ArrayList<M> dataFromTableForColumns (Class<M> tableClass, ArrayList<DBModel.DBColumn> objects) {

        ArrayList columnValues = new ArrayList();

        for (DBModel.DBColumn column : objects) {

            ArrayList objectsValues = new ArrayList();
            ArrayList values = column.values;

            if (values != null) {
                if (values.size() > 0) {

                    Object first = values.get(0);
                    Class columnClass = first.getClass();
                    String propertyName = "";

                    if (DBModel.DBPrimaryModelProtocol.class.isInstance(first)) {
                        propertyName = column.name != null ? DBModel.dbPropertyName(columnClass) : DBModel.classIDName(columnClass);
                    }
                    else if (column.name.length() > 0) {
                        propertyName = StringUtility.format(column.name, DBModel.dbPropertyNameFormat());
                    }
                    else {
                        continue;
                    }

                    if (!BaseModel.classHasRegisteredField(tableClass, propertyName)) continue;

                    String columnName = StringUtility.format(propertyName, DBModel.dbColumnNameFormat());

                    for (Object object : values) {
                        if (DBModel.DBPrimaryModelProtocol.class.isInstance(object)) {
                            String objectID = ((DBModel.DBPrimaryModelProtocol)object).IDString();
                            if (objectID != null) {
                                objectsValues.add(objectID);
                            }
                        }
                        else if (object.getClass() == String.class) {
                            objectsValues.add(StringUtility.quotations((String)object));
                        }
                        else {
                            objectsValues.add(object.toString());
                        }
                    }
                    String objectsValuesStr = ArrayUtility.componentsJoinedByString(objectsValues, ", ");
                    String whereStr = String.format(SQLConstants.clause_in(), columnName, objectsValuesStr);
                    columnValues.add(whereStr);
                }
            }
            else if (values == null) {
                String base = column.isNull ? " %s IS NULL " : " %s NOT NULL ";
                String whereStr = String.format(base, StringUtility.format(column.name, DBModel.dbColumnNameFormat()));
                columnValues.add(whereStr);
            }
        }

        String columnsSeparatedByAND = ArrayUtility.componentsJoinedByString(columnValues, " AND ");
        String className = DBModel.dbTableName(tableClass);
        String queryString = String.format(SQLConstants.load_where(), className, columnsSeparatedByAND);

        return loadDataWithQueryToClass(queryString, tableClass);
    }

    /** @brief SELECT * FROM table WHERE id IN (SELECT table_id FROM user_table WHERE user_id IN (1, 2)) AND address_table_id IN (3, 4) */
    public <M extends DBModel, P extends DBModel.DBPrimaryModelProtocol> ArrayList<M> loadDataFromTable (Class<M> tableClass, ArrayList<P> objects, Class joinClass, Class columnClass, ArrayList<P> columnObjects) {

        String queryString = stringForQueryDataFromTable(tableClass, objects, joinClass);
        String andColumnNameID = DBModel.dbTableName(columnObjects.get(0).getClass()) + "_id";
        String clauseIn = String.format(SQLConstants.clause_in(), andColumnNameID, valuesInObjects(columnObjects));
        queryString = String.format("%s AND %s ;", queryString, clauseIn);

        return loadDataWithQueryToClass(queryString, tableClass);
    }

    /** @note Models consumed by this API should have public columns */
    public <M extends DBModel> ArrayList<M> loadDataFromTable (Class<M> objectClass, String column, String value) {
        String stmt = String.format(SQLConstants.load_where_single_value(), DBModel.dbTableName(objectClass), column, value);
        return loadDataWithQueryToClass(stmt, objectClass);
    }

    /** @brief SELECT * FROM table WHERE id IN (SELECT table_id FROM user_table WHERE user_id=1) */
    public <M extends DBModel, P extends DBModel.DBPrimaryModelProtocol> ArrayList<M> loadDataFromTable (Class<M> tableClass, ArrayList<P> objects, Class joinClass) {
        String queryString = stringForQueryDataFromTable (tableClass, objects, joinClass) + " ;";
        return loadDataWithQueryToClass(queryString, tableClass);
    }

    /** @note Models consumed by this API should have public columns */
    public <M extends DBModel> ArrayList<M> loadAllDataOfClass (Class<M> tableClass) {
        return loadDataWithQueryToClass(String.format(SQLConstants.load_all(), DBModel.dbTableName(tableClass)), tableClass);
    }

    //endregion

    //region utility

    /** @brief SELECT * FROM table WHERE id IN (SELECT table_id FROM user_table WHERE user_id=1) */
    private <P extends DBModel.DBPrimaryModelProtocol> String stringForQueryDataFromTable (Class tableClass, ArrayList<P> objects, Class joinClass) {

        if (objects.size() == 0) return null;

        String tableName = DBModel.dbTableName(tableClass);
        String joinTableName = DBModel.dbTableName(joinClass);
        String columnNameID = tableName + "_id";
        String valuesStr = whereInObjects(objects);
        String queryString = String.format(SQLConstants.load_join_table_where_NO_SEMI(), tableName, columnNameID, joinTableName, valuesStr);
        return queryString;
    }

    private <P extends DBModel.DBPrimaryModelProtocol> String whereInObjects (ArrayList<P> objects) {
        ArrayList<String> andValues = new ArrayList<>();
        ArrayList<Class> classes = new ArrayList<>();

        for (DBModel.DBPrimaryModelProtocol obj : objects) {
            Class objectClass = obj.getClass();
            if (!classes.contains(objectClass)) classes.add(objectClass);
        }

        for (Class objectClass : classes) {
            ArrayList<String> values = new ArrayList<>();

            for (DBModel.DBPrimaryModelProtocol obj : objects) {
                if (!objectClass.isInstance(obj)) {
                    continue;
                }
                values.add(obj.IDString());
            }
            String valuesStr = ArrayUtility.componentsJoinedByString(values, ", ");
            String andColumnNameID = DBModel.dbTableName(objectClass) + "_id";
            andValues.add(String.format(SQLConstants.clause_in(), andColumnNameID, valuesStr));
        }

        String andStr = ArrayUtility.componentsJoinedByString(andValues, " AND ");
        return andStr;
    }

    private <P extends DBModel.DBPrimaryModelProtocol> String valuesInObjects (ArrayList<P> objects) {
        ArrayList<String> values = new ArrayList<>();

        for (DBModel.DBPrimaryModelProtocol obj : objects) {
            values.add(obj.IDString());
        }
        String valuesStr = ArrayUtility.componentsJoinedByString(values, ", ");
        return valuesStr;
    }

    //endregion
}
