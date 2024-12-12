package com.c242_ps302.sehatin.ui.components.sehatin_appbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.c242_ps302.sehatin.R
import com.c242_ps302.sehatin.ui.theme.SehatinTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SehatinAppBar(
    modifier: Modifier = Modifier,
    title: String = stringResource(id = R.string.app_name),
    navigationIcon: Boolean = false,
    onNavigateUp: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
//        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append(
                            title.substring(0, 5)
                        )
                    }
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.inversePrimary)) {
                        append(
                            title.substring(5)
                        )
                    }
                },
                fontWeight = FontWeight.ExtraBold
            )
        },
        navigationIcon = {
            if (navigationIcon) {
                IconButton(
                    onClick = {
                        onNavigateUp()
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            scrolledContainerColor = MaterialTheme.colorScheme.background
        ),
    )
}

@Preview
@Composable
private fun SehatinAppBarPreview() {
    SehatinTheme {
        SehatinAppBar()
    }
}