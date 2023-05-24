package com.kkstream.videopage.page

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager
import com.google.android.exoplayer2.drm.FrameworkMediaDrm
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.video.VideoListener
import com.kkstream.videopage.R
import com.kkstream.videopage.adapter.PagerAdapter.Companion.KEY_MEDIA_CONFIG
import com.kkstream.videopage.data.MediaConfig
import com.kkstream.videopage.databinding.FragmentSubBinding

class PagerSubFragment : Fragment() {

    private var _binding: FragmentSubBinding? = null
    private val binding get() = _binding!!

    private var player: SimpleExoPlayer? = null

    private var playerView: PlayerView? = null
    private var trackSelector: DefaultTrackSelector? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")

        initAndPrepare()
    }

    private fun initAndPrepare() {
        fun initMediaSource(mediaConfig: MediaConfig?): MediaSource {
            val licenseDataSourceFactory = DefaultHttpDataSource.Factory().setUserAgent("VideoPageSample").setTransferListener(null)
            val drmCallback = mediaConfig?.drmInfo?.let {
                HttpMediaDrmCallback(it.drmServer, licenseDataSourceFactory).apply {
                    it.headers?.map { header ->
                        setKeyRequestProperty(header.key, header.value)
                    }
                }
            } ?: HttpMediaDrmCallback("", licenseDataSourceFactory)

            val mediaDrmProvider = FrameworkMediaDrm.DEFAULT_PROVIDER
            val drmSessionManager = DefaultDrmSessionManager.Builder()
                .setUuidAndExoMediaDrmProvider(C.WIDEVINE_UUID, mediaDrmProvider)
                .setMultiSession(true)
                .build(drmCallback)

            val mediaSourceFactory = DashMediaSource.Factory(
                licenseDataSourceFactory
            ).setDrmSessionManagerProvider { drmSessionManager }

            val mediaItem = MediaItem.fromUri(Uri.parse(mediaConfig?.url))

            return mediaSourceFactory.createMediaSource(mediaItem)
        }

//        trackSelector = DefaultTrackSelector(requireContext())
        trackSelector?.buildUponParameters()?.setMaxVideoSize(Int.MAX_VALUE, 720)?.let {
            trackSelector?.setParameters(it)
        }
        player = SimpleExoPlayer.Builder(requireContext())
            .setTrackSelector(trackSelector ?: DefaultTrackSelector(requireContext()))
            .build()
//        binding.playerView.player = player

        player?.addVideoListener(object : VideoListener {
            override fun onRenderedFirstFrame() {
                super.onRenderedFirstFrame()

                showCoverImage(false)
                player?.volume = 1f
            }
        })

        val mediaConfig = arguments?.getParcelable(KEY_MEDIA_CONFIG) as? MediaConfig
        val mediaSource = initMediaSource(mediaConfig)
        player?.setMediaSource(mediaSource)
        player?.prepare()
    }

    override fun onStart() {
        super.onStart()

        showCoverImage(true)
    }

    private fun showCoverImage(showed: Boolean) {
        if (showed) {
            Glide.with(requireContext())
                .load(R.drawable.pikachu)
                .into(binding.coverImage)
            binding.coverImage.visibility = View.VISIBLE
        } else {
            Glide.with(requireContext()).clear(binding.coverImage)
            binding.coverImage.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()

        player?.volume = 0f
        playerView = PlayerView(requireContext())
        binding.root.addView(playerView, 0)
        playerView?.player = player

        player?.playWhenReady = true
    }

    override fun onPause() {
        super.onPause()
        player?.playWhenReady = false

        playerView?.player = null
        binding.root.removeView(playerView)

        showCoverImage(true)
    }

    override fun onStop() {
        super.onStop()
        player?.release()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private val TAG = PagerSubFragment::class.java.simpleName
    }
}