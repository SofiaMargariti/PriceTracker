package com.example.sofia.pricetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

/**
 * Created by sofia on 7/1/15.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "products.db";
    public static final String TABLE_NAME = "product";
    public static final String COLUMN_NAME_PRODUCT_ID = "id";
    public static final String COLUMN_NAME_SKU_ID = "sku_id";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_MIN_PRICE = "min_price";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table products " +
                        "(id integer primary key, name text, sku_id text, min_price float)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean insertProduct(String name, String sku_id, Double min_price){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("sku_id", sku_id);
        contentValues.put("min_price", min_price);
        db.insert("products", null, contentValues);
        return true;
    }

    public Integer deleteProduct(Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("products",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllCotacts()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from products", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(COLUMN_NAME_PRODUCT_ID)));
            res.moveToNext();
        }
        return array_list;
    }
}
