package com.msajid;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class add_masjid extends AppCompatActivity {

    SQLiteDatabase db;
    DbHelper mDbHelper;
    EditText title;
    EditText phone1;
    EditText phone2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_masjid);
        mDbHelper = new DbHelper(this);
        db = mDbHelper.getWritableDatabase();
//
        title = (EditText) findViewById(R.id.title);
        phone1 = (EditText) findViewById(R.id.phone1);
        phone2 = (EditText) findViewById(R.id.phone2);


    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(this, MainActivity.class);
        startActivity(setIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_masjid, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.action_save:

                // insert into database
                ContentValues cv = new ContentValues();
                cv.put(mDbHelper.NANE, title.getText().toString());
                cv.put(mDbHelper.PHONE1, phone1.getText().toString());

                cv.put(mDbHelper.PHONE2, phone2.getText().toString());

                db.insert(mDbHelper.MASJID_TABLE_NAME, null, cv);


                Toast.makeText(this, "Created Successfully", Toast.LENGTH_SHORT).show();




                return true;

            case R.id.action_back:
                Intent openMainActivity = new Intent(this, MainActivity.class);
                startActivity(openMainActivity);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
