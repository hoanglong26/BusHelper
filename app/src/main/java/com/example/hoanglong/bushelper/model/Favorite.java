package com.example.hoanglong.bushelper.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hoanglong.bushelper.R;
import com.example.hoanglong.bushelper.utils.Utils;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

/**
 * Created by hoanglong on 01-Jul-17.
 */
@DatabaseTable
public class Favorite extends AbstractItem<Favorite,Favorite.ViewHolder> implements Parcelable {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String name;

    @DatabaseField
    private double longitude;

    @DatabaseField
    private double latitude;

    @DatabaseField
    private String place_id;

    public Favorite(int id, String name, double latitude,double longitude) {
        this.id = id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Favorite(int id, String name, double latitude, double longitude, String place_id) {
        this.id = id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.place_id=place_id;
    }

    public Favorite() {
    }

    protected Favorite(Parcel in) {
        id = in.readInt();
        name = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
    }

    public static final Parcelable.Creator<Favorite> CREATOR = new Parcelable.Creator<Favorite>() {
        @Override
        public Favorite createFromParcel(Parcel in) {
            Favorite instance = new Favorite();
            instance.id = ((int) in.readValue((Integer.class.getClassLoader())));
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            instance.latitude = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.longitude = ((Double) in.readValue((Double.class.getClassLoader())));
            return instance;

        }

        @Override
        public Favorite[] newArray(int size) {
            return new Favorite[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    //The unique ID for this type of item
    @Override
    public int getType() {
        return 0;
    }

    //The layout to be used for this type of item
    @Override
    public int getLayoutRes() {
        return R.layout.item;
    }

    //The logic to bind your data to the view
    @Override
    public void bindView(Favorite.ViewHolder viewHolder, final List<Object> payloads) {
        //call super so the selection is already handled for you
        super.bindView(viewHolder, payloads);

        //bind our data
        //set the text for the name
        viewHolder.name.setText(name);

        viewHolder.latitude.setText("Latitude: "+latitude+"");
        viewHolder.longitude.setText("Longitude: "+longitude+"");
        if(place_id==null){
            Bitmap thumbnail = BitmapFactory.decodeResource(viewHolder.thumbnail.getResources(), R.drawable.star);
            viewHolder.thumbnail.setImageBitmap(thumbnail);

        }else{
            viewHolder.thumbnail.setImageBitmap(Utils.StringToBitMap(place_id));

        }


    }

    //reset the view here (this is an optional method, but recommended)
    @Override
    public void unbindView(Favorite.ViewHolder holder) {
        super.unbindView(holder);
        holder.name.setText(null);
        holder.latitude.setText(null);
        holder.longitude.setText(null);
        holder.thumbnail.setImageBitmap(null);
    }

    //Init the viewHolder for this Item
    @Override
    public Favorite.ViewHolder getViewHolder(View v) {
        return new Favorite.ViewHolder(v);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(latitude);
        dest.writeValue(longitude);
    }

    //The viewHolder used for this item. This viewHolder is always reused by the RecyclerView so scrolling is blazing fast
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected TextView longitude;
        protected TextView latitude;
        protected ImageView thumbnail;

        public ViewHolder(View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.tvName);
            this.latitude =(TextView) view.findViewById(R.id.tvLat);
            this.longitude =(TextView) view.findViewById(R.id.tvLong);
            this.thumbnail =(ImageView) view.findViewById(R.id.thumbnail);
//            this.description = (TextView) view.findViewById(com.mikepenz.materialdrawer.R.id.material_drawer_description);
        }
    }

    @Override
    public boolean equals(Object v) {
        boolean flag = false;

        if (v instanceof Favorite){
            Favorite ptr = (Favorite) v;
            flag = ptr.getName().equals(this.name);
        }

        return flag;
    }
}
