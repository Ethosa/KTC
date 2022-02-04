package com.ethosa.ktc.college

interface CollegeCallback<T> {
    fun onResponse(response: T)
}