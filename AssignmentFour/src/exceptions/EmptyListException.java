package exceptions;

/**
 * Created by REA on 6/18/2017.
 */
public class EmptyListException extends RuntimeException {
    public EmptyListException(){
        super();
    }

    public EmptyListException(String message){
        super(message);
    }
}
