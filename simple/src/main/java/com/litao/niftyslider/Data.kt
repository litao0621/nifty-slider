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
    const val ID_3 = 3
    const val ID_4 = 4

    val styleList = mutableListOf(
        StyleModel(ID_1, "Basics Demo"),
        StyleModel(ID_2, "M3 Style"),
        StyleModel(ID_3, "In scrolling container"),
        StyleModel(ID_4, "微信阅读菜单样式"),
        StyleModel(5, "建设中..."),
        StyleModel(6, "建设中..."),
        StyleModel(7, "建设中..."),
        StyleModel(8, "建设中..."),
        StyleModel(9, "建设中..."),
        StyleModel(10, "建设中..."),
        StyleModel(11, "建设中..."),
        StyleModel(12, "建设中..."),
        StyleModel(13, "建设中..."),
        StyleModel(14, "建设中...")
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

    fun getTestData(): MutableList<SliderModel> {
        val list = mutableListOf<SliderModel>()
        for (i in 1..100) {
            list.add(SliderModel(0.5f))
        }
        return list
    }


    val weReadFontSizeMap = mapOf(
        16 to 16,
        17 to 17,
        18 to 18,
        19 to 19,
        20 to 20,
        21 to 22,
        22 to 24,
        23 to 27,
        24 to 30,
        25 to 33,
        26 to 36,
        27 to 40
    )

}