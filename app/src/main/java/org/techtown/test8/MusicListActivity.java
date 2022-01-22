package org.techtown.test8;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MusicListActivity extends AppCompatActivity {
    private Button btn_Back1, btn_Add1;
    ArrayList<String> items;
    ArrayAdapter adapter;
    String tableName = "SavedList";
    String playListTitle ;
    DBHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        myDB = new DBHelper(this);

        Intent intent = getIntent();
        playListTitle= intent.getStringExtra("name");

        btn_Back1 = (Button) findViewById(R.id.btn_Back1);
        btn_Add1 = (Button) findViewById(R.id.btn_Add1);

        //리스트뷰
        items = new ArrayList<String>() ;
        // ArrayAdapter 생성. 아이템 View를 선택(multiple choice)가능하도록 만듦.
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, items) ;

        // listview 생성 및 adapter 지정.
        final ListView listview = (ListView) findViewById(R.id.listView1) ;
        listview.setAdapter(adapter) ;

        InitializeMusicData();

        btn_Back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = "title";
                // back_btn 눌렀을시 MainActivity 로
                Toast.makeText(getApplicationContext(), a, Toast.LENGTH_LONG).show();
                Intent intent1 = new Intent(MusicListActivity.this, PlayListActivity.class);
                startActivity(intent1);
            }
        });

        btn_Add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(MusicListActivity.this);
                ad.setIcon(R.mipmap.ic_launcher);
                ad.setTitle("노래 추가하기");
                ad.setMessage("노래 제목과 가수를 입력바랍니다.");

                EditText title = new EditText(MusicListActivity.this);
                EditText singer = new EditText(MusicListActivity.this);
                LinearLayout layout = new LinearLayout(getApplicationContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.addView(title);
                layout.addView(singer);
                ad.setView(layout);

                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String singTitle = title.getText().toString();
                        String singerName = singer.getText().toString();

                        //singerName 등에 입력을 안해도 이상한 값이 들어와 있어서 오류 처리를 못하겠음...왜지?
                        if(singerName != "" && singTitle !="") {
                            Toast.makeText(MusicListActivity.this, "노래 추가 완료", Toast.LENGTH_LONG).show();
                            items.add(singTitle + " - " + singerName);
                        }
                        else
                            Toast.makeText(MusicListActivity.this,"노래 추가 실패",Toast.LENGTH_LONG).show();
                    }
                });

                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                ad.show();
            }
        });


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(getApplicationContext(),"선택: "+i,Toast.LENGTH_LONG).show();
                listview.setItemChecked(i, true) ;
            }
        });

        Button addButton = (Button)findViewById(R.id.add) ;
        addButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                SparseBooleanArray checkedItems = listview.getCheckedItemPositions();
                int count = adapter.getCount() ;

                for (int i = count-1; i >= 0; i--) {
                    if (checkedItems.get(i)) {
                      String a =  items.get(i).toString();
                      Toast.makeText(getApplicationContext(),a,Toast.LENGTH_SHORT).show();
                    }
                }
//                myDB.insertData(tableName,playListTitle,);
                }
        }) ;

        Button modifyButton = (Button)findViewById(R.id.modify) ;
        modifyButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                AlertDialog.Builder ad = new AlertDialog.Builder(MusicListActivity.this);
                ad.setIcon(R.mipmap.ic_launcher);
                ad.setTitle("노래 수정하기");
                ad.setMessage("노래 제목과 가수를 재입력바랍니다.");

                EditText title = new EditText(MusicListActivity.this);
                EditText singer = new EditText(MusicListActivity.this);
                LinearLayout layout = new LinearLayout(getApplicationContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.addView(title);
                layout.addView(singer);
                ad.setView(layout);

                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int checked = listview.getCheckedItemPosition();
                        String singTitle = title.getText().toString();
                        String singerName = singer.getText().toString();

                        Toast.makeText(getApplicationContext(),checked,Toast.LENGTH_SHORT).show();
                        //singerName 등에 입력을 안해도 이상한 값이 들어와 있어서 오류 처리를 못하겠음...왜지?
                        if (singerName != "" && singTitle != "") {

                            Toast.makeText(MusicListActivity.this, "노래 추가 완료", Toast.LENGTH_LONG).show();
                            items.set(checked, singTitle + " - " + singerName);
                            adapter.notifyDataSetChanged();
                        } else
                            Toast.makeText(MusicListActivity.this, "노래 추가 실패", Toast.LENGTH_LONG).show();
                    }
                });

                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                ad.show();
            }
        }) ;

        Button deleteButton = (Button)findViewById(R.id.delete) ;
        deleteButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                SparseBooleanArray checkedItems = listview.getCheckedItemPositions();
                int count = adapter.getCount() ;

                for (int i = count-1; i >= 0; i--) {
                    if (checkedItems.get(i)) {
                        items.remove(i) ;
                    }
                }

                // 모든 선택 상태 초기화.
                listview.clearChoices() ;

                adapter.notifyDataSetChanged();
            }
        }) ;
    }


    //초기 음악 리스트 만들기
    public void InitializeMusicData()
    {
        items.add("밤편지 - 아이유");
        items.add("친구라도 될걸 그랬어 - 거미");
        items.add("사랑은 늘 도망가 - 이문세");
    }
}