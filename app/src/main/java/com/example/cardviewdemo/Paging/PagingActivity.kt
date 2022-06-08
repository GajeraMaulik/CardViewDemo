package com.example.cardviewdemo.Paging

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.cardviewdemo.R
import com.example.cardviewdemo.databinding.ActivityPagingBinding
import kotlinx.android.synthetic.main.activity_paging.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PagingActivity : AppCompatActivity() {
    lateinit var pagingAdapter: PagingAdapter
    lateinit var binding: ActivityPagingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPagingBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val actionBar = supportActionBar
        actionBar!!.title = "PagingView"
        actionBar.setDisplayHomeAsUpEnabled(true)


        pagingAdapter = PagingAdapter()


        initRecyclerView()
        initViewModel()


    }


    fun initViewModel(){
        val viewModel = ViewModelProvider(this).get(PagingActivityViewModel::class.java)
        lifecycleScope.launch {
            //pageProgressBar.visibility = View.VISIBLE
            delay(1800L)
            pageProgressBar.visibility = View.GONE


        }

        lifecycleScope.launchWhenCreated {
            viewModel.getListData().collectLatest {

                pagingAdapter.submitData(it)


            }
        }

    }

    fun initRecyclerView(){
        rvPagingView.apply {
          //  layoutManager = LinearLayoutManager(this@PagingActivity)
          //  val decoration = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
            //addItemDecoration(decoration)
            pagingAdapter = PagingAdapter()
            rvPagingView.adapter = pagingAdapter

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


}