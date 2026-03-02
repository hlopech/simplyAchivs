package com.example.simplyachivs.domain.model.complexity

enum class TaskComplexity(override val xp: Int, override val coins: Int) : Complexity {
    EASY(
        xp = 50,
        coins = 50
    ),
    MEDIUM(
        xp = 100,
        coins = 100,
    ),
    HARD(
        xp = 150,
        coins = 150,
    )
}