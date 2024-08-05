package com.app.tapngo.Frontend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tapngo.R;
import com.app.tapngo.Backend.HttpRequest;
import com.app.tapngo.models.StationDataModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    List<StationDataModel> stationDataList, finalList;
    RecyclerView recyclerView;
    TextView tvFrom, tvTo;
    Button btnReminder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showProgressDialog();
        stationDataList = new ArrayList<>();
        finalList = new ArrayList<>();

        tvFrom = findViewById(R.id.tvFrom);
        tvTo = findViewById(R.id.tvTo);
        btnReminder = findViewById(R.id.btnReminder);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        tvFrom.setText("From: "+SearchDestinationActivity.from);
        tvTo.setText("To: "+SearchDestinationActivity.to);

        btnReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ReminderActivity.class));
            }
        });
        postDataOnServer();
    }

    private void postDataOnServer() {
        new postDataTask().execute();
    }

    class postDataTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... args) {
            String response = "";
            stationDataList.clear();
            finalList.clear();
            stationDataList = HttpRequest.executeGet();
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            Log.d("responseListSize",stationDataList.size()+"");

            for(StationDataModel model : stationDataList){
                if(model.getOrigin().toLowerCase().contains(SearchDestinationActivity.from.toLowerCase()) && model.getDestination().toLowerCase().contains(SearchDestinationActivity.to.toLowerCase()) && model.getTraindate().equals(SearchDestinationActivity.date)){
                    finalList.add(model);
                }
            }
            hideProgressDialog();
            if(finalList.size()>0){
                recyclerView.setAdapter(null);
                ItemsListAdapter adapter = new ItemsListAdapter(MainActivity.this, finalList);
                recyclerView.setAdapter(adapter);
            }else {
                Toast.makeText(MainActivity.this, "No data found!", Toast.LENGTH_LONG).show();
            }

        }
    }

    public class ItemsListAdapter extends RecyclerView.Adapter<ItemsListAdapter.ImageViewHolder>{
        private Context mcontext ;
        private List<StationDataModel> muploadList;

        public ItemsListAdapter(Context context , List<StationDataModel> uploadList) {
            mcontext = context ;
            muploadList = uploadList ;
        }

        @Override
        public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mcontext).inflate(R.layout.train_data_layout, parent , false);
            return (new ImageViewHolder(v));
        }

        @Override
        public void onBindViewHolder(final ImageViewHolder holder, @SuppressLint("RecyclerView") final int position) {

            final StationDataModel item = muploadList.get(position);
            holder.tvTime.setText(item.getDestinationtime());
            holder.tvDestination.setText(item.getDestination());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return muploadList.size();
        }

        public class ImageViewHolder extends RecyclerView.ViewHolder{
            public TextView tvTime;
            public TextView tvDestination;

            public ImageViewHolder(View itemView) {
                super(itemView);

                tvTime = itemView.findViewById(R.id.tvTime);
                tvDestination = itemView.findViewById(R.id.tvDestination);
            }
        }
    }
}