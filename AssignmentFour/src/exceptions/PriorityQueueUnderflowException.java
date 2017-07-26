package exceptions;

/**
 * Created by REA on 7/16/2017.
 */
public class PriorityQueueUnderflowException extends RuntimeException{

    public PriorityQueueUnderflowException(){
        super();
    }

    public PriorityQueueUnderflowException(String message){
        super(message);
    }
}
