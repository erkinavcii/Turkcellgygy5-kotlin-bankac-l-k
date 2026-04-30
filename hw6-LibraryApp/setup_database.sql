-- 1. Kitaplar (books) Tablosunu Oluşturma
create table books (
  id uuid primary key default gen_random_uuid(),
  title text not null,
  author text not null,
  isbn text default '',
  category text default '',
  page_count integer not null,
  total_copies integer not null default 1,
  available_copies integer not null default 1,
  created_at timestamp with time zone default timezone('utc'::text, now()) not null
);

-- 2. Güvenlik Politikaları (RLS)
-- Kitapların herkes tarafından görülebilmesi için (ÖDEV gereği):
alter table books enable row level security;

create policy "Kitaplar herkes tarafından görüntülenebilir"
on books for select
using (true);

-- Not: Ekleme/Silme işlemleri için panelden "Enable RLS" kutucuğunu kapatabilir 
-- veya tüm yetkileri veren bir politika ekleyebilirsin.
create policy "Herkes kitap ekleyebilir ve silebilir"
on books for insert, update, delete
using (true)
with check (true);
