package com.litao.slider

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.Dimension
import java.lang.ref.WeakReference
import kotlin.math.roundToInt

/**
 * @author : litao
 * @date   : 2023/3/20 17:37
 */
object Utils {
    fun dpToPx(@Dimension(unit = Dimension.DP) dp: Int): Int {
        val r = Resources.getSystem()
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics).roundToInt()
    }

    fun getWindowWidth(context: Context): Int {
        return dpToPx(context.resources.configuration.screenWidthDp)
    }


    fun isActivityAlive(context: Context?): Boolean {
        return isActivityAlive(getActivityByContext(context)
        )
    }

    fun isActivityAlive(activity: Activity?): Boolean {
        return activity != null && !activity.isFinishing && !activity.isDestroyed
    }

    fun getActivityByContext(context: Context?): Activity? {
        val activity: Activity? = getActivityByContextInner(context)
        return if (!isActivityAlive(activity)) null else activity
    }


    private fun getActivityByContextInner(context: Context?): Activity? {
        var context: Context? = context ?: return null
        val list: MutableList<Context> = ArrayList()
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            val activity = getActivityFromDecorContext(context)
            if (activity != null) return activity
            list.add(context)
            context = context.baseContext
            if (context == null) {
                return null
            }
            if (list.contains(context)) {
                // loop context
                return null
            }
        }
        return null
    }

    private fun getActivityFromDecorContext(context: Context?): Activity? {
        if (context == null) return null
        if (context.javaClass.name == "com.android.internal.policy.DecorContext") {
            try {
                val mActivityContextField = context.javaClass.getDeclaredField("mActivityContext")
                mActivityContextField.isAccessible = true
                return (mActivityContextField[context] as WeakReference<Activity?>).get()
            } catch (ignore: Exception) {
            }
        }
        return null
    }
}