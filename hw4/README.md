# Ödev 4: Kişiler Uygulaması (Ekle/Sil ve Yenile)

Bu ödevde, Jetpack Compose ile bir "Kişiler Listesi" uygulaması geliştirilmiştir. Ödevin ana odak noktası, bir eleman eklendiğinde veya silindiğinde listenin otomatik olarak yenilenmesini sağlamaktır.

## 🔄 Liste Nasıl Yenileniyor? (Harbi Anlatım)

Eren Yağlı ve Neslihan'ın projelerindeki mantığı inceledikten sonra, en temiz ve anlık çalışan çözümü uyguladım:

1.  **mutableStateListOf:** `ViewModel` içinde normal bir `ArrayList` yerine `mutableStateListOf` kullandık. Bu özel yapı, liste içindeki herhangi bir değişiklikten (ekleme/silme) Jetpack Compose'u anında haberdar eder.
2.  **ViewModel State:** `addPerson` veya `deletePerson` fonksiyonları çağrıldığında, liste doğrudan güncellenir. `MainActivity` içinde bu listeyi gözlemleyen `LazyColumn`, liste değiştiği anda sadece değişen kısımları "Re-composition" (Yeniden çizim) yaparak ekrana yansıtır.
3.  **Navigation PopBack:** Ekleme sayfasından ana sayfaya `navController.popBackStack()` ile döndüğümüzde, ana sayfa zaten ViewModel'deki o listeyi izlediği için veri eklenmiş olarak bizi karşılar.

## 🚀 Projeye Entegrasyon

1.  Android Studio'da `Empty Activity` şablonuyla bir proje açın.
2.  `MainActivity.kt` içeriğini bu klasördeki kodla değiştirin.
3.  `build.gradle` dosyanızda navigasyon kütüphanesinin ekli olduğundan emin olun: 
    - `implementation("androidx.navigation:navigation-compose:2.7.7")`

*Bu çalışma Turkcell GYGY 5.0 eğitimi kapsamında, MVVM ve State Management konularını pekiştirmek için hazırlanmıştır.*
