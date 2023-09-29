const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
	app.use(
		'/api/v1/conv',
		createProxyMiddleware({
			target: 'ws://localhost:8888',
			ws: true,
			changeOrigin: true,
		})
	);

    app.use(
		'/api/v1',
        createProxyMiddleware({
            target: 'http://localhost:8888',
            changeOrigin: true,
        })
    );
};
