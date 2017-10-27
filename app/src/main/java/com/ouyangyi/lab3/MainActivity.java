package com.ouyangyi.lab3;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;


public class MainActivity extends AppCompatActivity {

    private List<Goods> itemList_goods = new ArrayList<>();  //商品详情的信息
    private List<Map<String, Object>> itemList = new ArrayList<>();  //商品列表
    private List<Goods> itemList2_goods = new ArrayList<>();
    private List<Map<String, Object>> itemList2 = new ArrayList<>();  //购物车中的商品列表
    private String[] goodsName = new String[]{"Enchated Forest", "Arla Milk", "Devondale Milk", "Kindle Oasis",
            "waitrose 早餐麦片", "Mcvitie's 饼干", "Ferrero Rocher", "Maltesers",
            "Lindt", "Borggreve"};      //商品名称
    private String[] goodsPrice = new String[]{"¥ 5.00", "¥ 59.00", "¥ 79.00", "¥ 2399.00",
            "¥ 179.00", "¥ 14.90", "¥ 132.59", "¥ 141.43",  //价格
            "¥ 139.43", "¥ 28.90"};

    private String[] goodsInfo = new String[]{"作者 Johanna Basford", "产地 德国", "产地 澳大利亚", "版本 8GB",
            "重量 2Kg", "产地 英国", "重量 300g", "重量 118g",        //详情
            "重量 249g", "重量 640g"};

    private  int[] goodsPic = new int[] {R.mipmap.enchatedforest, R.mipmap.arla,R.mipmap.devondale,
            R.mipmap.kindle, R.mipmap.waitrose, R.mipmap.mcvitie,
            R.mipmap.ferrero, R.mipmap.maltesers, R.mipmap.lindt,       //定义图片ID
            R.mipmap.borggreve};

    private int layout = 0;  //用于界面切换
    private SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView listView=(ListView)findViewById(R.id.shoppinglist);
        final RecyclerView recyclerView=(RecyclerView) findViewById(R.id.recy);
        listView.setVisibility(View.GONE);  //开始设置listView不可见

        //初始化数据
        for (int i = 0; i < goodsName.length; i++) {
            Goods goods = new Goods(goodsName[i], goodsPrice[i], goodsInfo[i], goodsPic[i]);
            itemList_goods.add(goods);
            Map<String, Object> temp = new LinkedHashMap<>();
            char firstLetter = goodsName[i].charAt(0);
            temp.put("firstLetter", firstLetter);
            temp.put("name", goodsName[i]);
            temp.put("price", goodsPrice[i]);
            itemList.add(temp);

            if(i==0){           //对购物车列表进行初步操作
                Goods goods2 = new Goods("购物车", "价格", null, -1);
                itemList2_goods.add(goods2);
                Map<String, Object> temp2 = new LinkedHashMap<>();
                temp2.put("firstLetter", '*');
                temp2.put("name", "购物车");
                temp2.put("price", "价格");
                itemList2.add(temp2);
            }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final CommonAdapter commonAdapter= new CommonAdapter<Map<String,Object>>(this,R.layout.thing,itemList)
        {
            @Override
            public void convert(ViewHolder holder, Map<String, Object> s) {
                TextView name = holder.getView(R.id.name);
                name.setText(s.get("name").toString());
                TextView first = holder.getView(R.id.cycle);
                first.setText(s.get("firstLetter").toString());
            }
        };

        //从商品列表跳到商品详情
        commonAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener(){
            @Override
            public void onClick(int position) {
                //发送商品信息给InfoActivity
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("goods", itemList_goods.get(position));
                bundle.putInt("position",position); //从商品列表进入商品详情界面，position是对于商品列表的
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }

            @Override
            public void onLongClick(int position) {
                itemList.remove(position);
                itemList_goods.remove(position);

                commonAdapter.notifyItemRemoved(position);
                Toast.makeText(MainActivity.this, "移除第"+ position +"个商品",Toast.LENGTH_SHORT).show();
            }

        });

        //recyclerView.setAdapter(commonAdapter);
        //动画
        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(commonAdapter);
        animationAdapter.setDuration(1000);
        recyclerView.setAdapter(animationAdapter);
        recyclerView.setItemAnimator(new OvershootInLeftAnimator());

        //购物车列表
        simpleAdapter =new SimpleAdapter(MainActivity.this,itemList2,R.layout.thing,
                new String[]{"firstLetter", "name", "price"},new int[]{R.id.cycle,R.id.name,R.id.price});
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                if(i!=0){       //跳掉第一行
                    Intent intent= new Intent(MainActivity.this,InfoActivity.class);
                    Bundle bundle= new Bundle();
                    bundle.putSerializable("goods", itemList2_goods.get(i));
                    bundle.putInt("position",i);
                    intent.putExtras(bundle);
                    startActivityForResult(intent,2);
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,final int i, long l) {
                if (i==0)return false;
                final AlertDialog.Builder message=new AlertDialog.Builder(MainActivity.this);
                message.setTitle("移除商品");
                message.setMessage("从购物车移除"+itemList2.get(i).get("name")+'?');
                message.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialogInterface, int i1){

                    }
                });
                message.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i1) {
                        itemList2.remove(i);
                        itemList2_goods.remove(i);
                        simpleAdapter.notifyDataSetChanged();
                    }
                });
                message.create().show();
                return true;

             }
        });

        //切换按钮
        final FloatingActionButton switchBtn= (FloatingActionButton)findViewById(R.id.fab);
        switchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layout==0){
                    recyclerView.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    switchBtn.setImageResource(R.mipmap.mainpage);
                    layout=1;
                }
                else if (layout==1){
                    recyclerView.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    switchBtn.setImageResource(R.mipmap.shoplist);
                    layout=0;
                }
            }
        });

    }

    //设置回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode==1){
            if (resultCode== RESULT_CANCELED){
                setTitle("取消");
            }
            else if(resultCode==RESULT_OK){
                Bundle extras= data.getExtras();
                if(extras!=null){
                    int position_rcv= extras.getInt("position");
                    boolean flag= extras.getBoolean("added");
                    if(flag== true){
                        itemList2.add(itemList.get(position_rcv));
                        itemList2_goods.add(itemList_goods.get(position_rcv));
                        simpleAdapter.notifyDataSetChanged();
                    }
                }
            }
        }

        if(requestCode== 2){
            if (resultCode ==RESULT_CANCELED){
                setTitle("取消");
            }
            else if(resultCode==RESULT_OK){
                Bundle extras= data.getExtras();
                if(extras!=null){
                    int position_rcv= extras.getInt("position");
                    boolean flag= extras.getBoolean("added");
                    if(flag== true){
                        itemList2.add(itemList2.get(position_rcv));
                        itemList2_goods.add(itemList2_goods.get(position_rcv));
                        simpleAdapter.notifyDataSetChanged();
                    }
                }
            }
        }


    }





}


