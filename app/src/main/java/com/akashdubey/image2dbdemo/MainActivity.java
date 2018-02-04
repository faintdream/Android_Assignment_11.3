package com.akashdubey.image2dbdemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class MainActivity extends AppCompatActivity {


    EditText name,age;
    ImageView empPhoto;
    SQLiteDatabase db;
    Context context=this;
    String tmpName,tmpAge;
    byte[] byteImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name=(EditText)findViewById(R.id.nameET);
        age=(EditText)findViewById(R.id.ageET);
        empPhoto=(ImageView) findViewById(R.id.photoIV);


        // here we get a specific path to the directory which is only open to our app and nothing else
        File dbDir=this.getDir(this.getClass().getName(),Context.MODE_PRIVATE);

        // creates the db file in the location passed
        File dbFile=new File(dbDir,"emp.db");

        db=SQLiteDatabase.openOrCreateDatabase(dbFile,null);
        db.execSQL("create table if not exists personal_data ( _id integer primary key autoincrement,name text," +
                "age text, photo blob);");

        tmpName ="Akash Dubey";
        tmpAge="35";

        //i transferred the file profile.jpg to /mnt/sdcard using ddms tool
        // using a bitmap variable  tos tore the file and callling decodeFile method of BitmpaFactory
        Bitmap tmpPhoto= BitmapFactory.decodeFile("/mnt/sdcard/profile.jpg");

        // we create a ByteArrayOutputStream and compress the actual bitmap to the stream
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        tmpPhoto.compress(Bitmap.CompressFormat.JPEG,100,stream);

        // storing the stream content into the Byte array, we needed to convert this as sqlite can only handle the byte array
        byteImage=stream.toByteArray();
//        insertData();
        showData();






    }

    //method to store content to db
    void insertData(){
        ContentValues value=new ContentValues();
        value.put("name",tmpName);
        value.put("age",tmpAge);
        value.put("photo",byteImage);
        db.insert("personal_data",null,value);
    }

 // method to fetch content from db
    void showData(){
        Cursor cursor=db.query("personal_data", new String[]{"name","age","photo"},null,null,null,null,null);
        cursor.moveToFirst();
        name.setText(cursor.getString(cursor.getColumnIndex("name")));
        age.setText(cursor.getString(cursor.getColumnIndex("age")));
        byteImage=cursor.getBlob(cursor.getColumnIndex("photo"));

        Bitmap bitmap=BitmapFactory.decodeByteArray(byteImage,0,byteImage.length);
        empPhoto.setImageBitmap(bitmap);

    }
}
