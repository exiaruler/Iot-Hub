package com.scheduler.app.backend.Messaging.Board.Models;
import java.util.Objects;
// used for board login response
public class BoardLogin extends DeviceCheck{
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

    public BoardLogin(boolean devMode, String devServerUrl, String devWsUrl, boolean loginServerFail) {
        this.devMode = devMode;
        this.devServerUrl = devServerUrl;
        this.devWsUrl = devWsUrl;
        this.loginServerFail = loginServerFail;
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
        return devMode == boardLogin.devMode && Objects.equals(devServerUrl, boardLogin.devServerUrl) && Objects.equals(devWsUrl, boardLogin.devWsUrl) && loginServerFail == boardLogin.loginServerFail;
    }

    @Override
    public int hashCode() {
        return Objects.hash(devMode, devServerUrl, devWsUrl, loginServerFail);
    }

    @Override
    public String toString() {
        return "{" +
            " devMode='" + isDevMode() + "'" +
            ", devServerUrl='" + getDevServerUrl() + "'" +
            ", devWsUrl='" + getDevWsUrl() + "'" +
            ", loginServerFail='" + isLoginServerFail() + "'" +
            "}";
    }

    
}
