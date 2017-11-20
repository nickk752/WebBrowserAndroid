package edu.temple.webbrowserandroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Objects;


public class WebFragment extends Fragment {


    private WebView webView;
    //public String url;

    public WebFragment() {
        // Required empty public constructor
    }

    //newInstance returns an instantiated WebFragment
    public static WebFragment newInstance(String newUrl) {
        WebFragment fragment = new WebFragment();
        Bundle args = new Bundle();

        args.putString("url", newUrl);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_web, container, false);
        webView = view.findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        //this.changeSite(url);
        this.changeSite(getArguments().getString("url", "www.bing.com"));

        return view;
    }


    public void changeSite(String url) {

        if(!url.isEmpty() && !Objects.equals(url, null)){
           // this.url = url;
            if (!url.contains("http://") && !url.contains("https://")) {
                //If the url doesn't contain http://, add it.
                url = "http://" + url;
            }
            //Load the url
            webView.loadUrl(url);
        }
    }
}
