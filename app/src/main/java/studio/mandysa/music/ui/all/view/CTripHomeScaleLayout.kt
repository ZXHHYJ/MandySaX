package studio.mandysa.music.ui.all.view

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout


/**
 * Created by kuangxiaoguo on 16/8/30.
 */
/**
 * Modification by liuxiaoliu66 on 2021/9/8
 */
/**
 * Modification by liuxiaoliu66 on 2021/9/19
 */
class CTripHomeScaleLayout @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(
    context!!, attrs, defStyleAttr
) {
    /**
     * 判断缩小动画有没有执行过
     */
    private var hasDoneAnimation = false

    /**
     * 点击事件监听
     */
    private var listener: OnClickListener? = null

    private val beginAnimator: ObjectAnimator =
        ObjectAnimator.ofPropertyValuesHolder(this, beginX, beginY)
            .setDuration(DURATION.toLong())

    private val backAnimator: ObjectAnimator =
        ObjectAnimator.ofPropertyValuesHolder(this, backX, backY)
            .setDuration(DURATION.toLong())

    private var lastClickTime: Long = 0

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (isFastClick) {
                    return true
                }
                parent.requestDisallowInterceptTouchEvent(true)

                hasDoneAnimation = false
                beginAnimator
                    .start()
            }
            MotionEvent.ACTION_MOVE -> {
                if (!hasDoneAnimation
                ) {
                    /**
                     * 拦截我们的事件,让最外层控件处理接下来的事件,比如scrollview的滑动.
                     * 二 因为我们执行了滑动操作,所以要执行view的返回动画
                     */
                    parent.requestDisallowInterceptTouchEvent(false)
                    hasDoneAnimation = true
                    backAnimator
                        .start()
                    return false
                }
            }
            MotionEvent.ACTION_UP ->
                /**
                 * 这里如果我们是单纯的点击事件就会执行
                 */
                if (!hasDoneAnimation) {
                    hasDoneAnimation = true
                    backAnimator
                        .start()
                    listener?.onClick(this@CTripHomeScaleLayout)
                    return true
                }
        }
        return true
    }

    override fun setOnClickListener(l: OnClickListener?) {
        listener = l
    }

    /**
     * Judge is fast click event.
     *
     * @return Is fast click or not.
     */
    private val isFastClick: Boolean
        get() {
            val time = System.currentTimeMillis()
            val timeD = time - lastClickTime
            return if (timeD < TIME_DELAY) {
                true
            } else {
                lastClickTime = time
                false
            }
        }

    companion object {
        /**
         * 动画持续时长
         */
        private const val DURATION = 100

        /**
         * 快速点击的时间间隔
         */
        private const val TIME_DELAY = 500

        /**
         * 缩放的scale
         */
        private const val SMALL_SCALE = 0.95f

        /**
         * 初始scale
         */
        private const val ONE_SCALE = 1f

        private val beginX = PropertyValuesHolder.ofFloat("scaleX", ONE_SCALE, SMALL_SCALE)
        private val beginY = PropertyValuesHolder.ofFloat("scaleY", ONE_SCALE, SMALL_SCALE)
        private val backX = PropertyValuesHolder.ofFloat("scaleX", SMALL_SCALE, ONE_SCALE)
        private val backY = PropertyValuesHolder.ofFloat("scaleY", SMALL_SCALE, ONE_SCALE)
    }

}