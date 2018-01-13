package com.example.administrator.networkbasic_post_image;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final int REQ_CODE = 101;
    private String mImgPath = null;
    private String mImgTitle = null;
    private String mImgOrientation = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_sel = (Button) findViewById(R.id.button);
        btn_sel.setOnClickListener(this);

        Button btn_load = (Button) findViewById(R.id.button2);
        btn_load.setOnClickListener(this);


    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                getGallery();
                break;
            case R.id.button2:
                new HttpAsyncTask().execute(mImgPath, mImgTitle, mImgOrientation);
                break;
            default:
                break;
        }
    }

    // Gallery 호출
    private void getGallery() {
        Intent intent = null;

        // 킷캣 이상 버전에서 이미지 가져오기 인텐트
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            intent.setType("images/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
            // 킷캣 미만 버전
        } else {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        }
        intent.setType("image/*"); // 이미지를 불러올 때 그 타입을 지정해 주는 듯 하다.
        startActivityForResult(intent, REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData(); // 이미지 주소값 얻어왔다.
                getImageInfo(uri);
                try {
                    Bitmap bMap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    ImageView imgView = (ImageView) findViewById(R.id.imageView);
                    imgView.setImageBitmap(bMap);
                } catch (Exception e) {

                }
            }

        }
    }

    private void getImageInfo(Uri uri) {
        String[] imgData = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.TITLE,
                MediaStore.Images.Media.ORIENTATION
        };

        Cursor cursor = getContentResolver().query(uri, imgData, null, null, null);
        cursor.moveToFirst();

        int columnData = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        int columnTitle = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
        int columnOrientation = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.ORIENTATION);

        mImgPath = cursor.getString(columnData);
        mImgTitle = cursor.getString(columnTitle);
        mImgOrientation = cursor.getString(columnOrientation);

    }

    private int uploadimgInfo(HashMap<String, String> param) {
        int result = 0;
        String responseData = "";
        final int TIME_OUT = 20;
        final String POST_METHOD = "POST";
        URL url = null;
        HttpURLConnection httpConnection = null;
        final String SVR_URL = "http://192.168.10.85:8080/LocalServerPgm/uploadPgm.jsp";


        try {
            // URL 객체 생성
            url = new URL(SVR_URL);
            // URL 을 이용한 HttpURLConnection 객체 생성
            httpConnection = (HttpURLConnection) url.openConnection();
            // 연결 만료 시간 설정
            httpConnection.setConnectTimeout(TIME_OUT);
            // 읽기 만료 시간 설정
            httpConnection.setReadTimeout(TIME_OUT);
            // 전송 방식 설정
            httpConnection.setRequestMethod(POST_METHOD);
            // Url encoded 방식으로 인코딩 후 전송
            httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 이미지를 write 하기(앱에서 가져온 데이터를 서버에 보내주기)
            OutputStream os = httpConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            bw.write(getPostDataString(param));
            bw.flush();
            bw.close();
            os.close();
//            // 서버에 연결 요청
//            httpConnection.connect();

            int stateCode = httpConnection.getResponseCode();

            // stateCode 확인
            if (stateCode >= 200 && stateCode < 300) {
                InputStream is = httpConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuffer stringBuffer = new StringBuffer();
                while ((responseData = br.readLine()) != null) {
                    stringBuffer.append(responseData);
                    stringBuffer.append("\n");
                }
                br.close();
                is.close();

                if (stringBuffer != null && stringBuffer.length() != 0) {
                    if (stringBuffer.toString().trim().equals("0")) {
                        result = 0; // 전송 성공
                    } else {
                        result = -1; // 전송 실패
                    }
                } else {
                    result = -1;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = -1;
        } finally {
            httpConnection.disconnect();
        }

        return result;
    }

    private String getPostDataString(HashMap<String, String> params) {

        boolean isFirst = true;
        StringBuilder result = new StringBuilder();

        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (isFirst == true) {
                    isFirst = false;
                } else {
                    result.append("&");
                }
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString();
    }


    public class HttpAsyncTask extends AsyncTask<String, Integer, Integer> {
        private String mImgPath = null;
        private String mImgTitle = null;
        private String mImgOrientation = null;

        private ProgressDialog waitDialog = null;

        @Override
        protected void onPreExecute() {
            waitDialog = new ProgressDialog(MainActivity.this);
            waitDialog.setMessage("이미지 전송 중...");
            waitDialog.show();
        }

        @Override
        protected Integer doInBackground(String... arg) {

            mImgPath = arg[0];
            mImgTitle = arg[1];
            mImgOrientation = arg[2];

            HashMap<String, String> params = new HashMap<>();
            params.put("imageFilePath", mImgPath);
            params.put("imageFileTitle", mImgTitle);
            params.put("imageFileOrientation", mImgOrientation);

            int result = uploadimgInfo(params);
            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected void onPostExecute(Integer result) {

            if (waitDialog != null) {
                waitDialog.dismiss();
                waitDialog = null;
            }
            if (result == null) {
                return;
            }
            if (result.intValue() == 0) {
                Toast.makeText(getApplicationContext(), "이미지 전송 성공", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "이미지 전송 실패패", Toast.LENGTH_SHORT).show();

            }
        }


    }

}

