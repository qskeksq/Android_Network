package com.example.administrator.httpbbs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

public class WriteActivity extends AppCompatActivity implements View.OnClickListener, DataSender.CallBack {

    EditText title, author, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_main);

        title = (EditText) findViewById(R.id.inputTitle);
        author = (EditText) findViewById(R.id.inputAuthor);
        content = (EditText) findViewById(R.id.inputContent);

        findViewById(R.id.post).setOnClickListener(this);

    }


    public void onClick(View view){
        Board board = new Board();
        board.title = title.getText().toString();
        board.author = author.getText().toString();
        board.content = content.getText().toString();
        board.date = "";

        Gson gson = new Gson();
        String jsonString = gson.toJson(board);

        Log.e("json 확인", jsonString);

        DataSender sender = new DataSender();
        String url = "http://192.168.10.85:8080/bbs/insert";
        sender.sendData( url , jsonString, this);
    }

    @Override
    public void call(boolean result) {
        Log.e("MainActivity", result+"");
    }
}
