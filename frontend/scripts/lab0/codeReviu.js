function runCode() {
	const code = document.getElementById("js-code").innerText;
	const output = document.getElementById("output-console");
	output.textContent = ''; // Очистка
	const originalLog = console.log;
	console.log = msg => output.textContent += msg + '\n';
	try {
		eval(code);
	} catch (e) {
		output.textContent = 'Ошибка: ' + e;
	}
	console.log = originalLog;
}