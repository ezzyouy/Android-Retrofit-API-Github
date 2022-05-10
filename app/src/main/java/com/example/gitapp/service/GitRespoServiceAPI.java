package com.example.gitapp.service;

import com.example.gitapp.model.GitRespo;
import com.example.gitapp.model.GitUserdResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitRespoServiceAPI {
    @GET("search/users")
    public Call<GitUserdResponse> search(@Query("q") String query);
    @GET("users/{u}/repos")
    public Call<List<GitRespo>> userRepo(@Path("u") String login);
}
