package com.c242_ps302.sehatin.presentation.components.AppBar

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SehatinAppBar(
    modifier: Modifier = Modifier,
//    scrollBehavior: TopAppBarScrollBehavior,
    title: String = "Sehatin",
//    onSearchClick: () -> Unit = {},
    navigationIcon: @Composable () -> Unit = {}
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
//        actions = {
//            IconButton(onClick = {  }) {
//                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
//            }
//        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            scrolledContainerColor = MaterialTheme.colorScheme.background
        ),
        navigationIcon = navigationIcon
    )
}

//@Composable
//fun FullImageViewTopBar(
//    modifier: Modifier = Modifier,
//    image: UnsplashImage?,
//    onBackClick: () -> Unit,
//    onPhotographerNameClick: (String) -> Unit,
//    onDownloadImgClick: () -> Unit,
//    isVisible: Boolean,
//) {
//    AnimatedVisibility(
//        visible = isVisible,
//        enter = fadeIn() + slideInVertically(),
//        exit = fadeOut() + slideOutVertically()
//    ) {
//        Row(
//            modifier = modifier,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            IconButton(
//                onClick = { onBackClick() }
//            ) {
//                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go Back")
//            }
//            AsyncImage(
//                modifier = Modifier.size(30.dp).clip(CircleShape),
//                model = image?.photographerProfileImgUrl,
//                contentDescription = null
//            )
//            Spacer(modifier = Modifier.width(10.dp))
//            Column(
//                modifier = Modifier.clickable {
//                    image?.let {
//                        onPhotographerNameClick(it.photographerProfileLink)
//                    }
//                }
//            ) {
//                Text(
//                    text = image?.photographerName ?: "",
//                    style = MaterialTheme.typography.bodySmall
//                )
//            }
//            Spacer(modifier = Modifier.weight(1f))
//            IconButton(
//                onClick = { onDownloadImgClick() }
//            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_download),
//                    contentDescription = "Download the image",
//                    tint = MaterialTheme.colorScheme.onBackground
//                )
//            }
//        }
//    }
//}