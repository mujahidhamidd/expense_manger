package com.msajid;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class forward_order extends AppCompatActivity {

    SQLiteDatabase db;
    DbHelper dbHelper;
    EditText Quantity,message;
    String masjid_name;
    Button forward;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forward_order);
        final long id = getIntent().getExtras().getLong(getString(R.string.row_id));

        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();

        Quantity= findViewById(R.id.quantity);
        message= findViewById(R.id.message);
        forward= findViewById(R.id.forward);


















        Cursor cursor = db.rawQuery("select * from " + dbHelper.MASJID_TABLE_NAME + " where " + dbHelper.ID + "=" + id, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {


                this.setTitle(cursor.getString(cursor.getColumnIndex(dbHelper.NANE)));
                masjid_name=cursor.getString(cursor.getColumnIndex(dbHelper.NANE));





            }
            cursor.close();

        }


          forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                // insert into database
                ContentValues cv = new ContentValues();
                cv.put("quantity", Quantity.getText().toString());
                cv.put("message", message.getText().toString());

                cv.put("masjid_name",masjid_name);

                db.insert("orders", null, cv);


                makeNotification(masjid_name,"new order");

                Toast.makeText(forward_order.this, "Added ", Toast.LENGTH_SHORT).show();



            }
        });
    }


    public  void makeNotification(String title,String content) {



        NotificationCompat.Builder builder =
                new NotificationCompat.Builder( getApplicationContext())
                        .setSmallIcon(R.drawable.ic_action_alarms)
                        .setContentTitle(title)
                        .setContentText(content).setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                        + "://" +  getApplicationContext().getPackageName() + "/raw/notify"));

        // Add as notification
        NotificationManager manager = (NotificationManager)  getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(this, MainActivity.class);
        startActivity(setIntent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final long id = getIntent().getExtras().getLong(getString(R.string.rowID));

        switch (item.getItemId()) {
            case R.id.action_back:
                Intent openMainActivity = new Intent(this, MainActivity.class);
                startActivity(openMainActivity);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
