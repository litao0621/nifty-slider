package com.litao.niftyslider

import com.litao.niftyslider.model.SliderModel
import com.litao.niftyslider.model.StyleModel

/**
 * @author : litao
 * @date   : 2023/2/15 17:06
 */
object Data {

    const val ID_1 = 1
    const val ID_2 = 2

    val styleList = mutableListOf(
        StyleModel(ID_1,"M3 Style"),
        StyleModel(ID_2,"In scrolling container"),
        StyleModel(3,"建设中..."),
        StyleModel(4,"建设中..."),
        StyleModel(5,"建设中..."),
        StyleModel(6,"建设中..."),
        StyleModel(7,"建设中..."),
        StyleModel(8,"建设中..."),
        StyleModel(9,"建设中..."),
        StyleModel(10,"建设中..."),
        StyleModel(11,"建设中..."),
        StyleModel(12,"建设中..."),
        StyleModel(13,"建设中..."),
        StyleModel(14,"建设中...")
    )

    val colors = mutableListOf(
        "#B71C1C",
        "#880E4F",
        "#4A148C",
        "#311B92",
        "#1A237E",
        "#0D47A1",
        "#01579B",
        "#006064",
        "#004D40",
        "#1B5E20",
        "#33691E",
        "#827717",
        "#F57F17",
        "#FF6F00",
        "#E65100"
    )

    fun getTestData():MutableList<SliderModel>{
        val list = mutableListOf<SliderModel>()
        for (i in 1..100){
            list.add(SliderModel(1))
        }
        return list
    }

}