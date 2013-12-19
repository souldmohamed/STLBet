function getWeather()
{
	var url = encodeURIComponent("http://api.openweathermap.org/data/2.5/weather?q=" + document.getElementById("city").value + "&callback=moncallback");
alert(url);
	var monScript = document.createElement("script");
	monScript.setAttribute("type", "application/javascript");
	monScript.setAttribute("src", url);
	document.getElementsByTagName('head')[0].appendChild(monScript);
}

function moncallback(jsonResponse)
{
	
	//temp.innerHTML = JSON.stringify(jsonResponse,null,2);

        var temp = document.createElement("p");
        temp.appendChild(document.createTextNode("Temperature :"+ jsonResponse.main.temp ));
	
	var hum = document.createElement("p");
        hum.appendChild(document.createTextNode("Humidity :"+ jsonResponse.main.humidity ));

        var press = document.createElement("p");
        press.appendChild(document.createTextNode("Pressure :"+ jsonResponse.main.pressure ));

	document.getElementsByTagName('body')[0].appendChild(temp);
	document.getElementsByTagName('body')[0].appendChild(hum);
	document.getElementsByTagName('body')[0].appendChild(press);

}

