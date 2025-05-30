document.querySelectorAll('.lab-card').forEach(card => {
	card.addEventListener('mousemove', e => {
	  const w = card.offsetWidth;
	  const h = card.offsetHeight;
	  const x = (e.layerX - w/2) / (w/2); // от -1 до 1
	  const y = (e.layerY - h/2) / (h/2);
	  // повороты в градусах, по X и Y
	  const rx = (-y * 5).toFixed(2) + 'deg';
	  const ry = ( x * 5).toFixed(2) + 'deg';
	  card.style.setProperty('--rx', rx);
	  card.style.setProperty('--ry', ry);
	  card.setAttribute('data-rx', '');
	  card.setAttribute('data-ry', '');
	});
	card.addEventListener('mouseleave', () => {
	  card.style.setProperty('--rx', '0deg');
	  card.style.setProperty('--ry', '0deg');
	});
  });
  