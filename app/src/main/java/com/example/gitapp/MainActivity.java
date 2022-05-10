package com.example.gitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.gitapp.model.GitUser;
import com.example.gitapp.model.GitUserdResponse;
import com.example.gitapp.model.UserListViewModel;
import com.example.gitapp.service.GitRespoServiceAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    List<GitUser> data=new ArrayList<>();
    public static final String USER_LOGIN_PARAM="user.login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        final EditText editText=findViewById(R.id.editText);
        Button btnSearch=findViewById(R.id.buttonSearch);
        ListView listV=findViewById(R.id.listViewUsers);
        UserListViewModel listViewModel=new UserListViewModel(this,R.layout.users_list_view_layout,data);
        listV.setAdapter(listViewModel);
        final Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        btnSearch.setOnClickListener(view -> {
            String query=editText.getText().toString();
            //Log.i("",query);
            GitRespoServiceAPI gitRespoServiceAPI=retrofit.create(GitRespoServiceAPI.class);
            Call<GitUserdResponse> call=gitRespoServiceAPI.search(query);
            call.enqueue(new Callback<GitUserdResponse>() {
                @Override
                public void onResponse(Call<GitUserdResponse> call, Response<GitUserdResponse> response) {
                    Log.i("",call.request().url().toString());
                    if(!response.isSuccessful()){
                        Log.i("indo",String.valueOf(response.code()));
                        return;
                    }
                    GitUserdResponse gitUserdResponse=response.body();
                    data.clear();
                    for (GitUser user:gitUserdResponse.users) {
                        data.add(user);
                    }
                    listViewModel.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<GitUserdResponse> call, Throwable throwable) {
                    Log.i("error", "Error");
                }
            });
        });
        listV.setOnItemClickListener((adapterView, view, position, l) -> {
            Log.i("info","you arrive this step");
            String login=data.get(position).login.toString();
            Log.i("info",login);
            Intent intent=new Intent(getApplicationContext(), RepositoryActivity.class);
            intent.putExtra(USER_LOGIN_PARAM,login);
            startActivity(intent);
        });
    }
}