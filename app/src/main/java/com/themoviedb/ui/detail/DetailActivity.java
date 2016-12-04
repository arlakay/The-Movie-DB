package com.themoviedb.ui.detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.themoviedb.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        long id = getIntent().getLongExtra("movie_id", -1);
        String movieData = getIntent().getStringExtra("movie_data");

        getSupportFragmentManager().beginTransaction().replace(R.id.content,
                DetailFragment.newInstance(id, movieData)).commit();

        setupToolbar();

    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() ==  null) {
            throw new IllegalStateException("Activity must implement toolbar.");
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
