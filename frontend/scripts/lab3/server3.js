// server3.js — поддержка обоих вариантов ввода

function showFlashMessage(message, isError) {
    const flash = document.createElement('div');
    flash.className = isError ? 'flash-error' : 'flash-success';
    flash.textContent = message;
    document.body.prepend(flash);
    setTimeout(() => flash.remove(), 3000);
}

function parseInput(text) {
    const lines = text.trim().split('\n');
    if (lines.length < 2) throw new Error('Файл должен содержать 2 строки: массив и шаблон');
    
    return {
        array: lines[0].trim().split(/\s+/).map(Number),
        pattern: lines[1].trim().split(/\s+/).map(Number)
    };
}

document.addEventListener('DOMContentLoaded', () => {
    const fileInput = document.getElementById('input-file');
    const arrayInput = document.getElementById('array-input');
    const patternInput = document.getElementById('pattern-input');
    const runButton = document.getElementById('run');
    const outputConsole = document.getElementById('output-console');

    runButton.addEventListener('click', async () => {
        let data;
        
        // Приоритет у файла, если он загружен
        if (fileInput.files.length) {
            try {
                const text = await fileInput.files[0].text();
                data = parseInput(text);
            } catch (err) {
                showFlashMessage(`Ошибка файла: ${err.message}`, true);
                return;
            }
        } else {
            // Иначе берём данные из инпутов
            try {
                data = {
                    array: arrayInput.value.trim().split(/\s+/).map(Number),
                    pattern: patternInput.value.trim().split(/\s+/).map(Number)
                };
                if (data.array.some(isNaN) || data.pattern.some(isNaN)) {
                    throw new Error('Только числа, разделённые пробелами');
                }
            } catch (err) {
                showFlashMessage(`Ошибка ввода: ${err.message}`, true);
                return;
            }
        }

        // Проверка данных
        if (data.pattern.length === 0) {
            showFlashMessage('Шаблон не может быть пустым', true);
            return;
        }

        outputConsole.textContent = 'Выполняю поиск...';
        
        try {
            const response = await fetch('/api/boyer-moore', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });

            if (!response.ok) throw new Error(`HTTP ${response.status}`);
            
            const result = await response.json();
            showResults(data, result);
            
        } catch (err) {
            showFlashMessage(`Ошибка: ${err.message}`, true);
            outputConsole.textContent = 'Ошибка при выполнении запроса';
        }
    });

    function showResults(input, result) {
        let output = `Массив: [${input.array.join(', ')}]\n`;
        output += `Шаблон: [${input.pattern.join(', ')}]\n\n`;
        
        if (result.foundIndices.length > 0) {
            output += `Найдено на позициях: ${result.foundIndices.join(', ')}\n`;
            result.foundIndices.forEach(index => {
                output += `→ С ${index} по ${index + input.pattern.length - 1}: `;
                output += `[${input.array.slice(index, index + input.pattern.length).join(', ')}]\n`;
            });
            showFlashMessage(`Найдено ${result.foundIndices.length} вхождений`, false);
        } else {
            output += `Совпадений не найдено`;
            showFlashMessage('Шаблон не найден', true);
        }
        
        outputConsole.textContent = output;
    }
});