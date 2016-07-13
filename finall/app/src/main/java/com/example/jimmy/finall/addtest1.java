package com.example.jimmy.finall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class addtest1 extends AppCompatActivity {
    ListView lv;
    final List<addtAdapter.DataHolder> items = new ArrayList<>();
    private addtAdapter addtAdapter;
    String sort, type, id;//title for add new
    TextView tv;
    int count = 0;
    ArrayList myList = new ArrayList();//占存剛開始近來的勾勾
    ArrayList myList1 = new ArrayList();//存變化的勾勾
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtest1);
        Intent it = getIntent();

        sort = it.getStringExtra("sort");
        type = it.getStringExtra("type");
        id = it.getStringExtra("testid");

        lv = (ListView) findViewById(R.id.listViewaddt1);
        tv = (TextView) findViewById(R.id.textView13);
        addgogo();
        getdata();
        addtAdapter = new addtAdapter(this, items);
        lv.setAdapter(addtAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                com.example.jimmy.finall.addtAdapter.ViewHolder holder = (com.example.jimmy.finall.addtAdapter.ViewHolder) view.getTag();
                holder.cbk.toggle();
                if (myList1.contains((items.get(position).idd))) {
                    myList1.remove(items.get(position).idd);
                } else {
                    myList1.add(items.get(position).idd);
                }
                if (holder.cbk.isChecked() == true) {
                    count++;
                    Log.e("!!!!!11", items.get(position).idd);
                } else {
                    count--;
                }
                tv.setText("已經選取" + count + "項");
            }
        });
    }

    private void getdata() {
        String result;
        if (sort.equals("7")) {
            result = DBConnector.executeQuery("select id,sort,question from question");
        } else {
            result = DBConnector.executeQuery("select id,sort,question from question where sort='" + sort + "'");
        }
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                addtAdapter.DataHolder item = new addtAdapter.DataHolder();
                item.title1 = jsonData.getString("question");
                item.sort = jsonData.getInt("sort");//只會有1~6
                item.idd = String.valueOf(jsonData.getInt("id"));
                if (myList.contains(String.valueOf(jsonData.getString("id")))) {
                    Log.e("!T", item.idd);
                    item.type = true;
                } else {
                    item.type = false;
                    Log.e("!F", "ZZ");
                }
                items.add(item);
            }
        } catch (Exception e) {
            Log.e("log_tag", e.toString());
        }
    }

    public void addgogo() {

        if (type.contains("fix")) {
            String result = DBConnector.executeQuery("SELECT question.id FROM testinside,testlist,question where testlist.num=testinside.testtitleid  and testinside.questionid=question.id and testlist.num LIKE '%" + id + "%'");
            Log.e("ZZZZ", result);
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    myList.add(jsonData.getString("id"));
                    count++;
                }
            } catch (Exception e) {
                Log.e("log_tag", e.toString());
            }
            tv.setText("已經選取" + count + "項");
        }
    }

    public void save(View v) {

        Log.e("ori", myList.toString());
        Log.e("aft", myList1.toString());
        for (int i = 0; i < myList1.size(); i++) {
            if (myList.contains(myList1.get(i))) {
                String putin = DBConnector.executeQuery("delete from testinside where testtitleid='" + id + "' and questionid='" + (myList1.get(i)) + "'");
                Log.e("AAA", "AAA");
            } else {
                String putin = DBConnector.executeQuery("insert into testinside(testtitleid,questionid) values('" + id + "','" + (myList1.get(i)) + "')");
                Log.e("QQQ", "QAQ");
            }
        }
        Toast.makeText(this, "!!!", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void cancel(View v) {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_addtest1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
