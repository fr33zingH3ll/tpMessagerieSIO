export const login = async (email, password) => {
	const result = await fetch("/api/v1/auth/login", {
		body: JSON.stringify({ email, password }),
		method: "POST",
		headers: {
			Accept: "application/json",
		},
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
	const result = await fetch("/api/v1/auth/register", {
		body: JSON.stringify({ name, firstname, email, phone, password, role }),
		method: "POST",
		headers: {
			Accept: "application/json",
		},
	});
	const body = await result.json();
	if (!result.ok) {
		throw new Error(body.error);
	}
	const token = body.token;
	localStorage.setItem("token", token);
};
