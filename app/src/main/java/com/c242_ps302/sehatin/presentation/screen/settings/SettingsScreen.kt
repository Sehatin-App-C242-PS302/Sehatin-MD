package com.c242_ps302.sehatin.presentation.screen.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.c242_ps302.sehatin.R
import com.c242_ps302.sehatin.presentation.components.sehatin_appbar.SehatinAppBar
import com.c242_ps302.sehatin.presentation.components.settings_item.Language
import com.c242_ps302.sehatin.presentation.components.settings_item.LanguageSettingsItem
import com.c242_ps302.sehatin.presentation.components.settings_item.SettingsItem
import com.c242_ps302.sehatin.presentation.theme.SehatinTheme
import com.c242_ps302.sehatin.presentation.utils.LanguageChangeHelper

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    languageChangeHelper: LanguageChangeHelper = hiltViewModel()
) {
    val context = LocalContext.current

    val languagesList = listOf(
        Language("en", "English", R.drawable.uk_flag),
        Language("in", "Indonesia", R.drawable.indo_flag)
    )

    val onCurrentLanguageChange: (String) -> Unit = { languageCode ->
        languageChangeHelper.changeLanguage(context, languageCode)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        SehatinAppBar()
        Spacer(modifier = Modifier.height(40.dp))
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Person Icon",
            modifier = Modifier
                .padding(20.dp)
                .size(160.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.inversePrimary)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "User Name",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "userpertama@gmail.com",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(20.dp))

        SettingsItem(
            leadingIcon = Icons.Default.Person,
            text = stringResource(R.string.account),
            trailingIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))
        LanguageSettingsItem(
            leadingIcon = Icons.Default.Language,
            languagesList = languagesList,
            text = stringResource(R.string.language),
            onCurrentLanguageChange = onCurrentLanguageChange,
            languageChangeHelper = languageChangeHelper
        )
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    SehatinTheme {
        SettingsScreen()
    }
}