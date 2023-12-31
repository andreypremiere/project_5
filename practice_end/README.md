## Несколько уточняющих моментов

- 1.gif и 2.gif показывают работу приложения на эмуляторе и уже установленного приложения на смартфоне.
- В папке `Word` содержится весь проект, разработанный в Android Studio. [Android Studio](https://developer.android.com/studio) доступен для установки по ссылке.
- В папке `apk` находится установщик приложения. Чтобы загрузить приложение, скачайте папку на свой Android-телефон и запустите установщик `app-debug.apk`. После установки приложения его можно будет запустить.

### О приложении

В приложении реализована только малая часть того, что должно было быть, а именно, повторение иностранных слов, загруженных в базу данных, и добавление своих слов, но этого было достаточно, чтобы применить некоторые паттерны проектирования.

Вот несколько паттернов, использованных в работе:


1. **Шаблонный метод (Template Method)**:
   - Методы `onCreate`, `onPause`, `onResume`, `onDestroy` - это шаблонные методы жизненного цикла активности во встроенном классе android studio `AppCompatActivity`. На диаграмме показана часть, где они используются. Они переопределены в классах `MainActivity` и `AddWordActivity`, добавляя свою логику к стандартному жизненному циклу активности. 
   - `SQLiteOpenHelper` предоставляет скелет для управления базой данных (этот класс является встроенным), но конкретная реализация этих методов остается на уровне дочернего класса `DatabaseHelper`. Использование этого шаблона позволяет стандартизировать структуру создания (`onCreate`) и обновления базы данных (`onUpgrade`) в рамках Android-приложения, предоставляя точки расширения для собственных настроек и логики в `DatabaseHelper`.

### Шаблонный метод

![Шаблонный метод](template.png)


2. **Фасад (Facade)**:
   - `DatabaseHelper` действует как фасад, предоставляющий удобный интерфейс для работы с базой данных. Он скрывает сложности взаимодействия с базой данных, предоставляя простые методы, такие как `insertData` (добавление), `getAllWords` (получение всех слов) и другие, скрывая подробности реализации. На диаграмме классов показана часть, где этот класс используется.

### Фасад

![Паттерн Фасад](facade.png)

#### Паттерн, который я бы не использовал

Абстрактная фабрика (Abstract Factory): В данном приложении не потребовалось создание семейства связанных или зависимых объектов без указания их конкретных классов на данном этапе разработки.
