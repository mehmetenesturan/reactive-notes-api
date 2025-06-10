# Quarkus Reactive PostgreSQL ile JSONB Tabanlı REST API Geliştirme

## Proje Açıklaması

Bu proje, Quarkus framework’ü ve Reactive PostgreSQL Client API’sini kullanarak geliştirilmiş basit bir CRUD (Create, Read, Update, Delete) REST servisidir. Servis, `Note` nesnesini yönetir ve bu nesne içinde `metadata` adlı bir alan JSONB formatında saklanır.

## Teknoloji Yığını

* Java 17+
* Quarkus 3.x
* PostgreSQL
* REST (JAX-RS)
* quarkus-reactive-pg-client
* io.vertx.core.json.JsonObject
* (Opsiyonel) quarkus-junit5, rest-assured

## Veri Modeli: Note

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

## Gerçekleştirilen Adımlar

1. **Quarkus Projesi Oluşturuldu**  
   Quarkus projesi oluşturuldu ve gerekli eklentiler (`reactive-pg-client`, `resteasy-reactive`, `quarkus-junit5`, `rest-assured`) eklendi.

2. **PostgreSQL'de Tablo Oluşturuldu**  
   `notes` adında tablo oluşturuldu; içinde `id` (UUID), `title` (TEXT), `metadata` (JSONB) alanları yer aldı.

3. **Note Sınıfı Oluşturuldu**  
   Java’da `Note` sınıfı tanımlandı; `UUID id`, `String title` ve `JsonObject metadata` alanlarını içerir.

4. **Repository Katmanı Oluşturuldu**  
   Reactive PostgreSQL client kullanılarak veri tabanı işlemleri için `NoteRepository` sınıfı geliştirildi.

5. **NoteResource.java Oluşturuldu**  
   REST API endpointleri geliştirildi:
   
   * `POST /notes` → Yeni not oluşturur  
   * `GET /notes/{id}` → ID ile not getirir  
   * `GET /notes` → Tüm notları listeler  
   * `DELETE /notes/{id}` → Notu siler  

6. **application.properties Ayarlandı**

   ```properties
   quarkus.datasource.db-kind=postgresql
   quarkus.datasource.reactive.url=postgresql://localhost:5432/notesdb
   quarkus.datasource.username=postgres
   quarkus.datasource.password=postgres
   ```

7. **Unit Test Yazıldı**  
   REST endpointleri `@QuarkusTest` kullanılarak test edildi.

## Hedefler

* Reactive programlama bilgisi
* JSONB kullanımı
* REST API geliştirme
* Quarkus framework adaptasyonu
* Test yazımı

## Kurulum ve Çalıştırma

### PostgreSQL Veritabanı

```sql
CREATE DATABASE notesdb;

CREATE TABLE IF NOT EXISTS notes (
    id UUID PRIMARY KEY,
    title TEXT NOT NULL,
    metadata JSONB
);
```

### Proje Çalıştırma

```bash
./mvnw quarkus:dev
```

### Test Çalıştırma

```bash
./mvnw test
```

## API Endpointleri

| Yöntem | URL           | Açıklama             |
| ------ | ------------- | -------------------- |
| POST   | `/notes`      | Yeni not oluşturur   |
| GET    | `/notes/{id}` | ID ile not getirir   |
| GET    | `/notes`      | Tüm notları listeler |
| DELETE | `/notes/{id}` | Notu siler           |
