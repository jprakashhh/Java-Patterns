package com.android.jayaprakash.patternsinjava;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.net.URLEncoder;

public class WebView extends AppCompatActivity {
    android.webkit.WebView wb;
    private FloatingActionMenu fam;
    private FloatingActionButton copy, about, rate, contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);


        String data;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i = getIntent();
        int position = i.getExtras().getInt("id");
        wb = (android.webkit.WebView) findViewById(R.id.jp);
        wb.getSettings().setBuiltInZoomControls(true);
        wb.getSettings().setDisplayZoomControls(false);
        wb.setVerticalScrollBarEnabled(false);
        String str = "file:///android_asset/23.html";
        wb.loadUrl(str);

        fam = (FloatingActionMenu) findViewById(R.id.menu);
        about = (FloatingActionButton) findViewById(R.id.about);
        rate = (FloatingActionButton) findViewById(R.id.rateApp);
        contact = (FloatingActionButton) findViewById(R.id.contact);
        copy = (FloatingActionButton) findViewById(R.id.copy);

        about.setOnClickListener(onButtonClick());
        rate.setOnClickListener(onButtonClick());
        contact.setOnClickListener(onButtonClick());
        copy.setOnClickListener(onButtonClick());

        //view.addJavascriptInterface(new IJavascriptHandler(this), "Android");


    }


    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    private void contactMe() {
        PackageManager packageManager = this.getPackageManager();
        Intent i = new Intent(Intent.ACTION_VIEW);


        try {
            String url = "https://api.whatsapp.com/send?phone=" + "919566264871" + "&text=" + URLEncoder.encode(" ", "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                this.startActivity(i);
            } else {
                Toast.makeText(this, "Unable to launch WhatsApp!!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private View.OnClickListener onButtonClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == about) {
                    showToast("reach via LinkedIn");
                    String url = "https://www.linkedin.com/in/jayaprakash-k-953976127/";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } else if (view == rate) {
                    String url3 = "https://play.google.com/store/apps/details?id=in.lifeinindia.lordshivachants&hl=en";
                    Intent i3 = new Intent(Intent.ACTION_VIEW);
                    i3.setData(Uri.parse(url3));
                    startActivity(i3);
                } else if (view == contact) {
                    contactMe();
                } else if (view == copy) {

                }

                fam.close(true);
            }
        };
    }


}


