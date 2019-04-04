package server_communication;

public class ERRExeption extends Exception{
    private String message;

    public ERRExeption(String message){
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
