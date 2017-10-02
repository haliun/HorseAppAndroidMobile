package com.example.dovchin.horseapplication1.Activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.dovchin.horseapplication1.Adapters.ViewPagerAdapter;
import com.example.dovchin.horseapplication1.R;
import com.example.dovchin.horseapplication1.fragments.FragmentFood;
import com.example.dovchin.horseapplication1.fragments.FragmentIncome;
import com.example.dovchin.horseapplication1.fragments.FragmentOutcome;
import com.example.dovchin.horseapplication1.fragments.FragmentVit;
import com.example.dovchin.horseapplication1.methods.AddFoodVitActivity;
import com.example.dovchin.horseapplication1.methods.AddMoneyActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

public class MoneyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        Button button_back=(Button)findViewById(R.id.button_back_money);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MoneyActivity.this,MainActivity.class));
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MoneyActivity.this,MainActivity.class));
            }
        });



        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        FloatingActionMenu materialDesignFAM = (FloatingActionMenu) findViewById(R.id.fab_add_money);
        FloatingActionButton floatingActionButton1 = (FloatingActionButton) findViewById(R.id.fab_income);
        FloatingActionButton floatingActionButton2 = (FloatingActionButton) findViewById(R.id.fac_outcome);
        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String food="Орлого";
                Bundle bundle=new Bundle();
                bundle.putString("type_money",food);
                Intent intent = new Intent(MoneyActivity.this, AddMoneyActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String food="Зарлага";
                Bundle bundle=new Bundle();
                bundle.putString("type_money",food);
                Intent intent = new Intent(MoneyActivity.this, AddMoneyActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentIncome(), "Орлого");
        adapter.addFragment(new FragmentOutcome(), "Зарлага");
        viewPager.setAdapter(adapter);

    }




}
