package com.example.reminder.screens

import android.app.LocaleManager
import android.content.Context
import android.os.LocaleList
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reminder.R

data class AppThemePalette(val primary: Color, val onPrimary: Color,
                           val secondary: Color, val onSecondary: Color,
                           val tertiary: Color, val onTeriary: Color)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawSettingScreen(onBack: () -> Unit, selectedTheme: String, onThemeSelected: (String) -> Unit) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Настройки", fontSize = 32.sp, fontFamily = FontFamily(Font(R.font.huninn_regular)), fontWeight = FontWeight.W400) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Назад")
                }
            }
        )
    }) { padding ->
        ColorPicker(
            modifier = Modifier.padding(padding),
            selectedTheme = selectedTheme,
            onThemeSelected = onThemeSelected
        )
    }
}

@Composable
fun ColorPicker(modifier: Modifier = Modifier, selectedTheme: String, onThemeSelected: (String) -> Unit) {
    val themes = listOf(
        stringResource(R.string.blue_theme) to AppThemePalette(
            Color(0xFFaac7ff), Color(0xFF0a305f),
            Color(0xFFbec6dc), Color(0xFF283141),
            Color(0xFFddbce0), Color(0xFF3f2844)),
        stringResource(R.string.yellow_theme) to AppThemePalette(
            Color(0xFFdbc66e), Color(0xFF3a3000),
            Color(0xFFd1c6a1), Color(0xFF363016),
            Color(0xFFa9d0b3), Color(0xFF143723)),
        stringResource(R.string.green_theme) to AppThemePalette(
            Color(0xFFb1d18a),Color(0xFF1f3701),
            Color(0xFFbfcbad), Color(0xFF2a331e),
            Color(0xFFa0d0cb),Color(0xFF003735)),
        stringResource(R.string.red_theme) to AppThemePalette(
            Color(0xFFffb5a0),Color(0xFF561f0f),
            Color(0xFFe7bdb2), Color(0xFF442a22),
            Color(0xFFd8c58d), Color(0xFF3b2f05))
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        themes.chunked(2).forEach { themeRow ->
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                themeRow.forEach { (name, palette) ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onThemeSelected(name) },
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedTheme == name) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceBright
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(name, fontFamily = FontFamily(Font(R.font.huninn_regular)))
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                listOf(palette.primary, palette.secondary, palette.tertiary).forEach { color ->
                                    Box(Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(color))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
