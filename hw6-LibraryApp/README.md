# 📚 LibraryApp - Turkcell GYGY 5.0 (Ödev 6)

Bu proje, Turkcell Geleceği Yazan Gençler (GYGY) 5.0 Kotlin Eğitimleri kapsamında geliştirilmiş olan kapsamlı bir kütüphane yönetim uygulamasıdır. Proje, modern Android geliştirme standartları (Jetpack Compose, MVVM) ve Supabase bulut veritabanı entegrasyonu ile oluşturulmuştur.

## 🚀 Özellikler

- **🔐 Kullanıcı Yetkilendirme (Authentication):**
    - Supabase Auth kullanarak E-posta ve Şifre ile Kayıt Olma.
    - Kayıtlı kullanıcılar için Güvenli Giriş Yapma.
    - Oturum kapatma (Logout) işlevi.
- **📖 Kitap Yönetimi (CRUD):**
    - **Ekleme:** İnteraktif diyalog penceresi ile yeni kitapları veritabanına kaydetme.
    - **Listeleme:** Veritabanındaki kitapları şık kart tasarımları (CardView) ile görüntüleme.
    - **Arama:** Kitap adı veya yazar ismine göre anlık filtreleme.
    - **Düzenleme:** Mevcut kitap bilgilerini diyalog üzerinden güncelleme.
    - **Silme:** Kitapları tek tıkla veritabanından kalıcı olarak kaldırma.

## 🛠 Kullanılan Teknolojiler

- **Dil:** Kotlin
- **UI Framework:** Jetpack Compose (Modern Bildirimsel Arayüz)
- **Mimari:** MVVM (Model-View-ViewModel)
- **Veritabanı & Auth:** Supabase (PostgreSQL & GoTrue)
- **Networking:** Ktor Client
- **Veri İşleme:** Kotlinx Serialization
- **Navigasyon:** Jetpack Compose Navigation

## ⚙️ Kurulum ve Yapılandırma

1. **Supabase Projesi:** [Supabase](https://supabase.com/) üzerinden bir proje oluşturun.
2. **Tablo Yapısı:** SQL Editor üzerinden `books` tablosunu aşağıdaki gibi oluşturun:
   ```sql
   create table public.books (
     id uuid default gen_random_uuid() primary key,
     title text not null,
     author text not null,
     isbn text,
     category text,
     page_count int4 default 0
   );
   ```
3. **RLS Politikaları:** `anon` ve `authenticated` rolleri için gerekli SELECT, INSERT, UPDATE, DELETE izinlerini tanımlayın.
4. **API Anahtarları:** `SupabaseConfig.kt` dosyası içerisine kendi `URL` ve `ANON_KEY` bilgilerinizi girin.

## 📱 Ekran Görüntüleri

*   **Giriş/Kayıt:** Kullanıcı dostu, Material3 tasarımlı giriş ekranları.
*   **Kütüphane Listesi:** Arama çubuğu ve yüzer işlem butonu (FAB) içeren ana ekran.
*   **İşlem Diyalogları:** Kitap ekleme ve düzenleme için açılır pencereler.

---
*Bu proje Erkin Avcı tarafından Turkcell GYGY 5.0 eğitimi final ödevi olarak hazırlanmıştır.* 🎓
