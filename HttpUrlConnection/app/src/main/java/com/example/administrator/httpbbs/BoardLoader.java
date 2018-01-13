package com.example.administrator.httpbbs;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class BoardLoader {


    public void getData(String url, final CallBack main){

        new AsyncTask<String, Void, List<Board>>(){

            @Override
            protected List<Board> doInBackground(String... params) {

                String json = getDataFromUrl(params[0]);
                Log.e("doInBackground", json);
                Gson gson = new Gson();
                // 사실 여기서 어떤 일이 발생하는지는 이름을 곰곰히 비교해 보면 알 수 있다.
                // 공공데이터 api 와 함께 비교해 보면 각각이 이름을 통해 하위로 접근하고, 또 그 안에서 객체가 발견되면
                // 그 안의 객체로 들어가고, 마지막까지 계속 이름으로 접근한다는 것을 알 수 있다.
                BoardData boardList = gson.fromJson(json, BoardData.class);     // json String 형태로 넘어옴
                // 보내주는 값과 이름이 다르면 인식을 못 함. 즉, BoardData 에도 bbsList 로 들어가야 일치되는 듯 하다.

//                List<Board> list = boardList.boardList;
                Log.e("doInBackground", boardList.bbsList.size()+"");
                return boardList.bbsList;
            }

            @Override
            protected void onPostExecute(List<Board> list) {
//                Log.e("onPostExecute", list.size()+"");
                main.setData(list);
            }

        }.execute(url);
    }


    public interface CallBack {

        void setData(List<Board> list);
    }



    public String getDataFromUrl(String url) { // 요청한 곳에서 예외 처리를 하도록 던져줌. 예외를 호출한 곳에서 넘겨받아 각각 팝업, 토스트, 스낵바, 뷰 등으로 처리를 결정하도록 넘겨준다.
        String result = "";

        try {

            URL serverUrl = new URL(url);

            HttpURLConnection conn = (HttpURLConnection) serverUrl.openConnection(); // url 객체에서 연결을 꺼낸다. 마치 헬퍼 클래스와 비슷한 존재

            conn.setRequestMethod("GET"); // 왜냐하면 서버에서 가져오는 것이기 때문

            int responseCode = conn.getResponseCode();
            // HTTP_OK 는 연결이 됬는지 확인해주는 작업이다. 만약 매니페스트에 인터넷 권한을 넣어주지 않으면 인터넷에 연결되지 않았기 때문에 계속 result 값이 "" 가 뜰 것이다.
            if (responseCode == HttpURLConnection.HTTP_OK) {  // 정상적인 코드 처리 -- 서버에서 정상적으로 데이터를 보내줄 수 있다

                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is)); // 문자 계열만 할 것이 아니기 때문에 Reader 지양
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    result += temp;
                }
                // 2.3 오류에 대한 응답 처리
            } else {
                // 각자 호출 측으로 Exception 을만들어서 오류 처리
                Log.e("Network", "error_code=" + responseCode);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

}
