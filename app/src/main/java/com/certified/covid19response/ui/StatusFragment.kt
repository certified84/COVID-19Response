package com.certified.covid19response.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.certified.covid19response.data.model.Result
import com.certified.covid19response.data.model.User
import com.certified.covid19response.databinding.DialogCovidStatusBinding
import com.certified.covid19response.databinding.FragmentStatusBinding
import com.certified.covid19response.util.Extensions.openBrowser
import com.certified.covid19response.util.Extensions.showToast
import com.certified.covid19response.util.PreferenceKeys
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StatusFragment : Fragment() {

    private var _binding: FragmentStatusBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    private var noOfSevereSymptoms: Int = 0
    private var noOfLessSymptoms: Int = 0
    private var noOfMostSymptoms: Int = 0
    private var totalNoOfSymptoms: Float = 0F

    private var severeSymptoms: String = ""
    private var lessSymptoms: String = ""
    private var mostSymptoms: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentStatusBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnSymptomsLink.setOnClickListener {
                requireContext().openBrowser(
                    "https://www.who.int/health-topics/coronavirus#tab=tab_3", findNavController(),
                    StatusFragmentDirections.actionStatusFragmentToWebFragment("https://www.who.int/health-topics/coronavirus#tab=tab_3")
                )
            }

            btnCheckStatus.setOnClickListener {
                performCheck()
            }

            cbConfusion.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    noOfSevereSymptoms++
                    totalNoOfSymptoms++
                    severeSymptoms += cbConfusion.text.toString()
                } else {
                    noOfSevereSymptoms--
                    totalNoOfSymptoms--
                }
            }

            cbDiarrhea.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    noOfLessSymptoms++
                    totalNoOfSymptoms++
                } else {
                    noOfLessSymptoms--
                    totalNoOfSymptoms--
                }
            }

            cbSoreThroat.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    noOfLessSymptoms++
                    totalNoOfSymptoms++
                } else {
                    noOfLessSymptoms--
                    totalNoOfSymptoms--
                }
            }

            cbFever.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    noOfMostSymptoms++
                    totalNoOfSymptoms++
                } else {
                    noOfMostSymptoms--
                    totalNoOfSymptoms--
                }
            }

            cbFatigue.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    noOfMostSymptoms++
                    totalNoOfSymptoms++
                } else {
                    noOfMostSymptoms--
                    totalNoOfSymptoms--
                }
            }

            cbDryCough.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    noOfMostSymptoms++
                    totalNoOfSymptoms++
                } else {
                    noOfMostSymptoms--
                    totalNoOfSymptoms--
                }
            }

            cbShortnessBreath.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    noOfSevereSymptoms++
                    totalNoOfSymptoms++
                } else {
                    noOfSevereSymptoms--
                    totalNoOfSymptoms--
                }
            }

            cbChillsDizziness.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    noOfSevereSymptoms++
                    totalNoOfSymptoms++
                } else {
                    noOfSevereSymptoms--
                    totalNoOfSymptoms--
                }
            }

            cbLossAppetite.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    noOfMostSymptoms++
                    totalNoOfSymptoms++
                } else {
                    noOfMostSymptoms--
                    totalNoOfSymptoms--
                }
            }

            cbConjunctivitis.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    noOfLessSymptoms++
                    totalNoOfSymptoms++
                } else {
                    noOfLessSymptoms--
                    totalNoOfSymptoms--
                }
            }

            cbNasalCongestion.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    noOfLessSymptoms++
                    totalNoOfSymptoms++
                } else {
                    noOfLessSymptoms--
                    totalNoOfSymptoms--
                }
            }

            cbHeadache.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    noOfLessSymptoms++
                    totalNoOfSymptoms++
                } else {
                    noOfLessSymptoms--
                    totalNoOfSymptoms--
                }
            }

            cbPersistentPain.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    noOfSevereSymptoms++
                    totalNoOfSymptoms++
                } else {
                    noOfSevereSymptoms--
                    totalNoOfSymptoms--
                }
            }

            cbHighTemp.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    noOfLessSymptoms++
                    totalNoOfSymptoms++
                } else {
                    noOfLessSymptoms--
                    totalNoOfSymptoms--
                }
            }
        }
    }

    private fun performCheck() {

        binding.apply {
            val userFeeling = etUserFeeling.text.toString().trim()

            if (totalNoOfSymptoms == 0F) {
                showToast("Please select at least one symptom")
                return
            }

            if (userFeeling.isBlank()) {
                etUserFeeling.error = "Required *"
                etUserFeeling.requestFocus()
                return
            }

            val preferenceManager = PreferenceManager.getDefaultSharedPreferences(requireContext())
            preferenceManager.apply {
                val user = User(
                    id = auth.currentUser!!.uid,
                    name = auth.currentUser!!.displayName!!,
                    email = auth.currentUser!!.email!!,
                    profile_image = getString(
                        PreferenceKeys.USER_PROFILE_IMAGE_KEY,
                        ""
                    ),
                    location = getString(PreferenceKeys.USER_LOCATION_KEY, "")!!,
                    nin = getString(PreferenceKeys.USER_NIN_KEY, "")!!,
                    bio = getString(PreferenceKeys.USER_BIO_KEY, "")!!,
                    sex = getString(PreferenceKeys.USER_SEX_KEY, "")!!,
                    position = getString(PreferenceKeys.USER_POSITION_KEY, "")!!
                )
                user.account_type = getString(PreferenceKeys.ACCOUNT_TYPE, "")!!

                val result = Result(
                    noOfSevereSymptoms = noOfSevereSymptoms,
                    noOfLessSymptoms = noOfLessSymptoms,
                    noOfMostSymptoms = noOfMostSymptoms,
                    totalNoOfSymptoms = totalNoOfSymptoms,
                    severeSymptoms = severeSymptoms,
                    lessSymptoms = lessSymptoms,
                    mostSymptoms = mostSymptoms
                )
                result.feeling = userFeeling
                launchBottomSheetDialog(result, user)
            }
        }
    }

    private fun launchBottomSheetDialog(result: Result, user: User) {
        val view = DialogCovidStatusBinding.inflate(layoutInflater)
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(view.root)
        view.apply {

            btnCancel.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            btnContinue.setOnClickListener {
                bottomSheetDialog.dismiss()
                findNavController().navigate(
                    StatusFragmentDirections.actionStatusFragmentToResultFragment(user, result)
                )
            }

            lifecycleScope.launch {

                var currentProgress = 0
                repeat(100) {
                    delay(100L)
                    currentProgress += 1
                    linearProgressIndicator.progress = currentProgress
                }

                btnContinue.isEnabled = true
            }
        }
        bottomSheetDialog.show()
    }

    override fun onResume() {
        super.onResume()
        val preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        if (preferences.getString(PreferenceKeys.ACCOUNT_TYPE, "") == "doctor")
            binding.apply {
                tvFeatureNotAvailable.visibility = View.VISIBLE
                scrollView.visibility = View.GONE
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}