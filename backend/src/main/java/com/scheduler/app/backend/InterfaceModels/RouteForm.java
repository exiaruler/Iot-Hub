package com.scheduler.app.backend.InterfaceModels;

import java.util.Objects;

import com.scheduler.app.backend.Command.Models.Command;
import com.scheduler.app.backend.aREST.Models.Route;

public class RouteForm extends Route {

    private Command commandRef;


    public RouteForm() {
    }

    public RouteForm(Command commandRef) {
        this.commandRef = commandRef;
    }

    public Command getCommandRef() {
        return this.commandRef;
    }

    public void setCommandRef(Command commandRef) {
        this.commandRef = commandRef;
    }

    public RouteForm commandRef(Command commandRef) {
        setCommandRef(commandRef);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof RouteForm)) {
            return false;
        }
        RouteForm routeForm = (RouteForm) o;
        return Objects.equals(commandRef, routeForm.commandRef);
    }
   


}
