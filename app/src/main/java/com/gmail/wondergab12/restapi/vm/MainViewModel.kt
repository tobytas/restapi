package com.gmail.wondergab12.restapi.vm

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.gmail.wondergab12.restapi.MyApplication
import com.gmail.wondergab12.restapi.repo.local.LocalRepository
import com.gmail.wondergab12.restapi.repo.model.Cat
import com.gmail.wondergab12.restapi.repo.network.NetworkRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class MainViewModel(application: Application) : AndroidViewModel(application) {

    @Inject lateinit var cats: MutableLiveData<MutableList<Cat>>
    @Inject lateinit var err: MutableLiveData<Throwable>
    @Inject lateinit var localRepository: LocalRepository
    @Inject lateinit var networkRepository: NetworkRepository
    @Inject lateinit var compositeDisposable: CompositeDisposable

    init {
        (application as MyApplication).getMainViewModelSubGraph()
                .inject(this)
    }

    fun loadListNetwork() {
        compositeDisposable.add(networkRepository.getList(5)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (cats.value != null) {
                        it.addAll(cats.value as Collection<Cat>)
                    }
                    cats.value = it
                }, {
                    err.value = Exception("ERROR WHILE LOAD FROM DATABASE\n" + it.message, it)
                    err.value = null
                }))
    }

    fun loadListLocal() {
        compositeDisposable.add(localRepository.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    cats.value = it
                }, {
                    err.value = Exception("ERROR WHILE LOAD FROM DATABASE\n" + it.message, it)
                    err.value = null
                }))
    }

    fun getDrawable(view: ImageView, cat: Cat) {
        Glide.with(view)
                .load(cat.url)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?,
                                              isFirstResource: Boolean): Boolean {
                        err.value = Exception("ERROR WHILE DOWNLOADING IMAGE FOR SAVING\n" + e?.message, e)
                        err.value = null
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?,
                                                 dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        return false
                    }
                }).into(view)
    }

    fun save(view: ImageView, cat: Cat) {
        Glide.with(view)
                .asBitmap()
                .load(cat.url)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {
                    }

                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        cat.url = view.context.filesDir.toString() + "/" + cat.id + ".jpg"
                        saveFilesDir(view, cat, resource)
                        localRepository.insert(cat)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    Toast.makeText(view.context, "Image saved as: " + cat.url, Toast.LENGTH_SHORT).show()
                                }, {
                                    err.value = Exception("ERROR WHILE SAVE TO DATABASE\n" + it.message, it)
                                    err.value = null
                                })
                    }
                })
    }

    fun saveFilesDir(view: ImageView, cat: Cat, bitmap: Bitmap) {
        object : Thread() {
            override fun run() {
                try {
                    val fos: FileOutputStream = view.context.openFileOutput(cat.id + ".jpg", Context.MODE_PRIVATE)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                    fos.close()
                } catch (e: IOException) {
                    err.postValue(Exception("ERROR WHILE SAVE IMAGE ON LOCAL FILES DIRECTORY\n" + e.message, e))
                    err.postValue(null)
                }
            }
        }.start()
    }

    fun delete(view: ImageView, cat: Cat) {
        view.context.deleteFile(cat.id + ".jpg")
        compositeDisposable.add(localRepository.delete(cat)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Toast.makeText(view.context, "Image: " + cat.url + " deleted", Toast.LENGTH_SHORT).show()
                }, {
                    err.value = Exception("ERROR WHILE DELETING FILE FROM DATABASE\n" + it.message, it)
                    err.value = null
                }))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
