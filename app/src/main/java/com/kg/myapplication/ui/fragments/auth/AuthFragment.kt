package com.kg.myapplication.ui.fragments.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kg.myapplication.R
import com.kg.myapplication.databinding.FragmentAuthBinding
import com.kg.myapplication.databinding.FragmentMainBinding
import com.santalu.maskara.Mask
import com.santalu.maskara.MaskChangedListener
import com.santalu.maskara.MaskStyle
import java.util.concurrent.TimeUnit

class AuthFragment : Fragment(R.layout.fragment_auth) {

    private var _binding: FragmentAuthBinding? = null
    private val binding: FragmentAuthBinding
        get() = _binding!!

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mResendingToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var mVerificationId: String

    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser != null) {
            findNavController().navigate(R.id.action_authFragment_to_regFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = Firebase.auth

        getPN()

    }


    private fun getPN() {
        binding.input.doAfterTextChanged {
            if (binding.input.isDone) {
                Log.e("PHONE NUMBER", binding.input.masked)

                otpSend(binding.input.masked)
            }
        }
    }


    private fun otpSend(phoneNumberWithCountryCode: String) {
        isVisible(true)

        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d("TAG", "onVerificationCompleted: ${credential.smsCode}")
            }

            override fun onVerificationFailed(e: FirebaseException) {
                isVisible(false)
                Log.e("ololo", e.localizedMessage.toString())
                Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
//                super.onCodeSent(verificationId, token)
                isVisible(false)
                Toast.makeText(requireContext(), "Код отправлен!", Toast.LENGTH_SHORT).show()
                mResendingToken = token
                mVerificationId = verificationId


                val bundle = bundleOf(
                    "verId" to verificationId, "token" to token,
                    "phone" to "phone"
                )

                findNavController().navigate(R.id.action_authFragment_to_regFragment, bundle)
            }
        }


        PhoneAuthProvider.verifyPhoneNumber(
            PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("$phoneNumberWithCountryCode")
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(requireActivity())
                .setCallbacks(mCallbacks)
                .build()
        )
    }

    private fun isVisible(visible: Boolean) {
        binding.progressBar.isVisible = visible
        binding.btnSendCode.isVisible = !visible
    }


}