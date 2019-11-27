package com.mrshadat.tourmateapp;


import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.mrshadat.tourmateapp.adapters.MomentsAdapter;
import com.mrshadat.tourmateapp.databinding.FragmentEventDetailsBinding;
import com.mrshadat.tourmateapp.helper.EventUtils;
import com.mrshadat.tourmateapp.pojos.EventExpense;
import com.mrshadat.tourmateapp.pojos.Moments;
import com.mrshadat.tourmateapp.pojos.TourmateEvent;
import com.mrshadat.tourmateapp.viewmodels.EventViewModel;
import com.mrshadat.tourmateapp.viewmodels.ExpenseViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailsFragment extends Fragment {
    private static final String TAG = EventDetailsFragment.class.getSimpleName();
    private String eventId = null;
    private int totalbudget = 0;
    private ExpenseViewModel expenseViewModel;
    private EventViewModel eventViewModel;
    private FragmentEventDetailsBinding binding;
    private final int REQUEST_STORAGE_CODE = 456;
    private final int REQUEST_CAMERA_CODE = 999;
    private String currentPhotoPath;
    private List<Moments> momentsList = new ArrayList<>();
    public EventDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEventDetailsBinding.inflate(
                LayoutInflater.from(getActivity())
        );
        expenseViewModel =
                ViewModelProviders.of(this)
                        .get(ExpenseViewModel.class);
        eventViewModel =
                ViewModelProviders.of(this)
                        .get(EventViewModel.class);
        Bundle bundle = getArguments();
        if (bundle != null){
            eventId = bundle.getString("id");
            eventViewModel.getEventDetails(eventId);
            expenseViewModel.getAllExpenses(eventId);
            eventViewModel.getMoments(eventId);
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        eventViewModel.eventDetailsLD.observe(this, new Observer<TourmateEvent>() {
            @Override
            public void onChanged(TourmateEvent event) {
                binding.detailsEventName.setText(event.getEventName());
                binding.detailsInitialBudget.setText("Total budget: "+event.getBudget());
                totalbudget = event.getBudget();
            }
        });

        eventViewModel.momentsLD.observe(this, new Observer<List<Moments>>() {
            @Override
            public void onChanged(List<Moments> moments) {
                momentsList = moments;
                Toast.makeText(getActivity(), ""+moments.size(), Toast.LENGTH_SHORT).show();
            }
        });

        expenseViewModel.expenseListLD.observe(this, new Observer<List<EventExpense>>() {
            @Override
            public void onChanged(List<EventExpense> eventExpenses) {
                int totalEx = 0;
                for (EventExpense ex : eventExpenses){
                    totalEx += ex.getAmount();
                }
                int remainingBudget = totalbudget - totalEx;
                binding.detailsTotalExpense.setText("Total expense: "+totalEx);
                binding.detailsRemainingBudget.setText("Remaining budget: "+remainingBudget);
            }
        });

        binding.addExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddExpenseDialog();
            }
        });

        binding.addMomentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkStoragePermission()){
                    dispatchCameraIntent();
                }
            }
        });

        binding.viewMomentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMomentGalleryDialog();
            }
        });
    }

    private void showMomentGalleryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("All Moments");
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.bottom_sheet_gallery, null);

        final RecyclerView recyclerView =
                view.findViewById(R.id.momentGalleryRV);
        final MomentsAdapter adapter = new MomentsAdapter(getActivity(), momentsList);
        GridLayoutManager glm = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(glm);
        recyclerView.setAdapter(adapter);
        builder.setView(view);
        builder.setNegativeButton("cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void showAddExpenseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add New Expense");
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.add_expense_dialog_box, null);
        builder.setView(view);
        final EditText amountET = view.findViewById(R.id.dialogAddExpenseAmountInput);
        final EditText nameET = view.findViewById(R.id.dialogAddExpenseNameInput);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String amount = amountET.getText().toString();
                String name = nameET.getText().toString();
                if (amount.isEmpty() && name.isEmpty()){
                    //toast
                    return;
                }

                EventExpense expense =
                        new EventExpense(null, eventId, name,
                                Integer.parseInt(amount),
                                EventUtils.getCurrentDateTime());
                expenseViewModel.saveExpense(expense);

            }
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void dispatchCameraIntent(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
              Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.mrshadat.tourmateapp",
                        photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, REQUEST_CAMERA_CODE);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA_CODE &&
                resultCode == Activity.RESULT_OK){
            Log.e(TAG, "onActivityResult: "+currentPhotoPath);
            File file = new File(currentPhotoPath);
            eventViewModel.uploadImageToFirebaseStorage(file, eventId);
        }
    }

    private boolean checkStoragePermission(){
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED){
            requestPermissions(permissions, REQUEST_STORAGE_CODE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_CODE && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getActivity(), "Permission accepted", Toast.LENGTH_SHORT).show();
            dispatchCameraIntent();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
