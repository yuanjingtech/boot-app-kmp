package com.yuanjingtech.boot.app.kmp.ui.fluent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun FluentTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector? = null,
    onNavigationClick: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    val colors = LocalFluentColors.current
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.surface)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (navigationIcon != null) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onNavigationClick,
                    )
                    .padding(8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Icon(navigationIcon, contentDescription = "Navigate")
            }
        }
        Text(
            text = title,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            color = colors.textPrimary,
            fontWeight = FontWeight.SemiBold,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            actions()
        }
    }
}

@Composable
fun FluentBottomAppBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit = {},
) {
    val colors = LocalFluentColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.surface)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

@Composable
fun FluentNavigationBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    items: List<Pair<ImageVector, String>> = emptyList(),
) {
    val colors = LocalFluentColors.current
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.surface)
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        items.forEachIndexed { index, (icon, label) ->
            val selected = index == selectedIndex
            val iconColor = if (selected) colors.accent else colors.textSecondary

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = { onItemSelected(index) },
                    )
                    .padding(12.dp),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    }
}

@Composable
fun FluentNavigationRail(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    items: List<Pair<ImageVector, String>> = emptyList(),
) {
    val colors = LocalFluentColors.current
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
            .background(colors.surface)
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        items.forEachIndexed { index, (icon, label) ->
            val selected = index == selectedIndex
            val iconColor = if (selected) colors.accent else colors.textSecondary

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = { onItemSelected(index) },
                    )
                    .padding(12.dp),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    }
}

@Composable
fun FluentTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    tabTitles: List<String> = emptyList(),
    onTabSelected: (Int) -> Unit = {},
) {
    val colors = LocalFluentColors.current
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.surface),
    ) {
        tabTitles.forEachIndexed { index, title ->
            val selected = index == selectedTabIndex
            val textColor = if (selected) colors.accent else colors.textSecondary

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = { onTabSelected(index) },
                    )
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = title,
                    color = textColor,
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                )
            }
        }
    }
}

@Composable
fun FluentListItem(
    headlineContent: String,
    modifier: Modifier = Modifier,
    overlineContent: String = "",
    supportingContent: String = "",
    leadingContent: ImageVector? = null,
    trailingContent: String = "",
) {
    val colors = LocalFluentColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.surface)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leadingContent?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                tint = colors.textSecondary,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(24.dp),
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            if (overlineContent.isNotEmpty()) {
                Text(
                    text = overlineContent,
                    color = colors.textTertiary,
                    fontSize = androidx.compose.ui.unit.TextUnit.Unspecified,
                )
            }
            Text(
                text = headlineContent,
                color = colors.textPrimary,
                fontWeight = FontWeight.Normal,
            )
            if (supportingContent.isNotEmpty()) {
                Text(
                    text = supportingContent,
                    color = colors.textSecondary,
                )
            }
        }
        if (trailingContent.isNotEmpty()) {
            Text(
                text = trailingContent,
                color = colors.textSecondary,
                modifier = Modifier.padding(start = 16.dp),
            )
        }
    }
}

@Composable
fun FluentLargeFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit = {},
    contentDescription: String? = null,
) {
    FluentFAB(onClick = onClick, modifier = modifier, icon = icon, contentDescription = contentDescription)
}

@Composable
fun FluentExtendedFAB(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit = {},
) {
    val colors = LocalFluentColors.current
    val interactionSource = remember { MutableInteractionSource() }
    val backgroundColor = colors.accent
    val shape = RoundedCornerShape(8.dp)

    Row(
        modifier = modifier
            .background(backgroundColor, shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Box(modifier = Modifier.padding(4.dp)) {
                icon()
            }
        }
        Text(text = text, color = colors.accentText)
    }
}