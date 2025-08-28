package com.scheduler.app.backend.Messaging.Board;
import java.util.Objects;

import org.springframework.web.socket.WebSocketSession;
// websocket session
public class BoardSession {
    // websocket session
    private WebSocketSession session;
    // session key 
    private String sessionId;
    // board associated
    private long boardId;

    public BoardSession() {
    }

    public BoardSession(WebSocketSession session, String sessionId, long boardId) {
        this.session = session;
        this.sessionId = sessionId;
        this.boardId = boardId;
    }

    public WebSocketSession getSession() {
        return this.session;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public long getBoardId() {
        return this.boardId;
    }

    public void setBoardId(long boardId) {
        this.boardId = boardId;
    }

    public BoardSession session(WebSocketSession session) {
        setSession(session);
        return this;
    }

    public BoardSession sessionId(String sessionId) {
        setSessionId(sessionId);
        return this;
    }

    public BoardSession boardId(long boardId) {
        setBoardId(boardId);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof BoardSession)) {
            return false;
        }
        BoardSession boardSession = (BoardSession) o;
        return Objects.equals(session, boardSession.session) && Objects.equals(sessionId, boardSession.sessionId) && boardId == boardSession.boardId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(session, sessionId, boardId);
    }

    @Override
    public String toString() {
        return "{" +
            " session='" + getSession() + "'" +
            ", sessionId='" + getSessionId() + "'" +
            ", boardId='" + getBoardId() + "'" +
            "}";
    }
    
}
