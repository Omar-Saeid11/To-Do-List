package com.example.todolist.screens

import com.example.todolist.R

class OnBoardingItems(
    val image: Int
) {
    companion object {
        fun getData(): List<OnBoardingItems> {
            return listOf(
                OnBoardingItems(
                    R.drawable.on_boarding_first
                ),
                OnBoardingItems(
                    R.drawable.on_boarding_second
                ),
                OnBoardingItems(
                    R.drawable.onboading3
                )
            )
        }
    }
}