package com.kkstream.videopage.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kkstream.videopage.data.MediaConfig
import com.kkstream.videopage.page.PagerMainFragment
import com.kkstream.videopage.page.PagerSubFragment

class PagerAdapter(
    fragment: PagerMainFragment,
    private val list: List<MediaConfig>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        val size = list.size
        return PagerSubFragment().apply {
            val bundle = Bundle().apply {
                putParcelable(KEY_MEDIA_CONFIG, list[position.mod(size)])
                putInt(KEY_POSITION, position)
            }
            arguments = bundle
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

//        recyclerView.setItemViewCacheSize(-1)
    }

    companion object {
        const val KEY_MEDIA_CONFIG = "KEY_MEDIA_CONFIG"
        const val KEY_POSITION = "KEY_POSITION"
    }
}