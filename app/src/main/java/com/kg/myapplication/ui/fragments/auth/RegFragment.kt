package com.kg.myapplication.ui.fragments.auth

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kg.myapplication.R
import com.kg.myapplication.databinding.FragmentRegBinding


class RegFragment : Fragment(R.layout.fragment_reg) {

    private var _binding: FragmentRegBinding? = null
    private val binding: FragmentRegBinding
        get() = _binding!!


    private lateinit var mAuth: FirebaseAuth
    private lateinit var mVerificationId: String
    private lateinit var mPhoneNumber: String

    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser != null) {
            findNavController().navigate(R.id.action_regFragment_to_mainFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = Firebase.auth
        mPhoneNumber = arguments?.getString("phone").toString()
        mVerificationId = arguments?.getString("verId").toString()

        binding.btnSendCode.setOnClickListener {
            if (mVerificationId != null) {
                val smsCode = "${binding.etCode.text}"
                val credential = PhoneAuthProvider.getCredential(mVerificationId, smsCode)

                Firebase.auth.signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            findNavController().navigate(R.id.action_regFragment_to_mainFragment)
                        } else {
                            Toast.makeText(requireContext(), "Код не верный", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            }
        }


    }


}