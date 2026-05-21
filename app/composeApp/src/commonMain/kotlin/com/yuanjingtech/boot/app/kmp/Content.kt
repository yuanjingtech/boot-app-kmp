package com.yuanjingtech.boot.app.kmp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.yuanjingtech.boot.app.kmp.network.TestNetworkScreen
import com.yuanjingtech.boot.app.kmp.subapp.TestSubAppScreen
import com.yuanjingtech.boot.app.kmp.webview.WebViewDemoScreen
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Serializable
private data object RouteA : NavKey

@Serializable
private data class RouteB(val id: String) : NavKey

@Serializable
private data class RouteC(val id: String) : NavKey

private val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(RouteA::class, RouteA.serializer())
            subclass(RouteB::class, RouteB.serializer())
            subclass(RouteC::class, RouteC.serializer())
        }
    }
}

@Composable
fun Content(
    modifier: Modifier = Modifier.fillMaxSize(),
) {
    val backStack = rememberNavBackStack(config, RouteA)
    Column(modifier) {
        Row {
            Button(onClick = { backStack.add(RouteA) }) {
                Text("WebViewDemoScreen")
            }
            Button(onClick = { backStack.add(RouteB("id")) }) {
                Text("TestSubAppScreen")
            }
            Button(onClick = { backStack.add(RouteC("id")) }) {
                Text("TestNetworkScreen")
            }
        }
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<RouteA> {
                    WebViewDemoScreen(Modifier.fillMaxSize())
                }
                entry<RouteB> { key ->
                    TestSubAppScreen(Modifier.fillMaxSize())
                }
                entry<RouteC> { key ->
                    TestNetworkScreen(Modifier.fillMaxSize())
                }
            }
        )

    }
}

