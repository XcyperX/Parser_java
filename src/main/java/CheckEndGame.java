package main.java;

import main.java.Team.TeamInfo;
import main.java.bot.Bot;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class CheckEndGame {
    private ChromeDriver driver;
    private String mainWindow;
    private Bot botTelegram = new Bot();

    //    Иницавлизация драйвера
    public void setDriver() {
        System.setProperty("webdriver.chrome.driver", "C:/Parser/src/main/java/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("headless");
//        options.addArguments("--window-size=1600,900");
        this.driver = new ChromeDriver(options);
    }

    public void start(String URL) throws SQLException {
        setDriver();
        driver.get(URL);
        this.mainWindow = driver.getWindowHandle();
        uncoverMatch();
    }

    //
    public boolean waitingByClass(String tag) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 5);
            wait.until(ExpectedConditions.elementToBeClickable(By.className(tag)));
            return true;
        } catch (Exception e) {
            System.out.println("Элемент не найден: " + tag);
            e.printStackTrace();
            return false;
        }
    }

    //
    public void waitingByXPATH(String tag) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 5);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(tag)));
        } catch (Exception e) {
            System.out.println("Элемент не найден: " + tag);
            e.printStackTrace();
        }
    }

    //
    public void uncoverMatch() throws SQLException {
        waitingByXPATH("//*[@id='live-table']/div[1]/div[1]/div[6]/div");
        driver.findElementByXPath("//*[@id='live-table']/div[1]/div[1]/div[5]/div").click();
        ArrayList<WebElement> uncoverMatch = (ArrayList<WebElement>)
                driver.findElementsByCssSelector(".event__expander.icon--expander.expand");
        for (WebElement uncover : uncoverMatch) {
            uncover.click();
        }
        driver.executeScript("window.scrollTo(0, 0)");
        collectionOfInformationByMatch();
    }

    //
    public void collectionOfInformationByMatch() throws SQLException {
        ArrayList<WebElement> allMatch = (ArrayList<WebElement>) driver.findElementsByClassName("event__match");
//        allMatch = deleteAvailableMatch(allMatch);
        openMatch(allMatch);
    }

    public void openMatch(ArrayList<WebElement> allMatch) throws SQLException {
        SqlSand sqlSand = new SqlSand();
        TeamInfo[] oneTeamInfo = new TeamInfo[allMatch.size()];
        TeamInfo[] twoTeamInfo = new TeamInfo[allMatch.size()];
        int i = 0;
        for (WebElement match : allMatch) {
//
            match.click();
            Set<String> window = driver.getWindowHandles();
            window.removeAll(Collections.singleton(mainWindow));
            driver.switchTo().window(window.iterator().next());
            waitingByClass("description__country");

            oneTeamInfo[i] = new TeamInfo();
            twoTeamInfo[i] = new TeamInfo();

            String matchData = driver.findElementByClassName("description__time").getText();
            String nameOneTeam = driver.findElementByXPath("//*[@id='flashscore']/div[1]/div[1]/div[2]/div/div/a").getText();
            String nameTwoTeam = driver.findElementByXPath("//*[@id='flashscore']/div[1]/div[3]/div[2]/div/div/a").getText();
            String resultOneTeam = driver.findElementByXPath("//*[@id='summary-content']/div[1]/div[1]/div[2]/span[1]").getText();
            String resultTwoTeam = driver.findElementByXPath("//*[@id='summary-content']/div[1]/div[1]/div[2]/span[2]").getText();

            HashMap<String, String> findMatch = sqlSand.findMatch(nameOneTeam, matchData.split(" ")[0]);
            if (findMatch.isEmpty()) {
                System.out.println("Матч с " + nameOneTeam + " сыгранный " + matchData + " уже есть в базе!");
                driver.close();
                driver.switchTo().window(mainWindow);
                continue;
            }

            if (resultOneTeam.contains("0") && resultTwoTeam.contains("0")) {
                System.out.println("В первом тайме нет голов");
                driver.close();
                driver.switchTo().window(mainWindow);
                continue;
            } else {
                sqlSand.findWinMatch(nameOneTeam, matchData.split(" ")[0]);
            }
        }
    }
}