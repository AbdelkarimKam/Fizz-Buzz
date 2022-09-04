package com.example.fizz_buzz.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.fizz_buzz.R
import com.example.fizz_buzz.databinding.SaisirDonnesLayoutBinding
import com.example.fizz_buzz.ui.viewmodel.ResultatViewModel
import com.example.fizz_buzz.ui.viewmodel.UserIntent
import com.example.jetpacktrainning.ui.fragments.BaseFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class SaisirDonneesFragment :
    BaseFragment<SaisirDonnesLayoutBinding>(SaisirDonnesLayoutBinding::inflate) {

    private val donneesSaisieViewModel: ResultatViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        donneesSaisieViewModel.entier1ErrorText.observe(viewLifecycleOwner) {
            binding.entier1.helperText = it
        }
        donneesSaisieViewModel.entier2ErrorText.observe(viewLifecycleOwner) {
            binding.entier2.helperText = it
        }
        donneesSaisieViewModel.mot1ErrorText.observe(viewLifecycleOwner) {
            binding.mot1.helperText = it
        }
        donneesSaisieViewModel.mot2ErrorText.observe(viewLifecycleOwner) {
            binding.mot2.helperText = it
        }
        donneesSaisieViewModel.entier1IsError.observe(viewLifecycleOwner) {
            binding.entier1.isHelperTextEnabled = it
        }
        donneesSaisieViewModel.entier2IsError.observe(viewLifecycleOwner) {
            binding.entier2.isHelperTextEnabled = it
        }
        donneesSaisieViewModel.mot1IsError.observe(viewLifecycleOwner) {
            binding.mot1.isHelperTextEnabled = it
        }
        donneesSaisieViewModel.mot2IsError.observe(viewLifecycleOwner) {
            binding.mot2.isHelperTextEnabled = it
        }
        lifecycleScope.launch {
            donneesSaisieViewModel.inputDataFlow.collectLatest {
                if (it) displayFragmentCountry()
            }
        }

        binding.afficheResultats.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                donneesSaisieViewModel.actionStateFlow.emit(
                    UserIntent.DonneesInput(
                        binding.entier1.editText?.text?.toString(),
                        binding.entier2.editText?.text?.toString(),
                        binding.mot1.editText?.text.toString(),
                        binding.mot2.editText?.text.toString()
                    )
                )
            }
        }
    }

    private fun displayFragmentCountry() =
        findNavController().navigate(R.id.action_saisirDonneesFragment_to_resultatsFragment)

}
