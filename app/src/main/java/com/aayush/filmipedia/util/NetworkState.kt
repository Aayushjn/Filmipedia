package com.aayush.filmipedia.util

class NetworkState(val status: Status, val msg: String) {
    enum class Status {
        RUNNING,
        SUCCESS,
        FAILED
    }

    companion object {
        val LOADED = NetworkState(Status.SUCCESS, "Success")
        val LOADING = NetworkState(Status.RUNNING, "Running")
    }
}