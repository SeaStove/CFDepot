
import java.security.InvalidParameterException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import saleapp.RentalAgreement;
import saleapp.RentalCalculator;
import org.apache.commons.lang3.builder.EqualsBuilder;
import saleapp.Database.ToolsDatabase;

/**
 *
 * @author Robert Stovall
 */
public class TestCFDepot {

    public TestCFDepot() {
    }

    @BeforeClass
    public static void setUpClass() {
        ToolsDatabase db = new ToolsDatabase();
        db.createDatabase();
        db.initialUpload();
    }
    
    @AfterClass
    public static void tearDownClass() {
        ToolsDatabase db = new ToolsDatabase();
        db.deleteDatabase();
    }

    @Test(expected = InvalidParameterException.class)
    public void pointOfSale_ThrowsInvalidParameterException_IfDiscountGreaterThanOneHundredPercent() {
        String toolCode = "JAKR";
        String checkoutDate = "9/3/15";
        int rentalDays = 5;
        int discount = 101;

        RentalCalculator.rentTool(toolCode, rentalDays, discount, checkoutDate);
    }

    @Test
    public void pointOfSale_MatchesExpectedValues_Ladder() {
        String toolCode = "LADW";
        String checkoutDate = "7/2/20";
        int rentalDays = 3;
        int discount = 10;

        RentalAgreement actual = RentalCalculator.rentTool(toolCode, rentalDays, discount, checkoutDate);
        RentalAgreement expected = new RentalAgreement(toolCode, "Ladder", "Werner", rentalDays, checkoutDate, "7/5/20", 1.99, 2, 3.98, 10, .40, 3.58);
        Assert.assertTrue(EqualsBuilder.reflectionEquals(expected,actual));
    }

    @Test
    public void pointOfSale_MatchesExpectedValues_Chainsaw() {
        String toolCode = "CHNS";
        String checkoutDate = "7/2/15";
        int rentalDays = 5;
        int discount = 25;

        RentalAgreement actual = RentalCalculator.rentTool(toolCode, rentalDays, discount, checkoutDate);
        RentalAgreement expected = new RentalAgreement(toolCode, "Chainsaw", "Stihl", rentalDays, checkoutDate, "7/7/15", 1.49, 3, 4.47, 25, 1.12, 3.35);
        Assert.assertTrue(EqualsBuilder.reflectionEquals(expected,actual));
    }

    @Test
    public void pointOfSale_MatchesExpectedValues_LaborDay() {
        String toolCode = "JAKD";
        String checkoutDate = "9/3/15";
        int rentalDays = 6;
        int discount = 0;

        RentalAgreement actual = RentalCalculator.rentTool(toolCode, rentalDays, discount, checkoutDate);
        RentalAgreement expected = new RentalAgreement(toolCode, "Jackhammer", "DeWalt", rentalDays, checkoutDate, "9/9/15", 2.99, 3, 8.97, 0, 0.00, 8.97);
        Assert.assertTrue(EqualsBuilder.reflectionEquals(expected,actual));
    }

    @Test
    public void pointOfSale_MatchesExpectedValues_HappyPath4() {
        String toolCode = "JAKR";
        String checkoutDate = "7/2/15";
        int rentalDays = 9;
        int discount = 0;

        RentalAgreement actual = RentalCalculator.rentTool(toolCode, rentalDays, discount, checkoutDate);
        RentalAgreement expected = new RentalAgreement(toolCode, "Jackhammer", "Ridgid", rentalDays, checkoutDate, "7/11/15", 2.99, 5, 14.95, 0, 0.00, 14.95);
        Assert.assertTrue(EqualsBuilder.reflectionEquals(expected,actual));
    }

    @Test
    public void pointOfSale_MatchesExpectedValues_HappyPath5() {
        String toolCode = "JAKR";
        String checkoutDate = "7/2/20";
        int rentalDays = 4;
        int discount = 50;

        RentalAgreement actual = RentalCalculator.rentTool(toolCode, rentalDays, discount, checkoutDate);
        RentalAgreement expected = new RentalAgreement(toolCode, "Jackhammer", "Ridgid", rentalDays, checkoutDate, "7/6/20", 2.99, 1, 2.99, 50, 1.50, 1.49);
        Assert.assertTrue(EqualsBuilder.reflectionEquals(expected,actual));
    }
}
