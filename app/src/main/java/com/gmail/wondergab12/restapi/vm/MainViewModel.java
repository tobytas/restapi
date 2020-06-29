package com.gmail.wondergab12.restapi.vm;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.gmail.wondergab12.restapi.MyApplication;
import com.gmail.wondergab12.restapi.repo.model.Cat;
import com.gmail.wondergab12.restapi.repo.local.LocalRepository;
import com.gmail.wondergab12.restapi.repo.network.NetworkRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

@SuppressWarnings({"Convert2Lambda"})
public class MainViewModel extends AndroidViewModel {

    @Inject MutableLiveData<List<Cat>> cats;
    @Inject MutableLiveData<Throwable> err;
    @Inject LocalRepository localRepository;
    @Inject NetworkRepository networkRepository;
    @Inject CompositeDisposable compositeDisposable;

    public MainViewModel(Application application) {
        super(application);
        ((MyApplication) application).getMainViewModelSubGraph()
                .inject(this);
    }

    public MutableLiveData<List<Cat>> getCats() {
        return cats;
    }

    public MutableLiveData<Throwable> getErr() {
        return err;
    }

    public void loadListNetwork() {
        compositeDisposable.add(networkRepository.getList(5)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Cat>>() {
                    @Override
                    public void accept(List<Cat> list) {
                        if (cats.getValue() != null) {
                            list.addAll(cats.getValue());
                        }
                        cats.setValue(list);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        err.setValue(new Exception("ERROR WHILE LOAD FROM NETWORK\n" + throwable.getMessage(), throwable));
                        err.setValue(null);
                    }
                }));
    }

    public void loadListLocal() {
        compositeDisposable.add(localRepository.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Cat>>() {
                    @Override
                    public void accept(List<Cat> list) {
                        cats.setValue(list);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        err.setValue(new Exception("ERROR WHILE LOAD FROM DATABASE\n" + throwable.getMessage(), throwable));
                        err.setValue(null);
                    }
                }));
    }

    public void getDrawable(ImageView view, Cat cat) {
        Glide.with(view)
                .load(cat.getUrl())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        err.setValue(new Exception("ERROR WHILE DOWNLOADING IMAGE FOR SAVING\n" + (e != null ? e.getMessage() : ""), e));
                        err.setValue(null);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                                   DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(view);
    }

    public void save(ImageView view, Cat cat) {
        Glide.with(view)
                .asBitmap()
                .load(cat.getUrl())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource,
                                                @Nullable Transition<? super Bitmap> transition) {
                        cat.setUrl(view.getContext().getFilesDir() + "/" + cat.getId() + ".jpg");
                        saveFilesDir(view, cat, resource);
                        compositeDisposable.add(localRepository.insert(cat)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Long>() {
                                    @Override
                                    public void accept(Long aLong) {
                                        Toast.makeText(view.getContext(), "Image saved as: " + cat.getUrl(), Toast.LENGTH_SHORT).show();
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) {
                                        err.setValue(new Exception("ERROR WHILE SAVE TO DATABASE\n" + throwable.getMessage(), throwable));
                                        err.setValue(null);
                                    }
                                }));
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    private void saveFilesDir(ImageView view, Cat cat, Bitmap bitmap) {
        new Thread() {
            @Override
            public void run() {
                try {
                    FileOutputStream fos = view.getContext()
                            .openFileOutput(cat.getId() + ".jpg", Context.MODE_PRIVATE);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.close();
                } catch (IOException e) {
                    err.postValue(new Exception("ERROR WHILE SAVE IMAGE ON LOCAL FILES DIRECTORY\n" + e.getMessage(), e));
                    err.postValue(null);
                }
            }
        }.start();
    }

    public void delete(ImageView view, Cat cat) {
        view.getContext().deleteFile(cat.getId() + ".jpg");
        compositeDisposable.add(localRepository.delete(cat).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Throwable {
                        Toast.makeText(view.getContext(), "Image: " + cat.getUrl() + " deleted", Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        err.setValue(new Exception("ERROR WHILE DELETING FILE FROM DATABASE\n" + throwable.getMessage(), throwable));
                        err.setValue(null);
                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
