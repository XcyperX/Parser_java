package main.java;
import main.java.Team.TeamInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;


public class SqlSand {
    private static Connection getDBConnection() {
        Connection dbConnection = null;
        try {
            dbConnection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost/Footbol_data_java/", "postgres", "admin");
            return dbConnection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void insertIntoBD(String nameBD, String name, String country, String data, String time, int touchdownHome, int touchdownAway, int passHome,
                             int passAway, int fullTimeTouchdownHome, int fullTimeTouchdownAway, int fullTimePassHome,
                             int fullTimePassAway, int countGame, int victoryGame, int loseGame, double resultsFirstFormula,
                             double resultsFullTimeFirstFormula, float motivationPercentage) throws SQLException {
        Connection dbConnection = null;
        Statement statement = null;
        String st = ("INSERT INTO " + nameBD + " (name, country, datamatch, timematch, touchdownHome," +
                "touchdownAway, passHome, passAway, fullTimeTouchdownHome, fullTimeTouchdownAway, fullTimePassHome," +
                "fullTimePassAway, countGame, victoryGame, loseGame, resultsFirstFormula, resultsFullTimeFirstFormula," +
                "motivationPercentage) VALUES ('" + name + "', '" + country + "', '" + data + "', '" + time + "', '" + touchdownHome + "', '" + touchdownAway + "', " +
                "'" + passHome + "', '" + passAway + "', '" + fullTimeTouchdownHome + "', '" + fullTimeTouchdownAway + "', '" + fullTimePassHome + "', " +
                "'" + fullTimePassAway + "','" + countGame + "','" + victoryGame + "','" + loseGame + "'," +
                "'" + resultsFirstFormula + "','" + resultsFullTimeFirstFormula + "','" + motivationPercentage + "')");
        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();
            // выполнить SQL запрос
            statement.executeUpdate(st);
            statement.close();
            dbConnection.close();
        } catch (SQLException e) {
            String string = "INSERT INTO " + nameBD + " (name) VALUES ('Ошибка')";
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();
            // выполнить SQL запрос
            statement.executeUpdate(string);
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (dbConnection != null) {
                dbConnection.close();
            }
        }
    }

    public HashMap<String, String> findMatch(String name, String data) throws SQLException {
        Connection dbConnection = null;
        Statement statement = null;
        String st = "SELECT name, datamatch FROM oneteam\n" +
                    "WHERE name IN ('" + name + "') and datamatch IN ('" + data + "')";
//        ArrayList<String> name = new ArrayList<String>();
        HashMap<String, String> nameAndDate = new HashMap<>();
        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();
            // выполнить SQL запрос
            ResultSet resultSet = statement.executeQuery(st);

            while (resultSet.next()) {
                nameAndDate.put(resultSet.getString("name"), resultSet.getString("datamatch"));
//                name.add(resultSet.getString("name"));
            }
//            return name;
            return nameAndDate;
        } catch (SQLException e) {
            return null;
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (dbConnection != null) {
                dbConnection.close();
            }
        }
    }

    public HashMap<String, String> findWinMatch(String name, String data) throws SQLException {
        Connection dbConnection = null;
        Statement statement = null;
        String st = "SELECT * FROM oneteam\n" +
                    "JOIN twoteam On twoteam.id = oneteam.id" +
                    "WHERE oneteam.name IN ('" + name + "') and oneteam.datamatch IN ('" + data + "')";
//        ArrayList<String> name = new ArrayList<String>();
        HashMap<String, String> nameAndDate = new HashMap<>();
        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();
            // выполнить SQL запрос
            ResultSet resultSet = statement.executeQuery(st);

            while (resultSet.next()) {
                System.out.println(resultSet.getArray(0));
//                nameAndDate.put(resultSet.getString("name"), resultSet.getString("datamatch"));
//                name.add(resultSet.getString("name"));
            }
//            return name;
            return nameAndDate;
        } catch (SQLException e) {
            return null;
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (dbConnection != null) {
                dbConnection.close();
            }
        }
    }

