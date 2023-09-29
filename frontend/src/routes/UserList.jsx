import { AlertOutlined, MailTwoTone } from "@ant-design/icons";
import { List, Typography } from "antd";
import { useEffect, useState } from "react";
import { fetchUsers } from "../Api";
import { Link } from "react-router-dom";

const { Text } = Typography;

function UserList() {
	const [users, setUsers] = useState([]);

	useEffect(() => {
		fetchUsers().then(users => setUsers(users));
	}, []);

	return <List bordered dataSource={users} renderItem={item =>
		<List.Item
			actions={[<Link to={"/dm/" + item.id}><MailTwoTone /></Link>]}
		>
			<Typography>
				<Text strong>{item.email}</Text>
				{item.roles.includes("ROLE_ADMIN") ? <AlertOutlined /> : undefined}
			</Typography>
		</List.Item>
	} />
}

export default UserList;
