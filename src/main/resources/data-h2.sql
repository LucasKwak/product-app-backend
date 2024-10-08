-- CREACIÓN DE MODULOS
INSERT INTO module (name, base_path) VALUES ('PRODUCT', '/products');
INSERT INTO module (name, base_path) VALUES ('CATEGORY', '/categories');
INSERT INTO module (name, base_path) VALUES ('CUSTOMER', '/customers');
INSERT INTO module (name, base_path) VALUES ('AUTH', '/auth');

-- CREACIÓN DE OPERACIONES
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ALL_PRODUCTS','', 'GET', false, 1);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ONE_PRODUCT','/[0-9]*', 'GET', false, 1);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CREATE_ONE_PRODUCT','', 'POST', false, 1);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('UPDATE_ONE_PRODUCT','/[0-9]*', 'PUT', false, 1);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('DISABLE_ONE_PRODUCT','/[0-9]*/disabled', 'PATCH', false, 1);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ALL_CATEGORIES','', 'GET', false, 2);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ONE_CATEGORY','/[0-9]*', 'GET', false, 2);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CREATE_ONE_CATEGORY','', 'POST', false, 2);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('UPDATE_ONE_CATEGORY','/[0-9]*', 'PUT', false, 2);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('DISABLE_ONE_CATEGORY','/[0-9]*/disabled', 'PATCH', false, 2);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ALL_CUSTOMERS','', 'GET', false, 3);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('REGISTER_ONE','', 'POST', true, 3);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('AUTHENTICATE','/authenticate', 'POST', true, 4);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_MY_PROFILE','/profile','GET', false, 4);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('LOG_OUT','/logout','POST', true, 4);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('VALIDATE-TOKEN','/validate-token', 'GET', true, 4);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('GET-ROLE','/get-role', 'GET', true, 4);

-- CREACIÓN DE ROLES
INSERT INTO role (name) VALUES ('CUSTOMER');
INSERT INTO role (name) VALUES ('ASSISTANT_ADMINISTRATOR');
INSERT INTO role (name) VALUES ('ADMINISTRATOR');

-- CREACIÓN DE PERMISOS
INSERT INTO granted_permissions (role_id, operation_id) VALUES (1, 1);
INSERT INTO granted_permissions (role_id, operation_id) VALUES (1, 2);
INSERT INTO granted_permissions (role_id, operation_id) VALUES (1, 6);
INSERT INTO granted_permissions (role_id, operation_id) VALUES (1, 7);
INSERT INTO granted_permissions (role_id, operation_id) VALUES (1, 14);

INSERT INTO granted_permissions (role_id, operation_id) VALUES (2, 1);
INSERT INTO granted_permissions (role_id, operation_id) VALUES (2, 2);
INSERT INTO granted_permissions (role_id, operation_id) VALUES (2, 4);
INSERT INTO granted_permissions (role_id, operation_id) VALUES (2, 6);
INSERT INTO granted_permissions (role_id, operation_id) VALUES (2, 7);
INSERT INTO granted_permissions (role_id, operation_id) VALUES (2, 9);
INSERT INTO granted_permissions (role_id, operation_id) VALUES (2, 14);

INSERT INTO granted_permissions (role_id, operation_id) VALUES (3, 1);
INSERT INTO granted_permissions (role_id, operation_id) VALUES (3, 2);
INSERT INTO granted_permissions (role_id, operation_id) VALUES (3, 3);
INSERT INTO granted_permissions (role_id, operation_id) VALUES (3, 4);
INSERT INTO granted_permissions (role_id, operation_id) VALUES (3, 5);
INSERT INTO granted_permissions (role_id, operation_id) VALUES (3, 6);
INSERT INTO granted_permissions (role_id, operation_id) VALUES (3, 7);
INSERT INTO granted_permissions (role_id, operation_id) VALUES (3, 8);
INSERT INTO granted_permissions (role_id, operation_id) VALUES (3, 9);
INSERT INTO granted_permissions (role_id, operation_id) VALUES (3, 10);
INSERT INTO granted_permissions (role_id, operation_id) VALUES (3, 14);

