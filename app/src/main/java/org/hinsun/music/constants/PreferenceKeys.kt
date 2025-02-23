package org.hinsun.music.constants

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import org.hinsun.music.BuildConfig

val IsFirstLaunchKey = booleanPreferencesKey("${BuildConfig.PREFIX}.isFirstLaunchKey")
val IsEnableCryptoStorageKey =
    booleanPreferencesKey("${BuildConfig.PREFIX}.isEnableCryptoStorageKey")
val MaxSongCacheSizeKey = intPreferencesKey("${BuildConfig.PREFIX}.maxSongCacheSizeKey")
val CurrentSongIdKey = intPreferencesKey("${BuildConfig.PREFIX}.currentSongIdKey")