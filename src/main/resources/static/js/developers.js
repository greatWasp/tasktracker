var selectedDevelopersCount = 0;

function loadAllProjects(){
    $.get("http://localhost:8080/api/manager/projects", function(response, status){
        window.projects = response;
        $.get("http://localhost:8080/api/manager/projects/" + window.projects[0].name + "/tasks", function(response, status){
            window.projects[0]["tasks"] = response;
            displayTasksInSelect(window.projects[0]);
        });
        response.forEach(function(element, index, array){
            if(index < 1) return;
            $.get("http://localhost:8080/api/manager/projects/" + element.name + "/tasks", function(response, status){
                element["tasks"] = response;
            });
        });
        displayProjectsInSelect();
        queryDevelopers();
    });
}

function displayProjectsInSelect(){
    window.projects.forEach(function(element, index, array){
        $("#project").append("<option>" + element.name + "</option>");
    });
}

function displayTasksInSelect(){
    $("#task").empty();
    window.projects[$("#project")[0].selectedIndex].tasks.forEach(function(element, index, array){
        $("#task").append("<option>" + element.name + "</option>");
    });
}

function displayDeveloperOnPage(user){
    $("#developers").append("<tr>" +
                                "<td><input id='" + user.id + "' type='checkbox'></td>" +
                                "<td>" + user.name + "</td>" +
                            "</tr>");
    $(":checkbox").change(onCheckBoxChange);
}

function onCheckBoxChange(){
    if(this.checked){
        selectedDevelopersCount++;
        $("#add-developer").removeAttr("disabled");
    } else {
        selectedDevelopersCount--;
        if(selectedDevelopersCount === 0){
            $("#add-developer").attr("disabled", "disabled");
        }
    }
}

function addToSelectOnValueChange(){
    if($(this).val() == "Task"){
        $("#select-task-wrapper").show();
        displayTasksInSelect(window.projects[$("#project")[0].selectedIndex]);
    } else {
        $("#select-task-wrapper").hide();
    }
    queryDevelopers();
}

$("#add-to").change(addToSelectOnValueChange);

function queryDevelopers(queryParameter){
    var query = "";
    if((typeof queryParameter !== 'undefined') && (queryParameter != "")){
        query = "?name=" + queryParameter;
    }
    $("#developers").find("tr:gt(0)").remove();
    if($("#add-to").val() == "Task" && $("#task").val() != null){
        queryDevelopersAvailableForTask(query);
    } else {
        queryDevelopersAvailableForProject(query);
    }
}

function queryDevelopersAvailableForProject(query){
    $.get("http://localhost:8080/api/manager/projects/" + $("#project").val() + "/developers/available" + query, function(response, status){
        response.forEach(function(element, index, array){
            displayDeveloperOnPage(element);
        });
    });
}

function queryDevelopersAvailableForTask(query){
    if($("#task").val() != null){
        $.get("http://localhost:8080/api/manager/projects/" + $("#project").val() + "/tasks/" + $("#task").val() + "/developers/available" + query, function(response, status){
            response.forEach(function(element, index, array){
                displayDeveloperOnPage(element);
            });
        });
    }
}

function onProjectChange(){
    displayTasksInSelect(window.projects[this.selectedIndex]);
    queryDevelopers();
}

$("#task").change(queryDevelopersAvailableForTask);
$("#project").change(onProjectChange);

$("#add-developer").click(function(){
    var developersIds = [];
    $(":checkbox:checked").each(function(index, element, array){ // cuz jQuErY
        developersIds.push(element.id);
    });
    if($("#add-to").val() == "Project"){
        $.ajax({
            url: "http://localhost:8080/api/manager/projects/" + window.projects[$("#project")[0].selectedIndex].name + "/developers/add",
            type: "PUT",
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(developersIds),
            success: function () {
                location.reload();
            }
        });
    } else {
        $.ajax({
            url: "http://localhost:8080/api/manager/projects/" + window.projects[$("#project")[0].selectedIndex].name + "/tasks/" + window.projects[$("#project")[0].selectedIndex].tasks[$("#task")[0].selectedIndex].name + "/developers/add",
            type: "PUT",
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(developersIds),
            success: function () {
                location.reload();
            }
        });
    }
});

function queryDevelopersAvailableForTask(){
    if($("#task").val() != null){
        $.get("http://localhost:8080/api/manager/projects/" + $("#project").val() + "/tasks/" + $("#task").val() + "/developers/available", function(response, status){
            response.forEach(function(element, index, array){
                displayDeveloperOnPage(element);
            });
        });
    }
}

$("#search-button").click(function(){
    queryDevelopers($("#search").val());
});

loadAllProjects();