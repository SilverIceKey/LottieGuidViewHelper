package com.sik.guid

import java.io.InputStream

interface IGuidData {
    /**
     * 返回界面动画
     */
    fun viewAnimation(): InputStream

    /**
     * 返回手指动画
     */
    fun fingerAnimation(): InputStream

    /**
     * 通过校验数值
     */
    fun clearProgress(): Float

    /**
     * 数值更新
     */
    fun progressUpdate(startX: Float, startY: Float, currentX: Float, currentY: Float): Float
}