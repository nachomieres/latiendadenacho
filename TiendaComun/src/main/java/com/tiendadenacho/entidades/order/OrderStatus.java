package com.tiendadenacho.entidades.order;

public enum OrderStatus {
	NUEVO {
		@Override
		public String defaultDescription() {
			return "El pedido ha sido completado por el cliente";
		}
	}, 
	CANCELADO {
		@Override
		public String defaultDescription() {
			return "El pedido ha sido completado";
		}
	}, 
	EN_PROCESO {
		@Override
		public String defaultDescription() {
			return "El pedido esta siendo procesado";
		}
	}, 
	EMPAQUETADO {
		@Override
		public String defaultDescription() {
			return "EL pedido se ha empaquetado";
		}
	}, 
	RECOGIDO {
		@Override
		public String defaultDescription() {
			return "El pedido ha sido recogido por el transportista";
		}
	}, 
	ENVIADO {
		@Override
		public String defaultDescription() {
			return "El pedido ha sido enviado";
		}
	},
	ENTREGADO{
		@Override
		public String defaultDescription() {
			return "El pedido ha sido entregado";
		}
	}, 
	PETICION_DEVOLUCION{
		@Override
		public String defaultDescription() {
			return "El cliente solicita devolucion";
		}
	},
	DEVUELTO {
		@Override
		public String defaultDescription() {
			return "El pedido ha sido devuelto";
		}
	}, 
	PAGADO {
		@Override
		public String defaultDescription() {
			return "El pedido ha sido pagado";
		}
	}, 
	REEMBOLSADO {
		@Override
		public String defaultDescription() {
			return "El pedido ha sido reembolsado";
		}
	};
	public abstract String defaultDescription();
}
