package com.xadaptersimple

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xadaptersimple.net.NetWorkActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        network.setOnClickListener { startActivity(NetWorkActivity::class.java) }
        LinearLayout.setOnClickListener { startActivity(LinearLayoutManagerActivity::class.java) }
        GridLayout.setOnClickListener { startActivity(GridLayoutManagerActivity::class.java) }
        StaggeredGridLayout.setOnClickListener { startActivity(StaggeredGridLayoutManagerActivity::class.java) }
        collapsingToolbar.setOnClickListener { startActivity(CollapsingToolbarLayoutActivity::class.java) }
        emptyView.setOnClickListener { startActivity(EmptyViewActivity::class.java) }
        multiple.setOnClickListener { startActivity(MultipleItemActivity::class.java) }
        test.setOnClickListener { startActivity(TestActivity::class.java) }
        refreshLayout.setOnClickListener { startActivity(RefreshLayoutActivity::class.java) }
        databinding.setOnClickListener { startActivity(DataBindingActivity::class.java) }
    }

    private fun startActivity(clz: Class<*>) {
        val intent = Intent(applicationContext, clz)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}