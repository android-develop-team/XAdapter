package com.xadaptersimple

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.xadapter.adapter.XRecyclerViewAdapter
import com.xadapter.addAll
import com.xadapter.widget.XRefreshView
import com.xadaptersimple.data.MainBean
import kotlinx.android.synthetic.main.recyclerview_layout.*

/**
 * by y on 2016/11/17
 */

class EmptyViewActivity : AppCompatActivity() {

    private lateinit var xRecyclerViewAdapter: XRecyclerViewAdapter<MainBean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recyclerview_layout)
        xRecyclerViewAdapter = XRecyclerViewAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = xRecyclerViewAdapter
                .apply {
                    emptyView = this@EmptyViewActivity.emptyView
                    recyclerView = this@EmptyViewActivity.recyclerView
                    itemLayoutId = R.layout.item
                    pullRefreshEnabled = true
                    xRefreshListener = {
                        this@EmptyViewActivity.recyclerView.postDelayed({
                            xRecyclerViewAdapter.refreshState = XRefreshView.SUCCESS
                            xRecyclerViewAdapter.addAll(ArrayList())
                        }, 2000)
                    }
                    xLoadMoreListener = {

                    }
                }

        xRecyclerViewAdapter.addAll(ArrayList())
    }
}
