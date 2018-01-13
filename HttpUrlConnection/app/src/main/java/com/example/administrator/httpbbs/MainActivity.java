package com.example.administrator.httpbbs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity implements BoardLoader.CallBack {

    RecyclerView recyclerView;

    BoardAdapter adapter;

    String tempUrl = "http://192.168.10.85:8080/bbs/list";  // 1. 첫번째는 어떤 데이터를 얻어오는지 명확하지가 않다.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1.
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // 2.
        BoardLoader loader = new BoardLoader();
        loader.getData(tempUrl, this);

        // 3.
        adapter = new BoardAdapter();

        // 4.
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WriteActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void setData(List<Board> list) {

        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }






    //    public void newTask(String url){
//
//        new AsyncTask<String, Void, String>(){
//
//            @Override
//            protected String doInBackground(String... params) {
//
//                loadData(params[0]);
//
//                return null;
//            }
//
//        }.execute(url);
//
//
//    }
//
//
//    public void loadData(String url){
//        data = new ArrayList<>();
//
//        try {
//            // 1. 권한 설정
//
//            // 2. URL
//            URL serverUrl = new URL(url);
//
//            // 3. 커넥션
//            HttpURLConnection connection = (HttpURLConnection) serverUrl.openConnection();
//
//            connection.setRequestMethod("GET");
//
//            // 4. 쿼리
//            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                InputStream is = connection.getInputStream();
//                BufferedReader br = new BufferedReader(new InputStreamReader(is));
//                String temp = "";
//
//                while((temp = br.readLine()) != null){
////                    Board board = new Board();
////                    String[] split = temp.split("|");
////                    board.no = split[0];
////                    board.title = split[1];
////                    board.author = split[2];
////                    board.content = split[3];
////                    board.date = split[4];
////                    data.add(board);
//                }
//
//                Gson gson = new Gson();
//                Board board = gson.fromJson(temp, Board.class);
//
//            }
//
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }

}
