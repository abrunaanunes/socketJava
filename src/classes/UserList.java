package classes;

import java.util.Arrays;

public class UserList {
	public static User users[];

	public UserList() {
		this.users = new User[10];
		this.initializeList();
	}

	public void initializeList() {

		User user1 = new User("Bruna Nunes", "2328585", "12345", 1, "Oferece serviços de manutenção", 0);
		User user2 = new User("Maria Eduarda", "2328589", "12345", 1, "Oferece serviços de manutenção", 0);
		User user3 = new User("Caroline Wagner", "2328580", "12345", 1, "Oferece serviços de manutenção", 0);

		this.users[0] = user1;
		this.users[1] = user2;
		this.users[2] = user3;

	}

	@Override
	public String toString() {
		return "UserList [users=" + Arrays.toString(users) + "]";
	}

	public static String getUsers() {
		return "UserList [users=" + Arrays.toString(users) + "]";
	}

	public static void setUsers(User[] users) {
		UserList.users = users;
	}
	
	
}
