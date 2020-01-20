package main.java;

import java.sql.SQLException;

public class WinGames {
    public static void main(String[] args) throws SQLException {
        CheckEndGame firstTest = new CheckEndGame();
        firstTest.start("https://www.myscore.ru/");
    }
}
