package com.example.yb.testtalk.Menu_Insert;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yb.testtalk.R;
import com.example.yb.testtalk.fragment_insert.Infusion_Insert_detailFragment;
import com.example.yb.testtalk.fragment_insert.Patient_Insert_detailFragment;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Insert_to_PatientInfo extends AppCompatActivity {

    TextView tvData;
    EditText Pin,name,age,telephone,Protector_Name,Protector_Phone;
    Button Info_More_details, infusion_more_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_to_patient);

        Pin = (EditText) findViewById(R.id.EditText_pin);
        name = (EditText)findViewById(com.example.yb.testtalk.R.id.EditText_name);
        age = (EditText)findViewById(com.example.yb.testtalk.R.id.EditText_age);
        telephone = (EditText)findViewById(com.example.yb.testtalk.R.id.EditText_telephone);
        Protector_Name = (EditText)findViewById(com.example.yb.testtalk.R.id.EditText_Protector_Name);
        Protector_Phone = (EditText)findViewById(com.example.yb.testtalk.R.id.EditText_Protector_Phone);
        Info_More_details = (Button)findViewById(R.id.button_Info_More_details);
        infusion_more_details = (Button)findViewById(R.id.button_Infusion_detail);

        tvData = (TextView)findViewById(R.id.SendJson_Result_TextView);
        Button btn = (Button)findViewById(com.example.yb.testtalk.R.id.httpTest);

        //버튼이 클릭되면 여기 리스너로 옴
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                JSONTask task = new Insert_to_PatientInfo.JSONTask();
                task.execute(getResources().getString(R.string.register));
            }
        });

        infusion_more_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.Infusion_Insert_frameLayout,new Infusion_Insert_detailFragment()).commit();
            }
        });
        Info_More_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.Menu_Patient_Insert_frameLayout,new Patient_Insert_detailFragment()).commit();
            }
        });

    }

    public class JSONTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("id",Pin.getText().toString());
                jsonObject.accumulate("name", name.getText().toString());
                jsonObject.accumulate("age", age.getText().toString());
                jsonObject.accumulate("telephone", telephone.getText().toString());
                jsonObject.accumulate("Protector_Name", Protector_Name.getText().toString());
                jsonObject.accumulate("Protector_Phone", Protector_Phone.getText().toString());

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    URL url = new URL(urls[0]);
                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");//POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();

                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }

                    return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임

                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        if(reader != null){
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            tvData.setText(result);//서버로 부터 받은 값을 출력해주는 부
        }
    }
}