package com.pktech.newapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.pktech.newapp.NewAppApplication
import com.pktech.newapp.databinding.FragmentHomeBinding
import com.pktech.newapp.R

class HomeFragment : Fragment(R.layout.fragment_home) {

    // ✅ Memory-safe ViewBinding
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // ✅ ViewModel with singleton repository from Application class
    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(
            (requireActivity().application as NewAppApplication)
                .employeeRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // ✅ Firebase welcome message
        val user = FirebaseAuth.getInstance().currentUser
        binding.tvWelcome.text = "Welcome, ${user?.email ?: "Employee"}"

        // ✅ Observe LiveData from ViewModel
        viewModel.employeeCount.observe(viewLifecycleOwner) { count ->
            binding.tvEmployeeCount.text = "Employees: $count"
        }

        viewModel.shiftCount.observe(viewLifecycleOwner) { count ->
            binding.tvShiftCount.text = "Shifts: $count"
        }

        // ✅ Load dashboard data
        viewModel.loadDashboardData()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // ✅ Avoid memory leaks
        _binding = null
    }
}
