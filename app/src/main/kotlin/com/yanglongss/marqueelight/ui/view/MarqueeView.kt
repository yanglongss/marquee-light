package com.yanglongss.marqueelight.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.sin

class MarqueeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var color: Int = 0xFF00FF00.toInt()
    private var speed: Float = 1.0f
    private var isRunning: Boolean = false
    private var pulseIntensity: Float = 1.0f
    private var animationProgress: Float = 0f

    // LED parameters
    private val ledRadius: Float = 8f
    private val ledSpacing: Float = 15f
    private val borderPadding: Float = 20f
    private val animationDuration: Long = 2000

    init {
        paint.style = Paint.Style.FILL
    }

    fun setColor(newColor: Int) {
        color = newColor
        invalidate()
    }

    fun setSpeed(newSpeed: Float) {
        speed = newSpeed.coerceIn(0.5f, 2.0f)
    }

    fun startAnimation() {
        isRunning = true
        animate()
    }

    fun stopAnimation() {
        isRunning = false
        removeCallbacks(animationRunnable)
    }

    fun triggerPulse(duration: Long = 3000) {
        animate().apply {
            setDuration(duration)
            withStartAction {
                pulseIntensity = 1.5f
                invalidate()
            }
            withEndAction {
                pulseIntensity = 1.0f
                invalidate()
            }
        }
    }

    private val animationRunnable = object : Runnable {
        override fun run() {
            if (isRunning) {
                animationProgress += (speed / 60f) % 1f
                invalidate()
                postDelayed(this, 16)  // ~60 FPS
            }
        }
    }

    private fun animate() {
        if (isRunning) {
            removeCallbacks(animationRunnable)
            post(animationRunnable)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()

        // Draw top edge
        drawLedStrip(
            canvas,
            borderPadding,
            borderPadding,
            width - 2 * borderPadding,
            0f,
            true
        )

        // Draw bottom edge
        drawLedStrip(
            canvas,
            borderPadding,
            height - borderPadding,
            width - 2 * borderPadding,
            0f,
            true
        )

        // Draw left edge
        drawLedStrip(
            canvas,
            borderPadding,
            borderPadding,
            0f,
            height - 2 * borderPadding,
            false
        )

        // Draw right edge
        drawLedStrip(
            canvas,
            width - borderPadding,
            borderPadding,
            0f,
            height - 2 * borderPadding,
            false
        )
    }

    private fun drawLedStrip(
        canvas: Canvas,
        startX: Float,
        startY: Float,
        lengthX: Float,
        lengthY: Float,
        isHorizontal: Boolean
    ) {
        val ledCount = if (isHorizontal) {
            (lengthX / ledSpacing).toInt()
        } else {
            (lengthY / ledSpacing).toInt()
        }

        for (i in 0 until ledCount) {
            val offsetRatio = (i.toFloat() / ledCount - animationProgress) % 1f
            val brightness = (sin((offsetRatio - 0.5f) * Math.PI) + 1) / 2
            val alpha = (brightness * 255 * pulseIntensity).toInt().coerceIn(0, 255)

            val adjustedColor = (color and 0x00FFFFFF) or (alpha shl 24)
            paint.color = adjustedColor

            val x = if (isHorizontal) startX + offsetRatio * lengthX else startX
            val y = if (isHorizontal) startY else startY + offsetRatio * lengthY

            canvas.drawCircle(x, y, ledRadius, paint)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnimation()
    }
}
