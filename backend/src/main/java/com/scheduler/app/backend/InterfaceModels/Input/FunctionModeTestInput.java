package com.scheduler.app.backend.InterfaceModels.Input;

import java.util.Objects;

import com.scheduler.app.backend.Messaging.Models.BoardTask;

public class FunctionModeTestInput {
    private String electrode;
    private BoardTask boardTask;


    public FunctionModeTestInput() {
    }

    public FunctionModeTestInput(String electrode, BoardTask boardTask) {
        this.electrode = electrode;
        this.boardTask = boardTask;
    }

    public String getElectrode() {
        return this.electrode;
    }

    public void setElectrode(String electrode) {
        this.electrode = electrode;
    }

    public BoardTask getBoardTask() {
        return this.boardTask;
    }

    public void setBoardTask(BoardTask boardTask) {
        this.boardTask = boardTask;
    }

    public FunctionModeTestInput electrode(String electrode) {
        setElectrode(electrode);
        return this;
    }

    public FunctionModeTestInput boardTask(BoardTask boardTask) {
        setBoardTask(boardTask);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof FunctionModeTestInput)) {
            return false;
        }
        FunctionModeTestInput functionModeTestInput = (FunctionModeTestInput) o;
        return Objects.equals(electrode, functionModeTestInput.electrode) && Objects.equals(boardTask, functionModeTestInput.boardTask);
    }

    @Override
    public int hashCode() {
        return Objects.hash(electrode, boardTask);
    }

    @Override
    public String toString() {
        return "{" +
            " electrode='" + getElectrode() + "'" +
            ", boardTask='" + getBoardTask() + "'" +
            "}";
    }

    
}
