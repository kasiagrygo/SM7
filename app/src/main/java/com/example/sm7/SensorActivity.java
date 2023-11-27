package com.example.sm7;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.MenuItem;


import java.util.List;

public class SensorActivity extends AppCompatActivity {

    private SensorAdapter adapter;
    private SensorManager sensorManager;
    private List<Sensor> sensorList;
    private RecyclerView recyclerView;
    private boolean subtitleVisible;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_activity);

        recyclerView = findViewById(R.id.sensor_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        if (adapter == null) {
            adapter = new SensorAdapter(sensorList);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

        Toolbar toolbar = findViewById(R.id.menu_toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem subtitlesItem = menu.findItem(R.id.sensor_count);
        if(subtitleVisible){
            subtitlesItem.setTitle(R.string.hide_subtitle);
        } else{
            subtitlesItem.setTitle(R.string.show_subtitle);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sensor_count) {
            subtitleVisible = !subtitleVisible;
            invalidateOptionsMenu();
            updateSubtitle();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void updateSubtitle() {
        String subtitle = getString(R.string.subtitle_format, getSensorCount());
        if (!subtitleVisible) {
            subtitle = "";
        }
        getSupportActionBar().setSubtitle(subtitle);
    }

    private int getSensorCount() {
        return sensorList.size();
    }

    private static class SensorHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView iconImageView;
        private TextView nameTextView;
        public SensorHolder(@NonNull View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.sensor_icon);
            nameTextView = itemView.findViewById(R.id.sensor_name);
        }

        public void bind(Sensor sensor) {
            iconImageView.setImageResource(R.drawable.ic_sensor);
            nameTextView.setText(sensor.getName());
        }

        @Override
        public void onClick(View v) {

        }
    }
    private static class SensorAdapter extends RecyclerView.Adapter<SensorHolder>{

        private List<Sensor> sensors;
        private OnItemClickListener onItemClickListener;

        public SensorAdapter(List<Sensor> sensors) {
            this.sensors = sensors;
        }

        public interface OnItemClickListener {
            void onItemClick(int position);
        }
        public void setOnItemClickListener(OnItemClickListener listener) {
            this.onItemClickListener = listener;
        }
        @NonNull
        @Override
        public SensorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.sensor_list_item, parent, false);
            return new SensorHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SensorHolder holder, int position) {
            Sensor sensor = sensors.get(position);
            holder.bind(sensor);

            holder.itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return sensors.size();
        }
    }
}
