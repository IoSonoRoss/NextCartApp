package com.example.nextcartapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
//import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Log

class OldMainActivity : AppCompatActivity() {

    /*

    private var authToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val baseUrl = getString(R.string.URL_FISSO)
        val finalUrl = if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"

        val btnScan = findViewById<Button>(R.id.btnScan)
        val txtResult = findViewById<TextView>(R.id.txtResult)

        // Configurazione Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(finalUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        // AUTO-LOGIN ALL'AVVIO
        lifecycleScope.launch {
            try {
                Log.d("DEBUG_APP", "Tentativo di login...")

                val response = apiService.login(LoginRequest("test@example.com", "password123"))

                // Usa accessToken dalla risposta
                authToken = response.accessToken

                Log.d("DEBUG_APP", "Token ricevuto: $authToken")

                txtResult.text = "Connesso al database!"
                Toast.makeText(this@MainActivity, "Login riuscito!", Toast.LENGTH_SHORT).show()

            } catch (e: retrofit2.HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("DEBUG_APP", "Errore HTTP ${e.code()}: $errorBody")
                txtResult.text = "Errore Login (${e.code()}): Verifica credenziali"
                Toast.makeText(this@MainActivity, "Login fallito!", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                Log.e("DEBUG_APP", "Errore: ${e.message}", e)
                txtResult.text = "Errore connessione: ${e.message}"
                Toast.makeText(this@MainActivity, "Errore di rete!", Toast.LENGTH_SHORT).show()
            }
        }

        // SCANNER QR
        val scanner = GmsBarcodeScanning.getClient(this)

        btnScan.setOnClickListener {
            if (authToken.isNullOrBlank()) {
                Toast.makeText(this, "Attendere login...", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            scanner.startScan()
                .addOnSuccessListener { barcode ->
                    val scannedId = barcode.rawValue ?: return@addOnSuccessListener

                    Log.d("DEBUG_APP", "QR scansionato: $scannedId")

                    lifecycleScope.launch {
                        try {
                            val bearerToken = "Bearer $authToken"

                            Log.d("DEBUG_APP", "Richiesta prodotto con token: $bearerToken")

                            val prodotto = apiService.getProduct(scannedId, bearerToken)

                            txtResult.text = "PRODOTTO TROVATO\n\nNome: ${prodotto.name}\nID: ${prodotto.id}"
                            Log.d("DEBUG_APP", "Prodotto: ${prodotto.name}")

                        } catch (e: retrofit2.HttpException) {
                            val errorBody = e.response()?.errorBody()?.string()
                            txtResult.text = "Errore ${e.code()}\n$errorBody"
                            Log.e("DEBUG_APP", "HTTP ${e.code()}: $errorBody")

                            if (e.code() == 401) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Token scaduto o non valido!",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        } catch (e: Exception) {
                            txtResult.text = "Errore: ${e.message}"
                            Log.e("DEBUG_APP", "${e.message}", e)
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Errore scanner: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("DEBUG_APP", "Scanner: ${e.message}")
                }
        }
    }


     */
}