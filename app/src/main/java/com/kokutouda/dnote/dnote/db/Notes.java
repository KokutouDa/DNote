package com.kokutouda.dnote.dnote.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.sql.Timestamp;

@Entity
public class Notes implements Serializable{
    @PrimaryKey(autoGenerate = true)
    public Integer id;

    public String title;

    public String content;

    @ColumnInfo(name = "category_id")
    public Integer categoryId;

    @ColumnInfo(name = "create_time")
    public long createTime;

    @ColumnInfo(name = "update_time")
    public long updateTime;

    @ColumnInfo(name = "delete_time")
    public long deleteTime;

    public Notes(String title, String content) {
        this.title = title;
        this.content = content;
        //todo createTime =
    }
}
