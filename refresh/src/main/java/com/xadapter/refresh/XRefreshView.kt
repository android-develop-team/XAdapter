package com.xadapter.refresh

import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.widget.FrameLayout

abstract class XRefreshView(context: Context, layoutId: Int) : FrameLayout(context) {


    private var refreshView: View = View.inflate(context, layoutId, null)
    private var mMeasuredHeight: Int = 0
    private val animator: ValueAnimator = ValueAnimator.ofInt().setDuration(300)

    var state: Int = Callback.NORMAL
        set(state) {
            if (state == field) {
                return
            }
            onStart()
            when (state) {
                Callback.NORMAL -> onNormal()
                Callback.READY -> onReady()
                Callback.REFRESH -> onRefresh()
                Callback.SUCCESS -> onSuccess()
                Callback.ERROR -> onError()
            }
            field = state
        }

    var visibleHeight: Int
        get() {
            return refreshView.layoutParams.height
        }
        private set(height) {
            if (height == 0) {
                state = Callback.NORMAL
            }
            val lp = refreshView.layoutParams
            lp.height = if (height < 0) 0 else height
            refreshView.layoutParams = lp
        }

    init {
        addView(refreshView, LayoutParams(LayoutParams.MATCH_PARENT, 0))
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        initView()
        measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        mMeasuredHeight = measuredHeight
        animator.addUpdateListener { animation -> visibleHeight = animation.animatedValue as Int }
    }

    fun refreshState(mState: Int) {
        state = mState
        postDelayed({ smoothScrollTo(0) }, 200)
    }

    fun onMove(delta: Float) {
        if (visibleHeight > 0 || delta > 0) {
            visibleHeight += delta.toInt()
            if (state < Callback.REFRESH) {
                state = if (visibleHeight > mMeasuredHeight) Callback.READY else Callback.NORMAL
            }
        }
    }

    fun releaseAction(): Boolean {
        var isOnRefresh = false
        if (visibleHeight > mMeasuredHeight && state < Callback.REFRESH) {
            state = Callback.REFRESH
            isOnRefresh = true
        }
        var destHeight = 0
        if (state == Callback.REFRESH) {
            destHeight = mMeasuredHeight
        }
        smoothScrollTo(destHeight)
        return isOnRefresh
    }

    private fun smoothScrollTo(destHeight: Int) {
        animator.setIntValues(visibleHeight, destHeight)
        animator.start()
    }

    protected abstract fun initView()
    protected abstract fun onStart()
    protected abstract fun onNormal()
    protected abstract fun onReady()
    protected abstract fun onRefresh()
    protected abstract fun onSuccess()
    protected abstract fun onError()

}
