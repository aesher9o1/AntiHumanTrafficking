package user.police.sadana.milan_user;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;
import user.police.sadana.milan_user.Adapters.MainPager;
import user.police.sadana.milan_user.Constants.BaseActivity;
import user.police.sadana.milan_user.Constants.StringValues;

public class MainActivity extends BaseActivity {




    /* General Constant */
    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;
    MainPager mPagerAdapter;

    String preferredLanguage;




    /* View Binders */
    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @BindView(R.id.tab)
    TabLayout mTabLayout;


    /*onClicks */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goFullScreen();
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mViewPager);

        mPagerAdapter = new MainPager(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(0);





    }


    @Override
    protected void onStart() {
        super.onStart();
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        StringValues stringValues = new StringValues();
        preferredLanguage = mPreferences.getString(stringValues.locale,"en");
        if(!mPreferences.getBoolean(stringValues.isSetup,false))
            showToast("NotSetup");
    }
}
