package com.example.milestone06;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    private ImageSlider imageSlider;

    CardView cardView1, cardView2,cardView3;

//    Animation anim_from_button,anim_from_top,anim_from_left;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        imageSlider = findViewById(R.id.imgslide);

        ArrayList<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel("https://lh4.googleusercontent.com/6qBPqbdY-NLwYb9kmo4a4PN6ecf9vfVpTnr1-YvYLW9AOEdvocZnmt71kNeJIWseWxI=w2400", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://lh3.googleusercontent.com/eQWAjVNOjjb8lANJOCvRALIhqRXvMD74Ulb5qpAJXjaYyKa6M5rWydraGQDxejCWSn4=w2400", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://lh3.googleusercontent.com/vDYMssKHhq-G2u-xkbaHCtwmbLZX_ptyK_emaTmen9vaMcFlEhTCH3dfthP2eEfvgek=w2400", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://lh5.googleusercontent.com/FdB7lLdqSl_seaM-QVKCQudAxftmdBJ9AbRPU0wlDfy3wdllKyvDViSbE9V3QO3ZWU8=w2400", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://lh3.googleusercontent.com/O9K6FxxIyMF_61MU12v3Ff47x4fxNO3KFwWu6mEu8cgAP3lz9uVN1a7Jw3HbmTnYQ0Q=w2400", ScaleTypes.FIT));

        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        //Scroll View
        cardView1 = findViewById(R.id.cdView1);
        cardView2 = findViewById(R.id.cdView2);
        cardView3 = findViewById(R.id.cdView3);

        //Load Animation
//        anim_from_button = AnimationUtils.loadAnimation(this,R.anim.anim_from_bottom);
//        anim_from_top = AnimationUtils.loadAnimation(this,R.anim.anim_from_top);
//        anim_from_left = AnimationUtils.loadAnimation(this,R.anim.anim_from_left);
//
//        cardView1.setAnimation(anim_from_button);
//        cardView2.setAnimation(anim_from_button);
//        cardView3.setAnimation(anim_from_button);


        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent secondActivity = new Intent(DashboardActivity.this,SecondActivity.class);
                startActivity(secondActivity);
            }
        });



    }
}