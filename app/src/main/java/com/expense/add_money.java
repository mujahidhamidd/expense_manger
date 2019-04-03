package com.expense;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class add_money extends AppCompatActivity {

    SQLiteDatabase db;
    DbHelper mDbHelper;
    EditText title;
    EditText price;


    Button buttonsave;

    Spinner type_spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);
        mDbHelper = new DbHelper(this);
        db = mDbHelper.getWritableDatabase();
//
        title = (EditText) findViewById(R.id.title);
        price = (EditText) findViewById(R.id.price);
        buttonsave=findViewById(R.id.buttonsave);

        type_spinner = findViewById(R.id.type_spinner);


        ArrayList<String> types = new ArrayList<>();
        types.add("in");
        types.add("out");


        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(add_money.this, android.R.layout.simple_spinner_dropdown_item, types);

        type_spinner.setAdapter(adapterSpinner);




        buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                String type=type_spinner.getSelectedItem().toString();


                // insert into database
                ContentValues cv = new ContentValues();
                cv.put(mDbHelper.TITLE, title.getText().toString());
                cv.put(mDbHelper.PRICE, price.getText().toString());

                cv.put(mDbHelper.DATE, date);
                cv.put(mDbHelper.TYPE, type);

                db.insert(mDbHelper.EXPENSE_TABLE_NAME, null, cv);


                Toast.makeText(add_money.this, "Saved Successfully", Toast.LENGTH_SHORT).show();




            }
        });




    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(this, MainActivity.class);
        startActivity(setIntent);
    }



}
