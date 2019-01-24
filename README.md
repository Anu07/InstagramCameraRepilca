# SquareCamera
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-SquareCamera-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/1745)
## Description
Android module that takes a square photo using the native Android Camera APIs. The new Camera2 APIs from the L release is not used because support has to go back to SDK version 14 for my own requirement. 

## Features
- Tap to focus
- Two fingers zooming
- Front & Back camera
- Flash mode (Saved when the user exits)
- Runtime permission is supported for saving/viewing photos
- Put Filters on image (Filters : Clarendon, OldMan, Mars, Rise, April, amazon etc.)
- Thumbnail list view like instagram
- Gallery View like instagram
- Spinner for different directories selection in top bar
- RecyclerView (Image Grids) for gallery images
- Multiple Image Selection like instagram (Count and frame is displayed, deselection also supported.)
- Videos are displayed in gallery files preview with duration
- Videos can be played for preview.
- Multiple image filters support added.

## SDK Support
Support from SDK version 14 onwards

## Download
jCenter:
```groovy
repositories {
    jcenter()
}

dependencies {
    compile 'com.github.boxme:squarecamera:1.1.0'
}
