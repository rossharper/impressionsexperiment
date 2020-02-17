package net.rossharper.impressionsexperiment.impressions.domain


interface TimestampProvider {
    val timeInMillis: Long
}