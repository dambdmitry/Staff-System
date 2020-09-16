
$('#OrderType').change(function(){
    if($(this).find('option:selected').val() === 'Увольнение'){
        $('#mayBeHidden').hide();
    }else{
        $('#mayBeHidden').show();
    }
})

