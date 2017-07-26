package model;

import java.io.*;

/**
 * Created by REA on 6/6/2017.
 * Responsible for reading from and writing to our .dat file.
 */
public class UserIO {
    /* https://docs.oracle.com/javase/7/docs/api/java/io/ObjectOutputStream.html
     *  https://docs.oracle.com/javase/7/docs/api/java/io/FileOutputStream.html
     *  I think the gist of it is that the ObjectOutputStream takes the data stream you
     *  provide and organizes it into a format that it can push to FileOutputStream.
     *  FileOutputStream then takes this stream of data and converts it into the actual
     *  file that you can access later.
     */

    public static void writeUsers(Object data) throws IOException{
        ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("users.dat"));
        output.writeObject(data);
    }

    public static Object readUsers() throws IOException, ClassNotFoundException{
        ObjectInputStream input = new ObjectInputStream(new FileInputStream("users.dat"));
        return input.readObject();
    }
}
