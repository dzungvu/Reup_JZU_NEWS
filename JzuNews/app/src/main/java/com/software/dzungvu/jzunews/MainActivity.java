package com.software.dzungvu.jzunews;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    LinearLayout llNews, llWorld, llBussiness, llEntertaiment, llSport, llMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addControls();
        addEvents();


        //FB APP ID

        try {
            @SuppressLint("PackageManagerGetSignatures") PackageInfo info = getPackageManager().getPackageInfo(
                    "com.software.dzungvu.jzunews",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    private void addControls() {
        llNews = findViewById(R.id.llNews);
        llWorld = findViewById(R.id.llWorld);
        llBussiness = findViewById(R.id.llBussiness);
        llEntertaiment = findViewById(R.id.llEntertaiment);
        llSport = findViewById(R.id.llSport);
        llMore = findViewById(R.id.llMore);

    }

    private void addEvents() {
        llNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoNewsPage();
            }
        });

        llWorld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoWorldPage();
            }
        });

        llBussiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoBussinessPage();
            }
        });

        llEntertaiment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoEntertaimentPage();
            }
        });

        llSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSportPage();
            }
        });

        llMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMorePage();
            }
        });

    }

    private void gotoNewsPage() {

        Intent intent = new Intent(this, NewsPage.class);
        startActivity(intent);

    }

    private void gotoWorldPage() {

        Intent intent = new Intent(this, WorldPage.class);
        startActivity(intent);

    }

    private void gotoBussinessPage() {

        Intent intent = new Intent(this, BussinessPage.class);
        startActivity(intent);

    }

    private void gotoEntertaimentPage() {

        Intent intent = new Intent(this, EntertaimentPage.class);
        startActivity(intent);

    }

    private void gotoSportPage() {

        Intent intent = new Intent(this, SportPage.class);
        startActivity(intent);

    }

    private void gotoMorePage() {
        Intent intent = new Intent(this, MorePage.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuFavouriteNews:
                Intent intent = new Intent(this, FavoriteActivity.class);
                startActivity(intent);
                return true;
            case R.id.mnuSetting:
                Toast.makeText(this, "S", Toast.LENGTH_LONG).show();
                return true;
            default:
                return onOptionsItemSelected(item);
        }
    }
}
