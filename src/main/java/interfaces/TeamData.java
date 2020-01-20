package main.java.interfaces;

public interface TeamData {
    String getName();
    String getCountry();
    String getTime();
    int getTouchdownHome();
    int getTouchdownAway();
    int getPassHome();
    int getPassAway();
    int getCountGame();
    int getVictoryGame();
    int getLoseGame();

    void setName(String name);
    void setCountry(String country);
    void setTime(String time);
    void setTouchdownHome(int touchdownHome);
    void setTouchdownAway(int touchdownAway);
    void setPassHome(int passHome);
    void setPassAway(int passAway);
    void setCountGame(int countGame);
    void setVictoryGame(int victoryGame);
    void setLoseGame(int loseGame);

    void sumTouchdownHome(int touchdown);
    void sumTouchdownAway(int touchdown);
    void sumPassHome(int touchdown);
    void sumPassAway(int touchdown);
}
