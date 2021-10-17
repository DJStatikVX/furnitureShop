drop table VentaProducto;
drop table ContienePedido;
drop table ContieneProducto;
drop table Pedido;
drop table ProveedorProducto;
drop table Proveedor;
drop table Venta;
drop table Vendedor;
drop table PersonalAlmacen;
drop table Transportista;
drop table Trabajador;
drop table AlmacenProducto;
drop table Producto;
drop table Almacen;
drop table Presupuesto;
drop table Cliente;

create table Cliente (
	dniCliente varchar(20) primary key not null,
	nombreCliente varchar(20) not null,
	apellidosCliente varchar(30) not null,
	direccionCliente varchar(30) not null,
	telefonoCliente decimal(20,0) not null,
	correoCliente varchar(30) not null
);

create table Presupuesto (
	codigoPresupuesto varchar(20) primary key not null,
	importePresupuesto decimal(22,2) not null,
	fechaPresupuesto varchar(20) not null,
	estadoPresupuesto varchar(20) not null,
	dniCliente varchar(20),
	nombrePresupuesto varchar(20),
	tipoPresupuesto varchar(20),
	codigoVendedor varchar(20) not null,
	check(estadoPresupuesto = 'Pendiente' or estadoPresupuesto = 'Caducado' or estadoPresupuesto = 'Tramitado'),
	foreign key(dniCliente) references Cliente(dniCliente),
	foreign key(codigoVendedor) references Vendedor(codigoVendedor)
);

create table Almacen (
	codigoAlmacen varchar(20) primary key not null
);

create table Producto (
	codigoProducto varchar(20) primary key not null,
	categoriaProducto varchar(20) not null,
	nombreProducto varchar(20) not null,
	precioProducto decimal(22,2) not null
);

create table AlmacenProducto (
	codigoAlmacen varchar(20) not null,
	codigoProducto varchar(20) not null,
	stock int not null,
	primary key(codigoAlmacen, codigoProducto),
	foreign key(codigoAlmacen) references Almacen(codigoAlmacen),
	foreign key(codigoProducto) references Producto(codigoProducto)
);

create table Trabajador (
	codigoTrabajador varchar(20) primary key not null,
	nifTrabajador varchar(20) not null,
	nombreTrabajador varchar(20) not null,
	apellidosTrabajador varchar(20) not null,
	departamentoTrabajador varchar(20) not null,
	numeroTrabajador decimal(20,0) not null, 
	horaInicioJornada varchar(20) not null, 
	horaFinJornada varchar(20) not null,
	usuarioTrabajador varchar(20),
	contrasenaTrabajador varchar(20),
	check(horaFinJornada > horaInicioJornada)
);

create table Transportista (
	codigoTransportista varchar(20) primary key not null,
	foreign key(codigoTransportista) references Trabajador(codigoTrabajador)
);

create table PersonalAlmacen (
	codigoPersonalAlmacen varchar(20) primary key not null,
	foreign key(codigoPersonalAlmacen) references Trabajador(codigoTrabajador)
);

create table Vendedor (
	codigoVendedor varchar(20) primary key not null,
	foreign key(codigoVendedor) references Trabajador(codigoTrabajador)
);

create table Venta (
	codigoVenta varchar(20) primary key not null,
	importeVenta decimal(22,2) not null,
	fechaVentaCreada varchar(20) not null,
	dniCliente varchar(20) not null,
	codigoTransportista varchar(20) not null,
	fechaEntregaDomicilio varchar(20) not null,
	horaEntregaDomicilio varchar(20) not null,
	estadoVenta varchar(20),
	importeVentaMasMontaje decimal(22,2),
	codigoVendedor varchar(20) not null,
	foreign key(codigoTransportista) references Transportista(codigoTransportista),
	foreign key(dniCliente) references Cliente(dniCliente),
	foreign key(codigoVendedor) references Vendedor(codigoVendedor)
);

create table Proveedor (
	codigoProveedor varchar(20) primary key not null
);

create table ProveedorProducto (
	codigoProveedor varchar(20) not null,
	codigoProducto varchar(20) not null,
	precioProductoProveedor decimal(22,2) not null,
	primary key(codigoProveedor, codigoProducto),
	foreign key(codigoProveedor) references Proveedor(codigoProveedor),
	foreign key(codigoProducto) references Producto(codigoProducto)
);

create table Pedido (
	codigoPedido varchar(20) primary key not null,
	estadoPedido varchar(20) not null,
	tipoPedido varchar(20) not null,
	codigoProveedor varchar(20) not null,
	fechaPedido varchar(20) not null,
	foreign key(codigoProveedor) references Proveedor(codigoProveedor)
);

create table ContieneProducto (
	codigoProducto varchar(20) not null,
	codigoPresupuesto varchar(20) not null,
	unidadesPresupuesto int not null,
	precioProductoPresupuesto decimal(22,2),
	primary key(codigoProducto, codigoPresupuesto),
	foreign key(codigoProducto) references Producto(codigoProducto),
	foreign key(codigoPresupuesto) references Presupuesto(codigoPresupuesto)
);

create table ContienePedido (
	codigoProducto varchar(20) not null,
	codigoPedido varchar(20) not null,
	unidadesPedido int not null,
	precioProductoPedido decimal(22,2),
	primary key(codigoProducto, codigoPedido),
	foreign key(codigoProducto) references Producto(codigoProducto),
	foreign key(codigoPedido) references Pedido(codigoPedido)
);

create table VentaProducto (
	codigoProducto varchar(20) not null,
	codigoVenta varchar(20) not null,
	tipoEntrega varchar(20) not null,
	montadoProducto int not null,
	unidadesVenta int not null,
	primary key(codigoProducto, codigoVenta),
	foreign key(codigoProducto) references Producto(codigoProducto),
	foreign key(codigoVenta) references Producto(codigoVenta)
);