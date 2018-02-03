package com.stackme.thewire.syllorg;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by suyog on 02/02/18.
 */

@Entity
public class College{

    @PrimaryKey
    String uid;

    @ColumnInfo(name = "college_name")
    String college_name;

    @ColumnInfo(name = "college_id")
    String college_id;

    @ColumnInfo(name = "University")
    String university = null;

    public void setCollege_name(String college_name) {
        this.college_name = college_name;
    }

    public String getCollege_name() {
        return college_name;
    }

    public void setCollege_id(String college_id) {
        this.college_id = college_id;
    }

    public String getCollege_id() {
        return college_id;
    }

    public String getUid() {
        return uid;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return college_name;
    }
}
