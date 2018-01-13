package com.example.administrator.httpbbs;

import android.os.AsyncTask;
import android.util.Log;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class DataSender {

    public void sendData(String url, String jsonString, final CallBack callBack){

        new AsyncTask<String, Void, Boolean>(){

            @Override
            protected Boolean doInBackground(String... params) {
                String url = params[0];
                String jsonString = params[1];
                boolean isSuccess = sendJsonStringByUrl(url, jsonString);
                return isSuccess;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                callBack.call(result);
            }

        }.execute(url, jsonString);
    }

    private boolean sendJsonStringByUrl(String url, String jsonString){

        try {

            // 1. 서버와 연결
            URL serverUrl = new URL(url);

            HttpURLConnection connection = (HttpURLConnection) serverUrl.openConnection();

            // 2. 전송방식 결정
            connection.setRequestMethod("POST");

            // 3. 데이터 전송
            connection.setDoOutput(true);   // 전송할 데이터가 있다고 알려줌

            // node 의 경우는 그냥 json 그대로 보내주면 되지만, jsp 의 경우 키-값 의 형태도 보내줘야 함
//            String data = "jsonString="+ URLEncoder.encode(jsonString, "utf-8"); // 무조건 이 형식으로 모든 서버가 동작한다.
            String data = "\"jsonString\":"+jsonString;
            Log.e("전송할 데이터", data);
            // 4. 전송결과 체크
            OutputStream os = connection.getOutputStream();
//                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
//                bw.write(data);
            os.write(data.getBytes());  // 네트워크에서 사용하는 타입이 바이트라서 바이트로 바꿔주는 것이로군
            os.flush();                 // 버터가 가득차지 않았어도 데이터를 전송하기 위해서 호출한다.
            os.close();

            int responseCode = connection.getResponseCode();
            if ( responseCode == HttpURLConnection.HTTP_OK) {
                return true;        // 리턴 밑으로는 실행되지 않기 때문에 실행이 되지 않을 경우 false 로만 가는 것임.
            } else {
                Log.e("HttpError", "errorCode=" + responseCode);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


    public interface CallBack {
        void call(boolean result);
    }

}
