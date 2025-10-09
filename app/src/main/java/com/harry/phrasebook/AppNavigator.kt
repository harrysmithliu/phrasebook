package com.harry.phrasebook

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavController

/** 页面只依赖这个接口，不知道 NavController 的存在 */
interface AppNavigator {
    fun toDetail(id: Long)
    fun back()
}

@Stable
private class AppNavigatorImpl(
    private val nav: NavController
) : AppNavigator {
    override fun toDetail(id: Long) {
        nav.navigate("detail/$id")
    }
    override fun back() {
        nav.navigateUp()
    }
}

/** 在 Compose 中记住一个基于 NavController 的实现 */
@Composable
fun rememberAppNavigator(navController: NavController): AppNavigator =
    remember(navController) { AppNavigatorImpl(navController) }
