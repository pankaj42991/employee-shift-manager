package com.pktech.newapp.logic

/**
 * Stores rotation indexes so equal rotation is always preserved
 * This can later be persisted in DB or backup
 */
data class RotationState(
    var nightShiftIndex: Int = 0,
    var compOffIndex: Int = 0
)