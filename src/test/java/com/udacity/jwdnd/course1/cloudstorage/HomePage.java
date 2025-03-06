package com.udacity.jwdnd.course1.cloudstorage;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class HomePage {

    //Note elements
    @FindBy(id = "nav-notes-tab")
    private WebElement navNotesTab;

    @FindBy(id = "add-new-note-btn")
    private WebElement addNewNoteBtn;

    @FindBy(id = "note-title")
    private WebElement noteTitleInput;

    @FindBy(id = "note-description")
    private WebElement noteDescriptionInput;

    @FindBy(id = "trigger-note-submit")
    private WebElement noteSubmitBtn;

    //Credentials elements
    @FindBy(id = "nav-credentials-tab")
    private WebElement navCredentialsTab;

    @FindBy(id = "add-new-credential-btn")
    private WebElement addNewCredentialBtn;

    @FindBy(id = "credential-url")
    private WebElement credentialUrl;

    @FindBy(id = "credential-username")
    private WebElement credentialUsername;

    @FindBy(id = "credential-password")
    private WebElement credentialPassword;

    @FindBy(id = "trigger-credential-submit")
    private WebElement credentialSubmitBtn;

    private WebDriver driver;

    private WebDriverWait webDriverWait;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
    }

    public Integer createNote(String title, String description){
        navNotesTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-new-note-btn")));
        addNewNoteBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        noteTitleInput.sendKeys(title);
        noteDescriptionInput.sendKeys(description);
        noteSubmitBtn.click();

        List<WebElement> noteTitles = getNoteTitles();
        String id = noteTitles.get(noteTitles.size()-1).getAttribute("id");
        return Integer.parseInt(id.substring(id.indexOf("-") + 1, id.lastIndexOf("-")));
    }

    public List<WebElement> getNoteTitles(){
        String noteTitlesXpath = "//th[starts-with(@id, 'note-') and contains(@id, '-title')]";
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(noteTitlesXpath)));
        return driver.findElements(By.xpath(noteTitlesXpath));
    }

    public void editNote(Integer noteId, String newTitle, String newDescription){
        navNotesTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-new-note-btn")));
        WebElement editNoteBtn = driver.findElement(By.id("edit-note-" + noteId + "-btn"));
        editNoteBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        noteTitleInput.clear();
        noteTitleInput.sendKeys(newTitle);
        noteDescriptionInput.clear();
        noteDescriptionInput.sendKeys(newDescription);
        noteSubmitBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit-note-" + noteId + "-btn")));
    }

    public void deleteNote(Integer noteId){
        navNotesTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-new-note-btn")));
        WebElement deleteNoteBtn = driver.findElement(By.id("delete-note-" + noteId + "-btn"));
        deleteNoteBtn.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-tabContent")));
    }

    public Integer createCredential(String url, String username, String password){
        navCredentialsTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials")));
        addNewCredentialBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialModal")));
        credentialUrl.sendKeys(url);
        credentialUsername.sendKeys(username);
        credentialPassword.sendKeys(password);
        credentialSubmitBtn.click();

        List<WebElement> credentialUrls = getCredentialUrls();
        String id = credentialUrls.get(credentialUrls.size()-1).getAttribute("id");
        return Integer.parseInt(id.substring(id.indexOf("-") + 1, id.lastIndexOf("-")));
    }

    public List<WebElement> getCredentialUrls(){
        String credentialUrlsXpath = "//th[starts-with(@id, 'credential-') and contains(@id, '-url')]";
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(credentialUrlsXpath)));
        return driver.findElements(By.xpath(credentialUrlsXpath));
    }

    public Integer editCredential(Integer credentialId, String url, String username, String password){
        navCredentialsTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials")));
        WebElement editCredentialBtn = driver.findElement(By.id("edit-credential-" + credentialId + "-btn"));
        editCredentialBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialModal")));
        credentialUrl.clear();
        credentialUrl.sendKeys(url);
        credentialUsername.clear();
        credentialUsername.sendKeys(username);
        credentialPassword.clear();
        credentialPassword.sendKeys(password);
        credentialSubmitBtn.click();

        List<WebElement> credentialUrls = getCredentialUrls();
        String id = credentialUrls.get(credentialUrls.size()-1).getAttribute("id");
        return Integer.parseInt(id.substring(id.indexOf("-") + 1, id.lastIndexOf("-")));
    }

    public void deleteCredential(Integer credentialId){
        navCredentialsTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials")));
        WebElement deleteCredentialBtn = driver.findElement(By.id("delete-credential-" + credentialId + "-btn"));
        deleteCredentialBtn.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials")));
    }
}
