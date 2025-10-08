package com.harry.phrasebook.core.model

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Suppress("unused")
fun _smokeTest(card: Card): String {
    return Json.encodeToString(card)  // 编译能过，就说明序列化插件生效了
}