package com.kavyakanaja.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.Locale
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.kavyakanaja.model.Poem
import com.kavyakanaja.model.WordMeaning
import com.kavyakanaja.ui.theme.HighlightText

@Composable
fun PoemOfTheDayScreen(poem: Poem?) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedMeaning by remember { mutableStateOf<WordMeaning?>(null) }
    
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    DisposableEffect(Unit) {
        var textToSpeech: TextToSpeech? = null
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val locale = Locale("kn", "IN")
                val result = textToSpeech?.setLanguage(locale)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    textToSpeech?.language = Locale.getDefault()
                }
                textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        isPlaying = true
                    }
                    override fun onDone(utteranceId: String?) {
                        isPlaying = false
                    }
                    override fun onError(utteranceId: String?) {
                        isPlaying = false
                    }
                })
            }
        }
        tts = textToSpeech

        onDispose {
            textToSpeech?.stop()
            textToSpeech?.shutdown()
        }
    }

    if (poem == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No poem found for today.", style = MaterialTheme.typography.bodyLarge)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Poem of the Day",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = poem.title,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        
        Text(
            text = "- ${poem.poet}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Listen & Learn Button
        Button(
            onClick = {
                if (isPlaying) {
                    tts?.stop()
                    isPlaying = false
                } else {
                    tts?.speak(poem.text, TextToSpeech.QUEUE_FLUSH, null, "TTS_ID")
                    isPlaying = true
                }
            },
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Icon(Icons.Filled.PlayArrow, contentDescription = "Play Audio")
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (isPlaying) "Pause" else "Listen & Learn")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Interactive Poem Text
        val annotatedString = buildAnnotatedString {
            var currentText = poem.text
            val matches = mutableListOf<Pair<Int, WordMeaning>>()
            
            for (meaning in poem.difficultWords) {
                var index = currentText.indexOf(meaning.word)
                while (index >= 0) {
                    matches.add(index to meaning)
                    index = currentText.indexOf(meaning.word, index + 1)
                }
            }
            
            matches.sortBy { it.first }
            
            var lastIndex = 0
            for (match in matches) {
                val startIndex = match.first
                if (startIndex < lastIndex) continue // Overlapping match, skip
                
                val meaning = match.second
                append(currentText.substring(lastIndex, startIndex))
                
                pushStringAnnotation(tag = "meaning", annotation = meaning.word)
                withStyle(style = SpanStyle(color = HighlightText, textDecoration = TextDecoration.Underline)) {
                    append(meaning.word)
                }
                pop()
                lastIndex = startIndex + meaning.word.length
            }
            if (lastIndex < currentText.length) {
                append(currentText.substring(lastIndex))
            }
        }

        Text(
            text = annotatedString,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.clickable { 
                // A simplified approach for click detection; a real app might use ClickableText
                // but ClickableText is partially deprecated in favor of modifier or complex setup.
                // For demonstration, we'll just show the first meaning if clicked anywhere,
                // or we can use the proper ClickableText if needed.
            }
        )
        
        // Proper Clickable Text using material3 compatible way
        androidx.compose.foundation.text.ClickableText(
            text = annotatedString,
            style = MaterialTheme.typography.bodyLarge.copy(
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            ),
            onClick = { offset ->
                annotatedString.getStringAnnotations(tag = "meaning", start = offset, end = offset)
                    .firstOrNull()?.let { annotation ->
                        val word = annotation.item
                        selectedMeaning = poem.difficultWords.find { it.word == word }
                        showDialog = true
                    }
            }
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Bhavartha Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Bhavartha (Meaning)",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = poem.bhavartha,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

    if (showDialog && selectedMeaning != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = selectedMeaning!!.word) },
            text = { Text(text = selectedMeaning!!.meaning) },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Got it")
                }
            }
        )
    }
}
