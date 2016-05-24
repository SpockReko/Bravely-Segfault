package com.games.monaden.control;

import com.games.monaden.services.audioplayer.AudioPlayer;

import java.io.File;

/**
 * Created by Admin on 2016-05-21.
 */
public class AudioController {
    AudioPlayer musicPlayer;

    public AudioController(){
        musicPlayer = new AudioPlayer();
    }

    // Changing the music by keys in the game.
    public void playMusic(int i){
        musicPlayer.playMusic(i);
    }

    // Stop the current music
    public void stopMusic(){
        musicPlayer.stopMusic();
    }

    // Loaded from mapname.xml for the level-music.
    public void playMusic(String filepath) { musicPlayer.playMusic(filepath); }

    // Loaded soundEffects
    public void playSound(String filepath){
        musicPlayer.playSound(filepath);
    }

    public double volumeDown(){
        return musicPlayer.volumeDown();
    }

    public double volumeUp(){
        return musicPlayer.volumeUp();
    }

}
