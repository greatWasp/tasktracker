var projectsCount = 0;

function loadAllProjects(){
    $.get("http://localhost:8080/api/developer/projects", function(response, status){
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

loadAllProjects();