package saleapp.Database;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import saleapp.RentalAgreement;
import saleapp.Tool;
import saleapp.ToolPrice;

/**
 *
 * @author Robert Stovall
 */
public class ToolsDatabase {

    private final String url = "jdbc:sqlite:cfdepot.db";

    //Connecting to the sqlite db
    private Connection connect() {

        Connection conn = null;

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * creates a new db
     */
    public void createDatabase() {
        //These are SQL for the tables needed, 
        String toolsTableSql
                = "CREATE TABLE IF NOT EXISTS tools (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	type String,\n"
                + "	brand String,\n"
                + "	code String\n"
                + ");";
        String pricesTableSql
                = "CREATE TABLE IF NOT EXISTS prices (\n"
                + "	id integer PRIMARY KEY,\n"
                + " toolType String,\n"
                + "	dailyCharge real,\n"
                + "	weekdayCharge integer,\n"
                + "	weekendCharge integer,\n"
                + "	holidayCharge integer\n"
                + ");";
        String rentalAgreementsTableSql
                = "CREATE TABLE IF NOT EXISTS rentalAgreements (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	toolCode text,\n"
                + "	toolType text,\n"
                + "	toolBrand text,\n"
                + "	rentalDays text,\n"
                + "	checkoutDate text,\n"
                + "	dueDate text,\n"
                + "	rentalCharge real,\n"
                + "	chargeDays integer,\n"
                + "	preDiscountCharge real,\n"
                + "	discountPercent integer,\n"
                + "	discountAmount real,\n"
                + "	finalCharge real\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url)) {
            createNewTable(pricesTableSql);
            createNewTable(toolsTableSql);
            createNewTable(rentalAgreementsTableSql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * optional delete at the beginning if you uncomment in the main class
     */
    public void deleteDatabase() {
        File file = new File("cfdepot.db");
        if (file.delete()) {
            System.out.println("cfdepot.db File deleted from Project root directory");
        } else {
            System.out.println("File cfdepot.db doesn't exists in project root directory");
        }
    }

    /**
     *
     * @param sql 
     */
    public void createNewTable(String sql) {
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //This function is acting in place of some sort of database migration. I wanted to show that this code is reusable for future applications
    /**
     *
     */
    public void initialUpload() {
        try {
            ArrayList<ToolPrice> toolPrices = selectAllPrices();
            ArrayList<Tool> tools = selectAllTools();
            if (toolPrices.isEmpty()) {
                //These are taken from the prompt, and loaded into the SQLite db if it is a fresh database
                insertToolPrice("Ladder", 1.99, true, true, false);
                insertToolPrice("Chainsaw", 1.49, true, false, true);
                insertToolPrice("Jackhammer", 2.99, true, false, false);
            }
            if (tools.isEmpty()) {
                insertTool("Ladder", "Werner", "LADW");
                insertTool("Chainsaw", "Stihl", "CHNS");
                insertTool("Jackhammer", "Ridgid", "JAKR");
                insertTool("Jackhammer", "DeWalt", "JAKD");
            }
        } catch (Exception e) {
            System.out.println("Error: Failed to insert initial Tool Prices and Descriptions." + e);
        }
    }

    //Select Statements
    /**
     *
     * @return
     */
    public ArrayList<ToolPrice> selectAllPrices() {
        String sql = "SELECT toolType, dailyCharge, weekdayCharge, weekendCharge, holidayCharge FROM prices";
        ArrayList<ToolPrice> toolPrices = new ArrayList();
        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                toolPrices.add(new ToolPrice(rs.getString("toolType"), rs.getDouble("dailyCharge"), rs.getBoolean("weekdayCharge"), rs.getBoolean("weekendCharge"), rs.getBoolean("holidayCharge")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return toolPrices;
    }

    /**
     *
     * @return
     */
    public ArrayList<Tool> selectAllTools() {
        String sql = "SELECT type, brand, code FROM tools";
        ArrayList<Tool> tools = new ArrayList();

        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                tools.add(new Tool(rs.getString("code"), rs.getString("brand"), rs.getString("type")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return tools;
    }

    /**
     *
     * @return
     */
    public ArrayList<RentalAgreement> selectAllRentalAgreements() {
        String sql = "SELECT toolCode,toolType,toolBrand,"
                + "rentalDays,checkoutDate,dueDate,rentalCharge,chargeDays,preDiscountCharge,"
                + "discountPercent,discountAmount,finalCharge FROM rentalAgreements";
        ArrayList<RentalAgreement> rentalAgreements = new ArrayList();

        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                rentalAgreements.add(new RentalAgreement(rs.getString("toolCode"), rs.getString("toolType"),
                        rs.getString("toolBrand"), rs.getInt("rentalDays"), rs.getString("checkoutDate"), rs.getString("dueDate"),
                        rs.getDouble("rentalCharge"), rs.getInt("chargeDays"), rs.getDouble("preDiscountCharge"),
                        rs.getInt("discountPercent"), rs.getDouble("discountAmount"), rs.getDouble("finalCharge")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return rentalAgreements;
    }

    //Insert Statements
    /**
     *
     * @param type
     * @param brand
     * @param code
     */
    public void insertTool(String type, String brand, String code) {
        String sql = "INSERT INTO tools(type,brand,code) VALUES(?,?,?)";

        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, type);
            pstmt.setString(2, brand);
            pstmt.setString(3, code);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     *
     * @param toolType
     * @param dailyCharge
     * @param weekdayCharge
     * @param weekendCharge
     * @param holidayCharge
     */
    public void insertToolPrice(String toolType, double dailyCharge, boolean weekdayCharge, boolean weekendCharge, boolean holidayCharge) {
        String sql = "INSERT INTO prices(toolType, dailyCharge,weekdayCharge,weekendCharge,holidayCharge) VALUES(?,?,?,?,?)";

        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, toolType);
            pstmt.setDouble(2, dailyCharge);
            pstmt.setBoolean(3, weekdayCharge);
            pstmt.setBoolean(4, weekendCharge);
            pstmt.setBoolean(5, holidayCharge);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     *
     * @param rentalAgreement
     */
    public void insertRentalAgreement(RentalAgreement rentalAgreement) {
        String sql = "INSERT INTO rentalAgreements(toolCode,toolType,toolBrand,"
                + "rentalDays,checkoutDate,dueDate,rentalCharge,chargeDays,preDiscountCharge,"
                + "discountPercent,discountAmount,finalCharge) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, rentalAgreement.getToolCode());
            pstmt.setString(2, rentalAgreement.getToolType());
            pstmt.setString(3, rentalAgreement.getToolBrand());
            pstmt.setInt(4, rentalAgreement.getRentalDays());
            pstmt.setString(5, rentalAgreement.getCheckoutDate());
            pstmt.setString(6, rentalAgreement.getDueDate());
            pstmt.setDouble(7, rentalAgreement.getRentalCharge());
            pstmt.setInt(8, rentalAgreement.getChargeDays());
            pstmt.setDouble(9, rentalAgreement.getPreDiscountCharge());
            pstmt.setInt(10, rentalAgreement.getDiscountPercent());
            pstmt.setDouble(11, rentalAgreement.getDiscountAmount());
            pstmt.setDouble(12, rentalAgreement.getFinalCharge());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
