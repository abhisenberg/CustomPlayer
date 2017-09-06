package com.example.abheisenberg.speedutube;

import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Surface;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

public class MainActivity extends AppCompatActivity implements  VideoRendererEventListener{

    public static final String TAG = "MA";
    private  SimpleExoPlayer player;
    public static final String rawUrl = "https://www.youtube.com/watch?v=4Aa9GwWaRv0";

    /*
        LifeCycle methods
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Create a default track selector
        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        //Create a load control
        LoadControl loadControl = new DefaultLoadControl();

        //Creating the player
        player =
                ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        SimpleExoPlayerView playerView = new SimpleExoPlayerView(this);
        playerView = (SimpleExoPlayerView) findViewById(R.id.exoPl_ma);

        //Set media conroller
        playerView.setUseController(true);
        playerView.requestFocus();

        //Build the player to the view
        playerView.setPlayer(player);

        YTLinkExtractor ytLinkExtractor = new YTLinkExtractor(rawUrl, this);
        String finalurl = ytLinkExtractor.getYoutubeDownloadUrl();
        Log.d(TAG, "finalurl "+finalurl);
        Uri vidURL = Uri.parse(finalurl);

        DefaultBandwidthMeter bandwidthMeterA = new DefaultBandwidthMeter();
        DefaultDataSourceFactory dataSourceFactory =
                new DefaultDataSourceFactory(
                        this,
                        Util.getUserAgent(this, "speedutube"),
                        bandwidthMeterA
                );

        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        //For video streaming
        MediaSource videoSource = new HlsMediaSource(vidURL, dataSourceFactory, 1, null, null);
        final LoopingMediaSource loopingMediaSource = new LoopingMediaSource(videoSource);

        player.prepare(loopingMediaSource);

        player.addListener(new ExoPlayer.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {
                Log.d(TAG, "onTimelineChanged: ");
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                Log.d(TAG, "onTracksChanged: ");
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                Log.d(TAG, "onLoadingChanged: ");
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Log.d(TAG, "onPlayerStateChanged: ");
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
                Log.d(TAG, "onRepeatModeChanged: ");
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.d(TAG, "onPlayerError: ");
                player.stop();
//                player.prepare(loopingMediaSource);
//                player.setPlayWhenReady(true);
            }

            @Override
            public void onPositionDiscontinuity() {
                Log.d(TAG, "onPositionDiscontinuity: ");
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                Log.d(TAG, "onPlaybackParametersChanged: ");
            }
        });

        player.setPlayWhenReady(true);
        player.setVideoDebugListener(this);

    }

    @Override
    public void onVideoEnabled(DecoderCounters counters) {

    }

    @Override
    public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {

    }

    @Override
    public void onVideoInputFormatChanged(Format format) {

    }

    @Override
    public void onDroppedFrames(int count, long elapsedMs) {

    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        Log.d(TAG, "onVideoSizeChanged: ");
    }

    @Override
    public void onRenderedFirstFrame(Surface surface) {

    }

    @Override
    public void onVideoDisabled(DecoderCounters counters) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }

}
