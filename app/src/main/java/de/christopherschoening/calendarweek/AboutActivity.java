package de.christopherschoening.calendarweek;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class AboutActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);    //set transition
        setContentView(R.layout.activity_about);
    }


    public void materialDesignHyperlink(View view) {
        String url = "http://www.material.io";
        Intent openMaterialDotIoIntent = new Intent(Intent.ACTION_VIEW);
        openMaterialDotIoIntent.setData(Uri.parse(url));
        startActivity(openMaterialDotIoIntent);
    }
}
