package com.xadaptersimple

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.xadapter.*
import com.xadapter.adapter.XRecyclerViewAdapter
import com.xadapter.holder.XViewHolder
import com.xadaptersimple.data.DataUtils
import com.xadaptersimple.data.MainBean
import kotlinx.android.synthetic.main.recyclerview_layout.*
import java.util.*

/**
 * by y on 2017/6/20.
 */

class TestActivity : AppCompatActivity(), OnXBindListener<MainBean>, OnXAdapterListener {

    private lateinit var xRecyclerViewAdapter: XRecyclerViewAdapter<MainBean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recyclerview_layout)
        val mainBeen = ArrayList<MainBean>()
        recyclerView.layoutManager = LinearLayoutManager(this)
        xRecyclerViewAdapter = XRecyclerViewAdapter()
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = xRecyclerViewAdapter
                .apply {
                    dataContainer = mainBeen
                    recyclerView = this@TestActivity.recyclerView
                    itemLayoutId = R.layout.item
                    pullRefreshEnabled = true
                    loadingMoreEnabled = true
                    onXBindListener = this@TestActivity
                    xAdapterListener = this@TestActivity
                    onItemClickListener = object : OnItemClickListener<MainBean> {
                        override fun onItemClick(view: View, position: Int, entity: MainBean) {
                            Log.i("onItemClick", position.toString())
                            xRecyclerViewAdapter.remove(position)
                        }

                    }
                }
                .refresh()

    }

    override fun onXBind(holder: XViewHolder, position: Int, entity: MainBean) {
        holder.setTextView(R.id.tv_name, entity.name)
        holder.setTextView(R.id.tv_age, entity.age.toString() + "")
    }

    override fun onXRefresh() {
        xRecyclerViewAdapter.removeAll()
        recyclerView.postDelayed({
            xRecyclerViewAdapter.addAll(DataUtils.getTestData(ArrayList()))
            xRecyclerViewAdapter.refreshState = XRefreshView.SUCCESS
            if (xRecyclerViewAdapter.dataContainer.size < 7) {
                xRecyclerViewAdapter.loadMoreState = XLoadMoreView.NOMORE
            }
        }, 1500)
    }

    override fun onXLoadMore() {
        recyclerView.postDelayed({
            if (xRecyclerViewAdapter.dataContainer.size < 7) {
                xRecyclerViewAdapter.loadMoreState = XLoadMoreView.NOMORE
            } else {
                xRecyclerViewAdapter.loadMoreState = XLoadMoreView.ERROR
            }
        }, 1500)
    }
}