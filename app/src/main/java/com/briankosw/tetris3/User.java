package com.briankosw.tetris3;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by briankosw on 4/18/2017.
 */

public class User {
    public String email;
    public String name;
    public HashMap<String, Boolean> friendList;
    public HashSet<String> pendingList;
    public int score;

    public User(){

    }

    public User(String email, String name, HashMap friendList, HashSet pendingList, int score) {
        this.email = email;
        this.name = name;
        this.friendList = friendList;
        this.pendingList = pendingList;
        this.score = score;
    }
}
