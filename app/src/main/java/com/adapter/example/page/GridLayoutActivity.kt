package com.adapter.example.page

import android.os.Bundle
import android.view.LayoutInflater
import com.adapter.example.R
import com.adapter.example.json.JsonUtils
import com.adapter.example.json.SampleEntity
import com.bumptech.glide.Glide
import com.xadapter.adapter.XAdapter
import com.xadapter.recyclerview.*
import com.xadapter.refresh.XLoadMoreView
import com.xadapter.refresh.XRefreshView
import com.xadapter.vh.getContext
import com.xadapter.vh.getImageView
import com.xadapter.vh.setText
import kotlinx.android.synthetic.main.layout_recyclerview.*

/**
 * by y on 2016/11/17
 */
class GridLayoutActivity : BaseActivity(R.layout.activity_grid_manager, "GridLayoutManagerSample") {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recyclerView
                .gridLayoutManager(2)
                .attachAdapter(XAdapter<SampleEntity>())
                .setItemLayoutId(R.layout.layout_json_item)
                .openPullRefresh()
                .openLoadingMore()
                .setOnBind<SampleEntity> { holder, _, entity ->
                    Glide.with(holder.getContext()).load(entity.image).into(holder.getImageView(R.id.image))
                    holder.setText(R.id.title, entity.title)
                }
                .setRefreshListener {
                    this@GridLayoutActivity.recyclerView.postDelayed({ recyclerView.setRefreshState(XRefreshView.SUCCESS) }, 1500)
                }
                .setLoadMoreListener {
                    this@GridLayoutActivity.recyclerView.postDelayed({ recyclerView.setLoadMoreState(XLoadMoreView.NO_MORE) }, 1500)
                }
                .addHeaderView(LayoutInflater.from(this).inflate(R.layout.adapter_header_1, findViewById(android.R.id.content), false))
                .addFooterView(LayoutInflater.from(this).inflate(R.layout.adapter_footer_1, findViewById(android.R.id.content), false))
                .addAll(JsonUtils.jsonList)
    }
}