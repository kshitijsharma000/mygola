package com.myday;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.myday.db.MydayContract;

public class FavActivity extends AppCompatActivity {

    LinearLayout ll_back_button;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);
        ll_back_button = (LinearLayout) findViewById(R.id.fav_activity_button_back);
        ll_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setTitle("Loading");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();

        String query = "SELECT * FROM " + MydayContract.Activity_entry.TABLE_NAME + " WHERE " + MydayContract.Activity_entry.COL_FAV
                + " = " + 1 + ";";

    }

}
