package com.example.unscramble.ui.test

import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.getUnscrambledWord
import com.example.unscramble.ui.GameViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GameViewModelTest {
    private val viewModel = GameViewModel()

    @Test
    fun gameViewModel_CorrectWordGuessed_ScoreUpdatedAndErrorFlagUnset() {
        var currentGameUIState = viewModel.uiState.value
        val correctPlayerWord = getUnscrambledWord(currentGameUIState.currentScrambledWord)

        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()

        currentGameUIState = viewModel.uiState.value

        assertFalse(currentGameUIState.isGuessedWordWrong)
        assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER, currentGameUIState.score)
    }

    @Test
    fun gameViewModel_IncorrectGuess_ErrorFlagSet() {
        val incorrectPlayerWord = "and"
        viewModel.updateUserGuess(incorrectPlayerWord)
        viewModel.checkUserGuess()

        val currentGameUIState = viewModel.uiState.value

        assertEquals(0, currentGameUIState.score)
        assertTrue(currentGameUIState.isGuessedWordWrong)
    }

    @Test
    fun gameViewModel_Initialization_FirstWordLoaded() {
        val gameUIState = viewModel.uiState.value
        val unscrambledWord = getUnscrambledWord(gameUIState.currentScrambledWord)

        assertNotEquals(unscrambledWord, gameUIState.currentScrambledWord)
        assertTrue(gameUIState.currentWordCount == 1)
        assertTrue(gameUIState.score == 0)
        assertFalse(gameUIState.isGuessedWordWrong)
        assertFalse(gameUIState.isGameOver)
    }

    @Test
    fun gameViewModel_AllWordsGuessed_UIStateUpdatedCorrectly() {
        var expectedScore = 0
        var currentGameUIState = viewModel.uiState.value
        var correctPlayerWord = getUnscrambledWord(currentGameUIState.currentScrambledWord)

        repeat(MAX_NO_OF_WORDS) {
            expectedScore += SCORE_INCREASE
            viewModel.updateUserGuess(correctPlayerWord)
            viewModel.checkUserGuess()
            currentGameUIState = viewModel.uiState.value
            correctPlayerWord = getUnscrambledWord(currentGameUIState.currentScrambledWord)

            assertEquals(expectedScore, currentGameUIState.score)
        }

        assertEquals(MAX_NO_OF_WORDS, currentGameUIState.currentWordCount)

        assertTrue(currentGameUIState.isGameOver)
    }

    @Test
    fun gameViewModel_WordSkipped_ScoreUnchangedAndWordCountIncreased() {
        var currentGameUIState = viewModel.uiState.value
        val correctPlayerWord = getUnscrambledWord(currentGameUIState.currentScrambledWord)

        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()

        currentGameUIState = viewModel.uiState.value
        val lastWordCount = currentGameUIState.currentWordCount
        viewModel.skipWord()
        currentGameUIState = viewModel.uiState.value
        assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER, currentGameUIState.score)
        assertEquals(lastWordCount + 1, currentGameUIState.currentWordCount)
    }

    companion object {
        private const val SCORE_AFTER_FIRST_CORRECT_ANSWER = SCORE_INCREASE
    }
}