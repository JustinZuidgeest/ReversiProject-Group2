package Games.Controllers;

public class ERRException extends Exception{
    private String message;

    public ERRException(String message){
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
