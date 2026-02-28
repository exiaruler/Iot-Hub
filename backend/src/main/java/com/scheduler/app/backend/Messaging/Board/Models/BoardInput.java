package com.scheduler.app.backend.Messaging.Board.Models;
import java.util.Objects;
// board message after executed of command 
public class BoardInput {
    // board 
    private long board;
    // message type
    private String action;
    // RAM space left on board
    private int ramSpace;
    // queue size
    private int queueSize;
    // system queue size
    private int systemQueueSize;
    // ip address
    private String ip;
    // board run time
    private long boardRunning;
    // milliseconds took to process message
    //private int responseTime;
    // executed task
    private BoardInputTask task;
    

    public BoardInput() {
    }

    public BoardInput(long board, String action, int ramSpace, int queueSize, int systemQueueSize, String ip, long boardRunning, BoardInputTask task) {
        this.board = board;
        this.action = action;
        this.ramSpace = ramSpace;
        this.queueSize = queueSize;
        this.systemQueueSize = systemQueueSize;
        this.ip = ip;
        this.boardRunning = boardRunning;
        this.task = task;
    }

    public long getBoard() {
        return this.board;
    }

    public void setBoard(long board) {
        this.board = board;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getRamSpace() {
        return this.ramSpace;
    }

    public void setRamSpace(int ramSpace) {
        this.ramSpace = ramSpace;
    }

    public int getQueueSize() {
        return this.queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public int getSystemQueueSize() {
        return this.systemQueueSize;
    }

    public void setSystemQueueSize(int systemQueueSize) {
        this.systemQueueSize = systemQueueSize;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getBoardRunning() {
        return this.boardRunning;
    }

    public void setBoardRunning(long boardRunning) {
        this.boardRunning = boardRunning;
    }

    public BoardInputTask getTask() {
        return this.task;
    }

    public void setTask(BoardInputTask task) {
        this.task = task;
    }

    public BoardInput board(long board) {
        setBoard(board);
        return this;
    }

    public BoardInput action(String action) {
        setAction(action);
        return this;
    }

    public BoardInput ramSpace(int ramSpace) {
        setRamSpace(ramSpace);
        return this;
    }

    public BoardInput queueSize(int queueSize) {
        setQueueSize(queueSize);
        return this;
    }

    public BoardInput systemQueueSize(int systemQueueSize) {
        setSystemQueueSize(systemQueueSize);
        return this;
    }

    public BoardInput ip(String ip) {
        setIp(ip);
        return this;
    }

    public BoardInput boardRunning(long boardRunning) {
        setBoardRunning(boardRunning);
        return this;
    }

    public BoardInput task(BoardInputTask task) {
        setTask(task);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof BoardInput)) {
            return false;
        }
        BoardInput boardInput = (BoardInput) o;
        return board == boardInput.board && Objects.equals(action, boardInput.action) && ramSpace == boardInput.ramSpace && queueSize == boardInput.queueSize && systemQueueSize == boardInput.systemQueueSize && Objects.equals(ip, boardInput.ip) && boardRunning == boardInput.boardRunning && Objects.equals(task, boardInput.task);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, action, ramSpace, queueSize, systemQueueSize, ip, boardRunning, task);
    }

    @Override
    public String toString() {
        return "{" +
            " board='" + getBoard() + "'" +
            ", action='" + getAction() + "'" +
            ", ramSpace='" + getRamSpace() + "'" +
            ", queueSize='" + getQueueSize() + "'" +
            ", systemQueueSize='" + getSystemQueueSize() + "'" +
            ", ip='" + getIp() + "'" +
            ", boardRunning='" + getBoardRunning() + "'" +
            ", task='" + getTask() + "'" +
            "}";
    }

    
}
