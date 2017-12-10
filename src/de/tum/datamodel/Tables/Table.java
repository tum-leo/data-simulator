package de.tum.datamodel.Tables;

import java.util.ArrayList;

public class Table {

    public ArrayList<ArrayList<Object>> table;

    public Table(int columnCount){

        this.table = this.createTableSchema(columnCount);

    }


    public ArrayList createTableSchema(int columnCount){
        ArrayList <Object> t = new ArrayList();

        for(int i = 0; i < columnCount; i++){
            t.add(new ArrayList<Object>());
        }

        return t;
    }

    public void addTuple(ArrayList<Object> tuple) {

    }

    public ArrayList<Object> getTuple(ArrayList<Object> key) {
        return null;
    }

}
