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
    private int isAvailable;

    public User(String name, String ra, String password, Integer category_id, String description, int isAvailable) {
    	this.name = name;
    	this.ra = ra;
    	this.password = password;
    	this.category_id = category_id;
    	this.description = description;
    	this.isAvailable = isAvailable;
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

	public Integer getCategoryId() {
		return category_id;
	}

	public void setCategoryId(Integer category_id) {
		this.category_id = category_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(int isAvailable) {
		this.isAvailable = isAvailable;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", ra=" + ra + ", password=" + password + ", category_id=" + category_id
				+ ", description=" + description + ", isAvailable=" + isAvailable + "]";
	}
	
	public static User getUser(String ra, String password) {
        int i;
        for (i = 0; i < UserList.users.length; i++) {
            if(UserList.users[i] != null) {
            	if ((UserList.users[i].getRa().equals(ra)) && (UserList.users[i].getPassword().equals(password))) {

                    return UserList.users[i];

                }
            }

        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public static JSONObject register(JSONObject json) {
    	
    	JSONObject data = new JSONObject();
    	JSONObject response = new JSONObject();
    	User user = new User();
    	
		JSONObject parametros = (JSONObject) json.get("parametros");
		String name = (String) parametros.get("nome");
		String ra = (String) parametros.get("ra");
		String password = (String) parametros.get("senha");
		Integer category_id = Integer.parseInt(parametros.get("categoria_id").toString());
		String description = (String) parametros.get("descricao");
    	
    	int i;
    	
    	if(CategoryList.getCategory(category_id) == null) {
    		response.put("status", 400);
            response.put("mensagem", "Formato incompatível com JSON");
            response.put("dados", data);
            return response;
    	}
    	
    	if(getUser(ra, password) != null) {
    		response.put("status", 202);
        	response.put("mensagem", "Usuário já encontra-se cadastrado!");
        	response.put("dados", data);
        	return response;
    	}
    	
    	for (i = 0; i < UserList.users.length; i++) {
            if (UserList.users[i] == null) {

                UserList.users[i] = new User(name, ra, password, category_id, description, 0);

                user = UserList.users[i];
            }

        }
    	
    	JSONObject userObj = new JSONObject();
        userObj.put("ra", user.getRa());
        userObj.put("senha", user.getPassword());
        userObj.put("nome", user.getName());
        userObj.put("categoria_id", user.getCategoryId());
        userObj.put("descricao", user.getDescription());
        userObj.put("disponivel", user.getIsAvailable());
        data.put("usuario", userObj);
        
    	response.put("status", 201);
    	response.put("mensagem", "Usuário cadastrado com sucesso!");
    	response.put("dados", data);
        return response;
    }
    
    @SuppressWarnings("unchecked")
    public static JSONObject login(JSONObject json) {
    	
    	JSONObject data = new JSONObject();
    	JSONObject response = new JSONObject();
    	User user = new User();
		
    	JSONObject parametros = (JSONObject) json.get("parametros");
    	
		String senha = (String) parametros.get("senha");
        String ra = (String) parametros.get("ra");
        user = getUser(ra, senha);

        if(user == null) {
        	response.put("status", 404);
        	response.put("mensagem", "Usuário não encontrado");
        	response.put("dados", data);
        	return response;
        }
        
        if(user.getIsAvailable() == 1) {
        	response.put("status", 403);
        	response.put("mensagem", "Usuário já encontra-se conectado");
        	response.put("dados", data);
        	return response;
        }
        
        // Atualizando isAvailable       	
        user.setIsAvailable(1);
        JSONObject userObj = new JSONObject();
        userObj.put("ra", user.getRa());
        userObj.put("senha", user.getPassword());
        userObj.put("nome", user.getName());
        userObj.put("categoria_id", user.getCategoryId());
        userObj.put("descricao", user.getDescription());
        userObj.put("disponivel", user.getIsAvailable());
        data.put("usuario", userObj);
        
        response.put("status", 200);
        response.put("mensagem", "Login efetuado com sucesso");
        response.put("dados", data);
        return response;
    }
    
    @SuppressWarnings("unchecked")
	public static JSONObject logout(String request) 
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
		String password = params.get("senha").toString();
		user = getUser(ra, password);
		
		if(user == null) {
			System.out.println(user);
            response.put("status", 404);
            response.put("mensagem", "Usuário não encontrado.");
            response.put("dados", data);
            return response;
            
		} else if(user.getIsAvailable() == 0) {
            response.put("status", 202);
            response.put("mensagem", "Usuário já  encontra-se desconectado.");
            response.put("dados", data);
            return response;
		} 
		
		// Atualizando isAvailable
		user.setIsAvailable(0);
		response.put("status", 600);
		response.put("mensagem", "Usuário desconectado com sucesso!");
		response.put("dados", data);
		return response;
    }
}
