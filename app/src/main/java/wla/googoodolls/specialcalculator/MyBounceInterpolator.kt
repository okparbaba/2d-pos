package wla.googoodolls.specialcalculator

import kotlin.math.cos
import kotlin.math.pow

class MyBounceInterpolator(amplitude: Double, frequency: Double) : android.view.animation.Interpolator {
    private var mAmplitude = 0.5
    private var mFrequency = 0.5

    init {
        mAmplitude = amplitude
        mFrequency = frequency
    }

    override fun getInterpolation(time: Float): Float {
        return (-1.0 * Math.E.pow(-time / mAmplitude) *
                cos(mFrequency * time) + 1).toFloat()
    }

}