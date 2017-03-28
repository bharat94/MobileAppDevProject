package edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven;

/**
 * Created by vaidhyanathannarayanan on 03/03/17.
 */

public class Game {

    String board;
    String selection;
    String gameID;
    int phase;
    String player1;
    String player2;
    String score1;
    String score2;
    int timeLeft;
    boolean joined;
    boolean hosted;
    int player1gridnumber;
    int player2gridnumber;
    String gridNumbers;


    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getScore1() {
        return score1;
    }

    public void setScore1(String score1) {
        this.score1 = score1;
    }

    public String getScore2() {
        return score2;
    }

    public void setScore2(String score2) {
        this.score2 = score2;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public boolean isJoined() {
        return joined;
    }

    public void setJoined(boolean joined) {
        this.joined = joined;
    }

    public boolean isHosted() {
        return hosted;
    }

    public void setHosted(boolean hosted) {
        this.hosted = hosted;
    }

    public String getSelection() {
        return selection;
    }

    public void setSelection(String selection) {
        this.selection = selection;
    }

    public int getPlayer1gridnumber() {
        return player1gridnumber;
    }

    public void setPlayer1gridnumber(int player1gridnumber) {
        this.player1gridnumber = player1gridnumber;
    }

    public int getPlayer2gridnumber() {
        return player2gridnumber;
    }

    public void setPlayer2gridnumber(int player2gridnumber) {
        this.player2gridnumber = player2gridnumber;
    }

    public String getGridNumbers() {
        return gridNumbers;
    }

    public void setGridNumbers(String gridNumbers) {
        this.gridNumbers = gridNumbers;
    }
}
