package com.example.androidtermprojectmotopedia.service

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView

@Composable
fun VideoPlayer(
    videoUri: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Create and remember ExoPlayer instance
    val exoPlayer = remember(context) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(Uri.parse(videoUri)))
            prepare()
            playWhenReady = true
        }
    }

    // Embed the PlayerView in Compose via AndroidView.
    AndroidView(
        factory = {
            PlayerView(context).apply {
                player = exoPlayer
                // Optionally, customize PlayerView (e.g., show playback controls)
            }
        },
        modifier = modifier
    )

    // Release ExoPlayer when no longer needed.
    DisposableEffect(
        key1 = exoPlayer
    ) {
        onDispose {
            exoPlayer.release()
        }
    }
}
