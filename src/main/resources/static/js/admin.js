var usersCount;
var start = 1;
var usersOnPage = 25;
var currentPageNumber = 1;
var pagesCount;

function getUsersCount(){
    $.get("http://localhost:8080/admin/api/users/count", function(response, status){
            usersCount = response;
            pagesCount = Math.ceil(usersCount / usersOnPage);
        })
}

function loadUsersToTable(pageNumber){
    var queryParams = {};
    queryParams["startRange"] = start;
    queryParams["endRange"] = start + usersOnPage;
    $.get("http://localhost:8080/admin/api/users?" + $.param(queryParams), function(response, status){
        start += usersOnPage * (pageNumber - 1) + 1;
        setPageButtons(pageNumber);
        setPageClickEvents(pageNumber);
        currentPageNumber = pageNumber;
        $("#users").find("tr:gt(0)").remove();
        response.map(function(element, index, array){
            let userRoles = "";
            element.roles.forEach(function(element, index, array){
                userRoles += element.role + ", ";
            });
            userRoles = userRoles.slice(0, -2);
            $("#users").append("<tr>" +
                                    "<td>" + element.id + "</td>" +
                                    "<td>" + element.username + "</td>" +
                                    "<td>" + element.connectionType + "</td>" +
                                    "<td>" + element.email + "</td>" +
                                    "<td>" + element.socialId + "</td>" +
                                    "<td>" + element.isBanned + "</td>" +
                                    "<td>" + userRoles + "</td>" +
                                "</tr>"
            )
        })
    })
}

function setPageButtons(pageNumber){
    let firstButton = currentPageNumber - 2;
    let lastButton = currentPageNumber + 2;
    if(firstButton < 1){
        firstButton = 1;
    }
    if(lastButton > pagesCount){
        lastButton = pagesCount;
    }
    var pagination = $(".pagination");
    pagination.empty();
    for(let i = firstButton ; i <= lastButton ; i++){
        pagination.append("<li><a href='#'>"+ i +"</a></li>");
    }
    pagination.find("li:eq(" + (pageNumber - 1) + ")").addClass("active");
}

function setPageClickEvents(except){
    $(".pagination").find("a").not(":eq(" + (except - 1) + ")").click(function(event){
        loadUsersToTable(event.target.innerHTML);
    });
}

getUsersCount();

loadUsersToTable(1);

