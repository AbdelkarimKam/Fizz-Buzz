package com.example.fizz_buzz.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fizz_buzz.databinding.ResultatsLayoutBinding
import com.example.fizz_buzz.ui.activity.ResultatViewModel
import com.example.fizz_buzz.ui.adapter.ResultatAdapter
import com.example.jetpacktrainning.ui.fragments.BaseFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class ResultatsFragment : BaseFragment<ResultatsLayoutBinding>(ResultatsLayoutBinding::inflate) {

    private val resultatViewModel: ResultatViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val items = resultatViewModel.pagingDataFlow
        val resultatAdapter = ResultatAdapter()

        binding.bindAdapter(resultatAdapter = resultatAdapter)

        // Collect from the PagingData Flow in the ViewModel, and submit it to the
        // PagingDataAdapter.
        viewLifecycleOwner.lifecycleScope.launch {
            // We repeat on the STARTED lifecycle because an Activity may be PAUSED
            // but still visible on the screen, for example in a multi window app
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                items.collectLatest {
                    resultatAdapter.submitData(it)
                }
            }
        }

        // Use the CombinedLoadStates provided by the loadStateFlow on the ResultatAdapter to
        // show progress bars when more data is being fetched
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                resultatAdapter.loadStateFlow.collect {
                    binding.prependProgress.isVisible = it.source.prepend is LoadState.Loading
                    binding.appendProgress.isVisible = it.source.append is LoadState.Loading
                }
            }
        }
    }
}
/**
 * Sets up the [RecyclerView] and binds [ResultatAdapter] to it
 */
private fun ResultatsLayoutBinding.bindAdapter(resultatAdapter: ResultatAdapter) {
    list.adapter = resultatAdapter
    list.layoutManager = LinearLayoutManager(list.context)
    val decoration = DividerItemDecoration(list.context, DividerItemDecoration.VERTICAL)
    list.addItemDecoration(decoration)
}

