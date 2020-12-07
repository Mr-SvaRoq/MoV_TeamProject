package com.example.practice_demo.wall.data.model

import android.content.Context
import com.example.practice_demo.R
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.upstream.cache.CacheDataSink
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File


public class CacheDataSourceFactory(context: Context, maxCacheSize: Long, maxFileSize: Long) : DataSource.Factory {
    private val defaultDataSourceFactory: DefaultDataSourceFactory
    private val simpleCache: SimpleCache = SimpleCache(
        File(context.cacheDir, "media"),
        LeastRecentlyUsedCacheEvictor(maxCacheSize)
    )
    private val cacheDataSink: CacheDataSink = CacheDataSink(simpleCache, maxFileSize)
    private val fileDataSource: FileDataSource = FileDataSource()

    init {
        val userAgent = context.getString(R.string.app_name)
        val bandwidthMeter = DefaultBandwidthMeter()
        defaultDataSourceFactory = DefaultDataSourceFactory(
            context,
            bandwidthMeter,
            DefaultHttpDataSourceFactory(userAgent, bandwidthMeter)
        )
    }
    override fun createDataSource(): DataSource {
        return CacheDataSource(
            simpleCache, defaultDataSourceFactory.createDataSource(),
            fileDataSource, cacheDataSink,
            CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR, null
        )
    }
}