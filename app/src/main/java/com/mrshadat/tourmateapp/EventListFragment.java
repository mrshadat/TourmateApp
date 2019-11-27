package com.mrshadat.tourmateapp;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mrshadat.tourmateapp.adapters.EventAdapter;
import com.mrshadat.tourmateapp.databinding.FragmentEventListBinding;
import com.mrshadat.tourmateapp.pojos.TourmateEvent;
import com.mrshadat.tourmateapp.viewmodels.EventViewModel;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventListFragment extends Fragment {
    private static final String TAG = EventListFragment.class.getSimpleName();
    private EventViewModel eventViewModel;
    private EventAdapter adapter;
    private FragmentEventListBinding binding;


    public EventListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_add_event:
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.action_eventListFragment_to_addEventFragment);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        eventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);
        binding = FragmentEventListBinding.inflate(LayoutInflater.from(getActivity()));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventViewModel.eventListLD.observe(this, new Observer<List<TourmateEvent>>() {
            @Override
            public void onChanged(List<TourmateEvent> tourmateEvents) {
                Log.e(TAG, "onChanged: " + tourmateEvents.size());
                adapter = new EventAdapter(getActivity(), tourmateEvents);
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                binding.eventRV.setLayoutManager(llm);
                binding.eventRV.setAdapter(adapter);
            }
        });
    }
}