# tesis

Version conceptual 0.01


# Proyecto java
Features :
	V0.01
* Splash Screen (✓) 
* diseño Login  (✓) 
* programacion Login (✓) 
* diseño Home (✓)
* libreria personal creada placeholder(✓)
* diseño sistema de reportes(✓)
* modelamiento base de datos(✓)
* programacion base de datos(✓)

# v 0.05
* programacion home (✓)
* encriptacion md5 contraseñas(✓) (Falta desencriptar md5 para editar contraseña)
* diseño de los crud(✓)
	* Usuarios(✓) 
	* Instituciones(✓) 
	* Sectores(✓) 
	* comunas(✓) 
	* Delincuentes(✓) 
	* Delincuentes parentesco (✓) 
	* Delitos(✓) 
	* Controles(✓) 
	
* Programacion sistema de generacion de reportes (✓)(✓) 	
	* Mostrar delincuentes orden Alfabetico (✓) 
	* listado de delincuentes agrupado por delito cometido (✓) 
	* mostrar delincuentes por comuna de residencia (✓)
	* mostrar delincuentes por ultimo lugar visto  (✓)  
	* mostrar delincuentes por parentezco entre ellos (✓) 
	* listado de delitos ocurrido por comuna o sector dentro de un rango de fechas (✓)  
	* Listado histórico de delitos ocurridos por sectores (✓) 
	* Búsqueda en cualquier campo (✓) 
	* Mostrar ranking de comunas con mayor cantidad de delitos dentro de un rango de fechas (✓)(✓) 
	* Mostrar ranking de sectores con mayor cantidad de delitos dentro de un rango de fechas(✓) (✓) 
	
* Validadores rut (✓)
* validadores campos en blancos(✓)
	
# Idea general del proyecto

Crear una aplicacion en java con todos los requisitos que pide el proyecto.

# lenguajes utilizados
* java

# base de datos
* Mysql

# Librerias utilizadas
* Placeholder : Libreria propia
* MYSQL - JDBC
* Commons-codec-1.14 : PAra encriptar las contraseñas MD5
* JXL : Libreria para generar archivos excel 
* JCalendar : Libreria para utilizar un calendario
* JFreeCharts: Libreria para generar graficos en java
* IText: libreria para generar archivos pdf




# anotaciones
Rol de operador: Solo puede ingresar delitos, controles y parentescos pertenecientes a su comuna
rol de jefe de zona: puede  editar, eliminar, y agregar delincuentes, delitos y comunas. Pertenecientes a su zona
Administrador General: Puede hacer de todo
