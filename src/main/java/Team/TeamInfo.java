package main.java.Team;

import main.java.interfaces.TeamData;

public class TeamInfo implements TeamData {
    private String name;
    private String country;
    private String data;
    private String time;
    private int touchdownHome = 0;
    private int touchdownAway = 0;
    private int passHome = 0;
    private int passAway = 0;
    private int fullTimeTouchdownHome = 0;
    private int fullTimeTouchdownAway = 0;
    private int fullTimePassHome = 0;
    private int fullTimePassAway = 0;
    private int countGame = 0;
    private int victoryGame = 0;
    private int loseGame = 0;
    private float resultsFirstFormula = 0;
    private float resultsFullTimeFirstFormula = 0;
    private float motivationPercentage = 0;

    public TeamInfo() {

    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getData() {
        return data;
    }

    public String getTime() {
        return time;
    }

    public int getTouchdownHome() {
        return touchdownHome;
    }

    public int getTouchdownAway() {
        return touchdownAway;
    }

    public int getPassHome() {
        return passHome;
    }

    public int getPassAway() {
        return passAway;
    }

    public int getCountGame() {
        return countGame;
    }

    public int getVictoryGame() {
        return victoryGame;
    }

    public int getLoseGame() {
        return loseGame;
    }

    public int getFullTimeTouchdownHome() { return fullTimeTouchdownHome; }

    public int getFullTimeTouchdownAway() { return fullTimeTouchdownAway; }

    public int getFullTimePassHome() { return fullTimePassHome; }

    public int getFullTimePassAway() { return  fullTimePassAway; }

    public double getResultsFirstFormula() {
        return resultsFirstFormula;
    }

    public double getResultsFullTimeFirstFormula() {
        return resultsFullTimeFirstFormula;
    }

    public float getMotivationPercentage() {
        return motivationPercentage;
    }

    public void sumTouchdownHome(int touchdown) {
        this.touchdownHome += touchdown;
    }

    public void sumTouchdownAway(int touchdown) {
        this.touchdownAway += touchdown;
    }

    public void sumPassHome(int touchdown) {
        this.passHome += touchdown;
    }

    public void sumPassAway(int touchdown) {
        this.passAway += touchdown;
    }

    public void sumFullTimeTouchdownHome(int touchdown) {
        this.fullTimeTouchdownHome += touchdown;
    }

    public void sumFullTimeTouchdownAway(int touchdown) {
        this.fullTimeTouchdownAway += touchdown;
    }

    public void sumFullTimePassHome(int touchdown) {
        this.fullTimePassHome += touchdown;
    }

    public void sumFullTimePassAway(int touchdown) {
        this.fullTimePassAway += touchdown;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTouchdownHome(int touchdownHome) {
        this.touchdownHome = touchdownHome;
    }

    public void setTouchdownAway(int touchdownAway) {
        this.touchdownAway = touchdownAway;
    }

    public void setPassHome(int passHome) {
        this.passHome = passHome;
    }

    public void setPassAway(int passAway) {
        this.passAway = passAway;
    }

    public void setResultsFirstFormula(float resultsFirstFormula) {
        this.resultsFirstFormula = resultsFirstFormula;
    }

    public void setResultsFullTimeFirstFormula(float resultsFullTimeFirstFormula) {
        this.resultsFullTimeFirstFormula = resultsFullTimeFirstFormula;
    }

    public void setFullTimeTouchdownHome(int fullTimeTouchdownHome) {
        this.fullTimeTouchdownHome = fullTimeTouchdownHome;
    }

    public void setFullTimeTouchdownAway(int fullTimeTouchdownAway) {
        this.fullTimeTouchdownAway = fullTimeTouchdownAway;
    }

    public void setFullTimePassHome(int fullTimePassHome) {
        this.fullTimePassHome = fullTimePassHome;
    }

    public void setFullTimePassAway(int fullTimePassAway) {
        this.fullTimePassAway = fullTimePassAway;
    }

    public void setMotivationPercentage(float motivationPercentage) {
        this.motivationPercentage = motivationPercentage;
    }

    public void setCountGame(int countGame) {
        this.countGame = countGame;
    }

    public void setVictoryGame(int victoryGame) {
        this.victoryGame = victoryGame;
    }

    public void setLoseGame(int loseGame) {
        this.loseGame = loseGame;
    }

    @Override
    public String toString() {
        if (fullTimePassAway != 0 || fullTimePassHome != 0 || fullTimeTouchdownAway != 0 || fullTimeTouchdownHome != 0) {
            return "Команда: " + name + "\n" +
                    "Страна: " + country + "\n" +
                    "Дата: " + data + " " +
                    "Время: " + time + "\n" +
                    "Голы дома: " + touchdownHome + "\n" +
                    "Голы в гостях: " + touchdownAway + "\n" +
                    "Пропуски дома: " + passHome + "\n" +
                    "Пропуски в гостях: " + passAway + "\n" +
                    "Количество игр: " + countGame + "\n" +
                    "Количество побед: " + victoryGame + "\n" +
                    "Количество проигрышей: " + loseGame + "\n" +
                    "Очные игры: \n" +
                    "Голы дома: " + fullTimeTouchdownHome + "\n" +
                    "Голы в гостях: " + fullTimeTouchdownAway + "\n" +
                    "Пропуски дома: " + fullTimePassHome + "\n" +
                    "Пропуски в гостях: " + fullTimePassAway + "\n" +
                    "Расчет по первой формуле: " + resultsFirstFormula + "\n" +
                    "Расчет по первой формуле очных игр: " + resultsFullTimeFirstFormula + "\n" +
                    "Процент мотивации: " + motivationPercentage + "%";
        } else {
            return "Команда: " + name + "\n" +
                    "Страна: " + country + "\n" +
                    "Дата: " + data + " " +
                    "Время: " + time + "\n" +
                    "Голы дома: " + touchdownHome + "\n" +
                    "Голы в гостях: " + touchdownAway + "\n" +
                    "Пропуски дома: " + passHome + "\n" +
                    "Пропуски в гостях: " + passAway + "\n" +
                    "Количество игр: " + countGame + "\n" +
                    "Количество побед: " + victoryGame + "\n" +
                    "Количество проигрышей: " + loseGame + "\n" +
                    "Расчет по первой формуле: " + resultsFirstFormula + "\n" +
                    "Процент мотивации: " + motivationPercentage + "%";
        }
    }


}
