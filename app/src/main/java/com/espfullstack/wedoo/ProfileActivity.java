package com.espfullstack.wedoo;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.espfullstack.wedoo.session.SessionMannager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @BindView(R.id.user_profile_image)
    ImageView userPhoto;

    @BindView(R.id.user_profile_name)
    TextView userName;

    @BindView(R.id.user_profile_email)
    TextView userMail;

    @BindView(R.id.btn_logout)
    Button btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();

        if (auth != null) {
            FirebaseUser user = auth.getCurrentUser();

            if (user != null) {
                userName.setText(user.getDisplayName());
                userMail.setText(user.getEmail());
                Uri photoUrl = user.getPhotoUrl();
                if (photoUrl == null) {
                    photoUrl = Uri.parse("https://goo.gl/pNsBHk");
                }
                Glide.with(this).load(photoUrl).into(userPhoto);
            }
            else {
                redirectToLogin();
            }
        }
    }

    private void redirectToLogin() {
        SessionMannager.clearToken(getApplicationContext());

        Intent signInIntent = new Intent(this.getApplicationContext(), LoginActivity.class);
//        signInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_AC);
        signInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(signInIntent);
        finish();
    }

    @OnClick(R.id.btn_logout)
    public void logOut(){
        auth.signOut();
        redirectToLogin();
        Toast.makeText(this, "Log out done!", Toast.LENGTH_SHORT).show();
    }

}
