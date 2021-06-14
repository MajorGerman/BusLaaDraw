class App {
    async send_data(){
        const data = {
            "data": lolCoords
        }
        const response = await fetch('sendDataToServer',{
            method: 'POST',
            body: JSON.stringify(lolCoords),
        }); 
        lolCoords = [];
    }

    async recieve_data(){
        const response = await fetch('getDataFromServer',{
            method: 'GET'
        }); 
        var result = await response.json();
        ctx.beginPath();
        ctx.moveTo(result[0].x, result[0].y);
        for (let dot of result) {		
            ctx.lineTo(dot.x-15, dot.y-5);
            ctx.stroke();
        }
    } 
    
    async create_room() {
        c.style.display = 'block';
        const response = await fetch('getGameCode',{
            method: 'GET',
        }); 
        var result = await response.json();
        document.getElementById("title").innerHTML += " " + result.gameCode;
        var a = setInterval(app.send_data, 1000);
    }
    
    async join_room() {
        const data = {
            "gameCode": document.getElementById("gameCode").value
        };
        const response = await fetch('joinGame',{
            method: 'POST',
            body: JSON.stringify(data),
        }); 
        var result = await response.json();
        if (result.response) {
            c.style.display = 'block';
        }   
        var b = setInterval(app.recieve_data, 1000)
    }
}

var app = new App();

var c = document.getElementById("canvas");
var ctx = c.getContext("2d");

var width = 600
var height = 600

c.width = width
c.height = height

var lolCoords =[];

var mouseDown = false;
var drawColor = "#000000";

c.addEventListener('mousedown', (e) => {
        mouseDown = true;
        ctx.beginPath();
        ctx.moveTo(e.x-15, e.y-5);

})

c.addEventListener('mouseup', (e) => {
        mouseDown = false;
})

c.addEventListener('mousemove', (e) => {
    if (mouseDown) {
        ctx.lineTo(e.x-15, e.y-5);
        lolCoords.push({
            "x": e.x,
            "y": e.y
        });
        ctx.stroke();
    }
})

document.getElementById("btn1").addEventListener("click", app.create_room);
document.getElementById("btn2").addEventListener("click", app.join_room);
//var a = setInterval(1000, app.send_data);
//var b = setInterval(1000, recieve_data);
