package com.software.dzungvu.jzunews;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.software.dzungvu.adapter.ElementsAdapter;
import com.software.dzungvu.model.NewsElements;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {

    SwipeMenuListView lvFavorNews;
    ElementsAdapter adapter;
    ArrayList<NewsElements> arrayList;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        this.setTitle("Save News");

        arrayList = new ArrayList<>();
        lvFavorNews = findViewById(R.id.lvFavorNews);
        toolbar = findViewById(R.id.newsToolBar);
        setSupportActionBar(toolbar);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("DATAREAD", null);
        if (json == null || json.equals("")) {
            Toast.makeText(this, "No item was saved", Toast.LENGTH_LONG).show();
        } else {
            Type type = new TypeToken<ArrayList<NewsElements>>() {
            }.getType();
            arrayList = gson.fromJson(json, type);
            adapter = new ElementsAdapter(this, R.layout.item_news, arrayList);
            lvFavorNews.setAdapter(adapter);

        }

        lvFavorNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FavoriteActivity.this, WebViewContent.class);
                intent.putExtra("URLNEWS", arrayList.get(position).getLink());
                startActivity(intent);
            }
        });

        //set listview long click listener
        lvFavorNews.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("LINK", arrayList.get(position).getLink());
                bundle.putString("DESCRIPTION", arrayList.get(position).getDescription());
                bundle.putString("PUBDATE", arrayList.get(position).getPubDate());
                bundle.putString("TITLE", arrayList.get(position).getTitle());
                bundle.putString("GUID", arrayList.get(position).getGuid());
                Intent intent = new Intent(FavoriteActivity.this, FavoritePopupActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

                return true;
            }
        });


        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(170);
                // set item title
                openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_action_name);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        lvFavorNews.setMenuCreator(creator);


        lvFavorNews.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Log.d("TAG", "1");
                        break;
                    case 1:
                        Log.d("TAG", "2");
                        arrayList.remove(position);
                        adapter = new ElementsAdapter(FavoriteActivity.this, R.layout.item_news, arrayList);
                        lvFavorNews.setAdapter(adapter);
                        break;
                }
                return false;
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);
        editor.putString("DATAREAD", json);
        editor.apply();
    }
}
