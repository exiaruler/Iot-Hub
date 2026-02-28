package com.scheduler.app.backend.Hardware.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.scheduler.Base.ModelBase.ModelBase;
import com.scheduler.app.backend.aREST.Models.Board;

@Entity
public class Hardware extends ModelBase {
    // board model
    @Column
    private String boardName;
    // RAM compacity
    @Column
    private int maxRam;
    // pins
    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "hardware", cascade =CascadeType.ALL, orphanRemoval = true)
    private List<HardwarePins> pins=new ArrayList<>();;
    // boards that use this hardware model
    //@JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "hardware", cascade =CascadeType.ALL)
    @JsonIgnore
    private List<Board> boardsUsed=new ArrayList<>();;


    public Hardware() {
    }

    public Hardware(String boardName, int maxRam, List<HardwarePins> pins, List<Board> boardsUsed) {
        this.boardName = boardName;
        this.maxRam = maxRam;
        this.pins = pins;
        this.boardsUsed = boardsUsed;
    }

    public String getBoardName() {
        return this.boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public int getMaxRam() {
        return this.maxRam;
    }

    public void setMaxRam(int maxRam) {
        this.maxRam = maxRam;
    }

    public List<HardwarePins> getPins() {
        return this.pins;
    }

    public void setPins(List<HardwarePins> pins) {
        this.pins = pins;
    }

    public List<Board> getBoardsUsed() {
        return this.boardsUsed;
    }

    public void setBoardsUsed(List<Board> boardsUsed) {
        this.boardsUsed = boardsUsed;
    }

    public Hardware boardName(String boardName) {
        setBoardName(boardName);
        return this;
    }

    public Hardware maxRam(int maxRam) {
        setMaxRam(maxRam);
        return this;
    }

    public Hardware pins(List<HardwarePins> pins) {
        setPins(pins);
        return this;
    }

    public Hardware boardsUsed(List<Board> boardsUsed) {
        setBoardsUsed(boardsUsed);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Hardware)) {
            return false;
        }
        Hardware hardware = (Hardware) o;
        return Objects.equals(boardName, hardware.boardName) && maxRam == hardware.maxRam && Objects.equals(pins, hardware.pins) && Objects.equals(boardsUsed, hardware.boardsUsed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardName, maxRam, pins, boardsUsed);
    }

    @Override
    public String toString() {
        return "{" +
            " boardName='" + getBoardName() + "'" +
            ", maxRam='" + getMaxRam() + "'" +
            ", pins='" + getPins() + "'" +
            ", boardsUsed='" + getBoardsUsed() + "'" +
            "}";
    }
    
    
}
