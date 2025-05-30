// server2.js — логика для лабораторной работы №2 (HeapSort через Deque) с flash-сообщениями

// Функция для показа флеш-сообщений
function showFlashMessage(message, isError) {
    const flash = document.createElement('div');
    flash.className = isError ? 'flash-error' : 'flash-success';
    flash.textContent = message;
    document.body.prepend(flash);
    setTimeout(() => flash.remove(), 3000);
  }
  
  // Основная логика после загрузки DOM
  document.addEventListener('DOMContentLoaded', () => {
    const fileInput = document.getElementById('input-file');
    const runButton = document.getElementById('run');
    const outputConsole = document.getElementById('output-console');
  
    runButton.addEventListener('click', async () => {
      // Проверка выбора файла
      if (!fileInput.files.length) {
        showFlashMessage('Файл не выбран', true);
        return;
      }
  
      let numbers;
      try {
        // Считываем содержимое файла
        const text = await fileInput.files[0].text();
        const content = text.trim();
        if (!content) {
          showFlashMessage('Файл пустой', true);
          return;
        }
        // Преобразуем в массив чисел
        numbers = content.split(/\s+/).map(str => {
          const num = Number(str);
          const INT_MIN = -2147483648, INT_MAX = 2147483647;
          if (
            isNaN(num) ||
            !Number.isInteger(num) ||
            num < INT_MIN ||
            num > INT_MAX
          ) {
            throw new Error(`Некорректное значение: "${str}"`);
          }
          return num;
        });
      } catch (err) {
        showFlashMessage(err.message, true);
        return;
      }
  
      // Очищаем вывод
      outputConsole.textContent = '';
  
      try {
        // Отправляем запрос на сервер
        const response = await fetch('/api/heap-sort', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ data: numbers, structure: 'deque' })
        });
        if (!response.ok) {
          throw new Error(`Сервер вернул ${response.status}`);
        }
        const data = await response.json();
  
        // Формируем вывод
        let resultHtml = `Исходный массив: [${numbers.join(', ')}]\n` +
                         `Отсортированный массив: [${data.sortedArray.join(', ')}]\n\n` +
                         `Метрики сложности:\n` +
                         `• Сравнения: ${data.comparisons}\n` +
                         `• Обмены: ${data.swaps}\n` +
                         `• Вызовы heapify: ${data.heapifyCalls}\n` +
                         `• Время: ${(data.timeNanos/1e6).toFixed(2)} мс`;
  
        outputConsole.textContent = resultHtml;
        showFlashMessage('Сортировка выполнена успешно!', false);
      } catch (err) {
        console.error(err);
        showFlashMessage('Ошибка при запросе: ' + err.message, true);
      }
    });
  });
  