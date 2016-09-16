package com.example.lenovo.movies;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Lenovo on 9/10/2016.
 */
public class Save {
    Context context;
    private SharedPreferences.Editor set;
    private SharedPreferences get;
    String de="asda";
    public Save(Context context){
        this.context=context;
        set=context.getSharedPreferences("save",context.MODE_PRIVATE).edit();
        get=context.getSharedPreferences("save",Context.MODE_PRIVATE);
    }

    public void setSave(boolean b){
        set.putBoolean("save",b);
        set.apply();
    }

    public boolean getSave(){
        return get.getBoolean("save",false);
    }

    public void setName(String s){
        set.putString("name",s);
        set.apply();
    }

    public String getName(){
        return get.getString("name",de);
    }

    public void setBack(String s){
        set.putString("back",s);
        set.apply();
    }

    public String getBack(){
        return get.getString("back",de);
    }

    public void setDesc(String s){
        set.putString("desc",s);
        set.apply();
    }

    public String getDesc(){
        return get.getString("desc",de);
    }

    public void setDate(String s){
        set.putString("date",s);
        set.apply();
    }

    public String getDate(){
        return get.getString("date",de);
    }

    public void setRate(String s){
        set.putString("rate",s);
        set.apply();
    }

    public String getRate(){
        return get.getString("rate",de);
    }


}
