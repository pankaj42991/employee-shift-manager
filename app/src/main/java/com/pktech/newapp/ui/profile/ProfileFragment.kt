package com.pktech.newapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.pktech.newapp.NewAppApplication
import com.pktech.newapp.R
import com.pktech.newapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(
            (requireActivity().application as NewAppApplication)
                .employeeRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        observeData()

        return binding.root
    }

    private fun observeData() {
        viewModel.profileData.observe(viewLifecycleOwner) { profile ->
            binding.tvName.text = profile.name
            binding.tvEmail.text = profile.email
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
