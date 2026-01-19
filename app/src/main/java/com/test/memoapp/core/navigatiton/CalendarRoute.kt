package com.test.memoapp.core.navigatiton

import kotlinx.serialization.Serializable


@Serializable
sealed interface CalendarRoute : Route {
    @Serializable
    data object Graph : CalendarRoute

    @Serializable data object Calendar : CalendarRoute
    @Serializable data object DayDetail : CalendarRoute
}