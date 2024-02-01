package com.example.mohyuof;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity3 extends AppCompatActivity {

    private ArrayList<HashMap<String, Object>> list_map = new ArrayList<>();
    private LinearLayout linear3;
    private LinearLayout linear6;
    private LinearLayout linear8;
    private LinearLayout linear9;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button2;
    private EditText edittext3;
    private EditText edittext1;
    private ListView listview1;
    private SharedPreferences phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
    }
    private void initialize(Bundle _savedInstanceState) {
        linear3 = findViewById(R.id.linear3);
        linear6 = findViewById(R.id.linear6);
        linear8 = findViewById(R.id.linear8);
        linear9 = findViewById(R.id.linear9);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button2 = findViewById(R.id.button2);
        edittext3 = findViewById(R.id.edittext3);
        edittext1 = findViewById(R.id.edittext1);
        listview1 = findViewById(R.id.listview1);
        phone = getSharedPreferences("phone", Activity.MODE_PRIVATE);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear9.setVisibility(View.VISIBLE);
                linear6.setVisibility(View.GONE);
                linear8.setVisibility(View.GONE);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear6.setVisibility(View.VISIBLE);
                linear8.setVisibility(View.GONE);
                linear9.setVisibility(View.GONE);
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                linear8.setVisibility(View.VISIBLE);
                linear6.setVisibility(View.GONE);
                linear9.setVisibility(View.GONE);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                addWord();
            }
        });

        edittext1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
                searchWords(_param1.toString().trim());
            }

            @Override
            public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {

            }

            @Override
            public void afterTextChanged(Editable _param1) {

            }
        });
}
    private void initializeLogic() {
        _setCornerRadius(edittext1, 50, "#616161");
        _setCornerRadius(edittext3, 50, "#616161");
        edittext1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.phone, 0, 0, 0);
        listview1.invalidateViews();
        linear9.setVisibility(View.GONE);
        linear6.setVisibility(View.GONE);
        linear8.setVisibility(View.GONE);

        if (phone.contains("ph")) {
            list_map = new Gson().fromJson(phone.getString("ph", ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
            listview1.setAdapter(new Listview1Adapter(list_map));
            ((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
        }
    }

    private void _setCornerRadius(final View _view, final double _value, final String _color) {
        android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable();
        gd.setColor(Color.parseColor(_color));
        gd.setCornerRadius((int)_value);
        _view.setBackground(gd);
        _view.setElevation(5);
    }

    private void addWord() {
        if (!edittext3.getText().toString().trim().isEmpty()) {
            HashMap<String, Object> item = new HashMap<>();
            item.put("key", edittext3.getText().toString());
            list_map.add(item);

            listview1.setAdapter(new Listview1Adapter(list_map));
            ((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
            phone.edit().putString("ph", new Gson().toJson(list_map)).apply();
            edittext3.setText("");
        } else {
            showMessage("Please enter a word");
        }
    }

    private void searchWords(String query) {
        if (!phone.getString("ph", "").isEmpty()) {
            list_map = new Gson().fromJson(phone.getString("ph", ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
            if (!query.isEmpty()) {
                for (int i = list_map.size() - 1; i >= 0; i--) {
                    if (!list_map.get(i).get("key").toString().toLowerCase().contains(query.toLowerCase())) {
                        list_map.remove(i);
                    }
                }
            }
            listview1.setAdapter(new Listview1Adapter(list_map));
            ((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
        }
        if (query.isEmpty()) {
            linear8.setVisibility(View.GONE);
        } else {
            linear8.setVisibility(View.VISIBLE);
        }
    }

    public class Listview1Adapter extends BaseAdapter {

        private ArrayList<HashMap<String, Object>> data;

        public Listview1Adapter(ArrayList<HashMap<String, Object>> arr) {
            data = arr;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public HashMap<String, Object> getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup container) {
            LayoutInflater inflater = getLayoutInflater();
            View view = convertView;
            if (view == null) {
                view = inflater.inflate(R.layout.custom_listview, null);
            }

            final LinearLayout linear1 = view.findViewById(R.id.linear1);
            final TextView textview1 = view.findViewById(R.id.textview1);

            textview1.setText(data.get(position).get("key").toString());
            _setCornerRadius(linear1, 35, "#006464");

            return view;
        }
    }

    public void showMessage(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}



