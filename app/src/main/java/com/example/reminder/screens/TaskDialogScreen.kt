package com.example.reminder.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reminder.R
import com.example.reminder.manage.ReminderData
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
fun TaskDialog(onDismiss: () -> Unit, onConfirm: (name: String, description: String) -> Unit, initial: ReminderData? = null) {
    var name by rememberSaveable() { mutableStateOf(initial?.name ?: "") }
    var description by rememberSaveable() { mutableStateOf(initial?.description ?: "") }

    AlertDialog(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        onDismissRequest = { onDismiss() },
        title = {
            Column() {
                Text(
                    stringResource(R.string.layer_add_text),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = FontFamily(Font(R.font.huninn_regular)),
                    fontWeight = FontWeight.W700
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = name, onValueChange = {
                            newName -> name = newName.replaceFirstChar {
                                it.titlecase()
                            }
                    },
                    label = {
                        Text(
                            stringResource(R.string.name_field),
                            modifier = Modifier.padding(10.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = FontFamily(Font(R.font.huninn_regular)),
                            fontWeight = FontWeight.W400,
                            fontSize = 15.sp
                        )
                    },
                    textStyle = TextStyle(MaterialTheme.colorScheme.onSurface, 22.sp, fontFamily = FontFamily(Font(
                        R.font.huninn_regular))),
                    shape = MaterialTheme.shapes.medium
                )
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = description, onValueChange = { newDescript -> description = newDescript.replaceFirstChar { it.titlecase() } },
                    label = {
                        Text(
                            stringResource(R.string.description_field),
                            modifier = Modifier.padding(10.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = FontFamily(Font(R.font.huninn_regular)),
                            fontWeight = FontWeight.W400,
                            fontSize = 15.sp
                        )
                    },
                    textStyle = TextStyle(MaterialTheme.colorScheme.onSurface, 22.sp, fontFamily = FontFamily(Font(
                        R.font.huninn_regular))),
                    shape = MaterialTheme.shapes.medium
                )
            }
        },
        confirmButton = {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                OutlinedButton(onClick = { onDismiss() }, shape = MaterialTheme.shapes.large) {
                    Text(
                        stringResource(R.string.field_button_close),
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = FontFamily(Font(R.font.huninn_regular)),
                        fontWeight = FontWeight.W500
                    )
                }
                FilledTonalButton(colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary) ,onClick = { onConfirm(name, description) }, enabled = name.isNotBlank(), shape = MaterialTheme.shapes.large) {
                    Text(
                        if (initial != null) "Редактировать" else "Добавить",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = FontFamily(Font(R.font.huninn_regular)),
                        fontWeight = FontWeight.W500
                    )
                }
            }
        },
        dismissButton = {}
    )
}
