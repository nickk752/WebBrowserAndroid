package edu.temple.webbrowser2;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ViewPager pager;
    EditText urlEditText;
    Button button;
    WebFragmentStatePagerAdapter fragAdapter;
    String url;

    int currentFragNum = 0;
    int NUM_FRAGS = 1;

    WebFragment currentFrag;
    ArrayList<WebFragment> fragments = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar urlToolbar = (Toolbar) findViewById(R.id.urlToolbar);
        setSupportActionBar(urlToolbar);


        fragments.add(WebFragment.newInstance("http://www.google.com"));


        pager = (ViewPager) findViewById(R.id.pager);
        urlEditText = (EditText) findViewById(R.id.urlEditText);


        fragAdapter = new WebFragmentStatePagerAdapter(getSupportFragmentManager());

        pager.setAdapter(fragAdapter);

        final Uri data = getIntent().getData();

        if (data != null) {
            url = data.toString();
            currentFrag.changeSite(url);
        }


        button = (Button) findViewById(R.id.goButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = urlEditText.getText().toString();
                currentFragNum = pager.getCurrentItem();
                currentFrag = fragments.get(currentFragNum);
                currentFrag.changeSite(url);
            }
        });

        //fm.beginTransaction().replace(R.id.linearLayout, WebFragment.newInstance("http://google.com/")).commit();


    }


    private class WebFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

        public WebFragmentStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            WebFragment defaultFragment = new WebFragment();
            fragments.add(currentFragNum, defaultFragment);
            currentFragNum++;
            return defaultFragment;
        }

        @Override
        public int getCount() {
            return NUM_FRAGS;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.urlmenu, menu);
/*
        MenuItem backButton = menu.findItem(R.id.backButton);
        MenuItem forwardButton = menu.findItem(R.id.forwardButton);
        MenuItem newTabButton = menu.findItem(R.id.newTabButton);
        forwardButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int itemPosition = pager.getCurrentItem();
                if(itemPosition < fragAdapter.getCount()){
                    pager.setCurrentItem(itemPosition + 1);
                } else {
                    pager.setCurrentItem(0);
                }
                return true;
            }
        });
        backButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int itemPosition = pager.getCurrentItem();
                if(itemPosition > 0){
                    pager.setCurrentItem(itemPosition - 1);
                } else {
                    pager.setCurrentItem(fragAdapter.getCount()-1);
                }

                return true;
            }
        });*/
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.backButton:
                int current = (pager.getCurrentItem() - 1) % NUM_FRAGS;
                pager.setCurrentItem(current);
                currentFragNum = current;

                urlEditText.setText(fragments.get(currentFragNum).url);
                return true;
            case R.id.forwardButton:
                current = (pager.getCurrentItem() + 1) % NUM_FRAGS;
                pager.setCurrentItem(current);
                currentFragNum = current;
                urlEditText.setText(fragments.get(currentFragNum).url);
                return true;
            case R.id.newTabButton:
                WebFragment newFrag = WebFragment.newInstance("http://google.com");
                fragments.add(NUM_FRAGS, newFrag);
                currentFragNum = NUM_FRAGS;
                NUM_FRAGS++;

                fragAdapter.notifyDataSetChanged();
                currentFrag = newFrag;
                pager.setCurrentItem(currentFragNum);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
