package com.hjw.cet4.utils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.media.SoundPool;

abstract class OnLoadCompleteListener {    
    final int testPeriodMs = 100; // period between tests in ms

     /**
     * OnLoadCompleteListener fallback implementation for Android versions before 2.2. 
     * After using: int soundId=SoundPool.load(..), call OnLoadCompleteListener.listenFor(soundId)
     * to periodically test sound load completion. If a sound is playable, onLoadComplete is called.
     *
     * @param soundPool  The SoundPool in which you loaded the sounds. 
     */
    public OnLoadCompleteListener(SoundPool soundPool) {
        testSoundPool = soundPool;
    }

    /**
     * Method called when determined that a soundpool sound has been loaded. 
     *
     * @param soundPool  The soundpool that was given to the constructor of this OnLoadCompleteListener
     * @param soundId    The soundId of the sound that loaded
     * @param status     Status value for forward compatibility. Always 0.  
     */
    public abstract void onLoadComplete(SoundPool soundPool, int soundId, int status); // implement yourself

     /**
     * Method to add sounds for which a test is required. Assumes that SoundPool.load(soundId,...) has been called.
     *
     * @param soundPool  The SoundPool in which you loaded the sounds. 
     */
    public void addSound(int soundId) {
        boolean isFirstOne;
        synchronized (this) {
            mySoundIds.add(soundId);
            isFirstOne = (mySoundIds.size()==1);
        }
        if (isFirstOne) {
            // first sound, start timer
            testTimer = new Timer();
            TimerTask task = new TimerTask() { // import java.util.TimerTask for this
                @Override
                public void run() {
                    testCompletions();
                }  
            };
            testTimer.scheduleAtFixedRate(task , 0, testPeriodMs);
        }
    }

    private ArrayList<Integer> mySoundIds = new ArrayList<Integer>();
    private Timer testTimer;  // import java.util.Timer for this
    private SoundPool testSoundPool;

    private synchronized void testCompletions() {
        ArrayList<Integer> completedOnes = new ArrayList<Integer>();
        for (Integer soundId: mySoundIds) {
            int streamId = testSoundPool.play(soundId, 0, 0, 0, 0, 1.0f);
            if (streamId>0) {                   // successful
                testSoundPool.stop(streamId);
                onLoadComplete(testSoundPool, soundId, 0); 
                completedOnes.add(soundId);
            }
        }
        mySoundIds.removeAll(completedOnes);
        if (mySoundIds.size()==0) {
            testTimer.cancel();
            testTimer.purge();
        }
    }
}