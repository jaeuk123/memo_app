package com.test.memoapp.core.navigatiton

import kotlinx.serialization.Serializable

@Serializable
sealed interface SettingsRoute : Route {
    @Serializable
    data object Graph : SettingsRoute
    @Serializable data object SettingsHome : SettingsRoute
}