package com.litao.niftyslider.effect

import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.airbnb.lottie.LottieResult
import com.airbnb.lottie.RenderMode
import com.litao.slider.NiftySlider
import com.litao.slider.effect.BaseEffect

/**
 * @author : litao
 * @date   : 2023/3/3 11:51
 */
class LottieAnimationEffect(private val slider: NiftySlider) : BaseEffect() {

    var animDrawable: LottieDrawable

    private var isAutoLoopMode = false


    init {
        slider.valueFrom = 0f
        slider.valueTo = 1f


        animDrawable = LottieDrawable().apply {
            enableMergePathsForKitKatAndAbove(true)
            callback = slider
            repeatCount = LottieDrawable.INFINITE
            setMinAndMaxProgress(0f,1f)
            addAnimatorUpdateListener {
                slider.invalidate()
            }
            progress = slider.value
        }

        slider.setThumbCustomDrawable(animDrawable)
    }


    override fun onValueChanged(slider: NiftySlider, value: Float, fromUser: Boolean) {
        super.onValueChanged(slider, value, fromUser)
        if (!isAutoLoopMode) {
            animDrawable.progress = value
        }
    }


    fun setAnimation(assetName: String) {
        val result: LottieResult<LottieComposition> =
            LottieCompositionFactory.fromAssetSync(slider.context.applicationContext, assetName)
        animDrawable.composition = result.value
    }

    fun setRenderMode(renderMode: RenderMode) {
        animDrawable.renderMode = renderMode
    }


    fun setAutoLoopMode(isLoop: Boolean){
        if (isLoop == isAutoLoopMode){
            return
        }

        this.isAutoLoopMode = isLoop

        if (isLoop){
            animDrawable.start()
        }else{
            animDrawable.stop()
            animDrawable.progress = slider.value
        }
    }




    

}