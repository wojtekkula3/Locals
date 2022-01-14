package com.wojciechkula.locals.di

import android.graphics.Bitmap
import com.wojciechkula.locals.common.bitmap.BitmapConfig
import com.wojciechkula.locals.common.bitmap.BitmapService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class BitmapModule {

    @Provides
    fun bitmapConfig() = BitmapConfig(
        compressFormat = Bitmap.CompressFormat.JPEG,
        quality = 80
    )

    @Provides
    fun bitmapService(bitmapConfig: BitmapConfig) = BitmapService(bitmapConfig)
}