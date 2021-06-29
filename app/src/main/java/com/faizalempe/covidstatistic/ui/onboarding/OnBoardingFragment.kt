package com.faizalempe.covidstatistic.ui.onboarding

import android.content.Intent
import android.content.res.TypedArray
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.faizalempe.covidstatistic.R
import com.faizalempe.covidstatistic.databinding.FragmentOnboardingBinding
import com.faizalempe.covidstatistic.util.REQUEST_GOOGLE_SIGN_IN
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import java.lang.Exception

class OnBoardingFragment: Fragment() {

    private lateinit var binding: FragmentOnboardingBinding

    private lateinit var imgArray: TypedArray
    private lateinit var titleArray: Array<String>
    private lateinit var textArray: Array<String>
    private lateinit var client: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    private val adapter: SliderAdapter by lazy {
        imgArray = resources.obtainTypedArray(R.array.onboarding_drawable)
        titleArray = resources.getStringArray(R.array.onboarding_title)
        textArray = resources.getStringArray(R.array.onboarding_text)
        SliderAdapter(requireContext(), imgArray, titleArray, textArray)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboardingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this

        initGoogle()
        initView()
    }

    private fun initGoogle() {
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        client = GoogleSignIn.getClient(requireContext(), gso)
    }

    private fun initView() {
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
        binding.apply {
            viewSlider.apply {
                setSliderAdapter(adapter)
                setIndicatorAnimation(IndicatorAnimationType.WORM)
                setCurrentPageListener { position ->
                    when(position) {
                        titleArray.lastIndex -> {
                            onboardingBtnNext.visibility = View.INVISIBLE
                            onboardingBtnGoogle.visibility = View.VISIBLE
                        }
                        else -> {
                            onboardingBtnGoogle.visibility = View.INVISIBLE
                            onboardingBtnNext.visibility = View.VISIBLE
                        }
                    }
                }

                onboardingBtnNext.setOnClickListener {
                    viewSlider.slideToNextPosition()
                }

                onboardingBtnGoogle.setOnClickListener {
                    val signInIntent = client.signInIntent
                    startActivityForResult(signInIntent, REQUEST_GOOGLE_SIGN_IN)
                }
            }
        }

    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                val user = auth.currentUser
                if (user != null) {
                    val action = OnBoardingFragmentDirections.actionOnBoardingFragmentToHomeFragment()
                    findNavController().navigate(action)
                }
            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(requireContext(), "You should sign in with a valid account", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val action = OnBoardingFragmentDirections.actionOnBoardingFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (!account?.idToken.isNullOrEmpty()) {
                    firebaseAuthWithGoogle(account?.idToken!!)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("ERROR", e.message.toString())
            }
        }
    }
}