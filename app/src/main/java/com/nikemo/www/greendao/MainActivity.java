package com.nikemo.www.greendao;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;
import www.nikemo.com.DaoMaster;
import www.nikemo.com.DaoSession;
import www.nikemo.com.Father;
import www.nikemo.com.FatherDao;
import www.nikemo.com.Son;
import www.nikemo.com.SonDao;

public class MainActivity extends Activity implements View.OnClickListener {

    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private SQLiteDatabase db;

    private FatherDao fatherDao;
    private SonDao sonDao;

    private Button abt, qbt;

    private TextView tv;

    private void init() {
        findViewById(R.id.abt).setOnClickListener(this);
        findViewById(R.id.qbt).setOnClickListener(this);
        tv = (TextView) findViewById(R.id.tv);
    }

    private void openDb() {
        db = new DaoMaster.DevOpenHelper(MainActivity.this, "person.db", null).getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        fatherDao = daoSession.getFatherDao();
        sonDao = daoSession.getSonDao();
    }

    private void addPerson() {
        Son son = new Son();
        son.setName("nikemo_xss");
        son.setAge(10);
        Father father = new Father();
        father.setAge(53);
        father.setName("nifaa");
        long fatherId = fatherDao.insert(father);
        Log.e("queryPerson1", fatherId + "");
        son.setFatherId(fatherId);
        sonDao.insert(son);

        Son tom = new Son();
        tom.setName("tom");
        tom.setAge(20);
        Father kobe = new Father();
        father.setAge(40);
        father.setName("nifa");
        fatherId = fatherDao.insert(kobe);
        Log.e("queryPerson1", fatherId + "");
        tom.setFatherId(fatherId);
        sonDao.insert(tom);
    }

    //notEq,ge
    private void queryPerson() {//按照id排序（升）
        List<Son> list = sonDao.queryBuilder().orderAsc(SonDao.Properties.Id).list();
        for (Son s : list) {
            Log.d("queryPerson", "queryPerson: " + s);
        }
    }

    private void queryThread() {
        final Query query = sonDao.queryBuilder().build();
        new Thread() {
            @Override
            public void run() {
                super.run();//多线程查询
                List data = query.forCurrentThread().list();
            }
        }.start();
    }

    private void queryOneToOne() {
        List<Son> list = sonDao.queryBuilder().list();
        for (Son s : list) {//一对一查询
            Log.d("queryPerson", "queryPerson: " + s.getFather().getName());
        }
    }

    private void queryeq() {
        Son s = sonDao.queryBuilder().where(SonDao.Properties.Name.eq("tom")).unique();
        Log.d("queryeq", "queryeq() called" + s);
    }

    private void querylike() {
        List<Son> list = sonDao.queryBuilder().where(SonDao.Properties.Name.like("nikemo%")).list();
        Log.d("queryeq", "queryeq() called" + list);
    }

    private void querybetween() {
        List<Son> list = sonDao.queryBuilder().where(SonDao.Properties.Age.between(15, 25)).list();
        Log.d("queryeq", "queryeq() called" + list);
    }

    //><
    private void querygt() {
        List<Son> list = sonDao.queryBuilder().where(SonDao.Properties.Age.gt(18)).list();
//        List<Son> list = sonDao.queryBuilder().where(SonDao.Properties.Age.lt(18)).list();
        Log.d("queryeq", "queryeq() called" + list);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
        init();
        openDb();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.abt:
                addPerson();
                break;
            case R.id.qbt:
//                queryPerson();
                querybetween();
                break;
            default:
                break;
        }
    }
}
