package Classes;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.cria.agora.R;

public class SoundPlayer {

    private static SoundPool soundPool;
    final int SOUND_POOL_MAX = 2;
    private AudioAttributes audioAttributes;
    private static int clickSound;
    private static int correctSound;
    private static int completedSound;
    private static int wrongSound;
    private static int dingSound;
    private static int successSound;

    public SoundPlayer(Context context){

        //SoundPool is deprecated in API level 21 (Lollipop)
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();


            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(SOUND_POOL_MAX)
                    .build();
        }else{
            //SoundPool (int maxStreams, int streamType, int srcQuality)
            soundPool= new SoundPool(SOUND_POOL_MAX, AudioManager.STREAM_MUSIC, 0);
        }




        clickSound = soundPool.load(context, R.raw.click, 1);
        correctSound = soundPool.load(context, R.raw.correct, 1);
        completedSound = soundPool.load(context, R.raw.completed, 1);
        wrongSound = soundPool.load(context, R.raw.wrong, 1);
        dingSound = soundPool.load(context, R.raw.ding, 1);
        successSound = soundPool.load(context, R.raw.success, 1);

    }
    public void playClickSound(){
        //play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        soundPool.play(clickSound, 1.0f, 1.0f, 1, 0, 1.0f);

    }
    public void playCorrectSound(){
        //play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        soundPool.play(correctSound, 1.0f, 1.0f, 1, 0, 1.0f);

    }
    public void playCompletedSound(){
        //play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        soundPool.play(completedSound, 1.0f, 1.0f, 1, 0, 1.0f);

    }
    public void playWrongSound(){
        //play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        soundPool.play(wrongSound, 1.0f, 1.0f, 1, 0, 1.0f);

    }
 public void playDingSound(){
        //play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        soundPool.play(dingSound, 1.0f, 1.0f, 1, 0, 1.0f);

    }
 public void playSuccessSound(){
        //play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        soundPool.play(successSound, 1.0f, 1.0f, 1, 0, 1.0f);

    }

}
