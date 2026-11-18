
package com.izooto.notifyhub.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.compose.AsyncImagePainter

@Composable
fun NotificationBannerImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    val validUrl = imageUrl
        ?.trim()
        ?.takeIf {
            it.startsWith("https://", ignoreCase = true) ||
                    it.startsWith("http://", ignoreCase = true)
        }

    if (validUrl.isNullOrBlank()) {
        ImageFallback(modifier)
    } else {
        val painter = rememberAsyncImagePainter(
            model = validUrl
        )

        Box(modifier = modifier) {

            Image(
                painter = painter,
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            when (painter.state) {

                is AsyncImagePainter.State.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is AsyncImagePainter.State.Error -> {
                    ImageFallback(
                        modifier = Modifier.fillMaxSize()
                    )
                }

                else -> Unit
            }
        }
    }
}

@Composable
private fun ImageFallback(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(
            MaterialTheme.colorScheme.secondaryContainer
        ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "NH",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}
