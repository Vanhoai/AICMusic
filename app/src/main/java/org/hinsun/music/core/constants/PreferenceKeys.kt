package org.hinsun.music.core.constants

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import org.hinsun.music.BuildConfig

val IsFirstLaunchKey = booleanPreferencesKey("${BuildConfig.PREFIX}.IsFirstLaunchKey")
val IsEnableCryptoStorageKey =
    booleanPreferencesKey("${BuildConfig.PREFIX}.IsEnableCryptoStorageKey")
val MaxSongCacheSizeKey = intPreferencesKey("${BuildConfig.PREFIX}.MaxSongCacheSizeKey")
val CurrentSongIdKey = intPreferencesKey("${BuildConfig.PREFIX}.CurrentSongIdKey")
val AccessTokenKey = stringPreferencesKey("${BuildConfig.PREFIX}.AccessToken")
val RefreshTokenKey = stringPreferencesKey("${BuildConfig.PREFIX}.RefreshToken")
val IsEnableBiometricKey = booleanPreferencesKey("${BuildConfig.PREFIX}.IsEnableBiometricKey")
