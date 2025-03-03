import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    videoUri: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Create and remember the Media3 ExoPlayer instance
    val exoPlayer = remember(videoUri) {
        ExoPlayer.Builder(context)
            .setHandleAudioBecomingNoisy(true)
            .build()
            .apply {
                try {
                    // Use Builder pattern for more control over MediaItem properties
                    val mediaItem = MediaItem.Builder()
                        .setUri(videoUri)
                        // Auto-detect MIME type, or explicitly set it if known
                        // .setMimeType(MimeTypes.VIDEO_MP4)
                        .build()

                    Log.d("VideoPlayer", "Loading video from URI: $videoUri")
                    setMediaItem(mediaItem)

                    // Set repeat mode
                    repeatMode = Player.REPEAT_MODE_ONE
                    // Prepare but don't auto-play
                    prepare()
                    playWhenReady = true  // Changed to true to autoplay
                    volume = 1f

                    // Add listener to debug playback issues
                    addListener(object : Player.Listener {
                        override fun onPlayerError(error: PlaybackException) {
                            Log.e("VideoPlayer", "Player error: ${error.message}")
                        }

                        override fun onPlaybackStateChanged(state: Int) {
                            when (state) {
                                Player.STATE_BUFFERING -> Log.d("VideoPlayer", "Buffering...")
                                Player.STATE_READY -> Log.d("VideoPlayer", "Ready to play")
                                Player.STATE_ENDED -> Log.d("VideoPlayer", "Playback ended")
                                Player.STATE_IDLE -> Log.d("VideoPlayer", "Player idle")
                            }
                        }

                        override fun onVideoSizeChanged(videoSize: androidx.media3.common.VideoSize) {
                            Log.d("VideoPlayer", "Video size changed: ${videoSize.width}x${videoSize.height}")
                        }
                    })
                } catch (e: Exception) {
                    // Handle potential errors with URI
                    Log.e("VideoPlayer", "Error setting media: ${e.message}", e)
                }
            }
    }

    // Handle lifecycle events
    DisposableEffect(lifecycleOwner, videoUri) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    if (exoPlayer.playbackState == Player.STATE_IDLE) {
                        exoPlayer.prepare()
                    }
                    exoPlayer.play()  // Resume playback when activity resumes
                }
                Lifecycle.Event.ON_PAUSE -> {
                    exoPlayer.pause()
                }
                Lifecycle.Event.ON_STOP -> {
                    exoPlayer.pause()
                }
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            exoPlayer.release()
        }
    }

    // Embed the Media3 PlayerView in Compose via AndroidView
    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
                useController = true
                setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS)

                // 1. Set proper resize mode
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT

                // 2. Keep surface active for faster rendering on content changes
                setKeepContentOnPlayerReset(true)

                // 3. Use surface view for better hardware acceleration
                setUseController(true)

                // 4. Make sure video rendering is enabled
                videoSurfaceView?.visibility = android.view.View.VISIBLE
            }
        },
        modifier = modifier,
        update = { playerView ->
            // Update player view when URI changes
            playerView.player = exoPlayer

            // Ensure surface is visible and properly configured
            playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
        }
    )
}