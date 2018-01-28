var projectName = document.title;
var project;
var description = $("#description");
var taskDescription = $("#task-description");
var tasks = $("#tasks");
var myTasks;
var developers = $("#developers");
var tasksCount;

function loadProject(){
    $.get("http://localhost:8080/api/developer/projects/" + projectName, function(response, status){
        project = response;
        $.get("http://localhost:8080/api/developer/projects/" + projectName + "/tasks?query=All", function(response, status){
            project["tasks"] = response;
            tasksCount = response.length;
            showProjectOnPage();
        });
        $.get("http://localhost:8080/api/developer/projects/" + projectName + "/tasks?query=My", function(response, status){
            myTasks = response;
        });
    });
}

function showProjectOnPage(){
    description.append(project.description);

    project.tasks.forEach(function(element, index, array){
        showTaskInTable(element, index + 1);
    });

    project.developers.forEach(function(element, index, array){
        developers.append("<a href='#'>" + element.name + "</a>  ")
    });
}

function showTaskInTable(task, number){
    tasks.append("<tr>"+
                    "<td>" + number + "</td>" +
                    "<td><a href='/projects/"+ projectName + "/tasks/" + task.name + "'>" + task.name + "</a></td>" +
                    "<td>" + task.status + "</td>"+
                 "</tr>");
}

function createTask(){
    var taskDto = {}

	taskDto["name"] = $("#name").val();
	taskDto["description"] = $("#task-description").val();

	$.post({
   		url: "http://localhost:8080/api/developer/projects/" + projectName + "/tasks/create",
   		type: "post",
   		data: JSON.stringify(taskDto),
   		dataType: "json",
   		contentType: "application/json"
   	}, function(response){
   	    if(response != null){
   	        tasksCount++;
            showTaskInTable(response, tasksCount);
            $(".modal-backdrop").remove();
            $("#myModal").hide();
            $("#myModal").removeClass("in");
            $("body").removeClass("modal-open");
   	    }
   	})
}

$("#create-task").click(function(){
    createTask();
})

loadProject();

$("#show-tasks").change(function(){
    $("#tasks").find("tr:gt(0)").remove();
    if($("#show-tasks").val() == "All"){
        project.tasks.forEach(function(element, index, array){
            showTaskInTable(element, index + 1);
        });
    } else {
        myTasks.forEach(function(element, index, array){
            showTaskInTable(element, index + 1);
        });
    }
});