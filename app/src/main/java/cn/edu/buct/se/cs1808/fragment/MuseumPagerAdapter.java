package cn.edu.buct.se.cs1808.fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MuseumPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    private ArrayList<String> pageNameList;
    public MuseumPagerAdapter(List<Fragment> Flist, ArrayList<String> NList, FragmentManager fm){
        super(fm);
        this.fragmentList = Flist;
        this.pageNameList = NList;
    }
    @Override
    public Fragment getItem(int position){
        return fragmentList.get(position);
    }
    @Override
    public int getCount(){
        return fragmentList.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return pageNameList.get(position);
    }
}
