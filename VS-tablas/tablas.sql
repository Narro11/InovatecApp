CREATE TABLE Estado (
    IdEstado VARCHAR(5) NOT NULL,
    Nombre VARCHAR(50) NULL,
    PRIMARY KEY (IdEstado)
);

CREATE TABLE CategoriaProyecto (
    idCategoriaProyecto VARCHAR(10) NOT NULL,
    Nombre VARCHAR(30) NULL,
    Descripcion VARCHAR(30) NULL,
    PRIMARY KEY (idCategoriaProyecto)
);

CREATE TABLE PersonaExterna (
    CURP VARCHAR(20) NOT NULL,
    Nombre VARCHAR(30) NULL,
    Materno VARCHAR(30) NULL,
    Numero VARCHAR(20) NULL,
    Correo VARCHAR(50) NULL,
    PRIMARY KEY (CURP)
);

CREATE TABLE Municipio (
    IdMunicipio VARCHAR(10) NOT NULL,
    IdEstado VARCHAR(5) NOT NULL,
    Nombre VARCHAR(30) NULL,
    PRIMARY KEY (IdMunicipio),

    FOREIGN KEY (IdEstado)
        REFERENCES Estado(IdEstado)
);

CREATE TABLE Instituto (
    idInstituto VARCHAR(10) NOT NULL,
    Municipio_IdMunicipio VARCHAR(10) NOT NULL,
    Nombre VARCHAR(30) NULL,
    PRIMARY KEY (idInstituto),

    FOREIGN KEY (Municipio_IdMunicipio)
        REFERENCES Municipio(IdMunicipio)
);

CREATE TABLE Campus (
    idCampus VARCHAR(10) NOT NULL,
    idInstituto VARCHAR(10) NOT NULL,
    Nombre VARCHAR(50) NULL,
    PRIMARY KEY (idCampus),

    FOREIGN KEY (idInstituto)
        REFERENCES Instituto(idInstituto)
);

CREATE TABLE Carreras (
    idCarreras VARCHAR(10) NOT NULL,
    idInstituto VARCHAR(10) NOT NULL,
    Nombre VARCHAR(50) NULL,
    PRIMARY KEY (idCarreras),

    FOREIGN KEY (idInstituto)
        REFERENCES Instituto(idInstituto)
);

CREATE TABLE Empleado (
    NumEmpleado NUMERIC(10) NOT NULL,
    idInstituto VARCHAR(10) NOT NULL,
    Telefono NUMERIC(10) NULL,
    Nombre VARCHAR(30) NULL,
    Paterno VARCHAR(30) NULL,
    Materno VARCHAR(30) NULL,
    Correo VARCHAR(50) NULL,
    RFC VARCHAR(20) NULL,
    PRIMARY KEY (NumEmpleado),

    FOREIGN KEY (idInstituto)
        REFERENCES Instituto(idInstituto)
);

CREATE TABLE Edicion (
    idEdicion VARCHAR(20) NOT NULL,
    Instituto_idInstituto VARCHAR(10) NOT NULL,
    Descripcion VARCHAR(50) NULL,
    FechaInicio DATE NULL,
    FechaFin DATE NULL,
    Nombre VARCHAR(30) NULL,
    PRIMARY KEY (idEdicion),

    FOREIGN KEY (Instituto_idInstituto)
        REFERENCES Instituto(idInstituto)
);

CREATE TABLE Proyecto (
    idProyecto VARCHAR(10) NOT NULL,
    Edicion_idEdicion VARCHAR(20) NOT NULL,
    idCategoriaProyecto VARCHAR(10) NOT NULL,
    Nombre VARCHAR(10) NULL,
    Descripcion VARCHAR(30) NULL,
    PRIMARY KEY (idProyecto),

    FOREIGN KEY (Edicion_idEdicion)
        REFERENCES Edicion(idEdicion),

    FOREIGN KEY (idCategoriaProyecto)
        REFERENCES CategoriaProyecto(idCategoriaProyecto)
);