-- CREACIÓN DE USUARIOS
INSERT INTO appuser (username, email, name, password, auth_provider, role_id) VALUES ('lmarquez', 'lmarquez@gmail.com', 'luis márquez', '$2a$10$ywh1O2EwghHmFIMGeHgsx.9lMw5IXpg4jafeFS.Oi6nFv0181gHli', 'NORMAL', 1);
INSERT INTO appuser (username, email, name, password, auth_provider, role_id) VALUES ('fperez', 'fperez@gmail.com', 'fulano pérez', '$2a$10$V29z7/qC9wpHfzRMxGOHye5RMAxCid2/MzJalk0dsiA3zZ9CJfub.', 'NORMAL',  2);
INSERT INTO appuser (username, email, name, password, auth_provider, role_id) VALUES ('mhernandez', 'mhernandez@gmail.com', 'mengano hernández', '$2a$10$TMbMuEZ8utU5iq8MOoxpmOc6QWQuYuwgx1xJF8lSMNkKP3hIrwYFG', 'NORMAL', 3);

-- CREACIÓN DE CATEGORIAS
INSERT INTO category (name, status) VALUES ('Electrónica', 'ENABLED');
INSERT INTO category (name, status) VALUES ('Ropa', 'ENABLED');
INSERT INTO category (name, status) VALUES ('Deportes', 'ENABLED');
INSERT INTO category (name, status) VALUES ('Hogar', 'ENABLED');
INSERT INTO category (name, status) VALUES ('Juguetes', 'ENABLED');

-- CREACIÓN DE PRODUCTOS
INSERT INTO product (name, price, status, category_id) VALUES ('Smartphone', 500.00, 'ENABLED', 1);
INSERT INTO product (name, price, status, category_id) VALUES ('Auriculares Bluetooth', 50.00, 'DISABLED', 1);
INSERT INTO product (name, price, status, category_id) VALUES ('Tablet', 300.00, 'ENABLED', 1);
INSERT INTO product (name, price, status, category_id) VALUES ('Laptop', 1200.00, 'ENABLED', 1);
INSERT INTO product (name, price, status, category_id) VALUES ('Smartwatch', 200.00, 'DISABLED', 1);
INSERT INTO product (name, price, status, category_id) VALUES ('Auriculares In-ear', 80.00, 'ENABLED', 1);


INSERT INTO product (name, price, status, category_id) VALUES ('Camiseta', 25.00, 'ENABLED', 2);
INSERT INTO product (name, price, status, category_id) VALUES ('Pantalones', 35.00, 'ENABLED', 2);
INSERT INTO product (name, price, status, category_id) VALUES ('Zapatos', 45.00, 'ENABLED', 2);
INSERT INTO product (name, price, status, category_id) VALUES ('Chaqueta', 75.00, 'ENABLED', 2);
INSERT INTO product (name, price, status, category_id) VALUES ('Bufanda', 15.00, 'ENABLED', 2);
INSERT INTO product (name, price, status, category_id) VALUES ('Gorro', 12.00, 'DISABLED', 2);


INSERT INTO product (name, price, status, category_id) VALUES ('Balón de Fútbol', 20.00, 'ENABLED', 3);
INSERT INTO product (name, price, status, category_id) VALUES ('Raqueta de Tenis', 80.00, 'DISABLED', 3);
INSERT INTO product (name, price, status, category_id) VALUES ('Camiseta de Fútbol', 30.00, 'ENABLED', 3);
INSERT INTO product (name, price, status, category_id) VALUES ('Guantes de Boxeo', 90.00, 'ENABLED', 3);
INSERT INTO product (name, price, status, category_id) VALUES ('Pelota de Baloncesto', 40.00, 'DISABLED', 3);


INSERT INTO product (name, price, status, category_id) VALUES ('Aspiradora', 120.00, 'ENABLED', 4);
INSERT INTO product (name, price, status, category_id) VALUES ('Licuadora', 50.00, 'ENABLED', 4);
INSERT INTO product (name, price, status, category_id) VALUES ('Batidora', 60.00, 'ENABLED', 4);
INSERT INTO product (name, price, status, category_id) VALUES ('Tostadora', 40.00, 'ENABLED', 4);
INSERT INTO product (name, price, status, category_id) VALUES ('Freidora', 80.00, 'DISABLED', 4);



INSERT INTO product (name, price, status, category_id) VALUES ('Muñeca', 25.00, 'ENABLED', 5);
INSERT INTO product (name, price, status, category_id) VALUES ('Tren Eléctrico', 150.00, 'ENABLED', 5);
INSERT INTO product (name, price, status, category_id) VALUES ('Puzzles', 20.00, 'DISABLED', 5);
