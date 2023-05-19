package com.kkstream.videopage.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kkstream.videopage.adapter.PagerAdapter
import com.kkstream.videopage.data.DrmInfo
import com.kkstream.videopage.data.MediaConfig
import com.kkstream.videopage.databinding.FragmentPagerMainBinding

class PagerMainFragment : Fragment() {

    private var _binding: FragmentPagerMainBinding? = null
    private val binding get() = _binding!!

    private var adapter: PagerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPagerMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contentList = mutableListOf<MediaConfig>()
        for (i in 0 until 20) {
            contentList.add(MEDIA)
        }

        adapter = PagerAdapter(this, contentList)

        binding.vpSlideRoot.let {
            it.adapter = adapter
            it.offscreenPageLimit = 1
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    companion object {
        private val MEDIA = MediaConfig(
            url = "https://storage.googleapis.com/wvmedia/cenc/h264/tears/tears.mpd",
            drmInfo = DrmInfo(
                drmServer = "https://proxy.uat.widevine.com/proxy?video_id=d286538032258a1c&provider=widevine_test"
            )
        )
    }
}