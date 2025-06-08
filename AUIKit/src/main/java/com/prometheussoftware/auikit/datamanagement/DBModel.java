package com.prometheussoftware.auikit.datamanagement;


import com.google.gson.annotations.SerializedName;
import com.prometheussoftware.auikit.model.BaseModel;
import com.prometheussoftware.auikit.model.Pair;
import com.prometheussoftware.auikit.model.PairArray;
import com.prometheussoftware.auikit.model.Text;
import com.prometheussoftware.auikit.utility.StringUtility;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DBModel extends BaseModel {

    public Integer id;

    @SerializedName("operation_type")
    public char operationType;

    @SerializedName("dt_modified")
    public Integer dtModified;

    static {
        BaseModel.Register(DBModel.class);
    }

    public DBModel() {
        super();
        operationType = SQLConstants.QUERY_TYPE.NONE.getType();
    }

    public String SQLKeysEqualValues () {
        PairArray<String, String> pairs = SQLKeyValuePairs();
        String str = "";

        for (Pair pair : pairs.getArray()) {
            String format = (pairs.lastObject() == pair ? "%s%s=%s" : "%s%s=%s, ");
            str = String.format(format, str, pair.getFirst(), pair.getSecond());
        }
        return str;
    }

    public Pair<String, String> SQLKeysWithValues () {
        PairArray<String, String> pairs = SQLKeyValuePairs();
        String keyStr = "";
        String valueStr = "";

        for (Pair pair : pairs.getArray()) {
            String format = (pairs.lastObject() == pair ? "%s%s" : "%s%s, ");
            keyStr = String.format(format, keyStr, pair.getFirst());
            valueStr = String.format(format, valueStr, pair.getSecond());
        }
        Pair pair = new Pair(keyStr, valueStr);

        return pair;
    }

    private PairArray<String, String> SQLKeyValuePairs () {
        PairArray<String, String> pairs = new PairArray<>();
        Reflect reflect = BaseModel.reflectForClass(this.getClass());
        if (reflect == null) return new PairArray();

        HashMap<String, Class> properties = reflect.getPropertyTypeNames();

        for (String name : properties.keySet()) {

            String keyStr = "";
            String valueStr = "";
            String value = "";

            try {
                Field field = this.getClass().getField(name);
                field.setAccessible(true);
                Object object = field.get(this);

                if (object != null) {
                    value = object.toString();
                    Class objectType = properties.get(name);

                    if (objectType == Date.class) {
                        Date date = (Date)object;
                        value = String.valueOf(date.getTime()*1000);

                        if (value.length() > 0) {
                            value = StringUtility.quotations(value);
                        }
                        else {
                            value = "NULL";
                        }

                        //TODO: from property format
                        keyStr = StringUtility.format(name, dbColumnNameFormat());
                        valueStr = value;
                        Pair pair = new Pair(keyStr, valueStr);
                        pairs.addPair(pair);
                    }
                }

            } catch (NoSuchFieldException e) {
            } catch (IllegalAccessException e) { }
        }
        return pairs;
    }

    public static String classIDName (Class tableClass) {
        return dbPropertyName(tableClass) + "Id";
    }

    /** @brief column name format, subclass can override for custom format */
    public static Text.TEXT_FORMAT dbColumnNameFormat () {
        return Text.TEXT_FORMAT.UnderScore;
    }
    /** @brief property name format, subclass can override for custom format */
    public static Text.TEXT_FORMAT dbPropertyNameFormat () {
        return Text.TEXT_FORMAT.CamelCase;
    }
    /** @brief class name from table, e.g. table --> ABCTable, subclass can override for custom format, default capitalized camel case */
    public static String dbClassNameForTable (String table) {
        return StringUtility.format(table, Text.TEXT_FORMAT.CapitalizedCamelCase);
    }
    /** @brief table name from class, e.g. ABCTable ---> table, subclass can override for custom format, default underscore */
    public static String dbTableName (Class tableClass) {
        return StringUtility.format(tableClass.getSimpleName(), Text.TEXT_FORMAT.UnderScoreIgnoreDigits);
    }
    /** @brief property name from class, e.g. ABCTableType ---> tableType, subclass can override for custom format, default camel case */
    public static String dbPropertyName (Class tableClass) {
        return StringUtility.format(tableClass.getSimpleName(), Text.TEXT_FORMAT.CamelCase);
    }

    public static class DBStaticModel extends DBModel {

    }

    public interface DBPrimaryModelProtocol {

        String IDString();
    }

    public static class DBStaticPrimaryModel extends DBModel implements DBPrimaryModelProtocol {

        @SerializedName("id")
        public Integer Id;

        public String IDString () {
            return Id != null ? Id.toString() : null;
        }
    }

    /** @note Models consumed by this API should have public columns */
    public static class DBColumn {

        public String name;

        /** @brief if it is _kindof DBModel<DBPrimaryModelProtocol> the column is used as foreign key of table of the class of value */
        public ArrayList values;
        public boolean isNull;

        public DBColumn (String name, ArrayList values) {
            this.name = name;
            this.values = values;
            isNull = false;
        }

        /** @brief creates an array of columns with name and single value */
        public static ArrayList<DBColumn> columnsWithNamesValues (HashMap<String, ArrayList<String>> values) {

            ArrayList arr = new ArrayList();
            for (String name : values.keySet()) {

                DBColumn column = new DBColumn(name, values.get(name));
                arr.add(column);
            }
            return arr;
        }
    }
}
