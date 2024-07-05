package com.alishoumar.diaryapp.model

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember

@Composable
fun rememberGalleryState():GalleryState {
    return remember { GalleryState() }
}

class GalleryState {
     val images = mutableStateListOf<GalleryImage>()
     val imagesToBeDeleted = mutableStateListOf<GalleryImage>()

    fun addImages(image: GalleryImage){
        images.add(image)
    }

    fun deleteImage(image: GalleryImage){
        images.remove(image)
        imagesToBeDeleted.add(image)
    }

    fun clearImagesToBeDelete(){
        imagesToBeDeleted.clear()
    }
}

data class GalleryImage(
    val image:Uri,
    val remoteImagePath:String = ""
)