package com.example.nextcartapp.presentation.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.nextcartapp.R
import com.example.nextcartapp.core.session.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(500)

            val isLoggedIn = sessionManager.isTokenValid()

            if (isLoggedIn) {
                // Prova a verificare che il token sia ancora valido facendo una chiamata API
                // Se fallisce con 401, l'AuthInterceptor cancellerà la sessione
                findNavController().navigate(R.id.action_splash_to_home)
            } else {
                findNavController().navigate(R.id.action_splash_to_login)
            }
        }
    }
}