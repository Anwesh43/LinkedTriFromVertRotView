package com.anwesh.uiprojects.trifromvertrotview

/**
 * Created by anweshmishra on 26/11/18.
 */

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.*

val nodes : Int = 5
val tris : Int = 3
val sizeFactor : Int = 3
val strokeFactor : Int = 90
val scDiv : Double = 0.51
val scGap : Float = 0.05f
val color : Int = Color.parseColor("#311B92")

fun Int.getInverse() : Float = 1f / this

fun Float.divideScale(i : Int, n : Int) = Math.min(n.getInverse(), Math.max(0f, this - i * n.getInverse())) * n

fun Float.scaleFactor() : Float = Math.floor(this / scDiv).toFloat()

fun Float.mirrorValue(a : Int, b : Int) : Float = (1 - scaleFactor()) * a.getInverse() + scaleFactor() * b.getInverse()

fun Float.updateScale(dir : Float, a : Int, b : Int) : Float = mirrorValue(a, b) * scGap * dir

fun Float.getPointInCircle(deg : Double) : PointF = PointF(this * Math.cos(deg).toFloat(), this * Math.sin(deg).toFloat())

fun Canvas.drawTriangle(r : Float, deg : Double, paint : Paint) {
    val path : Path = Path()
    for (j in (0..2)) {
        val pointInCircle : PointF = r.getPointInCircle(deg/4 + deg * j)
        if (j == 0) {
            path.moveTo(pointInCircle.x, pointInCircle.y)
        } else {
            path.lineTo(pointInCircle.x, pointInCircle.y)
        }
    }
    drawPath(path, paint)
}

fun Canvas.drawLineToVertex(r : Float, i : Int, deg : Double, paint : Paint) {
    val vertex : PointF = r.getPointInCircle(deg/4 + deg * i)
    drawLine(0f, 0f, vertex.x, vertex.y, paint)
}

fun Canvas.drawTFVRNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val gap : Float = w / (nodes + 1)
    val size : Float = gap / sizeFactor
    val deg : Double = 2 * Math.PI / (tris)
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    paint.strokeCap = Paint.Cap.ROUND
    paint.color = color
    val sc1 : Float = scale.divideScale(0, 2)
    val sc2 : Float = scale.divideScale(1, 2)
    save()
    translate(gap * (i + 1), h / 2)
    rotate(180f * sc2)
    drawTriangle(size, deg, paint)
    for (j in 0..(tris - 1)) {
        val sc : Float = sc1.divideScale(j, tris)
        drawLineToVertex(size * sc, j, deg, paint)
    }
    restore()
}

class TriFromVertRotView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var prevScale : Float = 0f, var dir : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += scale.updateScale(dir, tris, 1)
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }
}