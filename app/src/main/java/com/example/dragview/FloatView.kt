package com.example.dragview

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import com.google.android.material.imageview.ShapeableImageView

class FloatView : FrameLayout, View.OnTouchListener {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        initView()
    }

    private fun initView() {
        val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layoutParams = lp
        val imageView = LayoutInflater.from(context).inflate(R.layout.imageview, this, false)
        addView(imageView)
        setOnTouchListener(this)
        post {
            mViewWidth = width.toFloat()
            mViewHeight = height.toFloat()
        }
    }

    var mDownX: Float = 0f
    var mDownY: Float = 0f
    var mFirstX: Float = 0f
    var mViewWidth: Float = 0f
    var mViewHeight: Float = 0f

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mDownX = event.x
                mDownY = event.y
                mFirstX = event.rawX
            }
            MotionEvent.ACTION_MOVE -> {
                offsetLeftAndRight((x - mDownX).toInt())
                offsetTopAndBottom((y - mDownY).toInt())
            }
            MotionEvent.ACTION_UP -> {
                adsorbLeftAndRight(event)
            }
        }
        return true
    }

    //吸左边还是右边
    fun adsorbLeftAndRight(event: MotionEvent) {
        val leftX = 0
        val rightX = getScreenWidth() - mViewWidth.toInt()
        if (isOriginalFormLeft()) {
            if (distanceIsAboveHalf(event)) animateDistance(leftX) else animateDistance(rightX)
        } else {
            if (distanceIsAboveHalf(event)) animateDistance(rightX) else animateDistance(leftX)
        }
    }

    //判断滑动的距离是否大于半屏的宽度
    fun distanceIsAboveHalf(event: MotionEvent): Boolean {
        val cnterX = mViewWidth / 2 + kotlin.math.abs(event.rawX - mFirstX)
        return cnterX < getScreenWidth() / 2
    }

    //回弹到最左还是最右边
    fun animateDistance(duration: Int) {
        animate().setInterpolator(DecelerateInterpolator()).setDuration(300).x(duration.toFloat())
            .start()
    }

    //判断初始位置是否在最左边
    private fun isOriginalFormLeft(): Boolean {
        return mFirstX < getScreenWidth() / 2
    }

    fun getScreenWidth(): Int {
        val dm = DisplayMetrics()
        (context as Activity)?.windowManager?.defaultDisplay?.getMetrics(dm)
        return dm.widthPixels


    }


}