package com.nguyenhoangviet.vietnguyen.core.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by viet on 6/24/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyImages.db";
    public static final String IMAGES_TABLE_NAME = "images";
    public static final String IMAGES_COLUMN_ID = "id";
    public static final String IMAGES_COLUMN_TITLE = "title";
    public static final String IMAGES_COLUMN_DESCRIPTION = "description";
    public static final String IMAGES_COLUMN_PATH = "path";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table images (id integer primary key, title text,description text,path text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS images");
        onCreate(db);
    }

    public boolean insertImage  (String title, String description, String path) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("description", description);
        contentValues.put("path", path);
        db.insert("images", null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from images where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, IMAGES_TABLE_NAME);
        return numRows;
    }

    public boolean updateImage (Integer id, String title, String description, String path) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("description", description);
        contentValues.put("path", path);
        db.update("images", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteImage (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("images",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

//    public ArrayList<ImageEntity> getAllImages()
//    {
//        ArrayList<ImageEntity> array_list = new ArrayList<ImageEntity>() ;
//
//        //hp = new HashMap();
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "select * from images", null );
//        res.moveToFirst();
//
//        while(res.isAfterLast() == false){
//            ImageEntity imageEntity = new ImageEntity(
//                    res.getString(res.getColumnIndex(IMAGES_COLUMN_TITLE)),
//                    res.getString(res.getColumnIndex(IMAGES_COLUMN_DESCRIPTION)),
//                    res.getString(res.getColumnIndex(IMAGES_COLUMN_PATH))
//            );
//            array_list.add(imageEntity);
//            res.moveToNext();
//        }
//        return array_list;
//    }
}