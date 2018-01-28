var projectsCount = 0;

function createProject(){
    var projectDto = {}

	projectDto["name"] = $("#name").val();
	projectDto["description"] = $("#description").val();

	$.post({
   		url: "http://localhost:8080/api/manager/projects/create",
   		type: "post",
   		data: JSON.stringify(projectDto),
   		dataType: "json",
   		contentType: "application/json"
   	}, function(response){
   	    if(response != null){
   	        projectsCount++;
            showProjectInTable(response, projectsCount);
            $(".modal-backdrop").remove();
            $("#myModal").hide();
            $("#myModal").removeClass("in");
            $("body").removeClass("modal-open");
   	    }
   	})
}

function loadAllProjects(){
    $.get("http://localhost:8080/api/manager/projects", function(response, status){
        $("#projects").find("tr:gt(0)").remove();
        projectsCount = response.length;
        response.forEach(function(element, index, array){
            showProjectInTable(element, index + 1);
        })
    })
}

function showProjectInTable(project, number){
    $("#projects").append("<tr>" +
                            "<td>" + number + "</td>" +
                            "<td><a href='/projects/" + project.name + "'>" + project.name + "</a></td>" +
                            "<td>" + project.description + "</td>" +
                          "</tr>")
}

$("#create-project").click(function(){
    createProject();
})

loadAllProjects();
