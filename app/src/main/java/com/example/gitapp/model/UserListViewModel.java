package com.example.gitapp.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gitapp.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserListViewModel extends ArrayAdapter<GitUser> implements View.OnClickListener {
    private int resource;

    public UserListViewModel(@NonNull Context context, int resource, List<GitUser> data) {
        super(context, resource,data);
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listVItem=convertView;
        if(listVItem==null){
            listVItem= LayoutInflater.from(getContext()).inflate(resource, parent,false);
        }
        CircleImageView imageView=listVItem.findViewById(R.id.imageView);
        TextView textViewlogin=listVItem.findViewById(R.id.editTextlogin);
        TextView textViewScore=listVItem.findViewById(R.id.editTextScore);
        textViewlogin.setText(getItem(position).login);
        textViewScore.setText(String.valueOf(getItem(position).score));
        new Thread(()-> {
            try {
                URL url=new URL(getItem(position).avatarUrl);
                Bitmap bitmap= BitmapFactory.decodeStream(url.openStream());
                ((Activity)getContext()).runOnUiThread(()->{
                    imageView.setImageBitmap(bitmap);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        return listVItem;
    }


    @Override
    public void onClick(View v) {
        int pos = Integer.parseInt(v.getTag().toString());
        Toast.makeText(getContext(),"Item in position " + pos + " clicked",Toast.LENGTH_LONG).show();
    }
}
