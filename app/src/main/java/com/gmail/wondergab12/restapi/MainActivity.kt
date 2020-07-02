package com.gmail.wondergab12.restapi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.gmail.wondergab12.restapi.repo.model.Cat
import com.gmail.wondergab12.restapi.vm.MainViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var buttonRemote: Button
    @Inject lateinit var buttonLocal: Button
    @Inject lateinit var recyclerView: RecyclerView
    @Inject lateinit var progressBar: ProgressBar
    @Inject lateinit var mainViewModel: MainViewModel

    private var isRemote: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        isRemote = savedInstanceState?.getBoolean("REQUEST_TYPE") ?: isRemote

        (application as MyApplication).getApplicationGraph(this)
                .mainActivitySubGraphBuilder()
                .with(this)
                .build()
                .inject(this)

        mainViewModel.cats.observe(this, Observer<MutableList<Cat>> {
            recyclerView.adapter = RecyclerAdapter(it)
        })
        mainViewModel.err.observe(this, Observer<Throwable> {
                Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_SHORT).show()
        })
        buttonRemote.setOnClickListener {
            isRemote = true
            mainViewModel.loadListNetwork()
        }
        buttonLocal.setOnClickListener {
            isRemote = false
            mainViewModel.loadListLocal()
        }

        progressBar.visibility = View.INVISIBLE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("REQUEST_TYPE", isRemote)
    }

    private inner class RecyclerAdapter(private val data: MutableList<Cat>) : RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder {
            val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
            return RecyclerHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
            holder.bindData(data[position])
        }
        override fun getItemCount(): Int {
            return data.size
        }

        private inner class RecyclerHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

            private var imageView: ImageView = itemView.findViewById(R.id.imageView)
            private lateinit var cat: Cat

            init {
                val button: Button = itemView.findViewById(R.id.button)
                button.text = if (isRemote) "save" else "delete"
                button.setOnClickListener(this)
            }

            fun bindData(cat: Cat) {
                this.cat = cat
                mainViewModel.getDrawable(imageView, cat)
            }

            override fun onClick(v: View) {
                if (isRemote) {
                    mainViewModel.save(imageView, cat)
                } else {
                    mainViewModel.delete(imageView, cat);
                    val pos: Int = adapterPosition
                    data.removeAt(pos)
                    notifyItemRemoved(pos)
                    notifyItemRangeChanged(pos, data.size)
                }
            }
        }
    }
}
