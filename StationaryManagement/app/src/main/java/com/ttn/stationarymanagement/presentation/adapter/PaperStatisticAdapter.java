package com.ttn.stationarymanagement.presentation.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ttn.stationarymanagement.presentation.fragment.DepartmentStatisticFragment;
import com.ttn.stationarymanagement.presentation.fragment.ProductStatisticFragment;

import java.util.ArrayList;
import java.util.List;

public class PaperStatisticAdapter extends FragmentStatePagerAdapter {

    List<Fragment> listFragment;

    public PaperStatisticAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);

        listFragment = new ArrayList<>();

        ProductStatisticFragment productStatisticFragment = ProductStatisticFragment.newInstance();
        DepartmentStatisticFragment departmentStatisticFragment = DepartmentStatisticFragment.newInstance();

        listFragment.add(productStatisticFragment);
        listFragment.add(departmentStatisticFragment);



    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return listFragment.get(0);

            case 1:
                return listFragment.get(1);
        }
        return null;
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = "Sản phẩm";
                break;
            case 1:
                title = "Phòng ban";
                break;

        }
        return title;
    }
}
