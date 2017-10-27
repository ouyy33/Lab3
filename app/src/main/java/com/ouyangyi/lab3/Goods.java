package com.ouyangyi.lab3;

import java.io.Serializable;

/**
 * Created by Jay on 2017/10/24.
 */


public class Goods implements Serializable {


    private String name;    //
    private String price;   //
    private String info;    //
    private int picId;      //图片ID

    Goods(String a_name, String a_price, String a_info, int a_picId){
        name = a_name;
        price = a_price;
        info = a_info;
        picId = a_picId;
    }



    public String getName(){
        return name;
    }

    public String getPrice(){
        return price;
    }

    public String getInfo(){
        return info;
    }

    public int getPicId(){return picId;}
}