package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DemoblazeCartTest {
    WebDriver driver;
    WebDriverWait wait;

    @Before
    public void setUp(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }


    @Given("The user is in {string}")
    public void the_user_is_in(String url) {
        driver.get(url);
    }
    @Given("the user is in the login page")
    public void the_user_is_in_the_login_page() {
        driver.findElement(By.id("login2")).click();

        WebElement page = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".modal-open")));
        Assert.assertNotNull(page);
    }

    @When("they enter {string} and {string} as credentials")
    public void they_enter_and_as_credentials(String username, String password) {
//        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); // maybe fluent wait
        WebElement userName = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("loginusername"))));
        WebElement passWord = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("loginpassword"))));


        userName.sendKeys(username);
        passWord.sendKeys(password);
        driver.findElement(By.xpath("//button[@onclick = 'logIn()']")).click();
        WebElement presentElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nameofuser")));
        Assert.assertTrue(presentElement.isDisplayed());
    }
    @When("they select and add a {string} to cart")
    public void they_select_and_add_a_to_cart(String string) {
        WebElement productElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(@href," +
                "'prod.html?idp_=1') and @class='hrefch']")));
        productElement.click();
        WebElement cartButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@onclick = " +
                "'addToCart(1)']")));
        cartButton.click();
        wait.until(ExpectedConditions.alertIsPresent());
        String alertText = driver.switchTo().alert().getText();
        Assert.assertEquals(alertText, "Product added.");
        driver.switchTo().alert().accept();

    }
    @When("proceed to checkout and fill in random data in fields")
    public void proceed_to_checkout_and_fill_in_random_data_in_fields() {
        WebElement cartPage = wait.until(ExpectedConditions.elementToBeClickable(By.id("cartur")));
        cartPage.click();
        System.out.println(driver.getCurrentUrl());
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.demoblaze.com/cart.html");

        driver.findElement(By.xpath("//button[@data-target='#orderModal']")).click();

        driver.findElement(By.id("name")).sendKeys("leehle");
        driver.findElement(By.id("country")).sendKeys("SA");
        driver.findElement(By.id("city")).sendKeys("jhb");
        driver.findElement(By.id("card")).sendKeys("mastercard");
        driver.findElement(By.id("month")).sendKeys("JAN");
        driver.findElement(By.id("year")).sendKeys("2025");

        driver.findElement(By.xpath("//button[@onclick='purchaseOrder()']")).click();

        boolean confirm = driver.findElement(By.cssSelector(".sweet-alert.showSweetAlert.visible")).isDisplayed();
        Assert.assertTrue(confirm);
    }
    @Then("return the purchase ID")
    public void return_the_purchase_id() {
        WebElement purchaseDiv = driver.findElement(By.cssSelector(".lead.text-muted "));
        String idText = purchaseDiv.getText();
        System.out.println(idText);

        Pattern pattern = Pattern.compile("Id: (\\d+)");
        Matcher match = pattern.matcher(idText);

//        System.out.println(match.);

        if (match.find()) {
            System.out.println(match.group(0)); // print the extracted ID
        } else {
            throw new RuntimeException("ID not found in the element text.");
        }

        Assert.assertNotNull(match);
    }

    @After
    public void tearDown(){
        driver.quit();
    }
}
