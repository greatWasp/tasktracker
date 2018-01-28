$('.close-panel').on('click',function(){
    var effect = $(this).data('effect');
        $(this).closest('.panel')[effect]();
})