# Android TicketApp Typography Specification

Bu belgede, TicketApp uygulamasında kullanılan tipografi sistemi ve Material 3 standardına uygun Roboto font entegrasyonu detaylandırılmıştır.

## Font Seçimi: Roboto
Roboto, Google tarafından Android işletim sistemi için özel olarak geliştirilmiş modern, neo-grotesk bir sans-serif yazı tipidir. 
TicketApp uygulamasında Roboto'nun tercih edilme nedenleri:
- **Okunabilirlik**: Küçük ekran boyutlarında ve düşük çözünürlüklü cihazlarda dahi mükemmel netlik sunar.
- **Çeşitlilik**: Farklı ağırlıklarda (Thin, Light, Regular, Medium, Bold, Black) geniş bir stil yelpazesi sunarak zengin görsel hiyerarşi oluşturmayı kolaylaştırır.
- **Performans**: Google Fonts API entegrasyonu sayesinde cihazda yüklü olmasa bile dinamik olarak indirilir ve APK boyutunu şişirmez.

---

## Tipografi Skala Tablosu (Material 3)

| Stil İsmi | Ağırlık (Weight) | Boyut (Size) | Satır Yüksekliği (Line Height) | Karakter Aralığı (Letter Spacing) | Kullanım Alanı |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Display Large** | Light (300) | 57 sp | 64 sp | -0.25 sp | Büyük ekran karşılama sayıları/başlıkları |
| **Display Medium** | Regular (400) | 45 sp | 52 sp | 0 sp | Önemli promosyonlar veya vurgu metinleri |
| **Display Small** | Regular (400) | 36 sp | 44 sp | 0 sp | İkincil ekran başlıkları |
| **Headline Large** | Regular (400) | 32 sp | 40 sp | 0 sp | Ekran ana başlıkları |
| **Headline Medium** | Medium (500) | 28 sp | 36 sp | 0 sp | Bölüm başlıkları (Giriş/Kayıt ekranları vb.) |
| **Headline Small** | Medium (500) | 24 sp | 32 sp | 0 sp | Küçük kart veya diyalog başlıkları |
| **Title Large** | Regular (400) | 22 sp | 28 sp | 0 sp | Toolbar/AppBar başlıkları |
| **Title Medium** | Medium (500) | 16 sp | 24 sp | 0.15 sp | Liste elemanı ana metin başlıkları |
| **Title Small** | Medium (500) | 14 sp | 20 sp | 0.1 sp | Form etiketleri ve alt başlıklar |
| **Body Large** | Regular (400) | 16 sp | 24 sp | 0.5 sp | Genel paragraflar ve uzun okuma metinleri |
| **Body Medium** | Regular (400) | 14 sp | 20 sp | 0.25 sp | Liste detay açıklamaları |
| **Body Small** | Regular (400) | 12 sp | 16 sp | 0.4 sp | İpucu metinleri, hata mesajları |
| **Label Large** | Medium (500) | 14 sp | 20 sp | 0.1 sp | Buton metinleri |
| **Label Medium** | Medium (500) | 12 sp | 16 sp | 0.5 sp | Rozetler (Badge), durum etiketleri |
| **Label Small** | Regular (400) | 11 sp | 16 sp | 0.5 sp | Dipnotlar ve meta veri bilgileri |

---

## Tipografi Üretim Promtu

Kodun üretilmesinde kullanılan prompt aşağıdadır:

```
"Jetpack Compose ve Material 3 kullanan bir Android uygulaması için Roboto fontunu temel alan kapsamlı bir tipografi sistemi oluştur. FontFamily tanımı, tüm MaterialTheme.typography text stilleri (displayLarge'dan labelSmall'a kadar), font ağırlıkları (Thin, Light, Regular, Medium, Bold) ve boyut hiyerarşisi (12sp-57sp arası) içeren eksiksiz bir Type.kt dosyası yaz. Kod, Google Fonts API kullanarak Roboto fontunu dinamik olarak yüklemeli ve Koin DI ile uyumlu olmalıdır."
```
