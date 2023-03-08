

![banner](https://github.com/litao0621/NiftySlider/blob/main/art/banner1.png)  

![NiftySliderVersion](https://maven-badges.herokuapp.com/maven-central/io.github.litao0621/nifty-slider/badge.svg)
[![License](https://img.shields.io/badge/License%20-Apache%202-337ab7.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)
｜ [View documentation][2] | [中文][3]

# Getting Started

1. Maven library dependency
``` groovy
dependencies {
    implementation 'io.github.litao0621:nifty-slider:(latest version)'
    // Added effect for Sliders (Optional Features)
    implementation 'io.github.litao0621:nifty-slider-effect:(latest version)'
}
```

2. Add it to the layout file

``` xml
    <com.litao.slider.NiftySlider
        android:id="@+id/nifty_slider"
        android:layout_width="match_parent"
        android:layout_height="148dp"
        android:padding="16dp"
        android:value="50"
        android:valueFrom="0"
        android:valueTo="100"
        app:trackColor="@color/m3_demo_track_color"
        app:trackColorInactive="@color/m3_demo_track_inactive_color"
        app:thumbColor="@color/m3_demo_thumb_color"
        app:thumbShadowColor="@color/white"
        app:haloColor="@color/m3_demo_halo_color"/>
```
[View more attributes][1]


3. Registers a callback to be invoked when the slider changes

``` kotlin
        binding.niftySlider.setOnValueChangeListener { slider, value, fromUser ->
            //do something with float value
        }
        binding.niftySlider.setOnIntValueChangeListener { slider, value, fromUser ->
            //do something with int value
        }
        binding.niftySlider.setOnSliderTouchListener(object :NiftySlider.OnSliderTouchListener{
            override fun onStartTrackingTouch(slider: NiftySlider) {
                //do something on touch start
            }
            override fun onStopTrackingTouch(slider: NiftySlider) {
                //do something on touch stop
            }
        })
```

# Custom Effects

### 1. M3 Style
<img src="https://github.com/litao0621/NiftySlider/blob/main/art/m3_style.png" width="480">

  [View Sample](https://github.com/litao0621/NiftySlider/blob/main/simple/src/main/java/com/litao/niftyslider/fragment/M3StyleDemoFragment.kt)
<br/>

### 2. WeRead Style

<img src="https://github.com/litao0621/NiftySlider/blob/main/art/weread_style.png" width="480">

  [View Sample](https://github.com/litao0621/NiftySlider/blob/main/simple/src/main/java/com/litao/niftyslider/fragment/WeReadDemoFragment.kt)
<br/>

### 3. Color Pick Style
<img src="https://github.com/litao0621/NiftySlider/blob/main/art/color_pick_style.gif" width="480">

  [View Sample](https://github.com/litao0621/NiftySlider/blob/main/simple/src/main/java/com/litao/niftyslider/fragment/ColorPickDemoFragment.kt)
<br/>

### 4. Custom Thumb Drawable Style
<img src="https://github.com/litao0621/NiftySlider/blob/main/art/custom_thumb_drawable.png" width="480">

  [View Sample](https://github.com/litao0621/NiftySlider/blob/main/simple/src/main/java/com/litao/niftyslider/fragment/CustomThumbDemoFragment.kt)
<br/>

### 5. Lottie Animation style
<img src="https://github.com/litao0621/NiftySlider/blob/main/art/lottie_anim_style.gif" width="480">

  [View Sample](https://github.com/litao0621/NiftySlider/blob/main/simple/src/main/java/com/litao/niftyslider/fragment/CustomThumbWithLottieDemoFragment.kt)
<br/>

### 6. Tiktok Style

<img src="https://github.com/litao0621/NiftySlider/blob/main/art/tiktok_style.gif" width="480">

  [View Sample](https://github.com/litao0621/NiftySlider/blob/main/simple/src/main/java/com/litao/niftyslider/fragment/TiktokStyleDemoFragment.kt)
<br/>

### 7. BiliBili Style
<img src="https://github.com/litao0621/NiftySlider/blob/main/art/bilibili_style.gif" width="480">

  [View Sample](https://github.com/litao0621/NiftySlider/blob/main/simple/src/main/java/com/litao/niftyslider/fragment/BiliBiliDemoFragment.kt)
<br/>

# Contributing
You are welcome to send push requests (Just remember to add you name to the contributers list) or raise issues

# Donate
If you feel this library has helped you, you can click the Star button to support the author. You can also buy the author a cup of coffee

<img src="https://github.com/litao0621/nifty-slider/blob/main/art/wechat_pay.png" width="280"><img src="https://github.com/litao0621/nifty-slider/blob/main/art/alipay.png" width="280">


# License

    Copyright 2023 litao

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


[1]:https://github.com/litao0621/nifty-slider/wiki/Attribute&Method
[2]:https://github.com/litao0621/nifty-slider/wiki
[3]:https://github.com/litao0621/nifty-slider/blob/main/README-ZH.md
