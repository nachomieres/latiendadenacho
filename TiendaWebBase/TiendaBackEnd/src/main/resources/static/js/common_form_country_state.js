var dropdownCountries;
var dropdownStates;

$(document).ready(function() {
	dropdownCountries = $("#country");
	dropdownStates = $("#listStates");

	dropdownCountries.on("change", function() {
		loadStates4Country();
		$("#state").val("").focus();
	});	
	
	loadStates4Country();
});

function loadStates4Country() {
	selectedCountry = $("#country option:selected");
	countryId = selectedCountry.val();
	
	url = contextPath + "states/list_by_country/" + countryId;
	
	$.get(url, function(responseJson) {
		dropdownStates.empty();
		
		$.each(responseJson, function(index, state) {
			$("<option>").val(state.name).text(state.name).appendTo(dropdownStates);
		});
	}).fail(function() {
		showErrorModal("Error cargando las Provincias del Pais seleccionado.");
	})	
}	