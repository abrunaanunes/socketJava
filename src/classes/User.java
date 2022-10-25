package classes;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class User {
	private String name;
    private String ra;
    private String password;
    private Integer category_id;
    private String description;
    private Boolean isAvailable;
    public CategoryList categoryList;
    public UserList userList;

    public User(String name, String ra, String password, Integer category_id, String description, Boolean isActive) {
    	this.name = name;
    	this.ra = ra;
    	this.password = password;
    	this.category_id = category_id;
    	this.description = description;
    	this.isAvailable = false;
    }
    
    public User(CategoryList categoryList, UserList userList) {
    	this.categoryList = categoryList;
    	this.userList = userList;
    }
    
    public User() {

    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRa() {
		return ra;
	}

	public void setRa(String ra) {
		this.ra = ra;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getCategory_id() {
		return category_id;
	}

	public void setCategory_id(Integer category_id) {
		this.category_id = category_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", ra=" + ra + ", password=" + password + ", category_id=" + category_id
				+ ", description=" + description + ", isAvailable=" + isAvailable + "]";
	}
	
	public User getUser(String ra, String password) {
        int i;
        for (i = 0; i < this.userList.users.length; i++) {
            if(this.userList.users[i] != null) {
            	if ((this.userList.users[i].getRa().equals(ra)) && (this.userList.users[i].getPassword().equals(password))) {

                    return this.userList.users[i];

                }
            }

        }
        return null;
    }
    
    public User getUser(String ra) {
        int i;
        for (i = 0; i < 3; i++) {

            if (this.userList.users[i].getRa().equals(ra)) {

                return this.userList.users[i];

            }

        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public JSONObject register(JSONObject json) {
    	
    	JSONObject data = new JSONObject();
    	JSONObject response = new JSONObject();
    	User user = new User();
    	
		JSONObject parametros = null;
		JSONParser parserMessage = new JSONParser();
		
		parametros = (JSONObject) json.get("parametros");
		String name = (String) parametros.get("ra");
		String ra = (String) parametros.get("ra");
		String password = (String) parametros.get("senha");
		Integer category_id = Integer.parseInt(parametros.get("categoria_id").toString());
		String description = (String) parametros.get("senha");
		Boolean isActive = false;
    	
    	int i;
    	
    	if(categoryList.getCategory(category_id) == null) {
    		response.put("status", 400);
            response.put("mensagem", "Formato incompatível com JSON");
            response.put("dados", data);
            return response;
    	}
    	
    	if(this.getUser(ra) != null) {
    		response.put("status", 202);
        	response.put("mensagem", "Usuário já encontra-se cadastrado!");
        	response.put("dados", data);
        	return response;
    	}
    	
    	for (i = 0; i < this.userList.users.length; i++) {
            if (this.userList.users[i] == null) {

                this.userList.users[i] = new User(name, ra, password, category_id, description, isActive);

                user = this.userList.users[i];
            }

        }
    	
    	response.put("status", 201);
    	response.put("mensagem", "Usuário cadastrado com sucesso!");
    	response.put("dados", data);
        return response;
    }
    
    @SuppressWarnings("unchecked")
    public JSONObject login(JSONObject json) {
    	
    	JSONObject data = new JSONObject();
    	JSONObject response = new JSONObject();
    	User user = new User();
		
    	JSONObject parametros = (JSONObject) json.get("parametros");
		
		String senha = (String) parametros.get("senha");
        String ra = (String) parametros.get("ra");
        user = this.getUser(ra, senha);

        if(user == null) {
        	response.put("status", 404);
        	response.put("mensagem", "Usuário não encontrado");
        	response.put("dados", data);
        	return response;
        }
        
        // Atualizando isAvailable       	
        user.setIsAvailable(true);
        response.put("status", 200);
        response.put("mensagem", "Login efetuado com sucesso");
        response.put("dados", data);
        return response;
    }
    
    @SuppressWarnings("unchecked")
	public JSONObject logout(String request) 
    {	
    	JSONObject data = new JSONObject();
    	JSONObject response = new JSONObject();
    	User user = new User();
    	
    	String temp = request;
		JSONObject json = null;
		JSONObject params = null;
		JSONParser parserMessage = new JSONParser();
		
		try {
			json = (JSONObject) parserMessage.parse(temp);
		} catch (ParseException e) {
            response.put("status", 400);
            response.put("mensagem", "Formato incompatível com JSON");
            response.put("dados", data);
            return response;
		}
		
		params = (JSONObject) json.get("parametros");
		
		String ra = params.get("ra").toString();
		user = this.getUser(ra);
		
		if(user == null) {
            response.put("status", 404);
            response.put("mensagem", "Usuário não encontrado.");
            response.put("dados", data);
            return response;
            
		} else if(user.getIsAvailable() == false) {
            response.put("status", 202);
            response.put("mensagem", "Usuário já  encontra-se desconectado.");
            response.put("dados", data);
            return response;
		} 
		
		// Atualizando isAvailable
		user.setIsAvailable(false);
		response.put("status", 600);
		response.put("mensagem", "Usuário desconectado com sucesso!");
		response.put("dados", data);
		return response;
    }
}
