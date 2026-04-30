# LibraryApp - Supabase Entegrasyonu (Ödev 1, 2, 3)

Bu klasörde, LibraryApp projesinin Supabase ile güçlendirilmiş 3 farklı ödev adımını tamamladım.

## 📝 Tamamlanan Görevler

### ÖDEV 1: Kayıt Ol Success Yapısı
- `AuthState` sınıfı genişletilerek `Success` durumu eklendi.
- `AuthViewModel` içinde kayıt işlemi başarılı olduğunda bu state tetikleniyor.
- `RegisterScreen` içinde `LaunchedEffect` kullanılarak, `Success` durumu geldiğinde kullanıcının otomatik olarak ana sayfaya yönlendirilmesi sağlandı.

### ÖDEV 2: BookRepository Geliştirmeleri
- `BookRepository` sınıfı, Supabase Postgrest kütüphanesi kullanılarak şu fonksiyonlarla donatıldı:
    - **Arama (`searchBooks`):** `ilike` filtresi ile kitap adına göre arama.
    - **Güncelleme (`updateBook`):** Mevcut kitap verilerini ID üzerinden güncelleme.
    - **Silme (`deleteBook`):** Kitabı ID üzerinden veritabanından kaldırma.

### ÖDEV 3: Kitap Kartı Tasarımı (BookCard)
- `BookCard.kt` adında ayrı bir Composable bileşeni oluşturuldu.
- Tasarımda; Kitap Başlığı, Yazar, Kategori, Sayfa Sayısı ve **Stok Durumu** (Mevcut/Yok) bilgileri görselleştirildi.
- Düzenle ve Sil butonları eklendi.

## 🗄️ Veritabanı Şeması (Supabase SQL)

Projenin çalışması için Supabase SQL editöründe şu tabloyu oluşturmalısınız:

```sql
create table books (
  id uuid primary key default gen_random_uuid(),
  title text not null,
  author text not null,
  isbn text default '',
  category text default '',
  page_count integer not null,
  total_copies integer not null default 1,
  available_copies integer not null default 1
);

create policy "Kitaplar herkes tarafından görüntülenebilir"
on books for select
using (true);
```

## 🚀 Entegrasyon Notları
Kodlar `io.github.jan-tennert.supabase` kütüphanesi ile uyumlu yazılmıştır. Projenize kopyalarken kendi Supabase URL ve API Key tanımlamalarınızı yapmayı unutmayın.
