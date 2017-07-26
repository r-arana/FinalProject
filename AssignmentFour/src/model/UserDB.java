package model;

import structures.SortedList;

/**
 * Created by REA on 6/4/2017.
 *
 * The logic was in our assignment guide videos.
 * This class is used to make and pass around access to the data structure containing our users.
 *
 */
public class UserDB {

    private static SortedList<User> users = new SortedList<User>();

    public static SortedList<User> getUsers(){
        return users;
    }

    public static void setUsers(SortedList<User> users){
        UserDB.users = users;
    }

}
