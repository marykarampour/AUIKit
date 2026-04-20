package com.prometheussoftware.auikit;

import com.prometheussoftware.auikit.datamanagement.DBModel;
import com.prometheussoftware.auikit.datamanagement.SQLConstants;
import com.prometheussoftware.auikit.utility.DateUtility;

class TableObject extends DBModel {
    public String label;

    public TableObject() {
        super();
    }

    public TableObject(int id, String label) {
        super();
        this.id = id;
        this.label = label;
        this.operationType = SQLConstants.QUERY_TYPE.INSERT.getType();
        this.dtModified = DateUtility.timestamp();
    }
}
