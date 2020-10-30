package reo.gameapp;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPlayer {

    private static SoundPool music;
    private static int hitSound;
    private static int overSound;

    public SoundPlayer(Context context) {
        SoundPool music = new SoundPool(2, AudioManager.STREAM_MUSIC,0);

        hitSound = music.load(context,R.raw.hit,1);
        overSound = music.load(context,R.raw.over,1);
    }

    public void playHitSound() {music.play(hitSound,1.0f,1.0f,1,0,1.0f);
    }

    public void playOverSound(){ music.play(overSound,1.0f,1.0f,1,0,1.0f);
    }

    }