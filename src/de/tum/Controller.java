package de.tum;

import de.tum.datamodel.DataModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Controller {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public static void main(String[]args){

        Date date = new Date();
        System.out.println("---Start: " + sdf.format(date) + "---");

        Controller controller = new Controller();

        controller.control();

        date = new Date();
        System.out.println("---Ende: " + sdf.format(date) + "---");
    }

    public void control(){

    }

    public DataModel createDataModel(){
        return new DataModel();
    }
}
