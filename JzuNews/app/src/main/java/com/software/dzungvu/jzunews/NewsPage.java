package com.software.dzungvu.jzunews;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.software.dzungvu.adapter.ElementsAdapter;
import com.software.dzungvu.adapter.ElementsGridAdapter;
import com.software.dzungvu.configuration.Configuration;
import com.software.dzungvu.model.NewsElements;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class NewsPage extends AppCompatActivity {

    GridView gvNews;
    ProgressDialog progressDialog;
    private ListView lvNews;
    private ArrayList<NewsElements> newsElementsArrayList;

    public static InputStream getInputStream(URL url) {

        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_page);
        this.setTitle("News Page");

        addControls();
        addEvents();
    }

    private void addControls() {
        lvNews = findViewById(R.id.lvNews);
        gvNews = findViewById(R.id.gvNews);
        newsElementsArrayList = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        Toolbar toolbar = findViewById(R.id.newsToolBar);
        setSupportActionBar(toolbar);


        String link = Configuration.RSS_LINK + Configuration.NEWS_LINK;
        if (haveNetworkConnection()) {
            GetRssFile task = new GetRssFile();
            task.execute(link);
        } else {
            Toast.makeText(this, "No internet", Toast.LENGTH_LONG).show();
        }


        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NewsPage.this, WebViewContent.class);
                intent.putExtra("URLNEWS", newsElementsArrayList.get(position).getLink());
                startActivity(intent);
            }
        });


        lvNews.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("LINK", newsElementsArrayList.get(position).getLink());
                bundle.putString("DESCRIPTION", newsElementsArrayList.get(position).getDescription());
                bundle.putString("PUBDATE", newsElementsArrayList.get(position).getPubDate());
                bundle.putString("TITLE", newsElementsArrayList.get(position).getTitle());
                bundle.putString("GUID", newsElementsArrayList.get(position).getGuid());
                Intent intent = new Intent(NewsPage.this, PopupActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);


                return true;
            }
        });


    }

    private void addEvents() {


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.list_news_menu_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuBack:
                this.finish();
                return true;
            case R.id.mnu_news_grid_view:
                Toast.makeText(this, "Grid view", Toast.LENGTH_LONG).show();
                lvNews.setVisibility(View.GONE);
                gvNews.setVisibility(View.VISIBLE);
                return true;
            case R.id.mnu_news_list_view:
                Toast.makeText(this, "List view", Toast.LENGTH_LONG).show();
                lvNews.setVisibility(View.VISIBLE);
                gvNews.setVisibility(View.GONE);
                return true;
            default:
                return onOptionsItemSelected(item);
        }
    }

    private boolean haveNetworkConnection() {
        boolean haveWifiConnection = false;
        boolean haveMobileConnection = false;

        ConnectivityManager cm = (ConnectivityManager) NewsPage.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                haveWifiConnection = ni.isConnected();
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                haveMobileConnection = ni.isConnected();
        }
        return haveMobileConnection || haveWifiConnection;
    }

    public class GetRssFile extends AsyncTask<String, Void, ArrayList<NewsElements>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Loading data");
            progressDialog.setMessage("Please wait!");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<NewsElements> newsElementses) {
            super.onPostExecute(newsElementses);
            progressDialog.dismiss();
            ElementsAdapter elementsAdapter = new ElementsAdapter(NewsPage.this, R.layout.item_news, newsElementsArrayList);
            lvNews.setAdapter(elementsAdapter);
            ElementsGridAdapter elementsGridAdapter = new ElementsGridAdapter(NewsPage.this, R.layout.item_news_grid, newsElementsArrayList);
            gvNews.setAdapter(elementsGridAdapter);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<NewsElements> doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(getInputStream(url), "UTF-8");

                int eventType = xpp.getEventType();
                NewsElements elements = new NewsElements();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if (xpp.getName().equalsIgnoreCase("title")) {
                            elements.setTitle(xpp.nextText());
                        }
                        if (xpp.getName().equalsIgnoreCase("description")) {
                            elements.setDescription(xpp.nextText());
                        }
                        if (xpp.getName().equalsIgnoreCase("pubDate")) {
                            elements.setPubDate(xpp.nextText());
                        }
                        if (xpp.getName().equalsIgnoreCase("link")) {
                            elements.setLink(xpp.nextText());
                        }
                        if (xpp.getName().equalsIgnoreCase("guid")) {
                            elements.setGuid(xpp.nextText());
                        }
                    }
                    if (elements.getTitle() != null && elements.getDescription() != null
                            && elements.getPubDate() != null && elements.getLink() != null
                            && elements.getGuid() != null) {
                        newsElementsArrayList.add(new NewsElements(
                                elements.getTitle()
                                , elements.getDescription()
                                , elements.getPubDate()
                                , elements.getLink()
                                , elements.getGuid()));
                        elements = new NewsElements();
                    }
                    eventType = xpp.next();
                }
                return newsElementsArrayList;
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }


}