package com.alishoumar.diaryapp.presentation.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.alishoumar.diaryapp.model.GalleryImage
import com.alishoumar.diaryapp.model.GalleryState
import com.alishoumar.diaryapp.ui.theme.Elevation
import kotlin.math.max

@Composable
fun Gallery(
    modifier: Modifier = Modifier,
    images: List<String>,
    imageSize: Dp = 40.dp,
    spaceBetween: Dp = 10.dp,
    imageShape:CornerBasedShape = Shapes().small
    ) {
    BoxWithConstraints (
        modifier = modifier
    ){
        val numberOfVisibleImages = remember {
            derivedStateOf {
                max(a=0,b=this.maxWidth.div(spaceBetween+imageSize).toInt().minus(1))
            }
        }
        val remainingImages = remember {
            derivedStateOf {
                images.size - numberOfVisibleImages.value
            }
        }

        Row {
            images.take(numberOfVisibleImages.value).forEach{
                image ->
                AsyncImage(
                    modifier = modifier
                        .clip(imageShape)
                        .size(imageSize),
                    model = ImageRequest
                    .Builder(LocalContext.current)
                    .data(image)
                    .crossfade(true)
                    .build(),
                    contentDescription = "Gallery Image")
                Spacer(modifier = modifier.width(spaceBetween))
            }
            if(remainingImages.value > 0){
                LastImageOverlay(
                    imageSize = imageSize,
                    remainingImages = remainingImages.value,
                    imageShape = imageShape
                )
            }
        }
    }
}


@Composable
fun GalleryUploader(
    modifier: Modifier = Modifier,
    galleryState: GalleryState,
    imageSize: Dp = 60.dp,
    imageShape: CornerBasedShape = Shapes().medium,
    spaceBetween: Dp = 12.dp,
    onAddClick: ()-> Unit,
    onImageSelect:(Uri) -> Unit,
    onImageClicked:(GalleryImage) -> Unit
                    ) {

    val multiplePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 8)
    ) { images ->
        images.forEach {
            onImageSelect(it)
        }
    }
        BoxWithConstraints(
            modifier = modifier
        ) {
            val numberOfVisibleImages = remember {
                derivedStateOf {
                    max(a = 0, b = this.maxWidth.div(spaceBetween + imageSize).toInt().minus(2))
                }
            }
            val remainingImages = remember {
                derivedStateOf {
                    galleryState.images.size - numberOfVisibleImages.value
                }
            }

            Row {

                AddImageButton(
                    imageSize = imageSize,
                    imageShape = imageShape,
                    onAddClick = {
                        onAddClick()
                        multiplePhotoPicker.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }
                )

                Spacer(modifier = Modifier.width(spaceBetween))
                galleryState.images.take(numberOfVisibleImages.value).forEach { galleryImage ->
                    AsyncImage(
                        modifier = modifier
                            .clip(imageShape)
                            .size(imageSize),
                        model = ImageRequest
                            .Builder(LocalContext.current)
                            .data(galleryImage.image)
                            .crossfade(true)
                            .build(),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Gallery Image"
                    )
                    Spacer(modifier = modifier.width(spaceBetween))
                }
                if (remainingImages.value > 0) {
                    LastImageOverlay(
                        imageSize = imageSize,
                        remainingImages = remainingImages.value,
                        imageShape = imageShape
                    )
                }
            }
        }
    }


@Composable
fun AddImageButton(
    imageSize:Dp,
    imageShape:CornerBasedShape,
    onAddClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(imageSize)
            .clip(imageShape),
        onClick = onAddClick,
        tonalElevation = Elevation.level1
    ) {
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Icon(imageVector = Icons.Default.Add,
                contentDescription = "Add Icon")
        }
    }
}

@Composable
fun LastImageOverlay(
    imageSize: Dp,
    remainingImages: Int,
    imageShape: CornerBasedShape
) {
    Box (contentAlignment = Alignment.Center){
        Surface (
            modifier = Modifier
                .clip(imageShape)
                .size(imageSize),
            color = MaterialTheme.colorScheme.primaryContainer
        ){}
        Text(
            text = "+$remainingImages",
            style = TextStyle(
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onPrimaryContainer
            )
    }
}

