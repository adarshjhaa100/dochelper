package com.example.ayush.afinal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ItemView extends Activity {

    TextView doc,date;
    ImageView report;
    String[] docname;
    String[] Date1;
    String[] image;
    int position;
    private Context context;
    public final String Tag="hi";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

        Intent i = getIntent();

        position = i.getExtras().getInt("position");


        docname = i.getStringArrayExtra("docname");
        image = i.getStringArrayExtra("image");
        Date1=i.getStringArrayExtra("date");



        report = (ImageView) findViewById(R.id.image);






        Picasso.with(getApplicationContext()).load(image[position]).into(report);


    }
}
