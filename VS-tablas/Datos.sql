-- 1. Categorías de Proyectos
INSERT INTO CategoriaProyecto (idCategoriaProyecto, Nombre, Descripcion) 
VALUES ('CAT-01', 'Software', 'Desarrollo de Software y Apps');

INSERT INTO CategoriaProyecto (idCategoriaProyecto, Nombre, Descripcion) 
VALUES ('CAT-02', 'Hardware', 'Robótica y Electrónica');

-- 2. Instituto (Con el ID de Saltillo)
INSERT INTO Instituto (idInstituto, Municipio_IdMunicipio, Nombre) 
VALUES ('ITS', 'SLW', 'Tecnológico de Saltillo');

-- 3. Carreras
INSERT INTO Carreras (idCarreras, idInstituto, Nombre) 
VALUES ('ISC', 'ITS', 'Ing. Sistemas Computacionales');

INSERT INTO Carreras (idCarreras, idInstituto, Nombre) 
VALUES ('IND', 'ITS', 'Ing. Industrial');

-- 4. Empleados (Maestros o Asesores)
INSERT INTO Empleado (NumEmpleado, idInstituto, Telefono, Nombre, Paterno, Materno, Correo, RFC) 
VALUES (1001, 'ITS', '8441234567', 'Roberto', 'Gomez', 'Perez', 'roberto@tec.mx', 'GOPR800101XYZ');

INSERT INTO Empleado (NumEmpleado, idInstituto, Telefono, Nombre, Paterno, Materno, Correo, RFC) 
VALUES (1002, 'ITS', '8447654321', 'Maria', 'Lopez', 'Diaz', 'maria@tec.mx', 'LODM850101ABC');

-- 5. Persona Externa (Para probar jueces externos)
INSERT INTO PersonaExterna (CURP, Nombre, paterno, Materno, Numero, Correo) 
VALUES ('EXT990101XYZ12345', 'Carlos', 'Ruiz', 'Soto', '8449998877', 'carlos@empresa.com');

COMMIT;