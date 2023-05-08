package com.sik.guiddemo.utils.guid

import com.sik.guid.IGuidData
import java.io.File
import java.io.InputStream

class FirstGuidData : IGuidData {
    private val slideDistance: Float = 400f

    override fun viewAnimation(): InputStream {
        return File("/sdcard/Documents/viewAnimation.json").inputStream()
    }

    override fun fingerAnimation(): InputStream {
        return File("/sdcard/Documents/fingerAnimation.json").inputStream()
    }

    override fun clearProgress(): Float {
        return 0.6f
    }

    override fun progressUpdate(
        startX: Float,
        startY: Float,
        currentX: Float,
        currentY: Float
    ): Float {
        return (currentX - startX) / slideDistance
    }
}