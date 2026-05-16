package com.kavyakanaja.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class JnanpithAwardee(
    val name: String,
    val year: String,
    val famousWork: String,
    val biography: String
)

val awardees = listOf(
    JnanpithAwardee(
        "Kuppali Venkatappa Puttappa (Kuvempu)",
        "1967",
        "Sri Ramayana Darshanam",
        "The first Kannada writer to receive the Jnanpith Award. He championed the 'Vishwa Manava' (Universal Human) ideology and wrote extensively in forms ranging from epic poetry to novels."
    ),
    JnanpithAwardee(
        "Dattatreya Ramachandra Bendre",
        "1973",
        "Nakutanthi",
        "Known as 'Varakavi' (gifted poet), Bendre is considered one of the greatest lyric poets in Kannada literature. His works deeply explore nature, human emotions, and mysticism."
    ),
    JnanpithAwardee(
        "K. Shivaram Karanth",
        "1977",
        "Mookajjiya Kanasugalu",
        "A polymath who contributed to literature, art, and environmentalism. His novels explore the socio-cultural fabric of coastal Karnataka with profound philosophical insights."
    ),
    JnanpithAwardee(
        "Masti Venkatesha Iyengar",
        "1983",
        "Chikkaveera Rajendra",
        "Fondly known as 'Masti Kannadada Aasti' (Masti is Kannada's Treasure), he is regarded as the father of the Kannada short story."
    ),
    JnanpithAwardee(
        "V. K. Gokak",
        "1990",
        "Bharatha Sindhu Rashmi",
        "A prominent writer who led the Navya (Modernist) movement in Kannada literature. His epic 'Bharatha Sindhu Rashmi' is a monumental work on Vedic culture."
    ),
    JnanpithAwardee(
        "U. R. Ananthamurthy",
        "1994",
        "Samskara",
        "A leading figure of the Navya movement, his works critically examine traditional societal norms and the complexities of human existence."
    ),
    JnanpithAwardee(
        "Girish Karnad",
        "1998",
        "Tughlaq, Hayavadana",
        "A highly acclaimed playwright, actor, and director. His plays often use history and mythology to address contemporary socio-political issues."
    ),
    JnanpithAwardee(
        "Chandrashekhara Kambara",
        "2010",
        "Sirisampige, Shikharasoorya",
        "A poet and playwright known for his effective use of North Karnataka dialect and folklore in his works."
    )
)

@Composable
fun PoetsCornerScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Jnanpith Awardees",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(24.dp)
        )
        
        LazyColumn(
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(awardees) { awardee ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = awardee.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Awarded: ${awardee.year}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Famous Work: ${awardee.famousWork}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = awardee.biography,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
