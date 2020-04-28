package com.ilham.made.myunittesting.Model

class CuboidModel {
    private var width: Double = 0.0
    private var height: Double = 0.0
    private var length: Double = 0.0

    fun getVolume(): Double = width * length * height

    fun getSurfaceArea(): Double {
        val wl = width * length
        val wh = width * height
        val lh = length * height

        return 2 * (wl + wh + lh)
    }

    fun getCircumference(): Double = 4 * (width + length + height)

    fun save(width: Double, height: Double, length: Double){
        this.height = height
        this.length = length
        this.width = width
    }
}