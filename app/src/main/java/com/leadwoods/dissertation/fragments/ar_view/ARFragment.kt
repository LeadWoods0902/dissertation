package com.leadwoods.dissertation.fragments.ar_view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.leadwoods.dissertation.databinding.FragmentArBinding

class ARFragment : Fragment() {

    private var _binding: FragmentArBinding? = null
    private val binding get() = _binding!!




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentArBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}