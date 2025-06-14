package com.prometheussoftware.auikit.datamanagement;

public interface DBModelProtocol {
    /**
     * Table name associated with this class. Default is class.getSimpleName.
     */
    String dbTableName();

    public interface DBPrimaryModelProtocol extends DBModelProtocol {

        String IDString();
    }
}
