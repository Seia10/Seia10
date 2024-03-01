package project.simplechat;

import java.util.ArrayList;
import java.util.List;

public class Chat {
	
	private List<User> userList = new ArrayList<User>();
	
	private List<Statement> statementList = new ArrayList<Statement>();

	public List<User> getUserList() {
		return this.userList;
	}
	
	public List<Statement> getStatementList() {
		return this.statementList;
	}
	
	public User getUserByName(String name) {
		for(User user: this.userList) {
			if(user.getName().equals(name)) {
				return user;
			}
		}
		return null;
	}
	
	public void addUser(User user) {
		this.userList.add(user);
	}
	
	public void addStatement(Statement statement) {
		this.statementList.add(statement);
	}
	
}
