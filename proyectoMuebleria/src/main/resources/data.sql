--Datos para carga inicial de la base de datos
delete from VentaProducto;
delete from ContienePedido;
delete from ContieneProducto;
delete from Pedido;
delete from ProveedorProducto;
delete from Proveedor;
delete from Venta;
delete from Vendedor;
delete from PersonalAlmacen;
delete from Transportista;
delete from Trabajador;
delete from AlmacenProducto;
delete from Producto;
delete from Almacen;
delete from Presupuesto;
delete from Cliente;

insert into Cliente(dniCliente,nombreCliente,apellidosCliente,direccionCliente,telefonoCliente,correoCliente) values 
	('123456','nombre', 'apellido1 ap2', 'direccion', 123456,'test@outlook.es');
insert into Presupuesto(codigoPresupuesto,importePresupuesto,fechaPresupuesto,estadoPresupuesto,dniCliente,nombrePresupuesto,tipoPresupuesto,codigoVendedor) values
	('ewafaesfcsf','144.50','12-10-2020','Pendiente',null,'Conjunto de lámparas','MODELO','000'),
	('ewafaxsfcsf','999.95','12-11-2020','Pendiente',null,'Conjunto de literas','MODELO','000'),
	('ewqwaesfcsf','349.99','20-11-2020','Pendiente',null,'Muebles de hogar','MODELO','000');
insert into Producto(codigoProducto,categoriaProducto,nombreProducto,precioProducto) values 
	('001','cama', 'Litera infantil', 199.99),
	('002','sofa', 'Sofa de cuero negro', 349.99),
	('003','sillon', 'Butaca fridkov', 150.00),
	('004','estanteria', 'Estanteria madera maciza', 1049.95),
	('005','sofa', 'Chaiselongue de tela blanca', 399.95),
	('006','lampara', 'Lampara de escritorio metalica', 12.50),
	('007','lampara', 'Lampara de pared', 21.40);
insert into ContieneProducto(codigoProducto,codigoPresupuesto,unidadesPresupuesto,precioProductoPresupuesto) values
	('006','ewafaesfcsf',3,12.50),
	('007','ewafaesfcsf',5,21.40),
	('001','ewafaxsfcsf',5,199.99),
	('002','ewqwaesfcsf',1,349.99);
insert into Trabajador(codigoTrabajador, nifTrabajador, nombreTrabajador, apellidosTrabajador, departamentoTrabajador, numeroTrabajador, horaInicioJornada, horaFinJornada, usuarioTrabajador, contrasenaTrabajador) values 
	('000', '89224098W', 'Pablo', 'Suárez-Otero González', 'Vendedor', 651368470, '09:00', '14:00', 'pablo', '26079e41910bcde04be636fbeecc9045379882b5ad3fe7f70b762436c6d98055'),
	('001', '13869308P', 'Laura', 'González Rey', 'Transportista', 674701008, '08:00', '18:00', 'laura', '5d702eb07928ed7b84626b777c86c39bf4cb403d4024f031d5f97a4b0664421f'),
	('002', '24779756Q', 'Raúl', 'Vásquez Puche', 'Transportista', 736833454, '00:00', '14:00', 'raul', 'bbb0b5b15842af2a3f42072a418c908e8a63caab2120d15cfea68e433cfe68dd'),
	('003', '94412176M', 'Alex', 'Fajardo Navarro', 'Transportista', 658077886, '09:00', '15:00', 'alex', '4135aa9dc1b842a653dea846903ddb95bfb8c5a10c504a7fa16e10bc31d1fdf0'),
	('004', '35902242R', 'Maya', 'Alarcón Melgar', 'Transportista', 723002082, '07:00', '18:30', 'maya', 'a95db5b0ac159e4384ff55ef91c94a98dc563d66a88e7b027fcd5190c0f5bed5'),
	('005', '46232404N', 'Aristarco', 'Luján Sánchez', 'Transportista', 635834635, '12:00', '22:00', 'aristarco', '2222b84aa92f216e1539d745be6d397544689adac5e75b64baf4443efeb0a791'),
	('006', '02929187X', 'Enrique', 'Fernández Rojo', 'Vendedor', 611056927, '09:00', '14:00', 'enrique', 'aec70b6af7d213ba80c2297dfb51df4700d03d33e5846f3aa44f13142980e77f'),
	('007', '91881191U', 'Miguel', 'Arias Tornillo', 'Vendedor', 289819281, '10:00', '15:00', 'miguel', '5ef68465886fa04d3e0bbe86b59d964dd98e5775e95717df978d8bedee6ff16c');
insert into Vendedor(codigoVendedor) values
	('000'),
	('006'),
	('007');
insert into Transportista(codigoTransportista) values
	('001'),
	('002'),
	('003'),
	('004'),
	('005');
insert into Proveedor(codigoProveedor) values
	('001');
insert into ProveedorProducto(codigoProveedor, codigoProducto, precioProductoProveedor) values
	('001', '001', 150.00),
	('001', '002', 200.00),
	('001', '003', 90.00),
	('001', '004', 500.00),
	('001', '005', 300.00),
	('001', '006', 5.00),
	('001', '007', 10.00);
insert into Almacen(codigoAlmacen) values
	('A001');
insert into AlmacenProducto(codigoAlmacen,codigoProducto,stock) values
	('A001','001',3),
	('A001','002',10),
	('A001','003',2),
	('A001','004',20),
	('A001','005',6),
	('A001','006',0),
	('A001','007',1);
