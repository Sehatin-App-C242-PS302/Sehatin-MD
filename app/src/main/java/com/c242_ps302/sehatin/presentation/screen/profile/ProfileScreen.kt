package com.c242_ps302.sehatin.presentation.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.c242_ps302.sehatin.R
import com.c242_ps302.sehatin.presentation.components.sehatin_appbar.SehatinAppBar
import com.c242_ps302.sehatin.presentation.theme.SehatinTheme

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            SehatinAppBar(
                navigationIcon = true,
                onNavigateUp = onNavigateUp
            )
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(.7f)
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {

                    },
                    label = { Text(text = stringResource(R.string.name)) },
                    isError = false,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = "",
                    onValueChange = {

                    },
                    label = { Text(text = stringResource(R.string.email)) },
                    isError = false,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(30.dp))
                Button(
                    onClick = {}
                ) {
                    Text(
                        text = "Save"
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun ProfileScreenPreview() {
    SehatinTheme {
        ProfileScreen(
            onNavigateUp = {}
        )
    }
}