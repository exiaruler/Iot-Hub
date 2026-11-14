package com.scheduler.app.backend.aREST.Models;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.scheduler.Base.ModelBase.ModelBase;

@Entity
public class Section extends ModelBase {
    @Column
    private String location;
    // list of board in that location
    @JsonManagedReference("board-section")
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "section", cascade =CascadeType.ALL)
    private List<Board> boards;
    

    public Section() {
    }

    public Section(String location, List<Board> boards) {
        this.location = location;
        this.boards = boards;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Board> getBoards() {
        return this.boards;
    }

    public void setBoards(List<Board> boards) {
        this.boards = boards;
    }

    public Section location(String location) {
        setLocation(location);
        return this;
    }

    public Section boards(List<Board> boards) {
        setBoards(boards);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Section)) {
            return false;
        }
        Section section = (Section) o;
        return Objects.equals(location, section.location) && Objects.equals(boards, section.boards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, boards);
    }

    @Override
    public String toString() {
        return "{" +
            " location='" + getLocation() + "'" +
            ", boards='" + getBoards() + "'" +
            "}";
    }

}
