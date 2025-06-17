package com.prometheussoftware.auikit.datamanagement;

public interface DBModelProtocol {
    /**
     * Table name associated with this class. Default is class.getSimpleName.
     */
    String dbTableName();

    public interface Primary extends DBModelProtocol {
        String IDString();
    }
}
