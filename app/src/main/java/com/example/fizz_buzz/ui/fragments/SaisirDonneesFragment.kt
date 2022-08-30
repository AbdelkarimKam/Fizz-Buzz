package com.example.fizz_buzz.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.fizz_buzz.R
import com.example.fizz_buzz.databinding.SaisirDonnesLayoutBinding
import com.example.fizz_buzz.model.DonneesSaisie
import com.example.fizz_buzz.ui.activity.ResultatViewModel
import com.example.fizz_buzz.ui.activity.UserIntent
import com.example.jetpacktrainning.ui.fragments.BaseFragment


class SaisirDonneesFragment :
    BaseFragment<SaisirDonnesLayoutBinding>(SaisirDonnesLayoutBinding::inflate) {

    private val donneesSaisieViewModel: ResultatViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.afficheResultats.setOnClickListener {
            donneesSaisieViewModel.accept(                UserIntent.AfficheResultats(
                DonneesSaisie(
                    binding.entier1.editText?.text?.toString()?.toInt() ?: 3,
                    binding.entier2.editText?.text?.toString()?.toInt() ?: 5,
                    binding.mot1.editText?.text.toString(),
                    binding.mot2.editText?.text.toString()
                )
            ))
            displayFragmentCountry()
        }
    }

    private fun displayFragmentCountry() =
        findNavController().navigate(R.id.action_saisirDonneesFragment_to_resultatsFragment)

}
