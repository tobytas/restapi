package com.gmail.wondergab12.restapi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.wondergab12.restapi.repo.model.Cat;
import com.gmail.wondergab12.restapi.vm.MainViewModel;

import java.util.List;

import javax.inject.Inject;

@SuppressWarnings("Convert2Lambda")
public class MainActivity extends AppCompatActivity {

    @Inject Button buttonRemote;
    @Inject Button buttonLocal;
    @Inject RecyclerView recyclerView;
    @Inject ProgressBar progressBar;
    @Inject MainViewModel mainViewModel;

    private boolean isRemote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            isRemote = savedInstanceState.getBoolean("REQUEST_TYPE");
        }
        ((MyApplication) getApplication()).getApplicationGraph(this)
                .mainActivitySubGraphBuilder()
                .with(this)
                .build()
                .inject(this);
        mainViewModel.getCats().observe(this, new Observer<List<Cat>>() {
            @Override
            public void onChanged(List<Cat> cats) {
                recyclerView.setAdapter(new RecyclerAdapter(cats));
            }
        });
        mainViewModel.getErr().observe(this, new Observer<Throwable>() {
            @Override
            public void onChanged(Throwable throwable) {
                if (throwable != null) {
                    Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    throwable.printStackTrace();
                }
            }
        });
        buttonRemote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRemote = true;
                mainViewModel.loadListNetwork();
            }
        });
        buttonLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRemote = false;
                mainViewModel.loadListLocal();
            }
        });
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("REQUEST_TYPE", isRemote);
    }

    @SuppressWarnings("WeakerAccess")
    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {

        private List<Cat> data;

        public RecyclerAdapter(List<Cat> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            return new RecyclerHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
            holder.bindData(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data != null ? data.size() : 0;
        }

        private class RecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private ImageView imageView;
            private Cat cat;

            public RecyclerHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
                Button button = itemView.findViewById(R.id.button);
                button.setText(isRemote ? "save" : "delete");
                button.setOnClickListener(this);
            }

            void bindData(Cat cat) {
                this.cat = cat;
                mainViewModel.getDrawable(imageView, cat);
            }

            @Override
            public void onClick(View v) {
                if (isRemote) {
                    mainViewModel.save(imageView, cat);
                } else {
                    mainViewModel.delete(imageView, cat);
                    int pos = getAdapterPosition();
                    data.remove(pos);
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, data.size());
                }
            }
        }
    }
}
