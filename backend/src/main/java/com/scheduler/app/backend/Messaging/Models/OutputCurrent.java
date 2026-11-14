package com.scheduler.app.backend.Messaging.Models;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
// store output current
@Entity
public class OutputCurrent extends Current {

    // output
    @ManyToOne
    @JoinColumn(name="board_task_id")
    @JsonBackReference("boardvariable-output")
    private BoardTask boardTaskOutput;

    public OutputCurrent() {
    }

    public OutputCurrent(BoardTask boardTaskOutput) {
        this.boardTaskOutput = boardTaskOutput;
    }

    public BoardTask getBoardTaskOutput() {
        return this.boardTaskOutput;
    }

    public void setBoardTaskOutput(BoardTask boardTaskOutput) {
        this.boardTaskOutput = boardTaskOutput;
    }

    public OutputCurrent boardTaskOutput(BoardTask boardTaskOutput) {
        setBoardTaskOutput(boardTaskOutput);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof OutputCurrent)) {
            return false;
        }
        OutputCurrent outputCurrent = (OutputCurrent) o;
        return Objects.equals(boardTaskOutput, outputCurrent.boardTaskOutput);
    }

    

    @Override
    public String toString() {
        return "{" +
            " boardTaskOutput='" + getBoardTaskOutput() + "'" +
            "}";
    }
    
}
