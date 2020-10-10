$("#table").on('click', '.clickable-row', function(event){
   $(this).addClass('active').siblings().removeClass('active');
   var value = $(this).find("#forDelete").html();
   $('#propertyForDelete').val(value);
   $('#btnDelete').prop('disabled', false);
});