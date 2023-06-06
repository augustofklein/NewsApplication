package br.ucs.android.newsapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import br.ucs.android.newsapplication.R;
import br.ucs.android.newsapplication.model.WebViewClientImpl;

public class WebviewActivity extends AppCompatActivity {

    private WebView minhaWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        Intent intent = getIntent();
        final String url = intent.getStringExtra("URL");

        if(verifica_conexao_mobile()) {


            minhaWebView = findViewById(R.id.wvArtigo);

            WebViewClientImpl webViewClient = new WebViewClientImpl(this);

            //minhaWebView.setWebViewClient(webViewClient);
            minhaWebView.setWebViewClient(new WebViewClient());

            minhaWebView.loadUrl(url);

            WebSettings webSettings = minhaWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
        }
        else {
            finish();
        }


    }

    public boolean verifica_conexao_mobile() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected() &&
                (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE ||
                        activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI);
    }
}