package com.prometheussoftware.auikit.model;

public class IndexPath extends BaseModel {

    public Integer row;
    public Integer section;

    static {
        BaseModel.Register(IndexPath.class);
    }

    public IndexPath () {
        this.section = 0;
        this.row = 0;
    }

    public IndexPath (int section, int row) {
        init(section, row);
    }

    public IndexPath (Integer section, Integer row) {
        init(section, row);
    }

    public IndexPath (Integer section, int row) {
        init(section, row);
    }

    public IndexPath (int section, Integer row) {
        init(section, row);
    }

    private void init (int section, int row) {
        this.section = section >= 0 ? section : null;
        this.row = row >= 0 ? row : null;
    }

    public boolean isSection () {
        return row == null || row < 0;
    }
}
