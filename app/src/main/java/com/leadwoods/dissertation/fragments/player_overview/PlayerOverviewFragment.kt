package com.leadwoods.dissertation.fragments.player_overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.leadwoods.dissertation.databinding.FragmentPlayerOverviewBinding

/**
 * A simple [Fragment] subclass.
 * Use the [PlayerOverviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlayerOverviewFragment : Fragment() {

    private var _binding: FragmentPlayerOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val playerOverviewViewModel =
            ViewModelProvider(this)[PlayerOverviewViewModel::class.java]

        _binding = FragmentPlayerOverviewBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textView
        playerOverviewViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}