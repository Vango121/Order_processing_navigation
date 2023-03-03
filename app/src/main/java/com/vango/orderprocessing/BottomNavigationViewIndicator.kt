package com.vango.orderprocessing

import android.animation.AnimatorSet
import android.animation.ObjectAnimator.ofInt
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Keep
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.google.android.material.bottomnavigation.BottomNavigationMenuView

class BottomNavigationViewIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val targetId: Int
    private var target: BottomNavigationMenuView? = null

    private var rect = Rect()
    private val backgroundDrawable: Drawable

    private var index = 0
    private var animator: AnimatorSet? = null

    init {
        if (attrs == null) {
            targetId = NO_ID
            backgroundDrawable = ColorDrawable(Color.TRANSPARENT)
        } else {
            with(context.obtainStyledAttributes(attrs, R.styleable.BottomNavigationViewIndicator)) {
                targetId = getResourceId(R.styleable.BottomNavigationViewIndicator_targetBottomNavigation, NO_ID)
                backgroundDrawable = ColorDrawable(getColor(R.styleable.BottomNavigationViewIndicator_clipableBackground, Color.BLACK))
                recycle()
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (targetId == NO_ID) return
        val parentView = parent as? View ?: return
        val child = parentView.findViewById<View?>(targetId)
        if (child !is CustomBottomNavigationView) return
        for (i in 0 until child.childCount) {
            val subView = child.getChildAt(i)
            if (subView is BottomNavigationMenuView) target = subView
        }
        elevation = child.elevation
        child.addOnNavigationItemSelectedListener { updateRectByIndex(it, true) }
        post { updateRectByIndex(index, false) }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        target = null
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.clipRect(rect)
        backgroundDrawable.draw(canvas)
    }

    private fun updateRectByIndex(index: Int, animated: Boolean) {
        this.index = index
        target?.apply {
            if (childCount < 1 || index >= childCount) return
            val reference = getChildAt(index)

            val start = reference.left + left
            val end = reference.right + left

            backgroundDrawable.setBounds(left, top, right, bottom)
            val newRect = Rect(start, 0, end, height)
            if (animated) startUpdateRectAnimation(newRect) else updateRect(newRect)
        }
    }

    private fun startUpdateRectAnimation(rect: Rect) {
        animator?.cancel()
        animator = AnimatorSet().also {
            it.playTogether(
                ofInt(this, "rectLeft", this.rect.left, rect.left),
                ofInt(this, "rectRight", this.rect.right, rect.right),
                ofInt(this, "rectTop", this.rect.top, rect.top),
                ofInt(this, "rectBottom", this.rect.bottom, rect.bottom)
            )
            it.interpolator = FastOutSlowInInterpolator()
            it.duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
            it.start()
        }
    }

    private fun updateRect(rect: Rect) {
        this.rect = rect
        postInvalidate()
    }

    @Keep fun setRectLeft(left: Int) = updateRect(rect.apply { this.left = left })
    @Keep fun setRectRight(right: Int) = updateRect(rect.apply { this.right = right })
    @Keep fun setRectTop(top: Int) = updateRect(rect.apply { this.top = top })
    @Keep fun setRectBottom(bottom: Int) = updateRect(rect.apply { this.bottom = bottom })

}