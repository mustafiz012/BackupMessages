package com.android.musta.backupmessages;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView smsList;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        smsList = (ListView) findViewById(R.id.lv_sms_list);
        smsList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, getAllSmsFromProvider()));
        smsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("Current", "" +adapterView.getItemAtPosition(i));
                Intent intent = new Intent(getApplicationContext(), IndividualMsgActivity.class);
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
                Log.i("number", ""+cursor.getString(i));
                intent.putExtra("id", cursor.getString(i));
                startActivity(intent);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private List<String> getAllSmsFromProvider() {
        List<String> listSms = new ArrayList<>();
        ContentResolver resolver = this.getContentResolver();
        String[] distinctProjection = {"DISTINCT " + Telephony.Sms.ADDRESS};
        String whereAll = null;
        Cursor cursor = resolver.query(Telephony.Sms.CONTENT_URI,
                distinctProjection,
                whereAll,
                null,
                Telephony.Sms.DEFAULT_SORT_ORDER);
        int totalSms = cursor.getCount();
        Log.i("Total SMS", " " + totalSms);
        if (cursor.moveToFirst()) {
            while (totalSms > 0) {
                listSms.add(cursor.getString(0));
                cursor.moveToNext();
                totalSms--;
            }
        } else
            throw new RuntimeException("You've no SMS");
        cursor.close();
        return listSms;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
