package com.scheduler.app.backend.InterfaceModels.Input;

import java.util.Objects;

import com.scheduler.app.backend.aREST.Function.Models.Mode;

public class ModeInput {

    private Mode mode;
    
    private ModeParameterInput[] params;


    public ModeInput() {
    }

    public ModeInput(Mode mode, ModeParameterInput[] params) {
        this.mode = mode;
        this.params = params;
    }

    public Mode getMode() {
        return this.mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public ModeParameterInput[] getParams() {
        return this.params;
    }

    public void setParams(ModeParameterInput[] params) {
        this.params = params;
    }

    public ModeInput mode(Mode mode) {
        setMode(mode);
        return this;
    }

    public ModeInput params(ModeParameterInput[] params) {
        setParams(params);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ModeInput)) {
            return false;
        }
        ModeInput modeInput = (ModeInput) o;
        return Objects.equals(mode, modeInput.mode) && Objects.equals(params, modeInput.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mode, params);
    }

    @Override
    public String toString() {
        return "{" +
            " mode='" + getMode() + "'" +
            ", params='" + getParams() + "'" +
            "}";
    }
    
}
