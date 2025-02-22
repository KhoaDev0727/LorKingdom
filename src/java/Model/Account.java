package Model;

import java.sql.Timestamp;

public class Account {

    private int accountId;
    private int RoleID;
    private String userName;
    private String phoneNumber;
    private String email;
    private String image;
    private String password;
    private String address;
    private String status;
    private double balance;
        private int isDeleted;
    private Timestamp createdAt;
    private Timestamp updateAt;

    public Account() {
    }

    public Account(int accountID, String userName, String phoneNumber, String email, String password, String address, String status, double balance, String image) {
        this.accountId = accountID;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.address = address;
        this.status = status;
        this.balance = balance;
        this.image = image;
    }

    public Account(int accountId, int RoleID, String userName, String phoneNumber, String email, String image, String password, String address, int isDeleted, String status, double balance, Timestamp createdAt, Timestamp updateAt) {
        this.accountId = accountId;
        this.RoleID = RoleID;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.image = image;
        this.password = password;
        this.address = address;
        this.status = status;
        this.balance = balance;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }

    public Account(int accountId, int RoleID, String userName, String phoneNumber, String email, String image, String password, String address, String status, double balance, Timestamp updateAt) {
        this.accountId = accountId;
        this.RoleID = RoleID;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.image = image;
        this.password = password;
        this.address = address;
        this.status = status;
        this.balance = balance;
        this.updateAt = updateAt;
    }

    public Account(int accountId, String userName, String image) {
        this.accountId = accountId;
        this.userName = userName;
        this.image = image;
    }
    

    public Account(int accountId, int RoleID, String userName, String phoneNumber, String email, String image, String password, String address, String status, Timestamp updateAt) {
        this.accountId = accountId;
        this.RoleID = RoleID;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.image = image;
        this.password = password;
        this.address = address;
        this.status = status;
        this.updateAt = updateAt;
    }

    public Account(int accountId, int RoleID, String userName, String phoneNumber,String image, String email,  String password, String address, String status) {
        this.accountId = accountId;
        this.RoleID = RoleID;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.image = image;
        this.password = password;
        this.address = address;
        this.status = status;
    }

    public Account(int RoleID, String userName, String phoneNumber, String email, String image, String password, String address, String status) {
        this.RoleID = RoleID;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.image = image;
        this.password = password;
        this.address = address;
        this.status = status;
    }



    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setAccountID(int accountId) {
        this.accountId = accountId;
    }

    public int getRoleID() {
        return RoleID;
    }

    public void setRoleID(int RoleID) {
        this.RoleID = RoleID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Timestamp updateAt) {
        this.updateAt = updateAt;
    }

}
