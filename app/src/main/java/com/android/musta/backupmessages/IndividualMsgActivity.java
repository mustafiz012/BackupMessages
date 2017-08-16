package com.android.musta.backupmessages;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class IndividualMsgActivity extends AppCompatActivity {
    private ListView individualSmsBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_msg);
        Bundle bundle = getIntent().getExtras();
        individualSmsBody = (ListView) findViewById(R.id.lv_individual_sms);
        individualSmsBody.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, getSmsForIndividualNumber("" + bundle.getString("id"))));
    }

    private List<String> getSmsForIndividualNumber(String number) {
        List<String> listSms = new ArrayList<>();
        ContentResolver resolver = this.getContentResolver();
        String[] projectBody = {Telephony.Sms.BODY};
        String whereCondition = "address = 492"; //address = 492
        String whereAll = null;
        Cursor cursor = resolver.query(Telephony.Sms.CONTENT_URI,
                projectBody,
                whereCondition,
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
}
