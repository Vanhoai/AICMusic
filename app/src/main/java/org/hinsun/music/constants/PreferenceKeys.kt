package org.hinsun.music.constants

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

val IsFirstLaunchKey = booleanPreferencesKey("IS_FIRST_LAUNCH_KEY")
val IsEnableCryptoStorageKey = booleanPreferencesKey("IS_ENABLE_CRYPTO_STORAGE_KEY")
val MaxSongCacheSizeKey = intPreferencesKey("MAX_SONG_CACHE_SIZE_KEY")