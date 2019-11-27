package com.mrshadat.tourmateapp.viewmodels;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mrshadat.tourmateapp.pojos.Moments;
import com.mrshadat.tourmateapp.pojos.TourmateEvent;
import com.mrshadat.tourmateapp.repos.EventDBRepository;

import java.io.File;
import java.util.List;

public class EventViewModel extends ViewModel {
    private EventDBRepository dbRepository;
    public MutableLiveData<List<TourmateEvent>> eventListLD = new MutableLiveData<>();
    public MutableLiveData<TourmateEvent> eventDetailsLD = new MutableLiveData<>();
    public MutableLiveData<List<Moments>> momentsLD = new MutableLiveData<>();


    public EventViewModel(){
        dbRepository = new EventDBRepository();
        eventListLD = dbRepository.eventListLD;
    }

    public void saveEvent(TourmateEvent event){
        dbRepository.addNewEventToDB(event);
    }

    public void getEventDetails(String eventId){
        eventDetailsLD = dbRepository.getEventDetailsByEventId(eventId);
    }

    public void uploadImageToFirebaseStorage(File file, final String eventId){
        StorageReference rootRef = FirebaseStorage.getInstance().getReference();
        Uri fileUri = Uri.fromFile(file);
        final StorageReference imageRef = rootRef.child("batch15/"+ fileUri.getLastPathSegment());
        UploadTask uploadTask = imageRef.putFile(fileUri);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL

                return imageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Log.e("moment", "upload completed");
                    Uri downloadUri = task.getResult();
                    Moments moments = new Moments(null, eventId, downloadUri.toString());
                    dbRepository.addNewMoment(moments);
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    public void getMoments(String eventId){
        momentsLD = dbRepository.getAllMoments(eventId);
    }
}
