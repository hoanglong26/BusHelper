package com.example.hoanglong.bushelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hoanglong.bushelper.entities.Location;
import com.example.hoanglong.bushelper.ormlite.DatabaseManager;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryFragment extends Fragment {
    @BindView(R.id.rvHistory)
    RecyclerView rvHistory;

    @BindView(R.id.empty_view)
    TextView empty;

    public HistoryFragment() {
        // Required empty public constructor
    }


    public static HistoryFragment newInstance(String title) {
        HistoryFragment fragment = new HistoryFragment();
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
        View rootView =  inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this,rootView);


        FastItemAdapter<Location> fastAdapter = new FastItemAdapter<>();
        rvHistory.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        //set our adapters to the RecyclerView
        //we wrap our FastAdapter inside the ItemAdapter -> This allows us to chain adapters for more complex useCases
        rvHistory.setAdapter(fastAdapter);

        //set the items to your ItemAdapter
        List locationList = DatabaseManager.getInstance().getAllLocations();

        if(locationList.size()==0){
            rvHistory.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }else{
            empty.setVisibility(View.GONE);
            rvHistory.setVisibility(View.VISIBLE);
        }

        fastAdapter.add(locationList);

        fastAdapter.withSelectable(true);
        fastAdapter.withOnClickListener(new FastAdapter.OnClickListener<Location>() {
            @Override
            public boolean onClick(View v, IAdapter<Location> adapter, Location item, int position) {
                EventBus.getDefault().post(new FragmentAdapter.OpenEvent(position,item.getLatitude(),item.getLongitude()));
                return true;
            }
        });

        return rootView;

    }


}
