package com.example.hoanglong.bushelper.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.hoanglong.bushelper.R;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

/**
 * Created by hoanglong on 21-Jun-17.
 */

public class Member extends AbstractItem<Member,Member.ViewHolder> {
    private String name;

    public Member() {
    }

    public Member(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getType() {
        return 0;
    }

    //The layout to be used for this type of item
    @Override
    public int getLayoutRes() {
        return R.layout.member;
    }

    //The logic to bind your data to the view
    @Override
    public void bindView(Member.ViewHolder viewHolder, List<Object> payloads) {
        //call super so the selection is already handled for you
        super.bindView(viewHolder, payloads);

        //bind our data
        //set the text for the name
        viewHolder.name.setText(name);
    }

    //reset the view here (this is an optional method, but recommended)
    @Override
    public void unbindView(Member.ViewHolder holder) {
        super.unbindView(holder);
        holder.name.setText(null);
//        holder.description.setText(null);
    }

    //Init the viewHolder for this Item
    @Override
    public Member.ViewHolder getViewHolder(View v) {
        return new Member.ViewHolder(v);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;


        public ViewHolder(View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.tvMemberName);
        }
    }
}
