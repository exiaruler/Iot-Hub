package com.scheduler.app.backend.InterfaceModels.Output;

import com.scheduler.app.backend.aREST.Models.Board;

public class BoardResponse {
    private Board board;
    private String message;

    public BoardResponse(Board board, String message) {
        this.board = board;
        this.message = message;
    }

}
