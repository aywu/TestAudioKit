/**
* v1.2: cleaned
*/
package com.hms.audiokit.testaudiokit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {
     private static final String TAG = MainActivity.class.getSimpleName();

    public static class PlayerCallback {
        public void play(Context context) {
            if (PlayHelper.getInstance().isPlayManagerAvailable()) {
                Log.d(TAG, "PlayerCallback(): play mode Player is ready");
                playMyList(context);
            }
        }
    }

    private static void playMyList(Context context) {
      int REPEATNG_MODE = 2;
      PlayHelper.getInstance().setPlayMode(REPEATNG_MODE);
      PlayHelper.getInstance().buildOnlineList(); // no error but could not play.
      //PlayHelper.getInstance().buildLocal(context); // works and plays the songs in the phone.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkReadPermission();
        PlayHelper.getInstance().init(MainActivity.this,
                    new MainActivity.PlayerCallback());
    }

    @Override
    protected void onStop() {
      super.onStop();

      if (PlayHelper.getInstance().isPlayManagerAvailable()) {
          PlayHelper.getInstance().pause();
      }
    }

    @Override
    protected void onResume() {
      super.onResume();
      // this starts playing a local lst if it has not been started or
      // resumes playing if it is stopped.
      if (PlayHelper.getInstance().isPlayManagerAvailable()) {
        playMyList(MainActivity.this);
      }
    }

    @Override
    protected void onDestroy() {
      super.onDestroy();
      if (PlayHelper.getInstance().isPlayManagerAvailable()) {
          PlayHelper.getInstance().stop();
      }
    }

    /**
   * checkReadPermission
   */
  private void checkReadPermission() {
      if (ContextCompat.checkSelfPermission(this,
              Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
          ActivityCompat.requestPermissions(this,
                  new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                  1);
      }
  }

}
