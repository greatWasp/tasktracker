var panel = $("#panel");

$("#show-panel").click(function(){
    if(panel.is(":visible")){
        panel.hide();
    } else {
        panel.show();
    }
})