package learning;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Amazon {

	public static void main(String[] args) throws IOException {

		WebDriver driver = new ChromeDriver();
		driver.navigate().to("https://www.amazon.in/gp/bestsellers/garden/4297301031/ref=zg_bs_nav_garden_2_3638817031");
		driver.manage().window().maximize();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		try {
			driver.findElement(By.xpath("//span[contains(text(),'Delivering to')]")).click();
			System.out.println("Pincode input box clicked successfully in try block");
		}
		catch (Exception e) {
			driver.findElement(By.xpath("//a[@id='nav-global-location-popover-link']//div[@id='glow-ingress-block']")).click();
			System.out.println("Pincode input box clicked successfully in catch block");
		}

		WebElement setPincode = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='GLUXZipInputSection']//input[@id='GLUXZipUpdateInput']")));
		setPincode.click();
		setPincode.clear();
		setPincode.sendKeys("122011");

		driver.findElement(By.xpath("//span[.='Apply']/input")).click();

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("AmazonCategory1_Seeds");

		// Create the header row

		Row headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("Product Rank");
		headerRow.createCell(1).setCellValue("Product URL");
		headerRow.createCell(2).setCellValue("Product Name");
		headerRow.createCell(3).setCellValue("MRP");

		int rowNum = 1; // Start from row 1 (row 0 is for headers)

		List<WebElement> productCount = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@id='gridItemRoot']//span[@class='zg-bdg-text']")));

		List<WebElement> items = driver.findElements(By.xpath("//div[@class='_cDEzb_p13n-sc-css-line-clamp-3_g3dy1']"));

		// Iterate the loop for taking productCount and other extraction data

		for (int i = 0; i < items.size(); i++) {

			Row row = sheet.createRow(rowNum++);
			
			List<WebElement> productRank = driver.findElements(By.xpath("//div[@id='gridItemRoot']//span[@class='zg-bdg-text']"));			
			WebElement rank = productRank.get(i);
			String count = rank.getText();
			row.createCell(0).setCellValue(count);
			System.out.println("Product Rank: " + count);

			WebElement item = items.get(i); 
			item.click();

			String productUrl = driver.getCurrentUrl();
			row.createCell(1).setCellValue(productUrl);
			System.out.println("Product URL :" + productUrl);

			WebElement productName = driver.findElement(By.xpath("//span[@id='productTitle']"));
			String name = productName.getText();
			row.createCell(2).setCellValue(name);
			System.out.println("Product Name :" + name);

			try {
				WebElement mrp = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@id,'corePriceDisplay_desktop_feature_div')]//div[contains(@class, 'a-section a-spacing-small aok-align-center')]")));
				String replaceMRP =  mrp.getText().replace("M.R.P.:","").replace("₹", "").replace(",", "");
				row.createCell(3).setCellValue(replaceMRP);
				System.out.println("MRP : " + replaceMRP);
			}

			catch(Exception e) {
				String nullMRP = "NA";
				row.createCell(3).setCellValue(nullMRP);
				System.out.println(nullMRP);
			}

			driver.navigate().back();

			items = driver.findElements(By.xpath("//div[@class='_cDEzb_p13n-sc-css-line-clamp-3_g3dy1']"));
			productCount = driver.findElements(By.xpath("//div[@class='a-section zg-bdg-body zg-bdg-clr-body aok-float-left']//span[@class='zg-bdg-text']"));
		}

		FileOutputStream fileOut = new FileOutputStream(new File(".\\Output\\AmazonCategory1_output.xlsx"));
		workbook.write(fileOut);
		fileOut.close();

		System.out.println("Excel file created successfully!");
		// Close the main browser window
		driver.quit();
	}

}
