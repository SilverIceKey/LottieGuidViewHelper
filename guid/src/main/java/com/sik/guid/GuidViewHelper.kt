package com.sik.guid

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ValueAnimator
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.airbnb.lottie.LottieAnimationView

/**
 * 引导帮助
 */
class GuidViewHelper constructor(context: Context) {

    /**
     * 包裹导航控件的父控件
     */
    var parentView: ViewGroup? = null

    /**
     * 导航数据
     */
    var guidDataList: MutableList<IGuidData> = mutableListOf()

    /**
     * 起始坐标
     */
    private var startX: Float = 0f
    private var startY: Float = 0f

    /**
     * 界面动画
     */
    private var viewAnimationLottieView: LottieAnimationView = LottieAnimationView(context)

    /**
     * 手指动画
     */
    private var fingerAnimationLottieView: LottieAnimationView = LottieAnimationView(context)

    /**
     * 当前引导位置
     */
    private var currentGuidPosition: Int = 0

    /**
     * 是否添加到父控件
     */
    @Volatile
    private var isAddInParentView: Boolean = false

    /**
     * 引导结束
     */
    var onFinish: () -> Unit = {}

    /**
     * 引导未通过
     */
    var onNotClear: () -> Unit = {}

    init {
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        viewAnimationLottieView.layoutParams = layoutParams
        fingerAnimationLottieView.layoutParams = layoutParams
    }

    /**
     * 开始导航
     */
    @Synchronized
    fun start() {
        if (guidDataList.isEmpty()) {
            throw NullPointerException("导航数据为空")
        }
        viewAnimationLottieView.setAnimation(
            guidDataList[currentGuidPosition].viewAnimation(), "viewAnimation"
        )
        viewAnimationLottieView.repeatCount = ValueAnimator.INFINITE
        viewAnimationLottieView.removeAnimatorListener(animatorListener)
        fingerAnimationLottieView.setAnimation(
            guidDataList[currentGuidPosition].fingerAnimation(), "fingerAnimation"
        )
        fingerAnimationLottieView.repeatCount = ValueAnimator.INFINITE
        fingerAnimationLottieView.playAnimation()
        viewAnimationLottieView.playAnimation()
        if (!isAddInParentView) {
            parentView?.addView(viewAnimationLottieView)
            parentView?.addView(fingerAnimationLottieView)
            isAddInParentView = true
        }
    }

    /**
     * 触摸事件
     */
    fun onTouchEvent(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
                fingerAnimationLottieView.pauseAnimation()
                viewAnimationLottieView.pauseAnimation()
                fingerAnimationLottieView.visibility = View.GONE
            }

            MotionEvent.ACTION_MOVE -> {
                viewAnimationLottieView.progress = guidDataList[currentGuidPosition].progressUpdate(
                    startX, startY, event.x, event.y, event
                )
            }

            MotionEvent.ACTION_UP -> {
                if (viewAnimationLottieView.progress >= guidDataList[currentGuidPosition].clearProgress()) {
                    viewAnimationLottieView.resumeAnimation()
                    viewAnimationLottieView.repeatCount = 1
                    viewAnimationLottieView.addAnimatorListener(animatorListener)
                } else {
                    viewAnimationLottieView.progress = 0f
                    fingerAnimationLottieView.progress = 0f
                    viewAnimationLottieView.repeatCount = ValueAnimator.INFINITE
                    viewAnimationLottieView.removeAnimatorListener(animatorListener)
                    viewAnimationLottieView.resumeAnimation()
                    fingerAnimationLottieView.resumeAnimation()
                    fingerAnimationLottieView.visibility = View.VISIBLE
                    onNotClear()
                }
            }
        }
    }

    /**
     * 动画监听
     */
    private val animatorListener = object : AnimatorListener {
        override fun onAnimationStart(animation: Animator) {

        }

        override fun onAnimationEnd(animation: Animator) {
            if (currentGuidPosition == guidDataList.size - 1) {
                parentView?.removeView(viewAnimationLottieView)
                parentView?.removeView(fingerAnimationLottieView)
                onFinish()
                parentView = null
                onFinish = {}
                onNotClear = {}
            } else {
                currentGuidPosition++
                start()
            }
        }

        override fun onAnimationCancel(animation: Animator) {

        }

        override fun onAnimationRepeat(animation: Animator) {

        }

    }
}

/**
 * 结束监听
 */
fun GuidViewHelper.onFinish(onFinish: () -> Unit): GuidViewHelper {
    this.onFinish = onFinish
    return this
}

/**
 * 引导未通过监听
 */
fun GuidViewHelper.onNotClear(onNotClear: () -> Unit): GuidViewHelper {
    this.onNotClear = onNotClear
    return this
}

/**
 * 设置引导数据
 */
fun GuidViewHelper.setGuidDataList(vararg guidDataList: IGuidData): GuidViewHelper {
    this.guidDataList = guidDataList.toMutableList()
    return this
}

/**
 * 设置父容器
 */
fun GuidViewHelper.setParentView(parentView: ViewGroup): GuidViewHelper {
    this.parentView = parentView
    return this
}