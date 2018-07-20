package user.police.sadana.milan_user.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import user.police.sadana.milan_user.Fragments.Complaint;
import user.police.sadana.milan_user.Fragments.Home;

public class MainPager extends FragmentStatePagerAdapter {

    private String [] Topics = {"Milan","File Complaint", "Notice"};

    public MainPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0: new Home();
            case 1: new Complaint();
            default: return new Home();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }


    @Override
    public int getItemPosition(Object object){
        return MainPager.POSITION_NONE;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return  Topics[position];
    }

}
