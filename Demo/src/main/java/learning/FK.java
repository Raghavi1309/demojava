package learning;

import java.sql.Driver;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FK {

	public static void main(String[] args) {
		
		WebDriver driver = new ChromeDriver();
//		driver.navigate().to("https://www.flipkart.com/loopytots-reversible-bunny-rabbit-24-cm/p/itm2d481f3512dca?");
		driver.navigate().to("https://www.amazon.in/Mirana-Colorful-Multiple-Rechargeable-Various/dp/B0D6BVMM8C/");
		driver.manage().window().maximize();
		
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));


//		WebElement offerElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='UkUFwK WW8yVX']")));
		
		WebElement offerElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@class='a-size-large a-color-price savingPriceOverride aok-align-center reinventPriceSavingsPercentageMargin savingsPercentage']")));
		String offer = offerElement.getText();
//		System.out.println(offer);
		
		String replaceOffer = offer.replaceAll("-15%", "15% Off");
		System.out.println(replaceOffer);
		
		driver.quit();
	}

}
