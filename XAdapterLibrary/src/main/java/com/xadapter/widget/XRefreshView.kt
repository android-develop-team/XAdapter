@file:Suppress("LeakingThis")

package com.xadapter.widget

import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.widget.FrameLayout

/**
 * @author y
 * @create 2019/3/12
 */

abstract class XRefreshView(context: Context, layoutId: Int) : FrameLayout(context) {

    companion object {
        const val NORMAL = 0 //初始状态
        const val READY = 1 //准备
        const val REFRESH = 2 //正在刷新
        const val SUCCESS = 3 // 刷新成功
        const val ERROR = 4 // 刷新失败
    }

    private var refreshView: View = View.inflate(context, layoutId, null)
    private var mMeasuredHeight: Int = 0

    var state: Int = NORMAL
        set(state) {
            if (state == field) {
                return
            }
            onStart()
            when (state) {
                NORMAL -> onNormal()
                READY -> onReady()
                REFRESH -> onRefresh()
                SUCCESS -> onSuccess()
                ERROR -> onError()
            }
            field = state
        }

    var visibleHeight: Int
        get() {
            return refreshView.layoutParams.height
        }
        private set(height) {
            if (height == 0) {
                state = NORMAL
            }
            val lp = refreshView.layoutParams
            lp.height = if (height < 0) 0 else height
            refreshView.layoutParams = lp
        }

    init {
        addView(refreshView, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 0))
        layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        initView()
        measure(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        mMeasuredHeight = measuredHeight
    }

    fun refreshState(mState: Int) {
        state = mState
        postDelayed({ smoothScrollTo(0) }, 200)
    }

    fun onMove(delta: Float) {
        if (visibleHeight > 0 || delta > 0) {
            visibleHeight += delta.toInt()
            if (state < REFRESH) {
                state = if (visibleHeight > mMeasuredHeight) {
                    READY
                } else {
                    NORMAL
                }
            }
        }
    }

    fun releaseAction(): Boolean {
        var isOnRefresh = false
        if (visibleHeight > mMeasuredHeight && state < REFRESH) {
            state = REFRESH
            isOnRefresh = true
        }
        var destHeight = 0
        if (state == REFRESH) {
            destHeight = mMeasuredHeight
        }
        smoothScrollTo(destHeight)
        return isOnRefresh
    }

    private fun smoothScrollTo(destHeight: Int) {
        val animator = ValueAnimator.ofInt(visibleHeight, destHeight).setDuration(300)
        animator.addUpdateListener { animation -> visibleHeight = animation.animatedValue as Int }
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