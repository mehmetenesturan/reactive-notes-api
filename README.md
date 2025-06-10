# Reactive Notes API

Quarkus ve Reactive PostgreSQL kullanarak metadata içeren notları yöneten basit bir REST API.

## Teknoloji Altyapısı

* Java 17
* Quarkus 3.8.1
* PostgreSQL
* Reactive Programming

## Veritabanı Kurulumu

Veritabanını iki şekilde kurabilirsiniz:

1. **notes.sql Kullanarak**:
   ```bash
   # PostgreSQL'e bağlanın
   psql -U postgres
   
   # SQL scriptini çalıştırın
   \i notes.sql
   ```

2. **Manuel Kurulum**:
   ```sql
   -- PostgreSQL'e bağlanın ve bu komutları çalıştırın:
   CREATE DATABASE notesdb;
   
   \c notesdb;
   
   CREATE TABLE IF NOT EXISTS notes (
       id UUID PRIMARY KEY,
       title TEXT NOT NULL,
       metadata JSONB
   );
   ```
## Uygulamayı Çalıştırma

1. **Geliştirme Modu**:
   ```bash
   ./mvnw quarkus:dev
   ```

2. **Testleri Çalıştırma**:
   ```bash
   ./mvnw test
   ```

## API Endpointleri

| Metod  | Uç Nokta      | Açıklama           |
|--------|--------------|-------------------|
| POST   | `/notes`     | Yeni not oluştur  |
| GET    | `/notes/{id}`| ID ile not getir  |
| GET    | `/notes`     | Tüm notları listele|
| DELETE | `/notes/{id}`| Notu sil          |

## Örnek Not Yapısı

```json
{
  "id": "UUID",
  "title": "string",
  "metadata": {
    "tags": ["work", "urgent"],
    "color": "yellow"
  }
}
```

## Sorun Giderme

1. **Maven Wrapper Sorunları**:
   - Eğer `mvnw` komutu bulunamıyorsa veya çalışmıyorsa:
     ```bash
     mvn wrapper:wrapper
     ```
2. **İzin Sorunları** (macOS/Linux):
   - Eğer `mvnw` için izin hatası alıyorsanız:
     ```bash
     chmod +x mvnw
     ```

