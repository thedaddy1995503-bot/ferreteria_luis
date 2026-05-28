-- Comandos para actualizar la base de datos de FerreteriaLuis
-- Los nombres de las columnas en Java coinciden con estos: pagado y entregado.
-- EJECUTE ESTO EN SU CONSOLA DE MYSQL O MYSQL WORKBENCH:

ALTER TABLE ventas ADD COLUMN pagado BOOLEAN DEFAULT TRUE;
ALTER TABLE ventas ADD COLUMN entregado BOOLEAN DEFAULT TRUE;

ALTER TABLE detalle_venta ADD COLUMN pagado BOOLEAN DEFAULT TRUE;
ALTER TABLE detalle_venta ADD COLUMN entregado BOOLEAN DEFAULT TRUE;

-- Luego de ejecutar, reinicie su servidor GlassFish/Payara.
