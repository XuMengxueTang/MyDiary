package com.example.zpp.mydiary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase mDatabase;
    private ListView lv_dailylist;
    private SimpleCursorAdapter mAdapter;
    private String []title;
    private int []id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //显示主页面
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        lv_dailylist = (ListView) findViewById(R.id.lv_dailylist);

        setSupportActionBar(toolbar);

        mDatabase = new DBHelper(this).getWritableDatabase();
        queryTitle();
        initEvent();
    }

    //总的来说，这段代码的作用是初始化一个列表视图
    // 并为其设置适配器和点击事件监听器。
    // 当用户点击列表中的某一项时，会创建一个包含点击项位置信息的 Intent
    // 并启动 DetailActivity，同时结束当前的 MainActivity
    public void initEvent(){
        final MyBaseAdapter myBaseAdapter=new MyBaseAdapter();
        lv_dailylist.setAdapter(myBaseAdapter);
        lv_dailylist.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Bundle bundle = new Bundle();
                bundle.putInt("id", position);
                //创建了一个名为 intent 的新的 Intent 对象，用于启动 DetailActivity 类
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                intent.putExtras(bundle);
                finish();
                startActivity(intent);
            }
        });
    }

    @Override
    //添加过程页面
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    //点击上述页面的添加按钮，跳转到 AddActivity
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent intent = new Intent(this, AddActivity.class);
            startActivity(intent);   //startActivity方法
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //主显示页面的查询标题
    private void queryTitle() {
        //使用 rawQuery() 方法执行原始的 SQL 查询语句，查询数据库中表名为 DBHelper.TABLE_NAME 的记录数。
        //将查询结果保存到名为 cursor1 的 Cursor 对象中。
        Cursor cursor1= mDatabase.rawQuery("select count(2) from "+DBHelper.TABLE_NAME,null);
        cursor1.moveToFirst();
        long count = cursor1.getLong(0);
        int num=(int) count;
        title=new String[num];
        id=new int[num];
        cursor1.close();

        Cursor cursor;
        cursor = mDatabase.query(DBHelper.TABLE_NAME,DBHelper.TABLE_COLUMNS,null,null,null,null,null);
        int i=0;
        //在结果集中迭代循环，遍历每一条记录
        while (cursor != null && cursor.moveToNext()) {
            id[i]=cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_ID));
            title[i]=cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TITLE));
            i+=1;
        }
        cursor.close();
    }

    //MyBaseAdapter 是一个自定义的适配器类，它继承自 BaseAdapter。
    // getCount(): 返回列表中项的数量，通过执行查询语句获取数据库中特定表的记录数，并将其作为项的数量返回。
    //getItem(int position): 根据指定位置 position 的项，返回对应的数据对象。
    // 在这里，直接返回 title[position]，表示返回标题数组中指定位置的元素作为数据对象
    // getItemId(int position): 根据指定位置 position 的项，返回其ID值。
    // 在这里，直接返回 position，表示返回位置值作为ID。
    //getView(int position, View convertView, ViewGroup parent): 为列表中的每一项创建并返回视图
    class MyBaseAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            Cursor cursor= mDatabase.rawQuery("select count(2) from "+DBHelper.TABLE_NAME,null);
            cursor.moveToFirst();
            long count = cursor.getLong(0);
            cursor.close();
            int num=(int)count;
            return num;
        }

        @Override
        public Object getItem(int position) { return title[position]; }

        @Override
        public long getItemId(int position) { return position; }

        @Override
        public View getView(int position, View convertview, ViewGroup parent) {
            View view=View.inflate(MainActivity.this,R.layout.list_item,null);
            TextView tv_title=(TextView)view.findViewById(R.id.tv_title);
            tv_title.setText(title[position]);
            return view;
        }
    }

}
