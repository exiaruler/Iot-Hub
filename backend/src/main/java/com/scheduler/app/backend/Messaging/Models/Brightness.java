package com.scheduler.app.backend.Messaging.Models;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Brightness extends Current {

    @ManyToOne
    @JoinColumn(name="board_variable_id")
    @JsonBackReference("boardvariable-bright")
    private BoardVariable boardVariable;

    public Brightness() {
    }

    public Brightness(BoardVariable boardVariable) {
        this.boardVariable = boardVariable;
    }

    public BoardVariable getBoardVariable() {
        return this.boardVariable;
    }

    public void setBoardVariable(BoardVariable boardVariable) {
        this.boardVariable = boardVariable;
    }

    public Brightness boardVariable(BoardVariable boardVariable) {
        setBoardVariable(boardVariable);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Brightness)) {
            return false;
        }
        Brightness brightness = (Brightness) o;
        return Objects.equals(boardVariable, brightness.boardVariable);
    }

    @Override
    public String toString() {
        return "{" +
            " boardVariable='" + getBoardVariable() + "'" +
            "}";
    }
    
}
