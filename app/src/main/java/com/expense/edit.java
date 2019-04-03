package com.expense;

import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class edit extends AppCompatActivity {

    SQLiteDatabase db;
    DbHelper dbHelper;
    EditText title, amount;

    Button update;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        final long id = getIntent().getExtras().getLong(getString(R.string.row_id));

        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();

        title = findViewById(R.id.title);
        amount = findViewById(R.id.amount);
        update = findViewById(R.id.update);


















        Cursor cursor = db.rawQuery("select * from " + dbHelper.EXPENSE_TABLE_NAME + " where " + dbHelper.ID + "=" + id, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {


                this.setTitle(cursor.getString(cursor.getColumnIndex(dbHelper.TITLE)));
                title.setText(cursor.getString(cursor.getColumnIndex(dbHelper.TITLE)));
                amount.setText(cursor.getString(cursor.getColumnIndex(dbHelper.PRICE)));





            }
            cursor.close();

        }


          update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ContentValues cv = new ContentValues();
                cv.put(DbHelper.TITLE, title.getText().toString());
                cv.put(DbHelper.PRICE, amount.getText().toString());



                db.update("expense", cv, "_id=" + id, null);

                Toast.makeText(edit.this, "updated ", Toast.LENGTH_SHORT).show();



            }
        });
    }



    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(this, MainActivity.class);
        startActivity(setIntent);
    }



}
