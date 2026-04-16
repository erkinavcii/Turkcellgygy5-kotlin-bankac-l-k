# Android Jetpack Compose Profil Tasarımı (Ödev 3)

Selam! Bu klasörde senin için Android Studio'da tamamen şık ve çalışır durumda bir **Jetpack Compose Profil Tasarımı** hazırladım.

## Kodun Sahip Olduğu Özellikler
- **Kişisel Bilgiler:** İsim (Ali Erkin Avcı), Üniversite ve Bölüm bilgilerin modern bir fontla yazıldı.
- **Dinamik Yetenek Rozetleri (Skill Chips):** Listeye eklediğimiz `C++, C#, Python, SQL...` gibi yetenekler otomatik olarak ekrana yayılıyor ve sığmayanlar şık bir şekilde alta geçiyor (`FlowRow` sayesinde).
- **Gerçek Butonlar:** LinkedIn ve GitHub butonları tasarlandı. Uygulama telefonda çalıştırıldığında bu butonlara basıldığında senin gerçek profillerine gidiyor!

## Android Studio'da Nasıl Çalıştıracaksın?

1. Android Studio'yu açıp **New Project -> "Empty Activity" (Compose ile çalışan)** seç. Projenin adını `hw3Portfolio` yap.
2. Açılan projede `MainActivity.kt` dosyasının içindeki her şeyi sil ve benim sana verdiğim **`MainActivity.kt`** dosyasındaki kodları tamamen yapıştır.
3. Tasarımı sağ taraftaki `Design / Split` penceresinden anında görebilirsin.

## 📸 Fotoğrafı Nasıl Ekleyeceksin?

Kodda fotoğraf kısmı için bir kapsayıcı ayarladım. Şu an sen fotoğraf koymazsan diye varsayılan bir "Boş Kafa" (Backup Person Icon) görünüyor. Fotoğrafı eklemek için:

1. Kendi fotoğrafını (Örn: `erkin_profil.jpg`) kopyala.
2. Android Studio'da sol taraftaki dosya menüsünden: **`app` -> `res` -> `drawable`** klasörünün üstüne sağ tıklayıp yapıştır (Paste).
3. `MainActivity.kt` kodunun içinde şöyle bir yer var (yaklaşık 70. satırlarda):
   
   ```kotlin
   // Image(
   //     painter = painterResource(id = R.drawable.erkin_profil), 
   //     contentDescription = "Profil Fotoğrafı", ...
   // )
   ```
4. O `Image` kodunun başındaki `//` yorum satırlarını sil ve hemen altındaki `Icon(...)` kodunu sil. Artık senin fotoğrafın yuvarlak ve harika bir çubukla (border) gösterilecek!
