// server4.js — Поиск Кнута-Морриса-Пратта + Красно-чёрное дерево

function showFlashMessage(message, isError) {
    const flash = document.createElement('div');
    flash.className = isError ? 'flash-error' : 'flash-success';
    flash.textContent = message;
    document.body.prepend(flash);
    setTimeout(() => flash.remove(), 3000);
}

// Возвращает URL сервера с учётом IP/хоста
const HOST = `${location.protocol}//${location.hostname}:8080`;

document.addEventListener('DOMContentLoaded', () => {
    const fileInput = document.getElementById('input-file');
    const textInput = document.getElementById('text-input');
    const patternInput = document.getElementById('pattern-input');
    const runButton = document.getElementById('run');
    const outputConsole = document.getElementById('output-console');

    runButton.addEventListener('click', async () => {
        let text = textInput.value.trim();
        let pattern = patternInput.value.trim();

        // Если выбран файл — читаем из него
        if (fileInput.files.length) {
            try {
                const fileContent = await fileInput.files[0].text();
                const lines = fileContent.trim().split('\n');
                if (lines.length < 2) throw new Error('Файл должен содержать 2 строки: текст и шаблон');
                text = lines[0].trim();
                pattern = lines[1].trim();
            } catch (err) {
                showFlashMessage(`Ошибка чтения файла: ${err.message}`, true);
                return;
            }
        }

        if (!text || !pattern) {
            showFlashMessage('Введите текст и шаблон для поиска', true);
            return;
        }

        outputConsole.textContent = 'Выполняю поиск...';

        const payload = { text, pattern };

        try {
            const res = await fetch(`${HOST}/api/kmp`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (!res.ok) {
                throw new Error(`HTTP ${res.status}`);
            }

            const result = await res.json();
            showResults(payload, result);
        } catch (err) {
            showFlashMessage(`Ошибка запроса: ${err.message}`, true);
            outputConsole.textContent = 'Ошибка при выполнении запроса';
        }
    });

    function showResults(input, result) {
        let output = `Текст: "${input.text}"\n`;
        output += `Шаблон: "${input.pattern}"\n\n`;

        if (result.matches.length > 0) {
            output += `Найдено на позициях: ${result.matches.join(', ')}\n\n`;
            output += 'Красно-чёрное дерево (in-order):\n';
            output += result.tree.replaceAll('\\n', '\n');
            showFlashMessage(`Найдено ${result.matches.length} совпадений`, false);
        } else {
            output += 'Совпадений не найдено.';
            showFlashMessage('Шаблон не найден', true);
        }

        outputConsole.textContent = output;
    }
});
