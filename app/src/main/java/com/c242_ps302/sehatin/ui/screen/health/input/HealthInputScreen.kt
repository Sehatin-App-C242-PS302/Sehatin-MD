package com.c242_ps302.sehatin.ui.screen.health.input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.c242_ps302.sehatin.R
import com.c242_ps302.sehatin.ui.components.sehatin_appbar.SehatinAppBar
import com.c242_ps302.sehatin.ui.components.toast.SehatinToast
import com.c242_ps302.sehatin.ui.components.toast.ToastType
import com.c242_ps302.sehatin.ui.theme.SehatinTheme

@Composable
fun HealthInputScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    onSuccess: () -> Unit,
    recommendationViewModel: RecommendationViewModel = hiltViewModel()
) {
    val state by recommendationViewModel.healthInputState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    var expanded by remember { mutableStateOf(false) }
    var selectedGender by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var dropdownMenuHeight by remember { mutableStateOf(0.dp) }
    val localDensity = LocalDensity.current

    val genders = listOf(stringResource(R.string.male), stringResource(R.string.female))

    var toastMessage by remember { mutableStateOf("") }
    var toastType by remember { mutableStateOf(ToastType.INFO) }
    var showToast by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        if (state.error != null) {
            toastMessage = state.error ?: context.getString(R.string.unknown_error)
            toastType = ToastType.ERROR
            showToast = true
            recommendationViewModel.clearError()
        } else if (!state.isLoading && state.success) {
            toastMessage = context.getString(R.string.health_data_uploaded_successfully)
            toastType = ToastType.SUCCESS
            showToast = true
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AnimatedVisibility(
            visible = state.isLoading,
            enter = fadeIn() + expandVertically()
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp)
            )
        }

        AnimatedVisibility(
            visible = state.error != null,
            enter = fadeIn() + expandVertically()
        ) {
            Text(
                text = state.error ?: stringResource(R.string.unknown_error),
                color = MaterialTheme.colorScheme.error,
                maxLines = 2
            )
        }

        AnimatedVisibility(
            visible = !state.isLoading && state.error == null,
            enter = fadeIn() + expandVertically()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                SehatinAppBar(
                    navigationIcon = true,
                    onNavigateUp = onNavigateUp
                )
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    text = stringResource(R.string.masukkan_data_kamu_yuk),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(20.dp))

                Column {
                    Text(
                        text = selectedGender.ifEmpty { stringResource(R.string.gender) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { expanded = !expanded }
                            .padding(16.dp)
                            .onGloballyPositioned { coordinates ->
                                dropdownMenuHeight =
                                    with(localDensity) { coordinates.size.height.toDp() }
                            },
                        color = if (selectedGender.isEmpty()) MaterialTheme.colorScheme.onSurfaceVariant.copy(
                            alpha = 0.6f
                        )
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth(0.9f),
                        offset = DpOffset(0.dp, 0.dp)
                    ) {
                        genders.forEach { gender ->
                            DropdownMenuItem(
                                text = { Text(gender) },
                                onClick = {
                                    selectedGender = gender
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Input fields
                InputRow(icon = Icons.Default.CalendarToday, label = stringResource(R.string.age), value = age) { age = it }
                Spacer(modifier = Modifier.height(16.dp))
                InputRow(icon = Icons.Default.Height, label = stringResource(R.string.height_cm), value = height) { height = it }
                Spacer(modifier = Modifier.height(16.dp))
                InputRow(icon = Icons.Default.Scale, label = stringResource(R.string.weight_kg), value = weight) { weight = it }
                Spacer(modifier = Modifier.height(16.dp))

                // Submit button
                Button(
                    onClick = {
                        val ageInt = age.toIntOrNull() ?: 0
                        val heightDouble = height.toDoubleOrNull() ?: 0.0
                        val weightDouble = weight.toDoubleOrNull() ?: 0.0

                        if (selectedGender.isNotEmpty() && ageInt > 0 && heightDouble > 0.0 && weightDouble > 0.0) {
                            val genderApi = if (selectedGender.lowercase() == "male" || selectedGender.lowercase() == "laki-laki") "Male" else "Female"
                            recommendationViewModel.postRecommendation(
                                gender = genderApi,
                                age = ageInt,
                                height = heightDouble,
                                weight = weightDouble
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    Text(stringResource(R.string.count_my_bmi))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Success button
                AnimatedVisibility(
                    visible = state.success,
                    enter = fadeIn() + expandVertically()
                ) {
                    Button(
                        onClick = { onSuccess() },
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        Text(stringResource(R.string.see_result))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = stringResource(R.string.see_result),
                        )
                    }
                }
            }
        }
    }

    if (showToast) {
        SehatinToast(
            message = toastMessage,
            type = toastType,
            duration = 2000L,
            onDismiss = { showToast = false }
        )
    }
}

@Composable
fun InputRow(icon: ImageVector, label: String, value: String, onValueChange: (String) -> Unit) {
    val focusManager = LocalFocusManager.current

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier
                .size(24.dp)
                .padding(end = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
    }
}


@Composable
@Preview
fun HealthInputScreenPreview() {
    SehatinTheme {
        HealthInputScreen(
            onSuccess = {},
            onNavigateUp = {}
        )
    }
}
