# Android Переводчик

Современное Android приложение для перевода между русским и английским языками с темным дизайном и расширенным функционалом.

## Функции

- ✅ Перевод текста между русским и английским языками
- ✅ Транскрипция для английских слов
- ✅ Примеры использования переведенных слов в контексте
- ✅ История переводов с возможностью поиска
- ✅ Избранные переводы
- ✅ Темный современный дизайн
- ✅ Локальное хранение данных (без интернета для истории)
- ✅ Копирование переводов в буфер обмена
- ✅ Смена направления перевода одним нажатием

## Технические требования

- **Минимальная версия Android:** API 24 (Android 7.0)
- **Целевая версия Android:** API 34 (Android 14)
- **Язык программирования:** Kotlin
- **Архитектура:** MVVM с LiveData
- **База данных:** Room (SQLite)
- **Сеть:** Retrofit + MyMemory Translation API

## Структура проекта

```
app/
├── src/main/java/com/translator/app/
│   ├── data/                    # Модели данных и Room entities
│   │   ├── TranslationHistory.kt
│   │   ├── TranslationDao.kt
│   │   ├── TranslationDatabase.kt
│   │   └── TranslationResponse.kt
│   ├── network/                 # Сетевые сервисы
│   │   └── TranslationService.kt
│   ├── repository/              # Репозитории для работы с данными
│   │   ├── TranslationRepository.kt
│   │   └── DatabaseRepository.kt
│   ├── viewmodel/               # ViewModels
│   │   ├── MainViewModel.kt
│   │   ├── HistoryViewModel.kt
│   │   └── FavoritesViewModel.kt
│   ├── adapter/                 # RecyclerView адаптеры
│   │   └── TranslationHistoryAdapter.kt
│   ├── MainActivity.kt          # Главная активность
│   ├── MainFragment.kt          # Фрагмент перевода
│   ├── HistoryFragment.kt       # Фрагмент истории
│   └── FavoritesFragment.kt     # Фрагмент избранного
└── src/main/res/
    ├── layout/                  # XML макеты
    ├── values/                  # Ресурсы (цвета, строки, стили)
    ├── drawable/                # Иконки и drawable ресурсы
    └── menu/                    # Меню навигации
```

## Сборка и установка

### Предварительные требования

1. **Android Studio** (последняя версия)
2. **JDK 8** или выше
3. **Android SDK** с API 34
4. **Gradle** (встроен в Android Studio)

### Шаги для сборки

1. **Клонирование проекта:**
   ```bash
   git clone <repository-url>
   cd AndroidTranslatorApp
   ```

2. **Открытие в Android Studio:**
   - Запустите Android Studio
   - Выберите "Open an existing Android Studio project"
   - Выберите папку `AndroidTranslatorApp`

3. **Синхронизация Gradle:**
   - Android Studio автоматически предложит синхронизировать проект
   - Нажмите "Sync Now" если появится уведомление
   - Дождитесь завершения загрузки зависимостей

4. **Сборка APK:**
   ```bash
   # Через командную строку
   ./gradlew assembleDebug
   
   # Или через Android Studio:
   # Build → Build Bundle(s) / APK(s) → Build APK(s)
   ```

5. **Установка на устройство:**
   ```bash
   # Через ADB
   adb install app/build/outputs/apk/debug/app-debug.apk
   
   # Или через Android Studio:
   # Run → Run 'app'
   ```

### Настройка для разработки

1. **Включение режима разработчика на устройстве:**
   - Настройки → О телефоне → Нажать 7 раз на "Номер сборки"
   - Настройки → Для разработчиков → Включить "Отладка по USB"

2. **Подключение устройства:**
   - Подключите устройство через USB
   - Разрешите отладку по USB на устройстве

## Тестирование

### Модульные тесты
```bash
./gradlew test
```

### Инструментальные тесты
```bash
./gradlew connectedAndroidTest
```

### Ручное тестирование

1. **Основной функционал:**
   - Введите текст на русском языке
   - Нажмите "Перевести"
   - Проверьте корректность перевода, транскрипции и примера

2. **Смена языков:**
   - Нажмите кнопку смены языков
   - Введите текст на английском
   - Проверьте перевод на русский

3. **История и избранное:**
   - Выполните несколько переводов
   - Перейдите на вкладку "История"
   - Добавьте переводы в избранное
   - Проверьте вкладку "Избранное"

## Оптимизация и производительность

### Рекомендации по оптимизации

1. **Сетевые запросы:**
   - Используется кэширование переводов в локальной базе данных
   - Таймауты настроены на 10 секунд для стабильности

2. **База данных:**
   - Room обеспечивает эффективную работу с SQLite
   - Индексы настроены для быстрого поиска

3. **UI/UX:**
   - Использование ViewBinding для безопасной работы с View
   - LiveData для реактивного обновления UI
   - RecyclerView с DiffUtil для эффективного обновления списков

### Совместимость

- **Минимальная версия:** Android 7.0 (API 24) - 95%+ устройств
- **Целевая версия:** Android 14 (API 34) - последние возможности
- **Архитектуры:** ARM64, ARM32, x86, x86_64
- **Размеры экранов:** От 4" до планшетов
- **Ориентация:** Portrait и Landscape

## API и зависимости

### Основные зависимости

```gradle
// UI и Material Design
implementation 'com.google.android.material:material:1.11.0'
implementation 'androidx.constraintlayout:constraintlayout:2.1.4'

// Architecture Components
implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0'
implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.7.0'

// Room Database
implementation 'androidx.room:room-runtime:2.6.1'
implementation 'androidx.room:room-ktx:2.6.1'
kapt 'androidx.room:room-compiler:2.6.1'

// Networking
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'

// Coroutines
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3'
```

### API для переводов

Приложение использует **MyMemory Translation API** - бесплатный сервис для переводов:
- **URL:** `https://api.mymemory.translated.net/get`
- **Лимиты:** 1000 запросов в день (бесплатно)
- **Поддерживаемые языки:** Русский (ru) ↔ Английский (en)

## Устранение неполадок

### Частые проблемы

1. **Ошибка сборки Gradle:**
   ```bash
   # Очистка проекта
   ./gradlew clean
   ./gradlew build
   ```

2. **Проблемы с зависимостями:**
   - Проверьте подключение к интернету
   - Обновите Android Studio до последней версии
   - Синхронизируйте проект: File → Sync Project with Gradle Files

3. **Ошибки API переводов:**
   - Проверьте подключение к интернету на устройстве
   - API может быть временно недоступен
   - Переводы сохраняются в истории для офлайн доступа

4. **Проблемы с базой данных:**
   - Очистите данные приложения в настройках устройства
   - Переустановите приложение

### Логирование

Для отладки включите логирование в Android Studio:
```
View → Tool Windows → Logcat
Filter: com.translator.app
```

## Лицензия

Этот проект создан в образовательных целях. Все права защищены.

## Контакты

Для вопросов и предложений по улучшению приложения обращайтесь к разработчику.

