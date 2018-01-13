package com.example.administrator.networkbasic_versioncheck;

import android.app.ProgressDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new HttpAsyncTask().execute(getCurVersion());

    }

    // 1. 현재 버전 얻어오기
    private int getCurVersion(){
        int curVersion = 0;
        PackageInfo packageInfo = null;

        try{
            // PackageInfo 클래스 얻어오기
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            curVersion = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            return 0;
        }
        return curVersion;
    }


    // 2. 요청 메소드 만들기
    private int request(int curVersion){
        final int TIME_OUT = 20; // 클라이언트가 요청한 후 일정 시간이 지나면 CONNECTION 을 끊어준다. 이게 timeout 설정
        final String GET_METHOD = "GET"; // 방식 설정

        final String SERVER_URL = "http://192.168.10.85:8080/LocalServerPgm/newVersion.jsp";
        final String QUERY_STR = "?CURRENT_VERSION=";

        int result =  0;
        String urlStr = null;

        URL url = null;
        HttpURLConnection httpConnection = null;

        try{
            // 전송할 데이터
            urlStr = SERVER_URL + QUERY_STR + curVersion;
            // 2.1 URL 객체 생성
            url = new URL(urlStr);  // 주소값 : 인터넷 상 단 하나밖에 없는 서버의 주소 가져오는것
            // 2.2 HttpURLConnection 객체 만들기
            httpConnection = (HttpURLConnection) url.openConnection();
            // 2.3 설정 정보
            httpConnection.setConnectTimeout(TIME_OUT *  1000);  // 20초 동안 연결되지 않으면 끊겠다.
            httpConnection.setReadTimeout(TIME_OUT * 1000); // 읽어들이는데 20초 걸리면 끊어내겠다.
            httpConnection.setRequestMethod(GET_METHOD);  // GET 또는 POST 방식 지정
            httpConnection.connect(); // 서버에 접속 요청

            // 3.1 요청에 대한 응답 코드를 받아온다.
            int stateCode = httpConnection.getResponseCode();

            // 3.1 200~299 이면 OK, 나머지는 에러를 리턴
            if(stateCode < 200 || stateCode >= 300){
                return -1;
            }

            // 3.2 서버에서 보내온 데이터를InputStream 에서 String 값으로 읽어오기
            if(stateCode == httpConnection.HTTP_OK) {
                StringBuffer stringBuffer = new StringBuffer();
                InputStream is = httpConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String data = null;
                while ((data = br.readLine()) != null) {
                    stringBuffer.append(data + "\n");
                }


                // 3.3 닫아주기
                br.close();
                is.close();

                // 얻어온 값이 없으면 다시 오류 처리
                if (stringBuffer == null || stringBuffer.length() == 0) {
                    return -1;
                }

                // 4. 응답데이터에서 최신 정보를 추출하기
                String[] verStr = stringBuffer.toString().trim().split("=");
                result = Integer.valueOf(verStr[1]);
            }

        } catch (Exception e){
            e.printStackTrace();
            return -1;  // 오류가 났을 경우에 리턴 값을 -1 로 리턴한다.
        }

        return result;
    }


    // 3. 스레드로 실행해주기기
    public class HttpAsyncTask extends AsyncTask<Integer, Integer, Integer> {

        private ProgressDialog waitDialog = null;
        private int curVersion = 0;
        private int newVersion = 0;

        // 백그라운드 작업을 하기 전에 호출 : 초기화 작업 할 때 사용
        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            // 버전확인 대화상자를 띄워줌
            waitDialog = new ProgressDialog(MainActivity.this);
            waitDialog.setMessage("최신 버전 확인중...");
            waitDialog.show();
        }

        // 백그라운드 작업을 수행하는 메소드
        @Override
        protected Integer doInBackground(Integer ... arg){
            curVersion = arg[0].intValue();

            // 서버에서 응답해 온 최신 버전 정보를 얻어온다
            newVersion = request(curVersion);
            return newVersion;
        }

        @Override
        protected void onProgressUpdate(Integer...values){
            super.onProgressUpdate(values);
        }

        // 백그라운드 작업이 끝나면 호출되는 메소드
        @Override
        protected void onPostExecute(Integer result){

            // onPreExecute 에서 띄워놓은 waitDialog 를 제거한다
            if(waitDialog != null){
                waitDialog.dismiss();
                waitDialog = null;
            }

            int res = 0;

            int newVersion = result.intValue();
            if(newVersion > 0){
                TextView curVerTex = (TextView) findViewById(R.id.textView);
                curVerTex.setText("단말기에 설치된 버전은 ver"+curVersion+ " 입니다");
                TextView newVerTxt = (TextView) findViewById(R.id.textView2);
                newVerTxt.setText("현재 최신 버전은 ver"+newVersion+" 입니다");

                if(curVersion < newVersion){
                    res = 1;
                } else {
                    res = 0;
                }
            } else {
                res  = -1;
            }

            TextView displayTxt = (TextView) findViewById(R.id.textView3);

            switch (res){
                case -1:
                    displayTxt.setText("오류로 인해 앱의 업그레이드 버전을 확인할 수 없습니다");
                    break;
                case 0:
                    displayTxt.setText("현재 앱의 버전을 사용하고 있습니다");
                    break;
                case 1:
                    displayTxt.setText("최신 버전이 있습니다...앱을 업데이트 후 사용하시기 바랍니다");
                    break;
                default:
                    break;
            }

        }

    }


}
