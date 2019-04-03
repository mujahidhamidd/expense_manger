package com.expense;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;


public class Statistics extends AppCompatActivity {

    SQLiteDatabase db;
    DbHelper mDbHelper;
    ListView list;
    Spinner history_spinner;
    Button buttongo;
    FloatingActionButton floatingActionButton;

    LinearLayout stat;
    TextView Income,spent;


    int spending =0, income=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics);
        getSupportActionBar().setTitle("statistics");


        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue)));
        list = (ListView)findViewById(R.id.commentlist);
        buttongo = findViewById(R.id.buttongo);
        Income = findViewById(R.id.income);
        spent = findViewById(R.id.spent);
        stat = findViewById(R.id.stat);

        mDbHelper = new DbHelper(this);
        db= mDbHelper.getWritableDatabase();
        history_spinner= (Spinner) findViewById(R.id.history_spinner);

        String qu = "SELECT DISTINCT date FROM expense";


        ArrayList<String> dates = new ArrayList<>();

        Cursor cr = db.rawQuery(qu,null);
        if (cr != null) {
            cr.moveToFirst();
            while (!cr.isAfterLast()) {
                dates.add(cr.getString(cr.getColumnIndex("date")));

                cr.moveToNext();
            }
        }


        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(Statistics.this, android.R.layout.simple_spinner_dropdown_item, dates);

        history_spinner.setAdapter(adapterSpinner);



        buttongo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String selection = mDbHelper.DATE + " = ?";

                // selection argument
                String[] selectionArgs = {history_spinner.getSelectedItem() + ""};

                String[] from = {mDbHelper.TITLE, mDbHelper.PRICE,  mDbHelper.DATE, mDbHelper.TYPE};
                final String[] column = {mDbHelper.ID, mDbHelper.TITLE, mDbHelper.DATE,  mDbHelper.PRICE,mDbHelper.TYPE};
                int[] to = {R.id.title, R.id.date,R.id.price,R.id.type};


                final Cursor cursor = db.query(mDbHelper.EXPENSE_TABLE_NAME, column, selection, selectionArgs, null, null, null);
                final SimpleCursorAdapter adapter = new SimpleCursorAdapter(Statistics.this, R.layout.list_entry, cursor, from, to, 0);

                list.setAdapter(adapter);


                stat.setVisibility(View.VISIBLE);


                String selected_history=history_spinner.getSelectedItem()+"";


                Cursor cursor2 = db.rawQuery("select * from expense where date ='"+selected_history+"'", null);



                if (cursor2 != null) {


                    cursor2.moveToFirst();
                    while (!cursor2.isAfterLast()) {



                        if( cursor2.getString(cursor2.getColumnIndex("type")).equalsIgnoreCase("out")){

                           spending+=Integer.parseInt(cursor2.getString(cursor2.getColumnIndex("price")));

                        }

                        else{

                            income+=Integer.parseInt(cursor2.getString(cursor2.getColumnIndex("price")));


                        }



                        cursor2.moveToNext();

                        spent.setText("Spending :"+spending+"");
                        Income.setText("Income :"+income+"");
                    }




                  //  cursor2.close();
                }


            }
        });





    }




    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {



            case R.id.logout:
                logout();
               return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        SharedPrefManager.getInstance(Statistics.this).clear();
        Intent intent = new Intent(Statistics.this, login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


}