CREATE TABLE Patrocinador (
    idEmpresa VARCHAR(30) NOT NULL,
    Edicion_idEdicion VARCHAR(20) NOT NULL,
    Municipio_IdMunicipio VARCHAR(10) NOT NULL,
    Nombre VARCHAR(30) NULL,
    Aportacion VARCHAR(100) NULL,
    PRIMARY KEY (idEmpresa),

    FOREIGN KEY (Municipio_IdMunicipio)
        REFERENCES Municipio(IdMunicipio),

    FOREIGN KEY (Edicion_idEdicion)
        REFERENCES Edicion(idEdicion)
);

CREATE TABLE Juez (
    idJuez VARCHAR(10) NOT NULL,
    PersonaExterna_CURP VARCHAR(20) NULL,
    Edicion_idEdicion VARCHAR(20) NOT NULL,
    Empleado_NumEmpleado NUMERIC(10) NULL,
    PRIMARY KEY (idJuez),

    FOREIGN KEY (Empleado_NumEmpleado)
        REFERENCES Empleado(NumEmpleado),

    FOREIGN KEY (Edicion_idEdicion)
        REFERENCES Edicion(idEdicion),

    FOREIGN KEY (PersonaExterna_CURP)
        REFERENCES PersonaExterna(CURP)
);

CREATE TABLE Equipo (
    idEquipo NUMERIC(10) NOT NULL,
    idProyecto VARCHAR(10) NOT NULL,
    Nombre VARCHAR(50) NULL,
    PRIMARY KEY (idEquipo),

    FOREIGN KEY (idProyecto)
        REFERENCES Proyecto(idProyecto)
);

CREATE TABLE Alumno (
    N_Control NUMERIC(10,0) NOT NULL,
    idInstituto VARCHAR(10) NOT NULL,
    idCarreras VARCHAR(10) NOT NULL,
    Nombre VARCHAR(50) NULL,
    Paterno VARCHAR(50) NULL,
    Materno VARCHAR(50) NULL,
    Telefono NUMERIC(10,0) NULL,
    Correo VARCHAR(50) NULL,

    CONSTRAINT PK_Alumno
        PRIMARY KEY (N_Control),

    CONSTRAINT FK_Alumno_Instituto
        FOREIGN KEY (idInstituto)
        REFERENCES Instituto(idInstituto),

    CONSTRAINT FK_Alumno_Carreras
        FOREIGN KEY (idCarreras)
        REFERENCES Carreras(idCarreras)
);

CREATE TABLE RegistroAsesor (
    NumEmpleado NUMERIC(10) NOT NULL,
    idEquipo NUMERIC(10) NOT NULL,
    PRIMARY KEY (NumEmpleado, idEquipo),

    FOREIGN KEY (NumEmpleado)
        REFERENCES Empleado(NumEmpleado),

    FOREIGN KEY (idEquipo)
        REFERENCES Equipo(idEquipo)
);

CREATE TABLE Staf (
    idStaf VARCHAR(10) NOT NULL,
    N_Control NUMERIC(10,0) NOT NULL,
    Empleado_NumEmpleado NUMERIC(10) NOT NULL,
    Edicion_idEdicion VARCHAR(20) NOT NULL,
    PRIMARY KEY (idStaf),

    FOREIGN KEY (Edicion_idEdicion)
        REFERENCES Edicion(idEdicion),

    FOREIGN KEY (Empleado_NumEmpleado)
        REFERENCES Empleado(NumEmpleado),

    FOREIGN KEY (N_Control)
        REFERENCES Alumno(N_Control)
);

CREATE TABLE Registro (
    idRegistro VARCHAR(20) NOT NULL,
    Edicion_idEdicion VARCHAR(20) NOT NULL,
    idEquipo NUMERIC(10) NOT NULL,
    N_Control NUMERIC(10,0) NOT NULL,
    Rol VARCHAR(30) NULL,
    PRIMARY KEY (idRegistro),

    FOREIGN KEY (Edicion_idEdicion)
        REFERENCES Edicion(idEdicion),

    FOREIGN KEY (idEquipo)
        REFERENCES Equipo(idEquipo),

    FOREIGN KEY (N_Control)
        REFERENCES Alumno(N_Control)
);

CREATE TABLE Usuarios (
    idUsuario VARCHAR(20) NOT NULL,
    nombre VARCHAR(30) NOT NULL,
    paterno VARCHAR(30) NOT NULL,
    materno VARCHAR(30) NOT NULL,
    contraseña VARCHAR(30) NOT NULL,
    PRIMARY KEY (idUsuario)
);