//    public HashMap<String, String> selectName() throws SQLException {
//        Connection dbConnection = null;
//        Statement statement = null;
//        String st = "SELECT name, datamatch FROM oneteam";
////        ArrayList<String> name = new ArrayList<String>();
//        HashMap<String, String> nameAndDate = new HashMap<>();
//        try {
//            dbConnection = getDBConnection();
//            statement = dbConnection.createStatement();
//            // выполнить SQL запрос
//            ResultSet resultSet = statement.executeQuery(st);
//
//            while (resultSet.next()) {
//                nameAndDate.put(resultSet.getString("name"), resultSet.getString("datamatch"));
////                name.add(resultSet.getString("name"));
//            }
////            return name;
//            return nameAndDate;
//        } catch (SQLException e) {
//            return null;
//        } finally {
//            if (statement != null) {
//                statement.close();
//            }
//            if (dbConnection != null) {
//                dbConnection.close();
//            }
//        }
//    }

    public ArrayList<String> selectDataAndTime(String data, String time) throws SQLException {
        Connection dbConnection = null;
        Statement statementOne = null;
        Statement statementTwo = null;
        String timeMax = Integer.parseInt(time.split(":")[0]) + 1 + ":00";
        String oneTeam = "SELECT * FROM oneteam " +
                "WHERE datamatch = '" + data + "' and timematch >= '" + time + "' and timematch <= '" + timeMax + "'";
        String twoTeam = "SELECT * FROM twoteam " +
                "WHERE datamatch = '" + data + "' and timematch >= '" + time + "' and timematch <= '" + timeMax + "'";
        ArrayList<TeamInfo> oneTeamList = new ArrayList<>();
        ArrayList<TeamInfo> twoTeamList = new ArrayList<>();
        ArrayList<String> result = new ArrayList<>();
        try {
            dbConnection = getDBConnection();
            statementOne = dbConnection.createStatement();
            statementTwo = dbConnection.createStatement();
            // выполнить SQL запрос
            ResultSet infoOneTeam = statementOne.executeQuery(oneTeam);
            ResultSet infoTwoTeam = statementTwo.executeQuery(twoTeam);

            while (infoOneTeam.next()) {
                TeamInfo oneTeamInfo = new TeamInfo();
                oneTeamInfo.setName(infoOneTeam.getString("name"));
                oneTeamInfo.setCountry(infoOneTeam.getString("country"));
                oneTeamInfo.setTime(infoOneTeam.getString("timematch"));
                oneTeamInfo.setResultsFirstFormula(infoOneTeam.getFloat("resultsFirstFormula"));
                oneTeamInfo.setResultsFullTimeFirstFormula(infoOneTeam.getFloat("resultsFullTimeFirstFormula"));
                oneTeamInfo.setMotivationPercentage(infoOneTeam.getFloat("motivationPercentage"));
                oneTeamList.add(oneTeamInfo);
            }
            while (infoTwoTeam.next()) {
                TeamInfo twoTeamInfo = new TeamInfo();
                twoTeamInfo.setName(infoTwoTeam.getString("name"));
                twoTeamInfo.setCountry(infoTwoTeam.getString("country"));
                twoTeamInfo.setTime(infoTwoTeam.getString("timematch"));
                twoTeamInfo.setResultsFirstFormula(infoTwoTeam.getFloat("resultsFirstFormula"));
                twoTeamInfo.setResultsFullTimeFirstFormula(infoTwoTeam.getFloat("resultsFullTimeFirstFormula"));
                twoTeamInfo.setMotivationPercentage(infoTwoTeam.getFloat("motivationPercentage"));
                twoTeamList.add(twoTeamInfo);
            }

            for (int i = 0; i < oneTeamList.size(); i++) {
                TeamInfo oneTeamInfo = oneTeamList.get(i);
                TeamInfo twoTeamInfo = twoTeamList.get(i);
                String resultInfo = oneTeamInfo.getCountry() + " " + oneTeamInfo.getTime() + "\n" +
                        oneTeamInfo.getName() + " - " + oneTeamInfo.getResultsFirstFormula() + " | " +
                        twoTeamInfo.getResultsFirstFormula() + " - " + twoTeamInfo.getName() + "\n" +
                        "Процент мотивации: \n" +
                        oneTeamInfo.getName() + " - " + oneTeamInfo.getMotivationPercentage() + " | " +
                        twoTeamInfo.getMotivationPercentage() + " - " + twoTeamInfo.getName() + "\n" +
                        "Очные игры: \n" +
                        oneTeamInfo.getName() + " - " + oneTeamInfo.getResultsFullTimeFirstFormula() + " | " +
                        twoTeamInfo.getResultsFullTimeFirstFormula() + " - " + twoTeamInfo.getName() + "\n";
                result.add(resultInfo);
            }
            return result;
        } finally {
            if (statementOne != null) {
                statementOne.close();
                statementTwo.close();
            }
            if (dbConnection != null) {
                dbConnection.close();
            }
        }
    }


