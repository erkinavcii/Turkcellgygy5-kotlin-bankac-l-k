# Ödev 5: Kullanıcı Listesi Uygulaması (API + MVVM)

Bu ödev, PDF'teki tüm gereksinimleri (Retrofit, StateFlow, MVVM ve Jetpack Compose) karşılayacak şekilde hazırlanmıştır.

## 🛠 Teknik Özellikler

- **Mimar Yapısı (MVVM):** Veri katmanı (`data`), arayüz katmanı (`ui`) ve mantık katmanı (`viewmodel`) birbirinden tamamen ayrılmıştır.
- **API Entegrasyonu:** `jsonplaceholder.typicode.com/users` adresinden veriler **Retrofit** kullanılarak çekilmektedir.
- **Durum Yönetimi (StateFlow):** UI durumu `Loading`, `Success` ve `Error` olarak 3 farklı aşamada yönetilmektedir.
- **Arayüz (Jetpack Compose):** 
    - `LazyColumn` ile performanslı listeleme.
    - Her kullanıcı için dairesel avatar (ismin baş harfi).
    - Hata durumunda "Tekrar Dene" butonu.
    - Modern Material 3 bileşenleri.

## 📂 Klasör Yapısı

```text
hw5-KullaniciListesi/
├── data/
│   ├── model/ (User data class)
│   ├── remote/ (Retrofit ve ApiService)
│   └── repository/ (Veri çekme mantığı)
├── ui/
│   ├── components/ (UserItem tasarımı)
│   └── screen/ (Ana liste ekranı)
├── viewmodel/ (Durum yönetimi)
└── MainActivity.kt (Giriş noktası)
```

## 🚀 Projeye Nasıl Eklenir?

1. Android Studio'da yeni bir Compose projesi açın (Package name: `com.example.userapp`).
2. `build.gradle.kts` dosyanıza PDF'te belirtilen Retrofit, Gson ve Lifecycle kütüphanelerini ekleyin.
3. `AndroidManifest.xml` dosyasına internet iznini eklemeyi unutmayın:
   `<uses-permission android:name="android.permission.INTERNET"/>`
4. Bu klasördeki dosyaları kendi projenizdeki ilgili dizinlere kopyalayın.

*Not: ViewModel'i MainActivity'de başlatırken manuel bir Factory kullandım, böylece Hilt gibi ekstra kütüphane kurulumuna gerek kalmadan proje çalışacaktır.*
