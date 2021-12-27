package studio.mandysa.music.logic.utils

import android.content.Context
import android.net.Uri
import com.facebook.common.executors.CallerThreadExecutor
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber
import com.facebook.imagepipeline.request.ImageRequestBuilder


fun getFrescoCacheBitmap(
    context: Context,
    uri: String,
    baseBitmapDataSubscriber: BaseBitmapDataSubscriber
) {
    val imageRequest = ImageRequestBuilder
        .newBuilderWithSource(Uri.parse(uri))
        .setProgressiveRenderingEnabled(true)
        .build()
    val imagePipeline = Fresco.getImagePipeline()
    val dataSource = imagePipeline.fetchDecodedImage(imageRequest, context)
    dataSource.subscribe(baseBitmapDataSubscriber, CallerThreadExecutor.getInstance())
}
