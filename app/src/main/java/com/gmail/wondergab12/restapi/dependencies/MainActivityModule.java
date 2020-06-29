package com.gmail.wondergab12.restapi.dependencies;

import android.widget.Button;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.gmail.wondergab12.restapi.R;
import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityModule {

    private boolean remote = true;

    @Provides
    public Button provideButton(AppCompatActivity activity) {
        if (remote) {
            remote = false;
            return activity.findViewById(R.id.button);
        }
        return activity.findViewById(R.id.button2);
    }

    @Provides
    public RecyclerView provideRecyclerView(AppCompatActivity activity) {
        RecyclerView recyclerView = activity.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        return recyclerView;
    }

    @Provides
    public ProgressBar providesProgressBar(AppCompatActivity activity) {
        return activity.findViewById(R.id.progressBar);
    }
}