//    public static void main(String[] args) throws SQLException {
//        Connection dbConnection = null;
//        Statement statementOne = null;
//        Statement statementTwo = null;
//
//
//        String oneTeam = "SELECT * FROM oneteam" +
//                "WHERE datamatch = '" + data + "' and timematch = '" + time + "'";
//        String twoTeam = "SELECT * FROM twoteam" +
//                "WHERE datamatch = '" + data + "' and timematch = '" + time + "'";
//        ArrayList<TeamInfo> oneTeamList = new ArrayList<>();
//        ArrayList<TeamInfo> twoTeamList = new ArrayList<>();
//        ArrayList<String> result = new ArrayList<>();
//        try {
//            dbConnection = getDBConnection();
//            statementOne = dbConnection.createStatement();
//            statementTwo = dbConnection.createStatement();
//            // выполнить SQL запрос
//            ResultSet infoOneTeam = statementOne.executeQuery(oneTeam);
//            ResultSet infoTwoTeam = statementTwo.executeQuery(twoTeam);
//
//            while (infoOneTeam.next()) {
//                TeamInfo oneTeamInfo = new TeamInfo();
//                oneTeamInfo.setName(infoOneTeam.getString("name"));
//                oneTeamInfo.setCountry(infoOneTeam.getString("country"));
//                oneTeamInfo.setTime(infoOneTeam.getString("timematch"));
//                oneTeamInfo.setResultsFirstFormula(infoOneTeam.getFloat("resultsFirstFormula"));
//                oneTeamInfo.setResultsFullTimeFirstFormula(infoOneTeam.getFloat("resultsFullTimeFirstFormula"));
//                oneTeamInfo.setMotivationPercentage(infoOneTeam.getFloat("motivationPercentage"));
//                oneTeamList.add(oneTeamInfo);
//            }
//            while (infoTwoTeam.next()) {
//                TeamInfo twoTeamInfo = new TeamInfo();
//                twoTeamInfo.setName(infoTwoTeam.getString("name"));
//                twoTeamInfo.setCountry(infoTwoTeam.getString("country"));
//                twoTeamInfo.setTime(infoTwoTeam.getString("timematch"));
//                twoTeamInfo.setResultsFirstFormula(infoTwoTeam.getFloat("resultsFirstFormula"));
//                twoTeamInfo.setResultsFullTimeFirstFormula(infoTwoTeam.getFloat("resultsFullTimeFirstFormula"));
//                twoTeamInfo.setMotivationPercentage(infoTwoTeam.getFloat("motivationPercentage"));
//                twoTeamList.add(twoTeamInfo);
//            }
//
//            for (int i = 0; i < oneTeamList.size(); i++) {
//                TeamInfo oneTeamInfo = oneTeamList.get(i);
//                TeamInfo twoTeamInfo = twoTeamList.get(i);
//                String resultInfo = oneTeamInfo.getCountry() + " " + oneTeamInfo.getTime() + "\n" +
//                        oneTeamInfo.getName() + " - " + oneTeamInfo.getResultsFirstFormula() + " | " +
//                        twoTeamInfo.getResultsFirstFormula() + " - " + twoTeamInfo.getName() + "\n" +
//                        "Процент мотивации: \n" +
//                        oneTeamInfo.getName() + " - " + oneTeamInfo.getMotivationPercentage() + " | " +
//                        twoTeamInfo.getMotivationPercentage() + " - " + twoTeamInfo.getName() + "\n" +
//                        "Очные игры: \n" +
//                        oneTeamInfo.getName() + " - " + oneTeamInfo.getResultsFullTimeFirstFormula() + " | " +
//                        twoTeamInfo.getResultsFullTimeFirstFormula() + " - " + twoTeamInfo.getName() + "\n";
//                System.out.println(resultInfo);
//                result.add(resultInfo);
//            }
//        } finally {
//            if (statementOne != null) {
//                statementOne.close();
//                statementTwo.close();
//            }
//            if (dbConnection != null) {
//                dbConnection.close();
//            }
//        }
//    }
}
