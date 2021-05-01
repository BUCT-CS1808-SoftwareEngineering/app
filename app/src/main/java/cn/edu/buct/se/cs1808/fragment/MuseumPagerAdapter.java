package cn.edu.buct.se.cs1808.fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class MuseumPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    public MuseumPagerAdapter(List<Fragment> Flist, FragmentManager fm){
        super(fm);
        this.fragmentList = Flist;
    }
    @Override
    public Fragment getItem(int position){
        return fragmentList.get(position);
    }
    @Override
    public int getCount(){
        return fragmentList.size();
    }
}
