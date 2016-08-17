package com.example.jimmy.finall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class addquestion1 extends AppCompatActivity {
    EditText e1, e2, e3, e4, e5;
    Spinner sp1, sp2;
    String[] sans, ssort;
    String id;
    Boolean fixoradd;//true=>add false=>fix

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addquestion1);
        e1 = (EditText) findViewById(R.id.e1);
        e2 = (EditText) findViewById(R.id.e2);
        e3 = (EditText) findViewById(R.id.e3);
        e4 = (EditText) findViewById(R.id.e4);
        e5 = (EditText) findViewById(R.id.e5);
        sp1 = (Spinner) findViewById(R.id.sp1);
        sp2 = (Spinner) findViewById(R.id.sp2);
        sans = getResources().getStringArray(R.array.ans);
        ssort = getResources().getStringArray(R.array.sortforaddq1);
        Intent it = getIntent();
        id = it.getStringExtra("id");
        fixoradd = it.getBooleanExtra("fixoradd", true);//default true
        if (fixoradd == false) {
            String result = DBConnector.executeQuery("select * from question where id='" + id + "'");
            String ans = null, sort = null;
            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonData = jsonArray.getJSONObject(0);
                String question = jsonData.getString("question");
                String A = jsonData.getString("A");
                String B = jsonData.getString("B");
                String C = jsonData.getString("C");
                String D = jsonData.getString("D");
                sort = jsonData.getString("sort");
                ans = jsonData.getString("ans");
                e1.setText(question);
                e2.setText(A);
                e3.setText(B);
                e4.setText(C);
                e5.setText(D);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < sans.length; i++) {
                if (ans.equals(sans[i])) {
                    sp1.setSelection(i);
                }
            }
            sp2.setSelection(Integer.parseInt(sort));
        }
        else if(fixoradd == true)
        {
            TextView tt=(TextView)findViewById(R.id.textView17);
            tt.setText("新增題目");
        }
    }
    public void save(View v) {
        if (e1.getText().toString().trim().equals("") || e2.getText().toString().trim().equals("") || e3.getText().toString().trim().equals("") || e4.getText().toString().trim().equals("") || e5.getText().toString().trim().equals("")||sp2.getSelectedItemPosition()==0) {
            Toast.makeText(addquestion1.this, "請填寫完整", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(addquestion1.this, "修改V成功", Toast.LENGTH_SHORT).show();
            if (fixoradd == false) {
                String result = DBConnector.executeQuery("update question set sort='" + (sp2.getSelectedItemPosition()) + "', question='" + e1.getText() + "', A='" + e2.getText() + "',B='" + e3.getText() + "',C='" + e4.getText() + "',D='" + e5.getText() + "',ans='" + (sans[sp1.getSelectedItemPosition()]) + "' where id='" + id + "'");
            } else {
                String result = DBConnector.executeQuery("insert into question(id,sort,question,A,B,C,D,ans) values('" + "" + "','" + (sp2.getSelectedItemPosition()) + "','" + e1.getText() + "','" + e2.getText() + "','" + e3.getText() + "','" + e4.getText() + "','" + e5.getText() + "','" + sans[sp1.getSelectedItemPosition()] + "')");
            }
            finish();
        }
    }
    public void back(View v) {
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_addquestion1, menu);
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
