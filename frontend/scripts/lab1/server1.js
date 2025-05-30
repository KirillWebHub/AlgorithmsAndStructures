// Flash messages for success and error
function showFlashMessage(message, isError) {
	const flash = document.createElement('div');
	flash.className = isError ? 'flash-error' : 'flash-success';
	flash.textContent = message;
	document.body.prepend(flash);
	setTimeout(() => flash.remove(), 3000);
  }
  
  // Основная логика после загрузки страницы
  document.addEventListener('DOMContentLoaded', () => {
	const modeRadios = document.querySelectorAll('input[name="mode"]');
	const fileInputContainer = document.getElementById('file-input');
	const fileInput = document.getElementById('input-file');
	const runButton = document.getElementById('run');
	const outputConsole = document.getElementById('output-console');
  
	// Переключение между статическим массивом и загрузкой файла
	modeRadios.forEach(radio => {
	  radio.addEventListener('change', () => {
		fileInputContainer.style.display = (radio.value === 'file' && radio.checked) ? 'block' : 'none';
	  });
	});
  
	// Обработка нажатия на кнопку сортировки
	runButton.addEventListener('click', async () => {
	  const mode = document.querySelector('input[name="mode"]:checked').value;
	  let numbers = [];
  
	  // Получаем массив в зависимости от режима
	  if (mode === 'static') {
		numbers = [5, 3, 1, 4]; // пример статического массива
	  } else {
		if (!fileInput.files.length) {
		  showFlashMessage('Файл не выбран', true);
		  return;
		}
		try {
		  const text = await fileInput.files[0].text();
		  const content = text.trim();
		  if (!content) {
			showFlashMessage('Файл пустой', true);
			return;
		  }
		  numbers = content.split(/\s+/).map(n => {
			const num = Number(n);
			const INT_MIN = -2147483648;
			const INT_MAX = 2147483647;
			if (
			  isNaN(num) ||
			  !isFinite(num) ||
			  !Number.isInteger(num) ||
			  num < INT_MIN ||
			  num > INT_MAX
			) {
			  throw new Error(`Некорректное значение: "${n}"`);
			}
			return num;
		  });
		} catch (err) {
		  showFlashMessage(err.message, true);
		  return;
		}
	  }
  
	  // Очищаем консоль вывода
	  outputConsole.textContent = '';
  
	  // Отправка запроса на сервер
	  try {
		const response = await fetch('/api/heap-sort', {
		  method: 'POST',
		  headers: { 'Content-Type': 'application/json' },
		  body: JSON.stringify(numbers)
		});
		if (!response.ok) {
		  throw new Error(`Сервер вернул статус ${response.status}`);
		}
		const data = await response.json();
		const sortedArray = Array.isArray(data)
		? data : (data.sortedArray || [])
  
		// Вывод результатов
		outputConsole.textContent =
		  `Исходный: [${numbers.join(', ')}]\n` +
		  `Отсортированный: [${sortedArray.join(', ')}]`;
		showFlashMessage('Сортировка выполнена успешно!', false);
	  } catch (err) {
		console.error(err);
		showFlashMessage('Ошибка при отправке на сервер: ' + err.message, true);
	  }
	});
  });
  