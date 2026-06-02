package com.scheduler.app.backend.Messaging.Board.Models;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
// used for board login response
@JsonPropertyOrder({"id","devMode","devServerUrl","devWsUrl","loginServerFail",})
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public class BoardLogin extends DeviceCheck{
    // board long id/ put it into board login
    private long id;
    // dev mode
    private boolean devMode;
     // server url
    private String devServerUrl="";
    // websocket url
    private String devWsUrl="";
    // login server failed
    private boolean loginServerFail=false;


    public BoardLogin() {
    }

    public BoardLogin(long id, boolean devMode, String devServerUrl, String devWsUrl, boolean loginServerFail) {
        this.id = id;
        this.devMode = devMode;
        this.devServerUrl = devServerUrl;
        this.devWsUrl = devWsUrl;
        this.loginServerFail = loginServerFail;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isDevMode() {
        return this.devMode;
    }

    public boolean getDevMode() {
        return this.devMode;
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    public String getDevServerUrl() {
        return this.devServerUrl;
    }

    public void setDevServerUrl(String devServerUrl) {
        this.devServerUrl = devServerUrl;
    }

    public String getDevWsUrl() {
        return this.devWsUrl;
    }

    public void setDevWsUrl(String devWsUrl) {
        this.devWsUrl = devWsUrl;
    }

    public boolean isLoginServerFail() {
        return this.loginServerFail;
    }

    public boolean getLoginServerFail() {
        return this.loginServerFail;
    }

    public void setLoginServerFail(boolean loginServerFail) {
        this.loginServerFail = loginServerFail;
    }

    public BoardLogin id(long id) {
        setId(id);
        return this;
    }

    public BoardLogin devMode(boolean devMode) {
        setDevMode(devMode);
        return this;
    }

    public BoardLogin devServerUrl(String devServerUrl) {
        setDevServerUrl(devServerUrl);
        return this;
    }

    public BoardLogin devWsUrl(String devWsUrl) {
        setDevWsUrl(devWsUrl);
        return this;
    }

    public BoardLogin loginServerFail(boolean loginServerFail) {
        setLoginServerFail(loginServerFail);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof BoardLogin)) {
            return false;
        }
        BoardLogin boardLogin = (BoardLogin) o;
        return id == boardLogin.id && devMode == boardLogin.devMode && Objects.equals(devServerUrl, boardLogin.devServerUrl) && Objects.equals(devWsUrl, boardLogin.devWsUrl) && loginServerFail == boardLogin.loginServerFail;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, devMode, devServerUrl, devWsUrl, loginServerFail);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", devMode='" + isDevMode() + "'" +
            ", devServerUrl='" + getDevServerUrl() + "'" +
            ", devWsUrl='" + getDevWsUrl() + "'" +
            ", loginServerFail='" + isLoginServerFail() + "'" +
            "}";
    }

    
}
