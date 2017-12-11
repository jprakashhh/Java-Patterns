package com.android.jayaprakash.patternsinjava;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.net.URLEncoder;


public class MainActivity extends AppCompatActivity {
    private FloatingActionMenu fam;
    private FloatingActionButton about, rate, contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fam = (FloatingActionMenu) findViewById(R.id.menu);
        about = (FloatingActionButton) findViewById(R.id.about);
        rate = (FloatingActionButton) findViewById(R.id.rateApp);
        contact = (FloatingActionButton) findViewById(R.id.contact);


        about.setOnClickListener(onButtonClick());
        rate.setOnClickListener(onButtonClick());
        contact.setOnClickListener(onButtonClick());





        /* int i = AppPreferences.getInstance(getApplicationContext()).getLaunchCount();
        str = String.valueOf(i+1);
        Toast.makeText(getApplicationContext(),str, Toast.LENGTH_SHORT).show(); */
        AppPreferences.getInstance(getApplicationContext()).incrementLaunchCount();
        showRateAppDialogIfNeeded();

        GridView gridView = (GridView) findViewById(R.id.grid_view);
        gridView.setAdapter(new ImageAdapter(this));
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        final MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        //mp = MediaPlayer.create(this, R.raw.listsound);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                myAnim.setInterpolator(interpolator);
                //mp.start();
                v.startAnimation(myAnim);
                // Sending image id to FullScreenActivity
                Intent i = new Intent(getApplicationContext(), WebView.class);
                // passing array index
                i.putExtra("id", position + 1);
                startActivity(i);
            }
        });

    }

    private void openWhatsApp() {
        PackageManager packageManager = this.getPackageManager();
        Intent i = new Intent(Intent.ACTION_VIEW);
        String versionName = "";
        int versionCode = -1;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String str = "Feedback for my Android App" + "\n\n----------------------------------\n Device OS: Android \n Device OS version: " +
                Build.VERSION.RELEASE + "\n App Version: " + versionName + "\n Version code " + versionCode + "\n Device Brand: " + Build.BRAND +
                "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER + " \n \n Hello! Type your feedback below: \n";
        try {
            String url = "https://api.whatsapp.com/send?phone=" + "919566264871" + "&text=" + URLEncoder.encode(str, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                this.startActivity(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void showRateAppDialogIfNeeded() {
        boolean bool = AppPreferences.getInstance(getApplicationContext()).getAppRate();
        int i = AppPreferences.getInstance(getApplicationContext()).getLaunchCount();
        if ((bool) && (i % 10 == 9)) {
            createAppRatingDialog(getString(R.string.rate_app_title), getString(R.string.rate_app_message)).show();
        }
    }

    private AlertDialog createAppRatingDialog(String rateAppTitle, String rateAppMessage) {
        AlertDialog dialog = new AlertDialog.Builder(this).setPositiveButton(getString(R.string.dialog_app_rate), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                openAppInPlayStore(MainActivity.this);
                //AppPreferences.getInstance(MainActivity.this.getApplicationContext()).resetLaunchCount();
                //AppPreferences.getInstance(MainActivity.this.getApplicationContext()).setAppRate(false);
            }
        }).setNegativeButton(getString(R.string.dialog_your_feedback), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                Toast.makeText(getApplicationContext(), "Feedback via WhatsApp!", Toast.LENGTH_SHORT).show();
                openWhatsApp();
                //openFeedback(MainActivity.this);
                //AppPreferences.getInstance(MainActivity.this.getApplicationContext()).resetLaunchCount();
                //AppPreferences.getInstance(MainActivity.this.getApplicationContext()).setAppRate(false);
            }
        }).setNeutralButton(getString(R.string.dialog_ask_later), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                paramAnonymousDialogInterface.dismiss();
                //AppPreferences.getInstance(MainActivity.this.getApplicationContext()).resetLaunchCount();
            }
        }).setMessage(rateAppMessage).setTitle(rateAppTitle).create();
        return dialog;
    }

    public static void openAppInPlayStore(Context paramContext) {
        paramContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/")));
    }

    public static void openFeedback(Context paramContext) {

        Intent localIntent = new Intent(Intent.ACTION_SEND);
        localIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"jpavails@gmail.com"});
        localIntent.putExtra(Intent.EXTRA_CC, "");
        String str = null;
        try {
            str = paramContext.getPackageManager().getPackageInfo(paramContext.getPackageName(), 0).versionName;
            localIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for Your Android App");
            localIntent.putExtra(Intent.EXTRA_TEXT, "\n\n----------------------------------\n Device OS: Android \n Device OS version: " +
                    Build.VERSION.RELEASE + "\n App Version: " + str + "\n Device Brand: " + Build.BRAND +
                    "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER);
            localIntent.setType("message/rfc822");
            paramContext.startActivity(Intent.createChooser(localIntent, "Choose an Email client :"));
        } catch (Exception e) {
            Log.d("OpenFeedback", e.getMessage());
        }
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
                } else {
                    contactMe();
                }
                fam.close(true);
            }
        };
    }
}
