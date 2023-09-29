export const login = async (email, password) => {
	const result = await request("/auth/login", {
		body: JSON.stringify({ email, password }),
		method: "POST",
	});

	const body = await result.json();

	if (!result.ok) {
		throw new Error(body.error);
	}

	const token = body.token;
	localStorage.setItem("token", token);
};

export const logout = async () => {
	localStorage.removeItem("token");
};

export const register = async (name, firstname, email, phone, password, role) => {
	const result = await request("/auth/register", {
		body: JSON.stringify({ name, firstname, email, phone, password, role }),
		method: "POST",
	});

	const body = await result.json();

	if (!result.ok) {
		throw new Error(body.error);
	}
};

export const fetchUsers = async () => {
	const result = await request("/user/");
	return await result.json();
};

export const createDMChannel = async (userId) => {
	const result = await request("/channel/" + userId, {
		method: "POST"
	});
	return await result.json();
};

export const fetchMessages = async (channelId) => {
	const result = await request("/message/" + channelId);
	return await result.json();
}

export const createMessage = async (channelId, text) => {
	const result = await request("/message/" + channelId, {
		method: "POST",
		body: JSON.stringify({ text })
	});
	return await result.json();
};

export const liveChat = async (channelId) => {
	// const ws = new WebSocket(`ws://${window.location.host}/api/v1/conv`);
	const ws = new WebSocket(`ws://localhost:8888/api/v1/conv`);
	ws.onopen = () => ws.send(JSON.stringify({ token: localStorage.getItem("token") }));
	return ws;
};

const request = async (url, parameters) => {
	return await fetch("/api/v1" + url, {
		headers: {
			Accept: "application/json",
			"Content-Type": "application/json",
			...(localStorage.getItem("token") ? { Authorization: "Bearer " + localStorage.getItem("token") } : {})
		}, ...(parameters ? parameters : {})
	});
};
