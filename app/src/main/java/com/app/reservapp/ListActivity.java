package com.app.reservapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.studioidan.httpagent.HttpAgent;
import com.studioidan.httpagent.JsonArrayCallback;
import com.studioidan.httpagent.JsonCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class ListActivity extends AppCompatActivity {

    String from, to;
    ListView listView;
    ArrayList<Voyage> list;

    EditText datePick, email;
    int idVoyg = 0;
    int number = 0;

    public static String ROOT_URL = "https://e-gestion.000webhostapp.com/Transport/Reservation/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        to = intent.getStringExtra("to");
        number = intent.getIntExtra("number", 0);

        listView = findViewById(R.id.listView);
        datePick = findViewById(R.id.datePick);
        email = findViewById(R.id.email);
        list = new ArrayList<>();

        HttpAgent.get(ROOT_URL + "find_trip.php?depart="+from+"&arrive="+to)
                .goJsonArray(new JsonArrayCallback() {
                    @Override
                    protected void onDone(boolean success, JSONArray jsonArray) {
                        if(success){

                            if(jsonArray != null){
                                list = new ArrayList<>();
                                for (int i=0; i<jsonArray.length(); i++){
                                    try {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        list.add(new Voyage(jsonObject.getInt("id_vy"), jsonObject.getString("depart_vy"), jsonObject.getString("arrive_vy"), jsonObject.getString("temp_vy"), jsonObject.getString("prix_vy")));



                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                MyAdapter myAdapter = new MyAdapter(list);

                                listView.setAdapter(myAdapter);
                            }else {
                                Toast.makeText(ListActivity.this, "Pas de bus !", Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(ListActivity.this, MainActivity.class);
                                startActivity(intent1);
                            }

                        }
                    }
                });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                idVoyg = list.get(i).id;
                viewDatePicker(ListActivity.this, datePick);
            }
        });
    }

    public void viewDatePicker(final Context context, final EditText editText){
        Calendar calendar = Calendar.getInstance();
                int y = calendar.get(Calendar.YEAR);
                int m = calendar.get(Calendar.MONTH);
                int d = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String stringMonth = "";
                        String stringDay = "";
                        month += 1;
                        if((month >= 1) && (month <= 9)){
                            stringMonth = "0" + month;
                        }else{
                            stringMonth = month + "";
                        }
                        if((dayOfMonth >= 1) && (dayOfMonth <= 9)){
                            stringDay = "0" + dayOfMonth;
                        }else{
                            stringDay = dayOfMonth + "";
                        }
                    //    editText.setText(year + "-" + stringMonth + "-" + stringDay);
                        editText.setText(stringDay + "-" + stringMonth + "-" + year);
                    }
                }, y, m, d);
                datePickerDialog.show();
    }

    public void reserv(View view) {
        HttpAgent.get(ROOT_URL +
                "add_reservation.php?id_vy="+idVoyg+"&date_rs="+
                datePick.getText().toString()+"&nbr_ps="+number+
                "&id_ps="+email.getText().toString())
                .goJson(new JsonCallback() {
                    @Override
                    protected void onDone(boolean success, JSONObject jsonObject) {
                        if(success) {
                            if(jsonObject != null){
                                Toast.makeText(ListActivity.this, "Opération réussir", Toast.LENGTH_SHORT).show();
                                String message = "Bienvenu";
                                String subject = "Votre compte de l'app";

                                //Send Mail
                                JavaMailAPI javaMailAPI = new JavaMailAPI(ListActivity.this, email.getText().toString(), subject,message);

                                javaMailAPI.execute();

                                Log.d("Result", jsonObject.toString());
                            }else{
                                Toast.makeText(ListActivity.this, "Pas de bus !", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(ListActivity.this, "Pas de bus !", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    public class MyAdapter extends BaseAdapter {

        ArrayList<Voyage> list = new ArrayList<Voyage>();

        public MyAdapter(ArrayList<Voyage> list){
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.item, null);


            TextView depart = view.findViewById(R.id.d);
            TextView arrive = view.findViewById(R.id.a);
            TextView temp = view.findViewById(R.id.t);
            TextView prix = view.findViewById(R.id.p);


            depart.setText("Départ : " + list.get(position).depart);
            arrive.setText("Arrivé : " + list.get(position).arrive);
            temp.setText("Temp : " + list.get(position).time);
            prix.setText("Prix : " + list.get(position).prix);


            return view;
        }

    }

}