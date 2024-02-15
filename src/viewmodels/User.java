package viewmodels;

public class User {
    private String username;
    private int userId;
    private String password;

    /**
     * constructor for user object
     * @param username
     * @param userId
     * @param password
     */
    public User(String username, int userId, String password) {
        this.username = username;
        this.userId = userId;
        this.password = password;
    }
    /**
     * getter/setter for User
     * @return
     */
    public String getUsername() {
        return username;
    }
    /**
     * getter/setter for User
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
     * getter/setter for User
     * @return
     */
    public int getUserId() {
        return userId;
    }
    /**
     * getter/setter for User
     * @param userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }
    /**
     * getter/setter for User
     * @return
     */
    public String getPassword() {
        return password;
    }
    /**
     * getter/setter for User
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
