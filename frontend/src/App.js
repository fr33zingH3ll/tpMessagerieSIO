import React from 'react';
import { Routes, Route, BrowserRouter } from 'react-router-dom';
import { Layout } from 'antd';
import Login from './routes/Login';
import Home from './routes/Home';
import NotFound from './routes/NotFound';
import Navbar from './routes/Navbar';
import Register from './routes/Register';
import DMChat from './routes/DMChat';

const { Header, Footer, Content } = Layout;

function App() {
  return (
    <>
      <BrowserRouter>
			<Layout>
				<Header>
					<Navbar />
				</Header>
				<Content>
					<Routes>
						<Route path="/login" element={<Login />} />
						<Route path="/register" element={<Register />} />
						<Route path="/dm/:userId" element={<DMChat />} />
						<Route path="/" element={<Home />} />
						<Route path="*" element={<NotFound />} />
					</Routes>
				</Content>
				<Footer>

				</Footer>
			</Layout>
		</BrowserRouter>

    </>
  );
}

export default App;
