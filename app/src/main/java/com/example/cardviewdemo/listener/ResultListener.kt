package com.example.cardviewdemo.listener

import com.example.cardviewdemo.constant.ImageProvider

/**
 *
 * Generic Class To Listen Async Result
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 04 January 2018
 */
interface ResultListener<T> {

    fun onResult(t: T?)
}
