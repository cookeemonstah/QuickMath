package jeremy.applock.quickmath;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import java.util.Locale;
import java.util.Random;

public class Basic extends AppCompatActivity {

    public int LoopNumberB;
    public TextView t1;
    public EditText e1;
    public int Solution;
    public long StartTime;
    public long EndTime;
    public String Best;
    public AlertDialog.Builder builder;
    public TextView secondView;
    public TextView t4;
    public Animation shake;
    int prevrandom;
    int prevrandom2;
    private PublisherInterstitialAd mPublisherInterstitialAd;


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Basic.this, MainActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy);
        MobileAds.initialize(this, "ca-app-pub-4066890588601170~3417050817");

        mPublisherInterstitialAd = new PublisherInterstitialAd(this);
        mPublisherInterstitialAd.setAdUnitId("ca-app-pub-4066890588601170/4335504524");
        mPublisherInterstitialAd.loadAd(new PublisherAdRequest.Builder().build());
        prevrandom = 0;
        prevrandom2 = 0;
        View someView = findViewById(R.id.container);
        someView.setBackgroundColor(Color.parseColor("#80CBC4"));
        shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        AssetManager am = this.getApplicationContext().getAssets();
        builder = new AlertDialog.Builder(this);

        t4 = (TextView) findViewById(R.id.t4);
        t4.setText ("0/15");

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Best = sharedPref.getString("Best4", "999999");
        //now get Editor
        final SharedPreferences.Editor editor = sharedPref.edit();
        t1 = (TextView) findViewById(R.id.t3);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/oj.ttf");
        t1.setTypeface(typeface);
        t4.setTypeface(typeface);
        //number of loops
        LoopNumberB = 15;
        e1 = (EditText) findViewById(R.id.editText);

        //focus on text
        e1.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        Solution = loop();
        StartTime = System.currentTimeMillis();
        e1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    if (LoopNumberB >= 0 && Integer.parseInt(e1.getText().toString()) == Solution) {
                        e1.setText("");
                        Solution = loop();
                    }
                    else if (LoopNumberB <= 0){
                        EndTime = System.currentTimeMillis() - StartTime;
                        EndTime = EndTime / 1000;
                        //rounding
                        int counter = 0;
                        while (counter < EndTime){
                            counter++;
                        }

                        if (Integer.parseInt(Best) > counter){
                            String newString = Integer.toString(counter);
                            editor.putString("Best4", newString);
                            editor.commit();
                        }
                        int randomiser = new Random().nextInt(2);
                        if (mPublisherInterstitialAd.isLoaded() && (randomiser == 1)) {
                            mPublisherInterstitialAd.show();
                        } else {
                            Log.d("TAG", "The interstitial wasn't loaded yet.");
                        }
                        builder.setTitle("")
                                .setMessage("New   " + counter + " s\n\n" + "Best   " + sharedPref.getString("Best4", "999999") + " s\n")
                                .setPositiveButton(" Menu                   ", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Basic.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton(" Restart           ", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Basic.this, Load.class);
                                        startActivity(intent);
                                    }
                                });


                        AlertDialog dialog = builder.show();
                        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/oj.ttf");
                        Button btn1 = (Button) dialog.findViewById(android.R.id.button1);
                        btn1.setTypeface(face);
                        btn1.setTextColor(Color.parseColor("#000000"));
                        btn1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_menu_black_24dp, 0, 0, 0);
                        Button btn2 = (Button) dialog.findViewById(android.R.id.button2);
                        btn2.setTypeface(face);
                        btn2.setTextColor(Color.parseColor("#000000"));
                        btn2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_play_arrow_black_24dp, 0, 0, 0);
                        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                        textView.setTextSize(40);
                        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                        textView.setTypeface(face);
                        /*
                        editor.putString("TIME", newString);
                        //commits your edits
                        editor.commit();
                        Intent intent = new Intent(Easy.this, Final.class);
                        startActivity(intent);
                        */
                    }
                    else {
                        e1.startAnimation(shake);
                    }
                    e1.setText("");
                    return true;
                }
                return false;
            }
        });
    }

    private int loop() {
        int answer = 0;
        int random = new Random().nextInt(5) + 1;
        int secondrandom = new Random().nextInt(5) + 1;

        if ((prevrandom == random) && (prevrandom2 == secondrandom)){
            random = random + 1;
        }

        prevrandom = random;
        prevrandom2 = secondrandom;

        int randomSign = new Random().nextInt(3);

        if (randomSign == 0) {
            t1.setText(random + " x " + secondrandom);
            answer = random * secondrandom;
        }
        else if (randomSign == 1){
            t1.setText(random + " + " + secondrandom);
            answer = random + secondrandom;
        }
        else if (randomSign == 2){
            if (random >= secondrandom) {
                t1.setText(random + " - " + secondrandom);
                answer = random - secondrandom;
            }
            else {
                t1.setText(secondrandom + " - " + random);
                answer = secondrandom - random;
            }
        }

        LoopNumberB = LoopNumberB - 1;
        int current = 14 - LoopNumberB;
        String score = current + "/15";
        t4.setText(score);
        return answer;
    }
}

