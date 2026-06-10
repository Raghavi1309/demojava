package learning;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Flipkart {

	public static void main(String[] args) throws InterruptedException, IOException {


		WebDriver driver = new ChromeDriver();
		driver.navigate().to("https://www.flipkart.com");
		driver.manage().window().maximize();
		
		WebElement searchButton = driver.findElement(By.xpath("//input[@title='Search for Products, Brands and More']"));
		searchButton.sendKeys("Smart Phones");
		searchButton.sendKeys(Keys.ENTER);

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Selenium Data");

		// Create the header row
		Row headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("Product URL");
		headerRow.createCell(1).setCellValue("Product Name");
		headerRow.createCell(2).setCellValue("MRP");
		headerRow.createCell(3).setCellValue("SP");		
		headerRow.createCell(4).setCellValue("Offer");	

		int rowNum = 1; // Start from row 1 (row 0 is for headers)

		List<WebElement> items = driver.findElements(By.xpath("//div[@class='KzDlHZ']"));

		// Iterate the products 
		for (int i = 0; i < Math.min(3, items.size()); i++) {
			WebElement item = items.get(i); 
			Row row = sheet.createRow(rowNum++);

			// Click the product to go to the product page
			item.click();

			// Switch to the new tab
			Set<String> windowHandles = driver.getWindowHandles();
			String originalTab = driver.getWindowHandle(); // Store the handle of the original tab

			// Switch to the new tab 
			for (String handle : windowHandles) {
				if (!handle.equals(originalTab)) {
					driver.switchTo().window(handle); // Switch to the new tab
					break;
				}
			}

			String productUrl = driver.getCurrentUrl();
			row.createCell(0).setCellValue(productUrl);
			System.out.println("Product URL :" + productUrl);

			// Getting Product Name
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 

			WebElement titleElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@class='VU-ZEz']")));
			String productTitle = titleElement.getText();
			row.createCell(1).setCellValue(productTitle);
			System.out.println("Product Title: " + productTitle);	

			// Getting MRP

			try {
				WebDriverWait waitMRP = new WebDriverWait(driver, Duration.ofSeconds(10));
				WebElement productMRP = waitMRP.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='yRaY8j A6+E6v']")));
				String mrp = productMRP.getText();
				row.createCell(2).setCellValue(mrp);
				System.out.println("MRP: " + mrp);
			}

			catch (Exception e) {
				WebDriverWait waitMRP = new WebDriverWait(driver, Duration.ofSeconds(10));
				WebElement productMRP = waitMRP.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='yRaY8j A6+E6v yKS4la']")));
				String mrp = productMRP.getText();
				row.createCell(2).setCellValue(mrp);
				System.out.println("MRP: " + mrp);
			}

			// Getting SP

			try {
				WebDriverWait waitSP = new WebDriverWait(driver, Duration.ofSeconds(10)); 
				WebElement productSP = waitSP.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='Nx9bqj CxhGGd']")));
				String sp = productSP.getText();
				row.createCell(3).setCellValue(sp);
				System.out.println("SP: " + sp);
			}
			catch (Exception e) {
				WebDriverWait waitSP = new WebDriverWait(driver, Duration.ofSeconds(10)); 
				WebElement productSP = waitSP.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='Nx9bqj CxhGGd yKS4la']")));
				String sp = productSP.getText();
				row.createCell(3).setCellValue(sp);
				System.out.println("SP: " + sp);
			}

			// Getting Offer

			try {
				WebDriverWait waitOffer = new WebDriverWait(driver, Duration.ofSeconds(10)); 
				WebElement offer = waitOffer.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='UkUFwK WW8yVX']")));
				String productOffer = offer.getText();
				productOffer.replace('o', 'O');
				row.createCell(4).setCellValue(productOffer);
				System.out.println("Offer: " + productOffer);
			}
			catch (Exception e) {
				WebDriverWait waitOffer = new WebDriverWait(driver, Duration.ofSeconds(10)); 
				WebElement offer = waitOffer.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='UkUFwK WW8yVX yKS4la']")));
				String productOffer = offer.getText();
				productOffer.replace('o', 'O');
				row.createCell(4).setCellValue(productOffer);
				System.out.println("Offer: " + productOffer);
			}

			// Close the new tab
			driver.close();

			// Switch back to the original tab
			driver.switchTo().window(originalTab);

			// Optional: Wait a bit to simulate human interaction
			Thread.sleep(2000);
		}

		// ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 1000);");
		// Thread.sleep(2000);

		// Write the Excel file to disk
		FileOutputStream fileOut = new FileOutputStream(new File(".\\Output\\FK_output.xlsx"));
		workbook.write(fileOut);
		fileOut.close();

		System.out.println("Excel file created successfully!");

		// Close the main browser window

		driver.quit();
		
		}
}