import { useEffect, useState } from "react";
import { createDMChannel, createMessage, fetchMessages, fetchUsers, liveChat } from "../Api";
import { Button, Input, List, Space, Typography } from "antd";
import { useParams } from "react-router-dom";
import { SendOutlined } from "@ant-design/icons";

const { Title, Text } = Typography;

function DMChat() {
	const { userId } = useParams();

	const [channel, setChannel] = useState({});
	const [members, setMembers] = useState({});
	const [messages, setMessages] = useState([]);
	const [text, setText] = useState();
	const [ws, setWs] = useState();

	useEffect(() => {
		(async () => {
			const channel = await createDMChannel(userId);
			setChannel(channel);
			setMessages(await fetchMessages(channel.id));

			const users = await fetchUsers();

			const members = {};

			for (const user of users) {
				if (channel.members.includes(user.id)) {
					members[user.id] = user;
				}
			}

			setMembers(members);

			setWs(liveChat(channel.id));
		})();
	}, [userId]);

	useEffect(() => {
		if (ws) {
			ws.onmessage = (msg) => {
				const changeSet = JSON.parse(msg);
				const newMsg = changeSet.new_val;

				setMessages(messages => {
					messages.push(newMsg);
					return messages;
				});
			};
		}
	}, [ws]);

	const sendMessage = (channelId, text) => {
		createMessage(channelId, text);
		setText();
	};

	return (
		<>
			<List bordered dataSource={messages} renderItem={msg =>
				<List.Item>
					<Typography>
						<Title level={5}>{members[msg.author]?.email} <Text type="secondary">{new Date(msg.date).toLocaleString()}</Text></Title>
						<Text>{msg.text}</Text>
					</Typography>
				</List.Item>
			} />
			<Space.Compact>
				<Input value={text} onChange={e => setText(e.target.value)} onPressEnter={() => sendMessage(channel.id, text)} />
				<Button><SendOutlined /></Button>
			</Space.Compact>
		</>
	);
}

export default DMChat;
