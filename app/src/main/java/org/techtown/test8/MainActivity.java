package org.techtown.test8;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ListViewAdapter.ListBtnClickListener {
    ListView listview;
    ListViewAdapter adapter;
    ArrayList<PlayListItem> items;
    ArrayList<String> checkList;
    DBHelper myDB;
    String tableName = "PlayList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //디비 생산
        myDB = new DBHelper(this);

        TextView textView = (TextView) findViewById(R.id.textView1);
        checkList = new ArrayList<String>();
        items = new ArrayList<PlayListItem>();



        initialSetting();
        // items 로드.
//        loadItemsFromDB(items);
//
//        // Adapter 생성
//        adapter = new ListViewAdapter(this, R.layout.listview_btn_item, items, this);

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listView);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                PlayListItem item = (PlayListItem) adapter.getItem(position);
                String a = item.getText();
                Toast.makeText(getApplicationContext(), "선택 :"+ a, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), PlayListActivity.class);
                intent.putExtra("name",a);
                startActivity(intent); }
        });

        Button button = (Button) findViewById(R.id.btn_dialog);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
                ad.setIcon(R.mipmap.ic_launcher);
                ad.setTitle("플레이리스트");
                ad.setMessage("플레이리스트 제목을 입력바랍니다.");

                final EditText et = new EditText(MainActivity.this);
                ad.setView(et);

                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String title = et.getText().toString();

                        checkDB(title);
                        if(checkDB(title) == true) {
                            createList(items, title);
                            Toast.makeText(getApplicationContext(), "플레이 리스트 " + title + "가 생성되었습니다.", Toast.LENGTH_LONG).show();
                            ViewPlayList();
                        }

                        else
                        {
                            Toast.makeText(getApplicationContext(), "이미 존재하는 플레이 리스트 입니다.", Toast.LENGTH_LONG).show();
                        }
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
    }

    public boolean createList(ArrayList<PlayListItem> list, String title) {
        PlayListItem item;

        if (list == null) {
            list = new ArrayList<PlayListItem>();
        }

        // 아이템 생성.
        item = new PlayListItem();
        item.setIcon(ContextCompat.getDrawable(this, R.drawable.cover));
        item.setText(title);
        list.add(item);
        checkList.add(title);
        myDB.insertData(tableName, title, R.drawable.cover);
        return true;
    }

    public boolean loadItemsFromDB(ArrayList<PlayListItem> list,String title) {
        PlayListItem item;

        if (list == null) {
            list = new ArrayList<PlayListItem>();
        }

        // 아이템 생성.
        item = new PlayListItem();
        item.setIcon(ContextCompat.getDrawable(this, R.drawable.cover));
        item.setText(title);
        list.add(item);
        checkList.add(title);
        return true;
    }

    @Override
    public void onListBtnClick(int position) {
        AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
        ad.setIcon(R.mipmap.ic_launcher);
        ad.setTitle("수정 및 삭제");
        ad.setMessage("수정할 플레이리스트 제목을 입력바랍니다.");

        final EditText et = new EditText(MainActivity.this);
        ad.setView(et);

        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PlayListItem item = (PlayListItem) adapter.getItem(position);
                String title = et.getText().toString();
                item.setText(title);
                adapter.notifyDataSetChanged();
                myDB.updateData(tableName, title,null);
                Toast.makeText(getApplicationContext(), "수정 완료", Toast.LENGTH_LONG).show();
            }
        });

        ad.setNeutralButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        ad.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PlayListItem item = (PlayListItem) adapter.getItem(position);

                myDB.deleteData(tableName,item.getText().toString());
                adapter.remove(adapter.getItem(position));

                Toast.makeText(getApplicationContext(), "삭제 완료", Toast.LENGTH_LONG).show();

                //왜 바로 초기화가 안되지???....어뮬에서만 안보이는거였네...?
                adapter.notifyDataSetChanged();
            }
        });
        ad.show();
    }

    //뷰 보여주기.
    public void ViewPlayList() {
        Cursor res = myDB.getAllData(tableName);
        while (res.moveToNext()) {
            if (!checkList.contains(res.getString(0))) {
                createList(items, res.getString(0));
                adapter = new ListViewAdapter(getApplicationContext(), R.layout.listview_btn_item, items, MainActivity.this);
                //리스트 갱신
                adapter.notifyDataSetChanged();
            }
        }
    }
//DB중복체크
    public boolean checkDB(String name) {
        int count = 0;
        Cursor res = myDB.getAllData(tableName);
        while (res.moveToNext()) {
            if (checkList.contains(name)) {
                count++;
            }
        }
        if (count == 0) {
            return true;
        }
        else
            return false;

    }

    public void initialSetting()
    {
        Cursor res = myDB.getAllData(tableName);
        while(res.moveToNext()){
            loadItemsFromDB(items, res.getString(0));
            adapter = new ListViewAdapter(getApplicationContext(), R.layout.listview_btn_item, items, MainActivity.this);
        }
        adapter.notifyDataSetChanged();
    }
}