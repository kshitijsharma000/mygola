package com.myday;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.myday.network.Image_Handler;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    TextView discount;
    TextView loc;
    TextView city;
    TextView price;
    TextView desc;
    ImageView imageView;
    TextView name;
    RatingBar ratingBar;
    LinearLayout ll_back_button;
    LinearLayout ll_fav_button;
    LinearLayout ll_share_button;
    LinearLayout ll_sms_button;


    String image_url;
    String rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        ActivityData data = (ActivityData) intent.getExtras().get("activityObject");
        if (data == null) {
            System.out.println("Error in getting data");
            this.finish();
        }

        discount = (TextView) findViewById(R.id.detail_activity_discount);
        loc = (TextView) findViewById(R.id.detail_activity_location);
        city = (TextView) findViewById(R.id.detail_activity_city);
        price = (TextView) findViewById(R.id.detail_activity_price);
        desc = (TextView) findViewById(R.id.detail_activity_desc);
        name = (TextView) findViewById(R.id.detail_activity_name);
        imageView = (ImageView) findViewById(R.id.detail_activity_image);
        ratingBar = (RatingBar) findViewById(R.id.detail_activity_rating);

        ll_back_button = (LinearLayout) findViewById(R.id.detail_activity_ll_button_back);
        ll_fav_button = (LinearLayout) findViewById(R.id.detail_activity_ll_button_favorite);
        ll_share_button = (LinearLayout) findViewById(R.id.detail_activity_ll_button_share);
        ll_sms_button = (LinearLayout) findViewById(R.id.detail_activity_ll_button_SMS);

        ll_sms_button.setOnClickListener(this);
        ll_share_button.setOnClickListener(this);
        ll_fav_button.setOnClickListener(this);
        ll_back_button.setOnClickListener(this);

        discount.setText(String.valueOf(data.getDiscount()) + " %");
        city.setText(data.getCity());
        loc.setText(data.getLocation());
        desc.setText(data.getDesc());
        name.setText(data.getName());
        ratingBar.setRating(data.getRating());
        price.setText(calculatePrice(data.getActual_price(), data.getDiscount()));

        image_url = data.getImage_url();
        setImage(image_url);

    }

    private String calculatePrice(Float price, Float discount) {
        Float temp;
        temp = price - (price * (discount / 100));
        return String.valueOf(temp);
    }

    private void setImage(String image_url) {
        if (image_url == null) {
            System.out.println("image url empty");
            return;
        }
        Image_Handler.get_image_from_url(image_url, imageView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_activity_ll_button_back:
                onBackPressed();
                break;
            case R.id.detail_activity_ll_button_favorite:
                HomeActivity.controller.set_favorite_to_db(city.getText().toString(),name.getText().toString());
                break;
            case R.id.detail_activity_ll_button_share:
                share_to_other();
                break;
            case R.id.detail_activity_ll_button_SMS:
                send_sms();
                break;
        }
    }

    private void send_sms() {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.putExtra("sms_body", "Enjoying " + name.getText().toString() + " at " + city.getText().toString()
                + " sponsered by MyDay App");
        sendIntent.setType("vnd.android-dir/mms-sms");
        startActivity(sendIntent);
    }

    private void share_to_other() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Enjoying " + name.getText().toString() + " at " + city.getText().toString()
                + " sponsered by MyDay App");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share Text to.."));
    }
}
