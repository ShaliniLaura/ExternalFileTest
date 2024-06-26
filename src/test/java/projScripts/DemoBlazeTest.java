package projScripts;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Properties;

import org.junit.Assert;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class DemoBlazeTest {
	WebDriver driver;
	Properties prop;
  
	@BeforeTest
	public void initSetup() throws IOException, InterruptedException
	{
		String path= System.getProperty("user.dir")+"//src//test//resources//testFiles//testData.properties";
		prop= new Properties();
		FileInputStream fin=new FileInputStream(path);
		prop.load(fin);
		fin.close();
		
		String strBrowser= prop.getProperty("browser");
		if(strBrowser.equalsIgnoreCase("chrome"))
		{
			driver= new ChromeDriver();
		}
		else if (strBrowser.equalsIgnoreCase("edge"))
		{
			driver= new EdgeDriver();
		}
		driver.manage().window().maximize();
		driver.get(prop.getProperty("url"));
		driver.findElement(By.cssSelector("#login2")).click();
		  
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		driver.findElement(By.cssSelector("#loginusername")).sendKeys(prop.getProperty("username"));
		driver.findElement(By.cssSelector("#loginpassword")).sendKeys(prop.getProperty("password"));
		driver.findElement(By.xpath("//button[text()='Log in']")).click();
		
	}
	
	
  @Test(dataProvider="Products")
  public void validLogin(String prdtName) throws InterruptedException {
	
	  //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	  Thread.sleep(3000);
	  driver.findElement(By.xpath("//a[text()='" + prdtName +"']")).click();
	  Thread.sleep(1000);
	  //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	  driver.findElement(By.xpath("//a[text()='Add to cart']")).click();
	  //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	  Thread.sleep(1000);
	  Alert alt= driver.switchTo().alert();
	  alt.accept();
	  
	  driver.findElement(By.xpath("//a[text()='Home ']")).click();

	  
  }
  
  @Test
  public void validateCart()
  {
	  driver.findElement(By.xpath("//a[text()='Cart']")).click();
	  driver.findElement(By.xpath("//button[text()='Place Order']")).click();
  }
  
  @Test(priority=2, dataProvider="placeOrder", dataProviderClass=PlaceOrderTest.class)
  public void DeliveryDetails(String strName,String strCountry,String strCity,String strCreditCard, String strMonth,String strYear) throws InterruptedException
  {
	  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	  driver.findElement(By.cssSelector("#name")).sendKeys(strName);
	  driver.findElement(By.cssSelector("#country")).sendKeys(strCountry);
	  driver.findElement(By.cssSelector("#city")).sendKeys(strCity);
	  driver.findElement(By.cssSelector("#card")).sendKeys(strCreditCard);
	  driver.findElement(By.cssSelector("#month")).sendKeys(strMonth);
	  driver.findElement(By.cssSelector("#year")).sendKeys(strYear);
	  driver.findElement(By.xpath("//button[text()='Purchase']")).click();
	  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	  Assert.assertEquals(driver.findElement(By.xpath("//h2[text()='Thank you for your purchase!']")).getText(), "Thank you for your purchase!");
  }
  
 
  
  
  @DataProvider(name="Products")
  public Object[][] getData()
  {
	  String path= System.getProperty("user.dir")+"//src//test//resources//testFiles//testData.csv";
	  CSVReader reader=null;
	  try
	  {
		  reader= new CSVReader( new FileReader(path));
	  }
	  catch(FileNotFoundException e)
	  {
		  e.printStackTrace();
		  
	  }
	  String cols[];
	  ArrayList<Object> dataList = new ArrayList<Object>();
	  try {
		  while((cols=reader.readNext())!=null)
		  {
			  Object record[]= {cols[0]};
			  dataList.add(record);
		  }
		  reader.close();
	  }
	  catch(CsvValidationException e)
	  {
		e.printStackTrace();  
	  }
	  catch(IOException e)
	  {
		  e.printStackTrace();
	  }
	  return dataList.toArray(new Object[dataList.size()][]);
  }
  
  @AfterTest
  public void tearDown()
  {
	  driver.close();
  }
}
