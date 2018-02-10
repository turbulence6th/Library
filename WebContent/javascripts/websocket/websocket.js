var websocket = new WebSocket(":protocol:/:ip::port/websocket"
		.replace(":protocol", window.location.protocol=="http:"?"ws":"wss")
		.replace(":ip", window.location.hostname)
		.replace(":port", window.location.port));