insert into Venta(codigoVenta,importeVenta,fechaVentaCreada,dniCliente,codigoTransportista,fechaEntregaDomicilio,horaEntregaDomicilio,estadoVenta,importeVentaMasMontaje,codigoVendedor) values
	('001',199.99,'2-11-2020','89224098W','','','',null,199.99,'000'),
	('002',349.99,'5-11-2020','89224098W','','','',null,349.99,'006'),
	('003',150.00,'6-11-2020','89224098W','','','',null,150.00,'000'),
	('004',199.99,'9-11-2020','89224098W','','','',null,199.99,'000'),
	('005',349.99,'11-11-2020','89224098W','','','',null,349.99,'006'),
	('006',150.00,'16-11-2020','89224098W','','','',null,150.00,'000'),
	('007',199.99,'19-11-2020','89224098W','','','',null,199.99,'000'),
	('008',349.99,'15-10-2020','89224098W','','','',null,349.99,'006'),
	('009',150.00,'19-10-2020','89224098W','','','',null,150.00,'000'),
	('010',199.99,'19-10-2020','89224098W','','','',null,199.99,'006'),
	('011',349.99,'1-10-2020','89224098W','','','',null,349.99,'006'),
	('012',150.00,'12-10-2020','89224098W','','','',null,150.00,'000'),
	('013',199.99,'10-10-2020','89224098W','','','',null,199.99,'000'),
	('014',349.99,'15-10-2020','89224098W','','','',null,349.99,'000'),
	('015',150.00,'21-10-2020','89224098W','','','',null,150.00,'006'),
	('016',199.99,'31-10-2020','89224098W','','','',null,199.99,'000'),
	('017',349.99,'21-10-2020','89224098W','','','',null,349.99,'000'),
	('018',150.00,'2-11-2020','89224098W','','','',null,150.00,'006'),
	('019',150.00,'26-11-2020','89224098W','','','',null,150.00,'007'),
	('020',150.00,'3-11-2020','89224098W','','','',null,150.00,'007'),
	('021',150.00,'10-11-2020','89224098W','','','',null,150.00,'007'),
	('022',150.00,'20-11-2020','89224098W','','','',null,150.00,'007'),
	('023',150.00,'29-11-2020','89224098W','','','',null,150.00,'007'),
	('024',150.00,'21-11-2020','89224098W','','','',null,150.00,'007'),
	('025',150.00,'1-10-2020','89224098W','','','',null,150.00,'007'),
	('026',150.00,'19-9-2020','89224098W','','','',null,150.00,'007'),
	('027',150.00,'12-10-2020','89224098W','','','',null,150.00,'007');
insert into VentaProducto(codigoProducto,codigoVenta,tipoEntrega,montadoProducto,unidadesVenta) values 
	('001','001','Recogida en Tienda',0,1),
	('002','002','Recogida en Tienda',0,1),
	('003','003','Recogida en Tienda',0,1),
	('001','004','Recogida en Tienda',0,1),
	('002','005','Recogida en Tienda',0,1),
	('003','006','Recogida en Tienda',0,1),
	('001','007','Recogida en Tienda',0,1),
	('002','008','Recogida en Tienda',0,1),
	('003','009','Recogida en Tienda',0,1),
	('001','010','Recogida en Tienda',0,1),
	('002','011','Recogida en Tienda',0,1),
	('003','012','Recogida en Tienda',0,1),
	('001','013','Recogida en Tienda',0,1),
	('002','014','Recogida en Tienda',0,1),
	('003','015','Recogida en Tienda',0,1),
	('001','016','Recogida en Tienda',0,1),
	('002','017','Recogida en Tienda',0,1),
	('003','018','Recogida en Tienda',0,1),
	('003','019','Recogida en Tienda',0,1),
	('003','020','Recogida en Tienda',0,1),
	('003','021','Recogida en Tienda',0,1),
	('003','022','Recogida en Tienda',0,1),
	('003','023','Recogida en Tienda',0,1),
	('003','024','Recogida en Tienda',0,1),
	('003','025','Recogida en Tienda',0,1),
	('003','026','Recogida en Tienda',0,1),
	('003','027','Recogida en Tienda',0,1);
insert into Pedido(codigoPedido, estadoPedido, tipoPedido, codigoProveedor, fechaPedido) values
	('01','solicitado','Automático','001','12-11-2020'),
	('02','solicitado','Automático','001','1-11-2020'),
	('03','solicitado','Automático','001','10-11-2020'),
	('04','solicitado','Automático','001','15-11-2020'),
	('05','solicitado','Automático','001','20-11-2020'),
	('06','solicitado','Automático','001','12-10-2020'),
	('07','solicitado','Automático','001','4-10-2020'),
	('08','solicitado','Automático','001','25-10-2020'),
	('09','solicitado','Automático','001','29-10-2020'),
	('10','solicitado','Automático','001','13-10-2020'),
	('11','solicitado','Automático','001','18-9-2020');
insert into ContienePedido(codigoProducto, codigoPedido, unidadesPedido, precioProductoPedido) values
	('001','01',2,150.00),
	('001','02',1,150.00),
	('001','03',4,150.00),
	('001','04',5,150.00),
	('001','05',3,150.00),
	('001','06',3,150.00),
	('001','07',1,150.00),
	('001','08',6,150.00),
	('001','09',7,150.00),
	('001','10',3,150.00),
	('001','11',1,150.00);