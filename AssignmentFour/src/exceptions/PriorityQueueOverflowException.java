package exceptions;

/**
 * Created by REA on 7/16/2017.
 */
public class PriorityQueueOverflowException extends RuntimeException {

    public PriorityQueueOverflowException(){
        super();
    }

    public PriorityQueueOverflowException(String message){
        super(message);
    }
}
