package fii.practic.health.boundry.exceptions;

public class NotFoundException extends Exception{

    public NotFoundException(String message){
        super(message);
    }

    public NotFoundException(String message, Throwable throwable){
        super(message, throwable);
    }


}
