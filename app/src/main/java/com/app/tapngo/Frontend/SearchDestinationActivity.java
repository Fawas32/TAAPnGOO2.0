package com.app.tapngo.Frontend;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.tapngo.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SearchDestinationActivity extends AppCompatActivity {

    EditText edtFrom, edtTo,edtDate;
    ImageView imgDate;
    Button btnSearch, btnBack;
    public static String from="", to="", date="";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_destination);


        edtFrom = findViewById(R.id.edtFrom);
        edtTo = findViewById(R.id.edtTo);
        edtDate = findViewById(R.id.edtDate);
        edtDate.setEnabled(false);
        imgDate = findViewById(R.id.imgDate);
        btnSearch = findViewById(R.id.btnSearch);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                from = edtFrom.getText().toString().trim();
                to = edtTo.getText().toString().trim();

                if(from.isEmpty()){
                    edtFrom.setError("Required!");
                    edtFrom.requestFocus();
                    return;
                }
                if(to.isEmpty()){
                    edtTo.setError("Required!");
                    edtTo.requestFocus();
                    return;
                }
                if(date.isEmpty()){
                    edtDate.setError("Required!");
                    edtDate.requestFocus();
                    return;
                }
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    private void selectDate() {
        try {

            final Calendar calendar = Calendar.getInstance();
            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            final int month = calendar.get(Calendar.MONTH);
            final int year = calendar.get(Calendar.YEAR);

            DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                    calendar.set(year, month, day);
                    date = sdf.format(calendar.getTime());
                    edtDate.setText(date);

                }
            }, year, month, day);
            datePicker.setTitle("Select Date");
            datePicker.show();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}