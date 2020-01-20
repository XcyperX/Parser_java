package main.java;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        FirstTest firstTest = new FirstTest();
        firstTest.start("https://www.myscore.ru/");
    }
}
