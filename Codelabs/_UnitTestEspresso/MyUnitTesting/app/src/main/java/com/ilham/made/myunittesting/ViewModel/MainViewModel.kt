package com.ilham.made.myunittesting.ViewModel

import com.ilham.made.myunittesting.Model.CuboidModel

class MainViewModel(private val cuboidModel: CuboidModel) {

    fun getCircumference(): Double = cuboidModel.getCircumference()
    fun getVolume(): Double = cuboidModel.getVolume()
    fun getSurfaceArea(): Double = cuboidModel.getSurfaceArea()
    fun save(l: Double, w: Double, h: Double) {
        cuboidModel.save(l,w,h)
    }
}