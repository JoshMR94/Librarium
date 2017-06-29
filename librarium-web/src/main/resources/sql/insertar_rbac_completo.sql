/* rol */
INSERT INTO public.rol (id, descripcion) VALUES (0, 'Administrador');
INSERT INTO public.rol (id, descripcion) VALUES (1, 'Agente_archivo_radiologia');
INSERT INTO public.rol (id, descripcion) VALUES (2, 'Agente_radiologia');
INSERT INTO public.rol (id, descripcion) VALUES (3, 'Usuario');

/* usuario */ 
/* password admin -> 'pass_admin' */
INSERT INTO public.usuario(id, baja, descripcion, enabled, password, tipousuario, token, username)
	VALUES (1, false, 'descripcion_admin', true, 'X58NThKwx2H2u5pwbwv9icls3TEKYCLWjF/LJ/vemXg=', 0, null, 'usuario_admin');
/* password usuario -> 'pass_usuario' */
INSERT INTO public.usuario(id, baja, descripcion, enabled, password, tipousuario, token, username)
        VALUES (2, false, 'descripcion_usuario', true, 'iCT81zCzR7im0jlqTSirlE5rBncZSU4ZsuqZPw5cpIw=', 3, null, 'usuario');

/* usuario_rol */ 
INSERT INTO usuario_rol (usuario_id, roles_id) VALUES (1, 0);
INSERT INTO usuario_rol (usuario_id, roles_id) VALUES (2, 3);

/* recurso */
INSERT INTO public.recurso(id, descripcion) VALUES (1, '/lista-solicitudes');
INSERT INTO public.recurso(id, descripcion) VALUES (2, '/lista-solicitudes/[0-9]+?');
INSERT INTO public.recurso(id, descripcion) VALUES (3, '/lista-solicitudes/[0-9]+?/actualizar-estado');
INSERT INTO public.recurso(id, descripcion) VALUES (4, '/lista-solicitudes/[0-9]+?/actualizar-gestion');
INSERT INTO public.recurso(id, descripcion) VALUES (5, '/consultar-solicitud/[0-9]+?');
INSERT INTO public.recurso(id, descripcion) VALUES (6, '/crear-solicitud');
INSERT INTO public.recurso(id, descripcion) VALUES (7, '/usuarios');
INSERT INTO public.recurso(id, descripcion) VALUES (8, '/usuarios/crear-usuario');
INSERT INTO public.recurso(id, descripcion) VALUES (9, '/lista-solicitudes/[0-9]+?/borrar-solicitud');
INSERT INTO public.recurso(id, descripcion) VALUES (10, '/usuarios/[0-9]+?/borrar-usuario');
INSERT INTO public.recurso(id, descripcion) VALUES (11, '/usuarios/[0-9]+?');
INSERT INTO public.recurso(id, descripcion) VALUES (12, '/geopolitica/comunidades-autonomas');
INSERT INTO public.recurso(id, descripcion) VALUES (13, '/geopolitica/provincias/[0-9]+?');
INSERT INTO public.recurso(id, descripcion) VALUES (14, '/geopolitica/islas/[0-9]+?');
INSERT INTO public.recurso(id, descripcion) VALUES (15, '/geopolitica/municipios/[0-9]+?/[0-9]+?');
INSERT INTO public.recurso(id, descripcion) VALUES (16, '/geopolitica/municipios/[0-9]+?');
INSERT INTO public.recurso(id, descripcion) VALUES (17, '/usuarios/[0-9]+?/actualizar-usuario');
INSERT INTO public.recurso(id, descripcion) VALUES (18, '/email/[0-9]+?/[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,3})');
INSERT INTO public.recurso(id, descripcion) VALUES (19, '/usuarios/[0-9]+?/actualizar-password');

/* operacion */
INSERT INTO public.operacion(operacion, recurso_id) VALUES('GET', 1);
INSERT INTO public.operacion(operacion, recurso_id) VALUES('GET', 2);
INSERT INTO public.operacion(operacion, recurso_id) VALUES('GET', 5);
INSERT INTO public.operacion(operacion, recurso_id) VALUES('GET', 7);
INSERT INTO public.operacion(operacion, recurso_id) VALUES('HEAD', 1);
INSERT INTO public.operacion(operacion, recurso_id) VALUES('HEAD', 7);
INSERT INTO public.operacion(operacion, recurso_id) VALUES('POST', 6);
INSERT INTO public.operacion(operacion, recurso_id) VALUES('POST', 8);
INSERT INTO public.operacion(operacion, recurso_id) VALUES('PUT', 3);
INSERT INTO public.operacion(operacion, recurso_id) VALUES('PUT', 4);
INSERT INTO public.operacion(operacion, recurso_id) VALUES('DELETE', 9);
INSERT INTO public.operacion(operacion, recurso_id) VALUES('DELETE', 10);
INSERT INTO public.operacion(operacion, recurso_id) VALUES('GET', 11);
INSERT INTO public.operacion(operacion, recurso_id) VALUES('GET', 12);
INSERT INTO public.operacion(operacion, recurso_id) VALUES('GET', 13);
INSERT INTO public.operacion(operacion, recurso_id) VALUES('GET', 14);
INSERT INTO public.operacion(operacion, recurso_id) VALUES('GET', 15);
INSERT INTO public.operacion(operacion, recurso_id) VALUES('GET', 16);
INSERT INTO public.operacion(operacion, recurso_id) VALUES('PUT', 17);
INSERT INTO public.operacion(operacion, recurso_id) VALUES('POST', 18);
INSERT INTO public.operacion(operacion, recurso_id) VALUES('PUT', 19);

/* permiso */
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (1,0);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (1,1);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (1,2);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (2,0);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (2,1);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (2,2);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (3,0);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (3,1);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (3,2);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (4,0);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (5,0);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (5,1);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (5,2);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (6,0);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (7,0);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (7,1);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (7,2);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (8,0);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (9,0);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (9,1);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (9,2);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (10,0);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (10,1);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (11,0);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (11,1);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (12,0);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (13,0);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (14,0);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (14,1);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (14,2);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (15,0);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (15,1);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (15,2);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (16,0);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (16,1);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (16,2);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (17,0);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (17,1);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (17,2);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (18,0);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (18,1);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (18,2);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (19,0);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (3,3);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (7,3);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (14,3);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (15,3);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (16,3);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (17,3);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (18,3);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (20,3);
INSERT INTO public.permiso(operacion_id, rol_id) VALUES (21,0);