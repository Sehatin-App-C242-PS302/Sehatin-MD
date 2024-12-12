package com.c242_ps302.sehatin.ui.components.settings_item


import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.c242_ps302.sehatin.ui.utils.LanguageChangeHelper

data class Language(
    val code: String,
    val name: String,
    @DrawableRes val flag: Int,
)


@Composable
fun SettingsItem(
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector,
    text: String,
    onClick: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp),
    ) {
        Icon(
            imageVector = leadingIcon,
            contentDescription = text,
            modifier = Modifier
                .padding(end = 10.dp)
                .size(40.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun LanguageListItem(selectedItem: Language) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape),
            painter = painterResource(selectedItem.flag),
            contentScale = ContentScale.Crop,
            contentDescription = selectedItem.code
        )
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = selectedItem.name,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.W500,
                color = MaterialTheme.colorScheme.onBackground,
            )
        )
    }
}

@Composable
fun LanguageSettingsItem(
    leadingIcon: ImageVector,
    languagesList: List<Language>,
    text: String,
    onCurrentLanguageChange: (String) -> Unit,
    languageChangeHelper: LanguageChangeHelper,
) {
    val context = LocalContext.current
    val currentLanguageCode: String = languageChangeHelper.getLanguageCode(context)
    var currentLanguage by remember { mutableStateOf(currentLanguageCode) }
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(languagesList.first { it.code == currentLanguage }) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .clickable(onClick = { expanded = !expanded }),
    ) {
        Icon(
            imageVector = leadingIcon,
            contentDescription = text,
            modifier = Modifier
                .padding(end = 10.dp)
                .size(40.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 10.dp)
        ) {
            languagesList.forEach { item ->
                DropdownMenuItem(
                    text = {
                        LanguageListItem(selectedItem = item)
                    },
                    onClick = {
                        selectedItem = item
                        expanded = false
                        onCurrentLanguageChange(selectedItem.code)
                        languageChangeHelper.changeLanguage(context, selectedItem.code)
                    }
                )
            }
        }
    }
}



