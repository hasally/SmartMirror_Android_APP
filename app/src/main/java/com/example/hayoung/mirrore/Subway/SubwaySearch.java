package com.example.hayoung.mirrore.Subway;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.hayoung.mirrore.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class SubwaySearch extends Activity implements View.OnClickListener{
    private List<String> list;          // 데이터를 넣은 리스트변수
    private ListView listView;          // 검색을 보여줄 리스트변수
    private EditText editSearch;        // 검색어를 입력할 Input 창
    private SearchAdapter adapter;      // 리스트뷰에 연결할 아답터
    private ArrayList<String> arraylist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_subway);

        editSearch = (EditText) findViewById(R.id.editSearchSubway);
        listView = (ListView) findViewById(R.id.listView);

        // 리스트를 생성한다.
        list = new ArrayList<String>();

        //json파싱하고 리스트에 추가
        jsonParsing();
        // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
        arraylist = new ArrayList<String>();
        arraylist.addAll(list);

        // 리스트에 연동될 아답터를 생성한다.
        adapter = new SearchAdapter(list, this);

        // 리스트뷰에 아답터를 연결한다.
        listView.setAdapter(adapter);

        //아이템 클릭시 작동
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Toast.makeText(SubwaySearch.this, list.get(position), Toast.LENGTH_SHORT).show();

//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.putExtra("train_name",list.get(position));
//                startActivity(intent);


                Intent intent = getIntent();
                intent.putExtra("train_name", list.get(position));
                intent.putExtra("pre_train_name", list.get(position-1));
                intent.putExtra("next_train_name", list.get(position+1));
                setResult(RESULT_OK, intent);
                finish();

            }
        });

        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                String text = editSearch.getText().toString();
                search(text);
            }
        });


    }
    public void onClick(View view){
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//        overridePendingTransition(R.anim.slide_down,R.anim.slide_up);
    }

    // 검색을 수행하는 메소드
    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            list.addAll(arraylist);
        }
        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < arraylist.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (arraylist.get(i).toLowerCase().contains(charText))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    list.add(arraylist.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();
    }

    private void jsonParsing(){
        String json = null;
        try{
            InputStream is = getAssets().open("train_js.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            System.out.println("json buffer input: "+json);
            JSONObject root = new JSONObject(json);
            System.out.println("json root input: "+json);
            JSONArray array = root.getJSONArray("DATA");
            System.out.println("json DATA input: "+array);
            int i=0;
            int j=0;

            while (i<size){
                JSONObject arrayItem = array.getJSONObject(j);
                list.add(arrayItem.getString("line_num")+" "+arrayItem.getString("station_nm")+" ("+ arrayItem.getString("station_cd")+")");
                i++;
                j++;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //backButton 처리
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

}