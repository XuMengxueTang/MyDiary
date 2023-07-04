package com.example.zpp.mydiary;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Button btn_back,btn_delete;//返回，删除按钮
    private EditText tv_title,tv_content,tv_author;//标题，时间，内容
    private TextView tv_createtime;
    private SQLiteDatabase mDatabase;
    private int []idlist;
    private int id;
    private String title, createtime, content, dateStr,author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);//日记详情

        mDatabase = new DBHelper(this).getWritableDatabase();
        queryTitle();
        initView();//获得页面的信息
        initEvent();
    }

    //获得页面的信息
    public void initView(){
        tv_title = (EditText) findViewById(R.id.tv_title);
        tv_createtime = (TextView) findViewById(R.id.tv_createtime);
        //tv_author=(EditText) findViewById(R.id.tv_author);
        tv_content = (EditText) findViewById(R.id.tv_content);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(DetailActivity.this);
        btn_delete = (Button) findViewById(R.id.btn_delete);
    }

    public void initEvent(){
        Bundle b=getIntent().getExtras();
        //获取Bundle的信息
        //获取ID使用id查询数据库
        int pos=b.getInt("id");
        id=idlist[pos];
        System.out.println("id:"+id);
        //使用游标查询数据库
        Cursor cursor= mDatabase.query(DBHelper.TABLE_NAME,DBHelper.TABLE_COLUMNS,"id=?",new String[]{id+""},null,null,null,null);
        while (cursor != null && cursor.moveToNext()) {
            // 从游标中获取数据库字段的值，并设置到相应的界面元素中
            tv_title.setText(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TITLE)));
            tv_createtime.setText(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CREATETIME)));
            //tv_author.setText(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_AUTHOR)));
            tv_content.setText(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CONTENT)));
        }
        cursor.close();
        btn_delete.setOnClickListener(DetailActivity.this);
    }

    @Override
    public void onClick(View view) {
        //点击返回，返回
        if (view.getId() == R.id.btn_back) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        //点击删除，删除
        if (view.getId() == R.id.btn_delete) {
            deleteData(id);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        //点击修改,保存修改后的内容
        if (view.getId() == R.id.btn_change) {
            change(id);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void queryTitle() {
        //查询数据库的行数，使用 rawQuery() 方法执行原生 SQL 查询
        Cursor cursor1= mDatabase.rawQuery("select count(2) from "+DBHelper.TABLE_NAME,null);
        //将游标移到第一行，并使用 getLong(0) 方法获取查询结果的第一列值（即行数）
        cursor1.moveToFirst();
        long count = cursor1.getLong(0);
        //将长整型的行数转换为整数类型，并用于初始化 idlist 数组，该数组用于存储查询结果中每行的 id 值
        int num=(int) count;
        idlist=new int[num];
        cursor1.close();
        Cursor cursor;
        //查询数据库的全部内容
        //使用 query() 方法查询数据库的全部内容
        cursor = mDatabase.query(DBHelper.TABLE_NAME,DBHelper.TABLE_COLUMNS,null,null,null,null,null);
        int i=0;
        // 获取每行的 id 值，并存储到 idlist 数组中
        while (cursor != null && cursor.moveToNext()) {
            idlist[i]=cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_ID));
            i+=1;
        }
        cursor.close();
    }

    //删除数据
    private void deleteData(int id) {
        mDatabase.delete(DBHelper.TABLE_NAME,"id = ?",new String[]{id+""});
    }
    //保存
    private void change(int id){
        Date date = new Date();
        date.getTime();

        dateStr = sdf.format(date);
        //author = tv_author.getText().toString();
        title = tv_title.getText().toString();
        content = tv_content.getText().toString();
        ContentValues contentValues = new ContentValues();
        //contentValues.put("author",author);
        contentValues.put("title", title);
        contentValues.put("createtime", dateStr);
        contentValues.put("content", content);
        mDatabase.update(DBHelper.TABLE_NAME, contentValues,"id=?",  new String[]{String.valueOf(id)});
    }

}
