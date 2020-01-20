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

public class FirstTest {
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
        driver.findElementByXPath("//*[@id='live-table']/div[1]/div[1]/div[6]/div").click();
        ArrayList<WebElement> uncoverMatch = (ArrayList<WebElement>)
                driver.findElementsByCssSelector(".event__expander.icon--expander.expand");
        for (WebElement uncover : uncoverMatch) {
            try {
                uncover.click();
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                System.out.println("Не могу развернуть список!");
                uncover.click();
            }

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

//    public ArrayList<WebElement> deleteAvailableMatch(ArrayList<WebElement> arrayList) throws SQLException {
//        ArrayList<WebElement> deletedMatch = new ArrayList<WebElement>();
//        SqlSand sqlSand = new SqlSand();
////        ArrayList<String> name = sqlSand.selectName();
//        HashMap<String, String> nameAndDate = sqlSand.selectName();
//        System.out.println(nameAndDate.get(0));
//        for (WebElement match : arrayList) {
//            List<String> data = Arrays.asList(match.getText().split("\n"));
//            for (String nameTeam : name) {
//                if (nameTeam.toLowerCase().equals(data.get(1).toLowerCase())) {
//                    System.out.println("Матч c " + nameTeam + " уже есть в базе!");
//                    deletedMatch.add(match);
//                }
//            }
//        }
//        arrayList.removeAll(deletedMatch);
//        return arrayList;
//    }

    //
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

            String country = driver.findElementByClassName("description__country").getText();
            String matchData = driver.findElementByClassName("description__time").getText();
            String nameOneTeam = driver.findElementByXPath("//*[@id='flashscore']/div[1]/div[1]/div[2]/div/div/a").getText();
            String nameTwoTeam = driver.findElementByXPath("//*[@id='flashscore']/div[1]/div[3]/div[2]/div/div/a").getText();

            HashMap<String, String> findMatch = sqlSand.findMatch(nameOneTeam, matchData.split(" ")[0]);
            if (!findMatch.isEmpty()) {
                System.out.println("Матч с " + nameOneTeam + " сыгранный " + matchData + " уже есть в базе!");
                driver.close();
                driver.switchTo().window(mainWindow);
                continue;
            }

            oneTeamInfo[i].setName(nameOneTeam);
            oneTeamInfo[i].setData(matchData.split(" ")[0]);
            oneTeamInfo[i].setTime(matchData.split(" ")[1]);
            oneTeamInfo[i].setCountry(country);
            twoTeamInfo[i].setName(nameTwoTeam);
            twoTeamInfo[i].setData(matchData.split(" ")[0]);
            twoTeamInfo[i].setTime(matchData.split(" ")[1]);
            twoTeamInfo[i].setCountry(country);

//
            if (driver.findElementByClassName("li4").getText().equals("Таблица")) {
                addTableData(oneTeamInfo[i]);
                addTableData(twoTeamInfo[i]);
                resultMotivation(oneTeamInfo[i]);
                resultMotivation(twoTeamInfo[i]);
            } else {
                System.out.println("Нет таблицы");
            }
            driver.findElementByClassName("li2").click();

            waitingByClass("head_to_head");

//
            ArrayList<WebElement> numberGameOneTeam = (ArrayList<WebElement>) driver.findElementsByXPath(
                    "//*[@id='tab-h2h-overall']/div[1]/table/tbody/tr");
            ArrayList<WebElement> numberGameTwoTeam = (ArrayList<WebElement>) driver.findElementsByXPath(
                    "//*[@id='tab-h2h-overall']/div[2]/table/tbody/tr");

//
            if (numberGameOneTeam.get(0).getText().toLowerCase().contains(oneTeamInfo[i].getName().toLowerCase()) &&
                    numberGameTwoTeam.get(0).getText().toLowerCase().contains(twoTeamInfo[i].getName().toLowerCase())) {
                oneTeamInfo[i] = analysisData(oneTeamInfo[i], window.iterator().next(), numberGameOneTeam, false);
                twoTeamInfo[i] = analysisData(twoTeamInfo[i], window.iterator().next(), numberGameTwoTeam, false);
                resultOfTheFirstFormula(oneTeamInfo[i], twoTeamInfo[i]);
                resultOfTheFirstFormula(twoTeamInfo[i], oneTeamInfo[i]);
            } else {
                System.out.println("У матча: " + oneTeamInfo[i].getName() + " : " + twoTeamInfo[i].getName() + " нет данных!");
                driver.close();
                driver.switchTo().window(mainWindow);
                continue;
            }

            ArrayList<WebElement> numberGameFullTimeTeam = (ArrayList<WebElement>) driver.findElementsByXPath(
                    "//*[@id='tab-h2h-overall']/div[3]/table/tbody/tr");

//
            if (numberGameFullTimeTeam.get(0).getText().toLowerCase().contains(oneTeamInfo[i].getName().toLowerCase()) &&
                    numberGameFullTimeTeam.get(0).getText().toLowerCase().contains(twoTeamInfo[i].getName().toLowerCase())) {
                oneTeamInfo[i] = analysisData(oneTeamInfo[i], window.iterator().next(), numberGameFullTimeTeam, true);
                twoTeamInfo[i] = analysisData(twoTeamInfo[i], window.iterator().next(), numberGameFullTimeTeam, true);
                resultOfTheFirstFormulaFullTime(oneTeamInfo[i], twoTeamInfo[i]);
                resultOfTheFirstFormulaFullTime(twoTeamInfo[i], oneTeamInfo[i]);
            } else {
                System.out.println("У матча: " + oneTeamInfo[i].getName() + " : " + twoTeamInfo[i].getName() + " нет очных игр!");
                driver.close();
                driver.switchTo().window(mainWindow);
                continue;
            }

            System.out.println(oneTeamInfo[i].toString());
            System.out.println(twoTeamInfo[i].toString());

//            SqlSand sqlSand = new SqlSand();
            sqlSand.insertIntoBD("oneteam", oneTeamInfo[i].getName(), oneTeamInfo[i].getCountry(), oneTeamInfo[i].getData(),
                    oneTeamInfo[i].getTime(), oneTeamInfo[i].getTouchdownHome(), oneTeamInfo[i].getTouchdownAway(),
                    oneTeamInfo[i].getPassHome(), oneTeamInfo[i].getPassAway(), oneTeamInfo[i].getFullTimeTouchdownHome(),
                    oneTeamInfo[i].getFullTimeTouchdownAway(), oneTeamInfo[i].getFullTimePassHome(),
                    oneTeamInfo[i].getFullTimePassAway(), oneTeamInfo[i].getCountGame(), oneTeamInfo[i].getVictoryGame(),
                    oneTeamInfo[i].getLoseGame(), oneTeamInfo[i].getResultsFirstFormula(),
                    oneTeamInfo[i].getResultsFullTimeFirstFormula(), oneTeamInfo[i].getMotivationPercentage());

            sqlSand.insertIntoBD("twoteam", twoTeamInfo[i].getName(), twoTeamInfo[i].getCountry(), twoTeamInfo[i].getData(),
                    twoTeamInfo[i].getTime(), twoTeamInfo[i].getTouchdownHome(), twoTeamInfo[i].getTouchdownAway(),
                    twoTeamInfo[i].getPassHome(), twoTeamInfo[i].getPassAway(), twoTeamInfo[i].getFullTimeTouchdownHome(),
                    twoTeamInfo[i].getFullTimeTouchdownAway(), twoTeamInfo[i].getFullTimePassHome(),
                    twoTeamInfo[i].getFullTimePassAway(), twoTeamInfo[i].getCountGame(), twoTeamInfo[i].getVictoryGame(),
                    twoTeamInfo[i].getLoseGame(), twoTeamInfo[i].getResultsFirstFormula(),
                    twoTeamInfo[i].getResultsFullTimeFirstFormula(), twoTeamInfo[i].getMotivationPercentage());

            sendResultTelegram(oneTeamInfo[i], twoTeamInfo[i]);

            driver.close();
            driver.switchTo().window(mainWindow);
        }
    }

    //
    public TeamInfo addTableData(TeamInfo teamData) {
        driver.findElementByClassName("li4").click();
        waitingByClass("table__row");
        ArrayList<WebElement> tableData = (ArrayList<WebElement>) driver.findElementsByClassName("table__row");
        for (WebElement matchData : tableData) {
            if (matchData.getText().toLowerCase().contains(teamData.getName().toLowerCase())) {
                List<String> data = Arrays.asList(matchData.getText().split("\n"));
                if (data.get(2).equals("▼") || data.get(2).equals("▲")){
                    teamData.setCountGame(Integer.parseInt(data.get(3)));
                    teamData.setVictoryGame(Integer.parseInt(data.get(4)));
                    teamData.setLoseGame(Integer.parseInt(data.get(6)));
                } else {
                    teamData.setCountGame(Integer.parseInt(data.get(2)));
                    teamData.setVictoryGame(Integer.parseInt(data.get(3)));
                    teamData.setLoseGame(Integer.parseInt(data.get(5)));
                }
                return teamData;
            }
        }
        tableData.clear();
        return null;
    }

    public TeamInfo analysisData(TeamInfo teamData, String window, ArrayList<WebElement> numberGameOneTeam, boolean fullTime) {
        if (numberGameOneTeam.size() < 5) {
            for (WebElement gameOneTeam : numberGameOneTeam) {
                gameOneTeam.click();
                Set<String> windowAnalysis = driver.getWindowHandles();
                windowAnalysis.removeAll(Collections.singleton(window));
                windowAnalysis.removeAll(Collections.singleton(mainWindow));
                driver.switchTo().window(windowAnalysis.iterator().next());
                waitingByXPATH("//*[@id='summary-content']/div[1]/div[1]/div[2]/span[1]");
                if (driver.findElementByXPath("//*[@id='flashscore']/div[1]/div[1]/div[2]/div/div/a")
                        .getText().toLowerCase()
                        .contains(teamData.getName().toLowerCase())) {
                    if (fullTime) {
                        teamData.sumFullTimeTouchdownHome(Integer.parseInt(driver.findElementByXPath(
                                "//*[@id='summary-content']/div[1]/div[1]/div[2]/span[1]").getText()));
                        teamData.sumFullTimePassHome(Integer.parseInt(driver.findElementByXPath(
                                "//*[@id='summary-content']/div[1]/div[1]/div[2]/span[2]").getText()));
                    } else {
                        teamData.sumTouchdownHome(Integer.parseInt(driver.findElementByXPath(
                                "//*[@id='summary-content']/div[1]/div[1]/div[2]/span[1]").getText()));
                        teamData.sumPassHome(Integer.parseInt(driver.findElementByXPath(
                                "//*[@id='summary-content']/div[1]/div[1]/div[2]/span[2]").getText()));
                    }
                } else {
                    if (fullTime) {
                        teamData.sumFullTimeTouchdownAway(Integer.parseInt(driver.findElementByXPath(
                                "//*[@id='summary-content']/div[1]/div[1]/div[2]/span[2]").getText()));
                        teamData.sumFullTimePassAway(Integer.parseInt(driver.findElementByXPath(
                                "//*[@id='summary-content']/div[1]/div[1]/div[2]/span[1]").getText()));
                    } else {
                        teamData.sumTouchdownAway(Integer.parseInt(driver.findElementByXPath(
                                "//*[@id='summary-content']/div[1]/div[1]/div[2]/span[2]").getText()));
                        teamData.sumPassAway(Integer.parseInt(driver.findElementByXPath(
                                "//*[@id='summary-content']/div[1]/div[1]/div[2]/span[1]").getText()));
                    }
                }
                driver.close();
                driver.switchTo().window(window);
            }
            return teamData;
        } else {
            for (int i = 0; i < 5; i++) {
                numberGameOneTeam.get(i).click();
                Set<String> windowAnalysis = driver.getWindowHandles();
                windowAnalysis.removeAll(Collections.singleton(window));
                windowAnalysis.removeAll(Collections.singleton(mainWindow));
                driver.switchTo().window(windowAnalysis.iterator().next());
                waitingByXPATH("//*[@id='summary-content']/div[1]/div[1]/div[2]/span[1]");
                if (driver.findElementByXPath("//*[@id='flashscore']/div[1]/div[1]/div[2]/div/div/a")
                        .getText().toLowerCase()
                        .contains(teamData.getName().toLowerCase())) {
                    if (fullTime) {
                        teamData.sumFullTimeTouchdownHome(Integer.parseInt(driver.findElementByXPath(
                                "//*[@id='summary-content']/div[1]/div[1]/div[2]/span[1]").getText()));
                        teamData.sumFullTimePassHome(Integer.parseInt(driver.findElementByXPath(
                                "//*[@id='summary-content']/div[1]/div[1]/div[2]/span[2]").getText()));
                    } else {
                        teamData.sumTouchdownHome(Integer.parseInt(driver.findElementByXPath(
                                "//*[@id='summary-content']/div[1]/div[1]/div[2]/span[1]").getText()));
                        teamData.sumPassHome(Integer.parseInt(driver.findElementByXPath(
                                "//*[@id='summary-content']/div[1]/div[1]/div[2]/span[2]").getText()));
                    }
                } else {
                    if (fullTime) {
                        teamData.sumFullTimeTouchdownAway(Integer.parseInt(driver.findElementByXPath(
                                "//*[@id='summary-content']/div[1]/div[1]/div[2]/span[2]").getText()));
                        teamData.sumFullTimePassAway(Integer.parseInt(driver.findElementByXPath(
                                "//*[@id='summary-content']/div[1]/div[1]/div[2]/span[1]").getText()));
                    } else {
                        teamData.sumTouchdownAway(Integer.parseInt(driver.findElementByXPath(
                                "//*[@id='summary-content']/div[1]/div[1]/div[2]/span[2]").getText()));
                        teamData.sumPassAway(Integer.parseInt(driver.findElementByXPath(
                                "//*[@id='summary-content']/div[1]/div[1]/div[2]/span[1]").getText()));
                    }
                }
                driver.close();
                driver.switchTo().window(window);
            }
            return teamData;
        }
    }

    public void resultOfTheFirstFormula(TeamInfo teamOneInfo, TeamInfo teamTwoInfo) {
        if (teamTwoInfo.getPassAway() != 0 && teamTwoInfo.getPassHome() == 0) {
            teamOneInfo.setResultsFirstFormula((float) (teamOneInfo.getTouchdownHome() / teamTwoInfo.getPassAway()));
        } else if (teamTwoInfo.getPassAway() == 0 && teamTwoInfo.getPassHome() != 0) {
            teamOneInfo.setResultsFirstFormula((float) (teamOneInfo.getTouchdownAway() / teamTwoInfo.getPassHome()));
        } else if (teamTwoInfo.getPassAway() != 0 && teamTwoInfo.getPassHome() != 0) {
            teamOneInfo.setResultsFirstFormula((float) (teamOneInfo.getTouchdownHome() / teamTwoInfo.getPassAway()) +
                    (float) (teamOneInfo.getTouchdownAway() / teamTwoInfo.getPassHome()));
        } else {
            System.out.println("Ошибка!! Есть нулевые значения!!");
            teamOneInfo.setResultsFirstFormula(0);
        }
    }

    public void resultOfTheFirstFormulaFullTime(TeamInfo teamOneInfo, TeamInfo teamTwoInfo) {
        if (teamTwoInfo.getFullTimePassAway() != 0 && teamTwoInfo.getFullTimePassHome() == 0) {
            teamOneInfo.setResultsFullTimeFirstFormula((float) (teamOneInfo.getFullTimeTouchdownHome() / teamTwoInfo.getFullTimePassAway()));
        } else if (teamTwoInfo.getFullTimePassAway() == 0 && teamTwoInfo.getFullTimePassHome() != 0) {
            teamOneInfo.setResultsFullTimeFirstFormula((float) ((teamOneInfo.getFullTimeTouchdownAway() / teamTwoInfo.getFullTimePassHome())));
        } else if (teamTwoInfo.getFullTimePassAway() != 0 && teamTwoInfo.getFullTimePassHome() != 0) {
            teamOneInfo.setResultsFullTimeFirstFormula((float) (teamOneInfo.getFullTimeTouchdownHome() / teamTwoInfo.getFullTimePassAway()) +
                    (float) (teamOneInfo.getFullTimeTouchdownAway() / teamTwoInfo.getFullTimePassHome()));
        } else {
            System.out.println("Ошибка!! Есть нулевые значения!!");
            teamOneInfo.setResultsFullTimeFirstFormula(0);
        }
    }

    public void resultMotivation(TeamInfo teamInfo) {
        if (teamInfo.getVictoryGame() != 0) {
            teamInfo.setMotivationPercentage((float) teamInfo.getVictoryGame() / (float) teamInfo.getCountGame() * 100);
        } else {
            teamInfo.setMotivationPercentage(0);
        }
    }

    public void sendResultTelegram(TeamInfo teamOne, TeamInfo teamTwo) {
        String result = teamOne.getCountry() + " " + teamOne.getTime() + "\n" +
                teamOne.getName() + " - " + teamOne.getResultsFirstFormula() + " | " +
                teamTwo.getResultsFirstFormula() + " - " + teamTwo.getName() + "\n" +
                "Процент мотивации: \n" +
                teamOne.getName() + " - " + teamOne.getMotivationPercentage() + "% | " +
                teamTwo.getMotivationPercentage() + "%" + " - " + teamTwo.getName() + "\n" +
                "Очные игры: \n" +
                teamOne.getName() + " - " + teamOne.getResultsFullTimeFirstFormula() + " | " +
                teamTwo.getResultsFullTimeFirstFormula() + " - " + teamTwo.getName() + "\n";

        botTelegram.sendMatchResult(result);
    }
}
