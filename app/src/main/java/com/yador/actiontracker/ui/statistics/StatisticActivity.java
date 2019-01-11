package com.yador.actiontracker.ui.statistics;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.yador.actiontracker.R;
import com.yador.actiontracker.repository.entity.Category;
import com.yador.actiontracker.repository.entity.Record;
import com.yador.actiontracker.ui.category.CategoryFragment;
import com.yador.actiontracker.ui.record.RecordsFragment;

public class StatisticActivity extends AppCompatActivity
        implements RecordsFragment.OnRecordsListFragmentInteractionListener,
        CategoryFragment.OnCategoriesFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        ViewPager mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        TabsFragmentAdapter adapter = new TabsFragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new MostOftenFragment(), getString(R.string.most_often));
        adapter.addFragment(new MostSumFragment(), getString(R.string.most_sum));
        adapter.addFragment(new SumByCatFragment(), getString(R.string.sum));
        adapter.addFragment(new PieFragment(), getString(R.string.pie));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onRecordsListFragmentInteraction(Record item) {

    }

    @Override
    public void onCategoriesFragmentInteraction(Category item) {

    }
}
