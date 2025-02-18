package org.hinsun.music.constants

import androidx.datastore.preferences.core.booleanPreferencesKey
import org.hinsun.music.BuildConfig

val IsFirstLaunchKey = booleanPreferencesKey("${BuildConfig.PREFIX}.isFirstLaunchKey")
val IsEnableCryptoStorageKey =
    booleanPreferencesKey("${BuildConfig.PREFIX}.isEnableCryptoStorageKey")