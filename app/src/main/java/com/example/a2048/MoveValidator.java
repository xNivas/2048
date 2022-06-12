package com.example.a2048;

public class MoveValidator {
    //helper class to know if in one of methods "modifyRow.." or "modifyColumn" any action was performed
    //it is used to check if at least one action per move was done. if not - then not newNumber generated

    private boolean moveDone;

    public MoveValidator() {
    }

    public MoveValidator(boolean moveDone) {
        this.moveDone = moveDone;
    }

    public boolean isMoveDone() {
        return moveDone;
    }

    public void setMoveDone(boolean moveDone) {
        this.moveDone = moveDone;
    }

}
