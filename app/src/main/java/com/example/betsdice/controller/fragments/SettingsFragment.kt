package com.example.betsdice.controller.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import coil.load
import coil.size.Scale
import com.example.betsdice.databinding.FragmentSettingsBinding
import com.example.betsdice.model.Repository
import com.example.betsdice.model.constant.url_image_money
import com.example.betsdice.model.constant.url_image_settings_and_game

class SettingsFragment : Fragment() {

    private var binding: FragmentSettingsBinding? = null
    private var repository = Repository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater,container,false)
        return binding?.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //показ всплывающего сообщения о возможности пополнения счета
        if(repository.checkTodayAndLastDay()){
            repository.longToast(requireContext(),"click on the button to top up your money account")
        }else{
            repository.longToast(requireContext(), "you will be able to top up your account the next day")
        }

        //показ рекорда
        binding?.idSettingsTvRecord?.text = "-your record: ${repository.getRecordInGame()} lvl-"

        //показ количества денег
        binding?.idSettingsScMoneyTvMoney?.text = "money:${repository.getMoneyInCashAccount()}"

        //загрузка фоновой картинки
        binding?.idSettingsImg?.load(url_image_settings_and_game){
            scale(Scale.FILL)
        }

        //загрузка картинки монеты
        binding?.idSettingsScMoneyIvMoney?.load(url_image_money){
            scale(Scale.FIT)
        }

        //кнопка пополнения счета
        binding?.idSettingsCsMoneyButton?.setOnClickListener {
            if(repository.checkTodayAndLastDay()){
                val money = repository.getRandomMoneyForReplenish()
                repository.addMoneyInCashAccount(money)
                repository.shortToast(requireContext(),"$money coins")
                repository.setLastDay(repository.getCurrentDate())
                binding?.idSettingsScMoneyTvMoney?.text = "money:${repository.getMoneyInCashAccount()}"
            }else{
                repository.shortToast(requireContext(), "you will be able to top up your account the next day")
            }
        }

        //обработка выхода из игры путем нажатия на кнопку НАЗАД
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            repository.exitingTheApplication()
        }

    }

    //очистка биндинга при очистке вью
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}