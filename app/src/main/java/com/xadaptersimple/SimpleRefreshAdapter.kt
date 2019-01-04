package com.xadaptersimple

import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.xadapter.OnFooterClickListener
import com.xadapter.OnLoadMoreRetryListener
import com.xadapter.XLoadMoreView
import com.xadapter.adapter.XRecyclerViewAdapter

/**
 * @author y
 */
class SimpleRefreshAdapter<T>(private val swipeRefreshLayout: SwipeRefreshLayout) : XRecyclerViewAdapter<T>() {

    companion object {
        const val TYPE_STATUS = 0
        const val TYPE_REFRESH = 1
        const val TYPE_LOAD_MORE = 2
    }

    init {
        swipeRefreshLayout.setOnRefreshListener {
            if (loadMoreState != XLoadMoreView.LOAD) {
                loadMoreView?.state = XLoadMoreView.NORMAL
                xAdapterListener.onXRefresh()
            }
        }
    }

    override fun refresh() = apply {
        goneView(emptyView)
        visibleView(recyclerView)
        xAdapterListener.onXRefresh()
        swipeRefreshLayout.isRefreshing = true
        loadMoreView?.state = XLoadMoreView.NORMAL
    }

    override fun onScrollBottom() {
        if (!loadingMoreEnabled) {
            return
        }
        if (swipeRefreshLayout.isRefreshing) {
            return
        }
        if (loadMoreView?.state == XLoadMoreView.LOAD) {
            return
        }
        loadMoreView?.state = XLoadMoreView.LOAD
        xAdapterListener.onXLoadMore()
    }

    fun onComplete(type: Int) {
        if (type == TYPE_REFRESH) {
            swipeRefreshLayout.isRefreshing = false
            loadMoreState = XLoadMoreView.NOMORE
        } else {
            loadMoreState = XLoadMoreView.SUCCESS
        }
    }

    fun onError(type: Int) {
        if (type == TYPE_REFRESH) {
            swipeRefreshLayout.isRefreshing = false
        } else {
            loadMoreState = XLoadMoreView.ERROR
        }
    }

    fun loadNoMore() {
        loadMoreState = XLoadMoreView.NOMORE
    }

    fun setOnLoadMoreRetry(loadMoreRetryListener: OnLoadMoreRetryListener) = apply {
        onFooterListener = object : OnFooterClickListener {
            override fun onXFooterClick(view: View) {
                if (loadMoreState == XLoadMoreView.ERROR) {
                    loadMoreState = XLoadMoreView.LOAD
                    loadMoreRetryListener.onXLoadMoreRetry()
                }
            }
        }
    }

    fun setOnLoadMoreRetry(loadMoreRetryListener: () -> Unit) = apply {
        onFooterListener = object : OnFooterClickListener {
            override fun onXFooterClick(view: View) {
                if (loadMoreState == XLoadMoreView.ERROR) {
                    loadMoreState = XLoadMoreView.LOAD
                    loadMoreRetryListener()
                }
            }
        }
    }

}