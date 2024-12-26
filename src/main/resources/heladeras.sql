INSERT INTO heladeras (
    id,
    nombre_heladera,
    capacidad_total,
    peso_actual,
    numero_serie,
    estadoHeladera,
    tiempoExpiracionSolicitudes,
    temperaturaMaxima,
    temperaturaMinima,
    temperatura_actual,
    calle, altura, ciudad, localidad
)
VALUES
    (1, 'Heladera Restaurante Lo de Ramiro', 100.0, 50.0, '12345', 'ACTIVA', 30, 5.0, -5.0, 4.0, 'Av. Asamblea', 1112, 'CABA', 'Parque Chacabuco'),
    (2, 'Heladera Pizzeria Globo', 200.0, 75.0, '54321', 'ACTIVA', 60, 4.0, -4.0, 3.0, 'Av. Caseros', 3015, 'CABA', 'Parque Patricios'),
    (3, 'Heladera Restaurante San Jorge', 150.0, 75.0, '54345', 'ACTIVA', 60, 4.0, -4.0, 3.0, 'Primera Junta', 3448, 'CABA', 'Flores'),
    (4, 'Heladera Club de Barrio Urquiza 1', 150.0, 75.0, '54345', 'ACTIVA', 60, 4.0, -4.0, 3.0, 'Av. Congreso', 5382, 'CABA', 'Villa Urquiza'),
    (5, 'Heladera Club de Barrio Urquiza 2', 150.0, 75.0, '54345', 'ACTIVA', 60, 4.0, -4.0, 3.0, 'Av. Congreso', 5382, 'CABA', 'Villa Urquiza'),
    (6, 'Heladera YPF Warnes', 150.0, 75.0, '54345', 'ACTIVA', 60, 4.0, -4.0, 3.0, 'Av. Warnes', 236,'CABA','Villa Crespo'),
    (7, 'Heladera Axion Gaona', 150.0, 75.0, '54345', 'ACTIVA', 60, 4.0, -4.0, 3.0, 'Av. Gaona', 2399, 'CABA', 'Villa Gral Mitre'),
    (8, 'Heladera ONG Amor Puro 1', 150.0, 75.0, '54345', 'ACTIVA', 60, 4.0, -4.0, 3.0, 'Remedios', 3740, 'CABA', 'Parque Avellaneda'),
    (9, 'Heladera ONG Amor Puro 2', 150.0, 75.0, '54345', 'ACTIVA', 60, 4.0, -4.0, 3.0, 'Remedios', 3740, 'CABA', 'Parque Avellaneda'),
    (10, 'Heladera Restaurante Peralta', 150.0, 75.0, '54345', 'ACTIVA', 60, 4.0, -4.0, 3.0, 'Adolfo Alsina', 2072, 'CABA', 'Balvanera'),
    (11, 'Heladera Restaurante Del Progreso', 150.0, 75.0, '54345', 'ACTIVA', 60, 4.0, -4.0, 3.0, 'Thames', 550, 'CABA', 'Villa Crespo'),
    (12, 'Heladera Restaurante San Lorenzo', 150.0, 75.0, '54345', 'ACTIVA', 60, 4.0, -4.0, 3.0, 'Av. La Plata', 1759, 'CABA', 'Boedo'),
    (13, 'Heladera Restaurante Los Pinos', 150.0, 75.0, '54345', 'ACTIVA', 60, 4.0, -4.0, 3.0, 'Azcuénaga', 1500, 'CABA', 'Recoleta');
