package com.example.vietnguyen.views.widgets.notifications.adapters.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vietnguyen.core.ImageEntity;
import com.example.vietnguyen.myapplication.R;

import java.util.ArrayList;

/**
 * Created by viet on 8/13/2015.
 */
public class ImageAdapter extends BaseAdapter {

    Context context;
    ArrayList<ImageEntity> data;
    private static LayoutInflater inflater = null;

    public ImageAdapter(Context context, ArrayList<ImageEntity> data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if(vi == null){
            vi = inflater.inflate(R.layout.item_image, null);
        }
        TextView imgItemTitle = (TextView) vi.findViewById(R.id.imgItemTitle);
        TextView imgItemDescription = (TextView) vi.findViewById(R.id.imgItemDescription);
        ImageView savedImg = (ImageView) vi.findViewById(R.id.savedImg);

        imgItemTitle.setText(data.get(position).title);
        imgItemDescription.setText(data.get(position).description);
        Bitmap bitmap = BitmapFactory.decodeFile(data.get(position).path);
        savedImg.setImageBitmap(bitmap);
        return vi;
    }
}
