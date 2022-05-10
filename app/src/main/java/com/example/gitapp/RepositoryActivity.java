package com.example.gitapp;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gitapp.model.GitRespo;
import com.example.gitapp.service.GitRespoServiceAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RepositoryActivity extends AppCompatActivity {
    List<String> data=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository);
        Intent intent=getIntent();
        String login=intent.getStringExtra(MainActivity.USER_LOGIN_PARAM);
        setTitle("Repositories");
        TextView textViewLogin=findViewById(R.id.textViewL);
        ListView listViewR=findViewById(R.id.listV);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,data);
        listViewR.setAdapter(adapter);
        textViewLogin.setText(login);

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GitRespoServiceAPI gitRespoServiceAPI=retrofit.create(GitRespoServiceAPI.class);
        Call<List<GitRespo>> respoCall=gitRespoServiceAPI.userRepo(login);
        respoCall.enqueue(new Callback<List<GitRespo>>() {
            @Override
            public void onResponse(Call<List<GitRespo>> call, Response<List<GitRespo>> response) {
                if(!response.isSuccessful()){
                    Log.e("error",String.valueOf(response.code()));
                    return;
                }
                List<GitRespo> gitRespos=response.body();
                for (GitRespo gitR:gitRespos) {
                    String content="";
                    content+=gitR.id+"\n";
                    content+=gitR.name+"\n";
                    content+=gitR.language+"\n";
                    content+=gitR.size+"\n";
                    data.add(content);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<GitRespo>> call, Throwable throwable) {

            }
        });
    }
}