package saleapp;

import saleapp.Database.ToolsDatabase;

/**
 *
 * @author Robert Stovall
 */
public class CFDepot {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            ToolsDatabase db = new ToolsDatabase();
            //If you wish to start with a fresh SQLite file, you can uncomment the below line. 
            //db.deleteDatabase();
            //creates both the database if it is not already created
            db.createDatabase();
            
            //Does the initial upload of data from the given prompt
            db.initialUpload();
        } catch (Exception e) {
            System.out.println("Connecting to the Database has failed.");
            System.out.println(e.getMessage());
        }

        //kicks off the "ui"
        PointOfSaleUserInterface ui = new PointOfSaleUserInterface();
        ui.mainMenu();

    }

}
