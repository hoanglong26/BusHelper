package com.example.hoanglong.bushelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hoanglong.bushelper.model.Favorite;
import com.example.hoanglong.bushelper.model.Location;
import com.example.hoanglong.bushelper.model.Member;
import com.example.hoanglong.bushelper.ormlite.DatabaseManager;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FavoriteFragment extends Fragment {
    @BindView(R.id.rvFavorite)
    RecyclerView rvFavorite;

    @BindView(R.id.empty_view2)
    TextView empty;


    public FavoriteFragment() {
        // Required empty public constructor
    }

    public static FavoriteFragment newInstance(String title) {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        ButterKnife.bind(this,rootView);

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvMember.getContext(),
//                DividerItemDecoration.VERTICAL);
//        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_line));
//        rvMember.addItemDecoration(dividerItemDecoration);
//
//        FastItemAdapter<Member> fastAdapter = new FastItemAdapter<>();
//        rvMember.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
//
//        //set our adapters to the RecyclerView
//        //we wrap our FastAdapter inside the ItemAdapter -> This allows us to chain adapters for more complex useCases
//        rvMember.setAdapter(fastAdapter);
//
//        List memList = new ArrayList();
//        Member mem1 = new Member("Long");
//        Member mem2 = new Member("Duc");
//        Member mem3 = new Member("Khang");
//
//        memList.add(mem1);
//        memList.add(mem2);
//        memList.add(mem3);
//
//
//        fastAdapter.add(memList);
//
//        fastAdapter.withSelectable(true);
//        fastAdapter.withOnClickListener(new FastAdapter.OnClickListener<Member>() {
//            @Override
//            public boolean onClick(View v, IAdapter<Member> adapter, Member item, int position) {
//                EventBus.getDefault().post(new FragmentAdapter.OpenEvent(position,10.45,10.45));
//                return true;
//            }
//        });



        FastItemAdapter<Favorite> fastAdapter = new FastItemAdapter<>();
        rvFavorite.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        //set our adapters to the RecyclerView
        //we wrap our FastAdapter inside the ItemAdapter -> This allows us to chain adapters for more complex useCases
        rvFavorite.setAdapter(fastAdapter);

        //set the items to your ItemAdapter
        List locationList = DatabaseManager.getInstance().getAllFavorites();

        if(locationList.size()==0){
            rvFavorite.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }else{
            empty.setVisibility(View.GONE);
            rvFavorite.setVisibility(View.VISIBLE);
        }

        fastAdapter.add(locationList);

        fastAdapter.withSelectable(true);
        fastAdapter.withOnClickListener(new FastAdapter.OnClickListener<Favorite>() {
            @Override
            public boolean onClick(View v, IAdapter<Favorite> adapter, Favorite item, int position) {
                EventBus.getDefault().post(new FragmentAdapter.OpenEvent(position,item.getLatitude(),item.getLongitude()));
                return true;
            }
        });


        return rootView;
    }


}