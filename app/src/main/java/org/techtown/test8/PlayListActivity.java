package org.techtown.test8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class PlayListActivity extends AppCompatActivity {
    private TextView textView;
    private Button btn_Back, btn_Add;
    ArrayList<String> items;
    ArrayAdapter adapter;
    String tableName = "PlayList";
    String playListTitle;
    DBHelper myDB;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);

        Intent intent = getIntent();
        playListTitle= intent.getStringExtra("name");

//        myDB = new DBHelper(this);

        textView = (TextView) findViewById(R.id.title);
        btn_Back = (Button) findViewById(R.id.btn_Back);
        btn_Add = (Button) findViewById(R.id.btn_Add);
        textView.setText(playListTitle + " 노래 목록 ");

//        ViewPlayList();

        //리스트뷰
//        items = new ArrayList<String>() ;
//        // ArrayAdapter 생성. 아이템 View를 선택(multiple choice)가능하도록 만듦.
//        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, items) ;
//
//        // listview 생성 및 adapter 지정.
//        final ListView listview = (ListView) findViewById(R.id.listView1) ;
//        listview.setAdapter(adapter) ;

        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back_btn 눌렀을시 MainActivity 로
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("name",playListTitle);
                startActivity(intent);
            }
        });

        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MusicListActivity.class);
                startActivity(intent);
            }
        });
    }
    public void ViewPlayList() {
        Cursor res = myDB.getAllData(tableName);
        while (res.moveToNext()) {
            items.add(res.getString(0) + " - " + res.getString(1));
        }
    }
}