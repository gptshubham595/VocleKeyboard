package com.vocle.keyboard;

import android.content.SharedPreferences;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Toast;

public class VocleKeyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView kv;
    private Keyboard keyboard;
    public SharedPreferences pre;
    private boolean isCaps = false;


    //Press Ctrl+O


    @Override
    public View onCreateInputView() {
        pre = PreferenceManager.getDefaultSharedPreferences(this);
        int theme = pre.getInt("theme", 1);

        if (theme == 1) {
            kv = (KeyboardView) this.getLayoutInflater().inflate(R.layout.keyboard, null);
            keyboard = new Keyboard(this, R.xml.qwerty);

        } else if (theme == 2) {
            kv = (KeyboardView) this.getLayoutInflater().inflate(R.layout.voclekeyboard, null);
            keyboard = new Keyboard(this, R.xml.vocle);
        } else if (theme == 3) {
            kv = (KeyboardView) this.getLayoutInflater().inflate(R.layout.voclekeyboard, null);
            keyboard = new Keyboard(this, R.xml.voclerecording);
        } else {
            kv = (KeyboardView) this.getLayoutInflater().inflate(R.layout.voclekeyboard, null);
            keyboard = new Keyboard(this, R.xml.voclechoose);
        }

        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        return kv;
    }

    @Override
    public void onPress(int i) {

    }

    @Override
    public void onRelease(int i) {

    }

    @Override
    public void onKey(int i, int[] ints) {

        InputConnection ic = getCurrentInputConnection();
        playClick(i);
        switch (i) {
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1, 0);
                break;

            case Keyboard.KEYCODE_SHIFT:
                isCaps = !isCaps;
                keyboard.setShifted(isCaps);
                kv.invalidateAllKeys();
                break;
            case 208:
                try {
                    pre = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = pre.edit();
                    editor.putInt("theme", 4);
                    editor.apply();

                    onStartInputView(getCurrentInputEditorInfo(), true);
                } catch (Exception e) {
                    onFinishInput();
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }
                break;
            case 200:
                try {
                    pre = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = pre.edit();
                    editor.putInt("theme", 2);
                    editor.apply();

                    onStartInputView(getCurrentInputEditorInfo(), true);
                } catch (Exception e) {
                    onFinishInput();
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }
                break;
            case 204:
                try {
                    pre = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = pre.edit();
                    editor.putInt("theme", 1);
                    editor.apply();
                    onStartInputView(getCurrentInputEditorInfo(), true);
                } catch (Exception e) {
                    onFinishInput();
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }
                break;
            case 206:
            case 400:
                break;
            case 202:
                try {
                    pre = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = pre.edit();
                    editor.putInt("theme", 3);
                    editor.apply();
                    onStartInputView(getCurrentInputEditorInfo(), true);
                } catch (Exception e) {
                    onFinishInput();
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;

            default:
                char code = (char) i;
                if (Character.isLetter(code) && isCaps)
                    code = Character.toUpperCase(code);
                ic.commitText(String.valueOf(code), 1);
        }

    }

    private void playClick(int i) {

        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        switch (i) {
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case Keyboard.KEYCODE_DONE:
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }

    @Override
    public void onText(CharSequence charSequence) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    public void onStartInputView(EditorInfo attribute, boolean restarting) {
        super.onStartInputView(attribute, restarting);
        try {
            //set time in mili
            Thread.sleep(80);
            setInputView(onCreateInputView());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFinishInputView(boolean finishingInput) {
        pre = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pre.edit();
        editor.putInt("theme", 1);
        editor.apply();

        super.onFinishInputView(finishingInput);
    }

    @Override
    public void onDestroy() {
        pre = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pre.edit();
        editor.putInt("theme", 1);
        editor.apply();
        super.onDestroy();
    }

}
