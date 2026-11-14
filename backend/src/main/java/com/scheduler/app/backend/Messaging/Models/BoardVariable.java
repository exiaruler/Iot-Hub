package com.scheduler.app.backend.Messaging.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.scheduler.Base.ModelBase.TaskModelBase;
// background task variables data structure in device
@Entity
@Table(name="board_variable")
public class BoardVariable extends TaskModelBase {
    
    @JsonBackReference("boardtask-variable")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "board_task_id",referencedColumnName = "id")
    private BoardTask task;
    @Column
    private int x=1;
    // redudant
    @Column
    private int output=255;
    @Column
    private Long d1;
    @Column
    private Long d2;
    // fade
    @Column
    private int brightness = 0;
    // brightness array
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "boardVariable", cascade =CascadeType.ALL)
    @JsonManagedReference("boardvariable-bright")
    private List<Brightness> brightArray=new ArrayList<>();

    //variables to hold our color intensities and direction
    //and define some initial "random" values to seed it
    @Column
    private int red=254;
    @Column
    private int green=1;
    @Column
    private int blue=127;
    @Column
    private int red_direction= -1;
    @Column
    private int green_direction= 1;
    @Column
    private int blue_direction= -1;
    @Column
    private int count=0;
    @Column
    private int currentAngle=0;


    public BoardVariable() {
    }

    public BoardVariable(BoardTask task, int x, int output, Long d1, Long d2, int brightness, List<Brightness> brightArray, int red, int green, int blue, int red_direction, int green_direction, int blue_direction, int count, int currentAngle) {
        this.task = task;
        this.x = x;
        this.output = output;
        this.d1 = d1;
        this.d2 = d2;
        this.brightness = brightness;
        this.brightArray = brightArray;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.red_direction = red_direction;
        this.green_direction = green_direction;
        this.blue_direction = blue_direction;
        this.count = count;
        this.currentAngle = currentAngle;
    }

    public BoardTask getTask() {
        return this.task;
    }

    public void setTask(BoardTask task) {
        this.task = task;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getOutput() {
        return this.output;
    }

    public void setOutput(int output) {
        this.output = output;
    }

    public Long getD1() {
        return this.d1;
    }

    public void setD1(Long d1) {
        this.d1 = d1;
    }

    public Long getD2() {
        return this.d2;
    }

    public void setD2(Long d2) {
        this.d2 = d2;
    }

    public int getBrightness() {
        return this.brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public List<Brightness> getBrightArray() {
        return this.brightArray;
    }

    public void setBrightArray(List<Brightness> brightArray) {
        this.brightArray = brightArray;
    }

    public int getRed() {
        return this.red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return this.green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return this.blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getRed_direction() {
        return this.red_direction;
    }

    public void setRed_direction(int red_direction) {
        this.red_direction = red_direction;
    }

    public int getGreen_direction() {
        return this.green_direction;
    }

    public void setGreen_direction(int green_direction) {
        this.green_direction = green_direction;
    }

    public int getBlue_direction() {
        return this.blue_direction;
    }

    public void setBlue_direction(int blue_direction) {
        this.blue_direction = blue_direction;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCurrentAngle() {
        return this.currentAngle;
    }

    public void setCurrentAngle(int currentAngle) {
        this.currentAngle = currentAngle;
    }

    public BoardVariable task(BoardTask task) {
        setTask(task);
        return this;
    }

    public BoardVariable x(int x) {
        setX(x);
        return this;
    }

    public BoardVariable output(int output) {
        setOutput(output);
        return this;
    }

    public BoardVariable d1(Long d1) {
        setD1(d1);
        return this;
    }

    public BoardVariable d2(Long d2) {
        setD2(d2);
        return this;
    }

    public BoardVariable brightness(int brightness) {
        setBrightness(brightness);
        return this;
    }

    public BoardVariable brightArray(List<Brightness> brightArray) {
        setBrightArray(brightArray);
        return this;
    }

    public BoardVariable red(int red) {
        setRed(red);
        return this;
    }

    public BoardVariable green(int green) {
        setGreen(green);
        return this;
    }

    public BoardVariable blue(int blue) {
        setBlue(blue);
        return this;
    }

    public BoardVariable red_direction(int red_direction) {
        setRed_direction(red_direction);
        return this;
    }

    public BoardVariable green_direction(int green_direction) {
        setGreen_direction(green_direction);
        return this;
    }

    public BoardVariable blue_direction(int blue_direction) {
        setBlue_direction(blue_direction);
        return this;
    }

    public BoardVariable count(int count) {
        setCount(count);
        return this;
    }

    public BoardVariable currentAngle(int currentAngle) {
        setCurrentAngle(currentAngle);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof BoardVariable)) {
            return false;
        }
        BoardVariable boardVariable = (BoardVariable) o;
        return Objects.equals(task, boardVariable.task) && x == boardVariable.x && output == boardVariable.output && Objects.equals(d1, boardVariable.d1) && Objects.equals(d2, boardVariable.d2) && brightness == boardVariable.brightness && Objects.equals(brightArray, boardVariable.brightArray) && red == boardVariable.red && green == boardVariable.green && blue == boardVariable.blue && red_direction == boardVariable.red_direction && green_direction == boardVariable.green_direction && blue_direction == boardVariable.blue_direction && count == boardVariable.count && currentAngle == boardVariable.currentAngle;
    }

    @Override
    public int hashCode() {
        return Objects.hash(task, x, output, d1, d2, brightness, brightArray, red, green, blue, red_direction, green_direction, blue_direction, count, currentAngle);
    }

    @Override
    public String toString() {
        return "{" +
            " task='" + getTask() + "'" +
            ", x='" + getX() + "'" +
            ", output='" + getOutput() + "'" +
            ", d1='" + getD1() + "'" +
            ", d2='" + getD2() + "'" +
            ", brightness='" + getBrightness() + "'" +
            ", brightArray='" + getBrightArray() + "'" +
            ", red='" + getRed() + "'" +
            ", green='" + getGreen() + "'" +
            ", blue='" + getBlue() + "'" +
            ", red_direction='" + getRed_direction() + "'" +
            ", green_direction='" + getGreen_direction() + "'" +
            ", blue_direction='" + getBlue_direction() + "'" +
            ", count='" + getCount() + "'" +
            ", currentAngle='" + getCurrentAngle() + "'" +
            "}";
    }

    
}
