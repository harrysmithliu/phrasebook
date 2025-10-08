package com.harry.phrasebook.core.utils

import com.harry.phrasebook.core.model.ReviewState
import kotlin.math.max

object Sm2 {
    /** quality: 0..5 */
    fun next(prev: ReviewState, quality: Int, nowMillis: Long = System.currentTimeMillis()): ReviewState {
        val q = quality.coerceIn(0, 5)
        val newEase = (prev.ease + (0.1 - (5 - q) * (0.08 + (5 - q) * 0.02)))
            .coerceAtLeast(1.3)

        val (rep, interval) = when {
            q < 3 -> 0 to 1
            prev.repetitions == 0 -> 1 to 1
            prev.repetitions == 1 -> 2 to 6
            else -> (prev.repetitions + 1) to max(1, (prev.intervalDays * newEase).toInt())
        }

        val nextDue = nowMillis + interval * 86_400_000L // 天 → 毫秒
        return prev.copy(ease = newEase, repetitions = rep, intervalDays = interval, dueAt = nextDue)
    }
}