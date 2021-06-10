package com.example.angkoot.ui.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.angkoot.databinding.FragmentProfileBinding
import com.example.angkoot.ui.home.HomeActivity

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var _activity: HomeActivity? = null
    private var _view: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        _view = _binding?.root
        return _view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            val username = _activity?.currentUser?.username ?: "Unknown user!"
            val greetingText = "Hello, $username!"
            tvUsername.text = greetingText

            Log.d("Hehe", username)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _activity = context as HomeActivity
    }

    override fun onDetach() {
        super.onDetach()
        _activity = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _view = null
    }
}