package com.example.simplyachivs.domain.model.complexity

enum class TaskComplexity(override val xp: Int, override val coins: Int) : Complexity {
    EASY(
        xp = 100,
        coins = 100
    ),
    MEDIUM(
        xp = 200,
        coins = 200,
    ),
    HARD(
        xp = 300,
        coins = 300,
    )
}