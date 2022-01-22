package org.techtown.test8;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    final static String DB_NAME = "PlayList.db";  //DB이름

    //생성자
    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + "PlayList" + "(Name TEXT PRIMARY KEY NOT NULL, Cover INTEGER  NOT NULL,Favorite TEXT)");
        db.execSQL("create table " + "MusicList" + "(Title TEXT PRIMARY KEY NOT NULL, Singer TEXT)");
        db.execSQL("create table " + "SavedList" + "(Num INTEGER PRIMARY KEY AUTOINCREMENT, PlayListName STRING NOT NULL ,MusicName STRING NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);

    }

    public boolean insertData(String TABLE_NAME, String name, int cover){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name",name);
        contentValues.put("Cover",cover);
        long result = db.insert(TABLE_NAME, null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertMusicData(String TABLE_NAME, String title, String singer){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Title",title);
        contentValues.put("Singer",singer);
        long result = db.insert(TABLE_NAME, null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    //데이터베이스 항목 읽어오기 Read
    public Cursor getAllData(String tabelName){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+tabelName,null);
        return  res;
    }

    // 데이터베이스 삭제하기
    public Integer deleteData(String TABLE_NAME,String name){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "Name = ? ",new String[]{name});
    }

    //데이터베이스 수정하기
    public boolean updateData(String TABLE_NAME,String name, String favorite){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name",name);
        contentValues.put("Favorite",favorite);
        db.update(TABLE_NAME,contentValues,"Name = ?", new String[] { name });
        return true;
    }
}