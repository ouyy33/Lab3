package com.ouyangyi.lab3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jay on 2017/10/25.
 */

public class InfoActivity extends AppCompatActivity{
    private List<Map<String, Object>> itemList = new ArrayList<>();
    private String[]More = new String[]{"一键下单", "分享商品", "不感兴趣", "查看更多商品促销信息"};

    private int flag=0;  //收藏星星状态的转换
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more);

        for(int i=0; i<More.length;i++){
            Map<String, Object> temp = new LinkedHashMap<>();
            temp.put("moreOpt", More[i]);
            itemList.add(temp);
        }

        ListView listView = (ListView)findViewById(R.id.listView_info);
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, itemList, R.layout.item,
                new String[]{"moreOpt"}, new int[]{R.id.bottom_content});
        listView.setAdapter(simpleAdapter);


        Intent intent= getIntent();
        final Bundle bundle= intent.getExtras();
        final Goods goods= (Goods)bundle.getSerializable("goods");
        final int position= (int) bundle.getInt("position");
        TextView name= (TextView)findViewById(R.id.info_name);
        TextView price= (TextView)findViewById(R.id.info_price);
        TextView info= (TextView)findViewById(R.id.info1);
        ImageView picture= (ImageView)findViewById(R.id.image);
        name.setText(goods.getName());
        price.setText(goods.getPrice());
        info.setText(goods.getInfo());
        picture.setImageResource(goods.getPicId());

        final ImageView buy= (ImageView)findViewById(R.id.shop);
        buy.setTag(true);
        buy.setOnClickListener(
                new View.OnClickListener(){
            @Override
            public void onClick(View view){
                buy.setTag(false);
                Toast.makeText(InfoActivity.this,"商品已加到购物车",Toast.LENGTH_LONG).show();
            }
        });

        //定义返回按钮的功能
        ImageView back =(ImageView)findViewById(R.id.Back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1= new Bundle();
                bundle1.putInt("position",position);
                if((Boolean)buy.getTag() ==false){
                    bundle1.putBoolean("added",true);
                }
                else{
                    bundle1.putBoolean("added",false);
                }
                Intent intent1= new Intent();
                intent1.putExtras(bundle1);
                setResult(RESULT_OK,intent1);
                finish();
            }
        });

        //收藏功能
        final ImageView collect= (ImageView)findViewById(R.id.star);
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag==0){
                    collect.setImageResource(R.mipmap.full_star);
                    flag=1;
                }
                else if(flag==1){
                    collect.setImageResource(R.mipmap.empty_star);
                    flag=0;
                }
            }
        });



    }

}
