package com.espfullstack.wedoo;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.user_profile_image1)
    ImageView userPhoto1;

    @BindView(R.id.user_profile_image2)
    ImageView userPhoto2;

    @BindView(R.id.user_profile_image3)
    ImageView userPhoto3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ButterKnife.bind(this);

        Glide.with(this).load(R.drawable.richiely)
                .apply(RequestOptions.bitmapTransform(new CircleCrop())).into(userPhoto1);

        Glide.with(this).load(R.drawable.filipe)
                .apply(RequestOptions.bitmapTransform(new CircleCrop())).into(userPhoto2);

        Glide.with(this).load(R.drawable.mateus)
                .apply(RequestOptions.bitmapTransform(new CircleCrop())).into(userPhoto3);
    }

    public void openBrowser(View view) {
        String url = (String)view.getTag();

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);

        intent.setData(Uri.parse(url));

        startActivity(intent);
    }

}
