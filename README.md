# Sprint 4 — UI автотесты для сервиса аренды самокатов

Учебный проект по написанию UI-автотестов для веб-приложения **Яндекс Самокат**.

## Технологии

| Компонент      | Версия / Стек              |
|----------------|----------------------------|
| Java           | 11                         |
| Maven          | 3.x                        |
| JUnit          | 4.13.2                     |
| Selenium       | 4.x                        |
| WebDriver      | Chrome / Firefox           |

## Структура проекта

- `src/test/java/ru/yandex/praktikum/scooter/tests`
    - `BaseTest` — базовая фикстура для тестов.
    - `FaqAccordionTest` — проверки текста ответов в блоке FAQ.
    - `OrderScooterTest` — позитивный сценарий оформления заказа.
- `src/main/java/ru/yandex/praktikum/scooter/pages`
    - Page Object-классы: `MainPage`, `OrderPageOne`, `OrderPageTwo`.

## Запуск тестов

### Через Maven

```bash
mvn clean test.