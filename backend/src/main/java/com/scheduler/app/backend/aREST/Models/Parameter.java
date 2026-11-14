package com.scheduler.app.backend.aREST.Models;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.scheduler.Base.ModelBase.ModelBase;
import com.scheduler.app.backend.aREST.Function.Models.Mode;
// parameters for command
@Entity
public class Parameter extends ModelBase {
    
    @JsonBackReference("mode-params")
    @ManyToOne
    @JoinColumn(name="mode_id")
    private Mode mode;
    // array order
    @Column
    private int parameterOrder;
    // value
    @Column
    private String value;


    public Parameter() {
    }

    public Parameter(Mode mode, int parameterOrder, String value) {
        this.mode = mode;
        this.parameterOrder = parameterOrder;
        this.value = value;
    }

    public Mode getMode() {
        return this.mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public int getParameterOrder() {
        return this.parameterOrder;
    }

    public void setParameterOrder(int parameterOrder) {
        this.parameterOrder = parameterOrder;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Parameter mode(Mode mode) {
        setMode(mode);
        return this;
    }

    public Parameter parameterOrder(int parameterOrder) {
        setParameterOrder(parameterOrder);
        return this;
    }

    public Parameter value(String value) {
        setValue(value);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Parameter)) {
            return false;
        }
        Parameter parameter = (Parameter) o;
        return Objects.equals(mode, parameter.mode) && parameterOrder == parameter.parameterOrder && Objects.equals(value, parameter.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mode, parameterOrder, value);
    }

    @Override
    public String toString() {
        return "{" +
            " mode='" + getMode() + "'" +
            ", parameterOrder='" + getParameterOrder() + "'" +
            ", value='" + getValue() + "'" +
            "}";
    }

}
