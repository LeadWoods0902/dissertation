package com.leadwoods.dissertation.fragments.lobby_controls

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.leadwoods.dissertation.databinding.FragmentLobbyControlsBinding

/**
 * A simple [Fragment] subclass.
 * Use the [LobbyControlsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LobbyControlsFragment : Fragment() {

    private var _binding: FragmentLobbyControlsBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLobbyControlsBinding.inflate(inflater, container, false)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.requireContext())

        return binding.root
    }
}