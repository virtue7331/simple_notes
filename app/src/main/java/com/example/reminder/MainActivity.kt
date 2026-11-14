package com.example.reminder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.reminder.manage.DataStoreManager
import com.example.reminder.manage.ReminderData
import com.example.reminder.screens.DrawSettingScreen
import com.example.reminder.screens.TaskDialog
import com.example.reminder.ui.theme.MyAppTheme
import kotlinx.coroutines.launch
import java.text.DateFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val dataStore = remember { DataStoreManager(this) }
            val selectedTheme by dataStore.selectedThemeFlow.collectAsState(initial = "blue")
            val palette = when (selectedTheme) {
                "yellow" -> com.example.reminder.screens.AppThemePalette(
                    Color(0xFFdbc66e), Color(0xFF3a3000),
                    Color(0xFFd1c6a1), Color(0xFF363016),
                    Color(0xFFa9d0b3), Color(0xFF143723))
                "green" -> com.example.reminder.screens.AppThemePalette(
                    Color(0xFFb1d18a),Color(0xFF1f3701),
                    Color(0xFFbfcbad), Color(0xFF2a331e),
                    Color(0xFFa0d0cb),Color(0xFF003735))
                "red" -> com.example.reminder.screens.AppThemePalette(
                    Color(0xFFffb5a0),Color(0xFF561f0f),
                    Color(0xFFe7bdb2), Color(0xFF442a22),
                    Color(0xFFd8c58d), Color(0xFF3b2f05))
                else -> com.example.reminder.screens.AppThemePalette(
                    Color(0xFFaac7ff), Color(0xFF0a305f),
                    Color(0xFFbec6dc), Color(0xFF283141),
                    Color(0xFFddbce0), Color(0xFF3f2844))
            }
            MyAppTheme(dynamicColor = false, palette = palette) {
                val navController = rememberNavController();

                NavHost(
                    navController = navController,
                    startDestination = "main",
                    enterTransition = {
                        val movingToSettings = targetState.destination.route == "settings"
                        slideInHorizontally(
                            initialOffsetX = { width -> if (movingToSettings) width else -width },
                            animationSpec = tween(350)
                        ) + fadeIn(animationSpec = tween(350))
                    },
                    exitTransition = {
                        val movingToSettings = targetState.destination.route == "settings"
                        slideOutHorizontally(
                            targetOffsetX = { width -> if (movingToSettings) -width / 4 else width / 4 },
                            animationSpec = tween(350)
                        ) + fadeOut(animationSpec = tween(250))
                    }
                ) {
                    composable("main") {
                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                            DrawableContent(
                                modifier = Modifier.padding(innerPadding),
                                onSettingsClick = { navController.navigate("settings") }
                            )
                        }
                    }
                    composable("settings") {
                        val themeNames = mapOf(
                            "blue" to getString(R.string.blue_theme),
                            "yellow" to getString(R.string.yellow_theme),
                            "green" to getString(R.string.green_theme),
                            "red" to getString(R.string.red_theme)
                        )
                        DrawSettingScreen(
                            onBack = { navController.popBackStack() },
                            selectedTheme = themeNames[selectedTheme] ?: getString(R.string.blue_theme),
                            onThemeSelected = { name ->
                                val key = themeNames.entries.firstOrNull { it.value == name }?.key ?: "blue"
                                kotlinx.coroutines.MainScope().launch { dataStore.saveSelectedTheme(key) }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DrawableContent(
    modifier: Modifier = Modifier,
    onSettingsClick: () -> Unit
) {
    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.surface).fillMaxSize()
    )
    ScreenContent(modifier, onSettingsClick)
}

@Composable
fun ScreenContent(modifier: Modifier = Modifier, onSettingsClick: () -> Unit) {
    //Header
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            stringResource(R.string.app_name),
            modifier = Modifier.padding(start = 12.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 32.sp,
            fontFamily = FontFamily(Font(R.font.huninn_regular)),
            fontWeight = FontWeight.W400
        )
        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier.padding(end = 6.dp),
            colors = IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.primary),
            shape = MaterialTheme.shapes.medium,
        ) {
            Icon(
                imageVector = Icons.Rounded.Settings,
                contentDescription = "Настройки",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }

    var openAddDialog by rememberSaveable { mutableStateOf(false) }

    //Footer-Button
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        IconButton(
            onClick = { openAddDialog = true },
            modifier = Modifier
                .padding(12.dp)
                .size(60.dp),
            colors = IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.tertiary),
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.Rounded.Add,
                contentDescription = "Настройки",
                tint = MaterialTheme.colorScheme.onTertiary
            )
        }
    }

    val context = LocalContext.current
    val dataStore = remember { DataStoreManager(context) }
    val scope = rememberCoroutineScope()
    val reminders by dataStore.remindersFlow.collectAsState(initial = emptyList())

    var editingId by rememberSaveable { mutableStateOf<Long?>(null) }
    val editingReminder = reminders.find { it.id == editingId }

    //AddDialog
    if (openAddDialog) {
        TaskDialog(onDismiss = { openAddDialog = false }, onConfirm = {
            name, description ->
            scope.launch { dataStore.addReminder(ReminderData(name, description)) }
            openAddDialog = false
        })
    }

    Box(modifier = modifier.padding(top = 60.dp)) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(reminders, key = { it.id }) { reminder ->
                DrawReminderCard(
                    reminder,
                    onDelete = { scope.launch { dataStore.deleteRemind(reminder.id) } },
                    onEdit = { editingId = reminder.id })
            }
        }
    }

    editingReminder?.let { reminder ->
        TaskDialog(
            initial = reminder,
            onDismiss = { editingId = null },
            onConfirm = { name, description ->
                scope.launch {
                    dataStore.updateReminder(
                        reminder.copy(name = name, description = description)
                    )
                }
                editingId = null
            }
        )
    }
}

@Composable
fun DrawReminderCard(reminder: ReminderData, onDelete: () -> Unit, onEdit: () -> Unit) {
    val createdAt = java.util.Date(reminder.id)
    val dateFormat = DateFormat.getDateInstance().format(createdAt)
    val timeFormat = DateFormat.getTimeInstance().format(createdAt)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(Modifier.padding(start = 6.dp, top = 6.dp, end = 6.dp).fillMaxWidth()) {
            Text(reminder.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.W700,
                fontFamily = FontFamily(Font(R.font.huninn_regular)))
            if (reminder.description.isNotBlank()) {
                var isClickDescription by rememberSaveable { mutableStateOf(false) }
                var hasMultipleLines by remember { mutableStateOf(false) }
                Row(
                    modifier = Modifier.fillMaxWidth().clickable {
                        if (hasMultipleLines) isClickDescription = !isClickDescription
                    },
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = reminder.description,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.huninn_regular)),
                        modifier = Modifier.weight(1f),
                        maxLines = if (isClickDescription) Int.MAX_VALUE else 1,
                        onTextLayout = { layoutResult: TextLayoutResult ->
                            hasMultipleLines = layoutResult.lineCount > 1 || layoutResult.hasVisualOverflow
                        }
                    )
                    if (hasMultipleLines) {
                        Icon(
                            imageVector = if (isClickDescription) Icons.Rounded.ArrowDropUp else Icons.Rounded.ArrowDropDown,
                            contentDescription = if (isClickDescription) "Свернуть описание" else "Развернуть описание",
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }
            }
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onDelete,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = stringResource(R.string.button_delete),
                    modifier = Modifier.size(20.dp)
                )
            }
            IconButton(
                onClick = onEdit,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = stringResource(R.string.button_edit),
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = "$dateFormat $timeFormat",
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.huninn_regular)),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
