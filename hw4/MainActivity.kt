package com.example.hw4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/**
 * ÖDEV 4: KİŞİLER UYGULAMASI (SİLME VE EKLEME SONRASI YENİLEME)
 * 
 * Bu projede MVVM yapısını kullanarak, listenin herhangi bir işlemden sonra
 * otomatik olarak kendini yenilemesini sağlayan 'mutableStateListOf' yapısı kullanılmıştır.
 */

// 1. VERİ MODELİ
data class Person(
    val id: Int,
    val name: String,
    val phone: String
)

// 2. VIEWMODEL (İş Mantığı ve Liste Yönetimi)
class PersonViewModel : ViewModel() {
    // Jetpack Compose'un bu listeyi izleyebilmesi için 'mutableStateListOf' kullanıyoruz.
    // Bu sayede listeye eleman ekleme veya çıkarma yapıldığında UI anında kendini yeniler.
    private val _personList = mutableStateListOf(
        Person(1, "Eren Yağlı", "0555 111 22 33"),
        Person(2, "Neslihan", "0555 444 55 66"),
        Person(3, "Ali Erkin", "0555 777 88 99")
    )
    val personList: SnapshotStateList<Person> = _personList

    // Kişi Ekleme Fonksiyonu
    fun addPerson(name: String, phone: String) {
        val nextId = if (_personList.isEmpty()) 1 else _personList.maxOf { it.id } + 1
        _personList.add(Person(nextId, name, phone))
        // 'mutableStateListOf' kullandığımız için ekstra bir 'refresh' çağırmaya gerek yoktur!
    }

    // Kişi Silme Fonksiyonu
    fun deletePerson(person: Person) {
        _personList.remove(person)
        // Eleman silindiği anda UI bunu anlar ve listeyi yeniden çizer.
    }
}

// 3. ANA SAYFA EKRANI (LİSTELEME)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainListScreen(navController: NavController, viewModel: PersonViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Kişilerim (Hw 4)") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add_screen") }) {
                Icon(Icons.Default.Add, contentDescription = "Ekle")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (viewModel.personList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Liste boş, hadi birini ekle!", color = Color.Gray)
                }
            } else {
                LazyColumn {
                    items(viewModel.personList) { person ->
                        PersonRow(person = person, onDeleteClick = { viewModel.deletePerson(person) })
                    }
                }
            }
        }
    }
}

// Liste Satırı Tasarımı
@Composable
fun PersonRow(person: Person, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterHorizontally,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = person.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = person.phone, fontSize = 14.sp, color = Color.Gray)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Sil", tint = Color.Red)
            }
        }
    }
}

// 4. KİŞİ EKLEME EKRANI
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPersonScreen(navController: NavController, viewModel: PersonViewModel) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Yeni Kişi Ekle") }) }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("İsim Soyisim") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Telefon Numarası") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    if (name.isNotEmpty() && phone.isNotEmpty()) {
                        viewModel.addPerson(name, phone)
                        navController.popBackStack() // Kaydettikten sonra geri dön
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Kaydet ve Listeyi Yenile")
            }
        }
    }
}

// 5. NAVİGASYON VE MAIN ACTIVITY GİRİŞİ
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val viewModel: PersonViewModel = viewModel() // ViewModel her yere aynı instance olarak gider

            NavHost(navController = navController, startDestination = "main_screen") {
                composable("main_screen") {
                    MainListScreen(navController, viewModel)
                }
                composable("add_screen") {
                    AddPersonScreen(navController, viewModel)
                }
            }
        }
    }
}
