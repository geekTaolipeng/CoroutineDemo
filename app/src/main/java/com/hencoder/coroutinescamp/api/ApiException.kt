package com.hencoder.coroutinescamp.api

class ApiException(var code: Int, override var message: String) : RuntimeException()