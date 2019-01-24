/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saleapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import saleapp.Database.ToolsDatabase;

/**
 *
 * @author Robert Stovall
 */
public class PointOfSaleUserInterface {

    Scanner input = new Scanner(System.in);
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");

    /**
     * Main loop function for operations to stem from
     */
    public void mainMenu() {

        int userCommand = 0;
        while (userCommand >= 0) {
            System.out.println("\n------------------------------------------------------\n"
                    + "Welcome to the CFDepot Terminal. Here are your options:"
                    + "\n1 = Checkout a Customer"
                    + "\n2 = View Rental Agreements"
                    + "\n3 = Check Prices"
                    + "\n4 = Exit\n");
            System.out.print("What would you like to do?: ");

            try {
                userCommand = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
            }
                switch (userCommand) {
                    case 1:
                        System.out.println("\n~ENTERING CASHIER MODE~\n");
                        checkout();
                        break;
                    case 2:
                        System.out.println("\n~ENTERING RENTAL AGREEMENT HISTORY MODE~\n");
                        viewRentalAgreements();
                        break;
                    case 3:
                        System.out.println("\n~ENTERING PRICE CHECKER MODE~\n");
                        checkPrices();
                        break;
                    case 4:
                        userCommand = -1;
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("\nOops, looks like you're out of range. Please enter a number 1 - 4.");
                        mainMenu();
                        break;
                }
        }
    }

    /**
     *
     */
    public void checkout() {
        String toolCode;
        int rentalDayCount;
        int discountPercent;
        Date checkoutDate;
        String userCommand;
        RentalAgreement rentalAgreement;

        try {
            System.out.print("\nPlease enter the following information:\nTool Code: ");
            toolCode = input.nextLine();

            System.out.print("Number of days to rent (must be a whole number): ");
            rentalDayCount = Integer.parseInt(input.nextLine());
            System.out.print("Discount Percentage(Cannot be above 100%): ");
            discountPercent = Integer.parseInt(input.nextLine());
            System.out.print("Checkout out date (mm/dd/yy): ");
            checkoutDate = dateFormat.parse(input.nextLine());
            System.out.println("Date Given: " + dateFormat.format(checkoutDate));
            System.out.print("Is all the above information correct? Enter \"y\" to confirm, or \"n\" to cancel: ");
            userCommand = input.nextLine();

            //ensure the user doesn't do anything besides enter the desired commands
            while (!userCommand.equals("y") && !userCommand.equals("n")) {
                System.out.print("Oops, invalid entry. Enter \"y\" to confirm the transaction is correct, or \"n\" to cancel.");
                userCommand = input.nextLine();
            }

            if (userCommand.equals("y")) {
                rentalAgreement = RentalCalculator.rentTool(toolCode, rentalDayCount, discountPercent, dateFormat.format(checkoutDate));
                if (rentalAgreement == null) {
                    checkout();
                }
                rentalAgreement.printRentalAgreement();
                uploadRentalAgreement(rentalAgreement);
            } else {
                System.out.println("Transaction Canceled.");
            }

            System.out.print("\nTransaction Completed successfully.\nEnter \"y\" to make another purchase, or \"n\" to go to the main menu: ");
            userCommand = input.nextLine();

            while (!userCommand.equals("y") && !userCommand.equals("n")) {
                System.out.print("Oops, invalid entry. Enter \"y\" to make another purchase, or \"n\" to go to the main menu: ");
                userCommand = input.nextLine();
            }

            if (userCommand.equals("n")) {
                System.out.println("Returning to the main menu..\n");
                mainMenu();
            } else {
                checkout();
            }

        } catch (Exception e) {
            String error = e.getMessage() != null ? "Error: " + e.getMessage() : "";
            System.out.println(error + "\nPlease try again.");
            checkout();
        }

    }

    private void viewRentalAgreements() {
        try {
            ToolsDatabase db = new ToolsDatabase();
            ArrayList<RentalAgreement> rentalAgreements;
            rentalAgreements = db.selectAllRentalAgreements();

            for (int i = 0; i < rentalAgreements.size(); i++) {
                System.out.println((i + 1) + ") " + rentalAgreements.get(i).getCheckoutDate());
            }
            if (rentalAgreements.isEmpty()) {
                System.out.println("There are no Rental Agreements yet.\nPress Enter to return to the Main Menu.");
                input.nextLine();
            } else {
                System.out.println("\nPlease enter the Rental Agreement number you would like to view: ");
                int agreement = Integer.parseInt(input.nextLine());
                if (agreement > rentalAgreements.size() || agreement < 0) {
                    System.out.println("That Rental Agreement is not available. Please try again.\n\n");
                    viewRentalAgreements();
                }
                //subtract 1 to match the index
                RentalAgreement selectedRentalAgreement = rentalAgreements.get(agreement - 1);
                selectedRentalAgreement.printRentalAgreement();
                String userCommand = null;
                System.out.println("\nEnter \"y\" to view another Rental Agreement, or \"n\" to go to the main menu: ");
                userCommand = input.nextLine();
                while (!userCommand.equals("y") && !userCommand.equals("n")) {
                    System.out.print("Oops, invalid entry. Enter \"y\" to make another purchase, or \"n\" to go to the main menu: ");
                    userCommand = input.nextLine();
                }

                if (userCommand.equals("n")) {
                    mainMenu();
                } else {
                    viewRentalAgreements();
                }

            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    //
    private void checkPrices() {
        try {
            ToolsDatabase db = new ToolsDatabase();
            ArrayList<ToolPrice> toolPrices = db.selectAllPrices();

            for (ToolPrice toolPrice : toolPrices) {
                System.out.printf("Type: %s | Daily Cost: %.2f\n", toolPrice.getToolType(), toolPrice.getDailyCharge());
            }
            System.out.println("\nPress Enter to return to the Main Menu.");
            input.nextLine();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    private void uploadRentalAgreement(RentalAgreement rentalAgreement) {
        try {
            ToolsDatabase db = new ToolsDatabase();
            db.insertRentalAgreement(rentalAgreement);
        } catch (Exception e) {
            System.out.println("Error uploading Rental Agreement: " + e);
        }
    }

}
