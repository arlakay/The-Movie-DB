package com.themoviedb.ui.list;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.themoviedb.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    public static final String FRAGMENT_MOVIES_TAG = "FragmentMoviesTag";

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.spinner)Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupToolbar();
        setupSpinner();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content, MainFragment.newInstance(),
                            FRAGMENT_MOVIES_TAG).commit();
        }

    }

    private void setupSpinner() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(toolbar.getContext(),
                        R.array.sort,
                        R.layout.support_simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() == null) {
            throw new IllegalStateException("Activity must implement toolbar");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedSort = getSortByPosition(position);

        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(FRAGMENT_MOVIES_TAG);

        ((SortableFragment) fragment).sortMovie(selectedSort);
    }

    private String getSortByPosition(int position) {
        switch (position) {
            case 0:
                return "popular";
            case 1:
                return "top_rated";
            default:
                return "popular";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }

}
