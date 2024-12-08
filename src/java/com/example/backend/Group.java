package com.example.backend;

import java.util.HashSet;
import java.util.Set;

public class Group {
    private String groupName;
    private int groupId;
    private Set<Integer>set=new HashSet<Integer>();
    public Group(String groupName) {//新建一个组
        this.groupName = groupName;
        this.groupId = -1;
    }
}
