package com.earningfever.doublediceroller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;



import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final Random RANDOM = new Random();
    private Button rollDiceButton;
    private ImageView diceImage1, diceImage2;
    private TextView totalValue;
    private int diceValue1 = 0, diceValue2 = 0;
    private MediaPlayer mediaPlayer;
    private MediaPlayer outPutSound;
    private Switch vibrationCheck,speakCheck;
    private ImageView frogImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rollDiceButton =  findViewById(R.id.rollDices);
        diceImage1 =  findViewById(R.id.imageView1);
        diceImage2 =  findViewById(R.id.imageView2);
        totalValue = findViewById(R.id.total);
        vibrationCheck = findViewById(R.id.vibrationCheck);
        speakCheck = findViewById(R.id.speakCheck);
        frogImage =findViewById(R.id.frog_gif);
        mediaPlayer = MediaPlayer.create(this,R.raw.shake_sound);

        final Vibrator vibe = (Vibrator) MainActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
         this.getSystemService(Context.VIBRATOR_SERVICE);

         final Animation bounceAnimation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.bounce_anim);
         //bounceAnimation.setRepeatCount(Animation.INFINITE);


        rollDiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rollDiceButton.setEnabled(false);
                if(mediaPlayer != null  ){
                   // mediaPlayer.stop();
                }
                if(outPutSound != null && speakCheck.isChecked()){
                    outPutSound.stop();
                    outPutSound.release();
                }
                if(vibrationCheck.isChecked()){
                    vibe.vibrate(200);
                }
                final Animation anim1 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_anim);
                final Animation anim2 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_anim);
                final Animation.AnimationListener animationListener = new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        mediaPlayer.start();
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        int value = randomDiceValue();
                        int res = getResources().getIdentifier("dice_value_" + value, "drawable", "com.earningfever.doublediceroller");

                        if (animation == anim1) {
                            diceImage1.setImageResource(res);
                            diceValue1 = value;
                        } else if (animation == anim2) {
                            diceImage2.setImageResource(res);
                            diceValue2 = value;

                            totalValue.setText( (diceValue1 + diceValue2)+"");

                            int resSound = getResources().getIdentifier("sound_value_" +(diceValue1 + diceValue2), "raw", "com.earningfever.doublediceroller");
                            if(speakCheck.isChecked()){
                                playSound(resSound);
                            }
                            if(diceValue1 + diceValue2 > 9){
                                frogImage.startAnimation(bounceAnimation);
                            }
                            rollDiceButton.setEnabled(true);
                        }


                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                };

                anim1.setAnimationListener(animationListener);
                anim2.setAnimationListener(animationListener);

                diceImage1.startAnimation(anim1);
                diceImage2.startAnimation(anim2);

            }

            private void playSound(int resSound) {
                outPutSound = MediaPlayer.create(MainActivity.this,resSound);
                outPutSound.start();
            }
        });

    }

    public static int randomDiceValue() {
        return RANDOM.nextInt(6) + 1;
    }
}
