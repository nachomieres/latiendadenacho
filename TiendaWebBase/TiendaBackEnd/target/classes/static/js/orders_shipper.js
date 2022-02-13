var iconNames = {
	'RECOGIDO':'fa-people-carry',
	'ENVIADO':'fa-shipping-fast',
	'ENTREGADO':'fa-box-open',
	'DEVUELTO':'fa-undo'	
};

var confirmText;  
var confirmModalDialog;
var yesButton;
var noButton;

$(document).ready(function() {
	confirmText = $("#confirmText");
	confirmModalDialog = $("#confirmModal");
	yesButton = $("#yesButton");
	noButton = $("#noButton");
	
	$(".linkUpdateStatus").on("click", function(e) {
		e.preventDefault();
		link = $(this);
		showUpdateConfirmModal(link);
	});
	
	addEventHandlerForYesButton();
});

function addEventHandlerForYesButton() {
	yesButton.click(function(e) {
		e.preventDefault();
		sendRequestToUpdateOrderStatus($(this));
	});
}

function sendRequestToUpdateOrderStatus(button) {
	requestURL = button.attr("href");
	
	$.ajax({
		type: 'POST',
		url: requestURL,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function(response) {
		showMessageModal("Pedido actualizado correctamente");
		updateStatusIconColor(response.orderId, response.status);
		
		console.log(response);
	}).fail(function(err) {
		showMessageModal("Error actualizando pedido");
	})
}

function updateStatusIconColor(orderId, status) {
	link = $("#link" + status + orderId);
	link.replaceWith("<i class='fas " + iconNames[status] + " fa-2x icon-green'></i>");
}

function showUpdateConfirmModal(link) {
	noButton.text("NO");
	yesButton.show();
		
	orderId = link.attr("orderId");
	status = link.attr("status");
	yesButton.attr("href", link.attr("href"));
	
	confirmText.text("¿Estas seguro de querer cambiar el pedido #" + orderId
					 + " a " + status + "?");
					 
	confirmModalDialog.modal();
}

function showMessageModal(message) {
	noButton.text("Cerrar");
	yesButton.hide();
	confirmText.text(message);
}