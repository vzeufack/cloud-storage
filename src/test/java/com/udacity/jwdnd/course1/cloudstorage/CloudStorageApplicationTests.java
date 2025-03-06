package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.File;
import java.time.Duration;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private SignupPage signupPage;
	private LoginPage loginPage;

	private HomePage homePage;

	private WebDriver driver;

	private static EncryptionService encryptionService;

	@Autowired
	private CredentialService credentialService;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
		encryptionService = new EncryptionService();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test public void testUnauthorizedAccessToHomePage() {
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void getSignupPage() {
		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	public void testSignupLoginLogout(){
		driver.get("http://localhost:" + port + "/signup");
		signupPage = new SignupPage(driver);
		signupPage.signup("John", "Doe", "jdoe", "test");
		loginPage = new LoginPage(driver);
		loginPage.login("jdoe", "test");
		Assertions.assertEquals("Home", driver.getTitle());
		driver.get("http://localhost:" + port + "/logout");
		driver.get("http://localhost:" + port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the signup was successful.
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depending on the rest of your code.
		*/
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}

	
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling redirecting users 
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric: 
	 * https://review.udacity.com/#!/rubrics/2724/view 
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");
		
		// Check if we have been redirected to the login page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs 
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at: 
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");
		
		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code. 
	 * 
	 * Read more about file size limits here: 
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}

	@Test
	public void testCreateNote(){
		signupAndLogin("note_creator","note_creator","note_creator","note_creator");
		homePage = new HomePage(driver);
		int noteId = homePage.createNote("abc", "abc");
		WebElement noteTitle = driver.findElement(By.id("note-" + noteId + "-title"));
		WebElement noteDesc = driver.findElement(By.id("note-" + noteId + "-desc"));
		Assertions.assertEquals("abc", noteTitle.getText());
		Assertions.assertEquals("abc", noteDesc.getText());
	}

	@Test
	public void testEditNote(){
		signupAndLogin("note_editor","note_editor","note_editor","note_editor");
		homePage = new HomePage(driver);
		int noteId = homePage.createNote("note", "desc");
		homePage.editNote(noteId, "edited note", "edited desc");
		WebElement noteTitle = driver.findElement(By.id("note-" + noteId + "-title"));
		WebElement noteDesc = driver.findElement(By.id("note-" + noteId + "-desc"));
		Assertions.assertEquals("edited note", noteTitle.getText());
		Assertions.assertEquals("edited desc", noteDesc.getText());
	}

	@Test
	public void testDeleteNote(){
		signupAndLogin("note_del","note_del","note_del","note_del");
		homePage = new HomePage(driver);
		int noteId = homePage.createNote("note", "desc");
		WebElement noteTitle = driver.findElement(By.id("note-" + noteId + "-title"));
		WebElement noteDesc = driver.findElement(By.id("note-" + noteId + "-desc"));
		Assertions.assertEquals("note", noteTitle.getText());
		Assertions.assertEquals("desc", noteDesc.getText());

		homePage.deleteNote(noteId);

		boolean isTitlePresent = driver.findElements(By.id("note-" + noteId + "-title")).size() > 0;
		boolean isDescPresent = driver.findElements(By.id("note-" + noteId + "-desc")).size() > 0;

		Assertions.assertFalse(isTitlePresent);
		Assertions.assertFalse(isDescPresent);
	}

	private void signupAndLogin(String firstName, String lastName, String userName, String password){
		try {
			doMockSignUp(firstName, lastName, userName, password);
			doLogIn(userName, password);
		}
		catch(Exception e) {
			doLogIn(userName, password);
		}
	}

	@Test
	public void testCreateCredential(){
		signupAndLogin("credential_creator","credential_creator","credential_creator","credential_creator");
		homePage = new HomePage(driver);
		int credentialId = homePage.createCredential("facebook.com", "face", "book");
		WebElement credentialUrl = driver.findElement(By.id("credential-" + credentialId + "-url"));
		WebElement credentialUsername = driver.findElement(By.id("credential-" + credentialId + "-username"));
		WebElement credentialPassword = driver.findElement(By.id("credential-" + credentialId + "-password"));
		Assertions.assertEquals("facebook.com", credentialUrl.getText());
		Assertions.assertEquals("face", credentialUsername.getText());

		Credential credential = credentialService.getCredential(credentialId);
		Assertions.assertNotEquals("book", credentialPassword.getText());
		Assertions.assertEquals("book", encryptionService.decryptValue(credentialPassword.getText(), credential.getKey()));
	}

	@Test
	public void testEditCredential(){
		signupAndLogin("credential_editor","credential_editor","credential_editor","credential_editor");
		homePage = new HomePage(driver);
		int credentialId = homePage.createCredential("facebook.com", "face", "book");
		homePage.editCredential(credentialId, "google.com", "test_user", "test_password");
		WebElement credentialUrl = driver.findElement(By.id("credential-" + credentialId + "-url"));
		WebElement credentialUsername = driver.findElement(By.id("credential-" + credentialId + "-username"));
		WebElement credentialPassword = driver.findElement(By.id("credential-" + credentialId + "-password"));
		Assertions.assertEquals("google.com", credentialUrl.getText());
		Assertions.assertEquals("test_user", credentialUsername.getText());

		Credential credential = credentialService.getCredential(credentialId);
		Assertions.assertNotEquals("test_password", credentialPassword.getText());
		Assertions.assertEquals("test_password", encryptionService.decryptValue(credentialPassword.getText(), credential.getKey()));
	}

	@Test
	public void testDeleteCredential(){
		signupAndLogin("credential_del","credential_del","credential_del","credential_del");
		homePage = new HomePage(driver);
		int credentialId = homePage.createCredential("facebook.com", "face", "book");

		homePage.deleteCredential(credentialId);

		boolean isUrlPresent = driver.findElements(By.id("credential-" + credentialId + "-url")).size() > 0;
		boolean isUsernamePresent = driver.findElements(By.id("credential-" + credentialId + "-username")).size() > 0;
		boolean isPasswordPresent = driver.findElements(By.id("credential-" + credentialId + "-password")).size() > 0;

		Assertions.assertFalse(isUrlPresent);
		Assertions.assertFalse(isUsernamePresent);
		Assertions.assertFalse(isPasswordPresent);
	}
}
