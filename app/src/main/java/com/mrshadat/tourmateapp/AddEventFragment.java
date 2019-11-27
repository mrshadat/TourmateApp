package com.mrshadat.tourmateapp;


import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.mrshadat.tourmateapp.databinding.FragmentAddEventBinding;
import com.mrshadat.tourmateapp.pojos.TourmateEvent;
import com.mrshadat.tourmateapp.viewmodels.EventViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddEventFragment extends Fragment {
    private FragmentAddEventBinding binding;
    private String departureDate = null;
    private EventViewModel eventViewModel;

    public AddEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddEventBinding.inflate(LayoutInflater.from(getActivity()));
        eventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.createNewEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eventName = binding.eventNameInput.getText().toString();
                String departurePlace = binding.departureInput.getText().toString();
                String destination = binding.destinationInput.getText().toString();
                String budget = binding.budgetInput.getText().toString();
                if (eventName.isEmpty() && departurePlace.isEmpty() &&
                        destination.isEmpty() && budget.isEmpty() &&
                        departureDate.isEmpty()){
                    Toast.makeText(getActivity(), "please provide all information", Toast.LENGTH_SHORT).show();
                    return;
                }

                TourmateEvent event = new TourmateEvent(null,
                        eventName, departurePlace, destination,
                        Integer.parseInt(budget), departureDate);

                eventViewModel.saveEvent(event);
                Navigation.findNavController(view)
                        .navigate(R.id.eventListFragment);
            }
        });

        binding.dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(
                getActivity(),
                dateSetListener,
                year, month, day
        );
        dpd.show();
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(i, i1, i2);
            departureDate = new SimpleDateFormat("dd/MM/yyyy")
                    .format(calendar.getTime());
            binding.dateBtn.setText(departureDate);
        }
    };
}
