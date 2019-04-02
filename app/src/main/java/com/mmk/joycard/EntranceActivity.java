package com.mmk.joycard;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.mmk.joycard.Fragments.Login;
import com.mmk.joycard.Fragments.Register;

public class EntranceActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);
        mAuth= FirebaseAuth.getInstance();
        mViewPager=(ViewPager)findViewById(R.id.container);
        tabLayout=(TabLayout)findViewById(R.id.tabs);
        mSectionsPagerAdapter=new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.addFragment(new Login(),"Login");
        mSectionsPagerAdapter.addFragment(new Register(),"Register");

        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);


    }





}
