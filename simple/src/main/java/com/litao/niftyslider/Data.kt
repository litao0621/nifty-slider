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
    const val ID_5 = 5
    const val ID_6 = 6
    const val ID_7 = 7
    const val ID_8 = 8
    const val ID_9 = 9
    const val ID_10 = 10
    const val ID_11 = 11
    const val ID_12 = 12

    val styleList = mutableListOf(
        StyleModel(ID_1, "Basics Demo"),
        StyleModel(ID_12,"Adjust Position"),
        StyleModel(ID_2, "M3 Style"),
        StyleModel(ID_3, "In scrolling container"),
        StyleModel(ID_4, "WeRead Style"),
        StyleModel(ID_5, "Color Pick Style"),
        StyleModel(ID_6, "Custom Thumb Drawable"),
        StyleModel(ID_7, "Lottie File Thumb"),
        StyleModel(ID_8, "Tiktok Style"),
        StyleModel(ID_9, "BiliBili Style"),
        StyleModel(ID_10, "YouTube Style"),
        StyleModel(ID_11,"YouTube Chart Style"),

//        StyleModel(ID_12,"Custom Thumb Drawable2")

//        StyleModel(7, "building..."),
//        StyleModel(8, "building..."),
//        StyleModel(9, "building..."),
//        StyleModel(10, "building..."),
//        StyleModel(11, "building..."),
//        StyleModel(12, "building..."),
//        StyleModel(13, "building..."),
//        StyleModel(14, "building...")
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

    val videoImage = listOf(
        R.drawable.img1,
//        R.drawable.video_2,
//        R.drawable.video_3
    )

}