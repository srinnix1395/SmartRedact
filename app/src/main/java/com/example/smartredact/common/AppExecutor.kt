package com.example.smartredact.common

import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Created by TuHA on 11/8/2019.
 */
object AppExecutor {

    val ioExecutor: Executor by lazy {
        return@lazy Executors.newFixedThreadPool(2)
    }
}