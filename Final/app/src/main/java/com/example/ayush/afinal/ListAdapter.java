package com.example.ayush.afinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ListAdapter extends BaseAdapter {

    // Declare Variables

    String[] docname;
    String[] image;
    String[] Date1;
    LayoutInflater inflater;
    private Context context;


    public ListAdapter(Context context, String[] docname,String[] image, String[] date) {
        this.context = context;
        this.docname = docname;
        this.image = image;
        this.Date1=date;
    }

    @Override
    public int getCount() {
        return docname.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Declare Variables
        TextView docname1;
        TextView date;
        ImageView imageverrsion;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.listitem, parent, false);


        docname1 = (TextView) itemView.findViewById(R.id.docname);

        imageverrsion = (ImageView) itemView.findViewById(R.id.image);

        date=itemView.findViewById(R.id.date);
        docname1.setText(docname[position]);
        date.setText(Date1[position]);
        Picasso.with(context.getApplicationContext()).load(image[position]).into(imageverrsion);


        return itemView;
    }
}