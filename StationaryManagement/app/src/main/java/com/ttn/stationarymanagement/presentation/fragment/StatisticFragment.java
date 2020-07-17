package com.ttn.stationarymanagement.presentation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.presentation.adapter.PaperStatisticAdapter;
import com.ttn.stationarymanagement.presentation.baseview.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatisticFragment extends BaseFragment {

    @BindView(R.id.tab_fragment_statistic_tab_layout)
    TabLayout tablayout;

    @BindView(R.id.vp_fragment_statistic_view_paper)
    ViewPager vpViewPager;

    public static StatisticFragment newInstance() {
        Bundle args = new Bundle();
        StatisticFragment fragment = new StatisticFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_statistic, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setControls();

    }

    private PaperStatisticAdapter paperStatisticAdapter;

    private void setControls() {

        // Khởi tạo các view

        FragmentManager fm = getChildFragmentManager();
        paperStatisticAdapter = new PaperStatisticAdapter(fm, getContext());
        vpViewPager.setAdapter(paperStatisticAdapter);
        tablayout.setupWithViewPager(vpViewPager);
        vpViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));
        tablayout.setTabsFromPagerAdapter(paperStatisticAdapter);
        tablayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(vpViewPager));

    }
}
