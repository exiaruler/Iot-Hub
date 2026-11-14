package com.scheduler.app.backend.Messaging.Models;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
// store input current
@Entity
public class InputCurrent extends Current {

    // input
    @ManyToOne
    @JoinColumn(name="board_task_id")
    @JsonBackReference("boardvariable-input")
    private BoardTask boardTaskInput;

    public InputCurrent() {
    }

    public InputCurrent(BoardTask boardTaskInput) {
        this.boardTaskInput = boardTaskInput;
    }

    public BoardTask getBoardTaskInput() {
        return this.boardTaskInput;
    }

    public void setBoardTaskInput(BoardTask boardTaskInput) {
        this.boardTaskInput = boardTaskInput;
    }

    public InputCurrent boardTaskInput(BoardTask boardTaskInput) {
        setBoardTaskInput(boardTaskInput);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof InputCurrent)) {
            return false;
        }
        InputCurrent inputCurrent = (InputCurrent) o;
        return Objects.equals(boardTaskInput, inputCurrent.boardTaskInput);
    }

   
    @Override
    public String toString() {
        return "{" +
            " boardTaskInput='" + getBoardTaskInput() + "'" +
            "}";
    }
    
}
