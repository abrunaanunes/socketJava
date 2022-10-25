package classes;

import java.net.Socket;
import java.util.Arrays;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import common.Utils;

public class UserList {
	public User users[];

	public UserList() {
		this.users = new User[10];
		this.initializeList();
	}

	public void initializeList() {

		User user1 = new User("Bruna Nunes", "2328585", "12345", 1, "Oferece serviços de manutenção", false);
		User user2 = new User("Maria Eduarda", "2328589", "12345", 1, "Oferece serviços de manutenção", false);
		User user3 = new User("Caroline Wagner", "2328580", "12345", 1, "Oferece serviços de manutenção", false);

		this.users[0] = user1;
		this.users[1] = user2;
		this.users[2] = user3;

	}

	@Override
	public String toString() {
		return "UserList [users=" + Arrays.toString(users) + "]";
	}
}
