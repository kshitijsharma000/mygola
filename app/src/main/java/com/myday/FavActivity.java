package com.myday;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class FavActivity extends AppCompatActivity {

    LinearLayout ll_back_button;
    ProgressDialog dialog;
    RecyclerView recyclerView;
    ArrayList<ActivityData> activityDatas;
    Adapter_ActivityView adapter_favView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);
        ll_back_button = (LinearLayout) findViewById(R.id.fav_activity_button_back);
        recyclerView = (RecyclerView) findViewById(R.id.fav_activity_recycler_view);

        ll_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView.addOnItemTouchListener(new DragController(this, recyclerView, new Clicklistener() {
            @Override
            public void Onclick(View view, int position) {
                System.out.println("inside fav on click : " + position);
                ActivityData data = adapter_favView.getItem(position);
                Intent intent = new Intent(FavActivity.this, DetailActivity.class);
                intent.putExtra("activityObject", data);
                startActivity(intent);
            }

            @Override
            public void OnLongclick(View view, int position) {
                System.out.println("inside activity on long click : " + position);
            }
        }));


        adapter_favView = new Adapter_ActivityView(new ArrayList<ActivityData>(), this);
        get_data_from_db();
        update_recyclerview();
    }

    private void get_data_from_db() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setTitle("Loading");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();

        activityDatas = new ArrayList<>();
        activityDatas = HomeActivity.controller.get_favorites();
        if (activityDatas == null) {
            Toast.makeText(getApplicationContext(), "NO favorites yet", Toast.LENGTH_LONG);
            dialog.hide();
            finish();
        }
        adapter_favView.setItemList(activityDatas);
        adapter_favView.notifyDataSetChanged();
        dialog.hide();
    }

    private void update_recyclerview() {
        recyclerView.setAdapter(adapter_favView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SimpleSpaceDecorator(15, 10));
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
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
