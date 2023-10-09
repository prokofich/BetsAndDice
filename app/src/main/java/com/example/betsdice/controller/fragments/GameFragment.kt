package com.example.betsdice.controller.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.isVisible
import coil.load
import coil.size.Scale
import com.example.betsdice.databinding.FragmentGameBinding
import com.example.betsdice.model.Repository
import com.example.betsdice.model.constant.url_image_money
import com.example.betsdice.model.constant.url_image_question
import com.example.betsdice.model.constant.url_image_settings_and_game
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameFragment : Fragment() {

    private var binding: FragmentGameBinding? = null

    private var repository = Repository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameBinding.inflate(inflater,container,false)
        return binding?.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository.job.cancel()

        //показ количества денег
        binding!!.idGameTvCountMoney.text = "${repository.getMoneyInCashAccount()}"

        //загрузка фоновой картинки
        binding!!.idGameImg.load(url_image_settings_and_game){
            scale(Scale.FILL)
        }

        //загрузка картинки монеты
        binding!!.idGameIvMoneta.load(url_image_money){
            scale(Scale.FIT)
        }

        //выбор левых игральных костей
        binding!!.idGameButtonLeftDice.setOnClickListener {
            if (!repository.job.isActive){
                repository.job = CoroutineScope(Dispatchers.Main).launch {
                    repository.getRandomUrlImageDice()
                    loadImageAllDice()
                    if(repository.checkLeftDiceWin()){
                        correctAnswer()
                    }else{
                        wronglyAnswer()
                    }
                    repository.job.cancel()
                }
            }
        }

        //выбор правых игральных костей
        binding!!.idGameButtonRight.setOnClickListener {
            if (!repository.job.isActive){
                repository.job = CoroutineScope(Dispatchers.Main).launch {
                    repository.getRandomUrlImageDice()
                    loadImageAllDice()
                    if(repository.checkRightDiceWin()){
                        correctAnswer()
                    }else{
                        wronglyAnswer()
                    }
                    repository.job.cancel()
                }
            }
        }

        //выбор ничьи
        binding!!.idGameButtonDraw.setOnClickListener {
            if (!repository.job.isActive){
                repository.job = CoroutineScope(Dispatchers.Main).launch {
                    repository.getRandomUrlImageDice()
                    loadImageAllDice()
                    if(repository.checkDrawDice()){
                        correctAnswer()
                    }else{
                        wronglyAnswer()
                    }
                    repository.job.cancel()
                }
            }
        }

        //закончить игру и забрать выигрыш
        binding!!.idGameButtonTakeMoney.setOnClickListener {
            if(!repository.job.isActive){
                repository.shortToast(requireContext(),"you have earned ${repository.winMoney} coins")
                repository.addMoneyInCashAccount(repository.winMoney)
                binding!!.idGameTvCountMoney.text = "${repository.getMoneyInCashAccount()}"
                repository.job.cancel()
                visibleViewEndGame()
            }
        }

        //кнопка начала игры
        binding!!.idGameButtonPlay.setOnClickListener {
            //проверка нехватки денег
            if(repository.getMoneyInCashAccount() >= 25){
                repository.minusMoneyInCashAccount(25)
                binding!!.idGameTvCountMoney.text = "${repository.getMoneyInCashAccount()}"
                visibleViewStartGame()
                loadImageQuestion()
                repository.shortToast(requireContext(),"where will there be more points?click on the desired button")
            }else{
                repository.longToast(requireContext(),"insufficient funds")
            }
        }

        //выход из игры путем нажатия на кнопку НАЗАД
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            if(repository.job.isActive){
                repository.job.cancel()
            }
            repository.exitingTheApplication()
        }

    }

    //очистка биндинга при очистке вью
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    //функция установки видимости кнопки начала игры и цены
    private fun visibleViewStartGame(){
        binding!!.idGameButtonPlay.isVisible = false
        binding!!.idGameTvPrice.isVisible = false
        binding!!.idGameCs1.isVisible = true
        binding!!.idGameCs2.isVisible = true
        binding!!.idGameCsResult.isVisible = true
        binding!!.idGameButtonLeftDice.isVisible = true
        binding!!.idGameButtonDraw.isVisible = true
        binding!!.idGameButtonRight.isVisible = true
        binding!!.idGameButtonTakeMoney.isVisible = true
    }

    //функция установки видимости игровых показателей
    private fun visibleViewEndGame(){
        binding!!.idGameButtonPlay.isVisible = true
        binding!!.idGameTvPrice.isVisible = true
        binding!!.idGameCs1.isVisible = false
        binding!!.idGameCs2.isVisible = false
        binding!!.idGameCsResult.isVisible = false
        binding!!.idGameButtonLeftDice.isVisible = false
        binding!!.idGameButtonDraw.isVisible = false
        binding!!.idGameButtonRight.isVisible = false
        binding!!.idGameButtonTakeMoney.isVisible = false
    }

    //функция загрузки картинок со знаком вопроса
    private fun loadImageQuestion(){
        binding!!.idGameIvDice1.load(url_image_question){scale(Scale.FIT)}
        binding!!.idGameIvDice2.load(url_image_question){scale(Scale.FIT)}
        binding!!.idGameIvDice3.load(url_image_question){scale(Scale.FIT)}
        binding!!.idGameIvDice4.load(url_image_question){scale(Scale.FIT)}
    }

    //функция загрузки картинок игральных костей
    private fun loadImageAllDice(){
        binding!!.idGameIvDice1.load(repository.listDice[0]){scale(Scale.FIT)}
        binding!!.idGameIvDice2.load(repository.listDice[1]){scale(Scale.FIT)}
        binding!!.idGameIvDice3.load(repository.listDice[2]){scale(Scale.FIT)}
        binding!!.idGameIvDice4.load(repository.listDice[3]){scale(Scale.FIT)}
    }

    //функция правильного ответа
    @SuppressLint("SetTextI18n")
    private suspend fun correctAnswer(){
        repository.shortToast(requireContext(),"TRUE! + 20 COINS")
        repository.winMoney += 20
        repository.level += 1
        repository.checkNewRecord()
        binding!!.idGameCsResultTvMoney.text = "won:${repository.winMoney} coins"
        binding!!.idGameCsResultTvLvl.text = "lvl ${repository.level}"
        delay(4000)
        loadImageQuestion()
    }

    //функция неправильного ответа
    private suspend fun wronglyAnswer(){
        repository.shortToast(requireContext(),"wrongly...(")
        repository.winMoney = 0
        repository.level = 1
        delay(4000)
        visibleViewEndGame()
    }

}