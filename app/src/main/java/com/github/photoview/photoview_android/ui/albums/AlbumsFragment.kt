package com.github.photoview.photoview_android.ui.albums

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo3.exception.ApolloException
import com.github.photoview.photoview_android.MyAlbumsQuery
import com.github.photoview.photoview_android.apolloClient
import com.github.photoview.photoview_android.databinding.FragmentAlbumsBinding

class AlbumsFragment : Fragment() {

    private var _binding: FragmentAlbumsBinding? = null

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: AlbumsRecyclerAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val albumViewModel =
                ViewModelProvider(this).get(AlbumsViewModel::class.java)

        _binding = FragmentAlbumsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        linearLayoutManager = LinearLayoutManager(context)
        binding.albumsRecyclerView.layoutManager = linearLayoutManager

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenResumed {
            val response = try {
                apolloClient(view.context)!!.query(MyAlbumsQuery()).execute()
            } catch (e: ApolloException) {
                Log.d("LaunchList", "Failure", e)
                null
            }

            val albums = response?.data?.myAlbums
            if (albums != null && !response.hasErrors()) {
                val adapter = AlbumsRecyclerAdapter(albums)
                binding.albumsRecyclerView.adapter = adapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}