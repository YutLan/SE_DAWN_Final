import java.sql.*;


public class DatabaseAdapter {
    private String driveName = "com.mysql.cj.jdbc.Driver";
    private String url = "jdbc:mysql://39.105.27.108:3306/dawn?serverTimezone=GMT%2B8";
    // jdbc:mysql://127.0.0.1:3306/onestep?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&useSSL=false&serverTimezone=GMT%2B8
    private String user = "dawn";
    private String pass = "dawnnb";

    public  void create() throws ClassNotFoundException, SQLException {
        Class.forName(driveName);
        //连接
        Connection con = DriverManager.getConnection(url, user, pass);
        Statement state = con.createStatement();
        state.executeUpdate("create table userinfo(Account varchar(40),pwd varchar(40))");
    }




    public  void update(String account,String password) throws SQLException, ClassNotFoundException {
        Class.forName(driveName);
        //连接
        Connection con = DriverManager.getConnection(url, user, pass);
        Statement state = con.createStatement();
        String sql="insert into  userinfo (Account,pwd)  values(?,?)";//sql语句
        PreparedStatement pstmt=con.prepareStatement(sql);//获得预置对象
        pstmt.setString(1, account);//设置占位符的值
        pstmt.setString(2, password);//设置占位符的值
        pstmt.executeUpdate();
        state.close();
        con.close();
    }

    public boolean check(String account) throws ClassNotFoundException, SQLException {
        boolean flag=true;
        Class.forName(driveName);
        //连接
        Connection con = DriverManager.getConnection(url, user, pass);
        Statement state = con.createStatement();
        String  str="";
        // String querySql = "select * from userinfo where Account='1'";
        String querySql = "select * from userinfo where Account='"+account+"' ";
        System.out.println("Start looking");
        ResultSet rs = state.executeQuery(querySql);
        if (rs.next()){
            flag=false;
            str = str+rs.getString("Account");
            System.out.println(str);
        }
        return flag;
    }


    public boolean checkvalid(String account,String password) throws ClassNotFoundException, SQLException {
        boolean flag=true;
        Class.forName(driveName);
        //连接
        Connection con = DriverManager.getConnection(url, user, pass);
        Statement state = con.createStatement();
        String  str;
        String  pwd;
        // String querySql = "select * from userinfo where Account='1'";
        String querySql = "select * from userinfo where Account='"+account+"' ";
        System.out.println("start looking");
        ResultSet rs = state.executeQuery(querySql);
        if (rs.next()){
            str = rs.getString("Account");
            pwd=rs.getString("pwd");
            System.out.println("found");
            if (pwd.equals(password)){
                System.out.println("pwd correct");
                return  true;
            }
        }
        System.out.println("not found");
        return false;
    }

    public DatabaseAdapter(){

    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        DatabaseAdapter serverInfoData=new DatabaseAdapter();
        serverInfoData.create();
        String account,pwd;
        //get from server

//        if (serverInfoData.check(account))
//        {
//            serverInfoData.update(account,pwd);
//        }
//        else{
//            //传递valid信息
//        }


    }





}