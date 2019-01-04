package com.xadapter.simple

import com.xadapter.XMultiCallBack

/**
 * by y on 2017/3/9
 *
 *
 *
 *
 * 简单的MultiItem，使用者可继承[XMultiCallBack] 自己定制
 */
class SimpleXMultiItem(
        var message: String = "",
        var messageSuffix: String = "",
        var icon: Int = 0,
        var itemMultiType: Int = 0,
        var itemMultiPosition: Int = -1
) : XMultiCallBack {
    override val itemType: Int = itemMultiType
    override val position: Int = itemMultiPosition
}