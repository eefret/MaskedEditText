package com.kaissersoft.MaskedEditText.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import static android.view.View.*;
import static android.widget.TextView.*;
import android.widget.EditText;
import android.widget.TextView;
import com.kaissersoft.MaskedEditText.R;

import java.util.Arrays;

/**
 * Created by Christopher Herrera on 3/17/14.
 */
public class MaskedEditText extends EditText implements InputFilter{

    //==================================================================================================================
    //CONSTANTS
    //==================================================================================================================
    private static final char NUMBER_KEY = '#';
    private static final char LETTER_KEY = '*';
    private static final char SIGN_KEY = '?';
    private static final String ALLOWED_LETTER_KEYS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String ALLOWED_SIGNS_KEYS = "/-_()$.";
    private static final String TAG = "MaskedEditText";

    //==================================================================================================================
    //FIELDS
    //==================================================================================================================
    private String mask;
    private short maxLength;
    private boolean inited =false;
    //==================================================================================================================
    //CONSTRUCTORS
    //==================================================================================================================

    public MaskedEditText(Context context) {
        super(context);
        init();
    }

    public MaskedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.MaskedText);
        mask = attributes.getString(R.styleable.MaskedText_mask);
        init();
    }

    public MaskedEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    //==================================================================================================================
    //OVERRIDEN METHODS
    //==================================================================================================================


    //OVERRIDEN FROM INPUTFILTER-----------------------------------------------------------------------------------START
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if(inited){
            try {
                if(this.getSelectionStart() == this.maxLength ){
                    return "";
                }
                char sourceChar = source.charAt(start);
                char maskedChar = mask.charAt(getSelectionStart());
                if (validateChar(sourceChar,maskedChar)){
                    return null;
                }else{
                    if (ALLOWED_SIGNS_KEYS.indexOf(maskedChar)!=-1){
                        if (validateChar(sourceChar,(mask.charAt(getSelectionStart()+1)))){
                            return maskedChar+""+sourceChar;
                        }
                        return maskedChar+"";
                    }
                    return "";
                }
            }catch (IndexOutOfBoundsException e){
                return null;
            }
        }else{ return null;}
    }
    //OVERRIDEN FROM INPUTFILTER-------------------------------------------------------------------------------------END

    //==================================================================================================================
    //METHODS
    //==================================================================================================================

    /**
     * Author: Christopher herrera (Eefret)
     * Method that init the widget, applying the TextWatcher to the custom Widget
     */
    private void init() {
        this.setFilters(new InputFilter[]{this});
        setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        inited = true;
    }


    private boolean validateChar(char source, char maskedChar){
        if(source==' ' && maskedChar!=' '){
            return false;
        }
        switch (maskedChar){
            case NUMBER_KEY:
                return !Character.isLetter(source);
            case LETTER_KEY:
                return ALLOWED_LETTER_KEYS.indexOf(source)!=-1;
            case SIGN_KEY:
                return ALLOWED_SIGNS_KEYS.indexOf(source)!=-1;
            default:
                return source==maskedChar;
        }
    }

    private void changeKeyboard(char c){
        setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        if(c == '*'){
            setInputType(InputType.TYPE_CLASS_TEXT);
        }else if (c=='#'){
            setInputType(InputType.TYPE_CLASS_NUMBER);
        }

    }
    //==================================================================================================================
    //GETTER AND SETTERS
    //==================================================================================================================


    public void setMask(String mask) {
        this.mask = mask;
    }
}
