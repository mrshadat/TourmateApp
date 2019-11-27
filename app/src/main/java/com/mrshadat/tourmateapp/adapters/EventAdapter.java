package com.mrshadat.tourmateapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.mrshadat.tourmateapp.R;
import com.mrshadat.tourmateapp.databinding.EventRowBinding;
import com.mrshadat.tourmateapp.pojos.TourmateEvent;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private Context context;
    private List<TourmateEvent> eventList;
    private EventRowBinding binding;

    public EventAdapter(Context context, List<TourmateEvent> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.event_row, parent, false);
        return new EventViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull final EventViewHolder holder, final int position) {
        binding.rowEventName.setText(eventList.get(position).getEventName());
        binding.rowDepartureDate.setText(eventList.get(position).getDepartureDate());
        binding.rowMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater()
                        .inflate(R.menu.item_menu, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String eventId = eventList.get(position).getEventID();
                        Bundle bundle = new Bundle();
                        bundle.putString("id", eventId);
                        switch (item.getItemId()){
                            case R.id.item_details:
                                Navigation
                                        .findNavController(holder.itemView)
                                        .navigate(R.id.action_eventListFragment_to_eventDetailsFragment, bundle);

                                break;
                            case R.id.item_delete:
                                //code for delete
                                break;
                        }
                        return false;
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
