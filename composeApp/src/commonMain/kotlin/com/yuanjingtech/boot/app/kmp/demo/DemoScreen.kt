package com.yuanjingtech.boot.app.kmp.demo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.yuanjingtech.boot.app.kmp.Greeting
import com.yuanjingtech.boot.app.kmp.composeapp.generated.resources.Res
import com.yuanjingtech.boot.app.kmp.composeapp.generated.resources.compose_multiplatform
import com.yuanjingtech.boot.app.kmp.ui.AsyncImageView
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DemoScreen(modifier: Modifier = Modifier.Companion) {
    var showContent by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .safeContentPadding()
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row {
            AsyncImageView(
                modifier = Modifier.size(200.dp).clip(CircleShape),
                model = Res.getUri("drawable/apps.svg"),
            )
            AsyncImageView(
                modifier = Modifier.size(200.dp),
                model = Res.getUri("drawable/sample.webp"),
            )
            AsyncImageView(
                modifier = Modifier.size(200.dp),
                model = "https://gips3.baidu.com/it/u=3886271102,3123389489&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960"
            )
            AsyncImageView(
                modifier = Modifier.size(200.dp),
                model = "https://gips3.baidu.com/it/u=1821127123,1149655687&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280"
            )
        }
        Button(onClick = { showContent = !showContent }) {
            Text("Click me!")
        }
        AnimatedVisibility(showContent) {
            val greeting = remember { Greeting().greet() }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(painterResource(Res.drawable.compose_multiplatform), null)
                Text("Compose: $greeting")
            }
        }
    }
}

@Preview
@Composable
fun DemoScreenPreview() {
    MaterialTheme {
        DemoScreen()
    }
}