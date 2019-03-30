package com.msajid;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class AllordersActivity extends AppCompatActivity {

    SQLiteDatabase db;
    DbHelper mDbHelper;
    ListView list;
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allorders);
        getSupportActionBar().setTitle("All Orders");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue)));
        list = (ListView)findViewById(R.id.commentlist);

        mDbHelper = new DbHelper(this);
        db= mDbHelper.getWritableDatabase();

        String[] from = {"_id","quantity","message"  ,"masjid_name"};
        final String[] column = {"_id", "quantity", "message", "masjid_name"};
        int[] to = {R.id.id, R.id.quantity, R.id.message,R.id.masjid_name};

        final Cursor cursor = db.query("orders", column, null, null ,null, null, null);
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.order_list, cursor, from, to, 0);


        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> listView, View view, int position,
                                    long id){





                // get phone from  masjid table


                    Cursor cursor = db.rawQuery("select * from orders where _id =" + id, null);


                    String masjid_name="";

                    String masjid_phone="";
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {


                            masjid_name= cursor.getString(cursor.getColumnIndex("masjid_name"));



                        }
                        cursor.close();


                        Cursor cursor2 = db.rawQuery("select * from masjid where name ='"+masjid_name+"'", null);



                        if (cursor2 != null) {
                            if (cursor2.moveToFirst()) {


            masjid_phone=cursor2.getString(cursor2.getColumnIndex("phone1"));

            callphone();


                            }

                            cursor2.close();
                        }





                }


            }





        });



        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, final long id) {
                // TODO Auto-generated method stub

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                if(delete(String.valueOf(id)))
                                    Toast.makeText(AllordersActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(AllordersActivity.this);
                builder.setMessage("Are you sure you want to delete this?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

                return true;
            }
        });



    }


    public boolean delete(String id)
    {
        return db.delete("orders", "_id" + "=" + id, null) > 0;
    }


    private  void callphone(){

        if (ContextCompat.checkSelfPermission(AllordersActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AllordersActivity.this, new String[]{Manifest.permission.CALL_PHONE},99);
        }
        else
        {



            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" +"7878"));// Initiates the Intent
            startActivity(intent);
        }


    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {



            case R.id.action_settings:
                logout();
               return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        SharedPrefManager.getInstance(AllordersActivity.this).clear();
        Intent intent = new Intent(AllordersActivity.this, login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


}
