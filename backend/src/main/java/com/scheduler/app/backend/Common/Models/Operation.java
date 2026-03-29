package com.scheduler.app.backend.Common.Models;

import java.time.Duration;
import java.util.Objects;

import com.scheduler.Base.ModelBase.ModelBase;
import com.scheduler.app.backend.aREST.Models.Board;
public class Operation extends ModelBase{
    // days   1 day = 24 hours
    private long day;
    // hours 1 hour = 60 minutes
    private long hour;
    // minutes 1 minute = 60 seconds
    private long minute;
    // seconds
    private long second;
    // raw operation time in milliseconds
    private long rawTime;
    // board
    private Board board;


    public Operation() {
    }

    public Operation(long day, long hour, long minute, long second, long rawTime, Board board) {
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.rawTime = rawTime;
        this.board = board;
    }

    public long getDay() {
        return this.day;
    }

    private void setDay(long day) {
        this.day = day;
    }

    public long getHour() {
        return this.hour;
    }

    private void setHour(long hour) {
        this.hour = hour;
    }

    public long getMinute() {
        return this.minute;
    }

    private void setMinute(long minute) {
        this.minute = minute;
    }

    public long getSecond() {
        return this.second;
    }

    public void setSecond(long second) {
        this.second = second;
    }

    public long getRawTime() {
        return this.rawTime;
    }

    public void setRawTime(long rawTime) {
        long currentRawTime=this.getRawTime()+rawTime;
        Duration duration=Duration.ofMillis(currentRawTime);
        this.rawTime = rawTime;
        this.setDay(duration.toDays());
        this.setHour(duration.toHours() % 24);
        this.setMinute(duration.toMinutes() % 60);
        this.setSecond(duration.getSeconds() % 60);
    }

    public Board getBoard() {
        return this.board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Operation day(long day) {
        setDay(day);
        return this;
    }

    public Operation hour(long hour) {
        setHour(hour);
        return this;
    }

    public Operation minute(long minute) {
        setMinute(minute);
        return this;
    }

    public Operation second(long second) {
        setSecond(second);
        return this;
    }

    public Operation rawTime(long rawTime) {
        setRawTime(rawTime);
        return this;
    }

    public Operation board(Board board) {
        setBoard(board);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Operation)) {
            return false;
        }
        Operation operation = (Operation) o;
        return day == operation.day && hour == operation.hour && minute == operation.minute && second == operation.second && rawTime == operation.rawTime && Objects.equals(board, operation.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, hour, minute, second, rawTime, board);
    }

    @Override
    public String toString() {
        return "{" +
            " day='" + getDay() + "'" +
            ", hour='" + getHour() + "'" +
            ", minute='" + getMinute() + "'" +
            ", second='" + getSecond() + "'" +
            ", rawTime='" + getRawTime() + "'" +
            ", board='" + getBoard() + "'" +
            "}";
    }
    



}
