document.addEventListener('DOMContentLoaded', function() {
    // Анимация текста
    anime({
        targets: ['.hero-title', '.hero-subtitle', '.hero-button'],
        opacity: [0, 1],
        translateY: [20, 0],
        duration: 1500,
        delay: anime.stagger(300),
        easing: 'easeOutExpo'
    });

    // Создание алгоритмической анимации
    const container = document.getElementById('animation-container');
    const colors = ['#fbb034', '#ffdd59', '#ffffff', '#b8b8d0'];

    // 1. Анимация сортировки (пузырьковая сортировка)
    function createSortingAnimation() {
        const values = [5, 2, 8, 1, 9, 3, 7, 4, 6];
        const elements = [];

        // Создаем элементы массива
        for (let i = 0; i < values.length; i++) {
            const elem = document.createElement('div');
            elem.className = 'algorithm-node';
            elem.style.width = '30px';
            elem.style.height = '30px';
            elem.style.display = 'flex';
            elem.style.justifyContent = 'center';
            elem.style.alignItems = 'center';
            elem.style.color = '#1a1a2e';
            elem.style.fontWeight = 'bold';
            elem.style.fontSize = '12px';
            elem.textContent = values[i];
            elem.style.left = `${10 + i * 40}px`;
            elem.style.bottom = '100px';
            container.appendChild(elem);
            elements.push(elem);
        }

        // Анимация сортировки
        async function bubbleSort() {
            let swapped;
            do {
                swapped = false;
                for (let i = 0; i < elements.length - 1; i++) {
                    const current = parseInt(elements[i].textContent);
                    const next = parseInt(elements[i + 1].textContent);

                    if (current > next) {
                        // Подсветка сравниваемых элементов
                        anime({
                            targets: [elements[i], elements[i + 1]],
                            backgroundColor: '#ff5e5e',
                            duration: 300,
                            easing: 'easeInOutQuad'
                        });

                        await new Promise(resolve => {
                            anime({
                                targets: elements[i],
                                translateX: 40,
                                duration: 800,
                                easing: 'easeInOutQuad',
                                complete: resolve
                            });

                            anime({
                                targets: elements[i + 1],
                                translateX: -40,
                                duration: 800,
                                easing: 'easeInOutQuad'
                            });
                        });

                        // Обмен значениями
                        container.insertBefore(elements[i + 1], elements[i]);
                        [elements[i], elements[i + 1]] = [elements[i + 1], elements[i]];

                        // Возвращаем цвет
                        anime({
                            targets: [elements[i], elements[i + 1]],
                            backgroundColor: '#b8b8d0',  // Исправлено на актуальный цвет
                            duration: 300,
                            delay: 200,
                            easing: 'easeInOutQuad'
                        });

                        swapped = true;
                    } else {
                        // Подсветка без обмена
                        await new Promise(resolve => {
                            anime({
                                targets: [elements[i], elements[i + 1]],
                                backgroundColor: '#239423',
                                duration: 300,
                                easing: 'easeInOutQuad',
                                complete: resolve
                            });

                            anime({
                                targets: [elements[i], elements[i + 1]],
                                backgroundColor: '#b8b8d0',  // Исправлено на актуальный цвет
                                duration: 300,
                                delay: 500,
                                easing: 'easeInOutQuad'
                            });
                        });
                    }
                }
            } while (swapped);
        }

        bubbleSort();
    }

    // 2. Анимация графа
    function createGraphAnimation() {
        const nodes = [];
        const centerX = window.innerWidth / 2;
        const centerY = window.innerHeight / 2;
        const radius = Math.min(window.innerWidth, window.innerHeight) * 0.3;

        // Создаем узлы графа
        for (let i = 0; i < 8; i++) {
            const angle = (i / 8) * Math.PI * 2;
            const x = centerX + Math.cos(angle) * radius;
            const y = centerY + Math.sin(angle) * radius;

            const node = document.createElement('div');
            node.className = 'algorithm-node';
            node.style.left = `${x}px`;
            node.style.top = `${y}px`;
            container.appendChild(node);
            nodes.push({element: node, x, y});

            // Анимация появления узлов
            anime({
                targets: node,
                scale: [0, 1],
                opacity: [0, 1],
                duration: 1000,
                delay: i * 100,
                easing: 'easeOutElastic'
            });
        }

        // Создаем связи между узлами
        for (let i = 0; i < nodes.length; i++) {
            for (let j = i + 1; j < nodes.length; j++) {
                if (Math.random() > 0.6) continue; // Не все узлы соединяем

                const line = document.createElement('div');
                line.className = 'algorithm-line';
                const x1 = nodes[i].x;
                const y1 = nodes[i].y;
                const x2 = nodes[j].x;
                const y2 = nodes[j].y;

                const length = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
                const angle = Math.atan2(y2 - y1, x2 - x1);

                line.style.width = `${length}px`;
                line.style.left = `${x1}px`;
                line.style.top = `${y1}px`;
                line.style.transform = `rotate(${angle}rad)`;
                line.style.opacity = '0';
                container.appendChild(line);

                // Анимация появления линии
                anime({
                    targets: line,
                    opacity: [0, 0.7],
                    duration: 1500,
                    delay: (i + j) * 50,
                    easing: 'easeOutQuad'
                });

                // Анимация "пульсации" по линии
                const pulse = document.createElement('div');
                pulse.className = 'algorithm-node';
                pulse.style.width = '6px';
                pulse.style.height = '6px';
                pulse.style.left = `${x1}px`;
                pulse.style.top = `${y1}px`;
                container.appendChild(pulse);

                anime({
                    targets: pulse,
                    left: `${x2}px`,
                    top: `${y2}px`,
                    duration: 2000,
                    delay: (i + j) * 50 + 500,
                    easing: 'linear',
                    complete: () => pulse.remove()
                });
            }
        }
    }

    // 3. Летающие элементы кода
    function createCodeElements() {
        const codeSnippets = [
            'function sort() {',
            'return a * x ** 3 + b * x ** 2',
            'const node = new Node(value)',
            'while (left <= right) {',
            'hashTable.put(key, value)',
            'graph.addEdge(v1, v2)',
            'tree.balance()',
            'O(n log n)'
        ];

        for (let i = 0; i < 15; i++) {
            const code = document.createElement('div');
            code.className = 'algorithm-code';
            code.textContent = codeSnippets[Math.floor(Math.random() * codeSnippets.length)];
            code.style.left = `${Math.random() * 100}vw`;
            code.style.top = `${Math.random() * 100}vh`;
            code.style.opacity = '0';
            container.appendChild(code);

            // Анимация появления и движения
            anime({
                targets: code,
                opacity: [0, 0.7, 0],
                translateX: `${anime.random(-100, 100)}px`,
                translateY: `${anime.random(-100, 100)}px`,
                duration: anime.random(5000, 15000),
                delay: anime.random(0, 3000),
                easing: 'linear',
                loop: true,
                direction: 'alternate'
            });
        }
    }

    // Запуск всех анимаций
    createSortingAnimation();
    createGraphAnimation();
    createCodeElements();

    // Фоновые круги
    for (let i = 0; i < 10; i++) {
        const circle = document.createElement('div');
        circle.className = 'algorithm-circle';
        circle.style.width = `${anime.random(100, 500)}px`;
        circle.style.height = circle.style.width;
        circle.style.left = `${anime.random(-100, 100)}%`;
        circle.style.top = `${anime.random(-100, 100)}%`;
        circle.style.opacity = '0';
        container.appendChild(circle);

        anime({
            targets: circle,
            opacity: [0, anime.random(0.1, 0.3)],
            duration: 3000,
            delay: i * 300,
            easing: 'easeInOutQuad'
        });

        anime({
            targets: circle,
            translateX: `${anime.random(-100, 100)}px`,
            translateY: `${anime.random(-100, 100)}px`,
            duration: anime.random(10000, 20000),
            delay: i * 300,
            easing: 'linear',
            loop: true,
            direction: 'alternate'
        });
    }
});
