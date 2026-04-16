package com.example.hw3portfolio

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PortfolioTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PortfolioScreen()
                }
            }
        }
    }
}

// Varsayılan tema (Android Studio kendi temanızı oluşturur, bunu kullanmazsanız silip kendi temanızı yazabilirsiniz)
@Composable
fun PortfolioTheme(content: @Composable () -> Unit) {
    MaterialTheme(content = content)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PortfolioScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        
        // ==========================================
        // 1. PROFİL FOTOĞRAFI BÖLÜMÜ
        // ==========================================
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .border(4.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .background(Color.LightGray.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            // BURASI ÇOK ÖNEMLİ:
            // Android Studio'daki projenizde soldaki menüden 'res' -> 'drawable' klasörüne sağ tıklayın.
            // Fotoğrafınızı oraya kopyalayın (Örn: fotoğrafın adı "erkin_profil.jpg" olsun).
            // Sonra aşağıdaki Icon kısmını silip şu kodu açın:
            
            /*
            Image(
                painter = painterResource(id = R.drawable.erkin_profil), // İSMİ BURAYA YAZACAKSIN
                contentDescription = "Profil Fotoğrafı",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            */
            
            // Eğer fotoğrafı bulamazsa diye Backup Foto (Facebook/WhatsApp varsayılan boş kafa resmi):
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Varsayılan Profil",
                modifier = Modifier.size(100.dp),
                tint = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ==========================================
        // 2. KİŞİSEL BİLGİLER
        // ==========================================
        Text(
            text = "Ali Erkin Avcı",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Bilgisayar Mühendisi",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = "Eskişehir Osmangazi Üniversitesi",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        // ==========================================
        // 3. YETENEKLER (SKILL SET)
        // ==========================================
        Text(
            text = "Uzmanlık Alanları",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Buradaki listeyi istediğin gibi uzatabilirsin
        val skills = listOf("C++", "C#", "Python", ".NET", "SQL", "Kotlin", "Android")
        
        // FlowRow: Yetenekler ekrana sığmadığı an otomatik olarak alt satıra geçer
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            skills.forEach { skill ->
                SkillChip(skill = skill)
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // ==========================================
        // 4. İLETİŞİM / SOSYAL MEDYA BUTONLARI
        // ==========================================
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // LinkedIn Butonu
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/alierkinavci"))
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .weight(1f)
                    .height(55.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "LinkedIn", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.width(16.dp))

            // GitHub Butonu (Tasarımı farklı olması için Rengini Koyu Gri Yaptık)
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/erkinavcii"))
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                modifier = Modifier
                    .weight(1f)
                    .height(55.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "GitHub", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// Rozet (Chip) tasarımı için özel component
@Composable
fun SkillChip(skill: String) {
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(16.dp),
    ) {
        Text(
            text = skill,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PortfolioPreview() {
    PortfolioTheme {
        PortfolioScreen()
    }
}
