package com.myday;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.myday.db.MydayContract;
import com.myday.network.Appcontroller;
import com.myday.network.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ActivityData> activityDatas;
    Adapter_ActivityView adapter_activityView;
    TextView textView;
    TextView count;
    ProgressDialog dialog;
    RadioGroup radioGroup;
    Spinner spinner;
    String city;
    static DbController controller;
    HashSet<String> set_citySpinner;
    ArrayAdapter<String> adapter_spinner;
    LinearLayout layout_sortButton;
    LinearLayout layout_favoritebutton;
    boolean temp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView = (RecyclerView) findViewById(R.id.activity_recycler_view);
        textView = (TextView) findViewById(R.id.api_hit);
        count = (TextView) findViewById(R.id.activity_count);
        radioGroup = (RadioGroup) findViewById(R.id.radiogrp1);
        spinner = (Spinner) findViewById(R.id.city_spinner);
        layout_sortButton = (LinearLayout) findViewById(R.id.ll_button);
        layout_favoritebutton = (LinearLayout) findViewById(R.id.ll_favorites);

        controller = new DbController(this);

        count.setText("0");
        radioGroup.check(R.id.rb_price);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading Data..");
        dialog.setTitle("Loading");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();

        layout_favoritebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,FavActivity.class);
                startActivity(intent);
            }
        });

        layout_sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                String sortby;
                String title = radioButton.getText().toString();
                System.out.println("radio button checked : " + title);
                dialog = new ProgressDialog(HomeActivity.this);
                dialog.setTitle("Loading");
                dialog.setMessage("Loading...");
                dialog.show();

                if (title.equalsIgnoreCase(MydayContract.Activity_entry.COL_PRICE)) {
                    sortby = MydayContract.Activity_entry.COL_PRICE;
                } else if (title.equalsIgnoreCase(MydayContract.Activity_entry.COL_RATING)) {
                    sortby = MydayContract.Activity_entry.COL_RATING;
                } else {
                    sortby = MydayContract.Activity_entry.COL_DISCOUNT;
                }

                activityDatas.clear();
                activityDatas = controller.get_sorted_data_from_db(sortby, city);
                adapter_activityView.setItemList(activityDatas);
                adapter_activityView.notifyDataSetChanged();

                dialog.hide();
            }
        });

        set_citySpinner = new HashSet<>();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String city = parent.getItemAtPosition(position).toString();
                HomeActivity.this.city = city;
                if (!temp) {
                    temp = true;
                    return;
                }
                System.out.println("item seletecd : " + parent.getItemAtPosition(position));

                dialog.setMessage("Loading Data..");
                dialog.setTitle("Loading");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.show();

                activityDatas.clear();
                activityDatas = controller.get_city_wise_data(city);
                adapter_activityView.setItemList(activityDatas);
                adapter_activityView.notifyDataSetChanged();

                dialog.hide();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        adapter_activityView = new Adapter_ActivityView(new ArrayList<ActivityData>(), this);
        get_data_from_server();
        update_recyclerview();

        recyclerView.addOnItemTouchListener(new DragController(this, recyclerView, new Clicklistener() {
            @Override
            public void Onclick(View view, int position) {
                System.out.println("inside activity on click : " + position);
                ActivityData data = adapter_activityView.getItem(position);
                Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
                intent.putExtra("activityObject", data);
                startActivity(intent);
            }

            @Override
            public void OnLongclick(View view, int position) {
                System.out.println("inside activity on long click : " + position);
            }
        }));

    }

    private void get_data_from_server() {
        String url = Constants.base_url + Constants.activity_list;
        String api_url = Constants.base_url + Constants.api_hits;
        System.out.println("url : " + url);

        JsonObjectRequest list_request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                System.out.println("got data : " + jsonObject.toString());
                activityDatas = new ArrayList<>();
                try {
                    JSONArray jArray = jsonObject.getJSONArray("activities");
                    for (int i = 0; i < jArray.length(); i++) {
                        add_to_dataset(jArray.getJSONObject(i));
                    }
                    adapter_activityView.setItemList(activityDatas);
                    adapter_activityView.notifyDataSetChanged();

                    String[] temp = new String[set_citySpinner.size()];
                    set_citySpinner.toArray(temp);
                    adapter_spinner = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, temp);
                    spinner.setAdapter(adapter_spinner);

                    update_db();

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("error in json parsing");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("error in getting data");
            }
        });

        JsonObjectRequest api_hits_request = new JsonObjectRequest(Request.Method.GET, api_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                System.out.println("got data : " + jsonObject.toString());
                try {
                    textView.setText(jsonObject.getString("api_hits"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("error in json parsing api hits");
                }
                count.setText(String.valueOf(adapter_activityView.getItemCount()));

                if (dialog.isShowing()) {
                    dialog.hide();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("error in getting data");
            }
        });

        Appcontroller.getmInstance().addtoRequestqueue(list_request);
        Appcontroller.getmInstance().addtoRequestqueue(api_hits_request);
    }

    private void update_recyclerview() {
        recyclerView.setAdapter(adapter_activityView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SimpleSpaceDecorator(15, 10));
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void add_to_dataset(JSONObject jObject) {
        ActivityData activityData = new ActivityData();
        try {
            activityData.setName(jObject.getString("name"));
            activityData.setImage_url(jObject.getString("image"));
            activityData.setActual_price(Float.valueOf(jObject.getString("actual_price")));
            activityData.setDiscount(Float.valueOf(jObject.getString("discount").replace("%","")));
            activityData.setRating(Float.valueOf(jObject.getString("rating")));
            activityData.setCity(jObject.getString("city"));
            activityData.setLocation(jObject.getString("location"));
            activityData.setDesc(jObject.getString("description"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        activityDatas.add(activityData);
        set_citySpinner.add(activityData.getCity());
    }

    private void update_db() {
        //create new thread for db creation for activites from the server
        controller.createActivityDB(activityDatas);
    }

    private class DragController implements RecyclerView.OnItemTouchListener {
        private RecyclerView recyclerView;
        private GestureDetector gestureDetector;
        private Clicklistener clicklistener;

        public DragController(Context context, final RecyclerView recyclerView, final Clicklistener clicklistener) {
            this.recyclerView = recyclerView;
            this.clicklistener = clicklistener;
            this.gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    //return super.onSingleTapUp(e);
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clicklistener != null) {
                        clicklistener.OnLongclick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
                clicklistener.Onclick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public static interface Clicklistener {
        public void Onclick(View view, int position);

        public void OnLongclick(View view, int position);
    }
}
