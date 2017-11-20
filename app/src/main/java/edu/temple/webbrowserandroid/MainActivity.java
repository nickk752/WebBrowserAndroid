package edu.temple.webbrowserandroid;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String HOME_PAGE = "http://wikipedia.org";

    ViewPager pager;
    EditText urlEditText;
    Button button;
    WebFragmentStatePagerAdapter fragAdapter;
    String url;

    int currentFragNum = 0;
    int NUM_FRAGS = 0;

    WebFragment currentFrag;
    ArrayList<WebFragment> fragments = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Find the action toolbar and set it as the support action bar.
        Toolbar urlToolbar = (Toolbar) findViewById(R.id.urlToolbar);
        setSupportActionBar(urlToolbar);


        //fragments.add(WebFragment.newInstance("http://www.google.com"));


        pager = (ViewPager) findViewById(R.id.pager);
        urlEditText = (EditText) findViewById(R.id.urlEditText);


        //Create a new web fragment state pager adapter
        fragAdapter = new WebFragmentStatePagerAdapter(getSupportFragmentManager());

        //set the new adapter to be the view pager's adaoter
        pager.setAdapter(fragAdapter);

        //get the intent the activity was launched with
        final Uri data = getIntent().getData();

        //create the default frag or load the existing first frag.
        currentFrag = (WebFragment) fragAdapter.getItem(currentFragNum);//WebFragment.newInstance(data!=null ? data.toString() : "http://wikipedia.org");
        //Create a string with the url from the intent if it isn't null, otherwise it uses the fragment's url argument.
        String urlToSet = data!=null ? data.toString() : currentFrag.getArguments().getString("url", HOME_PAGE);
        Bundle newArgs = new Bundle();
        //set the fragment's url to whatever url was just decided on.
        newArgs.putString("url", urlToSet);
        currentFrag.setArguments(newArgs);

        //Increment the number of fragments
        NUM_FRAGS++;

        //Let the fragment adapter know that the number of fragments changed.
        fragAdapter.notifyDataSetChanged();

        //Set the view pager to display the newly created fragment.
        pager.setCurrentItem(currentFragNum);




        button = (Button) findViewById(R.id.goButton);
        //create an onclick listener for the go button.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get the typed url
                url = urlEditText.getText().toString();

                //Get the fragment number
                currentFragNum = pager.getCurrentItem();

                //get the fragment
                currentFrag = fragments.get(currentFragNum);
                //Change the site to the url found in the bar.
                currentFrag.changeSite(url);
            }
        });

    }


    private class WebFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

        public WebFragmentStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //if the fragment already exists, return it.
            if(fragments.size() > position) {
                return fragments.get(position);
            } else {
                //Create a new fragment
                WebFragment defaultFragment = WebFragment.newInstance("");

                //Add the fragment to the list of fragments
                fragments.add(position, defaultFragment);

                //increment the frag count and return the fragment.
                currentFragNum++;
                return defaultFragment;
            }
        }

        @Override
        public int getCount() {
            return NUM_FRAGS;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.urlmenu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.backButton:
                //Move back a tab, or move to the end
                int current = (pager.getCurrentItem() - 1) % NUM_FRAGS;
                pager.setCurrentItem(current);
                currentFragNum = current;

                //set the url bar to the destination frag's url.
                urlEditText.setText(fragments.get(currentFragNum).getArguments().getString("url", HOME_PAGE));
                return true;
            case R.id.forwardButton:
                //Move forward a tab or move to the beginning.
                current = (pager.getCurrentItem() + 1) % NUM_FRAGS;
                pager.setCurrentItem(current);
                currentFragNum = current;
                String urlToSet = fragments.get(currentFragNum).getArguments().getString("url", HOME_PAGE);
                //set the url bar to the destination frag's url.
                urlEditText.setText(urlToSet);
                return true;
            case R.id.newTabButton:
                //Create a new frag with the homepage as the url.
                currentFrag = WebFragment.newInstance(HOME_PAGE);
                currentFragNum = NUM_FRAGS;
                NUM_FRAGS++;

                fragAdapter.notifyDataSetChanged();
                pager.setCurrentItem(currentFragNum);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
