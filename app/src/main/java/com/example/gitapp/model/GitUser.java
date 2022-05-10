package com.example.gitapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GitUser{
    public int id;
    public String login;
    @SerializedName("avatar_url")
    public String avatarUrl;
    public int score;

}
